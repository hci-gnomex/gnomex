package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.BillingPrice;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.HibernateSession;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;




public class SaveBillingPrice extends GNomExCommand implements Serializable {
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveBillingPrice.class);
  

  private Integer       idBillingPrice;
  private BigDecimal    unitPrice;
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    if (request.getParameter("idBillingPrice") != null && !request.getParameter("idBillingPrice").equals("")) {
      idBillingPrice = new Integer(request.getParameter("idBillingPrice"));
    } else {
      this.addInvalidField("idBillingPrice", "idBillingPrice is required");
    }

    if (request.getParameter("unitPrice") != null && !request.getParameter("unitPrice").equals("")) {
      unitPrice = new BigDecimal(request.getParameter("unitPrice"));
    } 
    

   
  }

  public Command execute() throws RollBackCommandException {

    // Find out if use can edit price
    if (!this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_MANAGE_BILLING)) {
      this.addInvalidField("Insufficient permissions", "Insufficient permission to edit billing price.");
      setResponsePage(this.ERROR_JSP);
      return this;
    }

    try {
      Session sess = HibernateSession.currentSession(this.getUsername());

      BillingPrice billingPrice = (BillingPrice)sess.load(BillingPrice.class, idBillingPrice);
      billingPrice.setUnitPrice(unitPrice);
        
      sess.flush();

      this.xmlResult = "<SUCCESS/>";

      if (this.isValid()) {
        setResponsePage(this.SUCCESS_JSP);          
      } else {
        setResponsePage(this.ERROR_JSP);
      }

    }catch (Exception e){
      log.error("An exception has occurred in SaveBillingPrice ", e);
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