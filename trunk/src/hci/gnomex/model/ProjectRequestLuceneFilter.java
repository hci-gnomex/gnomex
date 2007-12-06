package hci.gnomex.model;


import hci.gnomex.security.SecurityAdvisor;
import hci.framework.model.DetailObject;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ProjectRequestLuceneFilter extends DetailObject {
  
  
  // Criteria
  private Integer               idAppUser;
  private Integer               idLab;
  private Integer               idRequest;
  private Integer               idProject;
  private String                codeRequestCategory;
  private String                codeMicroarrayCategory;
  private Integer               idSlideProduct;
  private Integer               idSampleSource;
  private Integer               idOrganism;
  private String                text1;
  private String                text2;
  private String                text3;
  private String                text4;
  private String                matchAnyTerm;
  private String                matchAllTerms;
  private List                  experimentDesignCodes;
  private List                  experimentFactorCodes;
  private String                searchOrganismOnSlideProduct;
  private String                searchOrganismOnSample;
  private String                searchPublicProjects;
  
  
  
  
  private StringBuffer          searchText;
  private boolean              firstTime = true;
  
  
  public StringBuffer getSearchText() {
    firstTime = true;
    searchText = new StringBuffer();
    
    addProjectCriteria();
    addRequestCriteria();
    addSampleCriteria();
    
    if (hasSlideProductCriteria()) {
      addSlideProductCriteria();
    }
    
    return searchText;
    
  }
  
  
  private boolean hasSlideProductCriteria() {
    if ((idOrganism != null && !searchOrganismOnSlideProduct.equals("") && searchOrganismOnSlideProduct.equalsIgnoreCase("Y")) || 
        idSlideProduct != null) {
      return true;
    } else {
      return false;
    }
  }
  
  
  private void addProjectCriteria() {
    // Search by lab 
    if (idLab != null){
      this.addLogicalOperator();
      searchText.append(" projectIdLab:");
      searchText.append(idLab);
    } 
    
    if ((text1 != null && !text1.equals("")) ||
        (text2 != null && !text2.equals("")) ||
        (text3 != null && !text3.equals("")) ||
        (text4 != null && !text4.equals(""))){ 
      
      this.addLogicalOperator();
      searchText.append("(");
      
      // Search by text1
      boolean textCriteriaAdded = false;
      if (text1 != null && !text1.equals("")){
        searchText.append(" text:");
        searchText.append(text1);
        textCriteriaAdded = true;
      }
      
      // Search by text2
      if (text2 != null && !text2.equals("")){
        if (textCriteriaAdded) {
          if (matchAnyTerm.equals("Y")) {
            this.addLogicalOperator();          
          } else {
            this.addLogicalOperator();
          }          
        }
        searchText.append(" text:");
        searchText.append(text2);
        textCriteriaAdded = true;
      } 
      
      //    Search by text3
      if (text3 != null && !text3.equals("")){
        if (textCriteriaAdded) {
          if (matchAnyTerm.equals("Y")) {
            this.addLogicalOperator();          
          } else {
            this.addLogicalOperator();
          }          
        }
        searchText.append(" text:");
        searchText.append(text3);
        textCriteriaAdded = true;
      } 
      
      //    Search by text4
      if (text4 != null && !text4.equals("")){
        if (textCriteriaAdded) {
          if (matchAnyTerm.equals("Y")) {
            this.addLogicalOperator();          
          } else {
            this.addLogicalOperator();
          }          
        }
        searchText.append(" text:");
        searchText.append(text4);
        textCriteriaAdded = true;
      } 

      searchText.append(")");
    }
    
    // Search by project experiment design codes
    if (experimentDesignCodes != null && experimentDesignCodes.size() > 0) {
      this.addLogicalOperator();
      searchText.append(" projectAnnotations:(");
     
      for(Iterator i = experimentDesignCodes.iterator(); i.hasNext();) {
        String code = (String)i.next();
        searchText.append(" '" + code + "'");
        if (i.hasNext()) {
          searchText.append("  ");
        }        
      }
      searchText.append(")");
    }
    // Search by project experiment factor codes
    if (experimentFactorCodes != null && experimentFactorCodes.size() > 0) {
      this.addLogicalOperator();
      searchText.append(" projectAnnotations:(");
     
      for(Iterator i = experimentFactorCodes.iterator(); i.hasNext();) {
        String code = (String)i.next();
        searchText.append(" '" + code + "'");
        if (i.hasNext()) {
          searchText.append(", ");
        }        
      }
      searchText.append(")");
    }
  }

  private void addRequestCriteria() {
    

    // Search by user 
    if (idAppUser != null){
      this.addLogicalOperator();
      searchText.append(" requestIdAppUser:");
      searchText.append(idAppUser);
    } 
    //  Search by idRequest 
    if (idRequest != null){
      this.addLogicalOperator();
      searchText.append(" idRequest:");
      searchText.append(idRequest);
    } 
    //  Search by Project 
    if (idProject != null){
      this.addLogicalOperator();
      searchText.append(" idProject:");
      searchText.append(idProject);
    } 
    //  Search by RequestCategory 
    if (codeRequestCategory != null && !codeRequestCategory.equals("")){
      this.addLogicalOperator();
      searchText.append(" codeRequestCategory:");
      searchText.append(codeRequestCategory);
      searchText.append("'");
    }
    //  Search by  MicroarrayCategory 
    if (codeMicroarrayCategory != null && !codeMicroarrayCategory.equals("")){
      this.addLogicalOperator();
      searchText.append(" codeMicroarrayCategory:");
      searchText.append(codeMicroarrayCategory);
      searchText.append("'");
    } 
    
  }
  
  private void addSlideProductCriteria() {
    //  Search by idSlideProduct 
    if (idSlideProduct != null){
      this.addLogicalOperator();
      searchText.append(" idSlideProduct:");
      searchText.append(idSlideProduct);
    }
    
    //  Search by idOrganism (of slide product)
    if (idOrganism != null && searchOrganismOnSlideProduct != null && searchOrganismOnSlideProduct.equalsIgnoreCase("Y")){
      this.addLogicalOperator();
      searchText.append(" idOrganismSlideProduct:");
      searchText.append(idOrganism);
    } 
  }
  
  private void addSampleCriteria() {
    // Search by sampleSource
    if (idSampleSource != null){
      this.addLogicalOperator();
      searchText.append(" idSampleSource:");
      searchText.append(idSampleSource);
    } 
    
    //  Search by organism (of sample)
    if (idOrganism != null && searchOrganismOnSample != null && searchOrganismOnSample.equalsIgnoreCase("Y")){
      this.addLogicalOperator();
      searchText.append(" idOrganismSample:");
      searchText.append(idOrganism);
    } 
  }
    
  
  protected void addLogicalOperator() {
    if (!firstTime) {
      if (matchAnyTerm != null && matchAnyTerm.equals("Y")) {
        searchText.append(" OR ");
      } else {
        searchText.append(" AND ");
      }
      
    }
    firstTime = false;
  }
  

  
  public Integer getIdLab() {
    return idLab;
  }

  
  public Integer getIdUser() {
    return idAppUser;
  }

  

  
  public void setIdLab(Integer idLab) {
    this.idLab = idLab;
  }

  
  public void setIdUser(Integer idAppUser) {
    this.idAppUser = idAppUser;
  }

 
  
  public Integer getIdAppUser() {
    return idAppUser;
  }

  
  public void setIdAppUser(Integer idAppUser) {
    this.idAppUser = idAppUser;
  }

  
  public Integer getIdRequest() {
    return idRequest;
  }

  
  public void setIdRequest(Integer idRequest) {
    this.idRequest = idRequest;
  }

  
  public Integer getIdProject() {
    return idProject;
  }

  
  public void setIdProject(Integer idProject) {
    this.idProject = idProject;
  }

  
  public String getCodeMicroarrayCategory() {
    return codeMicroarrayCategory;
  }

  
  public void setCodeMicroarrayCategory(String codeMicroarrayCategory) {
    this.codeMicroarrayCategory = codeMicroarrayCategory;
  }

  
  public String getCodeRequestCategory() {
    return codeRequestCategory;
  }

  
  public void setCodeRequestCategory(String codeRequestCategory) {
    this.codeRequestCategory = codeRequestCategory;
  }

  
  public Integer getIdSlideProduct() {
    return idSlideProduct;
  }

  
  public void setIdSlideProduct(Integer idSlideProduct) {
    this.idSlideProduct = idSlideProduct;
  }

  
  
  public List getExperimentDesignCodes() {
    return experimentDesignCodes;
  }

  
  public void setExperimentDesignCodes(List experimentDesignCodes) {
    this.experimentDesignCodes = experimentDesignCodes;
  }

  
  public List getExperimentFactorCodes() {
    return experimentFactorCodes;
  }

  
  public void setExperimentFactorCodes(List experimentFactorCodes) {
    this.experimentFactorCodes = experimentFactorCodes;
  }

  

  
  public Integer getIdSampleSource() {
    return idSampleSource;
  }

  
  public void setIdSampleSource(Integer idSampleSource) {
    this.idSampleSource = idSampleSource;
  }

  
  public Integer getIdOrganism() {
    return idOrganism;
  }

  
  public void setIdOrganism(Integer idOrganism) {
    this.idOrganism = idOrganism;
  }

  
  public String getSearchOrganismOnSample() {
    return searchOrganismOnSample;
  }

  
  public void setSearchOrganismOnSample(String searchOrganismOnSample) {
    this.searchOrganismOnSample = searchOrganismOnSample;
  }

  
  public String getSearchOrganismOnSlideProduct() {
    return searchOrganismOnSlideProduct;
  }

  
  public void setSearchOrganismOnSlideProduct(String searchOrganismOnSlideProduct) {
    this.searchOrganismOnSlideProduct = searchOrganismOnSlideProduct;
  }

  
  public String getSearchPublicProjects() {
    return searchPublicProjects;
  }

  
  public void setSearchPublicProjects(String searchPublicProjects) {
    this.searchPublicProjects = searchPublicProjects;
  }


  
  public String getMatchAllTerms() {
    return matchAllTerms;
  }


  
  public void setMatchAllTerms(String matchAllTerms) {
    this.matchAllTerms = matchAllTerms;
  }


  
  public String getMatchAnyTerm() {
    return matchAnyTerm;
  }


  
  public void setMatchAnyTerm(String matchAnyTerm) {
    this.matchAnyTerm = matchAnyTerm;
  }


  
  public String getText1() {
    return text1;
  }


  
  public void setText1(String text1) {
    this.text1 = text1;
  }


  
  public String getText2() {
    return text2;
  }


  
  public void setText2(String text2) {
    this.text2 = text2;
  }


  
  public String getText3() {
    return text3;
  }


  
  public void setText3(String text3) {
    this.text3 = text3;
  }


  
  public String getText4() {
    return text4;
  }


  
  public void setText4(String text4) {
    this.text4 = text4;
  }



  
  
}
