package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;

import java.io.Serializable;



public class Application extends DictionaryEntry implements Serializable {
  public static final String   EXPRESSION_MICROARRAY_CATEGORY   = "EXP";
  public static final String   CGH_MICROARRAY_CATEGORY          = "CGH";
  public static final String   CHIP_ON_CHIP_MICROARRAY_CATEGORY = "CHIP";
  public static final String   SNP_MICROARRAY_CATEGORY          = "SNP";
  public static final String   HYBMAP_MICROARRAY_CATEGORY       = "WTRANSCRP";
  public static final String   MIRNA_MICROARRAY_CATEGORY        = "MIRNA";
  
  public static final String   BIOANALYZER_QC                   = "BIOAN";
  public static final String   QUBIT_PICOGREEN_QC               = "QUBIT";
  public static final String   DNA_GEL_QC                       = "DNAGEL";
  
  public static final String   CHIP_SEQ_CATEGORY                 = "CHIPSEQ";
  public static final String   MRNA_SEQ                          = "MRNASEQ";
  public static final String   DIRECTIONAL_MRNA_SEQ              = "DMRNASEQ";
  public static final String   SMALL_MRNA_SEQ                    = "SMRNASEQ";
  public static final String   GENOMIC_DNA_SEQ                   = "DNASEQ";
  public static final String   TARGETED_GENOMIC_DNA_SEQ          = "TDNASEQ";
  
  

  private String  codeApplication;
  private String  application;
  private String  isActive;
  private Integer idApplicationTheme;
  private Integer sortOrder;
  private Integer avgInsertSizeFrom;
  private Integer avgInsertSizeTo;
  private String hasCaptureLibDesign;
 
  
  public Integer getAvgInsertSizeFrom() {
    return avgInsertSizeFrom;
  }

  public void setAvgInsertSizeFrom(Integer avgInsertSizeFrom) {
    this.avgInsertSizeFrom = avgInsertSizeFrom;
  }

  public Integer getAvgInsertSizeTo() {
    return avgInsertSizeTo;
  }

  public void setAvgInsertSizeTo(Integer avgInsertSizeTo) {
    this.avgInsertSizeTo = avgInsertSizeTo;
  } 

  
  public String getHasCaptureLibDesign() {
    return hasCaptureLibDesign;
  }

  public void setHasCaptureLibDesign(String hasCaptureLibDesign) {
    this.hasCaptureLibDesign = hasCaptureLibDesign;
  }

  public String getDisplay() {
    String display = this.getNonNullString(getApplication());
    return display;
  }

  public String getValue() {
    return getCodeApplication();
  }

  
  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }
  
  
  public String getCodeApplication() {
    return codeApplication;
  }

  
  public void setCodeApplication(String codeApplication) {
    this.codeApplication = codeApplication;
  }

  
  public String getApplication() {
    return application;
  }

  
  public void setApplication(String application) {
    this.application = application;
  }

  
  public Integer getIdApplicationTheme() {
    return idApplicationTheme;
  }

  
  public void setIdApplicationTheme(Integer idApplicationTheme) {
    this.idApplicationTheme = idApplicationTheme;
  }

  public int compareTo(Object other) {
    if (other instanceof Application) {
      return this.getCodeApplication().compareTo(((Application)other).getCodeApplication());
    } else {
      return 1;
    }
  }

  
  public Integer getSortOrder() {
    return sortOrder;
  }

  
  public void setSortOrder(Integer sortOrder) {
    this.sortOrder = sortOrder;
  }

  

}