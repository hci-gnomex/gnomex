package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.utility.PropertyHelper;

import java.io.File;
import java.io.Serializable;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;


public class FastDataTransferUploadStart extends GNomExCommand implements Serializable {

  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(FastDataTransferUploadStart.class);

  private String serverName;

  public void validate() {
  }

  public void loadCommand(HttpServletRequest request, HttpSession session) {
    serverName = request.getServerName();     
  }

  public Command execute() throws RollBackCommandException {

    try {
      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());

      UUID uuid = UUID.randomUUID();
      String uuidStr = uuid.toString();

      // Directories must be created one level at a time so that permissions will be set properly in Linux			
      String softlinks_dir = PropertyHelper.getInstance(sess).getFDTDirectoryForGNomEx(serverName)+uuidStr;				

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
      

      this.xmlResult = "<FDTUploadUuid uuid='" + uuidStr + "'/>";

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