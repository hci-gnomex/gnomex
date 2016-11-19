package hci.gnomex.model;



import hci.gnomex.utility.GnomexFile;
import hci.hibernate5utils.HibernateDetailObject;

import java.math.BigDecimal;
import java.sql.Date;



public class ProductOrderFile extends GnomexFile {

  private Integer        idProductOrderFile;
  private Integer        idProductOrder;
  private ProductOrder   productOrder;

  public Integer getIdProductOrderFile() {
    return idProductOrderFile;
  }
  public void setIdProductOrderFile(Integer idProductOrderFile) {
    this.idProductOrderFile = idProductOrderFile;
  }
  public Integer getIdProductOrder() {
    return idProductOrder;
  }
  public void setIdProductOrder(Integer idProductOrder) {
    this.idProductOrder = idProductOrder;
  }
  public ProductOrder getProductOrder() {
    return productOrder;
  }
  public void setProductOrder(ProductOrder productOrder) {
    this.productOrder = productOrder;
  }

  
  public void registerMethodsToExcludeFromXML() {
    this.excludeMethodFromXML("getProductOrder");
  }
}