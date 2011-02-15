package hci.gnomex.controller;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.Property;
import hci.gnomex.model.Request;
import hci.gnomex.utility.HibernateGuestSession;
import hci.gnomex.utility.PropertyHelper;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

public class UploadExperimentURLServlet extends HttpServlet {
  
  private Integer idRequest = null;
  private String  directoryName = "";
  
  private Request  request;
  private String   fileName;

  protected void doGet( HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException {

    // restrict commands to local host if request is not secure
    if (Constants.REQUIRE_SECURE_REMOTE && !req.isSecure()) {
      if (req.getRemoteAddr().equals(InetAddress.getLocalHost().getHostAddress())
          || req.getRemoteAddr().equals("127.0.0.1") 
          || InetAddress.getByName(req.getRemoteAddr()).isLoopbackAddress()) {
       
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
    
    Session sess = null;
    
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
      
      sess = HibernateGuestSession.currentGuestSession(req.getUserPrincipal().getName());
      String portNumber = PropertyHelper.getInstance(sess).getProperty(Property.HTTP_PORT);
      if (portNumber == null) {
        portNumber = "";
      } else {
        portNumber = ":" + portNumber;           
      }
      
      String baseURL =  "http"+  "://"  + req.getServerName() + portNumber + req.getContextPath();
      String URL = baseURL + "/" + "UploadExperimentFileServlet.gx";
      // Encode session id in URL so that session maintains for upload servlet when called from
      // Flex upload component inside FireFox, Safari
      URL += ";jsessionid=" + req.getRequestedSessionId();
      
      
      res.setContentType("application/xml");
      res.getOutputStream().println("<UploadExperimentURL url='" + URL + "'/>");
      
    } catch (Exception e) {
      System.out.println("An error has occured in UploadExperimentURLServlet - " + e.toString());
    } finally {
      if (sess != null) {
        try {
          HibernateGuestSession.closeGuestSession();          
        } catch (Exception e) {
        }
      }
    }
  }
}
