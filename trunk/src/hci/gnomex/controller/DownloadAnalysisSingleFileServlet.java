package hci.gnomex.controller;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.Analysis;
import hci.gnomex.model.TransferLog;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.AnalysisFileDescriptor;
import hci.gnomex.utility.FileDescriptor;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;


public class DownloadAnalysisSingleFileServlet extends HttpServlet { 

  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DownloadAnalysisSingleFileServlet.class);
  
  private String                          baseDir = null;
  private Integer                         idAnalysis = null;
  private String                          fileName = null;
  private String                          dir = null;
  private String                          view = "N";
  
  
  public void init() {
  
  }
    
  protected void doGet(HttpServletRequest req, HttpServletResponse response)
      throws ServletException, IOException {
    
    baseDir = null;
    idAnalysis = null;
    fileName = null;
    dir = null;
    
    // restrict commands to local host if request is not secure
    if (Constants.REQUIRE_SECURE_REMOTE && !req.isSecure()) {
      if (req.getRemoteAddr().equals(InetAddress.getLocalHost().getHostAddress())
          || req.getRemoteAddr().equals("127.0.0.1")
          || InetAddress.getByName(req.getRemoteAddr()).isLoopbackAddress()) {
        log.debug("Requested from local host");
      }
      else {
        log.error("Accessing secure command over non-secure line from remote host is not allowed");
        
        response.setContentType("text/html");
        response.getOutputStream().println(
            "<html><head><title>Error</title></head>");
        response.getOutputStream().println("<body><b>");
        response.getOutputStream().println(
            "Secure connection is required. Prefix your request with 'https: "
                + "<br>");
        response.getOutputStream().println("</body>");
        response.getOutputStream().println("</html>");
        return;
      }
    }

    // Get the idAnalysis parameter
    if (req.getParameter("idAnalysis") != null && !req.getParameter("idAnalysis").equals("")) {
      idAnalysis = new Integer(req.getParameter("idAnalysis"));
    }
    // Get the fileName parameter
    if (req.getParameter("fileName") != null && !req.getParameter("fileName").equals("")) {
      fileName = req.getParameter("fileName");
    }
    
    // Get the dir parameter
    if (req.getParameter("dir") != null && !req.getParameter("dir").equals("")) {
      dir = req.getParameter("dir");
    } 
    // Get the view flag
    if (req.getParameter("view") != null && !req.getParameter("view").equals("")) {
      view = req.getParameter("view");
    } 
    if (idAnalysis == null || fileName == null) {
      log.error("idAnalysis and fileName required");
      
      response.setContentType("text/html");
      response.getOutputStream().println(
          "<html><head><title>Error</title></head>");
      response.getOutputStream().println("<body><b>");
      response.getOutputStream().println(
          "Missing parameters:  idAnalysis and fileName required"
              + "<br>");
      response.getOutputStream().println("</body>");
      response.getOutputStream().println("</html>");
      return;
      
    }

    InputStream in = null;
    SecurityAdvisor secAdvisor = null;
    try {
      

      // Get security advisor
     secAdvisor = (SecurityAdvisor) req.getSession().getAttribute(SecurityAdvisor.SECURITY_ADVISOR_SESSION_KEY);

      if (secAdvisor != null) {
        if (view.equals("Y")) {
          String mimeType = req.getSession().getServletContext().getMimeType(fileName);
          response.setContentType(mimeType);
        } else {
          response.setContentType("application/x-download");
          response.setHeader("Content-Disposition", "attachment;filename=" + fileName);          
          response.setHeader("Cache-Control", "max-age=0, must-revalidate");
        }
        
        Session sess = secAdvisor.getHibernateSession(req.getUserPrincipal() != null ? req.getUserPrincipal().getName() : "guest");

        
        baseDir = PropertyDictionaryHelper.getInstance(sess).getAnalysisDirectory(req.getServerName());
        
          
        Analysis analysis = (Analysis)sess.load(Analysis.class, idAnalysis);


        // If we can't find the analysis in the database, just bypass it.
        if (analysis == null) {
          throw new Exception("Cannot find analysis " + idAnalysis);
        }

        // Check permissions - bypass this analysis if the user 
        // does not have  permission to read it.
        if (!secAdvisor.canRead(analysis)) {  
          throw new Exception("Insufficient permissions to read request " + analysis.getNumber() + ".  Bypassing download.");
        }

        // Now get the files that exist on the file server for this analysis
        Map analysisMap = new TreeMap();
        Map directoryMap = new TreeMap();
        Map fileMap = new HashMap();
        List analysisNumbers = new ArrayList<String>();
        GetExpandedAnalysisFileList.getFileNamesToDownload(baseDir, analysis.getKey(), analysisNumbers, analysisMap, directoryMap, false);


        // Find the file matching the fileName passed in as a parameter
        AnalysisFileDescriptor analysisFd = null;
        List directoryKeys   = (List)analysisMap.get(analysis.getNumber());
        for(Iterator i1 = directoryKeys.iterator(); i1.hasNext();) {
          String directoryKey = (String)i1.next();
          String dirTokens[] = directoryKey.split("-");
          
          String theDirectory = ""; 
          if (dirTokens.length > 1) {
            theDirectory = dirTokens[1];
          } 
          
          List   theFiles     = (List)directoryMap.get(directoryKey);
          for(Iterator i2 = theFiles.iterator(); i2.hasNext();) {
            AnalysisFileDescriptor fd = (AnalysisFileDescriptor) i2.next();
            fd.setQualifiedFilePath(theDirectory);
            AnalysisFileDescriptor matchingFd = recurseGetMatchingFileDescriptor(fd, fileName, theDirectory);
            if (matchingFd != null) {
              analysisFd = matchingFd;
              break;
            }
          }
          if (analysisFd != null) {
            break;
          }
          
        }

        // If we found the analysis, download it
        if (analysisFd != null) {
          
          
          // Insert a transfer log entry
          TransferLog xferLog = new TransferLog();
          
          xferLog.setFileName(analysisFd.getDisplayName());
          
          xferLog.setStartDateTime(new java.util.Date(System.currentTimeMillis()));
          xferLog.setTransferType(TransferLog.TYPE_DOWNLOAD);
          xferLog.setTransferMethod(TransferLog.METHOD_HTTP);
          xferLog.setPerformCompression("Y");
          xferLog.setIdAnalysis(analysis.getIdAnalysis());
          xferLog.setIdLab(analysis.getIdLab());
          
          
          
          in = new FileInputStream(analysisFd.getFileName());
          OutputStream out = response.getOutputStream();
          byte b[] = new byte[102400];
          int numRead = 0;
          int size = 0;
          while (numRead != -1) {
            numRead = in.read(b);
            if (numRead != -1) {
              out.write(b, 0, numRead);                                    
              size += numRead;
            }
          }
          
          // Save transfer log 
          xferLog.setFileSize(new BigDecimal(size));
          xferLog.setEndDateTime(new java.util.Date(System.currentTimeMillis()));
          sess.save(xferLog);
          
          in.close();
          out.close();
          out.flush();
          in = null;
        }

        sess.flush();
        

      } else {
        response.setContentType("text/html");
        response.getOutputStream().println(
            "<html><head><title>Error</title></head>");
        response.getOutputStream().println("<body><b>");
        response.getOutputStream().println(
            "DownloadAnalyisSingleFileServlet: You must have a SecurityAdvisor in order to run this command."
                + "<br>");
        response.getOutputStream().println("</body>");
        response.getOutputStream().println("</html>");
        System.out.println( "DownloadAnalyisSingleFileServlet: You must have a SecurityAdvisor in order to run this command.");
      }
    } catch (Exception e) {
      response.setContentType("text/html");
      response.getOutputStream().println(
          "<html><head><title>Error</title></head>");
      response.getOutputStream().println("<body><b>");
      response.getOutputStream().println(
          "DownloadAnalyisSingleFileServlet: An exception occurred " + e.toString()
              + "<br>");
      response.getOutputStream().println("</body>");
      response.getOutputStream().println("</html>");
      System.out.println( "DownloadAnalyisSingleFileServlet: An exception occurred " + e.toString());
      e.printStackTrace();
    } finally {
      try {
        secAdvisor.closeHibernateSession();        
      } catch (Exception e) {
        
      }
      
      if (in != null) {
        in.close();
      }
    }

  }    
  
  
  private AnalysisFileDescriptor recurseGetMatchingFileDescriptor(AnalysisFileDescriptor fd, String fileName, String theDirectory) {
    // Change all backslash to forward slash for comparison
    String fdFileName = fd.getFileName().replaceAll("\\\\", "/");
    if ( dir != null ) {
      dir = dir.replace("\\", "/");
    }
    theDirectory = theDirectory.replace("\\", "/");
    
    if (fdFileName.endsWith(fileName) &&
        (dir == null || dir.equals(theDirectory))) {
      return fd;
    } else if (fd.getChildren() != null && fd.getChildren().size() > 0) {
      for(Iterator i = fd.getChildren().iterator(); i.hasNext();) {
        AnalysisFileDescriptor childFd = (AnalysisFileDescriptor)i.next();
        
        childFd.setQualifiedFilePath(fd.getQualifiedFilePath() != "" ? fd.getQualifiedFilePath() + "/" + fd.getDisplayName() : fd.getDisplayName());
        
        AnalysisFileDescriptor matchingFd = recurseGetMatchingFileDescriptor(childFd, fileName, childFd.getQualifiedFilePath());
        if (matchingFd != null) {
          return matchingFd;
        }
      } 
      return null;
    } else {
      return null;
    }
  }
 
 
}