package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.framework.security.UnknownPermissionException;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.Price;
import hci.gnomex.model.Request;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.PropertyHelper;

import java.io.File;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;


public class FastDataTransferUploadExpStart extends GNomExCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(FastDataTransferUploadExpStart.class);
  
  private String serverName;
  private Integer idRequest;
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
	  serverName = request.getServerName();
      if (request.getParameter("idRequest") != null && !request.getParameter("idRequest").equals("")) {
          idRequest = new Integer(request.getParameter("idRequest"));
      }
      if (idRequest == null) {
          this.addInvalidField("idRequest", "idRequest must be provided");
      }      
  }

  public Command execute() throws RollBackCommandException {
	  
	    try {
	        Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
	        
	        Request request = (Request)sess.get(Request.class, idRequest);
	        
	        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
	        String createYear = formatter.format(request.getCreateDate());
        	        
	        UUID uuid = UUID.randomUUID();
			String uuidStr = uuid.toString();
	        
			String softlinks_dir = PropertyHelper.getInstance(sess).getSoftLinksDirectory(serverName)+uuidStr			
									+ System.getProperty("file.separator") + createYear
									+ System.getProperty("file.separator") + request.getNumber()
									+ System.getProperty("file.separator") + Constants.UPLOAD_STAGING_DIR;	
			File dir = new File(softlinks_dir);
		    boolean isDirCreated = dir.mkdirs();  
			if (!isDirCreated) {
				this.addInvalidField("Error.", "Unable to create " + softlinks_dir + " directory.");		
			}	        
			
	        this.xmlResult = "<FDTExpUpload uuid='" + uuidStr + "'/>";
	        
	        if (isValid()) {
	          setResponsePage(this.SUCCESS_JSP);
	        } else {
	          setResponsePage(this.ERROR_JSP);
	        }
	      
	      } catch (Exception e){
	        log.error("An exception has occurred in FastDataTransferStartUpload ", e);
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