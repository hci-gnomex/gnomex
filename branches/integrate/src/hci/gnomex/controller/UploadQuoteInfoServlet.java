package hci.gnomex.controller;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.IScanChip;
import hci.gnomex.model.InternalAccountFieldsConfiguration;
import hci.gnomex.model.Lab;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.Request;
import hci.gnomex.model.TransferLog;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.MailUtil;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import javax.mail.MessagingException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.hibernate.Query;
import org.hibernate.Session;

import com.oreilly.servlet.multipart.FilePart;
import com.oreilly.servlet.multipart.MultipartParser;
import com.oreilly.servlet.multipart.ParamPart;
import com.oreilly.servlet.multipart.Part;

public class UploadQuoteInfoServlet extends HttpServlet {

  private String  requestUuid = null;
  private String  quoteNumber = null;
  private String  directoryName = "";

  private Request request;
  private String  fileName;

  private String  serverName;

  protected void doGet( HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException {
  }

  
  protected void doPost( HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException {
    try {
      serverName = req.getServerName();

      Session sess =  HibernateSession.currentSession("uploadQuoteInfoServlet");


      res.setContentType("text/html");
      PrintWriter out = res.getWriter();
      res.setHeader("Cache-Control", "max-age=0, must-revalidate");
      DecimalFormat sizeFormatter = new DecimalFormat("###,###,###,####,###");

      Element body = null;

      org.dom4j.io.OutputFormat format = null;
      org.dom4j.io.HTMLWriter writer = null;
      Document doc = null;
      String baseURL = "";

      StringBuffer fullPath = req.getRequestURL();
      String extraPath = req.getServletPath() + (req.getPathInfo() != null ? req.getPathInfo() : "");
      int pos = fullPath.lastIndexOf(extraPath);
      if (pos > 0) {
        baseURL = fullPath.substring(0, pos);
      }

      res.setContentType("text/html");
      res.setHeader("Cache-Control", "max-age=0, must-revalidate");

      format = org.dom4j.io.OutputFormat.createPrettyPrint();        
      writer = new org.dom4j.io.HTMLWriter(res.getWriter(), format);         
      doc = DocumentHelper.createDocument();

      Element root = doc.addElement("HTML");
      Element head = root.addElement("HEAD");
      Element link = head.addElement("link");
      link.addAttribute("rel", "stylesheet");
      link.addAttribute("type", "text/css");
      link.addAttribute("href", baseURL + "/css/message.css");
      body = root.addElement("BODY");

      // Get the form data
      MultipartParser mp = new MultipartParser(req, Integer.MAX_VALUE); 
      Part part;
      while ((part = mp.readNextPart()) != null) {
        String name = part.getName();
        if (part.isParam()) {
          // it's a parameter part
          ParamPart paramPart = (ParamPart) part;
          String value = paramPart.getStringValue();
          if (name.equals("requestUuid")) {
            requestUuid = value;
          }
          if (name.equals("quoteNumber")) {
            quoteNumber = value;
            if (body != null) {
              Element h3 = body.addElement("H3");
              h3.addCDATA("Quote number " + quoteNumber + " received.");              
            }
            break;
          }
        }
      }

      // Get the request
      if (requestUuid != null) {
        String queryString = "SELECT r from Request r WHERE r.uuid = :reqUuid";
        Query reqQuery = sess.createQuery( queryString );
        reqQuery.setParameter( "reqUuid", requestUuid );
        request = (Request)reqQuery.uniqueResult();
      }

      if (request != null) {

        if ( quoteNumber != null && !quoteNumber.equals( "" )) {
          request.setMaterialQuoteNumber( quoteNumber );
          request.setQuoteReceivedDate( new java.sql.Date(System.currentTimeMillis()) );
          request.setUuid( null );
          sess.save( request );
          sess.flush();
        }

        // Send email to purchasing
        try {
          if ( sendPurchasingEmail(sess, request, serverName) ) {
            body.addElement("BR");
            body.addCDATA("Email sent to purchasing department."); 
          } 
        } catch (Exception e) {
          String msg = "Unable to send email to purchasing regarding Illumina iScan Chip(s) for request "
            + request.getNumber()
            + ".  " + e.toString();
          System.out.println(msg);
          e.printStackTrace();
          Element h3 = body.addElement("H3");
          h3.addCDATA("Warning: Email not sent to purchasing department.");   
        }

        // Make sure correct directories are in place or create them.
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        String createYear = formatter.format(request.getCreateDate());

        String baseDir = PropertyDictionaryHelper.getInstance(sess).getExperimentDirectory(req.getServerName(), request.getIdCoreFacility());
        baseDir +=  "/" + createYear;
        if (!new File(baseDir).exists()) {
          boolean success = (new File(baseDir)).mkdir();
          if (!success) {
            System.out.println("UploadQuoteInfoServlet: Unable to create base directory " + baseDir);      
          }      
        }

        directoryName = baseDir + "/" + Request.getBaseRequestNumber(request.getNumber());
        if (!new File(directoryName).exists()) {
          boolean success = (new File(directoryName)).mkdir();
          if (!success) {
            System.out.println("UploadQuoteInfoServlet: Unable to create directory " + directoryName);      
          }      
        }

        directoryName += "/" + Constants.MATERIAL_QUOTE_DIR;
        if (!new File(directoryName).exists()) {
          boolean success = (new File(directoryName)).mkdir();
          if (!success) {
            System.out.println("UploadQuoteInfoServlet: Unable to create directory " + directoryName);      
          }      
        }

        // Read the file.
        while ((part = mp.readNextPart()) != null) {        
          if (part.isFile()) {
            // it's a file part
            FilePart filePart = (FilePart) part;
            fileName = filePart.getFileName();
            if (fileName != null) {

              // Init the transfer log
              TransferLog xferLog = new TransferLog();
              xferLog.setStartDateTime(new java.util.Date(System.currentTimeMillis()));
              xferLog.setTransferType(TransferLog.TYPE_UPLOAD);
              xferLog.setTransferMethod(TransferLog.METHOD_HTTP);
              xferLog.setPerformCompression("N");
              xferLog.setIdRequest(request.getIdRequest());
              xferLog.setIdLab(request.getIdLab());
              xferLog.setFileName(request.getNumber() + "/" + fileName);

              // the part actually contained a file
              long size = filePart.writeTo(new File(directoryName));

              // Insert the transfer log
              xferLog.setFileSize(new BigDecimal(size));
              xferLog.setEndDateTime(new java.util.Date(System.currentTimeMillis()));
              sess.save(xferLog);

              body.addElement("P");
              body.addCDATA("File '" + fileName + "' successfully uploaded (" + sizeFormatter.format(size) + " bytes).");                             
            }

            out.flush();

          }
        }

        if (doc != null && writer != null) {
          writer.write(doc);
          writer.flush();
          writer.close(); 
        }

      } else {
        System.out.println("UploadQuoteInfoServlet - unable to upload quote information for request requestNumber=" + request.getNumber());
        System.out.println("valid requestNumber is required");
        throw new ServletException("Unable to upload quote information due to a server error.  Please contact GNomEx support.");
      }

    } catch (Exception e) {
      HibernateSession.rollback();
      System.out.println("UploadQuoteInfoServlet - unable to upload quote information for request " + request.getNumber());
      System.out.println(e.toString());
      e.printStackTrace();
      throw new ServletException("Unable to upload quote information due to a server error.  Please contact GNomEx support.");
    }  finally {
      try {
        HibernateSession.closeSession();
        HibernateSession.closeTomcatSession();
      } catch (Exception e1) {
        System.out.println("UploadQuoteInfoServlet warning - cannot close hibernate session");
      }
    } 

  }

  public static Boolean sendPurchasingEmail(Session sess, Request req, String serverName) throws NamingException, MessagingException, IOException {

    //workaround until NullPointer exception is dealt with
    InternalAccountFieldsConfiguration.getConfiguration(sess);
    
    DictionaryHelper dictionaryHelper = DictionaryHelper.getInstance(sess);
    IScanChip chip = (IScanChip) sess.get( IScanChip.class, req.getIdIScanChip() );
    Lab lab = (Lab) sess.get( Lab.class, req.getIdLab() );
    BillingAccount acct = (BillingAccount) sess.get( BillingAccount.class, req.getIdBillingAccount() );

    if ( req == null || chip == null || req.getNumberIScanChips()==0 || acct == null ) {
      throw new MessagingException( "Request or Acct is null" );
    }
    
    String labName = lab.getName()!=null ? lab.getName() : "";
    String acctName = acct.getAccountName() != null ? acct.getAccountName() : "";
    String acctNumber = acct.getAccountNumber() != null ? acct.getAccountNumber() : "";
    String numberChips = req.getNumberIScanChips() != null ? req.getNumberIScanChips().toString() : "";
    String chipName = chip.getName() != null ? chip.getName() : "";
    String catNumber = chip.getCatalogNumber() != null ? chip.getCatalogNumber() : "";
    String quoteNum = req.getMaterialQuoteNumber() != null ? req.getMaterialQuoteNumber() : "";

    BigDecimal estimatedCost = new BigDecimal( BigInteger.ZERO, 2 ) ;
    estimatedCost = chip.getCostPerSample().multiply( new BigDecimal(chip.getSamplesPerChip()) );
    estimatedCost = estimatedCost.multiply( new BigDecimal(req.getNumberIScanChips()) );

    StringBuffer emailBody = new StringBuffer();

    emailBody.append("A purchasing request for Illumina iScan chips has been submitted by the " + 
        labName +
    ".");
    emailBody.append("<br><br><table border='0' width = '600'><tr><td>Lab:</td><td>" + labName );
    emailBody.append("</td></tr><tr><td>Account Name:</td><td>" + acctName );
    emailBody.append("</td></tr><tr><td>Account Number:</td><td>" + acctNumber );
    emailBody.append("</td></tr><tr><td>Chip Type:</td><td>" + chipName );
    emailBody.append("</td></tr><tr><td>Catalog Number:</td><td>" + catNumber );
    emailBody.append("</td></tr><tr><td>Number of Chips Requested:</td><td>" + numberChips );
    emailBody.append("</td></tr><tr><td>Total Estimated Charges:</td><td>" + "$" + estimatedCost);
    emailBody.append("</td></tr><tr><td>Quote Number:</td><td>" + quoteNum);

    emailBody.append("</td></tr></table><br><br>");

    String subject = "Purchasing Request for iScan Chips";

    String contactEmailCoreFacility = PropertyDictionaryHelper.getInstance(sess).getCoreFacilityProperty(req.getIdCoreFacility(), PropertyDictionary.CONTACT_EMAIL_CORE_FACILITY);
    String contactEmailPurchasing  = PropertyDictionaryHelper.getInstance(sess).getCoreFacilityProperty(req.getIdCoreFacility(), PropertyDictionary.CONTACT_EMAIL_PURCHASING);

    String senderEmail = contactEmailCoreFacility;

    String contactEmail = contactEmailPurchasing;
    String ccEmail = null;


    String emailInfo = "";
    boolean send = false;

    if(contactEmail.contains(",")){
      for(String e: contactEmail.split(",")){
        if(!MailUtil.isValidEmail(e.trim())){
          System.out.println("Invalid email address: " + e);
        }
      }
    } else{
      if(!MailUtil.isValidEmail(contactEmail)){
        System.out.println("Invalid email address: " + contactEmail);
      }
    }
    if (dictionaryHelper.isProductionServer(serverName)) {
      send = true;
    } else {
      send = true;
      subject = subject + " (TEST)";
      contactEmail = dictionaryHelper.getPropertyDictionary(PropertyDictionary.CONTACT_EMAIL_SOFTWARE_TESTER);
      emailInfo = "[If this were a production environment then this email would have been sent to: " + contactEmailPurchasing + "]<br><br>";
      ccEmail = null;
    }    

    // Find requisition file
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
    String createYear = formatter.format(req.getCreateDate());
    String baseDir = PropertyDictionaryHelper.getInstance(sess).getExperimentDirectory(serverName, req.getIdCoreFacility());
    baseDir +=  "/" + createYear;
    String directoryName = baseDir + "/" + Request.getBaseRequestNumber(req.getNumber());
    directoryName += "/" + Constants.REQUISITION_DIR;
    File reqFolder = new File(directoryName);
    if ( !reqFolder.exists() ) {
      send = false;
    }
    
    if (send) {
      if(!MailUtil.isValidEmail(senderEmail)){
        senderEmail = DictionaryHelper.getInstance(sess).getPropertyDictionary(PropertyDictionary.GENERIC_NO_REPLY_EMAIL);
      }
      MailUtil.send_attach(contactEmail, 
          ccEmail,
          senderEmail, 
          subject, 
          emailInfo + emailBody.toString(),
          true,
          reqFolder);      
    }
     return send;
  }
}
