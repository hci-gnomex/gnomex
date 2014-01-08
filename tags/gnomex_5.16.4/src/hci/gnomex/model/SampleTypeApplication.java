package hci.gnomex.model;

import hci.hibernate3utils.HibernateDetailObject;

public class SampleTypeApplication extends HibernateDetailObject {
  private Integer idSampleTypeApplication;
  private Integer idSampleType;
  private String  codeApplication;
  private Integer idLabelingProtocolDefault;
  private Integer idHybProtocolDefault;
  private Integer idScanProtocolDefault;
  private Integer idFeatureExtractionProtocolDefault;
  private String  isActive;
  
  
  public Integer getIdSampleTypeApplication() {
    return idSampleTypeApplication;
  }
  public void setIdSampleTypeApplication(Integer idSampleTypeApplication) {
    this.idSampleTypeApplication = idSampleTypeApplication;
  }
  public Integer getIdSampleType() {
    return idSampleType;
  }
  public void setIdSampleType(Integer idSampleType) {
    this.idSampleType = idSampleType;
  }
  public String getCodeApplication() {
    return codeApplication;
  }
  public void setCodeApplication(String codeApplication) {
    this.codeApplication = codeApplication;
  }
  public Integer getIdLabelingProtocolDefault() {
    return idLabelingProtocolDefault;
  }
  public void setIdLabelingProtocolDefault(Integer idLabelingProtocolDefault) {
    this.idLabelingProtocolDefault = idLabelingProtocolDefault;
  }
  public Integer getIdHybProtocolDefault() {
    return idHybProtocolDefault;
  }
  public void setIdHybProtocolDefault(Integer idHybProtocolDefault) {
    this.idHybProtocolDefault = idHybProtocolDefault;
  }
  public Integer getIdScanProtocolDefault() {
    return idScanProtocolDefault;
  }
  public void setIdScanProtocolDefault(Integer idScanProtocolDefault) {
    this.idScanProtocolDefault = idScanProtocolDefault;
  }
  public Integer getIdFeatureExtractionProtocolDefault() {
    return idFeatureExtractionProtocolDefault;
  }
  public void setIdFeatureExtractionProtocolDefault(Integer idFeatureExtractionProtocolDefault) {
    this.idFeatureExtractionProtocolDefault = idFeatureExtractionProtocolDefault;
  }
  public String getIsActive() {
    return isActive;
  }
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }

}
