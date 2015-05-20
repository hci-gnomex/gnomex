package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;
import java.sql.Date;



public class BillingSlideServiceClass extends DictionaryEntry implements Serializable {
  private Integer  idBillingSlideServiceClass;
  private String   billingSlideServiceClass;
  private String   isActive;
  
  public String getDisplay() {
    String display = this.getNonNullString(getBillingSlideServiceClass());
    if (isActive == null || isActive.equals("N")) {
      display += " (inactive)";
    }

    return display;
  }

  public String getValue() {
    return getIdBillingSlideServiceClass().toString();
  }

  
  public Integer getIdBillingSlideServiceClass() {
    return idBillingSlideServiceClass;
  }

  
  public void setIdBillingSlideServiceClass(Integer idBillingSlideServiceClass) {
    this.idBillingSlideServiceClass = idBillingSlideServiceClass;
  }

  
  public String getBillingSlideServiceClass() {
    return billingSlideServiceClass;
  }

  
  public void setBillingSlideServiceClass(String billingSlideServiceClass) {
    this.billingSlideServiceClass = billingSlideServiceClass;
  }

  
  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }
 

}