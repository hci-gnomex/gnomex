package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.framework.security.UnknownPermissionException;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.Analysis;
import hci.gnomex.model.AnalysisFile;
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
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;


public class FastDataTransferUploadAnalysisProcess extends GNomExCommand implements Serializable {

	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(FastDataTransferUploadAnalysisProcess.class);

	private String serverName;
	private Integer idAnalysis = null;
	private String uuid = null;

	public void validate() {
	}

	public void loadCommand(HttpServletRequest request, HttpSession session) {
		serverName = request.getServerName();
		if (request.getParameter("idAnalysis") != null && !request.getParameter("idAnalysis").equals("")) {
			idAnalysis = new Integer(request.getParameter("idAnalysis"));
		}
		if (idAnalysis == null) {
			this.addInvalidField("idAnalysis", "idAnalysis must be provided");
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
			SecurityAdvisor secAdvisor = this.getSecAdvisor();
			Session sess = secAdvisor.getHibernateSession(this.getUsername());
			
		    DictionaryHelper dh = DictionaryHelper.getInstance(sess);

			Analysis analysis = (Analysis)sess.get(Analysis.class, idAnalysis);
			
			
	        if (secAdvisor.canUpdate(analysis)) {	 	
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
				String createYear = formatter.format(analysis.getCreateDate());

				// Get folder to retrieve files from		
				String softlinks_dir = PropertyHelper.getInstance(sess).getSoftLinksDirectory(serverName)+uuid;	
				File fFile = new File(softlinks_dir);
				
				
				// Set up folder (and any necessary parent folders) that files will be moved to
				String baseDir = dh.getAnalysisWriteDirectory(serverName) + createYear;
				if (!new File(baseDir).exists()) {
					File dir = new File(baseDir);
					boolean success = dir.mkdir();
					if (!success) {
			            this.addInvalidField("Directory create error", "Unable to create base directory " + baseDir);
			            setResponsePage(this.ERROR_JSP); 					
						return this;
					} 
					dir.setWritable(true, false);
				}

				String directoryName = baseDir + System.getProperty("file.separator") + analysis.getNumber();
				if (!new File(directoryName).exists()) {
					File dir = new File(directoryName);
					boolean success = dir.mkdir();
					if (!success) {
			            this.addInvalidField("Directory create error", "Unable to create base directory " + baseDir);
			            setResponsePage(this.ERROR_JSP); 					
						return this;
					}  
					dir.setWritable(true, false);
				}			

                String source = softlinks_dir+System.getProperty("file.separator");
                String destination = directoryName+System.getProperty("file.separator");
                
        		File srcDir = new File(source);  
        		File[] files = srcDir.listFiles();  
        		FileChannel in = null;  
        		FileChannel out = null; 
        		
        		for (File file : files) {
        			String fileName = file.getName();
        			String thisDestFilePath = destination + fileName;
        			File toFile = new File(thisDestFilePath);
        			if(!file.renameTo(toFile)) {
        				// If move doesn't work then do a copy/delete
        				try {  
        					in = new FileInputStream(file).getChannel();  
        					File outFile = new File(destination, file.getName());  
        					out = new FileOutputStream(outFile).getChannel(); 
        					in.transferTo(0, in.size(), out);
        					in.close();
        					in = null;
        					out.close();
        					out = null;	
        					file.delete();
        				} finally {  
        					if (in != null)  
        						in.close();  
        					if (out != null)  
        						out.close();  
        				}				
        			}
        			
                    // Save analysis file (name) in db
                    AnalysisFile analysisFile = null;                                  
                    
                    for(Iterator i = analysis.getFiles().iterator(); i.hasNext();) {
                      AnalysisFile af = (AnalysisFile)i.next();
                      if (af.getFileName().equals(fileName)) {
                        analysisFile = af;
                        break;
                      }
                    }
                    if (analysisFile == null) {
                      analysisFile = new AnalysisFile();
                      analysisFile.setIdAnalysis(analysis.getIdAnalysis());
                      analysisFile.setFileName(fileName);
                    }
                    analysisFile.setUploadDate(new java.sql.Date(System.currentTimeMillis()));
                    sess.save(analysisFile);     					
        		}
                sess.flush();
        		srcDir.delete();                
                
				this.xmlResult = "<FDTExpProcess result='success'/>";

				if (isValid()) {
		            this.xmlResult = "<SUCCESS/>";
		            setResponsePage(this.SUCCESS_JSP);     
				} else {
					setResponsePage(this.ERROR_JSP);
				}	        		        	
	        } else {
	            this.addInvalidField("Insufficient permissions", "Insufficient write permissions for user " + secAdvisor.getUserLastName() + ", " + secAdvisor.getUserFirstName());
	            setResponsePage(this.ERROR_JSP); 					
				return this;            
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
	

}