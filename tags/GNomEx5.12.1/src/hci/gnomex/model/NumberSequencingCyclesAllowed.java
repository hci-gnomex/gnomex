package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class NumberSequencingCyclesAllowed extends DictionaryEntry implements Serializable {
  private Integer  idNumberSequencingCyclesAllowed;
  private String   name;
  private String   notes;
  private Integer  idNumberSequencingCycles;
  private String   codeRequestCategory;
  private Integer  idSeqRunType;
  private NumberSequencingCycles numberSequencingCycles;
  private SeqRunType seqRunType;
  
  public String getDisplay() {
    return name != null && !name.equals("") ? 
        name : 
        getCodeRequestCategory() + " - " + getNumberSequencingCyclesDisplay() + " - " + getSeqRunTypeDisplay();
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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public Integer getIdSeqRunType() {
    return idSeqRunType;
  }

  public void setIdSeqRunType(Integer idSeqRunType) {
    this.idSeqRunType = idSeqRunType;
  }

  private NumberSequencingCycles getNumberSequencingCycles() {
    return numberSequencingCycles;
  }

  private void setNumberSequencingCycles(
      NumberSequencingCycles numberSequencingCycles) {
    this.numberSequencingCycles = numberSequencingCycles;
  }
  
  private SeqRunType getSeqRunType() {
    return seqRunType;
  }

  private void setSeqRunType(SeqRunType seqRunType) {
    this.seqRunType = seqRunType;
  }

  public String getNumberSequencingCyclesDisplay() {
    return numberSequencingCycles != null ? numberSequencingCycles.getNumberSequencingCycles().toString() : "";
  }

  public String getSeqRunTypeDisplay() {
    return seqRunType != null ? seqRunType.getSeqRunType() : "";
  }
  public String getSeqRunTypeSortOrder() {
    return seqRunType != null ? seqRunType.getSortOrder().toString() : "";
  }
  
 
}