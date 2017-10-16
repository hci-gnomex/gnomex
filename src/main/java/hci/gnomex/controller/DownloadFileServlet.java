package hci.gnomex.controller;

import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.Request;
import hci.gnomex.model.SequenceLane;
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

public class DownloadFileServlet extends HttpServlet {



  private static Logger LOG = Logger.getLogger(DownloadFileServlet.class);


  public void init() {

  }

  protected void doGet(HttpServletRequest req, HttpServletResponse response)
          throws ServletException, IOException {

    String serverName = req.getServerName();

    String username = req.getUserPrincipal() != null ? req.getUserPrincipal().getName() : "guest";
    ArchiveHelper archiveHelper = new ArchiveHelper();

    // Restrict commands to local host if request is not secure
    if (!ServletUtil.checkSecureRequest(req, LOG)) {
      ServletUtil.reportServletError(response, "Secure connection is required. Prefix your request with 'https'",
              LOG, "Accessing secure command over non-secure line from remote host is not allowed.");
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

    //  Get cached file descriptor parser
    FileDescriptorParser parser = (FileDescriptorParser) req.getSession().getAttribute(CacheFileDownloadList.SESSION_KEY_FILE_DESCRIPTOR_PARSER);
    if (parser == null) {
      LOG.error("Unable to get file descriptor parser from session");
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
        for(Iterator i = parser.getRequestNumbers().iterator(); i.hasNext();) {
          String requestNumber = (String)i.next();

          Request request = findRequest(sess, requestNumber);

          // If we can't find the request in the database, just bypass it.
          if (request == null) {
            LOG.error("Unable to find request " + requestNumber + ".  Bypassing download for user " + (req.getUserPrincipal() != null ? req.getUserPrincipal().getName() : "guest") + ".");
            continue;
          }

          // Check permissions - bypass this request if the user 
          // does not have  permission to read it.
          if (!secAdvisor.canRead(request)) {
            LOG.error("Insufficient permissions to read request " + requestNumber + ".  Bypassing download for user " + (req.getUserPrincipal() != null ? req.getUserPrincipal().getName() : "guest") + ".");
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
            xferLog.setEmailAddress(emailAddress);
            xferLog.setIpAddress(ipAddress);
            xferLog.setIdAppUser(secAdvisor.getIdAppUser());

            // Since we use the request number to determine if user has permission to read the data, match sure
            // it matches the request number of the directory.  If it doesn't bypass the download
            // for this file.
            String requestNumberBase = Request.getBaseRequestNumber(requestNumber);
            if (!requestNumberBase.equalsIgnoreCase(fd.getMainFolderName(sess, serverName, request.getIdCoreFacility()))) {
              boolean isAuthorizedDirectory = false;
              // If this is a flow cell, make sure that that a sequence lane on this request has this flow cell
              for(Iterator i2 = request.getSequenceLanes().iterator(); i2.hasNext();) {
                SequenceLane lane = (SequenceLane)i2.next();
                if (lane.getFlowCellChannel() != null &&
                        lane.getFlowCellChannel().getFlowCell().getNumber().equals(fd.getMainFolderName(sess, serverName, request.getIdCoreFacility()))) {
                  isAuthorizedDirectory = true;
                  break;
                }

              }
              if (!isAuthorizedDirectory) {
                LOG.error("Request number " + requestNumber + " does not correspond to the directory " + fd.getMainFolderName(sess, serverName, request.getIdCoreFacility()) + " for attempted download on " + fd.getFileName() +  ".  Bypassing download." );
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

        LOG.warn( "DownloadFileServlet: You must have a SecurityAdvisor in order to run this command.");
      }
    } catch (Exception e) {
      String errorMessage = Util.GNLOG(LOG,"Error in DownloadFileServlet ", e);
      StringBuilder requestDump = Util.printRequest(req);

      Util.sendErrorReport(HibernateSession.currentSession(),"GNomEx.Support@hci.utah.edu", "DoNotReply@hci.utah.edu", username, errorMessage, requestDump);

      HibernateSession.rollback();
      response.setStatus(999);

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
