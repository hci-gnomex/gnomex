package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;

import java.io.Serializable;



public class FeatureExtractionProtocol extends DictionaryEntry implements Serializable {
  private Integer  idFeatureExtractionProtocol;
  private String   featureExtractionProtocol;
  private String   codeRequestCategory;
  private String   description;
  private String   url;
  private String   isActive;
  
  public String getDisplay() {
    String display = this.getNonNullString(getFeatureExtractionProtocol());
    return display;
  }

  public String getValue() {
    return getIdFeatureExtractionProtocol().toString();
  }
  
  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }

  
  public String getCodeRequestCategory() {
    return codeRequestCategory;
  }

  
  public void setCodeRequestCategory(String codeRequestCategory) {
    this.codeRequestCategory = codeRequestCategory;
  }

  
  public String getDescription() {
    return description;
  }

  
  public void setDescription(String description) {
    this.description = description;
  }

  
  public Integer getIdFeatureExtractionProtocol() {
    return idFeatureExtractionProtocol;
  }

  
  public void setIdFeatureExtractionProtocol(Integer idFeatureExtractionProtocol) {
    this.idFeatureExtractionProtocol = idFeatureExtractionProtocol;
  }

  
  public String getFeatureExtractionProtocol() {
    return featureExtractionProtocol;
  }

  
  public void setFeatureExtractionProtocol(String featureExtractionProtocol) {
    this.featureExtractionProtocol = featureExtractionProtocol;
  }

  
  public String getUrl() {
    return url;
  }

  
  public void setUrl(String url) {
    this.url = url;
  }


}