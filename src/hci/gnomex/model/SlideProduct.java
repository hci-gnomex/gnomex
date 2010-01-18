package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;



public class SlideProduct extends DictionaryEntry implements Serializable {
  private Integer  idSlideProduct;
  private String   name;
  private Integer  idOrganism;
  private Integer  idVendor;
  private String   isCustom;
  private Integer  idLab;
  private String   catalogNumber;
  private Integer  arraysPerSlide;
  private Integer  slidesInSet;
  private String   isSlideSet;
  private String   isActive;
  private Set      slideDesigns;
  private Set      applications;
  private Integer  idBillingSlideServiceClass;
  private Integer  idBillingSlideProductClass;
  
  public void copyEditableDataFrom(SlideProduct sp) {
    this.setName(sp.getName());
    this.setIdOrganism(sp.getIdOrganism());
    this.setIdVendor(sp.getIdVendor());
    this.setIsCustom(sp.getIsCustom());
    this.setIsActive(sp.getIsActive());
    this.setIdLab(sp.getIdLab());
    this.setCatalogNumber(sp.getCatalogNumber());
    this.setIdBillingSlideProductClass(sp.getIdBillingSlideProductClass());
    this.setIdBillingSlideServiceClass(sp.getIdBillingSlideServiceClass());
  }
  
  public String getDisplay() {
    String display = this.getNonNullString(getName());
    return display;
  }

  public String getValue() {
    return getIdSlideProduct().toString();
  }

  
  public String getCatalogNumber() {
    return catalogNumber;
  }

  
  public void setCatalogNumber(String catalogNumber) {
    this.catalogNumber = catalogNumber;
  }

  
  public Integer getIdOrganism() {
    return idOrganism;
  }

  
  public void setIdOrganism(Integer idOrganism) {
    this.idOrganism = idOrganism;
  }

  
  public Integer getIdSlideProduct() {
    return idSlideProduct;
  }

  
  public void setIdSlideProduct(Integer idSlideProduct) {
    this.idSlideProduct = idSlideProduct;
  }

  
  public Integer getIdVendor() {
    return idVendor;
  }

  
  public void setIdVendor(Integer idVendor) {
    this.idVendor = idVendor;
  }

  
  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }

  
  public String getIsCustom() {
    return isCustom;
  }

  
  public void setIsCustom(String isCustom) {
    this.isCustom = isCustom;
  }

  
  public String getName() {
    return name;
  }

  
  public void setName(String name) {
    this.name = name;
  }

  
  public Integer getArraysPerSlide() {
    return arraysPerSlide;
  }

  
  public void setArraysPerSlide(Integer arraysPerSlide) {
    this.arraysPerSlide = arraysPerSlide;
  }

  
  public Integer getSlidesInSet() {
    return slidesInSet;
  }

  
  public void setSlidesInSet(Integer slidesInSet) {
    this.slidesInSet = slidesInSet;
  }

  
  public Integer getIdLab() {
    return idLab;
  }

  
  public void setIdLab(Integer idLab) {
    this.idLab = idLab;
  }

  
  public Set getSlideDesigns() {
    return slideDesigns;
  }

  
  public void setSlideDesigns(Set slideDesigns) {
    this.slideDesigns = slideDesigns;
  }

  
  public String getIsSlideSet() {
    return isSlideSet;
  }

  
  public void setIsSlideSet(String isSlideSet) {
    this.isSlideSet = isSlideSet;
  }

  
  public Set getApplications() {
    return applications;
  }

  
  public void setApplications(Set applications) {
    this.applications = applications;
  }

  
  public Integer getIdBillingSlideServiceClass() {
    return idBillingSlideServiceClass;
  }

  
  public void setIdBillingSlideServiceClass(Integer idBillingSlideServiceClass) {
    this.idBillingSlideServiceClass = idBillingSlideServiceClass;
  }

  
  public Integer getIdBillingSlideProductClass() {
    return idBillingSlideProductClass;
  }

  
  public void setIdBillingSlideProductClass(Integer idBillingSlideProductClass) {
    this.idBillingSlideProductClass = idBillingSlideProductClass;
  }

  
 
 
  
 

}