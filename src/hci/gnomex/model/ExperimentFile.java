package hci.gnomex.model;



import hci.hibernate3utils.HibernateDetailObject;

import java.math.BigDecimal;
import java.sql.Date;



public class ExperimentFile extends HibernateDetailObject {
  
  private Integer        idExperimentFile;
  private Integer        idRequest;
  private Request        request;
  private String         fileName;
  private BigDecimal     fileSize;
  private Date           createDate;

  public Integer getIdExperimentFile() {
    return idExperimentFile;
  }
  public void setIdExperimentFile(Integer idExperimentFile) {
    this.idExperimentFile = idExperimentFile;
  }
  public Integer getIdRequest() {
    return idRequest;
  }
  public void setIdRequest(Integer idRequest) {
    this.idRequest = idRequest;
  }
  public Request getRequest() {
    return request;
  }
  public void setRequest(Request request) {
    this.request = request;
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
    this.excludeMethodFromXML("getRequest");
  }
}