package hci.gnomex.model;

import hci.hibernate3utils.HibernateDetailObject;

import java.sql.Date;


public class CoreFacility extends HibernateDetailObject {
  
  private Integer  idCoreFacility;
  private String   facilityName;
  
  
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