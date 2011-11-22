package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;



public class Property extends DictionaryEntry implements Serializable, OntologyEntry, DictionaryEntryUserOwned {
  
  
  private Integer  idProperty;
  private String   name;
  private String   description;
  private String   mageOntologyCode;
  private String   mageOntologyDefinition;
  private String   isActive;
  private Integer  idAppUser;
  private String   codePropertyType;
  private String   isRequired;
  private Set      options = new TreeSet();
  private Set      organisms = new TreeSet();
  private Set      platforms = new TreeSet();
  

  public String getDisplay() {
    String display = this.getNonNullString(getName());
    return display;
  }

  public String getValue() {
    return getIdProperty().toString();
  }
  
  
  public String getName() {
    return name;
  }

  
  public void setName(String name) {
    this.name = name;
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
  
  public String getCodePropertyType() {
    return codePropertyType;
  }
  public void setCodePropertyType(String codePropertyType) {
    this.codePropertyType = codePropertyType;
  }
  public Set getOrganisms() {
    return organisms;
  }
  public void setOrganisms(Set organisms) {
    this.organisms = organisms;
  }
  public Integer getIdProperty() {
    return idProperty;
  }
  public void setIdProperty(Integer idProperty) {
    this.idProperty = idProperty;
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