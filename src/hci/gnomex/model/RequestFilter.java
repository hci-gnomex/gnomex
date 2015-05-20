package hci.gnomex.model;


import hci.framework.model.DetailObject;
import hci.gnomex.security.SecurityAdvisor;

import java.sql.Date;
import java.util.Calendar;

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

  private String                codeRequestCategory;
  private String                status;
  private Integer               idCoreFacility;
  
  private String                hasRedo = "N";
  
  private StringBuffer          queryBuf;
  private boolean               addWhere = true;
  private SecurityAdvisor       secAdvisor;
  
  
  public StringBuffer getQuery(SecurityAdvisor secAdvisor) {
    addWhere = true;
    this.secAdvisor = secAdvisor;
    queryBuf = new StringBuffer();
    
    queryBuf.append(" SELECT DISTINCT ");
    queryBuf.append(" req.idRequest, ");
    queryBuf.append(" req.number, ");
    queryBuf.append(" req.name, ");
    queryBuf.append(" req.description, ");
    queryBuf.append(" req.idSampleDropOffLocation, ");
    queryBuf.append(" req.codeRequestStatus, ");
    queryBuf.append(" req.codeRequestCategory, ");
    queryBuf.append(" req.createDate, ");
    queryBuf.append(" submitter.firstName, ");
    queryBuf.append(" submitter.lastName, ");
    queryBuf.append(" lab.firstName, ");
    queryBuf.append(" lab.lastName, ");
    queryBuf.append(" req.idAppUser, ");
    queryBuf.append(" req.idLab, ");
    queryBuf.append(" req.idCoreFacility, ");
    queryBuf.append(" req.corePrepInstructions, ");
    queryBuf.append(" count(sample.idSample) ");
    
    getQueryBody(queryBuf);

    queryBuf.append(" GROUP BY ");
    queryBuf.append(" req.idRequest, ");
    queryBuf.append(" req.number, ");
    queryBuf.append(" req.name, ");
    queryBuf.append(" req.description, ");
    queryBuf.append(" req.idSampleDropOffLocation, ");
    queryBuf.append(" req.codeRequestStatus, ");
    queryBuf.append(" req.codeRequestCategory, ");
    queryBuf.append(" req.createDate, ");
    queryBuf.append(" submitter.firstName, ");
    queryBuf.append(" submitter.lastName, ");
    queryBuf.append(" lab.firstName, ");
    queryBuf.append(" lab.lastName, ");
    queryBuf.append(" req.idAppUser, ");
    queryBuf.append(" req.idLab, ");
    queryBuf.append(" req.idCoreFacility, ");
    queryBuf.append(" req.corePrepInstructions ");
    
    queryBuf.append(" order by req.idRequest ");

    return queryBuf;
    
  }

  public StringBuffer getReactionPlateQuery(SecurityAdvisor secAdvisor) {
    addWhere = true;
    this.secAdvisor = secAdvisor;
    queryBuf = new StringBuffer();
    
    queryBuf.append(" SELECT DISTINCT ");
    queryBuf.append(" req.idRequest, ");
    queryBuf.append(" plate.label, ");
    queryBuf.append(" run.idInstrumentRun, ");
    queryBuf.append(" run.label ");
    
    getReactionPlateQueryBody(queryBuf);
    
    return queryBuf;
    
  }
  
  public StringBuffer getSourcePlateQuery(SecurityAdvisor secAdvisor) {
    addWhere = true;
    this.secAdvisor = secAdvisor;
    queryBuf = new StringBuffer();
    
    queryBuf.append( "SELECT distinct req.idRequest, plate.idPlate ");
    
    getSourcePlateQueryBody(queryBuf);
    
    return queryBuf;
    
  }
  
  
  public void getQueryBody(StringBuffer queryBuf) {
    
    queryBuf.append(" FROM        Request as req ");
    queryBuf.append(" JOIN        req.samples as sample ");
    queryBuf.append(" JOIN        req.submitter as submitter ");
    queryBuf.append(" JOIN        req.lab as lab ");
    
    if (hasRedo.equals("Y")) {
      queryBuf.append(" JOIN        sample.wells as well ");
    }
    queryBuf.append(" LEFT JOIN   req.collaborators as collab ");
    
    addRequestCriteria();
    addWellCriteria();
    addSecurityCriteria();
    
  
  }
  

  public void getReactionPlateQueryBody(StringBuffer queryBuf) {
    
    queryBuf.append(" FROM        Request as req ");
    queryBuf.append(" JOIN        req.samples as sample ");
    queryBuf.append(" JOIN        sample.wells as well ");
    queryBuf.append(" JOIN        well.plate as plate ");
    queryBuf.append(" LEFT JOIN   plate.instrumentRun as run ");
    queryBuf.append(" LEFT JOIN   req.collaborators as collab ");
    
    addRequestCriteria();
    addReactionPlateCriteria();
    addSecurityCriteria();
  }
  

  public void getSourcePlateQueryBody(StringBuffer queryBuf) {

    queryBuf.append(" FROM        Request as req ");
    queryBuf.append(" JOIN        req.samples as sample ");
    queryBuf.append(" JOIN        sample.wells as well ");
    queryBuf.append(" JOIN        well.plate as plate ");
    
    addRequestCriteria();
    addSourcePlateCriteria();
    addSecurityCriteria();
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
    if (codeRequestCategory != null && !codeRequestCategory.equals("")){
      this.addWhereOrAnd();
      queryBuf.append(" req.codeRequestCategory like '");
      queryBuf.append(codeRequestCategory);
      queryBuf.append("%'");
    } 
    if (idCoreFacility != null){
      this.addWhereOrAnd();
      queryBuf.append(" req.idCoreFacility = ");
      queryBuf.append(idCoreFacility);
    } 
  }
  
  private void addWellCriteria() {
    // Search by redoFlag
    if (hasRedo.equals("Y")){
      this.addWhereOrAnd();
      queryBuf.append("(");
      queryBuf.append(" well.redoFlag = 'Y'");
      queryBuf.append(")");
    }    
  } 

  private void addReactionPlateCriteria() {
      this.addWhereOrAnd();
      queryBuf.append(" plate.codePlateType = '" + PlateType.REACTION_PLATE_TYPE + "' ");
  }   
  
  private void addSourcePlateCriteria() {
    this.addWhereOrAnd();
    queryBuf.append(" plate.codePlateType = '" + PlateType.SOURCE_PLATE_TYPE + "' ");
    
  }
  private void addSecurityCriteria() {
    secAdvisor.buildSecurityCriteria(queryBuf, "req", "collab", addWhere, false, true);
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

  
  public String getCodeRequestCategory() {
    return codeRequestCategory;
  }

  
  public void setCodeRequestCategory( String codeRequestCategory ) {
    this.codeRequestCategory = codeRequestCategory;
  }

  public String getStatus() {
    return status;
  }

  
  public void setStatus( String status ) {
    this.status = status;
  }

  public String getHasRedo() {
    return hasRedo;
  }

  public void setHasRedo(String hasRedo) {
    this.hasRedo = hasRedo;
  }

  public Integer getIdCoreFacility() {
    return idCoreFacility;
  }
  
  public void setIdCoreFacility(Integer id) {
    idCoreFacility = id;
  }

  
  
}
