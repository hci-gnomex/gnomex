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
  private String               codeRequestCategory = RequestCategory.CAPILLARY_SEQUENCING_REQUEST_CATEGORY;
  private String               lastWeek = "N";
  private String               lastMonth = "N";
  private String               lastThreeMonths = "N";
  private String               lastYear = "N";
  
  
  private StringBuffer         queryBuf;
  private boolean              addWhere = true;
  
  public boolean hasRequiredCriteria() {
    if (codeRequestCategory == null || codeRequestCategory.equals("")) {
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
    queryBuf.append("            sample, ");
    queryBuf.append("            well.row, ");
    queryBuf.append("            well.col, ");
    queryBuf.append("            well.position, ");
    queryBuf.append("            well.idAssay, ");
    queryBuf.append("            well.idPrimer, ");
    queryBuf.append("            plate.idPlate, ");
    queryBuf.append("            plate.label ");
    
    
    queryBuf.append(" FROM       Request as req ");
    queryBuf.append(" JOIN       req.lab as lab ");
    queryBuf.append(" JOIN       req.appUser as appUser ");
    queryBuf.append(" JOIN       req.samples as sample ");
    queryBuf.append(" JOIN       sample.wells as well ");
    queryBuf.append(" LEFT JOIN  well.plate plate ");
    
    queryBuf.append(" WHERE well.redoFlag = 'Y' ");
    queryBuf.append(" AND   (well.idPlate is NULL or plate.codePlateType = '" + PlateType.SOURCE_PLATE_TYPE + "') "); 


    addWhere = false;
    
    addRequestCriteria();
    
    queryBuf.append(" ORDER BY well.idAssay, well.idPrimer, req.createDate, req.idRequest, plate.idPlate, well.position ");

    return queryBuf;
    
  }
  
  public StringBuffer getPendingSamplesQuery() {
    addWhere = true;
    queryBuf = new StringBuffer();
    
    // Get all samples that are in tubes (well with no idPlate) or plates
    queryBuf.append(" SELECT     req.idRequest, ");
    queryBuf.append("            req.number, ");
    queryBuf.append("            req.codeRequestStatus, ");
    queryBuf.append("            req.codeRequestCategory, ");
    queryBuf.append("            req.createDate, ");
    queryBuf.append("            lab.idLab, ");
    queryBuf.append("            lab.lastName, ");
    queryBuf.append("            lab.firstName, ");
    queryBuf.append("            appUser, ");
    queryBuf.append("            sample, ");
    queryBuf.append("            well.row, ");  // well row
    queryBuf.append("            well.col, ");  // well col
    queryBuf.append("            well.position, ");  // well position
    queryBuf.append("            well.idAssay, ");  // well idassay
    queryBuf.append("            well.idPrimer, ");  // well idprimer
    queryBuf.append("            plate.idPlate, ");
    queryBuf.append("            plate.label ");
    
    queryBuf.append(" FROM       Request as req ");
    queryBuf.append(" JOIN       req.lab as lab ");
    queryBuf.append(" JOIN       req.appUser as appUser ");
    queryBuf.append(" JOIN       req.samples as sample ");
    queryBuf.append(" JOIN       sample.wells as well ");
    queryBuf.append(" LEFT JOIN  well.plate plate ");
    
    queryBuf.append(" WHERE (well.redoFlag != 'Y' OR well.redoFlag is NULL) ");
    queryBuf.append(" AND   (req.codeRequestStatus = '" + RequestStatus.PROCESSING + "') ");
    queryBuf.append(" AND   (well.idPlate is NULL or plate.codePlateType = '" + PlateType.SOURCE_PLATE_TYPE + "') "); 

    addWhere = false;
    
    addRequestCriteria();
    
    queryBuf.append(" ORDER BY req.createDate, req.idRequest, plate.idPlate, well.position ");
   

    return queryBuf;
    
  }


  public StringBuffer getPendingSamplesAlreadyOnPlateQuery() {
    addWhere = true;
    queryBuf = new StringBuffer();
    
    // Get all samples that are in tubes (well with no idPlate) or plates
    queryBuf.append(" SELECT     sample ");
    
    queryBuf.append(" FROM       Request as req ");
    queryBuf.append(" JOIN       req.lab as lab ");
    queryBuf.append(" JOIN       req.appUser as appUser ");
    queryBuf.append(" JOIN       req.samples as sample ");
    queryBuf.append(" JOIN       sample.wells as well ");
    queryBuf.append(" JOIN       well.plate plate ");
    
    queryBuf.append(" WHERE (req.codeRequestStatus = '" + RequestStatus.PROCESSING + "') ");
    queryBuf.append(" AND   (plate.codePlateType = '" + PlateType.REACTION_PLATE_TYPE + "') "); 

    addWhere = false;
    
    addRequestCriteria();

    return queryBuf;
    
  }
  private void addRequestCriteria() {

    // Search by lab 
    if (idLab != null){
      this.addWhereOrAnd();
      queryBuf.append(" req.idLab =");
      queryBuf.append(idLab);
    } 
    
    // Search by request category 
    if (codeRequestCategory != null && 
        !codeRequestCategory.equals("")){
      this.addWhereOrAnd();
      queryBuf.append(" req.codeRequestCategory = '" + codeRequestCategory + "' ");
    }     

    
    // Search by request number 
    if (requestNumber != null && 
        !requestNumber.equals("")){
      this.addWhereOrAnd();
      
      String requestNumberBase = Request.getBaseRequestNumber(requestNumber);
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

  public String getCodeRequestCategory() {
    return codeRequestCategory;
  }

  public void setCodeRequestCategory(String codeRequestCategory) {
    this.codeRequestCategory = codeRequestCategory;
  }

  
  
}
