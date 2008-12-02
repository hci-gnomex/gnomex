package hci.gnomex.controller;

import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.HibernateSession;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.framework.utilities.XMLReflectException;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Hibernate;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.jdom.Element;
import org.jdom.Document;
import org.jdom.output.XMLOutputter;

import hci.gnomex.model.Lab;
import hci.gnomex.model.Project;
import hci.gnomex.model.ProjectRequestFilter;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.model.Visibility;


public class GetProjectRequestList extends GNomExCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetProjectRequestList.class);
  
  private ProjectRequestFilter filter;
  private Element              rootNode = null;
  private Element              labNode = null;
  private Element              projectNode = null;
  private Element              requestCatNode = null;
  private Element              requestNode = null;
  private String               listKind = "ProjectRequestList";
  private String               showMyLabsAlways = "N";
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    filter = new ProjectRequestFilter();
    HashMap errors = this.loadDetailObject(request, filter);
    this.addInvalidFields(errors);
    
    if (request.getParameter("showMyLabsAlways") != null && !request.getParameter("showMyLabsAlways").equals("")) {
      showMyLabsAlways = request.getParameter("showMyLabsAlways");
    }
    
    if (request.getParameter("listKind") != null && !request.getParameter("listKind").equals("")) {
      listKind = request.getParameter("listKind");
    }
    
    List experimentDesignCodes = new ArrayList();
    if (request.getParameter("experimentDesignConcatCodes") != null && !request.getParameter("experimentDesignConcatCodes").equals("")) {
      String[] codes = request.getParameter("experimentDesignConcatCodes").split(":");
      for (int i = 0; i < codes.length; i++) {
        String code = codes[i];
        experimentDesignCodes.add(code);
      }
      filter.setExperimentDesignCodes(experimentDesignCodes);
    }
    
    List experimentFactorCodes = new ArrayList();
    if (request.getParameter("experimentFactorConcatCodes") != null && !request.getParameter("experimentFactorConcatCodes").equals("")) {
      String[] codes = request.getParameter("experimentFactorConcatCodes").split(":");
      for (int i = 0; i < codes.length; i++) {
        String code = codes[i];
        experimentFactorCodes.add(code);
      }
      filter.setExperimentFactorCodes(experimentFactorCodes);
    }
    
    if (this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_ACCESS_ANY_OBJECT) && !filter.hasCriteria()) {
      this.addInvalidField("requiredCriteria", "Please enter at least one search criterion");
    }
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      
      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
      
      HashMap myLabMap = new HashMap();
      if (showMyLabsAlways.equals("Y")) {
        for(Iterator i = this.getSecAdvisor().getAllMyGroups().iterator(); i.hasNext();) {
          Lab lab = (Lab)i.next();
          if (this.getSecAdvisor().isGroupIAmMemberOrManagerOf(lab.getIdLab())) {
            myLabMap.put(lab.getIdLab(), lab);
          }
        }
      }
      
    
      StringBuffer buf = filter.getQuery(this.getSecAdvisor());
      log.info("Query for GetProjectRequestList: " + buf.toString());
      List results = (List)sess.createQuery(buf.toString()).list();

      
      buf = filter.getAnalysisExperimentQuery(this.getSecAdvisor());
      log.info("Query for GetProjectRequestList: " + buf.toString());
      List analysisResults = (List)sess.createQuery(buf.toString()).list();
      HashMap analysisMap = new HashMap();
      for(Iterator i = analysisResults.iterator(); i.hasNext();) {
        Object[] row = (Object[])i.next();
        Integer idRequest      = (Integer)row[0];
        String  analysisNumber = (String)row[1];
        String  analysisName   = (String)row[2];
        
        StringBuffer names = (StringBuffer)analysisMap.get(idRequest);
        if (names == null) {
          names = new StringBuffer();
        }
        if (names.length() > 0) {
          names.append(", ");
        }
        names.append(analysisNumber + " (" + analysisName + ")");
        analysisMap.put(idRequest, names);
      }

      Integer prevIdLab      = new Integer(-1);
      Integer prevIdProject  = new Integer(-1);
      Integer prevIdRequest  = new Integer(-1);
      String prevCodeRequestCategory    = "999";
      String prevCodeMicroarrayCategory = "999";
      
      Document doc = new Document(new Element(listKind));
      rootNode = doc.getRootElement();
      for(Iterator i = results.iterator(); i.hasNext();) {
        Object[] row = (Object[])i.next();
        
        
        Integer idProject = row[0] == null ? new Integer(-2) : (Integer)row[0];
        Integer idRequest = row[4] == null ? new Integer(-2) : (Integer)row[4];
        Integer idLab     = row[11]== null ? new Integer(-2) : (Integer)row[11];    
        String  codeRequestCategory        = row[15]== null ? "" : (String)row[15];     
        String  codeMicroarrayCategory     = row[16]== null ? "" : (String)row[16];
        StringBuffer analysisNames = (StringBuffer)analysisMap.get(idRequest);
        
        Element n = null;
        if (idLab.intValue() != prevIdLab.intValue()) {
          // Keep track of which of users labs are in results set
          if (showMyLabsAlways.equals("Y")) {
            myLabMap.remove(idLab);            
          }
          addLabNode(row);
          addProjectNode(row);
          if (idRequest.intValue() != -2) {
            addRequestCategoryNode(row);
            addRequestNode(row, analysisNames);          
            addSampleNode(row);            
          }
        } else if (idProject.intValue() != prevIdProject.intValue()) {
          addProjectNode(row);
          if (idRequest.intValue() != -2) {
            addRequestCategoryNode(row);
            addRequestNode(row, analysisNames);          
            addSampleNode(row);
          }
        } else if (!codeRequestCategory.equals(prevCodeRequestCategory) ||
                    !codeMicroarrayCategory.equals(prevCodeMicroarrayCategory)) {
          if (idRequest.intValue() != -2) {
            addRequestCategoryNode(row);
            addRequestNode(row, analysisNames);          
            addSampleNode(row);
          }
        } else if (idRequest.intValue() != prevIdRequest.intValue()) {
          if (idRequest.intValue() != -2) {
            addRequestNode(row, analysisNames);          
            addSampleNode(row);
          }
        } else {
          if (idRequest.intValue() != -2) {
            addSampleNode(row);
          }
        }

        prevIdRequest = idRequest;
        prevIdProject = idProject;
        prevIdLab     = idLab;
        prevCodeRequestCategory = codeRequestCategory;
        prevCodeMicroarrayCategory = codeMicroarrayCategory;
      }
      
      
      // For those labs that user is member of that do not have any projects,
      // create a lab node in the XML document.
      if (showMyLabsAlways.equals("Y")) {
        for(Iterator i = myLabMap.keySet().iterator(); i.hasNext();) {
          Lab lab = (Lab)myLabMap.get(i.next());
          addLabNode(lab);
        }
      }
   
      
      XMLOutputter out = new org.jdom.output.XMLOutputter();
      this.xmlResult = out.outputString(doc);

      // Garbage collect
      out = null;
      doc = null;
      rootNode = null;
      results = null;
      System.gc();
      
      
      setResponsePage(this.SUCCESS_JSP);
    }catch (NamingException e){
      log.error("An exception has occurred in GetRequestList ", e);
      e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());
        
    }catch (SQLException e) {
      log.error("An exception has occurred in GetRequestList ", e);
      e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());
    }catch (Exception e) {
      log.error("An exception has occurred in GetRequestList ", e);
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

  
  private void addLabNode(Object[] row) {
    labNode = new Element("Lab");
    labNode.setAttribute("idLab",            ((Integer)row[11]).toString());
    labNode.setAttribute("labName",          (row[17] == null? "" : (String)row[17]).toString());
    labNode.setAttribute("projectLabName",   row[19] == null ? "" : (String)row[19]);
    labNode.setAttribute("label",            row[19] == null ? "" : (String)row[19]);
    rootNode.addContent(labNode);
  }

  private void addLabNode(Lab lab) {
    labNode = new Element("Lab");
    labNode.setAttribute("idLab",            lab.getIdLab().toString());
    labNode.setAttribute("labName",          lab.getName());
    labNode.setAttribute("projectLabName",   lab.getName());
    labNode.setAttribute("label",            lab.getName());
    rootNode.addContent(labNode);
  }
  
  private void addProjectNode(Object[] row) {
    projectNode = new Element("Project");
    projectNode.setAttribute("idProject",              row[0] == null ? ""  : ((Integer)row[0]).toString());
    projectNode.setAttribute("projectName",            row[1] == null ? ""  : (String)row[1]);
    projectNode.setAttribute("label",                  row[1] == null ? ""  : (String)row[1]);
    projectNode.setAttribute("projectDescription",     row[2] == null ? ""  : (String)row[2]);
    projectNode.setAttribute("codeVisibility",         row[20] == null ? "" : (String)row[20]);
    projectNode.setAttribute("ownerFirstName",         row[22] == null ? "" : (String)row[22]);
    projectNode.setAttribute("ownerLastName",          row[23] == null ? "" : (String)row[23]);
    
    if (projectNode.getAttributeValue("codeVisibility").equals(Visibility.VISIBLE_TO_PUBLIC)) {
      projectNode.setAttribute("projectPublicNote",          "(Public) ");
    } else {
      projectNode.setAttribute("projectPublicNote", "");
    }
    
    
    projectNode.setAttribute("idLab",                  row[11] == null ? "" : ((Integer)row[11]).toString());
    projectNode.setAttribute("idAppUser",              row[13] == null ? "" : ((Integer)row[13]).toString());
    labNode.addContent(projectNode);
  }
  
  private void addRequestCategoryNode(Object[] row) {
    if (filter.getShowCategory().equals("Y")) {
      requestCatNode = new Element("RequestCategory");
      requestCatNode.setAttribute("idProject",              row[0] == null ? ""     : ((Integer)row[0]).toString());
      requestCatNode.setAttribute("codeRequestCategory",    row[15] == null ? ""    : (String)row[15]);
      requestCatNode.setAttribute("codeMicroarrayCategory", row[16] == null ? ""    : (String)row[16]);
      requestCatNode.setAttribute("label",                  row[16] == null ? ""    : (String)row[16]);
      projectNode.addContent(requestCatNode);
      
    }
  }
  
  private void addRequestNode(Object[] row, StringBuffer analysisNames) {
    requestNode = new Element("Request");
    requestNode.setAttribute("idRequest",              row[4] == null ? ""  : ((Integer)row[4]).toString());
    requestNode.setAttribute("requestNumber",          row[5] == null ? ""  : (String)row[5]);
    requestNode.setAttribute("requestCreateDate",      row[6] == null ? ""  : this.formatDate((java.sql.Date)row[6], this.DATE_OUTPUT_ALTIO));
    requestNode.setAttribute("requestCreateDateDisplay", row[6] == null ? ""  : this.formatDate((java.sql.Date)row[6], this.DATE_OUTPUT_SQL));
    requestNode.setAttribute("createDate",             row[6] == null ? ""  : this.formatDate((java.sql.Date)row[6], this.DATE_OUTPUT_SLASH));
    requestNode.setAttribute("idSlideProduct",         row[9] == null ? ""  : ((Integer)row[9]).toString());
    requestNode.setAttribute("idLab",                  row[12] == null ? "" : ((Integer)row[12]).toString());
    requestNode.setAttribute("idAppUser",              row[14] == null ? "" : ((Integer)row[14]).toString());
    requestNode.setAttribute("codeRequestCategory",    row[15] == null ? "" : ((String)row[15]).toString());
    requestNode.setAttribute("codeMicroarrayCategory", row[16] == null ? "" : ((String)row[16]).toString());
    requestNode.setAttribute("labName",                (row[17] == null? "" : (String)row[17]).toString());
    requestNode.setAttribute("slideProductName",       row[18] == null ? "" : (String)row[18]);
    requestNode.setAttribute("projectLabName",         row[19] == null ? "" : (String)row[19]);
    requestNode.setAttribute("projectName",            row[1] == null ? ""  : (String)row[1]);
    requestNode.setAttribute("codeVisibility",         row[21] == null ? "" : (String)row[21]);
    requestNode.setAttribute("ownerFirstName",         row[24] == null ? "" : (String)row[24]);
    requestNode.setAttribute("ownerLastName",          row[25] == null ? "" : (String)row[25]);
    requestNode.setAttribute("isDirty",                "N");
    requestNode.setAttribute("isSelected",             "N");
    requestNode.setAttribute("analysisNames",          analysisNames != null ? analysisNames.toString() : "");
    
    if (requestNode.getAttributeValue("codeVisibility").equals(Visibility.VISIBLE_TO_PUBLIC)) {
      requestNode.setAttribute("requestPublicNote",          "(Public) ");
    } else {
      requestNode.setAttribute("requestPublicNote", "");
    }
    
    if (requestNode.getAttributeValue("codeRequestCategory").equals(RequestCategory.QUALITY_CONTROL_REQUEST_CATEGORY)) {
      StringBuffer displayName = new StringBuffer();
      displayName.append(requestNode.getAttributeValue("requestNumber"));
      displayName.append(" - ");
      displayName.append(requestNode.getAttributeValue("ownerFirstName"));
      displayName.append(" ");
      displayName.append(requestNode.getAttributeValue("ownerLastName"));
      displayName.append(" ");
      displayName.append(requestNode.getAttributeValue("requestCreateDate"));      

      requestNode.setAttribute("displayName", displayName.toString());
      requestNode.setAttribute("label",       displayName.toString());
    } else {
      StringBuffer displayName = new StringBuffer();
      displayName.append(requestNode.getAttributeValue("requestNumber"));
      displayName.append(" - ");
      displayName.append(requestNode.getAttributeValue("slideProductName"));      
      displayName.append(" - ");
      displayName.append(requestNode.getAttributeValue("ownerFirstName"));
      displayName.append(" ");
      displayName.append(requestNode.getAttributeValue("ownerLastName"));
      displayName.append(" ");
      displayName.append(requestNode.getAttributeValue("requestCreateDateDisplay"));      
      
      requestNode.setAttribute("displayName", displayName.toString());
      requestNode.setAttribute("label",       displayName.toString());
    }
    
    if (filter.getShowCategory().equals("Y")) {
      requestCatNode.addContent(requestNode);         
    } else {
      projectNode.addContent(requestNode);
    }
  }
  
  private void addSampleNode(Object[] row) {
    if (filter.getShowSamples().equals("Y")) {
      Element n = new Element("Sample");
      n.setAttribute("sampleName",           row[7] == null ? ""  : (String)row[7]);
      n.setAttribute("label",                row[7] == null ? ""  : (String)row[7]);
      n.setAttribute("idSampleType",         row[8] == null ? ""  : ((Integer)row[8]).toString());
      n.setAttribute("idSample",             row[10] == null ? "" : ((Integer)row[10]).toString());
      requestNode.addContent(n);      
    }
  }
}