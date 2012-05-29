package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class ReactionType extends DictionaryEntry implements Comparable, Serializable {


  public static final String   SEQUENCING_REACTION_TYPE = "SEQ";
  public static final String   FRAGMENT_ANALYSIS_REACTION_TYPE = "FRAG";
  public static final String   CHERRY_PICKING_REACTION_TYPE = "CHERRY";
  public static final String   MITO_DLOOP_REACTION_TYPE = "MIT";

  
  private String   codeReactionType;
  private String   reactionType;

  private String   isActive;
  
  
  public String getDisplay() {
    String display = this.getNonNullString(getReactionType());
    return display;
  }

  public String getValue() {
    return getCodeReactionType();
  }

  public String getCodeReactionType() {
    return codeReactionType;
  }

  public void setCodeReactionType(String codeReactionType) {
    this.codeReactionType = codeReactionType;
  }

  public String getReactionType() {
    return reactionType;
  }

  public void setReactionType(String reactionType) {
    this.reactionType = reactionType;
  }

  public String getIsActive() {
    return isActive;
  }

  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }

  public int compareTo(Object o) {
    if (o instanceof ReactionType) {
      ReactionType other = (ReactionType)o;
      return this.codeReactionType.compareTo(other.getCodeReactionType());
    } 
    return -1;

  }



}