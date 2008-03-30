package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class FlowCellType extends DictionaryEntry implements Serializable {
  private Integer  idFlowCellType;
  private String   flowCellType;
  private String   isActive;
  
  public String getDisplay() {
    String display = this.getNonNullString(getFlowCellType());
    return display;
  }

  public String getValue() {
    return getIdFlowCellType().toString();
  }


  
  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }

  
  public String getFlowCellType() {
    return flowCellType;
  }

  
  public void setFlowCellType(String flowCellType) {
    this.flowCellType = flowCellType;
  }

  
  public Integer getIdFlowCellType() {
    return idFlowCellType;
  }

  
  public void setIdFlowCellType(Integer idFlowCellType) {
    this.idFlowCellType = idFlowCellType;
  }

}