package hci.gnomex.model;


import hci.framework.model.DetailObject;
import hci.gnomex.security.SecurityAdvisor;

public class AnalysisGroupFilter extends DetailObject {
  
  
  // Criteria
  private Integer               idLab;
  private String                searchPublicProjects;
  private Integer               idRequest;


  private StringBuffer          queryBuf;
  private boolean              addWhere = true;
  private SecurityAdvisor       secAdvisor;
  
  
  public StringBuffer getQuery(SecurityAdvisor secAdvisor) {
    this.secAdvisor = secAdvisor;
    queryBuf = new StringBuffer();
    addWhere = true;
    
    queryBuf.append(" SELECT ag.idAnalysisGroup, ");
    queryBuf.append("        ag.name, ");
    queryBuf.append("        ag.description, ");
    queryBuf.append("        ag.idLab, ");
    queryBuf.append("        ag.codeVisibility, ");
    queryBuf.append("        aglab.name, ");
    queryBuf.append("        a.idAnalysis, ");
    queryBuf.append("        a.number, ");
    queryBuf.append("        a.name, ");
    queryBuf.append("        a.description, ");
    queryBuf.append("        a.createDate,  ");
    queryBuf.append("        a.idLab, ");
    queryBuf.append("        alab.name, ");
    queryBuf.append("        a.idAnalysisType, ");
    queryBuf.append("        a.idAnalysisProtocol, ");
    queryBuf.append("        a.idOrganism, ");
    queryBuf.append("        a.idGenomeBuild, ");
    queryBuf.append("        a.codeVisibility ");
    
    getQueryBody(queryBuf);
    
    return queryBuf;
    
  }
  
  public void getQueryBody(StringBuffer queryBuf) {
    
    queryBuf.append(" FROM                AnalysisGroup as ag ");
    queryBuf.append(" JOIN                ag.lab as aglab ");
    queryBuf.append(" LEFT JOIN           ag.analysisItems as a ");
    queryBuf.append(" LEFT JOIN           a.lab as alab ");
    
    if (hasExperimentItemCriteria()) {
      queryBuf.append(" JOIN              a.experimentItems as ex ");
    }

    addAnalysisCriteria();
    addExperimentItemCriteria();
    
    addSecurityCriteria();
    
    
    queryBuf.append(" order by aglab.name, ag.name, a.number ");
  
  }
  
  private boolean hasExperimentItemCriteria() {
    if (idRequest != null) {
      return true;
    } else {
      return false;
    }
  }
  

  
  private void addAnalysisCriteria() {
    // Search by lab 
    if (idLab != null){
      this.addWhereOrAnd();
      queryBuf.append(" ag.idLab =");
      queryBuf.append(idLab);
    } 
    
  }
  private void addExperimentItemCriteria() {
    // Search by request (experiment item) 
    if (idRequest != null){
      this.addWhereOrAnd();
      queryBuf.append(" ex.idRequest =");
      queryBuf.append(idRequest);
    } 
    
  }
  
  private void addSecurityCriteria() {
    
    boolean scopeToGroup = true;
    if (this.searchPublicProjects != null && this.searchPublicProjects.equalsIgnoreCase("Y")) {
      scopeToGroup = false;
    }
    
    if (secAdvisor.hasPermission(secAdvisor.CAN_ACCESS_ANY_OBJECT)) {
   
      
    }  else {
      addWhere = secAdvisor.addSecurityCriteria(queryBuf, "ag",       addWhere, scopeToGroup);
      addWhere = secAdvisor.addSecurityCriteria(queryBuf, "a",        addWhere, scopeToGroup, "a.idAnalysis is NULL");      
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

  
  
  
  public void setIdLab(Integer idLab) {
    this.idLab = idLab;
  }

  
  
  public String getSearchPublicProjects() {
    return searchPublicProjects;
  }

  
  public void setSearchPublicProjects(String searchPublicProjects) {
    this.searchPublicProjects = searchPublicProjects;
  }

  
  public Integer getIdRequest() {
    return idRequest;
  }

  
  public void setIdRequest(Integer idRequest) {
    this.idRequest = idRequest;
  }

    
}
