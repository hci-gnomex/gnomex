package hci.gnomex.controller;

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
            if ( ProductOrder.sendPurchasingEmail(sess, productOrder, serverName) ) {
              body.addElement("BR");
              body.addCDATA("Email sent to purchasing department.");
            }
          } catch (Exception e) {
            String msg = "Unable to send email to purchasing regarding Product Order "
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

              String baseDirectory = PropertyDictionaryHelper.getInstance(sess).getProductOrderDirectory(serverName, productOrder.getIdCoreFacility()) +
                  productOrder.getCreateYear() + "/" + productOrder.getIdProductOrder();
              String qualDir =  "/" +  Constants.MATERIAL_QUOTE_DIR;
              ProductOrderFile poFile = new ProductOrderFile();
              poFile.setIdProductOrder(productOrder.getIdProductOrder());
              poFile.setCreateDate(new Date(System.currentTimeMillis()));
              poFile.setFileName(fileName);
              poFile.setFileSize(new BigDecimal(size));
              poFile.setBaseFilePath(baseDirectory);
              poFile.setQualifiedFilePath(qualDir);
              sess.save(poFile);
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
        System.out.println("UploadQuoteInfoServlet - unable to upload quote information for ProductOrder " + productOrder.getIdProductOrder());
        System.out.println("valid idProductOrder is required");
        throw new ServletException("Unable to upload quote information due to a server error.  Please contact GNomEx support.");
      }

    } catch (Exception e) {
      HibernateSession.rollback();
      System.out.println("UploadQuoteInfoServlet - unable to upload quote information for ProductOrder " + productOrder.getIdProductOrder());
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

}
