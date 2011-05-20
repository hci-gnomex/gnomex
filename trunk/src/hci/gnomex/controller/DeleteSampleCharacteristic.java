package hci.gnomex.controller;

import hci.gnomex.model.AppUser;
import hci.gnomex.model.SampleCharacteristic;
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




public class DeleteSampleCharacteristic extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DeleteSampleCharacteristic.class);
  
  
  private Integer      idSampleCharacteristic = null;
  
 
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
   if (request.getParameter("idSampleCharacteristic") != null && !request.getParameter("idSampleCharacteristic").equals("")) {
     idSampleCharacteristic = new Integer(request.getParameter("idSampleCharacteristic"));
   } else {
     this.addInvalidField("idSampleCharacteristic", "idSampleCharacteristic is required.");
   }

  }

  public Command execute() throws RollBackCommandException {
    Session sess = null;
    SampleCharacteristic sampleCharacteristic = null;
    
    try {
      sess = HibernateSession.currentSession(this.getUsername());
      sampleCharacteristic = (SampleCharacteristic)sess.load(SampleCharacteristic.class, idSampleCharacteristic);
      
      // Check permissions
      if (this.getSecAdvisor().canDelete(sampleCharacteristic)) {
        
        //
        // Clear out sampleCharacteristic organism list
        //
        sampleCharacteristic.setOrganisms(new TreeSet());
        sess.flush();
        
        //
        // Clear out sampleCharacteristic platform list
        //
        sampleCharacteristic.setPlatforms(new TreeSet());
        sess.flush();
        
        //
        // Delete sampleCharacteristic
        //
        sess.delete(sampleCharacteristic);
      
        
        sess.flush();
        
       
        DictionaryHelper.reload(sess);
        
        this.xmlResult = "<SUCCESS/>";
      
        setResponsePage(this.SUCCESS_JSP);
   
      } else {
        this.addInvalidField("insufficient permission", "Insufficient permissions to delete sampleCharacteristic.");
        setResponsePage(this.ERROR_JSP);
      }
    } catch (ConstraintViolationException ce) {
      this.addInvalidField("constraint", "Sample annotation set to inactive.  Unable to delete because of sample annotations on existing experiments.");
      
      try {
        sess.clear();
        sampleCharacteristic = (SampleCharacteristic)sess.load(SampleCharacteristic.class, idSampleCharacteristic);
        sampleCharacteristic.setIsActive("N");
        sess.flush();
      } catch(Exception e) {
        log.error("An exception has occurred in DeleteSampleCharacteristic when trying to inactivate sample characteristic ", e);
        e.printStackTrace();
        throw new RollBackCommandException(e.getMessage());
        
      }
      
    } catch (Exception e){
      log.error("An exception has occurred in DeleteSampleCharacteristic ", e);
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