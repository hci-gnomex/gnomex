package hci.gnomex.model;

import hci.hibernate3utils.HibernateDetailObject;

import java.math.BigDecimal;


public class BillingItem extends HibernateDetailObject {
  
  private Integer        idBillingItem;
  private String         category;
  private String         description;
  private Integer        qty;
  private BigDecimal     unitPrice;
  private BigDecimal     totalPrice;
  private BigDecimal     percentagePrice;
  private String         codeBillingChargeKind;
  private String         codeBillingStatus;
  private Integer        idBillingPeriod;  
  private Integer        idBillingCategory;
  private Integer        idBillingPrice;
  private Integer        idRequest;
  private Integer        idBillingAccount;
  private BillingAccount billingAccount;
  private String         notes;
  
  public Integer getIdBillingItem() {
    return idBillingItem;
  }
  
  public void setIdBillingItem(Integer idBillingItem) {
    this.idBillingItem = idBillingItem;
  }
  
  public String getCategory() {
    return category;
  }
  
  public void setCategory(String category) {
    this.category = category;
  }
  
  public String getDescription() {
    return description;
  }
  
  public void setDescription(String description) {
    this.description = description;
  }
  
  public Integer getQty() {
    return qty;
  }
  
  public void setQty(Integer qty) {
    this.qty = qty;
  }
  
  public BigDecimal getUnitPrice() {
    return unitPrice;
  }
  
  public void setUnitPrice(BigDecimal unitPrice) {
    this.unitPrice = unitPrice;
  }
  
  public BigDecimal getTotalPrice() {
    return totalPrice;
  }
  
  public void setTotalPrice(BigDecimal totalPrice) {
    this.totalPrice = totalPrice;
  }
  
  public String getCodeBillingChargeKind() {
    return codeBillingChargeKind;
  }
  
  public void setCodeBillingChargeKind(String codeBillingChargeKind) {
    this.codeBillingChargeKind = codeBillingChargeKind;
  }
  
  public String getCodeBillingStatus() {
    return codeBillingStatus;
  }
  
  public void setCodeBillingStatus(String codeBillingStatus) {
    this.codeBillingStatus = codeBillingStatus;
  }
  
  public Integer getIdBillingPeriod() {
    return idBillingPeriod;
  }
  
  public void setIdBillingPeriod(Integer idBillingPeriod) {
    this.idBillingPeriod = idBillingPeriod;
  }
  
  public Integer getIdRequest() {
    return idRequest;
  }
  
  public void setIdRequest(Integer idRequest) {
    this.idRequest = idRequest;
  }

  
  public Integer getIdBillingCategory() {
    return idBillingCategory;
  }

  
  public void setIdBillingCategory(Integer idBillingCategory) {
    this.idBillingCategory = idBillingCategory;
  }

  
  public Integer getIdBillingPrice() {
    return idBillingPrice;
  }

  
  public void setIdBillingPrice(Integer idBillingPrice) {
    this.idBillingPrice = idBillingPrice;
  }

  
  public Integer getIdBillingAccount() {
    return idBillingAccount;
  }

  
  public void setIdBillingAccount(Integer idBillingAccount) {
    this.idBillingAccount = idBillingAccount;
  }

  
  public BigDecimal getPercentagePrice() {
    return percentagePrice;
  }

  
  public void setPercentagePrice(BigDecimal percentagePrice) {
    this.percentagePrice = percentagePrice;
  }

  
  public BillingAccount getBillingAccount() {
    return billingAccount;
  }

  
  public void setBillingAccount(BillingAccount billingAccount) {
    this.billingAccount = billingAccount;
  }
  
  public void registerMethodsToExcludeFromXML() {
    this.excludeMethodFromXML("getBillingAccount");
  }

  
  public String getNotes() {
    return notes;
  }

  
  public void setNotes(String notes) {
    this.notes = notes;
  }

}
