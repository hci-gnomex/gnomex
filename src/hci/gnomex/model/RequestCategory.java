package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class RequestCategory extends DictionaryEntry implements Comparable, Serializable {


  public static final String   AGILIENT_MICROARRAY_REQUEST_CATEGORY = "AGIL";
  public static final String   AGILIENT_1_COLOR_MICROARRAY_REQUEST_CATEGORY = "AGIL1";
  public static final String   AFFYMETRIX_MICROARRAY_REQUEST_CATEGORY = "AFFY";
  public static final String   QUALITY_CONTROL_REQUEST_CATEGORY = "QC";
  public static final String   SOLEXA_REQUEST_CATEGORY = "SOLEXA";
  
  public static final String   NIMBLEGEN_MICROARRAY_REQUEST_CATEGORY = "NIMBLE";
  public static final String   INHOUSE_MICROARRAY_REQUEST_CATEGORY = "INHOUSE";
  public static final String   OTHER_MICROARRAY_REQUEST_CATEGORY = "OTHER";

  
  private String   codeRequestCategory;
  private String   requestCategory;
  private Integer  idVendor;
  private String   isActive;
  private Integer  numberOfChannels;
  private String   notes;

  
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
  

}