package hci.gnomex.model;

import java.util.Iterator;
import java.util.Set;
import java.util.Date;

import hci.hibernate3utils.HibernateDetailObject;


public class SequenceLane extends HibernateDetailObject {
  
  private Integer idSequenceLane;
  private String  number;
  private Date    createDate;
  private Integer idSample;
  private Sample  sample;
  private Integer idRequest;
  private Integer idFlowCellType;
  private Integer idNumberSequencingCycles;
  private Integer idGenomeBuildAlignTo;
  private String  alignNotes;
  
  public Integer getIdSample() {
    return idSample;
  }
  
  public void setIdSample(Integer idSample) {
    this.idSample = idSample;
  }

  
  public Integer getIdRequest() {
    return idRequest;
  }

  
  public void setIdRequest(Integer idRequest) {
    this.idRequest = idRequest;
  }

  
  public Integer getIdSequenceLane() {
    return idSequenceLane;
  }

  
  public void setIdSequenceLane(Integer idSequenceLane) {
    this.idSequenceLane = idSequenceLane;
  }

  
  public Sample getSample() {
    return sample;
  }

  
  public void setSample(Sample sample) {
    this.sample = sample;
  }

  
  public Date getCreateDate() {
    return createDate;
  }

  
  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  
  public String getNumber() {
    return number;
  }

  
  public void setNumber(String number) {
    this.number = number;
  }

  
  public Integer getIdNumberSequencingCycles() {
    return idNumberSequencingCycles;
  }

  
  public void setIdNumberSequencingCycles(Integer idNumberSequencingCycles) {
    this.idNumberSequencingCycles = idNumberSequencingCycles;
  }

  
  public Integer getIdFlowCellType() {
    return idFlowCellType;
  }

  
  public void setIdFlowCellType(Integer idFlowCellType) {
    this.idFlowCellType = idFlowCellType;
  }

  
  public Integer getIdGenomeBuildAlignTo() {
    return idGenomeBuildAlignTo;
  }

  
  public void setIdGenomeBuildAlignTo(Integer idGenomeBuildAlignTo) {
    this.idGenomeBuildAlignTo = idGenomeBuildAlignTo;
  }

  
  public String getAlignNotes() {
    return alignNotes;
  }

  
  public void setAlignNotes(String alignNotes) {
    this.alignNotes = alignNotes;
  }

  
  
  
    
}