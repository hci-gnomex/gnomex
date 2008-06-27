package hci.gnomex.model;



import java.sql.Date;

import hci.hibernate3utils.HibernateDetailObject;



public class AnalysisFile extends HibernateDetailObject {
  
  private Integer        idAnalysisFile;
  private Integer        idAnalysis;
  private Analysis       analysis;
  private String         fileName;
  private String         comments;
  private Date           uploadDate;
  

  public Integer getIdAnalysis() {
    return idAnalysis;
  }
  
  public void setIdAnalysis(Integer idAnalysis) {
    this.idAnalysis = idAnalysis;
  }
  
  public Analysis getAnalysis() {
    return analysis;
  }
  
  public void setAnalysis(Analysis analysis) {
    this.analysis = analysis;
  }

  
  public Integer getIdAnalysisFile() {
    return idAnalysisFile;
  }

  
  public void setIdAnalysisFile(Integer idAnalysisFile) {
    this.idAnalysisFile = idAnalysisFile;
  }

  
  public String getFileName() {
    return fileName;
  }

  
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  
  public String getComments() {
    return comments;
  }

  
  public void setComments(String comments) {
    this.comments = comments;
  }

  
  public Date getUploadDate() {
    return uploadDate;
  }

  
  public void setUploadDate(Date uploadDate) {
    this.uploadDate = uploadDate;
  }
  
  
  public void registerMethodsToExcludeFromXML() {
    this.excludeMethodFromXML("getAnalysis");
  }

 
}