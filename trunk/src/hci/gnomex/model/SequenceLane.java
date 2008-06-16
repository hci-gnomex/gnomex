package hci.gnomex.model;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Set;
import java.util.Date;

import hci.gnomex.constants.Constants;
import hci.hibernate3utils.HibernateDetailObject;


public class SequenceLane extends HibernateDetailObject {
  
  private Integer idSequenceLane;
  private String  number;
  private Date    createDate;
  private Integer idSample;
  private Sample  sample;
  private Integer idRequest;
  private Integer idFlowCellType;
  private Integer idNumberSequencingCycles;
  private Integer idGenomeBuildAlignTo;
  private String  analysisInstructions;
  private Integer idFlowCell;
  private FlowCell flowCell;
  private Integer flowCellLaneNumber;
  
  public Integer getIdSample() {
    return idSample;
  }
  
  public void setIdSample(Integer idSample) {
    this.idSample = idSample;
  }

  
  public Integer getIdRequest() {
    return idRequest;
  }

  
  public void setIdRequest(Integer idRequest) {
    this.idRequest = idRequest;
  }

  
  public Integer getIdSequenceLane() {
    return idSequenceLane;
  }

  
  public void setIdSequenceLane(Integer idSequenceLane) {
    this.idSequenceLane = idSequenceLane;
  }

  
  public Sample getSample() {
    return sample;
  }

  
  public void setSample(Sample sample) {
    this.sample = sample;
  }

  
  public Date getCreateDate() {
    return createDate;
  }

  
  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  
  public String getNumber() {
    return number;
  }

  
  public void setNumber(String number) {
    this.number = number;
  }

  
  public Integer getIdNumberSequencingCycles() {
    return idNumberSequencingCycles;
  }

  
  public void setIdNumberSequencingCycles(Integer idNumberSequencingCycles) {
    this.idNumberSequencingCycles = idNumberSequencingCycles;
  }

  
  public Integer getIdFlowCellType() {
    return idFlowCellType;
  }

  
  public void setIdFlowCellType(Integer idFlowCellType) {
    this.idFlowCellType = idFlowCellType;
  }

  
  public Integer getIdGenomeBuildAlignTo() {
    return idGenomeBuildAlignTo;
  }

  
  public void setIdGenomeBuildAlignTo(Integer idGenomeBuildAlignTo) {
    this.idGenomeBuildAlignTo = idGenomeBuildAlignTo;
  }

  
  public String getAnalysisInstructions() {
    return analysisInstructions;
  }

  
  public void setAnalysisInstructions(String analysisInstructions) {
    this.analysisInstructions = analysisInstructions;
  }

  
  public Integer getIdFlowCell() {
    return idFlowCell;
  }

  
  public void setIdFlowCell(Integer idFlowCell) {
    this.idFlowCell = idFlowCell;
  }

  
  public Integer getFlowCellLaneNumber() {
    return flowCellLaneNumber;
  }

  
  public void setFlowCellLaneNumber(Integer flowCellLaneNumber) {
    this.flowCellLaneNumber = flowCellLaneNumber;
  }

  
  public FlowCell getFlowCell() {
    return flowCell;
  }

  
  public void setFlowCell(FlowCell flowCell) {
    this.flowCell = flowCell;
  }

  public String getFlowCellNumber() {
    if (flowCell != null) {
      return flowCell.getNumber();
    } else {
      return "";
    }
  }
  
  public Date getFlowCellFirstCycleDate() {
    if (flowCell != null) {
      return flowCell.getFirstCycleDate();
    } else {
      return null;
    }
    
  }
  public String getFlowCellFirstCycleFailed() {
    if (flowCell != null) {
      return flowCell.getFirstCycleFailed();
    } else {
      return null;
    }
    
  }
  public Date getFlowCellLastCycleDate() {
    if (flowCell != null) {
      return flowCell.getLastCycleDate();
    } else {
      return null;
    }
    
  }
  public String getFlowCellLastCycleFailed() {
    if (flowCell != null) {
      return flowCell.getLastCycleFailed();
    } else {
      return null;
    }
    
  }

  public String getFirstCycleStatus() {
    if (getFlowCellFirstCycleDate() != null) {
      return Constants.STATUS_COMPLETED;
    } else if (this.getFlowCellFirstCycleFailed() != null && this.getFlowCellFirstCycleFailed().equals("Y")) {
      return Constants.STATUS_TERMINATED;
    } else {
      return "";
    }
  }
  
  public String getLastCycleStatus() {
    if (getFlowCellLastCycleDate() != null) {
      return Constants.STATUS_COMPLETED;
    } else if (this.getFlowCellLastCycleFailed() != null && this.getFlowCellLastCycleFailed().equals("Y")) {
      return Constants.STATUS_TERMINATED;
    } else {
      return "";
    }
  }  
  
  public String getWorkflowStatus() {
    if (getLastCycleStatus().equals(Constants.STATUS_COMPLETED)) {
      return "Sequenced";
    } else if (getLastCycleStatus().equals(Constants.STATUS_TERMINATED)) {
      return "Failed sequencing";
    } else if (getFirstCycleStatus().equals(Constants.STATUS_TERMINATED)) {
      return "Failed 1st cycle sequencing";
    } else if (getFlowCellFirstCycleDate() != null) {
      return  "1st cycle sequenced";
    } else if (getFlowCell() != null) {
      return "Ready for sequencing";
    } else if (getSample().getSeqPrepByCore() != null && !getSample().getSeqPrepByCore().equals("Y")) {
      return "Ready to place on flow cell";
    } else if (getSample().getSeqPrepDate() != null) {
      return "Sample prepped by core, ready to place on flow cell";
    } else if (getSample().getQualDate() != null) {
      return "Sample QC'd bycCore, ready for sample lib prep by core";
    } else {
      return "Ready for Sample QC";
    }
  }
  
  public String getSampleNumber() {
    if (sample != null) {
      return sample.getNumber();
    } else {
      return "";
    }
  }
  
  public Integer getIdOrganism() {
    if (sample != null) {
      return sample.getIdOrganism();
    } else {
      return null;
    }
  }
  
  public Integer getFragmentSizeFrom() {
    if (sample != null && sample.getFragmentSizeFrom() != null) {
      return sample.getFragmentSizeFrom();
    } else {
      return null;
    }
  }

  public Integer getFragmentSizeTo() {
    if (sample != null && sample.getFragmentSizeTo() != null) {
      return sample.getFragmentSizeTo();
    } else {
      return null;
    }
  }
  
  public BigDecimal getCalcConcentration() {
    if (sample != null && sample.getQualCalcConcentration() != null) {
      return sample.getQualCalcConcentration();
    } else {
      return null;
    }
  }

  public void registerMethodsToExcludeFromXML() {
    this.excludeMethodFromXML("getFlowCell");
    this.excludeMethodFromXML("getSample");
  }

  
    
}