package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class OligoBarcodeScheme extends DictionaryEntry implements Serializable {


  private Integer idOligoBarcodeScheme;
  private String  oligoBarcodeScheme;
  private String  description;
  private String  isActive;
  
  public String getDisplay() {
    String display = this.getNonNullString(this.oligoBarcodeScheme);
    return display;
  }

  public String getValue() {
    return getIdOligoBarcodeScheme().toString();
  }

  
  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }

  
  public Integer getIdOligoBarcodeScheme() {
    return idOligoBarcodeScheme;
  }

  
  public void setIdOligoBarcodeScheme(Integer idOligoBarcodeScheme) {
    this.idOligoBarcodeScheme = idOligoBarcodeScheme;
  }

  
  public String getOligoBarcodeScheme() {
    return oligoBarcodeScheme;
  }

  
  public void setOligoBarcodeScheme(String oligoBarcodeScheme) {
    this.oligoBarcodeScheme = oligoBarcodeScheme;
  }

  
  public String getDescription() {
    return description;
  }

  
  public void setDescription(String description) {
    this.description = description;
  }
  

}