package hci.gnomex.lucene;



import java.util.List;

import hci.framework.model.DetailObject;

public class DataTrackFilter extends DetailObject {
  
  
  // Criteria
  private String                text;
  private String                text1;
  private String                matchAnyTerm = "N";
  private String                matchAllTerms = "Y";
  private List<Integer>         idLabList;
  private List<Integer>         idOrganismList;
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
      searchText.append(" " + DataTrackIndexHelper.TEXT + ":(");
      searchText.append("*" + text + "*");
      searchText.append(") ");
    } 
    
    if ((text1 != null && !text1.equals(""))){ 
      
      this.addLogicalOperator();
      searchText.append("(");
      
      // Search by text1
      boolean textCriteriaAdded = false;
      if (text1 != null && !text1.equals("")){
        searchText.append(" " + DataTrackIndexHelper.TEXT + ":");
        searchText.append("*" + text1 + "*");
        textCriteriaAdded = true;
      }

      searchText.append(")");
    }

    //
    // Search by lab
    //
    if (idLabList != null && idLabList.size() > 0){
      this.addLogicalOperator();
      searchText.append(" " + DataTrackIndexHelper.ID_LAB + ":(");
      boolean firstTime = false;
      for(Integer i: idLabList) {
        if (!firstTime) {
          searchText.append(" ");
        }
        firstTime = false;
        searchText.append(i.toString());
      }
      searchText.append(")");
    } 
    
    //
    //  Search by idOrganism 
    //
    if (idOrganismList != null && idOrganismList.size() > 0){
      this.addLogicalOperator();
      searchText.append(" " + DataTrackIndexHelper.ID_ORGANISM + ":(");
      boolean firstTime = false;
      for(Integer i: idOrganismList) {
        if (!firstTime) {
          searchText.append(" ");
        }
        firstTime = false;
        searchText.append(i.toString());
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


  public List<Integer> getIdLabList() {
    return idLabList;
  }
  public void setIdLabList(List<Integer> list) {
    idLabList = list;
  }
  
  public List<Integer> getIdOrganismLit() {
    return idOrganismList;
  }

  
  public void setIdOrganismList(List<Integer> list) {
    this.idOrganismList = list;
  }


  public String getSearchListText() {
    return searchListText;
  }
  
  public void setSearchListText(String txt) {
    searchListText = txt;
  }

}
