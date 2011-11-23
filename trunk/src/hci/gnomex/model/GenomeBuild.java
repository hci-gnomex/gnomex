package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class GenomeBuild extends DictionaryEntry implements Serializable, DictionaryEntryUserOwned {
  private Integer       idGenomeBuild;
  private String        genomeBuildName;
  private Integer       idOrganism;
  private String        isActive;
  private String        isLatestBuild;
  private Integer       idAppUser;
  private String        das2Name;
  private java.sql.Date buildDate;
  private String        coordURI;
  private String        coordVersion;
  private String        coordTestRange;
  private String        coordAuthority;
  private String        ucscName;
  private String        dataPath;
  
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

  public String getDas2Name() {
    return das2Name;
  }

  public void setDas2Name(String das2Name) {
    this.das2Name = das2Name;
  }

  public java.sql.Date getBuildDate() {
    return buildDate;
  }

  public void setBuildDate(java.sql.Date buildDate) {
    this.buildDate = buildDate;
  }

  public String getCoordURI() {
    return coordURI;
  }

  public String getCoordVersion() {
    return coordVersion;
  }

  public String getCoordTestRange() {
    return coordTestRange;
  }

  public String getCoordAuthority() {
    return coordAuthority;
  }

  public String getUcscName() {
    return ucscName;
  }

  public String getDataPath() {
    return dataPath;
  }

  public void setCoordURI(String coordURI) {
    this.coordURI = coordURI;
  }

  public void setCoordVersion(String coordVersion) {
    this.coordVersion = coordVersion;
  }

  public void setCoordTestRange(String coordTestRange) {
    this.coordTestRange = coordTestRange;
  }

  public void setCoordAuthority(String coordAuthority) {
    this.coordAuthority = coordAuthority;
  }

  public void setUcscName(String ucscName) {
    this.ucscName = ucscName;
  }

  public void setDataPath(String dataPath) {
    this.dataPath = dataPath;
  }

}