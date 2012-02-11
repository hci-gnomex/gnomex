package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;

public class GenomeIndex extends DictionaryEntry implements Serializable {
  private Integer  idGenomeIndex;
  private String   genomeIndexName;
  private String   webServiceName;
  private Integer  idOrganism;
  private String   isActive;
  
 /*
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
  */ 

  public String getDisplay() {
    String display = this.getNonNullString(getGenomeIndexName());
    return display;
  }

  public String getValue() {
    return getIdGenomeIndex().toString();
  }

  public String getIsActive() {
    return isActive;
  }

  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }

  public Integer getIdGenomeIndex() {
    return idGenomeIndex;
  }

  public void setIdGenomeIndex(Integer idGenomeIndex) {
    this.idGenomeIndex = idGenomeIndex;
  }

  public String getGenomeIndexName() {
    return genomeIndexName;
  }

  public void setGenomeIndexName(String genomeIndexName) {
    this.genomeIndexName = genomeIndexName;
  }

  public String getWebServiceName() {
    return webServiceName;
  }

  public void setWebServiceName(String webServiceName) {
    this.webServiceName = webServiceName;
  }
  
  public Integer getIdOrganism() {
    return idOrganism;
  }

  public void setIdOrganism(Integer idOrganism) {
    this.idOrganism = idOrganism;
  }
}