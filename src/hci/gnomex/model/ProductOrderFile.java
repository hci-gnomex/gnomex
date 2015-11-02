package hci.gnomex.model;



import hci.hibernate3utils.HibernateDetailObject;

import java.math.BigDecimal;
import java.sql.Date;



public class ProductOrderFile extends HibernateDetailObject {
  
  private Integer        idProductOrderFile;
  private Integer        idProductOrder;
  private ProductOrder   productOrder;
  private String         fileName;
  private String         baseFilePath;
  private String         qualifiedFilePath;
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
  public String getBaseFilePath() {
    return baseFilePath;
  }
  public void setBaseFilePath(String baseFilePath) {
    this.baseFilePath = baseFilePath;
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
  public String getQualifiedFilePath(){
    return qualifiedFilePath;
  }

  public void setQualifiedFilePath(String qualifiedFilePath){
    this.qualifiedFilePath = qualifiedFilePath;
  }
  
  public String getFullPathName() {
    String fullPathName = getBaseFilePath();
    if ( getQualifiedFilePath() != null && !getQualifiedFilePath().equals("") ) {
      fullPathName += "/" + getQualifiedFilePath();  
    }
    fullPathName += "/" + getFileName();
    
    return fullPathName;
  }
  
  public String getQualifiedFileName() {
    String fullPathName = "";
    if ( getQualifiedFilePath() != null && !getQualifiedFilePath().equals("") ) {
      fullPathName += getQualifiedFilePath() + "/";  
    }
    fullPathName += getFileName();
    return fullPathName;
  }

  
  public void registerMethodsToExcludeFromXML() {
    this.excludeMethodFromXML("getProductOrder");
  }
}