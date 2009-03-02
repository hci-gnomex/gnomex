package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;



public class BillingPrice extends DictionaryEntry implements Serializable {
  private Integer    idBillingPrice;
  private Integer    idBillingCategory;
  private Integer    idBillingTemplate;
  private String     description;
  private String     filter1;
  private String     filter2;
  private BigDecimal unitPrice;
  private String     isActive;
  
  public String getDisplay() {
    String display = this.getNonNullString(getDescription());
    
    
    return display;
  }

  public String getValue() {
    return getIdBillingPrice().toString();
  }


  
  public String getDescription() {
    return description;
  }

  
  public void setDescription(String description) {
    this.description = description;
  }

  
  public Integer getIdBillingPrice() {
    return idBillingPrice;
  }

  
  public void setIdBillingPrice(Integer idBillingPrice) {
    this.idBillingPrice = idBillingPrice;
  }

  
  public String getFilter1() {
    return filter1;
  }

  
  public void setFilter1(String filter1) {
    this.filter1 = filter1;
  }

  
  public String getFilter2() {
    return filter2;
  }

  
  public void setFilter2(String filter2) {
    this.filter2 = filter2;
  }

  
  public String getUnitPriceCurrency() {
    if (unitPrice != null) {
      return NumberFormat.getCurrencyInstance().format(unitPrice);
    } else {
      return "";
    }
  }

  
  public void setUnitPrice(BigDecimal unitPrice) {
    this.unitPrice = unitPrice;
  }

  
  public Integer getIdBillingCategory() {
    return idBillingCategory;
  }

  
  public void setIdBillingCategory(Integer idBillingCategory) {
    this.idBillingCategory = idBillingCategory;
  }

  
  public BigDecimal getUnitPrice() {
    return unitPrice;
  }

  
  public Integer getIdBillingTemplate() {
    return idBillingTemplate;
  }

  
  public void setIdBillingTemplate(Integer idBillingTemplate) {
    this.idBillingTemplate = idBillingTemplate;
  }

  
  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }
 
}