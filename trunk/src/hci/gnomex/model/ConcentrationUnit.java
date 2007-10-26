package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class ConcentrationUnit extends DictionaryEntry implements Serializable, OntologyEntry {
  
  public static final String                        DEFAULT_SAMPLE_CONCENTRATION_UNIT = "ng/µl";
  
  private String  codeConcentrationUnit;
  private String  concentrationUnit;
  private String  mageOntologyCode;
  private String  mageOntologyDefinition;
  private String  isActive;
  
  public String getDisplay() {
    String display = this.getNonNullString(getConcentrationUnit());
    return display;
  }

  public String getValue() {
    return getCodeConcentrationUnit();
  }

  
  public String getCodeConcentrationUnit() {
    return codeConcentrationUnit;
  }

  
  public void setCodeConcentrationUnit(String codeConcentrationUnit) {
    this.codeConcentrationUnit = codeConcentrationUnit;
  }

  
  public String getConcentrationUnit() {
    return concentrationUnit;
  }

  
  public void setConcentrationUnit(String concentrationUnit) {
    this.concentrationUnit = concentrationUnit;
  }

  
  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
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

  
}