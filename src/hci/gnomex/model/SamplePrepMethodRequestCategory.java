package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class SamplePrepMethodRequestCategory extends DictionaryEntry implements Serializable {
  private Integer  idSamplePrepMethodRequestCategory;
  private Integer  idSamplePrepMethod;
  private String   codeRequestCategory;
  
  public String getDisplay() {
    String display = this.idSamplePrepMethodRequestCategory.toString();
    return display;
  }

  public String getValue() {
    return idSamplePrepMethodRequestCategory.toString();
  }

  
  public Integer getIdSamplePrepMethodRequestCategory() {
    return idSamplePrepMethodRequestCategory;
  }

  
  public void setIdSamplePrepMethodRequestCategory(
      Integer idSamplePrepMethodRequestCategory) {
    this.idSamplePrepMethodRequestCategory = idSamplePrepMethodRequestCategory;
  }

  
  public Integer getIdSamplePrepMethod() {
    return idSamplePrepMethod;
  }

  
  public void setIdSamplePrepMethod(Integer idSamplePrepMethod) {
    this.idSamplePrepMethod = idSamplePrepMethod;
  }

  
  public String getCodeRequestCategory() {
    return codeRequestCategory;
  }

  
  public void setCodeRequestCategory(String codeRequestCategory) {
    this.codeRequestCategory = codeRequestCategory;
  }

  

}