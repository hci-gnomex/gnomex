package hci.gnomex.utility;

import hci.gnomex.constants.Constants;
import hci.gnomex.controller.GNomExCommand;
import hci.gnomex.model.Request;
import hci.gnomex.security.SecurityAdvisor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;

public class UploadDownloadHelper {
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(UploadDownloadHelper.class);

  public static void writeDownloadInfoFile(String baseDir, String emailAddress, SecurityAdvisor secAdvisor, HttpServletRequest req) {
    if (!baseDir.endsWith(File.separator)) {
      baseDir += File.separator;
    }
    File info = new File(baseDir + Constants.FDT_DOWNLOAD_INFO_FILE_NAME);
    try {
      if (!info.createNewFile()) {
        log.error("Unable to create info file for FDT transfer.");
      } else {
        FileWriter fw = new FileWriter(info);
        PrintWriter pw = new PrintWriter(fw);
        pw.println(emailAddress);
        pw.println(GNomExCommand.getRemoteIP(req));
        pw.println(secAdvisor.getIdAppUser().toString());
        pw.flush();
        pw.close();
      }
    } catch(IOException ex) {
      log.error("Unable to write info file for FDT Transfer", ex);
    }

  }

  public static void getFileNamesToDownload(Session sess, String serverName, String baseDirFlowCell, String keysString, List requestNumbers, Map requestMap, Map directoryMap, String flowCellDirectoryFlag) {
    getFileNamesToDownload(sess, serverName, baseDirFlowCell, keysString, requestNumbers, requestMap, directoryMap, flowCellDirectoryFlag, false); 
  }

  
  public static void getFileNamesToDownload(Session sess, String serverName, String baseDirFlowCell, String keysString, List requestNumbers, Map requestMap, Map directoryMap, String flowCellDirectoryFlag, boolean flattenSubDirs) {
    String[] keys = keysString.split(":");
    for (int i = 0; i < keys.length; i++) {
      String key = keys[i];

      String tokens[] = key.split(Constants.DOWNLOAD_KEY_SEPARATOR);
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
      

      String directoryKey = requestNumber + Constants.DOWNLOAD_KEY_SEPARATOR + resultDirectory;
      
      String directoryName = null;
      String theBaseDir;
      String fullBaseDir;
      if (flowCellIndicator.equals(flowCellDirectoryFlag)) {
        directoryName = baseDirFlowCell  + createYear + "/" + resultDirectory;
        theBaseDir = baseDirFlowCell;
        fullBaseDir = baseDirFlowCell  + createYear + "/";
        
        //Make sure flow cell is for this request
        String flowCellNumber = resultDirectory;
        
      } else {
        String baseDir = PropertyDictionaryHelper.getInstance(sess).getExperimentDirectory(serverName, idCoreFacility);
        directoryName = baseDir + "/" + createYear + "/" + requestNumberBase + "/" + resultDirectory;
        theBaseDir = baseDir;
        fullBaseDir = baseDir + "/" + createYear + "/" + requestNumberBase + "/";
      }
      
      // We want the list to be ordered the same way as the original keys,
      // so we will keep the request numbers in a list
      if (!requestNumbers.contains(requestNumber)) {
        requestNumbers.add(requestNumber);
      }
      
      List theFiles = new ArrayList();    
      getFileNames(theBaseDir, fullBaseDir, requestNumber, directoryName, theFiles, null, flowCellIndicator, flowCellDirectoryFlag, flattenSubDirs);
      
      // Hash the list of file names (by directory name)
      directoryMap.put(directoryKey, theFiles);
      
      List directoryKeys = (List)requestMap.get(requestNumber);
      if (directoryKeys == null) {
        directoryKeys = new ArrayList<String>();
      }
      directoryKeys.add(directoryKey);
      
      // Hash the list of directories (by request number)
      requestMap.put(requestNumber, directoryKeys);
    }
  }      
      
  private static void getFileNames(String theBaseDir, String fullBaseDir, String requestNumber, String directoryName, List theFiles, String subDirName, String flowCellIndicator, String flowCellDirectoryFlag, boolean flattenSubDirs)  {
    File fd = new File(directoryName);
//    if (Util.isSymlink(fd)) {
//      return;
//    }
    

    if (fd.isDirectory()) {
      File[] fileList = fd.listFiles();
      
      Arrays.sort(fileList, new Comparator<File>(){     
        public int compare(File f1, File f2)     {         
          return f1.getName().compareTo(f2.getName());     
          } }); 
      
      
      for (int x = 0; x < fileList.length; x++) {
        File f1 = fileList[x];
//        if (Util.isSymlink(f1)) {
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
        
        if (f1.isDirectory()) {
          FileDescriptor dirFileDescriptor = new FileDescriptor(requestNumber, f1.getName(), f1, zipEntryName);
          dirFileDescriptor.setType("dir");
          String dirName = "";
          if (directoryName.startsWith(fullBaseDir)) {
            dirName = directoryName.substring(fullBaseDir.length());
          }
          dirFileDescriptor.setDirectoryName(dirName);
          theFiles.add(dirFileDescriptor);
          getFileNames(theBaseDir, fullBaseDir, requestNumber, fileName, dirFileDescriptor.getChildren(), subDirName != null ? subDirName + "/" + f1.getName() : f1.getName(), flowCellIndicator, flowCellDirectoryFlag, flattenSubDirs);
        } else {
          boolean include = true;
          if (f1.getName().toLowerCase().endsWith("thumbs.db") || f1.getName().toUpperCase().startsWith(".DS_STORE") || f1.getName().startsWith("._")) {
            include = false;
          } 
          if (include) {
            
            
            FileDescriptor fileDescriptor = new FileDescriptor(requestNumber, displayName, f1, zipEntryName);
            String dirName = "";
            if (directoryName.startsWith(fullBaseDir)) {
              dirName = directoryName.substring(fullBaseDir.length());
            }
            fileDescriptor.setDirectoryName(dirName);
            fileDescriptor.setFlowCellIndicator(flowCellIndicator);
            theFiles.add(fileDescriptor);
          }
        }
      }
    }
  }
}
