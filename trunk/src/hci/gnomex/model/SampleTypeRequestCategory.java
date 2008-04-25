package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class SampleTypeRequestCategory extends DictionaryEntry implements Serializable {
  private Integer  idSampleTypeRequestCategory;
  private Integer  idSampleType;
  private String   codeRequestCategory;
  
  public String getDisplay() {
    String display = this.idSampleTypeRequestCategory.toString();
    return display;
  }

  public String getValue() {
    return idSampleTypeRequestCategory.toString();
  }

  
  public Integer getIdSampleTypeRequestCategory() {
    return idSampleTypeRequestCategory;
  }

  
  public void setIdSampleTypeRequestCategory(
      Integer idSampleTypeRequestCategory) {
    this.idSampleTypeRequestCategory = idSampleTypeRequestCategory;
  }

  
  public Integer getIdSampleType() {
    return idSampleType;
  }

  
  public void setIdSampleType(Integer idSampleType) {
    this.idSampleType = idSampleType;
  }

  
  public String getCodeRequestCategory() {
    return codeRequestCategory;
  }

  
  public void setCodeRequestCategory(String codeRequestCategory) {
    this.codeRequestCategory = codeRequestCategory;
  }

  

}