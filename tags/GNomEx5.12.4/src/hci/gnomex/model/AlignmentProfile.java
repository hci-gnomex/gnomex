package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;
import java.util.Set;


public class AlignmentProfile extends DictionaryEntry implements Serializable {
  private Integer  idAlignmentProfile;
  private String   alignmentProfileName;
  private String   description;
  private String   parameters;
  private String   isActive;
  private Integer  idAlignmentPlatform;  
  private Integer  idSeqRunType;
  private Set      genomeIndexList;

  public String getDisplay() {
    String display = this.getNonNullString(getAlignmentProfileName());
    return display;
  }

  public String getValue() {
    return getIdAlignmentProfile().toString();
  }

  public String getIsActive() {
    return isActive;
  }

  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }

  public Integer getIdAlignmentProfile() {
    return idAlignmentProfile;
  }

  public void setIdAlignmentProfile(Integer idAlignmentProfile) {
    this.idAlignmentProfile = idAlignmentProfile;
  }

  public String getAlignmentProfileName() {
    return alignmentProfileName;
  }

  public void setAlignmentProfileName(String alignmentProfileName) {
    this.alignmentProfileName = alignmentProfileName;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
  
  public String getParameters() {
    return parameters;
  }

  public void setParameters(String parameters) {
    this.parameters = parameters;
  }

  public Integer getIdAlignmentPlatform() {
    return idAlignmentPlatform;
  }

  public void setIdAlignmentPlatform(Integer idAlignmentPlatform) {
    this.idAlignmentPlatform = idAlignmentPlatform;
  }

  public Integer getIdSeqRunType() {
    return idSeqRunType;
  }

  public void setIdSeqRunType(Integer idSeqRunType) {
    this.idSeqRunType = idSeqRunType;
  }
  
  public Set getGenomeIndexList() {
    return genomeIndexList;
  }

  public void setGenomeIndexList(Set genomeIndexList) {
    this.genomeIndexList = genomeIndexList;
  }

}