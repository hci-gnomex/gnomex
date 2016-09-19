package hci.dictionary.model;

import java.util.Comparator;




public class NullDictionaryEntry extends DictionaryEntry {
  private String isActive;
  private String dictionaryClassName;
  
  public NullDictionaryEntry(String dictionaryClassName) {
    this.dictionaryClassName = dictionaryClassName;
    this.canRead = true;
  }
  
  public String getCanWrite() {
    return "N";    
  }
    
  public String getCanUpdate() {
    return "N";
  } 
  public String getCanDelete() {
    return "N";
  }
  
  public String getDisplay() {
    return "";
  }

  public String getValue() {
    return "";
  }
  
  public String getDatakey() {
    return "";
  }
  
  public String getDictionaryClassName() {
    return dictionaryClassName;
  }
  
  public String getIsActive() {
    return "Y";
  }
  
  public void registerMethodsToExcludeFromXML() {
    this.excludeMethodFromXML("getDictionaryClassName");
    super.registerMethodsToExcludeFromXML();
  }
  
  public int compare(Object one, Object two) {
    if (one instanceof NullDictionaryEntry && two instanceof NullDictionaryEntry) {
      return 0;     
    } else if (one instanceof NullDictionaryEntry && !(two instanceof NullDictionaryEntry)) {
      return -1;
    } else if (two instanceof NullDictionaryEntry && !(one instanceof NullDictionaryEntry)) {
      return 1;
    } else {
      return ((Comparator)one).compare(one, two);
    }
  }
}
