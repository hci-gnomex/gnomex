package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;

public class RequestCategoryType extends DictionaryEntry implements Comparable, Serializable {

  public static final String   TYPE_MICROARRAY          = "MICROARRAY";
  public static final String   TYPE_QC                  = "QC";
  public static final String   TYPE_ILLUMINA            = "ILLUMINA";
  public static final String   TYPE_CAP_SEQ             = "CAPSEQ";
  public static final String   TYPE_FRAGMENT_ANALYSIS   = "FRAGANAL";
  public static final String   TYPE_MITOCHONDRIAL_DLOOP = "MITSEQ";
  public static final String   TYPE_CHERRY_PICKING      = "CHERRYPICK";
  public static final String   TYPE_ISCAN               = "ISCAN";
  public static final String   TYPE_CLINICAL_SEQUENOM   = "CLINSEQ";
  public static final String   TYPE_SEQUENOM            = "SEQUENOM";
  public static final String   TYPE_GENERIC             = "GENERIC";

  private String                codeRequestCategoryType;
  private String                isIllumina;
  
  public String getDisplay() {
    String display = this.getNonNullString(getCodeRequestCategoryType());
    return display;
  }

  public String getValue() {
    return getCodeRequestCategoryType();
  }

  
  public String getCodeRequestCategoryType() {
    return codeRequestCategoryType;
  }

  
  public void setCodeRequestCategoryType(String codeRequestCategoryType) {
    this.codeRequestCategoryType = codeRequestCategoryType;
  }

  public String getisIllumina() {
    return isIllumina;
  }

  
  public void setisIllumina(String isIllumina) {
    this.isIllumina = isIllumina;
  }
}