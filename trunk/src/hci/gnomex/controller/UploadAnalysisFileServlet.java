package hci.gnomex.controller;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.Analysis;
import hci.gnomex.model.AnalysisExperimentItem;
import hci.gnomex.model.AnalysisFile;
import hci.gnomex.security.SecurityAdvisor;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

import com.oreilly.servlet.multipart.FilePart;
import com.oreilly.servlet.multipart.MultipartParser;
import com.oreilly.servlet.multipart.ParamPart;
import com.oreilly.servlet.multipart.Part;

public class UploadAnalysisFileServlet extends HttpServlet {
  
  private Integer idAnalysis = null;
  private String  directoryName = "";
  
  private Analysis analysis;
  private String   fileName;

  protected void doGet( HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException {;}

  protected void doPost( HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException {
    try {
      // Get security advisor
      SecurityAdvisor secAdvisor = (SecurityAdvisor) req.getSession().getAttribute(SecurityAdvisor.SECURITY_ADVISOR_SESSION_KEY);

      PrintWriter out = res.getWriter();
            
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
        }
      }
      
      if (idAnalysis != null) {
        Session sess = secAdvisor.getHibernateSession(req.getUserPrincipal().getName());
        
        analysis = (Analysis)sess.get(Analysis.class, idAnalysis);
        if (secAdvisor.hasPermission(SecurityAdvisor.CAN_ADMINISTER_USERS)) {
          SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
          String createYear = formatter.format(analysis.getCreateDate());

          String baseDir = Constants.getAnalysisDirectory(req.getServerName());
          
          directoryName = baseDir + createYear + "\\" + analysis.getNumber();
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
          System.out.println("Insufficient write permissions for user " + secAdvisor.getUserLastName() + ", " + secAdvisor.getUserLastName());
          throw new ServletException("Unable to upload file " + fileName + " due to a server error.  Please contact GNomEx support.");
          
        }
        
      } else {
        System.out.println("UploadAnalysisFileServlet - unable to upload file " + fileName + " for analysis idAnalysis=" + idAnalysis);
        System.out.println("idAnalysis is required");
        throw new ServletException("Unable to upload file " + fileName + " due to a server error.  Please contact GNomEx support.");
        
      }
      
      
    } catch (Exception e) {
      System.out.println("UploadAnalysisFileServlet - unable to upload file " + fileName + " for analysis idAnalysis=" + idAnalysis);
      System.out.println(e.toString());
      e.printStackTrace();
      throw new ServletException("Unable to upload file " + fileName + " due to a server error.  Please contact GNomEx support.");
    }
  }
}
