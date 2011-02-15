package hci.gnomex.controller;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.Analysis;
import hci.gnomex.model.AnalysisFile;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.Property;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateGuestSession;
import hci.gnomex.utility.MailUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.hibernate.Session;

import com.oreilly.servlet.multipart.FilePart;
import com.oreilly.servlet.multipart.MultipartParser;
import com.oreilly.servlet.multipart.ParamPart;
import com.oreilly.servlet.multipart.Part;

public class UploadAndBroadcastEmailServlet extends HttpServlet {
  
  private String subject      = "GNomEx announcement";
  private StringBuffer body   = null;
  private String format       = "text";
  
  private static final int  STATUS_ERROR = 999;
  
  protected void doGet( HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException {
    doPost(req, res);
  }

  /*
   * SPECIAL NOTE -  This servlet must be run on non-secure socket layer (http) in order to
   *                 keep track of previously created session. (see note below concerning
   *                 flex upload bug on Safari and FireFox).  Otherwise, session is 
   *                 not maintained.  Although the code tries to work around this
   *                 problem by creating a new security advisor if one is not found,
   *                 the Safari browser cannot handle authenticating the user (this second time).
   *                 So for now, this servlet must be run non-secure. 
   */
  protected void doPost( HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException {
    try {
      Session sess = HibernateGuestSession.currentGuestSession(req.getUserPrincipal().getName());
      
      // Get the dictionary helper
      DictionaryHelper dh = DictionaryHelper.getInstance(sess);
      
      // Get security advisor
      SecurityAdvisor secAdvisor = (SecurityAdvisor) req.getSession().getAttribute(SecurityAdvisor.SECURITY_ADVISOR_SESSION_KEY);
      if (secAdvisor == null) {
        System.out.println("UploadAndBroadcaseEmailServlet:  Warning - unable to find existing session. Creating security advisor.");
        secAdvisor = SecurityAdvisor.create(sess, req.getUserPrincipal().getName());
      }
      
      //
      // To work around flex upload problem with FireFox and Safari, create security advisor since
      // we loose session and thus don't have security advisor in session attribute.
      //
      // Note from Flex developer forum (http://www.kahunaburger.com/2007/10/31/flex-uploads-via-httphttps/):
      // Firefox uses two different processes to upload the file.
      // The first one is the one that hosts your Flex (Flash) application and communicates with the server on one channel.
      // The second one is the actual file-upload process that pipes multipart-mime data to the server. 
      // And, unfortunately, those two processes do not share cookies. So any sessionid-cookie that was established in the first channel 
      // is not being transported to the server in the second channel. This means that the server upload code cannot associate the posted 
      // data with an active session and rejects the data, thus failing the upload.
      //
      if (secAdvisor == null) {
        System.out.println("UploadAndBroadcaseEmailServlet: Error - Unable to find or create security advisor.");
        throw new ServletException("Unable to upload analysis file.  Servlet unable to obtain security information. Please contact GNomEx support.");
      }
      
      // Only gnomex admins can send broadcast emails
      if (!secAdvisor.hasPermission(SecurityAdvisor.CAN_ACCESS_ANY_OBJECT)) {
        throw new ServletException("Insufficent permissions");
      }
      
      
      // Get a list of all active users with email accounts
      List appUsers = sess.createQuery("SELECT a from AppUser a where a.isActive = 'Y' and a.email is not NULL and a.email != '' ORDER BY a.lastName, a.firstName ").list();
      
      
      if (req.getParameter("body") != null && !req.getParameter("body").equals("")) {
        
        body = new StringBuffer(req.getParameter("body"));
        
        if (req.getParameter("subject") != null && !req.getParameter("subject").equals("")) {
          subject = req.getParameter("subject");
        }
        if (req.getParameter("format") != null && !req.getParameter("format").equals("")) {
          format = req.getParameter("format");
        }
        
      } else {
        
        MultipartParser mp = new MultipartParser(req, Integer.MAX_VALUE); 
        Part part;
        while ((part = mp.readNextPart()) != null) {
          String name = part.getName();
          if (part.isParam()) {
            // it's a parameter part
            ParamPart paramPart = (ParamPart) part;
            String value = paramPart.getStringValue();
            
            if (name.equals("format")) {
              this.format = value;
            } else if (name.equals("subject")) {
              this.subject = value;
            }
            
          } else if (part.isFile()) {
            FilePart filePart = (FilePart) part;
            InputStream is = filePart.getInputStream();
            
            if (is == null) {
              throw new ServletException("Empty input stream.");
            }
           
            String line;
            body = new StringBuffer();
            try { 
              BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8")); 
              while ((line = reader.readLine()) != null) { 
                body.append(line).append("\n"); 
              } 
            } finally { 
              is.close(); 
            } 
          }
        }
        
      }
            
        
      int userCount = 0;
      if (body != null && body.length() > 0) {

        
        for (Iterator i = appUsers.iterator(); i.hasNext();) {
          AppUser appUser = (AppUser)i.next();

          boolean send = false;
          String theSubject = subject;
          if (dh.isProductionServer(req.getServerName())) {
            send = true;
          } else {
            if (appUser.getEmail().equals(dh.getProperty(Property.CONTACT_EMAIL_SOFTWARE_TESTER))) {
              send = true;
              theSubject = "TEST - " + subject;
            }
          }

          // Email app user
          if (send) {
            MailUtil.send(appUser.getEmail(), 
                null,
                dh.getProperty(Property.CONTACT_EMAIL_CORE_FACILITY),
                theSubject,
                body.toString(),
                format.equalsIgnoreCase("HTML") ? true : false); 
            userCount++;

          }

        }
    
      }
      
      
      PrintWriter out = res.getWriter();
      res.setHeader("Cache-Control", "max-age=0, must-revalidate");

      String baseURL = "";
      StringBuffer fullPath = req.getRequestURL();
      String extraPath = req.getServletPath() + (req.getPathInfo() != null ? req.getPathInfo() : "");
      int pos = fullPath.lastIndexOf(extraPath);
      if (pos > 0) {
        baseURL = fullPath.substring(0, pos);
      }
      
      org.dom4j.io.OutputFormat format = org.dom4j.io.OutputFormat.createPrettyPrint();
      org.dom4j.io.HTMLWriter writer = null;
      res.setContentType("text/html");

      Document doc = DocumentHelper.createDocument();
      Element root = doc.addElement("HTML");
      Element head = root.addElement("HEAD");
      Element link = head.addElement("link");
      link.addAttribute("rel", "stylesheet");
      link.addAttribute("type", "text/css");
      link.addAttribute("href", baseURL + "/css/message.css");
      Element body = root.addElement("BODY");
      Element h3 = body.addElement("H3");
      h3.addCDATA("The email has been successfully sent to " + userCount + " GNomEx users.");
      body.addElement("BR");
      body.addElement("BR");
      writer = new org.dom4j.io.HTMLWriter(res.getWriter(), format);            
      writer.write(doc);
      writer.flush();
      writer.close(); 
      res.setStatus(HttpServletResponse.SC_ACCEPTED);


      
      
    } catch (Exception e) {
      res.setStatus(STATUS_ERROR);
      System.out.println(e.toString());
      e.printStackTrace();
      throw new ServletException("Unable to send broadcast email due to a server error.  Please contact GNomEx support.");
    }  finally {
      try {
        HibernateGuestSession.closeGuestSession();        
      } catch (Exception e1) {
        System.out.println("UploadAndBroadcaseEmailServlet warning - cannot close hibernate session");
      }
    } 
    
  }
}
