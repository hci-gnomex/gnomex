package hci.gnomex.lucene;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;

public class SearchListParser implements Serializable {
  
  private Document        searchDoc;
  private StringBuffer    searchText;
  private String          matchAnyTerm;
  private List<Integer>   idLabList;
  private List<Integer>  idOrganismList;
  
  public String getSearchText() {
    return searchText.toString();
  }
  
  public SearchListParser(Document searchDoc, String matchAnyTerm) {
    this.searchDoc = searchDoc;
    this.matchAnyTerm = matchAnyTerm;
  }
  
  public void Parse() throws Exception {
    Element rootNode = this.searchDoc.getRootElement();
    searchText = new StringBuffer();
    idLabList = new ArrayList<Integer>();
    idOrganismList = new ArrayList<Integer>();
    
    for(Iterator i = rootNode.getChildren("Field").iterator(); i.hasNext();) {
      Element fieldNode = (Element)i.next();
      String searchName = fieldNode.getAttributeValue("searchName");
      String isOptionChoice = fieldNode.getAttributeValue("isOptionChoice");
      String value = fieldNode.getAttributeValue("value");
      String[] values = value.split(",");
      if (searchName != null && !searchName.equals("") && value != null && !value.equals("")) {
        if (searchName != null && searchName.equals(AllObjectsIndexHelper.ID_LAB)) {
          for (String v:values) {
            idLabList.add(Integer.parseInt(v));
          }
        } else if (searchName != null && searchName.equals(AllObjectsIndexHelper.ID_ORGANISM)) {
          for (String v:values) {
            idOrganismList.add(Integer.parseInt(v));
          }
        } else {
          if (searchText.length() != 0) {
            if (matchAnyTerm != null && matchAnyTerm.equals("Y")) {
              searchText.append(" OR ");
            } else {
              searchText.append(" AND ");
            }
          }
          
          searchText.append(" ").append(searchName).append(":(");
          if (isOptionChoice.equals("N")) {
            searchText.append("*").append(value).append("*");
          } else {
            Boolean first = true;
            for(String v:values) {
              if (!first) {
                searchText.append(" ");
              }
              first = false;
              searchText.append(v);
            }
          }
          searchText.append(")");
        }
      }
    }

  }
  
  public List<Integer> getIdLabList() {
    return idLabList;
  }
  
  public List<Integer> getIdOrganismList() {
    return idOrganismList;
  }
}
