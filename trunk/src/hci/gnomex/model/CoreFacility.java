package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;

import java.io.Serializable;


public class CoreFacility extends DictionaryEntry implements Serializable {
  
  private Integer  idCoreFacility;
  private String   facilityName;
  
  
  public String getDisplay() {
    String display = this.getNonNullString(getFacilityName());
    return display;
  }

  public String getValue() {
    return getIdCoreFacility().toString();
  }
  
  public Integer getIdCoreFacility()
  {
    return idCoreFacility;
  }

  public void setIdCoreFacility(Integer idCoreFacility)
  {
    this.idCoreFacility = idCoreFacility;
  }

  public String getFacilityName()
  {
    return facilityName;
  }

  public void setFacilityName(String facilityName)
  {
    this.facilityName = facilityName;
  }

  
    
}