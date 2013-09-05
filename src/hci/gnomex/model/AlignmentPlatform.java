package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class AlignmentPlatform extends DictionaryEntry implements Serializable {
  private Integer  idAlignmentPlatform;
  private String   alignmentPlatformName;
  private String   webServiceName;
  private String   isActive;
  
  public String getDisplay() {
    String display = this.getNonNullString(getAlignmentPlatformName());
    return display;
  }

  public String getValue() {
    return getIdAlignmentPlatform().toString();
  }

  public String getIsActive() {
    return isActive;
  }

  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }

  public Integer getIdAlignmentPlatform() {
    return idAlignmentPlatform;
  }

  public void setIdAlignmentPlatform(Integer idAlignmentPlatform) {
    this.idAlignmentPlatform = idAlignmentPlatform;
  }

  public String getAlignmentPlatformName() {
    return alignmentPlatformName;
  }

  public void setAlignmentPlatformName(String alignmentPlatformName) {
    this.alignmentPlatformName = alignmentPlatformName;
  }

  public String getWebServiceName() {
    return webServiceName;
  }

  public void setWebServiceName(String webServiceName) {
    this.webServiceName = webServiceName;
  }

}