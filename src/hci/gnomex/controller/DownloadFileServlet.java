package hci.gnomex.controller;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.Property;
import hci.gnomex.model.Request;
import hci.gnomex.model.SequenceLane;
import hci.gnomex.model.TransferLog;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.ArchiveHelper;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.FileDescriptor;
import hci.gnomex.utility.FileDescriptorParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.hibernate.Session;


public class DownloadFileServlet extends HttpServlet { 


  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DownloadFileServlet.class);
  
  private FileDescriptorParser parser = null;

  
  private ArchiveHelper archiveHelper = new ArchiveHelper();
  
  private String serverName = "";
  

  
  public void init() {
  
  }
    
  protected void doGet(HttpServletRequest req, HttpServletResponse response)
      throws ServletException, IOException {
    
    serverName = req.getServerName();


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
    
    // Get the parameter that tells us if we are handling a large download.
    if (req.getParameter("mode") != null && !req.getParameter("mode").equals("")) {
      archiveHelper.setMode(req.getParameter("mode"));
    }
    
    //  Get cached file descriptor parser
    parser = (FileDescriptorParser) req.getSession().getAttribute(CacheFileDownloadList.SESSION_KEY_FILE_DESCRIPTOR_PARSER);
    if (parser == null) {
      log.error("Unable to get file descriptor parser from session");
      return;
    }
    
    SecurityAdvisor secAdvisor = null;
    try {
      

      // Get security advisor
      secAdvisor = (SecurityAdvisor) req.getSession().getAttribute(SecurityAdvisor.SECURITY_ADVISOR_SESSION_KEY);

      if (secAdvisor != null) {
        response.setContentType("application/x-download");
        response.setHeader("Content-Disposition", "attachment;filename=gnomex.zip");
        response.setHeader("Cache-Control", "max-age=0, must-revalidate");
        
        
        Session sess = secAdvisor.getHibernateSession(req.getUserPrincipal() != null ? req.getUserPrincipal().getName() : "guest");
        DictionaryHelper dh = DictionaryHelper.getInstance(sess);
        
        archiveHelper.setTempDir(dh.getProperty(Property.TEMP_DIRECTORY));
        
        parser.parse();
        
        
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
        for(Iterator i = parser.getRequestNumbers().iterator(); i.hasNext();) {
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
          
          List fileDescriptors = parser.getFileDescriptors(requestNumber);
          
          // For each file to be downloaded for the request
          for (Iterator i1 = fileDescriptors.iterator(); i1.hasNext();) {

            FileDescriptor fd = (FileDescriptor) i1.next();
            
            // Ignore file descriptors that represent directories.  We just
            // will zip up actual files.
            if (fd.getType().equals("dir")) {
              continue;
            }

            // Insert a transfer log entry
            TransferLog xferLog = new TransferLog();
            xferLog.setFileName(fd.getZipEntryName());
            xferLog.setStartDateTime(new java.util.Date(System.currentTimeMillis()));
            xferLog.setTransferType(TransferLog.TYPE_DOWNLOAD);
            xferLog.setTransferMethod(TransferLog.METHOD_HTTP);
            xferLog.setPerformCompression("Y");
            xferLog.setIdRequest(request.getIdRequest());
            xferLog.setIdLab(request.getIdLab());

            // Since we use the request number to determine if user has permission to read the data, match sure
            // it matches the request number of the directory.  If it doesn't bypass the download
            // for this file.
            String requestNumberBase = Request.getBaseRequestNumber(requestNumber);
            if (!requestNumberBase.equalsIgnoreCase(fd.getMainFolderName(dh, serverName))) {
              boolean isAuthorizedDirectory = false;
              // If this is a flow cell, make sure that that a sequence lane on this request has this flow cell
              for(Iterator i2 = request.getSequenceLanes().iterator(); i2.hasNext();) {
                SequenceLane lane = (SequenceLane)i2.next();
                if (lane.getFlowCellChannel() != null && 
                    lane.getFlowCellChannel().getFlowCell().getNumber().equals(fd.getMainFolderName(dh, serverName))) {
                  isAuthorizedDirectory = true;
                  break;
                }
                
              }
              if (!isAuthorizedDirectory) {
                log.error("Request number " + requestNumber + " does not correspond to the directory " + fd.getMainFolderName(dh, serverName) + " for attempted download on " + fd.getFileName() +  ".  Bypassing download." );
                continue;              
              }
            }


            // If we are using tar, compress the file first using
            // zip.  If we are zipping the file, just open
            // it to read.            
            InputStream in = archiveHelper.getInputStreamToArchive(fd.getFileName(), fd.getZipEntryName());
            

            // Add an entry to the archive 
            // (The file name starts after the year subdirectory)
            if (archiveHelper.isZipMode()) {
              // Add ZIP entry 
              zipOut.putNextEntry(new ZipEntry(archiveHelper.getArchiveEntryName()));              
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
            
            
            xferLog.setFileSize(new BigDecimal(size));
            xferLog.setEndDateTime(new java.util.Date(System.currentTimeMillis()));
            sess.save(xferLog);

            if (archiveHelper.isZipMode()) {
              zipOut.closeEntry();              
            } else {
              tarOut.closeArchiveEntry();
            }
            
            // Remove temporary files
            archiveHelper.removeTemporaryFile();
          }     
        }
        
        
        if (archiveHelper.isZipMode()) {
          zipOut.finish();
          zipOut.flush();          
        } else {
          tarOut.finish();
        }
        
        sess.flush();


      } else {
        response.setStatus(999);
        System.out.println( "DownloadFileServlet: You must have a SecurityAdvisor in order to run this command.");
      }
    } catch (Exception e) {
      response.setStatus(999);
      System.out.println( "DownloadFileServlet: An exception occurred " + e.toString());
      e.printStackTrace();
    } finally {
      
      if (secAdvisor != null) {
        try {
          secAdvisor.closeHibernateSession();
        } catch(Exception e){
        }
      }
      // clear out session variable
      req.getSession().setAttribute(CacheFileDownloadList.SESSION_KEY_FILE_DESCRIPTOR_PARSER, null);
      // Remove temporary files
      archiveHelper.removeTemporaryFile();

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
    
 
}