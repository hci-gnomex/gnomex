package hci.gnomex.model;

import java.util.Iterator;
import java.util.Set;

import hci.hibernate3utils.HibernateDetailObject;


public class PropertyEntryValue extends HibernateDetailObject {
  
  private Integer idPropertyEntryValue;
  private String  value;
  private Integer idPropertyEntry;
  
  
  
  public Integer getIdPropertyEntryValue() {
    return idPropertyEntryValue;
  }
  
  public void setIdPropertyEntryValue(Integer idPropertyEntryValue) {
    this.idPropertyEntryValue = idPropertyEntryValue;
  }

  
  public String getValue() {
    return value;
  }

  
  public void setValue(String value) {
    this.value = value;
  }

  public Integer getIdPropertyEntry() {
    return idPropertyEntry;
  }

  public void setIdPropertyEntry(Integer idPropertyEntry) {
    this.idPropertyEntry = idPropertyEntry;
  }

  
    
}