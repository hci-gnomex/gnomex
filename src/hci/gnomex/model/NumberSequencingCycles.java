package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class NumberSequencingCycles extends DictionaryEntry implements Serializable {
  private Integer  idNumberSequencingCycles;
  private Integer  numberSequencingCycles;
  private String   isActive;
  
  public String getDisplay() {
    String display = this.getNonNullString(getNumberSequencingCycles().toString());
    return display;
  }

  public String getValue() {
    return getIdNumberSequencingCycles().toString();
  }


  
  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }

  
  public Integer getIdNumberSequencingCycles() {
    return idNumberSequencingCycles;
  }

  
  public void setIdNumberSequencingCycles(Integer idNumberSequencingCycles) {
    this.idNumberSequencingCycles = idNumberSequencingCycles;
  }

  
  public Integer getNumberSequencingCycles() {
    return numberSequencingCycles;
  }

  
  public void setNumberSequencingCycles(Integer numberSequencingCycles) {
    this.numberSequencingCycles = numberSequencingCycles;
  }

}