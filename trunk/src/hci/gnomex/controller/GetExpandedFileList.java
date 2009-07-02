package hci.gnomex.controller;

import hci.gnomex.utility.FileDescriptor;
import hci.gnomex.utility.HibernateSession;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.framework.utilities.XMLReflectException;

import java.io.File;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Element;
import org.jdom.Document;
import org.jdom.output.XMLOutputter;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.RequestFilter;
import hci.gnomex.model.Request;
import hci.gnomex.model.SequenceLane;


public class GetExpandedFileList extends GNomExCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetExpandedFileList.class);
  
  
  private String    keysString;
  private String    baseDir;
  private String    baseDirFlowCell;
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    // Get input parameters
    keysString = request.getParameter("resultKeys");
    baseDir         = Constants.getMicroarrayDirectoryForReading(request.getServerName());
    baseDirFlowCell = Constants.getFlowCellDirectory(request.getServerName());

  }

  public Command execute() throws RollBackCommandException {
    
    try {
      
    Document doc = new Document(new Element("ExpandedFileList"));
   
    Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
    
    Map requestMap = new TreeMap();
    Map directoryMap = new TreeMap();
    List requestNumbers = new ArrayList();
    getFileNamesToDownload(baseDir, baseDirFlowCell, keysString, requestNumbers, requestMap, directoryMap);
   
    //  For each request number
    for(Iterator i = requestNumbers.iterator(); i.hasNext();) {
      String requestNumber = (String)i.next();
      
      List directoryKeys   = (List)requestMap.get(requestNumber);
      
      Request request = DownloadResultsServlet.findRequest(sess, requestNumber);
      
      Element requestNode = new Element("RequestDirectory");
      requestNode.setAttribute("number", requestNumber);
      doc.getRootElement().addContent(requestNode);
      
      // If we can't find the request in the database, just bypass it.
      if (request == null) {
        log.error("Unable to find request " + requestNumber + ".  Bypassing download for user " + this.getUsername() + ".");
        continue;
      }
      
      // Check permissions - bypass this request if the user 
      // does not have  permission to read it.
      if (!this.getSecAdvisor().canRead(request)) {  
        log.error("Insufficient permissions to read request " + requestNumber + ".  Bypassing download for user " + this.getUsername() + ".");
        continue;
      }

      // For each directory of request
      boolean firstDirForRequest = true;
      for(Iterator i1 = directoryKeys.iterator(); i1.hasNext();) {
        
        String directoryKey = (String)i1.next();
        List   theFiles     = (List)directoryMap.get(directoryKey);
        String[] tokens = directoryKey.split("-");
        
        Element dirNode = new Element("Directory");
        dirNode.setAttribute("name", tokens[1]);
        
        
        requestNode.addContent(dirNode);

        // For each file in the directory
        boolean firstFileInDir = true;
        for (Iterator i2 = theFiles.iterator(); i2.hasNext();) {
          FileDescriptor fd = (FileDescriptor) i2.next();
          
          fd.setDirectoryName(tokens[1]);
          
          if (fd.getFlowCellIndicator() != null && fd.getFlowCellIndicator().equals(Constants.FLOWCELL_DIRECTORY_FLAG)) {
            boolean isValidFlowCell = false;
            for(Iterator i3 = request.getSequenceLanes().iterator(); i3.hasNext();) {
              SequenceLane lane = (SequenceLane)i3.next();
              if (lane.getFlowCellChannel() != null && lane.getFlowCellChannel().getFlowCell().getNumber().equals(fd.getMainFolderName())) {
                isValidFlowCell = true;
                break;
              }
            }
            if (!isValidFlowCell) {
              log.error("Bypassing flow cell " + fd.getMainFolderName() + " for request " + request.getNumber() + ".  No lanes on request used this flow cell.");
              continue;
            }
          
          }


          // Use attribute to get "control break" on request number and directory name
          // for grid of files
          Element fdNode = fd.toXMLDocument(null, this.DATE_OUTPUT_ALTIO).getRootElement();
          if (firstDirForRequest) {
            fdNode.setAttribute("showRequestNumber", "Y");
            firstDirForRequest = false;
          } else {
            fdNode.setAttribute("showRequestNumber", "N");
          }          
          if (firstFileInDir) {
            fdNode.setAttribute("showDirectoryName", "Y");
            firstFileInDir = false;
          } else {
            fdNode.setAttribute("showDirectoryName", "N");
          }  
          // Fill in the codeRequestCategory so that the UI can show the appropriate icon
          fdNode.setAttribute("codeRequestCategory", request.getCodeRequestCategory() != null ? request.getCodeRequestCategory() : "");
          
          
          dirNode.addContent(fdNode);
        }
        
      }

      
    }
    

    
    XMLOutputter out = new org.jdom.output.XMLOutputter();
    this.xmlResult = out.outputString(doc);
    
    setResponsePage(this.SUCCESS_JSP);
    }catch (NamingException e){
      log.error("An exception has occurred in GetRequestList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    }catch (SQLException e) {
      log.error("An exception has occurred in GetRequestList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (XMLReflectException e){
      log.error("An exception has occurred in GetRequestList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e){
      log.error("An exception has occurred in GetRequestList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        this.getSecAdvisor().closeReadOnlyHibernateSession();        
      } catch(Exception e) {
        
      }
    }
    
    return this;
  }
  
  public static void getFileNamesToDownload(String baseDir, String baseDirFlowCell, String keysString, List requestNumbers, Map requestMap, Map directoryMap) {
    String[] keys = keysString.split(":");
    for (int i = 0; i < keys.length; i++) {
      String key = keys[i];

      String tokens[] = key.split("-");
      String createYear = tokens[0];
      String createDate = tokens[1];
      String requestNumber = tokens[2];
      String resultDirectory = tokens[3];
      String flowCellIndicator = "";
      if (tokens.length > 4) {
        flowCellIndicator = tokens[4];
      }

      String directoryKey = requestNumber + "-" + resultDirectory;
      
      String directoryName = null;
      String theBaseDir;
      if (flowCellIndicator.equals(Constants.FLOWCELL_DIRECTORY_FLAG)) {
        directoryName = baseDirFlowCell + createYear + "/" + resultDirectory;
        theBaseDir = baseDirFlowCell;
        
        //Make sure flow cell is for this request
        String flowCellNumber = resultDirectory;
        
      } else {
        directoryName = baseDir + createYear + "/" + requestNumber + "/" + resultDirectory;
        theBaseDir = baseDir;
      }
      
      // We want the list to be ordered the same way as the original keys,
      // so we will keep the request numbers in a list
      if (!requestNumbers.contains(requestNumber)) {
        requestNumbers.add(requestNumber);
      }
      
      List theFiles = new ArrayList();    
      getFileNames(theBaseDir, requestNumber, directoryName, theFiles, null, flowCellIndicator);
      
      // Hash the list of file names (by directory name)
      directoryMap.put(directoryKey, theFiles);
      
      List directoryKeys = (List)requestMap.get(requestNumber);
      if (directoryKeys == null) {
        directoryKeys = new ArrayList();
      }
      directoryKeys.add(directoryKey);
      
      // Hash the list of directories (by request number)
      requestMap.put(requestNumber, directoryKeys);
    }
  }      
      
  public static void getFileNames(String theBaseDir, String requestNumber, String directoryName, List theFiles, String subDirName, String flowCellIndicator) {
    File fd = new File(directoryName);
    

    if (fd.isDirectory()) {
      String[] fileList = fd.list();
      for (int x = 0; x < fileList.length; x++) {
        String fileName = directoryName + "/" + fileList[x];
        File f1 = new File(fileName);
        
        // Show the subdirectory in the name if we are not at the main folder level
        String displayName = "";
        if (subDirName != null) {
          displayName = subDirName + "/" + fileList[x];
        } else {
          displayName = f1.getName();        
        }

        String zipEntryName;
        if (flowCellIndicator.equals(Constants.FLOWCELL_DIRECTORY_FLAG)) {
          zipEntryName = requestNumber + "/" + fileName.substring(theBaseDir.length() + 5).replaceAll("\\\\", "/");
        } else {
          zipEntryName = fileName.substring(theBaseDir.length() + 5).replaceAll("\\\\", "/");  
        }
        
        if (f1.isDirectory()) {
          FileDescriptor dirFileDescriptor = new FileDescriptor(requestNumber, f1.getName() + "/", f1, zipEntryName);
          dirFileDescriptor.setType("dir");
          theFiles.add(dirFileDescriptor);
          getFileNames(theBaseDir, requestNumber, fileName, dirFileDescriptor.getChildren(), subDirName != null ? subDirName + "/" + f1.getName() : f1.getName(), flowCellIndicator);
        } else {
          boolean include = true;
          if (fileName.toLowerCase().endsWith("thumbs.db")) {
            include = false;
          } 
          if (include) {
            
            
            FileDescriptor fileDescriptor = new FileDescriptor(requestNumber, displayName, f1, zipEntryName);
            fileDescriptor.setFlowCellIndicator(flowCellIndicator);
            theFiles.add(fileDescriptor);
          }
        }
      }
    }
  }
  


}