package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class SampleTypeRequestCategory extends DictionaryEntry implements Serializable {
  private Integer  idSampleTypeRequestCategory;
  private Integer  idSampleType;
  private String   codeRequestCategory;
  private SampleType       sampleType;
  private RequestCategory  requestCategory;
  
  public String getDisplay() {
    String display =  (getSampleType() != null ? getSampleType().getSampleType() : "?") + 
                      " - " + 
                      (getRequestCategory() != null ? getRequestCategory().getRequestCategory() : "?");
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

  
  private RequestCategory getRequestCategory() {
    return requestCategory;
  }

  
  private void setRequestCategory(RequestCategory requestCategory) {
    this.requestCategory = requestCategory;
  }

  
  private SampleType getSampleType() {
    return sampleType;
  }

  
  private void setSampleType(SampleType sampleType) {
    this.sampleType = sampleType;
  }

  

}