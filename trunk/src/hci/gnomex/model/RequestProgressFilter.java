package hci.gnomex.model;


import hci.framework.model.DetailObject;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;

import java.sql.Date;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public class RequestProgressFilter extends DetailObject {
  
  
  // Criteria
  protected String                requestNumber;
  protected Integer               idAppUser;
  protected Integer               idLab;
  protected Date                  createDateFrom;
  protected Date                  createDateTo;
  protected String                isComplete;
  protected String                isNotComplete;
  protected String                requestDateLastMonth;
  protected String                requestDateLastYear;
  protected Integer               idProject;
  private String                  publicExperimentsInOtherGroups;
  private String                  lastWeek  = "N";
  private String                  lastThreeMonths = "N";
  private String                  lastMonth = "N";
  private String                  lastYear  = "N";
  private String                  isMicroarray = "N";
  private String                  isSolexa = "N";
  private String                  isBioanalyzer = "N";
  private String                  isNextGenSeq = "N";
  private String                  allExperiments;
  
  private String                  isCapSeq = "N";
  private String                  isMitSeq = "N";
  private String                  isFragAnal = "N";
  private String                  isCherryPick = "N";
  
  private String                  isExternalOnly = "N";
  
  
  protected StringBuffer          queryBuf;
  protected boolean              addWhere = true;
  protected SecurityAdvisor       secAdvisor;
  protected DictionaryHelper      dictionaryHelper;

  
  public boolean hasCriteria() {
    if ((requestNumber != null && !requestNumber.equals("")) ||
        idAppUser != null ||
        idLab != null ||
        idProject != null ||
        createDateFrom != null ||
        createDateTo != null ||
        (publicExperimentsInOtherGroups != null && publicExperimentsInOtherGroups.equalsIgnoreCase("Y")) ||        
        (isComplete != null && isComplete.equalsIgnoreCase("Y")) ||
        (isNotComplete != null && isNotComplete.equalsIgnoreCase("Y")) ||
        (isMicroarray != null && isMicroarray.equalsIgnoreCase("Y")) ||
        (isSolexa != null && isSolexa.equalsIgnoreCase("Y")) ||
        (isBioanalyzer != null && isBioanalyzer.equalsIgnoreCase("Y")) ||
        (isNextGenSeq != null && isNextGenSeq.equalsIgnoreCase("Y")) ||
        (lastWeek != null && lastWeek.equalsIgnoreCase("Y")) ||
        (lastMonth != null && lastMonth.equalsIgnoreCase("Y")) ||
        (lastThreeMonths != null && lastThreeMonths.equalsIgnoreCase("Y")) ||
        (lastYear != null && lastYear.equalsIgnoreCase("Y")) ||
        (isCapSeq != null && isCapSeq.equalsIgnoreCase("Y")) ||
        (isMitSeq != null && isMitSeq.equalsIgnoreCase("Y")) ||
        (isFragAnal != null && isFragAnal.equalsIgnoreCase("Y")) ||
        (isCherryPick != null && isCherryPick.equalsIgnoreCase("Y")) ||
        (isExternalOnly != null && isExternalOnly.equalsIgnoreCase("Y"))) {
      return true;
    } else {
      return false;
    }
  }
  
  
  public StringBuffer getQualityControlQuery(SecurityAdvisor secAdvisor, DictionaryHelper dictionaryHelper) {
    this.secAdvisor = secAdvisor;
    this.dictionaryHelper = dictionaryHelper;
    queryBuf = new StringBuffer();
    addWhere = true;
    
    queryBuf.append(" SELECT req.createDate, req.number, req.codeRequestCategory, "); 
    queryBuf.append("        req.codeApplication, req.idAppUser, ");
    queryBuf.append("        '', '', ");
    queryBuf.append("        '', '', ");
    queryBuf.append("        '', s.qualDate, s.number, s.name, ");
    queryBuf.append("        '', '', '', '', ");
    queryBuf.append("        req.idLab, ");
    queryBuf.append("        '', req.idRequest, ");
    queryBuf.append("        reqOwner.firstName, reqOwner.lastName");
    getQualityControlQueryBody(queryBuf);
    
    return queryBuf;
    
  }
  
  
  public StringBuffer getMicroarrayQuery(SecurityAdvisor secAdvisor, DictionaryHelper dictionaryHelper) {
    this.secAdvisor = secAdvisor;
    this.dictionaryHelper = dictionaryHelper;
    queryBuf = new StringBuffer();
    addWhere = true;
    
    queryBuf.append(" SELECT req.createDate, req.number, req.codeRequestCategory, "); 
    queryBuf.append("        req.codeApplication, req.idAppUser, ");
    queryBuf.append("        hyb.number, hyb.hybDate, ");
    queryBuf.append("        hyb.extractionDate, hyb.hybFailed, ");
    queryBuf.append("        ls1.labelingDate, s1.qualDate, s1.number, s1.name, ");
    queryBuf.append("        ls2.labelingDate, s2.qualDate, s2.number, s2.name, ");
    queryBuf.append("        req.idLab, ");
    queryBuf.append("        hyb.hasResults, req.idRequest, ");
    queryBuf.append("        reqOwner.firstName, reqOwner.lastName");
    getMicroarrayQueryBody(queryBuf);
    
    return queryBuf;
    
  }
  
  private void getMicroarrayQueryBody(StringBuffer queryBuf) {
    
    queryBuf.append(" FROM           Request as req ");
    queryBuf.append(" JOIN           req.hybridizations as hyb ");
    queryBuf.append(" JOIN           hyb.labeledSampleChannel1 as ls1 ");
    queryBuf.append(" JOIN           ls1.sample as s1 ");
    queryBuf.append(" LEFT JOIN      req.appUser as reqOwner ");
    queryBuf.append(" LEFT JOIN      req.collaborators as collab ");
    queryBuf.append(" LEFT JOIN      hyb.labeledSampleChannel2 as ls2 ");
    queryBuf.append(" LEFT JOIN      ls2.sample as s2 ");
    
    addRequestCriteria();
    addHybCriteria();
    addSecurityCriteria();
    
    
    this.addWhereOrAnd();
    queryBuf.append(" req.codeRequestCategory != '");
    queryBuf.append(RequestCategory.QUALITY_CONTROL_REQUEST_CATEGORY);
    queryBuf.append("'");
    
    queryBuf.append(" order by req.createDate desc, req.number desc , hyb.number asc");
  
  }
  
  private void getQualityControlQueryBody(StringBuffer queryBuf) {
     
    queryBuf.append(" FROM           Request as req ");
    queryBuf.append(" LEFT JOIN      req.samples as s ");
    queryBuf.append(" LEFT JOIN      req.appUser as reqOwner ");
    queryBuf.append(" LEFT JOIN      req.collaborators as collab ");

    
    addRequestCriteria();
    addQualityControlCriteria();
    addSecurityCriteria();

    
    this.addWhereOrAnd();
    queryBuf.append(" req.codeRequestCategory = '");
    queryBuf.append(RequestCategory.QUALITY_CONTROL_REQUEST_CATEGORY);
    queryBuf.append("'");

    queryBuf.append(" order by req.createDate desc, req.number desc , s.number asc");
  
  }
  
  
  

  protected void addRequestCriteria() {
    // Search by request number 
    if (requestNumber != null && !requestNumber.equals("")){
      this.addWhereOrAnd();
      queryBuf.append(" req.number like '");
      queryBuf.append(requestNumber);
      queryBuf.append("%'");
    } 
    // Search by lab 
    if (idLab != null){
      this.addWhereOrAnd();
      queryBuf.append(" req.idLab =");
      queryBuf.append(idLab);
    } 
    // Search by project 
    if (idProject != null){
      this.addWhereOrAnd();
      queryBuf.append(" req.idProject =");
      queryBuf.append(idProject);
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
    //  Search by create date to 
    if (createDateTo != null){
      createDateTo.setTime(createDateTo.getTime() + 24*60*60*1000);
      this.addWhereOrAnd();
      queryBuf.append(" req.createDate <= '");
      queryBuf.append(this.formatDate(createDateTo, this.DATE_OUTPUT_SQL) + " 12:00pm");
      queryBuf.append("'");
    } 
    
    //  External experiments (only)
    if (isExternalOnly != null && isExternalOnly.equals("Y")){
      this.addWhereOrAnd();
      queryBuf.append(" req.isExternal = 'Y'");
    } 

    // Search by request date last year
    if (lastYear != null && lastYear.equalsIgnoreCase("Y")) {
      Calendar end = Calendar.getInstance();
      end.add(Calendar.YEAR, -1);
      
      this.addWhereOrAnd();
      queryBuf.append(" req.createDate >= '");
      queryBuf.append(this.formatDate(end.getTime(), this.DATE_OUTPUT_SQL));
      queryBuf.append("'");      
    } 
    // Search by request date last month
    else if (lastMonth != null && lastMonth.equalsIgnoreCase("Y")) {
        Calendar end = Calendar.getInstance();
        end.add(Calendar.MONTH, -1);
        
        this.addWhereOrAnd();
        queryBuf.append(" req.createDate >= '");
        queryBuf.append(this.formatDate(end.getTime(), this.DATE_OUTPUT_SQL));
        queryBuf.append("'");      
    }
    // Search for requests submitted in last 3 months
    else if (lastThreeMonths.equals("Y")) {

      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.MONTH, -3);
      java.sql.Date last3Month = new java.sql.Date(cal.getTimeInMillis());
      
      this.addWhereOrAnd();
      queryBuf.append(" req.createDate >= '");
      queryBuf.append(this.formatDate(last3Month, this.DATE_OUTPUT_SQL));
      queryBuf.append("'");
    }
    // Search by request date last week
    else if (lastWeek != null && lastWeek.equalsIgnoreCase("Y")) {
        Calendar end = Calendar.getInstance();
        end.add(Calendar.DAY_OF_YEAR, -7);
        
        this.addWhereOrAnd();
        queryBuf.append(" req.createDate >= '");
        queryBuf.append(this.formatDate(end.getTime(), this.DATE_OUTPUT_SQL));
        queryBuf.append("'");      
    }
  
    if (isBioanalyzer.equals("Y")) {
      // Search for bioanalyzer requests
      this.addWhereOrAnd();
      queryBuf.append(" req.codeRequestCategory = '");
      queryBuf.append(RequestCategory.QUALITY_CONTROL_REQUEST_CATEGORY);
      queryBuf.append("'");
    } else if (isMicroarray.equals("Y")) {
      // Search for microarray requests
      this.addWhereOrAnd();
      queryBuf.append(" req.codeRequestCategory in (");
      queryBuf.append("'");
      queryBuf.append(RequestCategory.AFFYMETRIX_MICROARRAY_REQUEST_CATEGORY);
      queryBuf.append("', ");
      queryBuf.append("'");
      queryBuf.append(RequestCategory.INHOUSE_MICROARRAY_REQUEST_CATEGORY);
      queryBuf.append("', ");
      queryBuf.append("'");
      queryBuf.append(RequestCategory.NIMBLEGEN_MICROARRAY_REQUEST_CATEGORY);
      queryBuf.append("', ");
      queryBuf.append("'");
      queryBuf.append(RequestCategory.OTHER_MICROARRAY_REQUEST_CATEGORY);
      queryBuf.append("', ");
      queryBuf.append("'");
      queryBuf.append(RequestCategory.AGILIENT_1_COLOR_MICROARRAY_REQUEST_CATEGORY);
      queryBuf.append("', ");
      queryBuf.append("'");
      queryBuf.append(RequestCategory.AGILIENT_MICROARRAY_REQUEST_CATEGORY);
      queryBuf.append("') ");

    } else if (isNextGenSeq.equals("Y")) {
      // Search for Solexa requests
      this.addWhereOrAnd();
      queryBuf.append(" req.codeRequestCategory IN (");
      
      List requestCategories = dictionaryHelper.getRequestCategoryList();
      int count = 0;
      for (Iterator i = requestCategories.iterator(); i.hasNext();) {
        RequestCategory requestCategory = (RequestCategory)i.next();
        if (requestCategory.isNextGenSeqRequestCategory()) {
          if (count > 0) {
            queryBuf.append(", ");            
          }
          
          queryBuf.append("'");
          queryBuf.append(requestCategory.getCodeRequestCategory());
          queryBuf.append("'");    
          count++;
        }

      }
      
      queryBuf.append(") ");
    }  else if (isCapSeq.equals("Y")) {
      // Search for capillary sequencing requests
      this.addWhereOrAnd();
      queryBuf.append(" req.codeRequestCategory = '");
      queryBuf.append(RequestCategory.CAPILLARY_SEQUENCING_REQUEST_CATEGORY);
      queryBuf.append("'");
    } else if (isMitSeq.equals("Y")) {
      // Search for mitochondrial sequencing requests
      this.addWhereOrAnd();
      queryBuf.append(" req.codeRequestCategory = '");
      queryBuf.append(RequestCategory.MITOCHONDRIAL_DLOOP_SEQ_REQUEST_CATEGORY);
      queryBuf.append("'");
    } else if (isFragAnal.equals("Y")) {
      // Search for fragment analysis requests
      this.addWhereOrAnd();
      queryBuf.append(" req.codeRequestCategory = '");
      queryBuf.append(RequestCategory.FRAGMENT_ANALYSIS_REQUEST_CATEGORY);
      queryBuf.append("'");
    } else if (isCherryPick.equals("Y")) {
      // Search for cherry picking requests
      this.addWhereOrAnd();
      queryBuf.append(" req.codeRequestCategory = '");
      queryBuf.append(RequestCategory.CHERRY_PICKING_REQUEST_CATEGORY);
      queryBuf.append("'");
    }   
  }
 
  private void addHybCriteria() {
    
    //  Search by isComplete
    if (isComplete != null && isComplete.equalsIgnoreCase("Y")){
      this.addWhereOrAnd();
      queryBuf.append(" hyb.extractionDate != null");
    }   
    
    //  Search by isNotComplete
    if (isNotComplete != null && isNotComplete.equalsIgnoreCase("Y")){
      this.addWhereOrAnd();
      queryBuf.append(" hyb.extractionDate is null");
    }   
  }

  private void addQualityControlCriteria() {
    
    //  Search by isComplete
    if (isComplete != null && isComplete.equalsIgnoreCase("Y")){
      this.addWhereOrAnd();
      queryBuf.append(" s.qualDate != null");
    }  
    
    // Search by isNotComplete
    if (isNotComplete != null && isNotComplete.equalsIgnoreCase("Y")){
      this.addWhereOrAnd();
      queryBuf.append(" s.qualDate is null");
    }   
    
  }
  
  

  protected void addSecurityCriteria() {

    if (this.allExperiments != null && this.allExperiments.equals("Y")) {
      boolean scopeToGroup = false;
      addWhere = secAdvisor.buildSecurityCriteria(queryBuf, "req", "collab", addWhere, scopeToGroup, true);
    }else if (this.publicExperimentsInOtherGroups != null && this.publicExperimentsInOtherGroups.equalsIgnoreCase("Y")) {
      addWhere = secAdvisor.addPublicOnlySecurityCriteria(queryBuf, "req", addWhere, true);
    } else {
      boolean scopeToGroup = true;
      addWhere = secAdvisor.buildSecurityCriteria(queryBuf, "req", "collab", addWhere, scopeToGroup, true);
    }
    
    // Always exclude clinic research experiments.
    secAdvisor.appendExcludeClinicResearchCriteria(queryBuf, addWhere, dictionaryHelper, "req");
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

  
  public String getRequestNumber() {
    return requestNumber;
  }

  
  public void setIdLab(Integer idLab) {
    this.idLab = idLab;
  }

  
  public void setIdUser(Integer idAppUser) {
    this.idAppUser = idAppUser;
  }

  
  public void setRequestNumber(String requestNumber) {
    this.requestNumber = requestNumber;
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

  
  public Integer getIdAppUser() {
    return idAppUser;
  }

  
  public void setIdAppUser(Integer idAppUser) {
    this.idAppUser = idAppUser;
  }


  
  public String getIsComplete() {
    return isComplete;
  }


  
  public void setIsComplete(String isComplete) {
    this.isComplete = isComplete;
  }


  
  public String getIsNotComplete() {
    return isNotComplete;
  }


  
  public void setIsNotComplete(String isNotComplete) {
    this.isNotComplete = isNotComplete;
  }


  


  
  public Integer getIdProject() {
    return idProject;
  }


  
  public void setIdProject(Integer idProject) {
    this.idProject = idProject;
  }


  
  public String getPublicExperimentsInOtherGroups() {
    return publicExperimentsInOtherGroups;
  }


  
  public void setPublicExperimentsInOtherGroups(
      String publicExperimentsInOtherGroups) {
    this.publicExperimentsInOtherGroups = publicExperimentsInOtherGroups;
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


  
  public void setLastThreeMonths(String lastThreeMonths) {
    this.lastThreeMonths = lastThreeMonths;
  }


  
  public String getLastThreeMonths() {
    return lastThreeMonths;
  }


  
  public void setLastMonth(String lastMonth) {
    this.lastMonth = lastMonth;
  }


  
  public String getLastYear() {
    return lastYear;
  }


  
  public void setLastYear(String lastYear) {
    this.lastYear = lastYear;
  }


  
  public String getIsMicroarray() {
    return isMicroarray;
  }


  
  public void setIsMicroarray(String isMicroarray) {
    this.isMicroarray = isMicroarray;
  }


  
  public String getIsSolexa() {
    return isSolexa;
  }


  
  public void setIsSolexa(String isSolexa) {
    this.isSolexa = isSolexa;
  }


  
  public String getIsBioanalyzer() {
    return isBioanalyzer;
  }


  
  public void setIsBioanalyzer(String isBioanalyzer) {
    this.isBioanalyzer = isBioanalyzer;
  }


  public String getIsNextGenSeq() {
    return isNextGenSeq;
  }


  public void setIsNextGenSeq(String isNextGenSeq) {
    this.isNextGenSeq = isNextGenSeq;
  }


  public String getAllExperiments() {
    return allExperiments;
  }


  public void setAllExperiments(String allExperiments) {
    this.allExperiments = allExperiments;
  }


  
  public String getIsCapSeq() {
    return isCapSeq;
  }


  public void setIsCapSeq(String isCapSeq) {
    this.isCapSeq = isCapSeq;
  }


  public String getIsMitSeq() {
    return isMitSeq;
  }


  public void setIsMitSeq(String isMitSeq) {
    this.isMitSeq = isMitSeq;
  }


  public String getIsFragAnal() {
    return isFragAnal;
  }


  public void setIsFragAnal(String isFragAnal) {
    this.isFragAnal = isFragAnal;
  }


  public String getIsCherryPick() {
    return isCherryPick;
  }


  public void setIsCherryPick(String isCherryPick) {
    this.isCherryPick = isCherryPick;
  }


  public String getIsExternalOnly() {
    return isExternalOnly;
  }


  public void setIsExternalOnly(String isExternalOnly) {
    this.isExternalOnly = isExternalOnly;
  }
}
