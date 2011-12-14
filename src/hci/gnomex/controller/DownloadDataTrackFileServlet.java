package hci.gnomex.controller;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.DataTrack;
import hci.gnomex.model.DataTrackFolder;
import hci.gnomex.model.GenomeBuild;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.ArchiveHelper;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateGuestSession;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.hibernate.Session;



public class DownloadDataTrackFileServlet extends HttpServlet { 


  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DownloadDataTrackFileServlet.class);
  
  private SecurityAdvisor secAdvisor;
  
  private String serverName;
  private String baseDir;
  private String analysisBaseDir;
  
  public void init() {
  
  }
    
  protected void doGet(HttpServletRequest req, HttpServletResponse res)
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
        
        res.setContentType("text/html");
        res.getOutputStream().println(
            "<html><head><title>Error</title></head>");
        res.getOutputStream().println("<body><b>");
        res.getOutputStream().println(
            "Secure connection is required. Prefix your request with 'https: "
                + "<br>");
        res.getOutputStream().println("</body>");
        res.getOutputStream().println("</html>");
        return;
      }
    }

    Session sess = null;

    DictionaryHelper dh = DictionaryHelper.getInstance(sess);
    baseDir = PropertyDictionaryHelper.getInstance(sess).getDataTrackReadDirectory(serverName);
    analysisBaseDir = PropertyDictionaryHelper.getInstance(sess).getAnalysisReadDirectory(serverName);

    // Get the download keys stored in session when download size estimated.  
    // Can't use request parameter here do to Flex FileReference url properties
    // size restriction.
    String keys = (String)req.getSession().getAttribute(GetEstimatedDataTrackDownloadSize.SESSION_DATATRACK_KEYS);

    // Now empty out the session attribute
    req.getSession().setAttribute(GetEstimatedDataTrackDownloadSize.SESSION_DATATRACK_KEYS, "");

    // Get the parameter that tells us if we are handling a large download.
    ArchiveHelper archiveHelper = new ArchiveHelper();
    if (req.getParameter("mode") != null && !req.getParameter("mode").equals("")) {
      archiveHelper.setMode(req.getParameter("mode"));
    }

    try {
      if (keys == null || keys.equals("")) {
        throw new Exception("Cannot perform download due to empty keys parameter.");
      }
      sess = HibernateGuestSession.currentGuestSession();
      SecurityAdvisor secAdvisor = null;


      // Get security advisor
      secAdvisor = (SecurityAdvisor) req.getSession().getAttribute(SecurityAdvisor.SECURITY_ADVISOR_SESSION_KEY);
        
      res.setContentType("application/x-download");
      res.setHeader("Content-Disposition", "attachment;filename=genopub_dataTracks.zip");
      res.setHeader("Cache-Control", "max-age=0, must-revalidate");

      // Open the archive output stream
      archiveHelper.setTempDir("./");
      TarArchiveOutputStream tarOut = null;
      ZipOutputStream zipOut = null;
      if (archiveHelper.isZipMode()) {
        zipOut = new ZipOutputStream(res.getOutputStream());
      } else {
        tarOut = new TarArchiveOutputStream(res.getOutputStream());
      }

      long totalArchiveSize = 0;

      String[] keyTokens = keys.split(":");
      for(int x = 0; x < keyTokens.length; x++) {
        String key = keyTokens[x];

        String[] idTokens = key.split(",");
        if (idTokens.length != 2) {
          throw new Exception("Invalid parameter format " + key + " encountered. Expected 99,99 for idDataTrack and idDataTrackFolder");
        }
        Integer idDataTrack = new Integer(idTokens[0]);
        Integer idDataTrackFolder = new Integer(idTokens[1]);

        DataTrack dataTrack = DataTrack.class.cast(sess.load(DataTrack.class, idDataTrack));

        if (!this.secAdvisor.canRead(dataTrack)) {
          throw new Exception("Insufficient permission to read/download dataTrack.");
        }

        DataTrackFolder dataTrackFolder = null;
        if (idDataTrackFolder.intValue() == -99) {
          GenomeBuild gv = dh.getGenomeBuildObject(dataTrack.getIdGenomeBuild());
          dataTrackFolder = gv.getRootDataTrackFolder();
        } else {
          for(Iterator<?>i = dataTrack.getFolders().iterator(); i.hasNext();) {
            DataTrackFolder ag = DataTrackFolder.class.cast(i.next());
            if (ag.getIdDataTrackFolder().equals(idDataTrackFolder)) {
              dataTrackFolder = ag;
              break;

            }
          }

        }
        if (dataTrackFolder == null) {
          throw new Exception("Unable to find dataTrack folder " + idDataTrackFolder);
        }

        String path = dataTrackFolder.getQualifiedName() + "/" + dataTrack.getName() + "/";


        for (File file : dataTrack.getFiles(this.baseDir, this.analysisBaseDir)) {
          String zipEntryName = path + file.getName();
          archiveHelper.setArchiveEntryName(zipEntryName);

          // If we are using tar, compress the file first using
          // zip.  If we are zipping the file, just open
          // it to read.            
          InputStream in = archiveHelper.getInputStreamToArchive(file.getAbsolutePath(), zipEntryName);


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

      if (archiveHelper.isZipMode()) {
        zipOut.finish();
        zipOut.flush();          
      } else {
        tarOut.close();
        tarOut.flush();
      }


    } catch (Exception e) {
      Logger.getLogger(this.getClass().getName()).warning(e.toString());
      e.printStackTrace();
      res.setStatus(99);
    } finally {
      if (sess != null) {
        sess.close();
      }
    }
  }
}
