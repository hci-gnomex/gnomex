package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.Analysis;
import hci.gnomex.utility.HibernateSession;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.apache.log4j.Logger;

public class SaveAnalysisOwner extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static Logger LOG = Logger.getLogger(SaveLab.class);
  
  private Integer   idAnalysis; 
  private Integer   idOwner; 

  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    if (request.getParameter("idAnalysis") != null && !request.getParameter("idAnalysis").equals("")) {
      idAnalysis = Integer.valueOf(request.getParameter("idAnalysis"));
    } else {
      this.addInvalidField( "idAnalysis", "idAnalysis is required");
    }
    
    if (request.getParameter("idOwner") != null && !request.getParameter("idOwner").equals("")) {
      idOwner = Integer.valueOf(request.getParameter("idOwner"));
    } else {
      this.addInvalidField( "idOwner", "idOwner is required");
    }
  
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      
      Analysis analysis = (Analysis)sess.load(Analysis.class, idAnalysis);
      analysis.setIdAppUser(idOwner);
      sess.save(analysis);
      sess.flush();
      this.xmlResult = "<SUCCESS/>";
      
      setResponsePage(this.SUCCESS_JSP);
      
    }catch (Exception e){
      LOG.error("An exception has occurred in SaveAnalysis ", e);

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