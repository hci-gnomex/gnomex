package hci.gnomex.model;



import java.io.File;
import java.math.BigDecimal;
import java.sql.Date;

import hci.hibernate3utils.HibernateDetailObject;



public class AnalysisFile extends HibernateDetailObject {
  
  private Integer        idAnalysisFile;
  private Integer        idAnalysis;
  private Analysis       analysis;
  private String         fileName;
  private String         comments;
  private Date           uploadDate;
  private BigDecimal     fileSize;
  private String         qualifiedFilePath;
  private String         baseFilePath;
  

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

  
  public String getQualifiedFilePath()
  {
    return qualifiedFilePath;
  }

  public void setQualifiedFilePath(String qualifiedFilePath)
  {
    this.qualifiedFilePath = qualifiedFilePath;
  }

  public String getBaseFilePath()
  {
    return baseFilePath;
  }

  public void setBaseFilePath(String baseFilePath)
  {
    this.baseFilePath = baseFilePath;
  }

  public String getFileName() {
    return fileName;
  }

  
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getFullPathName() {
    String fullPathName = getBaseFilePath();
    if ( getQualifiedFilePath() != null && !getQualifiedFilePath().equals("") ) {
      fullPathName += "/" + getQualifiedFilePath();  
    }
    fullPathName += "/" + getFileName();
//    String fullPath = fullPathName.replaceAll("/", "\\\\");
    String fullPath = fullPathName.replace("\\", "/");
    
//    return fullPath;
    return fullPathName;
  }
  public String getQualifiedFileName() {
    String fullPathName = "";
    if ( getQualifiedFilePath() != null && !getQualifiedFilePath().equals("") ) {
      fullPathName += getQualifiedFilePath() + "/";  
    }
    fullPathName += getFileName();
//    String fullPath = fullPathName.replaceAll("/", "\\\\");
//    String fullPath = fullPathName.replace("\\", "/");
    
//    return fullPath;
    return fullPathName;
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

  public BigDecimal getFileSize() {
    return fileSize;
  }

  public void setFileSize(BigDecimal fileSize) {
    this.fileSize = fileSize;
  }
  
  public File getFile(String baseDir) {
    String filePath = "";
    if (baseFilePath == null || baseFilePath.equals("")) {
      String createYear = Analysis.getCreateYear(this.getAnalysis().getCreateDate());
      filePath = baseDir + "/" + createYear + "/" + this.getAnalysis().getNumber();
    } else {
      filePath = baseFilePath;
    }
    if (this.getQualifiedFilePath() != null && !this.getQualifiedFilePath().equals("")) {
      filePath += "/" + this.getQualifiedFilePath();
    }
    filePath +=  "/" + this.getFileName();
    
    return new File(filePath);
  }


 
}