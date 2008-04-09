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
  private String                searchPublicProjects;
  private String                requestDateLastWeek;
  private String                requestDateLastMonth;
  private String                requestDateLastYear;
  private String                isComplete;
  private String                isNotComplete;
  private Integer               idProject;
  
  
  
  private StringBuffer          queryBuf;
  private boolean              addWhere = true;
  private SecurityAdvisor       secAdvisor;
  
  
  public StringBuffer getQualityControlResultQuery(SecurityAdvisor secAdvisor) {
    this.secAdvisor = secAdvisor;
    queryBuf = new StringBuffer();
    addWhere = true;
    
    queryBuf.append(" SELECT distinct req.createDate, req.number, req.codeRequestCategory, "); 
    queryBuf.append("        req.codeMicroarrayCategory, req.idAppUser, ");
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
    queryBuf.append("        ''");
    getQualityControlResultQueryBody(queryBuf);
    
    
    queryBuf.append("        group by req.createDate, req.number, req.codeRequestCategory, req.codeMicroarrayCategory, req.idAppUser, reqOwner.firstName, reqOwner.lastName, req.idLab, req.idRequest ");
    
    return queryBuf;
    
  }
  
  
  public StringBuffer getMicroarrayResultQuery(SecurityAdvisor secAdvisor) {
    this.secAdvisor = secAdvisor;
    queryBuf = new StringBuffer();
    addWhere = true;
    
    queryBuf.append(" SELECT req.createDate, req.number, req.codeRequestCategory, "); 
    queryBuf.append("        req.codeMicroarrayCategory, req.idAppUser, ");
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
    queryBuf.append("        ''");
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
    queryBuf.append("        req.codeMicroarrayCategory, req.idAppUser, ");
    queryBuf.append("        s.number, '', ");
    queryBuf.append("        '', '', ");
    queryBuf.append("        '', '', s.number, s.name, ");
    queryBuf.append("        '', '', '', '', ");
    queryBuf.append("        req.idLab, ");
    queryBuf.append("        '', '', ");
    queryBuf.append("        '', req.idRequest, ");
    queryBuf.append("        '', '', '', '', ");
    queryBuf.append("        '', '', ");
    queryBuf.append("        reqOwner.firstName, reqOwner.lastName, ");
    queryBuf.append("        ''");
    getSolexaResultQueryBody(queryBuf);
    
    return queryBuf;
    
  }
  public void getSolexaResultQueryBody(StringBuffer queryBuf) {
    
    queryBuf.append(" FROM           Request as req ");
    queryBuf.append(" JOIN           req.samples as s ");
    queryBuf.append(" LEFT JOIN      req.appUser as reqOwner ");


    addRequestCriteria();
    addLaneCriteria();
    addSecurityCriteria();
    
    
    
    
  
  }
  
  
  public boolean hasCriteria() {
    if ((requestNumber != null && !requestNumber.equals("")) ||
        idLab != null ||
        idProject != null ||
        idAppUser != null ||
        createDateFrom != null ||
        createDateTo != null ||
        (idRequests != null && idRequests.size() > 0) ||
        (requestDateLastWeek != null && requestDateLastWeek.equalsIgnoreCase("Y")) ||
        (requestDateLastMonth != null && requestDateLastMonth.equalsIgnoreCase("Y")) ||
        (requestDateLastYear != null && requestDateLastYear.equalsIgnoreCase("Y")) ||
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
    //  Search by request date last year
    if (requestDateLastYear != null && requestDateLastYear.equalsIgnoreCase("Y")) {
      Calendar end = Calendar.getInstance();
      end.add(Calendar.YEAR, -1);
      
      this.addWhereOrAnd();
      queryBuf.append(" req.createDate >= '");
      queryBuf.append(this.formatDate(end.getTime()));
      queryBuf.append("'");      
    } 
    // Search by request date last month
    else if (requestDateLastMonth != null && requestDateLastMonth.equalsIgnoreCase("Y")) {
        Calendar end = Calendar.getInstance();
        end.add(Calendar.MONTH, -1);
        
        this.addWhereOrAnd();
        queryBuf.append(" req.createDate >= '");
        queryBuf.append(this.formatDate(end.getTime()));
        queryBuf.append("'");      
    }
    // Search by request date last month
    else if (requestDateLastWeek != null && requestDateLastWeek.equalsIgnoreCase("Y")) {
        Calendar end = Calendar.getInstance();
        end.add(Calendar.DAY_OF_YEAR, -7);
        
        this.addWhereOrAnd();
        queryBuf.append(" req.createDate >= '");
        queryBuf.append(this.formatDate(end.getTime()));
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

  private void addLaneCriteria() {
    this.addWhereOrAnd();
    queryBuf.append(" req.codeRequestCategory = 'SOLEXA'");

    //TODO - need to filter by lane complete date
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
    boolean scopeToGroup = true;
    if (this.searchPublicProjects != null && this.searchPublicProjects.equalsIgnoreCase("Y")) {
      scopeToGroup = false;
    }
    
    secAdvisor.addSecurityCriteria(queryBuf, "req", addWhere, scopeToGroup);
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


  
  public String getSearchPublicProjects() {
    return searchPublicProjects;
  }


  
  public void setSearchPublicProjects(String searchPublicProjects) {
    this.searchPublicProjects = searchPublicProjects;
  }


  
  public String getRequestDateLastMonth() {
    return requestDateLastMonth;
  }


  
  public void setRequestDateLastMonth(String requestDateLastMonth) {
    this.requestDateLastMonth = requestDateLastMonth;
  }


  
  public String getRequestDateLastWeek() {
    return requestDateLastWeek;
  }


  
  public void setRequestDateLastWeek(String requestDateLastWeek) {
    this.requestDateLastWeek = requestDateLastWeek;
  }


  
  public String getRequestDateLastYear() {
    return requestDateLastYear;
  }


  
  public void setRequestDateLastYear(String requestDateLastYear) {
    this.requestDateLastYear = requestDateLastYear;
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


  
  
}
