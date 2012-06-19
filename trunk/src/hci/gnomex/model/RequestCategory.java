package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class RequestCategory extends DictionaryEntry implements Comparable, Serializable {


  public static final String   AGILIENT_MICROARRAY_REQUEST_CATEGORY = "AGIL";
  public static final String   AGILIENT_1_COLOR_MICROARRAY_REQUEST_CATEGORY = "AGIL1";
  public static final String   AFFYMETRIX_MICROARRAY_REQUEST_CATEGORY = "AFFY";
  public static final String   QUALITY_CONTROL_REQUEST_CATEGORY = "QC";
  public static final String   SOLEXA_REQUEST_CATEGORY = "SOLEXA";
  public static final String   ILLUMINA_HISEQ_REQUEST_CATEGORY = "HISEQ";
  
  public static final String   NIMBLEGEN_MICROARRAY_REQUEST_CATEGORY = "NIMBLE";
  public static final String   INHOUSE_MICROARRAY_REQUEST_CATEGORY = "INHOUSE";
  public static final String   OTHER_MICROARRAY_REQUEST_CATEGORY = "OTHER";
  
  public static final String   CAPILLARY_SEQUENCING_REQUEST_CATEGORY = "CAPSEQ";
  public static final String   FRAGMENT_ANALYSIS_REQUEST_CATEGORY = "FRAGANAL";
  public static final String   MITOCHONDRIAL_DLOOP_SEQ_REQUEST_CATEGORY = "MITSEQ";
  public static final String   CHERRY_PICKING_REQUEST_CATEGORY = "CHERRYPICK";
  
  
  
  public static final String   TYPE_MICROARRAY          = "MICROARRAY";
  public static final String   TYPE_QC                  = "QC";
  public static final String   TYPE_ILLUMINA            = "ILLUMINA";
  public static final String   TYPE_CAP_SEQ             = "CAPSEQ";
  public static final String   TYPE_FRAGMENT_ANALYSIS   = "FRAGANAL";
  public static final String   TYPE_MITOCHONDRIAL_DLOOP = "MITSEQ";
  public static final String   TYPE_CHERRY_PICKING      = "CHERRYPICK";

  
  private String   codeRequestCategory;
  private String   requestCategory;
  private Integer  idVendor;
  private Integer  idCoreFacility;
  private String   isActive;
  private Integer  numberOfChannels;
  private String   notes;
  private String   icon;
  private String   type;
  private Integer  sortOrder;
  private Integer  idOrganism;
  private String   isSampleBarcodingOptional;
  private String   isInternal;
  private String   isExternal;
  
  public static boolean isMicroarrayRequestCategory(String codeRequestCategory) {
    if (codeRequestCategory.equals(AGILIENT_1_COLOR_MICROARRAY_REQUEST_CATEGORY) ||
        codeRequestCategory.equals(AGILIENT_MICROARRAY_REQUEST_CATEGORY) ||
        codeRequestCategory.equals(NIMBLEGEN_MICROARRAY_REQUEST_CATEGORY) ||
        codeRequestCategory.equals(INHOUSE_MICROARRAY_REQUEST_CATEGORY) ||
        codeRequestCategory.equals(OTHER_MICROARRAY_REQUEST_CATEGORY) ||
        codeRequestCategory.equals(AFFYMETRIX_MICROARRAY_REQUEST_CATEGORY)) {
      return true;
    } else {
      return false;
    }
  }  
  
  public static boolean isIlluminaRequestCategory(String codeRequestCategory) {
    if (codeRequestCategory.equals(SOLEXA_REQUEST_CATEGORY) ||
        codeRequestCategory.equals(ILLUMINA_HISEQ_REQUEST_CATEGORY)) {
      return true;
    } else {
      return false;
    }
  }
  public static boolean isDNASeqRequestCategory(String codeRequestCategory) {
    if (codeRequestCategory != null && (codeRequestCategory.equals(CAPILLARY_SEQUENCING_REQUEST_CATEGORY) ||
        codeRequestCategory.equals(FRAGMENT_ANALYSIS_REQUEST_CATEGORY) ||
        codeRequestCategory.equals(MITOCHONDRIAL_DLOOP_SEQ_REQUEST_CATEGORY) ||
        codeRequestCategory.equals(CHERRY_PICKING_REQUEST_CATEGORY))) {
      return true;
    } else {
      return false;
    }
  }
  
  public boolean isMicroarrayRequestCategory() {
    if (this.type != null && !this.type.equals("")) {
      return type.equals(TYPE_MICROARRAY);
    } else {
      return isMicroarrayRequestCategory(this.getCodeRequestCategory());
    }
  }
  
  public boolean isNextGenSeqRequestCategory() {
    if (this.type != null && !this.type.equals("")) {
      return type.equals(TYPE_ILLUMINA);
    } else {
      return isIlluminaRequestCategory(this.getCodeRequestCategory());
    }
  }
  
  
  public boolean isQCRequestCategory() {
    if (this.type != null && !this.type.equals("")) {
      return type.equals(TYPE_QC);
    } else {
      return codeRequestCategory.equals(this.QUALITY_CONTROL_REQUEST_CATEGORY);
    }
  }

  
  public String getDisplay() {
    String display = this.getNonNullString(getRequestCategory());
    return display;
  }

  public String getValue() {
    return getCodeRequestCategory();
  }

  
  public String getCodeRequestCategory() {
    return codeRequestCategory;
  }

  
  public void setCodeRequestCategory(String codeRequestCategory) {
    this.codeRequestCategory = codeRequestCategory;
  }

  
  public String getRequestCategory() {
    return requestCategory;
  }

  
  public void setRequestCategory(String requestCategory) {
    this.requestCategory = requestCategory;
  }

  
  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }

  
  public Integer getIdVendor() {
    return idVendor;
  }

  
  public void setIdVendor(Integer idVendor) {
    this.idVendor = idVendor;
  }

  
  public Integer getIdCoreFacility()
  {
    return idCoreFacility;
  }

  public void setIdCoreFacility(Integer idCoreFacility)
  {
    this.idCoreFacility = idCoreFacility;
  }

  public Integer getNumberOfChannels() {
    return numberOfChannels;
  }

  
  public void setNumberOfChannels(Integer numberOfChannels) {
    this.numberOfChannels = numberOfChannels;
  }

  
  public String getNotes() {
    return notes;
  }

  
  public void setNotes(String notes) {
    this.notes = notes;
  }

  
  
  public int compareTo(Object o) {
    if (o instanceof RequestCategory) {
      RequestCategory other = (RequestCategory)o;
      return this.codeRequestCategory.compareTo(other.getCodeRequestCategory());
    } else {
      return -1;
    }
  }

  
  public String getIcon() {
    return icon;
  }

  
  public void setIcon(String icon) {
    this.icon = icon;
  }

  
  public String getType() {
    return type;
  }

  
  public void setType(String type) {
    this.type = type;
  }

  public Integer getSortOrder() {
    return sortOrder;
  }

  public void setSortOrder(Integer sortOrder) {
    this.sortOrder = sortOrder;
  }

  public Integer getIdOrganism() {
    return idOrganism;
  }

  public void setIdOrganism(Integer idOrganism) {
    this.idOrganism = idOrganism;
  }

  public String getIsSampleBarcodingOptional() {
    return isSampleBarcodingOptional;
  }

  public void setIsSampleBarcodingOptional(String isSampleBarcodingOptional) {
    this.isSampleBarcodingOptional = isSampleBarcodingOptional;
  }

  public String getIsInternal() {
    return isInternal;
  }

  public void setIsInternal(String isInternal) {
    this.isInternal = isInternal;
  }

  public String getIsExternal() {
    return isExternal;
  }

  public void setIsExternal(String isExternal) {
    this.isExternal = isExternal;
  }
  

}