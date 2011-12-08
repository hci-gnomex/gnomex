package hci.gnomex.model;

import java.util.Iterator;
import java.util.Set;

import hci.hibernate3utils.HibernateDetailObject;


public class PropertyEntryValue extends HibernateDetailObject {
  
  private Integer       idPropertyEntryValue;
  private String        value;
  private Integer       idPropertyEntry;
  private PropertyEntry propertyEntry;
  
  
  
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

  private PropertyEntry getPropertyEntry() {
    return propertyEntry;
  }

  private void setPropertyEntry(PropertyEntry propertyEntry) {
    this.propertyEntry = propertyEntry;
  }
  
  public String getUrlAlias() {
    String alias = "";
    if (getPropertyEntry().getProperty().getCodePropertyType().equals(PropertyType.URL)) {
      if (getValue() != null) {
        String tokens[] = getValue().split(",");
        if (tokens.length > 1) {
          alias = tokens[1];
        }         
      }
    }
    return alias;
  }
  public String getUrl() {
    String url = "";
    if (getPropertyEntry().getProperty().getCodePropertyType().equals(PropertyType.URL)) {
      if (getValue() != null) {
        String tokens[] = getValue().split(",");
        if (tokens.length > 1) {
          url = tokens[0];
        } else {
          url = getValue();
        }        
      }
    }
    return url;
  }

  public String getUrlDisplay() {
    String urlDisplay = "";
    if (getPropertyEntry().getProperty().getCodePropertyType().equals(PropertyType.URL)) {
      if (getValue() != null) {
        String tokens[] = getValue().split(",");
        if (tokens.length > 1) {
          urlDisplay = tokens[1];
        } else {
          urlDisplay = getValue();
        }        
      }
    }
    return urlDisplay;
  }
  public void registerMethodsToExcludeFromXML() {
    this.excludeMethodFromXML("getPropertyEntry");
    this.excludeMethodFromXML("getExcludedMethodsMap");
  }
    
}