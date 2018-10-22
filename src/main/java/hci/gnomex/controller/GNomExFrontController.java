package hci.gnomex.controller;

/**
 * The front controller for the test application
 *
 * @author Tonya Di Sera
 * @created August 17, 2002
 */

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.constants.Constants;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.*;

import java.io.*;
import java.net.InetAddress;
import java.sql.SQLException;
import java.util.Date;
import java.util.*;

import javax.ejb.EJBException;
import javax.mail.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

import org.apache.log4j.Logger;

public class GNomExFrontController extends HttpServlet {
    private static Logger LOG = Logger.getLogger(GNomExFrontController.class);

    private static String webContextPath;
    private static Session mailSession;
    private static boolean GNomExLite;

    /**
     * Initialize global variables
     *
     * @exception ServletException
     *                Description of the Exception
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        webContextPath = config.getServletContext().getRealPath(Constants.FILE_SEPARATOR);

        // are we really GNomExLite?
        GNomExLite = areWeLite();

        // Get the mail session
        try {
            Context ec = (Context) new InitialContext().lookup("java:comp/env");
            mailSession = (Session) ec.lookup(Constants.MAIL_SESSION);
        } catch (Exception me) {
            LOG.error("Error in gnomexFrontController cannot get mail session: ", me);
        }

        initLog4j();
    }

    public static void setWebContextPath(String theWebContextPath) {
        webContextPath = theWebContextPath;
    }

    public static String getWebContextPath() {
        return webContextPath;
    }

    public static Session getMailSession() {
        return mailSession;
    }

    protected static void initLog4j() {
        String configFile = "";
        configFile = webContextPath + "/WEB-INF/classes/" + Constants.LOGGING_PROPERTIES;
        org.apache.log4j.PropertyConfigurator.configure(configFile);
        if (configFile == null) {
            System.err.println("[GNomExFrontController] No configuration file specified for log4j!");
        }
        org.apache.log4j.PropertyConfigurator.configure(configFile);
    }

    public static boolean areWeLite() {
        boolean glite = false;

        // check for GNomExLite.properties, if it exists, then we are GNomExLite
        String configFile = webContextPath + "/WEB-INF/classes/GNomExLite.properties";
        File glp = new File(configFile);
        if (glp.exists()) {
            glite = true;
        }

        return glite;
    }

    /**
     * Process the HTTP Get request
     *
     * @param request
     *            Description of the Parameter
     * @param response
     *            Description of the Parameter
     * @exception ServletException
     *                Description of the Exception
     * @exception IOException
     *                Description of the Exception
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * Process the HTTP Post request
     *
     * @param request
     *            Description of the Parameter
     * @param response
     *            Description of the Parameter
     * @exception ServletException
     *                Description of the Exception
     * @exception IOException
     *                Description of the Exception
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // get the users session
        HttpSession session = request.getSession(true);

        session.setAttribute("lastGNomExAccessTime", new Long(new Date().getTime()));

        // get our request from the url (prefixing .test)
        String fullURI = request.getRequestURI();
        String requestName = fullURI.substring((fullURI.lastIndexOf(Constants.FILE_SEPARATOR_CHAR) + 1), fullURI.lastIndexOf('.'));

        // restrict commands to local host if request is not secure
        if (!ServletUtil.checkSecureRequest(request, LOG)) {
            System.out.println(request.getRemoteAddr());
            System.out.println(InetAddress.getLocalHost().getHostAddress());
            LOG.error("Accessing secure command over non-secure line from remote host is not allowed");
            this.forwardWithError(request, response, "Secure connection is required. Prefix your request with 'https:'");
            return;
        }

        // now get our command class and instantiate
        Class commandClass = null;
        Command commandInstance = null;
        try {
            commandClass = Class.forName("hci.gnomex.controller" + "." + requestName);
            commandInstance = (Command) commandClass.newInstance();

            String username = (request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "guest");
            if (request.getUserPrincipal() != null) {
                commandInstance.setUsername(username);
            }

        } catch (ClassNotFoundException cnfe) {
            LOG.error("Command " + requestName + ".class not found");
            this.forwardWithError(request, response);
            return;
        } catch (IllegalAccessException ias) {
            LOG.error("IllegalAccessException while getting command " + requestName);
            this.forwardWithError(request, response);
            return;
        } catch (InstantiationException ie) {
            LOG.error("Unable to instantiate command " + requestName);
            this.forwardWithError(request, response);
            return;
        }
        // we should have a valid command.

        // If we do not have a security advisor for the session, add error to command
        // But do not require for initial data services and CMD to set the sec advisor
        // If we have a security advisor, add it to the command instance
        if (session.getAttribute(SecurityAdvisor.SECURITY_ADVISOR_SESSION_KEY) != null) {
            commandInstance.setSecurityAdvisor((SecurityAdvisor) session
                    .getAttribute(SecurityAdvisor.SECURITY_ADVISOR_SESSION_KEY));
        }

        if (commandInstance.getSecurityAdvisor() == null && (requestName.compareTo("ManageDictionaries") != 0 // You can reload dictionary cache without
                // security
                || request.getParameter("action") == null || !request.getParameter("action").equals("reload"))
                && (!requestName.startsWith("CreateSecurityAdvisor"))
                && (!requestName.equals("ShowAnalysisDownloadForm"))
                && (!requestName.equals("ShowAnalysisDownloadFormForGuest"))
                && (!requestName.equals("ShowRequestDownloadForm"))
                && (!requestName.equals("ChangePassword"))
                && (!requestName.equals("GetLaunchProperties"))
                && (!requestName.equals("PublicSaveSelfRegisteredAppUser"))
                && (!requestName.equals("ShowRequestDownloadFormForGuest"))
                && (!requestName.equals("ShowExperimentMatrix"))
                && (!requestName.equals("ShowTopicTree"))) {

            commandInstance.addInvalidField("SecurityAdvisor",
                    "You must create a SecurityAdvisor in order to run this command.");
        }
        // if command still valid, call the loadCommand method
        if (commandInstance.isValid()) {
            LOG.debug("Calling loadCommand on " + commandClass);
//            System.out.println ("Calling loadCommand on " + commandClass);

            commandInstance.loadCommand(request, session);
        }
        // see if it is valid, if so call execute
        if (commandInstance.isValid()) {
            LOG.debug("Forwarding " + commandClass + " to the request processor for execution");
            File whereami = new File (".");
            String pathname = whereami.getAbsolutePath();
//            System.out.println ("the pathname: " + pathname);
//            System.out.println ("[GNomExFrontController] --->Forwarding " + commandClass + " to the request processor for execution");
            try {
                commandInstance.execute();
            } catch (Exception e) {
                LOG.error("Error in gnomex front controller:", e);
                System.out.println ("Error in gnomex front controller: " +  e);
                StringBuilder requestDump = Util.printRequest(request);
                String serverName = request.getServerName();

                commandInstance.setRequestState(request);

                String errorMessage = (String) request.getAttribute("errorDetails");
                String username = commandInstance.getUsername();

                Util.sendErrorReport(HibernateSession.currentSession(), "GNomEx.Support@hci.utah.edu", "DoNotReply@hci.utah.edu", username, errorMessage, requestDump);


                HibernateSession.rollback();
                String msg = null;

                if (requestName.compareTo("ChangePassword") == 0) {
                    // Have to place error message here because by the time we get here the ChangePassword instance
                    // no longer retains state it was in when RollBackCommandException was thrown
                    request.setAttribute("message", "There was a database problem while changing the password.");
                    ChangePassword changePwdCommand = (ChangePassword) commandInstance;
                    forwardPage(request, response, changePwdCommand.ERROR_JSP);
                    return;
                }

                if (requestName.compareTo("PublicSaveSelfRegisteredAppUser") == 0) {
                    // Have to place error message here because by the time we get here the PublicSaveSelfRegisteredAppUser instance
                    // no longer retains state it was in when RollBackCommandException was thrown
                    request.setAttribute("message", "There was a database problem while running the self register command.");
                    PublicSaveSelfRegisteredAppUser selfRegisterCommand = (PublicSaveSelfRegisteredAppUser) commandInstance;
                    forwardPage(request, response, selfRegisterCommand.responsePageError);
                    return;
                }

                if (msg != null) {
                    this.forwardWithError(request, response, msg);
                } else {
                    LOG.error(e.getClass().getName() + " while executing command " + commandClass);
                    LOG.error("The stacktrace for the error:");
                    LOG.error(e.getMessage(), e);

                    if (e instanceof GNomExRollbackException
                            && ((GNomExRollbackException) e).getDisplayFriendlyMessage() != null) {
                        this.forwardWithError(request, response, ((GNomExRollbackException) e).getDisplayFriendlyMessage());
                    } else {
                        String exMsg = "";
                        if (e != null && e.getMessage() != null && e.getMessage().indexOf(':') != -1) {
                            exMsg = e.getMessage().substring(e.getMessage().indexOf(':') + 1);
                        } else
                            if (e != null && e.getMessage() != null ) {
                                exMsg = e.getMessage();
                            }
                        this.forwardWithError(request, response, exMsg);
                    }
                }
                return;
            } finally {
                try {
                    HibernateSession.closeSession();
                } catch (Exception ex) {
                    LOG.error("GNomExFrontController: Error closing hibernate session", ex);
                }
            }
        }
        // now set the request state, response state, and session state
        LOG.debug("Calling setRequestState on " + commandClass);
        commandInstance.setRequestState(request);

        LOG.debug("Calling setResponseState on " + commandClass);
        commandInstance.setResponseState(response);

        LOG.debug("Calling setSessionState on " + commandClass);
        commandInstance.setSessionState(session);

        // if GNomExLite, convert it to JSON and give it back
        if (GNomExLite && !requestName.contains("ShowAnnotationProgressReport")) {
            System.out.println("[GNomExFrontController] requestName: " + requestName);
            String thexml = (String) request.getAttribute("xmlResult");
            if (thexml != null && !thexml.equals("")) {

                if (thexml.length() < 80) {
                    System.out.println("WARNING short xml: -->" + thexml + "<--");
                }
                XMLSerializer xmlSerializer = new XMLSerializer();
                JSON json = xmlSerializer.read(thexml);
                String thejson = json.toString(2);

                // get rid of the "@
                thejson = thejson.replace("\"@", "\"");

                response.setContentType("application/json");
                // Get the printwriter object from response to write the required json object to the output stream
                PrintWriter out = response.getWriter();
                // Assuming your json object is **jsonObject**, perform the following, it will return your json object
                out.print(thejson);
                out.flush();
                out.close();

                System.out.println("Returned " + thejson.length() + " bytes of JSON for request " + requestName);

            } else {
                // debug ****************
                System.out.println("Empty XML for request: " + requestName);
            }
        }

        // get our response page
        String forwardJSP = commandInstance.getResponsePage();

        // if command didn't provide one, default to message.jsp (for error)
        if (forwardJSP == null || forwardJSP.equals("")) {
            forwardJSP = "/message.jsp";
        }

        if (!GNomExLite || (requestName != null && requestName.contains("ShowAnnotationProgressReport"))) {
            // System.out.println ("requestName: " + requestName + " forwardJSP: " + forwardJSP);

            if (!commandInstance.isValid()) {
                String tmpMessage = commandInstance.getInvalidFieldsMessage();
                request.setAttribute("message", tmpMessage);
            }

            if (commandInstance.isRedirect()) {
                this.sendRedirect(response, forwardJSP);
            } else {
                // forward to our response page
                forwardPage(request, response, forwardJSP);
            }
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
        LOG.debug("Forwarding response to " + url);
        try {
            getServletContext().getRequestDispatcher(url).forward(request, response);
        } catch (Exception e) {
            LOG.error(e.getClass().getName() + " while attempting to forward to " + url);
            LOG.error("The stacktrace for the error:");
            LOG.error(e.getMessage(), e);
        }
    }

    private void sendRedirect(HttpServletResponse response, String url) {
        LOG.debug("Redirecting response to " + url);
        try {
            response.sendRedirect(url);
        } catch (Exception e) {
            LOG.error(e.getClass().getName() + " while attempting to redirect to " + url);
            LOG.error("The stacktrace for the error:");
            LOG.error(e.getMessage(), e);
        }
    }

    /**
     * Clean up resources
     */
    public void destroy() {
    }
}
