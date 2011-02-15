package hci.gnomex.controller;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.Analysis;
import hci.gnomex.model.Property;
import hci.gnomex.model.Request;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.ArchiveHelper;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.hibernate.Session;


public class DownloadAnalysisFolderServlet extends HttpServlet { 
  
  
  private String    keysString = null;
  private String    baseDir = "";
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DownloadAnalysisFolderServlet.class);
  
  private ArchiveHelper archiveHelper = new ArchiveHelper();
  

  public void init() {
  
  }
    
  protected void doGet(HttpServletRequest req, HttpServletResponse response)
      throws ServletException, IOException {

    // Get input parameters
    keysString = req.getParameter("resultKeys");
    
    // Get the parameter that tells us if we are handling a large download.
    if (req.getParameter("mode") != null && !req.getParameter("mode").equals("")) {
      archiveHelper.setMode(req.getParameter("mode"));
    }
    
    
    try {

      // Get security advisor
      SecurityAdvisor secAdvisor = (SecurityAdvisor) req.getSession().getAttribute(SecurityAdvisor.SECURITY_ADVISOR_SESSION_KEY);

      if (secAdvisor != null) {
        response.setContentType("application/x-download");
        response.setHeader("Content-Disposition", "attachment;filename=microarray.zip");
        response.setHeader("Cache-Control", "max-age=0, must-revalidate");
        
        long time1 = System.currentTimeMillis();
        
        
        Session sess = secAdvisor.getReadOnlyHibernateSession(req.getUserPrincipal() != null ? req.getUserPrincipal().getName() : "guest");
        DictionaryHelper dh = DictionaryHelper.getInstance(sess);
        baseDir = dh.getAnalysisDirectory(req.getServerName());
       
        archiveHelper.setTempDir(dh.getProperty(Property.TEMP_DIRECTORY));        
        
        Map fileNameMap = new HashMap();
        long compressedFileSizeTotal = getFileNamesToDownload(baseDir, keysString, fileNameMap);

        
        
        
        // Open the archive output stream
        ZipOutputStream zipOut = null;
        TarArchiveOutputStream tarOut = null;
        if (archiveHelper.isZipMode()) {
          zipOut = new ZipOutputStream(response.getOutputStream());
        } else {
          tarOut = new TarArchiveOutputStream(response.getOutputStream());
        }
        

        
        int totalArchiveSize = 0;
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
            String zipEntryName = "bioinformatics-analysis-" + filename.substring(baseDir.length() + 5);
            archiveHelper.setArchiveEntryName(zipEntryName);
            
            // If we are using tar, compress the file first using
            // zip.  If we are zipping the file, just open
            // it to read.            
            InputStream in = archiveHelper.getInputStreamToArchive(filename, zipEntryName);
            

            // Add an entry to the archive 
            // (The file name starts after the year subdirectory)
            ZipEntry zipEntry = null;
            if (archiveHelper.isZipMode()) {
              // Add ZIP entry 
              zipEntry = new ZipEntry(archiveHelper.getArchiveEntryName());
              zipOut.putNextEntry(zipEntry);              
            } else {
              // Add a TAR archive entry
              TarArchiveEntry entry = new TarArchiveEntry(archiveHelper.getArchiveEntryName());
              entry.setSize(archiveHelper.getArchiveFileSize());
              tarOut.putArchiveEntry(entry);
            }
            

            // Transfer bytes from the file to the archive file
            OutputStream out = null;
            if (archiveHelper.isZipMode()) {
              out = zipOut;
            } else {
              out = tarOut;
            }
            int size = archiveHelper.transferBytes(in, out);
            totalArchiveSize += size;

            if (archiveHelper.isZipMode()) {
              zipOut.closeEntry();              
              totalArchiveSize += zipEntry.getCompressedSize();
            } else {
              tarOut.closeArchiveEntry();
              totalArchiveSize += archiveHelper.getArchiveFileSize();
            }
            
            // Remove temporary files
            archiveHelper.removeTemporaryFile();

          }     
          
          
        }
        
        response.setContentLength(totalArchiveSize);
        
        
        if (archiveHelper.isZipMode()) {
          zipOut.finish();
          zipOut.flush();          
        } else {
          tarOut.close();
          tarOut.flush();
        }

        
        secAdvisor.closeReadOnlyHibernateSession();

      } else {
        response.setStatus(999);
        System.out.println( "DownloadAnalyisFolderServlet: You must have a SecurityAdvisor in order to run this command.");
      }
    } catch (Exception e) {
      response.setStatus(999);
      System.out.println( "DownloadAnalyisFolderServlet: An exception occurred " + e.toString());
      e.printStackTrace();
    } finally {
      
      // Remove temporary files
      archiveHelper.removeTemporaryFile();
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
              fileNames = new ArrayList<String>();
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