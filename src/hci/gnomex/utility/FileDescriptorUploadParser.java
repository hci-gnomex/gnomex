package hci.gnomex.utility;


import hci.framework.model.DetailObject;
import hci.gnomex.constants.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;


public class FileDescriptorUploadParser extends DetailObject implements Serializable {
  
  protected Document   doc;
  protected Map        fileNameMap = new HashMap();
  
  public FileDescriptorUploadParser(Document doc) {
    this.doc = doc;
 
  }
  
  public void parse() throws Exception{
    
    Element root = this.doc.getRootElement();
    
    
    for(Iterator i = root.getChildren("RequestDownload").iterator(); i.hasNext();) {
      Element folderNode = (Element)i.next();      
      String requestNumber = folderNode.getAttributeValue("requestNumber");
      String []keyTokens = folderNode.getAttributeValue("key").split("-");
      String directoryName = keyTokens[3];
      
      for(Iterator i1 = folderNode.getChildren("FileDescriptor").iterator(); i1.hasNext();) {
        Element fileNode = (Element)i1.next();
        if (fileNode.getAttributeValue("directoryName").equals(Constants.UPLOAD_STAGING_DIR)) {
          List fileNames = (List)fileNameMap.get(directoryName);
          if (fileNames == null) {
            fileNames = new ArrayList();
            fileNameMap.put(directoryName, fileNames);
          }
          fileNames.add(fileNode.getAttributeValue("displayName"));
        }
      }
      
    }
    
   
  }
  
  public List parseFilesToRemove() throws Exception {
    ArrayList fileNames = new ArrayList();
    
    Element root = this.doc.getRootElement();
    for(Iterator i = root.getChildren("FileDescriptor").iterator(); i.hasNext();) {
      Element node = (Element)i.next();
      fileNames.add(node.getAttributeValue("fileName"));
    }

    return fileNames;
    
  }
  
  public Map getFileNameMap() {
    return fileNameMap;
  }


}
