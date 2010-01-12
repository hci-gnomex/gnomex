package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class MicroarrayCategory extends DictionaryEntry implements Serializable, Comparable {
  public static final String   EXPRESSION_MICROARRAY_CATEGORY   = "EXP";
  public static final String   CGH_MICROARRAY_CATEGORY          = "CGH";
  public static final String   CHIP_ON_CHIP_MICROARRAY_CATEGORY = "CHIP";
  public static final String   SNP_MICROARRAY_CATEGORY          = "SNP";
  public static final String   HYBMAP_MICROARRAY_CATEGORY       = "WTRANSCRP";
  public static final String   MIRNA_MICROARRAY_CATEGORY        = "MIRNA";
  

  private String codeMicroarrayCategory;
  private String microarrayCategory;
  private String isActive;
  
  public String getDisplay() {
    String display = this.getNonNullString(getMicroarrayCategory());
    return display;
  }

  public String getValue() {
    return getCodeMicroarrayCategory();
  }

  
  public String getCodeMicroarrayCategory() {
    return codeMicroarrayCategory;
  }

  
  public void setCodeMicroarrayCategory(String codeMicroarrayCategory) {
    this.codeMicroarrayCategory = codeMicroarrayCategory;
  }

  
  public String getMicroarrayCategory() {
    return microarrayCategory;
  }

  
  public void setMicroarrayCategory(String microarrayCategory) {
    this.microarrayCategory = microarrayCategory;
  }

  
  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }
  
  public int compareTo(Object other) {
    if (other instanceof MicroarrayCategory) {
      return this.getCodeMicroarrayCategory().compareTo(((MicroarrayCategory)other).getCodeMicroarrayCategory());
    } else {
      return 1;
    }
  }

}