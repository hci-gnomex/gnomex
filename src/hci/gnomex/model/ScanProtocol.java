package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class ScanProtocol extends DictionaryEntry implements Serializable {
  private Integer  idScanProtocol;
  private String   scanProtocol;
  private String   codeRequestCategory;
  private String   description;
  private String   url;
  private String   isActive;
  
  public String getDisplay() {
    String display = this.getNonNullString(getScanProtocol());
    return display;
  }

  public String getValue() {
    return getIdScanProtocol().toString();
  }
  
  public String getIsActive() {
    return isActive;
  }
  
  public String getActiveNote() {
    if (isActive == null || isActive.equals("N")) {
      return " (inactive)";
    } else {
      return "";
    }
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

  
  public Integer getIdScanProtocol() {
    return idScanProtocol;
  }

  
  public void setIdScanProtocol(Integer idScanProtocol) {
    this.idScanProtocol = idScanProtocol;
  }

  
  public String getScanProtocol() {
    return scanProtocol;
  }

  
  public void setScanProtocol(String scanProtocol) {
    this.scanProtocol = scanProtocol;
  }

  
  public String getUrl() {
    return url;
  }

  
  public void setUrl(String url) {
    this.url = url;
  }


}