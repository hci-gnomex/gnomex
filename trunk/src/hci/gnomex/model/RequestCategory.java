package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class RequestCategory extends DictionaryEntry implements Serializable {
  public static final String   AGILIENT_MICROARRAY_REQUEST_CATEGORY = "AGIL";
  public static final String   AFFYMETRIX_MICROARRAY_REQUEST_CATEGORY = "AFFY";
  public static final String   QUALITY_CONTROL_REQUEST_CATEGORY = "QC";
  public static final String   SOLEXA_REQUEST_CATEGORY = "SOLEXA";
  

  private String   codeRequestCategory;
  private String   requestCategory;
  private Integer  idVendor;
  private String   isActive;
  private Integer  numberOfChannels;

  
  
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
  
  

}