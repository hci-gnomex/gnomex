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
  private Integer               idOrganism;
  private String                text;
  private String                text1;
  private String                text2;
  private String                text3;
  private String                text4;
  private String                matchAnyTerm = "N";
  private String                matchAllTerms = "Y";
  private String                searchOrganismOnSlideProduct = "N";
  private String                searchOrganismOnSample = "N";
  private String                searchPublicProjects;
  private String                showCategory = "Y";
  private String                searchListText = "";
  
  // Display fields
  private String                lab;
  private String                requestCategory; 
  private String                application; 
  private String                slideProduct; 
  private String                sampleSource; 
  private String                sampleType; 
  private String                organism; 
  
  private StringBuffer          searchText;
  private boolean               firstTime = true;
  

  
  public StringBuffer getSearchText() {
    firstTime = true;
    searchText = new StringBuffer();
    
    addCriteria();
    
    return searchText;
    
  }
  
  public String toString() {
    return searchText.toString();
  }

  
  
  private void addCriteria() {

    // Add text from search list
    if (!searchListText.equals("")) {
      searchText.append(searchListText);
      firstTime = false;
    }
    
    //
    // Search by text (quick search
    //
    if (text != null && !text.trim().equals("")){
      text = text.replaceAll(" and ", " AND ");
      text = text.replaceAll(" or ", " OR ");
      this.addLogicalOperator();
      searchText.append(" " + ExperimentIndexHelper.TEXT + ":(");
      searchText.append("*" + text + "*");
      searchText.append(") ");
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
        searchText.append("*" + text1 + "*");
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
        searchText.append(" " + ExperimentIndexHelper.TEXT + ":");
        searchText.append("*" + text2 + "*");
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
        searchText.append(" " + ExperimentIndexHelper.TEXT + ":");
        searchText.append("*" + text3 + "*");
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
        searchText.append(" " + ExperimentIndexHelper.TEXT + ":");
        searchText.append("*" + text4 + "*");
        textCriteriaAdded = true;
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
      } // Search on organism of microarray
      else if (searchOrganismOnSlideProduct.equalsIgnoreCase("Y")) {
         this.addLogicalOperator();
         searchText.append(" " + ExperimentIndexHelper.ID_ORGANISM_SLIDE_PRODUCT + ":");
         searchText.append(idOrganism);
      } // Search on organism of sample
      else if (searchOrganismOnSample.equalsIgnoreCase("Y")) {
        this.addLogicalOperator();
        searchText.append(" " + ExperimentIndexHelper.ID_ORGANISM_SAMPLE + ":");
        searchText.append(idOrganism);
      }
    }
    
    //
    // Search by lab
    //
    if (idLab != null){
      this.addLogicalOperator();
      searchText.append(" " + ExperimentIndexHelper.ID_LAB_PROJECT + ":");
      searchText.append(idLab);
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

  

  

  
  public void setIdLab(Integer idLab) {
    this.idLab = idLab;
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

  
  public String getApplication() {
    return application;
  }

  
  public void setApplication(String application) {
    this.application = application;
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

  
  public String getSearchListText() {
    return searchListText;
  }
  
  public void setSearchListText(String txt) {
    searchListText = txt;
  }
  
  
}
