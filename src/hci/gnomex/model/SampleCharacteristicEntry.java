package hci.gnomex.model;

import java.util.Iterator;
import java.util.Set;

import hci.hibernate3utils.HibernateDetailObject;


public class SampleCharacteristicEntry extends HibernateDetailObject {
  public static final String     OTHER_LABEL = "otherLabel";
  
  private Integer idSampleCharacteristicEntry;
  private Integer idSampleCharacteristic;
  private Integer idSample;
  private String  value;
  private String  otherLabel;
  private Set     options;
  private Set     values;
  

  
  public Integer getIdSample() {
    return idSample;
  }
  
  public void setIdSample(Integer idSample) {
    this.idSample = idSample;
  }
  
  public Integer getIdSampleCharacteristicEntry() {
    return idSampleCharacteristicEntry;
  }
  
  public void setIdSampleCharacteristicEntry(Integer idSampleCharacteristicEntry) {
    this.idSampleCharacteristicEntry = idSampleCharacteristicEntry;
  }

  
  public String getValue() {
    return value;
  }

  
  public void setValue(String value) {
    this.value = value;
  }

  
  public String getOtherLabel() {
    return otherLabel;
  }

  
  public void setOtherLabel(String otherLabel) {
    this.otherLabel = otherLabel;
  }

  public Set getOptions() {
    return options;
  }

  public void setOptions(Set options) {
    this.options = options;
  }

  public Set getValues() {
    return values;
  }

  public void setValues(Set values) {
    this.values = values;
  }

  public Integer getIdSampleCharacteristic() {
    return idSampleCharacteristic;
  }

  public void setIdSampleCharacteristic(Integer idSampleCharacteristic) {
    this.idSampleCharacteristic = idSampleCharacteristic;
  }

}