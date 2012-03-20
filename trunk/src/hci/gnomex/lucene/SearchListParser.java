package hci.gnomex.lucene;

import java.io.Serializable;
import java.util.Iterator;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;

public class SearchListParser implements Serializable {
  
  private Document        searchDoc;
  private StringBuffer    searchText;
  private String          matchAnyTerm;
  
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
    
    for(Iterator i = rootNode.getChildren("Field").iterator(); i.hasNext();) {
      Element fieldNode = (Element)i.next();
      String searchName = fieldNode.getAttributeValue("searchName");
      String isOptionChoice = fieldNode.getAttributeValue("isOptionChoice");
      String value = fieldNode.getAttributeValue("value");
      if (searchName != null && !searchName.equals("") && value != null && !value.equals("")) {
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
          String[] values = value.split(",");
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
