package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class OligoBarcode extends DictionaryEntry implements Serializable {


  private Integer idOligoBarcode;
  private String  name;
  private String  barcodeSequence;
  private String  isActive;
  private Integer sortOrder;
  private Integer idOligoBarcodeScheme;
  private OligoBarcodeScheme oligoBarcodeScheme;
  
  public String getDisplay() {
    return name;
  }

  public String getBarcodeSequenceDisplay() {
    String display = this.getBarcodeSequence() + (this.getOligoBarcodeScheme() != null ? " (" + this.getOligoBarcodeScheme().getOligoBarcodeScheme() + ")": "");
    return display;
  }
  
  public String getValue() {
    return getIdOligoBarcode().toString();
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


  public Integer getIdOligoBarcode() {
    return idOligoBarcode;
  }

  
  public void setIdOligoBarcode(Integer idOligoBarcode) {
    this.idOligoBarcode = idOligoBarcode;
  }

  
  public String getBarcodeSequence() {
    return barcodeSequence;
  }

  
  public void setBarcodeSequence(String barcodeSequence) {
    this.barcodeSequence = barcodeSequence;
  }

  
  private OligoBarcodeScheme getOligoBarcodeScheme() {
    return oligoBarcodeScheme;
  }

  
  private void setOligoBarcodeScheme(OligoBarcodeScheme oligoBarcodeScheme) {
    this.oligoBarcodeScheme = oligoBarcodeScheme;
  }

  
  public Integer getSortOrder() {
    return sortOrder;
  }

  
  public void setSortOrder(Integer sortOrder) {
    this.sortOrder = sortOrder;
  }

  
  public String getName() {
    return name;
  }

  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getOligoBarcodeSchemeDisplay() {
    if (this.getOligoBarcodeScheme() != null) {
      return this.getOligoBarcodeScheme().getDisplay();
    } else {
      return "";
    }
  }
  

}