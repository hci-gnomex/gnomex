package hci.gnomex.utility;


import hci.framework.model.DetailObject;
import hci.gnomex.constants.Constants;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;


public class FileDescriptorUploadParser extends DetailObject implements Serializable {
  
  protected Document   doc;
  protected Map        fileNameMap = new LinkedHashMap();
  protected List       newDirectoryNames = new ArrayList();
  protected Map        filesToRename = new LinkedHashMap();
  protected Map        foldersToRename = new LinkedHashMap();
  
  public FileDescriptorUploadParser(Document doc) {
    this.doc = doc;
 
  }
  
  public void parse() throws Exception{
    fileNameMap = new LinkedHashMap();
    newDirectoryNames = new ArrayList();
    
    Element root = this.doc.getRootElement();
    
    
    for(Iterator i = root.getChildren("RequestDownload").iterator(); i.hasNext();) {
      Element folderNode = (Element)i.next();      
      String requestNumber = folderNode.getAttributeValue("requestNumber");
      String []keyTokens = folderNode.getAttributeValue("key").split(Constants.DOWNLOAD_KEY_SEPARATOR);
      String directoryName = keyTokens[3];
      if(folderNode.getAttribute("newName") != null && !folderNode.getAttribute("newName").equals("")){
        foldersToRename.put(directoryName, folderNode.getAttributeValue("newName"));
      }

      // Keep track of all new folders
      recurseDirectories(folderNode, null);
    }
   
    if(root.getChildren("FileDescriptor").iterator().hasNext()){
      recurseDirectories(root, null);
    }

 }
  
  private void recurseDirectories(Element folderNode, String parentDir) {
    String directoryName = null;
    if (folderNode.getName().equals("RequestDownload")) {
      String []keyTokens = folderNode.getAttributeValue("key").split(Constants.DOWNLOAD_KEY_SEPARATOR);
      directoryName = keyTokens[3];
      
    } else if (folderNode.getAttributeValue("type") != null && folderNode.getAttributeValue("type").equals("dir")) {
      directoryName = folderNode.getAttributeValue("displayName");
    }
    else if(folderNode.getName().equals("Request")){
      directoryName = folderNode.getAttributeValue("displayName");
      directoryName = directoryName.substring(0, directoryName.indexOf("R") + 1); //Strip any revision number off
    }
    
    if (directoryName == null) {
      return;
    }

    String qualifiedDir = parentDir != null ? parentDir  + File.separator + directoryName : directoryName;
    if (folderNode.getAttributeValue("isNew") != null && folderNode.getAttributeValue("isNew").equals("Y")) {
      newDirectoryNames.add(qualifiedDir);
    }
    
    // Get the files to be moved
    for(Iterator i1 = folderNode.getChildren("FileDescriptor").iterator(); i1.hasNext();) {
      Element fileNode = (Element)i1.next();
      //Check to see if we need to rename anything
      String fileName = fileNode.getAttributeValue("fileName").replaceAll("\\\\", Constants.FILE_SEPARATOR);
      String displayName = fileNode.getAttributeValue("displayName");
      String newFileName = fileName.replace(fileName.substring(fileName.lastIndexOf(Constants.FILE_SEPARATOR) + 1), displayName);
      if(!newFileName.equals(fileName) && !fileName.equals("")){
        filesToRename.put(fileName, newFileName);
      }

      // Ignore new directories here.
      if (fileNode.getAttributeValue("isNew") != null && fileNode.getAttributeValue("isNew").equals("Y")) {
        continue;
      }
      
      fileName = fileNode.getAttributeValue("fileName");
      
      List fileNames = (List)fileNameMap.get(qualifiedDir);
      if (fileNames == null) {
        fileNames = new ArrayList();
        qualifiedDir = qualifiedDir.replace("\\", Constants.FILE_SEPARATOR);
        fileNameMap.put(qualifiedDir, fileNames);
      }
      fileNames.add(fileName);
    }

    if(!folderNode.getName().equals("Request")){
      for(Iterator i = folderNode.getChildren("RequestDownload").iterator(); i.hasNext();) {
        Element childFolderNode = (Element)i.next();
        if(childFolderNode.getAttribute("newName") != null && !childFolderNode.getAttribute("newName").equals("")){
          foldersToRename.put(directoryName, childFolderNode.getAttributeValue("newName"));
        }
        recurseDirectories(childFolderNode, qualifiedDir);
      }
    }
    
    for(Iterator i = folderNode.getChildren("FileDescriptor").iterator(); i.hasNext();) {
      Element childFolderNode = (Element)i.next();
      recurseDirectories(childFolderNode, qualifiedDir);
    }
    
  }
  
  public List getNewDirectoryNames() {
    return newDirectoryNames;
  }
  
  public List parseFilesToRemove() throws Exception {
    ArrayList fileNames = new ArrayList();
    
    Element root = this.doc.getRootElement();
    for(Iterator i = root.getChildren().iterator(); i.hasNext();) {
      Element node = (Element)i.next();
//      System.out.println("ready to remove  fileName" + node.getAttributeValue("fileName"));
      fileNames.add(node.getAttributeValue("fileName"));
    }

    return fileNames;
    
  }
  
  public Map getFileNameMap() {
    return fileNameMap;
  }
  
  public Map getFilesToRenameMap(){
    return filesToRename;
  }
  
  public Map getFoldersToRenameMap(){
    return foldersToRename;
  }


}
