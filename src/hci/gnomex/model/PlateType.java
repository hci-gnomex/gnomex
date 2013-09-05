package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class PlateType extends DictionaryEntry implements Serializable {
  
  private String codePlateType;
  private String  plateTypeDescription;
  private String  isActive;

  public static final String REACTION_PLATE_TYPE = "REACTION";
  public static final String SOURCE_PLATE_TYPE = "SOURCE";
  
  public String getDisplay() {
    String display = this.getNonNullString(getPlateTypeDescription());
    return display;
  }

  public String getValue() {
    return getCodePlateType();
  }

  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }

  
  public String getPlateTypeDescription() {
    return plateTypeDescription;
  }

  
  public void setPlateTypeDescription(String desc) {
    this.plateTypeDescription = desc;
  }

  
  public String getCodePlateType() {
    return codePlateType;
  }

  
  public void setCodePlateType(String code) {
    this.codePlateType = code;
  }

  
  
}