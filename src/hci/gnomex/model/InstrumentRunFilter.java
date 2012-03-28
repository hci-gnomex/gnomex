package hci.gnomex.model;


import hci.framework.model.DetailObject;
import hci.gnomex.security.SecurityAdvisor;

import java.util.Calendar;

public class InstrumentRunFilter extends DetailObject {


  // Criteria
  private Integer               idInstrumentRun;

  private String                status;

  private String                lastWeek = "N";
  private String                lastMonth = "N";
  private String                lastThreeMonths = "N";
  private String                lastYear = "N";
  
  private StringBuffer          queryBuf;
  private boolean               addWhere = true;
  private SecurityAdvisor       secAdvisor;


  public StringBuffer getQuery(SecurityAdvisor secAdvisor) {
    this.secAdvisor = secAdvisor;
    queryBuf = new StringBuffer();
    addWhere = true;

    queryBuf.append(" SELECT ir.idInstrumentRun, ");
    queryBuf.append("        ir.runDate ");
    // Need to add Status to DB
    //    queryBuf.append("        ir.runDate, ");
    //    queryBuf.append("        ir.status ");

    getQueryBody(queryBuf);

    return queryBuf;

  }

  public boolean hasSufficientCriteria(SecurityAdvisor secAdvisor) {
    this.secAdvisor = secAdvisor;
    boolean hasLimitingCriteria = false;
    if (idInstrumentRun != null ||
        (status != null && !status.equals("")) ||
        (lastWeek != null && lastWeek.equals("Y")) ||
        (lastMonth != null && lastMonth.equals("Y")) ||
        (lastThreeMonths != null && lastThreeMonths.equals("Y")) ||
        (lastYear != null && lastYear.equals("Y"))) {
      hasLimitingCriteria = true;
    } else {
      hasLimitingCriteria = false;
    }

    if (secAdvisor.hasPermission(SecurityAdvisor.CAN_ACCESS_ANY_OBJECT)) {
      return hasLimitingCriteria;      
    } 
    return true;


  }

  public void getQueryBody(StringBuffer queryBuf) {

    queryBuf.append(" FROM                InstrumentRun as ir ");
    
    addRunCriteria();

    queryBuf.append(" order by ir.runDate");

  }

  private void addRunCriteria() {
    // Search by id 
    if (idInstrumentRun != null){
      this.addWhereOrAnd();
      queryBuf.append(" ir.idInstrumentRun =");
      queryBuf.append(idInstrumentRun);
    } 

    // Search by status
    if (status != null && !status.equals("")) {
      this.addWhereOrAnd();
      queryBuf.append("(");
      queryBuf.append(" ir.status like '%" + status + "%'");
      queryBuf.append(")");
    }

    // Search for instrument run created in last week
    if (lastWeek.equals("Y")) {

      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.DAY_OF_YEAR, -7);
      java.sql.Date lastWeek = new java.sql.Date(cal.getTimeInMillis());

      this.addWhereOrAnd();
      queryBuf.append(" ir.runDate >= '");
      queryBuf.append(this.formatDate(lastWeek, this.DATE_OUTPUT_SQL));
      queryBuf.append("'");
    }
    // Search for instrument run created in last month
    if (lastMonth.equals("Y")) {

      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.MONTH, -1);
      java.sql.Date lastMonth = new java.sql.Date(cal.getTimeInMillis());

      this.addWhereOrAnd();
      queryBuf.append(" ir.runDate >= '");
      queryBuf.append(this.formatDate(lastMonth, this.DATE_OUTPUT_SQL));
      queryBuf.append("'");
    }
    // Search for instrument run created in last 3 months
    if (lastThreeMonths.equals("Y")) {

      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.MONTH, -3);
      java.sql.Date last3Month = new java.sql.Date(cal.getTimeInMillis());

      this.addWhereOrAnd();
      queryBuf.append(" ir.runDate >= '");
      queryBuf.append(this.formatDate(last3Month, this.DATE_OUTPUT_SQL));
      queryBuf.append("'");
    }
    // Search for instrument run created in last year
    if (lastYear.equals("Y")) {

      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.YEAR, -1);
      java.sql.Date lastYear = new java.sql.Date(cal.getTimeInMillis());

      this.addWhereOrAnd();
      queryBuf.append(" ir.runDate >= '");
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

  protected boolean addWhereOrOr() {
    if (addWhere) {
      queryBuf.append(" WHERE ");
      addWhere = false;
    } else {
      queryBuf.append(" OR ");
    }
    return addWhere;
  }


  public Integer getIdInstrumentRun() {
    return idInstrumentRun;
  }


  public void setIdInstrumentRun(Integer idInstrumentRun) {
    this.idInstrumentRun = idInstrumentRun;
  }



  public String getStatus() {
    return status;
  }


  public void setStatus(String status) {
    this.status = status;
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
