
package hci.gnomex.controller;

import java.io.*;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.DictionaryEntryUserOwned;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;
import hci.dictionary.utility.DictionaryCommand;
import hci.dictionary.utility.DictionaryManager;
import hci.framework.control.*;

/**
 *
 *@author
 *@created
 *@version    1.0
 * Manage a dictionary by accepting parameters and passing the list along
 * to the appropriate dictionary class. Reloads the updated dictionary in the cache
 * so dictionaries will load with new/updated display values.
 * Returns an XML string with the dictionary items (to display for editing).
 *
 * Parameters:
 * action	  	 String     static variable name of the action to perform - specified in DictionaryActions.java
 */

public class ManageDictionaries extends DictionaryCommand implements Serializable {

  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ManageDictionaries.class);
  
  public static boolean isLoaded = false;

  // put any instance variables here (usually the DetailObjects used by this command)
  private DictionaryManager manager;

  public static final String  DICTIONARY_NAMES_XML = "applications/gnomex/Dictionaries.xml";
  
  public String SUCCESS_JSP = "/getXML.jsp";
  public String ERROR_JSP = "/message.jsp";
  
  private String action = null;

  public ManageDictionaries() {
    ManageDictionaries.initLog4j();
  }

  protected static void initLog4j() {
    String configFile = Constants.WEBCONTEXT_DIR + Constants.LOGGING_PROPERTIES;
    if (configFile == null) {
      System.err.println("No configuration file specified for log4j!");
    }
    org.apache.log4j.PropertyConfigurator.configure(configFile);
  }

  public void validate() {
    log.debug("Executing validate method in " + this.getClass().getName());

  }

  public void loadCommand(HttpServletRequest request, HttpSession session) {
    log.debug("Executing loadCommand method in " + this.getClass().getName());
    
    try {
    	Session sess = HibernateSession.currentSession(this.username);
    
    	//Get the dictionary manager and load it if it isn't already loaded
    	manager = DictionaryManager.getDictionaryManager(DICTIONARY_NAMES_XML, sess, this, true);
      manager.loadCommand(this, request);
      
      // Force personal ownership of dictionary entry if user not admin
      if (this.dictionaryEntry != null &&
          this.dictionaryEntry instanceof DictionaryEntryUserOwned &&
          !this.getSecurityAdvisor().hasPermission(SecurityAdvisor.CAN_WRITE_DICTIONARIES)) {

        SecurityAdvisor secAd = (SecurityAdvisor)this.getSecurityAdvisor();
        ((DictionaryEntryUserOwned)this.dictionaryEntry).setIdAppUser(secAd.getIdAppUser());
      }
      
      if (request.getParameter("action") != null && !request.getParameter("action").equals("")) {
        action = request.getParameter("action");
      }
      
      
  	} catch (Exception e) {  
  		e.printStackTrace();
    } finally {
      try {
        HibernateSession.closeSession();
      }
      catch (Exception ex) {
        log.error("Exception trying to close the Hibernate session: "+ ex);
      }
    }
    


    this.validate();

    // see if we have a valid form
    if (isValid()) {
      setResponsePage(this.SUCCESS_JSP);
      if (request.getParameter("target") != null) {
        this.setRedirect(true);
      }
    } else {
      setResponsePage(this.ERROR_JSP);
      this.setRedirect(false);
    }
  }

  public Command execute() throws RollBackCommandException {
    log.debug("Executing execute method in " + this.getClass().getName());
   
    try {
    	manager.executeCommand(this, HibernateSession.currentSession(this.username), this.getSecurityAdvisor(), true);

    	if (action != null && action.equals(manager.RELOAD_CACHE)) {
        DictionaryHelper.reload(HibernateSession.currentSession(this.username));
    	}
    	isLoaded = true;

		} catch (Exception e) {
			String msg = e.getMessage();
			if (e.getCause() != null && e.getCause() instanceof SQLException) {
				msg = e.getCause().getMessage();
				if (msg != null) {
					if (msg.indexOf("]") > 0) {
						msg = msg.substring(msg.lastIndexOf("]") + 1);
					}
					msg = "Error: " + msg; 
				}
			} else if (e instanceof org.hibernate.id.IdentifierGenerationException) {
			  msg = "Incomplete data.  Please fill in mandatory fields.";
			}
      e.printStackTrace();
			throw new RollBackCommandException(msg);
    } finally {
      try {
        HibernateSession.closeSession();
      }
      catch (Exception ex) {
        log.error("Exception trying to close the Hibernate session: "+ ex);
      }
    }
		return this;
  }

  public HttpServletRequest setRequestState(HttpServletRequest request) {
    request.setAttribute("xmlResult",this.xmlResult);

    return request;
  }

  public HttpServletResponse setResponseState(HttpServletResponse response) {
    log.debug("Executing setResponseState method in " + this.getClass().getName());
    response.setHeader("Cache-Control", "max-age=0, must-revalidate");
    return response;
  }


  public HttpSession setSessionState(HttpSession session) {
    log.debug("Executing setSessionState method in " + this.getClass().getName());

    return session;
  } 
}