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
import java.util.Map;
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
import hci.gnomex.model.PlateWell;
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
  private Element              plateNode = null;
  
  private Integer              idPlatePrev = new Integer(-1);

  
  TreeMap<Integer, TreeMap<String, List<Object[]>>> requestMap = null;
  TreeMap<String, List<Object[]>>                   assayMap = null;
  HashMap<Integer, Element>                         requestNodeMap = new HashMap<Integer, Element>();

  
  private static final String DELIM = ",,,";
  
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
      
      // Get the pending samples that are already on a reaction plate.
      // We hash these so that they will be excluded from the
      // pending sample list
      //
      HashMap<Integer, Sample> samplesToFilter = new HashMap<Integer, Sample>(); 
      StringBuffer buf = filter.getPendingSamplesAlreadyOnPlateQuery();
      log.info("Pending samples alrady on plate GetPendingSampleList: " + buf.toString());
      Query query = sess.createQuery(buf.toString());
      List pendingSamplesAlreadyOnPlate = (List)query.list();
      for (Iterator i = pendingSamplesAlreadyOnPlate.iterator(); i.hasNext();) {
        Sample s = (Sample)i.next();
        samplesToFilter.put(s.getIdSample(), s);
      }

      //
      // Get the 'redo' samples. Organize by primer or assay, then request, then well
      //
      buf = filter.getRedoQuery();
      log.info("Redo sample query for GetPendingSampleList: " + buf.toString());
      query = sess.createQuery(buf.toString());
      List redoResults = (List)query.list();
      requestMap = new  TreeMap<Integer, TreeMap<String, List<Object[]>>>();
      hashResults(redoResults, dictionaryHelper, null);
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
      
      requestMap = new  TreeMap<Integer, TreeMap<String, List<Object[]>>>();
      hashResults(pendingSamples, dictionaryHelper, samplesToFilter);
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
  
  private void hashResults(List rows, DictionaryHelper dictionaryHelper, Map<Integer, Sample>samplesToFilter) {
    // Hash the pending tubes
    for(Iterator i = rows.iterator(); i.hasNext();) {
      Object[] row = (Object[])i.next();
      Integer idRequest           = (Integer)row[0];
      Integer idAssay             = (Integer)row[13];
      Integer idPrimer            = (Integer)row[14];
      Sample sample               = (Sample)row[9];
      
      // Filter out rows for samples already on reaction plate
      if (samplesToFilter != null) {
        if (samplesToFilter.containsKey(sample.getIdSample())) {
          continue;
        }
      }

      String assayKey = " ";
      if (idAssay != null && idAssay.intValue() != -1) {
        assayKey = DictionaryManager.getDisplay("hci.gnomex.model.Assay", idAssay.toString());
      } else if (idPrimer != null && idPrimer.intValue() != -1){
        assayKey = DictionaryManager.getDisplay("hci.gnomex.model.Primer", idPrimer.toString());
      }
      
      
      assayMap = requestMap.get(idRequest);
      if (assayMap == null) {
        assayMap = new TreeMap<String, List<Object[]>>();
        requestMap.put(idRequest, assayMap);
        Element requestNode = createRequestNode(row, dictionaryHelper);
        
        // First time we encounter a request, create a request node
        // from the row and hash it.
        requestNodeMap.put(idRequest, requestNode);
      }
      
      
      List<Object[]> results = assayMap.get(assayKey);
      if (results == null) {
        results = new ArrayList<Object[]>();
        assayMap.put(assayKey, results);
      }
      results.add(row);
    }
    
  }
  
  private void fillNodes(Element statusNode, DictionaryHelper dictionaryHelper) {
    Element parentNode = null;
    
    // Now create a request node for each key in the map
    // and create a well node child of the request node
    // for every row associated with the request.
    for(Iterator i = requestMap.keySet().iterator(); i.hasNext();) {
      Integer idRequest = (Integer)i.next();
      
      assayMap = requestMap.get(idRequest);
      Element requestNode = requestNodeMap.get(idRequest);
      statusNode.addContent(requestNode);

      // Now we will add either assay/primer nodes to this
      // request node or we will add well nodes (in the
      // case of capillary sequencing).
      parentNode = requestNode;
      
      int totalSampleCount = 0;
      for (Iterator i1 = assayMap.keySet().iterator(); i1.hasNext();) {
        String assayKey = (String)i1.next();
        List<Object[]> results = assayMap.get(assayKey);

        idPlatePrev = new Integer(-1);
        boolean firstTime = true;
        for (Object[]row : results) {
          if (firstTime && !assayKey.equals(" ")) {
            parentNode = createAssayNode(row, dictionaryHelper); 
            requestNode.addContent(parentNode);
          }
          if (firstTime) {
            firstTime = false;
            parentNode.setAttribute("sampleCount", Integer.valueOf(results.size()).toString());
          }
          addWellNode(row, dictionaryHelper, parentNode, results);
          totalSampleCount++;
        }
      }
      requestNode.setAttribute("sampleCount", Integer.valueOf(totalSampleCount).toString());
      
    }
    
  }
  
  
  private Element createRequestNode(Object[] row, DictionaryHelper dictionaryHelper) {
    
    Integer idRequest           = (Integer)row[0];
    String requestNumber        = (String)row[1]  == null ? ""  : (String)row[1];
    Date createDate             = (Date)row[4];
    Integer idLab               = (Integer)row[5];
    String labLastName          = (String)row[6]  == null ? ""  : (String)row[6];
    String labFirstName         = (String)row[7]  == null ? ""  : (String)row[7];

    String labName = Lab.formatLabName(labLastName, labFirstName);
    
    requestNode = new Element("Request");
    requestNode.setAttribute("idRequest",              idRequest.toString());
    requestNode.setAttribute("label",                  requestNumber);
    requestNode.setAttribute("requestSubmitDate",      createDate == null ? ""  : this.formatDate((java.sql.Date)createDate, this.DATE_OUTPUT_DASH));
    requestNode.setAttribute("idLab",                  idLab == null ? "" : idLab.toString());
    requestNode.setAttribute("lab",                    labName);
  
    return requestNode;
  
  }
  private Element createAssayNode(Object[] row, DictionaryHelper dictionaryHelper) {
    
    Integer idAssay             = (Integer)row[13];
    Integer idPrimer            = (Integer)row[14];

    Element n = null;
    String label = "";
    if (idAssay != null && idAssay.intValue() != -1) {
      n = new Element("Assay");
      n.setAttribute( "idAssay", idAssay.toString() );
      label = DictionaryManager.getDisplay("hci.gnomex.model.Assay", idAssay.toString());
    } else if (idPrimer != null && idPrimer.intValue() != -1){
      n = new Element("Primer");
      n.setAttribute( "idPrimer", idPrimer.toString() );
      label = DictionaryManager.getDisplay("hci.gnomex.model.Primer", idPrimer.toString());
    }
    n.setAttribute("label", label);
    
    return n;      
  }
  
  private void addWellNode(Object[] row, DictionaryHelper dictionaryHelper, Element parentNode, List<Object[]> results) {
    
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
    
    Integer idAssay             = (Integer)row[13];
    Integer idPrimer            = (Integer)row[14];
    Integer idPlate             = (Integer)row[15];
    String plateLabel           = (String)row[16]  == null ? ""  : (String)row[16];
    
    if (idPlate != null) {
      if (plateLabel == null || plateLabel.trim().equals("")) {
        plateLabel = idPlate.toString();
      }
    }
    
    Element wellParentNode = parentNode;
    
    RequestCategory requestCategory = dictionaryHelper.getRequestCategoryObject(codeRequestCategory);
    if (idPlate != null) {
      if (!idPlate.equals(idPlatePrev)) {
        plateNode = new Element("Plate");
        plateNode.setAttribute("label",        plateLabel);
        plateNode.setAttribute("idPlate",      idPlate != null ? idPlate.toString() : "");

        int sampleCount = getSampleCountForPlate(idPlate, results);
        plateNode.setAttribute("sampleCount", Integer.valueOf(sampleCount).toString());
        
        parentNode.addContent(plateNode);
        wellParentNode = plateNode;
      } else {
        wellParentNode = plateNode;
      }
    }
    Element n = new Element("Well");
    n.setAttribute("name",           sample.getName());
    n.setAttribute("idRequest",      idRequest != null ? idRequest.toString() : "");
    n.setAttribute("idLab",          idLab != null ? idLab.toString() : "");
    n.setAttribute("idSample",       sample.getIdSample().toString());
    n.setAttribute("isControl",      sample.getIsControl() != null ? sample.getIsControl() : "N"  );
    n.setAttribute("type",           requestCategory.getRequestCategory());
    n.setAttribute("row",            wellRow != null ? wellRow : "");
    n.setAttribute("col",            wellCol != null ? wellCol.toString() : "");
    n.setAttribute("index",          wellIndex != null ? wellIndex.toString() : "");
    n.setAttribute("idPlate",        idPlate != null ? idPlate.toString() : "");
    n.setAttribute("requestSubmitDate",  createDate == null ? ""  : this.formatDate((java.sql.Date)createDate, this.DATE_OUTPUT_DASH));
    n.setAttribute("requestSubmitter",   submitter != null ? submitter.getDisplayName() : "");
    
    if ( idAssay != null && idAssay.intValue() != -1 ) {
      n.setAttribute( "idAssay", idAssay.toString() );
      String assayLabel = DictionaryManager.getDisplay("hci.gnomex.model.Assay", idAssay.toString());
      n.setAttribute("label", assayLabel);
    }
    if ( idPrimer != null && idPrimer.intValue() != -1 ) {
      n.setAttribute( "idPrimer", idPrimer.toString() );
      String primerLabel = DictionaryManager.getDisplay("hci.gnomex.model.Primer", idPrimer.toString());
      n.setAttribute("label", primerLabel);
    }
    
    idPlatePrev = idPlate;
    
    wellParentNode.addContent(n);
  
  }
  
  private int getSampleCountForPlate(Integer theIdPlate, List<Object[]> results) {
    int sampleCount = 0;
    
    for (Object[] row : results) {
      Integer idPlate             = (Integer)row[15];
      
      if (idPlate.equals(theIdPlate)) {
        sampleCount++;
      }
    }
    return sampleCount;
  }
}