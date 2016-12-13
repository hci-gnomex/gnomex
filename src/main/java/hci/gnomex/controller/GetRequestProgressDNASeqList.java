package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.InstrumentRunStatus;
import hci.gnomex.model.PlateType;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.model.RequestProgressDNASeqFilter;
import hci.gnomex.model.RequestStatus;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.apache.log4j.Logger;

public class GetRequestProgressDNASeqList extends GNomExCommand implements Serializable {
  
  private static Logger LOG = Logger.getLogger(GetRequestProgressSolexaList.class);
  
  private RequestProgressDNASeqFilter filter;
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    filter = new RequestProgressDNASeqFilter();
    HashMap errors = this.loadDetailObject(request, filter);
    this.addInvalidFields(errors);
    
    
    if (this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_ACCESS_ANY_OBJECT) && !filter.hasCriteria()) {
      this.addInvalidField("filterRequired", "Please enter at least one search criterion");
    }
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      
   
      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
      DictionaryHelper dictionaryHelper = DictionaryHelper.getInstance(sess);

      Map<Integer, java.util.Date> chromoMap = getChromoMap(sess, dictionaryHelper);

      Document doc = new Document(new Element("RequestProgressList"));
      
      StringBuffer buf = filter.getDNASeqQuery(this.getSecAdvisor(), dictionaryHelper);
      LOG.info(buf.toString());
      List rows = (List)sess.createQuery(buf.toString()).list();
      TreeMap<String, RowContainer> rowMap = new TreeMap<String, RowContainer>();
      for(Iterator i = rows.iterator(); i.hasNext(); ) {
        RowContainer container = new RowContainer((Object[])i.next());
        rowMap.put(container.getCompareKey(), container);
      }
      
      RowContainer prevContainer = null;
      Element currentNode = null;
      boolean alt = false;
      boolean firstForWell = true;
      Status previousStatus = null;
      for(Iterator i = rowMap.keySet().iterator(); i.hasNext(); ) {
        String key = (String)i.next();
        RowContainer container = rowMap.get(key);
        if (prevContainer != null && !prevContainer.getRequestNumber().equals(container.getRequestNumber())) {
          alt = !alt;
        }
        if (!container.isSameWell(prevContainer)) {
          if (currentNode != null) {
            doc.getRootElement().addContent(currentNode);
          }
          firstForWell = true;
          previousStatus = null;
          RequestCategory requestCategory = dictionaryHelper.getRequestCategoryObject(container.getCodeRequestCategory());
          currentNode = new Element("RequestProgress");
          currentNode.setAttribute("isSeletected",          "N");
          currentNode.setAttribute("altColor",              new Boolean(alt).toString());
          currentNode.setAttribute("showRequestNumber",     prevContainer == null || !container.getRequestNumber().equals(prevContainer.getRequestNumber()) ? "Y" : "N");       
          currentNode.setAttribute("requestNumber",         container.getRequestNumber());
          currentNode.setAttribute("createDate",            container.getCreateDate() != null ? this.formatDate(container.getCreateDate()) : "");
          currentNode.setAttribute("codeRequestCategory",   container.getCodeRequestCategory());
          currentNode.setAttribute("appUserName",           container.getRequestor()!= null ? container.getRequestor() : "");
          currentNode.setAttribute("sampleName",            container.getSampleName() == null ? "" : container.getSampleName());
          currentNode.setAttribute("sampleNumber",          container.getSampleNumber() == null ? "" : container.getSampleNumber());
          currentNode.setAttribute("assay",                 container.getAssayOrPrimerName());
          currentNode.setAttribute("icon",                  requestCategory != null && requestCategory.getIcon() != null ? requestCategory.getIcon() : "");
          currentNode.setAttribute("type",                  requestCategory != null && requestCategory.getType() != null ? requestCategory.getType() : "");
        }
        if (container.getPlateType().equals(PlateType.SOURCE_PLATE_TYPE)) {
          currentNode.setAttribute("sourceWell",            container.getWellName());
          currentNode.setAttribute("plateName",             container.getPlateLabel() == null ? "" : container.getPlateLabel());
        }
        previousStatus = container.getEffectiveWellStatus(chromoMap, previousStatus);
        currentNode.setAttribute("status",                previousStatus.getPhrase());
        firstForWell = false;
        prevContainer = container;
      }
      if (prevContainer != null) {
        doc.getRootElement().addContent(currentNode);
      }
    
      XMLOutputter out = new org.jdom.output.XMLOutputter();
      this.xmlResult = out.outputString(doc);
    
      setResponsePage(this.SUCCESS_JSP);
    } catch (Exception e) {
      LOG.error("An exception has occurred in GetRequestProgressList ", e);
      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        //closeReadOnlyHibernateSession;        
      } catch(Exception e){
        LOG.error("Error", e);
      }
    }
    
    return this;
  }

  private Map<Integer, java.util.Date> getChromoMap(Session sess, DictionaryHelper dictionaryHelper) {
    HashMap<Integer, java.util.Date> chromoMap = new HashMap<Integer, java.util.Date>();
    String queryString = filter.getDNASeqChromoQuery(this.getSecAdvisor(), dictionaryHelper).toString();
    Query query = sess.createQuery(queryString);
    List results = query.list();
    // Saves release date of newest release associated with the plate well.  Note important now, but maybe in the future.
    for(Iterator i = results.iterator(); i.hasNext(); ) {
      Object[] row = (Object[])i.next();
      Integer idPlateWell = (Integer)row[RequestProgressDNASeqFilter.CHROMO_ID_PLATE_WELL];
      java.util.Date releaseDate = (java.util.Date)row[RequestProgressDNASeqFilter.CHROMO_RELEASE_DATE];
      if (chromoMap.containsKey(idPlateWell)) {
        java.util.Date oldReleaseDate = chromoMap.get(idPlateWell);
        if(oldReleaseDate == null || oldReleaseDate.before(releaseDate)) {
          chromoMap.put(idPlateWell, releaseDate);
        }
      } else {
        chromoMap.put(idPlateWell, releaseDate);
      }
    }
    
    return chromoMap;
  }
  
  public static class  RowContainer {
    private Integer           idRequest;
    private java.util.Date    createDate;
    private String            requestNumber;
    private Integer           requestIdAppUser;
    private String            sampleNumber;
    private String            sampleName;
    private Integer           idLab;
    private String            codeRequestCategory;
    private String            codeRequestStatus;
    private String            ownerFirstName;
    private String            ownerLastName;
    private String            codePlateType;
    private String            plateLabel;
    private String            wellRedoFlag;
    private String            wellIsControl;
    private java.util.Date    wellCreateDate;
    private Integer           idPlateWell;
    private String            wellRow;
    private Integer           wellCol;
    private Integer           idPrimer;
    private String            primerName;
    private Integer           idAssay;
    private String            assayName;
    private String            runStatus;

    public RowContainer(Object[] row) {
      idRequest = (Integer)row[RequestProgressDNASeqFilter.REQ_ID_REQUEST];
      createDate = (java.util.Date)row[RequestProgressDNASeqFilter.REQ_CREATE_DATE];
      requestNumber = (String)row[RequestProgressDNASeqFilter.REQ_NUMBER];
      requestIdAppUser = (Integer)row[RequestProgressDNASeqFilter.REQ_ID_APP_USER];
      sampleNumber = (String)row[RequestProgressDNASeqFilter.SAMPLE_NUMBER];
      sampleName = (String)row[RequestProgressDNASeqFilter.SAMPLE_NAME];
      idLab = (Integer)row[RequestProgressDNASeqFilter.REQ_ID_LAB];
      codeRequestCategory = (String)row[RequestProgressDNASeqFilter.REQ_CODE_REQUEST_CATEGORY];
      codeRequestStatus = (String)row[RequestProgressDNASeqFilter.REQ_CODE_REQUEST_STATUS];
      ownerFirstName = (String)row[RequestProgressDNASeqFilter.REQOWNER_FIRST_NAME];
      ownerLastName = (String)row[RequestProgressDNASeqFilter.REQOWNER_LAST_NAME];
      codePlateType = (String)row[RequestProgressDNASeqFilter.PLATE_CODE_PLATE_TYPE];
      plateLabel = (String)row[RequestProgressDNASeqFilter.PLATE_LABEL];
      wellRedoFlag = (String)row[RequestProgressDNASeqFilter.WELL_REDO_FLAG];
      wellIsControl = (String)row[RequestProgressDNASeqFilter.WELL_IS_CONTROL];
      idPlateWell = (Integer)row[RequestProgressDNASeqFilter.WELL_ID_PLATEWELL];
      wellRow = (String)row[RequestProgressDNASeqFilter.WELL_ROW];
      wellCol = (Integer)row[RequestProgressDNASeqFilter.WELL_COL];
      if (row[RequestProgressDNASeqFilter.WELL_CREATE_DATE] == null) {
        wellCreateDate = null;
      } else {
        wellCreateDate = (java.util.Date)row[RequestProgressDNASeqFilter.WELL_CREATE_DATE];
      }
      idPrimer = (Integer)row[RequestProgressDNASeqFilter.PRIMER_ID_PRIMER];
      primerName = (String)row[RequestProgressDNASeqFilter.PRIMER_NAME];
      idAssay = (Integer)row[RequestProgressDNASeqFilter.ASSAY_ID_ASSAY];
      assayName = (String)row[RequestProgressDNASeqFilter.ASSAY_NAME];
      runStatus = (String)row[RequestProgressDNASeqFilter.RUN_CODE_INSTRUMENT_RUN_STATUS];
    }
    
    public String getRequestNumber() {
      return requestNumber;
    }
    public java.util.Date getCreateDate() {
      return createDate;
    }
    public String getCodeRequestCategory() {
      return codeRequestCategory;
    }
    public String getRequestor() {
      String requestor = "";
      if (ownerLastName != null) {
        requestor += ownerLastName + ", ";
      }
      if (ownerFirstName != null) {
        requestor += ownerFirstName;
      }
      return requestor;
    }
    public String getPlateLabel() {
      return plateLabel;
    }
    public String getSampleName() {
      return sampleName;
    }
    public String getPlateType() {
      if (this.codePlateType == null || this.codePlateType.equals(PlateType.SOURCE_PLATE_TYPE)) {
        return PlateType.SOURCE_PLATE_TYPE;
      } else {
        return PlateType.REACTION_PLATE_TYPE;
      }
    }
    public String getWellName() {
      if (wellRow != null && wellCol != null) {
        return wellRow + wellCol.toString();
      } else {
        return "";
      }
    }
    public String getAssayOrPrimerName() {
      if (assayName != null) {
        return assayName;
      }
      if (primerName != null) {
        return primerName;
      }
      return "";
    }
    public String getSampleNumber() {
      return sampleNumber;
    }
    public java.util.Date getWellCreateDate() {
      return wellCreateDate;
    }
    public boolean isSameWell(RowContainer prev) {
      boolean res = false;
      if (prev != null) {
        if (prev.getRequestNumber().equals(getRequestNumber())) {
          if (prev.getSampleNumber().equals(getSampleNumber())) {
            if (prev.getAssayOrPrimerName().equals(getAssayOrPrimerName())) {
              res = true;
            }
          }
        }
      }
     
      return res;
    }
    
    public String getCompareKey() {
      // Makes sure sorted by request, sample, assay or primer, well creation.
      String key = requestNumber;
      String[] sampleTokens = sampleNumber.split("X");
      Integer sampleSequence = Integer.parseInt(sampleTokens[sampleTokens.length - 1]) + 10000;
      Integer idPlateWellLeadingZeroes = idPlateWell + 100000000;
      Calendar cal = Calendar.getInstance();
      if (wellCreateDate == null) {
        cal.setTimeInMillis(0);
      } else {
        cal.setTime(wellCreateDate);
      }
      key += "\t" + sampleSequence.toString();
      key += "\t" + getAssayOrPrimerName();
      key += "\t" + new Integer(cal.get(Calendar.YEAR)).toString();
      key += "\t" + new Integer(cal.get(Calendar.MONTH)+100).toString();
      key += "\t" + new Integer(cal.get(Calendar.DAY_OF_MONTH)+100).toString();
      key += "\t" + idPlateWellLeadingZeroes.toString();
      
      return key;
    }
    
    public Status getWellStatus(Map<Integer, java.util.Date> chromoMap) {
      Status status = Status.PROCESSING;
      if (this.codePlateType == null || this.codePlateType.equals(PlateType.SOURCE_PLATE_TYPE)) {
        if (this.wellRedoFlag.equals("Y")) {
          status = Status.REDO;
        } else if (codeRequestStatus == null || codeRequestStatus.equals(RequestStatus.NEW)) {
          status = Status.NEW;
        } else {
          status = Status.SUBMITTED;
        }
      } else if (this.codePlateType != null && this.runStatus == null) {
        status = Status.ONPLATE;
      } else if (this.runStatus != null) {
        if (chromoMap.containsKey(this.idPlateWell)) {
          status = Status.COMPLETE;
        } else if (this.runStatus.equals(InstrumentRunStatus.COMPLETE)) {
          status = Status.COMPLETE;
        } else {
          status = Status.PROCESSING;
        }
      }

      return status;
    }
    
    public Status getEffectiveWellStatus(Map<Integer, java.util.Date> chromoMap, Status previousStatus) {
      // Redo trumps, but otherwise get status from reaction well most recently created.
      Status status = previousStatus;
      if (previousStatus == null) {
        status = getWellStatus(chromoMap);
      } else if (!previousStatus.equals(Status.REDO)) {
        status = getWellStatus(chromoMap);
      }
      return status;
    }
  }
 
  private enum Status {
    NEW(0, "New"),
    SUBMITTED(1, "Submitted"),
    ONPLATE(2, "On Plate"),
    PROCESSING(3, "Processing"),
    COMPLETE(4, "Complete"),
    REDO(5, "Redo");
    
    private final Integer level;
    private final String phrase;
    
    Status(Integer level, String phrase) {
      this.level = level;
      this.phrase = phrase;
    }
    
    public Integer getLevel() {
      return level;
    }
    
    public String getPhrase() {
      return phrase;
    }
  }
}
