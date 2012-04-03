package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.Analysis;
import hci.gnomex.model.AnalysisFile;
import hci.gnomex.model.AnalysisGroupFilter;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.AnalysisFileDescriptor;
import hci.gnomex.utility.DictionaryHelper;

import java.io.File;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;


public class GetAnalysisDownloadList extends GNomExCommand implements Serializable {

  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetAnalysisDownloadList.class);

  private AnalysisGroupFilter            filter;
  private String                         includeUploadStagingDir = "Y";

  private String                         serverName;
  private String                         baseDir;
//  private SimpleDateFormat               yearFormat= new SimpleDateFormat("yyyy");
  private Integer                        idAnalysis;    
  private String                         analysisNumber;
  

  public void validate() {
  }

  public void loadCommand(HttpServletRequest request, HttpSession session) {
    filter = new AnalysisGroupFilter();
    HashMap errors = this.loadDetailObject(request, filter);
    this.addInvalidFields(errors);

    if (request.getParameter("includeUploadStagingDir") != null && !request.getParameter("includeUploadStagingDir").equals("")) {
      includeUploadStagingDir = request.getParameter("includeUploadStagingDir");
    }

    String idAnalysisStringList = request.getParameter("idAnalysisStringList");
    if (idAnalysisStringList != null&& !idAnalysisStringList.equals("")) {
      List idAnalyses = new ArrayList<Integer>();
      String[] keys = idAnalysisStringList.split(":");
      for (int i = 0; i < keys.length; i++) {
        String idAnalysis = keys[i];
        idAnalyses.add(new Integer(idAnalysis));
      }
      filter.setIdAnalyses(idAnalyses);
    }

    if (request.getParameter("idAnalysis") != null) {
      idAnalysis = new Integer(request.getParameter("idAnalysis"));
    }     

    if (request.getParameter("analysisNumber") != null && !request.getParameter("analysisNumber").equals("")) {
      analysisNumber = request.getParameter("analysisNumber");
    }     
    if (idAnalysis == null && analysisNumber == null) {
      this.addInvalidField("idAnalysis or analysisNumber", "Either idAnalysis or analysisNumber must be provided");
    }

    if (this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_ACCESS_ANY_OBJECT) && !filter.hasSufficientCriteria(this.getSecAdvisor())) {
      this.addInvalidField("filterRequired", "Please enter at least one search criterion.");
    }

    serverName = request.getServerName();

  }

  public Command execute() throws RollBackCommandException {

    try {
      
      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());

      DictionaryHelper dh = DictionaryHelper.getInstance(sess);
      baseDir = dh.getAnalysisReadDirectory(serverName);

      Analysis a = null;
      if (idAnalysis != null && idAnalysis.intValue() == 0) {
        a = new Analysis();
        a.setIdAnalysis(new Integer(0));
      } else if (idAnalysis != null){
        a = (Analysis)sess.get(Analysis.class, idAnalysis);
        Hibernate.initialize(a.getAnalysisGroups());

      }else {
        analysisNumber = analysisNumber.replaceAll("#", "");
        StringBuffer buf = new StringBuffer("SELECT a from Analysis as a where a.number = '" + analysisNumber.toUpperCase() + "'");
        List analyses = sess.createQuery(buf.toString()).list();
        if (analyses.size() > 0) {
          a = (Analysis)analyses.get(0);
          Hibernate.initialize(a.getAnalysisGroups());
        }
      }

      if (a == null) {
        this.addInvalidField("missingAnalysis", "Cannot find analysis idAnalysis=" + idAnalysis + " analysisNumber=" + analysisNumber);
      } else {
        if (!this.getSecAdvisor().canRead(a)) {
          this.addInvalidField("permissionerror", "Insufficient permissions to access this analysis Group.");
        } else {
          this.getSecAdvisor().flagPermissions(a);
        }         
      }
      
      if (isValid())  {

        Document doc = new Document(new Element("AnalysisDownloadList"));

        a.excludeMethodFromXML("getIdLab");
        a.excludeMethodFromXML("getLab");
        a.excludeMethodFromXML("getIdAppUser");
        a.excludeMethodFromXML("getIdAnalysisType");
        a.excludeMethodFromXML("getIdAnalysisProtocol");
        a.excludeMethodFromXML("getIdOrganism");
        a.excludeMethodFromXML("getCodeVisibility");
        a.excludeMethodFromXML("getIdInstitution");
        a.excludeMethodFromXML("getPrivacyExpirationDate");
        a.excludeMethodFromXML("getAnalysisGroups");
        a.excludeMethodFromXML("getExperimentItems");
        a.excludeMethodFromXML("getFiles");
        a.excludeMethodFromXML("getCollaborators");
        a.excludeMethodFromXML("getGenomeBuilds");
        Element aNode = a.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();
        aNode.setAttribute("displayName", a.getName());
        aNode.setAttribute("idLab", a.getIdLab().toString());
        aNode.setAttribute("number", a.getNumber());
        aNode.setAttribute("isSelected", "N");
        aNode.setAttribute("state", "unchecked");
        aNode.setAttribute("isEmpty", "N");
        
        
        // Hash the know analysis files
        Map knownAnalysisFileMap = new HashMap();
        for(Iterator i = a.getFiles().iterator(); i.hasNext();) {
          AnalysisFile af = (AnalysisFile)i.next();
          knownAnalysisFileMap.put(af.getQualifiedFileName(), af);
        }

        // Now add in the files that exist on the file server
        Map analysisMap = new TreeMap();
        Map directoryMap = new TreeMap();
        Map fileMap = new HashMap();
        List analysisNumbers = new ArrayList<String>();
        GetExpandedAnalysisFileList.getFileNamesToDownload(baseDir, a.getKey(), analysisNumbers, analysisMap, directoryMap, false);
        
        for(Iterator i = analysisNumbers.iterator(); i.hasNext();) {
          String analysisNumber = (String)i.next();
          List directoryKeys   = (List)analysisMap.get(analysisNumber);

          // For each directory of analysis
          for(Iterator i1 = directoryKeys.iterator(); i1.hasNext();) {

            String directoryKey = (String)i1.next();
            
            String[] dirTokens = directoryKey.split("-");

            String directoryName = ""; 
            if (dirTokens.length > 1) {
              directoryName = dirTokens[1];
            } 
            
            
            // Show files uploads that are in the staging area.
            if (includeUploadStagingDir.equals("Y")) {
              String key = a.getKey(Constants.UPLOAD_STAGING_DIR);
              addExpandedFileNodes(baseDir, aNode, aNode, analysisNumber, key, dh, knownAnalysisFileMap, fileMap);
            } else {
              // This will add the uploaded files to the file map so if they are not displayed, 
              // they will not be displayed because they are in the DB.
              String key = a.getKey(Constants.UPLOAD_STAGING_DIR);
              Element dummyNode = new Element("dummy");
              addExpandedFileNodes(baseDir, aNode, dummyNode, analysisNumber, key, dh, knownAnalysisFileMap, fileMap);
            }

            List   theFiles     = (List)directoryMap.get(directoryKey);
            
            // For each file in the directory
            for (Iterator i2 = theFiles.iterator(); i2.hasNext();) {
              AnalysisFileDescriptor fd = (AnalysisFileDescriptor) i2.next();
              
              AnalysisFile af = (AnalysisFile)knownAnalysisFileMap.get(fd.getQualifiedFileName());
              
              if (fd!=null && fd.getDisplayName().equals(Constants.UPLOAD_STAGING_DIR)) {
                continue;
              }
              
              Element fdNode = new Element("AnalysisFileDescriptor");
              
              if (af != null) {
                
                fd.setUploadDate(af.getUploadDate());
                fd.setIdAnalysisFileString(af.getIdAnalysisFile().toString());
                fd.setComments(af.getComments());
                fd.setIdAnalysis(a.getIdAnalysis());
              } else {
                fd.setIdAnalysisFileString("AnalysisFile-" + fd.getDisplayName());
                fd.setIdAnalysis(a.getIdAnalysis());
              }
              fd.setQualifiedFilePath(directoryName);
              fd.setBaseFilePath(getAnalysisDirectory(baseDir,a));
              fd.setIdLab(a.getIdLab());
              
              fdNode.setAttribute("idAnalysis", a.getIdAnalysis()!=null?a.getIdAnalysis().toString():"");
              fdNode.setAttribute("key", directoryName != "" ? a.getKey(directoryName) : a.getKey());
              fdNode.setAttribute("type", fd.getType() != null ? fd.getType() : "");
              fdNode.setAttribute("displayName", fd.getDisplayName() != null ? fd.getDisplayName() : "");
              fdNode.setAttribute("fileSize", String.valueOf(fd.getFileSize()));
              fdNode.setAttribute("fileSizeText", fd.getFileSizeText());
              fdNode.setAttribute("childFileSize", String.valueOf(fd.getFileSize()));
              fdNode.setAttribute("fileName", fd.getFileName() != null ? fd.getFileName() : "");
              fdNode.setAttribute("qualifiedFilePath", fd.getQualifiedFilePath() != null ? fd.getQualifiedFilePath() : "");
              fdNode.setAttribute("baseFilePath", fd.getBaseFilePath() != null ? fd.getBaseFilePath() : "");
              fdNode.setAttribute("comments", fd.getComments() != null & fd.getType()!="dir" ? fd.getComments() : "");
              fdNode.setAttribute("lastModifyDate", fd.getLastModifyDate() != null ? fd.getLastModifyDate().toString() : "");
              fdNode.setAttribute("zipEntryName", fd.getZipEntryName() != null ? fd.getZipEntryName() : "");
              fdNode.setAttribute("number", fd.getAnalysisNumber() != null ? fd.getAnalysisNumber() : "");
              fdNode.setAttribute("idAnalysisFileString", fd.getIdAnalysisFileString());
              fdNode.setAttribute("idLab", a.getIdLab() != null ? a.getIdLab().toString() : "");
              fdNode.setAttribute("isSelected", "N");
              fdNode.setAttribute("state", "unchecked");
              fdNode.setAttribute("viewURL", fd.getViewURL());
              
              aNode.addContent(fdNode);
              recurseAddChildren(fdNode, fd, fileMap, knownAnalysisFileMap);
              
              fileMap.put(fd.getQualifiedFileName(), null);
            }
          }
        }

        // Add any files that are registered in the db, but not on the fileserver
        for(Iterator i = a.getFiles().iterator(); i.hasNext();) {
          AnalysisFile af = (AnalysisFile)i.next();
          
          // We will look at every file regardless of if it has the staging directory as
          // its qualified path.  The files that are physically in the staging directory
          // will be in the file map and therefore will not be re-added.
          
          if (!fileMap.containsKey(af.getQualifiedFileName())) {
            AnalysisFileDescriptor fd = new AnalysisFileDescriptor();

            fd.setDisplayName(af.getFileName());
            fd.setFileName(af.getFullPathName());
            fd.setQualifiedFilePath(af.getQualifiedFilePath());
            fd.setBaseFilePath(af.getBaseFilePath());
            fd.setIdAnalysisFileString(af.getIdAnalysisFile() != null ? af.getIdAnalysisFile().toString() : "");
            fd.setIdAnalysis(af.getIdAnalysis());
            fd.setAnalysisNumber(a.getNumber());
            fd.setUploadDate(af.getUploadDate());
            fd.setComments(af.getComments());
            fd.setIdLab(a.getIdLab());
            fd.excludeMethodFromXML("getChildren");

            Element fdNode = fd.toXMLDocument(null, fd.DATE_OUTPUT_ALTIO).getRootElement();
            fdNode.setAttribute("isSelected", "N");
            fdNode.setAttribute("state", "unchecked");

            aNode.addContent(fdNode);
            recurseAddChildren(fdNode, fd, fileMap, knownAnalysisFileMap);
          }
        }

        doc.getRootElement().addContent(aNode);

        XMLOutputter out = new org.jdom.output.XMLOutputter();
        this.xmlResult = out.outputString(doc);
      }

      if (isValid()) {
        setResponsePage(this.SUCCESS_JSP);
      } else {
        setResponsePage(this.ERROR_JSP);
      }

    }catch (NamingException e){
      log.error("An exception has occurred in GetAnalysisDownloadList ", e);
      e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());
    }catch (SQLException e) {
      log.error("An exception has occurred in GetAnalysisDownloadList ", e);
      e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e) {
      log.error("An exception has occurred in GetAnalysisDownloadList ", e);
      e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        this.getSecAdvisor().closeReadOnlyHibernateSession();        
      } catch(Exception e) {

      }
    }

    return this;
  }
  
  public static String getAnalysisDirectory(String baseDir, Analysis analysis) {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
    String createYear = formatter.format(analysis.getCreateDate());
    
    if (!baseDir.endsWith("/") && !baseDir.endsWith("\\")) {
      baseDir += "/";
    }
    
    String directoryName = baseDir + createYear + "/" + analysis.getNumber();    
    return directoryName;
  }
  
  public static void addExpandedFileNodes(String baseDir,
      Element analysisNode,
      Element analysisDownloadNode, 
      String analysisNumber, 
      String key, 
      DictionaryHelper dh,
      Map knownAnalysisFileMap,
      Map fileMap) throws XMLReflectException {
    //
    // Get expanded file list
    //
    Map analysisMap = new TreeMap();
    Map directoryMap = new TreeMap();
    
    if (!baseDir.endsWith("/") && !baseDir.endsWith("\\")) {
      baseDir += "/";
    }
    
    List analysisNumbers = new ArrayList<String>();
    GetExpandedAnalysisFileList.getFileNamesToDownload(baseDir, key, analysisNumbers, analysisMap, directoryMap, true);
    List directoryKeys   = (List)analysisMap.get(analysisNumber);
    
    String[] tokens = key.split("-");
    String createYear  = tokens[0];
    
    for(Iterator i1 = directoryKeys.iterator(); i1.hasNext();) {
      String directoryKey = (String)i1.next();
      String[] dirTokens = directoryKey.split("-");
      String directoryName = dirTokens[1]; 
      if (dirTokens.length > 2) {
        directoryName += "/" + dirTokens[2];
      }
      
      List   theFiles     = (List)directoryMap.get(directoryKey);

      // For each file in the directory
      if (theFiles != null && theFiles.size() > 0) {
        for (Iterator i2 = theFiles.iterator(); i2.hasNext();) {
          AnalysisFileDescriptor fd = (AnalysisFileDescriptor) i2.next();
          fd.setQualifiedFilePath(directoryName);
          
          AnalysisFile af = (AnalysisFile)knownAnalysisFileMap.get(fd.getQualifiedFileName());
          
          
          if (af != null) {
            fd.setUploadDate(af.getUploadDate());
            fd.setIdAnalysisFileString(af.getIdAnalysisFile().toString());
            fd.setComments(af.getComments());
          } else {
            fd.setIdAnalysisFileString("AnalysisFile-" + fd.getDisplayName());
          }
          
          fd.setQualifiedFilePath(directoryName);
          fd.setBaseFilePath(baseDir + createYear + "/" + analysisNumber);
          fd.setIdAnalysis(analysisNode.getAttributeValue("idAnalysis") != null ? Integer.valueOf(analysisNode.getAttributeValue("idAnalysis")) : null);
          String idLab = analysisNode.getAttributeValue("idLab");
          fd.setIdLab(idLab == null || idLab.equals("") ? null : Integer.valueOf(idLab));
          fd.excludeMethodFromXML("getChildren");
          
          Element fdNode = fd.toXMLDocument(null, fd.DATE_OUTPUT_ALTIO).getRootElement();
          fdNode.setAttribute("isSelected", "N");
          fdNode.setAttribute("state", "unchecked");
          fdNode.setAttribute("viewURL", fd.getViewURL()!=null?fd.getViewURL():"");
          recurseAddChildren(fdNode, fd, fileMap, knownAnalysisFileMap);
          
          analysisDownloadNode.addContent(fdNode);
          analysisDownloadNode.setAttribute("isEmpty", "N");
          analysisNode.setAttribute("isEmpty", "N");
          
          fileMap.put(fd.getQualifiedFileName(), null);
        }

      } else {
        if (!analysisDownloadNode.getName().equals("Analysis")) {
          analysisDownloadNode.setAttribute("isEmpty", "Y");
        }
      }
    }
  }

  public static void recurseAddChildren(Element fdNode, AnalysisFileDescriptor fd, Map fileMap, Map knownFilesMap) throws XMLReflectException {
    if (fd.getChildren() == null || fd.getChildren().size() == 0) {
      if ( fd.getType() == "dir" ) {
        fdNode.setAttribute("isEmpty", "Y");
      }
    } else if (fd.getChildren() == null || fd.getChildren().size() > 0) {
      if ( fd.getType() == "dir" ) {
        fdNode.setAttribute("isEmpty", "N");
      }
    }
    
    for(Iterator i = fd.getChildren().iterator(); i.hasNext();) {
      
      AnalysisFileDescriptor childFd = (AnalysisFileDescriptor)i.next();
      
      childFd.setIdAnalysis(fd.getIdAnalysis());
      childFd.setQualifiedFilePath(fd.getQualifiedFilePath() != "" ? fd.getQualifiedFilePath() + "/" + fd.getDisplayName() : fd.getDisplayName());
      childFd.setBaseFilePath(fd.getBaseFilePath());
      childFd.setIdLab(fd.getIdLab());
      
      AnalysisFile af = (AnalysisFile)knownFilesMap.get(childFd.getQualifiedFileName());
      
      if (af != null) {
        fdNode.setAttribute("comments",fd.getType()!="dir"&af.getComments()!=null?af.getComments():"");
        childFd.setIdAnalysisFileString(af.getIdAnalysisFile().toString());
        childFd.setUploadDate(af.getUploadDate());
        childFd.setComments(af.getComments());
        childFd.setIdAnalysis(af.getIdAnalysis());
      } else {
        childFd.setIdAnalysisFileString("AnalysisFile-" + childFd.getDisplayName());
        childFd.setIdAnalysis(fd.getIdAnalysis());
      }
      
      childFd.excludeMethodFromXML("getChildren");

      Element childFdNode = childFd.toXMLDocument(null, childFd.DATE_OUTPUT_ALTIO).getRootElement();
      childFdNode.setAttribute("isSelected", "N");
      childFdNode.setAttribute("state", "unchecked");

      fdNode.addContent(childFdNode);
      fileMap.put(childFd.getQualifiedFileName(), null);
      if (childFd.getChildren() != null && childFd.getChildren().size() > 0) {
        recurseAddChildren(childFdNode, childFd, fileMap, knownFilesMap);
      } else {
        if ( childFd.getType() == "dir" ) {
          childFdNode.setAttribute("isEmpty", "Y");
        }
      }
    }
  }


  public static Set getAnalysisDownloadFolders(String baseDir, String analysisNumber, String createYear) {

    TreeSet folders = new TreeSet<String>(new FolderComparator());
    String directoryName = baseDir + createYear + "/" + analysisNumber;
    File fd = new File(directoryName);

    if (fd.isDirectory()) {
      String[] fileList = fd.list();
      for (int x = 0; x < fileList.length; x++) {
        String fileName = directoryName + "/" + fileList[x];
        File f1 = new File(fileName);
        if (f1.isDirectory()) {
          folders.add(fileList[x]);          
        }
      }
    }
    return folders;
  }

  
  public static class  FolderComparator implements Comparator, Serializable {
    public int compare(Object o1, Object o2)  {
      AnalysisFile af1 = (AnalysisFile)o1;
      AnalysisFile af2 = (AnalysisFile)o2;

      if (af1.getIdAnalysisFile() == null || af2.getIdAnalysisFile() == null) {
        return af1.getFileName().compareTo(af2.getFileName());
      } 
      return af1.getIdAnalysisFile().compareTo(af2.getIdAnalysisFile());

    }
  }


  public static class  AnalysisComparator implements Comparator, Serializable {
    public int compare(Object o1, Object o2)  {
      Analysis a1 = (Analysis)o1;
      Analysis a2 = (Analysis)o2;

      if (a1.getIdAnalysis() == null || a2.getIdAnalysis() == null) {
        return a1.getName().compareTo(a2.getName());
      } 
      return a1.getIdAnalysis().compareTo(a2.getIdAnalysis());

    }
  }

}