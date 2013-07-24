package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class PriceSheetPriceCategory extends DictionaryEntry implements Comparable, Serializable {
  private Integer       idPriceSheet;
  private Integer       idPriceCategory;
  private PriceCategory priceCategory;
  private Integer       sortOrder;
  
  
  
  public String getDisplay() {
    String display = idPriceSheet + "-" + idPriceCategory;
    return display;
  }

  public String getValue() {
    return getIdPriceSheet().toString() + getIdPriceCategory().toString();
  }



  /* Override equals() to handle dual key */
  public boolean equals(Object obj) {
    boolean tmpIsEqual = false;
    if (this.getIdPriceSheet() == null || this.getIdPriceCategory() == null) {
      tmpIsEqual = super.equals(obj);
    }
    else {
      if (this == obj) {
        tmpIsEqual = true;
      }
      else if (obj instanceof PriceSheetPriceCategory) {
        if (this.getIdPriceSheet().equals(((PriceSheetPriceCategory) obj).getIdPriceSheet())
            && this.getIdPriceCategory().equals(((PriceSheetPriceCategory) obj).getIdPriceCategory())) {
          tmpIsEqual = true;
        }
      }
    }
    return tmpIsEqual;
  }

  /* Override hashCode() to handle dual key */
  public int hashCode() {
    if (this.getIdPriceSheet() == null || this.getIdPriceCategory() == null) {
      return super.hashCode();
    }
    else {
      // Use exclusive or between hashCodes to get new result
      return this.getIdPriceSheet().hashCode() ^ this.getIdPriceCategory().hashCode();
    }
  }

  
  public Integer getSortOrder() {
    return sortOrder;
  }

  
  public void setSortOrder(Integer sortOrder) {
    this.sortOrder = sortOrder;
  }

  
  public Integer getIdPriceSheet() {
    return idPriceSheet;
  }

  
  public void setIdPriceSheet(Integer idPriceSheet) {
    this.idPriceSheet = idPriceSheet;
  }

  
  public Integer getIdPriceCategory() {
    return idPriceCategory;
  }

  
  public void setIdPriceCategory(Integer idPriceCategory) {
    this.idPriceCategory = idPriceCategory;
  }

  
  public PriceCategory getPriceCategory() {
    return priceCategory;
  }

  
  public void setPriceCategory(PriceCategory priceCategory) {
    this.priceCategory = priceCategory;
  }

  
  public int compareTo(Object o) {
    if (o instanceof PriceSheetPriceCategory) {
      PriceSheetPriceCategory other = (PriceSheetPriceCategory)o;
      String key1 = this.getIdPriceSheet() + "-" + this.getIdPriceCategory();
      String key2 = other.getIdPriceSheet() + "-" + other.getIdPriceCategory();
      return key1.compareTo(key2);
      
    } else {
      return -1;
    }
  }

  
  

}