package hci.gnomex.model;


import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.framework.model.DetailObject;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ProjectRequestLuceneFilter extends DetailObject {
  
  
  // Criteria
  private Integer               idLab;
  private String                codeRequestCategory;
  private String                codeMicroarrayCategory;
  private Integer               idSlideProduct;
  private Integer               idSampleSource;
  private Integer               idOrganism;
  private String                text;
  private String                text1;
  private String                text2;
  private String                text3;
  private String                text4;
  private String                matchAnyTerm = "N";
  private String                matchAllTerms = "Y";
  private List                  experimentDesignCodes;
  private List                  experimentFactorCodes;
  private String                searchOrganismOnSlideProduct;
  private String                searchOrganismOnSample;
  private String                searchPublicProjects;
  
  // Display fields
  private String                lab;
  private String                requestCategory; 
  private String                microarrayCategory; 
  private String                slideProduct; 
  private String                sampleSrouce; 
  private String                organism; 
  
  private StringBuffer          searchText;
  private StringBuffer          displayText;
  private boolean              firstTime = true;
  

  
  public StringBuffer getSearchText() {
    firstTime = true;
    searchText = new StringBuffer();
    displayText = new StringBuffer();
    
    addCriteria();
    
    return searchText;
    
  }
  
  public String toString() {
    return displayText.toString();
  }
  
  private boolean hasSlideProductCriteria() {
    if ((idOrganism != null && !searchOrganismOnSlideProduct.equals("") && searchOrganismOnSlideProduct.equalsIgnoreCase("Y")) || 
        idSlideProduct != null) {
      return true;
    } else {
      return false;
    }
  }
  
  
  private void addCriteria() {

    //
    // Search by text (quick search
    //
    if (text != null && !text.trim().equals("")){
      text = text.replaceAll(" and ", " AND ");
      text = text.replaceAll(" or ", " OR ");
      this.addLogicalOperator();
      searchText.append(" text:(");
      searchText.append(text);
      searchText.append(") ");

      displayText.append(" any text field = " + text);
    } 
    
    //
    // Search by text1, 2, 3 or 4
    //
    //
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
        
        displayText.append(" any text field = " + text1);
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
        
        displayText.append(" any text field = " + text2);
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
        
        displayText.append(" any text field = " + text3);
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
        
        displayText.append(" any text field = " + text4);
      } 

      searchText.append(")");
    }
    
    //
    //  Search by idOrganism (of slide product)
    //
    if (idOrganism != null && searchOrganismOnSlideProduct != null && searchOrganismOnSlideProduct.equalsIgnoreCase("Y")){
      this.addLogicalOperator();
      searchText.append(" idOrganismSlideProduct:");
      searchText.append(idOrganism);

      displayText.append(" organism of microarray slide = " + organism);
    } 

    //
    //  Search by organism (of sample)
    //
    if (idOrganism != null && searchOrganismOnSample != null && searchOrganismOnSample.equalsIgnoreCase("Y")){
      this.addLogicalOperator();
      searchText.append(" idOrganismSamples:");
      searchText.append(idOrganism);

      displayText.append(" organism of sample = " + organism);
    }
    
    
    //
    //  Search by RequestCategory
    //
    if (codeRequestCategory != null && !codeRequestCategory.equals("")){
      this.addLogicalOperator();
      searchText.append(" codeRequestCategory:");
      searchText.append(codeRequestCategory);
      searchText.append("'");
      
      displayText.append(" request category = " + requestCategory);
    }


    //
    //  Search by  MicroarrayCategory
    //
    if (codeMicroarrayCategory != null && !codeMicroarrayCategory.equals("")){
      this.addLogicalOperator();
      searchText.append(" codeMicroarrayCategory:");
      searchText.append(codeMicroarrayCategory);
      searchText.append("'");
      
      displayText.append(" experiment category = " + microarrayCategory);
    } 

    //
    // Search by lab
    //
    if (idLab != null){
      this.addLogicalOperator();
      searchText.append(" projectIdLab:");
      searchText.append(idLab);
      
      displayText.append(" project lab = " + lab);
    } 

    
    //
    //  Search by idSlideProduct
    //
    if (idSlideProduct != null){
      this.addLogicalOperator();
      searchText.append(" idSlideProduct:");
      searchText.append(idSlideProduct);
      
      displayText.append(" microarray slide used = " + slideProduct);
    }

    //
    // Search by sampleSource
    //
    if (idSampleSource != null){
      this.addLogicalOperator();
      searchText.append(" idSampleSources:");
      searchText.append(idSampleSource);

      displayText.append(" sample source = " + idSampleSource);
    } 
    
    
    //
    // Search by project experiment design codes
    //    
    if (experimentDesignCodes != null && experimentDesignCodes.size() > 0) {
      this.addLogicalOperator();
      searchText.append(" codeExperimentDesigns:(");
      displayText.append(" project experiment designs at least one of (");
     
      for(Iterator i = experimentDesignCodes.iterator(); i.hasNext();) {
        String code = (String)i.next();
        searchText.append(code);
        displayText.append(code);
        if (i.hasNext()) {
          searchText.append(" ");
          displayText.append(" ");
        }        
      }
      searchText.append(")");
      displayText.append(")");
    }
    
    //
    // Search by project experiment factor codes
    //
    if (experimentFactorCodes != null && experimentFactorCodes.size() > 0) {
      this.addLogicalOperator();
      searchText.append(" codeExperimentFactors:(");
      displayText.append(" project experiment factors at least one of (");
     
      for(Iterator i = experimentFactorCodes.iterator(); i.hasNext();) {
        String code = (String)i.next();
        searchText.append(code);
        displayText.append(code);
        if (i.hasNext()) {
          searchText.append(" ");
          displayText.append(" ");
        }        
      }
      searchText.append(")");
      displayText.append(")");
    }
  }

  
    
  
  protected void addLogicalOperator() {
    if (!firstTime) {
      if (matchAnyTerm != null && matchAnyTerm.equals("Y")) {
        searchText.append(" OR ");
        displayText.append("  OR  ");
      } else {
        searchText.append(" AND ");
        displayText.append("  AND  ");
      }
      
    }
    firstTime = false;
  }
  

  
  public Integer getIdLab() {
    return idLab;
  }

  

  

  
  public void setIdLab(Integer idLab) {
    this.idLab = idLab;
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


  
  public String getText() {
    return text;
  }


  
  public void setText(String text) {
    this.text = text;
  }

  
  public String getLab() {
    return lab;
  }

  
  public void setLab(String lab) {
    this.lab = lab;
  }

  
  public String getMicroarrayCategory() {
    return microarrayCategory;
  }

  
  public void setMicroarrayCategory(String microarrayCategory) {
    this.microarrayCategory = microarrayCategory;
  }

  
  public String getOrganism() {
    return organism;
  }

  
  public void setOrganism(String organism) {
    this.organism = organism;
  }

  
  public String getRequestCategory() {
    return requestCategory;
  }

  
  public void setRequestCategory(String requestCategory) {
    this.requestCategory = requestCategory;
  }

  
  public String getSampleSrouce() {
    return sampleSrouce;
  }

  
  public void setSampleSrouce(String sampleSrouce) {
    this.sampleSrouce = sampleSrouce;
  }

  
  public String getSlideProduct() {
    return slideProduct;
  }

  
  public void setSlideProduct(String slideProduct) {
    this.slideProduct = slideProduct;
  }



  
  
}
