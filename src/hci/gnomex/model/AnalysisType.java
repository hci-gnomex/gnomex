package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class AnalysisType extends DictionaryEntry implements Serializable, DictionaryEntryUserOwned {
  private Integer  idAnalysisType;
  private String   analysisType;
  private String   isActive;
  private Integer  idAppUser;
  
  public String getDisplay() {
    String display = this.getNonNullString(getAnalysisType());
    return display;
  }

  public String getValue() {
    return getIdAnalysisType().toString();
  }


  
  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }

  
  public Integer getIdAnalysisType() {
    return idAnalysisType;
  }

  
  public void setIdAnalysisType(Integer idAnalysisType) {
    this.idAnalysisType = idAnalysisType;
  }

  
  public String getAnalysisType() {
    return analysisType;
  }

  
  public void setAnalysisType(String analysisType) {
    this.analysisType = analysisType;
  }

  
  public Integer getIdAppUser() {
    return idAppUser;
  }

  
  public void setIdAppUser(Integer idAppUser) {
    this.idAppUser = idAppUser;
  }



}