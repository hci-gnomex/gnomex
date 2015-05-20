package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class SampleSource extends DictionaryEntry implements Serializable {
  
  
  private Integer idSampleSource;
  private String  sampleSource;
  private String  isActive;
  
  public String getDisplay() {
    String display = this.getNonNullString(getSampleSource());
    return display;
  }

  public String getValue() {
    return getIdSampleSource().toString();
  }

  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }

  
  public String getSampleSource() {
    return sampleSource;
  }

  
  public void setSampleSource(String sampleSource) {
    this.sampleSource = sampleSource;
  }

  
  public Integer getIdSampleSource() {
    return idSampleSource;
  }

  
  public void setIdSampleSource(Integer idSampleSource) {
    this.idSampleSource = idSampleSource;
  }

  
  
}