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
  private String   forSample;
  private String   forAnalysis;
  private String   forDataTrack;
  private Integer  sortOrder;
  private Integer  idCoreFacility;
  private Set      options = new TreeSet();
  private Set      organisms = new TreeSet();
  private Set      platformApplications = new TreeSet();
  private Set      analysisTypes = new TreeSet();
  

  public Set getAnalysisTypes() {
    return analysisTypes;
  }

  public void setAnalysisTypes(Set analysisTypes) {
    this.analysisTypes = analysisTypes;
  }

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

  public Set getPlatformApplications() {
    return platformApplications;
  }

  public void setPlatformApplications(Set platformApplications) {
    this.platformApplications = platformApplications;
  }

  public String getIsRequired() {
    return isRequired;
  }

  public void setIsRequired(String isRequired) {
    this.isRequired = isRequired;
  }

  public String getForSample() {
    return forSample;
  }

  public String getForAnalysis() {
    return forAnalysis;
  }

  public String getForDataTrack() {
    return forDataTrack;
  }

  public void setForSample(String forSample) {
    this.forSample = forSample;
  }

  public void setForAnalysis(String forAnalysis) {
    this.forAnalysis = forAnalysis;
  }

  public void setForDataTrack(String forDataTrack) {
    this.forDataTrack = forDataTrack;
  }
  
  public Integer getSortOrder() {
    return sortOrder;
  }
  public void setSortOrder(Integer sortOrder) {
    this.sortOrder = sortOrder;
  }
  
  public Integer getIdCoreFacility() {
    return idCoreFacility;
  }
  public void setIdCoreFacility(Integer idCoreFacility) {
    this.idCoreFacility = idCoreFacility;
  }
  
  public String getAppliesToOrganism() {
    StringBuffer buf = new StringBuffer();
    if (getOrganisms() != null) {
      for (Organism org : (Set<Organism>)getOrganisms()) {
        if (buf.length() > 0) {
          buf.append(", ");
        }
        buf.append(org.getOrganism());
      }
    }
    return buf.toString();
  }
  
  public String getAppliesToAnalysisType() {
    StringBuffer buf = new StringBuffer();
    if (getAnalysisTypes() != null) {
      for (AnalysisType at : (Set<AnalysisType>)getAnalysisTypes()) {
        if (buf.length() > 0) {
          buf.append(", ");
        }
        buf.append(at.getAnalysisType());
      }
    } 
    return buf.toString();
  }    
  
  public String getAppliesToPlatform() {
    StringBuffer buf = new StringBuffer();

    if (getPlatformApplications() != null) {
      for (PropertyPlatformApplication pa : (Set<PropertyPlatformApplication>)getPlatformApplications()) {
        if (buf.length() > 0) {
          buf.append(", ");
        }
        buf.append(pa.getDisplay() + (pa.getApplicationDisplay().length() > 0 ? " " + pa.getApplicationDisplay() : ""));
      }
    }
    return buf.toString();
  }
  
  public String getAppliesToRequestCategory(){
    StringBuffer buf = new StringBuffer();

    if (getPlatformApplications() != null) {
      for (PropertyPlatformApplication pa : (Set<PropertyPlatformApplication>)getPlatformApplications()) {
        if (buf.length() > 0) {
          buf.append(", ");
        }
        buf.append(pa.getDisplay());
      }
    }
    return buf.toString();
    
  }
    
}