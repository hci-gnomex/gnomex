package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class Vendor extends DictionaryEntry implements Serializable {
  private Integer  idVendor;
  private String   vendorName;
  private String   description;
  private String   isActive;
  
  public String getDisplay() {
    String display = this.getNonNullString(getVendorName());
    return display;
  }

  public String getValue() {
    return getIdVendor().toString();
  }


  
  public Integer getIdVendor() {
    return idVendor;
  }

  
  public void setIdVendor(Integer idVendor) {
    this.idVendor = idVendor;
  }

  
  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }

  
  public String getVendorName() {
    return vendorName;
  }

  
  public void setVendorName(String vendorName) {
    this.vendorName = vendorName;
  }

  
  public String getDescription() {
    return description;
  }

  
  public void setDescription(String description) {
    this.description = description;
  }
  
 

}