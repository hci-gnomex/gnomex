package hci.gnomex.controller;

import hci.gnomex.utility.AnalysisFileDescriptor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.framework.security.UnknownPermissionException;
import hci.framework.utilities.XMLReflectException;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.jdom.Element;
import org.jdom.Document;
import org.jdom.output.XMLOutputter;


import hci.gnomex.constants.Constants;
import hci.gnomex.model.Analysis;
import hci.gnomex.model.AnalysisCollaborator;
import hci.gnomex.model.AnalysisFile;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.ExperimentDesign;
import hci.gnomex.model.ExperimentDesignEntry;
import hci.gnomex.model.ExperimentFactor;
import hci.gnomex.model.ExperimentFactorEntry;
import hci.gnomex.model.Project;
import hci.gnomex.model.Property;
import hci.gnomex.model.PropertyEntry;
import hci.gnomex.model.PropertyEntryValue;
import hci.gnomex.model.PropertyOption;
import hci.gnomex.model.PropertyType;
import hci.gnomex.model.Request;


public class GetAnalysis extends GNomExCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetAnalysis.class);
  
  private Integer idAnalysis;
  private String  analysisNumber;
  private String  showUploads = "N";
  private String  serverName;
  private String  baseDir;

  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    if (request.getParameter("idAnalysis") != null) {
      idAnalysis = new Integer(request.getParameter("idAnalysis"));
    }     
    if (request.getParameter("analysisNumber") != null && !request.getParameter("analysisNumber").equals("")) {
      analysisNumber = request.getParameter("analysisNumber");
    } 
    if (request.getParameter("showUploads") != null && !request.getParameter("showUploads").equals("")) {
      showUploads = request.getParameter("showUploads");
    } 
    
    if (idAnalysis == null && analysisNumber == null) {
      this.addInvalidField("idAnalysis or analysisNumber", "Either idAnalysis or analysisNumber must be provided");
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
        
        // If user can write analysis, show collaborators.
        if (this.getSecAdvisor().canUpdate(a)) {
          Hibernate.initialize(a.getCollaborators());
        } else {
          a.excludeMethodFromXML("getCollaborators");
        }
        
        Document doc = new Document(new Element("OpenAnalysisList"));
        Element aNode = a.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();
        
        
        // Hash the know analysis files
        Map knownAnalysisFileMap = new HashMap();
        for(Iterator i = a.getFiles().iterator(); i.hasNext();) {
          AnalysisFile af = (AnalysisFile)i.next();
          knownAnalysisFileMap.put(af.getQualifiedFileName(), af);
        }

        
        // Now add in the files that exist on the file server
        Element filesNode = new Element("ExpandedAnalysisFileList");
        aNode.addContent(filesNode);
        
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
            // Only show these files if user has write permissions.
            if (showUploads.equals("Y") && this.getSecAdvisor().canUploadData(a)) {
              Element analysisUploadNode = new Element("AnalysisUpload");
              filesNode.addContent(analysisUploadNode);
              String key = a.getKey(Constants.UPLOAD_STAGING_DIR);
              GetAnalysisDownloadList.addExpandedFileNodes(baseDir, aNode, analysisUploadNode, analysisNumber, key, dh, knownAnalysisFileMap, fileMap);
            }

            List   theFiles     = (List)directoryMap.get(directoryKey);
            
            // For each file in the directory
            
            for (Iterator i2 = theFiles.iterator(); i2.hasNext();) {
              AnalysisFileDescriptor fd = (AnalysisFileDescriptor) i2.next();

              AnalysisFile af = (AnalysisFile)knownAnalysisFileMap.get(fd.getDisplayName());
              
              if (fd!=null && fd.getDisplayName().equals(Constants.UPLOAD_STAGING_DIR)) {
                continue;
              }
              
              Element fdNode = new Element("AnalysisFileDescriptor");
              
              if (af != null) {
                fd.setUploadDate(af.getUploadDate());
                fd.setComments(af.getComments());
                fd.setIdAnalysisFileString(af.getIdAnalysisFile().toString());
                fd.setIdAnalysis(a.getIdAnalysis());
              } else {
                fd.setIdAnalysisFileString("AnalysisFile-" + fd.getDisplayName());
                fd.setIdAnalysis(a.getIdAnalysis());
              }
              fd.setQualifiedFilePath(directoryName);
              fd.setBaseFilePath(GetAnalysisDownloadList.getAnalysisDirectory(baseDir,a));

              fdNode.setAttribute("idAnalysis", a.getIdAnalysis()!=null?a.getIdAnalysis().toString():"");
              fdNode.setAttribute("key", directoryName != "" ? a.getKey(directoryName) : a.getKey());
              fdNode.setAttribute("type", fd.getType() != null ? fd.getType() : "");
              fdNode.setAttribute("displayName", fd.getDisplayName() != null ? fd.getDisplayName() : "");
              fdNode.setAttribute("fileSize", String.valueOf(fd.getFileSize()));
              fdNode.setAttribute("fileSizeText", String.valueOf(fd.getFileSize()) + " b");
              fdNode.setAttribute("childFileSize", String.valueOf(fd.getFileSize()));
              fdNode.setAttribute("fileName", fd.getFileName() != null ? fd.getFileName() : "");
              fdNode.setAttribute("qualifiedFilePath", fd.getQualifiedFilePath() != null ? fd.getQualifiedFilePath() : "");
              fdNode.setAttribute("baseFilePath", fd.getBaseFilePath() != null ? fd.getBaseFilePath() : "");
              fdNode.setAttribute("comments", fd.getComments() != null ? fd.getComments() : "");
              fdNode.setAttribute("lastModifyDate", fd.getLastModifyDate() != null ? fd.getLastModifyDate().toString() : "");
              fdNode.setAttribute("zipEntryName", fd.getZipEntryName() != null ? fd.getZipEntryName() : "");
              fdNode.setAttribute("number", fd.getAnalysisNumber() != null ? fd.getAnalysisNumber() : "");
              fdNode.setAttribute("idAnalysisFileString", fd.getIdAnalysisFileString());
              fdNode.setAttribute("isSelected", "N");
              fdNode.setAttribute("state", "unchecked");
              
              filesNode.addContent(fdNode);
              GetAnalysisDownloadList.recurseAddChildren(fdNode, fd, fileMap, knownAnalysisFileMap);
              
              fileMap.put(fd.getQualifiedFileName(), null);
            }
          }
        }

        // Add any files that are registered in the db, but not on the fileserver
        for(Iterator i = a.getFiles().iterator(); i.hasNext();) {
          AnalysisFile af = (AnalysisFile)i.next();

          if (!fileMap.containsKey(af.getQualifiedFileName())) {
            AnalysisFileDescriptor fd = new AnalysisFileDescriptor();

            fd.setDisplayName(af.getFileName());
            fd.setFileName(af.getFullPathName());
            fd.setQualifiedFilePath(af.getQualifiedFilePath());
            fd.setBaseFilePath(af.getBaseFilePath());
            fd.setIdAnalysisFileString(af.getIdAnalysisFile().toString());
            fd.setIdAnalysis(af.getIdAnalysis());
            fd.setAnalysisNumber(a.getNumber());
            fd.setUploadDate(af.getUploadDate());
            fd.setComments(af.getComments());
            fd.setFileSize(af.getFileSize() != null ? af.getFileSize().longValue() : 0);
            fd.excludeMethodFromXML("getChildren");

            Element fdNode = fd.toXMLDocument(null, fd.DATE_OUTPUT_ALTIO).getRootElement();
            fdNode.setAttribute("isSelected", "N");
            fdNode.setAttribute("state", "unchecked");

            filesNode.addContent(fdNode);
            GetAnalysisDownloadList.recurseAddChildren(fdNode, fd,fileMap, knownAnalysisFileMap);
          }
        }

        // Add properties
        Element pNode = getProperties(dh, a);
        aNode.addContent(pNode);

        doc.getRootElement().addContent(aNode);
        
        XMLOutputter out = new org.jdom.output.XMLOutputter();
        this.xmlResult = out.outputString(doc);
      }
    
      if (isValid()) {
        setResponsePage(this.SUCCESS_JSP);
      } else {
        setResponsePage(this.ERROR_JSP);
      }
    
    }catch (UnknownPermissionException e){
      log.error("An exception has occurred in GetAnalysis ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }catch (NamingException e){
      log.error("An exception has occurred in GetAnalysis ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    }catch (SQLException e) {
      log.error("An exception has occurred in GetAnalysis ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (XMLReflectException e){
      log.error("An exception has occurred in GetAnalysis ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e){
      log.error("An exception has occurred in GetAnalysis ", e);
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
  
  /**
   *  Adds custom properties to the xml document.
   *
   *@param  aNode     root element of document to append properties to
   *@param  dh        Dictionary helper to find properties
   *@param  analysis  Analysis object with property values.
   */
  private Element getProperties(DictionaryHelper dh, Analysis analysis) {
    Element propertiesNode = new Element("AnalysisProperties");
    for (Property property : dh.getPropertyList()) {

      if (property.getForAnalysis() == null || !property.getForAnalysis().equals("Y")) {
        continue;
      }
      
      Element propNode = new Element("PropertyEntry");
      propertiesNode.addContent(propNode);

      PropertyEntry ap = null;
      for(Iterator i = analysis.getPropertyEntries().iterator(); i.hasNext();) {
        PropertyEntry propertyEntry = (PropertyEntry)i.next();
        if (propertyEntry.getIdProperty().equals(property.getIdProperty())) {
          ap = propertyEntry;
          break;
        }
      }

      propNode.setAttribute("idPropertyEntry", ap != null ? ap.getIdPropertyEntry().toString() : "");  
      propNode.setAttribute("name", property.getName());
      propNode.setAttribute("value", ap != null && ap.getValue() != null ? ap.getValue() : "");
      propNode.setAttribute("codePropertyType", property.getCodePropertyType());
      propNode.setAttribute("idProperty", property.getIdProperty().toString());

      if (ap != null && ap.getValues() != null && ap.getValues().size() > 0) {
        for (Iterator i1 = ap.getValues().iterator(); i1.hasNext();) {
          PropertyEntryValue av = (PropertyEntryValue)i1.next();
          Element valueNode = new Element("PropertyEntryValue");
          propNode.addContent(valueNode);
          valueNode.setAttribute("idPropertyEntryValue", av.getIdPropertyEntryValue().toString());
          valueNode.setAttribute("value", av.getValue() != null ? av.getValue() : "");
          valueNode.setAttribute("url", av.getUrl() != null ? av.getUrl() : "");
          valueNode.setAttribute("urlDisplay", av.getUrlDisplay() != null ? av.getUrlDisplay() : "");
          valueNode.setAttribute("urlAlias", av.getUrlAlias() != null ? av.getUrlAlias() : "");
        }
      }
      if (property.getCodePropertyType().equals(PropertyType.URL)) {
        // Add an empty value for URL
        Element emptyNode = new Element("PropertyEntryValue");
        propNode.addContent(emptyNode);
        emptyNode.setAttribute("idPropertyEntryValue", "");
        emptyNode.setAttribute("url", "Enter URL here...");
        emptyNode.setAttribute("urlAlias", "Enter alias here...");
        emptyNode.setAttribute("urlDisplay", "");
        emptyNode.setAttribute("value", "");
      }

      if (property.getOptions() != null && property.getOptions().size() > 0) {
        for (Iterator i1 = property.getOptions().iterator(); i1.hasNext();) {
          PropertyOption option = (PropertyOption)i1.next();
          Element optionNode = new Element("PropertyOption");
          propNode.addContent(optionNode);
          optionNode.setAttribute("idPropertyOption", option.getIdPropertyOption().toString());
          optionNode.setAttribute("name", option.getOption());
          boolean isSelected = false;
          if (ap != null && ap.getOptions() != null) {
            for (Iterator i2 = ap.getOptions().iterator(); i2.hasNext();) {
              PropertyOption optionSelected = (PropertyOption)i2.next();
              if (optionSelected.getIdPropertyOption().equals(option.getIdPropertyOption())) {
                isSelected = true;
                break;
              }
            }
          }
          optionNode.setAttribute("selected", isSelected ? "Y" : "N");
        }
      }
    }      
    return propertiesNode;
  }
  
  /**
   *  The callback method allowing you to manipulate the HttpServletRequest
   *  prior to forwarding to the response JSP. This can be used to put the
   *  results from the execute method into the request object for display in the
   *  JSP.
   *
   *@param  request  The new requestState value
   *@return          Description of the Return Value
   */
  public HttpServletRequest setRequestState(HttpServletRequest request) {
    // load any result objects into request attributes, keyed by the useBean id in the jsp
    request.setAttribute("xmlResult",this.xmlResult);
    
    // Garbage collect
    this.xmlResult = null;
    System.gc();
    
    return request;
  }

  /**
   *  The callback method called after the loadCommand, and execute methods,
   *  this method allows you to manipulate the HttpServletResponse object prior
   *  to forwarding to the result JSP (add a cookie, etc.)
   *
   *@param  request  The HttpServletResponse for the command
   *@return          The processed response
   */
  public HttpServletResponse setResponseState(HttpServletResponse response) {
    response.setHeader("Cache-Control", "max-age=0, must-revalidate");
    return response;
  }

}