package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class GenomeBuild extends DictionaryEntry implements Serializable {
  private Integer  idGenomeBuild;
  private String   genomeBuildName;
  private Integer  idOrganism;
  private String   isActive;
  private String   isLatestBuild;
  
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

}