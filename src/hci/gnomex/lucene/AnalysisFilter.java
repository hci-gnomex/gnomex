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
  private Integer               idAnalysisType;
  private Integer               idAnalysisProtocol;
  private Integer               idOrganism;
  private String                text;
  private String                text1;
  private String                text2;
  private String                text3;
  private String                text4;
  private String                matchAnyTerm = "N";
  private String                matchAllTerms = "Y";
  private String                searchPublicProjects;
  private String                showCategory = "Y";
  
  // Display fields
  private String                lab;
  private String                analysisType; 
  private String                analysisProtocol; 
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
      searchText.append(" " + AnalysisIndexHelper.TEXT + ":(");
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
        searchText.append(" " + AnalysisIndexHelper.TEXT + ":");
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
        searchText.append(" " + AnalysisIndexHelper.TEXT + ":");
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
        searchText.append(" " + AnalysisIndexHelper.TEXT + ":");
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
        searchText.append(" " + AnalysisIndexHelper.TEXT + ":");
        searchText.append(text4 + "*");
        textCriteriaAdded = true;
        
        displayText.append(" any text field = " + text4);
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

      displayText.append(" organism  = " + organism);
    } 

    //
    // Search by lab
    //
    if (idLab != null){
      this.addLogicalOperator();
      searchText.append(" " + AnalysisIndexHelper.ID_LAB + ":");
      searchText.append(idLab);
      
      displayText.append(" lab = " + lab);
    } 
    
    //
    //  Search by idAnalysisType
    //
    if (idAnalysisType != null){
      this.addLogicalOperator();
      searchText.append(" " + AnalysisIndexHelper.ID_ANALYSIS_TYPE + ":");
      searchText.append(idAnalysisType);

      displayText.append(" analysis type  = " + analysisType);
    } 

    
    //
    //  Search by idAnalysisProtocol
    //
    if (idAnalysisProtocol != null){
      this.addLogicalOperator();
      searchText.append(" " + AnalysisIndexHelper.ID_ANALYSIS_PROTOCOL + ":");
      searchText.append(idAnalysisProtocol);

      displayText.append(" analysis protocol = " + analysisProtocol);
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

  
  public String getOrganism() {
    return organism;
  }

  
  public void setOrganism(String organism) {
    this.organism = organism;
  }

  
  public void setShowCategory(String showCategory) {
    this.showCategory = showCategory;
  }

  
  public Integer getIdAnalysisType() {
    return idAnalysisType;
  }

  
  public void setIdAnalysisType(Integer idAnalysisType) {
    this.idAnalysisType = idAnalysisType;
  }

  
  public Integer getIdAnalysisProtocol() {
    return idAnalysisProtocol;
  }

  
  public void setIdAnalysisProtocol(Integer idAnalysisProtocol) {
    this.idAnalysisProtocol = idAnalysisProtocol;
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



  
  
}
