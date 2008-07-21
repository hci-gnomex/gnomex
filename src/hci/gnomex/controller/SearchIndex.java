package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.security.UnknownPermissionException;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.constants.Constants;
import hci.gnomex.lucene.AnalysisFilter;
import hci.gnomex.lucene.AnalysisIndexHelper;
import hci.gnomex.lucene.ExperimentIndexHelper;
import hci.gnomex.lucene.ExperimentFilter;
import hci.gnomex.lucene.ProtocolIndexHelper;
import hci.gnomex.lucene.ProtocolFilter;
import hci.gnomex.model.Visibility;

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
import javax.servlet.http.HttpSession;

import org.apache.log4j.Level;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.Searcher;
import org.jdom.Element;


public class SearchIndex extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SearchIndex.class);

  private boolean isExperimentOnlySearch = false;
  private boolean isAnalysisOnlySearch = false;
  
  private ExperimentFilter experimentFilter = null;
  private ProtocolFilter       protocolFilter = null;
  private AnalysisFilter       analysisFilter = null;
  
  private String listKind = "SearchList";
  
  private String searchDisplayText = "";
  
  private Map labMap        = null;
  private Map projectMap    = null;
  private Map categoryMap   = null;
  private Map requestMap    = null;
  private Map labToProjectMap = null;
  private Map projectToRequestCategoryMap = null;
  private Map categoryToRequestMap = null;
  
  private Map labAnalysisMap = null;
  private Map analysisGroupMap = null;
  private Map analysisMap = null;
  private Map labToAnalysisGroupMap = null;
  private Map analysisGroupToAnalysisMap = null;
  
  private ArrayList rankedRequestNodes = null;

  private ArrayList rankedProtocolNodes = null;
  
  private ArrayList rankedAnalysisNodes = null;
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    
    if (request.getParameter("listKind") != null && !request.getParameter("listKind").equals("")) {
      listKind = request.getParameter("listKind");
    }

    
    
    if (request.getParameter("isExperimentOnlySearch") != null && request.getParameter("isExperimentOnlySearch").equals("Y")) {
      isExperimentOnlySearch = true;
    }    
    if (request.getParameter("isAnalysisOnlySearch") != null && request.getParameter("isAnalysisOnlySearch").equals("Y")) {
      isAnalysisOnlySearch = true;
    }    
   
    
    
    
    experimentFilter = new ExperimentFilter();
    HashMap errors = this.loadDetailObject(request, experimentFilter);
    this.addInvalidFields(errors);
    
    protocolFilter = new ProtocolFilter();
    errors = this.loadDetailObject(request, protocolFilter);
    this.addInvalidFields(errors);
    
    analysisFilter = new AnalysisFilter();
    errors = this.loadDetailObject(request, analysisFilter);
    this.addInvalidFields(errors);    
    
    List experimentDesignCodes = new ArrayList();
    if (request.getParameter("experimentDesignConcatCodes") != null && !request.getParameter("experimentDesignConcatCodes").equals("")) {
      String[] codes = request.getParameter("experimentDesignConcatCodes").split(":");
      for (int i = 0; i < codes.length; i++) {
        String code = codes[i];
        experimentDesignCodes.add(code);
      }
      experimentFilter.setExperimentDesignCodes(experimentDesignCodes);
    }
    
    List experimentFactorCodes = new ArrayList();
    if (request.getParameter("experimentFactorConcatCodes") != null && !request.getParameter("experimentFactorConcatCodes").equals("")) {
      String[] codes = request.getParameter("experimentFactorConcatCodes").split(":");
      for (int i = 0; i < codes.length; i++) {
        String code = codes[i];
        experimentFactorCodes.add(code);
      }
      experimentFilter.setExperimentFactorCodes(experimentFactorCodes);
    }
    
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      

      
      if (!isAnalysisOnlySearch) {
        //
        // Experiments
        //
        
        //  Build a Query object
        IndexReader indexReader = IndexReader.open(Constants.LUCENE_EXPERIMENT_INDEX_DIRECTORY);      
        Searcher searcher = new IndexSearcher(indexReader);

        String searchText = experimentFilter.getSearchText().toString();
        searchDisplayText = experimentFilter.toString();
        
        // Build a Query to represent the security filter
        String securitySearchText = this.buildSecuritySearch();
        
        
        if (searchText != null && searchText.trim().length() > 0) {
          log.debug("Lucene search: " + searchText);
          Query query = new QueryParser("text", new StandardAnalyzer()).parse(searchText);
          
          // Build security filter        
          QueryWrapperFilter filter = this.buildSecurityQueryFilter(securitySearchText);

          // Search for the query
          Hits hits = null;
          if (filter == null) {
             hits = searcher.search(query);
          } else {
             hits = searcher.search(query, filter);
          }  
          
          processHits(hits, searchText);
          
        } else {
          if (securitySearchText != null) {
            Query query = new QueryParser("text", new StandardAnalyzer()).parse(securitySearchText);     
            Hits hits = searcher.search(query);
            processHits(hits, securitySearchText);
          } else {
            buildProjectRequestMap(indexReader);
          }
        }
        
      }
      
      
      if (!isExperimentOnlySearch && !isAnalysisOnlySearch) {
        //
        // Protocols
        //
        IndexReader protocolIndexReader = IndexReader.open(Constants.LUCENE_PROTOCOL_INDEX_DIRECTORY);      
        Searcher protocolSearcher = new IndexSearcher(protocolIndexReader);
        
        //  Build a Query object
        String protocolSearchText = protocolFilter.getSearchText().toString();
        
        if (protocolSearchText != null && protocolSearchText.trim().length() > 0) {
          log.debug("Lucene protocol search: " + protocolSearchText);
          Query query = new QueryParser("text", new StandardAnalyzer()).parse(protocolSearchText);
          
          // Search for the query
          Hits protocolHits = protocolSearcher.search(query);
          processProtocolHits(protocolHits, protocolSearchText);
          
        } else {
          buildProtocolMap(protocolIndexReader);
          
        }
        
      }
      
      
      if (!isExperimentOnlySearch) {
        //
        // Analysis
        //
        IndexReader analysisIndexReader = IndexReader.open(Constants.LUCENE_ANALYSIS_INDEX_DIRECTORY);      
        Searcher analysisSearcher = new IndexSearcher(analysisIndexReader);
        
        //  Build a Query object
        String analysisSearchText = analysisFilter.getSearchText().toString();
        
        if (isAnalysisOnlySearch) {
          searchDisplayText = analysisFilter.toString();          
        }
        
        
        // Build a Query to represent the security filter
        String analysisSecuritySearchText = this.buildAnalysisSecuritySearch();

        if (analysisSearchText != null && analysisSearchText.trim().length() > 0) {
          log.debug("Lucene analysis search: " + analysisSearchText);
          Query query = new QueryParser("text", new StandardAnalyzer()).parse(analysisSearchText);
          
          // Build security filter        
          QueryWrapperFilter filter = this.buildSecurityQueryFilter(analysisSecuritySearchText);

          // Search for the query
          Hits hits = null;
          if (filter == null) {
             hits = analysisSearcher.search(query);
          } else {
             hits = analysisSearcher.search(query, filter);
          }  
          
          processAnalysisHits(hits, analysisSearchText);
          
        } else {
          if (analysisSecuritySearchText != null) {
            Query query = new QueryParser("text", new StandardAnalyzer()).parse(analysisSecuritySearchText);     
            Hits hits = analysisSearcher.search(query);
            processAnalysisHits(hits, analysisSecuritySearchText);
          } else {
            this.buildAnalysisGroupMap(analysisIndexReader);
          }
        }
      }
      

      
      org.jdom.Document xmlDoc = this.buildXMLDocument();
      
      org.jdom.output.XMLOutputter out = new org.jdom.output.XMLOutputter();
      this.xmlResult = out.outputString(xmlDoc);

    }catch (UnknownPermissionException e){
      log.error("An exception has occurred in SearchIndex ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }catch (NamingException e){
      log.error("An exception has occurred in SearchIndex ", e);
      throw new RollBackCommandException(e.getMessage());
        
    }catch (SQLException e) {
      log.error("An exception has occurred in SearchIndex ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (XMLReflectException e){
      log.error("An exception has occurred in SearchIndex ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e){
      log.error("An exception has occurred in SearchIndex ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        this.getSecAdvisor().closeReadOnlyHibernateSession();        
      } catch(Exception e) {
        
      }
    }
    
    
    if (isValid()) {
      setResponsePage(this.SUCCESS_JSP);
    } else {
      setResponsePage(this.ERROR_JSP);
    }
    
    return this;
  }
  
  private void processHits(Hits hits, String searchText) throws Exception {
    
    // Show hits
    showExperimentHits(hits, searchText);
    
    // Map search results
    this.buildProjectRequestMap(hits);
    
  }
  
  private void processProtocolHits(Hits hits, String searchText) throws Exception {

    // Map search results
    this.buildProtocolMap(hits);
    
  }
  
  private void processAnalysisHits(Hits hits, String searchText) throws Exception {
    
    // Show hits
    showAnalysisHits(hits, searchText);
    
    // Map search results
    this.buildAnalysisGroupMap(hits);
    
  }
  private void buildProjectRequestMap(Hits hits) throws Exception{
    
    labMap        = new HashMap();
    projectMap    = new HashMap();
    categoryMap   = new HashMap();
    requestMap    = new HashMap();
    labToProjectMap = new TreeMap();
    projectToRequestCategoryMap = new TreeMap();
    categoryToRequestMap = new TreeMap();
    rankedRequestNodes = new ArrayList();
    
    for (int i = 0; i < hits.length(); i++) {
      org.apache.lucene.document.Document doc = hits.doc(i);
      float score = hits.score(i);
      mapDocument(doc, i, score);
      
    }
  }
  
  private void buildProjectRequestMap(IndexReader indexReader) throws Exception{
    
    labMap        = new HashMap();
    projectMap    = new HashMap();
    categoryMap   = new HashMap();
    requestMap    = new HashMap();
    labToProjectMap = new TreeMap();
    projectToRequestCategoryMap = new TreeMap();
    categoryToRequestMap = new TreeMap();
    rankedRequestNodes = new ArrayList();
    
    for (int i = 0; i < indexReader.numDocs(); i++) {
      org.apache.lucene.document.Document doc = indexReader.document(i);
      mapDocument(doc, -1, -1);      
    }
  }
  
  private void buildAnalysisGroupMap(Hits hits) throws Exception{
    
    labAnalysisMap = new HashMap();
    analysisGroupMap = new HashMap();
    analysisMap = new HashMap();
    labToAnalysisGroupMap = new TreeMap();
    analysisGroupToAnalysisMap = new TreeMap();
    rankedAnalysisNodes = new ArrayList();
    
    for (int i = 0; i < hits.length(); i++) {
      org.apache.lucene.document.Document doc = hits.doc(i);
      float score = hits.score(i);
      mapAnalysisDocument(doc, i, score);
      
    }
  }
  
  private void buildAnalysisGroupMap(IndexReader indexReader) throws Exception{
    
    labAnalysisMap = new HashMap();
    analysisGroupMap = new HashMap();
    analysisMap = new HashMap();
    labToAnalysisGroupMap = new TreeMap();
    analysisGroupToAnalysisMap = new TreeMap();
    rankedAnalysisNodes = new ArrayList();
    
    for (int i = 0; i < indexReader.numDocs(); i++) {
      org.apache.lucene.document.Document doc = indexReader.document(i);
      mapAnalysisDocument(doc, -1, -1);      
    }
  }
  
  
  private void buildProtocolMap(Hits hits) throws Exception{
    

    rankedProtocolNodes = new ArrayList();
    
    for (int i = 0; i < hits.length(); i++) {
      org.apache.lucene.document.Document doc = hits.doc(i);
      float score = hits.score(i);
      mapProtocolDocument(doc, i, score);
      
    }
  }
  
  private void buildProtocolMap(IndexReader indexReader) throws Exception{
    
    rankedProtocolNodes = new ArrayList();
        
    for (int i = 0; i < indexReader.numDocs(); i++) {
      org.apache.lucene.document.Document doc = indexReader.document(i);
      mapProtocolDocument(doc, -1, -1);      
    }
  }
  
  private void mapProtocolDocument(org.apache.lucene.document.Document doc, int rank, float score) {
    
    Integer idProtocol         = new Integer(doc.get(ProtocolIndexHelper.ID_PROTOCOL));
    String protocolType        = doc.get(ProtocolIndexHelper.PROTOCOL_TYPE);
    String protocolName        = doc.get(ProtocolIndexHelper.NAME) != null ? doc.get(ProtocolIndexHelper.NAME) : "";
    String protocolDescription = doc.get(ProtocolIndexHelper.DESCRIPTION) != null ? doc.get(ProtocolIndexHelper.DESCRIPTION) : "";
    String protocolClassName   = doc.get(ProtocolIndexHelper.CLASS_NAME) != null ? doc.get(ProtocolIndexHelper.CLASS_NAME) : "";
    
    Element node = new Element("Protocol");
    node.setAttribute("idProtocol",    idProtocol.toString());
    node.setAttribute("protocolType", protocolType);
    node.setAttribute("name",         protocolName);
    node.setAttribute("label",        protocolName);
    node.setAttribute("description",  protocolDescription);
    node.setAttribute("className",    protocolClassName);
    if (rank >= 0) {
      node.setAttribute("searchRank", new Integer(rank + 1).toString());
      node.setAttribute("searchInfo", " (Search rank #" + (rank + 1) + ")");
    }
    if (score >= 0) {
      node.setAttribute("searchScore", new Integer(Math.round(score * 100)).toString() + "%");                    
    }
    
    rankedProtocolNodes.add(node);
  }
  
  private void mapDocument(org.apache.lucene.document.Document doc, int rank, float score) {
    
    Integer idProject = new Integer(doc.get(ExperimentIndexHelper.ID_PROJECT));
    Integer idLab     = new Integer(doc.get(ExperimentIndexHelper.ID_LAB_PROJECT));
    Integer idRequest          = doc.get(ExperimentIndexHelper.ID_REQUEST).equals("unknown") ? null : new Integer(doc.get(ExperimentIndexHelper.ID_REQUEST));
    String codeRequestCategory = doc.get(ExperimentIndexHelper.CODE_REQUEST_CATEGORY) != null ? doc.get(ExperimentIndexHelper.CODE_REQUEST_CATEGORY) : null;
    String codeMicroarrayCategory = doc.get(ExperimentIndexHelper.CODE_MICROARRAY_CATEGORY) != null ? doc.get(ExperimentIndexHelper.CODE_MICROARRAY_CATEGORY) : null;
    String catKey = idProject + "-" + codeRequestCategory + "-" + codeMicroarrayCategory;      
    
    Element labNode = (Element)labMap.get(idLab);
    if (labNode == null) {
      Element node = new Element("Lab");
      node.setAttribute("idLab", idLab.toString());
      node.setAttribute("labName", doc.get(ExperimentIndexHelper.PROJECT_LAB_NAME));
      node.setAttribute("label", doc.get(ExperimentIndexHelper.PROJECT_LAB_NAME));
      node.setAttribute("projectLabName", doc.get(ExperimentIndexHelper.PROJECT_LAB_NAME));
      labMap.put(idLab, node);
    }

    Element projectNode = (Element)projectMap.get(idProject);
    if (projectNode == null) {
      Element node = new Element("Project");
      node.setAttribute("idProject", idProject.toString());
      node.setAttribute("projectName", doc.get(ExperimentIndexHelper.PROJECT_NAME) != null ? doc.get(ExperimentIndexHelper.PROJECT_NAME) : "");
      node.setAttribute("label", doc.get(ExperimentIndexHelper.PROJECT_NAME) != null ? doc.get(ExperimentIndexHelper.PROJECT_NAME) : "");
      node.setAttribute("projectDescription", doc.get(ExperimentIndexHelper.PROJECT_DESCRIPTION) != null ? doc.get(ExperimentIndexHelper.PROJECT_DESCRIPTION) : "");
      node.setAttribute("codeVisibility", doc.get(ExperimentIndexHelper.PROJECT_CODE_VISIBILITY));
      node.setAttribute("projectPublicNote", doc.get(ExperimentIndexHelper.PROJECT_PUBLIC_NOTE) != null ? doc.get(ExperimentIndexHelper.PROJECT_PUBLIC_NOTE) : "");
      if (idRequest == null || idRequest.intValue() == 0) {
        if (rank >= 0) {
          node.setAttribute("searchRank", new Integer(rank + 1).toString());
          node.setAttribute("searchInfo", " (Search rank #" + (rank + 1) + ")");
        }
        if (score >= 0) {
          node.setAttribute("searchScore", new Integer(Math.round(score * 100)).toString() + "%");                    
        }
      }
      projectMap.put(idProject, node);
    }
    
    if (idRequest != null) {
      
      Element categoryNode = (Element)categoryMap.get(catKey);
      if (categoryNode == null) {
        Element node = new Element("RequestCategory");
        node.setAttribute("codeRequestCategory", codeRequestCategory != null ? codeRequestCategory : "");
        node.setAttribute("codeMicroarrayCategory", codeMicroarrayCategory != null ? codeMicroarrayCategory : "");
        node.setAttribute("label", codeRequestCategory + " " + codeMicroarrayCategory);
        node.setAttribute("idProject",idProject.toString());     
        categoryMap.put(catKey, node);
      }

      
      Element requestNode = (Element)requestMap.get(idRequest);
      if (requestNode == null) {
        Element node = new Element("RequestNode");
        Element node1 = new Element("Request");
        buildRequestNode(node, idRequest, codeRequestCategory, codeMicroarrayCategory, doc, score, rank);
        buildRequestNode(node1, idRequest, codeRequestCategory, codeMicroarrayCategory, doc, score, rank);
        
        requestMap.put(idRequest, node);
        rankedRequestNodes.add(node1);
      }        
    } else {
      Element node = new Element("Request");
      buildEmptyRequestNode(node, idProject, doc, score, rank);
      rankedRequestNodes.add(node);
    }
    
    
    String labName     = doc.get(ExperimentIndexHelper.PROJECT_LAB_NAME);
    String projectName = doc.get(ExperimentIndexHelper.PROJECT_NAME);
    String requestCreateDate = doc.get(ExperimentIndexHelper.CREATE_DATE);
    String labKey = labName + "---" + idLab;
    Map projectIdMap = (Map)labToProjectMap.get(labKey);
    if (projectIdMap == null) {
      projectIdMap = new TreeMap();
      labToProjectMap.put(labKey, projectIdMap);
    }
    String projectKey = projectName + "---" + idProject;
    projectIdMap.put(projectKey, idProject);        
    
    if (idRequest != null) {
      Map codeMap = (Map)projectToRequestCategoryMap.get(idProject);
      if (codeMap == null) {
        codeMap = new TreeMap();
        projectToRequestCategoryMap.put(idProject, codeMap);
      }        
      codeMap.put(catKey, null);

      Map idRequestMap = (Map)categoryToRequestMap.get(catKey);
      if (idRequestMap == null) {
        idRequestMap = new TreeMap();
        categoryToRequestMap.put(catKey, idRequestMap);
      }
      idRequestMap.put(requestCreateDate + "-" + idRequest, idRequest);        
    }

  }
  
 private void mapAnalysisDocument(org.apache.lucene.document.Document doc, int rank, float score) {
    
    Integer idAnalysisGroup = new Integer(doc.get(AnalysisIndexHelper.ID_ANALYSISGROUP));
    Integer idLab     = new Integer(doc.get(AnalysisIndexHelper.ID_LAB_ANALYSISGROUP));
    Integer idAnalysis          = doc.get(AnalysisIndexHelper.ID_ANALYSIS) == null ? null : new Integer(doc.get(AnalysisIndexHelper.ID_ANALYSIS));
    
    Element labNode = (Element)labAnalysisMap.get(idLab);
    if (labNode == null) {
      Element node = new Element("Lab");
      node.setAttribute("idLab", idLab.toString());
      node.setAttribute("labName", doc.get(AnalysisIndexHelper.LAB_NAME_ANALYSISGROUP));
      node.setAttribute("label", doc.get(AnalysisIndexHelper.LAB_NAME_ANALYSISGROUP));
      labAnalysisMap.put(idLab, node);
    }

    Element analysisGroupNode = (Element)analysisGroupMap.get(idAnalysisGroup);
    if (analysisGroupNode == null) {
      Element node = new Element("AnalysisGroup");
      node.setAttribute("idAnalysisGroup", idAnalysisGroup.toString());
      node.setAttribute("name", doc.get(AnalysisIndexHelper.ANALYSIS_GROUP_NAME) != null ? doc.get(AnalysisIndexHelper.ANALYSIS_GROUP_NAME) : "");
      node.setAttribute("label", doc.get(AnalysisIndexHelper.ANALYSIS_GROUP_NAME) != null ? doc.get(AnalysisIndexHelper.ANALYSIS_GROUP_NAME) : "");
      node.setAttribute("description", doc.get(AnalysisIndexHelper.ANALYSIS_GROUP_DESCRIPTION) != null ? doc.get(AnalysisIndexHelper.ANALYSIS_GROUP_DESCRIPTION) : "");
      node.setAttribute("codeVisibility", doc.get(AnalysisIndexHelper.CODE_VISIBILITY) != null ? doc.get(AnalysisIndexHelper.CODE_VISIBILITY) : "");
      if (idAnalysis == null || idAnalysis.intValue() == 0) {
        if (rank >= 0) {
          node.setAttribute("searchRank", new Integer(rank + 1).toString());
          node.setAttribute("searchInfo", " (Search rank #" + (rank + 1) + ")");
        }
        if (score >= 0) {
          node.setAttribute("searchScore", new Integer(Math.round(score * 100)).toString() + "%");                    
        }
      }
      analysisGroupMap.put(idAnalysisGroup, node);
    }
    
    if (idAnalysis != null) {
      
      
      Element analysisNode = (Element)analysisMap.get(idAnalysis);
      if (analysisNode == null) {
        Element node = new Element("AnalysisNode");
        Element node1 = new Element("Analysis");
        buildAnalysisNode(node, idAnalysis, doc, score, rank);
        buildAnalysisNode(node1, idAnalysis, doc, score, rank);
        
        analysisMap.put(idAnalysisGroup + "-" + idAnalysis, node);
        rankedAnalysisNodes.add(node1);
      }        
    } else {
      Element node = new Element("Analysis");
      buildEmptyAnalysisNode(node, idAnalysisGroup, doc, score, rank);
      rankedAnalysisNodes.add(node);
    }
    
    
    String labName     = doc.get(AnalysisIndexHelper.LAB_NAME_ANALYSISGROUP);
    String analysisGroupName = doc.get(AnalysisIndexHelper.ANALYSIS_GROUP_NAME);
    String analysisCreateDate = doc.get(AnalysisIndexHelper.CREATE_DATE);
    String labKey = labName + "---" + idLab;
    Map analysisGroupIdMap = (Map)labToAnalysisGroupMap.get(labKey);
    if (analysisGroupIdMap == null) {
      analysisGroupIdMap = new TreeMap();
      labToAnalysisGroupMap.put(labKey, analysisGroupIdMap);
    }
    String analysisGroupKey = analysisGroupName + "---" + idAnalysisGroup;
    analysisGroupIdMap.put(analysisGroupKey, idAnalysisGroup);        
    
    if (idAnalysis != null) {


      Map idAnalysisMap = (Map)analysisGroupToAnalysisMap.get(idAnalysisGroup);
      if (idAnalysisMap == null) {
        idAnalysisMap = new TreeMap();
        analysisGroupToAnalysisMap.put(idAnalysisGroup, idAnalysisMap);
      }
      idAnalysisMap.put(idAnalysisGroup + "-" + analysisCreateDate + "-" + idAnalysis, idAnalysis);        
    }

  }
  
  private void buildRequestNode(Element node, Integer idRequest, String codeRequestCategory, String codeMicroarrayCategory, Document doc, float score, int rank) {
    node.setAttribute("idRequest", idRequest.toString());
    node.setAttribute("requestNumber", doc.get(ExperimentIndexHelper.REQUEST_NUMBER));
    node.setAttribute("requestCreateDate", doc.get(ExperimentIndexHelper.CREATE_DATE));
    node.setAttribute("codeVisibility",  doc.get(ExperimentIndexHelper.CODE_VISIBILITY) != null ? doc.get(ExperimentIndexHelper.CODE_VISIBILITY) : "");
    node.setAttribute("public",  doc.get(ExperimentIndexHelper.CODE_VISIBILITY) != null && doc.get(ExperimentIndexHelper.CODE_VISIBILITY).equals(Visibility.VISIBLE_TO_PUBLIC) ? "Public" : "");
    node.setAttribute("requestPublicNote", doc.get(ExperimentIndexHelper.PUBLIC_NOTE) != null ? doc.get(ExperimentIndexHelper.PUBLIC_NOTE) : "");
    node.setAttribute("displayName", doc.get(ExperimentIndexHelper.DISPLAY_NAME));
    node.setAttribute("label", doc.get(ExperimentIndexHelper.DISPLAY_NAME));
    node.setAttribute("ownerFirstName", doc.get(ExperimentIndexHelper.OWNER_FIRST_NAME) != null ? doc.get(ExperimentIndexHelper.OWNER_FIRST_NAME) : "");
    node.setAttribute("ownerLastName", doc.get(ExperimentIndexHelper.OWNER_LAST_NAME) != null ? doc.get(ExperimentIndexHelper.OWNER_LAST_NAME) : "");
    node.setAttribute("slideProductName", doc.get(ExperimentIndexHelper.SLIDE_PRODUCT) != null ? doc.get(ExperimentIndexHelper.SLIDE_PRODUCT) : "");
    node.setAttribute("codeRequestCategory", codeRequestCategory != null ? codeRequestCategory : "");
    node.setAttribute("codeMicroarrayCategory", codeMicroarrayCategory != null ? codeMicroarrayCategory : "");
    node.setAttribute("requestLabName", doc.get(ExperimentIndexHelper.LAB_NAME));
    node.setAttribute("projectName", doc.get(ExperimentIndexHelper.PROJECT_NAME));
    node.setAttribute("idSlideProduct", doc.get(ExperimentIndexHelper.ID_SLIDE_PRODUCT) != null ? doc.get(ExperimentIndexHelper.ID_SLIDE_PRODUCT) : "");
    if (rank >= 0) {
      node.setAttribute("searchRank", new Integer(rank + 1).toString());          
      node.setAttribute("searchInfo", " (Search rank #" + (rank + 1) + ")");
    } 
    if (score >= 0) {
      node.setAttribute("searchScore", new Integer(Math.round(score * 100)).toString() + "%");          
    }

  }
  
  private void buildEmptyRequestNode(Element node,  Integer idProject, Document doc, float score, int rank) {
    node.setAttribute("idRequest", "-1");
    node.setAttribute("idProject", idProject.toString());
    node.setAttribute("projectName",  doc.get(ExperimentIndexHelper.PROJECT_NAME) != null ? doc.get(ExperimentIndexHelper.PROJECT_NAME) : "");
    node.setAttribute("label",  doc.get(ExperimentIndexHelper.PROJECT_NAME) != null ? doc.get(ExperimentIndexHelper.PROJECT_NAME) : "");
    if (rank >= 0) {
      node.setAttribute("searchRank", new Integer(rank + 1).toString());          
      node.setAttribute("searchInfo", " (Search rank #" + (rank + 1) + ")");
    } 
    if (score >= 0) {
      node.setAttribute("searchScore", new Integer(Math.round(score * 100)).toString() + "%");          
    }

  }
  
  
  private void buildAnalysisNode(Element node, Integer idAnalysis, Document doc, float score, int rank) {
    node.setAttribute("idAnalysis", idAnalysis.toString());
    node.setAttribute("analysisGroupName", doc.get(AnalysisIndexHelper.ANALYSIS_GROUP_NAME));
    node.setAttribute("number", doc.get(AnalysisIndexHelper.ANALYSIS_NUMBER));
    node.setAttribute("label", doc.get(AnalysisIndexHelper.ANALYSIS_NUMBER) + " (" + doc.get(AnalysisIndexHelper.ANALYSIS_NAME) + ")");
    node.setAttribute("createDate", doc.get(AnalysisIndexHelper.CREATE_DATE));
    node.setAttribute("codeVisibility",  doc.get(AnalysisIndexHelper.CODE_VISIBILITY) != null ? doc.get(AnalysisIndexHelper.CODE_VISIBILITY) : "");
    node.setAttribute("public",  doc.get(AnalysisIndexHelper.CODE_VISIBILITY) != null && doc.get(AnalysisIndexHelper.CODE_VISIBILITY).equals(Visibility.VISIBLE_TO_PUBLIC) ? "Public" : "");
    node.setAttribute("publicNote", doc.get(AnalysisIndexHelper.PUBLIC_NOTE) != null ? doc.get(AnalysisIndexHelper.PUBLIC_NOTE) : "");
    node.setAttribute("name", doc.get(AnalysisIndexHelper.ANALYSIS_NAME) != null ?  doc.get(AnalysisIndexHelper.ANALYSIS_NAME) : "");
    node.setAttribute("ownerFirstName", doc.get(AnalysisIndexHelper.OWNER_FIRST_NAME) != null ? doc.get(AnalysisIndexHelper.OWNER_FIRST_NAME) : "");
    node.setAttribute("ownerLastName", doc.get(AnalysisIndexHelper.OWNER_LAST_NAME) != null ? doc.get(AnalysisIndexHelper.OWNER_LAST_NAME) : "");
    node.setAttribute("labName", doc.get(AnalysisIndexHelper.LAB_NAME) != null ? doc.get(AnalysisIndexHelper.LAB_NAME) : "" );
    node.setAttribute("idAnalysisType", doc.get(AnalysisIndexHelper.ID_ANALYSIS_TYPE) != null ? doc.get(AnalysisIndexHelper.ID_ANALYSIS_TYPE) : "");
    node.setAttribute("idOrganism", doc.get(AnalysisIndexHelper.ID_ANALYSIS_PROTOCOL) != null ? doc.get(AnalysisIndexHelper.ID_ANALYSIS_PROTOCOL) : "");
    node.setAttribute("idAnalysisProtocol", doc.get(AnalysisIndexHelper.ID_ORGANISM) != null ? doc.get(AnalysisIndexHelper.ID_ORGANISM) : "");
    node.setAttribute("organism", doc.get(AnalysisIndexHelper.ORGANISM) != null ? doc.get(AnalysisIndexHelper.ORGANISM) : "");
    node.setAttribute("analysisType", doc.get(AnalysisIndexHelper.ANALYSIS_TYPE) != null ? doc.get(AnalysisIndexHelper.ANALYSIS_TYPE) : "");
    node.setAttribute("analysisProtocol", doc.get(AnalysisIndexHelper.ANALYSIS_PROTOCOL) != null ? doc.get(AnalysisIndexHelper.ANALYSIS_PROTOCOL) : "");
    if (rank >= 0) {
      node.setAttribute("searchRank", new Integer(rank + 1).toString());          
      node.setAttribute("searchInfo", " (Search rank #" + (rank + 1) + ")");
    } 
    if (score >= 0) {
      node.setAttribute("searchScore", new Integer(Math.round(score * 100)).toString() + "%");          
    }

  }

  private void buildEmptyAnalysisNode(Element node,  Integer idAnalysisGroup, Document doc, float score, int rank) {
    node.setAttribute("idAnalysis", "-1");
    node.setAttribute("idAnalysisGroup", idAnalysisGroup.toString());
    node.setAttribute("analysisGroupName", doc.get(AnalysisIndexHelper.ANALYSIS_GROUP_NAME));
    node.setAttribute("name",  doc.get(AnalysisIndexHelper.ANALYSIS_GROUP_NAME) != null ? doc.get(AnalysisIndexHelper.ANALYSIS_GROUP_NAME) : "");
    node.setAttribute("label",  doc.get(AnalysisIndexHelper.ANALYSIS_GROUP_NAME) != null ? doc.get(AnalysisIndexHelper.ANALYSIS_GROUP_NAME) : "");
    if (rank >= 0) {
      node.setAttribute("searchRank", new Integer(rank + 1).toString());          
      node.setAttribute("searchInfo", " (Search rank #" + (rank + 1) + ")");
    } 
    if (score >= 0) {
      node.setAttribute("searchScore", new Integer(Math.round(score * 100)).toString() + "%");          
    }

  }
  
  private org.jdom.Document buildXMLDocument() {
    org.jdom.Document doc = new org.jdom.Document(new Element(listKind));
    doc.getRootElement().setAttribute("search", searchDisplayText);
    
    if (!isAnalysisOnlySearch) {
      buildProjectRequestList(doc.getRootElement());
      buildRequestList(doc.getRootElement());      
    }
    if (!isExperimentOnlySearch && !isAnalysisOnlySearch) {
      buildProtocolList(doc.getRootElement());      
    }
    if (!isExperimentOnlySearch) {
      buildAnalysisGroupList(doc.getRootElement());
      buildAnalysisList(doc.getRootElement());      
    }
    
    return doc;
    
  }
  
  private void buildProjectRequestList(Element root) {
    Element parent = new Element("ProjectRequestList");
    root.addContent(parent);
    
    // For each lab
    for(Iterator i = labToProjectMap.keySet().iterator(); i.hasNext();) {
      String key = (String)i.next();
      
      String []tokens = key.split("---");
      Integer idLab = new Integer(tokens[1]);
      
      Element labNode = (Element)labMap.get(idLab);
      Map projectIdMap = (Map)labToProjectMap.get(key);
      
      
      parent.addContent(labNode);
      
      // For each project in lab
      for(Iterator i1 = projectIdMap.keySet().iterator(); i1.hasNext();) {
        String projectKey = (String)i1.next();

        
        Integer idProject = (Integer)projectIdMap.get(projectKey);

        Element projectNode = (Element)projectMap.get(idProject);
        Map codeMap = (Map)projectToRequestCategoryMap.get(idProject);
        
        labNode.addContent(projectNode);
        
        
        // For each request category in project
        if (codeMap != null) {
          for(Iterator i2 = codeMap.keySet().iterator(); i2.hasNext();) {
            String code = (String)i2.next();
            Element categoryNode = (Element)categoryMap.get(code);
            Map idRequestMap = (Map)categoryToRequestMap.get(code);
            
            if (categoryNode != null) {
              
              if (this.experimentFilter.getShowCategory().equals("Y")) {
                projectNode.addContent(categoryNode);                
              }
              
              // For each request in request category
              for(Iterator i3 = idRequestMap.keySet().iterator(); i3.hasNext();) {
                String requestKey = (String)i3.next();
                Integer idRequest = (Integer)idRequestMap.get(requestKey);
                Element requestNode = (Element)requestMap.get(idRequest);
                
                if (this.experimentFilter.getShowCategory().equals("Y")) {
                  categoryNode.addContent(requestNode);                  
                } else {
                  projectNode.addContent(requestNode);
                }
                
              }              
            }
            
          }
          
        }
          
        }
      
    }

    
  }

  private void buildAnalysisGroupList(Element root) {
    Element parent = new Element("AnalysisGroupList");
    root.addContent(parent);
    
    // For each lab
    for(Iterator i = labToAnalysisGroupMap.keySet().iterator(); i.hasNext();) {
      String key = (String)i.next();
      
      String []tokens = key.split("---");
      Integer idLab = new Integer(tokens[1]);
      
      Element labNode = (Element)labAnalysisMap.get(idLab);
      Map analysisGroupIdMap = (Map)labToAnalysisGroupMap.get(key);
      
      parent.addContent(labNode);
      
      // For each analysis group in lab
      for(Iterator i1 = analysisGroupIdMap.keySet().iterator(); i1.hasNext();) {
        String agKey = (String)i1.next();

        
        Integer idAnalysisGroup = (Integer)analysisGroupIdMap.get(agKey);

        Element agNode = (Element)analysisGroupMap.get(idAnalysisGroup);
        Map idAnalysisMap = (Map)analysisGroupToAnalysisMap.get(idAnalysisGroup);
        
        labNode.addContent(agNode);
        
        
        // For each analysis in analysis group
        if (idAnalysisMap != null) {
          for(Iterator i2 = idAnalysisMap.keySet().iterator(); i2.hasNext();) {
            
            String akey = (String)i2.next();
            Integer idAnalysis = (Integer)idAnalysisMap.get(akey);
            Element aNode = (Element)analysisMap.get(idAnalysisGroup + "-" + idAnalysis);
            agNode.addContent(aNode);
          }
        }
      }
    }

    
  }

  private void buildRequestList(Element root) {
    Element parent = new Element("RequestList");
    root.addContent(parent);
    
    // For each request node
    for(Iterator i = rankedRequestNodes.iterator(); i.hasNext();) {
      Element requestNode = (Element)i.next();
      parent.addContent(requestNode);
    }
    
  }
  
  
  private void buildProtocolList(Element root) {
    Element parent = new Element("ProtocolList");
    root.addContent(parent);
    
    // For each request node
    for(Iterator i = rankedProtocolNodes.iterator(); i.hasNext();) {
      Element protocolNode = (Element)i.next();
      parent.addContent(protocolNode);
    }
  }
  
  private void buildAnalysisList(Element root) {
    Element parent = new Element("AnalysisList");
    root.addContent(parent);
    
    // For each request node
    for(Iterator i = rankedAnalysisNodes.iterator(); i.hasNext();) {
      Element node = (Element)i.next();
      parent.addContent(node);
    }
    
  }
  
  private String buildSecuritySearch() throws Exception {
    boolean scopeToGroup = true;
    if (experimentFilter.getSearchPublicProjects() != null && experimentFilter.getSearchPublicProjects().equalsIgnoreCase("Y")) {
      scopeToGroup = false;
    }
    
   
    StringBuffer searchText = new StringBuffer();
    boolean addedFilter1 = false;
    boolean addedFilter2 = false;
    StringBuffer searchText1 = new StringBuffer();
    StringBuffer searchText2 = new StringBuffer();
    addedFilter1 = this.getSecAdvisor().buildLuceneSecurityFilter(searchText1, 
                                                                   ExperimentIndexHelper.ID_LAB_PROJECT, 
                                                                   ExperimentIndexHelper.PROJECT_CODE_VISIBILITY, 
                                                                   scopeToGroup,
                                                                   null);
    if (addedFilter1) {
      searchText2 = new StringBuffer();
      addedFilter2 = this.getSecAdvisor().buildLuceneSecurityFilter(searchText2, 
                                                                    ExperimentIndexHelper.ID_LAB,
                                                                    ExperimentIndexHelper.CODE_VISIBILITY,
                                                                    scopeToGroup,
                                                                    ExperimentIndexHelper.ID_REQUEST + ":unknown" );
    }
    if (addedFilter1) {
      searchText.append("(");
      searchText.append(searchText1);
      searchText.append(")");
    }
    if (addedFilter1 && addedFilter2) {
      searchText.append(" AND ");
    }
    searchText.append("(");
    searchText.append(searchText2);
    searchText.append(")");
    
    if (addedFilter1 || addedFilter2) {
      
      log.debug("Security filter: " + searchText.toString());
      return searchText.toString();           
    } else {
      return null;
    }
  }
  
  private String buildAnalysisSecuritySearch() throws Exception {
    boolean scopeToGroup = true;
    if (experimentFilter.getSearchPublicProjects() != null && experimentFilter.getSearchPublicProjects().equalsIgnoreCase("Y")) {
      scopeToGroup = false;
    }
    
   
    StringBuffer searchText = new StringBuffer();
    boolean addedFilter1 = false;
    boolean addedFilter2 = false;
    StringBuffer searchText1 = new StringBuffer();
    StringBuffer searchText2 = new StringBuffer();
    addedFilter1 = this.getSecAdvisor().buildLuceneSecurityFilter(searchText1, 
                                                                   AnalysisIndexHelper.ID_LAB_ANALYSISGROUP, 
                                                                   AnalysisIndexHelper.ANALYSIS_GROUP_CODE_VISIBILITY, 
                                                                   scopeToGroup,
                                                                   null);
    if (addedFilter1) {
      searchText2 = new StringBuffer();
      addedFilter2 = this.getSecAdvisor().buildLuceneSecurityFilter(searchText2, 
                                                                    AnalysisIndexHelper.ID_LAB,
                                                                    AnalysisIndexHelper.CODE_VISIBILITY,
                                                                    scopeToGroup,
                                                                    AnalysisIndexHelper.ID_ANALYSIS + ":unknown" );
    }
    if (addedFilter1) {
      searchText.append("(");
      searchText.append(searchText1);
      searchText.append(")");
    }
    if (addedFilter1 && addedFilter2) {
      searchText.append(" AND ");
    }
    searchText.append("(");
    searchText.append(searchText2);
    searchText.append(")");
    
    if (addedFilter1 || addedFilter2) {
      
      log.debug("Security filter: " + searchText.toString());
      return searchText.toString();           
    } else {
      return null;
    }
  }
  
  private QueryWrapperFilter buildSecurityQueryFilter(String searchText) throws Exception {
    
    QueryWrapperFilter filter = null;
    if (searchText != null) {
      log.debug("Security filter: " + searchText.toString());
      Query securityQuery = new QueryParser("text", new StandardAnalyzer()).parse(searchText.toString());
      filter = new QueryWrapperFilter(securityQuery);            
    }
    return filter;
  }


  
  private void showExperimentHits(Hits hits, String searchText) throws Exception {

    if (log.getEffectiveLevel().equals(Level.DEBUG)) {
      // Examine the Hits object to see if there were any matches
      int hitCount = hits.length();
      if (hitCount == 0) {
          System.out.println(
              "No matches were found for \"" + searchText + "\"");
      }
      else {
          System.out.println("Hits for \"" + searchText + "\""  + ":");

          // Iterate over the Documents in the Hits object
          for (int i = 0; i < hitCount; i++) {
              org.apache.lucene.document.Document doc = hits.doc(i);
              float score = hits.score(i);

              // Print the value that we stored in the "title" field. Note
              // that this Field was not indexed, but (unlike the
              // "contents" field) was stored verbatim and can be
              // retrieved.
              System.out.println("  " + (i + 1) + ". " + " score=" + score + " " + doc.get("projectName") + " " + doc.get("requestNumber"));
          }
      }
      System.out.println();      
    }

  }
  
  
  private void showAnalysisHits(Hits hits, String searchText) throws Exception {

    if (log.getEffectiveLevel().equals(Level.DEBUG)) {
      // Examine the Hits object to see if there were any matches
      int hitCount = hits.length();
      if (hitCount == 0) {
          System.out.println(
              "No matches were found for \"" + searchText + "\"");
      }
      else {
          System.out.println("Hits for \"" + searchText + "\""  + ":");

          // Iterate over the Documents in the Hits object
          for (int i = 0; i < hitCount; i++) {
              org.apache.lucene.document.Document doc = hits.doc(i);
              float score = hits.score(i);

              // Print the value that we stored in the "title" field. Note
              // that this Field was not indexed, but (unlike the
              // "contents" field) was stored verbatim and can be
              // retrieved.
              System.out.println("  " + (i + 1) + ". " + " score=" + score + " " + doc.get(AnalysisIndexHelper.ANALYSIS_GROUP_NAME) + " " + doc.get(AnalysisIndexHelper.ANALYSIS_NAME));
          }
      }
      System.out.println();      
    }

  }

}