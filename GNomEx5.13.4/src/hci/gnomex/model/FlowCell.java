package hci.gnomex.model;

import hci.gnomex.utility.DictionaryHelper;

import hci.hibernate3utils.HibernateDetailObject;

import java.util.Set;
import java.util.TreeSet;
import java.sql.Date;
import java.text.SimpleDateFormat;


public class FlowCell extends HibernateDetailObject {
  
  private String   number;
  private Date     createDate;
  private String   notes;
  private Integer  idFlowCell;
  private Integer  idSeqRunType;
  private Integer  idNumberSequencingCycles;
  private String   barcode;
  private String   codeSequencingPlatform;
  private Integer  runNumber;
  private Integer  idInstrument;
  private String   side;
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

  public String getCodeSequencingPlatform() {
    return codeSequencingPlatform;
  }

  
  public void setCodeSequencingPlatform(String codeSequencingPlatform) {
    this.codeSequencingPlatform = codeSequencingPlatform;
  }
  
  public Integer getRunNumber() {
    return runNumber;
  }
  public void setRunNumber(Integer rn) {
    runNumber = rn;
  }
  
  public Integer getIdInstrument() {
    return idInstrument;
  }
  public void setIdInstrument(Integer id) {
    idInstrument = id;
  }
  
  public String getSide() {
    return side;
  }
  public void setSide(String s) {
    side = s;
  }

  public String getCreateYear() {
    String createDate    = this.formatDate(this.getCreateDate());
    String tokens[] = createDate.split("/");
    String createYear  = tokens[2];
    return createYear;
  }
  
  public String getRunFolderName(DictionaryHelper dh) {
    String runFolder = "";
    // If any piece of the folder is null we return the folder name as
    // null in order to flag that it should not be updated.  This is
    // really an interim issue for legacy data before we were building
    // the folder name automatically.
    Boolean pieceNull = false;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
    if (this.getCreateDate() != null) {
      runFolder += dateFormat.format(this.getCreateDate());
    } else {
      pieceNull = true;
    }
    runFolder += "_";
    if (this.getIdInstrument() != null) {
      runFolder += dh.getInstrument(this.getIdInstrument());
    } else {
      pieceNull = true;
    }
    runFolder += "_";
    if (this.getRunNumber() != null) {
      Integer runNumberPlus = this.getRunNumber() + 10000;
      runFolder += runNumberPlus.toString().substring(1,5);
    } else {
      pieceNull = true;
    }
    runFolder += "_";
    if (this.getSide() != null && this.getSide().length() > 0) {
      runFolder += this.getSide();
    } else {
      pieceNull = true;
    }
    if (this.getBarcode() != null && this.getBarcode().length() > 0) {
      runFolder += this.getBarcode();
    } else {
      pieceNull = true;
    }
    
    if (pieceNull) {
      return null;
    } else {
      return runFolder;
    }
  }
    
}