package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.InternalAccountFieldsConfiguration;
import hci.gnomex.model.OtherAccountFieldsConfiguration;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.parsers.InternalBillingAccountFieldsConfigurationParser;
import hci.gnomex.utility.parsers.OtherBillingAccountFieldsConfigurationParser;

import java.io.Serializable;
import java.io.StringReader;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;




public class SaveBillingAccountFieldsConfiguration extends GNomExCommand implements Serializable {
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveBillingAccountFieldsConfiguration.class);
  
  private InternalBillingAccountFieldsConfigurationParser internalFieldsParser;
  private OtherBillingAccountFieldsConfigurationParser    otherFieldsParser;
  
  private String                                          appURL;
  
  private String                                          serverName;
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    if (request.getParameter("internalFields") != null && !request.getParameter("internalFields").equals("")) {
      StringReader internalReader = new StringReader(request.getParameter("internalFields"));
      try {
        SAXBuilder sax = new SAXBuilder();
        Document doc = sax.build(internalReader);
        internalFieldsParser = new InternalBillingAccountFieldsConfigurationParser(doc);
      } catch (JDOMException je ) {
        log.error( "Cannot parse internalFields", je );
        this.addInvalidField( "internalFields", "Invalid configuration field xml");
      }
    } else {
      log.error("internal fields xml not specified");
      this.addInvalidField("internalFieldsXMLString", "Internal fields xml string not specified");
    }
    
    if (request.getParameter("otherFields") != null && !request.getParameter("otherFields").equals("")) {
      StringReader otherReader = new StringReader(request.getParameter("otherFields"));
      try {
        SAXBuilder sax = new SAXBuilder();
        Document doc = sax.build(otherReader);
        otherFieldsParser = new OtherBillingAccountFieldsConfigurationParser(doc);
      } catch (JDOMException je ) {
        log.error( "Cannot parse otherFields", je );
        this.addInvalidField( "otherFields", "Invalid configuration field xml");
      }
    } else {
      log.error("other fields xml not specified");
      this.addInvalidField("otherFieldsXMLString", "Other fields xml string not specified");
    }
    
    try {
      appURL = this.getLaunchAppURL(request);      
    } catch (Exception e) {
      log.warn("Cannot get launch app URL in SaveBillingItemList", e);
    }
    
    serverName = request.getServerName();
    
  }

  public Command execute() throws RollBackCommandException {
    
    if (internalFieldsParser != null && otherFieldsParser != null) {
      try {
        Session sess = HibernateSession.currentSession(this.getUsername());
        internalFieldsParser.parse(sess);
        for(Iterator i = internalFieldsParser.getConfigurations().iterator();i.hasNext();) {
          sess.save(i.next());
        }
        otherFieldsParser.parse(sess);
        for(Iterator i = otherFieldsParser.getConfigurations().iterator();i.hasNext();) {
          sess.save(i.next());
        }
        sess.flush();
        
        InternalAccountFieldsConfiguration.reloadConfigurations(sess);
        OtherAccountFieldsConfiguration.reloadConfigurations(sess);
      }catch (Exception e){
        log.error("An exception has occurred in SaveBillingItem ", e);
        e.printStackTrace();
        throw new RollBackCommandException(e.getMessage());
          
      }finally {
        try {
          HibernateSession.closeSession();        
        } catch(Exception e) {
          
        }
      }
      this.xmlResult = "<SUCCESS/>";
      setResponsePage(this.SUCCESS_JSP);
      
    }
    
    return this;
  }
}