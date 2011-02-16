package hci.gnomex.controller;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.Request;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateGuestSession;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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

public class UploadExperimentFileServlet extends HttpServlet {
  
  private Integer idRequest = null;
  private String  requestNumber = null;
  private String  directoryName = "";
  
  private Request request;
  private String   fileName;

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
    try {
      Session sess = HibernateGuestSession.currentGuestSession(req.getUserPrincipal().getName());
      
      // Get the dictionary helper
      DictionaryHelper dh = DictionaryHelper.getInstance(sess);
      
      // Get security advisor
      SecurityAdvisor secAdvisor = (SecurityAdvisor) req.getSession().getAttribute(SecurityAdvisor.SECURITY_ADVISOR_SESSION_KEY);
      if (secAdvisor == null) {
        System.out.println("UploadExperimentFileServlet:  Warning - unable to find existing session. Creating security advisor.");
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
        System.out.println("UploadExperimentFileServlet: Error - Unable to find or create security advisor.");
        throw new ServletException("Unable to upload request file.  Servlet unable to obtain security information. Please contact GNomEx support.");
      }
      

      res.setContentType("text/html");
      PrintWriter out = res.getWriter();
      res.setHeader("Cache-Control", "max-age=0, must-revalidate");
      DecimalFormat sizeFormatter = new DecimalFormat("###,###,###,####,###");
      
      Element body = null;
      
      org.dom4j.io.OutputFormat format = null;
      org.dom4j.io.HTMLWriter writer = null;
      Document doc = null;
      String baseURL = "";
      
      if (requestNumber != null) {
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
      }

            
      MultipartParser mp = new MultipartParser(req, Integer.MAX_VALUE); 
      Part part;
      while ((part = mp.readNextPart()) != null) {
        String name = part.getName();
        if (part.isParam()) {
          // it's a parameter part
          ParamPart paramPart = (ParamPart) part;
          String value = paramPart.getStringValue();
          if (name.equals("idRequest")) {
            idRequest = new Integer((String)value);
            break;
          }
          if (name.equals("requestNumber")) {
            requestNumber = (String)value;
            if (body != null) {
              Element h3 = body.addElement("H3");
              h3.addCDATA("Upload experiment files for " + request.getNumber());              
            }
            break;
          }
        }
      }
      
      if (idRequest != null) {
        
        request = (Request)sess.get(Request.class, idRequest);
      } else if (requestNumber != null) {
        List requestList = sess.createQuery("SELECT r from Request r WHERE r.number = '" + requestNumber + "'").list();
        if (requestList.size() == 1) {
          request = (Request)requestList.get(0);
        }
      }
      
      if (request != null) {
        if (secAdvisor.canUpdate(request)) {
          SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
          String createYear = formatter.format(request.getCreateDate());

          String baseDir = dh.getMicroarrayDirectoryForWriting(req.getServerName()) + "/" + createYear;
          if (!new File(baseDir).exists()) {
            boolean success = (new File(baseDir)).mkdir();
            if (!success) {
              System.out.println("UploadExperimentFileServlet: Unable to create base directory " + baseDir);      
            }      
          }
          
          directoryName = baseDir + "/" + request.getNumber();
          if (!new File(directoryName).exists()) {
            boolean success = (new File(directoryName)).mkdir();
            if (!success) {
              System.out.println("UploadExperimentFileServlet: Unable to create directory " + directoryName);      
            }      
          }
          
          directoryName += "/" + Constants.UPLOAD_STAGING_DIR;
          if (!new File(directoryName).exists()) {
            boolean success = (new File(directoryName)).mkdir();
            if (!success) {
              System.out.println("UploadExperimentFileServlet: Unable to create directory " + directoryName);      
            }      
          }
          
          while ((part = mp.readNextPart()) != null) {        
            if (part.isFile()) {
              // it's a file part
              FilePart filePart = (FilePart) part;
              fileName = filePart.getFileName();
              if (fileName != null) {

                // the part actually contained a file
                long size = filePart.writeTo(new File(directoryName));

                if (requestNumber != null && body != null) {
                  body.addElement("BR");
                  body.addCDATA(fileName + "   -   successfully uploaded " + sizeFormatter.format(size) + " bytes.");                  
                }
              }
              else { 
              }
              out.flush();

            }
          }
          
          if (requestNumber != null && doc != null && writer != null) {
            body.addElement("BR");
            Element h5 = body.addElement("H5");
            h5.addCDATA("In GNomEx, click refresh button on 'Organize' tab to see list of uploaded files.");    
            
            writer.write(doc);
            writer.flush();
            
            writer.close(); 
          }
          
        } else {
          System.out.println("UploadExperimentFileServlet - unable to upload file " + fileName + " for request idRequest=" + idRequest);
          System.out.println("Insufficient write permissions for user " + secAdvisor.getUserLastName() + ", " + secAdvisor.getUserFirstName());
          throw new ServletException("Unable to upload file " + fileName + " due to a server error.  Please contact GNomEx support.");
          
        }
        
      } else {
        System.out.println("UploadExperimentFileServlet - unable to upload file " + fileName + " for request idRequest=" + idRequest);
        System.out.println("idRequest is required");
        throw new ServletException("Unable to upload file " + fileName + " due to a server error.  Please contact GNomEx support.");
        
      }
      

      
      
    } catch (Exception e) {
      System.out.println("UploadExperimentFileServlet - unable to upload file " + fileName + " for request idRequest=" + idRequest);
      System.out.println(e.toString());
      e.printStackTrace();
      throw new ServletException("Unable to upload file " + fileName + " due to a server error.  Please contact GNomEx support.");
    }  finally {
      try {
        HibernateGuestSession.closeGuestSession();        
      } catch (Exception e1) {
        System.out.println("UploadExperimentFileServlet warning - cannot close hibernate session");
      }
    } 
    
  }
}
