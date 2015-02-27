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
  private String hasCaptureLibDesign;
  private String coreSteps;
  private String coreStepsNoLibPrep;
  private String codeApplicationType;
  private String onlyForLabPrepped;
  private Integer samplesPerBatch;
  private Integer idCoreFacility;
  private String hasChipTypes;

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
    }
    return 1;
  }

  
  public Integer getSortOrder() {
    return sortOrder;
  }

  
  public void setSortOrder(Integer sortOrder) {
    this.sortOrder = sortOrder;
  }

	public String getCoreSteps() {
		return coreSteps;
	}
	
	public void setCoreSteps(String coreSteps) {
		this.coreSteps = coreSteps;
	}
	
	public String getCoreStepsNoLibPrep() {
		return coreStepsNoLibPrep;
	}
	
	public void setCoreStepsNoLibPrep(String coreStepsNoLibPrep) {
		this.coreStepsNoLibPrep = coreStepsNoLibPrep;
	}
	
	public String getCodeApplicationType() {
	  return codeApplicationType;
	}
	
	public void setCodeApplicationType(String codeApplicationType) {
	  this.codeApplicationType = codeApplicationType;
	}
	
	public String getOnlyForLabPrepped() {
	  return onlyForLabPrepped;
	}
	
	public void setOnlyForLabPrepped(String onlyForLabPrepped) {
	  this.onlyForLabPrepped = onlyForLabPrepped;
	}
	
	public Boolean isApplicableApplication(RequestCategoryType rct, Integer idCoreFacility) {
	  Boolean isApplicable = true;
    String appType = ApplicationType.getCodeApplicationType(rct);
    if (getCodeApplicationType() == null && !appType.equals(ApplicationType.TYPE_OTHER)) {
      isApplicable = false;
    } else if (!getCodeApplicationType().equals(appType)) {
      isApplicable = false;
    } else if (!getIdCoreFacility().equals(idCoreFacility)) {
      isApplicable = false;
    }

    return isApplicable;
	}

  
  public Integer getSamplesPerBatch() {
    return samplesPerBatch;
  }

  
  public void setSamplesPerBatch( Integer samplesPerBatch ) {
    this.samplesPerBatch = samplesPerBatch;
  }
  
  public Integer getIdCoreFacility() {
    return idCoreFacility;
  }
  
  public void setIdCoreFacility(Integer id) {
    idCoreFacility = id;
  }
  
  public String getHasChipTypes() {
    return hasChipTypes;
  }

  
  public void setHasChipTypes(String hasChipTypes) {
    this.hasChipTypes = hasChipTypes;
  }
}