package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.BillingTemplate;
import hci.gnomex.model.Lab;
import hci.gnomex.model.ProjectRequestFilter;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.Request;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.model.Step;
import hci.gnomex.model.Visibility;
import hci.gnomex.model.WorkItem;
import hci.gnomex.utility.BillingTemplateQueryManager;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.apache.log4j.Logger;

public class GetProjectRequestList extends GNomExCommand implements Serializable {

  private static Logger LOG = Logger.getLogger(GetProjectRequestList.class);

  private ProjectRequestFilter filter;
  private Element              rootNode = null;
  private Element              labNode = null;
  private Element              projectNode = null;
  private Element              requestCatNode = null;
  private Element              requestNode = null;
  private String               listKind = "ProjectRequestList";
  private String               showMyLabsAlways = "N";
  private Boolean              hasQcWorkItems = false;


  private int                  experimentCount = 0;
  private String               message = "";
  
  private static final int     DEFAULT_MAX_REQUESTS_COUNT = 100;


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

    List experimentDesignCodes = new ArrayList<String>();
    if (request.getParameter("experimentDesignConcatCodes") != null && !request.getParameter("experimentDesignConcatCodes").equals("")) {
      String[] codes = request.getParameter("experimentDesignConcatCodes").split(":");
      for (int i = 0; i < codes.length; i++) {
        String code = codes[i];
        experimentDesignCodes.add(code);
      }
      filter.setExperimentDesignCodes(experimentDesignCodes);
    }

    List experimentFactorCodes = new ArrayList<String>();
    if (request.getParameter("experimentFactorConcatCodes") != null && !request.getParameter("experimentFactorConcatCodes").equals("")) {
      String[] codes = request.getParameter("experimentFactorConcatCodes").split(":");
      for (int i = 0; i < codes.length; i++) {
        String code = codes[i];
        experimentFactorCodes.add(code);
      }
      filter.setExperimentFactorCodes(experimentFactorCodes);
    }



  }

  public Command execute() throws RollBackCommandException {
    Document doc = new Document(new Element(listKind));
    rootNode = doc.getRootElement();
    List results = null;

    try {
      if (!filter.hasSufficientCriteria(this.getSecAdvisor())) {
        message = "Please select a filter";
        rootNode.setAttribute("message", message);

      } else {
        Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());

        DictionaryHelper dictionaryHelper = DictionaryHelper.getInstance(sess);

        HashMap myLabMap = new HashMap();
        if (showMyLabsAlways.equals("Y")) {
          for(Iterator i = this.getSecAdvisor().getAllMyGroups().iterator(); i.hasNext();) {
            Lab lab = (Lab)i.next();
            if (this.getSecAdvisor().isGroupIAmMemberOrManagerOf(lab.getIdLab())) {
              myLabMap.put(lab.getIdLab(), lab);
            }
          }
        }

        //        if(PropertyDictionaryHelper.getInstance(sess).getProperty(PropertyDictionary.EXTERNAL_DATA_SHARING_SITE).equals("Y")) {
        //        	filter.setIsForExternalDataSharingSite(true);
        //        }   


        String message = "";
        StringBuffer buf = filter.getQuery(this.getSecAdvisor(), dictionaryHelper);
        LOG.info("Query for GetProjectRequestList: " + buf.toString());
        Query query = sess.createQuery(buf.toString());
        results = (List)query.list();


        buf = filter.getAnalysisExperimentQuery(this.getSecAdvisor());
        LOG.info("Query for GetProjectRequestList: " + buf.toString());
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
        String prevCodeApplication = "999";


        Integer maxExperiments = getMaxExperiments(sess);

        Map<Integer, Integer> requestsToSkip = this.getSecAdvisor().getBSTXSecurityIdsToExclude(sess, dictionaryHelper, results, 4, 15);

        for(Iterator i = results.iterator(); i.hasNext();) {
          Object[] row = (Object[])i.next();


          Integer idProject = row[0] == null ? new Integer(-2) : (Integer)row[0];
          Integer idRequest = row[4] == null ? new Integer(-2) : (Integer)row[4];
          if (requestsToSkip.get(idRequest) != null) {
            // skip request due to bstx security.
            continue;
          }
          Integer idLab     = row[11]== null ? new Integer(-2) : (Integer)row[11];    
          String  codeRequestCategory        = row[15]== null ? "" : (String)row[15];     
          String  codeApplication     = row[16]== null ? "" : (String)row[16];
          StringBuffer analysisNames = (StringBuffer)analysisMap.get(idRequest);

          if(idRequest != -2){
            Request req = (Request)sess.load(Request.class, idRequest);
            if (!this.getSecAdvisor().canRead(req)) {
              continue;
            } else {
              hasQcWorkItems = false;
              for(Iterator j = req.getWorkItems().iterator(); j.hasNext();){
                WorkItem wi = (WorkItem)j.next();
                if(wi.getCodeStepNext().equals(Step.QUALITY_CONTROL_STEP)){
                  hasQcWorkItems = true;
                  break;
                }
              }
            }
          }

          if (idLab.intValue() != prevIdLab.intValue()) {
            // Keep track of which of users labs are in results set
            if (showMyLabsAlways.equals("Y")) {
              myLabMap.remove(idLab);            
            }
            addLabNode(row);
            addProjectNode(row);
            if (idRequest.intValue() != -2) {
              addRequestCategoryNode(row);
              addRequestNode(row, analysisNames, dictionaryHelper, sess);                   
              addSampleNode(row);            
            }
          } else if (idProject.intValue() != prevIdProject.intValue()) {
            addProjectNode(row);
            if (idRequest.intValue() != -2) {
              addRequestCategoryNode(row);
              addRequestNode(row, analysisNames, dictionaryHelper, sess);          
              addSampleNode(row);
            }
          } else if (filter.getShowCategory().equals("Y") &&
              !codeRequestCategory.equals(prevCodeRequestCategory) ||
              !codeApplication.equals(prevCodeApplication)) {
            if (idRequest.intValue() != -2) {
              addRequestCategoryNode(row);
              addRequestNode(row, analysisNames, dictionaryHelper, sess);          
              addSampleNode(row);
            }
          } else if (idRequest.intValue() != prevIdRequest.intValue()) {
            if (idRequest.intValue() != -2) {
              addRequestNode(row, analysisNames, dictionaryHelper, sess);          
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
          prevCodeApplication = codeApplication;

          if (experimentCount >= maxExperiments) {
            break;
          }
        }


        // For those labs that user is member of that do not have any projects,
        // create a lab node in the XML document.
        if (showMyLabsAlways.equals("Y")) {
          for(Iterator i = myLabMap.keySet().iterator(); i.hasNext();) {
            Lab lab = (Lab)myLabMap.get(i.next());
            addLabNode(lab);
          }
        }

        rootNode.setAttribute("experimentCount", Integer.valueOf(experimentCount).toString());
        message = experimentCount == maxExperiments ? "First " + maxExperiments + " displayed" : ""; 
        rootNode.setAttribute("message", message);

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
    }catch (Exception e) {
      LOG.error("An exception has occurred in GetRequestList ", e);
      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        this.getSecAdvisor().closeReadOnlyHibernateSession();        
      } catch(Exception e){
        LOG.error("Error", e);
      }
    }

    return this;
  }

  private Integer getMaxExperiments(Session sess) {
    Integer maxExperiments = DEFAULT_MAX_REQUESTS_COUNT;
    String prop = PropertyDictionaryHelper.getInstance(sess).getProperty(PropertyDictionary.EXPERIMENT_VIEW_LIMIT);
    if (prop != null && prop.length() > 0) {
      try {
        maxExperiments = Integer.parseInt(prop);
      }
      catch(NumberFormatException e) {
      }    
    }
    return maxExperiments;
  }

  private void addLabNode(Object[] row) {
    String labName = Lab.formatLabNameFirstLast((String)row[18], (String)row[17]);
    String projectLabName = Lab.formatLabNameFirstLast((String)row[21], (String)row[20]);

    labNode = new Element("Lab");
    labNode.setAttribute("idLab",            ((Integer)row[11]).toString());
    labNode.setAttribute("labName",          labName);
    labNode.setAttribute("projectLabName",   projectLabName);
    labNode.setAttribute("label",            projectLabName);
    rootNode.addContent(labNode);
  }

  private void addLabNode(Lab lab) {
    labNode = new Element("Lab");
    labNode.setAttribute("idLab",            lab.getIdLab().toString());
    labNode.setAttribute("labName",          lab.getName(false, true));
    labNode.setAttribute("projectLabName",   lab.getName(false, true));
    labNode.setAttribute("label",            lab.getName(false, true));
    rootNode.addContent(labNode);
  }

  private void addProjectNode(Object[] row) {
    projectNode = new Element("Project");
    projectNode.setAttribute("idProject",              row[0] == null ? ""  : ((Integer)row[0]).toString());
    projectNode.setAttribute("projectName",            row[1] == null ? ""  : (String)row[1]);
    projectNode.setAttribute("label",                  row[1] == null ? ""  : (String)row[1]);
    projectNode.setAttribute("projectDescription",     row[2] == null ? ""  : (String)row[2]);
    projectNode.setAttribute("ownerFirstName",         row[26] == null ? "" : (String)row[26]);
    projectNode.setAttribute("ownerLastName",          row[27] == null ? "" : (String)row[27]);



    projectNode.setAttribute("idLab",                  row[11] == null ? "" : ((Integer)row[11]).toString());
    projectNode.setAttribute("idAppUser",              row[13] == null ? "" : ((Integer)row[13]).toString());
    labNode.addContent(projectNode);
  }

  private void addRequestCategoryNode(Object[] row) {
    if (filter.getShowCategory().equals("Y")) {
      requestCatNode = new Element("RequestCategory");
      requestCatNode.setAttribute("idProject",              row[0] == null ? ""     : ((Integer)row[0]).toString());
      requestCatNode.setAttribute("codeRequestCategory",    row[15] == null ? ""    : (String)row[15]);
      requestCatNode.setAttribute("codeApplication",        row[16] == null ? ""    : (String)row[16]);
      requestCatNode.setAttribute("label",                  row[16] == null ? ""    : (String)row[16]);
      projectNode.addContent(requestCatNode);

    }
  }

  private void addRequestNode(Object[] row, StringBuffer analysisNames, DictionaryHelper dictionaryHelper, Session sess) {
    experimentCount++;
    String labName = Lab.formatLabNameFirstLast((String)row[18], (String)row[17]);
    String projectLabName = Lab.formatLabNameFirstLast((String)row[21], (String)row[20]);


    String codeRequestCategory =  row[15] == null ? "" : ((String)row[15]).toString();
    RequestCategory requestCategory = dictionaryHelper.getRequestCategoryObject(codeRequestCategory);
    
    boolean hasMultipleAccounts = false;
    Request request = null;
    if (row[4] != null) {
        request = sess.load(Request.class, (Integer) row[4]);
    }
    BillingTemplate billingTemplate = null;
    if (request != null) {
        billingTemplate = BillingTemplateQueryManager.retrieveBillingTemplate(sess, request);
    }
    if (billingTemplate != null && billingTemplate.getItems() != null && billingTemplate.getItems().size() > 1) {
        hasMultipleAccounts = true;
    }

    requestNode = new Element("Request");
    requestNode.setAttribute("idRequest",              row[4] == null ? ""  : ((Integer)row[4]).toString());
    requestNode.setAttribute("requestNumber",          row[5] == null ? ""  : (String)row[5]);
    requestNode.setAttribute("requestCreateDate",      row[6] == null ? ""  : this.formatDate((java.util.Date)row[6], this.DATE_OUTPUT_ALTIO));
    requestNode.setAttribute("requestCreateDateDisplay", row[6] == null ? ""  : this.formatDate((java.util.Date)row[6], this.DATE_OUTPUT_SQL));
    requestNode.setAttribute("requestCreateDateDisplayMedium", row[6] == null ? ""  : DateFormat.getDateInstance(DateFormat.MEDIUM).format((java.util.Date)row[6]));
    requestNode.setAttribute("createDate",             row[6] == null ? ""  : this.formatDate((java.util.Date)row[6], this.DATE_OUTPUT_SLASH));
    requestNode.setAttribute("idSlideProduct",         row[9] == null ? ""  : ((Integer)row[9]).toString());
    requestNode.setAttribute("idLab",                  row[12] == null ? "" : ((Integer)row[12]).toString());
    requestNode.setAttribute("idAppUser",              row[14] == null ? "" : ((Integer)row[14]).toString());
    requestNode.setAttribute("codeRequestCategory",    codeRequestCategory);
    requestNode.setAttribute("icon",                   requestCategory != null && requestCategory.getIcon() != null ? requestCategory.getIcon() : "");
    requestNode.setAttribute("type",                   requestCategory != null && requestCategory.getType() != null ? requestCategory.getType() : "");
    requestNode.setAttribute("codeApplication",        row[16] == null ? "" : ((String)row[16]).toString());
    requestNode.setAttribute("labName",                labName);
    requestNode.setAttribute("slideProductName",       row[19] == null ? "" : (String)row[19]);
    requestNode.setAttribute("projectLabName",         projectLabName);
    requestNode.setAttribute("projectName",            row[1] == null ? ""  : (String)row[1]);
    requestNode.setAttribute("codeVisibility",         row[23] == null ? "" : (String)row[23]);
    requestNode.setAttribute("ownerFirstName",         row[26] == null ? "" : (String)row[26]);
    requestNode.setAttribute("ownerLastName",          row[27] == null ? "" : (String)row[27]);
    requestNode.setAttribute("isExternal",             row[28] == null ? "" : (String)row[28]);
    requestNode.setAttribute("name",                   row[29] == null ? "" : (String)row[29]);
    requestNode.setAttribute("isDirty",                "N");
    requestNode.setAttribute("isSelected",             "N");
    requestNode.setAttribute("analysisNames",          analysisNames != null ? analysisNames.toString() : "");
    requestNode.setAttribute("idInstitution",          row[31] == null ? "" : ((Integer)row[31]).toString());
    requestNode.setAttribute("hasQcWorkItems",         hasQcWorkItems == true ? "Y" : "N");
    requestNode.setAttribute("idSubmitter",            row[32] == null ? "" : ((Integer)row[32]).toString());
    requestNode.setAttribute("hasMultipleAccounts",    hasMultipleAccounts ? "Y" : "N");

    if (requestNode.getAttributeValue("codeVisibility").equals(Visibility.VISIBLE_TO_PUBLIC)) {
      requestNode.setAttribute("requestPublicNote",          "(Public) ");
    } else {
      requestNode.setAttribute("requestPublicNote", "");
    }

    Integer idLab = (Integer)row[12];
    Integer idAppUser = (Integer)row[14];
    requestNode.setAttribute("canUpdateVisibility", this.getSecAdvisor().canUpdateVisibility(idLab, idAppUser) ? "Y" : "N");

    if (RequestCategory.isMicroarrayRequestCategory(requestNode.getAttributeValue("codeRequestCategory"))) {
      StringBuffer displayName = new StringBuffer();
      displayName.append(requestNode.getAttributeValue("requestNumber"));
      if (requestNode.getAttributeValue("name") != null && !requestNode.getAttributeValue("name").equals("")) {
        displayName.append(" - ");
        displayName.append(requestNode.getAttributeValue("name"));                
      }      
      displayName.append(" - ");
      displayName.append(requestNode.getAttributeValue("slideProductName"));      
      displayName.append(" - ");
      displayName.append(requestNode.getAttributeValue("ownerFirstName"));
      displayName.append(" ");
      displayName.append(requestNode.getAttributeValue("ownerLastName"));
      displayName.append(" ");
      displayName.append(requestNode.getAttributeValue("requestCreateDateDisplayMedium"));      

      requestNode.setAttribute("displayName", displayName.toString());
      requestNode.setAttribute("label",       displayName.toString());

    } else {
      StringBuffer displayName = new StringBuffer();
      displayName.append(requestNode.getAttributeValue("requestNumber"));
      if (requestNode.getAttributeValue("name") != null && !requestNode.getAttributeValue("name").equals("")) {
        displayName.append(" - ");
        displayName.append(requestNode.getAttributeValue("name"));                
      }
      if (requestNode.getAttributeValue("codeApplication") != null && !requestNode.getAttributeValue("codeApplication").equals("")) {
        displayName.append(" - ");
        displayName.append(dictionaryHelper.getApplication(requestNode.getAttributeValue("codeApplication")));                
      }
      displayName.append(" - ");
      displayName.append(requestNode.getAttributeValue("ownerFirstName"));
      displayName.append(" ");
      displayName.append(requestNode.getAttributeValue("ownerLastName"));
      displayName.append(" ");
      displayName.append(requestNode.getAttributeValue("requestCreateDateDisplayMedium"));      

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