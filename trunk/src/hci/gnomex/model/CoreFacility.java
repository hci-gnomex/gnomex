package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import hci.gnomex.utility.DictionaryHelper;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;


public class CoreFacility extends DictionaryEntry implements Serializable {
  
  public static final String CORE_FACILITY_DNA_SEQ = "DNA Sequencing";
  public static final String CORE_FACILITY_GENOMICS = "Microarray and Genomic Analysis";
  
  private Integer  idCoreFacility;
  private String   facilityName;
  private String   isActive;
  
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
  
  public String getIsActive() {
    return isActive;
  }
  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }
  
  public static List getActiveCoreFacilities(Session sess) {
    List facilities = sess.createQuery("from CoreFacility where isActive = 'Y'").list();
    return facilities;
  }
}