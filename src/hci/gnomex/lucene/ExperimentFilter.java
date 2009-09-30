  package hci.gnomex.lucene;


import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.framework.model.DetailObject;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ExperimentFilter extends DetailObject {
  
  
  // Criteria
  private Integer               idLab;
  private String                codeRequestCategory;
  private String                codeMicroarrayCategory;
  private Integer               idSlideProduct;
  private Integer               idSampleType;
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
  private String                searchOrganismOnSlideProduct = "N";
  private String                searchOrganismOnSample = "N";
  private String                searchPublicProjects;
  private String                showCategory = "Y";
  
  // Display fields
  private String                lab;
  private String                requestCategory; 
  private String                microarrayCategory; 
  private String                slideProduct; 
  private String                sampleSource; 
  private String                sampleType; 
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

  
  
  private void addCriteria() {

    //
    // Search by text (quick search
    //
    if (text != null && !text.trim().equals("")){
      text = text.replaceAll(" and ", " AND ");
      text = text.replaceAll(" or ", " OR ");
      this.addLogicalOperator();
      searchText.append(" " + ExperimentIndexHelper.TEXT + ":(");
      searchText.append(text + "*");
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
        searchText.append(" " + ExperimentIndexHelper.TEXT + ":");
        searchText.append(text1 + "*");
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
        searchText.append(" " + ExperimentIndexHelper.TEXT + ":");
        searchText.append(text2 + "*");
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
        searchText.append(" " + ExperimentIndexHelper.TEXT + ":");
        searchText.append(text3 + "*");
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
        searchText.append(" " + ExperimentIndexHelper.TEXT + ":");
        searchText.append(text4 + "*");
        textCriteriaAdded = true;
        
        displayText.append(" any text field = " + text4);
      } 

      searchText.append(")");
    }

    //
    //  Search by idOrganism (of slide product or sample)
    //
    if (idOrganism != null) {
      if ((searchOrganismOnSlideProduct.equalsIgnoreCase("N") && searchOrganismOnSample.equalsIgnoreCase("N")) ||
          (searchOrganismOnSlideProduct.equalsIgnoreCase("Y") && searchOrganismOnSample.equalsIgnoreCase("Y"))) {
       this.addLogicalOperator();
       searchText.append("(");
       searchText.append(" " + ExperimentIndexHelper.ID_ORGANISM_SLIDE_PRODUCT + ":");
       searchText.append(idOrganism);

       searchText.append(" OR ");
       
       searchText.append(" " + ExperimentIndexHelper.ID_ORGANISM_SAMPLE + ":");
       searchText.append(idOrganism);
       searchText.append(")");

       displayText.append(" organism  = " + organism);
      } // Search on organism of microarray
      else if (searchOrganismOnSlideProduct.equalsIgnoreCase("Y")) {
         this.addLogicalOperator();
         searchText.append(" " + ExperimentIndexHelper.ID_ORGANISM_SLIDE_PRODUCT + ":");
         searchText.append(idOrganism);

         displayText.append(" organism of microarray slide = " + organism);
      } // Search on organism of sample
      else if (searchOrganismOnSample.equalsIgnoreCase("Y")) {
        this.addLogicalOperator();
        searchText.append(" " + ExperimentIndexHelper.ID_ORGANISM_SAMPLE + ":");
        searchText.append(idOrganism);
        
        displayText.append(" organism of sample = " + organism);
      }
    }
    
    
    //
    //  Search by RequestCategory
    //
    if (codeRequestCategory != null && !codeRequestCategory.equals("")){
      this.addLogicalOperator();
      searchText.append(" " + ExperimentIndexHelper.CODE_REQUEST_CATEGORY + ":");
      searchText.append(codeRequestCategory);
      searchText.append("'");
      
      displayText.append(" request category = " + requestCategory);
    }


    //
    //  Search by  MicroarrayCategory
    //
    if (codeMicroarrayCategory != null && !codeMicroarrayCategory.equals("")){
      this.addLogicalOperator();
      searchText.append(" " + ExperimentIndexHelper.CODE_MICROARRAY_CATEGORY + ":");
      searchText.append(codeMicroarrayCategory);
      searchText.append("'");
      
      displayText.append(" experiment category = " + microarrayCategory);
    } 

    //
    // Search by lab
    //
    if (idLab != null){
      this.addLogicalOperator();
      searchText.append(" " + ExperimentIndexHelper.ID_LAB_PROJECT + ":");
      searchText.append(idLab);
      
      displayText.append(" project lab = " + lab);
    } 

    
    //
    //  Search by idSlideProduct
    //
    if (idSlideProduct != null){
      this.addLogicalOperator();
      searchText.append(" " + ExperimentIndexHelper.ID_SLIDE_PRODUCT + ":");
      searchText.append(idSlideProduct);
      
      displayText.append(" microarray slide used = " + slideProduct);
    }

    //
    // Search by sampleType
    //
    if (idSampleType != null){
      this.addLogicalOperator();
      searchText.append(" " + ExperimentIndexHelper.ID_SAMPLE_TYPES + ":");
      searchText.append(idSampleType);

      displayText.append(" sample type = " + sampleType);
    } 
    
    
    
    //
    // Search by project experiment design codes
    //    
    if (experimentDesignCodes != null && experimentDesignCodes.size() > 0) {
      this.addLogicalOperator();
      searchText.append(" " + ExperimentIndexHelper.CODE_EXPERIMENT_DESIGNS + ":(");
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
      searchText.append(" " + ExperimentIndexHelper.CODE_EXPERIMENT_FACTORS + ":(");
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

  
  public String getSampleSource() {
    return sampleSource;
  }

  
  public void setSampleSource(String sampleSource) {
    this.sampleSource = sampleSource;
  }

  
  public String getSlideProduct() {
    return slideProduct;
  }

  
  public void setSlideProduct(String slideProduct) {
    this.slideProduct = slideProduct;
  }

  
  public String getShowCategory() {
    return showCategory;
  }

  
  public void setShowCategory(String showCategory) {
    this.showCategory = showCategory;
  }

  
  public String getSampleType() {
    return sampleType;
  }

  
  public void setSampleType(String sampleType) {
    this.sampleType = sampleType;
  }

  
  public Integer getIdSampleType() {
    return idSampleType;
  }

  
  public void setIdSampleType(Integer idSampleType) {
    this.idSampleType = idSampleType;
  }



  
  
}
