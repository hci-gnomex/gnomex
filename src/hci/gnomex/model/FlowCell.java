package hci.gnomex.model;

import hci.hibernate3utils.HibernateDetailObject;

import java.util.Set;
import java.util.TreeSet;
import java.sql.Date;


public class FlowCell extends HibernateDetailObject {
  
  private String  number;
  private Date    createDate;
  private String  notes;
  private Integer idFlowCell;
  private Integer idFlowCellType;
  private Integer idNumberSequencingCycles;
  private Date    startDate;
  private Date    firstCycleDate;
  private String  firstCycleFailed;
  private Date    lastCycleDate;
  private String  lastCycleFailed;
  private String  barcode;
  private Set     sequenceLanes = new TreeSet();
  
  public Integer getIdFlowCell() {
    return idFlowCell;
  }
  
  public void setIdFlowCell(Integer idFlowCell) {
    this.idFlowCell = idFlowCell;
  }
  
  public Integer getIdFlowCellType() {
    return idFlowCellType;
  }
  
  public void setIdFlowCellType(Integer idFlowCellType) {
    this.idFlowCellType = idFlowCellType;
  }
  
  public Integer getIdNumberSequencingCycles() {
    return idNumberSequencingCycles;
  }
  
  public void setIdNumberSequencingCycles(Integer idNumberSequencingCycles) {
    this.idNumberSequencingCycles = idNumberSequencingCycles;
  }
  
  public Set getSequenceLanes() {
    return sequenceLanes;
  }
  
  public void setSequenceLanes(Set sequenceLanes) {
    this.sequenceLanes = sequenceLanes;
  }

  
  public String getNumber() {
    return number;
  }

  
  public void setNumber(String number) {
    this.number = number;
  }

  
  public Date getCreateDate() {
    return createDate;
  }

  
  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  
  public String getNotes() {
    return notes;
  }

  
  public void setNotes(String notes) {
    this.notes = notes;
  }

  
  public Date getFirstCycleDate() {
    return firstCycleDate;
  }

  
  public void setFirstCycleDate(Date firstCycleDate) {
    this.firstCycleDate = firstCycleDate;
  }

  
  public String getFirstCycleFailed() {
    return firstCycleFailed;
  }

  
  public void setFirstCycleFailed(String firstCycleFailed) {
    this.firstCycleFailed = firstCycleFailed;
  }

  
  public Date getLastCycleDate() {
    return lastCycleDate;
  }

  
  public void setLastCycleDate(Date lastCycleDate) {
    this.lastCycleDate = lastCycleDate;
  }


  
  public String getLastCycleFailed() {
    return lastCycleFailed;
  }

  
  public void setLastCycleFailed(String lastCycleFailed) {
    this.lastCycleFailed = lastCycleFailed;
  }

  
  public String getBarcode() {
    return barcode;
  }

  
  public void setBarcode(String barcode) {
    this.barcode = barcode;
  }

  
  public Date getStartDate() {
    return startDate;
  }

  
  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  

  
    
}