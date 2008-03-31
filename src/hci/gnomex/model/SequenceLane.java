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
  
  
    
}