package hci.gnomex.controller;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.TransferLog;
import hci.gnomex.security.SecurityAdvisor;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.InetAddress;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;


public class DownloadChromatogramFileServlet extends HttpServlet { 

  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DownloadChromatogramFileServlet.class);
  
  private String                          fileName = null;
  private String                          dir = null;
  
  
  public void init() {
  
  }
    
  protected void doGet(HttpServletRequest req, HttpServletResponse response)
      throws ServletException, IOException {
    
    fileName = null;
    dir = null;
    
    // restrict commands to local host if request is not secure
    if (Constants.REQUIRE_SECURE_REMOTE && !req.isSecure()) {
      if (req.getRemoteAddr().equals(InetAddress.getLocalHost().getHostAddress())
          || req.getRemoteAddr().equals("127.0.0.1")
          || InetAddress.getByName(req.getRemoteAddr()).isLoopbackAddress()) {
        log.debug("Requested from local host");
      }
      else {
        log.error("Accessing secure command over non-secure line from remote host is not allowed");
        
        response.setContentType("text/html");
        response.getOutputStream().println(
            "<html><head><title>Error</title></head>");
        response.getOutputStream().println("<body><b>");
        response.getOutputStream().println(
            "Secure connection is required. Prefix your request with 'https: "
                + "<br>");
        response.getOutputStream().println("</body>");
        response.getOutputStream().println("</html>");
        return;
      }
    }
    
    
    // Get the fileName parameter
    if (req.getParameter("fileName") != null && !req.getParameter("fileName").equals("")) {
      fileName = req.getParameter("fileName");
    }
    
    // Get the dir parameter
    if (req.getParameter("dir") != null && !req.getParameter("dir").equals("")) {
      dir = req.getParameter("dir");
    } 
    
    if (fileName == null) {
      log.error("fileName required");
      
      response.setContentType("text/html");
      response.getOutputStream().println(
          "<html><head><title>Error</title></head>");
      response.getOutputStream().println("<body><b>");
      response.getOutputStream().println(
          "Missing parameter:  fileName required"
              + "<br>");
      response.getOutputStream().println("</body>");
      response.getOutputStream().println("</html>");
      return;
      
    }

    InputStream in = null;
    SecurityAdvisor secAdvisor = null;
    try {
      

      // Get security advisor
     secAdvisor = (SecurityAdvisor) req.getSession().getAttribute(SecurityAdvisor.SECURITY_ADVISOR_SESSION_KEY);

      if (secAdvisor != null) {

        response.setContentType("application/x-download");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);          
        response.setHeader("Cache-Control", "max-age=0, must-revalidate");


        Session sess = secAdvisor.getHibernateSession(req.getUserPrincipal() != null ? req.getUserPrincipal().getName() : "guest");

        
        // Check permissions - bypass this file if the user 
        // does not have  permission to read it.
//        if (!secAdvisor.canRead(analysis)) {  
//          throw new Exception("Insufficient permissions to read request " + analysis.getNumber() + ".  Bypassing download.");
//        }

        
        if (fileName != null) {
          
          
          // Insert a transfer log entry
          TransferLog xferLog = new TransferLog();
          
          xferLog.setFileName(fileName);
          
          xferLog.setStartDateTime(new java.util.Date(System.currentTimeMillis()));
          xferLog.setTransferType(TransferLog.TYPE_DOWNLOAD);
          xferLog.setTransferMethod(TransferLog.METHOD_HTTP);
          xferLog.setPerformCompression("Y");
          
          
          
          in = new FileInputStream(fileName);
          OutputStream out = response.getOutputStream();
          byte b[] = new byte[102400];
          int numRead = 0;
          int size = 0;
          while (numRead != -1) {
            numRead = in.read(b);
            if (numRead != -1) {
              out.write(b, 0, numRead);                                    
              size += numRead;
            }
          }
          
          // Save transfer log 
          xferLog.setFileSize(new BigDecimal(size));
          xferLog.setEndDateTime(new java.util.Date(System.currentTimeMillis()));
          sess.save(xferLog);
          
          in.close();
          out.close();
          out.flush();
          in = null;
        }

        sess.flush();
        

      } else {
        response.setContentType("text/html");
        response.getOutputStream().println(
            "<html><head><title>Error</title></head>");
        response.getOutputStream().println("<body><b>");
        response.getOutputStream().println(
            "DownloadChromatogramFileServlet: You must have a SecurityAdvisor in order to run this command."
                + "<br>");
        response.getOutputStream().println("</body>");
        response.getOutputStream().println("</html>");
        System.out.println( "DownloadChromatogramFileServlet: You must have a SecurityAdvisor in order to run this command.");
      }
    } catch (Exception e) {
      response.setContentType("text/html");
      response.getOutputStream().println(
          "<html><head><title>Error</title></head>");
      response.getOutputStream().println("<body><b>");
      response.getOutputStream().println(
          "DownloadChromatogramFileServlet: An exception occurred " + e.toString()
              + "<br>");
      response.getOutputStream().println("</body>");
      response.getOutputStream().println("</html>");
      System.out.println( "DownloadChromatogramFileServlet: An exception occurred " + e.toString());
      e.printStackTrace();
    } finally {
      try {
        secAdvisor.closeHibernateSession();        
      } catch (Exception e) {
        
      }
      
      if (in != null) {
        in.close();
      }
    }

  }    
  
  
 
 
}