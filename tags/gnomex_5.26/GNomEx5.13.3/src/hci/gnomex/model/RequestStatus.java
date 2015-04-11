package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;


import java.io.Serializable;



public class RequestStatus extends DictionaryEntry implements Serializable {
  public static final String                  NEW                 = "NEW";
  public static final String                  SUBMITTED           = "SUBMITTED";
  public static final String                  PROCESSING          = "PROCESSING";
  public static final String                  COMPLETED           = "COMPLETE";
  public static final String                  FAILED              = "FAILED";
  
  private String   codeRequestStatus;
  private String   requestStatus;
  private String   isActive;
  
  public String getDisplay() {
    String display = this.getNonNullString(getRequestStatus());
    return display;
  }

  public String getValue() {
    return getCodeRequestStatus();
  }

  
  public String getCodeRequestStatus() {
    return codeRequestStatus;
  }

  
  public void setCodeRequestStatus(String codeRequestStatus) {
    this.codeRequestStatus = codeRequestStatus;
  }

  
  public String getRequestStatus() {
    return requestStatus;
  }

  
  public void setRequestStatus(String requestStatus) {
    this.requestStatus = requestStatus;
  }

  
  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }



  
}