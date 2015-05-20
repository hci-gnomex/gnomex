package hci.gnomex.controller;

import hci.gnomex.model.AlignmentProfile;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;

import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;




public class DeleteAlignmentProfile extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DeleteAlignmentProfile.class);
  
  
  private Integer      idAlignmentProfile = null;
    
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
   if (request.getParameter("idAlignmentProfile") != null && !request.getParameter("idAlignmentProfile").equals("")) {
     idAlignmentProfile = new Integer(request.getParameter("idAlignmentProfile"));
   } else {
     this.addInvalidField("idAlignmentProfile", "idAlignmentProfile is required.");
   }

  }

  public Command execute() throws RollBackCommandException {
    Session sess = null;
    AlignmentProfile ap = null;
    
    try {
      sess = HibernateSession.currentSession(this.getUsername());
      ap = (AlignmentProfile)sess.load(AlignmentProfile.class, idAlignmentProfile);
      
      // Check permissions
      if (this.getSecAdvisor().canDelete(ap)) {
        
        //
        // Delete property
        //
        sess.delete(ap);
      
        
        sess.flush();
        
       
        DictionaryHelper.reload(sess);
        
        this.xmlResult = "<SUCCESS/>";
      
        setResponsePage(this.SUCCESS_JSP);
   
      } else {
        this.addInvalidField("insufficient permission", "Insufficient permissions to delete property.");
        setResponsePage(this.ERROR_JSP);
      }
    } catch (ConstraintViolationException ce) {
      this.addInvalidField("constraint", "AlignmentProfile set to inactive.  Unable to delete because of existing records.");
      
      try {
        sess.clear();
        ap = (AlignmentProfile)sess.load(AlignmentProfile.class, idAlignmentProfile);
        ap.setIsActive("N");
        sess.flush();
      } catch(Exception e) {
        log.error("An exception has occurred in DeleteAlignmentProfile when trying to inactivate property ", e);
        e.printStackTrace();
        throw new RollBackCommandException(e.getMessage());
        
      }
      
    } catch (Exception e){
      log.error("An exception has occurred in DeleteAlignmentProfile ", e);
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