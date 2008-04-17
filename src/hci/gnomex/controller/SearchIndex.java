package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.security.UnknownPermissionException;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.constants.Constants;
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

  private ExperimentFilter experimentFilter = null;
  private ProtocolFilter       protocolFilter = null;
  
  private String listKind = "SearchList";
  
  private String searchDisplayText = "";
  
  private Map labMap        = null;
  private Map projectMap    = null;
  private Map categoryMap   = null;
  private Map requestMap    = null;
  private Map labToProjectMap = null;
  private Map projectToRequestCategoryMap = null;
  private Map categoryToRequestMap = null;
  
  private ArrayList rankedRequestNodes = null;

  private ArrayList rankedProtocolNodes = null;
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    
    if (request.getParameter("listKind") != null && !request.getParameter("listKind").equals("")) {
      listKind = request.getParameter("listKind");
    }

    
    
   
    
    
    
    experimentFilter = new ExperimentFilter();
    HashMap errors = this.loadDetailObject(request, experimentFilter);
    this.addInvalidFields(errors);
    
    protocolFilter = new ProtocolFilter();
    errors = this.loadDetailObject(request, protocolFilter);
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
      
      
   
      IndexReader indexReader = IndexReader.open(Constants.LUCENE_EXPERIMENT_INDEX_DIRECTORY);      
      Searcher searcher = new IndexSearcher(indexReader);
      
      //  Build a Query object
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
      
      
      IndexReader protocolIndexReader = IndexReader.open(Constants.LUCENE_PROTOCOL_INDEX_DIRECTORY);      
      Searcher protocolSearcher = new IndexSearcher(protocolIndexReader);
      
      //  Build a Query object
      String protocolSearchText = experimentFilter.getSearchText().toString();
      
      if (protocolSearchText != null && protocolSearchText.trim().length() > 0) {
        log.debug("Lucene protocol search: " + protocolSearchText);
        Query query = new QueryParser("text", new StandardAnalyzer()).parse(protocolSearchText);
        
        // Search for the query
        Hits protocolHits = protocolSearcher.search(query);
        processProtocolHits(protocolHits, protocolSearchText);
        
      } else {
        buildProtocolMap(protocolIndexReader);
        
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
      idRequestMap.put(requestCreateDate, idRequest);        
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

  
  private org.jdom.Document buildXMLDocument() {
    org.jdom.Document doc = new org.jdom.Document(new Element(listKind));
    doc.getRootElement().setAttribute("search", searchDisplayText);
    
    buildProjectRequestList(doc.getRootElement());
    buildRequestList(doc.getRootElement());
    buildProtocolList(doc.getRootElement());
    
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

}