package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class SealType extends DictionaryEntry implements Comparable, Serializable {


  public static final String   HEATSEAL = "HEATSEAL";
  public static final String   SEPTA = "Septa";

  
  private String   codeSealType;
  private String   sealType;
  
  private String   isActive;
  
  
  public String getDisplay() {
    String display = this.getNonNullString(getSealType());
    return display;
  }

  public String getValue() {
    return getCodeSealType();
  }

  public String getCodeSealType() {
    return codeSealType;
  }

  public void setCodeSealType(String codeSealType) {
    this.codeSealType = codeSealType;
  }

  public String getSealType() {
    return sealType;
  }

  public void setSealType(String sealType) {
    this.sealType = sealType;
  }

  public String getIsActive() {
    return isActive;
  }

  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }

  public int compareTo(Object o) {
    if (o instanceof SealType) {
      SealType other = (SealType)o;
      return this.codeSealType.compareTo(other.getCodeSealType());
    } 
    return -1;

  }



}