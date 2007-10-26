package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class HybProtocol extends DictionaryEntry implements Serializable {
  private Integer  idHybProtocol;
  private String   hybProtocol;
  private String   codeRequestCategory;
  private String   description;
  private String   url;
  private String   isActive;
  
  public String getDisplay() {
    String display = this.getNonNullString(getHybProtocol());
    return display;
  }

  public String getValue() {
    return getIdHybProtocol().toString();
  }
  
  public Integer getIdHybProtocol() {
    return idHybProtocol;
  }

  
  public void setIdHybProtocol(Integer idHybProtocol) {
    this.idHybProtocol = idHybProtocol;
  }

  
  public String getHybProtocol() {
    return hybProtocol;
  }

  
  public void setHybProtocol(String hybProtocol) {
    this.hybProtocol = hybProtocol;
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

  
  public String getUrl() {
    return url;
  }

  
  public void setUrl(String url) {
    this.url = url;
  }

}