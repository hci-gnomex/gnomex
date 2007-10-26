package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class SampleType extends DictionaryEntry implements Serializable {
  private Integer  idSampleType;
  private String   sampleType;
  private Integer  sortOrder;
  private String   isActive;
  
  public String getDisplay() {
    String display = this.getNonNullString(getSampleType());
    return display;
  }

  public String getValue() {
    return getIdSampleType().toString();
  }
  
  public Integer getIdSampleType() {
    return idSampleType;
  }

  
  public void setIdSampleType(Integer idSampleType) {
    this.idSampleType = idSampleType;
  }

  
  public String getSampleType() {
    return sampleType;
  }

  
  public void setSampleType(String sampleType) {
    this.sampleType = sampleType;
  }

  
  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }

  
  public Integer getSortOrder() {
    return sortOrder;
  }

  
  public void setSortOrder(Integer sortOrder) {
    this.sortOrder = sortOrder;
  }

 

}