package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.security.UnknownPermissionException;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.model.Lab;
import hci.gnomex.model.ProjectRequestFilter;
import hci.gnomex.model.ProjectRequestLuceneFilter;
import hci.gnomex.security.SecurityAdvisor;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
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
  
  /*
  private String   searchText = null;
  private String   field      = "text";
  private String   searchPublicProjects = null;
  */
  private ProjectRequestLuceneFilter gnomexFilter = null;
  
  private Map labMap        = null;
  private Map projectMap    = null;
  private Map categoryMap   = null;
  private Map requestMap    = null;
  private Map labToProjectMap = null;
  private Map projectToRequestCategoryMap = null;
  private Map categoryToRequestMap = null;

  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    /*
    if(request.getParameter("searchText") != null && !request.getParameter("searchText").equals("")) {
      searchText = request.getParameter("searchText");
    }
    if(request.getParameter("field") != null && !request.getParameter("field").equals("")) {
      field = request.getParameter("field");
    } 
    if(request.getParameter("searchPublicProjects") != null && !request.getParameter("searchPublicProjects").equals("")) {
      searchPublicProjects = request.getParameter("searchPublicProjects");
    } 
    */
    
    gnomexFilter = new ProjectRequestLuceneFilter();
    HashMap errors = this.loadDetailObject(request, gnomexFilter);
    this.addInvalidFields(errors);
    
    
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      
   
      IndexReader indexReader = IndexReader.open("c:/orion/luceneIndexGnomEx/");      
      Searcher searcher = new IndexSearcher(indexReader);
      
      
      
      //  Build a Query object
      String searchText = gnomexFilter.getSearchText().toString();
      Query query = new QueryParser("text", new StandardAnalyzer()).parse(searchText);
      
      // Build security filter
      QueryWrapperFilter filter = this.buildSecurityQueryFilter();


      // Search for the query
      Hits hits = null;
      if (filter == null) {
         hits = searcher.search(query);
      } else {
         hits = searcher.search(query, filter);
      }
      
      

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
      
      this.buildProjectRequestMap(hits);
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
  
  private void buildProjectRequestMap(Hits hits) throws Exception{
    
    labMap        = new HashMap();
    projectMap    = new HashMap();
    categoryMap   = new HashMap();
    requestMap    = new HashMap();
    labToProjectMap = new HashMap();
    projectToRequestCategoryMap = new HashMap();
    categoryToRequestMap = new HashMap();
    
    for (int i = 0; i < hits.length(); i++) {
      org.apache.lucene.document.Document doc = hits.doc(i);
      float score = hits.score(i);
      
      Integer idProject = new Integer(doc.get("idProject"));
      Integer idLab     = new Integer(doc.get("projectIdLab"));
      Integer idRequest          = doc.get("idRequest")           != null ? new Integer(doc.get("idRequest")) : null;
      String codeRequestCategory = doc.get("codeRequestCategory") != null ? doc.get("codeRequestCategory") : null;
      String codeMicroarrayCategory = doc.get("codeMicroarrayCategory") != null ? doc.get("codeMicroarrayCategory") : null;
      String catKey = idProject + "-" + codeRequestCategory + "-" + codeMicroarrayCategory;      
      
      Element labNode = (Element)labMap.get(idLab);
      if (labNode == null) {
        Element node = new Element("Lab");
        node.setAttribute("idLab", idLab.toString());
        node.setAttribute("labName", doc.get("projectLab"));
        node.setAttribute("projectLabName", doc.get("projectLab"));
        labMap.put(idLab, node);
      }

      Element projectNode = (Element)projectMap.get(idProject);
      if (projectNode == null) {
        Element node = new Element("Project");
        node.setAttribute("idProject", idProject.toString());
        node.setAttribute("projectName", doc.get("projectName"));
        node.setAttribute("projectDescription", doc.get("projectDescription") != null ? doc.get("projectDescription") : "");
        node.setAttribute("codeVisibility", doc.get("projectCodeVisibility"));
        node.setAttribute("projectPublicNote", doc.get("projectPublicNote") != null ? doc.get("projectPublicNote") : "");
        if (idRequest == null) {
          node.setAttribute("searchRank", new Integer(i + 1).toString());
          node.setAttribute("searchScore", new Float(hits.score(i)).toString());          
        }
        projectMap.put(idProject, node);
      }
      
      if (idRequest != null) {
        
        Element categoryNode = (Element)categoryMap.get(catKey);
        if (categoryNode == null) {
          Element node = new Element("RequestCategory");
          node.setAttribute("codeRequestCategory", codeRequestCategory != null ? codeRequestCategory : "");
          node.setAttribute("codeMicroarrayCategory", codeMicroarrayCategory != null ? codeMicroarrayCategory : "");
          node.setAttribute("idProject",idProject.toString());     
          categoryMap.put(catKey, node);
        }

        
        Element requestNode = (Element)requestMap.get(idRequest);
        if (requestNode == null) {
          Element node = new Element("Request");
          node.setAttribute("idProject", idRequest.toString());
          node.setAttribute("requestNumber", doc.get("requestNumber"));
          node.setAttribute("requestCreateDate", doc.get("requestCreateDate"));
          node.setAttribute("codeVisibility",  doc.get("requestCodeVisibility") != null ? doc.get("requestCodeVisibility") : "");
          node.setAttribute("requestPublicNote", doc.get("requestPublicNote") != null ? doc.get("requestPublicNote") : "");
          node.setAttribute("displayName", doc.get("requestDisplayName"));
          node.setAttribute("ownerFirstName", doc.get("requestOwnerFirstName"));
          node.setAttribute("ownerLastName", doc.get("requestOwnerLastName"));
          node.setAttribute("slideProductName", doc.get("slideProduct") != null ? doc.get("slideProduct") : "");
          node.setAttribute("codeRequestCategory", codeRequestCategory != null ? codeRequestCategory : "");
          node.setAttribute("codeMicroarrayCategory", codeMicroarrayCategory != null ? codeMicroarrayCategory : "");
          node.setAttribute("requestLabName", doc.get("requestLab"));
          node.setAttribute("projectName", doc.get("projectName"));
          node.setAttribute("idSlideProduct", doc.get("idSlideProduct") != null ? doc.get("idSlideProduct") : "");
          node.setAttribute("searchRank", new Integer(i + 1).toString());
          node.setAttribute("searchScore", new Float(hits.score(i)).toString());
          
          requestMap.put(idRequest, node);
        }        
      }
      
      List ids = (List)labToProjectMap.get(idLab);
      if (ids == null) {
        ids = new ArrayList();
        labToProjectMap.put(idLab, ids);
      }
      if (!ids.contains(idProject)) {
        ids.add(idProject);        
      }
      

      
      if (idRequest != null) {
        List codes = (List)projectToRequestCategoryMap.get(idProject);
        if (codes == null) {
          codes = new ArrayList();
          projectToRequestCategoryMap.put(idProject, codes);
        }        
        if (!codes.contains(catKey)) {        
          codes.add(catKey);        
        }

        List idRequests = (List)categoryToRequestMap.get(catKey);
        if (idRequests == null) {
          idRequests = new ArrayList();
          categoryToRequestMap.put(catKey, idRequests);
        }
        idRequests.add(idRequest);        
      }
      
    }
  }
  
  private org.jdom.Document buildXMLDocument() {
    org.jdom.Document doc = new org.jdom.Document(new Element("ProjectRequestList"));
    
    // For each lab
    for(Iterator i = labToProjectMap.keySet().iterator(); i.hasNext();) {
      Integer idLab = (Integer)i.next();
      Element labNode = (Element)labMap.get(idLab);
      List ids = (List)labToProjectMap.get(idLab);
      
      
      doc.getRootElement().addContent(labNode);
      
      // For each project in lab
      for(Iterator i1 = ids.iterator(); i1.hasNext();) {
        Integer idProject = (Integer)i1.next();

        Element projectNode = (Element)projectMap.get(idProject);
        List codes = (List)projectToRequestCategoryMap.get(idProject);
        
        labNode.addContent(projectNode);
        
        
        // For each request category in project
        if (codes != null) {
          for(Iterator i2 = codes.iterator(); i2.hasNext();) {
            String code = (String)i2.next();
            Element categoryNode = (Element)categoryMap.get(code);
            List idRequests = (List)categoryToRequestMap.get(code);
            
            if (categoryNode != null) {
              projectNode.addContent(categoryNode);
              
              // For each request in request category
              for(Iterator i3 = idRequests.iterator(); i3.hasNext();) {
                Integer idRequest = (Integer)i3.next();
                Element requestNode = (Element)requestMap.get(idRequest);
                categoryNode.addContent(requestNode);
                
              }              
            }
            
          }
          
        }
          
        }
      
    }
    return doc;
    
  }
  
  
  private QueryWrapperFilter buildSecurityQueryFilter() throws Exception {
    boolean scopeToGroup = true;
    if (gnomexFilter.getSearchPublicProjects() != null && gnomexFilter.getSearchPublicProjects().equalsIgnoreCase("Y")) {
      scopeToGroup = false;
    }
    
    QueryWrapperFilter filter = null;
    StringBuffer searchText = new StringBuffer();
    boolean addedFilter1 = false;
    boolean addedFilter2 = false;
    StringBuffer searchText1 = new StringBuffer();
    StringBuffer searchText2 = new StringBuffer();
    addedFilter1 = this.getSecAdvisor().buildLuceneSecurityFilter(searchText1, 
                                                                   "projectIdLab", 
                                                                   "projectCodeVisibility", 
                                                                   scopeToGroup);
    if (addedFilter1) {
      searchText2 = new StringBuffer();
      addedFilter2 = this.getSecAdvisor().buildLuceneSecurityFilter(searchText2, 
                                                                    "requestIdLab",
                                                                    "requestCodeVisibility",
                                                                    scopeToGroup);
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
      Query securityQuery = new QueryParser("text", new StandardAnalyzer()).parse(searchText.toString());
      filter = new QueryWrapperFilter(securityQuery);            
    }
    return filter;
  }


}