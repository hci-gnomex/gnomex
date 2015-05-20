package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;

import java.io.Serializable;
import java.math.BigDecimal;



public class ProductLineItem extends DictionaryEntry implements Serializable {
  
  private Integer     idProductLineItem;
  private Integer     idProductOrder;
  private Integer     idProduct;
  private Product     product;
  private Integer     qty;
  private BigDecimal  unitPrice;
 
  
  public String getDisplay() {
    String display = this.getNonNullString(getIdProductLineItem());
    return display;
  }

  public String getValue() {
    return getIdProductLineItem().toString();
  }

  
  public Integer getIdProductLineItem() {
    return idProductLineItem;
  }

  
  public void setIdProductLineItem( Integer idProductLineItem ) {
    this.idProductLineItem = idProductLineItem;
  }

  
  public Integer getIdProductOrder() {
    return idProductOrder;
  }

  
  public void setIdProductOrder( Integer idProductOrder ) {
    this.idProductOrder = idProductOrder;
  }

  
  public Integer getIdProduct() {
    return idProduct;
  }

  
  public void setIdProduct( Integer idProduct ) {
    this.idProduct = idProduct;
  }

  
  
  public Product getProduct() {
    return product;
  }

  
  public void setProduct( Product product ) {
    this.product = product;
  }

  public Integer getQty() {
    return qty;
  }

  
  public void setQty( Integer qty ) {
    this.qty = qty;
  }

  
  public BigDecimal getUnitPrice() {
    return unitPrice;
  }

  
  public void setUnitPrice( BigDecimal unitPrice ) {
    this.unitPrice = unitPrice;
  }

}