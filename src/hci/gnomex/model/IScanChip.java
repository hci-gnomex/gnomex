package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;
import java.math.BigDecimal;



public class IScanChip extends DictionaryEntry implements Serializable {
  
  private Integer     idIScanChip;
  private String      name;
  private BigDecimal  costPerSample;
  private Integer     samplesPerChip;
  private Integer     chipsPerKit;
  private String      markersPerSample;
  private String      catalogNumber;
  private String      isActive;
  
  public String getDisplay() {
    String display = this.getNonNullString(name);
    return display;
  }

  public String getValue() {
    return getIdIScanChip().toString();
  }

  
  public Integer getIdIScanChip() {
    return idIScanChip;
  }

  
  public void setIdIScanChip( Integer idIScanChip ) {
    this.idIScanChip = idIScanChip;
  }

  
  public String getName() {
    return name;
  }

  
  public void setName( String name ) {
    this.name = name;
  }

  
  public Integer getSamplesPerChip() {
    return samplesPerChip;
  }

  
  public void setSamplesPerChip( Integer samplesPerChip ) {
    this.samplesPerChip = samplesPerChip;
  }

  
  
  public Integer getChipsPerKit() {
    return chipsPerKit;
  }

  
  public void setChipsPerKit( Integer chipsPerKit ) {
    this.chipsPerKit = chipsPerKit;
  }

  public String getMarkersPerSample() {
    return markersPerSample != null ? markersPerSample : "";
  }

  
  public void setMarkersPerSample( String markersPerSample ) {
    this.markersPerSample = markersPerSample;
  }

  
  public String getCatalogNumber() {
    return catalogNumber != null ? catalogNumber : "";
  }

  
  public void setCatalogNumber( String catalogNumber ) {
    this.catalogNumber = catalogNumber;
  }

  public BigDecimal getCostPerSample() {
    return costPerSample;
  }

  
  public void setCostPerSample(BigDecimal costPerSample) {
    this.costPerSample = costPerSample;
  }

  
  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }


  
  public String isSelected() {
    return "N";
  }

  

}