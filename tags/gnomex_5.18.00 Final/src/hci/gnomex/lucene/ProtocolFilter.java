package hci.gnomex.lucene;



import hci.framework.model.DetailObject;

public class ProtocolFilter extends DetailObject {
  
  
  // Criteria
  private String                text;
  private String                text1;
  private String                matchAnyTerm = "N";
  private String                matchAllTerms = "Y";
  private String                searchListText = "";

  
  
  
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
      searchText.append(" " + ProtocolIndexHelper.TEXT + ":(");
      searchText.append("*" + text + "*");
      searchText.append(") ");
    } 
    
    if ((text1 != null && !text1.equals(""))){ 
      
      this.addLogicalOperator();
      searchText.append("(");
      
      // Search by text1
      boolean textCriteriaAdded = false;
      if (text1 != null && !text1.equals("")){
        searchText.append(" " + ProtocolIndexHelper.TEXT + ":");
        searchText.append("*" + text1 + "*");
        textCriteriaAdded = true;
      }

      searchText.append(")");
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


  
  public String getText() {
    return text;
  }


  
  public void setText(String text) {
    this.text = text;
  }


  public String getSearchListText() {
    return searchListText;
  }
  
  public void setSearchListText(String txt) {
    searchListText = txt;
  }



  
  
}
