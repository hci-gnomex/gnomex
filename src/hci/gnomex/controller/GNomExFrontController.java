package hci.gnomex.controller;

/**
 *  The front controller for the test application
 *
 *@author     Tonya Di Sera
 *@created    August 17, 2002
 */

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;


import hci.gnomex.constants.Constants;
import hci.gnomex.security.SecurityAdvisor;
import hci.framework.control.*;
import javax.ejb.*;

import java.net.InetAddress;
import java.rmi.*;
import javax.naming.*;

import org.jdom.output.XMLOutputter;

import hci.utility.server.JNDILocator;

public class GNomExFrontController extends HttpServlet {
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GNomExFrontController.class);

  /**
   *  Initialize global variables
   *
   *@exception  ServletException  Description of the Exception
   */
  public void init() throws ServletException {
    initLog4j();
  }


  /**
   */
  protected static void initLog4j() {
    String configFile = Constants.WEBCONTEXT_DIR + Constants.LOGGING_PROPERTIES;
    if (configFile == null) {
      System.err.println("No configuration file specified for log4j!");
    }
    org.apache.log4j.PropertyConfigurator.configure(configFile);
  }

  /**
   *  Process the HTTP Get request
   *
   *@param  request               Description of the Parameter
   *@param  response              Description of the Parameter
   *@exception  ServletException  Description of the Exception
   *@exception  IOException       Description of the Exception
   */
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doPost(request, response);
  }


  /**
   *  Process the HTTP Post request
   *
   *@param  request               Description of the Parameter
   *@param  response              Description of the Parameter
   *@exception  ServletException  Description of the Exception
   *@exception  IOException       Description of the Exception
   */
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // get the users session
    HttpSession session = request.getSession(true);
    
    session.setAttribute("lastGNomExAccessTime", new Long(new Date().getTime()));

    // get our request from the url (prefixing .test)
    String fullURI = request.getRequestURI();
    String requestName = fullURI.substring((fullURI.lastIndexOf('/') + 1), fullURI.lastIndexOf('.'));

    // restrict commands to local host if request is not secure
    if (Constants.REQUIRE_SECURE_REMOTE && !request.isSecure()) {
      if (request.getRemoteAddr().equals(InetAddress.getLocalHost().getHostAddress())
          || request.getRemoteAddr().equals("127.0.0.1") 
          || InetAddress.getByName(request.getRemoteAddr()).isLoopbackAddress()) {
        log.debug("Requested from local host");
      }
      else {
        System.out.println(request.getRemoteAddr());
        System.out.println(InetAddress.getLocalHost().getHostAddress());
        log.error("Accessing secure command over non-secure line from remote host is not allowed");
        this.forwardWithError(request, response,"Secure connection is required. Prefix your request with 'https:'");
        return;
      }
    }
    

    // now get our command class and instantiate
    Class commandClass = null;
    Command commandInstance = null;
    try {
    	commandClass = Class.forName("hci.gnomex.controller" + "." + requestName);
      commandInstance = (Command) commandClass.newInstance();

      if (request.getUserPrincipal() != null) {
        commandInstance.setUsername(request.getUserPrincipal().getName());
      }

    } catch (ClassNotFoundException cnfe) {
      log.error("Command " + requestName + ".class not found");
      this.forwardWithError(request, response);
      return;
    } catch (IllegalAccessException ias) {
      log.error("IllegalAccessException while getting command " + requestName);
      this.forwardWithError(request, response);
      return;
    } catch (InstantiationException ie) {
      log.error("Unable to instantiate command " + requestName);
      this.forwardWithError(request, response);
      return;
    }

    // we should have a valid command.

    // If we do not have a security advisor for the session, add error to command
    // But do not require for initial data services and CMD to set the sec advisor
    // If we have a security advisor, add it to the command instance
    if (session.getAttribute(SecurityAdvisor.SECURITY_ADVISOR_SESSION_KEY) != null) {
      commandInstance.setSecurityAdvisor((SecurityAdvisor) session.getAttribute(SecurityAdvisor.SECURITY_ADVISOR_SESSION_KEY));
    }
  
    
    if (commandInstance.getSecurityAdvisor() == null
    	  && (requestName.compareTo("ManageDictionaries") != 0 //You can reload dictionary cache without security
    			|| request.getParameter("action") == null || ! request.getParameter("action").equals("reload"))
        && (!requestName.startsWith("CreateSecurityAdvisor"))
        && (!requestName.equals("ChangePasswordExternalUser"))
        && (!requestName.equals("ShowAnalysisDownloadForm"))
        && (!requestName.equals("ShowAnalysisDownloadFormForGuest"))
        && (!requestName.equals("ShowRequestDownloadForm"))
        && (!requestName.equals("ChangePassword"))
        && (!requestName.equals("GetLaunchProperties"))
        && (!requestName.equals("ShowRequestDownloadFormForGuest"))) {
    		
       commandInstance.addInvalidField("SecurityAdvisor", "You must create a SecurityAdvisor in order to run this command.");
    }

    // if command still valid, call the loadCommand method
    if (commandInstance.isValid()) {
    log.debug("Calling loadCommand on " + commandClass);
    commandInstance.loadCommand(request, session);
    }

    // see if it is valid, if so call execute (if using SBRequestProcessor, forward to it)
    if (commandInstance.isValid()) {
      log.debug("Forwarding " + commandClass + " to the request processor for execution");
      try {
        commandInstance = getRequestProcessor(request, response).processCommand(commandInstance);
      } catch (Exception e) {
        String msg = null;
        if (e.getCause() != null && e.getCause() instanceof EJBException) {
        	EJBException ejbe = (EJBException) e.getCause();
        	if (ejbe.getCausedByException() != null && ejbe.getCausedByException() instanceof RollBackCommandException) {
        		RollBackCommandException rbce = (RollBackCommandException) ejbe.getCausedByException();
        		msg = rbce.getMessage();
        	}
        }
        
        if (msg != null) {
          response.setHeader("altio.status", "0");
          response.setHeader("altio.message", "Success");        	
        	this.forwardWithError(request, response, msg);
        }
        else {
          log.error(e.getClass().getName() + " while executing command " + commandClass);
          log.error("The stacktrace for the error:");
          log.error(e.getMessage());
          
        	this.forwardWithError(request, response);
        }
        return;
      }
    }

    // now set the request state, response state, and session state
    log.debug("Calling setRequestState on " + commandClass);
    commandInstance.setRequestState(request);

    log.debug("Calling setResponseState on " + commandClass);
    commandInstance.setResponseState(response);

    log.debug("Calling setSessionState on " + commandClass);
    commandInstance.setSessionState(session);

    // get our response page
    String forwardJSP = commandInstance.getResponsePage();
    // if command didn't provide one, default to message.jsp (for error)
    if (forwardJSP == null || forwardJSP.equals("")) {
      forwardJSP = "/message.jsp";
    }

    // set our success headers for altio
    response.setHeader("altio.status", "0");
    response.setHeader("altio.message", "Success");
    if (!commandInstance.isValid()) {
      String tmpMessage = commandInstance.getInvalidFieldsMessage();
      request.setAttribute("message", tmpMessage);
    }

    // if command is redirect
    if (commandInstance.isRedirect()) {
      this.sendRedirect(response, forwardJSP);
    } else {
      // forward to our response page
      forwardPage(request, response, forwardJSP);
    }

  }

  private void forwardWithError(HttpServletRequest request, HttpServletResponse response) {

    String errMsg = "There has been a system error, please try the request again";
    this.forwardWithError(request, response, errMsg);
  }

  private void forwardWithError(HttpServletRequest request, HttpServletResponse response, String message) {
    String errMsg = message;

    request.setAttribute("message", errMsg);
    this.forwardPage(request, response, "/message.jsp");
  }


  private void forwardPage(HttpServletRequest request, HttpServletResponse response, String url) {
    log.debug("Forwarding response to " + url);
    try {
      getServletContext().getRequestDispatcher(url).forward(request, response);
    } catch (Exception e) {
      log.error(e.getClass().getName() + " while attempting to forward to " + url);
      log.error("The stacktrace for the error:");
      log.error(e.getMessage());
    }
  }

  private void sendRedirect(HttpServletResponse response, String url) {
    log.debug("Redirecting response to " + url);
    try {
      response.sendRedirect(url);
    } catch (Exception e) {
      log.error(e.getClass().getName() + " while attempting to forward to " + url);
      log.error("The stacktrace for the error:");
      log.error(e.getMessage());
    }
  }

  private SBGNomExRequestProcessor getRequestProcessor(HttpServletRequest request, HttpServletResponse response) {
    SBGNomExRequestProcessor remote = null;
    SBGNomExRequestProcessorHome home = null;

    try {
      // look up home interface
      home = (SBGNomExRequestProcessorHome) JNDILocator.getJNDILocator().getEJBHome("SBGNomExRequestProcessor", SBGNomExRequestProcessorHome.class, true);

      // create a remote reference
      remote = home.create();

    } catch (NamingException ne) {
      log.error("Unable to find " + home.getClass().getName());
      this.forwardWithError(request, response);
    } catch (CreateException ce) {
      log.error("Unable to create " + remote.getClass().getName());
      this.forwardWithError(request, response);
    } catch (RemoteException re) {
      log.error(re.getClass().getName() + " while looking up and creating " + remote.getClass().getName());
      log.error("The stacktrace for the error:");
      log.error(re.getMessage());
      this.forwardWithError(request, response);
    }

    return remote;
  }


  /**
   *  Clean up resources
   */
  public void destroy() {
  }
}
