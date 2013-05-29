package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;


import java.io.Serializable;



public class BillingChargeKind extends DictionaryEntry implements Serializable {
  public static final String                   SERVICE = "SERVICE";
  public static final String                   PRODUCT = "PRODUCT";
  
  private String   codeBillingChargeKind;
  private String   billingChargeKind;
  private String   isActive;
  
  public String getDisplay() {
    String display = this.getNonNullString(getBillingChargeKind());
    return display;
  }

  public String getValue() {
    return getCodeBillingChargeKind();
  }

  
  public String getCodeBillingChargeKind() {
    return codeBillingChargeKind;
  }

  
  public void setCodeBillingChargeKind(String codeBillingChargeKind) {
    this.codeBillingChargeKind = codeBillingChargeKind;
  }

  
  public String getBillingChargeKind() {
    return billingChargeKind;
  }

  
  public void setBillingChargeKind(String billingChargeKind) {
    this.billingChargeKind = billingChargeKind;
  }

  
  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }



  
}