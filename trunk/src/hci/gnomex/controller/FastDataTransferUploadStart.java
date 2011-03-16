package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.Analysis;
import hci.gnomex.model.Request;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.PropertyHelper;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;


public class FastDataTransferUploadStart extends GNomExCommand implements Serializable {

  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(FastDataTransferUploadStart.class);

  private String serverName;
  private Integer idAnalysis;
  private Integer idRequest;
  private String targetDir;

  public void validate() {
  }

  public void loadCommand(HttpServletRequest request, HttpSession session) {
    serverName = request.getServerName();     
    
    if (request.getParameter("idRequest") != null && !request.getParameter("idRequest").equals("")) {
      idRequest = Integer.valueOf(request.getParameter("idRequest"));
    }
    if (request.getParameter("idAnalysis") != null && !request.getParameter("idAnalysis").equals("")) {
      idAnalysis = Integer.valueOf(request.getParameter("idAnalysis"));
    }
    
    if (idAnalysis == null && idRequest == null) {
      this.addInvalidField("missing id", "idRequest or idAnalysis must be provided");
    }
  }

  public Command execute() throws RollBackCommandException {

    try {
      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
      SecurityAdvisor secAdvisor = this.getSecAdvisor();
      DictionaryHelper dh = DictionaryHelper.getInstance(sess);
      SimpleDateFormat yearFormatter = new SimpleDateFormat("yyyy");
      String createYear = "";

      if (idAnalysis != null) {
        Analysis analysis = (Analysis)sess.get(Analysis.class, idAnalysis);
        if (!secAdvisor.canUpdate(analysis)) {
          this.addInvalidField("insufficient permissions", "insufficient permissions to upload analysis files");
        }
        if (this.isValid()) {
          createYear = yearFormatter.format(analysis.getCreateDate());
          String baseDir = dh.getAnalysisWriteDirectory(serverName);
          targetDir = baseDir + File.separator + createYear + File.separator + analysis.getNumber();
        }
      } else if (idRequest != null) {
        Request experiment = (Request)sess.get(Request.class, idRequest);
        if (!secAdvisor.canUpdate(experiment)) {
          this.addInvalidField("insufficient permissions", "insufficient permissions to upload experiment files");
        }
        if (this.isValid()) {
          createYear = yearFormatter.format(experiment.getCreateDate());
          String baseDir = dh.getMicroarrayDirectoryForWriting(serverName);
          targetDir = baseDir + File.separator + createYear + File.separator + Request.getBaseRequestNumber(experiment.getNumber()) + File.separator + Constants.UPLOAD_STAGING_DIR;
        }
      }
      if (this.isValid()) {
        if (!new File(targetDir).exists()) {
          File dir = new File(targetDir);
          boolean success = dir.mkdirs();
          if (!success) {
            this.addInvalidField("mkdir", "Unable to make target dir");
          }
        }
      }
      
      if (this.isValid()) {
        UUID uuid = UUID.randomUUID();
        String uuidStr = uuid.toString();

        // Directories must be created one level at a time so that permissions will be set properly in Linux      
        String softlinks_dir = PropertyHelper.getInstance(sess).getFDTDirectoryForGNomEx(serverName)+ File.separator + uuidStr;       

        File dir = new File(softlinks_dir);
        boolean isDirCreated = dir.mkdir();  
        if (!isDirCreated) {
          this.addInvalidField("Error.", "Unable to create " + softlinks_dir + " directory.");    
        }         

        // change ownership to fdt
        Process process = Runtime.getRuntime().exec( new String[] { "chown", "-R", "fdt:fdt", softlinks_dir } );          
        process.waitFor();
        process.destroy();        
        
        // only fdt user (and root) can read and write to this directory
        process = Runtime.getRuntime().exec( new String[] { "chmod", "700", softlinks_dir } );          
        process.waitFor();
        process.destroy();        
        
        // start daemon
        process = Runtime.getRuntime().exec( new String[] { "gnomex_fdt_daemon", "-sourceDir", softlinks_dir, "-targetDir", targetDir } );          
        process.waitFor();
        process.destroy();        
        

        this.xmlResult = "<FDTUploadUuid uuid='" + uuidStr + "'/>";
        
      }
      
      

      if (isValid()) {
        setResponsePage(this.SUCCESS_JSP);
      } else {
        setResponsePage(this.ERROR_JSP);
      }

    } catch (Exception e){
      log.error("An exception has occurred in FastDataTransferUploadStart", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        this.getSecAdvisor().closeReadOnlyHibernateSession();
      } catch(Exception e) {

      }
    }

    return this;	  

  }

}