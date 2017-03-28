package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;


public class CoreFacility extends DictionaryEntry implements Serializable {
  
  public static final String CORE_FACILITY_DNA_SEQ = "DNA Sequencing";
  public static final String CORE_FACILITY_GENOMICS = "Microarray and Genomic Analysis";
  public static final Integer CORE_FACILITY_DNA_SEQ_ID = 2;
  public static final Integer CORE_FACILITY_GENOMICS_ID = 1;
  
  private Integer  idCoreFacility;
  private String   facilityName;
  private String   isActive;
  private String   showProjectAnnotations;
  private String   description;
  private String   acceptOnlineWorkAuth;
  private String   shortDescription;
  private String   contactName;
  private String   contactEmail;
  private String   contactPhone;
  private Integer  sortOrder;
  private String   contactImage;
  private String   contactRoom;
  private String   labRoom;
  private String   labPhone;
  
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

  
  public String getDescription() {
    return description;
  }

  
  public void setDescription( String description ) {
    this.description = description;
  }

  
  public String getAcceptOnlineWorkAuth() {
    return acceptOnlineWorkAuth;
  }

  
  public void setAcceptOnlineWorkAuth( String acceptOnlineWorkAuth ) {
    this.acceptOnlineWorkAuth = acceptOnlineWorkAuth;
  }

  public String getShowProjectAnnotations() {
    return showProjectAnnotations;
  }
  
  public void setShowProjectAnnotations(String val) {
    showProjectAnnotations = val;
  }
  
  
  public String getShortDescription() {
    return shortDescription;
  }

  
  public void setShortDescription( String shortDescription ) {
    this.shortDescription = shortDescription;
  }

  
  public String getContactName() {
    return contactName;
  }

  
  public void setContactName( String contactName ) {
    this.contactName = contactName;
  }

  
  public String getContactEmail() {
    return contactEmail;
  }

  
  public void setContactEmail( String contactEmail ) {
    this.contactEmail = contactEmail;
  }

  
  public String getContactPhone() {
    return contactPhone;
  }

  
  public void setContactPhone( String contactPhone ) {
    this.contactPhone = contactPhone;
  }
  
  public Integer getSortOrder() {
    return sortOrder;
  }
  public void setSortOrder(Integer sortOrder) {
    this.sortOrder = sortOrder;
  }

  
  public String getContactImage() {
    return contactImage;
  }

  
  public void setContactImage( String contactImage ) {
    this.contactImage = contactImage;
  }

  
  public String getLabRoom() {
    return labRoom;
  }

  
  public void setLabRoom( String labRoom ) {
    this.labRoom = labRoom;
  }
  
  public String getContactRoom() {
    return contactRoom;
  }

  
  public void setContactRoom( String contactRoom ) {
    this.contactRoom = contactRoom;
  }
  
  public String getLabPhone() {
    return labPhone;
  }

  
  public void setLabPhone( String labPhone ) {
    this.labPhone = labPhone;
  }

  public void registerMethodsToExcludeFromXML() {
    this.excludeMethodFromXML("getExcludedMethodsMap");
  }

  public static List<CoreFacility> getActiveCoreFacilities(Session sess) {
    return sess.createQuery("from CoreFacility where isActive = 'Y' order by sortOrder").list();
  }
}