package hci.gnomex.model;

import java.util.Iterator;
import java.util.Set;

import hci.hibernate3utils.HibernateDetailObject;


public class SampleCharacteristicEntryValue extends HibernateDetailObject {
  
  private Integer idSampleCharacteristicEntryValue;
  private String  value;
  private Integer idSampleCharacteristicEntry;
  
  
  
  public Integer getIdSampleCharacteristicEntryValue() {
    return idSampleCharacteristicEntryValue;
  }
  
  public void setIdSampleCharacteristicEntryValue(Integer idSampleCharacteristicEntryValue) {
    this.idSampleCharacteristicEntryValue = idSampleCharacteristicEntryValue;
  }

  
  public String getValue() {
    return value;
  }

  
  public void setValue(String value) {
    this.value = value;
  }

  public Integer getIdSampleCharacteristicEntry() {
    return idSampleCharacteristicEntry;
  }

  public void setIdSampleCharacteristicEntry(Integer idSampleCharacteristicEntry) {
    this.idSampleCharacteristicEntry = idSampleCharacteristicEntry;
  }

  
    
}