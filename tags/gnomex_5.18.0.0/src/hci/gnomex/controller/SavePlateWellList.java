package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.PlateWellParser;
import hci.gnomex.utility.HibernateSession;

import java.io.Serializable;
import java.io.StringReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;


public class SavePlateWellList extends GNomExCommand implements Serializable {
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SavePlateWellList.class);
  
  private String                       plateWellXMLString;
  private Document                     plateWellDoc;
  private PlateWellParser           parser;
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    if (request.getParameter("plateWellXMLString") != null && !request.getParameter("plateWellXMLString").equals("")) {
      plateWellXMLString = request.getParameter("plateWellXMLString");
      
      StringReader reader = new StringReader(plateWellXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        plateWellDoc = sax.build(reader);
        parser = new PlateWellParser(plateWellDoc);
      } catch (JDOMException je ) {
        log.error( "Cannot parse plateWellXMLString", je );
        this.addInvalidField( "PlateWellXMLString", "Invalid xml");
      }
    }

  }

  public Command execute() throws RollBackCommandException {
    
    if (plateWellXMLString != null) {
      try {
        Session sess = HibernateSession.currentSession(this.getUsername());

        if (this.getSecurityAdvisor().hasPermission( SecurityAdvisor.CAN_MANAGE_DNA_SEQ_CORE )) {

          parser.parse(sess);

          sess.flush();

          this.xmlResult = "<SUCCESS/>";

          setResponsePage(this.SUCCESS_JSP);          

        } else {
          this.addInvalidField("Insufficient permissions", "Insufficient permission to save plateWell list.");
          setResponsePage(this.ERROR_JSP);
        }
        
      }catch (Exception e){
        log.error("An exception has occurred in SavePlateWellList ", e);
        e.printStackTrace();
        throw new RollBackCommandException(e.getMessage());

      }finally {
        try {
          HibernateSession.closeSession();        
        } catch(Exception e) {

        }
      }
      
    } else {
      this.xmlResult = "<SUCCESS/>";
      setResponsePage(this.SUCCESS_JSP);
    }
    
    return this;
  }
  
  

}