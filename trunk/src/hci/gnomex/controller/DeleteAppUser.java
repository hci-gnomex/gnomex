package hci.gnomex.controller;

import hci.gnomex.model.AppUser;
import hci.gnomex.model.Lab;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.HibernateSession;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;

import java.io.Serializable;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;




public class DeleteAppUser extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DeleteAppUser.class);
  
  
  private Integer      idAppUser = null;
  
 
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
   if (request.getParameter("idAppUser") != null && !request.getParameter("idAppUser").equals("")) {
     idAppUser = new Integer(request.getParameter("idAppUser"));
   } else {
     this.addInvalidField("idAppUser", "idAppUser is required.");
   }

  }

  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      
      // Check permissions
      if (this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_ADMINISTER_USERS)) {
        AppUser appUser = (AppUser)sess.load(AppUser.class, idAppUser);
        
        //
        // Delete lab members
        //
        for(Iterator i = appUser.getLabs().iterator();i.hasNext();) {
          Lab lab = (Lab)i.next();
          for(Iterator i1 = lab.getMembers().iterator(); i1.hasNext();) {
            AppUser member = (AppUser)i1.next();
            if (member.getIdAppUser().intValue() == appUser.getIdAppUser().intValue()) {
              lab.getMembers().remove(member);
              break;
            }
            
          }
        }
        
        
        //
        // Delete app user
        //
        sess.delete(appUser);
        
        sess.flush();
        
       

        this.xmlResult = "<SUCCESS/>";
      
        setResponsePage(this.SUCCESS_JSP);
        
      } else {
        this.addInvalidField("insufficient permission", "Insufficient permissions to delete member.");
        setResponsePage(this.ERROR_JSP);
      }
      
    }catch (Exception e){
      log.error("An exception has occurred in DeleteAppUser ", e);
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