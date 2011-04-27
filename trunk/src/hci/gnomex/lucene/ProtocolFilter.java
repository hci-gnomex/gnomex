package hci.gnomex.lucene;



import hci.framework.model.DetailObject;

public class ProtocolFilter extends DetailObject {
  
  
  // Criteria
  private String                text;
  private String                text1;
  private String                text2;
  private String                text3;
  private String                text4;
  private String                matchAnyTerm = "N";
  private String                matchAllTerms = "Y";
  
  
  
  
  private StringBuffer          searchText;
  private StringBuffer          displayText;
  private boolean              firstTime = true;
  
  
  public StringBuffer getSearchText() {
    firstTime   = true;
    searchText  = new StringBuffer();
    displayText = new StringBuffer();
    
    addCriteria();
    
    return searchText;
    
  }
  
  public String toString() {
    return displayText.toString();
  }
  
  
  private void addCriteria() {

    // Search by text (quick search 
    if (text != null && !text.trim().equals("")){
      text = text.replaceAll(" and ", " AND ");
      text = text.replaceAll(" or ", " OR ");
      this.addLogicalOperator();
      searchText.append(" " + ProtocolIndexHelper.TEXT + ":(");
      searchText.append("*" + text + "*");
      searchText.append(") ");
      
      displayText.append(" Any text = " + text);
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
        searchText.append(" " + ProtocolIndexHelper.TEXT + ":");
        searchText.append("*" + text1 + "*");
        textCriteriaAdded = true;
        
        displayText.append(" Any text = " + text1);
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
        searchText.append(" " + ProtocolIndexHelper.TEXT + ":");
        searchText.append("*" + text2 + "*");
        textCriteriaAdded = true;
        
        displayText.append(" Any text = " + text2);
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
        searchText.append(" " + ProtocolIndexHelper.TEXT + ":");
        searchText.append("*" + text3 + "*");
        textCriteriaAdded = true;
        
        displayText.append(" Any text = " + text3);
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
        searchText.append(" " + ProtocolIndexHelper.TEXT + ":");
        searchText.append("*" + text4 + "*");
        textCriteriaAdded = true;
        
        displayText.append(" Any text = " + text4);
      } 

      searchText.append(")");
    }
   
  }

    
  
  protected void addLogicalOperator() {
    if (!firstTime) {
      if (matchAnyTerm != null && matchAnyTerm.equals("Y")) {
        searchText.append(" OR ");
        displayText.append("   OR   ");
      } else {
        searchText.append(" AND ");
        displayText.append("   AND   ");
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



  
  
}
