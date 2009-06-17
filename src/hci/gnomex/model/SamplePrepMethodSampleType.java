package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class SamplePrepMethodSampleType extends DictionaryEntry implements Serializable {
  private Integer  idSamplePrepMethodSampleType;
  private Integer  idSamplePrepMethod;
  private Integer  idSampleType;
  private String   isDefaultForSampleType;
  private String   isActive;
  private SamplePrepMethod samplePrepMethod;
  private SampleType       sampleType;
  
  public String getDisplay() {
    String display = (getSamplePrepMethod() != null ? getSamplePrepMethod().getSamplePrepMethod() : "?") + " - " +
    (getSampleType() != null ? getSampleType().getSampleType() : "?");

    return display;
  }

  public String getValue() {
    return idSamplePrepMethodSampleType.toString();
  }

  

  public Integer getIdSampleType() {
    return idSampleType;
  }

  
  public void setIdSampleType(Integer idSampleType) {
    this.idSampleType = idSampleType;
  }

  
  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }

  
  public Integer getIdSamplePrepMethod() {
    return idSamplePrepMethod;
  }

  
  public void setIdSamplePrepMethod(Integer idSamplePrepMethod) {
    this.idSamplePrepMethod = idSamplePrepMethod;
  }

  
  public Integer getIdSamplePrepMethodSampleType() {
    return idSamplePrepMethodSampleType;
  }

  
  public void setIdSamplePrepMethodSampleType(Integer idSamplePrepMethodSampleType) {
    this.idSamplePrepMethodSampleType = idSamplePrepMethodSampleType;
  }


  
  public String getIsDefaultForSampleType() {
    return isDefaultForSampleType;
  }

  
  public void setIsDefaultForSampleType(String isDefaultForSampleType) {
    this.isDefaultForSampleType = isDefaultForSampleType;
  }

  
  private SamplePrepMethod getSamplePrepMethod() {
    return samplePrepMethod;
  }

  
  private void setSamplePrepMethod(SamplePrepMethod samplePrepMethod) {
    this.samplePrepMethod = samplePrepMethod;
  }

  
  private SampleType getSampleType() {
    return sampleType;
  }

  
  private void setSampleType(SampleType sampleType) {
    this.sampleType = sampleType;
  }

  

}