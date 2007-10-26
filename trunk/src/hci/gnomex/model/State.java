package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class State extends DictionaryEntry implements Serializable {
  private String codeState;
  private String state;
  private String isActive;
  
  public String getDisplay() {
    String display = this.getNonNullString(getState());
    return display;
  }

  public String getValue() {
    return getCodeState();
  }

  
  public String getCodeState() {
    return codeState;
  }

  
  public void setCodeState(String codeState) {
    this.codeState = codeState;
  }

  
  public String getState() {
    return state;
  }
  
  

  
  public void setState(String state) {
    this.state = state;
  }

  
  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }

}