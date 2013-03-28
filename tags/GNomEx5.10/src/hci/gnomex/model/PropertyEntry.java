package hci.gnomex.model;

import java.util.Iterator;
import java.util.Set;

import hci.hibernate3utils.HibernateDetailObject;


public class PropertyEntry extends HibernateDetailObject {
  public static final String     OTHER_LABEL = "otherLabel";
  
  private Integer            idPropertyEntry;
  private Integer            idProperty;
  private Integer            idSample;
  private Integer            idDataTrack;
  private Integer            idAnalysis;
  private String             value;
  private String             otherLabel;
  private Set                options;
  private Set                values;
  private Property           property;


  public Integer getIdSample() {
    return idSample;
  }
  
  public void setIdSample(Integer idSample) {
    this.idSample = idSample;
  }
  
  public Integer getIdPropertyEntry() {
    return idPropertyEntry;
  }
  
  public void setIdPropertyEntry(Integer idPropertyEntry) {
    this.idPropertyEntry = idPropertyEntry;
  }

  
  public String getValue() {
    return value;
  }

  
  public void setValue(String value) {
    this.value = value;
  }

  
  public String getOtherLabel() {
    return otherLabel;
  }

  
  public void setOtherLabel(String otherLabel) {
    this.otherLabel = otherLabel;
  }

  public Set getOptions() {
    return options;
  }

  public void setOptions(Set options) {
    this.options = options;
  }

  public Set getValues() {
    return values;
  }

  public void setValues(Set values) {
    this.values = values;
  }

  public Integer getIdProperty() {
    return idProperty;
  }

  public void setIdProperty(Integer idProperty) {
    this.idProperty = idProperty;
  }
  
  public Property getProperty() {
    return property;
  }

  public void setProperty(Property property) {
    this.property = property;
  }

  public void registerMethodsToExcludeFromXML() {
    this.excludeMethodFromXML("getProperty");
    this.excludeMethodFromXML("getExcludedMethodsMap");
  }

  public Integer getIdDataTrack() {
    return idDataTrack;
  }

  public void setIdDataTrack(Integer idDataTrack) {
    this.idDataTrack = idDataTrack;
  }
  
  public Integer getIdAnalysis() {
    return idAnalysis;
  }
  
  public void setIdAnalysis(Integer id) {
    idAnalysis = id;
  }
}