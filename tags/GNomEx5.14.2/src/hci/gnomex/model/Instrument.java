package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class Instrument extends DictionaryEntry implements Serializable {
  
  
  private Integer idInstrument;
  private String  instrument;
  private String  isActive;
  
  public String getDisplay() {
    String display = this.getNonNullString(getInstrument());
    return display;
  }

  public String getValue() {
    return getIdInstrument().toString();
  }

  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }

  
  public String getInstrument() {
    return instrument;
  }

  
  public void setInstrument(String instrument) {
    this.instrument = instrument;
  }

  
  public Integer getIdInstrument() {
    return idInstrument;
  }

  
  public void setIdInstrument(Integer idInstrument) {
    this.idInstrument = idInstrument;
  }

  
  
}