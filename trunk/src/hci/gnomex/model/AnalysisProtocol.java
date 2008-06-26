package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class AnalysisProtocol extends DictionaryEntry implements Serializable {
  private Integer  idAnalysisProtocol;
  private String   analysisProtocol;
  private Integer  idAnalysisType;
  private String   isActive;
  
  public String getDisplay() {
    String display = this.getNonNullString(getAnalysisProtocol());
    return display;
  }

  public String getValue() {
    return getIdAnalysisProtocol().toString();
  }


  
  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }

  
  public Integer getIdAnalysisProtocol() {
    return idAnalysisProtocol;
  }

  
  public void setIdAnalysisProtocol(Integer idAnalysisProtocol) {
    this.idAnalysisProtocol = idAnalysisProtocol;
  }

  
  public String getAnalysisProtocol() {
    return analysisProtocol;
  }

  
  public void setAnalysisProtocol(String analysisProtocol) {
    this.analysisProtocol = analysisProtocol;
  }

  
  public Integer getIdAnalysisType() {
    return idAnalysisType;
  }

  
  public void setIdAnalysisType(Integer idAnalysisType) {
    this.idAnalysisType = idAnalysisType;
  }



}