package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class CreditCardCompany extends DictionaryEntry implements Serializable {
  private Integer  idCreditCardCompany;
  private String   name;
  private String   isActive;
  private Integer  sortOrder;
  
  public String getDisplay() {
    String display = this.getNonNullString(getName());
    return display;
  }

  public String getValue() {
    return getIdCreditCardCompany().toString();
  }


  
  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }

  
  public Integer getIdCreditCardCompany() {
    return idCreditCardCompany;
  }

  
  public void setIdCreditCardCompany(Integer idCreditCardCompany) {
    this.idCreditCardCompany = idCreditCardCompany;
  }



  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getSortOrder() {
    return sortOrder;
  }

  public void setSortOrder(Integer sortOrder) {
    this.sortOrder = sortOrder;
  }



}