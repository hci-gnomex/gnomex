package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.Analysis;
import hci.gnomex.model.AnalysisFile;
import hci.gnomex.model.Property;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;

import java.io.Serializable;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Hibernate;
import org.hibernate.Session;




public class DeleteAnalysis extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DeleteAnalysis.class);
  
  
  private Integer      idAnalysis = null;
  private String       baseDir;
 
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
   if (request.getParameter("idAnalysis") != null && !request.getParameter("idAnalysis").equals("")) {
     idAnalysis = new Integer(request.getParameter("idAnalysis"));
   } else {
     this.addInvalidField("idAnalysis", "idAnalysis is required.");
   }
   baseDir = request.getServerName();

  }

  public Command execute() throws RollBackCommandException {
    try {

      Session sess = HibernateSession.currentSession(this.getUsername());
      DictionaryHelper dh = DictionaryHelper.getInstance(sess);
      baseDir = dh.getAnalysisDirectory(baseDir);
      Analysis analysis = (Analysis)sess.load(Analysis.class, idAnalysis);
      Hibernate.initialize(analysis.getAnalysisGroups());
      analysis.setAnalysisGroups(null);
    
      if (this.getSecAdvisor().canDelete(analysis)) {
        
        // Remove files from file system
        for(Iterator i = analysis.getFiles().iterator(); i.hasNext();) {
          AnalysisFile af = (AnalysisFile)i.next();
          SaveAnalysis.removeAnalysisFileFromFileSystem(baseDir, analysis, af);
        }
        SaveAnalysis.removeAnalysisDirectoryFromFileSystem(baseDir, analysis);
        
        
        //
        // Delete Analysis
        //
        sess.delete(analysis);
        
        sess.flush();
        
       

        this.xmlResult = "<SUCCESS/>";
      
        setResponsePage(this.SUCCESS_JSP);
      
      
      } else {
        this.addInvalidField("Insufficient permissions", "Insufficient permissions to delete this analysis.");
        setResponsePage(this.ERROR_JSP);
      }
    }catch (Exception e){
      log.error("An exception has occurred in DeleteRequest ", e);
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