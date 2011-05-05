package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.util.Iterator;
import java.util.Set;

import hci.dictionary.model.DictionaryEntry;
import hci.hibernate3utils.HibernateDetailObject;


public class SampleCharacteristicOption  extends DictionaryEntry {
  
  private Integer idSampleCharacteristicOption;
  private String  codeSampleCharacteristic;
  private String  option;
  private Integer sortOrder;
  private String  isActive;
  
  public String getDisplay() {
    return option != null ? option : "";
  }
  
  public String getValue() {
    return idSampleCharacteristicOption.toString();
  }
  public String getIsActive() {
    return isActive;
  }

  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }

  public String getCodeSampleCharacteristic() {
    return codeSampleCharacteristic;
  }
  
  public void setCodeSampleCharacteristic(String codeSampleCharacteristic) {
    this.codeSampleCharacteristic = codeSampleCharacteristic;
  }
  
  
  public Integer getIdSampleCharacteristicOption() {
    return idSampleCharacteristicOption;
  }
  
  public void setIdSampleCharacteristicOption(Integer idSampleCharacteristicOption) {
    this.idSampleCharacteristicOption = idSampleCharacteristicOption;
  }

  
  public String getOption() {
    return option;
  }

  
  public void setOption(String option) {
    this.option = option;
  }

  public Integer getSortOrder() {
    return sortOrder;
  }

  public void setSortOrder(Integer sortOrder) {
    this.sortOrder = sortOrder;
  }

  
    
}