package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class PropertyType extends DictionaryEntry implements Serializable {
  
  public static final String    TEXT         = "TEXT";
  public static final String    URL          = "URL";
  public static final String    CHECKBOX     = "CHECK";
  public static final String    OPTION       = "OPTION";
  public static final String    MULTI_OPTION = "MOPTION";
  
  private String codePropertyType;
  private String name;  
  private String isActive;
  
  public String getDisplay() {
    String display = this.getNonNullString(getName());
    return display;
  }

  public String getValue() {
    return getCodePropertyType();
  }

  
 
  
  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }

  
  public String getCodePropertyType() {
    return codePropertyType;
  }

  
  public void setCodePropertyType(String codePropertyType) {
    this.codePropertyType = codePropertyType;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


}