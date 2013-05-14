package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class QualityControlStep extends DictionaryEntry implements Serializable, OntologyEntry {
  public static final String        OTHER            = "OTHER";
  public static final String        OTHER_VALIDATION = "OTHERVALID";
  
  private String   codeQualityControlStep;
  private String   qualityControlStep;
  private String   mageOntologyCode;
  private String   mageOntologyDefinition; 
  private String   isActive;
  
  public String getDisplay() {
    String display = this.getNonNullString(getQualityControlStep());
    return display;
  }

  public String getValue() {
    return getCodeQualityControlStep();
  }
  
  public String getQualityControlStep() {
    return qualityControlStep;
  }

  
  public void setQualityControlStep(String qualityControlStep) {
    this.qualityControlStep = qualityControlStep;
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

  
  public String getCodeQualityControlStep() {
    return codeQualityControlStep;
  }

  
  public void setCodeQualityControlStep(String codeQualityControlStep) {
    this.codeQualityControlStep = codeQualityControlStep;
  }

}