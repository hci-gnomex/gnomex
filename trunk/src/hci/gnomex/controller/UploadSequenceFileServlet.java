package hci.gnomex.controller;

import hci.gnomex.model.GenomeBuild;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DataTrackUtil;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.oreilly.servlet.multipart.FilePart;
import com.oreilly.servlet.multipart.MultipartParser;
import com.oreilly.servlet.multipart.ParamPart;
import com.oreilly.servlet.multipart.Part;

public class UploadSequenceFileServlet extends HttpServlet {
  


  private String fileName = null;
  private  StringBuffer bypassedFiles = new StringBuffer();
  private File tempBulkUploadFile = null;

  private SecurityAdvisor secAdvisor = null;

  private String serverName;
  private String baseDir;
  

  protected void doGet( HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException {
  }

  /*
   * SPECIAL NOTE -  This servlet must be run on non-secure socket layer (http) in order to
   *                 keep track of previously created session. (see note below concerning
   *                 flex upload bug on Safari and FireFox).  Otherwise, session is 
   *                 not maintained.  Although the code tries to work around this
   *                 problem by creating a new security advisor if one is not found,
   *                 the Safari browser cannot handle authenicating the user (this second time).
   *                 So for now, this servlet must be run non-secure. 
   */
  protected void doPost( HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException {
    Session sess = null;
    Transaction tx = null;
    
    serverName = req.getServerName();
    
    Integer idGenomeBuild = null;
    GenomeBuild genomeBuild = null;
    String fileName = null;

    try {
      sess = HibernateSession.currentSession(req.getUserPrincipal().getName());
      tx = sess.beginTransaction();
      tx.begin();
      
      baseDir = PropertyDictionaryHelper.getInstance(sess).getDataTrackReadDirectory(serverName);
     
      // Get the dictionary helper
      DictionaryHelper dh = DictionaryHelper.getInstance(sess);
      
      // Get security advisor
      secAdvisor = (SecurityAdvisor) req.getSession().getAttribute(SecurityAdvisor.SECURITY_ADVISOR_SESSION_KEY);
      if (secAdvisor == null) {
        System.out.println("UploadSequenceFileServlet:  Warning - unable to find existing session. Creating security advisor.");
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
        System.out.println("UploadSequenceFileServlet: Error - Unable to find or create security advisor.");
        throw new ServletException("Unable to upload analysis file.  Servlet unable to obtain security information. Please contact GNomEx support.");
      }
      
      if (secAdvisor.getIsGuest().equals("Y")) {
        throw new Exception("Insufficient permissions to upload data.");
      }

      res.setDateHeader("Expires", -1);
      res.setDateHeader("Last-Modified", System.currentTimeMillis());
      res.setHeader("Pragma", "");
      res.setHeader("Cache-Control", "");


      MultipartParser mp = new MultipartParser(req, Integer.MAX_VALUE); 
      Part part;
      while ((part = mp.readNextPart()) != null) {
        String name = part.getName();
        if (part.isParam()) {
          // it's a parameter part
          ParamPart paramPart = (ParamPart) part;
          String value = paramPart.getStringValue();
          if (name.equals("idGenomeBuild")) {
            idGenomeBuild = new Integer(String.class.cast(value));
          } 
        }

        if (idGenomeBuild != null) {
          break;
        }

      }

      if (idGenomeBuild != null) {
        genomeBuild = (GenomeBuild)sess.get(GenomeBuild.class, idGenomeBuild);
      } 
      if (genomeBuild != null) {
        if (this.secAdvisor.hasPermission(SecurityAdvisor.CAN_WRITE_DICTIONARIES)) {

          // Make sure that the data root dir exists
          if (!new File(baseDir).exists()) {
            boolean success = (new File(baseDir)).mkdir();
            if (!success) {
              throw new Exception("Unable to create directory " + baseDir);      
            }
          }

          String sequenceDir = genomeBuild.getSequenceDirectory(baseDir);

          // Create sequence directory if it doesn't exist
          if (!new File(sequenceDir).exists()) {
            boolean success = (new File(sequenceDir)).mkdir();
            if (!success) {
              throw new Exception("Unable to create directory " + sequenceDir);      
            }      
          }

          while ((part = mp.readNextPart()) != null) {        
            if (part.isFile()) {
              // it's a file part
              FilePart filePart = (FilePart) part;
              fileName = filePart.getFileName();
              if (fileName != null) {

                // Is the fileName valid?
                if (!DataTrackUtil.isValidSequenceFileType(fileName)) {
                  throw new Exception("Bypassing upload of sequence files for  " + genomeBuild.getDas2Name() + " for file" + fileName + ". Unsupported file extension");
                }

                // Write the file
                long size = filePart.writeTo(new File(sequenceDir));

              }
              else { 
              }
            }
          }
          sess.flush();
        }
      }
      tx.commit();
      
      Document doc = DocumentHelper.createDocument();
      Element root = doc.addElement("SUCCESS");
      root.addAttribute("idGenomeBuild", idGenomeBuild.toString());
      XMLWriter writer = new XMLWriter(res.getOutputStream(), OutputFormat.createCompactFormat());
      writer.write(doc);

    } catch (Exception e) {
      Logger.getLogger(this.getClass().getName()).warning(e.getMessage());
      e.printStackTrace();
      // TODO: Figure out auto-commit setting that prevents rollback
      // not sure we want to rollback anyways....
      //tx.rollback();
      sess.flush();
      tx.commit();
      res.addHeader("message", e.getMessage());
      Document doc = DocumentHelper.createDocument();
      Element root = doc.addElement("ERROR");
      root.addAttribute("message", e.getMessage());
      XMLWriter writer = new XMLWriter(res.getOutputStream(), OutputFormat.createCompactFormat());
      writer.write(doc);  
      
      
    } finally {
      if (tempBulkUploadFile != null && tempBulkUploadFile.exists()) tempBulkUploadFile.delete();
      if (sess != null) {
        try {
          HibernateSession.closeSession();          
        } catch (Exception e) {
        }
      }
      res.setHeader("Cache-Control", "max-age=0, must-revalidate");

    }
  }
  

}
