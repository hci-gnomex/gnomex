package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class PriceSheetRequestCategory extends DictionaryEntry implements Serializable {
  private Integer  idPriceSheet;
  private String   codeRequestCategory;
  private Integer  sortOrder;
  
  
  
  public String getDisplay() {
    String display = idPriceSheet + "-" + codeRequestCategory;
    return display;
  }

  public String getValue() {
    return getIdPriceSheet().toString() + getCodeRequestCategory().toString();
  }





  /* Override equals() to handle dual key */
  public boolean equals(Object obj) {
    boolean tmpIsEqual = false;
    if (this.getIdPriceSheet() == null || this.getCodeRequestCategory() == null) {
      tmpIsEqual = super.equals(obj);
    }
    else {
      if (this == obj) {
        tmpIsEqual = true;
      }
      else if (obj instanceof PriceSheetRequestCategory) {
        if (this.getIdPriceSheet().equals(((PriceSheetRequestCategory) obj).getIdPriceSheet())
            && this.getCodeRequestCategory().equals(((PriceSheetRequestCategory) obj).getCodeRequestCategory())) {
          tmpIsEqual = true;
        }
      }
    }
    return tmpIsEqual;
  }

  /* Override hashCode() to handle dual key */
  public int hashCode() {
    if (this.getIdPriceSheet() == null || this.getCodeRequestCategory() == null) {
      return super.hashCode();
    }
    else {
      // Use exclusive or between hashCodes to get new result
      return this.getIdPriceSheet().hashCode() ^ this.getCodeRequestCategory().hashCode();
    }
  }

  
  public Integer getSortOrder() {
    return sortOrder;
  }

  
  public void setSortOrder(Integer sortOrder) {
    this.sortOrder = sortOrder;
  }

  
  public String getCodeRequestCategory() {
    return codeRequestCategory;
  }

  
  public void setCodeRequestCategory(String codeRequestCategory) {
    this.codeRequestCategory = codeRequestCategory;
  }

  
  public Integer getIdPriceSheet() {
    return idPriceSheet;
  }

  
  public void setIdPriceSheet(Integer idPriceSheet) {
    this.idPriceSheet = idPriceSheet;
  }

  
  

}