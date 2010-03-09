package hci.gnomex.model;


import java.io.Serializable;
import java.sql.Date;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import hci.gnomex.security.SecurityAdvisor;
import hci.framework.model.DetailObject;

public class RequestDownloadFilter extends DetailObject {
  
  
  // Criteria
  private String                requestNumber;
  private Integer               idAppUser;
  private Integer               idLab;
  private Date                  createDateFrom;
  private Date                  createDateTo;
  private List                  idRequests;
  private String                publicExperimentsInOtherGroups;
  private String                isComplete;
  private String                isNotComplete;
  private Integer               idProject;
  private String                lastWeek  = "N";
  private String                lastMonth = "N";
  private String                lastYear  = "N";
  private String                isMicroarray = "N";
  private String                isSolexa = "N";
  private String                isBioanalyzer = "N";
  
  
  
  private StringBuffer          queryBuf;
  private boolean              addWhere = true;
  private SecurityAdvisor       secAdvisor;
  
  
  public StringBuffer getQualityControlResultQuery(SecurityAdvisor secAdvisor) {
    this.secAdvisor = secAdvisor;
    queryBuf = new StringBuffer();
    addWhere = true;
    
    queryBuf.append(" SELECT distinct req.createDate, req.number, req.codeRequestCategory, "); 
    queryBuf.append("        req.codeApplication, req.idAppUser, ");
    queryBuf.append("        '', '', ");
    queryBuf.append("        '', '', ");
    queryBuf.append("        '', '', '', '', ");
    queryBuf.append("        '', '', '', '', ");
    queryBuf.append("        req.idLab, ");
    queryBuf.append("        '', max(s.qualDate), ");
    queryBuf.append("        '', req.idRequest,  ");
    queryBuf.append("        '', '', '', '', ");
    queryBuf.append("        '', '',");
    queryBuf.append("        reqOwner.firstName, reqOwner.lastName, ");
    queryBuf.append("        max(s.seqPrepByCore) ");
    getQualityControlResultQueryBody(queryBuf);
    
    
    queryBuf.append("        group by req.createDate, req.number, req.codeRequestCategory, req.codeApplication, req.idAppUser, reqOwner.firstName, reqOwner.lastName, req.idLab, req.idRequest ");
    
    return queryBuf;
    
  }
  
  
  public StringBuffer getMicroarrayResultQuery(SecurityAdvisor secAdvisor) {
    this.secAdvisor = secAdvisor;
    queryBuf = new StringBuffer();
    addWhere = true;
    
    queryBuf.append(" SELECT req.createDate, req.number, req.codeRequestCategory, "); 
    queryBuf.append("        req.codeApplication, req.idAppUser, ");
    queryBuf.append("        hyb.number, hyb.hybDate, ");
    queryBuf.append("        hyb.extractionDate, hyb.hybFailed, ");
    queryBuf.append("        ls1.labelingDate, s1.qualDate, s1.number, s1.name, ");
    queryBuf.append("        ls2.labelingDate, s2.qualDate, s2.number, s2.name, ");
    queryBuf.append("        req.idLab, ");
    queryBuf.append("        hyb.hasResults, '', ");
    queryBuf.append("        hyb.idSlideDesign, req.idRequest, ");
    queryBuf.append("        s1.qualFailed, s2.qualFailed, ls1.labelingFailed, ls2.labelingFailed, ");
    queryBuf.append("        hyb.extractionFailed, hyb.extractionBypassed, ");
    queryBuf.append("        reqOwner.firstName, reqOwner.lastName, ");
    queryBuf.append("        '' ");
    getMicroarrayResultQueryBody(queryBuf);
    
    return queryBuf;
    
  }
  
  public void getMicroarrayResultQueryBody(StringBuffer queryBuf) {
    
    queryBuf.append(" FROM           Request as req ");
    queryBuf.append(" JOIN           req.hybridizations as hyb ");
    queryBuf.append(" JOIN           hyb.labeledSampleChannel1 as ls1 ");
    queryBuf.append(" JOIN           ls1.sample as s1 ");
    queryBuf.append(" LEFT JOIN      req.appUser as reqOwner ");
    queryBuf.append(" LEFT JOIN      hyb.labeledSampleChannel2 as ls2 ");
    queryBuf.append(" LEFT JOIN      ls2.sample as s2 ");


    addRequestCriteria();
    addHybCriteria();
    addSecurityCriteria();
    
    
    
    
  
  }
  
  public void getQualityControlResultQueryBody(StringBuffer queryBuf) {
     
    queryBuf.append(" FROM           Request as req ");
    queryBuf.append(" LEFT JOIN      req.appUser as reqOwner ");
    queryBuf.append(" LEFT JOIN      req.samples as s ");

    
    addRequestCriteria();
    addQualityControlCriteria();
    addSecurityCriteria();

    
  
  }
  public StringBuffer getSolexaResultQuery(SecurityAdvisor secAdvisor) {
    this.secAdvisor = secAdvisor;
    queryBuf = new StringBuffer();
    addWhere = true;
    
    queryBuf.append(" SELECT req.createDate, req.number, req.codeRequestCategory, "); 
    queryBuf.append("        req.codeApplication, req.idAppUser, ");
    queryBuf.append("        '', '', ");
    queryBuf.append("        '', '', ");
    queryBuf.append("        '', '', '', '', ");
    queryBuf.append("        '', '', '', '', ");
    queryBuf.append("        req.idLab, ");
    queryBuf.append("        '', '', ");
    queryBuf.append("        '', req.idRequest, ");
    queryBuf.append("        '', '', '', '', ");
    queryBuf.append("        '', '', ");
    queryBuf.append("        reqOwner.firstName, reqOwner.lastName, ");
    queryBuf.append("        '' " );
    getSolexaResultQueryBody(queryBuf);
    
    return queryBuf;
    
  }
  public void getSolexaResultQueryBody(StringBuffer queryBuf) {
    
    queryBuf.append(" FROM           Request as req ");
    queryBuf.append(" LEFT JOIN      req.appUser as reqOwner ");


    addRequestCriteria();
    addSolexaCriteria();
    addSecurityCriteria();
  }
  
  
  public StringBuffer getSolexaLaneStatusQuery(SecurityAdvisor secAdvisor) {
    this.secAdvisor = secAdvisor;
    queryBuf = new StringBuffer();
    addWhere = true;
    
    queryBuf.append(" SELECT DISTINCT req.idRequest, ");
    queryBuf.append("        s.idSample, ");
    queryBuf.append("        s.number, ");
    queryBuf.append("        max(ch.firstCycleDate), ");
    queryBuf.append("        max(ch.firstCycleFailed), ");
    queryBuf.append("        max(ch.lastCycleDate), ");
    queryBuf.append("        max(ch.lastCycleFailed) "); 
    getSolexaLaneStatusQueryBody(queryBuf);
    
    return queryBuf;
    
  }
  public void getSolexaLaneStatusQueryBody(StringBuffer queryBuf) {
    
    queryBuf.append(" FROM           Request as req ");
    queryBuf.append(" JOIN           req.sequenceLanes as l ");
    queryBuf.append(" JOIN           l.sample as s ");
    queryBuf.append(" JOIN           l.flowCellChannel as ch ");
    queryBuf.append(" JOIN           ch.flowCell as fc ");

    addRequestCriteria();
    addSolexaCriteria();
    addSecurityCriteria();

    queryBuf.append("        group by req.idRequest, s.idSample, s.number ");
    
  }

  public StringBuffer getSolexaFlowCellQuery(SecurityAdvisor secAdvisor) {
    this.secAdvisor = secAdvisor;
    queryBuf = new StringBuffer();
    addWhere = true;
    
    queryBuf.append(" SELECT DISTINCT req.number, ");
    queryBuf.append("                 fc.number, ");
    queryBuf.append("                 fc.createDate ");
    getSolexaFlowCellQueryBody(queryBuf);
    
    return queryBuf;
    
  }
  public void getSolexaFlowCellQueryBody(StringBuffer queryBuf) {
    
    queryBuf.append(" FROM           Request as req ");
    queryBuf.append(" JOIN           req.sequenceLanes as l ");
    queryBuf.append(" JOIN           l.flowCellChannel as ch ");
    queryBuf.append(" JOIN           ch.flowCell as fc ");

    addRequestCriteria();
    addSolexaCriteria();
    addSecurityCriteria();

    queryBuf.append("        group by req.number, fc.number, fc.createDate ");
    queryBuf.append("        order by req.number, fc.number  ");
    
  }
  
  public boolean hasCriteria() {
    if ((requestNumber != null && !requestNumber.equals("")) ||
        idLab != null ||
        idProject != null ||
        idAppUser != null ||
        createDateFrom != null ||
        createDateTo != null ||
        (publicExperimentsInOtherGroups != null && publicExperimentsInOtherGroups.equalsIgnoreCase("Y")) ||                
        (idRequests != null && idRequests.size() > 0) ||
        (isMicroarray != null && isMicroarray.equalsIgnoreCase("Y")) ||
        (isSolexa != null && isSolexa.equalsIgnoreCase("Y")) ||
        (isBioanalyzer != null && isBioanalyzer.equalsIgnoreCase("Y")) ||
        (lastWeek != null && lastWeek.equalsIgnoreCase("Y")) ||
        (lastMonth != null && lastMonth.equalsIgnoreCase("Y")) ||
        (lastYear != null && lastYear.equalsIgnoreCase("Y")) ||
        (isComplete != null && isComplete.equalsIgnoreCase("Y")) ||
        (isNotComplete != null && isNotComplete.equalsIgnoreCase("Y"))) {
      return true;
    } else {
      return false;
    }
  }
  
  
  

  private void addRequestCriteria() {
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
      queryBuf.append(this.formatDate(createDateFrom));
      queryBuf.append("'");
    } 
    //  Search by create date from 
    if (createDateTo != null){
      this.addWhereOrAnd();
      queryBuf.append(" req.createDate <= '");
      queryBuf.append(this.formatDate(createDateTo));
      queryBuf.append("'");
    }
    // Search by requestIds
    if (idRequests != null && idRequests.size() > 0) {
      this.addWhereOrAnd();
      queryBuf.append(" req.idRequest in (");
      for(Iterator i = idRequests.iterator(); i.hasNext();) {
        Integer idRequest = (Integer)i.next();
        queryBuf.append(idRequest);
        if (i.hasNext()) {
          queryBuf.append(", ");
        }
      }
      queryBuf.append(")");
    }
    //  Search requests made in last year
    if (lastYear != null && lastYear.equalsIgnoreCase("Y")) {
      Calendar end = Calendar.getInstance();
      end.add(Calendar.YEAR, -1);
      
      this.addWhereOrAnd();
      queryBuf.append(" req.createDate >= '");
      queryBuf.append(this.formatDate(end.getTime()));
      queryBuf.append("'");      
    } 
    // Search requests made in last month
    else if (lastMonth != null && lastMonth.equalsIgnoreCase("Y")) {
        Calendar end = Calendar.getInstance();
        end.add(Calendar.MONTH, -1);
        
        this.addWhereOrAnd();
        queryBuf.append(" req.createDate >= '");
        queryBuf.append(this.formatDate(end.getTime()));
        queryBuf.append("'");      
    }
    // Search requests made in last week
    else if (lastWeek != null && lastWeek.equalsIgnoreCase("Y")) {
        Calendar end = Calendar.getInstance();
        end.add(Calendar.DAY_OF_YEAR, -7);
        
        this.addWhereOrAnd();
        queryBuf.append(" req.createDate >= '");
        queryBuf.append(this.formatDate(end.getTime()));
        queryBuf.append("'");      
    } 
    
    // Search for Solexa requests
    if (isSolexa.equals("Y")) {
      this.addWhereOrAnd();
      queryBuf.append(" req.codeRequestCategory = '");
      queryBuf.append(RequestCategory.SOLEXA_REQUEST_CATEGORY);
      queryBuf.append("'");
    }  else if (isBioanalyzer.equals("Y")) {
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

  private void addSolexaCriteria() {
    this.addWhereOrAnd();
    queryBuf.append(" req.codeRequestCategory = 'SOLEXA'");
  }

  private void addQualityControlCriteria() {
    
    //  Search by isComplete
    if (isComplete != null && isComplete.equalsIgnoreCase("Y")){
      this.addWhereOrAnd();
      queryBuf.append(" s.qualDate != null");
    }   
    
    //  Search by isNotComplete
    if (isNotComplete != null && isNotComplete.equalsIgnoreCase("Y")){
      this.addWhereOrAnd();
      queryBuf.append(" s.qualDate is null");
    }   
    
  }
 
  
  private void addSecurityCriteria() {
    if (this.publicExperimentsInOtherGroups != null && this.publicExperimentsInOtherGroups.equalsIgnoreCase("Y")) {
      addWhere = secAdvisor.addPublicOnlySecurityCriteria(queryBuf, "req", addWhere);
    } else {
      boolean scopeToGroup = true;
      addWhere = secAdvisor.addSecurityCriteria(queryBuf, "req", addWhere, scopeToGroup, true);
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


  
  public List getIdRequests() {
    return idRequests;
  }


  
  public void setIdRequests(List idRequests) {
    this.idRequests = idRequests;
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


  
  
}
