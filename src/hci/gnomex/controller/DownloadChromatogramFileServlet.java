package hci.gnomex.controller;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.Chromatogram;
import hci.gnomex.security.SecurityAdvisor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;


public class DownloadChromatogramFileServlet extends HttpServlet { 

  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DownloadChromatogramFileServlet.class);
  
  private Integer     idChromatogram = null;
  
  
  public void init() {
  
  }
    
  protected void doGet(HttpServletRequest req, HttpServletResponse response)
      throws ServletException, IOException {
    

    // restrict commands to local host if request is not secure
    if (Constants.REQUIRE_SECURE_REMOTE && !req.isSecure()) {
      if (req.getRemoteAddr().equals(InetAddress.getLocalHost().getHostAddress())
          || req.getRemoteAddr().equals("127.0.0.1")
          || InetAddress.getByName(req.getRemoteAddr()).isLoopbackAddress()) {
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
    if (req.getParameter("idChromatogram") != null && !req.getParameter("idChromatogram").equals("")) {
      idChromatogram = Integer.valueOf(req.getParameter("idChromatogram"));
    }
    
    if (idChromatogram == null) {
      log.error("idChromatogram required");
      
      response.setContentType("text/html");
      response.getOutputStream().println(
          "<html><head><title>Error</title></head>");
      response.getOutputStream().println("<body><b>");
      response.getOutputStream().println(
          "Missing parameter:  idChromatogram required"
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

        Session sess = secAdvisor.getHibernateSession(req.getUserPrincipal() != null ? req.getUserPrincipal().getName() : "guest");
        Chromatogram chromatogram = (Chromatogram)sess.load(Chromatogram.class, idChromatogram);

        
        // Check permissions - bypass this file if the user 
        // does not have  permission to read it.
        boolean hasPermission = false;
        if (chromatogram.getRequest() == null) {
          // Only the admins and dna seq core admins can access chromatograms for control samples
          hasPermission = secAdvisor.hasPermission(SecurityAdvisor.CAN_ADMINISTER_ALL_CORE_FACILITIES) ||
                          secAdvisor.hasPermission(SecurityAdvisor.CAN_MANAGE_DNA_SEQ_CORE);
        } else {
          // For chromatograms that belong to an experiment, make sure
          // the user has read permission on the experiment itself.
          if (secAdvisor.canRead(chromatogram.getRequest())) {
            hasPermission = true;
          }
        }
        if (!hasPermission) {
          response.setContentType("text/html");
          response.getOutputStream().println(
              "<html><head><title>Error</title></head>");
          response.getOutputStream().println("<body><b>");
          response.getOutputStream().println(
              "DownloadChromatogramFileServlet: Insufficient permission to read this chromatogram."
                  + "<br>");
          response.getOutputStream().println("</body>");
          response.getOutputStream().println("</html>");
          System.out.println( "DownloadChromatogramFileServlet: Insufficient permission to read chromatogram.");
          return;
        }
        
        
        response.setContentType("application/x-download");
        response.setHeader("Content-Disposition", "attachment;filename=" + chromatogram.getDisplayName());          
        response.setHeader("Cache-Control", "max-age=0, must-revalidate");


        String fileName = chromatogram.getQualifiedFilePath() + File.separator + chromatogram.getDisplayName();
        
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
        in.close();
        out.close();
        out.flush();
        in = null;


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