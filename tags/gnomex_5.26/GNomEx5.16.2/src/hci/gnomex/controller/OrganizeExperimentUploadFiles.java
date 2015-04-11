package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.ExperimentFile;
import hci.gnomex.model.Request;
import hci.gnomex.model.TransferLog;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.FileDescriptorUploadParser;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.File;
import java.io.Serializable;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
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
    ArrayList tryLater = null;
    if (filesXMLString != null) {
      try {
        sess = this.getSecAdvisor().getHibernateSession(this.getUsername());

        Request request = (Request)sess.load(Request.class, idRequest);
        String baseRequestNumber = Request.getBaseRequestNumber(request.getNumber());
        String baseDir = PropertyDictionaryHelper.getInstance(sess).getExperimentDirectory(serverName, request.getIdCoreFacility());
        baseDir += request.getCreateYear() + File.separator + Request.getBaseRequestNumber(request.getNumber());

        if (this.getSecAdvisor().canUploadData(request)) {

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
          
          //Rename files
          for(Iterator i = parser.getFilesToRenameMap().keySet().iterator(); i.hasNext();) {
            String file = (String)i.next();
            String newFileName = (String)parser.getFilesToRenameMap().get(file);
            File f1 = new File(file);
            File f2 = new File(newFileName);
            boolean success = f1.renameTo(f2);
            if(success){
              for(Iterator k = parser.getFileNameMap().keySet().iterator(); k.hasNext();) {
                String directory = (String)k.next();
                List fileNames = (List)parser.getFileNameMap().get(directory);
                for(Iterator i1 = fileNames.iterator(); i1.hasNext();) {
                  String parserFile = (String)i1.next();
                  if(parserFile.equals(file)){
                    fileNames.remove(parserFile);
                    fileNames.add(f2.getCanonicalPath());
                    parser.getFileNameMap().put(directory, fileNames);
                    break;
                  }
                }
              }
              //Update experiment file name if registered in the db
              String oldExpFileName = file.substring(file.indexOf(baseRequestNumber)).replace("\\", "/"); //REMOVE REPLACE AFTER DEBUGGING
              String newExpFileName = newFileName.substring(newFileName.indexOf(baseRequestNumber)).replace("\\", "/"); //Remove replace after debugging
              List expFiles = sess.createQuery("Select exp from ExperimentFile exp where fileName = " + "'" + oldExpFileName + "'").list();
              if(expFiles.size() == 1) {
                ExperimentFile ef = (ExperimentFile)expFiles.get(0);
                ef.setFileName(newExpFileName);
                sess.save(ef); 
              }
            }
            else{
              throw new Exception("Unable to rename file.  Invalid file name");
            }

          }
          
          //Rename Folders
          for(Iterator i = parser.getFoldersToRenameMap().keySet().iterator(); i.hasNext();) {
            String folder = (String)i.next();
            String newFolder = (String)parser.getFoldersToRenameMap().get(folder);
            File f1 = new File(baseDir + File.separator + folder);
            File f2 = new File(baseDir + File.separator + newFolder);
            f2.mkdir();
              for(Iterator j = parser.getFileNameMap().keySet().iterator(); j.hasNext();) {
                String directory = (String)j.next();
                if(directory.contains(folder + File.separator)){
                  parser.getFileNameMap().remove(directory);
                  j = parser.getFileNameMap().keySet().iterator();
                }
                if(directory.equals(folder)){
                  List fileNames = (List)parser.getFileNameMap().get(directory);
                  parser.getFileNameMap().remove(directory);
                  parser.getFileNameMap().put(newFolder, fileNames);
                  j = parser.getFileNameMap().keySet().iterator();
                }
              }
          }
          
          // Move files to designated folder
          tryLater = new ArrayList();
          File oldFileToDelete = null;
          for(Iterator i = parser.getFileNameMap().keySet().iterator(); i.hasNext();) {
            String directoryName = (String)i.next();
            List fileNames = (List)parser.getFileNameMap().get(directoryName);
            String targetDirName = "";

            for(Iterator i1 = fileNames.iterator(); i1.hasNext();) {
              String fileName = (String)i1.next();

              File sourceFile = new File(fileName);
              
              if(baseDir.contains(directoryName) || baseDir.contains(directoryName.subSequence(0, directoryName.length() - 1))){
                targetDirName = baseDir + File.separator;
              }
              else{
                targetDirName = baseDir + File.separator + directoryName;
              }
              File targetDir = new File(targetDirName);

              if (!targetDir.exists()) {
                boolean success = targetDir.mkdirs();
                if (!success) {
                  throw new Exception("Unable to create directory " + targetDir.getCanonicalPath());                    
                }
              }

              // Don't try to move if the file is in the same directory

              String td = targetDir.getAbsolutePath();
              String sd = sourceFile.getAbsolutePath();
              sd = sd.substring(0,sd.lastIndexOf(File.separator));
              oldFileToDelete = new File(sd);

              if ( td.equals(sd)) {
                continue;
              }

              File destFile = new File(targetDir, sourceFile.getName());
              boolean success = sourceFile.renameTo(destFile);
              
              //If we have renamed a file that is registered in the database under the ExperimentFile table, then update the ExperimentFile name
              //so that we don't do an unnecessary delete in the register files servlet.
              if(success) {
                String currentExpFileName = fileName.substring(fileName.indexOf(baseRequestNumber)).replace("\\", "/"); //REMOVE REPLACE AFTER DEBUGGING
                List expFiles = sess.createQuery("Select exp from ExperimentFile exp where fileName = " + "'" + currentExpFileName + "'").list();
                if(expFiles.size() == 1) {
                  String newExpFileName = targetDirName.substring(targetDirName.indexOf(baseRequestNumber)).replace("\\", "/"); //Remove replace after debugging
                  newExpFileName += "/" + destFile.getName();
                  ExperimentFile ef = (ExperimentFile)expFiles.get(0);
                  ef.setFileName(newExpFileName);
                  ef.setFileSize(BigDecimal.valueOf(destFile.length()));
                  sess.save(ef); 
                }
              } else {
                if ( destFile.exists() ) {
                  if ( sourceFile.exists() ) {
                    if (!sourceFile.delete() ) {
                      if(sourceFile.isDirectory()) {
                        // If can't delete directory then try again after everything has been moved
                        tryLater.add(sourceFile.getAbsolutePath());
                      } else {
                        throw new Exception("Unable to move file " + fileName + " to " + targetDirName);                        
                      }
                    }
                  }
                } else {
                  //throw new Exception("Unable to move file " + fileName + " to " + targetDirName);
                }
              }

            }
            if(oldFileToDelete != null && oldFileToDelete.exists() && oldFileToDelete.list().length == 0) {
              try {
                oldFileToDelete.delete();
              } catch (Exception e) {
                log.warn("Unable to delete " + oldFileToDelete.getAbsolutePath() + ": ");
                e.printStackTrace();
              }
            } else {
              log.warn("Unable to delete " + oldFileToDelete.getAbsolutePath() + ": directory is not empty");
            }
            
          }
          

          // Remove files from file system
          if (filesToRemoveParser != null) {
            for (Iterator i = filesToRemoveParser.parseFilesToRemove().iterator(); i.hasNext();) {
              String fileName = (String)i.next();
              File f = new File(fileName);

              // Remove references of file in TransferLog
              String queryBuf = "SELECT tl from TransferLog tl where tl.idRequest = " + idRequest + " AND tl.fileName like '%" + new File(fileName).getName() + "'";
              List transferLogs = sess.createQuery(queryBuf).list();
              // Go ahead and delete the transfer log if there is just one row.
              // If there are multiple transfer log rows for this filename, just
              // bypass deleting the transfer log since it is not possible
              // to tell which entry should be deleted.
              if (transferLogs.size() == 1) {
                TransferLog transferLog = (TransferLog)transferLogs.get(0);
                sess.delete(transferLog);
              }
              
              //The "file" might be a directory so we have to delete all of the files underneath it first
              if(f.isDirectory()){
                deleteDir(f, fileName);
              }

              if(f.exists()){
                boolean success = f.delete();
                if (!success) { 
                  // File was not successfully deleted
                  throw new Exception("Unable to delete file " + fileName);
                }
              }

            }
            sess.flush();
          }
          
          if(tryLater != null) {
            for (Iterator i = tryLater.iterator(); i.hasNext();) {
              String fileName = (String)i.next();
              File deleteFile = new File(fileName);
              if ( deleteFile.exists() ) {
                // Try to delete but don't throw error if unsuccessful.
                // Just leave it to user to sort out the problem.
                deleteFile.delete();
              }
            }
          }
          
          sess.flush();
          
          
          
          
          XMLOutputter out = new org.jdom.output.XMLOutputter();
          this.xmlResult = "<SUCCESS/>";
          setResponsePage(this.SUCCESS_JSP);          

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
            this.getSecAdvisor().closeHibernateSession();
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



  public void deleteDir(File f, String fileName) throws Exception{
    for(String file : f.list()){
      File child = new File(fileName + File.separator + file);
      if(child.isDirectory()){
        deleteDir(child, child.getCanonicalPath());
      }
      else if (!(new File(fileName + File.separator + file).delete())) {
        throw new Exception("Unable to delete file " + fileName + File.separator + file);
    }
      else{
        filesToRemoveParser.parseFilesToRemove().remove(fileName + File.separator + file);
      }
    
  }
    if(f.list().length == 0){
      if(!f.delete()){
        throw new Exception("Unable to delete file " + f.getCanonicalPath());
      }
      return;
    }
    
  }

}
