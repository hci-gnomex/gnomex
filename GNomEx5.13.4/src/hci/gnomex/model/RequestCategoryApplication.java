package hci.gnomex.model;


import hci.dictionary.model.DictionaryEntry;

import java.io.Serializable;



public class RequestCategoryApplication extends DictionaryEntry implements Serializable {
  

  private String codeApplication;
  private String codeRequestCategory;
  private RequestCategory    requestCategory;
  private Application application;
  private Integer  idLabelingProtocolDefault;
  private Integer  idHybProtocolDefault;
  private Integer  idScanProtocolDefault;
  private Integer  idFeatureExtractionProtocolDefault;
 
  
  public String getDisplay() {
    String display =  (getRequestCategory() != null ? getRequestCategory().getRequestCategory() : "?") + 
                      " - " + 
                      (getApplication() != null ? getApplication().getApplication() : "?");
    return display;
  }

  public String getValue() {
    return getCodeRequestCategory() + " " + getCodeApplication();
  }

  
  public String getCodeApplication() {
    return codeApplication;
  }

  
  public void setCodeApplication(String codeApplication) {
    this.codeApplication = codeApplication;
  }

  
  public String getCodeRequestCategory() {
    return codeRequestCategory;
  }

  
  public void setCodeRequestCategory(String codeRequestCategory) {
    this.codeRequestCategory = codeRequestCategory;
  }

  /* Override equals() to handle dual key */
  public boolean equals(Object obj) {
    boolean tmpIsEqual = false;
    if (this.getCodeApplication() == null || this.getCodeRequestCategory() == null) {
      tmpIsEqual = super.equals(obj);
    }
    else {
      if (this == obj) {
        tmpIsEqual = true;
      }
      else if (obj instanceof RequestCategoryApplication) {
        if (this.getCodeRequestCategory().equals(((RequestCategoryApplication) obj).getCodeRequestCategory())
            && this.getCodeApplication().equals(((RequestCategoryApplication) obj).getCodeApplication())) {
          tmpIsEqual = true;
        }
      }
    }
    return tmpIsEqual;
  }

  /* Override hashCode() to handle dual key */
  public int hashCode() {
    if (this.getCodeApplication() == null || this.getCodeRequestCategory() == null) {
      return super.hashCode();
    }
    else {
      // Use exclusive or between hashCodes to get new result
      return this.getCodeApplication().hashCode() ^ this.getCodeRequestCategory().hashCode();
    }
  }

  
  private RequestCategory getRequestCategory() {
    return requestCategory;
  }

  
  private void setRequestCategory(RequestCategory requestCategory) {
    this.requestCategory = requestCategory;
  }

  
  private Application getApplication() {
    return application;
  }

  
  private void setApplication(Application application) {
    this.application = application;
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

  public void setIdFeatureExtractionProtocolDefault(
      Integer idFeatureExtractionProtocolDefault) {
    this.idFeatureExtractionProtocolDefault = idFeatureExtractionProtocolDefault;
  }

  
}