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

public class CreateAnalysisServlet extends HttpClientServletBase {
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CreateAnalysisServlet.class);
  
  protected String getNameOfServlet() {
    return "CreateAnalysisServlet";
  }
  
  protected GNomExCommand getCommand() {
    return new SaveAnalysis();
  }

  protected void logError(String msg, Exception ex) {
    log.error(msg, ex);
  }
}
