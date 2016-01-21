package hci.gnomex.model;

import java.io.Serializable;

import hci.dictionary.model.DictionaryEntry;



public class ProductType extends DictionaryEntry implements Serializable {

  private Integer  idProductType;
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
    return getNonNullString( getIdProductType() );
  }


  public Integer getIdProductType() {
    return idProductType;
  }


  public void setIdProductType(Integer idProductType) {
    this.idProductType = idProductType;
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