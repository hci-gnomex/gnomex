package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;
import java.sql.Date;



public class BillingTemplate extends DictionaryEntry implements Serializable {
  private Integer  idBillingTemplate;
  private String   description;
  private String   codeRequestCategory;
  private String   isActive;
  
  public String getDisplay() {
    String display = this.getNonNullString(getDescription());
    if (isActive == null || isActive.equals("N")) {
      display += " (inactive)";
    }

    return display;
  }

  public String getValue() {
    return getIdBillingTemplate().toString();
  }


  
  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }

  
  public Integer getIdBillingTemplate() {
    return idBillingTemplate;
  }

  
  public void setIdBillingTemplate(Integer idBillingTemplate) {
    this.idBillingTemplate = idBillingTemplate;
  }

  

  
  public String getCodeRequestCategory() {
    return codeRequestCategory;
  }

  
  public void setCodeRequestCategory(String codeRequestCategory) {
    this.codeRequestCategory = codeRequestCategory;
  }

  
  public String getDescription() {
    return description;
  }

  
  public void setDescription(String description) {
    this.description = description;
  }
 

}