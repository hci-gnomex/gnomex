package hci.gnomex.model;

import java.io.Serializable;

import hci.dictionary.model.DictionaryEntry;

public class Product extends DictionaryEntry implements Serializable {

  private Integer     idProduct;
  private String      name;
  private Integer     idProductType;
  private ProductType productType;
  private Integer     idPrice;
  private Integer     orderQty;
  private Integer     useQty;
  private String      catalogNumber;
  private String      isActive;
  private String      batchSamplesByUseQuantity;
  private String      billThroughGnomex;

  public String getDisplay() {
    String display = this.getNonNullString(name);
    return display;
  }

  public String getValue() {
    return getIdProduct().toString();
  }

  public Integer getIdProduct() {
    return idProduct;
  }

  public void setIdProduct(Integer idProduct) {
    this.idProduct = idProduct;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getIdProductType() {
    return idProductType;
  }

  public void setIdProductType(Integer idProductType) {
    this.idProductType = idProductType;
  }

  public ProductType getProductType() {
    return productType;
  }

  public void setProductType(ProductType productType) {
    this.productType = productType;
  }

  public Integer getIdPrice() {
    return idPrice;
  }

  public void setIdPrice(Integer idPrice) {
    this.idPrice = idPrice;
  }

  public Integer getOrderQty() {
    return orderQty;
  }

  public void setOrderQty(Integer orderQty) {
    this.orderQty = orderQty;
  }

  public Integer getUseQty() {
    return useQty;
  }

  public void setUseQty(Integer useQty) {
    this.useQty = useQty;
  }

  public String getCatalogNumber() {
    return catalogNumber != null ? catalogNumber : "";
  }

  public void setCatalogNumber(String catalogNumber) {
    this.catalogNumber = catalogNumber;
  }

  public String getIsActive() {
    return isActive;
  }

  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }

  public String isSelected() {
    return "N";
  }

  public String getBatchSamplesByUseQuantity() {
    return batchSamplesByUseQuantity;
  }

  public void setBatchSamplesByUseQuantity(String batchSamplesByUseQuantity) {
    this.batchSamplesByUseQuantity = batchSamplesByUseQuantity;
  }

  public String getBillThroughGnomex() {
    return billThroughGnomex;
  }

  public void setBillThroughGnomex(String billThroughGnomex) {
    this.billThroughGnomex = billThroughGnomex;
  }

}