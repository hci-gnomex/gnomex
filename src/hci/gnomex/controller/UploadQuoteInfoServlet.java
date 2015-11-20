package hci.gnomex.controller;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.CoreFacility;
import hci.gnomex.model.InternalAccountFieldsConfiguration;
import hci.gnomex.model.Lab;
import hci.gnomex.model.Product;
import hci.gnomex.model.ProductLineItem;
import hci.gnomex.model.ProductOrder;
import hci.gnomex.model.ProductOrderFile;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.TransferLog;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.MailUtil;
import hci.gnomex.utility.MailUtilHelper;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;

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

  private String  orderUuid = null;
  private String  quoteNumber = null;
  private String  directoryName = "";

  private ProductOrder productOrder;
  private String  fileName;

  private String  serverName;
  private String appURL;

  protected void doGet( HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException {
  }


  protected void doPost( HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException {
    try {
      serverName = req.getServerName();

      String requestURL = req.getRequestURL().toString();
      appURL = requestURL.substring(0, requestURL.indexOf("/gnomex"));
      appURL += ":" + req.getServerPort() + "/gnomex";

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
          if (name.equals("orderUuid")) {
            orderUuid = value;
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

      // Get the product order
      if (orderUuid != null) {
        String queryString = "SELECT po from ProductOrder po WHERE po.uuid = :orderUuid";
        Query poQuery = sess.createQuery( queryString );
        poQuery.setParameter( "orderUuid", orderUuid );
        productOrder = (ProductOrder)poQuery.uniqueResult();
      }

      if (productOrder != null) {

        if (quoteNumber != null && !quoteNumber.equals("")) {
          productOrder.setQuoteNumber( quoteNumber );
          productOrder.setQuoteReceivedDate( new java.sql.Date(System.currentTimeMillis()) );
          productOrder.setUuid( null );
          sess.save( productOrder );
          sess.flush();
        }

        if (productOrder.getQuoteNumber() != null && !productOrder.getQuoteNumber().equals( "" )) {
          // Send email to purchasing
          try {
            if ( sendPurchasingEmail(sess, productOrder, serverName) ) {
              body.addElement("BR");
              body.addCDATA("Email sent to purchasing department.");
            }
          } catch (Exception e) {
            String msg = "Unable to send email to purchasing regarding Illumina iScan Chip(s) for Product Order "
              + productOrder.getIdProductOrder()
              + ".  " + e.toString();
            System.out.println(msg);
            e.printStackTrace();
            Element h3 = body.addElement("H3");
            h3.addCDATA("Warning: Email not sent to purchasing department.");
          }
        }

        // Make sure correct directories are in place or create them.
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        String createYear = formatter.format(productOrder.getSubmitDate());

        String baseDir = PropertyDictionaryHelper.getInstance(sess).getProductOrderDirectory(req.getServerName(), productOrder.getIdCoreFacility());
        baseDir +=  "/" + createYear;
        if (!new File(baseDir).exists()) {
          boolean success = (new File(baseDir)).mkdir();
          if (!success) {
            System.out.println("UploadQuoteInfoServlet: Unable to create base directory " + baseDir);
          }
        }

        directoryName = baseDir + "/" + productOrder.getIdProductOrder();
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
              xferLog.setIdLab(productOrder.getIdLab());
              xferLog.setFileName(productOrder.getIdProductOrder() + "/" + fileName);

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

        File uploadedFile = new File(directoryName + File.pathSeparator + fileName);
        if(uploadedFile.exists()) {
          ProductOrderFile poFile = new ProductOrderFile();
          poFile.setIdProductOrder(productOrder.getIdProductOrder());
          poFile.setCreateDate(new Date(System.currentTimeMillis()));
          poFile.setFileName(directoryName + File.pathSeparator + fileName);
          poFile.setFileSize(new BigDecimal(uploadedFile.length()));
          sess.save(poFile);
        }


        if (doc != null && writer != null) {
          writer.write(doc);
          writer.flush();
          writer.close();
        }

      } else {
        System.out.println("UploadQuoteInfoServlet - unable to upload quote information for request requestNumber=" + productOrder.getIdProductOrder());
        System.out.println("valid requestNumber is required");
        throw new ServletException("Unable to upload quote information due to a server error.  Please contact GNomEx support.");
      }

    } catch (Exception e) {
      HibernateSession.rollback();
      System.out.println("UploadQuoteInfoServlet - unable to upload quote information for product order " + productOrder.getIdProductOrder());
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

  public static Boolean sendPurchasingEmail(Session sess, ProductOrder po, String serverName) throws NamingException, MessagingException, IOException {

    //workaround until NullPointer exception is dealt with
    InternalAccountFieldsConfiguration.getConfiguration(sess);

    DictionaryHelper dictionaryHelper = DictionaryHelper.getInstance(sess);
    //IScanChip chip = (IScanChip) sess.get( IScanChip.class, req.getIdIScanChip() );
    Lab lab = (Lab) sess.get( Lab.class, po.getIdLab() );
    BillingAccount acct = (BillingAccount) sess.get( BillingAccount.class, po.getIdBillingAccount() );
    AppUser user = (AppUser) sess.get(AppUser.class, po.getIdAppUser());

    if ( po == null || po.getProductLineItems().size() == 0 || acct == null ) {
      throw new MessagingException( "Request or Acct is null" );
    }

    String labName = lab.getName(false, true)!=null ? lab.getName(false, true) : "";
    String submitterName = user.getFirstLastDisplayName()!=null ? user.getFirstLastDisplayName() : "";
    String acctName = acct.getAccountName() != null ? acct.getAccountName() : "";
    String acctNumber = acct.getAccountNumber() != null ? acct.getAccountNumber() : "";
    String quoteNum = po.getQuoteNumber() != null ? po.getQuoteNumber() : "";

    BigDecimal grandTotal = new BigDecimal( BigInteger.ZERO, 2 ) ;

    // Email to purchasing department
    StringBuffer emailBody = new StringBuffer();

    emailBody.append("A purchasing request for Illumina iScan chips has been submitted by the " +
        labName +
    ".");

    // Email for the lab PI and Billing contact requesting signature on req form
    StringBuffer emailBodyForLab = new StringBuffer();

    emailBodyForLab.append("A purchasing request for Illumina iScan chips has been submitted by " +
        submitterName +
    ".");

    for(Iterator i = po.getProductLineItems().iterator(); i.hasNext();) {
      ProductLineItem lineItem = (ProductLineItem) i.next();
      Product product = (Product)sess.load(Product.class, lineItem.getIdProduct());

      String numberChips = lineItem.getQty() != null ? lineItem.getQty().toString() : "";
      String chipName = product.getName() != null ? product.getName() : "";
      String catNumber = product.getCatalogNumber() != null ? product.getCatalogNumber() : "";

      BigDecimal estimatedCost = new BigDecimal( BigInteger.ZERO, 2 ) ;
      estimatedCost = lineItem.getUnitPrice().multiply(new BigDecimal(lineItem.getQty()));

      grandTotal.add(estimatedCost);

      emailBody.append("<br><br><table border='0' width = '600'><tr><td>Lab:</td><td>" + labName );
      emailBody.append("</td></tr><tr><td>Account Name:</td><td>" + acctName );
      emailBody.append("</td></tr><tr><td>Account Number:</td><td>" + acctNumber );
      emailBody.append("</td></tr><tr><td>Chip Type:</td><td>" + chipName );
      emailBody.append("</td></tr><tr><td>Catalog Number:</td><td>" + catNumber );
      emailBody.append("</td></tr><tr><td>Number of Chips Requested:</td><td>" + numberChips );
      emailBody.append("</td></tr><tr><td>Total Estimated Charges:</td><td>" + "$" + estimatedCost);
      emailBody.append("</td></tr><tr><td>Quote Number:</td><td>" + quoteNum);

      emailBody.append("</td></tr></table><br><br>");


      emailBodyForLab.append("<br><br><table border='0' width = '600'><tr><td>Lab:</td><td>" + labName );
      emailBodyForLab.append("</td></tr><tr><td>Account Name:</td><td>" + acctName );
      emailBodyForLab.append("</td></tr><tr><td>Account Number:</td><td>" + acctNumber );
      emailBodyForLab.append("</td></tr><tr><td>Chip Type:</td><td>" + chipName );
      emailBodyForLab.append("</td></tr><tr><td>Catalog Number:</td><td>" + catNumber );
      emailBodyForLab.append("</td></tr><tr><td>Number of Chips Requested:</td><td>" + numberChips );
      emailBodyForLab.append("</td></tr><tr><td>Total Estimated Charges:</td><td>" + "$" + estimatedCost);
      emailBodyForLab.append("</td></tr><tr><td>Quote Number:</td><td>" + quoteNum);

      emailBodyForLab.append("</td></tr></table><br><br>");
      emailBodyForLab.append("</td></tr></table>");


    }

    emailBody.append("Grand Total:  " + grandTotal);
    emailBodyForLab.append("Grand Total:  " + grandTotal);

    emailBodyForLab.append("<b><FONT COLOR=\"#ff0000\">Please sign the attached requisition form and send it to the purchasing department.</FONT></b>");

    String subject = "Purchasing Request for iScan Chips";

    String contactEmailCoreFacility = ((CoreFacility)sess.load(CoreFacility.class, po.getIdCoreFacility())).getContactEmail();
    String contactEmailPurchasing  = PropertyDictionaryHelper.getInstance(sess).getCoreFacilityProperty(po.getIdCoreFacility(), PropertyDictionary.CONTACT_EMAIL_PURCHASING);
    String contactEmailLabBilling = lab.getBillingContactEmail() != null ? lab.getBillingContactEmail() : "";
    String contactEmailLabPI = lab.getContactEmail() != null ? lab.getContactEmail() : "";

    String senderEmail = contactEmailCoreFacility;
    String contactEmail = contactEmailPurchasing;
    String ccEmail = null;

    boolean send = true;

    // Check that purchasing email is valid
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

    // Find requisition file
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
    String createYear = formatter.format(po.getSubmitDate());
    String baseDir = PropertyDictionaryHelper.getInstance(sess).getExperimentDirectory(serverName, po.getIdCoreFacility());
    baseDir +=  "/" + createYear;
    baseDir = baseDir + "/" + po.getIdProductOrder();
    String directoryName = baseDir + "/" + Constants.REQUISITION_DIR;
    File reqFolder = new File(directoryName);
    if ( !reqFolder.exists() ) {
      send = false;
    }

    // Send email to purchasing
    if (send) {
      if(!MailUtil.isValidEmail(senderEmail)){
        senderEmail = DictionaryHelper.getInstance(sess).getPropertyDictionary(PropertyDictionary.GENERIC_NO_REPLY_EMAIL);
      }
      MailUtilHelper helper = new MailUtilHelper(contactEmail, ccEmail, null, senderEmail, subject, emailBody.toString(), new File(baseDir), true, dictionaryHelper, serverName);
      MailUtil.validateAndSendEmail(helper);
    }

    // Now send the email to lab billing contact and PI
    contactEmail = contactEmailLabPI;
    ccEmail = contactEmailLabBilling;

    // Check billing contact email
    if(contactEmail.contains(",")){
      for(String e: contactEmail.split(",")){
        if(!MailUtil.isValidEmail(e.trim())){
          System.out.println("Invalid email address for lab: " + e);
        }
      }
    } else{
      if(!MailUtil.isValidEmail(contactEmail)){
        System.out.println("Invalid email address for lab: " + contactEmail);
      }
    }

    // Check PI email
    if(ccEmail.contains(",")){
      for(String e: ccEmail.split(",")){
        if(!MailUtil.isValidEmail(e.trim())){
          ccEmail = null;
        }
      }
    } else{
      if(!MailUtil.isValidEmail(ccEmail)){
        ccEmail = null;
      }
    }
    if ( contactEmail.equals("") && ccEmail != null ) {
      contactEmail = ccEmail;
      ccEmail = null;
    }
    if (send && !contactEmail.equals( "" )) {
    	MailUtilHelper helper = new MailUtilHelper(contactEmail, ccEmail, null, senderEmail, subject, emailBodyForLab.toString(), reqFolder, true, dictionaryHelper, serverName);
    	MailUtil.validateAndSendEmail(helper);
    }

    return send;
  }
}
