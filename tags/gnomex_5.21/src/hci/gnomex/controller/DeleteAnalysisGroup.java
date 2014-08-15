package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.AnalysisGroup;
import hci.gnomex.utility.HibernateSession;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Hibernate;
import org.hibernate.Session;




public class DeleteAnalysisGroup extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DeleteAnalysisGroup.class);
  
  
  private Integer      idAnalysisGroup = null;
  
 
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
   if (request.getParameter("idAnalysisGroup") != null && !request.getParameter("idAnalysisGroup").equals("")) {
     idAnalysisGroup = new Integer(request.getParameter("idAnalysisGroup"));
   } else {
     this.addInvalidField("idAnalysisGroup", "idAnalysisGroup is required.");
   }

  }

  public Command execute() throws RollBackCommandException {
    try {

      Session sess = HibernateSession.currentSession(this.getUsername());
    
      AnalysisGroup analysisGroup = (AnalysisGroup)sess.load(AnalysisGroup.class, idAnalysisGroup);
    
      if (this.getSecAdvisor().canDelete(analysisGroup)) {
        
        //
        // Initialize the analysis items.  We don't want to orphan them unintentionally.
        //
        Hibernate.initialize(analysisGroup.getAnalysisItems());
        if (analysisGroup.getAnalysisItems().size() > 0) {
          this.addInvalidField("analysisGroup with analysis", 
              "Analysis Group cannot be deleted because it has analysis.  Please reassign analysis items to another analysis group before deleting.");
        }
        
        if (this.isValid()) {
          
          //
          // Delete AnalysisGroup
          //
          sess.delete(analysisGroup);
          
          sess.flush();
          
         

          this.xmlResult = "<SUCCESS/>";
          setResponsePage(this.SUCCESS_JSP);
          
        } else {
          this.setResponsePage(this.ERROR_JSP);
        }
        
      
      
      
      } else {
        this.addInvalidField("Insufficient permissions", "Insufficient permissions to delete analysisGroup.");
        this.setResponsePage(this.ERROR_JSP);
      }
    }catch (Exception e){
      log.error("An exception has occurred in DeleteAnalysisGroup ", e);
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