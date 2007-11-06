package hci.gnomex.utility;

import hci.framework.model.DetailObject;
import hci.gnomex.constants.Constants;

import java.io.File;
import java.io.Serializable;
import java.util.Date;


public class FileDescriptor extends DetailObject implements Serializable {
  private static final double    KB = Math.pow(2, 10);
  private static final double    MB = Math.pow(2, 20);
  private static final double    GB = Math.pow(2, 30);


  private String    displayName;
  private String    requestNumber;
  private long     fileSize;
  private String    fileName;
  private Date      lastModifyDate;
  private String    type;
  private String    zipEntryName;
  
  public FileDescriptor() {    
  }
  
  public FileDescriptor(String requestNumber, String displayName, File file) {
    this.requestNumber = requestNumber;
    this.displayName = displayName;
    
    this.fileSize = file.length();
    this.lastModifyDate  = new Date(file.lastModified());
    try {
      this.fileName = file.getCanonicalPath();      
    } catch (Exception e) {
      System.err.println("IO Exception occurred when trying to get absolute path for file " + file.toString());
      this.fileName = file.getAbsolutePath().replaceAll("\\", "/");
    }
    this.zipEntryName = fileName.substring(Constants.MICROARRAY_DIRECTORY.length() + 5).replaceAll("\\\\", "/");  
    
    String ext = "";
    String[] fileParts = file.getName().split("\\.");
    if (fileParts != null && fileParts.length == 2) {
      ext = fileParts[1];
    }
    type = ext;
    
    
  }
  
  public String getDisplayName() {
    return displayName;
  }
  
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }
  

  public String getFileSizeText() {
    
    long size = 0;
    String sizeTxt = "";
    if (fileSize > GB ) {
      size = Math.round(fileSize / GB);
      sizeTxt = size + " " + " gb";
    }  else if (fileSize > MB ) {
      size = Math.round(fileSize / MB);
      sizeTxt = size + " " + " mb";
    } else if (fileSize > KB ) {
      size = Math.round(fileSize / KB);
      sizeTxt = size + " " + " kb";
    } else {
      sizeTxt = fileSize + " b";
    }
    return sizeTxt;

  }
  
  public long getFileSize() {
    return fileSize;
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

  
  public String getRequestNumber() {
    return requestNumber;
  }

  
  public void setRequestNumber(String requestNumber) {
    this.requestNumber = requestNumber;
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
  
  public String getDirectoryRequestNumber() {
    String requestNumber = "";
    if (fileName != null && !fileName.equals("")) {
      // Get the directory name starting after the year
      String relativePath = fileName.substring(Constants.MICROARRAY_DIRECTORY.length() + 5);
      String tokens[] = relativePath.split("/", 2);
      if (tokens == null || tokens.length == 1) {
        tokens = relativePath.split("\\\\", 2);
      }
      if (tokens.length == 2) {
        requestNumber = tokens[0];
      }
    }
    return requestNumber;
  }
 
}
