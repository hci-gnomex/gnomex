package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;


import java.io.Serializable;



public class BillingStatus extends DictionaryEntry implements Serializable {
  public static final String                  NEW        = "NEW";
  public static final String                  PENDING    = "PENDING";
  public static final String                  COMPLETED  = "COMPLETE";
  public static final String                  APPROVED   = "APPROVED";
  
  private String   codeBillingStatus;
  private String   billingStatus;
  private String   isActive;
  
  public String getDisplay() {
    String display = this.getNonNullString(getBillingStatus());
    return display;
  }

  public String getValue() {
    return getCodeBillingStatus();
  }

  
  public String getCodeBillingStatus() {
    return codeBillingStatus;
  }

  
  public void setCodeBillingStatus(String codeBillingStatus) {
    this.codeBillingStatus = codeBillingStatus;
  }

  
  public String getBillingStatus() {
    return billingStatus;
  }

  
  public void setBillingStatus(String billingStatus) {
    this.billingStatus = billingStatus;
  }

  
  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }



  
}