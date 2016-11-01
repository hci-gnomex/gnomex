package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import hci.dictionary.model.NullDictionaryEntry;

import java.io.Serializable;



public class Assay extends DictionaryEntry implements Serializable {
  private Integer  idAssay;
  private String   name;
  private String   description;
  private String   isActive;
  
  public String getDisplay() {
    String display = this.getNonNullString(getName());
    return display;
  }

  public String getValue() {
    return getIdAssay().toString();
  }


  
  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }

  
  public Integer getIdAssay() {
    return idAssay;
  }

  
  public void setIdAssay(Integer idAssay) {
    this.idAssay = idAssay;
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

  public int compare(Object one, Object two) {
    if (one instanceof Assay && two instanceof Assay) {
      
      Assay at1 = (Assay)one;
      Assay at2 = (Assay)two;
      
      return at1.getName().compareTo(at2.getName());
       
    } else if (one instanceof NullDictionaryEntry && two instanceof Assay) {
      return -1;
    } else if (two instanceof NullDictionaryEntry && one instanceof Assay) {
      return 1;
    } else {
      return 0;
    }
  }
  
}