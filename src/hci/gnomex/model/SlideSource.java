package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class SlideSource extends DictionaryEntry implements Serializable {
  
  public static final String    STRIPPED    = "REUSE";
  public static final String    CORE        = "CORE";
  public static final String    CLIENT      = "CLIENT";
  
  private String codeSlideSource;
  private String slideSource;  
  private String isActive;
  
  public String getDisplay() {
    String display = this.getNonNullString(getSlideSource());
    return display;
  }

  public String getValue() {
    return getCodeSlideSource();
  }

  
 
  
  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }

  
  public String getCodeSlideSource() {
    return codeSlideSource;
  }

  
  public void setCodeSlideSource(String codeSlideSource) {
    this.codeSlideSource = codeSlideSource;
  }

  
  public String getSlideSource() {
    return slideSource;
  }

  
  public void setSlideSource(String slideSource) {
    this.slideSource = slideSource;
  }

}