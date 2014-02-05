package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;

import java.io.Serializable;



public class ExperimentFactor extends DictionaryEntry implements Serializable, OntologyEntry,  DictionaryEntryUserOwned {
  public static final String        OTHER = "OTHER";
  
  private String   codeExperimentFactor;
  private String   experimentFactor;
  private String   mageOntologyCode;
  private String   mageOntologyDefinition;
  private String   isActive;
  private Integer  idAppUser;
  
  public String getDisplay() {
    String display = this.getNonNullString(getExperimentFactor());
    return display;
  }

  public String getValue() {
    return getCodeExperimentFactor();
  }
  
  
  public String getExperimentFactor() {
    return experimentFactor;
  }

  
  public void setExperimentFactor(String experimentFactor) {
    this.experimentFactor = experimentFactor;
  }

  
  public String getMageOntologyCode() {
    return mageOntologyCode;
  }

  
  public void setMageOntologyCode(String mageOntologyCode) {
    this.mageOntologyCode = mageOntologyCode;
  }

  
  public String getMageOntologyDefinition() {
    return mageOntologyDefinition;
  }

  
  public void setMageOntologyDefinition(String mageOntologyDefinition) {
    this.mageOntologyDefinition = mageOntologyDefinition;
  }

  
  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }

  
  public String getCodeExperimentFactor() {
    return codeExperimentFactor;
  }

  
  public void setCodeExperimentFactor(String codeExperimentFactor) {
    this.codeExperimentFactor = codeExperimentFactor;
  }

  public void registerMethodsToExcludeFromXML() {
    this.excludeMethodFromXML("getMageOntologyCode");
    this.excludeMethodFromXML("getMageOntologyDefinition");
    super.registerMethodsToExcludeFromXML();
  }

  
  public Integer getIdAppUser() {
    return idAppUser;
  }

  
  public void setIdAppUser(Integer idAppUser) {
    this.idAppUser = idAppUser;
  }
  

}