package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.ProductOrder;
import hci.gnomex.model.ProductOrderFile;
import hci.gnomex.model.TransferLog;
import hci.gnomex.utility.ProductOrderFileDescriptorUploadParser;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.apache.log4j.Logger;



public class OrganizeProductOrderUploadFiles extends GNomExCommand implements Serializable {



  // the static field for logging in Log4J
  private static Logger LOG = Logger.getLogger(OrganizeProductOrderUploadFiles.class);

  private Integer                      idProductOrder;
  private String                       filesXMLString;
  private Document                     filesDoc;
  private String                       filesToRemoveXMLString;
  private Document                     filesToRemoveDoc;
  private ProductOrderFileDescriptorUploadParser   parser;
  private ProductOrderFileDescriptorUploadParser   filesToRemoveParser;

  private String                       serverName;


  public void validate() {
  }


  public void loadCommand(HttpServletRequest request, HttpSession session) {

    if (request.getParameter("idProductOrder") != null && !request.getParameter("idProductOrder").equals("")) {
      idProductOrder = new Integer(request.getParameter("idProductOrder"));
    } else {
      this.addInvalidField("idProductOrder", "idProductOrder is required");
    }

    if (request.getParameter("filesXMLString") != null && !request.getParameter("filesXMLString").equals("")) {
      filesXMLString = request.getParameter("filesXMLString");

      StringReader reader = new StringReader(filesXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        filesDoc = sax.build(reader);
        parser = new ProductOrderFileDescriptorUploadParser(filesDoc);
      } catch (JDOMException je ) {
        LOG.error( "Cannot parse filesXMLString", je );
        this.addInvalidField( "FilesLXMLString", "Invalid files xml");
      }
    }

    if (request.getParameter("filesToRemoveXMLString") != null && !request.getParameter("filesToRemoveXMLString").equals("")) {
      filesToRemoveXMLString = "<FilesToRemove>" + request.getParameter("filesToRemoveXMLString") +  "</FilesToRemove>";

      StringReader reader = new StringReader(filesToRemoveXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        filesToRemoveDoc = sax.build(reader);
        filesToRemoveParser = new ProductOrderFileDescriptorUploadParser(filesToRemoveDoc);
      } catch (JDOMException je ) {
        LOG.error( "Cannot parse filesToRemoveXMLString", je );
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

        ProductOrder productOrder = (ProductOrder)sess.load(ProductOrder.class, idProductOrder);

        String baseDir = PropertyDictionaryHelper.getInstance(sess).getDirectory(serverName, productOrder.getIdCoreFacility(), PropertyDictionaryHelper.PROPERTY_PRODUCT_ORDER_DIRECTORY);
        baseDir += productOrder.getCreateYear() ;

         
          parser.parse();

          // Add new directories to the file system
          for (Iterator i = parser.getNewDirectoryNames().iterator(); i.hasNext();) {
            String directoryName = (String)i.next();
            File dir = new File(baseDir + File.separator + directoryName);
            if (!dir.exists()) {
              boolean success = dir.mkdirs();
              if (!success) { 
                // Directory not successfully created
                throw new Exception("Unable to create directory " + directoryName);
              }
            }
          }

          //Rename files for(Iterator i = parser.getFilesToRenameMap().keySet().iterator(); i.hasNext();)
          Object [] keys = parser.getFilesToRenameMap().keySet().toArray();
          for(int i = keys.length - 1; i >= 0; i--) {
            String file = (String)keys[i];
            File f1 = new File(file);
            String [] contents = (String[]) parser.getFilesToRenameMap().get(file);
            File f2 = new File(contents[0]);
            String idFileString = contents[1];
            String qualifiedFilePath = contents[2];
            String displayName = contents[3];

            if(!f1.renameTo(f2)){
              throw new Exception("Error Renaming File");
            }
            else{
              // Rename the files in the DB
              if (idFileString != null) {
                ProductOrderFile pof;
                if (!idFileString.startsWith("ProductOrderFile") && !idFileString.equals("")) {
                  pof = (ProductOrderFile)sess.load(ProductOrderFile.class, new Integer(idFileString));
                  pof.setFileName(displayName);
                  pof.setBaseFilePath(f2.getCanonicalPath());
                  pof.setQualifiedFilePath(qualifiedFilePath);
                  sess.save(pof);
                  sess.flush();
                } else if(idFileString.startsWith("ProductOrderFile") && !f2.exists()){
                  pof = new ProductOrderFile();
                  pof.setFileName(displayName);
                  pof.setBaseFilePath(f2.getCanonicalPath());
                  pof.setQualifiedFilePath(qualifiedFilePath);
                  sess.save(pof);
                  sess.flush();
                }
                else{
                  for(Iterator j = parser.getChildrenToMoveMap().keySet().iterator(); j.hasNext();){
                    String oldFileName = (String)j.next();
                    String [] afParts = (String [])parser.getChildrenToMoveMap().get(oldFileName);
                    if(afParts[1].startsWith("ProductOrderFile")){
                      continue;
                    }
                    pof = (ProductOrderFile)sess.load(ProductOrderFile.class, new Integer(afParts[1]));
                    pof.setFileName(afParts[3]);
                    pof.setBaseFilePath(afParts[0]);

                    String [] filePath = afParts[0].split("/");
                    pof.setQualifiedFilePath(filePath[filePath.length - 2]);

                    sess.save(pof);
                    sess.flush();


                  }
                }
              }
            }

          }

          // Move files to designated folder
          tryLater = new ArrayList();
          for(Iterator i = parser.getFileNameMap().keySet().iterator(); i.hasNext();) {

            String directoryName = (String)i.next();

            // Get the qualifiedFilePath (need to remove the productOrder number folder from directory name)
            String []pathTokens = directoryName.split("/");
            String qualifiedFilePath = "";
            if ( pathTokens.length > 1 ) {
              qualifiedFilePath = pathTokens[1];
            }
            for (int i2 = 2; i2 < pathTokens.length; i2++ ) {
              qualifiedFilePath += File.separator + pathTokens[i2];
            }

            List fileNames = (List)parser.getFileNameMap().get(directoryName);

            for(Iterator i1 = fileNames.iterator(); i1.hasNext();) {
              String fileName = (String)i1.next();
              File sourceFile = new File(fileName);
              // don't move it it doesn't exist.
              if (!sourceFile.exists()) {
                continue;
              }
              int lastIndex = fileName.lastIndexOf("\\");
              if (lastIndex == -1) {
                lastIndex = fileName.lastIndexOf("/");
              }
              String baseFileName = fileName;
              if (lastIndex != -1) {
                baseFileName = fileName.substring(lastIndex);
              }
              Boolean duplicateUpload = fileNames.contains(baseDir + "\\" + productOrder.getProductOrderNumber() + "\\" + Constants.UPLOAD_STAGING_DIR + baseFileName);
              String mostRecentFile = "";
              if(duplicateUpload){
                mostRecentFile = (String)fileNames.get(fileNames.indexOf(baseDir + "\\" + productOrder.getProductOrderNumber() + "\\" + Constants.UPLOAD_STAGING_DIR + baseFileName));
              }


              // Change qualifiedFilePath if the file is registered in the db
              if ( parser.getFileIdMap().containsKey(fileName) ) {

                String idFileString = (String) parser.getFileIdMap().get(fileName);

                if (idFileString != null) {
                  ProductOrderFile pof = new ProductOrderFile();
                  if (!idFileString.startsWith("ProductOrderFile") && !idFileString.equals("")) {
                    pof = (ProductOrderFile)sess.load(ProductOrderFile.class, new Integer(idFileString));
                  } else if(idFileString.startsWith("ProductOrderFile")){ //new File(baseDir + "\\" + analysis.getNumber() + baseFileName).exists()  WHY DO WE NEED THIS?  MAYBE WE DON"T
                    pof = new ProductOrderFile();
                    pof.setCreateDate(new java.sql.Date(System.currentTimeMillis()));
                    pof.setCreateDate(new java.sql.Date(System.currentTimeMillis()));
                    pof.setIdProductOrder(Integer.valueOf(idProductOrder));
                    pof.setFileName(new File(fileName).getName());
                    pof.setBaseFilePath(baseDir + File.separator + productOrder.getProductOrderNumber());
                  }

                  if(duplicateUpload){
                    pof.setFileSize(new BigDecimal(new File(mostRecentFile).length()));
                    Boolean firstUpload = true;
                    while(i1.hasNext()){
                      String test = (String) i1.next();
                      if(test.equals(mostRecentFile)){
                        i1.remove();
                        i1 = fileNames.iterator();
                        new File(mostRecentFile).delete();
                        firstUpload = false;
                        break;
                      }
                    }
                    if(firstUpload){
                      i1 = fileNames.iterator();
                      i1.next();
                      i1.remove();
                      i1 = fileNames.iterator();
                    }
                  }
                  else{
                    if(new File(fileName).exists()){
                      pof.setFileSize(new BigDecimal(new File(fileName).length()));
                    }
                  }
                  pof.setQualifiedFilePath(qualifiedFilePath);
                  sess.save(pof);

                }

              }
              sess.flush();

              sourceFile = sourceFile.getCanonicalFile();
              String targetDirName = baseDir + File.separator + productOrder.getProductOrderNumber() + File.separator + qualifiedFilePath;
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
                String queryBuf = "SELECT tl from TransferLog tl where tl.idProductOrder = :idProductOrder AND tl.fileName like :fileName";
                Query query = sess.createQuery(queryBuf);
                query.setParameter("idProductOrder", idProductOrder);
                query.setParameter("fileName", "%" + new File(fileName).getName());
                List transferLogs = query.list();

                // Go ahead and delete the transfer log if there is just one row.
                // If there are multiple transfer log rows for this filename, just;
                // bypass deleting the transfer log since it is not possible
                // to tell which entry should be deleted.
                if (transferLogs.size() == 1) {
                  TransferLog transferLog = (TransferLog)transferLogs.get(0);
                  sess.delete(transferLog);
                } 

                // Delete the file from the DB
                if (idFileString != null) {
                  ProductOrderFile pof;
                  if (!idFileString.startsWith("ProductOrderFile") && !idFileString.equals("")) {
                    pof = (ProductOrderFile)sess.load(ProductOrderFile.class, new Integer(idFileString));
                    productOrder.getFiles().remove(pof);
                  } 
                }
                sess.flush();

                // Delete the file from the file system
                if (new File(fileName).exists() ) {
                  File deleteFile = new File(fileName);

                  if (deleteFile.isDirectory()) {
                    if(!deleteDir(deleteFile)){
                      throw new Exception("Unable to delete files");
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

          //clean up ghost files
          String queryBuf = "SELECT pof from ProductOrderFile pof where pof.idProductOrder = :idProductOrder";
          Query query = sess.createQuery(queryBuf);
          query.setParameter("idProductOrder", idProductOrder);
          List ghostFiles = query.list();

          for(Iterator i = ghostFiles.iterator(); i.hasNext();) {
            ProductOrderFile pof  = (ProductOrderFile) i.next();
            String filePath = pof.getBaseFilePath() + File.separator + pof.getQualifiedFilePath() + File.separator + pof.getFileName();

            if(!new File(filePath).exists()) {
              productOrder.getFiles().remove(pof);
            }
          }

          sess.flush();

          XMLOutputter out = new org.jdom.output.XMLOutputter();
          this.xmlResult = "<SUCCESS/>";
          setResponsePage(this.SUCCESS_JSP);          

      }catch (Exception e){
        LOG.error("An exception has occurred in OrganizeProdutOrderUploadFiles ", e);

        throw new RollBackCommandException(e.getMessage());

      }finally {
        try {
          if (sess != null) {
            this.getSecAdvisor().closeHibernateSession();
          }
        } catch(Exception e){
        LOG.error("Error", e);
      }
      }

    } else {
      this.xmlResult = "<SUCCESS/>";
      setResponsePage(this.SUCCESS_JSP);
    }

    return this;
  }

  private Boolean deleteDir(File childFile) throws IOException{
    for(String f : childFile.list()){
      File delFile = new File(childFile.getCanonicalPath() + "/" + f);
      if(delFile.isDirectory()){
        deleteDir(delFile);
        if(!delFile.delete()){
          return false;
        }
      }
      else{
        if(!delFile.delete()){
          return false;
        }
      }
    }

    return true;

  }



}