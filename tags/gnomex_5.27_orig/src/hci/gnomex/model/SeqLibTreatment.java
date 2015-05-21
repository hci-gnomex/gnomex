package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import hci.dictionary.model.NullDictionaryEntry;

import java.io.Serializable;



public class SeqLibTreatment extends DictionaryEntry implements Serializable, Comparable {



  private Integer  idSeqLibTreatment;
  private String   seqLibTreatment;
  private String   isActive;
  
  public String getDisplay() {
    String display = this.getNonNullString(getSeqLibTreatment());
    return display;
  }

  public String getValue() {
    return getIdSeqLibTreatment().toString();
  }


  
  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }


  public int compare(Object one, Object two) {
    if (one instanceof SeqLibTreatment && two instanceof SeqLibTreatment) {
      
      SeqLibTreatment at1 = (SeqLibTreatment)one;
      SeqLibTreatment at2 = (SeqLibTreatment)two;
      
      return at1.getSeqLibTreatment().compareTo(at2.getSeqLibTreatment());
       
    } else if (one instanceof NullDictionaryEntry && two instanceof SeqLibTreatment) {
      return -1;
    } else if (two instanceof NullDictionaryEntry && one instanceof SeqLibTreatment) {
      return 1;
    } else {
      return 0;
    }
  }
  
  public int compareTo(Object o) {
    if (o instanceof SeqLibTreatment) {
      SeqLibTreatment other = (SeqLibTreatment)o;      
      return this.getSeqLibTreatment().compareTo(other.getSeqLibTreatment());
    } else if (o instanceof NullDictionaryEntry) {
      return 1;
    } else {
      return -1;      
    }
  }

  
  public Integer getIdSeqLibTreatment() {
    return idSeqLibTreatment;
  }

  
  public void setIdSeqLibTreatment(Integer idSeqLibTreatment) {
    this.idSeqLibTreatment = idSeqLibTreatment;
  }

  
  public String getSeqLibTreatment() {
    return seqLibTreatment;
  }

  
  public void setSeqLibTreatment(String seqLibTreatment) {
    this.seqLibTreatment = seqLibTreatment;
  }
  
}