package hci.gnomex.model;



import java.sql.Date;

import hci.hibernate3utils.HibernateDetailObject;



public class WorkItem extends HibernateDetailObject {
  
  private Integer        idWorkItem;
  private String         codeStepNext;
  private Date           createDate;
  private Integer        idRequest;
  private Sample         sample;
  private LabeledSample  labeledSample;
  private Hybridization  hybridization;
  
 
  public String getCodeStepNext() {
    return codeStepNext;
  }
  
  public void setCodeStepNext(String codeStepNext) {
    this.codeStepNext = codeStepNext;
  }

  
  public Hybridization getHybridization() {
    return hybridization;
  }

  
  public void setHybridization(Hybridization hybridization) {
    this.hybridization = hybridization;
  }

  
  public Integer getIdRequest() {
    return idRequest;
  }

  
  public void setIdRequest(Integer idRequest) {
    this.idRequest = idRequest;
  }

  
  public Integer getIdWorkItem() {
    return idWorkItem;
  }

  
  public void setIdWorkItem(Integer idWorkItem) {
    this.idWorkItem = idWorkItem;
  }

  
  public LabeledSample getLabeledSample() {
    return labeledSample;
  }

  
  public void setLabeledSample(LabeledSample labeledSample) {
    this.labeledSample = labeledSample;
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
}