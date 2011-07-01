package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.NumberSequencingCyclesAllowed;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.model.RequestCategoryApplication;
import hci.gnomex.model.SamplePrepMethodRequestCategory;
import hci.gnomex.model.SampleTypeRequestCategory;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;

import java.io.Serializable;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;




public class DeleteExperimentPlatform extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DeleteExperimentPlatform.class);
  
  
  private String      codeRequestCategory = null;
  
 
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
   if (request.getParameter("codeRequestCategory") != null && !request.getParameter("codeRequestCategory").equals("")) {
     codeRequestCategory = request.getParameter("codeRequestCategory");
   } else {
     this.addInvalidField("codeRequestCategory", "codeRequestCategory is required.");
   }

  }

  public Command execute() throws RollBackCommandException {
    Session sess = null;
    RequestCategory requestCategory = null;
    
    try {
      sess = HibernateSession.currentSession(this.getUsername());
      requestCategory = (RequestCategory)sess.load(RequestCategory.class, codeRequestCategory);
      
      // Check permissions
      if (this.getSecAdvisor().canDelete(requestCategory)) {
        
        // Delete associations
        for (Iterator i = sess.createQuery("SELECT x from SampleTypeRequestCategory x where x.codeRequestCategory = '" + codeRequestCategory + "'").list().iterator(); i.hasNext();) {
          SampleTypeRequestCategory x = (SampleTypeRequestCategory)i.next();
          sess.delete(x);
        }
        for (Iterator i = sess.createQuery("SELECT x from NumberSequencingCyclesAllowed x where x.codeRequestCategory = '" + codeRequestCategory + "'").list().iterator(); i.hasNext();) {
          NumberSequencingCyclesAllowed x = (NumberSequencingCyclesAllowed)i.next();
          sess.delete(x);
        }
        for (Iterator i = sess.createQuery("SELECT x from RequestCategoryApplication x where x.codeRequestCategory = '" + codeRequestCategory + "'").list().iterator(); i.hasNext();) {
          RequestCategoryApplication x = (RequestCategoryApplication)i.next();
          sess.delete(x);
        }
        for (Iterator i = sess.createQuery("SELECT x from SamplePrepMethodRequestCategory x where x.codeRequestCategory = '" + codeRequestCategory + "'").list().iterator(); i.hasNext();) {
          SamplePrepMethodRequestCategory x = (SamplePrepMethodRequestCategory)i.next();
          sess.delete(x);
        }
        
        //
        // Delete sampleCharacteristic
        //
        sess.delete(requestCategory);
      
        
        sess.flush();
        
       
        DictionaryHelper.reload(sess);
        
        this.xmlResult = "<SUCCESS/>";
      
        setResponsePage(this.SUCCESS_JSP);
   
      } else {
        this.addInvalidField("insufficient permission", "Insufficient permissions to delete experiment platform.");
        setResponsePage(this.ERROR_JSP);
      }
    } catch (ConstraintViolationException ce) {
      this.addInvalidField("constraint", "Experiment platform set to inactive.  Unable to delete because of existing experiments.");
      
      try {
        sess.clear();
        requestCategory = (RequestCategory)sess.load(RequestCategory.class, codeRequestCategory);
        requestCategory.setIsActive("N");
        sess.flush();
      } catch(Exception e) {
        log.error("An exception has occurred in DeleteExperimentPlatform when trying to inactivate it ", e);
        e.printStackTrace();
        throw new RollBackCommandException(e.getMessage());
        
      }
      
    } catch (Exception e){
      log.error("An exception has occurred in DeleteExperimentPlatform ", e);
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