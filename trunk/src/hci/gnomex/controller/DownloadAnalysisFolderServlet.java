package hci.gnomex.controller;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.Analysis;
import hci.gnomex.model.Request;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.HibernateSession;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;


public class DownloadAnalysisFolderServlet extends HttpServlet { 
  
  
  private String    keysString = null;
  private String    baseDir = "";
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DownloadAnalysisFolderServlet.class);
  
  public void init() {
  
  }
    
  protected void doGet(HttpServletRequest req, HttpServletResponse response)
      throws ServletException, IOException {

    // Get input parameters
    keysString = req.getParameter("resultKeys");
    baseDir = Constants.getAnalysisDirectory(req.getServerName());
    
    try {

      // Get security advisor
      SecurityAdvisor secAdvisor = (SecurityAdvisor) req.getSession().getAttribute(SecurityAdvisor.SECURITY_ADVISOR_SESSION_KEY);

      if (secAdvisor != null) {
        response.setContentType("application/x-download");
        response.setHeader("Content-Disposition", "attachment;filename=microarray.zip");
        response.setHeader("Cache-Control", "max-age=0, must-revalidate");
        
        long time1 = System.currentTimeMillis();
        
        
        Session sess = secAdvisor.getReadOnlyHibernateSession(req.getUserPrincipal().getName());
        
       
        Map fileNameMap = new HashMap();
        long compressedFileSizeTotal = getFileNamesToDownload(baseDir, keysString, fileNameMap);

        
        
        ZipOutputStream zout = new ZipOutputStream(response.getOutputStream());
        byte b[] = new byte[102400];

        
        int totalZipSize = 0;
        // For each request
        for(Iterator i = fileNameMap.keySet().iterator(); i.hasNext();) {
          String analysisNumber = (String)i.next();
          
          Analysis analysis = null;
          List analysisList = sess.createQuery("SELECT a from Analysis a where a.number = '" + analysisNumber + "'").list();
          if (analysisList.size() == 1) {
            analysis = (Analysis)analysisList.get(0);
          }
          
          
          // If we can't find the request in the database, just bypass it.
          if (analysis == null) {
            log.error("Unable to find request " + analysisNumber + ".  Bypassing download for user " + req.getUserPrincipal().getName() + ".");
            continue;
          }
          
          // Check permissions - bypass this request if the user 
          // does not have  permission to read it.
          if (!secAdvisor.canRead(analysis)) {  
            log.error("Insufficient permissions to read analysis " + analysisNumber + ".  Bypassing download for user " + req.getUserPrincipal().getName() + ".");
            continue;
          }
          
          List fileNames = (List)fileNameMap.get(analysisNumber);
          
          // For each file to be downloaded for the analysis
          for (Iterator i1 = fileNames.iterator(); i1.hasNext();) {

            String filename = (String) i1.next();

            
            FileInputStream in = new FileInputStream(filename);

            // Add ZIP entry to output stream.
            // (The file name starts after the year subdirectory)
            ZipEntry zipEntry = new ZipEntry("bioinformatics-analysis-" + filename.substring(baseDir.length() + 5));
            zout.putNextEntry(zipEntry);

            // Transfer bytes from the file to the ZIP file
            int numRead = 0;
            int size = 0;

            
            while (numRead != -1) {
              numRead = in.read(b);
              if (numRead != -1) {
                zout.write(b, 0, numRead);
                size += numRead;
              }
            }

            zout.closeEntry();
            totalZipSize += zipEntry.getCompressedSize();
          }     
          
          
        }
        
        response.setContentLength(totalZipSize);
        
        zout.finish();
        zout.flush();
        

        
        long time3 = System.currentTimeMillis();

        secAdvisor.closeReadOnlyHibernateSession();


      } else {
        response.setContentType("text/html");
        response.getOutputStream().println(
            "<html><head><title>Error</title></head>");
        response.getOutputStream().println("<body><b>");
        response.getOutputStream().println(
            "You must have a SecurityAdvisor in order to run this command "
                + "<br>");
        response.getOutputStream().println("</body>");
        response.getOutputStream().println("</html>");
      }
    } catch (Exception e) {
      response.setContentType("text/html");
      response.getOutputStream().println(
          "<html><head><title>Error</title></head>");
      response.getOutputStream().println("<body><b>");
      response.getOutputStream().println(
          "An error has occured while processing " + "<br>");
      response.getOutputStream().println(
          "Here is the exception: <br>" + e + "<br>");
      e.printStackTrace(new PrintWriter(response.getOutputStream()));
      response.getOutputStream().println("</body>");
      response.getOutputStream().println("</html>");
      e.printStackTrace();
    }

  }    
  

    
  public static long getFileNamesToDownload(String baseDir, String keysString, Map fileNameMap) {

    long fileSizeTotal = 0;
    String[] keys = keysString.split(":");
    for (int i = 0; i < keys.length; i++) {
      String key = keys[i];

      String tokens[] = key.split("-");
      String createYear = tokens[0];
      String createDate = tokens[1];
      String analysisNumber = tokens[2];


      String directoryName = baseDir + createYear + "/" + analysisNumber;
      fileSizeTotal += getFileNames(analysisNumber, directoryName, fileNameMap);
    }
    return fileSizeTotal;
  }      
      
     
    
  public static long getFileNames(String analysisNumber, String directoryName, Map fileNameMap) {
    File fd = new File(directoryName);
    
    long fileSizeTotal = 0;

    if (fd.isDirectory()) {
      String[] fileList = fd.list();
      for (int x = 0; x < fileList.length; x++) {
        String fileName = directoryName + "/" + fileList[x];
        File f1 = new File(fileName);
        if (f1.isDirectory()) {
          fileSizeTotal += getFileNames(analysisNumber, fileName, fileNameMap);
        } else {
          boolean include = true;
          if (include) {
            long fileSize = f1.length();
            fileSizeTotal += DownloadResultsServlet.getEstimatedCompressedFileSize(fileName, fileSize);
            
            
            List fileNames = (List)fileNameMap.get(analysisNumber);
            if (fileNames == null) {
              fileNames = new ArrayList();
              fileNameMap.put(analysisNumber, fileNames);
            }
            fileNames.add(fileName);
          }
        }
      }
    }
    return fileSizeTotal;
  }
}