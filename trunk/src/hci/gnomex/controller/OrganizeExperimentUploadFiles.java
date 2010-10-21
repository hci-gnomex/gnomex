package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.Request;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.FileDescriptorUploadParser;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.PropertyHelper;

import java.io.File;
import java.io.Serializable;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;




public class OrganizeExperimentUploadFiles extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(OrganizeExperimentUploadFiles.class);
  
  private Integer                      idRequest;
  private String                       filesXMLString;
  private Document                     filesDoc;
  private String                       filesToRemoveXMLString;
  private Document                     filesToRemoveDoc;
  private FileDescriptorUploadParser   parser;
  private FileDescriptorUploadParser   filesToRemoveParser;
  
  private String                       serverName;
  
  private DictionaryHelper             dictionaryHelper = null;
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    if (request.getParameter("idRequest") != null && !request.getParameter("idRequest").equals("")) {
      idRequest = new Integer(request.getParameter("idRequest"));
    } else {
      this.addInvalidField("idRequest", "idRequest is required");
    }
    
    if (request.getParameter("filesXMLString") != null && !request.getParameter("filesXMLString").equals("")) {
      filesXMLString = request.getParameter("filesXMLString");
      
      StringReader reader = new StringReader(filesXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        filesDoc = sax.build(reader);
        parser = new FileDescriptorUploadParser(filesDoc);
      } catch (JDOMException je ) {
        log.error( "Cannot parse filesXMLString", je );
        this.addInvalidField( "FilesLXMLString", "Invalid files xml");
      }
    }
    
    if (request.getParameter("filesToRemoveXMLString") != null && !request.getParameter("filesToRemoveXMLString").equals("")) {
      filesToRemoveXMLString = "<FilesToRemove>" + request.getParameter("filesToRemoveXMLString") +  "</FilesToRemove>";
      
      StringReader reader = new StringReader(filesToRemoveXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        filesToRemoveDoc = sax.build(reader);
        filesToRemoveParser = new FileDescriptorUploadParser(filesToRemoveDoc);
      } catch (JDOMException je ) {
        log.error( "Cannot parse filesToRemoveXMLString", je );
        this.addInvalidField( "FilesToRemoveXMLString", "Invalid filesToRemove xml");
      }
    }

    serverName = request.getServerName();
    
  }

  public Command execute() throws RollBackCommandException {
    
    Session sess = null;
    if (filesXMLString != null) {
      try {
        sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
        
        Request request = (Request)sess.load(Request.class, idRequest);
        String baseDir = PropertyHelper.getInstance(sess).getMicroarrayDirectoryForWriting(serverName);
        baseDir += "/" + request.getCreateYear() + "/" + Request.getBaseRequestNumber(request.getNumber());
        
        
        if (this.getSecAdvisor().canUpdate(request)) {
          
          if (request.getIsExternal() != null && request.getIsExternal().equals("Y")) {
            parser.parse();
            
            // Add new directories to the file system
            for (Iterator i = parser.getNewDirectoryNames().iterator(); i.hasNext();) {
              String directoryName = (String)i.next();
              File dir = new File(baseDir + File.separator + directoryName);
              if (!dir.exists()) {
                boolean success = dir.mkdirs();
                if (!success) { 
                  // File was not successfully deleted
                  throw new Exception("Unable to create directory " + directoryName);
                }
                
              }
            }
            
            
            // Move files to designated folder
            for(Iterator i = parser.getFileNameMap().keySet().iterator(); i.hasNext();) {
              String directoryName = (String)i.next();
              List fileNames = (List)parser.getFileNameMap().get(directoryName);
              
              for(Iterator i1 = fileNames.iterator(); i1.hasNext();) {
                String fileName = (String)i1.next();
                
                File sourceFile = new File(fileName);
                String targetDirName = baseDir + File.separator + directoryName;
                File targetDir = new File(targetDirName);
                
                // Don't try to move if the file is in the same directory
                String td = targetDirName.replaceAll("\\\\", "_");
                td = td.replaceAll("/", "_");
                td = td.replaceAll("__", "_");
                String spath = sourceFile.getAbsolutePath().replaceAll("\\\\", "_");
                spath = spath.replaceAll("/", "_");
                spath = spath.replaceAll("__", "_");
                
                if (spath.startsWith(td)) {
                  continue;
                }
                
                boolean success = sourceFile.renameTo(new File(targetDir, sourceFile.getName()));
                if (!success) {
                  // File was not successfully moved
                  throw new Exception("Unable to move file " + fileName + " to " + targetDirName);
                }
                
              }
              
            }
            
            // Remove files from file system
            if (filesToRemoveParser != null) {
              for (Iterator i = filesToRemoveParser.parseFilesToRemove().iterator(); i.hasNext();) {
                String fileName = (String)i.next();
                boolean success = new File(fileName).delete();
                if (!success) { 
                  // File was not successfully deleted
                  throw new Exception("Unable to delete file " + fileName);
                }
              }
            }
            XMLOutputter out = new org.jdom.output.XMLOutputter();
            this.xmlResult = "<SUCCESS/>";
            setResponsePage(this.SUCCESS_JSP);          
            
          } else {
            this.addInvalidField("Not external experiment", "This command is only valid for external experiments");
            setResponsePage(this.ERROR_JSP);           
          }
        } else {
          this.addInvalidField("Insufficient permissions", "Insufficient permission to organize uploaded files");
          setResponsePage(this.ERROR_JSP);
        }


      }catch (Exception e){
        log.error("An exception has occurred in OrganizeExperimentUploadFiles ", e);
        e.printStackTrace();
        throw new RollBackCommandException(e.getMessage());
          
      }finally {
        try {
          if (sess != null) {
            this.getSecAdvisor().closeReadOnlyHibernateSession();
          }
        } catch(Exception e) {
          
        }
      }
      
    } else {
      this.xmlResult = "<SUCCESS/>";
      setResponsePage(this.SUCCESS_JSP);
    }
    
    return this;
  }
  

  

}