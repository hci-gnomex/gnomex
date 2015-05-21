package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.HibernateSession;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;

public abstract class HttpClientServletBase extends HttpServlet {
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    GNomExFrontController.setWebContextPath(config.getServletContext().getRealPath("/"));    
  }

  protected abstract String getNameOfServlet();
  protected abstract GNomExCommand getCommand();
  protected abstract void logError(String msg, Exception ex);
  
  protected void doPost( HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException {
    doGet(req, res);
  }
  
  protected void doGet( HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException {
    Session sess = null;
    try {
      sess = HibernateSession.currentSession(getNameOfServlet());
      
      String username=req.getParameter("userName");

      // Get security advisor
      SecurityAdvisor secAdvisor = (SecurityAdvisor) req.getSession().getAttribute(SecurityAdvisor.SECURITY_ADVISOR_SESSION_KEY);
      if (secAdvisor == null) {
        if(!getNameOfServlet().equals("SaveChromatogramFromFileServlet")) {
          System.out.println(getNameOfServlet() + ":  Warning - unable to find existing session. Creating security advisor.");
        }
        secAdvisor = SecurityAdvisor.create(sess, username);
      }

      HttpSession session = req.getSession(true);
  
      // Load dictionaries if necessary
      if (!ManageDictionaries.isLoaded) {
        Command loadCmd = new ManageDictionaries();
        loadCmd.setSecurityAdvisor(secAdvisor);
        loadCmd.loadCommand(req,  session);
        loadCmd.execute();
      }
      
      // Execute the command
      GNomExCommand cmd = getCommand();
      cmd.setSecurityAdvisor(secAdvisor);
      cmd.loadCommand(req, session);
      cmd.execute();
      cmd.setRequestState(req);
      cmd.setResponseState(res);
      cmd.setSessionState(session);
      getServletContext().getRequestDispatcher(cmd.getResponsePage()).forward(req, res);
    } catch(Exception ex) {
      HibernateSession.rollback();
      logError(getNameOfServlet() + " -- Unhandled exception", ex);
    } finally {
      if (sess != null) {
        try {
          HibernateSession.closeSession();
          HibernateSession.closeTomcatSession();
        } catch(Exception ex1) {
        }
      }
    }

  }

}
