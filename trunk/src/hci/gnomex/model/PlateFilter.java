package hci.gnomex.model;


import java.util.Calendar;

import hci.framework.model.DetailObject;
import hci.gnomex.security.SecurityAdvisor;

public class PlateFilter extends DetailObject {


  // Criteria
  private Integer               idPlate;
  private Integer               idInstrumentRun;
  
  private String                getAll = "N";
  private String                notAddedToARun = "N";

  private String                status;
  private String                plateType;
  private String                codeReactionType;

  private StringBuffer          queryBuf;
  private boolean               addWhere = true;
  private SecurityAdvisor       secAdvisor;
  
  private String                createdLastWeek = "N";
  private String                createdLastMonth = "N";
  private String                createdLastThreeMonths = "N";
  private String                createdLastYear = "N";


  public StringBuffer getQuery(SecurityAdvisor secAdvisor) {
    this.secAdvisor = secAdvisor;
    queryBuf = new StringBuffer();
    addWhere = true;

    queryBuf.append(" SELECT p.idPlate, ");
    queryBuf.append("        p.idInstrumentRun, ");
    queryBuf.append("        p.quadrant, ");
    queryBuf.append("        p.createDate, ");
    queryBuf.append("        p.comments, ");
    queryBuf.append("        p.label, ");
    queryBuf.append("        p.codeReactionType, ");
    queryBuf.append("        p.creator, ");
    queryBuf.append("        p.codeSealType, ");
    queryBuf.append("        p.codePlateType ");

    getQueryBody(queryBuf);

    return queryBuf;

  }

  public boolean hasSufficientCriteria(SecurityAdvisor secAdvisor) {
    this.secAdvisor = secAdvisor;
    boolean hasLimitingCriteria = false;
    if (idPlate != null ||
        idInstrumentRun != null ||
        (status != null && !status.equals("")) ||
        (plateType != null && !plateType.equals("")) ||
        (codeReactionType != null && !codeReactionType.equals("")) ||
        (getAll != null && getAll.equals("Y")) ||
        (createdLastWeek != null && createdLastWeek.equals("Y")) ||
        (createdLastMonth != null && createdLastMonth.equals("Y")) ||
        (createdLastThreeMonths != null && createdLastThreeMonths.equals("Y")) ||
        (createdLastYear != null && createdLastYear.equals("Y"))  ||
        (notAddedToARun != null && notAddedToARun.equals("Y"))) {
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

    queryBuf.append(" FROM                Plate as p ");
    
    if (!getAll.equals("Y")) {
      addCriteria();
    }
    
    queryBuf.append(" order by p.createDate");

  }

  private void addCriteria() {

    if (idPlate != null){
      this.addWhereOrAnd();
      queryBuf.append(" p.idPlate =");
      queryBuf.append(idPlate);
    } 

    if (idInstrumentRun != null){
      this.addWhereOrAnd();
      queryBuf.append(" p.idInstrumentRun =");
      queryBuf.append(idInstrumentRun);
    } 

    // Search by plate type
    if (plateType != null && !plateType.equals("")) {
      this.addWhereOrAnd();
      queryBuf.append("(");
      queryBuf.append(" p.codePlateType like '%" + plateType + "%'");
      queryBuf.append(")");
    }
    
    // Search by reaction type
    if (codeReactionType != null && !codeReactionType.equals("")) {
      this.addWhereOrAnd();
      queryBuf.append("(");
      queryBuf.append(" p.codeReactionType like '%" + codeReactionType + "%'");
      queryBuf.append(")");
    }
    
    // Search by not added to run
    if (notAddedToARun.equals("Y")) {
      this.addWhereOrAnd();
      queryBuf.append("(");
      queryBuf.append(" p.idInstrumentRun is null");
      queryBuf.append(")");
    }
    
    if (createdLastWeek.equals("Y")) {

      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.DAY_OF_YEAR, -7);
      java.sql.Date lastWeek = new java.sql.Date(cal.getTimeInMillis());

      this.addWhereOrAnd();
      queryBuf.append(" p.createDate >= '");
      queryBuf.append(this.formatDate(lastWeek, this.DATE_OUTPUT_SQL));
      queryBuf.append("'");
    }
    // Search for instrument run created in last month
    if (createdLastMonth.equals("Y")) {

      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.MONTH, -1);
      java.sql.Date lastMonth = new java.sql.Date(cal.getTimeInMillis());

      this.addWhereOrAnd();
      queryBuf.append(" p.createDate >= '");
      queryBuf.append(this.formatDate(lastMonth, this.DATE_OUTPUT_SQL));
      queryBuf.append("'");
    }
    // Search for instrument run created in last 3 months
    if (createdLastThreeMonths.equals("Y")) {

      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.MONTH, -3);
      java.sql.Date last3Month = new java.sql.Date(cal.getTimeInMillis());

      this.addWhereOrAnd();
      queryBuf.append(" p.createDate >= '");
      queryBuf.append(this.formatDate(last3Month, this.DATE_OUTPUT_SQL));
      queryBuf.append("'");
    }
    // Search for instrument run created in last year
    if (createdLastYear.equals("Y")) {

      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.YEAR, -1);
      java.sql.Date lastYear = new java.sql.Date(cal.getTimeInMillis());

      this.addWhereOrAnd();
      queryBuf.append(" p.createDate >= '");
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


  public Integer getIdPlate() {
    return idPlate;
  }


  public void setIdPlate(Integer idPlate) {
    this.idPlate = idPlate;
  }

  public Integer getIdInstrumentRun() {
    return idInstrumentRun;
  }


  public void setIdInstrumentRun(Integer idInstrumentRun) {
    this.idInstrumentRun = idInstrumentRun;
  }


  public String getGetAll()
  {
    return getAll;
  }

  public void setGetAll(String getAll)
  {
    this.getAll = getAll;
  }

  public String getStatus() {
    return status;
  }


  public void setStatus(String status) {
    this.status = status;
  }

  
  public String getPlateType() {
    return plateType;
  }

  
  public void setPlateType( String plateType ) {
    this.plateType = plateType;
  }

  
  public String getCodeReactionType() {
    return codeReactionType;
  }

  
  public void setCodeReactionType( String codeReactionType ) {
    this.codeReactionType = codeReactionType;
  }

  public String getNotAddedToARun()
  {
    return notAddedToARun;
  }

  public void setNotAddedToARun(String notAddedToARun)
  {
    this.notAddedToARun = notAddedToARun;
  }
  
  public String getCreatedLastWeek() {
    return createdLastWeek;
  }

  public void setCreatedLastWeek(String createdLastWeek) {
    this.createdLastWeek = createdLastWeek;
  }

  public String getCreatedLastMonth() {
    return createdLastMonth;
  }

  public void setCreatedLastMonth(String createdLastMonth) {
    this.createdLastMonth = createdLastMonth;
  }

  public String getCreatedLastThreeMonths() {
    return createdLastThreeMonths;
  }

  public void setCreatedLastThreeMonths(String createdLastThreeMonths) {
    this.createdLastThreeMonths = createdLastThreeMonths;
  }

  public String getCreatedLastYear() {
    return createdLastYear;
  }

  public void setCreatedLastYear(String createdLastYear) {
    this.createdLastYear = createdLastYear;
  }
  
  public String getCodePlateType() {
    return plateType;
  }

  public void setCodePlateType(String codePlateType) {
    this.plateType = codePlateType;
  }

}
