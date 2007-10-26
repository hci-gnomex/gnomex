package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class SampleTypeMicroarrayCategory extends DictionaryEntry implements Serializable {
  private Integer  idMicroarrayCategorySampleType;
  private String   codeMicroarrayCategory;
  private Integer  idSampleType;
  private Integer  idLabelingProtocolDefault;
  private Integer  idHybProtocolDefault;
  private Integer  idScanProtocolDefault;
  private Integer  idFeatureExtractionProtocolDefault;
  
  private String   isActive;
  
  public String getDisplay() {
    String display = this.codeMicroarrayCategory + this.idSampleType;
    return display;
  }

  public String getValue() {
    return getIdMicroarrayCategorySampleType().toString();
  }

  
  public String getCodeMicroarrayCategory() {
    return codeMicroarrayCategory;
  }

  
  public void setCodeMicroarrayCategory(String codeMicroarrayCategory) {
    this.codeMicroarrayCategory = codeMicroarrayCategory;
  }

  
  public Integer getIdMicroarrayCategorySampleType() {
    return idMicroarrayCategorySampleType;
  }

  
  public void setIdMicroarrayCategorySampleType(
      Integer idMicroarrayCategorySampleType) {
    this.idMicroarrayCategorySampleType = idMicroarrayCategorySampleType;
  }

  
  public Integer getIdSampleType() {
    return idSampleType;
  }

  
  public void setIdSampleType(Integer idSampleType) {
    this.idSampleType = idSampleType;
  }

  
  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }

  
  public Integer getIdFeatureExtractionProtocolDefault() {
    return idFeatureExtractionProtocolDefault;
  }

  
  public void setIdFeatureExtractionProtocolDefault(
      Integer idFeatureExtractionProtocolDefault) {
    this.idFeatureExtractionProtocolDefault = idFeatureExtractionProtocolDefault;
  }

  
  public Integer getIdHybProtocolDefault() {
    return idHybProtocolDefault;
  }

  
  public void setIdHybProtocolDefault(Integer idHybProtocolDefault) {
    this.idHybProtocolDefault = idHybProtocolDefault;
  }

  
  public Integer getIdLabelingProtocolDefault() {
    return idLabelingProtocolDefault;
  }

  
  public void setIdLabelingProtocolDefault(Integer idLabelingProtocolDefault) {
    this.idLabelingProtocolDefault = idLabelingProtocolDefault;
  }

  
  public Integer getIdScanProtocolDefault() {
    return idScanProtocolDefault;
  }

  
  public void setIdScanProtocolDefault(Integer idScanProtocolDefault) {
    this.idScanProtocolDefault = idScanProtocolDefault;
  }
  

}