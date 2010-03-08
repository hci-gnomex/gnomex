package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Set;
import java.util.TreeSet;



public class Price extends DictionaryEntry implements Serializable {
  private Integer    idPrice;
  private Integer    idPriceCategory;
  private String     name;
  private String     description;
  private BigDecimal unitPrice;
  private BigDecimal unitPriceExternal;
  private String     isActive;
  private Set        priceCriterias = new TreeSet();
  
  public String getDisplay() {
    String display = this.getNonNullString(getName());
    
    
    return display;
  }

  public String getValue() {
    return getIdPrice().toString();
  }


  
  public String getDescription() {
    return description;
  }

  
  public void setDescription(String description) {
    this.description = description;
  }

  
  
  
  public BigDecimal getUnitPrice() {
    return unitPrice;
  }

  
  
  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }

  
  public Integer getIdPrice() {
    return idPrice;
  }

  
  public void setIdPrice(Integer idPrice) {
    this.idPrice = idPrice;
  }

  
  public Integer getIdPriceCategory() {
    return idPriceCategory;
  }

  
  public void setIdPriceCategory(Integer idPriceCategory) {
    this.idPriceCategory = idPriceCategory;
  }

  
  public String getName() {
    return name;
  }

  
  public void setName(String name) {
    this.name = name;
  }

  
  public BigDecimal getUnitPriceExternal() {
    return unitPriceExternal;
  }


  
  public void setUnitPriceExternal(BigDecimal unitPriceExternal) {
    this.unitPriceExternal = unitPriceExternal;
  }

  
  public void setUnitPrice(BigDecimal unitPrice) {
    this.unitPrice = unitPrice;
  }
  
  
  public String getUnitPriceCurrency() {
    if (unitPrice != null) {
      return NumberFormat.getCurrencyInstance().format(unitPrice);
    } else {
      return "";
    }
  }
  
  
  public String getUnitPriceExternalCurrency() {
    if (unitPriceExternal != null) {
      return NumberFormat.getCurrencyInstance().format(unitPriceExternal);
    } else {
      return "";
    }
  }

  
  public Set getPriceCriterias() {
    return priceCriterias;
  }

  
  public void setPriceCriterias(Set priceCriterias) {
    this.priceCriterias = priceCriterias;
  }
  
 
}