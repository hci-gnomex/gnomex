package hci.gnomex.model;


import java.util.Calendar;

import hci.framework.model.DetailObject;
import hci.gnomex.security.SecurityAdvisor;

public class AnalysisGroupFilter extends DetailObject {
  
  
  // Criteria
  private Integer               idLab;
  private String                searchPublicProjects;
  private Integer               idRequest;
  private Integer               idAnalysis;
  private String                labKeys;
  private String                searchText;
  private String                lastWeek = "N";
  private String                lastMonth = "N";
  private String                lastThreeMonths = "N";
  private String                lastYear = "N";


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
    queryBuf.append("        '', ");
    queryBuf.append("        aglab.lastName, ");
    queryBuf.append("        aglab.firstName, ");
    queryBuf.append("        a.idAnalysis, ");
    queryBuf.append("        a.number, ");
    queryBuf.append("        a.name, ");
    queryBuf.append("        a.description, ");
    queryBuf.append("        a.createDate,  ");
    queryBuf.append("        a.idLab, ");
    queryBuf.append("        alab.lastName, ");
    queryBuf.append("        alab.firstName, ");
    queryBuf.append("        a.idAnalysisType, ");
    queryBuf.append("        a.idAnalysisProtocol, ");
    queryBuf.append("        a.idOrganism, ");
    queryBuf.append("        a.idGenomeBuild, ");
    queryBuf.append("        a.codeVisibility, ");
    queryBuf.append("        owner.lastName, ");
    queryBuf.append("        owner.firstName, ");
    queryBuf.append("        a.idAppUser ");
    
    getQueryBody(queryBuf);
    
    return queryBuf;
    
  }
  
  public void getQueryBody(StringBuffer queryBuf) {
    
    queryBuf.append(" FROM                AnalysisGroup as ag ");
    queryBuf.append(" JOIN                ag.lab as aglab ");
    queryBuf.append(" LEFT JOIN           ag.analysisItems as a ");
    
    if (hasExperimentItemCriteria()) {
      queryBuf.append(" JOIN              a.experimentItems as ex ");
    }
    
    queryBuf.append(" LEFT JOIN           a.lab as alab ");
    queryBuf.append(" LEFT JOIN           a.appUser as owner ");
    queryBuf.append(" LEFT JOIN           a.collaborators as collab ");
    

    addAnalysisCriteria();
    addExperimentItemCriteria();
    
    addSecurityCriteria();
    
    
    queryBuf.append(" order by aglab.lastName, aglab.firstName, ag.name, a.number ");
  
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
    // Search by multiple labs
    if (labKeys != null && !labKeys.equals("")) {
      String[] tokens = labKeys.split(":");
      if (tokens.length > 0) {
        this.addWhereOrAnd();
        queryBuf.append(" ag.idLab  in (");
        for(int x = 0; x < tokens.length; x++) {
          if (x > 0) {
            queryBuf.append(", ");
          }
          queryBuf.append(tokens[x]);
        }
        queryBuf.append(")");
      }
      
    }
    // Search by idAnalysis 
    if (idAnalysis != null){
      this.addWhereOrAnd();
      queryBuf.append(" a.idAnalysis =");
      queryBuf.append(idAnalysis);
    }
    
    // Search by text
    if (searchText != null && !searchText.equals("")) {
      this.addWhereOrAnd();
      queryBuf.append("(");
      queryBuf.append(" a.name like '%" + searchText + "%'");
      queryBuf.append(" OR ");
      queryBuf.append(" a.description like '%" + searchText + "%'");
      queryBuf.append(" OR ");
      queryBuf.append(" ag.name like '%" + searchText + "%'");
      queryBuf.append(" OR ");
      queryBuf.append(" ag.description like '%" + searchText + "%'");
      queryBuf.append(")");
    }
    
    // Search for analysis create in last week
    if (lastWeek.equals("Y")) {

      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.DAY_OF_YEAR, -7);
      java.sql.Date lastWeek = new java.sql.Date(cal.getTimeInMillis());
      
      this.addWhereOrAnd();
      queryBuf.append(" a.createDate >= '");
      queryBuf.append(this.formatDate(lastWeek));
      queryBuf.append("'");
    }
    // Search for analysis created in last month
    if (lastMonth.equals("Y")) {

      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.MONTH, -1);
      java.sql.Date lastMonth = new java.sql.Date(cal.getTimeInMillis());
      
      this.addWhereOrAnd();
      queryBuf.append(" a.createDate >= '");
      queryBuf.append(this.formatDate(lastMonth));
      queryBuf.append("'");
    }
    // Search for analysis created in last 3 months
    if (lastThreeMonths.equals("Y")) {

      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.MONTH, -3);
      java.sql.Date last3Month = new java.sql.Date(cal.getTimeInMillis());
      
      this.addWhereOrAnd();
      queryBuf.append(" a.createDate >= '");
      queryBuf.append(this.formatDate(last3Month));
      queryBuf.append("'");
    }
    // Search for analysis created in last year
    if (lastYear.equals("Y")) {

      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.YEAR, -1);
      java.sql.Date lastYear = new java.sql.Date(cal.getTimeInMillis());
      
      this.addWhereOrAnd();
      queryBuf.append(" a.createDate >= '");
      queryBuf.append(this.formatDate(lastYear));
      queryBuf.append("'");
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
      addWhere = secAdvisor.buildSpannedSecurityCriteria(queryBuf, "ag", "a", "collab", addWhere, "a.codeVisibility", scopeToGroup, "a.idAnalysis");
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

  
  public Integer getIdAnalysis() {
    return idAnalysis;
  }

  
  public void setIdAnalysis(Integer idAnalysis) {
    this.idAnalysis = idAnalysis;
  }

  
  public String getLabKeys() {
    return labKeys;
  }

  
  public void setLabKeys(String labKeys) {
    this.labKeys = labKeys;
  }

  
  public String getSearchText() {
    return searchText;
  }

  
  public void setSearchText(String searchText) {
    this.searchText = searchText;
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

    
}
