package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.Analysis;
import hci.gnomex.model.Request;
import hci.gnomex.model.TransferLog;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.PropertyHelper;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;




public class SaveTransferLog extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveTransferLog.class);

  private String      serverName;
  private String      number;
  private Integer     idLab;
  private Integer     idAnalysis;
  private Integer     idRequest;

  
  private TransferLog transferLog;
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    transferLog = new TransferLog();
    HashMap errors = this.loadDetailObject(request, transferLog);
    this.addInvalidFields(errors);
    
    serverName = request.getServerName();
    


  }

  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      
      String fdtDirectory = PropertyHelper.getInstance(sess).GetFDTDirectory(serverName);
      
      
      HashMap filePartMap  = parseFileName(transferLog.getFileName(), fdtDirectory);
      String number        = (String)filePartMap.get("number");
      String baseFilePath  = (String)filePartMap.get("baseFilePath");

      
      if (this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_WRITE_ANY_OBJECT)) {
        
        idLab = null;
        idAnalysis = null;
        idRequest = null;
        java.util.Date estimatedUploadTime = null;
        if (number.startsWith("A")) {
          List results = sess.createQuery("SELECT a from Analysis a where a.number = '" + number + "'").list();
          if (results.size() == 0) {
            throw new Exception("cannot find analysis " + number);
          }
          Analysis a = (Analysis)results.get(0);
          idLab = a.getIdLab();
          idAnalysis = a.getIdAnalysis();
          estimatedUploadTime = a.getCreateDate();
        } else {
          List results = sess.createQuery("SELECT r from Request r where r.number like '" + number + "%'").list();
          if (results.size() == 0) {
            throw new Exception("cannot find experiment " + number);
          }
          Request r = (Request)results.get(0);
          idLab = r.getIdLab();
          idRequest = r.getIdRequest();
          estimatedUploadTime = r.getCompletedDate() != null ? r.getCompletedDate() : r.getCreateDate();
        }
        
        transferLog.setFileName(number + "/" + baseFilePath);
        transferLog.setIdLab(idLab);
        transferLog.setIdAnalysis(idAnalysis);
        transferLog.setIdRequest(idRequest);
        transferLog.setPerformCompression("N");
        
        // If we can't rely on the start date time, use the request or analysis completed 
        // (or created) date for the start time
        if (transferLog.getStartDateTime() == null) {
          transferLog.setStartDateTime(estimatedUploadTime);
        }
        
        sess.save(transferLog);
        sess.flush();

        this.xmlResult = "<SUCCESS idTransferLog=\"" + transferLog.getIdTransferLog() + "\"/>";
      
        setResponsePage(this.SUCCESS_JSP);
      
      } else {
        this.addInvalidField("Insufficient permissions", "Insufficient permission to insert transfer log.");
        setResponsePage(this.ERROR_JSP);
      }
      
    }catch (Exception e){
      log.error("An exception has occurred in SaveTransferLog ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }finally {
      try {
        HibernateSession.closeSession();        
      } catch(Exception e) {
        
      }
    }
    
    return this;
  }

  private static HashMap parseFileName(String fileName, String fdt_directory) throws Exception {
    HashMap filePartMap = new HashMap();
    // Parse the fileName to figure out the analysis or request number
    // which will be part after the fdt directory and the uuid
    if (!fileName.startsWith(fdt_directory)) {
      throw new RuntimeException("unexpected file path pattern - cannot find fdt_directory " + fdt_directory + " for file " + fileName);
    }
    
    String filePathWithUUID = fileName.substring(fdt_directory.length());  
    String[] tokens = filePathWithUUID.split("[\\\\|/]+");
    if (tokens.length <= 1) {
      throw new RuntimeException("unexpected file path pattern - cannot split file path parts " + filePathWithUUID);
    }
    String number = null;
    String uuid = null;
    String baseFilePath = "";
    int x = 0;
    for(x = 0; x < tokens.length; x++) {        
      if (tokens[x] == null || tokens[x].equals("")) {
        continue;
      }
      // First non-blank token is uuid
      if (uuid == null) {
        uuid = tokens[x];
        continue;
      }
      // Second non-blank token is request or analysis number
      if (number == null) {
        number = tokens[x];
        continue;
      }
      // Rest of file parts is the base file path to log
      if (baseFilePath.length() > 0) {
        baseFilePath += "/";
      }
      baseFilePath += tokens[x];
    }
    filePartMap.put("number", number);
    filePartMap.put("baseFilePath", baseFilePath);
    return filePartMap;
  }

 }