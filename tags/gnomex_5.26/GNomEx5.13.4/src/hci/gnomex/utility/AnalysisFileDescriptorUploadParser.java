package hci.gnomex.utility;


import hci.framework.model.DetailObject;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.AnalysisFile;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;


public class AnalysisFileDescriptorUploadParser extends DetailObject implements Serializable {
  
  protected Document   doc;
  protected Map        fileNameMap = new HashMap();
  protected List       newDirectoryNames = new ArrayList();
  protected Map        fileIdMap = new HashMap();
  protected Map        filesToDeleteMap = new HashMap();
  protected Map        filesToRename = new HashMap();
  protected Map        childrenToMoveMap = new HashMap();
  
  public AnalysisFileDescriptorUploadParser(Document doc) {
    this.doc = doc;
 
  }
  
  public void parse() throws Exception{
    
    Element root = this.doc.getRootElement();
    
    recurseDirectories(root, null);
   
  }
  
  private void recurseDirectories(Element folderNode, String parentDir) {
    
    String directoryName = null;
    
    if (folderNode.getName().equals("Analysis")) {
      String [] fileParts = folderNode.getAttributeValue("key").split("-");
      directoryName = fileParts[2];
      
      } else {
        if (folderNode.getAttributeValue("type") != null && folderNode.getAttributeValue("type").equals("dir")) {
          directoryName= folderNode.getAttributeValue("displayName");
          
        } 
    }
    
    if (directoryName == null) {
      return;
    }

    // Create the folderNode's folder if needed
    String qualifiedDir = parentDir != null ? parentDir  + "/" + directoryName : directoryName;
    
    if (folderNode.getAttributeValue("isNew") != null && folderNode.getAttributeValue("isNew").equals("Y")) {
      newDirectoryNames.add(qualifiedDir);
    }
    
    
    for(Iterator i1 = folderNode.getChildren("AnalysisFileDescriptor").iterator(); i1.hasNext();) {
      Element childFileNode = (Element)i1.next();
      String fileName = childFileNode.getAttributeValue("fileName");
      String displayName = childFileNode.getAttributeValue("displayName");
      if(fileName == null){
        continue;
      }
      fileName = fileName.replace("\\", "/");
      String newFileName = fileName.replace(fileName.substring(fileName.lastIndexOf("/") + 1), displayName);
      String fileIdString = childFileNode.getAttributeValue("idAnalysisFileString");
      String qualifiedFilePath = childFileNode.getAttributeValue("qualifiedFilePath");
      String [] contents = {newFileName, fileIdString, qualifiedFilePath, displayName};
      if(!newFileName.equals(fileName) && !fileName.equals("")){
        filesToRename.put(fileName, contents);
        if(childFileNode.getAttributeValue("type").equals("dir")){
          renameDirectoryChildren(childFileNode, newFileName);
        }
      }
      
      String childFileIdString = childFileNode.getAttributeValue("idAnalysisFileString");
        
      // Ignore new directories here.
      if (childFileNode.getAttributeValue("isNew") != null && childFileNode.getAttributeValue("isNew").equals("Y")) {
        continue;
      }
      
      String childFileName = childFileNode.getAttributeValue("fileName");
      if (childFileName.equals("")) {
        newDirectoryNames.add(qualifiedDir + "/" + childFileNode.getAttributeValue("displayName"));
        continue;
      }
      
      if (childFileNode.getAttributeValue("type") != null && !childFileNode.getAttributeValue("type").equals("dir")) {
        fileIdMap.put(childFileName, childFileIdString);
      }
      
      List fileNames = (List)fileNameMap.get(qualifiedDir);
      
      
      if (fileNames == null) {
        fileNames = new ArrayList();
        fileNameMap.put(qualifiedDir, fileNames);
      }
      
      fileNames.add(childFileName);
      
    }
    
    
    for(Iterator i = folderNode.getChildren("AnalysisFileDescriptor").iterator(); i.hasNext();) {
      Element childFolderNode = (Element)i.next();
      recurseDirectories(childFolderNode, qualifiedDir);
    }
    
  }
  
  
  public List getNewDirectoryNames() {
    return newDirectoryNames;
  }
  
  private void renameDirectoryChildren(Element childFileNode, String newName){
    for(Element e : (List<Element>)childFileNode.getChildren()){
      String displayName = e.getAttributeValue("displayName");
      String fileName = e.getAttributeValue("fileName").replace("\\", "/");
      String newFileName = newName + "/" + displayName;//fileName.replace(fileName.substring(fileName.lastIndexOf("/") + 1), newName) + "/" + displayName  ;
      String fileIdString = e.getAttributeValue("idAnalysisFileString");
      String qualifiedFilePath = newName.substring(newName.lastIndexOf("/") + 1);
      String [] contents = {newFileName, fileIdString, qualifiedFilePath, displayName};
 
      childrenToMoveMap.put(fileName, contents);
      
      if(e.hasChildren()){
        renameDirectoryChildren(e, newFileName);
      }
    }
    
  }
  
  public void parseFilesToRemove() throws Exception {
    
    Element root = this.doc.getRootElement();
    for(Iterator i = root.getChildren().iterator(); i.hasNext();) {
      Element node = (Element)i.next();
      System.out.println("ready to remove fileName " + node.getAttributeValue("fileName"));
      
      String fileIdString = node.getAttributeValue("idAnalysisFileString");
      String fileName = node.getAttributeValue("fileName");
      
      List fileNames = (List)filesToDeleteMap.get(fileIdString);
      
      if (fileNames == null) {
        fileNames = new ArrayList();
        filesToDeleteMap.put(fileIdString, fileNames);
      }

      fileNames.add(fileName);
      
    }
  }
  
  public Map getFileNameMap() {
    return fileNameMap;
  }

  public Map getFileIdMap() {
    return fileIdMap;
  }
  
  public Map getFilesToDeleteMap() {
    return filesToDeleteMap;
  }
  
  public Map getFilesToRenameMap(){
    return filesToRename;
  }
  
  public Map getChildrenToMoveMap(){
    return childrenToMoveMap;
  }

}
