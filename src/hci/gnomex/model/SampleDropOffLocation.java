package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class SampleDropOffLocation extends DictionaryEntry implements Serializable {
  
  
  private Integer idSampleDropOffLocation;
  private String  sampleDropOffLocation;
  private String  isActive;
  
  public String getDisplay() {
    String display = this.getNonNullString(getSampleDropOffLocation());
    return display;
  }

  public String getValue() {
    return getIdSampleDropOffLocation().toString();
  }

  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }

  
  public String getSampleDropOffLocation() {
    return sampleDropOffLocation;
  }

  
  public void setSampleDropOffLocation(String sampleDropOffLocation) {
    this.sampleDropOffLocation = sampleDropOffLocation;
  }

  
  public Integer getIdSampleDropOffLocation() {
    return idSampleDropOffLocation;
  }

  
  public void setIdSampleDropOffLocation(Integer idSampleDropOffLocation) {
    this.idSampleDropOffLocation = idSampleDropOffLocation;
  }

  
  
}