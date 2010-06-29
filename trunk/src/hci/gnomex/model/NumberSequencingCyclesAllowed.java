package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class NumberSequencingCyclesAllowed extends DictionaryEntry implements Serializable {
  private Integer  idNumberSequencingCyclesAllowed;
  private Integer  idNumberSequencingCycles;
  private String   codeRequestCategory;
  private NumberSequencingCycles       numberSequencingCycles;
  private RequestCategory              requestCategory;
  
  public String getDisplay() {
    String display =  (getNumberSequencingCycles() != null ? getNumberSequencingCycles().getNumberSequencingCycles() : "?") + 
                      " - " + 
                      (getRequestCategory() != null ? getRequestCategory().getRequestCategory() : "?");
    return display;
  }

  public String getValue() {
    return idNumberSequencingCyclesAllowed.toString();
  }

  
  public Integer getIdNumberSequencingCyclesAllowed() {
    return idNumberSequencingCyclesAllowed;
  }

  
  public void setIdNumberSequencingCyclesAllowed(
      Integer idNumberSequencingCyclesAllowed) {
    this.idNumberSequencingCyclesAllowed = idNumberSequencingCyclesAllowed;
  }

  
  public Integer getIdNumberSequencingCycles() {
    return idNumberSequencingCycles;
  }

  
  public void setIdNumberSequencingCycles(Integer idNumberSequencingCycles) {
    this.idNumberSequencingCycles = idNumberSequencingCycles;
  }

  
  public String getCodeRequestCategory() {
    return codeRequestCategory;
  }

  
  public void setCodeRequestCategory(String codeRequestCategory) {
    this.codeRequestCategory = codeRequestCategory;
  }

  
  private RequestCategory getRequestCategory() {
    return requestCategory;
  }

  
  private void setRequestCategory(RequestCategory requestCategory) {
    this.requestCategory = requestCategory;
  }

  
  private NumberSequencingCycles getNumberSequencingCycles() {
    return numberSequencingCycles;
  }

  
  private void setNumberSequencingCycles(NumberSequencingCycles numberSequencingCycles) {
    this.numberSequencingCycles = numberSequencingCycles;
  }

 

}