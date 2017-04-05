package hci.gnomex.lucene;



import hci.framework.model.DetailObject;

import hci.gnomex.controller.SearchIndex;

import java.util.List;

public class GlobalFilter extends DetailObject {
  
  
  // Criteria
  private String                text;
  private String                text1;
  private String                matchAnyTerm = "N";
  private String                matchAllTerms = "Y";
  private List<Integer>         idLabList;
  private List<Integer>         idOrganismList;
  private String                searchListText = "";

  private StringBuffer          searchText;
  private boolean               firstTime = true;
  
  
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
      searchText.append(" " + GlobalIndexHelper.TEXT + ":(");
      searchText.append("*" + text + "*");
      searchText.append(") ");
    } 
    
    if ((text1 != null && !text1.equals(""))){ 
      
      this.addLogicalOperator();
      searchText.append("(");
      
      // Search by text1
      boolean textCriteriaAdded = false;
      if (text1 != null && !text1.equals("")){
    	text1 = SearchIndex.escapeSearchText(text1); // Kludge for dealing with apostrophes in search
        searchText.append(" " + GlobalIndexHelper.TEXT + ":");
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
      // Note that the -1 makes sure all protocols are found when searching on a lab.
      searchText.append(" " + GlobalIndexHelper.ID_LAB + ":(g1");
      for(Integer i: idLabList) {
        searchText.append(" ");
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
      // Note appending the -1 makes sure all protocols/topics are found when searching on an organism
      searchText.append(" " + GlobalIndexHelper.ID_ORGANISM + ":(g1");
      for(Integer i: idOrganismList) {
        searchText.append(" ");
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
