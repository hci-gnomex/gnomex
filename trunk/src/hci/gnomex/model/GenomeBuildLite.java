package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;
import java.util.Set;



public class GenomeBuildLite extends DictionaryEntry implements Serializable, DictionaryEntryUserOwned {
  private Integer       idGenomeBuild;
  private String        genomeBuildName;
  private Integer       idOrganism;
  private String        isActive;
  private String        isLatestBuild;
  private Integer       idAppUser;
  
  public String getDisplay() {
    String display = this.getNonNullString(getGenomeBuildName());
    return display;
  }

  public String getValue() {
    return getIdGenomeBuild().toString();
  }


  
  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }


  
  public String getGenomeBuildName() {
    return genomeBuildName;
  }

  
  public void setGenomeBuildName(String genomeBuildName) {
    this.genomeBuildName = genomeBuildName;
  }

  
  public Integer getIdGenomeBuild() {
    return idGenomeBuild;
  }

  
  public void setIdGenomeBuild(Integer idGenomeBuild) {
    this.idGenomeBuild = idGenomeBuild;
  }

  
  public Integer getIdOrganism() {
    return idOrganism;
  }

  
  public void setIdOrganism(Integer idOrganism) {
    this.idOrganism = idOrganism;
  }

  
  public String getIsLatestBuild() {
    return isLatestBuild;
  }

  
  public void setIsLatestBuild(String isLatestBuild) {
    this.isLatestBuild = isLatestBuild;
  }

  
  public Integer getIdAppUser() {
    return idAppUser;
  }

  
  public void setIdAppUser(Integer idAppUser) {
    this.idAppUser = idAppUser;
  }

}