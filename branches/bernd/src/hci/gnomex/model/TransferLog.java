package hci.gnomex.model;

import hci.hibernate3utils.HibernateDetailObject;

import java.math.BigDecimal;


import javax.swing.text.NumberFormatter;


public class TransferLog extends HibernateDetailObject {
  public static final String     TYPE_DOWNLOAD = "download";
  public static final String     TYPE_UPLOAD = "upload";
  
  public static final String     METHOD_HTTP = "http";
  public static final String     METHOD_FTD = "fdt";
  
  private Integer          idTransferLog;
  private String           transferType;
  private String           transferMethod;
  private java.util.Date   startDateTime;
  private java.util.Date   endDateTime;
  private String           fileName;
  private BigDecimal       fileSize;
  private String           performCompression;
  private Integer          idAnalysis;
  private Integer          idRequest;
  private Integer          idLab;
  private Lab              lab;
  private Request          request;
  private String           emailAddress;
  private String           ipAddress;
  private Integer          idAppUser;
  
  public Integer getIdTransferLog() {
    return idTransferLog;
  }
  public void setIdTransferLog(Integer idTransferLog) {
    this.idTransferLog = idTransferLog;
  }
  public String getTransferType() {
    return transferType;
  }
  public void setTransferType(String transferType) {
    this.transferType = transferType;
  }
  public String getTransferMethod() {
    return transferMethod;
  }
  public void setTransferMethod(String transferMethod) {
    this.transferMethod = transferMethod;
  }
  public java.util.Date getStartDateTime() {
    return startDateTime;
  }
  public void setStartDateTime(java.util.Date startDateTime) {
    this.startDateTime = startDateTime;
  }
  public java.util.Date getEndDateTime() {
    return endDateTime;
  }
  public void setEndDateTime(java.util.Date endDateTime) {
    this.endDateTime = endDateTime;
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
  public Integer getIdAnalysis() {
    return idAnalysis;
  }
  public void setIdAnalysis(Integer idAnalysis) {
    this.idAnalysis = idAnalysis;
  }
  public Integer getIdRequest() {
    return idRequest;
  }
  public void setIdRequest(Integer idRequest) {
    this.idRequest = idRequest;
  }
  public Integer getIdLab() {
    return idLab;
  }
  public void setIdLab(Integer idLab) {
    this.idLab = idLab;
  }
  public String getPerformCompression() {
    return performCompression;
  }
  public void setPerformCompression(String performCompression) {
    this.performCompression = performCompression;
  }
  public Lab getLab() {
    return lab;
  }
  public void setLab(Lab lab) {
    this.lab = lab;
  }
  public Request getRequest() {
    return request;
  }
  public void setRequest(Request request) {
    this.request = request;
  } 
  public String getEmailAddress() {
    return emailAddress;
  }
  public void setEmailAddress(String address) {
    this.emailAddress = address;
  }
  public String getIpAddress() {
    return ipAddress;
  }
  public void setIpAddress(String address) {
    this.ipAddress = address;
  }
  public Integer getIdAppUser() {
    return idAppUser;
  }
  public void setIdAppUser(Integer id) {
    this.idAppUser = id;
  }
}