package hci.gnomex.model;


import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import hci.framework.model.DetailObject;
import hci.gnomex.security.SecurityAdvisor;

public class AnalysisGroupFilter extends DetailObject {
  
  
  // Criteria
  private Integer               idLab;
  private Integer               idAppUser;
  private String                publicProjects = "N";
  private Integer               idRequest;
  private Integer               idAnalysis;
  private List                  idAnalyses;
  private String                labKeys;
  private String                searchText;
  private String                lastWeek = "N";
  private String                lastMonth = "N";
  private String                lastThreeMonths = "N";
  private String                lastYear = "N";
  private String                allAnalysis = "N";
  private String                publicAnalysisOtherGroups = "N";


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
    queryBuf.append("        a.codeVisibility, ");
    queryBuf.append("        owner.lastName, ");
    queryBuf.append("        owner.firstName, ");
    queryBuf.append("        a.idAppUser ");
    
    getQueryBody(queryBuf);
    
    return queryBuf;
    
  }
  
  public boolean hasSufficientCriteria(SecurityAdvisor secAdvisor) {
    this.secAdvisor = secAdvisor;
    boolean hasLimitingCriteria = false;
    if (idLab != null ||
        idAppUser != null ||
        (publicProjects != null && publicProjects.equals("Y")) ||
        idRequest != null ||
        idAnalysis != null ||
        (idAnalyses != null && idAnalyses.size() > 0) ||
        (labKeys != null && !labKeys.equals("")) ||
        (searchText != null && !searchText.equals("")) ||
        (lastWeek != null && lastWeek.equals("Y")) ||
        (lastMonth != null && lastMonth.equals("Y")) ||
        (lastThreeMonths != null && lastThreeMonths.equals("Y")) ||
        (lastYear != null && lastYear.equals("Y")) ||
        (allAnalysis != null && allAnalysis.equals("Y"))) {
      hasLimitingCriteria = true;
    } else {
      hasLimitingCriteria = false;
    }
    
    if (secAdvisor.hasPermission(SecurityAdvisor.CAN_ACCESS_ANY_OBJECT)) {
      return hasLimitingCriteria;      
    } else {
      return true;
    }
    
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
    

    // Only add selection criteria when "all analysis" is not turned on
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
    // Search by owner 
    if (idAppUser != null){
      this.addWhereOrAnd();
      queryBuf.append(" a.idAppUser =");
      queryBuf.append(idAppUser);
    } 
    
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
    // Search by analysis IDs
    if (idAnalyses != null && idAnalyses.size() > 0) {
      this.addWhereOrAnd();
      queryBuf.append(" req.idAnalysis in (");
      for(Iterator i = idAnalyses.iterator(); i.hasNext();) {
        Integer idAnalysis = (Integer)i.next();
        queryBuf.append(idAnalysis);
        if (i.hasNext()) {
          queryBuf.append(", ");
        }
      }
      queryBuf.append(")");
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
      queryBuf.append(" Coalesce(a.createDate,CURRENT_TIMESTAMP) >= '");
      queryBuf.append(this.formatDate(lastWeek, this.DATE_OUTPUT_SQL));
      queryBuf.append("'");
    }
    // Search for analysis created in last month
    if (lastMonth.equals("Y")) {

      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.MONTH, -1);
      java.sql.Date lastMonth = new java.sql.Date(cal.getTimeInMillis());
      
      this.addWhereOrAnd();
      queryBuf.append(" Coalesce(a.createDate,CURRENT_TIMESTAMP) >= '");
      queryBuf.append(this.formatDate(lastMonth, this.DATE_OUTPUT_SQL));
      queryBuf.append("'");
    }
    // Search for analysis created in last 3 months
    if (lastThreeMonths.equals("Y")) {

      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.MONTH, -3);
      java.sql.Date last3Month = new java.sql.Date(cal.getTimeInMillis());
      
      this.addWhereOrAnd();
      queryBuf.append(" Coalesce(a.createDate,CURRENT_TIMESTAMP) >= '");
      queryBuf.append(this.formatDate(last3Month, this.DATE_OUTPUT_SQL));
      queryBuf.append("'");
    }
    // Search for analysis created in last year
    if (lastYear.equals("Y")) {

      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.YEAR, -1);
      java.sql.Date lastYear = new java.sql.Date(cal.getTimeInMillis());
      
      this.addWhereOrAnd();
      queryBuf.append(" Coalesce(a.createDate,CURRENT_TIMESTAMP) >= '");
      queryBuf.append(this.formatDate(lastYear, this.DATE_OUTPUT_SQL));
      queryBuf.append("'");
    }    
    
    // Search for public projects
    if (publicProjects != null && publicProjects.equals("Y")) {
      this.addWhereOrAnd();
      queryBuf.append(" a.codeVisibility = '" + Visibility.VISIBLE_TO_PUBLIC + "'");
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
    
    if (publicAnalysisOtherGroups != null && publicAnalysisOtherGroups.equals("Y")) {
      addWhere = secAdvisor.addPublicOnlySecurityCriteria(queryBuf, "a", addWhere);
    } else {
      boolean scopeToGroup = true;
      // Don't limit to user's lab if "show all analysis" checked.
      // or "show public experiments" checked.
      if (this.allAnalysis != null && this.allAnalysis.equals("Y")) {
        scopeToGroup = false;
      } else if (this.publicProjects != null && this.publicProjects.equalsIgnoreCase("Y")) {
        scopeToGroup = false;
      }
      
      if (secAdvisor.hasPermission(secAdvisor.CAN_ACCESS_ANY_OBJECT)) {
      }  else {
        addWhere = secAdvisor.buildSpannedSecurityCriteria(queryBuf, "ag", "a", "collab", addWhere, "a.codeVisibility", scopeToGroup, "a.idAnalysis");
      }
      
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

  
  public List getIdAnalyses()
  {
    return idAnalyses;
  }

  public void setIdAnalyses(List idAnalyses)
  {
    this.idAnalyses = idAnalyses;
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

  public String getAllAnalysis() {
    return allAnalysis;
  }

  public void setAllAnalysis(String allAnalysis) {
    this.allAnalysis = allAnalysis;
  }

  public String getPublicProjects() {
    return publicProjects;
  }

  public void setPublicProjects(String publicProjects) {
    this.publicProjects = publicProjects;
  }

  public Integer getIdAppUser() {
    return idAppUser;
  }

  public void setIdAppUser(Integer idAppUser) {
    this.idAppUser = idAppUser;
  }

  public String getPublicAnalysisOtherGroups() {
    return publicAnalysisOtherGroups;
  }

  public void setPublicAnalysisOtherGroups(String publicAnalysisOtherGroups) {
    this.publicAnalysisOtherGroups = publicAnalysisOtherGroups;
  }

    
}
