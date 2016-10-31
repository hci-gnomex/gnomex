package hci.gnomex.model;



import hci.gnomex.constants.Constants;
import hci.gnomex.utility.GnomexFile;
import hci.hibernate5utils.HibernateDetailObject;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Date;



public class AnalysisFile extends GnomexFile {

  private Integer        idAnalysisFile;
  private Integer        idAnalysis;
  private Analysis       analysis;
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


  public Date getEffectiveCreateDate() {
    if (uploadDate == null) {
      return createDate;
    } else {
      return uploadDate;
    }
  }
  
  public File getFile(String baseDir) {
    String filePath = "";
    if (baseFilePath == null || baseFilePath.equals("")) {
      String createYear = Analysis.getCreateYear(this.getAnalysis().getCreateDate());
      filePath = baseDir + Constants.FILE_SEPARATOR + createYear + Constants.FILE_SEPARATOR + this.getAnalysis().getNumber();
    } else {
      filePath = baseFilePath;
    }
    if (this.getQualifiedFilePath() != null && !this.getQualifiedFilePath().equals("")) {
      filePath += Constants.FILE_SEPARATOR + this.getQualifiedFilePath();
    }
    filePath +=  Constants.FILE_SEPARATOR + this.getFileName();
    
    return new File(filePath);
  }


 
}