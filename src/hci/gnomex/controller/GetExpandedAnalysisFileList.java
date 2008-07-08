package hci.gnomex.controller;

import hci.gnomex.utility.AnalysisFileDescriptor;
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
import hci.gnomex.model.Analysis;
import hci.gnomex.model.RequestFilter;
import hci.gnomex.model.Request;


public class GetExpandedAnalysisFileList extends GNomExCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetExpandedAnalysisFileList.class);
  
  
  private String    keysString;
  private String    baseDir;
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    // Get input parameters
    keysString = request.getParameter("resultKeys");
    baseDir = Constants.getAnalysisDirectory(request.getServerName());
    
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      
    Document doc = new Document(new Element("ExpandedAnalysisFileList"));
   
    Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
    
    Map analysisMap = new TreeMap();
    Map directoryMap = new TreeMap();
    List analysisNumbers = new ArrayList();
    getFileNamesToDownload(baseDir, keysString, analysisNumbers, analysisMap, directoryMap);
   
    //  For each request number
    for(Iterator i = analysisNumbers.iterator(); i.hasNext();) {
      String analysisNumber = (String)i.next();
      
      List directoryKeys   = (List)analysisMap.get(analysisNumber);
      
      Analysis analysis = null;
      List analysisList = sess.createQuery("SELECT a from Analysis a where a.number = '" + analysisNumber + "'").list();
      if (analysisList.size() == 1) {
        analysis = (Analysis)analysisList.get(0);
      }

      
      Element analysisNode = new Element("Directory");
      analysisNode.setAttribute("number", analysisNumber);
      analysisNode.setAttribute("name",   analysis.getName());
      doc.getRootElement().addContent(analysisNode);
      
      // If we can't find the analysis in the database, just bypass it.
      if (analysis == null) {
        log.error("Unable to find analysis " + analysisNumber + ".  Bypassing download for user " + this.getUsername() + ".");
        continue;
      }
      
      // Check permissions - bypass this analysis if the user 
      // does not have  permission to read it.
      if (!this.getSecAdvisor().canRead(analysis)) {  
        log.error("Insufficient permissions to read analysis " + analysisNumber + ".  Bypassing download for user " + this.getUsername() + ".");
        continue;
      }

      // For each directory of request
      boolean firstDirForAnalysis = true;
      for(Iterator i1 = directoryKeys.iterator(); i1.hasNext();) {
        
        String directoryKey = (String)i1.next();
        List   theFiles     = (List)directoryMap.get(directoryKey);

        
        
        // For each file in the directory
        boolean firstFileInDir = true;
        for (Iterator i2 = theFiles.iterator(); i2.hasNext();) {
          AnalysisFileDescriptor fd = (AnalysisFileDescriptor) i2.next();
          fd.setDirectoryName(directoryKey);
          


          // Use attribute to get "control break" on request number and directory name
          // for grid of files
          Element fdNode = fd.toXMLDocument(null, this.DATE_OUTPUT_ALTIO).getRootElement();
          fdNode.setAttribute("folderName", analysisNumber + " (" + analysis.getName() + ")");
          if (firstDirForAnalysis) {
            fdNode.setAttribute("showAnalysisNumber", "Y");
            firstDirForAnalysis = false;
          } else {
            fdNode.setAttribute("showAnalysisNumber", "N");
          }          
          if (firstFileInDir) {
            fdNode.setAttribute("showAnalysisNumber", "Y");
            firstFileInDir = false;
          } else {
            fdNode.setAttribute("showAnalysisNumber", "N");
          }          
          
          
          analysisNode.addContent(fdNode);
        }
        
      }

      
    }
    

    
    XMLOutputter out = new org.jdom.output.XMLOutputter();
    this.xmlResult = out.outputString(doc);
    
    setResponsePage(this.SUCCESS_JSP);
    }catch (NamingException e){
      log.error("An exception has occurred in GetExpandedAnalysisFileList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    }catch (SQLException e) {
      log.error("An exception has occurred in GetExpandedAnalysisFileList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (XMLReflectException e){
      log.error("An exception has occurred in GetExpandedAnalysisFileList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e){
      log.error("An exception has occurred in GetExpandedAnalysisFileList ", e);
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
  
  public static void getFileNamesToDownload(String baseDir, String keysString, List analysisNumbers, Map analysisMap, Map directoryMap) {
    String[] keys = keysString.split(":");
    for (int i = 0; i < keys.length; i++) {
      String key = keys[i];

      String tokens[] = key.split("-");
      String createYear = tokens[0];
      String createDate = tokens[1];
      String analysisNumber = tokens[2];

      String directoryKey = analysisNumber;
      String directoryName = baseDir + createYear + "/" + analysisNumber;
      
      // We want the list to be ordered the same way as the original keys,
      // so we will keep the analysis numbers in a list
      if (!analysisNumbers.contains(analysisNumber)) {
        analysisNumbers.add(analysisNumber);
      }
      
      List theFiles = new ArrayList();    
      getFileNames(analysisNumber, directoryName, theFiles, null);
      
      // Hash the list of file names (by directory name)
      directoryMap.put(directoryKey, theFiles);
      
      List directoryKeys = (List)analysisMap.get(analysisNumber);
      if (directoryKeys == null) {
        directoryKeys = new ArrayList();
      }
      directoryKeys.add(directoryKey);
      
      // Hash the list of directories (by analysisNumber)
      analysisMap.put(analysisNumber, directoryKeys);
    }
  }      
      
  public static void getFileNames(String analysisNumber, String directoryName, List theFiles, String subDirName) {
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
        
        if (f1.isDirectory()) {
          AnalysisFileDescriptor dirFileDescriptor = new AnalysisFileDescriptor(analysisNumber, f1.getName() + "/", f1);
          dirFileDescriptor.setType("dir");
          theFiles.add(dirFileDescriptor);
          getFileNames(analysisNumber, fileName, dirFileDescriptor.getChildren(), subDirName != null ? subDirName + "/" + f1.getName() : f1.getName());
        } else {
          boolean include = true;
          if (fileName.toLowerCase().endsWith("thumbs.db")) {
            include = false;
          } 
          if (include) {
            AnalysisFileDescriptor fileDescriptor = new AnalysisFileDescriptor(analysisNumber, displayName, f1);
            theFiles.add(fileDescriptor);
          }
        }
      }
    }
  }
  


}