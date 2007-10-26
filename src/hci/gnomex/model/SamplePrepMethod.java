package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class SamplePrepMethod extends DictionaryEntry implements Serializable {
  private Integer  idSamplePrepMethod;
  private String   samplePrepMethod;
  private String   isActive;
  
  public String getDisplay() {
    String display = this.getNonNullString(getSamplePrepMethod());
    return display;
  }

  public String getValue() {
    return getIdSamplePrepMethod().toString();
  }
  
  public Integer getIdSamplePrepMethod() {
    return idSamplePrepMethod;
  }

  
  public void setIdSamplePrepMethod(Integer idSamplePrepMethod) {
    this.idSamplePrepMethod = idSamplePrepMethod;
  }

  
  public String getSamplePrepMethod() {
    return samplePrepMethod;
  }

  
  public void setSamplePrepMethod(String samplePrepMethod) {
    this.samplePrepMethod = samplePrepMethod;
  }

  
  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }

  
}