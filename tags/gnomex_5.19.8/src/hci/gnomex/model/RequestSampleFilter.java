package hci.gnomex.model;


import hci.framework.model.DetailObject;
import hci.gnomex.security.SecurityAdvisor;

import java.sql.Date;
import java.util.Calendar;

public class RequestSampleFilter extends DetailObject {
  
  
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

  private boolean               isCreateReport = false;
  private StringBuffer          queryBuf;
  private boolean               addWhere = true;
  private SecurityAdvisor       secAdvisor;
  
  public static final int       COL_LAB_LAST_NAME = 0;
  public static final int       COL_LAB_FIRST_NAME = 1;
  public static final int       COL_SUBMITTER_LAST_NAME = 2;
  public static final int       COL_SUBMITTER_FIRST_NAME = 3;
  public static final int       COL_ID_REQUEST = 4;
  public static final int       COL_REQUEST_NUMBER = 5;
  public static final int       COL_CODE_REQUEST_CATEGORY = 6;
  public static final int       COL_ID_SAMPLE = 7;
  public static final int       COL_SAMPLE_NUMBER = 8;
  public static final int       COL_SAMPLE_NAME = 9;
  public static final int       COL_SAMPLE_DESCRIPTION = 10;
  public static final int       COL_SAMPLE_ID_ORGANISM = 11;
  public static final int       COL_CODE_APPLICATION = 12;

  public static final int       PROPCOL_ID_SAMPLE = 2;
  public static final int       COL_ID_PROPERTY = 3;
  public static final int       COL_PROPERTY_TYPE = 4;
  public static final int       COL_PROPERTY_NAME = 5;
  public static final int       COL_PROPERTY_VALUE = 6;
  public static final int       COL_PROPERTY_MULTI_VALUE = 7;
  public static final int       COL_PROPERTY_OPTION = 8;

  
  public StringBuffer getQuery(SecurityAdvisor secAdvisor){
    return getQuery(secAdvisor, false);
  }
  
  public StringBuffer getQuery(SecurityAdvisor secAdvisor, Boolean isCreateReport) {
    addWhere = true;
    this.secAdvisor = secAdvisor;
    queryBuf = new StringBuffer();
    
    queryBuf.append(" SELECT DISTINCT ");
    queryBuf.append(" lab.lastName, ");
    queryBuf.append(" lab.firstName, ");
    queryBuf.append(" submitter.lastName, ");
    queryBuf.append(" submitter.firstName, ");
    queryBuf.append(" req.idRequest, ");
    queryBuf.append(" req.number, ");
    queryBuf.append(" req.codeRequestCategory, ");
    queryBuf.append(" sample.idSample, ");
    queryBuf.append(" sample.number, ");
    queryBuf.append(" sample.name, ");
    queryBuf.append(" sample.description, ");
    queryBuf.append(" sample.idOrganism, ");
    queryBuf.append(" req.codeApplication ");
    
    if(isCreateReport){
      this.isCreateReport = true;
    }
    getQueryBody(queryBuf);

    queryBuf.append(" order by lab.lastName, lab.firstName, req.idRequest, sample.idSample ");

    return queryBuf;
    
  }

  public StringBuffer getAnnotationQuery(SecurityAdvisor secAdvisor){
    return getAnnotationQuery(secAdvisor, false);
  }
  
  public StringBuffer getAnnotationQuery(SecurityAdvisor secAdvisor, Boolean isCreateReport) {
    addWhere = true;
    this.secAdvisor = secAdvisor;
    queryBuf = new StringBuffer();
    
    queryBuf.append(" SELECT DISTINCT ");
    queryBuf.append(" req.idRequest, ");
    queryBuf.append(" req.number, ");
    queryBuf.append(" sample.idSample, ");
    queryBuf.append(" prop.idProperty, ");
    queryBuf.append(" prop.codePropertyType, ");
    queryBuf.append(" prop.name, ");
    queryBuf.append(" propEntry.value, ");
    queryBuf.append(" value.value, ");
    queryBuf.append(" option.option ");
    
    if(isCreateReport){
      this.isCreateReport = true;
    }
    getAnnotationQueryBody(queryBuf);
    
    return queryBuf;
    
  }
  
  public StringBuffer getSampleFileLinkQuery(SecurityAdvisor secAdvisor, Boolean isCreateReport) {
    addWhere = true;
    this.secAdvisor = secAdvisor;
    queryBuf = new StringBuffer();
    
    queryBuf.append(" SELECT DISTINCT ");
    queryBuf.append(" lab.lastName, ");
    queryBuf.append(" lab.firstName, ");
    queryBuf.append(" submitter.lastName, ");
    queryBuf.append(" submitter.firstName, ");
    queryBuf.append(" req.idRequest, ");
    queryBuf.append(" req.number, ");
    queryBuf.append(" req.codeRequestCategory, ");
    queryBuf.append(" sample.idSample ");

    
    if(isCreateReport){
      this.isCreateReport = true;
    }
    getSampleFileLinkQueryBody(queryBuf);
    
    return queryBuf;
    
  }

  
  public void getQueryBody(StringBuffer queryBuf) {
    
    queryBuf.append(" FROM        Request as req ");
    queryBuf.append(" JOIN        req.samples as sample ");
    queryBuf.append(" JOIN        req.submitter as submitter ");
    queryBuf.append(" JOIN        req.lab as lab ");
    queryBuf.append(" LEFT JOIN   req.collaborators as collab ");
    
    addRequestCriteria();
    if(isCreateReport){
      filterByExcludeUsage();
    }
    addSecurityCriteria();
    
  }
  

  public void getSampleFileLinkQueryBody(StringBuffer queryBuf) {
    
    queryBuf.append(" FROM        Request as req ");
    queryBuf.append(" JOIN        req.samples as sample ");
    queryBuf.append(" JOIN        req.submitter as submitter ");
    queryBuf.append(" JOIN        req.lab as lab ");
    queryBuf.append(" LEFT JOIN   req.collaborators as collab ");
    queryBuf.append(" JOIN        sample.sampleExperimentFiles as sf ");
    
    addRequestCriteria();
    if(isCreateReport){
      filterByExcludeUsage();
    }
    addSampleExperimentFileCriteria();
    addSecurityCriteria();
  }

  
  public void getAnnotationQueryBody(StringBuffer queryBuf) {
    
    queryBuf.append(" FROM        Request as req ");
    queryBuf.append(" JOIN        req.samples as sample ");
    queryBuf.append(" JOIN        req.submitter as submitter ");
    queryBuf.append(" JOIN        req.lab as lab ");
    queryBuf.append(" LEFT JOIN   req.collaborators as collab ");
    queryBuf.append(" JOIN        sample.propertyEntries as propEntry ");
    queryBuf.append(" JOIN        propEntry.property as prop ");
    queryBuf.append(" LEFT JOIN   propEntry.values as value ");
    queryBuf.append(" LEFT JOIN   propEntry.options as option ");
    
    addRequestCriteria();
    if(isCreateReport){
      filterByExcludeUsage();
    }
    addSecurityCriteria();
  }
  
  private void filterByExcludeUsage(){
    //If getting lab info for all labs don't include labs with excludeUsage == 'Y'
    if(idLab == null){
      this.addWhereOrAnd();
      queryBuf.append(" lab.excludeUsage != 'Y' ");
    }
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
  }
  
  private void addSampleExperimentFileCriteria() {
    this.addWhereOrAnd();
    queryBuf.append("  (sf.idExpFileRead1 != NULL or sf.idExpFileRead2 != NULL)");
  }
  
  
  private void addSecurityCriteria() {
    this.addWhere = secAdvisor.buildSecurityCriteria(queryBuf, "req", "collab", addWhere, false, true);
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

  
}