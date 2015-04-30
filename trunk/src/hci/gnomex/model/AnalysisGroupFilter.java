package hci.gnomex.model;


import hci.framework.model.DetailObject;
import hci.gnomex.security.SecurityAdvisor;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

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
  private String                allAnalysis = "N";
  private String                publicAnalysisOtherGroups = "N";
  
  private Date					createDateFrom;
  private Date					createDateTo;

  private String				idGenomeBuild;
  private String				idOrganism;


  private StringBuffer          queryBuf;
  private boolean               addWhere = true;
  private SecurityAdvisor       secAdvisor;


  public static final int       COL_ID_ANALYSIS = 7;
  public static final int       COL_ANALYSIS_NUMBER = 8;
  public static final int       COL_ANALYSIS_NAME = 9;
  public static final int       COL_ANALYSIS_DESCRIPTION = 10;
  public static final int       COL_LAB_LAST_NAME = 13;
  public static final int       COL_LAB_FIRST_NAME = 14;
  public static final int       COL_ID_ANALYSIS_TYPE = 15;
  public static final int       COL_ID_ORGANISM = 17;
  public static final int       COL_OWNER_LAST_NAME = 19;
  public static final int       COL_OWNER_FIRST_NAME = 20;
  public static final int       COL_ID_ANALYSIS_GROUP_NAME = 1;

  public static final int       PROPCOL_ID_ANALYSIS = 0;
  public static final int       PROPCOL_ID_PROPERTY = 1;
  public static final int       PROPCOL_PROPERTY_TYPE = 2;
  public static final int       PROPCOL_PROPERTY_NAME = 3;
  public static final int       PROPCOL_PROPERTY_VALUE = 4;
  public static final int       PROPCOL_PROPERTY_MULTI_VALUE = 5;
  public static final int       PROPCOL_PROPERTY_OPTION = 6;

  private boolean               isCreateReport = false;


  public StringBuffer getQuery(SecurityAdvisor secAdvisor){
    return getQuery(secAdvisor, false);
  }

  public StringBuffer getQuery(SecurityAdvisor secAdvisor, Boolean isCreateReport) {
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
    queryBuf.append("        a.idAppUser, ");
    queryBuf.append("        a.idInstitution ");

    if(isCreateReport){
      this.isCreateReport = true;
    }
    getQueryBody(queryBuf);

    return queryBuf;

  }

  //returns an ArrayList of all labs in user's query (not just those with >0 analyses)
  // ArrayList will be rows from query ordered by lastName, firstName, idLab
  public StringBuffer getAllLabsInQuery(SecurityAdvisor secAdvisor) {
    this.secAdvisor = secAdvisor;
    addWhere = true;
    boolean labSearch = false;
    boolean textSearch = false;
    ArrayList<Object[]> allLabsInQuery = new ArrayList<Object[]>();
    StringBuffer queryBuf = new StringBuffer();
    queryBuf.append("SELECT idLab, lastName, firstName ");
    queryBuf.append("FROM Lab as lab ");


    // if we have one or more labKeys then filter query by those ids
    if (labKeys != null && !labKeys.equals("")) {
      labSearch = true;
      String[] tokens = labKeys.split(":");
      if (tokens.length > 0) {
        queryBuf.append(" WHERE ");
        queryBuf.append(" idLab IN (");
        for(int x=0; x < tokens.length; x++){
          if(x > 0) { //not the first element
            queryBuf.append(", "); }
          queryBuf.append(tokens[x]);
        }
        queryBuf.append(")");
      }
    }
    if (searchText != null && !searchText.equals("")) {
      textSearch = true;
      if(labSearch){
        queryBuf.append(" AND ");
      }
      else {
        queryBuf.append(" WHERE ");
      }
      queryBuf.append("(");
      queryBuf.append(" lab.firstName like '%" + searchText + "%'");
      queryBuf.append(" OR ");
      queryBuf.append(" lab.lastName like '%" + searchText + "%'");
      queryBuf.append(")");
    }
    if(!labSearch && !textSearch){
      return new StringBuffer(); // no need to find labs without analyses as there is no applicable search criteria, should guarantee an error in caller if my logic doesn't cover all possibilities
    }

    queryBuf.append(" ORDER BY lastName, firstName, idLab");
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
    queryBuf.append(" a.idAnalysis, ");
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



  public boolean hasSufficientCriteria(SecurityAdvisor secAdvisor) {
    this.secAdvisor = secAdvisor;
    boolean hasLimitingCriteria = false;
    if (idLab != null ||
        idOrganism != null ||
        idAppUser != null ||
        (publicProjects != null && publicProjects.equals("Y")) ||
        idRequest != null ||
        idAnalysis != null ||
        (idAnalyses != null && idAnalyses.size() > 0) ||
        (labKeys != null && !labKeys.equals("")) ||
        (searchText != null && !searchText.equals("")) ||
        (allAnalysis != null && allAnalysis.equals("Y")) ||
        createDateFrom != null ||
        createDateTo != null) {
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
    queryBuf.append(" LEFT JOIN			  a.genomeBuilds as genBuild ");


    // Only add selection criteria when "all analysis" is not turned on
    addAnalysisCriteria();

    addExperimentItemCriteria();

    addSecurityCriteria();
    if(isCreateReport){
      filterByExcludeUsage();
    }


    queryBuf.append(" order by aglab.lastName, aglab.firstName, ag.name, a.number ");

  }

  public void getAnnotationQueryBody(StringBuffer queryBuf) {
    queryBuf.append(" FROM                AnalysisGroup as ag ");
    queryBuf.append(" JOIN                ag.lab as aglab ");
    queryBuf.append(" LEFT JOIN           ag.analysisItems as a ");
    queryBuf.append(" JOIN        a.submitter as submitter ");
    queryBuf.append(" JOIN        a.lab as lab ");
    queryBuf.append(" LEFT JOIN   a.collaborators as collab ");
    queryBuf.append(" JOIN        a.propertyEntries as propEntry ");
    queryBuf.append(" JOIN        propEntry.property as prop ");
    queryBuf.append(" LEFT JOIN   propEntry.values as value ");
    queryBuf.append(" LEFT JOIN   propEntry.options as option ");

    // Only add selection criteria when "all analysis" is not turned on
    addAnalysisCriteria();
    addExperimentItemCriteria();

    if(isCreateReport){
      filterByExcludeUsage();
    }

    addSecurityCriteria();
  }


  private boolean hasExperimentItemCriteria() {
    if (idRequest != null) {
      return true;
    } else {
      return false;
    }
  }

  private void filterByExcludeUsage(){
    //If getting lab info for all labs don't include labs with excludeUsage == 'Y'
    if(idLab == null){
      this.addWhereOrAnd();
      queryBuf.append(" aglab.excludeUsage != 'Y' ");
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
      queryBuf.append(" OR ");
      queryBuf.append(" aglab.firstName like '%" + searchText + "%'");
      queryBuf.append(" OR ");
      queryBuf.append(" aglab.lastName like '%" + searchText + "%'");
      queryBuf.append(")");
    }
    
    // Search by create date from
    if (createDateFrom != null) {
    	this.addWhereOrAnd();
    	if (secAdvisor.isGuest()) { // Show guests on CvDC analyses that have been released to the public since create date
    		queryBuf.append(" CASE WHEN a.privacyExpirationDate IS NULL THEN Coalesce(a.createDate,CURRENT_TIMESTAMP) ELSE Coalesce(a.privacyExpirationDate,CURRENT_TIMESTAMP) END  >= '");
    	} else {
    		queryBuf.append(" Coalesce(a.createDate,CURRENT_TIMESTAMP) >= '");
    	}
    	queryBuf.append(this.formatDate(createDateFrom, this.DATE_OUTPUT_SQL));
    	queryBuf.append("'");
    }
    
    // Search by create date to
    if (createDateTo != null) {
    	createDateTo.setTime(createDateTo.getTime() + 24*60*60*1000);
    	this.addWhereOrAnd();
    	if (secAdvisor.isGuest()) { // Show guests on CvDC analyses that have been released to the public within create date to
    		queryBuf.append(" CASE WHEN a.privacyExpirationDate IS NULL THEN Coalesce(a.createDate,CURRENT_TIMESTAMP) ELSE Coalesce(a.privacyExpirationDate,CURRENT_TIMESTAMP) END  < '");
    	} else {
    		queryBuf.append(" Coalesce(a.createDate,CURRENT_TIMESTAMP) < '");
    	}
    	queryBuf.append(this.formatDate(createDateTo, this.DATE_OUTPUT_SQL));
    	queryBuf.append("'");    	
    }

    // Search for public projects
    if (publicProjects != null && publicProjects.equals("Y")) {
      this.addWhereOrAnd();
      queryBuf.append(" a.codeVisibility = '" + Visibility.VISIBLE_TO_PUBLIC + "'");
    }

    if( this.idGenomeBuild != null && !this.idGenomeBuild.equals("") ) { // user selected a specific Genome Build for an Organism
      this.addWhereOrAnd();
      queryBuf.append(" genBuild.idGenomeBuild = '" + this.idGenomeBuild + "' ");
    }else if(this.idOrganism != null && !this.idOrganism.equals("") ){ // user only selected an Organism, not a specific Genome Build for that Organism
      this.addWhereOrAnd();
      queryBuf.append(" a.idOrganism = '" + this.idOrganism + "' ");
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
      addWhere = secAdvisor.addPublicOnlySecurityCriteria(queryBuf, "a", addWhere, false);
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
        addWhere = secAdvisor.buildSpannedSecurityCriteria(queryBuf, "ag", "a", "collab", addWhere, "a.codeVisibility", scopeToGroup, "a.idAnalysis", null, false);
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

  public void setIdOrganism(String idOrganism) {
    this.idOrganism = idOrganism;
  }
  public String getIdOrganism(String idOrganism) {
    return this.idOrganism;
  }

  public void setIdGenomeBuild(String idGenomeBuild) {
    this.idGenomeBuild = idGenomeBuild;
  }
  public String getIdGenomeBuild(String idGenomeBuild) {
    return this.idGenomeBuild;
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

}
