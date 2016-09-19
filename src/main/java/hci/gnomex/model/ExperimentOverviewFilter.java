package hci.gnomex.model;


import java.math.BigDecimal;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import hci.gnomex.security.SecurityAdvisor;
import hci.framework.model.DetailObject;

public class ExperimentOverviewFilter extends DetailObject {
  
  // Criteria / Argumented mx:request variables
  private String				workflow;
  private String				expType;
  private String				requestUser;
  private String				seqType;
  private String				seqLength;
  private String				seqInstrument;
  private String				experimentId;
  private String				lab;
  private String				coreFacilityId;
  
  private StringBuffer          queryBuf;
  private boolean               addWhere = true;
  private SecurityAdvisor       secAdvisor;
  
  public StringBuffer getQuery(SecurityAdvisor secAdvisor) {
    this.secAdvisor = secAdvisor;
    
    // Get Base query. 
    return getQuery();

  }

  private StringBuffer getQuery() {
    queryBuf = new StringBuffer();
    addWhere = true;
      
    addBaseColumns(queryBuf);
    addBaseQueryBody(queryBuf);
    addRequestCriteria();

    return queryBuf;
  }

  public void addBaseColumns(StringBuffer queryBuf) {
	  queryBuf.append("SELECT distinct ");
	  queryBuf.append(" s.idSample, s.name, s.barcodeSequence, s.seqPrepDate, ");
	  queryBuf.append(" req.codeApplication, req.number, req.createDate, req.codeRequestStatus, ");
	  queryBuf.append(" appU.firstName, appU.lastName, ");
	  queryBuf.append(" lab.idLab, lab.firstName, ");
	  queryBuf.append(" app.application, ");
	  queryBuf.append(" srt.seqRunType, ");
	  queryBuf.append(" nsc.numberSequencingCycles, ");
	  queryBuf.append(" ins.instrument, lab.lastName, ");
	  queryBuf.append(" wi.codeStepNext, req.idCoreFacility, s.number ");
   } 

  private void addBaseQueryBody(StringBuffer queryBuf) {
	  queryBuf.append(" FROM         Sample s ");
	  queryBuf.append(" JOIN    s.request as req ");
	  queryBuf.append(" JOIN         req.appUser appU ");
	  queryBuf.append(" LEFT JOIN    s.sequenceLanes l ");
	  queryBuf.append(" LEFT JOIN    req.lab lab ");
	  queryBuf.append(" LEFT JOIN    req.application app ");
	  queryBuf.append(" LEFT JOIN    l.seqRunType srt ");
	  queryBuf.append(" LEFT JOIN    l.numberSeqCycles nsc ");
	  queryBuf.append(" LEFT JOIN    l.flowCellChannel fcc ");
	  queryBuf.append(" LEFT JOIN    fcc.flowCell fc");
	  queryBuf.append(" LEFT JOIN    fc.instrument ins ");
	  queryBuf.append(" LEFT JOIN	 s.workItems wi ");
  }
  
  
  private void addRequestCriteria() {
    
    // Search by workflowStatus 
    if (workflow != null){
      this.addWhereOrAnd();
      queryBuf.append(" wi.codeStepNext = '");
      queryBuf.append(workflow);
      queryBuf.append("'");
    } 
    // Search by user 
    if (requestUser != null){
      this.addWhereOrAnd();
      queryBuf.append(" req.idAppUser = '");
      queryBuf.append(requestUser);
      queryBuf.append("'");
    } 
    //  Search by request number / Experiment ID
    if (experimentId != null && !experimentId.equals("")){
      this.addWhereOrAnd();
      queryBuf.append(" req.number = '");
      queryBuf.append(experimentId);
      queryBuf.append("'");
    }
    // Search by experiment type
    if(expType != null){
      this.addWhereOrAnd();
      queryBuf.append(" req.codeApplication = '");
      queryBuf.append(expType);
      queryBuf.append("'");
    }
    //  Search by sequencing type
    if(seqType != null){
    	this.addWhereOrAnd();
    	queryBuf.append(" srt.seqRunType = '");
    	queryBuf.append(seqType);
    	queryBuf.append("'");
    }
    //  Search by sequence length (Sequencing Cycles used)
    if(seqLength != null){
    	this.addWhereOrAnd();
    	queryBuf.append(" nsc.numberSequencingCycles = '");
    	queryBuf.append(seqLength);
    	queryBuf.append("'");
    }
    // Search by Instrument
    if(seqInstrument != null){
    	this.addWhereOrAnd();
    	queryBuf.append(" ins.instrument = '");
    	queryBuf.append(seqInstrument);
    	queryBuf.append("'");
    }
    // Search by LabId
    if(lab != null){
    	this.addWhereOrAnd();
    	queryBuf.append(" lab.idLab = '");
    	queryBuf.append(lab);
    	queryBuf.append("'");
    }
    
    // Filter by Core Facility Id
    if(coreFacilityId != null){
    	this.addWhereOrAnd();
    	queryBuf.append(" req.idCoreFacility = '");
    	queryBuf.append(coreFacilityId);
    	queryBuf.append("'");
    }
    //Only get past two months of requests
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.MONTH, -2);
    java.sql.Date lastMonth = new java.sql.Date(cal.getTimeInMillis());
    this.addWhereOrAnd();
    queryBuf.append(" req.createDate >= '");
    queryBuf.append(this.formatDate(lastMonth, this.DATE_OUTPUT_SQL));
    queryBuf.append("'");
    
  }

  
  
  protected boolean addWhereOrAnd() {
    if (addWhere) {
      queryBuf.append(" WHERE ");
      addWhere = false;
    } else {
      queryBuf.append(" AND ");
    }
    return addWhere;
  }

  
  public String getCodeStepNext() {
    return expType;
  }

  
  public void setCodeStepNext(String codeStepNext) {
    this.expType = codeStepNext;
  }

  
  public String getRequestNumber() {
    return experimentId;
  }

  
  public void setRequestNumber(String requestNumber) {
    this.experimentId = requestNumber;
  }

  public void setWorkflow(String workflow){
	  this.workflow = workflow;
  }
  
  public String getWorkflow(){
	  return workflow;
  }
  
  public void setRequestUser(String requestUser){
	  this.requestUser = requestUser;
  }
  
  public String getRequestUser(){
	  return requestUser;
  }
  
  public void setExpType(String expType){
	  this.expType = expType;
  }
  
  public String getExpType(){
	  return expType;
  }
  
  public void setSeqType(String seqtype){
	  this.seqType= seqtype;
  }
  
  public String getSeqType(){
	  return seqType;
  }
  
  public void setSeqLength(String seqLength){
	  this.seqLength = seqLength;
  }
  
  public String getSeqLength(){
	  return seqLength;
  }
  
  public void setSeqInstrument(String instrument){
	  this.seqInstrument = instrument;
  }
  
  public String getInstrument(){
	  return seqInstrument;
  }
  
  public void setExperimentId(String expID){
	  this.experimentId = expID;
  }
  
  public String getExperimentId(){
	  return experimentId;
  }
  
  public void setLab(String lab){
	  this.lab = lab;
  }
  
  public String getLab(){
	  return lab;
  }
  
  public String getCoreFacilityId() {
  	  return coreFacilityId;
  }

  public void setCoreFacilityId(String coreFacilityId) {
	  this.coreFacilityId = coreFacilityId;
  }
}
