package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;

import java.io.Serializable;

public class Product extends DictionaryEntry implements Serializable {

  private Integer     idProduct;
  private String      name;
  private String      codeProductType;
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

  public String getCodeProductType() {
    return codeProductType;
  }

  public void setCodeProductType(String codeProductType) {
    this.codeProductType = codeProductType;
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