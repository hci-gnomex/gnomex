package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import hci.dictionary.model.NullDictionaryEntry;

import java.io.Serializable;



public class Primer extends DictionaryEntry implements Serializable {
  private Integer  idPrimer;
  private String   name;
  private String   description;
  private String   sequence;
  private String   isActive;
  
  public String getDisplay() {
    String display = this.getNonNullString(getName());
    return display;
  }

  public String getValue() {
    return getIdPrimer().toString();
  }


  
  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }

  
  public Integer getIdPrimer() {
    return idPrimer;
  }

  
  public void setIdPrimer(Integer idPrimer) {
    this.idPrimer = idPrimer;
  }

  

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getSequence() {
    return sequence;
  }

  public void setSequence(String sequence) {
    this.sequence = sequence;
  }

  public int compare(Object one, Object two) {
    if (one instanceof Primer && two instanceof Primer) {
      
      Primer at1 = (Primer)one;
      Primer at2 = (Primer)two;
      
      return at1.getName().compareTo(at2.getName());
       
    } else if (one instanceof NullDictionaryEntry && two instanceof Primer) {
      return -1;
    } else if (two instanceof NullDictionaryEntry && one instanceof Primer) {
      return 1;
    } else {
      return 0;
    }
  }
  
}