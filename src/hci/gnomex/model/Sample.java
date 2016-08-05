package hci.gnomex.model;



import hci.dictionary.utility.DictionaryManager;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.constants.Constants;
import hci.hibernate5utils.HibernateDetailObject;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.jdom.Document;



public class Sample extends HibernateDetailObject {

  private Integer     idSample;
  private Integer     idRequest;
  private Request     request;
  private String      number;
  private String      name;
  private String      description;
  private BigDecimal  concentration;
  private String      codeConcentrationUnit;
  private Organism    organism;
  private Integer     idOrganism;
  private String      otherOrganism;
  private Integer     idSampleType;
  private String      otherSamplePrepMethod;
  private Integer     idSeqLibProtocol;
  private Integer     idSampleSource;
  private String      codeBioanalyzerChipType;
  private Integer     idOligoBarcode;
  private Date        qualDate;
  private String      qualFailed;
  private String      qualBypassed;
  private BigDecimal  qual260nmTo280nmRatio;
  private BigDecimal  qual260nmTo230nmRatio;
  private BigDecimal  qualCalcConcentration;
  private BigDecimal  qual28sTo18sRibosomalRatio;
  private String      qualRINNumber;
  private Integer     qualFragmentSizeFrom;
  private Integer     qualFragmentSizeTo;
  private Integer     fragmentSizeFrom;
  private Integer     fragmentSizeTo;
  private String      seqPrepByCore;
  private Date        seqPrepDate;
  private String      seqPrepFailed;
  private String      seqPrepBypassed;
  private BigDecimal  seqPrepLibConcentration;
  private String      seqPrepQualCodeBioanalyzerChipType;
  private Integer     seqPrepGelFragmentSizeFrom;
  private Integer     seqPrepGelFragmentSizeTo;
  private BigDecimal  seqPrepStockLibVol;
  private BigDecimal  seqPrepStockEBVol;
  private Date        seqPrepStockDate;
  private String      seqPrepStockFailed;
  private String      seqPrepStockBypassed;
  private String      prepInstructions;
  private String      ccNumber;
  private Integer     multiplexGroupNumber;
  private String      barcodeSequence;
  private Set         propertyEntries;
  private Set         treatmentEntries;
  private Set         labeledSamples;
  private Set         wells;
  private Set         sequenceLanes;
  private Integer     meanLibSizeActual;
  private Integer     idOligoBarcodeB;
  private String      barcodeSequenceB;
  private Set         sampleExperimentFiles;
  private Set         workItems;
  private BigDecimal  qubitConcentration;
  private String      groupName;
  private String      qcCodeApplication;
  private BigDecimal  qcLibConcentration;
  private Integer     idLibPrepQCProtocol;
  private BigDecimal  sampleVolume;
  private Integer     libPrepPerformedByID;

  private int         sequenceLaneCount; // a non-persistent variable used for XML

  private String      idSampleString;  // a non-persistent variable used for estimated billing charges
  // before sample has been saved.


  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Integer getIdRequest() {
    return idRequest;
  }

  public void setIdRequest(Integer idRequest) {
    this.idRequest = idRequest;
  }

  public Integer getIdSample() {
    return idSample;
  }

  public void setIdSample(Integer idSample) {
    this.idSample = idSample;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public BigDecimal getQual260nmTo230nmRatio() {
    return qual260nmTo230nmRatio;
  }

  public void setQual260nmTo230nmRatio(BigDecimal qual260nmTo230nmRatio) {
    this.qual260nmTo230nmRatio = qual260nmTo230nmRatio;
  }

  public BigDecimal getQual260nmTo280nmRatio() {
    return qual260nmTo280nmRatio;
  }

  public void setQual260nmTo280nmRatio(BigDecimal qual260nmTo280nmRatio) {
    this.qual260nmTo280nmRatio = qual260nmTo280nmRatio;
  }

  public BigDecimal getQual28sTo18sRibosomalRatio() {
    return qual28sTo18sRibosomalRatio;
  }

  public void setQual28sTo18sRibosomalRatio(BigDecimal qual28sTo18sRibosomalRatio) {
    this.qual28sTo18sRibosomalRatio = qual28sTo18sRibosomalRatio;
  }

  public Date getQualDate() {
    return qualDate;
  }

  public void setQualDate(Date qualDate) {
    this.qualDate = qualDate;
  }

  public String getQualFailed() {
    return qualFailed;
  }

  public void setQualFailed(String qualFailed) {
    this.qualFailed = qualFailed;
  }

  public String getQualRINNumber() {
    return qualRINNumber;
  }

  public void setQualRINNumber(String qualRINNumber) {
    this.qualRINNumber = qualRINNumber;
  }


  public Set getPropertyEntries() {
    return propertyEntries;
  }


  public void setPropertyEntries(Set propertyEntries) {
    this.propertyEntries = propertyEntries;
  }


  public Set getTreatmentEntries() {
    return treatmentEntries;
  }


  public void setTreatmentEntries(Set treatmentEntries) {
    this.treatmentEntries = treatmentEntries;
  }


  public Integer getIdSampleType() {
    return idSampleType;
  }


  public void setIdSampleType(Integer idSampleType) {
    this.idSampleType = idSampleType;
  }


  public Set getLabeledSamples() {
    return labeledSamples;
  }


  public void setLabeledSamples(Set labeledSamples) {
    this.labeledSamples = labeledSamples;
  }


  public BigDecimal getConcentration() {
    return concentration;
  }


  public void setConcentration(BigDecimal concentration) {
    this.concentration = concentration;
  }


  public BigDecimal getQualCalcConcentration() {
    return qualCalcConcentration;
  }


  public void setQualCalcConcentration(BigDecimal qualCalcConcentration) {
    this.qualCalcConcentration = qualCalcConcentration;
  }

  public Integer getRow() {
    return this.idSample;
  }

  public void registerMethodsToExcludeFromXML() {
    this.excludeMethodFromXML("getPropertyEntries");
    this.excludeMethodFromXML("getTreatmentEntries");
    this.excludeMethodFromXML("getLabeledSamples");
    this.excludeMethodFromXML("getRequest");
    this.excludeMethodFromXML("getWells");
    this.excludeMethodFromXML("getASourceWell");
    this.excludeMethodFromXML("getSourceWells");
    this.excludeMethodFromXML("getAssays");
    this.excludeMethodFromXML("getADestinationWell");
    this.excludeMethodFromXML("getSequenceLanes");
    this.excludeMethodFromXML("getRedoFlag");
    this.excludeMethodFromXML("getReactionPlateNames");
  }

  public Document toXMLDocument(List useBaseClass) throws XMLReflectException {
    return toXMLDocument(useBaseClass, DATE_OUTPUT_SQL);
  }

  public Document toXMLDocument(List list, int dateOutputStyle ) throws XMLReflectException {

    Document doc = super.toXMLDocument(list, dateOutputStyle);
    for (Iterator i = getPropertyEntries().iterator(); i.hasNext();) {
      PropertyEntry entry = (PropertyEntry) i.next();
      doc.getRootElement().setAttribute("ANNOT" + entry.getIdProperty().toString(), entry.getValue());
      if (entry.getOtherLabel() != null && !entry.getOtherLabel().equals("")) {
        doc.getRootElement().setAttribute(PropertyEntry.OTHER_LABEL, entry.getOtherLabel());
      }
    }


    return doc;
  }


  public String getCodeBioanalyzerChipType() {
    return codeBioanalyzerChipType;
  }


  public void setCodeBioanalyzerChipType(String codeBioanalyzerChipType) {
    this.codeBioanalyzerChipType = codeBioanalyzerChipType;
  }

  public String getQualCompleted() {
    if (qualDate != null) {
      return "Y";
    } else {
      return "N";
    }
  }

  public String getCanChangeSampleInfo() {
    if (this.getQualDate() != null || 
        (this.getQualFailed() != null && this.getQualFailed().equals("Y"))) {
      return "N";
    } else {
      return "Y";
    }
  }

  public String getCanChangeSampleName() {
    return getCanChangeSampleInfo();
  }

  public String getCanChangeSampleType() {
    return getCanChangeSampleInfo();

  }

  public String getCanChangeSampleConcentration() {
    return getCanChangeSampleInfo();    
  }

  public String getCanChangeSeqPrepByCore() {
    return getCanChangeSampleInfo();
  }

  public String getCodeConcentrationUnit() {
    return codeConcentrationUnit;
  }


  public void setCodeConcentrationUnit(String codeConcentrationUnit) {
    this.codeConcentrationUnit = codeConcentrationUnit;
  }



  public Integer getIdOrganism() {
    return idOrganism;
  }


  public void setIdOrganism(Integer idOrganism) {
    this.idOrganism = idOrganism;
  }


  public String getQualBypassed() {
    return qualBypassed;
  }


  public void setQualBypassed(String qualBypassed) {
    this.qualBypassed = qualBypassed;
  }

  public String getQualStatus() {
    if (qualDate != null) {
      return Constants.STATUS_COMPLETED;
    } else if (this.getQualFailed() != null && this.getQualFailed().equals("Y")) {
      return Constants.STATUS_TERMINATED;
    } else if (this.getQualBypassed() != null && this.getQualBypassed().equals("Y")) {
      return Constants.STATUS_BYPASSED;
    } else {
      return "";
    }
  }

  public String getQualStatusAbbreviated() {
    if (qualDate != null) {
      return "Done";
    } else if (this.getQualFailed() != null && this.getQualFailed().equals("Y")) {
      return "Failed";
    } else if (this.getQualBypassed() != null && this.getQualBypassed().equals("Y")) {
      return "Bypassed";
    } else {
      return "";
    }
  }




  public Integer getFragmentSizeFrom() {
    return fragmentSizeFrom;
  }


  public void setFragmentSizeFrom(Integer fragmentSizeFrom) {
    this.fragmentSizeFrom = fragmentSizeFrom;
  }


  public Integer getFragmentSizeTo() {
    return fragmentSizeTo;
  }


  public void setFragmentSizeTo(Integer fragmentSizeTo) {
    this.fragmentSizeTo = fragmentSizeTo;
  }


  public String getSeqPrepByCore() {
    return seqPrepByCore;
  }


  public void setSeqPrepByCore(String seqPrepByCore) {
    this.seqPrepByCore = seqPrepByCore;
  }


  public Date getSeqPrepDate() {
    return seqPrepDate;
  }


  public void setSeqPrepDate(Date seqPrepDate) {
    this.seqPrepDate = seqPrepDate;
  }

  public String getSeqPrepStatus() {
    if (seqPrepDate != null) {
      return Constants.STATUS_COMPLETED;
    } else if (this.getSeqPrepFailed() != null && this.getSeqPrepFailed().equals("Y")) {
      return Constants.STATUS_TERMINATED;
    } else if (this.getSeqPrepBypassed() != null && this.getSeqPrepBypassed().equals("Y")) {
      return Constants.STATUS_BYPASSED;
    } else {
      return "";
    }
  }


  public String getSeqPrepFailed() {
    return seqPrepFailed;
  }


  public void setSeqPrepFailed(String seqPrepFailed) {
    this.seqPrepFailed = seqPrepFailed;
  }


  public String getSeqPrepBypassed() {
    return seqPrepBypassed;
  }


  public void setSeqPrepBypassed(String seqPrepBypassed) {
    this.seqPrepBypassed = seqPrepBypassed;
  }


  public Integer getQualFragmentSizeFrom() {
    return qualFragmentSizeFrom;
  }


  public void setQualFragmentSizeFrom(Integer qualFragmentSizeFrom) {
    this.qualFragmentSizeFrom = qualFragmentSizeFrom;
  }


  public Integer getQualFragmentSizeTo() {
    return qualFragmentSizeTo;
  }


  public void setQualFragmentSizeTo(Integer qualFragmentSizeTo) {
    this.qualFragmentSizeTo = qualFragmentSizeTo;
  }


  public BigDecimal getSeqPrepLibConcentration() {
    return seqPrepLibConcentration;
  }


  public void setSeqPrepLibConcentration(BigDecimal seqPrepLibConcentration) {
    this.seqPrepLibConcentration = seqPrepLibConcentration;
  }


  public String getSeqPrepQualCodeBioanalyzerChipType() {
    return seqPrepQualCodeBioanalyzerChipType;
  }


  public void setSeqPrepQualCodeBioanalyzerChipType(
      String seqPrepQualCodeBioanalyzerChipType) {
    this.seqPrepQualCodeBioanalyzerChipType = seqPrepQualCodeBioanalyzerChipType;
  }


  public BigDecimal getSeqPrepStockLibVol() {
    return seqPrepStockLibVol;
  }


  public void setSeqPrepStockLibVol(BigDecimal seqPrepStockLibVol) {
    this.seqPrepStockLibVol = seqPrepStockLibVol;
  }


  public BigDecimal getSeqPrepStockEBVol() {
    return seqPrepStockEBVol;
  }


  public void setSeqPrepStockEBVol(BigDecimal seqPrepStockEBVol) {
    this.seqPrepStockEBVol = seqPrepStockEBVol;
  }


  public Date getSeqPrepStockDate() {
    return seqPrepStockDate;
  }


  public void setSeqPrepStockDate(Date seqPrepStockDate) {
    this.seqPrepStockDate = seqPrepStockDate;
  }


  public String getSeqPrepStockFailed() {
    return seqPrepStockFailed;
  }


  public void setSeqPrepStockFailed(String seqPrepStockFailed) {
    this.seqPrepStockFailed = seqPrepStockFailed;
  }


  public String getSeqPrepStockBypassed() {
    return seqPrepStockBypassed;
  }


  public void setSeqPrepStockBypassed(String seqPrepStockBypassed) {
    this.seqPrepStockBypassed = seqPrepStockBypassed;
  }


  public Integer getSeqPrepGelFragmentSizeFrom() {
    return seqPrepGelFragmentSizeFrom;
  }


  public void setSeqPrepGelFragmentSizeFrom(Integer seqPrepGelFragmentSizeFrom) {
    this.seqPrepGelFragmentSizeFrom = seqPrepGelFragmentSizeFrom;
  }


  public Integer getSeqPrepGelFragmentSizeTo() {
    return seqPrepGelFragmentSizeTo;
  }


  public void setSeqPrepGelFragmentSizeTo(Integer seqPrepGelFragmentSizeTo) {
    this.seqPrepGelFragmentSizeTo = seqPrepGelFragmentSizeTo;
  }


  public Integer getIdOligoBarcode() {
    return idOligoBarcode;
  }


  public void setIdOligoBarcode(Integer idOligoBarcode) {
    this.idOligoBarcode = idOligoBarcode;
  }


  public Integer getIdSeqLibProtocol() {
    return idSeqLibProtocol;
  }


  public void setIdSeqLibProtocol(Integer idSeqLibProtocol) {
    this.idSeqLibProtocol = idSeqLibProtocol;
  }


  public int getSequenceLaneCount() {
    return sequenceLaneCount;
  }


  public void setSequenceLaneCount(int sequenceLaneCount) {
    this.sequenceLaneCount = sequenceLaneCount;
  }


  public String getPrepInstructions() {
    return prepInstructions;
  }


  public void setPrepInstructions(String prepInstructions) {
    this.prepInstructions = prepInstructions;
  }

  public String getCcNumber() {
    return ccNumber;
  }

  public void setCcNumber(String ccNumber) {
    this.ccNumber = ccNumber;
  }


  public String getOtherSamplePrepMethod() {
    return otherSamplePrepMethod;
  }


  public void setOtherSamplePrepMethod(String otherSamplePrepMethod) {
    this.otherSamplePrepMethod = otherSamplePrepMethod;
  }


  public Integer getMultiplexGroupNumber() {
    return multiplexGroupNumber;
  }


  public void setMultiplexGroupNumber(Integer multiplexGroupNumber) {
    this.multiplexGroupNumber = multiplexGroupNumber;
  }


  public String getBarcodeSequence() {
    return barcodeSequence;
  }


  public void setBarcodeSequence(String barcodeSequence) {
    this.barcodeSequence = barcodeSequence;
  }

  public String getOtherOrganism() {
    return otherOrganism;
  }

  public void setOtherOrganism(String otherOrganism) {
    this.otherOrganism = otherOrganism;
  }

  public Request getRequest() {
    return request;
  }

  public void setRequest(Request request) {
    this.request = request;
  }

  /*
   * Added this getter so that XML has attribute @numberSequencingLanes
   * so that it is highlighted on 'Add services' when QC request
   * is converted to a sequencing request.
   */
  public String getNumberSequencingLanes() {
    return "";
  }

  public Set getWells() {
    return wells;
  }

  public void setWells(Set wells) {
    this.wells = wells;
  }

  public String getIdSampleString() {
    return idSampleString;
  }

  public void setIdSampleString(String idSampleString) {
    this.idSampleString = idSampleString;
  }

  public PlateWell getASourceWell() {
    PlateWell well = null;
    for (Iterator i = getWells().iterator(); i.hasNext();) {
      PlateWell w = (PlateWell)i.next();
      if (w.getIdPlate() == null || w.getPlate().getCodePlateType().equals(PlateType.SOURCE_PLATE_TYPE)) {
        well = w;
        break;
      }
    }
    return well;
  }

  public List<PlateWell> getSourceWells() {
    ArrayList<PlateWell> wells = new ArrayList<PlateWell>();
    for (Iterator i = getWells().iterator(); i.hasNext();) {
      PlateWell w = (PlateWell)i.next();
      if (w.getIdPlate() == null || w.getPlate().getCodePlateType().equals(PlateType.SOURCE_PLATE_TYPE)) {
        wells.add(w);
      }
    }
    return wells;
  }

  public Map<String, Assay> getAssays() {
    TreeMap<String, Assay> assays = new TreeMap<String, Assay>();
    for (Iterator i = getWells().iterator(); i.hasNext();) {
      PlateWell w = (PlateWell)i.next();
      if (w.getIdPlate() == null || w.getPlate().getCodePlateType().equals(PlateType.SOURCE_PLATE_TYPE)) {
        if (w.getAssay() != null) {
          assays.put(w.getAssay().getName(), w.getAssay());
        }
      }
    }
    return assays;
  }

  public PlateWell getADestinationWell() {
    PlateWell well = null;
    for (Iterator i = getWells().iterator(); i.hasNext();) {
      PlateWell w = (PlateWell)i.next();
      if (w.getIdPlate() == null || w.getPlate().getCodePlateType().equals(PlateType.REACTION_PLATE_TYPE)) {
        well = w;
        break;
      }
    }
    return well;
  }

  public String getRedoFlag() {
    boolean redoFlag = false;
    if (this.getWells() != null) {
      for (PlateWell well : (Set<PlateWell>)this.getWells()) {
        if (well.getRedoFlag() != null && well.getRedoFlag().equals("Y")) {
          redoFlag = true;
        }
      }
    }
    return redoFlag ? "Y" : "N";
  }

  public String getReactionPlateNames() {
    TreeMap<Integer, Plate> rxnPlates = new TreeMap<Integer, Plate>();
    if (this.getWells() != null) {
      for (PlateWell well : (Set<PlateWell>)this.getWells()) {
        if (well.getPlate() != null && well.getPlate().getCodePlateType().equals(PlateType.REACTION_PLATE_TYPE)) {
          rxnPlates.put(well.getIdPlate(), well.getPlate());
        }
      }
    }

    String rxnPlateNames = "";
    for (Integer idPlate : rxnPlates.keySet()) {
      Plate plate = rxnPlates.get(idPlate);
      if (rxnPlateNames.length() > 0) {
        rxnPlateNames += ", ";
      }
      rxnPlateNames += plate.getLabel();
    }
    return rxnPlateNames;

  }

  public String getWorkflowStep() {

    String step = "1";
    if (RequestCategory.isIlluminaRequestCategory(getRequest().getCodeRequestCategory())) {
      int lastStep = this.getSeqPrepByCore() == null || this.getSeqPrepByCore().equals("Y") ? 7 : 5;
      TreeMap<String, String> laneStatusMap = getLaneWorkflowStep(lastStep);
      TreeMap<String, String> workItemStatusMap = getWorkItemStep(lastStep);
      if (this.getSeqPrepByCore() == null || this.getSeqPrepByCore().equals("Y")) {
        if (laneStatusMap.size() > 0) {
          step = laneStatusMap.lastKey();
          if (laneStatusMap.size() > 1) {
            step += ",partial";
          }
        } else if (workItemStatusMap.size() > 0) {
          step = workItemStatusMap.lastKey();
          if (workItemStatusMap.size() > 1) {
            step += ",partial";
          }
        } else if (this.getSeqPrepDate() != null) {
          step = new Integer(lastStep - 3).toString();
        } else if (this.getQualDate() != null) {
          step = new Integer(lastStep - 4).toString();
        } else {
          step = new Integer(lastStep - 5).toString();
        }      

      } else {
        if (laneStatusMap.size() > 0) {
          step = laneStatusMap.lastKey();
          if (laneStatusMap.size() > 1) {
            step += ",partial";
          }
        } else if (workItemStatusMap.size() > 0) {
          step = workItemStatusMap.lastKey();
          if (workItemStatusMap.size() > 1) {
            step += ",partial";
          }
        }  else if (this.getSeqPrepDate() != null) {
          step = new Integer(lastStep - 3).toString();
        } else {
          step = new Integer(lastStep - 4).toString();
        }      

      }
    } else if (RequestCategory.isMicroarrayRequestCategory(request.getCodeRequestCategory())) {
      int lastStep = 5;
      TreeMap<String, String> hybStatusMap = getHybWorkflowStep(5);
      if (hybStatusMap.size() > 0) {
        step = hybStatusMap.lastKey();
        if (hybStatusMap.size() > 1) {
          step += ",partial";
        }
      } else if (this.getQualDate() != null) {
        step = new Integer(lastStep - 3).toString();
      } else {
        step = new Integer(lastStep - 4).toString();
      }
    } else if (request.getRequestCategory().getType().equals(RequestCategoryType.TYPE_QC)) {
      if (this.getQualDate() != null) {
        step = "2";
      } else {
        step = "1";
      }
    } else {
      step = "1";
    }

    return step;
  }

  private TreeMap<String, String> getWorkItemStep(int lastStep) {
    TreeMap<String, String> stepMap = new TreeMap();
    for (SequenceLane lane : (Set<SequenceLane>)this.getSequenceLanes()) {
      for (WorkItem workItem : (Set<WorkItem>) lane.getWorkItems()) {

        // Check that the sample is ready to be added to a flow cell
        if (workItem.getCodeStepNext().equals(Step.HISEQ_CLUSTER_GEN)) {
          stepMap.put(new Integer(lastStep - 2).toString(), null);
        } else if (workItem.getCodeStepNext().equals(Step.MISEQ_CLUSTER_GEN)) {
          stepMap.put(new Integer(lastStep - 2).toString(), null);
        }
      }
    }
    return stepMap;

  }

  private TreeMap<String, String> getLaneWorkflowStep(int lastStep) {
    TreeMap<String, String> stepMap = new TreeMap();
    for (SequenceLane lane : (Set<SequenceLane>)this.getSequenceLanes()) {

      if (lane.getPipelineStatus().equals(Constants.STATUS_COMPLETED)) {
        stepMap.put(new Integer(lastStep).toString(), null);
      } else if (lane.getPipelineStatus().equals(Constants.STATUS_TERMINATED)) {
        stepMap.put(new Integer(lastStep - 1).toString(), null);
      } else if (lane.getLastCycleStatus().equals(Constants.STATUS_COMPLETED)) {
        stepMap.put(new Integer(lastStep - 1).toString(), null);
      } else if (lane.getLastCycleStatus().equals(Constants.STATUS_TERMINATED)) {
        stepMap.put(new Integer(lastStep - 1).toString(), null);
      } else if (lane.getFirstCycleStatus().equals(Constants.STATUS_TERMINATED)) {
        stepMap.put(new Integer(lastStep - 1).toString(), null);
      } else if (lane.getFlowCellChannelFirstCycleDate() != null) {
        stepMap.put(new Integer(lastStep - 1).toString(), null);
      } else if (lane.getFlowCellChannelStartDate() != null) {
        stepMap.put(new Integer(lastStep - 1).toString(), null);
      } else if (lane.getFlowCellChannel() != null) {
        stepMap.put(new Integer(lastStep - 1).toString(), null);
      } 
    }
    return stepMap;

  }

  private TreeMap<String, String> getHybWorkflowStep(int lastStep) {
    TreeMap<String, String> stepMap = new TreeMap<String, String>();
    for (LabeledSample ls : (Set<LabeledSample>)this.getLabeledSamples()) {
      for (Hybridization hyb : (Set<Hybridization>)request.getHybridizations()) {
        if ((hyb.getLabeledSampleChannel1().getIdLabeledSample().equals(ls.getIdLabeledSample())) ||
            (hyb.getLabeledSampleChannel2() != null && hyb.getLabeledSampleChannel2().getIdLabeledSample().equals(ls.getIdLabeledSample()))) {
          if (hyb.getExtractionDate() != null) {
            stepMap.put(new Integer(lastStep).toString(), null);
          } else if (hyb.getHybDate() != null) {
            stepMap.put(new Integer(lastStep - 1).toString(), null);
          } else if (ls.getLabelingDate() != null) {
            stepMap.put(new Integer(lastStep - 2).toString(), null);
          }   
        }
      }
    }
    return stepMap;
  }

  public Set getSequenceLanes() {
    return sequenceLanes;
  }

  public void setSequenceLanes(Set sequenceLanes) {
    this.sequenceLanes = sequenceLanes;
  }

  public Integer getIdSampleSource() {
    return idSampleSource;
  }

  public void setIdSampleSource(Integer idSampleSource) {
    this.idSampleSource = idSampleSource;
  }

  public Integer getMeanLibSizeActual() {
    return meanLibSizeActual;
  }

  public void setMeanLibSizeActual(Integer meanLibSizeActual) {
    this.meanLibSizeActual = meanLibSizeActual;
  }

  public Integer getIdOligoBarcodeB() {
    return idOligoBarcodeB;
  }

  public void setIdOligoBarcodeB(Integer idOligoBarcodeB) {
    this.idOligoBarcodeB = idOligoBarcodeB;
  }

  public String getBarcodeSequenceB() {
    return barcodeSequenceB;
  }

  public void setBarcodeSequenceB(String barcodeSequenceB) {
    this.barcodeSequenceB = barcodeSequenceB;
  }


  public BigDecimal getQubitConcentration() {
    return qubitConcentration;
  }


  public void setQubitConcentration( BigDecimal qubitConcentration ) {
    this.qubitConcentration = qubitConcentration;
  }

  public Set getSampleExperimentFiles() {
    return sampleExperimentFiles;
  }

  public void setSampleExperimentFiles(Set sampleExperimentFiles) {
    this.sampleExperimentFiles = sampleExperimentFiles;
  }

  public Set getWorkItems() {
    return workItems;
  }

  public void setWorkItems(Set workItems) {
    this.workItems = workItems;
  }

  public Organism getOrganism() {
    return organism;
  }

  public void setOrganism(Organism organism) {
    this.organism = organism;
  }

  public String getGroupName() {
    return groupName;
  }

  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }
  
  public String getQcCodeApplication() {
    return qcCodeApplication;
  }
  public void setQcCodeApplication(String qcCodeApplication) {
    this.qcCodeApplication = qcCodeApplication;
  }
  
  public String getBarcodeA() {
    return getIdOligoBarcode() != null ? 
        DictionaryManager.getDisplay("hci.gnomex.model.OligoBarcode", getIdOligoBarcode().toString()) 
        : (getBarcodeSequence() != null && !getBarcodeSequence().equals("") ? getBarcodeSequence() : "");
  }
  public String getBarcodeB() {
    return getIdOligoBarcodeB() != null ? 
        DictionaryManager.getDisplay("hci.gnomex.model.OligoBarcode", getIdOligoBarcodeB().toString()) 
        : (getBarcodeSequenceB() != null && !getBarcodeSequenceB().equals("") ? getBarcodeSequenceB() : "");
  }

  public BigDecimal getQcLibConcentration() {
    return qcLibConcentration;
  }

  public void setQcLibConcentration(BigDecimal qcLibConcentration) {
    this.qcLibConcentration = qcLibConcentration;
  }

  public Integer getIdLibPrepQCProtocol() {
    return idLibPrepQCProtocol;
  }

  public void setIdLibPrepQCProtocol(Integer idLibPrepQCProtocol) {
    this.idLibPrepQCProtocol = idLibPrepQCProtocol;
  }

  public BigDecimal getSampleVolume() {
    return sampleVolume;
  }

  public void setSampleVolume(BigDecimal sampleVolume) {
    this.sampleVolume = sampleVolume;
  }

  public Integer getLibPrepPerformedByID() {
    return libPrepPerformedByID;
  }

  public void setLibPrepPerformedByID(Integer libPrepPerformedByID) {
    this.libPrepPerformedByID = libPrepPerformedByID;
  }
}
