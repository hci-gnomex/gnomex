package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;
import java.sql.Date;



public class BillingSlideProductClass extends DictionaryEntry implements Serializable {
  private Integer  idBillingSlideProductClass;
  private String   billingSlideProductClass;
  private String   isActive;
  
  public String getDisplay() {
    String display = this.getNonNullString(getBillingSlideProductClass());
    if (isActive == null || isActive.equals("N")) {
      display += " (inactive)";
    }

    return display;
  }

  public String getValue() {
    return getIdBillingSlideProductClass().toString();
  }

  
  public Integer getIdBillingSlideProductClass() {
    return idBillingSlideProductClass;
  }

  
  public void setIdBillingSlideProductClass(Integer idBillingSlideProductClass) {
    this.idBillingSlideProductClass = idBillingSlideProductClass;
  }

  
  public String getBillingSlideProductClass() {
    return billingSlideProductClass;
  }

  
  public void setBillingSlideProductClass(String billingSlideProductClass) {
    this.billingSlideProductClass = billingSlideProductClass;
  }

  
  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }
 

}