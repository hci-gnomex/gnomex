package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.AnalysisGroup;
import hci.gnomex.model.AnalysisGroup;
import hci.gnomex.model.Visibility;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.HibernateSession;

import java.io.Serializable;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Element;




public class SaveAnalysisGroup extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveAnalysisGroup.class);
  
  private AnalysisGroup    load;
  private AnalysisGroup    analysisGroup;
  private boolean         isNewAnalysisGroup = false;
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    load = new AnalysisGroup();
    HashMap errors = this.loadDetailObject(request, load);
    this.addInvalidFields(errors);

  }

  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      
      this.initializeAnalysisGroup(load, sess);
      
      if (this.getSecAdvisor().canUpdate(analysisGroup)) {

        this.analysisGroup.setName(this.unEscape(analysisGroup.getName()));
        this.analysisGroup.setDescription(this.unEscape(analysisGroup.getDescription()));
        
        sess.save(analysisGroup);
        sess.flush();

        this.xmlResult = "<SUCCESS idAnalysisGroup=\"" + analysisGroup.getIdAnalysisGroup() + "\"/>";
      
        setResponsePage(this.SUCCESS_JSP);
      
      } else {
        this.addInvalidField("Insufficient permissions", "Insufficient permission to save analysis group.");
        setResponsePage(this.ERROR_JSP);
      }
      
    }catch (Exception e){
      log.error("An exception has occurred in SaveAnalysisGroup ", e);
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
  
  private void initializeAnalysisGroup(AnalysisGroup load, Session sess) throws Exception {
    
    if (load.getIdAnalysisGroup().intValue() == 0) {
      analysisGroup = load;
      analysisGroup.setIdAppUser(this.getSecAdvisor().getIdAppUser());
      isNewAnalysisGroup = true;
      
    } else {
      analysisGroup = (AnalysisGroup)sess.load(AnalysisGroup.class, load.getIdAnalysisGroup());
      
    }
    
    analysisGroup.setName(this.unEscape(load.getName()));
    analysisGroup.setDescription(this.unEscape(load.getDescription()));
  }  
  
   public static String unEscape(String text) {
     if (text == null) {
       return text;
     }
     text = text.replaceAll("&amp;",    "&");
     text = text.replaceAll("&quot;",   "\"");
     text = text.replaceAll("&apos;",   "'");
     text = text.replaceAll("&gt;",     ">");
     text = text.replaceAll("&lt;",     "<");
     text = text.replaceAll("&#181;",   "�");
     return text;
   }

  
  

}