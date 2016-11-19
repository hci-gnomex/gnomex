package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;

import java.io.Serializable;

public class ContextSensitiveHelp extends DictionaryEntry implements Serializable {

  private Integer idContextSensitiveHelp;
  private String context1;
  private String context2;
  private String context3;
  private String helpText;
  private String toolTipText;
  
  @Override
  public String getDisplay() {
    return helpText;
  }

  @Override
  public String getValue() {
    return idContextSensitiveHelp.toString();
  }

  public Integer getIdContextSensitiveHelp() {
    return idContextSensitiveHelp;
  }
  public void setIdContextSensitiveHelp(Integer idContextSensitiveHelp) {
    this.idContextSensitiveHelp = idContextSensitiveHelp;
  }
  
  public String getContext1() {
    return context1;
  }
  public void setContext1(String context1) {
    this.context1 = context1;
  }
  
  public String getContext2() {
    return context2;
  }
  public void setContext2(String context2) {
    this.context2 = context2;
  }
  
  public String getContext3() {
    return context3;
  }
  public void setContext3(String context3) {
    this.context3 = context3;
  }
  
  public String getHelpText() {
    return helpText;
  }
  public void setHelpText(String helpText) {
    this.helpText = helpText;
  }
  
  public String getToolTipText() {
    return toolTipText;
  }
  public void setToolTipText(String toolTipText) {
    this.toolTipText = toolTipText;
  }
}
