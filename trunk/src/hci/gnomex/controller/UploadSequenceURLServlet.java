package hci.gnomex.controller;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.utility.HibernateGuestSession;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.IOException;
import java.net.InetAddress;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

public class UploadSequenceURLServlet extends HttpServlet {
  

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
      String portNumber = PropertyDictionaryHelper.getInstance(sess).getProperty(PropertyDictionary.HTTP_PORT);
      if (portNumber == null) {
        portNumber = "";
      } else {
        portNumber = ":" + portNumber;           
      }

      String baseURL =  "http"+  "://"  + req.getServerName()  + portNumber + req.getContextPath();
      String URL = baseURL + "/UploadSequenceFileServlet.gx";
      // Encode session id in URL so that session maintains for upload servlet when called from
      // Flex upload component inside FireFox, Safari
      URL += ";jsessionid=" + req.getRequestedSessionId();

      // Get the valid file extensions
      StringBuffer fileExtensions = new StringBuffer();
      for (int x=0; x < Constants.DATATRACK_FILE_EXTENSIONS.length; x++) {
        if (fileExtensions.length() > 0) {
          fileExtensions.append(";");
        }
        fileExtensions.append("*" + Constants.SEQUENCE_FILE_EXTENSIONS[x]);
      }


      res.setContentType("application/xml");
      res.getOutputStream().println("<UploadURL url='" + URL + "'" + " fileExtensions='" + fileExtensions.toString() + "'" + "/>");
      
      
    } catch (Exception e) {
      System.out.println("An error has occured in UploadSequenceURLServlet - " + e.toString());
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
