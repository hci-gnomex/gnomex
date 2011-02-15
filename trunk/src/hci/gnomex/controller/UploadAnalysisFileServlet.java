package hci.gnomex.controller;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.Analysis;
import hci.gnomex.model.AnalysisFile;
import hci.gnomex.model.Property;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
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

public class UploadAnalysisFileServlet extends HttpServlet {
  
  private Integer idAnalysis = null;
  private String  analysisNumber = null;
  private String  directoryName = "";
  
  private Analysis analysis;
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
      Session sess = HibernateSession.currentSession(req.getUserPrincipal().getName());
      
      // Get the dictionary helper
      DictionaryHelper dh = DictionaryHelper.getInstance(sess);
      
      // Get security advisor
      SecurityAdvisor secAdvisor = (SecurityAdvisor) req.getSession().getAttribute(SecurityAdvisor.SECURITY_ADVISOR_SESSION_KEY);
      if (secAdvisor == null) {
        System.out.println("UploadAnalysisFileServlet:  Warning - unable to find existing session. Creating security advisor.");
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
        System.out.println("UploadAnalysisFileServlet: Error - Unable to find or create security advisor.");
        throw new ServletException("Unable to upload analysis file.  Servlet unable to obtain security information. Please contact GNomEx support.");
      }
      
      DecimalFormat sizeFormatter = new DecimalFormat("###,###,###,####,###");


      res.setContentType("text/html");
      PrintWriter out = res.getWriter();
      res.setHeader("Cache-Control", "max-age=0, must-revalidate");
      
      org.dom4j.io.OutputFormat format = null;
      org.dom4j.io.HTMLWriter writer = null;
      Document doc = null;
      String baseURL = "";
      Element body = null;
      
      if (analysisNumber != null) {
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
          if (name.equals("idAnalysis")) {
            idAnalysis = new Integer((String)value);
            break;
          }
          if (name.equals("analysisNumber")) {
            analysisNumber = (String)value;
            if (body != null) {
              Element h3 = body.addElement("H3");
              h3.addCDATA("Upload analysis files for " + analysisNumber);              
            }
            break;
          }
        }
      }
      
      if (idAnalysis != null) {
        
        analysis = (Analysis)sess.get(Analysis.class, idAnalysis);
      } else if (analysisNumber != null) {
        List analysisList = sess.createQuery("SELECT a from Analysis a WHERE a.number = '" + analysisNumber + "'").list();
        if (analysisList.size() == 1) {
          analysis = (Analysis)analysisList.get(0);
        }
      }
      
      if (analysis != null) {
        if (secAdvisor.canUpdate(analysis)) {
          SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
          String createYear = formatter.format(analysis.getCreateDate());

          String baseDir = dh.getAnalysisDirectory(req.getServerName()) + "/" + createYear;
          if (!new File(baseDir).exists()) {
            boolean success = (new File(baseDir)).mkdir();
            if (!success) {
              System.out.println("UploadAnalysisFileServlet: Unable to create base directory " + baseDir);      
            }      
          }
          
          directoryName = baseDir + "/" + analysis.getNumber();
          if (!new File(directoryName).exists()) {
            boolean success = (new File(directoryName)).mkdir();
            if (!success) {
              System.out.println("UploadAnalysisFileServlet: Unable to create directory " + directoryName);      
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
                
                if (analysisNumber != null && body != null) {
                  body.addElement("BR");
                  body.addCDATA(fileName + "   -   successfully uploaded " + sizeFormatter.format(size) + " bytes.");                  
                }
                
                // Save analysis file (name) in db
                AnalysisFile analysisFile = null;
                for(Iterator i = analysis.getFiles().iterator(); i.hasNext();) {
                  AnalysisFile af = (AnalysisFile)i.next();
                  if (af.getFileName().equals(fileName)) {
                    analysisFile = af;
                    break;
                  }
                }
                if (analysisFile == null) {
                  analysisFile = new AnalysisFile();
                  analysisFile.setIdAnalysis(analysis.getIdAnalysis());
                  analysisFile.setFileName(fileName);
                }
                analysisFile.setUploadDate(new java.sql.Date(System.currentTimeMillis()));
                sess.save(analysisFile);
                
              }
              else { 
              }
              out.flush();
            }
          }
          sess.flush();
          
        } else {
          System.out.println("UploadAnalysisFileServlet - unable to upload file " + fileName + " for analysis idAnalysis=" + idAnalysis);
          System.out.println("Insufficient write permissions for user " + secAdvisor.getUserLastName() + ", " + secAdvisor.getUserFirstName());
          throw new ServletException("Unable to upload file " + fileName + " due to a server error.  Please contact GNomEx support.");
          
        }
        
      } else {
        System.out.println("UploadAnalysisFileServlet - unable to upload file " + fileName + " for analysis idAnalysis=" + idAnalysis);
        System.out.println("idAnalysis is required");
        throw new ServletException("Unable to upload file " + fileName + " due to a server error.  Please contact GNomEx support.");
        
      }
      
      if (analysisNumber != null && doc != null && writer != null && doc != null && body != null) {
        body.addElement("BR");
        Element h5 = body.addElement("H5");
        h5.addCDATA("In GNomEx, click refresh button to see uploaded analysis files.");    
        
        writer.write(doc);
        writer.flush();
        
        writer.close(); 
      }

      
      
    } catch (Exception e) {
      System.out.println("UploadAnalysisFileServlet - unable to upload file " + fileName + " for analysis idAnalysis=" + idAnalysis);
      System.out.println(e.toString());
      e.printStackTrace();
      throw new ServletException("Unable to upload file " + fileName + " due to a server error.  Please contact GNomEx support.");
    }  finally {
      try {
        HibernateSession.closeSession();        
      } catch (Exception e1) {
        System.out.println("UploadAnalysisFileServlet warning - cannot close hibernate session");
      }
    } 
    
  }
}
