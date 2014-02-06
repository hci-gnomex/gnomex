package hci.gnomex.model;

import hci.hibernate3utils.HibernateDetailObject;

public class SampleExperimentFile extends HibernateDetailObject {
  private Integer idSampleExperimentFile;
  private Integer idSample;
  private Integer idExpFileRead1;
  private Integer idExpFileRead2;
  private Integer seqRunNumber;
    
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
  public Integer getIdExpFileRead1() {
    return idExpFileRead1;
  }
  public void setIdExpFileRead1(Integer idExpFileRead1) {
    this.idExpFileRead1 = idExpFileRead1;
  }
  public Integer getIdExpFileRead2() {
    return idExpFileRead2;
  }
  public void setIdExpFileRead2(Integer idExpFileRead2) {
    this.idExpFileRead2 = idExpFileRead2;
  }
  public Integer getSeqRunNumber() {
    return seqRunNumber;
  }
  public void setSeqRunNumber(Integer seqRunNumber) {
    this.seqRunNumber = seqRunNumber;
  }

}
