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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.channels.FileChannel;
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


public class FastDataTransferUploadExpProcess extends GNomExCommand implements Serializable {

	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(FastDataTransferUploadExpProcess.class);

	private String serverName;
	private Integer idRequest = null;
	private String uuid = null;

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
		if (request.getParameter("uuid") != null && !request.getParameter("uuid").equals("")) {
			uuid = new String(request.getParameter("uuid"));
		}
		if (uuid == null) {
			this.addInvalidField("idRequest", "idRequest must be provided");
		}      
	}

	public Command execute() throws RollBackCommandException {

		try {
			Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
			
		    DictionaryHelper dh = DictionaryHelper.getInstance(sess);

			
			Request request = (Request)sess.get(Request.class, idRequest);

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
			String createYear = formatter.format(request.getCreateDate());

			// Get folder to retrieve files from		
			String softlinks_dir = PropertyHelper.getInstance(sess).getSoftLinksDirectory(serverName)+uuid;	
			File fFile = new File(softlinks_dir);
			
			
			// Set up folder (and any necessary parent folders) that files will be moved to
			String baseDir = dh.getMicroarrayDirectoryForWriting(serverName) + createYear;
			if (!new File(baseDir).exists()) {
				boolean success = (new File(baseDir)).mkdir();
				if (!success) {
		            this.addInvalidField("Directory create error", "Unable to create base directory " + baseDir);
		            setResponsePage(this.ERROR_JSP); 					
					return this;
				}      
			}

			String directoryName = baseDir + System.getProperty("file.separator") + request.getNumber();
			if (!new File(directoryName).exists()) {
				boolean success = (new File(directoryName)).mkdir();
				if (!success) {
		            this.addInvalidField("Directory create error", "Unable to create base directory " + baseDir);
		            setResponsePage(this.ERROR_JSP); 					
					return this;
				}      
			}

			directoryName += System.getProperty("file.separator") + Constants.UPLOAD_STAGING_DIR;
			File fDest = new File(directoryName);
			if (!fDest.exists()) {
				boolean success = fDest.mkdir();
				if (!success) {
		            this.addInvalidField("Directory create error", "Unable to create base directory " + baseDir);
		            setResponsePage(this.ERROR_JSP); 					
					return this;
				}      
			}			

			moveFiles(softlinks_dir+System.getProperty("file.separator"), directoryName+System.getProperty("file.separator"));		
			
			this.xmlResult = "<FDTExpProcess result='success'/>";

			if (isValid()) {
	            this.xmlResult = "<SUCCESS/>";
	            setResponsePage(this.SUCCESS_JSP);     
			} else {
				setResponsePage(this.ERROR_JSP);
			}

		} catch (Exception e){
            this.addInvalidField("Exception", e.getMessage());
            setResponsePage(this.ERROR_JSP); 							
			throw new RollBackCommandException(e.getMessage());
		} finally {
			try {
				this.getSecAdvisor().closeReadOnlyHibernateSession();
			} catch(Exception e) {

			}
		}

		return this;	  

	}
	
	public void moveFiles(String source, String destination) throws IOException {  
		File srcDir = new File(source);  
		File[] files = srcDir.listFiles();  
		
		for (File file : files) {
			String thisSourceFilePath = source + file.getName();
			String thisDestFilePath = destination + file.getName();
			new File(thisSourceFilePath).renameTo(new File(thisDestFilePath));
		}
		srcDir.delete();
	} 	

}