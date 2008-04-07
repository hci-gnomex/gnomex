package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class SolexaLibraryType extends DictionaryEntry implements Serializable {
  private Integer  idSolexaLibraryType;
  private String   solexaLibraryType;
  private String   isActive;
  
  public String getDisplay() {
    String display = this.getNonNullString(getSolexaLibraryType());
    return display;
  }

  public String getValue() {
    return getIdSolexaLibraryType().toString();
  }


  
  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }

  
  public Integer getIdSolexaLibraryType() {
    return idSolexaLibraryType;
  }

  
  public void setIdSolexaLibraryType(Integer idSolexaLibraryType) {
    this.idSolexaLibraryType = idSolexaLibraryType;
  }

  
  public String getSolexaLibraryType() {
    return solexaLibraryType;
  }

  
  public void setSolexaLibraryType(String solexaLibraryType) {
    this.solexaLibraryType = solexaLibraryType;
  }


}