package hci.gnomex.controller;


import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateGuestSession;
import hci.gnomex.utility.MailUtil;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.awt.image.BufferedImage;

import java.io.ByteArrayInputStream;
import java.io.File;

import java.io.IOException;
import java.io.InputStream;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;

import java.util.Date;



import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import org.hibernate.Session;


import com.oreilly.servlet.multipart.FilePart;
import com.oreilly.servlet.multipart.MultipartParser;
import com.oreilly.servlet.multipart.ParamPart;
import com.oreilly.servlet.multipart.Part;


public class ReportIssueFeedbackServlet extends HttpServlet {
  private String subject = "Issue Reported";
  private String fromAddress;
  private String body = "Feedback";
  private String format = "text";
  private String IdAppUser = "";
  private String AppUserName = "";
  private String UNID = "";		
  private BufferedImage image = null;
  private File outputfile = null;
  private String Filename = null; //not needed
  private SimpleDateFormat sdf = new SimpleDateFormat();
  private Date currentDate;

  private static final int STATUS_ERROR = 999;

  protected void doGet(HttpServletRequest req, HttpServletResponse res)
  throws ServletException, IOException {
    doPost(req, res);
  }

  /*
   * SPECIAL NOTE - This servlet must be run on non-secure socket layer (http)
   * in order to keep track of previously created session. (see note below
   * concerning flex upload bug on Safari and FireFox). Otherwise, session is
   * not maintained. Although the code tries to work around this problem by
   * creating a new security advisor if one is not found, the Safari browser
   * cannot handle authenticating the user (this second time). So for now,
   * this servlet must be run non-secure.
   */
  protected void doPost(HttpServletRequest req, HttpServletResponse res)
  throws ServletException, IOException {
    try {
      currentDate = new Date(System.currentTimeMillis());
      Session sess = HibernateGuestSession.currentGuestSession(req.getUserPrincipal() != null ? req.getUserPrincipal().getName() : "guest");


      // Get the dictionary helper
      DictionaryHelper dh = DictionaryHelper.getInstance(sess);
      PropertyDictionaryHelper pdh = PropertyDictionaryHelper.getInstance(sess);

      // Get security advisor
      SecurityAdvisor secAdvisor = (SecurityAdvisor) req.getSession().getAttribute(SecurityAdvisor.SECURITY_ADVISOR_SESSION_KEY);
      if (secAdvisor == null) {
        System.out.println("ReportIssueServlet:  Warning - unable to find existing session. Creating security advisor.");
        secAdvisor = SecurityAdvisor.create(sess, req.getUserPrincipal().getName());
      }

      //
      // To work around flex upload problem with FireFox and Safari,
      // create security advisor since
      // we loose session and thus don't have security advisor in session
      // attribute.
      //
      // Note from Flex developer forum
      // (http://www.kahunaburger.com/2007/10/31/flex-uploads-via-httphttps/):
      // Firefox uses two different processes to upload the file.
      // The first one is the one that hosts your Flex (Flash) application
      // and communicates with the server on one channel.
      // The second one is the actual file-upload process that pipes
      // multipart-mime data to the server.
      // And, unfortunately, those two processes do not share cookies. So
      // any sessionid-cookie that was established in the first channel
      // is not being transported to the server in the second channel.
      // This means that the server upload code cannot associate the
      // posted
      // data with an active session and rejects the data, thus failing
      // the upload.
      //
      if (secAdvisor == null) {
        System.out.println("ReportIssueServlet: Error - Unable to find or create security advisor.");
        throw new ServletException("Unable to report issue.  Servlet unable to obtain security information. Please contact GNomEx support directly.");
      }
      /*
			// Only gnomex admins can send broadcast emails
			if (!secAdvisor
					.hasPermission(SecurityAdvisor.CAN_ACCESS_ANY_OBJECT)) {
				throw new ServletException("Insufficent permissions");
			}
       */			
      MultipartParser mp = new MultipartParser(req, Integer.MAX_VALUE);
      Part part;
      while ((part = mp.readNextPart()) != null) {
        String name = part.getName();
        if (part.isParam()) {
          // it's a parameter part
          ParamPart paramPart = (ParamPart) part;
          String value = paramPart.getStringValue();
          if (name.equals("Filename")) { // not needed
            this.Filename = value;
          } else if (name.equals("subject")) {
            this.subject = value;
          } else if (name.equals("fromAddress")) {
            this.fromAddress = value;
          } else if (name.equals("body")) {
            this.body = value;
          } else if (name.equals("IdAppUser")) {
            this.IdAppUser = value;
          } else if (name.equals("AppUserName")) {
            this.AppUserName = value;
          } else if (name.equals("UNID")) {
            this.UNID = value;
          }
        } else if (part.isFile()) {
          FilePart filePart = (FilePart) part;

          InputStream is = filePart.getInputStream();
          byte[] binaryData = IOUtils.toByteArray(is);

          {
          }
          if (is == null) {
            throw new ServletException("Empty input stream.");
          }
          try {
            ByteArrayInputStream bais = new ByteArrayInputStream(
                binaryData);
            image = ImageIO.read(bais);
            String filename = "IssueReportedScreenshot " + currentDate.toString() + ".png";
            filename = filename.replaceAll("\\s", "");
            filename = filename.replaceAll(":", "");
            filename = pdh.getQualifiedProperty(PropertyDictionary.TEMP_DIRECTORY, req.getServerName()) + filename;
            File outputfileTemp = new File(filename);
            //File outputfileTemp = new File("ReportIssueScreenshot.png");
            ImageIO.write(image, "png", outputfileTemp);
            String absolutePath = outputfileTemp.getAbsolutePath();
            outputfile = new File(absolutePath);

          } catch (Exception e) {
            res.setStatus(STATUS_ERROR);
            System.out.println(e.toString());
            e.printStackTrace();
            throw new ServletException(
            "Failed to write screenshot to file.");

          } finally {
            is.close();
          }
        }
      }

      boolean send = false;
      String theSubject = subject + " " + currentDate.toString();
      String addPHI = DictionaryHelper.getInstance(sess).getPropertyDictionary(PropertyDictionary.ADD_PHI_TO_SUPPORT_EMAIL);
      if (addPHI != null && addPHI.equals("Y")) {
        theSubject += " PHI ";
      }
      String emailInfo = "";
      String emailRecipients = DictionaryHelper.getInstance(sess).getPropertyDictionary(PropertyDictionary.CONTACT_EMAIL_SOFTWARE_BUGS);
      if (dh.isProductionServer(req.getServerName())) {
        send = true;
      } else {
        send = true;
        theSubject = theSubject + "  (TEST)";
        emailInfo = "[If this were a production environment then this email would have been sent to: "
          + emailRecipients + "]\r\r";
        emailRecipients = DictionaryHelper
        .getInstance(sess)
        .getPropertyDictionary(
            PropertyDictionary.CONTACT_EMAIL_SOFTWARE_TESTER);
      }



      String emailBody =  "Time: "					+ sdf.format(currentDate)															+ "\r" +
      "RequestURL: " 				+ req.getRequestURL().toString() 													+ "\r" + 
      "IdAppUser: " 				+ this.IdAppUser 																	+ "\r" +
      "AppUserName: " 			+ this.AppUserName 																	+ "\r" +
      "UNID: " 					+ this.UNID 																		+ "\r" +
      "-------------------------------------------User Feedback-----------------------------------------------------" + "\r" +
      body.toString();
      // Email app user
      if (send) {
        if (!MailUtil.isValidEmail(fromAddress)) {
          fromAddress = DictionaryHelper.getInstance(sess)
          .getPropertyDictionary(
              PropertyDictionary.GENERIC_NO_REPLY_EMAIL);
        }
        MailUtil.send_attach(emailRecipients,
            "", fromAddress, theSubject,
            emailInfo + emailBody,
            format.equalsIgnoreCase("HTML") ? true : false,
                outputfile);
        outputfile.delete();
      }

      PrintWriter out = res.getWriter();
      res.setHeader("Cache-Control", "max-age=0, must-revalidate");

      String baseURL = "";
      StringBuffer fullPath = req.getRequestURL();
      String extraPath = req.getServletPath()
      + (req.getPathInfo() != null ? req.getPathInfo() : "");
      int pos = fullPath.lastIndexOf(extraPath);
      if (pos > 0) {
        baseURL = fullPath.substring(0, pos);
      }

      org.dom4j.io.OutputFormat format = org.dom4j.io.OutputFormat
      .createPrettyPrint();
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
      h3.addCDATA("The issue has been successfully reported. Thank you.");
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
      throw new ServletException("Unable to report issue due to a server error.  Please contact GNomEx support directly.");
    } finally {
      try {
        HibernateGuestSession.closeGuestSession();
      } catch (Exception e1) {
        System.out.println("ReportIssueServlet warning - cannot close hibernate session");
      }
    }

  }
}
