package hci.gnomex.controller;

import hci.gnomex.model.AppUser;
import hci.gnomex.model.Lab;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.HibernateSession;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;

import java.io.Serializable;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;




public class DeleteLab extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DeleteLab.class);
  
  
  private Integer      idLab = null;
  
 
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
   if (request.getParameter("idLab") != null && !request.getParameter("idLab").equals("")) {
     idLab = new Integer(request.getParameter("idLab"));
   } else {
     this.addInvalidField("idLab", "idLab is required.");
   }

  }

  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      
      // Check permissions
      if (this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_ADMINISTER_USERS)) {
        Lab lab = (Lab)sess.load(Lab.class, idLab);
        
        //
        // Clear out lab member list
        //
        lab.setMembers(new TreeSet());
        sess.flush();
        
        //
        // Delete lab
        //
        sess.delete(lab);
        
        sess.flush();
        
       

        this.xmlResult = "<SUCCESS/>";
      
        setResponsePage(this.SUCCESS_JSP);
   
      } else {
        this.addInvalidField("insufficient permission", "Insufficient permissions to delete lab.");
        setResponsePage(this.ERROR_JSP);
      }
    }catch (Exception e){
      log.error("An exception has occurred in DeleteLab ", e);
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