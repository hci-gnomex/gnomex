package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.security.UnknownPermissionException;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.constants.Constants;
import hci.gnomex.lucene.AnalysisFilter;
import hci.gnomex.lucene.AnalysisIndexHelper;
import hci.gnomex.lucene.DataTrackFilter;
import hci.gnomex.lucene.DataTrackIndexHelper;
import hci.gnomex.lucene.ExperimentFilter;
import hci.gnomex.lucene.ExperimentIndexHelper;
import hci.gnomex.lucene.ProtocolFilter;
import hci.gnomex.lucene.ProtocolIndexHelper;
import hci.gnomex.lucene.SearchListParser;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.model.Visibility;
import hci.gnomex.utility.DataTrackQuery;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.PropertyDictionaryHelper;
import hci.gnomex.utility.RequestParser;


import java.io.Serializable;
import java.io.StringReader;
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
import org.hibernate.Session;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;


public class SearchIndex extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SearchIndex.class);

  private boolean isExperimentOnlySearch = false;
  private boolean isAnalysisOnlySearch = false;
  private boolean isProtocolOnlySearch = false;
  private boolean isDataTrackOnlySearch = false;
  
  private ExperimentFilter experimentFilter = null;
  private ProtocolFilter   protocolFilter = null;
  private AnalysisFilter   analysisFilter = null;
  private DataTrackFilter  dataTrackFilter = null;
  
  private String listKind = "SearchList";
  
  private String serverName = "";

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
  
  private Map labDataTrackMap = null;
  private Map dataTrackFolderMap = null;
  private Map dataTrackMap = null;
  private Map labToDataTrackFolderMap = null;
  private Map dataTrackFolderToDataTrackMap = null;
  private List<Integer> dataTrackIds = null;
  
  private ArrayList rankedRequestNodes = null;

  private ArrayList rankedProtocolNodes = null;
  
  private ArrayList rankedAnalysisNodes = null;
  
  private ArrayList rankedDataTrackNodes = null;
  
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
    if (request.getParameter("isProtocolOnlySearch") != null && request.getParameter("isProtocolOnlySearch").equals("Y")) {
      isProtocolOnlySearch = true;
    }    
    if (request.getParameter("isDataTrackOnlySearch") != null && request.getParameter("isDataTrackOnlySearch").equals("Y")) {
      isDataTrackOnlySearch = true;
    }    
   
    serverName = request.getServerName();

    
    experimentFilter = new ExperimentFilter();
    HashMap errors = this.loadDetailObject(request, experimentFilter);
    this.addInvalidFields(errors);
    
    protocolFilter = new ProtocolFilter();
    errors = this.loadDetailObject(request, protocolFilter);
    this.addInvalidFields(errors);
    
    analysisFilter = new AnalysisFilter();
    errors = this.loadDetailObject(request, analysisFilter);
    this.addInvalidFields(errors);    
    
    dataTrackFilter = new DataTrackFilter();
    errors = this.loadDetailObject(request, dataTrackFilter);
    this.addInvalidFields(errors);    
    
    if (request.getParameter("searchList") != null && !request.getParameter("searchList").equals("")) {
      String searchXMLString = request.getParameter("searchList");
      searchXMLString = searchXMLString.replaceAll("&", "&amp;");
      searchXMLString = "<SearchList>" + searchXMLString + "</SearchList>";
      StringReader reader = new StringReader(searchXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        org.jdom.Document searchDoc = sax.build(reader);
        SearchListParser searchListParser = new SearchListParser(searchDoc, experimentFilter.getMatchAnyTerm());
        searchListParser.Parse();
        experimentFilter.setSearchListText(searchListParser.getSearchText());
        analysisFilter.setSearchListText(searchListParser.getSearchText());
        protocolFilter.setSearchListText(searchListParser.getSearchText());
        dataTrackFilter.setSearchListText(searchListParser.getSearchText());
        experimentFilter.setIdLab(searchListParser.getIdLab());
        experimentFilter.setIdOrganism(searchListParser.getIdOrganism());
        analysisFilter.setIdLab(searchListParser.getIdLab());
        analysisFilter.setIdOrganism(searchListParser.getIdOrganism());
        dataTrackFilter.setIdLab(searchListParser.getIdLab());
        dataTrackFilter.setIdOrganism(searchListParser.getIdOrganism());
      } catch (Exception je ) {
        log.error( "Cannot parse searchXMLString", je );
        this.addInvalidField( "searchXMLString", "Invalid search xml");
      }
    }
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      
      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
      DictionaryHelper dh = DictionaryHelper.getInstance(sess);
      PropertyDictionaryHelper ph = PropertyDictionaryHelper.getInstance(sess);

      
      if (!isAnalysisOnlySearch && !isProtocolOnlySearch && !isDataTrackOnlySearch) {
        //
        // Experiments
        //
        
        //  Build a Query object
        IndexReader indexReader = IndexReader.open(ph.getQualifiedProperty(PropertyDictionary.LUCENE_EXPERIMENT_INDEX_DIRECTORY, serverName));      
        Searcher searcher = new IndexSearcher(indexReader);

        String searchText = experimentFilter.getSearchText().toString();
        
        // Build a Query to represent the security filter
        String securitySearchText = this.buildSecuritySearch();
        
        
        if (searchText != null && searchText.trim().length() > 0) {
          log.debug("Lucene search: " + searchText);
          QueryParser myQueryParser = new QueryParser("text", new StandardAnalyzer());
          myQueryParser.setAllowLeadingWildcard(true);
          Query query = myQueryParser.parse(searchText);
          
          // Build security filter        
          QueryWrapperFilter filter = this.buildSecurityQueryFilter(securitySearchText);

          // Search for the query
          Hits hits = null;
          if (filter == null) {
             hits = searcher.search(query);
          } else {
             hits = searcher.search(query, filter);
          }  
          
          processHits(hits, searchText, dh);
          
        } else {
          if (securitySearchText != null) {
            QueryParser myQueryParser = new QueryParser("text", new StandardAnalyzer());
            myQueryParser.setAllowLeadingWildcard(true);
            Query query = myQueryParser.parse(securitySearchText);
            Hits hits = searcher.search(query);
            processHits(hits, securitySearchText, dh);
          } else {
            buildProjectRequestMap(indexReader, dh);
          }
        }
        
      }
      
      
      if (!isExperimentOnlySearch && !isAnalysisOnlySearch && !isDataTrackOnlySearch) {
        //
        // Protocols
        //
        IndexReader protocolIndexReader = IndexReader.open(ph.getQualifiedProperty(PropertyDictionary.LUCENE_PROTOCOL_INDEX_DIRECTORY, serverName));      
        Searcher protocolSearcher = new IndexSearcher(protocolIndexReader);
        
        //  Build a Query object
        String protocolSearchText = protocolFilter.getSearchText().toString();
        
        if (protocolSearchText != null && protocolSearchText.trim().length() > 0) {
          log.debug("Lucene protocol search: " + protocolSearchText);
          QueryParser myQueryParser = new QueryParser("text", new StandardAnalyzer());
          myQueryParser.setAllowLeadingWildcard(true);
          Query query = myQueryParser.parse(protocolSearchText);          
          
          // Search for the query
          Hits protocolHits = protocolSearcher.search(query);
          processProtocolHits(protocolHits, protocolSearchText);
          
        } else {
          buildProtocolMap(protocolIndexReader);
          
        }
      }

      if (!isExperimentOnlySearch && !isAnalysisOnlySearch && !isProtocolOnlySearch) {
        //
        // Data Tracks
        //
        IndexReader dataTrackIndexReader = IndexReader.open(ph.getQualifiedProperty(PropertyDictionary.LUCENE_DATATRACK_INDEX_DIRECTORY, serverName));      
        Searcher dataTrackSearcher = new IndexSearcher(dataTrackIndexReader);
        
        //  Build a Query object
        String dataTrackSearchText = dataTrackFilter.getSearchText().toString();
        
        // Build a Query to represent the security filter
        String dataTrackSecuritySearchText = this.buildDataTrackSecuritySearch();

        if (dataTrackSearchText != null && dataTrackSearchText.trim().length() > 0) {
          log.debug("Lucene data track search: " + dataTrackSearchText);
          QueryParser myQueryParser = new QueryParser("text", new StandardAnalyzer());
          myQueryParser.setAllowLeadingWildcard(true);
          Query query = myQueryParser.parse(dataTrackSearchText);          
          
          // Build security filter        
          QueryWrapperFilter filter = this.buildSecurityQueryFilter(dataTrackSecuritySearchText);

          // Search for the query
          Hits hits = null;
          if (filter == null) {
             hits = dataTrackSearcher.search(query);
          } else {
             hits = dataTrackSearcher.search(query, filter);
          }  
          
          processDataTrackHits(hits, dataTrackSearchText);
          
        } else {
          if (dataTrackSecuritySearchText != null) {
            QueryParser myQueryParser = new QueryParser("text", new StandardAnalyzer());
            myQueryParser.setAllowLeadingWildcard(true);
            Query query = myQueryParser.parse(dataTrackSecuritySearchText);          
            Hits hits = dataTrackSearcher.search(query);
            processDataTrackHits(hits, dataTrackSecuritySearchText);
          } else {
            this.buildDataTrackGroupMap(dataTrackIndexReader);
          }
        }
      }
      
      
      if (!isExperimentOnlySearch && !isProtocolOnlySearch && !isDataTrackOnlySearch) {
        //
        // Analysis
        //
        IndexReader analysisIndexReader = IndexReader.open(ph.getQualifiedProperty(PropertyDictionary.LUCENE_ANALYSIS_INDEX_DIRECTORY, serverName));      
        Searcher analysisSearcher = new IndexSearcher(analysisIndexReader);
        
        //  Build a Query object
        String analysisSearchText = analysisFilter.getSearchText().toString();
        
        
        // Build a Query to represent the security filter
        String analysisSecuritySearchText = this.buildAnalysisSecuritySearch();

        if (analysisSearchText != null && analysisSearchText.trim().length() > 0) {
          log.debug("Lucene analysis search: " + analysisSearchText);
          QueryParser myQueryParser = new QueryParser("text", new StandardAnalyzer());
          myQueryParser.setAllowLeadingWildcard(true);
          Query query = myQueryParser.parse(analysisSearchText);          
          
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
            QueryParser myQueryParser = new QueryParser("text", new StandardAnalyzer());
            myQueryParser.setAllowLeadingWildcard(true);
            Query query = myQueryParser.parse(analysisSecuritySearchText);          
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
  
  private void processHits(Hits hits, String searchText, DictionaryHelper dh) throws Exception {
    
    // Show hits
    showExperimentHits(hits, searchText);
    
    // Map search results
    this.buildProjectRequestMap(hits, dh);
    
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
  
  private void processDataTrackHits(Hits hits, String searchText) throws Exception {
    
    // Show hits
    showDataTrackHits(hits, searchText);
    
    // Map search results
    this.buildDataTrackMap(hits);
    
  }
  
  private void buildProjectRequestMap(Hits hits, DictionaryHelper dh) throws Exception{
    
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
      mapDocument(doc, i, score, dh);
      
    }
  }
  
  private void buildProjectRequestMap(IndexReader indexReader, DictionaryHelper dh) throws Exception{
    
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
      mapDocument(doc, -1, -1, dh);      
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
  
  private void buildDataTrackMap(Hits hits) throws Exception{
    
    labDataTrackMap = new HashMap();
    dataTrackFolderMap = new HashMap();
    dataTrackMap = new HashMap();
    labToDataTrackFolderMap = new TreeMap();
    dataTrackFolderToDataTrackMap = new TreeMap();
    rankedDataTrackNodes = new ArrayList();
    
    dataTrackIds = new ArrayList<Integer>();
    for (int i = 0; i < hits.length(); i++) {
      org.apache.lucene.document.Document doc = hits.doc(i);
      Integer idDataTrack  = doc.get(DataTrackIndexHelper.ID_DATATRACK).equals("unknown") ? null : new Integer(doc.get(DataTrackIndexHelper.ID_DATATRACK));
      if (idDataTrack != null) {
        dataTrackIds.add(idDataTrack);
      }
      float score = hits.score(i);
      mapDataTrackDocument(doc, i, score);
      
    }
  }
  
  private void buildDataTrackGroupMap(IndexReader indexReader) throws Exception{
    
    labDataTrackMap = new HashMap();
    dataTrackFolderMap = new HashMap();
    dataTrackMap = new HashMap();
    labToDataTrackFolderMap = new TreeMap();
    dataTrackFolderToDataTrackMap = new TreeMap();
    rankedDataTrackNodes = new ArrayList();
    
    for (int i = 0; i < indexReader.numDocs(); i++) {
      org.apache.lucene.document.Document doc = indexReader.document(i);
      mapDataTrackDocument(doc, -1, -1);      
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
  
  private void mapDocument(org.apache.lucene.document.Document doc, int rank, float score, DictionaryHelper dh) {
    
    Integer idProject = new Integer(doc.get(ExperimentIndexHelper.ID_PROJECT));
    //Integer idLab     = new Integer(doc.get(ExperimentIndexHelper.ID_LAB_PROJECT));
    Integer idLab     = doc.get(ExperimentIndexHelper.ID_LAB_PROJECT) != null ? new Integer(doc.get(ExperimentIndexHelper.ID_LAB_PROJECT)) : null;
    Integer idRequest          = doc.get(ExperimentIndexHelper.ID_REQUEST).equals("unknown") ? null : new Integer(doc.get(ExperimentIndexHelper.ID_REQUEST));
    String codeRequestCategory = doc.get(ExperimentIndexHelper.CODE_REQUEST_CATEGORY) != null ? doc.get(ExperimentIndexHelper.CODE_REQUEST_CATEGORY) : null;
    String codeApplication = doc.get(ExperimentIndexHelper.CODE_APPLICATION) != null ? doc.get(ExperimentIndexHelper.CODE_APPLICATION) : null;
    String catKey = idProject + "-" + codeRequestCategory + "-" + codeApplication;  
    
    
    if(idLab != null) {
      Element labNode = (Element)labMap.get(idLab);
      if (labNode == null) {
        Element node = new Element("Lab");
        node.setAttribute("idLab", idLab.toString());
        node.setAttribute("labName", doc.get(ExperimentIndexHelper.PROJECT_LAB_NAME));
        node.setAttribute("label", doc.get(ExperimentIndexHelper.PROJECT_LAB_NAME));
        node.setAttribute("projectLabName", doc.get(ExperimentIndexHelper.PROJECT_LAB_NAME));
        labMap.put(idLab, node);
      }      
    }


    Element projectNode = (Element)projectMap.get(idProject);
    if (projectNode == null) {
      Element node = new Element("Project");
      node.setAttribute("idProject", idProject.toString());
      node.setAttribute("projectName", doc.get(ExperimentIndexHelper.PROJECT_NAME) != null ? doc.get(ExperimentIndexHelper.PROJECT_NAME) : "");
      node.setAttribute("label", doc.get(ExperimentIndexHelper.PROJECT_NAME) != null ? doc.get(ExperimentIndexHelper.PROJECT_NAME) : "");
      node.setAttribute("projectDescription", doc.get(ExperimentIndexHelper.PROJECT_DESCRIPTION) != null ? doc.get(ExperimentIndexHelper.PROJECT_DESCRIPTION) : "");
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
        node.setAttribute("codeApplication", codeApplication != null ? codeApplication : "");
        node.setAttribute("label", codeRequestCategory + " " + codeApplication);
        node.setAttribute("idProject",idProject.toString());     
        categoryMap.put(catKey, node);
      }

      
      Element requestNode = (Element)requestMap.get(idRequest);
      if (requestNode == null) {
        Element node = new Element("RequestNode");
        Element node1 = new Element("Request");
        buildRequestNode(node, idRequest, codeRequestCategory, codeApplication, doc, score, rank, dh);
        buildRequestNode(node1, idRequest, codeRequestCategory, codeApplication, doc, score, rank, dh);
        
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
      if(idLab != null) {
        labToProjectMap.put(labKey, projectIdMap);     
      }
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
    Integer idAnalysis          = doc.get(AnalysisIndexHelper.ID_ANALYSIS).equals("unknown") ? null : new Integer(doc.get(AnalysisIndexHelper.ID_ANALYSIS));
    
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
 
 private void mapDataTrackDocument(org.apache.lucene.document.Document doc, int rank, float score) {
    
    Integer idDataTrackFolder = doc.get(DataTrackIndexHelper.ID_DATATRACKFOLDER) == null ? -1 : new Integer(doc.get(DataTrackIndexHelper.ID_DATATRACKFOLDER));
    Integer idLab        = doc.get(DataTrackIndexHelper.ID_LAB_DATATRACKFOLDER) == null ? -1 : new Integer(doc.get(DataTrackIndexHelper.ID_LAB_DATATRACKFOLDER));
    Integer idDataTrack  = doc.get(DataTrackIndexHelper.ID_DATATRACK).equals("unknown") ? null : new Integer(doc.get(DataTrackIndexHelper.ID_DATATRACK));
    
    Element labNode = (Element)labDataTrackMap.get(idLab);
    if (labNode == null) {
      Element node = new Element("Lab");
      node.setAttribute("idLab", idLab.toString());
      node.setAttribute("labName", doc.get(DataTrackIndexHelper.LAB_NAME_DATATRACKFOLDER) == null ? "unknown" : doc.get(DataTrackIndexHelper.LAB_NAME_DATATRACKFOLDER));
      node.setAttribute("label", doc.get(DataTrackIndexHelper.LAB_NAME_DATATRACKFOLDER) == null ? "unknown" : doc.get(DataTrackIndexHelper.LAB_NAME_DATATRACKFOLDER));
      labDataTrackMap.put(idLab, node);
    }

    Element dataTrackFolderNode = (Element)dataTrackFolderMap.get(idDataTrackFolder);
    if (dataTrackFolderNode == null) {
      Element node = new Element("DataTrackFolder");
      node.setAttribute("idDataTrackFolder", idDataTrackFolder.toString());
      node.setAttribute("name", doc.get(DataTrackIndexHelper.DATATRACK_FOLDER_NAME) != null ? doc.get(DataTrackIndexHelper.DATATRACK_FOLDER_NAME) : "");
      node.setAttribute("label", doc.get(DataTrackIndexHelper.DATATRACK_FOLDER_NAME) != null ? doc.get(DataTrackIndexHelper.DATATRACK_FOLDER_NAME) : "");
      node.setAttribute("description", doc.get(DataTrackIndexHelper.DATATRACK_FOLDER_DESCRIPTION) != null ? doc.get(DataTrackIndexHelper.DATATRACK_FOLDER_DESCRIPTION) : "");
      node.setAttribute("codeVisibility", doc.get(DataTrackIndexHelper.CODE_VISIBILITY) != null ? doc.get(DataTrackIndexHelper.CODE_VISIBILITY) : "");
      if (idDataTrack == null || idDataTrack.intValue() == 0) {
        if (rank >= 0) {
          node.setAttribute("searchRank", new Integer(rank + 1).toString());
          node.setAttribute("searchInfo", " (Search rank #" + (rank + 1) + ")");
        }
        if (score >= 0) {
          node.setAttribute("searchScore", new Integer(Math.round(score * 100)).toString() + "%");                    
        }
      }
      dataTrackFolderMap.put(idDataTrackFolder, node);
    }
    
    if (idDataTrack != null) {
      
      
      Element dataTrackNode = (Element)dataTrackMap.get(idDataTrack);
      if (dataTrackNode == null) {
        Element node = new Element("DataTrackNode");
        Element node1 = new Element("DataTrack");
        buildDataTrackNode(node, idDataTrack, doc, score, rank);
        buildDataTrackNode(node1, idDataTrack, doc, score, rank);
        
        dataTrackMap.put(idDataTrackFolder + "-" + idDataTrack, node);
        rankedDataTrackNodes.add(node1);
      }        
    } else {
      Element node = new Element("DataTrack");
      buildEmptyDataTrackNode(node, idDataTrackFolder, doc, score, rank);
      rankedDataTrackNodes.add(node);
    }
    
    
    String labName     = doc.get(DataTrackIndexHelper.LAB_NAME_DATATRACKFOLDER);
    String dataTrackFolderName = doc.get(DataTrackIndexHelper.DATATRACK_FOLDER_NAME);
    String dataTrackCreateDate = doc.get(DataTrackIndexHelper.CREATE_DATE);
    String labKey = labName + "---" + idLab;
    Map dataTrackFolderIdMap = (Map)labToDataTrackFolderMap.get(labKey);
    if (dataTrackFolderIdMap == null) {
      dataTrackFolderIdMap = new TreeMap();
      labToDataTrackFolderMap.put(labKey, dataTrackFolderIdMap);
    }
    String dataTrackFolderKey = dataTrackFolderName + "---" + idDataTrackFolder;
    dataTrackFolderIdMap.put(dataTrackFolderKey, idDataTrackFolder);        
    
    if (idDataTrack != null) {


      Map idDataTrackMap = (Map)dataTrackFolderToDataTrackMap.get(idDataTrackFolder);
      if (idDataTrackMap == null) {
        idDataTrackMap = new TreeMap();
        dataTrackFolderToDataTrackMap.put(idDataTrackFolder, idDataTrackMap);
      }
      idDataTrackMap.put(idDataTrackFolder + "-" + dataTrackCreateDate + "-" + idDataTrack, idDataTrack);        
    }

  }
  
  private void buildRequestNode(Element node, Integer idRequest, String codeRequestCategory, String codeApplication, Document doc, float score, int rank, DictionaryHelper dh) {
    RequestCategory requestCategory = dh.getRequestCategoryObject(codeRequestCategory);
    
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
    node.setAttribute("icon", requestCategory != null && requestCategory.getIcon() != null ? requestCategory.getIcon() : "");
    node.setAttribute("type", requestCategory != null && requestCategory.getType() != null ? requestCategory.getType() : "");
    node.setAttribute("codeApplication", codeApplication != null ? codeApplication : "");
    node.setAttribute("labName", doc.get(ExperimentIndexHelper.LAB_NAME) != null ? doc.get(ExperimentIndexHelper.LAB_NAME) : "");
    node.setAttribute("projectName", doc.get(ExperimentIndexHelper.PROJECT_NAME) != null ? doc.get(ExperimentIndexHelper.PROJECT_NAME) : "");
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
    node.setAttribute("labName", doc.get(ExperimentIndexHelper.PROJECT_LAB_NAME) != null ? doc.get(ExperimentIndexHelper.PROJECT_LAB_NAME) : "");
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
    node.setAttribute("labName", doc.get(AnalysisIndexHelper.LAB_NAME_ANALYSISGROUP) != null ? doc.get(AnalysisIndexHelper.LAB_NAME_ANALYSISGROUP) : "" );
    node.setAttribute("label",  doc.get(AnalysisIndexHelper.ANALYSIS_GROUP_NAME) != null ? doc.get(AnalysisIndexHelper.ANALYSIS_GROUP_NAME) : "");
    if (rank >= 0) {
      node.setAttribute("searchRank", new Integer(rank + 1).toString());          
      node.setAttribute("searchInfo", " (Search rank #" + (rank + 1) + ")");
    } 
    if (score >= 0) {
      node.setAttribute("searchScore", new Integer(Math.round(score * 100)).toString() + "%");          
    }

  }
  
  private void buildDataTrackNode(Element node, Integer idDataTrack, Document doc, float score, int rank) {
    node.setAttribute("idDataTrack", idDataTrack.toString());
    node.setAttribute("dataTrackFolderName", doc.get(DataTrackIndexHelper.DATATRACK_FOLDER_NAME));
    node.setAttribute("label", doc.get(DataTrackIndexHelper.DATATRACK_NAME));
    node.setAttribute("createDate", doc.get(DataTrackIndexHelper.CREATE_DATE) != null ? doc.get(DataTrackIndexHelper.CREATE_DATE) : "");
    node.setAttribute("codeVisibility",  doc.get(DataTrackIndexHelper.CODE_VISIBILITY) != null ? doc.get(DataTrackIndexHelper.CODE_VISIBILITY) : "");
    node.setAttribute("public",  doc.get(DataTrackIndexHelper.CODE_VISIBILITY) != null && doc.get(DataTrackIndexHelper.CODE_VISIBILITY).equals(Visibility.VISIBLE_TO_PUBLIC) ? "Public" : "");
    node.setAttribute("publicNote", doc.get(DataTrackIndexHelper.PUBLIC_NOTE) != null ? doc.get(DataTrackIndexHelper.PUBLIC_NOTE) : "");
    node.setAttribute("name", doc.get(DataTrackIndexHelper.DATATRACK_NAME) != null ?  doc.get(DataTrackIndexHelper.DATATRACK_NAME) : "");
    node.setAttribute("ownerFirstName", doc.get(DataTrackIndexHelper.OWNER_FIRST_NAME) != null ? doc.get(DataTrackIndexHelper.OWNER_FIRST_NAME) : "");
    node.setAttribute("ownerLastName", doc.get(DataTrackIndexHelper.OWNER_LAST_NAME) != null ? doc.get(DataTrackIndexHelper.OWNER_LAST_NAME) : "");
    node.setAttribute("labName", doc.get(DataTrackIndexHelper.LAB_NAME) != null ? doc.get(DataTrackIndexHelper.LAB_NAME) : "" );
    node.setAttribute("description", doc.get(DataTrackIndexHelper.DESCRIPTION) != null ? doc.get(DataTrackIndexHelper.DESCRIPTION) : "" );
    node.setAttribute("fileName", doc.get(DataTrackIndexHelper.FILE_NAME) != null ? doc.get(DataTrackIndexHelper.FILE_NAME) : "" );
    node.setAttribute("summary", doc.get(DataTrackIndexHelper.SUMMARY) != null ? doc.get(DataTrackIndexHelper.SUMMARY) : "" );
    if (rank >= 0) {
      node.setAttribute("searchRank", new Integer(rank + 1).toString());          
      node.setAttribute("searchInfo", " (Search rank #" + (rank + 1) + ")");
    } 
    if (score >= 0) {
      node.setAttribute("searchScore", new Integer(Math.round(score * 100)).toString() + "%");          
    }

  }

  private void buildEmptyDataTrackNode(Element node,  Integer idDataTrackFolder, Document doc, float score, int rank) {
    node.setAttribute("idDataTrack", "-1");
    node.setAttribute("idDataTrackFolder", idDataTrackFolder.toString());
    node.setAttribute("dataTrackFolderName", doc.get(DataTrackIndexHelper.DATATRACK_FOLDER_NAME));
    node.setAttribute("labName", doc.get(DataTrackIndexHelper.LAB_NAME_DATATRACKFOLDER) != null ? doc.get(DataTrackIndexHelper.LAB_NAME_DATATRACKFOLDER) : "" );
    node.setAttribute("label",  doc.get(DataTrackIndexHelper.DATATRACK_FOLDER_NAME) != null ? doc.get(DataTrackIndexHelper.DATATRACK_FOLDER_NAME) : "");
    if (rank >= 0) {
      node.setAttribute("searchRank", new Integer(rank + 1).toString());          
      node.setAttribute("searchInfo", " (Search rank #" + (rank + 1) + ")");
    } 
    if (score >= 0) {
      node.setAttribute("searchScore", new Integer(Math.round(score * 100)).toString() + "%");          
    }

  }
  
  private org.jdom.Document buildXMLDocument() throws Exception {
    org.jdom.Document xmlDoc = new org.jdom.Document(new Element(listKind));
    
    if (!isAnalysisOnlySearch && !isDataTrackOnlySearch && !isProtocolOnlySearch) {
      buildProjectRequestList(xmlDoc.getRootElement());
      buildRequestList(xmlDoc.getRootElement());      
    }
    if (!isExperimentOnlySearch && !isAnalysisOnlySearch && !isDataTrackOnlySearch) {
      buildProtocolList(xmlDoc.getRootElement());
    }
    if (!isExperimentOnlySearch && !isAnalysisOnlySearch && !isProtocolOnlySearch) {
     // buildDataTrackFolderList(doc.getRootElement());
      DataTrackQuery q = new DataTrackQuery(dataTrackIds);
      Element qElem = q.getDataTrackDocument(this.getSecAdvisor().getReadOnlyHibernateSession(this.username), this.getSecAdvisor()).getRootElement();
      qElem.setName("DataTrackFolderList");
      xmlDoc.getRootElement().addContent(qElem);
      buildDataTrackList(xmlDoc.getRootElement());
    }
    if (!isExperimentOnlySearch && !isProtocolOnlySearch && !isDataTrackOnlySearch) {
      buildAnalysisGroupList(xmlDoc.getRootElement());
      buildAnalysisList(xmlDoc.getRootElement());      
    }
    
    return xmlDoc;
    
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

  private void buildDataTrackFolderList(Element root) {
    Element parent = new Element("DataTrackFolderList");
    root.addContent(parent);
    
    // For each lab
    for(Iterator i = labToDataTrackFolderMap.keySet().iterator(); i.hasNext();) {
      String key = (String)i.next();
      
      String []tokens = key.split("---");
      Integer idLab = new Integer(tokens[1]);
      
      Element labNode = (Element)labDataTrackMap.get(idLab);
      Map dataTrackFolderIdMap = (Map)labToDataTrackFolderMap.get(key);
      
      parent.addContent(labNode);
      
      // For each data track folder in lab
      for(Iterator i1 = dataTrackFolderIdMap.keySet().iterator(); i1.hasNext();) {
        String dtfKey = (String)i1.next();

        
        Integer idDataTrackFolder = (Integer)dataTrackFolderIdMap.get(dtfKey);

        Element dtfNode = (Element)dataTrackFolderMap.get(idDataTrackFolder);
        Map idDataTrackMap = (Map)dataTrackFolderToDataTrackMap.get(idDataTrackFolder);
        
        labNode.addContent(dtfNode);
        
        
        // For each data track in data track folder
        if (idDataTrackMap != null) {
          for(Iterator i2 = idDataTrackMap.keySet().iterator(); i2.hasNext();) {
            
            String dtkey = (String)i2.next();
            Integer idDataTrack = (Integer)idDataTrackMap.get(dtkey);
            Element dtNode = (Element)dataTrackMap.get(idDataTrackFolder + "-" + idDataTrack);
            dtfNode.addContent(dtNode);
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
    
    // For each protocol node
    for(Iterator i = rankedProtocolNodes.iterator(); i.hasNext();) {
      Element protocolNode = (Element)i.next();
      parent.addContent(protocolNode);
    }
  }
  
  private void buildAnalysisList(Element root) {
    Element parent = new Element("AnalysisList");
    root.addContent(parent);
    
    // For each analysis node
    for(Iterator i = rankedAnalysisNodes.iterator(); i.hasNext();) {
      Element node = (Element)i.next();
      parent.addContent(node);
    }
    
  }
  
  private void buildDataTrackList(Element root) {
    Element parent = new Element("DataTrackList");
    root.addContent(parent);
    
    // For each data track node
    for(Iterator i = rankedDataTrackNodes.iterator(); i.hasNext();) {
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
    
    
    boolean addedFilter = this.getSecAdvisor().buildLuceneSecurityFilter(searchText, 
                                                                    ExperimentIndexHelper.ID_LAB,
                                                                    ExperimentIndexHelper.ID_INSTITUTION,
                                                                    ExperimentIndexHelper.COLLABORATORS,
                                                                    ExperimentIndexHelper.ID_APPUSER,
                                                                    ExperimentIndexHelper.CODE_VISIBILITY,
                                                                    scopeToGroup,
                                                                    ExperimentIndexHelper.ID_LAB_PROJECT, 
                                                                    ExperimentIndexHelper.ID_REQUEST + ":unknown" );

   
    if (addedFilter) {
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
    
    boolean addedFilter = this.getSecAdvisor().buildLuceneSecurityFilter(searchText, 
                                                                    AnalysisIndexHelper.ID_LAB,
                                                                    AnalysisIndexHelper.ID_INSTITUTION,
                                                                    AnalysisIndexHelper.COLLABORATORS,
                                                                    AnalysisIndexHelper.ID_APPUSER,
                                                                    AnalysisIndexHelper.CODE_VISIBILITY,
                                                                    scopeToGroup,
                                                                    AnalysisIndexHelper.ID_LAB_ANALYSISGROUP, 
                                                                    AnalysisIndexHelper.ID_ANALYSIS + ":unknown" );

    
    if (addedFilter) {
      log.debug("Security filter: " + searchText.toString());
      return searchText.toString();           
    } else {
      return null;
    }
  }
  
  private String buildDataTrackSecuritySearch() throws Exception {
    boolean scopeToGroup = true;
    if (experimentFilter.getSearchPublicProjects() != null && experimentFilter.getSearchPublicProjects().equalsIgnoreCase("Y")) {
      scopeToGroup = false;
    }
    
   
    StringBuffer searchText = new StringBuffer();
    
    boolean addedFilter = this.getSecAdvisor().buildLuceneSecurityFilter(searchText, 
                                                                    DataTrackIndexHelper.ID_LAB,
                                                                    DataTrackIndexHelper.ID_INSTITUTION,
                                                                    DataTrackIndexHelper.COLLABORATORS,
                                                                    DataTrackIndexHelper.ID_APPUSER,
                                                                    DataTrackIndexHelper.CODE_VISIBILITY,
                                                                    scopeToGroup,
                                                                    DataTrackIndexHelper.ID_LAB_DATATRACKFOLDER, 
                                                                    DataTrackIndexHelper.ID_DATATRACK + ":unknown" );

    
    if (addedFilter) {
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
      QueryParser myQueryParser = new QueryParser("text", new StandardAnalyzer());
      myQueryParser.setAllowLeadingWildcard(true);
      Query securityQuery = myQueryParser.parse(searchText.toString());          
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
  
  private void showDataTrackHits(Hits hits, String searchText) throws Exception {

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
              System.out.println("  " + (i + 1) + ". " + " score=" + score + " " + doc.get(DataTrackIndexHelper.DATATRACK_FOLDER_NAME) + " " + doc.get(DataTrackIndexHelper.DATATRACK_NAME));
          }
      }
      System.out.println();      
    }

  }

}