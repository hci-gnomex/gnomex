package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class Organism extends DictionaryEntry implements Serializable, OntologyEntry, DictionaryEntryUserOwned {
  private Integer idOrganism;
  private String  organism;
  private String  abbreviation;
  private String  mageOntologyCode;
  private String  mageOntologyDefinition;
  private String  isActive;
  private Integer idAppUser;
  
  public String getDisplay() {
    String display = this.getNonNullString(getOrganism());
    return display;
  }

  public String getValue() {
    return getIdOrganism().toString();
  }

  
  public Integer getIdOrganism() {
    return idOrganism;
  }

  
  public void setIdOrganism(Integer idOrganism) {
    this.idOrganism = idOrganism;
  }

  
  public String getOrganism() {
    return organism;
  }

  
  public void setOrganism(String organism) {
    this.organism = organism;
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

  
  public String getAbbreviation() {
    return abbreviation;
  }

  
  public void setAbbreviation(String abbreviation) {
    this.abbreviation = abbreviation;
  }

  
  public Integer getIdAppUser() {
    return idAppUser;
  }

  
  public void setIdAppUser(Integer idAppUser) {
    this.idAppUser = idAppUser;
  }




}