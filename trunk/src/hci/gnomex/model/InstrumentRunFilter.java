package hci.gnomex.model;


import hci.framework.model.DetailObject;
import hci.gnomex.security.SecurityAdvisor;

import java.util.Calendar;

public class InstrumentRunFilter extends DetailObject {


  // Criteria
  private Integer               idInstrumentRun;

  private String                getAll = "N";

  private String                status;

  private String                runLastWeek = "N";
  private String                runLastMonth = "N";
  private String                runLastThreeMonths = "N";
  private String                runLastYear = "N";

  private String                createdLastWeek = "N";
  private String                createdLastMonth = "N";
  private String                createdLastThreeMonths = "N";
  private String                createdLastYear = "N";

  private StringBuffer          queryBuf;
  private boolean               addWhere = true;
  private SecurityAdvisor       secAdvisor;


  public StringBuffer getQuery(SecurityAdvisor secAdvisor) {
    this.secAdvisor = secAdvisor;
    queryBuf = new StringBuffer();
    addWhere = true;

    queryBuf.append(" SELECT ir.idInstrumentRun, ");
    queryBuf.append("        ir.runDate, ");
    queryBuf.append("        ir.createDate, ");
    queryBuf.append("        ir.codeInstrumentRunStatus, ");
    queryBuf.append("        ir.comments, ");
    queryBuf.append("        ir.label, ");
    queryBuf.append("        ir.codeReactionType, ");
    queryBuf.append("        ir.creator, ");
    queryBuf.append("        ir.codeSealType ");

    getQueryBody(queryBuf);

    return queryBuf;

  }

  public boolean hasSufficientCriteria(SecurityAdvisor secAdvisor) {
    this.secAdvisor = secAdvisor;
    boolean hasLimitingCriteria = false;
    if (idInstrumentRun != null ||
        (status != null && !status.equals("")) ||
        (runLastWeek != null && runLastWeek.equals("Y")) ||
        (runLastMonth != null && runLastMonth.equals("Y")) ||
        (runLastThreeMonths != null && runLastThreeMonths.equals("Y")) ||
        (runLastYear != null && runLastYear.equals("Y")) ||
        (createdLastWeek != null && createdLastWeek.equals("Y")) ||
        (createdLastMonth != null && createdLastMonth.equals("Y")) ||
        (createdLastThreeMonths != null && createdLastThreeMonths.equals("Y")) ||
        (createdLastYear != null && createdLastYear.equals("Y"))  ||
        (getAll != null && getAll.equals("Y"))) {
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
    
    if (! getAll.equals("Y")) {
      addRunCriteria();
    }

    queryBuf.append(" order by ir.createDate");

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
      queryBuf.append(" ir.codeInstrumentRunStatus like '%" + status + "%'");
      queryBuf.append(")");
    }

    // --------------------------
    // Search by run date
    //---------------------------
    // Search for instrument run that ran in last week
    if (runLastWeek.equals("Y")) {

      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.DAY_OF_YEAR, -7);
      java.sql.Date lastWeek = new java.sql.Date(cal.getTimeInMillis());

      this.addWhereOrAnd();
      queryBuf.append(" ir.runDate >= '");
      queryBuf.append(this.formatDate(lastWeek, this.DATE_OUTPUT_SQL));
      queryBuf.append("'");
    }
    // Search for instrument run that ran in last month
    if (runLastMonth.equals("Y")) {

      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.MONTH, -1);
      java.sql.Date lastMonth = new java.sql.Date(cal.getTimeInMillis());

      this.addWhereOrAnd();
      queryBuf.append(" ir.runDate >= '");
      queryBuf.append(this.formatDate(lastMonth, this.DATE_OUTPUT_SQL));
      queryBuf.append("'");
    }
    // Search for instrument run that ran in last 3 months
    if (runLastThreeMonths.equals("Y")) {

      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.MONTH, -3);
      java.sql.Date last3Month = new java.sql.Date(cal.getTimeInMillis());

      this.addWhereOrAnd();
      queryBuf.append(" ir.runDate >= '");
      queryBuf.append(this.formatDate(last3Month, this.DATE_OUTPUT_SQL));
      queryBuf.append("'");
    }
    // Search for instrument run that ran in last year
    if (runLastYear.equals("Y")) {

      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.YEAR, -1);
      java.sql.Date lastYear = new java.sql.Date(cal.getTimeInMillis());

      this.addWhereOrAnd();
      queryBuf.append(" ir.runDate >= '");
      queryBuf.append(this.formatDate(lastYear, this.DATE_OUTPUT_SQL));
      queryBuf.append("'");
    }    

    // --------------------------
    // Search by create date
    //---------------------------
    // Search for instrument run created in last week
    if (createdLastWeek.equals("Y")) {

      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.DAY_OF_YEAR, -7);
      java.sql.Date lastWeek = new java.sql.Date(cal.getTimeInMillis());

      this.addWhereOrAnd();
      queryBuf.append(" ir.createDate >= '");
      queryBuf.append(this.formatDate(lastWeek, this.DATE_OUTPUT_SQL));
      queryBuf.append("'");
    }
    // Search for instrument run created in last month
    if (createdLastMonth.equals("Y")) {

      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.MONTH, -1);
      java.sql.Date lastMonth = new java.sql.Date(cal.getTimeInMillis());

      this.addWhereOrAnd();
      queryBuf.append(" ir.createDate >= '");
      queryBuf.append(this.formatDate(lastMonth, this.DATE_OUTPUT_SQL));
      queryBuf.append("'");
    }
    // Search for instrument run created in last 3 months
    if (createdLastThreeMonths.equals("Y")) {

      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.MONTH, -3);
      java.sql.Date last3Month = new java.sql.Date(cal.getTimeInMillis());

      this.addWhereOrAnd();
      queryBuf.append(" ir.createDate >= '");
      queryBuf.append(this.formatDate(last3Month, this.DATE_OUTPUT_SQL));
      queryBuf.append("'");
    }
    // Search for instrument run created in last year
    if (createdLastYear.equals("Y")) {

      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.YEAR, -1);
      java.sql.Date lastYear = new java.sql.Date(cal.getTimeInMillis());

      this.addWhereOrAnd();
      queryBuf.append(" ir.createDate >= '");
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


  public String getGetAll()
  {
    return getAll;
  }

  public void setGetAll(String getAll)
  {
    this.getAll = getAll;
  }

  public String getRunLastWeek() {
    return runLastWeek;
  }


  public void setRunLastWeek(String lastWeek) {
    this.runLastWeek = lastWeek;
  }


  public String getRunLastMonth() {
    return runLastMonth;
  }


  public void setRunLastMonth(String lastMonth) {
    this.runLastMonth = lastMonth;
  }


  public String getRunLastThreeMonths() {
    return runLastThreeMonths;
  }


  public void setRunLastThreeMonths(String lastThreeMonths) {
    this.runLastThreeMonths = lastThreeMonths;
  }


  public String getRunLastYear() {
    return runLastYear;
  }


  public void setRunLastYear(String lastYear) {
    this.runLastYear = lastYear;
  }

  public String getCreatedLastWeek()
  {
    return createdLastWeek;
  }

  public void setCreatedLastWeek(String createdLastWeek)
  {
    this.createdLastWeek = createdLastWeek;
  }

  public String getCreatedLastMonth()
  {
    return createdLastMonth;
  }

  public void setCreatedLastMonth(String createdLastMonth)
  {
    this.createdLastMonth = createdLastMonth;
  }

  public String getCreatedLastThreeMonths()
  {
    return createdLastThreeMonths;
  }

  public void setCreatedLastThreeMonths(String createdLastThreeMonths)
  {
    this.createdLastThreeMonths = createdLastThreeMonths;
  }

  public String getCreatedLastYear()
  {
    return createdLastYear;
  }

  public void setCreatedLastYear(String createdLastYear)
  {
    this.createdLastYear = createdLastYear;
  }

}
