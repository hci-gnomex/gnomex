package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.BillingItem;
import hci.gnomex.model.ProductOrder;
import hci.gnomex.model.Request;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.Order;

import java.io.Serializable;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;




public class ReassignBillingAccount extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ReassignBillingAccount.class);
  
  private Integer                      idBillingPeriod; 
  private Integer                      idRequest;
  private Integer                      idProductOrder;
  private Integer                      idLab;
  private Integer                      idBillingAccount;
  private Integer                      idBillingAccountOld;
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    if (request.getParameter("idBillingPeriod") != null && !request.getParameter("idBillingPeriod").equals("")) {
      idBillingPeriod = new Integer(request.getParameter("idBillingPeriod"));
    } else {
      this.addInvalidField("idBillingPeriod", "idBillingPeriod is required");
    }
    
    if (request.getParameter("idRequest") != null && !request.getParameter("idRequest").equals("")) {
      idRequest = new Integer(request.getParameter("idRequest"));
    } /*else {
      this.addInvalidField("idRequest", "idRequest is required");
    }*/
    
    if (request.getParameter("idProductOrder") != null && !request.getParameter("idProductOrder").equals("")) {
      idProductOrder = new Integer(request.getParameter("idProductOrder"));
    } /*else {
      this.addInvalidField("idProductOrder", "idProductOrder is required");
    }*/
    
    if (request.getParameter("idLab") != null && !request.getParameter("idLab").equals("")) {
      idLab = new Integer(request.getParameter("idLab"));
    } else {
      this.addInvalidField("idLab", "idLab is required");
    }
    
    if (request.getParameter("idBillingAccount") != null && !request.getParameter("idBillingAccount").equals("")) {
      idBillingAccount = new Integer(request.getParameter("idBillingAccount"));
    } else {
      this.addInvalidField("idBillingAccount", "idBillingAccount is required");
    }
    
    if (request.getParameter("idBillingAccountOld") != null && !request.getParameter("idBillingAccountOld").equals("")) {
      idBillingAccountOld = new Integer(request.getParameter("idBillingAccountOld"));
    } else {
      this.addInvalidField("idBillingAccountOld", "idBillingAccountOld is required");
    } 
    


  
    
  }

  public Command execute() throws RollBackCommandException {
    
    
      try {
        Session sess = HibernateSession.currentSession(this.getUsername());
        
        if (this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_MANAGE_BILLING)) {
         
            BillingAccount ba = (BillingAccount)sess.load(BillingAccount.class, idBillingAccountOld);
            Order request;
            if ( idProductOrder != null ) {
              ProductOrder po = (ProductOrder)sess.load(ProductOrder.class, idProductOrder);
              request = po;
            } else {
              Request r = (Request)sess.load(Request.class, idRequest);
              request = r;
            }
            
            if (ba.getIdCoreFacility().equals(request.getIdCoreFacility())) {
              boolean found = false;
              for(Iterator i1 = request.getBillingItems().iterator(); i1.hasNext();) {
                BillingItem bi = (BillingItem)i1.next();
                
                // Only reassign billing account for billing items in specified period.
                if (!bi.getIdBillingPeriod().equals(idBillingPeriod)) {
                  continue;
                }
                
                // Reassign the billing account and lab on the billing items matching 
                // the old billing account.
                if (bi.getIdBillingAccount().equals(ba.getIdBillingAccount())) {
                  bi.setIdLab(idLab);
                  bi.setIdBillingAccount(idBillingAccount);
                  bi.resetInvoiceForBillingItem(sess);
                }
                
              }
       
            
            sess.flush();
            
            
            this.xmlResult = "<SUCCESS/>";
            
            setResponsePage(this.SUCCESS_JSP);
          } else {
            this.addInvalidField("Invalid Core", "Core facility for the billing account does not match that of the request");
            setResponsePage(this.ERROR_JSP);
          }
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
      
  
    return this;
  }
  
    

}