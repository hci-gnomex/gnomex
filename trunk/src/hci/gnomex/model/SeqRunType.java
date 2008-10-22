package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class SeqRunType extends DictionaryEntry implements Serializable {
  private Integer  idSeqRunType;
  private String   seqRunType;
  private String   isActive;
  
  public String getDisplay() {
    String display = this.getNonNullString(getSeqRunType());
    return display;
  }

  public String getValue() {
    return getIdSeqRunType().toString();
  }


  
  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }

  
  public Integer getIdSeqRunType() {
    return idSeqRunType;
  }

  
  public void setIdSeqRunType(Integer idSeqRunType) {
    this.idSeqRunType = idSeqRunType;
  }

  
  public String getSeqRunType() {
    return seqRunType;
  }

  
  public void setSeqRunType(String seqRunType) {
    this.seqRunType = seqRunType;
  }

}