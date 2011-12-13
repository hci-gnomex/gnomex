package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.DataTrack;
import hci.gnomex.utility.DictionaryHelper;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;



public class GetEstimatedDataTrackDownloadSize extends GNomExCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetEstimatedDataTrackDownloadSize.class);
  
  private String    keysString = null;

  private String    baseDir;

  protected final static String   SESSION_DATATRACK_KEYS = "GNomExDataTrackKeys";

  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    // Get input parameters
    keysString = request.getParameter("keys");
    baseDir         = request.getServerName();
    
    // Store download keys in session b/c Flex FileReference cannnot
    // handle long request parameter
    request.getSession().setAttribute(SESSION_DATATRACK_KEYS, keysString);

  }

  public Command execute() throws RollBackCommandException {
    
    Session sess = null;
    try {
      sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
      DictionaryHelper dh = DictionaryHelper.getInstance(sess);
      // TODO:  GenoPub - need directory
      baseDir = dh.getMicroarrayDirectoryForReading(baseDir);

      long estimatedDownloadSize = 0;
      long uncompressedDownloadSize = 0;

      String[] keyTokens = keysString.split(":");
      for(int x = 0; x < keyTokens.length; x++) {
        String key = keyTokens[x];

        String[] idTokens = key.split(",");
        if (idTokens.length != 2) {
          throw new Exception("Invalid parameter format " + key + " encountered. Expected 99,99 for idDataTrack and idDataTrackGrouping");
        }
        Integer idDataTrack = new Integer(idTokens[0]);

        DataTrack dataTrack = DataTrack.class.cast(sess.load(DataTrack.class, idDataTrack));
        for (File file : dataTrack.getFiles(this.baseDir)) {
          double compressionRatio = 1;
          if (file.getName().toUpperCase().endsWith("BAR")) {
            compressionRatio = 3;
          } else if (file.getName().toUpperCase().endsWith("BED")) {
            compressionRatio = 2.5;
          } else if (file.getName().toUpperCase().endsWith("GFF")) {
            compressionRatio = 3;
          } else if (file.getName().toUpperCase().endsWith("BRS")) {
            compressionRatio = 4;
          } else if (file.getName().toUpperCase().endsWith("BGN")) {
            compressionRatio = 3;
          } else if (file.getName().toUpperCase().endsWith("BGR")) {
            compressionRatio = 3;
          } else if (file.getName().toUpperCase().endsWith("BP1")) {
            compressionRatio = 3;
          } else if (file.getName().toUpperCase().endsWith("BP2")) {
            compressionRatio = 3;
          } else if (file.getName().toUpperCase().endsWith("CYT")) {
            compressionRatio = 3;
          } else if (file.getName().toUpperCase().endsWith("GTF")) {
            compressionRatio = 3;
          } else if (file.getName().toUpperCase().endsWith("PSL")) {
            compressionRatio = 3;
          } else if (file.getName().toUpperCase().endsWith("USEQ")) {
            compressionRatio = 1;
          } else if (file.getName().toUpperCase().endsWith("BNIB")) {
            compressionRatio = 2;
          }  else if (file.getName().toUpperCase().endsWith("FASTA")) {
            compressionRatio = 2;
          }       
          estimatedDownloadSize += new BigDecimal(file.length() / compressionRatio).longValue();
          uncompressedDownloadSize += file.length();
        }
      }
      
      this.xmlResult = "<SUCCESS size=" +  Long.valueOf(estimatedDownloadSize).toString() + " uncompressedSize=" + Long.valueOf(uncompressedDownloadSize) + "/>";
      this.setResponsePage(SUCCESS_JSP);

    } catch (Exception e) {
      Logger.getLogger(this.getClass().getName()).warning(e.toString());
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        this.getSecAdvisor().closeReadOnlyHibernateSession();
      } catch (Exception e) {
      }
    }
    return this;
  }
}