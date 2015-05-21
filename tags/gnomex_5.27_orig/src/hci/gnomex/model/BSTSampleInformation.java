package hci.gnomex.model;



import hci.hibernate3utils.HibernateDetailObject;

import java.math.BigDecimal;



public class BSTSampleInformation extends HibernateDetailObject {
  
  private Integer        idSample;
  private String         ccNumber;
  private String         lastName;
  private String         firstName;
  private String         mrn;
  private java.sql.Date  birthDate;
  private java.sql.Date  collectDate;
  private java.sql.Date  completedDate;
  private String         gender;
  private java.sql.Date  createDate;
  private String         diagnosisIcd9Description;
  private BigDecimal     qual260nmTo280nmRatio;
  private BigDecimal     concentration;
  private String         specimenTested;
  private String         patientSpNumber;
  private BigDecimal     percentTumor;

  public Integer getIdSample() {
    return idSample;
  }
  public void setIdSample(Integer idSample) {
    this.idSample = idSample;
  }

  public String getCcNumber() {
    return ccNumber;
  }
  public void setCcNumber(String ccNumber) {
    this.ccNumber = ccNumber;
  }

  public String getLastName() {
    return lastName;
  }
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getFirstName() {
    return firstName;
  }
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getMrn() {
    return mrn;
  }
  public void setMrn(String mrn) {
    this.mrn = mrn;
  }

  public java.sql.Date getBirthDate() {
    return birthDate;
  }
  public void setBirthDate(java.sql.Date birthDate) {
    this.birthDate = birthDate;
  }

  public java.sql.Date getCollectDate() {
    return collectDate;
  }
  public void setCollectDate(java.sql.Date collectDate) {
    this.collectDate = collectDate;
  }

  public java.sql.Date getCompletedDate() {
    return completedDate;
  }
  public void setCompletedDate(java.sql.Date completedDate) {
    this.completedDate = completedDate;
  }

  public String getGender() {
    return gender;
  }
  public void setGender(String gender) {
    this.gender = gender;
  }

  public java.sql.Date getCreateDate() {
    return createDate;
  }
  public void setCreateDate(java.sql.Date createDate) {
    this.createDate = createDate;
  }

  public String getDiagnosisIcd9Description() {
    return diagnosisIcd9Description;
  }
  public void setDiagnosisIcd9Description(String diagnosisIcd9Description) {
    this.diagnosisIcd9Description = diagnosisIcd9Description;
  }

  public BigDecimal getQual260nmTo280nmRatio() {
    return qual260nmTo280nmRatio;
  }
  public void setQual260nmTo280nmRatio(BigDecimal ratio) {
    this.qual260nmTo280nmRatio = ratio;
  }
  
  public BigDecimal getConcentration() {
    return concentration;
  }
  public void setConcentration(BigDecimal concentration) {
    this.concentration = concentration;
  }
  
  public String getSpecimenTested() {
    return specimenTested;
  }
  public void setSpecimenTested(String specimenTested) {
    this.specimenTested = specimenTested;
  }
  
  public String getPatientSpNumber() {
    return patientSpNumber;
  }
  public void setPatientSpNumber(String patientSpNumber) {
    this.patientSpNumber = patientSpNumber;
  }

  public BigDecimal getPercentTumor() {
    return percentTumor;
  }
  public void setPercentTumor(BigDecimal percentTumor) {
    this.percentTumor = percentTumor;
  }
  
  public String getName() {
    String name = "";
    if (firstName != null) {
      name = firstName;
    }
    if (lastName != null) {
      if (name.length() > 0) {
        name += " ";
      }
      name += lastName;
    }
    
    return name;
  }
  
  public String getExpandedGender() {
    String g = "";
    if (gender != null) {
      if (gender.equals("M")) {
        g = "Male";
      } else if (gender.equals("F")) {
        g = "Female";
      }
    }
    
    return g;
  }
}