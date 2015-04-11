package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;

import java.io.Serializable;



public class RNAPrepType extends DictionaryEntry implements Serializable {
  public static final String   TYPE_FFPE            = "FFPE Scrolls";
  public static final String   TYPE_MICRO           = "Micro-Dissection";
  
  private String     codeRNAPrepType;
  private String     rnaPrepType;
  private String     isActive;
  
  public String getDisplay() {
    return rnaPrepType;
  }

  public String getValue() {
    return codeRNAPrepType;
  }

  
  
  public String getCodeRNAPrepType() {
    return codeRNAPrepType;
  }

  
  public void setCodeRNAPrepType( String codeRNAPrepType ) {
    this.codeRNAPrepType = codeRNAPrepType;
  }

  
  public String getRnaPrepType() {
    return rnaPrepType;
  }

  
  public void setRnaPrepType( String rnaPrepType ) {
    this.rnaPrepType = rnaPrepType;
  }

  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }



  

}