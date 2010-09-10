package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class Visibility extends DictionaryEntry implements Serializable {
  public static final String                  VISIBLE_TO_OWNER                           = "OWNER";
  public static final String                  VISIBLE_TO_GROUP_MEMBERS                   = "MEM";
  public static final String                  VISIBLE_TO_GROUP_MEMBERS_AND_COLLABORATORS = "MEMCOL";
  public static final String                  VISIBLE_TO_PUBLIC                          = "PUBLIC";
  
  private String   codeVisibility;
  private String   visibility;
  
  public String getDisplay() {
    String display = this.getNonNullString(getVisibility());
    return display;
  }

  public String getValue() {
    return getCodeVisibility();
  }


  
  public String getVisibility() {
    return visibility;
  }

  
  public void setVisibility(String visibility) {
    this.visibility = visibility;
  }

  
  public String getCodeVisibility() {
    return codeVisibility;
  }

  
  public void setCodeVisibility(String codeVisibility) {
    this.codeVisibility = codeVisibility;
  }


  
 

}