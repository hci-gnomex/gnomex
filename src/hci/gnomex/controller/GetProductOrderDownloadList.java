package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.ProductOrder;
import hci.gnomex.model.ProductOrderFile;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.ProductOrderFileDescriptor;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

public class GetProductOrderDownloadList extends GNomExCommand implements Serializable {

  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetProductOrderDownloadList.class);  
  private Integer idProductOrder;
  private String serverName;
  private String baseDir;
  
  private String includeUploadStagingDir = "Y";
  
  @Override
  public void loadCommand(HttpServletRequest req, HttpSession sess) {
    if(req.getParameter("idProductOrder") != null && !req.getParameter("idProductOrder").equals("")){
      idProductOrder = new Integer(req.getParameter("idProductOrder"));
    } else{
      this.addInvalidField("Missing idProductOrder", "Please select a product order");
    }
    
    if(req.getParameter("includeUploadStagingDir") != null && !req.getParameter("includeUploadStagingDir").equals("")){
      includeUploadStagingDir = req.getParameter("includeUploadStagingDir");
    }
    
    serverName = req.getServerName();

  }
  
  @Override
  public Command execute() throws RollBackCommandException {

    try{
      if(this.isValid()){
        Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());

        DictionaryHelper dh = DictionaryHelper.getInstance(sess);

        ProductOrder po = (ProductOrder)sess.load(ProductOrder.class, idProductOrder);
        
        baseDir = PropertyDictionaryHelper.getInstance(sess).getProductOrderDirectory(serverName, po.getIdCoreFacility());
        
        Document doc = new Document(new Element("ProductOrderDownloadList"));
        po.excludeMethodFromXML("getProductLineItems");
        po.excludeMethodFromXML("getBillingItemList");
        po.excludeMethodFromXML("getFiles");
        po.excludeMethodFromXML("getLab");
        po.excludeMethodFromXML("getSubmitter");
        
        
        Element poNode = po.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();
        poNode.setAttribute("displayName", po.getDisplay());
        poNode.setAttribute("idLab", po.getIdLab().toString());
        poNode.setAttribute("number", po.getProductOrderNumber());
        poNode.setAttribute("isSelected", "N");
        poNode.setAttribute("state", "unchecked");
        poNode.setAttribute("isEmpty", "N");
        
        // Hash the know product order files
        Map knownProductOrderFileMap = new HashMap();
        for(Iterator i = po.getFiles().iterator(); i.hasNext();) {
          ProductOrderFile pof = (ProductOrderFile)i.next();
          knownProductOrderFileMap.put(pof.getFileName(), pof);
        }
        
        // Now add in the files that exist on the file server
        Map productOrderMap = new TreeMap();
        Map directoryMap = new TreeMap();
        Map fileMap = new HashMap();
        List productOrderNumbers = new ArrayList<String>();
        //The analysis file code looks like it will server our purpose here for Product Orders
        getFileNamesToDownload(baseDir, po.getKey(), productOrderNumbers, productOrderMap, directoryMap, false);
    
        for(Iterator i = productOrderNumbers.iterator(); i.hasNext();) {
          String productOrderNumber = (String)i.next();
          List directoryKeys   = (List)productOrderMap.get(productOrderNumber);

          // For each directory of product orders
          for(Iterator i1 = directoryKeys.iterator(); i1.hasNext();) {

            String directoryKey = (String)i1.next();
            
            String[] dirTokens = directoryKey.split("-");

            String directoryName = ""; 
            if (dirTokens.length > 1) {
              directoryName = dirTokens[1];
            } 
            
            
            // Show files uploads that are in the staging area.
            if (includeUploadStagingDir.equals("Y")) {
              String key = po.getKey(Constants.UPLOAD_STAGING_DIR);
              addExpandedFileNodes(baseDir, poNode, poNode, productOrderNumber, key, dh, knownProductOrderFileMap, fileMap, sess);
            } else {
              // This will add the uploaded files to the file map so if they are not displayed, 
              // they will not be displayed because they are in the DB.
              String key = po.getKey(Constants.UPLOAD_STAGING_DIR);
              Element dummyNode = new Element("dummy");
              addExpandedFileNodes(baseDir, poNode, dummyNode, productOrderNumber, key, dh, knownProductOrderFileMap, fileMap, sess);
            }

            List   theFiles     = (List)directoryMap.get(directoryKey);
            // For each file in the directory
            for (Iterator i2 = theFiles.iterator(); i2.hasNext();) {
              ProductOrderFileDescriptor fd = (ProductOrderFileDescriptor) i2.next();
              
              ProductOrderFile pof = (ProductOrderFile)knownProductOrderFileMap.get(fd.getQualifiedFileName());
              
              if (fd!=null && fd.getDisplayName().equals(Constants.UPLOAD_STAGING_DIR)) {
                continue;
              }
              
              Element fdNode = new Element("ProductOrderFileDescriptor");
              
              if (pof != null) {
                
                fd.setUploadDate(pof.getCreateDate());
                fd.setIdProductOrderFileString(pof.getIdProductOrderFile().toString());
                
                fd.setIdProductOrder(po.getIdProductOrder());
              } else {
                fd.setIdProductOrderFileString("ProductOrderFile-" + fd.getFilePathName());
                fd.setIdProductOrder(po.getIdProductOrder());
              }
              fd.setQualifiedFilePath(directoryName);
              fd.setBaseFilePath(getProductOrderDirectory(baseDir,po));
              fd.setIdLab(po.getIdLab());
              
              String comments = "";
              if ((fd.getType() == null || !fd.getType().equals("dir")) && fd.getComments() != null) {
                comments = fd.getComments();
              }
              fdNode.setAttribute("idProductOrder", po.getIdProductOrder()!=null?po.getIdProductOrder().toString():"");
              fdNode.setAttribute("dirty", "N");
              fdNode.setAttribute("key", directoryName != null && directoryName.length() > 0 ? po.getKey(directoryName) : po.getKey());
              fdNode.setAttribute("type", fd.getType() != null ? fd.getType() : "");
              fdNode.setAttribute("displayName", fd.getDisplayName() != null ? fd.getDisplayName() : "");
              fdNode.setAttribute("fileSize", String.valueOf(fd.getFileSize()));
              fdNode.setAttribute("fileSizeText", fd.getFileSizeText());
              fdNode.setAttribute("childFileSize", String.valueOf(fd.getFileSize()));
              fdNode.setAttribute("fileName", fd.getFileName() != null ? fd.getFileName() : "");
              fdNode.setAttribute("qualifiedFilePath", fd.getQualifiedFilePath() != null ? fd.getQualifiedFilePath() : "");
              fdNode.setAttribute("baseFilePath", fd.getBaseFilePath() != null ? fd.getBaseFilePath() : "");
              fdNode.setAttribute("comments", comments);
              fdNode.setAttribute("lastModifyDate", fd.getLastModifyDate() != null ? fd.getLastModifyDate().toString() : "");
              fdNode.setAttribute("zipEntryName", fd.getZipEntryName() != null ? fd.getZipEntryName() : "");
              fdNode.setAttribute("number", fd.getProductOrderNumber() != null ? fd.getProductOrderNumber() : "");
              fdNode.setAttribute("idProductOrderFileString", fd.getIdProductOrderFileString());
              fdNode.setAttribute("idLab", po.getIdLab() != null ? po.getIdLab().toString() : "");
              fdNode.setAttribute("isSelected", "N");
              fdNode.setAttribute("state", "unchecked");
              
              
              poNode.addContent(fdNode);
              recurseAddChildren(fdNode, fd, fileMap, knownProductOrderFileMap, sess);
              
              fileMap.put(fd.getQualifiedFileName(), null);
            }
          }
        }
        
        doc.getRootElement().addContent(poNode);
        XMLOutputter out = new org.jdom.output.XMLOutputter();
        this.xmlResult = out.outputString(doc);
        setResponsePage(this.SUCCESS_JSP);

      } else{
        setResponsePage(this.ERROR_JSP);
      }


    }catch(Exception e){
      log.error("An exception has occurred in GetProductOrderDownloadList ", e);
      e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());
      
    }finally{
      try {
        this.getSecAdvisor().closeReadOnlyHibernateSession();        
      } catch(Exception e) {

      }
      
    }
    
    return this;
  }

  @Override
  public void validate() {
    // TODO Auto-generated method stub

  }
  
  
  public static void addExpandedFileNodes(String baseDir,
      Element poNode,
      Element poDownloadNode, 
      String productOrderNumber, 
      String key, 
      DictionaryHelper dh,
      Map knownProductOrderFileMap,
      Map fileMap,
      Session sess) throws XMLReflectException {
    //
    // Get expanded file list
    //
    Map productOrderMap = new TreeMap();
    Map directoryMap = new TreeMap();
    
    if (!baseDir.endsWith("/") && !baseDir.endsWith("\\")) {
      baseDir += "/";
    }
    
    List productOrderNumbers = new ArrayList<String>();
    GetProductOrderDownloadList.getFileNamesToDownload(baseDir, key, productOrderNumbers, productOrderMap, directoryMap, true);
    List directoryKeys   = (List)productOrderMap.get(productOrderNumber);
    
    String[] tokens = key.split("-");
    String createYear  = tokens[0];
    
    for(Iterator i1 = directoryKeys.iterator(); i1.hasNext();) {
      String directoryKey = (String)i1.next();
      String[] dirTokens = directoryKey.split("-");
      String directoryName = dirTokens[1]; 
      if (dirTokens.length > 2) {
        directoryName += File.separator + dirTokens[2];
      }
      
      List   theFiles     = (List)directoryMap.get(directoryKey);

      // For each file in the directory
      if (theFiles != null && theFiles.size() > 0) {
        for (Iterator i2 = theFiles.iterator(); i2.hasNext();) {
          ProductOrderFileDescriptor fd = (ProductOrderFileDescriptor) i2.next();
          fd.setQualifiedFilePath(directoryName);
          
          ProductOrderFile pof = (ProductOrderFile)knownProductOrderFileMap.get(fd.getQualifiedFileName());
          
          
          if (pof != null) {
            fd.setUploadDate(pof.getCreateDate());
            fd.setIdProductOrderFileString(pof.getIdProductOrderFile().toString());
          } else {
            fd.setIdProductOrderFileString("ProductOrderFile-" + fd.getFilePathName());
          }
          
          fd.setQualifiedFilePath(directoryName);
          fd.setBaseFilePath(baseDir + createYear + File.separator + productOrderNumber);
          fd.setIdProductOrder(poNode.getAttributeValue("idProductOrder") != null ? Integer.valueOf(poNode.getAttributeValue("idProductOrder")) : null);
          String idLab = poNode.getAttributeValue("idLab");
          fd.setIdLab(idLab == null || idLab.equals("") ? null : Integer.valueOf(idLab));
          fd.excludeMethodFromXML("getChildren");
          
          Element fdNode = fd.toXMLDocument(null, fd.DATE_OUTPUT_ALTIO).getRootElement();
          fdNode.setAttribute("isSelected", "N");
          fdNode.setAttribute("state", "unchecked");
          
          recurseAddChildren(fdNode, fd, fileMap, knownProductOrderFileMap, sess);
          
          poDownloadNode.addContent(fdNode);
          poDownloadNode.setAttribute("isEmpty", "N");
          poNode.setAttribute("isEmpty", "N");
          
          fileMap.put(fd.getQualifiedFileName(), null);
        }

      } else {
        if (!poDownloadNode.getName().equals("ProductOrder")) {
          poDownloadNode.setAttribute("isEmpty", "Y");
        }
      }
    }
  }
  
  private static void recurseAddChildren(Element fdNode, ProductOrderFileDescriptor fd, Map fileMap, Map knownFilesMap, Session sess) throws XMLReflectException {
    if (fd.getChildren() == null || fd.getChildren().size() == 0) {
      if ( fd.getType() != null && fd.getType().equals("dir") ) {
        fdNode.setAttribute("isEmpty", "Y");
      }
    } else if (fd.getChildren() == null || fd.getChildren().size() > 0) {
      if ( fd.getType() != null && fd.getType().equals("dir") ) {
        fdNode.setAttribute("isEmpty", "N");
      }
    }
    
    for(Iterator i = fd.getChildren().iterator(); i.hasNext();) {
      
      ProductOrderFileDescriptor childFd = (ProductOrderFileDescriptor)i.next();
      
      childFd.setIdProductOrder(fd.getIdProductOrder());
      childFd.setQualifiedFilePath(fd.getQualifiedFilePath() != null && fd.getQualifiedFilePath().length() > 0 ? fd.getQualifiedFilePath() + File.separator + fd.getDisplayName() : fd.getDisplayName());
      childFd.setBaseFilePath(fd.getBaseFilePath());
      childFd.setIdLab(fd.getIdLab());
      
      ProductOrderFile pof = (ProductOrderFile)knownFilesMap.get(childFd.getQualifiedFileName());
      
      if (pof != null) {
        childFd.setIdProductOrderFileString(pof.getIdProductOrderFile().toString());
        childFd.setUploadDate(pof.getCreateDate());
        childFd.setIdProductOrder(pof.getIdProductOrder());
      } else {
        childFd.setIdProductOrderFileString("ProductOrderFile-" + childFd.getFilePathName());
        childFd.setIdProductOrder(fd.getIdProductOrder());
      }
      
      childFd.excludeMethodFromXML("getChildren");

      Element childFdNode = new Element("ProductOrderFileDescriptor");
      childFdNode.setAttribute("idProductOrder", childFd.getIdProductOrder() != null ? childFd.getIdProductOrder().toString() : "");
      childFdNode.setAttribute("dirty", "N");
      childFdNode.setAttribute("type", childFd.getType() != null ? childFd.getType() : "");
      childFdNode.setAttribute("displayName", childFd.getDisplayName() != null ? childFd.getDisplayName() : "");
      childFdNode.setAttribute("fileSize", Long.valueOf(childFd.getFileSize()).toString() );
      childFdNode.setAttribute("fileSizeText", childFd.getFileSizeText() != null ? childFd.getFileSizeText() : "");
      childFdNode.setAttribute("childFileSize", Long.valueOf(childFd.getChildFileSize()).toString() );
      childFdNode.setAttribute("fileName", childFd.getFileName() != null ? childFd.getFileName() : "");
      childFdNode.setAttribute("filePathName", childFd.getFilePathName() != null ? childFd.getFilePathName() : "");
      childFdNode.setAttribute("qualifiedFileName", childFd.getQualifiedFileName() != null ? childFd.getQualifiedFileName() : "");
      childFdNode.setAttribute("qualifiedFilePath", childFd.getQualifiedFilePath() != null ? childFd.getQualifiedFilePath() : "");
      childFdNode.setAttribute("baseFilePath", childFd.getBaseFilePath() != null ? childFd.getBaseFilePath() : "");
      childFdNode.setAttribute("comments", childFd.getComments() != null ? childFd.getComments() : "");
      childFdNode.setAttribute("lastModifyDateDisplay", childFd.getLastModifyDateDisplay() != null ? childFd.getLastModifyDateDisplay() : "");
      childFdNode.setAttribute("uploadDate", childFd.getUploadDate() != null ? childFd.formatDate(childFd.getUploadDate(), DATE_OUTPUT_SQL) : "");
      childFdNode.setAttribute("zipEntryName", childFd.getZipEntryName() != null ? childFd.getZipEntryName() : "");
      childFdNode.setAttribute("number", childFd.getProductOrderNumber() != null ? childFd.getProductOrderNumber() : "");
      childFdNode.setAttribute("productOrderNumber", childFd.getProductOrderNumber() != null ? childFd.getProductOrderNumber() : "");
      childFdNode.setAttribute("idProductOrderFileString", childFd.getIdProductOrderFileString() != null ? childFd.getIdProductOrderFileString() : "");
      childFdNode.setAttribute("idLab", childFd.getIdLab() != null ? childFd.getIdLab().toString() : "");

      childFdNode.setAttribute("isSelected", "N");
      childFdNode.setAttribute("state", "unchecked");
      
      fdNode.addContent(childFdNode);
      fileMap.put(childFd.getQualifiedFileName(), null);
      if (childFd.getChildren() != null && childFd.getChildren().size() > 0) {
        recurseAddChildren(childFdNode, childFd, fileMap, knownFilesMap, sess);
      } else {
        if ( childFd.getType() != null && childFd.getType().equals("dir") ) {
          childFdNode.setAttribute("isEmpty", "Y");
        }
      }
    }
  }
  
  private String getProductOrderDirectory(String baseDir, ProductOrder po) {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
    String createYear = formatter.format(po.getSubmitDate());
    
    if (!baseDir.endsWith("/") && !baseDir.endsWith("\\")) {
      baseDir += "/";
    }
    
    String directoryName = baseDir + createYear + File.separator + po.getProductOrderNumber();    
    return directoryName;
  }
  
  public static void getFileNamesToDownload(String baseDir, String keysString, List productOrderNumbers, Map productOrderMap, Map directoryMap, boolean flattenSubDirs){
    String[] keys = keysString.split(":");
    for (int i = 0; i < keys.length; i++) {
      String key = keys[i];

      String tokens[] = key.split("-");
      String createYear = tokens[0];
      String analysisNumber = tokens[2];

      String directoryKey = analysisNumber;
      if (!baseDir.endsWith("/") && !baseDir.endsWith("\\")) {
        baseDir += "/";
      }
      String directoryName = baseDir + createYear + "/" + analysisNumber;    

      if (tokens.length > 3 ) {
        String resultDir = tokens[3];
        directoryKey = analysisNumber + "-" + resultDir;
        directoryName = baseDir +  createYear + "/" + analysisNumber + "/" + resultDir;
      }

      // We want the list to be ordered the same way as the original keys,
      // so we will keep the analysis numbers in a list
      if (!productOrderNumbers.contains(analysisNumber)) {
        productOrderNumbers.add(analysisNumber);
      }

      List theFiles = new ArrayList();    
      getFileNames(analysisNumber, directoryName, theFiles, null, baseDir, flattenSubDirs);

      // Hash the list of file names (by directory name)
      directoryMap.put(directoryKey, theFiles);

      List directoryKeys = (List)productOrderMap.get(analysisNumber);
      if (directoryKeys == null) {
        directoryKeys = new ArrayList<String>();
      }
      directoryKeys.add(directoryKey);

      // Hash the list of directories (by analysisNumber)
      productOrderMap.put(analysisNumber, directoryKeys);
    }
    
  }
  
  public static void getFileNames(String productOrderNumber, String directoryName, List theFiles, String subDirName, String baseDir, boolean flattenSubDirs){
    File fd = new File(directoryName);

    if (fd.isDirectory()) {
      File[] fileList = fd.listFiles();
      
      Arrays.sort(fileList, new Comparator<File>(){     
        public int compare(File f1, File f2)     {         
          return f1.getName().compareTo(f2.getName());     
          } }); 
      
      
      for (int x = 0; x < fileList.length; x++) {
        File f1 = fileList[x];
//        if (k(f1)) {
//          continue;
//        }
        
        String fileName = directoryName + "/" + f1.getName();

        // Show the subdirectory in the name if we are not at the main folder level
        String displayName = "";
        if (flattenSubDirs && subDirName != null) {
          displayName = subDirName + "/" + f1.getName();
        } else {
          displayName = f1.getName();        
        }

        if (f1.isDirectory()) {
          ProductOrderFileDescriptor dirFileDescriptor = new ProductOrderFileDescriptor(productOrderNumber, f1.getName(), f1, baseDir);
          dirFileDescriptor.setType("dir");
          dirFileDescriptor.setQualifiedFilePath(subDirName!=null ? subDirName : "");
          theFiles.add(dirFileDescriptor);
          getFileNames(productOrderNumber, fileName, dirFileDescriptor.getChildren(), subDirName != null ? subDirName + "/" + f1.getName() : f1.getName(), baseDir, flattenSubDirs);
        } else {
          boolean include = true;
          if (fileName.toLowerCase().endsWith("thumbs.db")) {
            include = false;
          } 
          if (include) {
            ProductOrderFileDescriptor fileDescriptor = new ProductOrderFileDescriptor(productOrderNumber, displayName, f1, baseDir);
            fileDescriptor.setQualifiedFilePath(subDirName!=null ? subDirName : "");
            theFiles.add(fileDescriptor);
          }
        }
      }
    }
  }
}
