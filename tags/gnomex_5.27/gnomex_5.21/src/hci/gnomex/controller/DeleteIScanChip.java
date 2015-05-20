package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.IScanChip;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.HibernateSession;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;




public class DeleteIScanChip extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DeleteIScanChip.class);
  
  
  private Integer      idIScanChip = null; 
  private Integer      idIScanChipOld = null; 
  
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
   if (request.getParameter("idIScanChip") != null && !request.getParameter("idIScanChip").equals("")) {
     idIScanChip = new Integer(request.getParameter("idIScanChip"));
   }

  }

  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      
      if (this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_MANAGE_DNA_SEQ_CORE)) {
        IScanChip chip = (IScanChip)sess.load(IScanChip.class, idIScanChip);
        idIScanChipOld = chip.getIdIScanChip();
        
        
        //
        // Delete chip
        //
        sess.delete(chip);
        
        sess.flush();
        
        

        this.xmlResult = "<SUCCESS/>";
      
        setResponsePage(this.SUCCESS_JSP);
        
      } else {
        this.addInvalidField("Insufficient permissions", "Insufficient permissions to delete iScan chip.");
        setResponsePage(this.ERROR_JSP);
      }
      
    }catch (Exception e){
      log.error("An exception has occurred in DeleteIScanChip ", e);
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