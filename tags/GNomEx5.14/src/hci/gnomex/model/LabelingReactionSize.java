package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class LabelingReactionSize extends DictionaryEntry implements Serializable {
  
  public static final String                  STANDARD = "STD";
  public static final String                  LARGE = "LRG";
  
  private String   codeLabelingReactionSize;
  private String   labelingReactionSize;
  private String   isActive;
  
  public String getDisplay() {
    String display = this.getNonNullString(getLabelingReactionSize());
    return display;
  }

  public String getValue() {
    return getCodeLabelingReactionSize();
  }

  
  public String getCodeLabelingReactionSize() {
    return codeLabelingReactionSize;
  }

  
  public void setCodeLabelingReactionSize(String codeLabelingReactionSize) {
    this.codeLabelingReactionSize = codeLabelingReactionSize;
  }

  
  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }

  
  public String getLabelingReactionSize() {
    return labelingReactionSize;
  }

  
  public void setLabelingReactionSize(String labelingReactionSize) {
    this.labelingReactionSize = labelingReactionSize;
  }

  
}