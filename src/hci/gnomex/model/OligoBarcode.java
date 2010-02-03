package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class OligoBarcode extends DictionaryEntry implements Serializable {


  private Integer idOligoBarcode;
  private String  barcodeSequence;
  private String  isActive;
  private Integer idOligoBarcodeScheme;
  private OligoBarcodeScheme oligoBarcodeScheme;
  
  public String getDisplay() {
    String display = (this.getOligoBarcodeScheme() != null ? this.getOligoBarcodeScheme().getOligoBarcodeScheme() : "") + " - " + this.getBarcodeSequence();
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
  

}