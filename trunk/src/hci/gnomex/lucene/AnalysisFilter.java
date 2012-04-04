  package hci.gnomex.lucene;


import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.framework.model.DetailObject;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class AnalysisFilter extends DetailObject {
  
  
  // Criteria
  private Integer               idLab; 
  private Integer               idOrganism;
  private String                text;
  private String                text1;
  private String                matchAnyTerm = "N";
  private String                matchAllTerms = "Y";
  private String                searchPublicProjects;
  private String                showCategory = "Y";
  private String                searchListText = "";

  // Display fields
  private String                lab;
  private String                analysisType; 
  private String                analysisProtocol; 
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
      searchText.append(" " + AnalysisIndexHelper.TEXT + ":(");
      searchText.append("*" + text + "*");
      searchText.append(") ");
    } 
    
    //
    // Search by text1, 2, 3 or 4
    //
    //
    if ((text1 != null && !text1.equals(""))) { 
      
      this.addLogicalOperator();
      searchText.append("(");
      
      // Search by text1
      boolean textCriteriaAdded = false;
      if (text1 != null && !text1.equals("")){
        searchText.append(" " + AnalysisIndexHelper.TEXT + ":");
        searchText.append("*" + text1 + "*");
        textCriteriaAdded = true;
      }

      searchText.append(")");
    }
    
    //
    //  Search by idOrganism 
    //
    if (idOrganism != null){
      this.addLogicalOperator();
      searchText.append(" " + AnalysisIndexHelper.ID_ORGANISM + ":");
      searchText.append(idOrganism);
    } 

    //
    // Search by lab
    //
    if (idLab != null){
      this.addLogicalOperator();
      searchText.append(" " + AnalysisIndexHelper.ID_LAB + ":");
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

  
  public String getOrganism() {
    return organism;
  }

  
  public void setOrganism(String organism) {
    this.organism = organism;
  }

  
  public void setShowCategory(String showCategory) {
    this.showCategory = showCategory;
  }

  
  public String getAnalysisType() {
    return analysisType;
  }

  
  public void setAnalysisType(String analysisType) {
    this.analysisType = analysisType;
  }

  
  public String getAnalysisProtocol() {
    return analysisProtocol;
  }

  
  public void setAnalysisProtocol(String analysisProtocol) {
    this.analysisProtocol = analysisProtocol;
  }


  public String getSearchListText() {
    return searchListText;
  }
  
  public void setSearchListText(String txt) {
    searchListText = txt;
  }



  
  
}
