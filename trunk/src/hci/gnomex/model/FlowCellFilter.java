package hci.gnomex.model;


import hci.framework.model.DetailObject;
import hci.gnomex.security.SecurityAdvisor;

import java.util.Date;

public class FlowCellFilter extends DetailObject {
  
  
  // Criteria
  private String                codeSequencingPlatform;
  private String                flowCellNumber;
  private String                requestNumber;
  private Date					createDateFrom;
  private Date					createDateTo;
  private String				codeStepNext;
  private SecurityAdvisor       secAdvisor;
  
  private StringBuffer          queryBuf;
  private boolean              	addWhere = true;
  
  
  public boolean hasSufficientCriteria(SecurityAdvisor secAdvisor) {
	    this.secAdvisor = secAdvisor;
	    boolean hasLimitingCriteria = false;
	    
	    if ((flowCellNumber != null && !flowCellNumber.equals("")) ||
	    	(requestNumber != null && !requestNumber.equals("")) ||
	    	(createDateFrom != null && createDateTo != null)) {
	    	hasLimitingCriteria = true;
	    } else {
	    	hasLimitingCriteria = false;
	    }

	    if (this.secAdvisor.hasPermission(SecurityAdvisor.CAN_ACCESS_ANY_OBJECT)) {
	    	return hasLimitingCriteria;      
	    } else {
	    	return true;
	    }
  }
  
  public StringBuffer getQuery(SecurityAdvisor secAdvisor) {
    
    queryBuf = new StringBuffer();
    
    queryBuf.append(" SELECT distinct fc ");
    
    getQueryBody(queryBuf, secAdvisor);
    
    return queryBuf;
    
  }

  public void getQueryBody(StringBuffer queryBuf, SecurityAdvisor secAdvisor) {
    if(codeStepNext != null && codeStepNext != "") {
      queryBuf.append(" FROM WorkItem as wi ");
      queryBuf.append(" JOIN wi.flowCellChannel as ch ");
      queryBuf.append(" JOIN ch.flowCell as fc ");
      if (this.hasRequestCriteria()) {
        queryBuf.append(" JOIN   ch.sequenceLanes as lane ");
        queryBuf.append(" JOIN   lane.request as req ");      
      }	    
      String[] codeStepNextArr = codeStepNext.split(",");
      codeStepNext = "";
      for(String s : codeStepNextArr) {
        codeStepNext += "'" + s + "',";
      }
      codeStepNext = codeStepNext.substring(0, codeStepNext.length()-1);
      queryBuf.append(" WHERE wi.codeStepNext IN (" + codeStepNext + ") " );
      
      addWhere = false;		
    } else {
      queryBuf.append(" FROM        FlowCell as fc ");
      if (this.hasRequestCriteria()) {
        queryBuf.append(" JOIN   fc.flowCellChannels as ch ");
        queryBuf.append(" JOIN   ch.sequenceLanes as lane ");
        queryBuf.append(" JOIN   lane.request as req ");      
      }	    
      addWhere = true;
      addFlowCellCriteria();
      addRequestCriteria();
    }

    if (!secAdvisor.hasPermission(SecurityAdvisor.CAN_ADMINISTER_ALL_CORE_FACILITIES)) {
      queryBuf.append(" AND ");
      secAdvisor.appendCoreFacilityCriteria(queryBuf, "fc");
      queryBuf.append(" ");
    }
    
    queryBuf.append(" order by fc.createDate desc, fc.number");
  }
  
  private boolean hasRequestCriteria() {
    if (this.requestNumber != null && !this.requestNumber.equals("")) {
      return true;
    } else {
      return false;
    }
  }
  

  private void addFlowCellCriteria() {
    // Search by sequencing platform
    if (codeSequencingPlatform != null && !codeSequencingPlatform.equals("")) {
      this.addWhereOrAnd();
      queryBuf.append(" fc.codeSequencingPlatform = '");
      queryBuf.append(codeSequencingPlatform);
      queryBuf.append("'");
    }    
    
    // Search by flowCellNumber
    if (flowCellNumber != null && !flowCellNumber.equals("")) {
      this.addWhereOrAnd();
      queryBuf.append(" fc.number = '");
      queryBuf.append(flowCellNumber);
      queryBuf.append("'");
    }
    
    // Search by create date
    if (createDateFrom != null) {
    	this.addWhereOrAnd();
        queryBuf.append(" fc.createDate >= '");
        queryBuf.append(this.formatDate(createDateFrom, this.DATE_OUTPUT_SQL));
        queryBuf.append("'");
    }
    if (createDateTo != null) {
    	this.addWhereOrAnd();
        queryBuf.append(" fc.createDate < '");
        queryBuf.append(this.formatDate(createDateTo, this.DATE_OUTPUT_SQL));
        queryBuf.append("'");
    }

  }
  

  private void addRequestCriteria() {
    // Search by request number
    if (requestNumber != null && !requestNumber.equals("")) {
      this.addWhereOrAnd();
      String requestNumberBase = Request.getBaseRequestNumber(requestNumber);
      queryBuf.append(" (req.number like '" + requestNumberBase + "[0-9]' OR req.number = '" + requestNumberBase + "') ");
    }    
    
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

  
  public String getFlowCellNumber() {
    return flowCellNumber;
  }

  
  public void setFlowCellNumber(String flowCellNumber) {
    this.flowCellNumber = flowCellNumber;
  }

  
  public String getRequestNumber() {
    return requestNumber;
  }

  
  public void setRequestNumber(String requestNumber) {
    this.requestNumber = requestNumber;
  }

  
  public String getCodeSequencingPlatform() {
    return codeSequencingPlatform;
  }

  
  public void setCodeSequencingPlatform(String codeSequencingPlatform) {
    this.codeSequencingPlatform = codeSequencingPlatform;
  }

  public String getCodeStepNext() {
	  return codeStepNext;
  }
  
  public void setCodeStepNext(String codeStepNext) {
	  this.codeStepNext = codeStepNext;
  }

  public Date getCreateDateFrom() {
	return createDateFrom;
  }

  public void setCreateDateFrom(Date createDateFrom) {
    this.createDateFrom = createDateFrom;
  }
  
  public Date getCreateDateTo() {
	return createDateTo;
  }

  public void setCreateDateTo(Date createDateTo) {
	this.createDateTo = createDateTo;
  }
  
}
