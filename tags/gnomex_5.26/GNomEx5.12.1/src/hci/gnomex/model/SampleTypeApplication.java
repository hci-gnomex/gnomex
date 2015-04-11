package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class SampleTypeApplication extends DictionaryEntry implements Serializable {
  private Integer  idSampleTypeApplication;
  private String   codeApplication;
  private Integer  idSampleType;
  private Integer  idLabelingProtocolDefault;
  private Integer  idHybProtocolDefault;
  private Integer  idScanProtocolDefault;
  private Integer  idFeatureExtractionProtocolDefault;
  private SampleType          sampleType;
  private Application  application;
  
  private String   isActive;
  
  public String getDisplay() {
    String display =  (getSampleType() != null ? getSampleType().getSampleType() : "?") + 
                      " - " + 
                      (getApplication() != null ? getApplication().getApplication() : "?");
    return display;
  }

  public String getValue() {
    return getIdSampleTypeApplication().toString();
  }

  
  public String getCodeApplication() {
    return codeApplication;
  }

  
  public void setCodeApplication(String codeApplication) {
    this.codeApplication = codeApplication;
  }

  
  public Integer getIdSampleTypeApplication() {
    return idSampleTypeApplication;
  }

  
  public void setIdSampleTypeApplication(
      Integer idSampleTypeApplication) {
    this.idSampleTypeApplication = idSampleTypeApplication;
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

  
  private SampleType getSampleType() {
    return sampleType;
  }

  
  private void setSampleType(SampleType sampleType) {
    this.sampleType = sampleType;
  }

  
  private Application getApplication() {
    return application;
  }

  
  private void setApplication(Application application) {
    this.application = application;
  }
  

}