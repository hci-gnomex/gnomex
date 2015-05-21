package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;

import java.io.Serializable;



public class ProductType extends DictionaryEntry implements Serializable {
  
  private String   codeProductType;
  private String   description;
  private Integer  idCoreFacility;
  private Integer  idVendor;
  private Integer  idPriceCategory;

  public static final String   TYPE_ISCAN_CHIP  = "ISCAN";
  
  public String getDisplay() {
    String display = this.getNonNullString(getDescription());
    return display;
  }

  public String getValue() {
    return getCodeProductType();
  }

  
  public String getCodeProductType() {
    return codeProductType;
  }

  
  public void setCodeProductType(String code) {
    this.codeProductType = code;
  }

  
  public String getDescription() {
    return description;
  }

  
  public void setDescription( String description ) {
    this.description = description;
  }

  
  
  public Integer getIdCoreFacility() {
    return idCoreFacility;
  }

  
  public void setIdCoreFacility( Integer idCoreFacility ) {
    this.idCoreFacility = idCoreFacility;
  }

  public Integer getIdVendor() {
    return idVendor;
  }

  
  public void setIdVendor( Integer idVendor ) {
    this.idVendor = idVendor;
  }

  
  public Integer getIdPriceCategory() {
    return idPriceCategory;
  }

  
  public void setIdPriceCategory( Integer idPriceCategory ) {
    this.idPriceCategory = idPriceCategory;
  }

  
  
}