package hci.gnomex.model;



import java.math.BigDecimal;
import java.sql.Date;

import hci.hibernate3utils.HibernateDetailObject;



public class ExperimentFile extends HibernateDetailObject {
  
  private Integer        idExperimentFile;
  private Integer        idRequest;
  private Request        request;
  private String         fileName;
  private BigDecimal     fileSize;
  
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
}