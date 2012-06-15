package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.utility.ChromatogramParser;
import hci.gnomex.utility.HibernateSession;

import java.io.Serializable;
import java.io.StringReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;


public class SaveChromatogramList extends GNomExCommand implements Serializable {
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveChromatogramList.class);
  
  private String                       chromatogramXMLString;
  private Document                     chromatogramDoc;
  private ChromatogramParser           parser;
  
  
  private String                serverName = null;
  private String                launchAppURL;
  private String                appURL;
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    if (request.getParameter("chromatogramXMLString") != null && !request.getParameter("chromatogramXMLString").equals("")) {
      chromatogramXMLString = "<ChromatogramList>" + request.getParameter("chromatogramXMLString") + "</ChromatogramList>";
      
      StringReader reader = new StringReader(chromatogramXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        chromatogramDoc = sax.build(reader);
        parser = new ChromatogramParser(chromatogramDoc);
      } catch (JDOMException je ) {
        log.error( "Cannot parse chromatogramXMLString", je );
        this.addInvalidField( "ChromatogramXMLString", "Invalid xml");
      }
    }

    
    serverName = request.getServerName();
    
    try {
      launchAppURL = this.getLaunchAppURL(request);      
      appURL = this.getAppURL(request);      
    } catch (Exception e) {
      log.warn("Cannot get launch app URL in SaveChromatogram", e);
    }


  }

  public Command execute() throws RollBackCommandException {
    
    if (chromatogramXMLString != null) {
      try {
        Session sess = HibernateSession.currentSession(this.getUsername());


        parser.parse(sess, this.getSecAdvisor(), launchAppURL, appURL, serverName);

        sess.flush();

        this.xmlResult = "<SUCCESS/>";

        setResponsePage(this.SUCCESS_JSP);          


      }catch (Exception e){
        log.error("An exception has occurred in SaveChromatogramList ", e);
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