package hci.gnomex.controller;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import hci.gnomex.security.*;
import hci.gnomex.utility.HibernateSession;
import hci.framework.control.*;
import hci.framework.model.DetailObject;

import nl.captcha.Captcha;

import org.hibernate.*;
import org.hibernate.type.*;
import org.jdom.*;
import org.jdom.output.XMLOutputter;

/**
 *
 *@author
 *@created
 *@version    1.0
 * Generated by the CommandBuilder tool - Kirt Henrie
 */

public class CreateSecurityAdvisorForGuest extends GNomExCommand implements Serializable {

  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CreateSecurityAdvisorForGuest.class);

  private SecurityAdvisor     secAdvisor;
  private String              launchAction;
  private String              errorAction;

  
  private static final String ERROR_CAPTCHA_JSP = "/captcha.jsp";
  
  

  /**
   *  The method in which you can do any final validation and add any additional
   *  validation entries into the invalidField hashmap, this should be called in
   *  the loadCommand prior to setting the response jsp
   */
  public void validate() {
  }

  /**
   *  The callback method in which any pre-processing of the command takes place
   *  before the execute method is called. This method is where you would want
   *  to load objects from the HttpServletRequest (passed in), do form
   *  validation, etc. The HttpSession is also available in this method in case
   *  any session data is necessary.
   *
   *@param  request  The HttpServletRequest object
   *@param  session  The HttpSession object
   */
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    this.validate();
    
    Captcha captcha = (Captcha) session.getAttribute(Captcha.NAME);
    String captchaPhrase = (String) request.getParameter("captchafield");
    launchAction  = (String) request.getParameter("launchAction");
    errorAction   = (String) request.getParameter("errorAction");


    /*
    Captcha check eliminated 7/6/12
    if ( captchaPhrase == null || captchaPhrase.equals("")) {
      this.addInvalidField("captcha", "Please enter the text that matches the image");
    } else if ( captcha == null) {
      this.addInvalidField("captcha", "No captcha phrase");
    } else if (!captcha.isCorrect(captchaPhrase)) {
      this.addInvalidField("captch", "Text does not match image.  Please try again.");
    }
    */

    // see if we have a valid form
    if (isValid()) {
      setResponsePage(this.SUCCESS_JSP);
    } else {
      if (errorAction != null && !errorAction.equals("")) {
        setResponsePage(this.errorAction);                
      } else {
        setResponsePage(this.ERROR_CAPTCHA_JSP);        
      }
    }
  }

  /**
   *  The callback method where your business logic should be placed. This
   *  method is either called from the FrontController servlet or from the
   *  RequestProcessor Session Bean (if EJB is used). Any data resulting from
   *  the execution of this method should be put into instance variables in this
   *  class.
   *
   *@return                               Returns this command with the results
   *      of the execute method
   *@exception  RollBackCommandException  Description of the Exception
   */
  public Command execute() throws RollBackCommandException {
    
    try {
      
      secAdvisor = SecurityAdvisor.createGuest();
      
      

      // Output the security advisor information
      Document doc = secAdvisor.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL);
      
      // Set gnomex version
      String filename= this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
      filename = filename.replace("%20", " ");      // convert any blanks
      JarFile jarfile = new JarFile( filename );
      Manifest manifest = jarfile.getManifest();
      Attributes value = (Attributes)manifest.getEntries().get("gnomex");
      secAdvisor.setVersion(value.getValue("Implementation-Version"));

      XMLOutputter out = new org.jdom.output.XMLOutputter();
      this.xmlResult = out.outputString(doc);

    }
    catch (InvalidSecurityAdvisorException e) {
      this.addInvalidField("invalid permission", e.getMessage());
      if (isValid()) {
        setResponsePage(this.SUCCESS_JSP);
      } else {
        setResponsePage(this.ERROR_JSP);
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
      log.fatal(ex.getClass().toString() + " occurred in CreateSecurityAdvisorForGuest " + ex);
      throw new RollBackCommandException();
    }
    
    if (isValid()) {
      if (launchAction != null && !launchAction.equals("")) {
        setResponsePage(launchAction);
      } else {
        setResponsePage(this.SUCCESS_JSP);
      }
    } else {
      if (errorAction != null && !errorAction.equals("")) {
        setResponsePage(this.errorAction);                
      } else {
        setResponsePage(this.ERROR_CAPTCHA_JSP);        
      }
    }
    return this;
  }
  
  /**
   *  The callback method called after the loadCommand and execute methods
   *  allowing you to do any post-execute processing of the HttpSession. Should
   *  be used to add/remove session data resulting from the execution of this
   *  command
   *
   *@param  session  The HttpSession
   *@return          The processed HttpSession
   */
  public HttpSession setSessionState(HttpSession session) {
    session.setAttribute(SecurityAdvisor.SECURITY_ADVISOR_SESSION_KEY, secAdvisor);
    return session;
  }

}

