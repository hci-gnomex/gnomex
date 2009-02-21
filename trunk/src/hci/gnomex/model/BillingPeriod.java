package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;
import java.sql.Date;



public class BillingPeriod extends DictionaryEntry implements Serializable {
  private Integer  idBillingPeriod;
  private String   billingPeriod;
  private Date     startDate;
  private Date     endDate;
  private String   isActive;
  
  public String getDisplay() {
    String display = this.getNonNullString(getBillingPeriod());
    if (isActive == null || isActive.equals("N")) {
      display += " (inactive)";
    }

    return display;
  }

  public String getValue() {
    return getIdBillingPeriod().toString();
  }
  
  public Integer getIdBillingPeriod() {
    return idBillingPeriod;
  }

  
  public void setIdBillingPeriod(Integer idBillingPeriod) {
    this.idBillingPeriod = idBillingPeriod;
  }

  
  
  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }

  
  public String getBillingPeriod() {
    return billingPeriod;
  }

  
  public void setBillingPeriod(String billingPeriod) {
    this.billingPeriod = billingPeriod;
  }

  
  public Date getStartDate() {
    return startDate;
  }

  
  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }
  
  
  public Date getEndDate() {
    return endDate;
  }

  
  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  
  public String getStartDateSort() {
    return this.formatDate(this.startDate, this.DATE_OUTPUT_SQL);
  }


  public String getIsCurrentPeriod() {
    java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
    if (this.startDate.getTime() <= today.getTime() &&
        this.endDate.getTime() >= today.getTime()) {
      return "Y";
    } else {
      return "N";
    }
    
  }

}