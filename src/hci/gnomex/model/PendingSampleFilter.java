package hci.gnomex.model;


import java.util.Calendar;
import java.util.Set;
import java.util.Iterator;

import hci.framework.model.DetailObject;
import hci.gnomex.security.SecurityAdvisor;

public class PendingSampleFilter extends DetailObject {
  
  
  // Criteria
  private String               requestNumber;
  private Integer              idLab;
  private String               lastWeek = "N";
  private String               lastMonth = "N";
  private String               lastThreeMonths = "N";
  private String               lastYear = "N";
  
  
  private StringBuffer         queryBuf;
  private boolean              addWhere = true;
  
  public boolean hasCriteria() {
    if ((requestNumber == null || requestNumber.equals("")) ||
        idLab != null ||
        (lastWeek == null || lastWeek.equals("")) ||
        (lastMonth == null || lastMonth.equals("")) ||
        (lastThreeMonths == null || lastThreeMonths.equals("")) ||
        (lastYear == null || lastYear.equals(""))) {
      return false;
    } else {
      return true;
    }
  
  }

  public StringBuffer getRedoQuery() {
    addWhere = true;
    queryBuf = new StringBuffer();
    
    queryBuf.append(" SELECT     req.idRequest, ");
    queryBuf.append("            req.number, ");
    queryBuf.append("            req.codeRequestStatus, ");
    queryBuf.append("            req.codeRequestCategory, ");
    queryBuf.append("            req.createDate, ");
    queryBuf.append("            lab.idLab, ");
    queryBuf.append("            lab.lastName, ");
    queryBuf.append("            lab.firstName, ");
    queryBuf.append("            appUser, ");
    queryBuf.append("            sample ");
    
    queryBuf.append(" FROM       Request as req ");
    queryBuf.append(" JOIN       req.lab as lab ");
    queryBuf.append(" LEFT JOIN  req.appUser as appUser ");
    queryBuf.append(" LEFT JOIN  req.samples as sample ");
    
    queryBuf.append(" WHERE sample.redoFlag = 'Y' ");
    

    addWhere = false;
    
    addRequestCriteria();
    
    queryBuf.append(" ORDER BY req.createDate, req.idRequest ");

    return queryBuf;
    
  }
  
  public StringBuffer getPendingQuery() {
    addWhere = true;
    queryBuf = new StringBuffer();
    
    queryBuf.append(" SELECT     req.idRequest, ");
    queryBuf.append("            req.number, ");
    queryBuf.append("            req.codeRequestStatus, ");
    queryBuf.append("            req.codeRequestCategory, ");
    queryBuf.append("            req.createDate, ");
    queryBuf.append("            lab.idLab, ");
    queryBuf.append("            lab.lastName, ");
    queryBuf.append("            lab.firstName, ");
    queryBuf.append("            appUser, ");
    queryBuf.append("            sample ");
    
    queryBuf.append(" FROM       Request as req ");
    queryBuf.append(" JOIN       req.lab as lab ");
    queryBuf.append(" LEFT JOIN  req.appUser as appUser ");
    queryBuf.append(" LEFT JOIN  req.samples as sample ");
    
    queryBuf.append(" WHERE (sample.redoFlag != 'Y' AND req.codeRequestStatus = '" + RequestStatus.SUBMITTED + "') ");
    

    addWhere = false;
    
    addRequestCriteria();
    
    queryBuf.append(" ORDER BY req.createDate, req.idRequest ");
   

    return queryBuf;
    
  }
  

  private void addRequestCriteria() {

    // Search by lab 
    if (idLab != null){
      this.addWhereOrAnd();
      queryBuf.append(" req.idLab =");
      queryBuf.append(idLab);
    } 
    // Search by request number 
    if (requestNumber != null && 
        !requestNumber.equals("")){
      this.addWhereOrAnd();
      
      String requestNumberBase = Request.getBaseRequestNumber(requestNumber);
      //queryBuf.append(" (req.number like '" + requestNumberBase + "[0-9]' OR req.number = '" + requestNumberBase + "') ");
      queryBuf.append(" (req.number like '" + requestNumberBase + "[0-9]' OR req.number = '" + requestNumberBase + "' OR req.number like '" + requestNumberBase + "R[0-9]' OR req.number = '" + requestNumberBase + "R') ");
    }     
    
    
    // Search for requests submitted in last week
    if (lastWeek.equals("Y")) {

      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.DAY_OF_YEAR, -7);
      java.sql.Date lastWeek = new java.sql.Date(cal.getTimeInMillis());
      
      this.addWhereOrAnd();
      queryBuf.append(" req.createDate >= '");
      queryBuf.append(this.formatDate(lastWeek, this.DATE_OUTPUT_SQL));
      queryBuf.append("'");
    }
    // Search for requests submitted in last month
    if (lastMonth.equals("Y")) {

      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.MONTH, -1);
      java.sql.Date lastMonth = new java.sql.Date(cal.getTimeInMillis());
      
      this.addWhereOrAnd();
      queryBuf.append(" req.createDate >= '");
      queryBuf.append(this.formatDate(lastMonth, this.DATE_OUTPUT_SQL));
      queryBuf.append("'");
    }
    // Search for requests submitted in last 3 months
    if (lastThreeMonths.equals("Y")) {

      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.MONTH, -3);
      java.sql.Date last3Month = new java.sql.Date(cal.getTimeInMillis());
      
      this.addWhereOrAnd();
      queryBuf.append(" req.createDate >= '");
      queryBuf.append(this.formatDate(last3Month, this.DATE_OUTPUT_SQL));
      queryBuf.append("'");
    }
    // Search for requests submitted in last year
    if (lastYear.equals("Y")) {

      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.YEAR, -1);
      java.sql.Date lastYear = new java.sql.Date(cal.getTimeInMillis());
      
      this.addWhereOrAnd();
      queryBuf.append(" req.createDate >= '");
      queryBuf.append(this.formatDate(lastYear, this.DATE_OUTPUT_SQL));
      queryBuf.append("'");
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

  
  public String getRequestNumber() {
    return requestNumber;
  }

  
  public void setRequestNumber(String requestNumber) {
    this.requestNumber = requestNumber;
  }


  public Integer getIdLab() {
    return idLab;
  }
  
  
  public void setIdLab(Integer idLab) {
    this.idLab = idLab;
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

  
  
}
