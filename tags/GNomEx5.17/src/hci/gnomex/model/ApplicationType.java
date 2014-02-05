package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;

import java.io.Serializable;



public class ApplicationType extends DictionaryEntry implements Serializable {
  public static final String   TYPE_ILLUMINA            = "Illumina";
  public static final String   TYPE_MICROARRAY          = "Microarray";
  public static final String   TYPE_QC                  = "QC";
  public static final String   TYPE_SEQUENOM            = "Sequenom";
  public static final String   TYPE_OTHER               = "Other";

  private String  codeApplicationType;
  private String  applicationType;

  public String getDisplay() {
    String display = this.getNonNullString(getApplicationType());
    return display;
  }

  public String getValue() {
    return getCodeApplicationType();
  }
  
  public String getCodeApplicationType() {
    return codeApplicationType;
  }

  
  public void setCodeApplicationType(String codeApplicationType) {
    this.codeApplicationType = codeApplicationType;
  }

  
  public String getApplicationType() {
    return applicationType;
  }

  
  public void setApplicationType(String applicationType) {
    this.applicationType = applicationType;
  }
  
  public static String getCodeApplicationType(RequestCategoryType rct) {
    String code = TYPE_OTHER;
    if (rct.getCodeRequestCategoryType().equals(RequestCategoryType.TYPE_QC)) {
      code = TYPE_QC;
    } else if (rct.getCodeRequestCategoryType().equals(RequestCategoryType.TYPE_MICROARRAY)) {
      code = TYPE_MICROARRAY;
    } else if (rct.getIsIllumina().equals("Y")) {
      code = TYPE_ILLUMINA;
    } else if (rct.getCodeRequestCategoryType().equals(RequestCategoryType.TYPE_SEQUENOM) || rct.getCodeRequestCategoryType().equals(RequestCategoryType.TYPE_CLINICAL_SEQUENOM)) {
      code = TYPE_SEQUENOM;
    }
    
    return code;
  }
}