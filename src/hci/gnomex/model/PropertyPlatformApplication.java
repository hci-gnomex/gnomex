package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;

import java.io.Serializable;



public class PropertyPlatformApplication extends DictionaryEntry implements Comparable, Serializable {

  private Integer         idPlatformApplication;
  private Integer         idProperty;
  private String          codeRequestCategory;
  private RequestCategory requestCategory;
  private String          codeApplication;
  private Application     application;

  public String getDisplay() {
    String display = this.getNonNullString(getRequestCategory().getRequestCategory());
    return display;
  }
  
  public String getApplicationDisplay() {
    String display = "";
    if(getApplication() != null) {
      display = this.getNonNullString(getApplication().getApplication());
    }
    return display;
  }


  public Integer getIdPlatformApplication() {
    return idPlatformApplication;
  }

  public void setIdPlatformApplication(Integer idPlatformApplication) {
    this.idPlatformApplication = idPlatformApplication;
  }

  public Integer getIdProperty() {
    return idProperty;
  }

  public void setIdProperty(Integer idProperty) {
    this.idProperty = idProperty;
  }

  public RequestCategory getRequestCategory() {
    return requestCategory;
  }

  public void setRequestCategory(RequestCategory requestCategory) {
    this.requestCategory = requestCategory;
  }

  public String getCodeApplication() {
    return codeApplication;
  }

  public void setCodeApplication(String codeApplication) {
    this.codeApplication = codeApplication;
  }

  public Application getApplication() {
    return application;
  }

  public void setApplication(Application application) {
    this.application = application;
  }

  public String getValue() {
    return getCodeRequestCategory();
  }

  
  public String getCodeRequestCategory() {
    return codeRequestCategory;
  }
  
  public void setCodeRequestCategory(String codeRequestCategory) {
    this.codeRequestCategory = codeRequestCategory;
  }

}