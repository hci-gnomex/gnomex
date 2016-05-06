package hci.gnomex.utility;


import hci.framework.model.DetailObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jdom.Document;
import org.jdom.Element;


public class ProductOrderFileDescriptorParser extends DetailObject implements Serializable {
  
  protected Document   doc;
  protected Map        fileDescriptorMap = new HashMap();
  
  public ProductOrderFileDescriptorParser(Document doc) {
    this.doc = doc;
 
  }
  
  public void parse() throws Exception{
    
    Element root = this.doc.getRootElement();
    
    
    for(Iterator i = root.getChildren("FileDescriptor").iterator(); i.hasNext();) {
      Element node = (Element)i.next();      
      String idProductOrder = node.getAttributeValue("idProductOrder");
      FileDescriptor fd = initializeFileDescriptor(node);
      
      List fileDescriptors = (List)fileDescriptorMap.get(idProductOrder);
      if (fileDescriptors == null) {
        fileDescriptors = new ArrayList();
        fileDescriptorMap.put(idProductOrder, fileDescriptors);
      }
      
      fileDescriptors.add(fd);
      getChildrenFileDescriptors(node, fd);
      
    }
    
   
  }
  
  private void getChildrenFileDescriptors(Element parentNode, FileDescriptor parentFileDescriptor) {

    if (parentNode.getChildren("children") != null && parentNode.getChildren("children").size() > 0) {
      for(Iterator i = parentNode.getChild("children").getChildren("FileDescriptor").iterator(); i.hasNext();) {
        Element node = (Element)i.next();      
        String idProductOrder = node.getAttributeValue("idProductOrder");
        FileDescriptor fd = initializeFileDescriptor(node);
        
        List fileDescriptors = (List)fileDescriptorMap.get(idProductOrder);
        if (fileDescriptors == null) {
          fileDescriptors = new ArrayList();
          fileDescriptorMap.put(idProductOrder, fileDescriptors);
        }
        
        fileDescriptors.add(fd);
        
        getChildrenFileDescriptors(node, fd);
        
      }
      
    }
    
  }
  
  protected FileDescriptor initializeFileDescriptor(Element n){
    FileDescriptor fd = new FileDescriptor();
    
    fd.setFileName(n.getAttributeValue("fileName"));
    fd.setZipEntryName(n.getAttributeValue("zipEntryName"));
    fd.setType(n.getAttributeValue("type"));
    fd.setNumber(n.getAttributeValue("productOrderNumber"));
    fd.setId(Integer.valueOf(n.getAttributeValue("idProductOrder")));
    if(n.getAttributeValue("fileSize").length() > 0) {
        long fileSize = Long.parseLong(n.getAttributeValue("fileSize"));
        fd.setFileSize(fileSize);    	
    }
    
    return fd;

  }

  
  public Set getProductOrderIds() {
    return fileDescriptorMap.keySet();
  }

  
  public List getFileDescriptors(String idProductOrder) {
    return (List)fileDescriptorMap.get(idProductOrder);
  }
  


}
