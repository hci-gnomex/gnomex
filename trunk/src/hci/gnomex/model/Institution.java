package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;

import java.io.Serializable;
import java.util.Set;

public class Institution extends DictionaryEntry implements Serializable {

  private Integer idInstitution;
  private String  institution;
  private String  description;
  private String  isActive;
  private Set     labs;
  
  public String getDisplay() {
    return institution;
  }
  public String getValue() {
    return idInstitution.toString();
  } 
  public Integer getIdInstitution() {
    return idInstitution;
  }
  public void setIdInstitution(Integer idInstitution) {
    this.idInstitution = idInstitution;
  }
  public String getInstitution() {
    return institution;
  }
  public void setInstitution(String institution) {
    this.institution = institution;
  }
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public String getIsActive() {
    return isActive;
  }
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }
  private Set getLabs() {
    return labs;
  }
  private void setLabs(Set labs) {
    this.labs = labs;
  }

}
