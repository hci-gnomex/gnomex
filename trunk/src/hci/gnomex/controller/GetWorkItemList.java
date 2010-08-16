package hci.gnomex.controller;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.ArrayCoordinate;
import hci.gnomex.model.FlowCell;
import hci.gnomex.model.FlowCellChannel;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.model.Sample;
import hci.gnomex.model.Step;
import hci.gnomex.model.WorkItemFilter;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.HibernateSession;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;


public class GetWorkItemList extends GNomExCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetWorkItemList.class);
  
  private WorkItemFilter filter;
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    filter = new WorkItemFilter();
    HashMap errors = this.loadDetailObject(request, filter);
    this.addInvalidFields(errors);
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      
      if (this.getSecurityAdvisor().hasPermission(SecurityAdvisor.CAN_MANAGE_WORKFLOW)) {
        Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
        
       
        Comparator comparator = null;
        if (filter.getCodeStepNext().equals(Step.QUALITY_CONTROL_STEP) ||
            filter.getCodeStepNext().equals(Step.SEQ_QC) ||
            filter.getCodeStepNext().equals(Step.HISEQ_QC) ||
            filter.getCodeStepNext().equals(Step.SEQ_FLOWCELL_STOCK) ||
            filter.getCodeStepNext().equals(Step.SEQ_PREP) ||
            filter.getCodeStepNext().equals(Step.HISEQ_PREP)) {
          comparator = new SampleComparator();
        } else if (filter.getCodeStepNext().equals(Step.LABELING_STEP)) {
          comparator  = new LabeledSampleComparator();
        } else if (filter.getCodeStepNext().equals(Step.SEQ_CLUSTER_GEN) ||
                    filter.getCodeStepNext().equals(Step.HISEQ_CLUSTER_GEN)) {
          comparator  = new LaneComparator();
        } else if (filter.getCodeStepNext().equals(Step.SEQ_RUN) ||
                    filter.getCodeStepNext().equals(Step.HISEQ_RUN) ||
                    filter.getCodeStepNext().equals(Step.SEQ_DATA_PIPELINE) ||
                    filter.getCodeStepNext().equals(Step.HISEQ_DATA_PIPELINE)) {
          comparator = new FlowCellChannelComparator();
        } else {
          comparator =  new HybComparator();
        }
        
        // Map the default seq lib protocols for a given sample type
        HashMap<String, Integer> seqLibProtocolMap = new HashMap<String, Integer>();
        if (filter.getCodeStepNext().equals(Step.SEQ_PREP)) {
          StringBuffer buf = new StringBuffer();
          buf.append("SELECT x.codeApplication, x.idSeqLibProtocol ");
          buf.append(" FROM  SeqLibProtocolApplication x ");
          List rows = sess.createQuery(buf.toString()).list();
          if (rows.size() > 0) {
            for(Iterator i = rows.iterator(); i.hasNext();) {
              Object[] row = (Object[])i.next();
              seqLibProtocolMap.put((String)row[0], (Integer)row[1]);
            }
          }
          
        }

        
        TreeMap allRows = new TreeMap(comparator);
        StringBuffer queryBuf = filter.getQuery(this.getSecAdvisor());
        log.info("GetWorkItemList query: " + queryBuf.toString());
        List rows = (List) sess.createQuery(queryBuf.toString()).list();
        
        
        Map relatedFlowCellInfoMap = new HashMap();


        Map idSamples = new HashMap();
        for (Iterator i1 = rows.iterator(); i1.hasNext();) {
          Object[] row = (Object[]) i1.next();
          
          String requestNumber    = (String) row[1];
          String status           = (String) row[8];
          String sampleNumber     = (String) row[10];
          String itemNumber       = (String) row[12];
          

          String key = null;
          if (filter.getCodeStepNext().equals(Step.QUALITY_CONTROL_STEP) ||
              filter.getCodeStepNext().equals(Step.SEQ_QC) ||
              filter.getCodeStepNext().equals(Step.HISEQ_QC) ||
              filter.getCodeStepNext().equals(Step.SEQ_PREP) ||
              filter.getCodeStepNext().equals(Step.HISEQ_PREP) ||
              filter.getCodeStepNext().equals(Step.SEQ_FLOWCELL_STOCK)) {
            key = requestNumber + "," + sampleNumber;
          } else if (filter.getCodeStepNext().equals(Step.LABELING_STEP)) {
            Integer idLabel         = row[17] == null || row[19].equals("") ? null : (Integer)row[19];
            Integer idLabeledSample = row[19] == null || row[21].equals("") ? null : (Integer)row[21];
            key = requestNumber + "," + sampleNumber + "," + idLabel  + "," + idLabeledSample;
          } else if (filter.getCodeStepNext().equals(Step.SEQ_RUN) || 
                      filter.getCodeStepNext().equals(Step.HISEQ_RUN)) {
            FlowCell fc = (FlowCell)row[16];
            FlowCellChannel ch = (FlowCellChannel)row[17];
            String flowCellNumber              = fc.getNumber();
            Integer flowCellChannelNumber      = ch.getNumber();
            key = flowCellNumber + "," + flowCellChannelNumber; 
          } else if (filter.getCodeStepNext().equals(Step.SEQ_DATA_PIPELINE) ||
                      filter.getCodeStepNext().equals(Step.HISEQ_DATA_PIPELINE)) {
            FlowCell fc = (FlowCell)row[16];
            FlowCellChannel ch = (FlowCellChannel)row[17];
            String flowCellNumber              = fc.getNumber();
            Integer flowCellChannelNumber      = ch.getNumber();
            key = flowCellNumber + "," + flowCellChannelNumber; 
          } else if (filter.getCodeStepNext().equals(Step.SEQ_CLUSTER_GEN) ||
                      filter.getCodeStepNext().equals(Step.HISEQ_CLUSTER_GEN)) {
            // Sort the 'On hold' items so they are at the bottom of the list
            // for cluster gen
            key = (status != null && !status.equals("") ? status : " ") + "," + requestNumber + "," + itemNumber;
          }else {
            key = requestNumber + "," + itemNumber;
          }
          
          if (filter.getCodeStepNext().equals(Step.SEQ_CLUSTER_GEN) ||
              filter.getCodeStepNext().equals(Step.HISEQ_CLUSTER_GEN)) {
            Integer idSample = (Integer)row[9];
            idSamples.put(idSample, null);
          }

          allRows.put(key, row);
        }
        
        if ((filter.getCodeStepNext().equals(Step.SEQ_CLUSTER_GEN) || 
             filter.getCodeStepNext().equals(Step.HISEQ_CLUSTER_GEN)) && !idSamples.isEmpty()) {
          List flowCells = sess.createQuery(filter.getRelatedFlowCellQuery(idSamples.keySet()).toString()).list();
          for(Iterator i = flowCells.iterator(); i.hasNext();) {
            Object[] row = (Object[])i.next();
            Integer idSample                 = (Integer)row[0];
            Integer clustersPerTile          = (Integer)row[1];
            BigDecimal sampleConcentrationpM = (BigDecimal)row[2];
            String  seqLaneNumber            = (String)row[3];
            Integer idSequenceLane          = (Integer)row[4];
            
            List infoList = (List)relatedFlowCellInfoMap.get(idSample);
            if (infoList == null) {
              infoList = new ArrayList<RelatedFlowCellInfo>();
            }
            infoList.add(new RelatedFlowCellInfo(clustersPerTile, sampleConcentrationpM, seqLaneNumber, idSequenceLane));
            
            relatedFlowCellInfoMap.put(idSample, infoList);
          }
          
        }
        
        boolean alt = false;
        String prevRequestNumber = "";
        String prevFlowCellNumber = "";
        
        
        DecimalFormat clustersPerTileFormat = new DecimalFormat("###,###,###");
        
      
        Document doc = new Document(new Element("WorkItemList"));
        for(Iterator i = allRows.keySet().iterator(); i.hasNext();) {
          String key = (String)i.next();
          Object[] row = (Object[])allRows.get(key);
          
          
          String requestNumber = (String)row[1];
          String flowCellNumber = null; 
          
          if (filter.getCodeStepNext().equals(Step.SEQ_RUN) || 
              filter.getCodeStepNext().equals(Step.HISEQ_RUN)) {
            FlowCell fc = (FlowCell)row[16];
            flowCellNumber            = fc.getNumber();
            if (flowCellNumber != null && !flowCellNumber.equals(prevFlowCellNumber)) {
              alt = !alt;
            }
            
          } else if (filter.getCodeStepNext().equals(Step.SEQ_DATA_PIPELINE) || 
                      filter.getCodeStepNext().equals(Step.HISEQ_DATA_PIPELINE)) {
            FlowCell fc = (FlowCell)row[16];
            flowCellNumber            = fc.getNumber();
            if (flowCellNumber != null && !flowCellNumber.equals(prevFlowCellNumber)) {
              alt = !alt;
            }
            
          } else {
            if (requestNumber != null && !requestNumber.equals(prevRequestNumber)) {
              alt = !alt;
            }
            
          }
            
          
          Element n = new Element("WorkItem");
          n.setAttribute("key",                    key);
          n.setAttribute("isSelected",             "N");
          n.setAttribute("altColor",               new Boolean(alt).toString());
          
          n.setAttribute("idRequest",              row[0] == null ? "" : ((Integer)row[0]).toString());
          n.setAttribute("requestNumber",          row[1] == null ? "" : (String)row[1]);
          n.setAttribute("createDate",             row[2] == null ? "" :  this.formatDate((java.sql.Date)row[2]));
          n.setAttribute("codeRequestCategory",    row[3] == null ? "" :  (String)row[3]);
          n.setAttribute("codeRequestCategory1",   row[3] == null ? "" :  (String)row[3]);
          n.setAttribute("idAppUser",              row[4] == null ? "" :  ((Integer)row[4]).toString());
          n.setAttribute("idLab",                  row[5] == null ? "" :  ((Integer)row[5]).toString());
          n.setAttribute("idWorkItem",            ((Integer)row[6]).toString());
          n.setAttribute("codeStepNext",           row[7] == null ? "" :  (String)row[7]);

          n.setAttribute("idSample",               row[9] == null ? "" :  ((Integer)row[9]).toString());
          n.setAttribute("sampleNumber",           row[10] == null ? "" :  (String)row[10]);
          n.setAttribute("idHybridization",        row[11] == null ? "" :  ((Integer)row[11]).toString());
          if (filter.getCodeStepNext().equals(Step.SEQ_CLUSTER_GEN) ||
              filter.getCodeStepNext().equals(Step.HISEQ_CLUSTER_GEN)) {
            n.setAttribute("laneNumber",             row[12] == null ? "" :  (String)row[12]);  
          } else {
            n.setAttribute("hybNumber",              row[12] == null ? "" :  (String)row[12]);
          }
          n.setAttribute("workItemCreateDate",     row[13] == null ? "" :  this.formatDate((java.sql.Date)row[13]));
          
          String appUserName = "";
          if (row[14] != null) {
            appUserName = (String)row[14];
          }
          if (row[15] != null) {
            if (appUserName.length() > 0) {
              appUserName += ", ";
            }
            appUserName += (String)row[15];
          }
          n.setAttribute("appUserName",            appUserName);
          n.setAttribute("isDirty","N");
          
          
          if (filter.getCodeStepNext().equals(Step.QUALITY_CONTROL_STEP) ||
              filter.getCodeStepNext().equals(Step.SEQ_QC) ||
              filter.getCodeStepNext().equals(Step.HISEQ_QC)) {
            n.setAttribute("qualDate",                   row[16] == null ? "" :  this.formatDate((java.sql.Date)row[16]));
            n.setAttribute("qualCompleted",              row[16] == null ? "N" : "Y");
            n.setAttribute("qualFailed",                 row[17] == null ? "" :  (String)row[17]);
            n.setAttribute("qual260nmTo280nmRatio",      row[18] == null ? "" :  ((BigDecimal)row[18]).toString());
            n.setAttribute("qual260nmTo230nmRatio",      row[19] == null ? "" :  ((BigDecimal)row[19]).toString());
            n.setAttribute("qualCalcConcentration",      row[20] == null ? "" :  Constants.concentrationFormatter.format((BigDecimal)row[20]));
            n.setAttribute("qual28sTo18sRibosomalRatio", row[21] == null ? "" :  ((BigDecimal)row[21]).toString());
            n.setAttribute("qualRINNumber",              row[22] == null ? "" :  ((String)row[22]));
            n.setAttribute("qualBypassed",               row[23] == null ? "" :  (String)row[23]);
            n.setAttribute("qualCodeBioanalyzerChipType",row[24] == null ? "" :  (String)row[24]);
            n.setAttribute("qualFragmentSizeFrom",       row[25] == null ? "" :  ((Integer)row[25]).toString());
            n.setAttribute("qualFragmentSizeTo",         row[26] == null ? "" :  ((Integer)row[26]).toString());
            
            String qualStatus = "";
            if (n.getAttributeValue("qualCompleted").equals("Y")) {
              qualStatus = Constants.STATUS_COMPLETED;
            } else if (n.getAttributeValue("qualFailed").equals("Y")) {
              qualStatus = Constants.STATUS_TERMINATED;
            } else if (n.getAttributeValue("qualBypassed").equals("Y")) {
              qualStatus = Constants.STATUS_BYPASSED;
            } else {
              qualStatus = row[8] == null ? "" :  (String)row[8];
            }
            n.setAttribute("qualStatus", qualStatus);
            
          } else if (filter.getCodeStepNext().equals(Step.LABELING_STEP)) {
            n.setAttribute("labelingDate",               row[16] == null ? "" :  this.formatDate((java.sql.Date)row[16]));
            n.setAttribute("labelingCompleted",          row[16] == null ? "N" : "Y");
            n.setAttribute("labelingFailed",             row[17] == null ? "" :  (String)row[15]).toString();
            n.setAttribute("labelingYield",              row[18] == null ? "" :  ((BigDecimal)row[18]).toString());
            n.setAttribute("idLabel",                    row[19] == null ? "" :  ((Integer)row[19]).toString());
            n.setAttribute("idLabelingProtocol",         row[20] == null ? "" :  ((Integer)row[20]).toString());
            n.setAttribute("idLabeledSample",            row[21] == null ? "" :  ((Integer)row[21]).toString());
            n.setAttribute("codeLabelingReactionSize",   row[22] == null ? "" :  (String)row[22]);
            n.setAttribute("numberOfReactions",          row[23] == null ? "" :  ((Integer)row[23]).toString());
            n.setAttribute("labelingBypassed",           row[24] == null ? "" :  (String)row[24]);
            
            String labelingStatus = "";
            if (n.getAttributeValue("labelingCompleted").equals("Y")) {
              labelingStatus = Constants.STATUS_COMPLETED;
            } else if (n.getAttributeValue("labelingFailed").equals("Y")) {
              labelingStatus = Constants.STATUS_TERMINATED;
            } else if (n.getAttributeValue("labelingBypassed").equals("Y")) {
              labelingStatus = Constants.STATUS_BYPASSED;
            } else {
              labelingStatus = row[8] == null ? "" :  (String)row[8];
            }
            
            n.setAttribute("labelingStatus", labelingStatus);
            
          } else if (filter.getCodeStepNext().equals(Step.HYB_STEP)) {
            n.setAttribute("hybDate",                   row[16] == null ? "" :  this.formatDate((java.sql.Date)row[16]));
            n.setAttribute("hybCompleted",              row[16] == null ? "N" : "Y");
            n.setAttribute("hybFailed",                 row[17] == null ? "" :  (String)row[17]);
            n.setAttribute("idHybProtocol",             row[18] == null ? "" :  ((Integer)row[18]).toString());
            n.setAttribute("idSlide",                   row[19] == null ? "" :  ((Integer)row[19]).toString());
            n.setAttribute("slideBarcode",              row[20] == null ? "" :  (String)row[20]);
            n.setAttribute("idSlideDesign",             row[21] == null ? "" :  ((Integer)row[21]).toString());
            n.setAttribute("idArrayCoordinate",         row[22] == null ? "" :  ((Integer)row[22]).toString());
            n.setAttribute("arrayCoordinate",           row[23] == null ? "" :  (String)row[23]);
            n.setAttribute("hybBypassed",               row[24] == null ? "" :  (String)row[24]);
            n.setAttribute("slideDesignName",           row[25] == null ? "" :  (String)row[25]);
            n.setAttribute("arraysPerSlide",            row[26] == null ? "" :  ((Integer)row[26]).toString());

            String hybStatus = "";
            if (n.getAttributeValue("hybCompleted").equals("Y")) {
              hybStatus = Constants.STATUS_COMPLETED;
            } else if (n.getAttributeValue("hybFailed").equals("Y")) {
              hybStatus = Constants.STATUS_TERMINATED;
            } else if (n.getAttributeValue("hybBypassed").equals("Y")) {
              hybStatus = Constants.STATUS_BYPASSED;
            } else {
              hybStatus = row[8] == null ? "" :  (String)row[8];
            }
            
            n.setAttribute("hybStatus", hybStatus);

          } else if (filter.getCodeStepNext().equals(Step.SCAN_EXTRACTION_STEP)) {
            n.setAttribute("extractionDate",               row[16] == null ? "" :  this.formatDate((java.sql.Date)row[16]));
            n.setAttribute("extractionCompleted",          row[16] == null ? "N" : "Y");
            n.setAttribute("idScanProtocol",               row[17] == null ? "" :  ((Integer)row[17]).toString());
            n.setAttribute("idFeatureExtractionProtocol",  row[18] == null ? "" :  ((Integer)row[18]).toString());
            n.setAttribute("extractionFailed",             row[19] == null ? "" :  (String)row[19]);
            n.setAttribute("extractionBypassed",           row[20] == null ? "" :  (String)row[20]);
            

            String extractionStatus = "";
            if (n.getAttributeValue("extractionCompleted").equals("Y")) {
              extractionStatus = Constants.STATUS_COMPLETED;
            } else if (n.getAttributeValue("extractionFailed").equals("Y")) {
              extractionStatus = Constants.STATUS_TERMINATED;
            } else if (n.getAttributeValue("extractionBypassed").equals("Y")) {
              extractionStatus = Constants.STATUS_BYPASSED;
            } else {
              extractionStatus = row[8] == null ? "" :  (String)row[8];
            }
            n.setAttribute("extractionStatus", extractionStatus);
          }  else if (filter.getCodeStepNext().equals(Step.SEQ_PREP) ||
                       filter.getCodeStepNext().equals(Step.HISEQ_PREP)) {
            n.setAttribute("idSeqLibProtocol",                  row[16] == null ? "" :  ((Integer)row[16]).toString());
            n.setAttribute("seqPrepByCore",                     row[17] == null ? "" :  (String)row[17]);
            n.setAttribute("seqPrepLibConcentration",           row[18] == null ? "" :  ((BigDecimal)row[18]).toString());
            n.setAttribute("seqPrepQualCodeBioanalyzerChipType",row[19] == null ? "" :  (String)row[19]);
            n.setAttribute("seqPrepGelFragmentSizeFrom",        row[20] == null ? "" :  ((Integer)row[20]).toString());
            n.setAttribute("seqPrepGelFragmentSizeTo",          row[21] == null ? "" :  ((Integer)row[21]).toString());
            n.setAttribute("seqPrepDate",                       row[22] == null ? "" :  this.formatDate((java.sql.Date)row[22]));
            n.setAttribute("seqPrepFailed",                     row[23] == null ? "" :  (String)row[23]);
            n.setAttribute("seqPrepBypassed",                   row[24] == null ? "" :  (String)row[24]);
            n.setAttribute("idSampleType",                      row[25] == null ? "" :  ((Integer)row[25]).toString());
            // Fill in the seq lib protocol with the default specified in dictionary
            // SampleTypeApplication.
            String codeApplication = (String)row[26];
            if (codeApplication != null) {
              Integer idSeqLibProtocolDefault = seqLibProtocolMap.get(codeApplication);
              if (n.getAttributeValue("seqPrepByCore").equals("Y") && 
                  n.getAttributeValue("idSeqLibProtocol").equals("") &&
                  idSeqLibProtocolDefault != null) {
                n.setAttribute("idSeqLibProtocol", idSeqLibProtocolDefault.toString());
              }              
            }

            String seqPrepStatus = "";
            if (!n.getAttributeValue("seqPrepDate").equals("")) {
              seqPrepStatus = Constants.STATUS_COMPLETED;
            } else if (n.getAttributeValue("seqPrepFailed").equals("Y")) {
              seqPrepStatus = Constants.STATUS_TERMINATED;
            } else if (n.getAttributeValue("seqPrepBypassed").equals("Y")) {
              seqPrepStatus = Constants.STATUS_BYPASSED;
            } else {
              seqPrepStatus = row[8] == null ? "" :  (String)row[8];
            }
            n.setAttribute("seqPrepStatus", seqPrepStatus);
          
          }   else if (filter.getCodeStepNext().equals(Step.SEQ_FLOWCELL_STOCK)) {
            n.setAttribute("seqPrepStockLibVol",         row[16] == null ? "" :  ((BigDecimal)row[16]).toString());
            n.setAttribute("seqPrepStockEBVol",          row[17] == null ? "" :  ((BigDecimal)row[17]).toString());
            n.setAttribute("seqPrepStockDate",           row[18] == null ? "" :  this.formatDate((java.sql.Date)row[18]));
            n.setAttribute("seqPrepStockFailed",         row[19] == null ? "" :  (String)row[19]);
            n.setAttribute("seqPrepStockBypassed",       row[20] == null ? "" :  (String)row[20]);

            String seqPrepStockStatus = "";
            if (!n.getAttributeValue("seqPrepStockDate").equals("")) {
              seqPrepStockStatus = Constants.STATUS_COMPLETED;
            } else if (n.getAttributeValue("seqPrepStockFailed").equals("Y")) {
              seqPrepStockStatus = Constants.STATUS_TERMINATED;
            } else if (n.getAttributeValue("seqPrepStockBypassed").equals("Y")) {
              seqPrepStockStatus = Constants.STATUS_BYPASSED;
            } else {
              seqPrepStockStatus = row[8] == null ? "" :  (String)row[8];
            }
            n.setAttribute("seqPrepStockStatus", seqPrepStockStatus);
          
          } else if (filter.getCodeStepNext().equals(Step.SEQ_CLUSTER_GEN) ||
                      filter.getCodeStepNext().equals(Step.HISEQ_CLUSTER_GEN)) {
            n.setAttribute("idSequenceLane",               row[16] == null ? "" :  ((Integer)row[16]).toString());
            n.setAttribute("idSeqRunType",                 row[17] == null ? "" :  ((Integer)row[17]).toString());
            n.setAttribute("idOrganism",                   row[18] == null ? "" :  ((Integer)row[18]).toString());
            n.setAttribute("idNumberSequencingCycles",     row[19] == null ? "" :  ((Integer)row[19]).toString());
            n.setAttribute("idOligoBarcode",               row[20] == null ? "" :  ((Integer)row[20]).toString());
            n.setAttribute("isControl",                    "false");            
            n.setAttribute("assembleStatus",               row[8] == null ? "" :  (String)row[8]);
            
            
            
            Integer idSample = (Integer)row[9];
            Integer idSequenceLane = (Integer)row[16];
            
            StringBuffer infoBuf = new StringBuffer();
            List infoList = (List)relatedFlowCellInfoMap.get(idSample);
            if (infoList != null) {
              for(Iterator i2 = infoList.iterator(); i2.hasNext();) {
                RelatedFlowCellInfo info = (RelatedFlowCellInfo)i2.next();
                if (info.idSequenceLane.equals(idSequenceLane)) {
                  continue;
                } 
                
                infoBuf.append(info.toString());
                if (i2.hasNext()) { 
                  infoBuf.append("\n");
                }
                
              }  
              n.setAttribute("relatedFlowCellInfo", infoBuf.toString());             
            }
          
          }  else if (filter.getCodeStepNext().equals(Step.SEQ_RUN) ||
                       filter.getCodeStepNext().equals(Step.HISEQ_RUN)) {
            
           
            FlowCell fc = (FlowCell)row[16];
            FlowCellChannel ch = (FlowCellChannel)row[17];
            
            n.setAttribute("idFlowCellChannel",            ch.getIdFlowCellChannel().toString());
            n.setAttribute("idSeqRunType",                 fc.getIdSeqRunType().toString());
            n.setAttribute("idNumberSequencingCycles",     fc.getIdNumberSequencingCycles().toString());
            n.setAttribute("number",                       ch.getContentNumbers());
            n.setAttribute("channelNumber",                ch.getNumber().toString());
            n.setAttribute("sequencingControl",            ch.getIdSequencingControl() != null ? ch.getIdSequencingControl().toString() : "");
            n.setAttribute("firstCycleDate",               ch.getFirstCycleDate() == null ? "" :  this.formatDate((java.sql.Date)ch.getFirstCycleDate()));
            n.setAttribute("firstCycleCompleted",          ch.getFirstCycleDate() == null ? "N" : "Y");
            n.setAttribute("firstCycleFailed",             ch.getFirstCycleFailed() == null ? "" :  ((String)ch.getFirstCycleFailed()));
            n.setAttribute("lastCycleDate",                ch.getLastCycleDate() == null ? "" :  this.formatDate((java.sql.Date)ch.getLastCycleDate()));
            n.setAttribute("lastCycleCompleted",           ch.getLastCycleDate() == null ? "N" : "Y");
            n.setAttribute("lastCycleFailed",              ch.getLastCycleFailed() == null ? "" :  ((String)ch.getLastCycleFailed()));
            n.setAttribute("firstCycleStartDate",          ch.getFirstCycleDate() == null ? "" :   this.formatDate((java.sql.Date)ch.getFirstCycleDate()));
            n.setAttribute("flowCellNumber",               fc.getNumber() == null ? "" :  ((String)fc.getNumber()));
            n.setAttribute("numberSequencingCyclesActual", ch.getNumberSequencingCyclesActual() == null ? "" :  ((Integer)ch.getNumberSequencingCyclesActual()).toString());
            n.setAttribute("clustersPerTile",              ch.getClustersPerTile() == null ? "" :  clustersPerTileFormat.format((Integer)ch.getClustersPerTile()));
            n.setAttribute("fileName",                     ch.getFileName() == null ? "" :  ((String)ch.getFileName()));
            n.setAttribute("flowCellBarcode",              fc.getBarcode() == null ? "" :  ((String)fc.getBarcode()));


            String firstCycleStatus = "";
            if (n.getAttributeValue("firstCycleCompleted").equals("Y")) {
              firstCycleStatus = Constants.STATUS_COMPLETED;
            } else if (n.getAttributeValue("firstCycleFailed").equals("Y")) {
              firstCycleStatus = Constants.STATUS_TERMINATED;
            } else if (n.getAttributeValue("firstCycleStartDate") != "") {
              firstCycleStatus = Constants.STATUS_IN_PROGRESS;
            } 
            
            
            n.setAttribute("firstCycleStatus", firstCycleStatus);
          
            String lastCycleStatus = "";
            if (n.getAttributeValue("lastCycleCompleted").equals("Y")) {
              lastCycleStatus = Constants.STATUS_COMPLETED;
            } else if (n.getAttributeValue("lastCycleFailed").equals("Y")) {
              lastCycleStatus = Constants.STATUS_TERMINATED;
            }  else {
              lastCycleStatus = row[8] == null ? "" :  (String)row[8];
            }
            n.setAttribute("lastCycleStatus", lastCycleStatus);
          
         
          }  else if (filter.getCodeStepNext().equals(Step.SEQ_DATA_PIPELINE) ||
                       filter.getCodeStepNext().equals(Step.HISEQ_DATA_PIPELINE)) {

            FlowCell fc = (FlowCell)row[16];
            FlowCellChannel ch = (FlowCellChannel)row[17];
           
            n.setAttribute("idFlowCellChannel",            ch.getIdFlowCellChannel().toString());
            n.setAttribute("idSeqRunType",                 fc.getIdSeqRunType().toString());
            n.setAttribute("idNumberSequencingCycles",     fc.getIdNumberSequencingCycles().toString());
            n.setAttribute("number",                       ch.getContentNumbers());
            n.setAttribute("channelNumber",                ch.getNumber().toString());
            n.setAttribute("sequencingControl",            ch.getIdSequencingControl() != null ? ch.getIdSequencingControl().toString() : "");
            n.setAttribute("pipelineDate",                 ch.getPipelineDate() == null ? "" :  this.formatDate((java.sql.Date)ch.getPipelineDate()));
            n.setAttribute("pipelineCompleted",            ch.getPipelineDate() == null ? "N" : "Y");
            n.setAttribute("pipelineFailed",               ch.getPipelineFailed() == null ? "" :  ((String)ch.getPipelineFailed()));
            n.setAttribute("flowCellNumber",               fc.getNumber() == null ? "" :  ((String)fc.getNumber()));
            n.setAttribute("numberSequencingCyclesActual", ch.getNumberSequencingCyclesActual() == null ? "" :  ((Integer)ch.getNumberSequencingCyclesActual()).toString());
            n.setAttribute("clustersPerTile",              ch.getClustersPerTile() == null ? "" :  clustersPerTileFormat.format((Integer)ch.getClustersPerTile()));
            n.setAttribute("fileName",                     ch.getFileName() == null ? "" :  ((String)ch.getFileName()));
            n.setAttribute("flowCellBarcode",              fc.getBarcode() == null ? "" :  ((String)fc.getBarcode()));
            n.setAttribute("phiXErrorRate",                ch.getPhiXErrorRate()  == null ? "" :  ch.getPhiXErrorRate().toString());
            

            String pipelineStatus = "";
            if (n.getAttributeValue("pipelineCompleted").equals("Y")) {
              pipelineStatus = Constants.STATUS_COMPLETED;
            } else if (n.getAttributeValue("pipelineFailed").equals("Y")) {
              pipelineStatus = Constants.STATUS_TERMINATED;
            } else {
              pipelineStatus = row[8] == null ? "" :  (String)row[8];
            }
               
            
            n.setAttribute("pipelineStatus", pipelineStatus);
          
         
          }
          
          



          
          doc.getRootElement().addContent(n);
          
          prevRequestNumber = requestNumber;
          if (filter.getCodeStepNext().equals(Step.SEQ_RUN) ||
              filter.getCodeStepNext().equals(Step.HISEQ_RUN) ||
              filter.getCodeStepNext().equals(Step.SEQ_DATA_PIPELINE) ||
              filter.getCodeStepNext().equals(Step.HISEQ_DATA_PIPELINE)) {
            prevFlowCellNumber = flowCellNumber;
          }
          
        }
      
        XMLOutputter out = new org.jdom.output.XMLOutputter();
        this.xmlResult = out.outputString(doc);
      
        setResponsePage(this.SUCCESS_JSP);
        
      } else {
        this.addInvalidField("Insufficient permissions", "Insufficient permission to manage workflow.");
        setResponsePage(this.ERROR_JSP);
      }
      
      
   
    }catch (NamingException e){
      log.error("An exception has occurred in GetRequestList ", e);
      e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());
        
    }catch (SQLException e) {
      log.error("An exception has occurred in GetRequestList ", e);
      e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e) {
      log.error("An exception has occurred in GetRequestList ", e);
      e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        this.getSecAdvisor().closeReadOnlyHibernateSession();        
      } catch(Exception e) {
        
      }
    }
    
    return this;
  }
  
  public static class  SampleComparator implements Comparator, Serializable {
    public int compare(Object o1, Object o2) {
      String key1 = (String)o1;
      String key2 = (String)o2;

      
      
      String[] tokens1 = key1.split(",");
      String[] tokens2 = key2.split(",");
      
      String reqNumber1    = tokens1[0];
      String itemNumber1 = tokens1[1];
      
      String reqNumber2    = tokens2[0];
      String itemNumber2 = tokens2[1];

      String[] itemNumberTokens1 = itemNumber1.split("X");
      String number1 = itemNumberTokens1[itemNumberTokens1.length - 1];

      String[] itemNumberTokens2 = itemNumber2.split("X");
      String number2 = itemNumberTokens2[itemNumberTokens2.length - 1];

      if (reqNumber1.equals(reqNumber2)) {
        return new Integer(number1).compareTo(new Integer(number2));        
      } else {
        return reqNumber1.compareTo(reqNumber2);
      }
      
    }
  }
  public static class  HybComparator implements Comparator, Serializable {
    public int compare(Object o1, Object o2) {
      String key1 = (String)o1;
      String key2 = (String)o2;

      
      
      String[] tokens1 = key1.split(",");
      String[] tokens2 = key2.split(",");
      
      String reqNumber1    = tokens1[0];
      String itemNumber1 = tokens1[1];
      
      String reqNumber2    = tokens2[0];
      String itemNumber2 = tokens2[1];

      String[] itemNumberTokens1 = itemNumber1.split("E");
      String number1 = itemNumberTokens1[itemNumberTokens1.length - 1];

      String[] itemNumberTokens2 = itemNumber2.split("E");
      String number2 = itemNumberTokens2[itemNumberTokens2.length - 1];

      if (reqNumber1.equals(reqNumber2)) {
        return new Integer(number1).compareTo(new Integer(number2));        
      } else {
        return reqNumber1.compareTo(reqNumber2);
      }
      
    }
  }
  public static class  LaneComparator implements Comparator, Serializable {
    public int compare(Object o1, Object o2) {
      String key1 = (String)o1;
      String key2 = (String)o2;

      
      
      String[] tokens1 = key1.split(",");
      String[] tokens2 = key2.split(",");
      
      
      
      String status1       = tokens1[0];
      String reqNumber1    = tokens1[1];
      String itemNumber1   = tokens1[2];
      
      String status2       = tokens2[0];
      String reqNumber2    = tokens2[1];
      String itemNumber2   = tokens2[2];

     
      
      String sampleNumber1;
      String seqNumber1 = "";
      // Deal with old (ex. 6142L1) and new (ex. 6142F1_1) naming scheme
      if (itemNumber1.indexOf("F") >= 0) {
        String[] tokens        = itemNumber1.split("F");
        String number1         = tokens[tokens.length - 1];
        String[] numberTokens  = number1.split("_");
        sampleNumber1          = numberTokens[0];
        seqNumber1             = numberTokens[1];        
      } else {
        String[] tokens        = itemNumber1.split("L");
        sampleNumber1          = tokens[tokens.length - 1];
        seqNumber1 = "-1";
      }

      String sampleNumber2;
      String seqNumber2 = "";
      if (itemNumber2.indexOf("F") >= 0) {
        String[] tokens        = itemNumber2.split("F");
        String number2         = tokens[tokens.length - 1];
        String[] numberTokens  = number2.split("_");
        sampleNumber2          = numberTokens[0];
        seqNumber2             = numberTokens[1];        
      } else {
        String[] tokens        = itemNumber2.split("L");
        sampleNumber2          = tokens[tokens.length - 1];
        seqNumber2 = "-1";
      }


      if (status1.equals(status2)) {
        if (reqNumber1.equals(reqNumber2)) {
          if (sampleNumber1.equals(sampleNumber2)) {
            return new Integer(seqNumber1).compareTo(new Integer(seqNumber2));
          } else {
            return new Integer(sampleNumber1).compareTo(new Integer(sampleNumber2));        
          }              
        } else {
          return reqNumber1.compareTo(reqNumber2);
        }        
      } else {
        return status1.compareTo(status2);
      }
    }
  }  
  public static class  FlowCellChannelComparator implements Comparator, Serializable {
    public int compare(Object o1, Object o2) {
      String key1 = (String)o1;
      String key2 = (String)o2;

      
      
      String[] tokens1 = key1.split(",");
      String[] tokens2 = key2.split(",");
      
      
      
      String fcNumber1    = tokens1[0];
      String fcTokens1[] = fcNumber1.split("FC");
      String fcOrdinal1 = fcTokens1[1];
      String channelNumber1 = tokens1[1];
      
      String fcNumber2    = tokens2[0];
      String fcTokens2[] = fcNumber2.split("FC");
      String fcOrdinal2 = fcTokens2[1];
      String channelNumber2 = tokens2[1];
      
      if (fcNumber1.equals(fcNumber2)) {
        return new Integer(channelNumber1).compareTo(new Integer(channelNumber2));
      } else {
        return new Integer(fcOrdinal1).compareTo(new Integer(fcOrdinal2));        
      }      
      
    }
  }  
  
  public static class  LabeledSampleComparator implements Comparator, Serializable {
    public int compare(Object o1, Object o2) {
      String key1 = (String)o1;
      String key2 = (String)o2;

      
      
      String[] tokens1 = key1.split(",");
      String[] tokens2 = key2.split(",");
      
      String reqNumber1       = tokens1[0];
      String sampleNumber1    = tokens1[1];
      String idLabel1         = tokens1[2];
      String idLabeledSample1 = tokens1[3];
      
      String reqNumber2       = tokens2[0];
      String sampleNumber2    = tokens2[1];
      String idLabel2         = tokens2[2];
      String idLabeledSample2 = tokens2[3];


      String[] tokens = sampleNumber1.split("X");
      String sampleNum1 = tokens[tokens.length - 1];

      tokens = sampleNumber2.split("X");
      String sampleNum2 = tokens[tokens.length - 1];


      if (reqNumber1.equals(reqNumber2)) {
        if (sampleNum1.equals(sampleNum2)) {
          if (idLabel1.equals(idLabel2)) {
            return new Integer(idLabeledSample1).compareTo(new Integer(idLabeledSample2));
          } else {
            return new Integer(idLabel1).compareTo(new Integer(idLabel2));
          }
        } else {
          return new Integer(sampleNum1).compareTo(new Integer(sampleNum2));
        }
      } else {
        return reqNumber1.compareTo(reqNumber2);
      }
      
    }
  }
  
  public static class RelatedFlowCellInfo {
    private Integer     clustersPerTile;
    private BigDecimal  sampleConcentrationpM;
    private String      sequenceLaneNumber;
    private Integer     idSequenceLane;
    
    public RelatedFlowCellInfo(Integer clustersPerTile, BigDecimal sampleConcentrationpM, String seqLaneNumber, Integer idSequenceLane) {
      this.clustersPerTile = clustersPerTile;
      this.sampleConcentrationpM = sampleConcentrationpM;
      this.sequenceLaneNumber = seqLaneNumber;
      this.idSequenceLane = idSequenceLane;
    }
    
    public String toString() {
      return sequenceLaneNumber + " - " +
      (sampleConcentrationpM != null ? Constants.concentrationFormatter.format(sampleConcentrationpM) + " pM, " : "") + 
      (clustersPerTile != null ? clustersPerTile + " clusters/tile " : "");          
    }
    
    
    
  }

}