package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;

import java.io.Serializable;



public class PriceCategoryLite extends DictionaryEntry implements Serializable {
  
  private Integer  idPriceCategory;
  private String   name;
  private String   description;
  private String   pluginClassName;
  private String   codeBillingChargeKind;
  private String   dictionaryClassNameFilter1;
  private String   dictionaryClassNameFilter2;
  private String   isActive;
  
  
  public String getDisplay() {
    String display = this.getNonNullString(getName());
    return display;
  }

  public String getValue() {
    return getIdPriceCategory().toString();
  }


  public Integer getIdPriceCategory() {
    return idPriceCategory;
  }

  
  public void setIdPriceCategory(Integer idPriceCategory) {
    this.idPriceCategory = idPriceCategory;
  }
  
  public String getDescription() {
    return description;
  }

  
  public void setDescription(String description) {
    this.description = description;
  }

  
  public String getPluginClassName() {
    return pluginClassName;
  }

  
  public void setPluginClassName(String pluginClassName) {
    this.pluginClassName = pluginClassName;
  }

  
  public String getCodeBillingChargeKind() {
    return codeBillingChargeKind;
  }

  
  public void setCodeBillingChargeKind(String codeBillingChargeKind) {
    this.codeBillingChargeKind = codeBillingChargeKind;
  }

  
  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }

  
  public String getName() {
    return name;
  }

  
  public void setName(String name) {
    this.name = name;
  }

  
  public String getDictionaryClassNameFilter1() {
    return dictionaryClassNameFilter1;
  }

  
  public void setDictionaryClassNameFilter1(String dictionaryClassNameFilter1) {
    this.dictionaryClassNameFilter1 = dictionaryClassNameFilter1;
  }

  
  public String getDictionaryClassNameFilter2() {
    return dictionaryClassNameFilter2;
  }

  
  public void setDictionaryClassNameFilter2(String dictionaryClassNameFilter2) {
    this.dictionaryClassNameFilter2 = dictionaryClassNameFilter2;
  }

  
}