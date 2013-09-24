package hci.gnomex.model;

import java.util.Iterator;
import java.util.Set;

import hci.hibernate3utils.HibernateDetailObject;


public class TreatmentEntry extends HibernateDetailObject {
  
  public static final String      TREATMENT = "treatment";
  
  private Integer idTreatmentEntry;
  private String  treatment;
  private Integer idSample;
  
  public Integer getIdSample() {
    return idSample;
  }
  
  public void setIdSample(Integer idSample) {
    this.idSample = idSample;
  }
  
  public Integer getIdTreatmentEntry() {
    return idTreatmentEntry;
  }
  
  public void setIdTreatmentEntry(Integer idTreatmentEntry) {
    this.idTreatmentEntry = idTreatmentEntry;
  }
  
  public String getTreatment() {
    return treatment;
  }
  
  public void setTreatment(String treatment) {
    this.treatment = treatment;
  }
  
  
  
    
}