package hci.gnomex.lucene;



import hci.framework.model.DetailObject;

public class DataTrackFilter extends DetailObject {
  
  
  // Criteria
  private String                text;
  private String                text1;
  private String                text2;
  private String                text3;
  private String                text4;
  private String                matchAnyTerm = "N";
  private String                matchAllTerms = "Y";
  private Integer               idLab;
  private String                searchListText = "";

  // Display fields
  private String                lab;

  private StringBuffer          searchText;
  private boolean              firstTime = true;
  
  
  public StringBuffer getSearchText() {
    firstTime   = true;
    searchText  = new StringBuffer();
    
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

    // Search by text (quick search 
    if (text != null && !text.trim().equals("")){
      text = text.replaceAll(" and ", " AND ");
      text = text.replaceAll(" or ", " OR ");
      this.addLogicalOperator();
      searchText.append(" " + DataTrackIndexHelper.TEXT + ":(");
      searchText.append("*" + text + "*");
      searchText.append(") ");
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
        searchText.append(" " + DataTrackIndexHelper.TEXT + ":");
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
        searchText.append(" " + DataTrackIndexHelper.TEXT + ":");
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
        searchText.append(" " + DataTrackIndexHelper.TEXT + ":");
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
        searchText.append(" " + DataTrackIndexHelper.TEXT + ":");
        searchText.append("*" + text4 + "*");
        textCriteriaAdded = true;
      } 

      searchText.append(")");
    }

    //
    // Search by lab
    //
    if (idLab != null){
      this.addLogicalOperator();
      searchText.append(" " + DataTrackIndexHelper.ID_LAB + ":");
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


  public Integer getIdLab() {
    return idLab;
  }
  public void setIdLab(Integer id) {
    idLab = id;
  }
  
  public String getLab() {
    return lab;
  }
  public void setLab(String l) {
    lab = l;
  }


  public String getSearchListText() {
    return searchListText;
  }
  
  public void setSearchListText(String txt) {
    searchListText = txt;
  }

}
