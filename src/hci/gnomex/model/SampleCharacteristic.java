package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class SampleCharacteristic extends DictionaryEntry implements Serializable, OntologyEntry {
  public static final String     AGE                     = "AGE";
  public static final String     CELL_LINE               = "CELLLINE";
  public static final String     CELL_TYPE               = "CELLTYPE";
  public static final String     CLINICAL_INFO           = "CLININFO";
  public static final String     COMPOUND                = "COMPOUND";
  public static final String     DISEASE_STATE           = "DISSTATE";
  public static final String     DOSE                    = "DOSE";
  public static final String     GROWTH_CONDITIONS       = "GROWTHCOND";
  public static final String     GENOTYPE                = "GENOTYPE";
  public static final String     GENETIC_MODIFICATION    = "GENMOD";
  public static final String     INDIVIDUAL              = "INDIV";
  public static final String     TEMP                    = "TEMP";
  public static final String     TIME                    = "TIME";
  public static final String     ORGANISM_PART           = "ORGPART";
  public static final String     SEX                     = "SEX";
  public static final String     OTHER                   = "OTHER";
  
  
  
  
  private String   codeSampleCharacteristic;
  private String   sampleCharacteristic;
  private String   mageOntologyCode;
  private String   mageOntologyDefinition;
  private String   isActive;
  
  public static boolean isValidCode(String code) {
    if (code.equals(AGE) ||
        code.equals(CELL_LINE) ||
        code.equals(CELL_TYPE) ||
        code.equals(CLINICAL_INFO) ||
        code.equals(COMPOUND) ||
        code.equals(DISEASE_STATE) ||
        code.equals(DOSE) ||
        code.equals(GENETIC_MODIFICATION) ||
        code.equals(GENOTYPE) ||
        code.equals(GROWTH_CONDITIONS) ||
        code.equals(INDIVIDUAL) ||
        code.equals(TEMP) ||
        code.equals(TIME) ||
        code.equals(ORGANISM_PART) ||
        code.equals(SEX) ||
        code.equals(OTHER)) {
      return true;
    } else {
      return false;
    }
    
  }
  public String getDisplay() {
    String display = this.getNonNullString(getSampleCharacteristic());
    return display;
  }

  public String getValue() {
    return getCodeSampleCharacteristic();
  }
  
  
  public String getSampleCharacteristic() {
    return sampleCharacteristic;
  }

  
  public void setSampleCharacteristic(String sampleCharacteristic) {
    this.sampleCharacteristic = sampleCharacteristic;
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

  
  public String getCodeSampleCharacteristic() {
    return codeSampleCharacteristic;
  }

  
  public void setCodeSampleCharacteristic(String codeSampleCharacteristic) {
    this.codeSampleCharacteristic = codeSampleCharacteristic;
  }
  

}