package hci.gnomex.controller;

import hci.gnomex.model.ProductOrder;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.TransferLog;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
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
import org.apache.log4j.Logger;

public class DownloadProductOrderFileServlet extends HttpServlet { 


  
  private static Logger LOG = Logger.getLogger(DownloadProductOrderFileServlet.class);
  
  private ProductOrderFileDescriptorParser parser = null;
  
  private ArchiveHelper archiveHelper = new ArchiveHelper();

  private String serverName = "";
  

  
  public void init() {
  
  }
    
  protected void doGet(HttpServletRequest req, HttpServletResponse response)
      throws ServletException, IOException {
    
    serverName = req.getServerName();

    // Restrict commands to local host if request is not secure
    if (!ServletUtil.checkSecureRequest(req, LOG)) {
      ServletUtil.reportServletError(response, "Secure connection is required. Prefix your request with 'https'",
              LOG, "Accessing secure command over non-secure line from remote host is not allowed.");
      return;
    }

    //  Get cached file descriptor parser
    parser = (ProductOrderFileDescriptorParser) req.getSession().getAttribute(CacheProductOrderFileDownloadList.SESSION_KEY_FILE_DESCRIPTOR_PARSER);
    if (parser == null) {
      LOG.error("Unable to get file descriptor parser from session");
      return;
    }
    
    // Get the parameter that tells us if we are handling a large download.
    if (req.getParameter("mode") != null && !req.getParameter("mode").equals("")) {
      archiveHelper.setMode(req.getParameter("mode"));
    }
    
    String emailAddress = "";
    if (req.getParameter("emailAddress") != null && !req.getParameter("emailAddress").equals("")) {
      emailAddress = req.getParameter("emailAddress");
    }
    
    String ipAddress = GNomExCommand.getRemoteIP(req);
    
    SecurityAdvisor secAdvisor = null;
    try {
      

      // Get security advisor
      secAdvisor = (SecurityAdvisor) req.getSession().getAttribute(SecurityAdvisor.SECURITY_ADVISOR_SESSION_KEY);

      if (secAdvisor != null) {
        response.setContentType("application/x-download");
        response.setHeader("Content-Disposition", "attachment;filename=gnomexProductOrder.zip");
        response.setHeader("Cache-Control", "max-age=0, must-revalidate");
        
        
        Session sess = secAdvisor.getWritableHibernateSession(req.getUserPrincipal() != null ? req.getUserPrincipal().getName() : "guest");

        DictionaryHelper dh = DictionaryHelper.getInstance(sess);
        archiveHelper.setTempDir(dh.getPropertyDictionary(PropertyDictionary.TEMP_DIRECTORY));
        

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
        
        for(Iterator i = parser.getProductOrderIds().iterator(); i.hasNext();) {
          String idProductOrder = (String)i.next();
          
          ProductOrder productOrder = null;
          List productOrderList = sess.createQuery("SELECT po from ProductOrder po where po.idProductOrder = " + idProductOrder ).list();
          if (productOrderList.size() == 1) {
            productOrder = (ProductOrder)productOrderList.get(0);
          }
          
          // If we can't find the productOrder in the database, just bypass it.
          if (productOrder == null) {
            LOG.error("Unable to find productOrder " + idProductOrder + ".  Bypassing download for user " + req.getUserPrincipal().getName() + ".");
            continue;
          }
          
          // Check permissions - bypass this productOrder if the user 
          // does not have  permission to read it.
          if (!secAdvisor.canRead(productOrder)) {  
            LOG.error("Insufficient permissions to read productOrder " + idProductOrder + ".  Bypassing download for user " + req.getUserPrincipal().getName() + ".");
            continue;
          }
          
          List fileDescriptors = parser.getFileDescriptors(idProductOrder);
          
          // For each file to be downloaded for the productOrder
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
            xferLog.setIdProductOrder(productOrder.getIdProductOrder());
            xferLog.setIdLab(productOrder.getIdLab());
            xferLog.setEmailAddress(emailAddress);
            xferLog.setIpAddress(ipAddress);
            xferLog.setIdAppUser(secAdvisor.getIdAppUser());
            
            
            // Since we use the request number to determine if user has permission to read the data, match sure
            // it matches the request number of the directory.  If it doesn't bypass the download
            // for this file.
            if (!idProductOrder.equalsIgnoreCase(String.valueOf(fd.getId()))) {
              LOG.error("ProductOrder id does not match directory for attempted download on " + fd.getFileName() + " for user " + req.getUserPrincipal().getName() + ".  Bypassing download." );
              continue;
            }


            // If we are using tar, compress the file first using
            // zip.  If we are zipping the file, just open
            // it to read.            
            InputStream in = archiveHelper.getInputStreamToArchive(fd.getFileName(), fd.getZipEntryName());
            

            // Add an entry to the archive 
            // (The file name starts after the year subdirectory)
            if (archiveHelper.isZipMode()) {
              // Add ZIP entry 
              zipOut.putNextEntry(new ZipEntry("productOrder-" + archiveHelper.getArchiveEntryName()));
            } else {
              // Add a TAR archive entry
              TarArchiveEntry entry = new TarArchiveEntry("productOrder-" + archiveHelper.getArchiveEntryName());
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
            
            // Save transfer log
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
        
        sess.flush();
          
          
        if (archiveHelper.isZipMode()) {
          zipOut.finish();
          zipOut.flush();
        } else {
          tarOut.close();
          tarOut.flush();
        }

      } else {
        response.setStatus(999);
        LOG.warn( "DownloadAnalyisFileServlet: You must have a SecurityAdvisor in order to run this command.");
      }
    } catch (Exception e) {
      HibernateSession.rollback();
      response.setStatus(999);
      LOG.error( "DownloadAnalyisFileServlet: An exception occurred " + e.toString(), e);

    } finally {
      try {
        if (secAdvisor != null) {
          secAdvisor.closeHibernateSession();        
        }
      }catch(Exception e) {
        LOG.error("DownloadAnalyisFileServlet Error", e);
      }
      // clear out session variable
      req.getSession().setAttribute(CacheProductOrderFileDownloadList.SESSION_KEY_FILE_DESCRIPTOR_PARSER, null);
      
      // Remove temporary files
      archiveHelper.removeTemporaryFile();
    }

  }    
  
  
 
 
}