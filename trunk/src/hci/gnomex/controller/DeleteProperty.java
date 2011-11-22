package hci.gnomex.controller;

import hci.gnomex.model.AppUser;
import hci.gnomex.model.Property;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;

import java.io.Serializable;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;




public class DeleteProperty extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DeleteProperty.class);
  
  
  private Integer      idProperty = null;
  
 
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
   if (request.getParameter("idProperty") != null && !request.getParameter("idProperty").equals("")) {
     idProperty = new Integer(request.getParameter("idProperty"));
   } else {
     this.addInvalidField("idProperty", "idProperty is required.");
   }

  }

  public Command execute() throws RollBackCommandException {
    Session sess = null;
    Property property = null;
    
    try {
      sess = HibernateSession.currentSession(this.getUsername());
      property = (Property)sess.load(Property.class, idProperty);
      
      // Check permissions
      if (this.getSecAdvisor().canDelete(property)) {
        
        //
        // Clear out property organism list
        //
        property.setOrganisms(new TreeSet());
        sess.flush();
        
        //
        // Clear out property platform list
        //
        property.setPlatforms(new TreeSet());
        sess.flush();
        
        //
        // Delete property
        //
        sess.delete(property);
      
        
        sess.flush();
        
       
        DictionaryHelper.reload(sess);
        
        this.xmlResult = "<SUCCESS/>";
      
        setResponsePage(this.SUCCESS_JSP);
   
      } else {
        this.addInvalidField("insufficient permission", "Insufficient permissions to delete property.");
        setResponsePage(this.ERROR_JSP);
      }
    } catch (ConstraintViolationException ce) {
      this.addInvalidField("constraint", "Property set to inactive.  Unable to delete because of sample annotations on existing experiments.");
      
      try {
        sess.clear();
        property = (Property)sess.load(Property.class, idProperty);
        property.setIsActive("N");
        sess.flush();
      } catch(Exception e) {
        log.error("An exception has occurred in DeleteProperty when trying to inactivate property ", e);
        e.printStackTrace();
        throw new RollBackCommandException(e.getMessage());
        
      }
      
    } catch (Exception e){
      log.error("An exception has occurred in DeleteProperty ", e);
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