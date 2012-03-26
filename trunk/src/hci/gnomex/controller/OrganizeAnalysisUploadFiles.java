package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.Analysis;
import hci.gnomex.model.AnalysisFile;
import hci.gnomex.model.TransferLog;
import hci.gnomex.utility.AnalysisFileDescriptorUploadParser;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.File;
import java.io.Serializable;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;




public class OrganizeAnalysisUploadFiles extends GNomExCommand implements Serializable {



  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(OrganizeAnalysisUploadFiles.class);

  private Integer                      idAnalysis;
  private String                       filesXMLString;
  private Document                     filesDoc;
  private String                       filesToRemoveXMLString;
  private Document                     filesToRemoveDoc;
  private AnalysisFileDescriptorUploadParser   parser;
  private AnalysisFileDescriptorUploadParser   filesToRemoveParser;

  private String                       serverName;


  public void validate() {
  }

  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    if (request.getParameter("idAnalysis") != null && !request.getParameter("idAnalysis").equals("")) {
      idAnalysis = new Integer(request.getParameter("idAnalysis"));
    } else {
      this.addInvalidField("idAnalysis", "idAnalysis is required");
    }

    if (request.getParameter("filesXMLString") != null && !request.getParameter("filesXMLString").equals("")) {
      filesXMLString = request.getParameter("filesXMLString");

      StringReader reader = new StringReader(filesXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        filesDoc = sax.build(reader);
        parser = new AnalysisFileDescriptorUploadParser(filesDoc);
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
        filesToRemoveParser = new AnalysisFileDescriptorUploadParser(filesToRemoveDoc);
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
        sess = this.getSecAdvisor().getHibernateSession(this.getUsername());

        Analysis analysis = (Analysis)sess.load(Analysis.class, idAnalysis);
        
        String baseDir = PropertyDictionaryHelper.getInstance(sess).getAnalysisReadDirectory(serverName);
        baseDir += analysis.getCreateYear() ;

        if (this.getSecAdvisor().canUploadData(analysis)) {
          parser.parse();

          // Add new directories to the file system
          for (Iterator i = parser.getNewDirectoryNames().iterator(); i.hasNext();) {
            String directoryName = (String)i.next();
            File dir = new File(baseDir + "/" + directoryName);
            if (!dir.exists()) {
              boolean success = dir.mkdirs();
              if (!success) { 
                // Directory not successfully created
                throw new Exception("Unable to create directory " + directoryName);
              }
            }
          }

          // Move files to designated folder
          for(Iterator i = parser.getFileNameMap().keySet().iterator(); i.hasNext();) {
            
            String directoryName = (String)i.next();
            
            // Get the qualifiedFilePath (need to remove the analysis number folder from directory name)
            String []pathTokens = directoryName.split("/");
            String qualifiedFilePath = "";
            if ( pathTokens.length > 1 ) {
              qualifiedFilePath = pathTokens[1];
            }
            for (int i2 = 2; i2 < pathTokens.length; i2++ ) {
              qualifiedFilePath += "/" + pathTokens[i2];
            }
            
            List fileNames = (List)parser.getFileNameMap().get(directoryName);
            
            for(Iterator i1 = fileNames.iterator(); i1.hasNext();) {
              String fileName = (String)i1.next();
              
              // Change qualifiedFilePath if the file is registered in the db
              if ( parser.getFileIdMap().containsKey(fileName) ) {

                String idFileString = (String) parser.getFileIdMap().get(fileName);
                
                if (idFileString != null) {
                  AnalysisFile af;
                  if (!idFileString.startsWith("AnalysisFile") && !idFileString.equals("")) {
                    af = (AnalysisFile)sess.load(AnalysisFile.class, new Integer(idFileString));
                  } else {
                    af = new AnalysisFile();
                    af.setUploadDate(new java.sql.Date(System.currentTimeMillis()));
                    af.setIdAnalysis(Integer.valueOf(idAnalysis));
                    af.setFileName(new File(fileName).getName());
                    af.setBaseFilePath(baseDir + "/" + analysis.getNumber());
                  }
                  af.setFileSize(new BigDecimal(new File(fileName).length()));
                  af.setQualifiedFilePath(qualifiedFilePath);
                  sess.save(af);
                  
                }
                
              }
              sess.flush();
              
              File sourceFile = new File(fileName);
              sourceFile = sourceFile.getCanonicalFile();
              String targetDirName = baseDir + "/" + analysis.getNumber() + "/" + qualifiedFilePath;
              File targetDir = new File(targetDirName);
              targetDir = targetDir.getCanonicalFile();

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
              
              if ( td.equals(sd)) {
                continue;
              }
              
              File destFile = new File(targetDir, sourceFile.getName());
              boolean success = sourceFile.renameTo(destFile);
              
              // If the rename didn't work, check to see if the destination file was created, if so
              // delete the source file.
              if (!success) {
                if ( destFile.exists() ) {
                  if ( sourceFile.exists() ) {
                    if ( !sourceFile.delete() ) {
                      throw new Exception("Unable to move file " + fileName + " to " + targetDirName);
                    }
                  }
                } else {
                  throw new Exception("Unable to move file " + fileName + " to " + targetDirName);
                }
              }
            }
          }

          // Remove files from file system
          if (filesToRemoveParser != null) {
            filesToRemoveParser.parseFilesToRemove();
            
            for(Iterator i = filesToRemoveParser.getFilesToDeleteMap().keySet().iterator(); i.hasNext();) {

              String idFileString = (String)i.next();
              
              List fileNames = (List)filesToRemoveParser.getFilesToDeleteMap().get(idFileString);

              for(Iterator i1 = fileNames.iterator(); i1.hasNext();) {
                String fileName = (String)i1.next();


                // Remove references of file in TransferLog
                String queryBuf = "SELECT tl from TransferLog tl where tl.idAnalysis = " + idAnalysis + " AND tl.fileName like '%" + new File(fileName).getName() + "'";
                List transferLogs = sess.createQuery(queryBuf).list();

                // Go ahead and delete the transfer log if there is just one row.
                // If there are multiple transfer log rows for this filename, just
                // bypass deleting the transfer log since it is not possible
                // to tell which entry should be deleted.
                if (transferLogs.size() == 1) {
                  TransferLog transferLog = (TransferLog)transferLogs.get(0);
                  sess.delete(transferLog);
                } 

                // Delete the file from the DB
                if (idFileString != null) {
                  AnalysisFile af;
                  if (!idFileString.startsWith("AnalysisFile") && !idFileString.equals("")) {
                    af = (AnalysisFile)sess.load(AnalysisFile.class, new Integer(idFileString));
                    Set aFiles = analysis.getFiles();
                    analysis.getFiles().remove(af);
                  } 
                }
                sess.flush();

                // Delete the file from the file system
                if (new File(fileName).exists() ) {
                  File deleteFile = new File(fileName);
                  
                  if (deleteFile.isDirectory()) {
                    List childrenFiles =  Arrays.asList(deleteFile.listFiles());
                    for (Iterator i2 = childrenFiles.iterator(); i2.hasNext();) {
                      File childFile = (File) i2.next();
                      boolean successDel = childFile.delete();
                      if (!successDel) { 
                        // File was not successfully deleted
                        throw new Exception("Unable to delete file " + childFile.getName());
                      }
                    }
                  }
                  
                  boolean success = new File(fileName).delete();
                  if (!success) { 
                    // File was not successfully deleted
                    throw new Exception("Unable to delete file " + fileName);
                  }
                }
                
                sess.flush();
              }
            }
          }
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

  protected AnalysisFile initializeFile(String idAnalysisFileString, Session sess){

    AnalysisFile af = null;
    if (!idAnalysisFileString.startsWith("AnalysisFile") && !idAnalysisFileString.equals("")) {
      af = (AnalysisFile)sess.load(AnalysisFile.class, new Integer(idAnalysisFileString));
    } else {
      af = new AnalysisFile();
      af.setUploadDate(new java.sql.Date(System.currentTimeMillis()));
    }
    af.setIdAnalysis(Integer.valueOf(idAnalysis));
    return af;

  }



}