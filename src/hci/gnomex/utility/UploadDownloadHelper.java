package hci.gnomex.utility;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;

import hci.gnomex.constants.Constants;
import hci.gnomex.controller.FastDataTransferDownloadAnalysisServlet;
import hci.gnomex.controller.GNomExCommand;
import hci.gnomex.model.Request;
import hci.gnomex.security.SecurityAdvisor;

public class UploadDownloadHelper {
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(UploadDownloadHelper.class);

  public static void writeDownloadInfoFile(String baseDir, String emailAddress, SecurityAdvisor secAdvisor, HttpServletRequest req) {
    if (!baseDir.endsWith(File.separator)) {
      baseDir += File.separator;
    }
    File info = new File(baseDir + Constants.FDT_DOWNLOAD_INFO_FILE_NAME);
    try {
      if (!info.createNewFile()) {
        log.error("Unable to create info file for FDT transfer.");
      } else {
        FileWriter fw = new FileWriter(info);
        PrintWriter pw = new PrintWriter(fw);
        pw.println(emailAddress);
        pw.println(GNomExCommand.getRemoteIP(req));
        pw.println(secAdvisor.getIdAppUser().toString());
        pw.flush();
        pw.close();
      }
    } catch(IOException ex) {
      log.error("Unable to write info file for FDT Transfer", ex);
    }

  }
}
