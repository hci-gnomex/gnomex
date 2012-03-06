package hci.gnomex.model;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import org.jdom.Element;

import hci.framework.model.DetailObject;
import hci.framework.model.FieldFormatter;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.constants.Constants;
import hci.gnomex.utility.MultiplexLaneComparator;
import hci.gnomex.utility.SequenceLaneNumberComparator;
import hci.hibernate3utils.HibernateDetailObject;


public class SequenceLane extends HibernateDetailObject {
  
  private Integer         idSequenceLane;
  private String          number;
  private Date            createDate;
  private Integer         idSample;
  private Sample          sample;
  private Integer         idRequest;
  private Request         request;
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
    if (flowCellChannel != null && flowCellChannel.getFlowCell() != null && flowCellChannel.getFlowCell().getNumber() != null) {
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
  
  
  public BigDecimal getPhiXErrorRate() {
    if (flowCellChannel != null) {
      return flowCellChannel.getPhiXErrorRate();
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
    if (getRequest() != null && getRequest().getIsExternal() != null && getRequest().getIsExternal().equals("Y")) {
      return "";
    } else if (getPipelineStatus().equals(Constants.STATUS_COMPLETED)) {
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
  public String getSampleBarcodeSequence() {
    if (sample != null) {
      return sample.getBarcodeSequence();
    } else {
      return "";
    }
  }
  public Integer getSampleIdOligoBarcode() {
    if (sample != null) {
      return sample.getIdOligoBarcode();
    } else {
      return null;
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
    this.excludeMethodFromXML("getRequest");
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
  
  public String getFlowCellChannelSampleConcentrationPmDisplay() {
    if (this.getFlowCellChannel() != null) {
      return this.getFlowCellChannel().getSampleConcentrationpMDisplay();
    } else {
      return "";
    }
  }

  public String getRead1ClustersPassedFilterM() {
    if (this.getFlowCellChannel() != null && this.getFlowCellChannel().getRead1ClustersPassedFilterM() != null ) {
      return this.getFlowCellChannel().getRead1ClustersPassedFilterM().toString();
    } else {
      return "";
    }
  }
  public String getQ30Percent() {
    if (this.getFlowCellChannel() != null && this.getFlowCellChannel().getQ30Percent() != null) {
      return this.getFlowCellChannel().getQ30PercentForDisplay();
    } else {
      return "";
    }
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

  
  public Request getRequest() {
    return request;
  }

  
  public void setRequest(Request request) {
    this.request = request;
  }
  
  public static boolean hasBarcodeTags(Set seqLanes) {
    int tagCount = 0;
    for(Iterator i = seqLanes.iterator(); i.hasNext();) {
      SequenceLane theLane = (SequenceLane)i.next();
      String tag = theLane.getSample().getBarcodeSequence();
      
      if (tag != null && !tag.equals("")) {
        tagCount++;
      }
    }
    return  seqLanes.size() > 0 && seqLanes.size() == tagCount;
  }
  
  public static void addMultiplexLaneNodes(Element parentNode, Collection sequenceLanes, Date requestDate) throws XMLReflectException {
    SortedMap multiplexLaneMap = getMultiplexLaneMap(sequenceLanes, requestDate);
    
    for(Iterator i1 = multiplexLaneMap.keySet().iterator(); i1.hasNext();) {
      String key = (String)i1.next();
      Collection theLanes = (Collection)multiplexLaneMap.get(key);
      
      if (key.equals("")) {
        for(Iterator i2 = theLanes.iterator(); i2.hasNext();) {
          SequenceLane l = (SequenceLane)i2.next();
          Element multiplexLaneNode = l.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();
          multiplexLaneNode.setName("MultiplexLane");
          parentNode.addContent(multiplexLaneNode);
        }
      } else {
        Element multiplexLaneNode = new Element("MultiplexLane");
        // The multiplex group identifier will be either the flow cell number and channel number
        // if the lane has been sequenced or the mutiplex group number.  This identifier is
        // in the third token of the key, separated by a dash.  
        String[] tokens = key.split("-");
        String mutiplexGroupID = tokens[2];
        multiplexLaneNode.setAttribute("number", mutiplexGroupID);
        parentNode.addContent(multiplexLaneNode);
        
        for(Iterator i2 = theLanes.iterator(); i2.hasNext();) {
          SequenceLane l = (SequenceLane)i2.next();
          multiplexLaneNode.addContent(l.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement());
        }
      }
    }
  }
  
  public static int getMultiplexLaneCount(Collection sequenceLanes, Date requestCreateDate) {
    Map multiplexLaneMap = getMultiplexLaneMap(sequenceLanes, requestCreateDate);
    int laneCount = 0;
    for(Iterator i = multiplexLaneMap.keySet().iterator(); i.hasNext();) {
      String key = (String)i.next();
      Collection theLanes = (Collection)multiplexLaneMap.get(key);
      if (key.equals("")) {
        laneCount += theLanes.size();
      } else {
        laneCount++;
      }
    }
    return laneCount;
  
  }
  public static SortedMap getMultiplexLaneMap(Collection sequenceLanes, Date requestCreateDate) {
    TreeMap laneMap = new TreeMap(new MultiplexLaneComparator());
    for(Iterator i = sequenceLanes.iterator(); i.hasNext();) {
      SequenceLane lane = (SequenceLane)i.next();

      String key = "";
      String displayKey = "";
      if (lane.getIdFlowCellChannel() != null && lane.getFlowCellChannel().getFlowCell() != null &&  lane.getFlowCellChannel().getFlowCell().getNumber() != null) {
        displayKey = lane.getFlowCellChannel().getFlowCell().getNumber() + "_" + lane.getFlowCellChannel().getNumber();
        key = "0-" + Integer.valueOf(0) + "-" + displayKey;
      } else if (lane.getSample().getMultiplexGroupNumber() != null) {
        // Only consider the multiplex group of the sample if the lane was
        // created on the same date that the request was submitted.
        // When lanes are added at a later date, we will just group
        // according to the sequence tags.
        displayKey =  lane.getSample().getMultiplexGroupNumber().toString();   
        if (lane.getCreateDate() == null || requestCreateDate == null || lane.getCreateDate().equals(requestCreateDate)) {
          key =  "1-" + lane.getSample().getMultiplexGroupNumber() + "-" + lane.getSample().getMultiplexGroupNumber().toString();             
        } else {
          key =  "2-" + lane.getSample().getMultiplexGroupNumber() + "-" + new SimpleDateFormat("yyyy-MM-dd").format(lane.getCreateDate()) + " " + lane.getSample().getMultiplexGroupNumber().toString();         
        }
      }

      List theLanes = (List)laneMap.get(key);
      if (theLanes == null) {
        theLanes = new ArrayList();
        laneMap.put(key, theLanes);
      }
      theLanes.add(lane);    
    }
    
    return laneMap;
  }
}  
