package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.BillingStatus;
import hci.gnomex.model.Lab;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.BillingInvoiceEmailFormatter;
import hci.gnomex.utility.BillingItemParser;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.MailUtil;

import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;




public class SaveBillingItemList extends GNomExCommand implements Serializable {
  
  private class LabAccountBillingPeriod implements Serializable {
    public Lab getLab() {
      return lab;
    }
    public Integer getIdBillingPeriod() {
      return idBillingPeriod;
    }
    public BillingAccount getBillingAccount() {
      return billingAccount;
    }
    public LabAccountBillingPeriod(Lab lab, Integer idBillingPeriod,
        BillingAccount billingAccount) {
      super();
      this.lab = lab;
      this.idBillingPeriod = idBillingPeriod;
      this.billingAccount = billingAccount;
    }
    
    private Lab lab;
    private Integer idBillingPeriod;
    private BillingAccount billingAccount;
    
    public int hashCode() {
      return new HashCodeBuilder()
      .append(getLab().getIdLab())
      .append(getIdBillingPeriod())
      .append(getBillingAccount().getIdBillingAccount())
      .toHashCode();
    }
    
    public boolean equals(Object other) {
      if ( !(other instanceof LabAccountBillingPeriod) ) return false;
      LabAccountBillingPeriod castOther = (LabAccountBillingPeriod) other;
      return new EqualsBuilder()
          .append(this.getLab().getIdLab(), castOther.getLab().getIdLab())
          .append(this.getIdBillingPeriod(), castOther.getIdBillingPeriod())
          .append(this.getBillingAccount().getIdBillingAccount(), castOther.getBillingAccount().getIdBillingAccount())
          .isEquals();
  }    

} 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveBillingItemList.class);
  
  private String                       billingItemXMLString;
  private Document                     billingItemDoc;
  private BillingItemParser            parser;
  
  private String                       appURL;
  
  private String                       serverName;
  
  private Map<LabAccountBillingPeriod, Object[]>  labAccountBillingPeriodMap = new HashMap<LabAccountBillingPeriod, Object[]>();
  
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
          
          //this.getCheckInvoiceMap(sess, parser);
          
          ArrayList billingItems = new ArrayList();
          for(Iterator i = parser.getBillingItems().iterator(); i.hasNext();) {
            BillingItem billingItem = (BillingItem)i.next();
            sess.save(billingItem);
            
            billingItems.add(billingItem);

          }
          
          sess.flush();

          for(Iterator i = billingItems.iterator(); i.hasNext();) {
            BillingItem billingItem = (BillingItem)i.next();
            
            sess.refresh(billingItem);
            
            // For PO Billing account, approved status is changed to 
            // 'Approved External' so that it shows under a different
            // folder in the billing tree.
            if (billingItem.getCodeBillingStatus().equals(BillingStatus.APPROVED)) {
              if (billingItem.getBillingAccount().getIsPO() != null && billingItem.getBillingAccount().getIsPO().equals("Y")) {
                billingItem.setCodeBillingStatus(BillingStatus.APPROVED_PO);
              }
            }
            

          }
          
          sess.flush();

          for(Iterator i = parser.getBillingItems().iterator(); i.hasNext();) {
            BillingItem bi = (BillingItem)i.next();
            sess.refresh(bi);
            // This item should not contribute to decision to send billing statement if previously approved
            if (!(bi.getCodeBillingStatus().equals(BillingStatus.APPROVED) && (bi.getCurrentCodeBillingStatus().equals(BillingStatus.APPROVED)))) {
              LabAccountBillingPeriod labp = new LabAccountBillingPeriod(bi.getLab(), bi.getBillingPeriod().getIdBillingPeriod(), bi.getBillingAccount());
              labAccountBillingPeriodMap.put(labp, null);              
            }

          }
          
          for(Iterator i = parser.getBillingItemsToRemove().iterator(); i.hasNext();) {
            BillingItem bi = (BillingItem)i.next();
            sess.delete(bi);
          }
          sess.flush();

          
          for(Iterator<LabAccountBillingPeriod> i = labAccountBillingPeriodMap.keySet().iterator(); i.hasNext();) {
            LabAccountBillingPeriod labp = (LabAccountBillingPeriod) i.next();
            this.checkToSendInvoiceEmail(sess, labp.getLab(), labp.getIdBillingPeriod(), labp.getBillingAccount());
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
  
  private void checkToSendInvoiceEmail(Session sess, Lab lab, Integer idBillingPeriod, BillingAccount billingAccount) {
    if (lab != null && lab.getContactEmail() != null && !lab.getContactEmail().equals("")) {
      if (this.readyToInvoice(sess, idBillingPeriod, lab, billingAccount.getIdBillingAccount())) {
        try {
          sendInvoiceEmail(sess, idBillingPeriod, lab, billingAccount);        
        } catch (Exception e) {
          log.error("Unable to send invoice email to billing contact " + lab.getContactEmail() + " for lab " + lab.getName() + ".", e);
        }
        
      }
    } else {
      log.error("Unable to send invoice email to billing contact for lab " + lab.getName()
          + ".Billing contact email is blank.");      
    }
  }
  
  private void sendInvoiceEmail(Session sess, Integer idBillingPeriod, Lab lab, BillingAccount billingAccount) throws Exception {
    
    dictionaryHelper = DictionaryHelper.getInstance(sess);
    
    BillingPeriod billingPeriod = dictionaryHelper.getBillingPeriod(idBillingPeriod);

    TreeMap requestMap = new TreeMap();
    TreeMap billingItemMap = new TreeMap();
    TreeMap relatedBillingItemMap = new TreeMap();
    ShowBillingInvoiceForm.cacheBillingItemMap(sess, idBillingPeriod, lab.getIdLab(), billingAccount.getIdBillingAccount(), billingItemMap, relatedBillingItemMap, requestMap);
    
    BillingInvoiceEmailFormatter emailFormatter = new BillingInvoiceEmailFormatter(sess, billingPeriod, lab, billingAccount, billingItemMap, relatedBillingItemMap, requestMap);
    String subject = emailFormatter.getSubject();
    
    boolean send = false;
    if (dictionaryHelper.isProductionServer(serverName)) {
      send = true;
    } else {
      if (lab.getContactEmail().equals(dictionaryHelper.getPropertyDictionary(PropertyDictionary.CONTACT_EMAIL_SOFTWARE_TESTER))) {
        send = true;
        subject = "(TEST) " + subject;
      }
    }
    
    if (send) {
      MailUtil.send(lab.getContactEmail(), 
          null,
          dictionaryHelper.getPropertyDictionary(PropertyDictionary.CONTACT_EMAIL_CORE_FACILITY), 
          subject, 
          emailFormatter.format(),
          true); 
     
    }
  }  

  
  private boolean readyToInvoice(Session sess, Integer idBillingPeriod, Lab lab, Integer idBillingAccount) {
    boolean readyToInvoice = false;
    
    if (lab != null && idBillingAccount != null) {
      // Find out if this if all billing items for this lab and billing period
      // are approved.  If so, send out a billing invoice to the 
      // lab's billing contact.
      StringBuffer buf = new StringBuffer();
      buf.append("SELECT DISTINCT bi.codeBillingStatus, count(*) ");
      buf.append("FROM   Request req ");
      buf.append("JOIN   req.billingItems bi ");
      buf.append("WHERE  bi.idBillingPeriod = " + idBillingPeriod + " ");
      buf.append("AND    bi.idLab = " + lab.getIdLab());
      buf.append("AND    bi.idBillingAccount = " + idBillingAccount);
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