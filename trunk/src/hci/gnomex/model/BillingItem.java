package hci.gnomex.model;

import hci.hibernate3utils.HibernateDetailObject;

import java.math.BigDecimal;
import java.text.NumberFormat;

import javax.swing.text.NumberFormatter;


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
  private BillingPeriod  billingPeriod;
  private Integer        idPriceCategory;
  private PriceCategory  priceCategory;
  private Integer        idPrice;
  private Integer        idRequest;
  private Integer        idBillingAccount;
  private BillingAccount billingAccount;
  private Integer        idLab;
  private Lab            lab;
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
  
  public String getTotalPriceDisplay() {
    return "$" + totalPrice;
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
    this.excludeMethodFromXML("getLab");
    this.excludeMethodFromXML("getBillingPeriod");
    this.excludeMethodFromXML("getPriceCategory");
  }

  
  public String getNotes() {
    return notes;
  }

  
  public void setNotes(String notes) {
    this.notes = notes;
  }

  
  public Integer getIdLab() {
    return idLab;
  }

  
  public void setIdLab(Integer idLab) {
    this.idLab = idLab;
  }

  
  public Lab getLab() {
    return lab;
  }

  
  public void setLab(Lab lab) {
    this.lab = lab;
  }
  
  public String getAccountName() {
    if (billingAccount != null) {
      return billingAccount.getAccountName();
    } else {
      return "";
    }
  }
  public String getAccountNameAndNumber() {
    if (billingAccount != null) {
      return billingAccount.getAccountNameAndNumber();
    } else {
      return "";
    }
  }
  public String getAccountNumberDisplay() {
    if (billingAccount != null) {
      return billingAccount.getAccountNumberDisplay();
    } else {
      return "";
    }
  }
  public String getLabName() {
    if (lab != null) {
      return lab.getName();
    } else {
      return "";
    }
  }
  
  public String getPercentageDisplay() {
    if (percentagePrice != null) {
      return new Integer(percentagePrice.multiply(new BigDecimal(100)).intValue()).toString() + "%"; 
    } else {
      return "100%";
    }
  }

  
  public BillingPeriod getBillingPeriod() {
    return billingPeriod;
  }

  
  public void setBillingPeriod(BillingPeriod billingPeriod) {
    this.billingPeriod = billingPeriod;
  }

  
  public Integer getIdPriceCategory() {
    return idPriceCategory;
  }

  
  public void setIdPriceCategory(Integer idPriceCategory) {
    this.idPriceCategory = idPriceCategory;
  }

  
  public Integer getIdPrice() {
    return idPrice;
  }

  
  public void setIdPrice(Integer idPrice) {
    this.idPrice = idPrice;
  }

  
  public PriceCategory getPriceCategory() {
    return priceCategory;
  }

  
  public void setPriceCategory(PriceCategory priceCategory) {
    this.priceCategory = priceCategory;
  }
}
