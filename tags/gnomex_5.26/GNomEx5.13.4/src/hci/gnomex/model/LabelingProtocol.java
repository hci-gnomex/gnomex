package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class LabelingProtocol extends DictionaryEntry implements Serializable {
  private Integer  idLabelingProtocol;
  private String   labelingProtocol;
  private String   codeRequestCategory;
  private String   description;
  private String   url;
  private String   isActive;
  
  public String getDisplay() {
    String display = this.getNonNullString(getLabelingProtocol());
    if (isActive == null || isActive.equals("N")) {
      display += " (inactive)";
    }

    return display;
  }

  public String getValue() {
    return getIdLabelingProtocol().toString();
  }
  
  public Integer getIdLabelingProtocol() {
    return idLabelingProtocol;
  }

  
  public void setIdLabelingProtocol(Integer idLabelingProtocol) {
    this.idLabelingProtocol = idLabelingProtocol;
  }

  
  public String getLabelingProtocol() {
    return labelingProtocol;
  }

  
  public void setLabelingProtocol(String labelingProtocol) {
    this.labelingProtocol = labelingProtocol;
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