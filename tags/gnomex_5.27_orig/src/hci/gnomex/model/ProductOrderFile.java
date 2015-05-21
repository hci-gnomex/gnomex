package hci.gnomex.model;



import hci.hibernate3utils.HibernateDetailObject;

import java.math.BigDecimal;
import java.sql.Date;



public class ProductOrderFile extends HibernateDetailObject {
  
  private Integer        idProductOrderFile;
  private Integer        idProductOrder;
  private ProductOrder   productOrder;
  private String         fileName;
  private BigDecimal     fileSize;
  private Date           createDate;

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
  public String getFileName() {
    return fileName;
  }
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }
  public BigDecimal getFileSize() {
    return fileSize;
  }
  public void setFileSize(BigDecimal fileSize) {
    this.fileSize = fileSize;
  }
  public Date getCreateDate() {
    return createDate;
  }
  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  
  public void registerMethodsToExcludeFromXML() {
    this.excludeMethodFromXML("getProductOrder");
  }
}