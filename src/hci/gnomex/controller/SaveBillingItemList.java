package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.BillingStatus;
import hci.gnomex.model.Lab;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.BillingInvoiceEmailFormatter;
import hci.gnomex.utility.BillingItemParser;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.MailUtil;

import java.io.Serializable;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.mail.MessagingException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;




public class SaveBillingItemList extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveBillingItemList.class);
  
  private String                       billingItemXMLString;
  private Document                     billingItemDoc;
  private BillingItemParser            parser;
  
  private String                       appURL;
  
  private String                       serverName;
  
  private Map                          checkInvoiceMap = new HashMap();
  
  private DictionaryHelper             dictionaryHelper = null;
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    
    if (request.getParameter("billingItemXMLString") != null && !request.getParameter("billingItemXMLString").equals("")) {
      billingItemXMLString = "<BillingItemList>" + request.getParameter("billingItemXMLString") + "</BillingItemList>";
      
      StringReader reader = new StringReader(billingItemXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        billingItemDoc = sax.build(reader);
        parser = new BillingItemParser(billingItemDoc);
      } catch (JDOMException je ) {
        log.error( "Cannot parse billingItemXMLString", je );
        this.addInvalidField( "BillingItemXMLString", "Invalid work item xml");
      }
    }

    
    try {
      appURL = this.getLaunchAppURL(request);      
    } catch (Exception e) {
      log.warn("Cannot get launch app URL in SaveBillingItemList", e);
    }
    
    serverName = request.getServerName();
    
  }

  public Command execute() throws RollBackCommandException {
    
    if (billingItemXMLString != null) {
      try {
        Session sess = HibernateSession.currentSession(this.getUsername());
        
        if (this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_MANAGE_BILLING)) {
          parser.parse(sess);
          
          this.getCheckInvoiceMap(sess, parser);
          
          for(Iterator i = parser.getBillingItems().iterator(); i.hasNext();) {
            BillingItem billingItem = (BillingItem)i.next();
            sess.save(billingItem);
          }
          
          sess.flush();

          for(Iterator i = parser.getBillingItems().iterator(); i.hasNext();) {
            BillingItem bi = (BillingItem)i.next();
            sess.refresh(bi);
          }
          
          for(Iterator i = parser.getBillingItemsToRemove().iterator(); i.hasNext();) {
            BillingItem bi = (BillingItem)i.next();
            sess.delete(bi);
          }
          sess.flush();
          
          
          
          
          // For each unique request, check if all billing items are approved
          // for the billing period.  If so, send billing invoice email
          // to billing contact.
          for(Iterator i = checkInvoiceMap.keySet().iterator(); i.hasNext();) {
            Integer idRequest = (Integer)i.next();
            Integer idBillingPeriod = parser.getIdBillingPeriodForRequest(idRequest);
            
            this.checkToSendInvoiceEmail(sess, idRequest, idBillingPeriod);
            
          }
          
          
          this.xmlResult = "<SUCCESS/>";
          
          setResponsePage(this.SUCCESS_JSP);          
        } else {
          this.addInvalidField("Insufficient permissions", "Insufficient permission to manage workflow");
          setResponsePage(this.ERROR_JSP);
        }


      }catch (Exception e){
        log.error("An exception has occurred in SaveBillingItem ", e);
        e.printStackTrace();
        throw new RollBackCommandException(e.getMessage());
          
      }finally {
        try {
          HibernateSession.closeSession();        
        } catch(Exception e) {
          
        }
      }
      
    } else {
      this.xmlResult = "<SUCCESS/>";
      setResponsePage(this.SUCCESS_JSP);
    }
    
    return this;
  }
  
  private void checkToSendInvoiceEmail(Session sess, Integer idRequest, Integer idBillingPeriod) {
    Object[] row = (Object[])checkInvoiceMap.get(idRequest);
    
    String contactEmail = null;
    Lab lab = null;
    BillingAccount billingAccount = null;
 
    if (row != null) {
      lab              = (Lab)row[1];
      billingAccount   = (BillingAccount)row[2];
      contactEmail     = (String)row[3];      
    }
    
    
    if (contactEmail != null && !contactEmail.equals("")) {
      if (this.readyToInvoice(sess, idRequest, idBillingPeriod, lab.getIdLab(), billingAccount.getIdBillingAccount())) {
        try {
          sendInvoiceEmail(sess, contactEmail, idBillingPeriod, lab, billingAccount);        
        } catch (Exception e) {
          log.error("Unable to send invoice email to billing contact " + contactEmail + " for lab " + lab.getName() + ".", e);
        }
        
      }
    } else {
      log.error("Unable to send invoice email to billing contact for lab " + lab.getName()
          + ".Billing contact email is blank.");      
    }
  }
  
  private void sendInvoiceEmail(Session sess, String contactEmail, Integer idBillingPeriod, Lab lab, BillingAccount billingAccount) throws Exception {
    
    dictionaryHelper = DictionaryHelper.getInstance(sess);
    
    BillingPeriod billingPeriod = dictionaryHelper.getBillingPeriod(idBillingPeriod);

    TreeMap requestMap = new TreeMap();
    TreeMap billingItemMap = new TreeMap();
    ShowBillingInvoiceForm.cacheBillingItemMap(sess, idBillingPeriod, lab.getIdLab(), billingAccount.getIdBillingAccount(), billingItemMap, requestMap);
    
    BillingInvoiceEmailFormatter emailFormatter = new BillingInvoiceEmailFormatter(sess, billingPeriod, lab, billingAccount, billingItemMap, requestMap);
    String subject = emailFormatter.getSubject();
    
    boolean send = false;
    if (serverName.equals(Constants.PRODUCTION_SERVER)) {
      send = true;
    } else {
      if (contactEmail.equals(Constants.DEVELOPER_EMAIL)) {
        send = true;
        subject = "(TEST) " + subject;
      }
    }
    
    if (send) {
      
      MailUtil.send(contactEmail, 
          null,
          Constants.EMAIL_MICROARRAY_CORE_FACILITY, 
          subject, 
          emailFormatter.format(),
          true);      
    }
    
  }  
  
  private void getCheckInvoiceMap(Session sess, BillingItemParser parser) {
    checkInvoiceMap = new HashMap();
   
    if (parser.getIdRequests().size() > 0) {
      StringBuffer buf = new StringBuffer();
      buf.append("SELECT req.idRequest, lab, billingAccount, lab.contactEmail ");
      buf.append("FROM Request as req ");
      buf.append("JOIN req.lab as lab ");
      buf.append("JOIN req.billingAccount as billingAccount ");
      buf.append("WHERE req.idRequest in (" );
      for(Iterator i = parser.getIdRequests().iterator(); i.hasNext();) {
        Integer idRequest = (Integer)i.next();
        buf.append(idRequest.toString());
        if (i.hasNext()) {
          buf.append(", ");
        }
      }
      buf.append(")");
      
      
      List rows = sess.createQuery(buf.toString()).list();
      if (rows.size() > 0) {
        Object[] row = (Object[])rows.iterator().next();
        Integer idRequest = (Integer)row[0];
        
        checkInvoiceMap.put(idRequest, row);
      }    
      
    }
  }
  
  
  private boolean readyToInvoice(Session sess, Integer idRequest, Integer idBillingPeriod, Integer idLab, Integer idBillingAccount) {
    boolean readyToInvoice = false;
    

    Object[] row = (Object[])checkInvoiceMap.get(idRequest);

    Lab lab = null;
    BillingAccount billingAccount = null;
    if (row != null) {
      lab              = (Lab)row[1];
      billingAccount   = (BillingAccount)row[2];
    }
    
    if (lab != null && billingAccount != null) {
      // Find out if this if all billing items for this lab and billing period
      // are approved.  If so, send out a billing invoice to the 
      // lab's billing contact.
      StringBuffer buf = new StringBuffer();
      buf.append("SELECT DISTINCT bi.codeBillingStatus, count(*) ");
      buf.append("FROM   Request req ");
      buf.append("JOIN   req.billingItems bi ");
      buf.append("WHERE  bi.idBillingPeriod = " + idBillingPeriod + " ");
      buf.append("AND    req.idLab = " + idLab);
      buf.append("AND    req.idBillingAccount = " + idBillingAccount);
      buf.append("GROUP BY bi.codeBillingStatus ");
      
      List countList = sess.createQuery(buf.toString()).list();
      int approvedCount = 0;
      int otherCount = 0;
      for(Iterator i = countList.iterator(); i.hasNext();) {
        Object[] countRow = (Object[])i.next();
        String codeBillingStatus = (String)countRow[0];
        Integer count = (Integer)countRow[1];
        
        if (codeBillingStatus.equals(BillingStatus.APPROVED)) {
          approvedCount = count.intValue();
        } else {
          otherCount += count.intValue();
        }
        
      }
      if (approvedCount > 0 && otherCount == 0) {
        readyToInvoice = true;
      }
      
    }
    
    return readyToInvoice;
    
  }
  
  

}