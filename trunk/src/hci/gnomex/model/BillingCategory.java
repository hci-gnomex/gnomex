package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;
import java.sql.Date;



public class BillingCategory extends DictionaryEntry implements Serializable {
  private Integer  idBillingCategory;
  private String   description;
  private String   pluginClassname;
  private String   codeBillingChargeKind;
  
  public String getDisplay() {
    String display = this.getNonNullString(getDescription());
    

    return display;
  }

  public String getValue() {
    return getIdBillingCategory().toString();
  }


  
  public Integer getIdBillingCategory() {
    return idBillingCategory;
  }

  
  public void setIdBillingCategory(Integer idBillingCategory) {
    this.idBillingCategory = idBillingCategory;
  }
  
  public String getDescription() {
    return description;
  }

  
  public void setDescription(String description) {
    this.description = description;
  }

  
  public String getPluginClassname() {
    return pluginClassname;
  }

  
  public void setPluginClassname(String pluginClassname) {
    this.pluginClassname = pluginClassname;
  }

  
  public String getCodeBillingChargeKind() {
    return codeBillingChargeKind;
  }

  
  public void setCodeBillingChargeKind(String codeBillingChargeKind) {
    this.codeBillingChargeKind = codeBillingChargeKind;
  }

 

}