package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;

import java.io.Serializable;



public class ProductOrderStatus extends DictionaryEntry implements Serializable {
  
  public static final String                  NEW                 = "NEW";
  public static final String                  PENDING             = "PENDING";
  public static final String                  COMPLETED           = "COMPLETE";
  
  private String   codeProductOrderStatus;
  private String   productOrderStatus;
  private String   isActive;
  
  public String getDisplay() {
    String display = this.getNonNullString(getProductOrderStatus());
    return display;
  }

  public String getValue() {
    return getCodeProductOrderStatus();
  }

  
  public String getCodeProductOrderStatus() {
    return codeProductOrderStatus;
  }

  
  public void setCodeProductOrderStatus(String codeProductOrderStatus) {
    this.codeProductOrderStatus = codeProductOrderStatus;
  }

  
  public String getProductOrderStatus() {
    return productOrderStatus;
  }

  
  public void setProductOrderStatus(String productOrderStatus) {
    this.productOrderStatus = productOrderStatus;
  }

  
  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }



  
}