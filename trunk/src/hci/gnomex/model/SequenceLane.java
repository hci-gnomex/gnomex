package hci.gnomex.model;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Set;
import java.util.Date;

import hci.gnomex.constants.Constants;
import hci.hibernate3utils.HibernateDetailObject;


public class SequenceLane extends HibernateDetailObject {
  
  private Integer         idSequenceLane;
  private String          number;
  private Date            createDate;
  private Integer         idSample;
  private Sample          sample;
  private Integer         idRequest;
  private Integer         idSeqRunType;
  private Integer         idNumberSequencingCycles;
  private Integer         idGenomeBuildAlignTo;
  private String          analysisInstructions;
  private Integer         idFlowCellChannel;
  private FlowCellChannel flowCellChannel;
  
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

  
  public Integer getIdSeqRunType() {
    return idSeqRunType;
  }

  
  public void setIdSeqRunType(Integer idSeqRunType) {
    this.idSeqRunType = idSeqRunType;
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
  
  public String getFlowCellNumber() {
    if (flowCellChannel != null) {
      return flowCellChannel.getFlowCell().getNumber().toString();
    } else {
      return "";
    }
  }

  public String getFlowCellChannelNumber() {
    if (flowCellChannel != null) {
      return flowCellChannel.getNumber() != null ? flowCellChannel.getNumber().toString() : "";
    } else {
      return "";
    }
  }
  
  public Date getFlowCellChannelFirstCycleDate() {
    if (flowCellChannel != null) {
      return flowCellChannel.getFirstCycleDate();
    } else {
      return null;
    }
    
  }
  public String getFlowCellChannelFirstCycleFailed() {
    if (flowCellChannel != null) {
      return flowCellChannel.getFirstCycleFailed();
    } else {
      return null;
    }
    
  }
  public Date getFlowCellChannelLastCycleDate() {
    if (flowCellChannel != null) {
      return flowCellChannel.getLastCycleDate();
    } else {
      return null;
    }
    
  }
  public String getFlowCellChannelLastCycleFailed() {
    if (flowCellChannel != null) {
      return flowCellChannel.getLastCycleFailed();
    } else {
      return null;
    }
    
  }
  
  public Date getFlowCellChannelPipelineDate() {
    if (flowCellChannel != null) {
      return flowCellChannel.getPipelineDate();
    } else {
      return null;
    }
    
  }
  public String getFlowCellChannelPipelineFailed() {
    if (flowCellChannel != null) {
      return flowCellChannel.getPipelineFailed();
    } else {
      return null;
    }
    
  }
  

  public String getFirstCycleStatus() {
    if (getFlowCellChannelFirstCycleDate() != null) {
      return Constants.STATUS_COMPLETED;
    } else if (this.getFlowCellChannelFirstCycleFailed() != null && this.getFlowCellChannelFirstCycleFailed().equals("Y")) {
      return Constants.STATUS_TERMINATED;
    } else {
      return "";
    }
  }
  
  public String getLastCycleStatus() {
    if (getFlowCellChannelLastCycleDate() != null) {
      return Constants.STATUS_COMPLETED;
    } else if (this.getFlowCellChannelLastCycleFailed() != null && this.getFlowCellChannelLastCycleFailed().equals("Y")) {
      return Constants.STATUS_TERMINATED;
    } else {
      return "";
    }
  }  
  
  public String getPipelineStatus() {
    if (getFlowCellChannelPipelineDate() != null) {
      return Constants.STATUS_COMPLETED;
    } else if (this.getFlowCellChannelPipelineFailed() != null && this.getFlowCellChannelPipelineFailed().equals("Y")) {
      return Constants.STATUS_TERMINATED;
    } else {
      return "";
    }
  }  
  
  public Date getFlowCellChannelStartDate() {
    if (flowCellChannel != null) {
      return flowCellChannel.getStartDate();
    } else {
      return null;
    }
  }
  
  public Integer getNumberSequencingCyclesActual() {
    if (flowCellChannel != null) {
      return flowCellChannel.getNumberSequencingCyclesActual();
    } else {
      return null;
    }
  }
  
  public Integer getClustersPerTile() {
    if (flowCellChannel != null) {
      return flowCellChannel.getClustersPerTile();
    } else {
      return null;
    }
  }

  
  public String getFileName() {
    if (flowCellChannel != null) {
      return flowCellChannel.getFileName();
    } else {
      return null;
    }
  }

  public String getWorkflowStatus() {
    if (getPipelineStatus().equals(Constants.STATUS_COMPLETED)) {
      return "Sequenced";
    } else if (getPipelineStatus().equals(Constants.STATUS_TERMINATED)) {
      return "Failed GA pipeline";
    } if (getLastCycleStatus().equals(Constants.STATUS_COMPLETED)) {
      return "Completed seq run";
    } else if (getLastCycleStatus().equals(Constants.STATUS_TERMINATED)) {
      return "Failed seq run";
    } else if (getFirstCycleStatus().equals(Constants.STATUS_TERMINATED)) {
      return "Failed 1st cycle seq run";
    } else if (getFlowCellChannelFirstCycleDate() != null) {
      return  "1st cycle seq run";
    } else if (this.getFlowCellChannelStartDate() != null) {
      return  "1st cycle seq run in progress";
    } else if (getFlowCellChannel() != null) {
      return "Ready for seq run";
    } else if (getSample().getSeqPrepByCore() != null && !getSample().getSeqPrepByCore().equals("Y")) {
      return "Ready to place on flow cell";
    } else if (getSample().getSeqPrepDate() != null) {
      return "Sample prepped by core, ready to place on flow cell";
    } else if (getSample().getQualDate() != null) {
      return "Sample QC'd by core, ready for sample lib prep by core";
    } else {
      return "Ready for Sample QC";
    }
  }

  public String getWorkflowStatusAbbreviated() {
    if (getPipelineStatus().equals(Constants.STATUS_COMPLETED)) {
      return "Done";
    } else if (getPipelineStatus().equals(Constants.STATUS_TERMINATED)) {
      return "Done";
    } if (getLastCycleStatus().equals(Constants.STATUS_COMPLETED)) {
      return "Done";
    } else if (getLastCycleStatus().equals(Constants.STATUS_TERMINATED)) {
      return "Failed";
    } else if (getFirstCycleStatus().equals(Constants.STATUS_TERMINATED)) {
      return "Failed";
    } else if (getFlowCellChannelFirstCycleDate() != null) {
      return  "In Progress";
    } else if (this.getFlowCellChannelStartDate() != null) {
      return  "In Progress";
    } else if (getFlowCellChannel() != null) {
      return "Ready";
    } else if (getSample().getSeqPrepByCore() != null && !getSample().getSeqPrepByCore().equals("Y")) {
      return "Ready";
    } else if (getSample().getSeqPrepDate() != null) {
      return "Ready";
    } else if (getSample().getQualDate() != null) {
      return "";
    } else {
      return "";
    }
  }

  public String getSampleNumber() {
    if (sample != null) {
      return sample.getNumber();
    } else {
      return "";
    }
  }
  
  
  public String getSampleName() {
    if (sample != null) {
      return sample.getName();
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
    this.excludeMethodFromXML("getFlowCellChannel");
    this.excludeMethodFromXML("getSample");
  }

  
  public Integer getIdFlowCellChannel() {
    return idFlowCellChannel;
  }

  
  public void setIdFlowCellChannel(Integer idFlowCellChannel) {
    this.idFlowCellChannel = idFlowCellChannel;
  }

  
  public FlowCellChannel getFlowCellChannel() {
    return flowCellChannel;
  }

  
  public void setFlowCellChannel(FlowCellChannel flowCellChannel) {
    this.flowCellChannel = flowCellChannel;
  }

  
  public String getCanChangeSeqRunType() {
    if (this.getFlowCellChannel() != null) {
      return "N";
    } else {
      return "Y";
    }
  }
  public String getCanChangeNumberSequencingCycles() {
    if (this.getFlowCellChannel() != null) {
      return "N";
    } else {
      return "Y";
    }
  }
  public String getCanChangeGenomeBuildAlignTo() {
    return "Y";
  }

  
    
}