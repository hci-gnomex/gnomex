package hci.gnomex.utility;

import hci.framework.model.DetailObject;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.PropertyDictionary;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


public class ProductOrderFileDescriptor extends DetailObject implements Serializable {
  private static final double    KB = Math.pow(2, 10);
  private static final double    MB = Math.pow(2, 20);
  private static final double    GB = Math.pow(2, 30);


  private String    displayName;
  private String    productOrderNumber;
  private long     fileSize;
  private String    fileName;
  private Date      lastModifyDate;
  private String    type;
  private String    zipEntryName;
  private String    qualifiedFilePath;
  private String    baseFilePath;
  private List      children = new ArrayList();
  private Date      uploadDate;
  private String    comments;
  private Integer   idProductOrder;
  private String    idProductOrderFileString;
  private Integer   idLab;
  
  private boolean   found = false;
  
  public ProductOrderFileDescriptor() {    
  }
  
  public ProductOrderFileDescriptor(Integer idProductOrder, String displayName, File file, String baseDir) {
    this.idProductOrder = idProductOrder;
    this.displayName = displayName;
    
    this.fileSize = file.length();
    this.lastModifyDate  = new Date(file.lastModified());
    try {
      this.fileName = file.getCanonicalPath();      
    } catch (Exception e) {
      System.err.println("IO Exception occurred when trying to get absolute path for file " + file.toString());
      this.fileName = file.getAbsolutePath().replace("\\", "/");
    }
    this.zipEntryName = PropertyDictionaryHelper.parseZipEntryName(baseDir, fileName);  
    
  }
  
  public String getDisplayName() {
    return displayName;
  }
  
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }
  

  public String getFileSizeText() {
    
    long theFileSize = getFileSize();

    
    long size = 0;
    String sizeTxt = "";
    if (theFileSize > GB ) {
      size = Math.round(theFileSize / GB);
      sizeTxt = size + " " + " gb";
    }  else if (theFileSize > MB ) {
      size = Math.round(theFileSize / MB);
      sizeTxt = size + " " + " mb";
    } else if (theFileSize > KB ) {
      size = Math.round(theFileSize / KB);
      sizeTxt = size + " " + " kb";
    } else {
      sizeTxt = theFileSize + " b";
    }
    return sizeTxt;

  }
  
  public long getChildFileSize() {
    
    if (isDirectory()) {
      long total = 0;
      for(Iterator i = children.iterator(); i.hasNext();) {
        ProductOrderFileDescriptor fd = (ProductOrderFileDescriptor)i.next();
        total += fd.getChildFileSize();            
      }      
      return total;
      
    } else {
      return fileSize;
    }
    
  }
  
  private Boolean isDirectory() {
    return (this.type != null && this.type.equals("dir"));
  }
  
  public long getFileSize() {
    
    if (isDirectory()) {
      long theFileSize = 0;
      theFileSize = this.getChildFileSize();
      return theFileSize;
    } else {      
      return fileSize;
    }
  }

  public String getType() {
    return type;
  }
  
  public java.util.Date getLastModifyDate() {
    return lastModifyDate;
  }
  
  public String getFileName() {
    return fileName;
  }
  
  public String getZipEntryName() {
    return zipEntryName;
  }

  
  public String getProductOrderNumber() {
    return productOrderNumber;
  }

  
  public void setProductOrderNumber(String productOrderNumber) {
    this.productOrderNumber = productOrderNumber;
  }


  public void setFileSize(long fileSize) {
    this.fileSize = fileSize;
  }

  
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  
  public void setLastModifyDate(Date lastModifyDate) {
    this.lastModifyDate = lastModifyDate;
  }

  
  public void setType(String type) {
    this.type = type;
  }

  
  public void setZipEntryName(String zipEntryName) {
    this.zipEntryName = zipEntryName;
  }
  

  public String getDirectoryNumber(int analysisFileDirectoryLength) {
    String idProductOrder = "";
    if (fileName != null && !fileName.equals("")) {
      // Get the directory name starting after the year
      String relativePath = fileName.substring(analysisFileDirectoryLength + 5);
      String tokens[] = relativePath.split("/", 2);
      if (tokens == null || tokens.length == 1) {
        tokens = relativePath.split("/", 2);
      }
      if (tokens.length == 2) {
        idProductOrder = tokens[0];
      }
    }
    return idProductOrder;
  }

  
  public List getChildren() {
    return children;
  }

  
  public void setChildren(List children) {
    this.children = children;
  }

  
  public String getQualifiedFilePath() {
    return qualifiedFilePath;
  }

  public void setQualifiedFilePath(String qualifiedFilePath) {
    this.qualifiedFilePath = qualifiedFilePath;
  }

  public String getFilePathName() {
    String fullPathName = "";
    
    if (qualifiedFilePath != null && qualifiedFilePath.length() != 0) {
      fullPathName += getQualifiedFilePath() + "/"; 
    }
    fullPathName += getDisplayName();

//    String fullPath = fullPathName.replaceAll("/", "\\\\");
    String fullPath = fullPathName.replace("\\", "/");
    
//    return fullPath;
    return fullPathName;
  }
  
  public String getQualifiedFileName() {
    String fullPathName = "";
    
    if (qualifiedFilePath != null && qualifiedFilePath.length() != 0) {
      fullPathName += getQualifiedFilePath() + "/"; 
    }
    fullPathName += getDisplayName();

//    String fullPath = fullPathName.replaceAll("/", "\\\\");
    String fullPath = fullPathName.replace("\\", "/");
    
//    return fullPath;
    return fullPathName;
  }
  
  public String getBaseFilePath()
  {
    return baseFilePath;
  }

  public void setBaseFilePath(String baseFilePath)
  {
    this.baseFilePath = baseFilePath;
  }

  public String getLastModifyDateDisplay() {
    if (this.lastModifyDate != null) {
      return this.formatDate(this.lastModifyDate, DATE_OUTPUT_SQL);
    } else {
      return "";
    }
  }
  
 	  

  public String getIsSelected() {
    return "false";
  }

  
  public Date getUploadDate() {
    return uploadDate;
  }

  
  public void setUploadDate(Date uploadDate) {
    this.uploadDate = uploadDate;
  }

  
  public String getComments() {
    return comments;
  }

  
  public void setComments(String comments) {
    this.comments = comments;
  }

  
  public String getIdProductOrderFileString() {
    return idProductOrderFileString;
  }

  
  public void setIdProductOrderFileString(String idProductOrderFileString) {
    this.idProductOrderFileString = idProductOrderFileString;
  }

  
  public Integer getIdProductOrder() {
    return idProductOrder;
  }

  
  public void setIdProductOrder(Integer idProductOrder) {
    this.idProductOrder = idProductOrder;
  }

  
  public void isFound(boolean isFound) {
    this.found = isFound;
  }
  
  public boolean isFound() {
    return this.found;
  }
  
  public Integer getIdLab() {
    return idLab;
  }

  public void setIdLab(Integer idLab) {
    this.idLab = idLab;
  }  
}
