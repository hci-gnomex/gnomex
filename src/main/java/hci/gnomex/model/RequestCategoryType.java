package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;

import java.io.Serializable;

public class RequestCategoryType extends DictionaryEntry implements Comparable, Serializable {

  public static final String   TYPE_MICROARRAY          = "MICROARRAY";
  public static final String   TYPE_QC                  = "QC";
  public static final String   TYPE_NOSEQ               = "NOSEQ";
  public static final String   TYPE_HISEQ               = "HISEQ";
  public static final String   TYPE_MISEQ               = "MISEQ";
  public static final String   TYPE_CAP_SEQ             = "CAPSEQ";
  public static final String   TYPE_FRAGMENT_ANALYSIS   = "FRAGANAL";
  public static final String   TYPE_MITOCHONDRIAL_DLOOP = "MITSEQ";
  public static final String   TYPE_CHERRY_PICKING      = "CHERRYPICK";
  public static final String   TYPE_ISCAN               = "ISCAN";
  public static final String   TYPE_CLINICAL_SEQUENOM   = "CLINSEQ";
  public static final String   TYPE_SEQUENOM            = "SEQUENOM";
  public static final String   TYPE_NANOSTRING          = "NANOSTRING";
  public static final String   TYPE_ISOLATION           = "ISOLATION";
  public static final String   TYPE_GENERIC             = "GENERIC";

  private String                codeRequestCategoryType;
  private String                description;
  private String                defaultIcon;
  private String                isIllumina;
  private String                hasChannels;
  
  public String getDisplay() {
    String display = this.getNonNullString(getDescription());
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

  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }

  public String getIsIllumina() {
    return isIllumina;
  }
  public void setIsIllumina(String isIllumina) {
    this.isIllumina = isIllumina;
  }

  public String getDefaultIcon() {
    return defaultIcon;
  }
  public void setDefaultIcon(String defaultIcon) {
    this.defaultIcon = defaultIcon;
  }

  public String getHasChannels() {
    return hasChannels;
  }
  public void setHasChannels(String hasChannels) {
    this.hasChannels = hasChannels;
  }
}
