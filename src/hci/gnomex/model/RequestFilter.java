package hci.gnomex.model;


import java.sql.Date;
import java.util.Calendar;

import hci.gnomex.security.SecurityAdvisor;
import hci.framework.model.DetailObject;

public class RequestFilter extends DetailObject {
  
  
  // Criteria
  private String                number;
  private Integer               idAppUser;
  private Integer               idLab;
  private Date                  createDateFrom;
  private Date                  createDateTo;
  

  private String                lastWeek = "N";
  private String                lastMonth = "N";
  private String                lastThreeMonths = "N";
  private String                lastYear = "N";

  
  private String                status;
  
  private StringBuffer          queryBuf;
  private boolean               addWhere = true;
  private SecurityAdvisor       secAdvisor;
  
  
  public StringBuffer getQuery(SecurityAdvisor secAdvisor) {
    this.secAdvisor = secAdvisor;
    queryBuf = new StringBuffer();
    
    queryBuf.append(" SELECT req");
    
    getQueryBody(queryBuf);
    
    return queryBuf;
    
  }
  
  public void getQueryBody(StringBuffer queryBuf) {
    
    queryBuf.append(" FROM        Request as req ");
    queryBuf.append(" LEFT JOIN   req.collaborators as collab ");
    
    addRequestCriteria();
    addSecurityCriteria();
    
    queryBuf.append(" order by req.number");
  
  }
  
  

  private void addRequestCriteria() {
    // Search by request number 
    if (number != null && !number.equals("")){
      this.addWhereOrAnd();
      queryBuf.append(" req.number like '");
      queryBuf.append(number);
      queryBuf.append("%'");
    } 
    // Search by lab 
    if (idLab != null){
      this.addWhereOrAnd();
      queryBuf.append(" req.idLab =");
      queryBuf.append(idLab);
    } 
    // Search by user 
    if (idAppUser != null){
      this.addWhereOrAnd();
      queryBuf.append(" req.idAppUser = ");
      queryBuf.append(idAppUser);
    } 
    //  Search by create date from 
    if (createDateFrom != null){
      this.addWhereOrAnd();
      queryBuf.append(" req.createDate >= '");
      queryBuf.append(this.formatDate(createDateFrom, this.DATE_OUTPUT_SQL));
      queryBuf.append("'");
    } 
    //  Search by create date from 
    if (createDateTo != null){
      this.addWhereOrAnd();
      queryBuf.append(" req.createDate <= '");
      queryBuf.append(this.formatDate(createDateTo, this.DATE_OUTPUT_SQL));
      queryBuf.append("'");
    } 
    // Search last week
    if (lastWeek.equals("Y")) {

      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.DAY_OF_YEAR, -7);
      java.sql.Date lastWeek = new java.sql.Date(cal.getTimeInMillis());

      this.addWhereOrAnd();
      queryBuf.append(" req.createDate >= '");
      queryBuf.append(this.formatDate(lastWeek, this.DATE_OUTPUT_SQL));
      queryBuf.append("'");
    }
    // Search last month
    if (lastMonth.equals("Y")) {

      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.MONTH, -1);
      java.sql.Date lastMonth = new java.sql.Date(cal.getTimeInMillis());

      this.addWhereOrAnd();
      queryBuf.append(" req.createDate >= '");
      queryBuf.append(this.formatDate(lastMonth, this.DATE_OUTPUT_SQL));
      queryBuf.append("'");
    }
    // Search last 3 months
    if (lastThreeMonths.equals("Y")) {

      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.MONTH, -3);
      java.sql.Date last3Month = new java.sql.Date(cal.getTimeInMillis());

      this.addWhereOrAnd();
      queryBuf.append(" req.createDate >= '");
      queryBuf.append(this.formatDate(last3Month, this.DATE_OUTPUT_SQL));
      queryBuf.append("'");
    }
    // Search last year
    if (lastYear.equals("Y")) {

      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.YEAR, -1);
      java.sql.Date lastYear = new java.sql.Date(cal.getTimeInMillis());

      this.addWhereOrAnd();
      queryBuf.append(" req.createDate >= '");
      queryBuf.append(this.formatDate(lastYear, this.DATE_OUTPUT_SQL));
      queryBuf.append("'");
    }    
    // Search by request status 
    if (status != null && !status.equals("")){
      this.addWhereOrAnd();
      queryBuf.append(" req.codeRequestStatus like '");
      queryBuf.append(status);
      queryBuf.append("%'");
    } 
  }
  
  private void addSecurityCriteria() {
    secAdvisor.buildSecurityCriteria(queryBuf, "req", "collab", addWhere, false);
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

  
  public Integer getIdLab() {
    return idLab;
  }

  
  public Integer getIdUser() {
    return idAppUser;
  }

  
  public String getNumber() {
    return number;
  }

  
  public void setIdLab(Integer idLab) {
    this.idLab = idLab;
  }

  
  public void setIdUser(Integer idAppUser) {
    this.idAppUser = idAppUser;
  }

  
  public void setNumber(String number) {
    this.number = number;
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

  
  
  public String getLastWeek() {
    return lastWeek;
  }

  
  public void setLastWeek( String lastWeek ) {
    this.lastWeek = lastWeek;
  }

  
  public String getLastMonth() {
    return lastMonth;
  }

  
  public void setLastMonth( String lastMonth ) {
    this.lastMonth = lastMonth;
  }

  
  public String getLastThreeMonths() {
    return lastThreeMonths;
  }

  
  public void setLastThreeMonths( String lastThreeMonths ) {
    this.lastThreeMonths = lastThreeMonths;
  }

  
  public String getLastYear() {
    return lastYear;
  }

  
  public void setLastYear( String lastYear ) {
    this.lastYear = lastYear;
  }

  public String getStatus() {
    return status;
  }

  
  public void setStatus( String status ) {
    this.status = status;
  }



  
  
}
