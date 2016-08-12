package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.Request;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.FileDescriptor;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.apache.log4j.Logger;

public class GetDownloadEstimatedSize extends GNomExCommand implements Serializable {
  
  private static Logger LOG = Logger.getLogger(GetDownloadEstimatedSize.class);
  
  private String    keysString = null;
  private String    includeTIF = "N";
  private String    includeJPG = "N";
  private String    serverName = null;

  private String    baseDir;
  private String    baseDirFlowCell;

  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    // Get input parameters
    keysString = request.getParameter("resultKeys");
    if (request.getParameter("includeTIF") != null
        && !request.getParameter("includeTIF").equals("")) {
      includeTIF = request.getParameter("includeTIF");
    }
    if (request.getParameter("includeJPG") != null
        && !request.getParameter("includeJPG").equals("")) {
      includeJPG = request.getParameter("includeJPG");
    }
    serverName         = request.getServerName();
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
      DictionaryHelper dh = DictionaryHelper.getInstance(sess);
      baseDirFlowCell = PropertyDictionaryHelper.getInstance(sess).getDirectory(serverName, null, PropertyDictionaryHelper.PROPERTY_FLOWCELL_DIRECTORY);
      
      
      Map fileNameMap = new HashMap();      
      long compressedFileSizeTotal = getFileNamesToDownload(sess, serverName, baseDirFlowCell, keysString, fileNameMap, includeTIF.equals("Y"), includeJPG.equals("Y"), dh.getPropertyDictionary(PropertyDictionary.FLOWCELL_DIRECTORY_FLAG));
      this.xmlResult = "<DownloadEstimatedSize size='" + compressedFileSizeTotal + "'/>";
      
      if (isValid()) {
        setResponsePage(this.SUCCESS_JSP);
      } else {
        setResponsePage(this.ERROR_JSP);
      }
    
    } catch (Exception e){
      LOG.error("An exception has occurred in GetProject ", e);

      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        this.getSecAdvisor().closeReadOnlyHibernateSession();
      } catch (Exception e) {
      }
    }
    
    return this;
  }
  
private long getFileNamesToDownload(Session sess, String serverName, String baseDirFlowCell, String keysString, Map fileDescriptorMap, boolean includeAllTIFFiles, boolean includeAllJPGFiles, String flowCellDirectoryFlag) {

  long fileSizeTotal = 0;
  String[] keys = keysString.split(":");
  for (int i = 0; i < keys.length; i++) {
    String key = keys[i];

    String tokens[] = key.split("-");
    String createYear = tokens[0];
    String createDate = tokens[1];
    String requestNumber = tokens[2];
    String requestNumberBase = Request.getBaseRequestNumber(requestNumber);
    String resultDirectory = tokens[3];
    Integer idCoreFacility = Integer.valueOf(tokens[4]);
    String flowCellIndicator = "";
    if (tokens.length > 5) {
      flowCellIndicator = tokens[5];
    }
    
    String baseDir = PropertyDictionaryHelper.getInstance(sess).getDirectory(serverName, idCoreFacility, PropertyDictionaryHelper.PROPERTY_EXPERIMENT_DIRECTORY);

    
    String directoryName = "";
    String theBaseDir;
    if (flowCellIndicator.equals(flowCellDirectoryFlag)) {
      directoryName = baseDirFlowCell + createYear + "/" + resultDirectory;
      theBaseDir = baseDirFlowCell;
    } else {
      directoryName = baseDir + createYear + "/" + requestNumberBase + "/" + resultDirectory;
      theBaseDir = baseDir;
    }
    

    
    fileSizeTotal += getFileNames(requestNumber, directoryName, fileDescriptorMap, includeAllTIFFiles, includeAllJPGFiles, flowCellIndicator, theBaseDir, flowCellDirectoryFlag);
  }
  return fileSizeTotal;
}      
    
   
  
private long getFileNames(String requestNumber, String directoryName, Map fileDescriptorMap, boolean includeAllTIFFiles, boolean includeAllJPGFiles, String flowCellIndicator, String theBaseDir, String flowCellDirectoryFlag) {
  File fd = new File(directoryName);
  long fileSizeTotal = 0;

  if (fd.isDirectory()) {
    String[] fileList = fd.list();
    for (int x = 0; x < fileList.length; x++) {
      String fileName = directoryName + "/" + fileList[x];
      File f1 = new File(fileName);
      if (f1.isDirectory()) {
        fileSizeTotal += getFileNames(requestNumber, fileName, fileDescriptorMap, includeAllTIFFiles, includeAllJPGFiles, flowCellIndicator, theBaseDir, flowCellDirectoryFlag);
      } else {
        boolean include = true;
        if (!includeAllJPGFiles && fileName.toLowerCase().endsWith(".jpg")) {
          include = false;
        } else if (!includeAllTIFFiles && 
            (fileName.toLowerCase().endsWith(".tif") || 
             fileName.toLowerCase().endsWith(".tif.gz") || 
             fileName.toLowerCase().endsWith(".tif.gzip") || 
             fileName.toLowerCase().endsWith(".tif.zip"))) {
          include = false;
        } else if (fileName.toUpperCase().endsWith(".DS_Store")) {
          include = false;
        } else if (fileName.toUpperCase().endsWith("THUMBS.DB")) {
          include = false;
        }
        if (include) {
          long fileSize = f1.length();            
          
          fileSizeTotal += getEstimatedCompressedFileSize(fileName, fileSize);
          
          
          List fileDescriptors = (List)fileDescriptorMap.get(requestNumber);
          if (fileDescriptors == null) {
            fileDescriptors = new ArrayList<FileDescriptor>();
            fileDescriptorMap.put(requestNumber, fileDescriptors);
          }
          
          String zipEntryName;
          if (flowCellIndicator.equals(flowCellDirectoryFlag)) {
            zipEntryName = Request.getBaseRequestNumber(requestNumber) + "/" + fileName.substring(theBaseDir.length() + 5).replaceAll("\\\\", "/");  
          } else {
            try {
              zipEntryName = PropertyDictionaryHelper.parseZipEntryName(theBaseDir, f1.getCanonicalPath());  
            } catch (IOException  e) {
              throw new RuntimeException("Cannot get canonical file name for " + f1.getName());
            }
            if (zipEntryName.startsWith("/")) {
              zipEntryName = zipEntryName.substring(1);
            }
          }
          
          fileDescriptors.add(new FileDescriptor(requestNumber, "", f1, zipEntryName));
        }
      }
    }
  }
  return fileSizeTotal;
}

private long getEstimatedCompressedFileSize(String fileName, long fileSize) {
  double compressionRatio = 1;
  if (fileName.toUpperCase().endsWith("FEP")) {
    compressionRatio = 1.6;
  } else if (fileName.toUpperCase().endsWith("PDF")) {
    compressionRatio = 1;
  } else if (fileName.toUpperCase().endsWith("TIF")) {
    compressionRatio = 1.9;
  } else if (fileName.toUpperCase().endsWith("TIFF")) {
    compressionRatio = 1.9;
  } else if (fileName.toUpperCase().endsWith("JPG")) {
    compressionRatio = 1;
  } else if (fileName.toUpperCase().endsWith("JPEG")) {
    compressionRatio = 1;
  } else if (fileName.toUpperCase().endsWith("TXT")) {
    compressionRatio = 2.7; 
  } else if (fileName.toUpperCase().endsWith("RTF")) {
    compressionRatio = 2.7;
  } else if (fileName.toUpperCase().endsWith("DAT")) {
    compressionRatio = 1.6;
  } else if (fileName.toUpperCase().endsWith("CEL")) {
    compressionRatio = 2.8;
  } else if (fileName.toUpperCase().endsWith("ZIP")) {
    compressionRatio = 1;
  } else if (fileName.toUpperCase().endsWith("GZ")) {
    compressionRatio = 1;
  }     
  return new BigDecimal(fileSize / compressionRatio).longValue();
}

}