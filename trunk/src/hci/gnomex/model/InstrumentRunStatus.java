package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;


import java.io.Serializable;



public class InstrumentRunStatus extends DictionaryEntry implements Serializable {
  public static final String                  PENDING             = "PENDING";
  public static final String                  RUNNING             = "RUNNING";
  public static final String                  PROCESSING          = "PROCESSING";
  public static final String                  COMPLETED           = "COMPLETE";
  public static final String                  FAILED              = "FAILED";
  
  private String   codeInstrumentRunStatus;
  private String   instrumentRunStatus;
  private String   isActive;
  
  public String getDisplay() {
    String display = this.getNonNullString(getInstrumentRunStatus());
    return display;
  }

  public String getValue() {
    return getCodeInstrumentRunStatus();
  }

  
  public String getCodeInstrumentRunStatus() {
    return codeInstrumentRunStatus;
  }

  
  public void setCodeInstrumentRunStatus(String codeInstrumentRunStatus) {
    this.codeInstrumentRunStatus = codeInstrumentRunStatus;
  }

  
  public String getInstrumentRunStatus() {
    return instrumentRunStatus;
  }

  
  public void setInstrumentRunStatus(String instrumentRunStatus) {
    this.instrumentRunStatus = instrumentRunStatus;
  }

  
  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }



  
}