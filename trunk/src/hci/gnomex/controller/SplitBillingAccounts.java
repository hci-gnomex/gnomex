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
import hci.gnomex.utility.BillingAccountSplitParser;
import hci.gnomex.utility.BillingInvoiceEmailFormatter;
import hci.gnomex.utility.BillingItemParser;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.MailUtil;

import java.io.Serializable;
import java.io.StringReader;
import java.math.BigDecimal;
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




public class SplitBillingAccounts extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SplitBillingAccounts.class);
  
  private String                       accountXMLString;
  private Document                     accountDoc;
  private BillingAccountSplitParser    parser;
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    
    if (request.getParameter("accountXMLString") != null && !request.getParameter("accountXMLString").equals("")) {
      accountXMLString = request.getParameter("accountXMLString");
      
      StringReader reader = new StringReader(accountXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        accountDoc = sax.build(reader);
        parser = new BillingAccountSplitParser(accountDoc);
      } catch (JDOMException je ) {
        log.error( "Cannot parse accountXMLString", je );
        this.addInvalidField( "accountXMLString", "Invalid xml");
      }
    }

  
    
  }

  public Command execute() throws RollBackCommandException {
    
    if (accountXMLString != null) {
      try {
        Session sess = HibernateSession.currentSession(this.getUsername());
        
        if (this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_MANAGE_BILLING)) {
          parser.parse(sess);
          for(Iterator i = parser.getBillingAccounts().iterator(); i.hasNext();) {
            BillingAccount ba = (BillingAccount)i.next();
            BigDecimal percentage = parser.getPercentage(ba.getIdBillingAccount());
            
            boolean found = false;
            // For billing account, find all matching billing items for the request and
            // change the percentage.
            for(Iterator i1 = parser.getRequest().getBillingItems().iterator(); i1.hasNext();) {
              BillingItem bi = (BillingItem)i1.next();
              if (bi.getIdBillingAccount().equals(ba.getIdBillingAccount())) {
                bi.setPercentagePrice(percentage);
                if (bi.getQty().intValue() > 0 && bi.getUnitPrice() != null) {
                  bi.setTotalPrice(bi.getUnitPrice().multiply(bi.getPercentagePrice().multiply(new BigDecimal(bi.getQty().intValue()))));          
                }
                found = true;
              }
              
            }
            // If we didn't find any billing items for this account, clone the billing
            // items and assign to the billing account.
            if (!found) {
              for(Iterator i1 = parser.getRequest().getBillingItems().iterator(); i1.hasNext();) {
                BillingItem bi = (BillingItem)i1.next();
                BillingItem billingItem = new BillingItem();
                billingItem.setIdBillingAccount(ba.getIdBillingAccount());
                billingItem.setIdLab(ba.getIdLab());

                billingItem.setCodeBillingChargeKind(bi.getCodeBillingChargeKind());
                billingItem.setIdBillingPeriod(bi.getIdBillingPeriod());
                billingItem.setDescription(bi.getDescription());
                billingItem.setQty(bi.getQty());
                billingItem.setUnitPrice(bi.getUnitPrice());
                billingItem.setPercentagePrice(percentage);
                if (bi.getQty().intValue() > 0 && bi.getUnitPrice() != null) {
                  billingItem.setTotalPrice(bi.getUnitPrice().multiply(billingItem.getPercentagePrice().multiply(new BigDecimal(billingItem.getQty().intValue()))));          
                }
                billingItem.setCodeBillingStatus(BillingStatus.PENDING);
                billingItem.setIdRequest(parser.getRequest().getIdRequest());
                billingItem.setIdBillingPrice(bi.getIdBillingPrice());
                billingItem.setIdBillingCategory(bi.getIdBillingCategory());
                billingItem.setCategory(bi.getCategory());
                
                sess.save(billingItem);
                
              }
            }
          }
          
          sess.flush();
          
          
          this.xmlResult = "<SUCCESS/>";
          
          setResponsePage(this.SUCCESS_JSP);          
        } else {
          this.addInvalidField("Insufficient permissions", "Insufficient permission to manage billing");
          setResponsePage(this.ERROR_JSP);
        }


      }catch (Exception e){
        log.error("An exception has occurred in SplitBillingAccounts ", e);
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
  
    

}