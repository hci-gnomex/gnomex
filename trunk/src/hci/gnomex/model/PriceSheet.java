package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;
import java.sql.Date;
import java.util.Set;
import java.util.TreeSet;



public class PriceSheet extends DictionaryEntry implements Serializable {

  private static final long serialVersionUID = -4247178347081835331L;
  
  private Integer  idPriceSheet;
  private String   name;
  private String   description;
  private String   isActive;
  private Set      priceCategories = new TreeSet();
  private Set      requestCategories = new TreeSet();
  
  public String getDisplay() {
    String display = this.getNonNullString(getName());
    if (isActive == null || isActive.equals("N")) {
      display += " (inactive)";
    }

    return display;
  }

  public String getValue() {
    return getIdPriceSheet().toString();
  }


  
  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }

  

  
  public String getDescription() {
    return description;
  }

  
  public void setDescription(String description) {
    this.description = description;
  }

  
  public Integer getIdPriceSheet() {
    return idPriceSheet;
  }

  
  public void setIdPriceSheet(Integer idPriceSheet) {
    this.idPriceSheet = idPriceSheet;
  }

  
  public String getName() {
    return name;
  }

  
  public void setName(String name) {
    this.name = name;
  }

  
  public Set getPriceCategories() {
    return priceCategories;
  }

  
  public void setPriceCategories(Set priceCategories) {
    this.priceCategories = priceCategories;
  }

  
  public Set getRequestCategories() {
    return requestCategories;
  }

  
  public void setRequestCategories(Set requestCategories) {
    this.requestCategories = requestCategories;
  }



}