package hci.gnomex.controller;

import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;
import hci.dictionary.utility.DictionaryManager;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.framework.utilities.XMLReflectException;

import java.io.Serializable;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.jdom.Element;
import org.jdom.Document;
import org.jdom.output.XMLOutputter;

import hci.gnomex.model.AppUser;
import hci.gnomex.model.Lab;
import hci.gnomex.model.PendingSampleFilter;
import hci.gnomex.model.Project;
import hci.gnomex.model.ProjectRequestFilter;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.model.Sample;
import hci.gnomex.model.Visibility;


public class GetPendingSampleList extends GNomExCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetPendingSampleList.class);
  
  private PendingSampleFilter  filter;
  private Element              rootNode = null;
  private Element              redoNode = null;
  private Element              pendingNode = null;
  private Element              requestNode = null;
  
  TreeMap<String, TreeMap<Integer, List<Object[]>>> assayMap = null;
  TreeMap<Integer, List<Object[]>> requestMap = null;

  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    if (!this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_MANAGE_DNA_SEQ_CORE)) {
      this.addInvalidField("perm", "Insufficient permissions to view pending samples for DNA Seq core");
    }

    filter = new PendingSampleFilter();
    HashMap errors = this.loadDetailObject(request, filter);
    this.addInvalidFields(errors);
    
    
  }

  public Command execute() throws RollBackCommandException {
    Document doc = new Document(new Element("SampleList"));
    rootNode = doc.getRootElement();
    
    redoNode = new Element("Status");
    redoNode.setAttribute("label", "Redos");
    rootNode.addContent(redoNode);

    pendingNode = new Element("Status");
    pendingNode.setAttribute("label", "Pending");
    rootNode.addContent(pendingNode);
    
    try {
      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
      
      DictionaryHelper dictionaryHelper = DictionaryHelper.getInstance(sess);

      //
      // Get the 'redo' samples. Organize by primer or assay, then request, then well
      //
      StringBuffer buf = filter.getRedoQuery();
      log.info("Redo sample query for GetPendingSampleList: " + buf.toString());
      Query query = sess.createQuery(buf.toString());
      List redoResults = (List)query.list();
      assayMap = new  TreeMap<String, TreeMap<Integer, List<Object[]>>>();
      hashResults(redoResults);
      fillNodes(redoNode, dictionaryHelper);


      //
      // Get the pending samples under a status xml node, and then assay or
      // primer (if applicable), then by request, then well.  
      // Tubes have wells that don't belong to a source plate.
      //
      buf = filter.getPendingSamplesQuery();
      log.info("Pending tube query for GetPendingSampleList: " + buf.toString());
      query = sess.createQuery(buf.toString());
      List pendingSamples = (List)query.list();
      
      assayMap = new  TreeMap<String, TreeMap<Integer, List<Object[]>>>();
      hashResults(pendingSamples);
      fillNodes(pendingNode, dictionaryHelper);
      
       

      
      
      XMLOutputter out = new org.jdom.output.XMLOutputter();
      this.xmlResult = out.outputString(doc);
      
      setResponsePage(this.SUCCESS_JSP);
    }catch (NamingException e){
      log.error("An exception has occurred in GetPendingSampleList ", e);
      e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());
        
    }catch (SQLException e) {
      log.error("An exception has occurred in GetPendingSampleList ", e);
      e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());
    }catch (Exception e) {
      log.error("An exception has occurred in GetPendingSampleList ", e);
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
  
  private void hashResults(List rows) {
    // Hash the pending tubes
    for(Iterator i = rows.iterator(); i.hasNext();) {
      Object[] row = (Object[])i.next();
      Integer idRequest           = (Integer)row[0];
      Integer idAssay             = (Integer)row[13];
      Integer idPrimer            = (Integer)row[14];
      
      String assayKey = " ";
      if (idAssay != null && idAssay.intValue() != -1) {
        assayKey = DictionaryManager.getDisplay("hci.gnomex.model.Assay", idAssay.toString());
      } else if (idPrimer != null && idPrimer.intValue() != -1){
        assayKey = DictionaryManager.getDisplay("hci.gnomex.model.Primer", idPrimer.toString());
      }
      
      requestMap = assayMap.get(assayKey);
      if (requestMap == null) {
        requestMap = new TreeMap<Integer, List<Object[]>>();
        assayMap.put(assayKey, requestMap);
      }
      
      
      List<Object[]> results = requestMap.get(idRequest);
      if (results == null) {
        results = new ArrayList<Object[]>();
        requestMap.put(idRequest, results);
      }
      results.add(row);
    }
    
  }
  
  private void fillNodes(Element statusNode, DictionaryHelper dictionaryHelper) {
    Element parentNode = null;
    
    // Now create a request node for each key in the map
    // and create a well node child of the request node
    // for every row associated with the request.
    for(Iterator i = assayMap.keySet().iterator(); i.hasNext();) {
      String assayKey = (String)i.next();
      requestMap = assayMap.get(assayKey);
      
      if (filter.getCodeRequestCategory().equals(RequestCategory.CAPILLARY_SEQUENCING_REQUEST_CATEGORY)) {
        parentNode = statusNode;
      } else if (filter.getCodeRequestCategory().equals(RequestCategory.FRAGMENT_ANALYSIS_REQUEST_CATEGORY)) {
        parentNode = new Element("Assay");
        statusNode.addContent(parentNode);
      } if (filter.getCodeRequestCategory().equals(RequestCategory.MITOCHONDRIAL_DLOOP_SEQ_REQUEST_CATEGORY)) {
        parentNode = new Element("Primer");
        statusNode.addContent(parentNode);
      } else {
        parentNode = statusNode;
      }
      
      for (Iterator i1 = requestMap.keySet().iterator(); i1.hasNext();) {
        Integer idRequest = (Integer)i1.next();
        List<Object[]> results = requestMap.get(idRequest);

        boolean firstTime = true;
        for (Object[]row : results) {
          if (firstTime) {
            addRequestNode(parentNode, row, dictionaryHelper);  
            firstTime = false;
          }
          addWellNode(row, dictionaryHelper);
        }
      }
      
    }
    
  }
  
  
  private void addRequestNode(Element parentNode, Object[] row, DictionaryHelper dictionaryHelper) {
    
    Integer idRequest           = (Integer)row[0];
    String requestNumber        = (String)row[1]  == null ? ""  : (String)row[1];
    String codeRequestStatus    = (String)row[2]  == null ? ""  : (String)row[2];
    String codeRequestCategory  = (String)row[3]  == null ? ""  : (String)row[3];
    Date createDate             = (Date)row[4];
    Integer idLab               = (Integer)row[5];
    String labLastName          = (String)row[6]  == null ? ""  : (String)row[6];
    String labFirstName         = (String)row[7]  == null ? ""  : (String)row[7];
    AppUser submitter           = (AppUser)row[8];
    Sample sample               = (Sample)row[9];
    String wellRow              = (String)row[10]  == null ? ""  : (String)row[10];
    Integer wellCol             = (Integer)row[11];
    Integer wellIndex           = (Integer)row[12];

    
    requestNode = new Element("Request");
    requestNode.setAttribute("idRequest",              idRequest.toString());
    requestNode.setAttribute("label",                  requestNumber);
    requestNode.setAttribute("submitDate",             createDate == null ? ""  : this.formatDate((java.sql.Date)createDate, this.DATE_OUTPUT_DASH));
    requestNode.setAttribute("idLab",                  idLab == null ? "" : idLab.toString());
    requestNode.setAttribute("submitter",              submitter != null ? submitter.getDisplayName() : "?");
  
    parentNode.addContent(requestNode);
  
  }
  
  private void addWellNode(Object[] row, DictionaryHelper dictionaryHelper) {
    
    Integer idRequest           = (Integer)row[0];
    String requestNumber        = (String)row[1]  == null ? ""  : (String)row[1];
    String codeRequestStatus    = (String)row[2]  == null ? ""  : (String)row[2];
    String codeRequestCategory  = (String)row[3]  == null ? ""  : (String)row[3];
    Date createDate             = (Date)row[4];
    Integer idLab               = (Integer)row[5];
    String labLastName          = (String)row[6]  == null ? ""  : (String)row[6];
    String labFirstName         = (String)row[7]  == null ? ""  : (String)row[7];
    AppUser submitter           = (AppUser)row[8];
    Sample sample               = (Sample)row[9];
    String wellRow              = (String)row[10]  == null ? ""  : (String)row[10];
    Integer wellCol             = (Integer)row[11];
    Integer wellIndex           = (Integer)row[12];
    
    RequestCategory requestCategory = dictionaryHelper.getRequestCategoryObject(codeRequestCategory);
      
    Element n = new Element("Well");
    n.setAttribute("name",           sample.getName());
    n.setAttribute("idRequest",      idRequest != null ? idRequest.toString() : "");
    n.setAttribute("idLab",          idLab != null ? idLab.toString() : "");
    n.setAttribute("idSample",       sample.getIdSample().toString());
    n.setAttribute("type",           requestCategory.getRequestCategory());
    n.setAttribute("row",            wellRow != null ? wellRow : "");
    n.setAttribute("col",            wellCol != null ? wellCol.toString() : "");
    n.setAttribute("index",          wellIndex != null ? wellIndex.toString() : "");
    n.setAttribute("idPlate",        "");
    
    requestNode.addContent(n);      
  
  }
}