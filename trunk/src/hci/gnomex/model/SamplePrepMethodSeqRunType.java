package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class SamplePrepMethodSeqRunType extends DictionaryEntry implements Serializable {
  private Integer  idSamplePrepMethodSeqRunType;
  private Integer  idSamplePrepMethod;
  private Integer  idSeqRunType;
  
  public String getDisplay() {
    String display = this.idSamplePrepMethodSeqRunType.toString();
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



  

}