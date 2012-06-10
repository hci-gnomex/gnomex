package hci.gnomex.model;


import java.util.Calendar;

import hci.framework.model.DetailObject;
import hci.gnomex.security.SecurityAdvisor;

public class ChromatogramFilter extends DetailObject {


  // Criteria
  private Integer               idChromatogram;
  private Integer               idRequest;
  
  private String                requestNumber;
  
  private String                getAll = "N";

  private String                lastWeek = "N";
  private String                lastMonth = "N";
  private String                lastThreeMonths = "N";
  private String                lastYear = "N";
  
  private String                released = "";
  
  private StringBuffer          queryBuf;
  private boolean               addWhere = true;
  private SecurityAdvisor       secAdvisor;


  public StringBuffer getQuery(SecurityAdvisor secAdvisor) {
    this.secAdvisor = secAdvisor;
    queryBuf = new StringBuffer();
    addWhere = true;

    queryBuf.append(" SELECT c.idChromatogram, ");
    queryBuf.append("        c.idPlateWell, ");
    queryBuf.append("        c.idRequest, ");
    queryBuf.append("        c.fileName, ");
    queryBuf.append("        c.displayName, ");
    queryBuf.append("        c.readLength, ");
    queryBuf.append("        c.trimmedLength, ");
    queryBuf.append("        c.q20, ");
    queryBuf.append("        c.q40, ");
    queryBuf.append("        c.aSignalStrength, ");
    queryBuf.append("        c.cSignalStrength, ");
    queryBuf.append("        c.gSignalStrength, ");
    queryBuf.append("        c.tSignalStrength, ");
    queryBuf.append("        c.releaseDate, ");
    queryBuf.append("        pw.row, ");
    queryBuf.append("        pw.col, ");
    queryBuf.append("        req.number, ");
    queryBuf.append("        sample.name, ");
    queryBuf.append("        submitter.firstName, ");
    queryBuf.append("        submitter.lastName, ");
    queryBuf.append("        run.idInstrumentRun, ");
    queryBuf.append("        run.label ");
    
    getQueryBody(queryBuf);

    return queryBuf;

  }

  public boolean hasSufficientCriteria(SecurityAdvisor secAdvisor) {
    this.secAdvisor = secAdvisor;
    boolean hasLimitingCriteria = false;
    if (idChromatogram != null ||
        idRequest != null ||
        requestNumber != null ||
        (lastWeek != null && lastWeek.equals("Y")) ||
        (lastMonth != null && lastMonth.equals("Y")) ||
        (lastThreeMonths != null && lastThreeMonths.equals("Y")) ||
        (lastYear != null && lastYear.equals("Y")) ||
        (released != null && !released.equals(""))  ||
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

    queryBuf.append(" FROM                Chromatogram as c ");
    
    queryBuf.append("LEFT JOIN    c.plateWell as pw ");
    queryBuf.append("LEFT JOIN    pw.plate as plate ");
    queryBuf.append("LEFT JOIN    plate.instrumentRun as run ");
    queryBuf.append("LEFT JOIN    pw.sample as sample ");
    queryBuf.append("LEFT JOIN    c.request as req ");
    queryBuf.append("LEFT JOIN    req.appUser as submitter ");

    addCriteria();
    
    queryBuf.append(" order by run.idInstrumentRun, plate.idPlate, pw.position, c.idRequest");
  }

  private void addCriteria() {

    // Search for chromat created in last week
    if (lastWeek.equals("Y")) {

      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.DAY_OF_YEAR, -7);
      java.sql.Date lastWeek = new java.sql.Date(cal.getTimeInMillis());

      this.addWhereOrAnd();
      queryBuf.append(" pw.createDate >= '");
      queryBuf.append(this.formatDate(lastWeek, this.DATE_OUTPUT_SQL));
      queryBuf.append("'");
    }
    // Search last month
    if (lastMonth.equals("Y")) {

      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.MONTH, -1);
      java.sql.Date lastMonth = new java.sql.Date(cal.getTimeInMillis());

      this.addWhereOrAnd();
      queryBuf.append(" pw.createDate >= '");
      queryBuf.append(this.formatDate(lastMonth, this.DATE_OUTPUT_SQL));
      queryBuf.append("'");
    }
    // Search last 3 months
    if (lastThreeMonths.equals("Y")) {

      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.MONTH, -3);
      java.sql.Date last3Month = new java.sql.Date(cal.getTimeInMillis());

      this.addWhereOrAnd();
      queryBuf.append(" pw.createDate >= '");
      queryBuf.append(this.formatDate(last3Month, this.DATE_OUTPUT_SQL));
      queryBuf.append("'");
    }
    // Search last year
    if (lastYear.equals("Y")) {

      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.YEAR, -1);
      java.sql.Date lastYear = new java.sql.Date(cal.getTimeInMillis());

      this.addWhereOrAnd();
      queryBuf.append(" pw.createDate >= '");
      queryBuf.append(this.formatDate(lastYear, this.DATE_OUTPUT_SQL));
      queryBuf.append("'");
    }    
    
    if (requestNumber != null) {
      this.addWhereOrAnd();
      
      String requestNumberBase = Request.getBaseRequestNumber(requestNumber);
      queryBuf.append(" (req.number like '" + requestNumberBase + "[0-9]' OR req.number = '" + requestNumberBase + "') ");
    }
    
    if (idChromatogram != null){
      this.addWhereOrAnd();
      queryBuf.append(" c.idChromatogram = ");
      queryBuf.append(idChromatogram);
    } 

    if (idRequest != null){
      this.addWhereOrAnd();
      queryBuf.append(" c.idRequest = ");
      queryBuf.append(idRequest);
    } 

    if (released != null && released.equals("Y")){
      this.addWhereOrAnd();
      queryBuf.append(" c.releaseDate is not null ");
    } 

    if (released != null && released.equals("N")){
      this.addWhereOrAnd();
      queryBuf.append(" c.releaseDate is null ");
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


  public Integer getIdChromatogram() {
    return idChromatogram;
  }


  public void setIdChromatogram(Integer idChromatogram) {
    this.idChromatogram = idChromatogram;
  }

  public Integer getIdRequest() {
    return idRequest;
  }


  public void setIdRequest(Integer idRequest) {
    this.idRequest = idRequest;
  }


  public String getRequestNumber()
  {
    return requestNumber;
  }

  public void setRequestNumber(String requestNumber)
  {
    this.requestNumber = requestNumber;
  }

  public String getGetAll()
  {
    return getAll;
  }

  public void setGetAll(String getAll)
  {
    this.getAll = getAll;
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

  public String getReleased()
  {
    return released;
  }

  public void setReleased(String released)
  {
    this.released = released;
  }
  


}
