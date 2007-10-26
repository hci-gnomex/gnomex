package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class SamplePrepMethodSampleType extends DictionaryEntry implements Serializable {
  private Integer  idSamplePrepMethodSampleType;
  private Integer  idSamplePrepMethod;
  private Integer  idSampleType;
  private String   isDefaultForSampleType;
  private String   isActive;
  
  public String getDisplay() {
    String display = this.idSamplePrepMethod.toString() + this.idSampleType.toString();
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
  

}