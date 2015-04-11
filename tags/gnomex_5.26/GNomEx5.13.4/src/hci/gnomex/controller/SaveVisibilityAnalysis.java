package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.AnalysisGroup;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.AnalysisVisibilityParser;
import hci.gnomex.utility.HibernateSession;

import java.io.Serializable;
import java.io.StringReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;




public class SaveVisibilityAnalysis extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveVisibilityAnalysis.class);
  
  private String                       visibilityXMLString;
  private Document                     visibilityDoc;
  private AnalysisVisibilityParser      parser;
  
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    
    if (request.getParameter("visibilityXMLString") != null && !request.getParameter("visibilityXMLString").equals("")) {
      visibilityXMLString = "<AnalysisVisibilityList>" + request.getParameter("visibilityXMLString") + "</AnalysisVisibilityList>";

      StringReader reader = new StringReader(visibilityXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        visibilityDoc = sax.build(reader);
        parser = new AnalysisVisibilityParser(visibilityDoc);
      } catch (JDOMException je ) {
        log.error( "Cannot parse visibilityXMLString", je );
        this.addInvalidField( "visibilityXMLString", "Invalid visibilityXMLString");
      }
    }
    
    
    

  }

  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());

      


      if (visibilityXMLString != null) {
        parser.parse(sess, this.getSecAdvisor(), log);
      }

      sess.flush();

      if (visibilityXMLString != null) {
        parser.resetIsDirty();
        XMLOutputter out = new org.jdom.output.XMLOutputter();
        this.xmlResult = out.outputString(visibilityDoc);
      } else {
        this.xmlResult = "<SUCCESS/>";
        setResponsePage(this.SUCCESS_JSP);
      }

      setResponsePage(this.SUCCESS_JSP);


    } catch (Exception e) {
      log.error("An exception has occurred in SaveVisibilityAnalysis ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());

    } finally {
      try {
        HibernateSession.closeSession();
      } catch (Exception e) {

      }
    }
      
    
    return this;
  }
  

  
  
  

}