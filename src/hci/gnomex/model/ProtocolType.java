package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class ProtocolType extends DictionaryEntry implements Serializable {
  private String  codeProtocolType;
  private String  protocolType;
  private String  isActive;
  
  public String getDisplay() {
    String display = this.getNonNullString(getProtocolType());
    return display;
  }

  public String getValue() {
    return getCodeProtocolType();
  }

  
  public String getCodeProtocolType() {
    return codeProtocolType;
  }

  
  public void setCodeProtocolType(String codeProtocolType) {
    this.codeProtocolType = codeProtocolType;
  }

  
  public String getProtocolType() {
    return protocolType;
  }

  
  public void setProtocolType(String protocolType) {
    this.protocolType = protocolType;
  }

  
  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }

}