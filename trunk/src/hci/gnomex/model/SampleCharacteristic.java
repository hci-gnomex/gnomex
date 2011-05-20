package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;



public class SampleCharacteristic extends DictionaryEntry implements Serializable, OntologyEntry, DictionaryEntryUserOwned {
  
  
  private Integer  idSampleCharacteristic;
  private String   sampleCharacteristic;
  private String   description;
  private String   mageOntologyCode;
  private String   mageOntologyDefinition;
  private String   isActive;
  private Integer  idAppUser;
  private String   codeCharacteristicType;
  private String   isRequired;
  private Set      options = new TreeSet();
  private Set      organisms = new TreeSet();
  private Set      platforms = new TreeSet();
  

  public String getDisplay() {
    String display = this.getNonNullString(getSampleCharacteristic());
    return display;
  }

  public String getValue() {
    return getIdSampleCharacteristic().toString();
  }
  
  
  public String getSampleCharacteristic() {
    return sampleCharacteristic;
  }

  
  public void setSampleCharacteristic(String sampleCharacteristic) {
    this.sampleCharacteristic = sampleCharacteristic;
  }

  
  public String getMageOntologyCode() {
    return mageOntologyCode;
  }

  
  public void setMageOntologyCode(String mageOntologyCode) {
    this.mageOntologyCode = mageOntologyCode;
  }

  
  public String getMageOntologyDefinition() {
    return mageOntologyDefinition;
  }

  
  public void setMageOntologyDefinition(String mageOntologyDefinition) {
    this.mageOntologyDefinition = mageOntologyDefinition;
  }

  
  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }


  public Integer getIdAppUser() {
    return idAppUser;
  }
  
  public void setIdAppUser(Integer idAppUser) {
    this.idAppUser = idAppUser;
  }

  public Set getOptions() {
    return this.options;
  }
  
  public void setOptions(Set options) {
    this.options = options;
  }
  
  public String getCodeCharacteristicType() {
    return codeCharacteristicType;
  }
  public void setCodeCharacteristicType(String codeCharacteristicType) {
    this.codeCharacteristicType = codeCharacteristicType;
  }
  public Set getOrganisms() {
    return organisms;
  }
  public void setOrganisms(Set organisms) {
    this.organisms = organisms;
  }
  public Integer getIdSampleCharacteristic() {
    return idSampleCharacteristic;
  }
  public void setIdSampleCharacteristic(Integer idSampleCharacteristic) {
    this.idSampleCharacteristic = idSampleCharacteristic;
  }
  
  public String getCanRead() {
    if (this.canRead()) {
      return "Y";
    } else {
      return "N";
    }
  }

  public String getCanUpdate() {
    if (this.canUpdate()) {
      return "Y";
    } else {
      return "N";
    }
  }

  public String getCanDelete() {
    if (this.canDelete()) {
      return "Y";
    } else {
      return "N";
    }
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Set getPlatforms() {
    return platforms;
  }

  public void setPlatforms(Set platforms) {
    this.platforms = platforms;
  }

  public String getIsRequired() {
    return isRequired;
  }

  public void setIsRequired(String isRequired) {
    this.isRequired = isRequired;
  }
    
}