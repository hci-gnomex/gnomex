package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class Label extends DictionaryEntry implements Serializable {
  private Integer  idLabel;
  private String   label;
  private String   isActive;
  
  public String getDisplay() {
    String display = this.getNonNullString(getLabel());
    return display;
  }

  public String getValue() {
    return getIdLabel().toString();
  }
  
  public Integer getIdLabel() {
    return idLabel;
  }

  
  public void setIdLabel(Integer idLabel) {
    this.idLabel = idLabel;
  }

  
  public String getLabel() {
    return label;
  }

  
  public void setLabel(String label) {
    this.label = label;
  }

  
  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }

}