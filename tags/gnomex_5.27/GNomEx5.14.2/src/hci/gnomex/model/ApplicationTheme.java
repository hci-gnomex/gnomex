package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import hci.dictionary.model.NullDictionaryEntry;

import java.io.Serializable;



public class ApplicationTheme extends DictionaryEntry implements Serializable {
  private Integer  idApplicationTheme;
  private String   applicationTheme;
  private String   isActive;
  
  public String getDisplay() {
    String display = this.getNonNullString(getApplicationTheme());
    return display;
  }

  public String getValue() {
    return getIdApplicationTheme().toString();
  }


  
  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }


  public int compare(Object one, Object two) {
    if (one instanceof ApplicationTheme && two instanceof ApplicationTheme) {
      
      ApplicationTheme at1 = (ApplicationTheme)one;
      ApplicationTheme at2 = (ApplicationTheme)two;
      
      return at1.getApplicationTheme().compareTo(at2.getApplicationTheme());
       
    } else if (one instanceof NullDictionaryEntry && two instanceof ApplicationTheme) {
      return -1;
    } else if (two instanceof NullDictionaryEntry && one instanceof ApplicationTheme) {
      return 1;
    } else {
      return 0;
    }
  }

  
  public Integer getIdApplicationTheme() {
    return idApplicationTheme;
  }

  
  public void setIdApplicationTheme(Integer idApplicationTheme) {
    this.idApplicationTheme = idApplicationTheme;
  }

  
  public String getApplicationTheme() {
    return applicationTheme;
  }

  
  public void setApplicationTheme(String applicationTheme) {
    this.applicationTheme = applicationTheme;
  }
  
}