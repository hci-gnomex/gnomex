package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class OligoBarcodeSchemeAllowed extends DictionaryEntry implements Serializable {
  
  private Integer            idOligoBarcodeSchemeAllowed;
  private Integer            idOligoBarcodeScheme;
  private String             codeRequestCategory;
  private OligoBarcodeScheme oligoBarcodeScheme;
  private RequestCategory    requestCategory;
  
  
  public String getDisplay() {
    String display =  (getOligoBarcodeScheme() != null ? getOligoBarcodeScheme().getOligoBarcodeScheme() : "?") + 
    " - " + 
    (getRequestCategory() != null ? getRequestCategory().getRequestCategory() : "?");
return display;
  }

  public String getValue() {
    return getIdOligoBarcodeSchemeAllowed().toString();
  }
  
  public String getCodeRequestCategory() {
    return codeRequestCategory;
  }

  
  public void setCodeRequestCategory(String codeRequestCategory) {
    this.codeRequestCategory = codeRequestCategory;
  }

  
  public Integer getIdOligoBarcodeScheme() {
    return idOligoBarcodeScheme;
  }

  
  public void setIdOligoBarcodeScheme(Integer idOligoBarcodeScheme) {
    this.idOligoBarcodeScheme = idOligoBarcodeScheme;
  }

  
  private OligoBarcodeScheme getOligoBarcodeScheme() {
    return oligoBarcodeScheme;
  }

  
  public void setOligoBarcodeScheme(OligoBarcodeScheme oligoBarcodeScheme) {
    this.oligoBarcodeScheme = oligoBarcodeScheme;
  }

  
  private RequestCategory getRequestCategory() {
    return requestCategory;
  }

  
  public void setRequestCategory(RequestCategory requestCategory) {
    this.requestCategory = requestCategory;
  }

  
  public Integer getIdOligoBarcodeSchemeAllowed() {
    return idOligoBarcodeSchemeAllowed;
  }

  
  public void setIdOligoBarcodeSchemeAllowed(Integer idOligoBarcodeSchemeAllowed) {
    this.idOligoBarcodeSchemeAllowed = idOligoBarcodeSchemeAllowed;
  }


  

}