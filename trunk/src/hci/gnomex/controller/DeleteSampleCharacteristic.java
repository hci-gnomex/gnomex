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
    
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      SampleCharacteristic sampleCharacteristic = (SampleCharacteristic)sess.load(SampleCharacteristic.class, idSampleCharacteristic);
      
      // Check permissions
      if (this.getSecAdvisor().canDelete(sampleCharacteristic)) {
        
        //
        // Clear out sampleCharacteristic organism list
        //
        sampleCharacteristic.setOrganisms(new TreeSet());
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
    }catch (Exception e){
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