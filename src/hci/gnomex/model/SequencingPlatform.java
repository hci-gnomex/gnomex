package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import hci.dictionary.model.NullDictionaryEntry;

import java.io.Serializable;



public class SequencingPlatform extends DictionaryEntry implements Serializable {
  private String   codeSequencingPlatform;
  private String   sequencingPlatform;
  private String   isActive;

  public static final String   ILLUMINA_GAIIX_SEQUENCING_PLATFORM      = "GAIIX";
  public static final String   ILLUMINA_HISEQ_2000_SEQUENCING_PLATFORM = "HISEQ";
  public static final String   ILLUMINA_MISEQ_SEQUENCING_PLATFORM = "MISEQ";
  
  public String getDisplay() {
    String display = this.getNonNullString(getSequencingPlatform());
    return display;
  }

  public String getValue() {
    return getCodeSequencingPlatform();
  }

  
  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }

  public String getSequencingPlatform() {
    return sequencingPlatform;
  }

  
  public void setSequencingPlatform(String sequencingPlatform) {
    this.sequencingPlatform = sequencingPlatform;
  }

  
  public String getCodeSequencingPlatform() {
    return codeSequencingPlatform;
  }

  
  public void setCodeSequencingPlatform(String codeSequencingPlatform) {
    this.codeSequencingPlatform = codeSequencingPlatform;
  }


  
}