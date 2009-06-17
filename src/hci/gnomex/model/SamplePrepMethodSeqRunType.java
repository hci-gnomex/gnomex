package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class SamplePrepMethodSeqRunType extends DictionaryEntry implements Serializable {
  private Integer  idSamplePrepMethodSeqRunType;
  private Integer  idSamplePrepMethod;
  private Integer  idSeqRunType;
  private SamplePrepMethod samplePrepMethod;
  private SeqRunType       seqRunType;
  
  public String getDisplay() {
    String display = (getSamplePrepMethod() != null ? getSamplePrepMethod().getSamplePrepMethod() : "?") + " - " +
                     (getSeqRunType() != null ? getSeqRunType().getSeqRunType() : "?");
    return display;
  }

  public String getValue() {
    return idSamplePrepMethodSeqRunType.toString();
  }

  
  public Integer getIdSamplePrepMethodSeqRunType() {
    return idSamplePrepMethodSeqRunType;
  }

  
  public void setIdSamplePrepMethodSeqRunType(
      Integer idSamplePrepMethodSeqRunType) {
    this.idSamplePrepMethodSeqRunType = idSamplePrepMethodSeqRunType;
  }

  
  public Integer getIdSamplePrepMethod() {
    return idSamplePrepMethod;
  }

  
  public void setIdSamplePrepMethod(Integer idSamplePrepMethod) {
    this.idSamplePrepMethod = idSamplePrepMethod;
  }

  
  public Integer getIdSeqRunType() {
    return idSeqRunType;
  }

  
  public void setIdSeqRunType(Integer idSeqRunType) {
    this.idSeqRunType = idSeqRunType;
  }

  
  private SamplePrepMethod getSamplePrepMethod() {
    return samplePrepMethod;
  }

  
  private void setSamplePrepMethod(SamplePrepMethod samplePrepMethod) {
    this.samplePrepMethod = samplePrepMethod;
  }

  
  private SeqRunType getSeqRunType() {
    return seqRunType;
  }

  
  private void setSeqRunType(SeqRunType seqRunType) {
    this.seqRunType = seqRunType;
  }



  

}