package hci.gnomex.model;

import hci.hibernate3utils.HibernateDetailObject;

import java.util.Set;
import java.util.TreeSet;
import java.sql.Date;


public class FlowCell extends HibernateDetailObject {
  
  private String   number;
  private Date     createDate;
  private String   notes;
  private Integer  idFlowCell;
  private Integer  idSeqRunType;
  private Integer  idNumberSequencingCycles;
  private String   barcode;
  private Set      flowCellChannels = new TreeSet();
  
  public Integer getIdFlowCell() {
    return idFlowCell;
  }
  
  public void setIdFlowCell(Integer idFlowCell) {
    this.idFlowCell = idFlowCell;
  }
  
  public Integer getIdSeqRunType() {
    return idSeqRunType;
  }
  
  public void setIdSeqRunType(Integer idSeqRunType) {
    this.idSeqRunType = idSeqRunType;
  }
  
  public Integer getIdNumberSequencingCycles() {
    return idNumberSequencingCycles;
  }
  
  public void setIdNumberSequencingCycles(Integer idNumberSequencingCycles) {
    this.idNumberSequencingCycles = idNumberSequencingCycles;
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

  
  
  public String getBarcode() {
    return barcode;
  }

  
  public void setBarcode(String barcode) {
    this.barcode = barcode;
  }

  
  public Set getFlowCellChannels() {
    return flowCellChannels;
  }

  
  public void setFlowCellChannels(Set flowCellChannels) {
    this.flowCellChannels = flowCellChannels;
  }

    
}