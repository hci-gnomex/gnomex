package hci.gnomex.model;


import hci.framework.model.DetailObject;

import java.util.Calendar;

public class FlowCellFilter extends DetailObject {
  
  
  // Criteria
  private String                codeSequencingPlatform;
  private String                flowCellNumber;
  private String                requestNumber;
  private String                lastWeek = "N";
  private String                lastMonth = "N";
  private String                lastThreeMonths = "N";
  private String                lastYear = "N";
  
  
  private StringBuffer          queryBuf;
  private boolean              addWhere = true;
  
  
  
  public StringBuffer getQuery() {
    
    queryBuf = new StringBuffer();
    
    queryBuf.append(" SELECT fc ");
    
    getQueryBody(queryBuf);
    
    return queryBuf;
    
  }
  
  public void getQueryBody(StringBuffer queryBuf) {
    
    queryBuf.append(" FROM        FlowCell as fc ");
    if (this.hasRequestCriteria()) {
      queryBuf.append(" JOIN   fc.flowCellChannels as ch ");
      queryBuf.append(" JOIN   ch.sequenceLanes as lane ");
      queryBuf.append(" JOIN   lane.request as req ");      
    }  
    
    addWhere = true;
    addFlowCellCriteria();
    addRequestCriteria();
    
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
    
    // Search for analysis created in last week
    if (lastWeek.equals("Y")) {

      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.DAY_OF_YEAR, -7);
      java.sql.Date lastWeek = new java.sql.Date(cal.getTimeInMillis());
      
      this.addWhereOrAnd();
      queryBuf.append(" fc.createDate >= '");
      queryBuf.append(this.formatDate(lastWeek, this.DATE_OUTPUT_SQL));
      queryBuf.append("'");
    }
    // Search for analysis created in last month
    if (lastMonth.equals("Y")) {

      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.MONTH, -1);
      java.sql.Date lastMonth = new java.sql.Date(cal.getTimeInMillis());
      
      this.addWhereOrAnd();
      queryBuf.append(" fc.createDate >= '");
      queryBuf.append(this.formatDate(lastMonth, this.DATE_OUTPUT_SQL));
      queryBuf.append("'");
    }
    // Search for analysis created in last 3 months
    if (lastThreeMonths.equals("Y")) {

      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.MONTH, -3);
      java.sql.Date last3Month = new java.sql.Date(cal.getTimeInMillis());
      
      this.addWhereOrAnd();
      queryBuf.append(" fc.createDate >= '");
      queryBuf.append(this.formatDate(last3Month, this.DATE_OUTPUT_SQL));
      queryBuf.append("'");
    }
    // Search for analysis created in last year
    if (lastYear.equals("Y")) {

      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.YEAR, -1);
      java.sql.Date lastYear = new java.sql.Date(cal.getTimeInMillis());
      
      this.addWhereOrAnd();
      queryBuf.append(" fc.createDate >= '");
      queryBuf.append(this.formatDate(lastYear, this.DATE_OUTPUT_SQL));
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


  
  public String getLastWeek() {
    return lastWeek;
  }

  
  public void setLastWeek(String lastWeek) {
    this.lastWeek = lastWeek;
  }

  
  public String getLastMonth() {
    return lastMonth;
  }

  
  public void setLastMonth(String lastMonth) {
    this.lastMonth = lastMonth;
  }

  
  public String getLastThreeMonths() {
    return lastThreeMonths;
  }

  
  public void setLastThreeMonths(String lastThreeMonths) {
    this.lastThreeMonths = lastThreeMonths;
  }

  
  public String getLastYear() {
    return lastYear;
  }

  
  public void setLastYear(String lastYear) {
    this.lastYear = lastYear;
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

  


  
 

  
}
