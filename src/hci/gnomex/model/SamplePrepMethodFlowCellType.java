package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class SamplePrepMethodFlowCellType extends DictionaryEntry implements Serializable {
  private Integer  idSamplePrepMethodFlowCellType;
  private Integer  idSamplePrepMethod;
  private Integer  idFlowCellType;
  
  public String getDisplay() {
    String display = this.idSamplePrepMethodFlowCellType.toString();
    return display;
  }

  public String getValue() {
    return idSamplePrepMethodFlowCellType.toString();
  }

  
  public Integer getIdSamplePrepMethodFlowCellType() {
    return idSamplePrepMethodFlowCellType;
  }

  
  public void setIdSamplePrepMethodFlowCellType(
      Integer idSamplePrepMethodFlowCellType) {
    this.idSamplePrepMethodFlowCellType = idSamplePrepMethodFlowCellType;
  }

  
  public Integer getIdSamplePrepMethod() {
    return idSamplePrepMethod;
  }

  
  public void setIdSamplePrepMethod(Integer idSamplePrepMethod) {
    this.idSamplePrepMethod = idSamplePrepMethod;
  }

  
  public Integer getIdFlowCellType() {
    return idFlowCellType;
  }

  
  public void setIdFlowCellType(Integer idFlowCellType) {
    this.idFlowCellType = idFlowCellType;
  }



  

}