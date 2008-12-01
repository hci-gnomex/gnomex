package hci.gnomex.controller;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.Request;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.FileDescriptor;
import hci.gnomex.utility.HibernateSession;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.InetAddress;
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


public class DownloadResultsServlet extends HttpServlet { 
  
  
  private String    keysString = null;
  private String    includeTIF = "N";
  private String    includeJPG = "N";

  private String    baseDir;
  private String    baseDirFlowCell;
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DownloadResultsServlet.class);
  
  public void init() {
  
  }
    
  protected void doGet(HttpServletRequest req, HttpServletResponse response)
      throws ServletException, IOException {
    
    // restrict commands to local host if request is not secure
    if (!req.isSecure()) {
      if (req.getRemoteAddr().equals(InetAddress.getLocalHost().getHostAddress())
          || req.getRemoteAddr().equals("127.0.0.1")) {
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

    // Get input parameters
    keysString = req.getParameter("resultKeys");
    if (req.getParameter("includeTIF") != null
        && !req.getParameter("includeTIF").equals("")) {
      includeTIF = req.getParameter("includeTIF");
    }
    if (req.getParameter("includeJPG") != null
        && !req.getParameter("includeJPG").equals("")) {
      includeJPG = req.getParameter("includeJPG");
    }
    baseDir         = Constants.getMicroarrayDirectoryForReading(req.getServerName());
    baseDirFlowCell = Constants.getFlowCellDirectory(req.getServerName());



    try {

      // Get security advisor
      SecurityAdvisor secAdvisor = (SecurityAdvisor) req.getSession().getAttribute(SecurityAdvisor.SECURITY_ADVISOR_SESSION_KEY);

      if (secAdvisor != null) {
        response.setContentType("application/x-download");
        response.setHeader("Content-Disposition", "attachment;filename=gnomexExperimentData.zip");
        response.setHeader("Cache-Control", "max-age=0, must-revalidate");
        
        long time1 = System.currentTimeMillis();
        
        
        Session sess = secAdvisor.getReadOnlyHibernateSession(req.getUserPrincipal().getName());
        
       
        Map fileDescriptorMap = new HashMap();
        long compressedFileSizeTotal = getFileNamesToDownload(baseDir, baseDirFlowCell, keysString, fileDescriptorMap, includeTIF.equals("Y"), includeJPG.equals("Y"));

        
        
        ZipOutputStream zout = new ZipOutputStream(response.getOutputStream());
        byte b[] = new byte[102400];

        
        int totalZipSize = 0;
        // For each request
        for(Iterator i = fileDescriptorMap.keySet().iterator(); i.hasNext();) {
          String requestNumber = (String)i.next();
          
          Request request = findRequest(sess, requestNumber);
          
          // If we can't find the request in the database, just bypass it.
          if (request == null) {
            log.error("Unable to find request " + requestNumber + ".  Bypassing download for user " + req.getUserPrincipal().getName() + ".");
            continue;
          }
          
          // Check permissions - bypass this request if the user 
          // does not have  permission to read it.
          if (!secAdvisor.canRead(request)) {  
            log.error("Insufficient permissions to read request " + requestNumber + ".  Bypassing download for user " + req.getUserPrincipal().getName() + ".");
            continue;
          }
          
          List fileDescriptors = (List)fileDescriptorMap.get(requestNumber);
          
          // For each file to be downloaded for the request
          for (Iterator i1 = fileDescriptors.iterator(); i1.hasNext();) {

            FileDescriptor fileDescriptor = (FileDescriptor) i1.next();

            
            FileInputStream in = new FileInputStream(fileDescriptor.getFileName());

            // Add ZIP entry to output stream.
            // (The file name starts after the year subdirectory)
            ZipEntry zipEntry = new ZipEntry(fileDescriptor.getZipEntryName());
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
  
  
  public static Request findRequest(Session sess, String requestNumber) {
    Request request = null;
    List requests = sess.createQuery("SELECT req from Request req where req.number = '" + requestNumber + "'").list();
    if (requests.size() == 1) {
      request = (Request)requests.get(0);
    }
    return request;    
  }
    
  public static long getFileNamesToDownload(String baseDir, String baseDirFlowCell, String keysString, Map fileDescriptorMap, boolean includeAllTIFFiles, boolean includeAllJPGFiles) {

    long fileSizeTotal = 0;
    String[] keys = keysString.split(":");
    for (int i = 0; i < keys.length; i++) {
      String key = keys[i];

      String tokens[] = key.split("-");
      String createYear = tokens[0];
      String createDate = tokens[1];
      String requestNumber = tokens[2];
      String resultDirectory = tokens[3];
      String flowCellIndicator = "";
      if (tokens.length > 4) {
        flowCellIndicator = tokens[4];
      }
      
      String directoryName = "";
      String theBaseDir;
      if (flowCellIndicator.equals(Constants.FLOWCELL_DIRECTORY_FLAG)) {
        directoryName = baseDirFlowCell + createYear + "/" + resultDirectory;
        theBaseDir = baseDirFlowCell;
      } else {
        directoryName = baseDir + createYear + "/" + requestNumber + "/" + resultDirectory;
        theBaseDir = baseDir;
      }
      

      
      fileSizeTotal += getFileNames(requestNumber, directoryName, fileDescriptorMap, includeAllTIFFiles, includeAllJPGFiles, flowCellIndicator, theBaseDir);
    }
    return fileSizeTotal;
  }      
      
     
    
  public static long getFileNames(String requestNumber, String directoryName, Map fileDescriptorMap, boolean includeAllTIFFiles, boolean includeAllJPGFiles, String flowCellIndicator, String theBaseDir) {
    File fd = new File(directoryName);
    
    long fileSizeTotal = 0;

    if (fd.isDirectory()) {
      String[] fileList = fd.list();
      for (int x = 0; x < fileList.length; x++) {
        String fileName = directoryName + "/" + fileList[x];
        File f1 = new File(fileName);
        if (f1.isDirectory()) {
          fileSizeTotal += getFileNames(requestNumber, fileName, fileDescriptorMap, includeAllTIFFiles, includeAllJPGFiles, flowCellIndicator, theBaseDir);
        } else {
          boolean include = true;
          if (!includeAllJPGFiles && fileName.toLowerCase().endsWith(".jpg")) {
            include = false;
          } else if (!includeAllTIFFiles && fileName.toLowerCase().endsWith(".tif")) {
            include = false;
          } else if (fileName.toUpperCase().endsWith(".DS_Store")) {
            include = false;
          } else if (fileName.toUpperCase().endsWith("THUMBS.DB")) {
            include = false;
          }
          if (include) {
            long fileSize = f1.length();            
            
            fileSizeTotal += getEstimatedCompressedFileSize(fileName, fileSize);
            
            
            List fileDescriptors = (List)fileDescriptorMap.get(requestNumber);
            if (fileDescriptors == null) {
              fileDescriptors = new ArrayList();
              fileDescriptorMap.put(requestNumber, fileDescriptors);
            }
            
            String zipEntryName;
            if (flowCellIndicator.equals(Constants.FLOWCELL_DIRECTORY_FLAG)) {
              zipEntryName = requestNumber + "/" + fileName.substring(theBaseDir.length() + 5).replaceAll("\\\\", "/");  
            } else {
              zipEntryName = fileName.substring(theBaseDir.length() + 5).replaceAll("\\\\", "/");  
            }
            
            fileDescriptors.add(new FileDescriptor(requestNumber, "", f1, zipEntryName));
          }
        }
      }
    }
    return fileSizeTotal;
  }
  
  public static long getEstimatedCompressedFileSize(String fileName, long fileSize) {
    double compressionRatio = 1;
    if (fileName.toUpperCase().endsWith("FEP")) {
      compressionRatio = 1.6;
    } else if (fileName.toUpperCase().endsWith("PDF")) {
      compressionRatio = 1;
    } else if (fileName.toUpperCase().endsWith("TIF")) {
      compressionRatio = 1.9;
    } else if (fileName.toUpperCase().endsWith("TIFF")) {
      compressionRatio = 1.9;
    } else if (fileName.toUpperCase().endsWith("JPG")) {
      compressionRatio = 1;
    } else if (fileName.toUpperCase().endsWith("JPEG")) {
      compressionRatio = 1;
    } else if (fileName.toUpperCase().endsWith("TXT")) {
      compressionRatio = 2.7; 
    } else if (fileName.toUpperCase().endsWith("RTF")) {
      compressionRatio = 2.7;
    } else if (fileName.toUpperCase().endsWith("DAT")) {
      compressionRatio = 1.6;
    } else if (fileName.toUpperCase().endsWith("CEL")) {
      compressionRatio = 2.8;
    } else if (fileName.toUpperCase().endsWith("ZIP")) {
      compressionRatio = 1;
    }     
    return new BigDecimal(fileSize / compressionRatio).longValue();
  }
}