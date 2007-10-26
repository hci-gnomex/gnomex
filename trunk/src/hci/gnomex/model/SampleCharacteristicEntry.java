package hci.gnomex.model;

import java.util.Iterator;
import java.util.Set;

import hci.hibernate3utils.HibernateDetailObject;


public class SampleCharacteristicEntry extends HibernateDetailObject {
  public static final String     OTHER_LABEL = "otherLabel";
  
  private Integer idSampleCharacteristicEntry;
  private String  codeSampleCharacteristic;
  private Integer idSample;
  private String  value;
  private String  otherLabel;
  
  public String getCodeSampleCharacteristic() {
    return codeSampleCharacteristic;
  }
  
  public void setCodeSampleCharacteristic(String codeSampleCharacteristic) {
    this.codeSampleCharacteristic = codeSampleCharacteristic;
  }
  
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
  
  
    
}