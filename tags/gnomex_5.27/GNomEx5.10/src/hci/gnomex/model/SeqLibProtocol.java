package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class SeqLibProtocol extends DictionaryEntry implements Serializable {
  private Integer  idSeqLibProtocol;
  private String   seqLibProtocol;
  private String   description;
  private String   url;
  private String   isActive;
  
  public String getDisplay() {
    String display = this.getNonNullString(getSeqLibProtocol());
    return display;
  }

  public String getValue() {
    return getIdSeqLibProtocol().toString();
  }
  
  public String getIsActive() {
    return isActive;
  }
  
  public String getActiveNote() {
    if (isActive == null || isActive.equals("N")) {
      return " (inactive)";
    } else {
      return "";
    }
  }

  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }

  public String getDescription() {
    return description;
  }

  
  public void setDescription(String description) {
    this.description = description;
  }

  
  public Integer getIdSeqLibProtocol() {
    return idSeqLibProtocol;
  }

  
  public void setIdSeqLibProtocol(Integer idSeqLibProtocol) {
    this.idSeqLibProtocol = idSeqLibProtocol;
  }

  
  public String getSeqLibProtocol() {
    return seqLibProtocol;
  }

  
  public void setSeqLibProtocol(String seqLibProtocol) {
    this.seqLibProtocol = seqLibProtocol;
  }

  
  public String getUrl() {
    return url;
  }

  
  public void setUrl(String url) {
    this.url = url;
  }


}