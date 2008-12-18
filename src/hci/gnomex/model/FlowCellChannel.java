package hci.gnomex.model;

import hci.gnomex.constants.Constants;
import hci.hibernate3utils.HibernateDetailObject;

import java.util.Set;
import java.util.TreeSet;
import java.math.BigDecimal;
import java.sql.Date;


public class FlowCellChannel extends HibernateDetailObject {

  private Integer           idFlowCellChannel;
  private Integer           number;
  private FlowCell          flowCell;
  private Integer           idFlowCell;
  private SequenceLane      sequenceLane;
  private Integer           idSequenceLane;
  private SequencingControl sequencingControl;
  private Integer           idSequencingControl;
  private Date              startDate;
  private Date              firstCycleDate;
  private String            firstCycleFailed;
  private Date              lastCycleDate;
  private String            lastCycleFailed;
  private Integer           clustersPerTile;
  private String            fileName;
  private BigDecimal        sampleConcentrationpM;
  private Integer           numberSequencingCyclesActual;
  private Date              pipelineDate;
  private String            pipelineFailed;
  
  public Integer getIdFlowCellChannel() {
    return idFlowCellChannel;
  }
  
  public void setIdFlowCellChannel(Integer idFlowCellChannel) {
    this.idFlowCellChannel = idFlowCellChannel;
  }

  public Integer getIdFlowCell() {
    return idFlowCell;
  }
  
  public void setIdFlowCell(Integer idFlowCell) {
    this.idFlowCell = idFlowCell;
  }
  
  public Integer getIdSequenceLane() {
    return idSequenceLane;
  }
  
  public void setIdSequenceLane(Integer idSequenceLane) {
    this.idSequenceLane = idSequenceLane;
  }
  
  public Integer getIdSequencingControl() {
    return idSequencingControl;
  }
  
  public void setIdSequencingControl(Integer idSequencingControl) {
    this.idSequencingControl = idSequencingControl;
  }
  
  public Date getStartDate() {
    return startDate;
  }
  
  public void setStartDate(Date startDate) {
    this.startDate = startDate;
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

  
  public Integer getClustersPerTile() {
    return clustersPerTile;
  }

  
  public void setClustersPerTile(Integer clustersPerTile) {
    this.clustersPerTile = clustersPerTile;
  }

  
  public String getFileName() {
    return fileName;
  }

  
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  
  public Integer getNumberSequencingCyclesActual() {
    return numberSequencingCyclesActual;
  }

  
  public void setNumberSequencingCyclesActual(Integer numberSequencingCyclesActual) {
    this.numberSequencingCyclesActual = numberSequencingCyclesActual;
  }

  
  public Integer getNumber() {
    return number;
  }

  
  public void setNumber(Integer number) {
    this.number = number;
  }

  
  public SequenceLane getSequenceLane() {
    return sequenceLane;
  }

  
  public void setSequenceLane(SequenceLane sequenceLane) {
    this.sequenceLane = sequenceLane;
  }

  
  public SequencingControl getSequencingControl() {
    return sequencingControl;
  }

  
  public void setSequencingControl(SequencingControl sequencingControl) {
    this.sequencingControl = sequencingControl;
  }
  
  public String getContentNumber() {
    if (sequenceLane != null) {
      return sequenceLane.getNumber();
    } else if (sequencingControl != null){
      return sequencingControl.getSequencingControl();
    }else {
      return "";
    }
  }
  
  public Integer getIdSeqRunType() {
    if (sequenceLane != null) {
      return sequenceLane.getIdSeqRunType();
    } else {
      return null;
    }
  }

  public Integer getIdOrganism() {
    if (sequenceLane != null) {
      return sequenceLane.getIdOrganism();
    } else {
      return null;
    }
  }


  public Integer getIdNumberSequencingCycles() {
    if (sequenceLane != null) {
      return sequenceLane.getIdNumberSequencingCycles();
    } else {
      return null;
    }
  }
  
  public Integer getFragmentSizeFrom() {
    if (sequenceLane != null) {
      return sequenceLane.getFragmentSizeFrom();
    } else {
      return null;
    }
  }

  public Integer getFragmentSizeTo() {
    if (sequenceLane != null) {
      return sequenceLane.getFragmentSizeTo();
    } else {
      return null;
    }
  }
  
  public BigDecimal getCalcConcentration() {
    if (sequenceLane != null) {
      return sequenceLane.getCalcConcentration();
    } else {
      return null;
    }
  }
  
  public Integer getSeqPrepLibConcentration() {
    if (sequenceLane != null && sequenceLane.getSample() != null) {
      return sequenceLane.getSample().getSeqPrepLibConcentration();
    } else {
      return null;
    }
  }  
 
  public Integer getSeqPrepGelFragmentSizeFrom() {
    if (sequenceLane != null && sequenceLane.getSample() != null) {
      return sequenceLane.getSample().getSeqPrepGelFragmentSizeFrom();
    } else {
      return null;
    }
  }

  public Integer getSeqPrepGelFragmentSizeTo() {
    if (sequenceLane != null && sequenceLane.getSample() != null) {
      return sequenceLane.getSample().getSeqPrepGelFragmentSizeTo();
    } else {
      return null;
    }
  }
  
  public BigDecimal getSeqPrepStockLibVol() {
    if (sequenceLane != null && sequenceLane.getSample() != null) {
      return sequenceLane.getSample().getSeqPrepStockLibVol();
    } else {
      return null;
    }
  }
 
  
  public BigDecimal getSeqPrepStockEBVol() {
    if (sequenceLane != null && sequenceLane.getSample() != null) {
      return sequenceLane.getSample().getSeqPrepStockEBVol();
    } else {
      return null;
    }
  }
 
  
  public String getWorkflowStatus() {
    if (sequenceLane != null) {
      return sequenceLane.getWorkflowStatus();
    } else {
      if (getPipelineDate() != null) {
        return "Sequenced";
      } else if (getLastCycleDate() != null) {
        return "Completed seq run";
      } else if (this.getLastCycleFailed() != null && this.getLastCycleFailed().equals("Y")) {
        return "Failed seq run";
      } else if (this.getFirstCycleFailed() != null && this.getFirstCycleFailed().equals("Y")) {
        return "Failed 1st cycle seq run";
      } else if (getFirstCycleDate() != null) {
        return "1st cycle done";
      } else {
        return "Ready for seq run";
      }
    }
  }

  
  public FlowCell getFlowCell() {
    return flowCell;
  }

  
  public void setFlowCell(FlowCell flowCell) {
    this.flowCell = flowCell;
  }

  
  public BigDecimal getSampleConcentrationpM() {
    return sampleConcentrationpM;
  }
  
  public String getSampleConcentrationpMDisplay() {
    if (sampleConcentrationpM != null) {
      return Constants.concentrationFormatter.format(sampleConcentrationpM);      
    } else {
      return "";
    }
  }


  
  public void setSampleConcentrationpM(BigDecimal sampleConcentrationpM) {
    this.sampleConcentrationpM = sampleConcentrationpM;
  }

  
  public Date getPipelineDate() {
    return pipelineDate;
  }

  
  public void setPipelineDate(Date pipelineDate) {
    this.pipelineDate = pipelineDate;
  }

  
  public String getPipelineFailed() {
    return pipelineFailed;
  }

  
  public void setPipelineFailed(String pipelineFailed) {
    this.pipelineFailed = pipelineFailed;
  }

  
    
}