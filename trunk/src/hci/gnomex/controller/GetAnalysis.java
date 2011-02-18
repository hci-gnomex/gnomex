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


import hci.gnomex.model.Analysis;
import hci.gnomex.model.AnalysisFile;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.ExperimentDesign;
import hci.gnomex.model.ExperimentDesignEntry;
import hci.gnomex.model.ExperimentFactor;
import hci.gnomex.model.ExperimentFactorEntry;
import hci.gnomex.model.Project;
import hci.gnomex.model.Request;


public class GetAnalysis extends GNomExCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetAnalysis.class);
  
  private Integer idAnalysis;
  private String  analysisNumber;
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
    
    
    if (idAnalysis == null && analysisNumber == null) {
      this.addInvalidField("idAnalysi or analysisNumber", "Either idAnalysis or analysisNumber must be provided");
    }
    serverName = request.getServerName();
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      
      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
      
      DictionaryHelper dh = DictionaryHelper.getInstance(sess);
      baseDir = dh.getAnalysisWriteDirectory(serverName);
      
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
        List analyses = (List)sess.createQuery(buf.toString()).list();
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
        
        // If user can write analysis, show collaborators, 
        // but cull out everything but collaborator name
        if (this.getSecAdvisor().canUpdate(a)) {
          Hibernate.initialize(a.getCollaborators());
          for (Iterator i = a.getCollaborators().iterator(); i.hasNext();) {
            AppUser collab = (AppUser)i.next();
            collab.excludeMethodFromXML("getDepartment");
            collab.excludeMethodFromXML("getCodeUserPermissionKind");
            collab.excludeMethodFromXML("getEmail");
            collab.excludeMethodFromXML("getuNID");
            collab.excludeMethodFromXML("getUserNameExternal");
            collab.excludeMethodFromXML("getInstitute");
            collab.excludeMethodFromXML("getIsActive");
            collab.excludeMethodFromXML("getJobTitle");
            collab.excludeMethodFromXML("getPhone");
            collab.excludeMethodFromXML("getIsAdminPermissionLevel");
            collab.excludeMethodFromXML("getIsLabPermissionLevel");
            collab.excludeMethodFromXML("getPasswordExternalEntered");
            collab.excludeMethodFromXML("getPasswordExternal");
            collab.excludeMethodFromXML("getIsExternalUser");
            collab.excludeMethodFromXML("getLabs");
            collab.excludeMethodFromXML("getCollaboratingLabs");
            collab.excludeMethodFromXML("getManagingLabs");
          }
          
        } else {
          a.excludeMethodFromXML("getCollaborators");
        }
        
        
      
        Document doc = new Document(new Element("OpenAnalysisList"));
        Element aNode = a.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();
        
        
        // Hash the know analysis files
        Map knownAnalysisFileMap = new HashMap();
        for(Iterator i = a.getFiles().iterator(); i.hasNext();) {
          AnalysisFile af = (AnalysisFile)i.next();
          knownAnalysisFileMap.put(af.getFileName(), af);
        }

        
        // Now add in the files that exist on the file server
        Element filesNode = new Element("ExpandedAnalysisFileList");
        aNode.addContent(filesNode);
        Map analysisMap = new TreeMap();
        Map directoryMap = new TreeMap();
        Map fileMap = new HashMap();
        List analysisNumbers = new ArrayList<String>();
        GetExpandedAnalysisFileList.getFileNamesToDownload(baseDir, a.getKey(), analysisNumbers, analysisMap, directoryMap);

        
        for(Iterator i = analysisNumbers.iterator(); i.hasNext();) {
          String analysisNumber = (String)i.next();
          List directoryKeys   = (List)analysisMap.get(analysisNumber);

          // For each directory of analysis
          boolean firstDirForAnalysis = true;
          int unregisteredFileCount = 0;
          for(Iterator i1 = directoryKeys.iterator(); i1.hasNext();) {
            
            String directoryKey = (String)i1.next();
            List   theFiles     = (List)directoryMap.get(directoryKey);
            
            // For each file in the directory
            boolean firstFileInDir = true;
            for (Iterator i2 = theFiles.iterator(); i2.hasNext();) {
              AnalysisFileDescriptor fd = (AnalysisFileDescriptor) i2.next();
              
              AnalysisFile af = (AnalysisFile)knownAnalysisFileMap.get(fd.getDisplayName());
              if (af != null) {
                fd.setUploadDate(af.getUploadDate());
                fd.setComments(af.getComments());
                fd.setIdAnalysisFileString(af.getIdAnalysisFile().toString());
                fd.setIdAnalysis(a.getIdAnalysis());
              } else {
                fd.setIdAnalysisFileString("AnalysisFile" + unregisteredFileCount++);
                fd.setIdAnalysis(a.getIdAnalysis());
              }
              
              filesNode.addContent(fd.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement());
              fileMap.put(fd.getDisplayName(), null);
            }
          }
        }

        // Add any files that are registered in the db, but not on the fileserver
        for(Iterator i = a.getFiles().iterator(); i.hasNext();) {
          AnalysisFile af = (AnalysisFile)i.next();
          
          if (!fileMap.containsKey(af.getFileName())) {
            AnalysisFileDescriptor fd = new AnalysisFileDescriptor();
            fd.setDisplayName(af.getFileName());
            fd.setIdAnalysisFileString(af.getIdAnalysisFile().toString());
            fd.setIdAnalysis(af.getIdAnalysis());
            fd.setAnalysisNumber(a.getNumber());
            fd.setUploadDate(af.getUploadDate());
            fd.setComments(af.getComments());
            filesNode.addContent(fd.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement());
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