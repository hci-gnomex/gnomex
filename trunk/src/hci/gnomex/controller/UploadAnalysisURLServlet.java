package hci.gnomex.controller;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.Analysis;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UploadAnalysisURLServlet extends HttpServlet {
  
  private Integer idAnalysis = null;
  private String  directoryName = "";
  
  private Analysis analysis;
  private String   fileName;

  protected void doGet( HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException {

    // restrict commands to local host if request is not secure
    if (Constants.REQUIRE_SECURE_REMOTE && !req.isSecure()) {
      if (req.getRemoteAddr().equals(InetAddress.getLocalHost().getHostAddress())
          || req.getRemoteAddr().equals("127.0.0.1")) {
       
      }
      else {
        
        res.setContentType("text/html");
        res.getOutputStream().println(
            "<html><head><title>Error</title></head>");
        res.getOutputStream().println("<body><b>");
        res.getOutputStream().println(
            "Secure connection is required. Prefix your request with 'https: "
                + "<br>");
        res.getOutputStream().println("</body>");
        res.getOutputStream().println("</html>");
        return;
      }
    }
    
    try {
      
      boolean isLocalHost = req.getServerName().equalsIgnoreCase("localhost") || req.getServerName().equals("127.0.0.1");
      
      //
      // COMMENTED OUT CODE: 
      //    String baseURL =  "http"+ (isLocalHost ? "://" : "s://") + req.getServerName() + req.getContextPath();
      //
      // To fix upload problem (missing session in upload servlet for FireFox, Safari), encode session in URL
      // for upload servlet.  Also, use non-secure (http: rather than https:) when making http request; 
      // otherwise, existing session is not accessible to upload servlet.
      //
      //
      
      String baseURL =  "http"+  "://"  + req.getServerName() + req.getContextPath();
      String URL = baseURL + "/" + "UploadAnalysisFileServlet.gx";
      // Encode session id in URL so that session maintains for upload servlet when called from
      // Flex upload component inside FireFox, Safari
      URL += ";jsessionid=" + req.getRequestedSessionId();
      
      
      res.setContentType("application/xml");
      res.getOutputStream().println("<UploadAnalysisURL url='" + URL + "'/>");
      
    } catch (Exception e) {
      System.out.println("An error has occured in UploadAnalysisURLServlet - " + e.toString());
    }
  }
}
