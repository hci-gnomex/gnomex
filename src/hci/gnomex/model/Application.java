package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class Application extends DictionaryEntry implements Serializable, Comparable {
  public static final String   EXPRESSION_MICROARRAY_CATEGORY   = "EXP";
  public static final String   CGH_MICROARRAY_CATEGORY          = "CGH";
  public static final String   CHIP_ON_CHIP_MICROARRAY_CATEGORY = "CHIP";
  public static final String   SNP_MICROARRAY_CATEGORY          = "SNP";
  public static final String   HYBMAP_MICROARRAY_CATEGORY       = "WTRANSCRP";
  public static final String   MIRNA_MICROARRAY_CATEGORY        = "MIRNA";
  

  private String codeApplication;
  private String application;
  private String isActive;
  
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
  
  public int compareTo(Object other) {
    if (other instanceof Application) {
      return this.getCodeApplication().compareTo(((Application)other).getCodeApplication());
    } else {
      return 1;
    }
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

}