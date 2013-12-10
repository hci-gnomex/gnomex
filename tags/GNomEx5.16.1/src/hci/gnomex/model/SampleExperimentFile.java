package hci.gnomex.model;

import hci.hibernate3utils.HibernateDetailObject;

public class SampleExperimentFile extends HibernateDetailObject {
  private Integer idSampleExperimentFile;
  private Integer idSample;
  private Integer idExperimentFile;
  private String  codeSampleFileType;
  
  
  public Integer getIdSampleExperimentFile() {
    return idSampleExperimentFile;
  }
  public void setIdSampleExperimentFile(Integer idSampleExperimentFile) {
    this.idSampleExperimentFile = idSampleExperimentFile;
  }
  public Integer getIdSample() {
    return idSample;
  }
  public void setIdSample(Integer idSample) {
    this.idSample = idSample;
  }
  public Integer getIdExperimentFile() {
    return idExperimentFile;
  }
  public void setIdExperimentFile(Integer idExperimentFile) {
    this.idExperimentFile = idExperimentFile;
  }
  public String getCodeSampleFileType() {
    return codeSampleFileType;
  }
  public void setCodeSampleFileType(String codeSampleFileType) {
    this.codeSampleFileType = codeSampleFileType;
  }
  
  

}
