package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class ExperimentDesign extends DictionaryEntry implements Serializable, OntologyEntry {
  public static final String        OTHER            = "OTHER";
  public static final String        OTHER_VALIDATION = "OTHERVALID";
  
  private String  codeExperimentDesign;
  private String  experimentDesign;
  private String  mageOntologyCode;
  private String  mageOntologyDefinition;
  private String  isActive;
  
  public String getDisplay() {
    String display = this.getNonNullString(getExperimentDesign());
    return display;
  }

  public String getValue() {
    return getCodeExperimentDesign();
  }

  
  public String getExperimentDesign() {
    return experimentDesign;
  }

  
  public void setExperimentDesign(String experimentDesign) {
    this.experimentDesign = experimentDesign;
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

  
  public String getCodeExperimentDesign() {
    return codeExperimentDesign;
  }

  
  public void setCodeExperimentDesign(String codeExperimentDesign) {
    this.codeExperimentDesign = codeExperimentDesign;
  }

}