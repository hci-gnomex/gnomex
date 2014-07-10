package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;

import java.io.Serializable;



public class DNAPrepType extends DictionaryEntry implements Serializable {
  public static final String   TYPE_FFPE            = "FFPE Scrolls";
  public static final String   TYPE_MICRO           = "Micro-Dissection";
  
  private String     codeDNAPrepType;
  private String     dnaPrepType;
  private String     isActive;
  
  public String getDisplay() {
    return dnaPrepType;
  }

  public String getValue() {
    return codeDNAPrepType;
  }

  
  
  public String getCodeDNAPrepType() {
    return codeDNAPrepType;
  }

  
  public void setCodeDNAPrepType( String codeDNAPrepType ) {
    this.codeDNAPrepType = codeDNAPrepType;
  }

  
  public String getDnaPrepType() {
    return dnaPrepType;
  }

  
  public void setDnaPrepType( String dnaPrepType ) {
    this.dnaPrepType = dnaPrepType;
  }

  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }



  

}