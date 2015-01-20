package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import hci.gnomex.utility.DictionaryHelper;

import java.io.Serializable;



public class RequestCategory extends DictionaryEntry implements Comparable, Serializable {


  public static final String   AGILIENT_MICROARRAY_REQUEST_CATEGORY = "AGIL";
  public static final String   AGILIENT_1_COLOR_MICROARRAY_REQUEST_CATEGORY = "AGIL1";
  public static final String   AFFYMETRIX_MICROARRAY_REQUEST_CATEGORY = "AFFY";
  public static final String   QUALITY_CONTROL_REQUEST_CATEGORY = "QC";
  public static final String   SOLEXA_REQUEST_CATEGORY = "SOLEXA";
  public static final String   ILLUMINA_HISEQ_REQUEST_CATEGORY = "HISEQ";
  public static final String   ILLUMINA_MISEQ_REQUEST_CATEGORY = "MISEQ";

  public static final String   NIMBLEGEN_MICROARRAY_REQUEST_CATEGORY = "NIMBLE";
  public static final String   INHOUSE_MICROARRAY_REQUEST_CATEGORY = "INHOUSE";
  public static final String   OTHER_MICROARRAY_REQUEST_CATEGORY = "OTHER";

  public static final String   CAPILLARY_SEQUENCING_REQUEST_CATEGORY = "CAPSEQ";
  public static final String   FRAGMENT_ANALYSIS_REQUEST_CATEGORY = "FRAGANAL";
  public static final String   MITOCHONDRIAL_DLOOP_SEQ_REQUEST_CATEGORY = "MITSEQ";
  public static final String   CHERRY_PICKING_REQUEST_CATEGORY = "CHERRYPICK";
  public static final String   ION_TORRENT_REQUEST_CATEGORY = "ISCAN";
  public static final String   ISCAN_REQUEST_CATEGORY = "ISCAN";

  public static final String   CLINICAL_SEQUENOM_REQUEST_CATEGORY = "CLINSEQ";
  public static final String   SEQUENOM_REQUEST_CATEGORY = "SEQUENOM";
  public static final String   DNA_ISOLATION_REQUEST_CATEGORY = "DNAISOL";
  public static final String   RNA_ISOLATION_REQUEST_CATEGORY = "RNAISOL";
  public static final String   MD_MISEQ_REQUEST_CATEGORY = "MDMISEQ";

  private String                codeRequestCategory;
  private String                requestCategory;
  private Integer               idVendor;
  private Integer               idCoreFacility;
  private String                isActive;
  private Integer               numberOfChannels;
  private String                notes;
  private String                icon;
  private String                type;
  private Integer               sortOrder;
  private Integer               idOrganism;
  private String                isInternal;
  private String                isExternal;
  private String                refrainFromAutoDelete;
  private String                isClinicalResearch;
  private String                isOwnerOnly;
  private RequestCategoryType   categoryType;
  private Integer               sampleBatchSize;

  public static boolean isMicroarrayRequestCategory(String codeRequestCategory) {
    DictionaryHelper dh = DictionaryHelper.getInstance(null);
    RequestCategory cat = dh.getRequestCategoryObject(codeRequestCategory);
    if (cat != null) {
      return cat.isMicroarrayRequestCategory();
    } else {
      return false;
    }
  }  

  public static boolean isIlluminaRequestCategory(String codeRequestCategory) {
    DictionaryHelper dh = DictionaryHelper.getInstance(null);
    RequestCategory cat = dh.getRequestCategoryObject(codeRequestCategory);
    RequestCategoryType type = dh.getRequestCategoryType(cat.getType());
    if (type != null && type.getIsIllumina().equals("Y")) {
      return true;
    } else {
      return false;
    }
  }

  public static boolean isDNASeqCoreRequestCategory(String codeRequestCategory) {
    if (codeRequestCategory != null && (codeRequestCategory.equals(CAPILLARY_SEQUENCING_REQUEST_CATEGORY) ||
        codeRequestCategory.equals(FRAGMENT_ANALYSIS_REQUEST_CATEGORY) ||
        codeRequestCategory.equals(MITOCHONDRIAL_DLOOP_SEQ_REQUEST_CATEGORY) ||
        codeRequestCategory.equals(CHERRY_PICKING_REQUEST_CATEGORY) || 
        codeRequestCategory.equals(ISCAN_REQUEST_CATEGORY))) {
      return true;
    } else {
      return false;
    }
  }

  public boolean isMicroarrayRequestCategory() {
    if (this.type != null && !this.type.equals("")) {
      return type.equals(RequestCategoryType.TYPE_MICROARRAY);
    } else {
      return false;
    }
  }

  public static boolean isQCRequestCategory(String codeRequestCategory) {
    DictionaryHelper dh = DictionaryHelper.getInstance(null);
    RequestCategory cat = dh.getRequestCategoryObject(codeRequestCategory);
    if (cat != null) {
      return cat.isQCRequestCategory();
    } else {
      return false;
    }
  }

  public boolean isNextGenSeqRequestCategory() {
    return isIlluminaRequestCategory(this.getCodeRequestCategory());
  }


  public boolean isQCRequestCategory() {
    if (this.type != null && !this.type.equals("")) {
      return type.equals(RequestCategoryType.TYPE_QC);
    } else {
      return false;
    }
  }

  public static boolean isSequenom(String codeRequestCategory) {
    DictionaryHelper dh = DictionaryHelper.getInstance(null);
    RequestCategory cat = dh.getRequestCategoryObject(codeRequestCategory);
    RequestCategoryType type = dh.getRequestCategoryType(cat.getType());
    if (type != null && type.getCodeRequestCategoryType().equals( RequestCategoryType.TYPE_SEQUENOM ) ||
        type.getCodeRequestCategoryType().equals(RequestCategoryType.TYPE_CLINICAL_SEQUENOM) ) {
      return true;
    } 
    return false;

  }

  public static boolean isSequenomType(String codeRequestCategory) {
    DictionaryHelper dh = DictionaryHelper.getInstance(null);
    RequestCategory cat = dh.getRequestCategoryObject(codeRequestCategory);
    RequestCategoryType type = dh.getRequestCategoryType(cat.getType());
    if (type != null && type.getCodeRequestCategoryType().equals( RequestCategoryType.TYPE_SEQUENOM )) {
      return true;
    }
    return false;
  }

  public static boolean isMolecularDiagnoticsRequestCategory(String codeRequestCategory) {
    if (codeRequestCategory != null &&
        ( RequestCategory.isSequenom( codeRequestCategory ) || 
            RequestCategory.isSequenomType( codeRequestCategory ) ||
            codeRequestCategory.equals( DNA_ISOLATION_REQUEST_CATEGORY ) || 
            codeRequestCategory.equals( RNA_ISOLATION_REQUEST_CATEGORY ) || 
            codeRequestCategory.equals( MD_MISEQ_REQUEST_CATEGORY ))) {
      return true;
    } 
    return false;

  }

  public static boolean isNanoStringRequestCategoryType(String codeRequestCategory) {
    DictionaryHelper dh = DictionaryHelper.getInstance(null);
    RequestCategory cat = dh.getRequestCategoryObject(codeRequestCategory);
    RequestCategoryType type = dh.getRequestCategoryType(cat.getType());
    if (type != null && type.getCodeRequestCategoryType().equals( RequestCategoryType.TYPE_NANOSTRING )) {
      return true;
    }
    return false;

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

  public String getRefrainFromAutoDelete() {
    return refrainFromAutoDelete;
  }

  public void setRefrainFromAutoDelete(String refrainFromAutoDelete) {
    this.refrainFromAutoDelete = refrainFromAutoDelete;
  }

  public String getIsClinicalResearch() {
    return isClinicalResearch;
  }

  public void setIsClinicalResearch(String isClinicalResearch) {
    this.isClinicalResearch = isClinicalResearch;
  }

  public String getIsOwnerOnly() {
    return isOwnerOnly;
  }

  public void setIsOwnerOnly(String isOwnerOnly) {
    this.isOwnerOnly = isOwnerOnly;
  }

  public RequestCategoryType getCategoryType() {
    return categoryType;
  }

  public void setCategoryType(RequestCategoryType categoryType) {
    this.categoryType = categoryType;
  }

  public String getIsIlluminaType() {
    return this.getCategoryType().getIsIllumina();
  }

  public Integer getSampleBatchSize() {
    return sampleBatchSize;
  }

  public void setSampleBatchSize(Integer sampleBatchSize) {
    this.sampleBatchSize = sampleBatchSize;
  }
}