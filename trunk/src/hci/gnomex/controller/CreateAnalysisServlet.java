package hci.gnomex.controller;

import hci.gnomex.constants.Constants;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.HibernateSession;

import java.io.IOException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;

public class CreateAnalysisServlet extends HttpServlet {
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CreateAnalysisServlet.class);
  
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    GNomExFrontController.setWebContextPath(config.getServletContext().getRealPath("/"));    
  }

  protected void doPost( HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException {
    doGet(req, res);
  }
  
  protected void doGet( HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException {
    Session sess = null;
    try {
      sess = HibernateSession.currentSession("CreateAnalysisServlet");
      
      String username=req.getParameter("userName");

      // Get security advisor
      SecurityAdvisor secAdvisor = (SecurityAdvisor) req.getSession().getAttribute(SecurityAdvisor.SECURITY_ADVISOR_SESSION_KEY);
      if (secAdvisor == null) {
        System.out.println("CreateAnalysisServlet:  Warning - unable to find existing session. Creating security advisor.");
        secAdvisor = SecurityAdvisor.create(sess, username);
      }
  
      SaveAnalysis cmd = new SaveAnalysis();
      cmd.setSecurityAdvisor(secAdvisor);
      HttpSession session = req.getSession(true);
      cmd.loadCommand(req, session);
      cmd.execute();
      cmd.setRequestState(req);
      cmd.setResponseState(res);
      cmd.setSessionState(session);
      getServletContext().getRequestDispatcher(cmd.getResponsePage()).forward(req, res);
    } catch(Exception ex) {
      HibernateSession.rollback();
      log.error("CreateAnalysisServlet -- Unhandled exception", ex);
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
