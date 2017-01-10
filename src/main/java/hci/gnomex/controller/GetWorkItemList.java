package hci.gnomex.controller;

import hci.dictionary.utility.DictionaryManager;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.*;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.PropertyDictionaryHelper;
import hci.gnomex.utility.Util;

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
import java.util.Set;
import java.util.TreeMap;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.apache.log4j.Logger;

public class GetWorkItemList extends GNomExCommand implements Serializable {

  private static Logger LOG = Logger.getLogger(GetWorkItemList.class);

  private WorkItemFilter filter;

  private DecimalFormat clustersPerTileFormat = new DecimalFormat("###,###,###");
  private String labName = "";
  private static final String DELIM = "%%%";

  private TreeMap<String, List<Element>> clusterGenNodeMap = null;
  private TreeMap<String, List<SequenceLane>> clusterGenLaneMap = null;
  private String clusterGenKey = "";
  private TreeMap<String, String> numberSequencingCyclesAllowedMap = null;



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

        DictionaryHelper dh = DictionaryHelper.getInstance(sess);

        Comparator comparator = null;
        if (filter.getCodeStepNext().equals(Step.QUALITY_CONTROL_STEP) ||
            filter.getCodeStepNext().equals(Step.SEQ_QC) ||
            filter.getCodeStepNext().equals(Step.HISEQ_QC) ||
            filter.getCodeStepNext().equals(Step.MISEQ_QC) ||
            filter.getCodeStepNext().equals(Step.SEQ_FLOWCELL_STOCK) ||
            filter.getCodeStepNext().equals(Step.SEQ_PREP) ||
            filter.getCodeStepNext().equals(Step.HISEQ_PREP) ||
            filter.getCodeStepNext().equals(Step.MISEQ_PREP) ||
            filter.getCodeStepNext().equals(Step.ALL_PREP) ||
            filter.getCodeStepNext().equals(Step.HISEQ_PREP_QC) ||
            filter.getCodeStepNext().equals(Step.MISEQ_PREP_QC) ||
            filter.getCodeStepNext().equals(Step.ALL_PREP_QC)    ) {
          comparator = new SampleComparator();
        } else if (filter.getCodeStepNext().equals(Step.LABELING_STEP)) {
          comparator  = new LabeledSampleComparator();
        } else if (filter.getCodeStepNext().equals(Step.SEQ_CLUSTER_GEN) ||
            filter.getCodeStepNext().equals(Step.HISEQ_CLUSTER_GEN) ||
            filter.getCodeStepNext().equals(Step.MISEQ_CLUSTER_GEN)) {
          comparator  = new LaneComparator();
        } else if (filter.getCodeStepNext().equals(Step.SEQ_RUN) ||
            filter.getCodeStepNext().equals(Step.HISEQ_RUN) ||
            filter.getCodeStepNext().equals(Step.SEQ_DATA_PIPELINE) ||
            filter.getCodeStepNext().equals(Step.HISEQ_DATA_PIPELINE) ||
            filter.getCodeStepNext().equals(Step.MISEQ_DATA_PIPELINE)) {
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
        LOG.info("GetWorkItemList query: " + queryBuf.toString());
        List rows = sess.createQuery(queryBuf.toString()).list();

        Map<Integer, Integer> idsToSkip = this.getSecAdvisor().getBSTXSecurityIdsToExclude(sess, dh, rows, 0, 3);

        Map relatedFlowCellInfoMap = new HashMap();


        Map idSamples = new HashMap();
        for (Iterator i1 = rows.iterator(); i1.hasNext();) {
          Object[] row = (Object[]) i1.next();

          String requestNumber    = (String) row[WorkItemFilter.REQ_NUMBER];
          String status           = (String) row[WorkItemFilter.WI_STATUS];
          String sampleNumber     = (String) row[WorkItemFilter.SAMPLE_NUMBER];
          String itemNumber       = (String) row[WorkItemFilter.SEQ_NUMBER];

          if (idsToSkip.get(row[WorkItemFilter.ID_REQUEST]) != null) {
            // skip for BSTX Security
            continue;
          }

          String key = null;
          if (filter.getCodeStepNext().equals(Step.QUALITY_CONTROL_STEP) ||
              filter.getCodeStepNext().equals(Step.SEQ_QC) ||
              filter.getCodeStepNext().equals(Step.HISEQ_QC) ||
              filter.getCodeStepNext().equals(Step.MISEQ_QC) ||
              filter.getCodeStepNext().equals(Step.SEQ_PREP) ||
              filter.getCodeStepNext().equals(Step.HISEQ_PREP) ||
              filter.getCodeStepNext().equals(Step.MISEQ_PREP) ||
              filter.getCodeStepNext().equals(Step.SEQ_FLOWCELL_STOCK) ||
              filter.getCodeStepNext().equals(Step.ALL_PREP) ||
              filter.getCodeStepNext().equals(Step.HISEQ_PREP_QC) ||
              filter.getCodeStepNext().equals(Step.MISEQ_PREP_QC) ||
              filter.getCodeStepNext().equals(Step.ALL_PREP_QC)    ) {
            key = requestNumber + "," + sampleNumber;
          } else if (filter.getCodeStepNext().equals(Step.LABELING_STEP)) {
            Integer idLabel         = row[WorkItemFilter.LABELING_FAILED] == null || row[WorkItemFilter.ID_LABEL].equals("") ? null : (Integer)row[WorkItemFilter.ID_LABEL];
            Integer idLabeledSample = row[WorkItemFilter.ID_LABEL] == null || row[WorkItemFilter.ID_LABELED_SAMPLE].equals("") ? null : (Integer)row[WorkItemFilter.ID_LABELED_SAMPLE];
            key = requestNumber + "," + sampleNumber + "," + idLabel  + "," + idLabeledSample;
          } else if (filter.getCodeStepNext().equals(Step.SEQ_RUN) ||
              filter.getCodeStepNext().equals(Step.HISEQ_RUN)) {
            FlowCell fc = (FlowCell)row[WorkItemFilter.FLOWCELL_OBJECT];
            FlowCellChannel ch = (FlowCellChannel)row[WorkItemFilter.FLOWCELL_CHANNEL_OBJECT];
            String flowCellNumber              = fc.getNumber();
            Integer flowCellChannelNumber      = ch.getNumber();
            key = flowCellNumber + "," + flowCellChannelNumber;
          } else if (filter.getCodeStepNext().equals(Step.SEQ_DATA_PIPELINE) ||
              filter.getCodeStepNext().equals(Step.HISEQ_DATA_PIPELINE) ||
              filter.getCodeStepNext().equals(Step.MISEQ_DATA_PIPELINE)) {
            FlowCell fc = (FlowCell)row[WorkItemFilter.FLOWCELL_OBJECT];
            FlowCellChannel ch = (FlowCellChannel)row[WorkItemFilter.FLOWCELL_CHANNEL_OBJECT];
            String flowCellNumber              = fc.getNumber();
            Integer flowCellChannelNumber      = ch.getNumber();
            key = flowCellNumber + "," + flowCellChannelNumber;
          } else if (filter.getCodeStepNext().equals(Step.SEQ_CLUSTER_GEN) ||
              filter.getCodeStepNext().equals(Step.HISEQ_CLUSTER_GEN) ||
              filter.getCodeStepNext().equals(Step.MISEQ_CLUSTER_GEN)) {

            String multiplexGroupNumber = row[WorkItemFilter.CLSTR_MULTIPLEX_GROUP_NUM] != null ? ((Integer)row[WorkItemFilter.CLSTR_MULTIPLEX_GROUP_NUM]).toString() : "";
            // Sort the 'On hold' items so they are at the bottom of the list
            // for cluster gen
            key = (status != null && !status.equals("") ? status : " ") + "," + requestNumber + "," + multiplexGroupNumber + "," + itemNumber;
          }else {
            key = requestNumber + "," + itemNumber;
          }

          if (filter.getCodeStepNext().equals(Step.SEQ_CLUSTER_GEN) ||
              filter.getCodeStepNext().equals(Step.HISEQ_CLUSTER_GEN) ||
              filter.getCodeStepNext().equals(Step.MISEQ_CLUSTER_GEN)) {
            Integer idSample = (Integer)row[WorkItemFilter.ID_SAMPLE];
            idSamples.put(idSample, null);
          }

          allRows.put(key, row);
        }

        if ((filter.getCodeStepNext().equals(Step.SEQ_CLUSTER_GEN) ||
            filter.getCodeStepNext().equals(Step.HISEQ_CLUSTER_GEN) ||
            filter.getCodeStepNext().equals(Step.MISEQ_CLUSTER_GEN)) && !idSamples.isEmpty()) {
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



        clusterGenNodeMap = new TreeMap<String, List<Element>>(new ClusterGenComparator());
        clusterGenLaneMap = new TreeMap<String, List<SequenceLane>>(new ClusterGenComparator());

        // Need NumberSequencingCyclesAllowed for WorkItems
        numberSequencingCyclesAllowedMap = getNumberSequencingCyclesAllowedMap(sess);
        Document doc = new Document(new Element("WorkItemList"));
        for(Iterator i = allRows.keySet().iterator(); i.hasNext();) {
          String key = (String)i.next();

          Object[] row = (Object[])allRows.get(key);
          if (filter.getCodeStepNext().equals(Step.QUALITY_CONTROL_STEP) ||
              filter.getCodeStepNext().equals(Step.SEQ_QC) ||
              filter.getCodeStepNext().equals(Step.HISEQ_QC) ||
              filter.getCodeStepNext().equals(Step.MISEQ_QC)) {

            String codeRequestStatus = (String)row[WorkItemFilter.CODE_REQUEST_STATUS] != null ? (String)row[WorkItemFilter.CODE_REQUEST_STATUS] : "";
            Integer idCoreFacility = (Integer)row[WorkItemFilter.REQ_ID_CORE_FACILITY];
            if(idCoreFacility != null) {
              String statusToStartWorkflow = PropertyDictionaryHelper.getInstance(sess).getCoreFacilityProperty(idCoreFacility,PropertyDictionary.STATUS_TO_START_WORKFLOW);
              if(statusToStartWorkflow != null && !statusToStartWorkflow.equals("") && !codeRequestStatus.equals(statusToStartWorkflow)) {
                continue;
              }
            }
          }

          Integer idRequest = (Integer)row[WorkItemFilter.ID_REQUEST];
          String requestNumber = (String)row[WorkItemFilter.REQ_NUMBER];
          String codeRequestCategory = row[WorkItemFilter.CODE_REQUEST_CATEGORY] == null ? "" :  (String)row[WorkItemFilter.CODE_REQUEST_CATEGORY];
          String flowCellNumber = null;
          labName = "";

          if (filter.getCodeStepNext().equals(Step.SEQ_RUN) ||
              filter.getCodeStepNext().equals(Step.HISEQ_RUN)) {
            FlowCell fc = (FlowCell)row[WorkItemFilter.FLOWCELL_OBJECT];
            flowCellNumber            = fc.getNumber();
            if (flowCellNumber != null && !flowCellNumber.equals(prevFlowCellNumber)) {
              alt = !alt;
            }

          } else if (filter.getCodeStepNext().equals(Step.SEQ_DATA_PIPELINE) ||
              filter.getCodeStepNext().equals(Step.HISEQ_DATA_PIPELINE) ||
              filter.getCodeStepNext().equals(Step.MISEQ_DATA_PIPELINE)) {
            FlowCell fc = (FlowCell)row[WorkItemFilter.FLOWCELL_OBJECT];
            flowCellNumber            = fc.getNumber();
            if (flowCellNumber != null && !flowCellNumber.equals(prevFlowCellNumber)) {
              alt = !alt;
            }

          } else {
            if (requestNumber != null && !requestNumber.equals(prevRequestNumber)) {
              alt = !alt;
            }

          }



          // Create a WorkItem XML node.
          Element n = createWorkItemNode(row, key, alt, dh);


          // Fill the XML node with the specific fields for the worklist step.
          if (filter.getCodeStepNext().equals(Step.QUALITY_CONTROL_STEP) ||
              filter.getCodeStepNext().equals(Step.SEQ_QC) ||
              filter.getCodeStepNext().equals(Step.HISEQ_QC) ||
              filter.getCodeStepNext().equals(Step.MISEQ_QC)) {
            fillQC(n, row, codeRequestCategory);

          } else if (filter.getCodeStepNext().equals(Step.LABELING_STEP)) {
            fillLabeling(n, row, codeRequestCategory);

          } else if (filter.getCodeStepNext().equals(Step.HYB_STEP)) {
            fillHyb(n, row, codeRequestCategory);

          } else if (filter.getCodeStepNext().equals(Step.SCAN_EXTRACTION_STEP)) {
            fillExtraction(n, row, codeRequestCategory);
          } else if (filter.getCodeStepNext().equals(Step.SEQ_PREP) ||
              filter.getCodeStepNext().equals(Step.HISEQ_PREP) ||
              filter.getCodeStepNext().equals(Step.MISEQ_PREP) ||
              filter.getCodeStepNext().equals(Step.ALL_PREP)) {
            fillSeqPrep(n, row, codeRequestCategory, seqLibProtocolMap);

          } else if (filter.getCodeStepNext().equals(Step.SEQ_PREP_QC) ||
                  filter.getCodeStepNext().equals(Step.HISEQ_PREP_QC) ||
                  filter.getCodeStepNext().equals(Step.MISEQ_PREP_QC) ||
                  filter.getCodeStepNext().equals(Step.ALL_PREP_QC)) {
            fillSeqPrepQC(n, row, codeRequestCategory);

          } else if (filter.getCodeStepNext().equals(Step.SEQ_FLOWCELL_STOCK)) {
            fillFlowCellStock(n, row, codeRequestCategory);
          } else if (filter.getCodeStepNext().equals(Step.SEQ_CLUSTER_GEN) ||
              filter.getCodeStepNext().equals(Step.HISEQ_CLUSTER_GEN) ||
              filter.getCodeStepNext().equals(Step.MISEQ_CLUSTER_GEN)) {

            String labLastName = (String)row[WorkItemFilter.CLSTR_LAB_LAST_NAME];
            String labFirstName = (String)row[WorkItemFilter.CLSTR_LAB_FIRST_NAME];
            labName = Lab.formatLabNameFirstLast(labFirstName, labLastName);
            String multiplexGroupNumber = row[WorkItemFilter.CLSTR_MULTIPLEX_GROUP_NUM] != null ? ((Integer)row[WorkItemFilter.CLSTR_MULTIPLEX_GROUP_NUM]).toString() : "-1";

            clusterGenKey = requestNumber + DELIM + codeRequestCategory + DELIM + labName + DELIM + idRequest + DELIM + multiplexGroupNumber;

            fillSeqAssemble(n, row, codeRequestCategory, dh, relatedFlowCellInfoMap, clusterGenKey, labName);
          }  else if (filter.getCodeStepNext().equals(Step.SEQ_RUN) ||
              filter.getCodeStepNext().equals(Step.HISEQ_RUN)) {

            fillSeqRun(n, row, codeRequestCategory);


          }  else if (filter.getCodeStepNext().equals(Step.SEQ_DATA_PIPELINE) ||
              filter.getCodeStepNext().equals(Step.HISEQ_DATA_PIPELINE) ||
              filter.getCodeStepNext().equals(Step.MISEQ_DATA_PIPELINE)) {
            fillSeqDataPipeline(n, row, codeRequestCategory);
          }

          // Cluster gen work items are organized in a hierarchical fashion
          if (filter.getCodeStepNext().equals(Step.SEQ_CLUSTER_GEN) ||
              filter.getCodeStepNext().equals(Step.HISEQ_CLUSTER_GEN) ||
              filter.getCodeStepNext().equals(Step.MISEQ_CLUSTER_GEN)) {
            List nodes = clusterGenNodeMap.get(clusterGenKey);

            if (nodes == null) {
              nodes = new ArrayList();
              clusterGenNodeMap.put(clusterGenKey, nodes);
            }
            nodes.add(n);


          } else {
            // All other work items are in a flat XML list
            doc.getRootElement().addContent(n);

          }



          prevRequestNumber = requestNumber;
          if (filter.getCodeStepNext().equals(Step.SEQ_RUN) ||
              filter.getCodeStepNext().equals(Step.HISEQ_RUN) ||
              filter.getCodeStepNext().equals(Step.SEQ_DATA_PIPELINE) ||
              filter.getCodeStepNext().equals(Step.HISEQ_DATA_PIPELINE) ||
              filter.getCodeStepNext().equals(Step.MISEQ_DATA_PIPELINE)) {
            prevFlowCellNumber = flowCellNumber;
          }

        }  // End of for loop of each row from query

        // Organize cluster gen workitems.  Under experiment number,
        // we organize sequence lanes under multiplex group number.
        if (filter.getCodeStepNext().equals(Step.SEQ_CLUSTER_GEN) ||
            filter.getCodeStepNext().equals(Step.HISEQ_CLUSTER_GEN) ||
            filter.getCodeStepNext().equals(Step.MISEQ_CLUSTER_GEN)) {
          organizeSeqAssembleNodes(doc);
        }

        XMLOutputter out = new org.jdom.output.XMLOutputter();
        this.xmlResult = out.outputString(doc);

        setResponsePage(this.SUCCESS_JSP);

      } else {
        this.addInvalidField("Insufficient permissions", "Insufficient permission to manage workflow.");
        setResponsePage(this.ERROR_JSP);
      }



    }catch (Exception e) {
      LOG.error("An exception has occurred in GetWorkItemList ", e);
      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        //closeReadOnlyHibernateSession;
      } catch(Exception e){
        LOG.error("Error", e);
      }
    }

    return this;
  }
  /**
   * 	Soon after April, 2014 we will switch from allowing the user to choose separately: number of sequencing cycles, code request category and seq run type.
   * Instead they will be combined, along with a version number into protocols identified in the NumberSequencingCyclesAllowed table.
   * We will use this TreeMap to identify to which protocol a Work Item belongs by using the following attributes from the Work Item:
   * 1. the Sequence Lane's number of sequencing cycles
   * 2. the Request's code request category
   * 3. the Sequence Lane's Seq Run Type
   *
   * These three will uniquely identify a protocol in NumberSequencingCyclesAllowed
   *
   * @param sess
   * @return TreeMap<String, String>
   */
  private TreeMap<String, String> getNumberSequencingCyclesAllowedMap(Session sess) {
    StringBuffer buf = new StringBuffer();
    buf.append(" SELECT idNumberSequencingCyclesAllowed, idNumberSequencingCycles, codeRequestCategory, idSeqRunType ");
    buf.append(" FROM NumberSequencingCyclesAllowed ");
    buf.append(" WHERE isActive = 'Y' ");
    TreeMap<String,String> numberSequencingCyclesAllowedMap = new TreeMap<String, String>();
    List rows = sess.createQuery(buf.toString()).list();
    for(Iterator i = rows.iterator(); i.hasNext();) {
      Object[] row = (Object[])i.next();
      String key = row[1] + "\t" + row[2] + "\t" + row[3];
      String value = "" + row[0];
      numberSequencingCyclesAllowedMap.put(key, value);
    }

    return numberSequencingCyclesAllowedMap;
  }

  private Element createWorkItemNode(Object[] row, String key, boolean alt, DictionaryHelper dh) {
    Element n = new Element("WorkItem");
    n.setAttribute("key",                    key);
    n.setAttribute("isSelected",             "N");
    n.setAttribute("altColor",               new Boolean(alt).toString());

    n.setAttribute("idRequest",              row[WorkItemFilter.ID_REQUEST] == null ? "" : ((Integer)row[WorkItemFilter.ID_REQUEST]).toString());
    n.setAttribute("requestNumber",          row[WorkItemFilter.REQ_NUMBER] == null ? "" : (String)row[WorkItemFilter.REQ_NUMBER]);
    n.setAttribute("createDate",             row[WorkItemFilter.REQ_CREATE_DATE] == null ? "" :  this.formatDate((java.util.Date)row[WorkItemFilter.REQ_CREATE_DATE]));
    n.setAttribute("codeRequestCategory",    row[WorkItemFilter.CODE_REQUEST_CATEGORY] == null ? "" :  (String)row[WorkItemFilter.CODE_REQUEST_CATEGORY]);
    n.setAttribute("codeRequestCategory1",   row[WorkItemFilter.CODE_REQUEST_CATEGORY] == null ? "" :  (String)row[WorkItemFilter.CODE_REQUEST_CATEGORY]);
    n.setAttribute("idCoreFacility",         row[WorkItemFilter.CODE_REQUEST_CATEGORY] == null ? "" :  dh.getRequestCategoryObject((String)row[WorkItemFilter.CODE_REQUEST_CATEGORY]).getIdCoreFacility().toString());
    n.setAttribute("idAppUser",              row[WorkItemFilter.ID_APP_USER] == null ? "" :  ((Integer)row[WorkItemFilter.ID_APP_USER]).toString());
    n.setAttribute("idLab",                  row[WorkItemFilter.ID_LAB] == null ? "" :  ((Integer)row[WorkItemFilter.ID_LAB]).toString());
    n.setAttribute("idWorkItem",            ((Integer)row[WorkItemFilter.ID_WORK_ITEM]).toString());
    n.setAttribute("codeStepNext",           row[WorkItemFilter.CODE_STEP_NEXT] == null ? "" :  (String)row[WorkItemFilter.CODE_STEP_NEXT]);

    n.setAttribute("idSample",               row[WorkItemFilter.ID_SAMPLE] == null ? "" :  ((Integer)row[WorkItemFilter.ID_SAMPLE]).toString());
    n.setAttribute("sampleNumber",           row[WorkItemFilter.SAMPLE_NUMBER] == null ? "" :  (String)row[WorkItemFilter.SAMPLE_NUMBER]);
    n.setAttribute("idHybridization",        row[WorkItemFilter.ID_HYBRIDIZATION] == null ? "" :  ((Integer)row[WorkItemFilter.ID_HYBRIDIZATION]).toString());
    if (filter.getCodeStepNext().equals(Step.SEQ_CLUSTER_GEN) ||
        filter.getCodeStepNext().equals(Step.HISEQ_CLUSTER_GEN) ||
        filter.getCodeStepNext().equals(Step.MISEQ_CLUSTER_GEN)) {
      n.setAttribute("laneNumber",           row[WorkItemFilter.SEQ_NUMBER] == null ? "" :  (String)row[WorkItemFilter.SEQ_NUMBER]);
      n.setAttribute("number",               row[WorkItemFilter.SEQ_NUMBER] == null ? "" :  (String)row[WorkItemFilter.SEQ_NUMBER]);
    } else {
      n.setAttribute("hybNumber",            row[WorkItemFilter.HYB_NUMBER] == null ? "" :  (String)row[WorkItemFilter.HYB_NUMBER]);
    }
    n.setAttribute("workItemCreateDate",     row[WorkItemFilter.WI_CREATE_DATE] == null ? "" :  this.formatDate((java.sql.Date)row[WorkItemFilter.WI_CREATE_DATE]));

    String appUserName = "";
    if (row[WorkItemFilter.USER_LAST_NAME] != null) {
      appUserName = (String)row[WorkItemFilter.USER_LAST_NAME];
    }
    if (row[WorkItemFilter.USER_FIRST_NAME] != null) {
      if (appUserName.length() > 0) {
        appUserName += ", ";
      }
      appUserName += (String)row[WorkItemFilter.USER_FIRST_NAME];
    }
    n.setAttribute("appUserName",            appUserName);
    n.setAttribute("isDirty","N");


    return n;
  }

  private void fillQC(Element n, Object[] row, String codeRequestCategory) {
    n.setAttribute("qualDate",                   row[WorkItemFilter.SAMPLE_QUAL_DATE] == null ? "" :  this.formatDate((java.sql.Date)row[WorkItemFilter.SAMPLE_QUAL_DATE]));
    n.setAttribute("qualCompleted",              row[WorkItemFilter.SAMPLE_QUAL_DATE] == null ? "N" : "Y");
    n.setAttribute("qualFailed",                 row[WorkItemFilter.SAMPLE_QUAL_FAILED] == null ? "" :  (String)row[WorkItemFilter.SAMPLE_QUAL_FAILED]);
    n.setAttribute("qual260nmTo280nmRatio",      row[WorkItemFilter.QUAL_260_280_RATIO] == null ? "" :  ((BigDecimal)row[WorkItemFilter.QUAL_260_280_RATIO]).toString());
    n.setAttribute("qual260nmTo230nmRatio",      row[WorkItemFilter.QUAL_260_230_RATIO] == null ? "" :  ((BigDecimal)row[WorkItemFilter.QUAL_260_230_RATIO]).toString());
    n.setAttribute("qualCalcConcentration",      row[WorkItemFilter.QUAL_CALC_CONC] == null ? "" :  Constants.concentrationFormatter.format(row[WorkItemFilter.QUAL_CALC_CONC]));
    n.setAttribute("qual28sTo18sRibosomalRatio", row[WorkItemFilter.QUAL_28_18_RIBO_RATIO] == null ? "" :  ((BigDecimal)row[WorkItemFilter.QUAL_28_18_RIBO_RATIO]).toString());
    n.setAttribute("qualRINNumber",              row[WorkItemFilter.QUAL_RIN_NUMBER] == null ? "" :  ((String)row[WorkItemFilter.QUAL_RIN_NUMBER]));
    n.setAttribute("qualBypassed",               row[WorkItemFilter.QUAL_BYPASSED] == null ? "" :  (String)row[WorkItemFilter.QUAL_BYPASSED]);
    n.setAttribute("qualCodeApplication",        row[WorkItemFilter.QUAL_APPLICATION_CODE] == null ? "" :  (String)row[WorkItemFilter.QUAL_APPLICATION_CODE]);
    n.setAttribute("qualCodeBioanalyzerChipType",row[WorkItemFilter.CODE_BIO_CHIP_TYPE] == null ? "" :  (String)row[WorkItemFilter.CODE_BIO_CHIP_TYPE]);
    n.setAttribute("qualFragmentSizeFrom",       row[WorkItemFilter.QUAL_FRAG_SIZE_FROM] == null ? "" :  ((Integer)row[WorkItemFilter.QUAL_FRAG_SIZE_FROM]).toString());
    n.setAttribute("qualFragmentSizeTo",         row[WorkItemFilter.QUAL_FRAG_SIZE_TO] == null ? "" :  ((Integer)row[WorkItemFilter.QUAL_FRAG_SIZE_TO]).toString());
    n.setAttribute("requestCategoryType",        row[WorkItemFilter.REQUEST_CATEGORY_TYPE] == null ? "" :  (String)row[WorkItemFilter.REQUEST_CATEGORY_TYPE]);
    Integer idSampleType                       = row[WorkItemFilter.ID_SAMPLE_TYPE] == null ? 0 : (Integer)row[WorkItemFilter.ID_SAMPLE_TYPE];

    String sampleType = DictionaryManager.getDisplay("hci.gnomex.model.SampleType", idSampleType.toString());
    n.setAttribute("sampleType", sampleType == null ? "" : sampleType);

    String experimentType = "";
    if (RequestCategory.isMicroarrayRequestCategory(codeRequestCategory) ||
        RequestCategory.isIlluminaRequestCategory(codeRequestCategory)) {
      experimentType = DictionaryManager.getDisplay("hci.gnomex.model.RequestCategory", codeRequestCategory);
    } else {
      experimentType = DictionaryManager.getDisplay("hci.gnomex.model.BioanalyzerChipType", n.getAttributeValue("qualCodeBioanalyzerChipType"));
      if(experimentType == null || experimentType.length()==0) {
        // If nothing in BioanalyzerChipType then use RequestCategory so the column isn't blank
        experimentType = DictionaryManager.getDisplay("hci.gnomex.model.RequestCategory", codeRequestCategory);
      }
    }
    n.setAttribute("experimentType", experimentType == null ? "" : experimentType);

    String qualStatus = "";
    if (n.getAttributeValue("qualCompleted").equals("Y")) {
      qualStatus = Constants.STATUS_COMPLETED;
    } else if (n.getAttributeValue("qualFailed").equals("Y")) {
      qualStatus = Constants.STATUS_TERMINATED;
    } else if (n.getAttributeValue("qualBypassed").equals("Y")) {
      qualStatus = Constants.STATUS_BYPASSED;
    } else {
      qualStatus = row[WorkItemFilter.WI_STATUS] == null ? "" :  (String)row[WorkItemFilter.WI_STATUS];
    }
    n.setAttribute("qualStatus", qualStatus);

  }

  private void fillLabeling(Element n, Object[] row, String codeRequestCategory) {
    n.setAttribute("labelingDate",               row[WorkItemFilter.LABELING_DATE] == null ? "" :  this.formatDate((java.sql.Date)row[WorkItemFilter.LABELING_DATE]));
    n.setAttribute("labelingCompleted",          row[WorkItemFilter.LABELING_DATE] == null ? "N" : "Y");
    n.setAttribute("labelingFailed",             row[WorkItemFilter.LABELING_FAILED] == null ? "" :  (String)row[WorkItemFilter.LABELING_FAILED]).toString();
    n.setAttribute("labelingYield",              row[WorkItemFilter.LABELING_YIELD] == null ? "" :  ((BigDecimal)row[WorkItemFilter.LABELING_YIELD]).toString());
    n.setAttribute("idLabel",                    row[WorkItemFilter.ID_LABEL] == null ? "" :  ((Integer)row[WorkItemFilter.ID_LABEL]).toString());
    n.setAttribute("idLabelingProtocol",         row[WorkItemFilter.LABELING_PROTOCOL] == null ? "" :  ((Integer)row[WorkItemFilter.LABELING_PROTOCOL]).toString());
    n.setAttribute("idLabeledSample",            row[WorkItemFilter.ID_LABELED_SAMPLE] == null ? "" :  ((Integer)row[WorkItemFilter.ID_LABELED_SAMPLE]).toString());
    n.setAttribute("codeLabelingReactionSize",   row[WorkItemFilter.CODE_LABELING_REACTION_SIZE] == null ? "" :  (String)row[WorkItemFilter.CODE_LABELING_REACTION_SIZE]);
    n.setAttribute("numberOfReactions",          row[WorkItemFilter.NUMBER_OF_REACTIONS] == null ? "" :  ((Integer)row[WorkItemFilter.NUMBER_OF_REACTIONS]).toString());
    n.setAttribute("labelingBypassed",           row[WorkItemFilter.LABELING_BYPASSED] == null ? "" :  (String)row[WorkItemFilter.LABELING_BYPASSED]);

    String labelingStatus = "";
    if (n.getAttributeValue("labelingCompleted").equals("Y")) {
      labelingStatus = Constants.STATUS_COMPLETED;
    } else if (n.getAttributeValue("labelingFailed").equals("Y")) {
      labelingStatus = Constants.STATUS_TERMINATED;
    } else if (n.getAttributeValue("labelingBypassed").equals("Y")) {
      labelingStatus = Constants.STATUS_BYPASSED;
    } else {
      labelingStatus = row[WorkItemFilter.WI_STATUS] == null ? "" :  (String)row[WorkItemFilter.WI_STATUS];
    }

    n.setAttribute("labelingStatus", labelingStatus);

  }

  private void fillHyb(Element n, Object[] row, String codeRequestCategory) {
    n.setAttribute("hybDate",                   row[WorkItemFilter.HYB_DATE] == null ? "" :  this.formatDate((java.sql.Date)row[WorkItemFilter.HYB_DATE]));
    n.setAttribute("hybCompleted",              row[WorkItemFilter.HYB_DATE] == null ? "N" : "Y");
    n.setAttribute("hybFailed",                 row[WorkItemFilter.HYB_FAILED] == null ? "" :  (String)row[WorkItemFilter.HYB_FAILED]);
    n.setAttribute("idHybProtocol",             row[WorkItemFilter.ID_HYB_PROTOCOL] == null ? "" :  ((Integer)row[WorkItemFilter.ID_HYB_PROTOCOL]).toString());
    n.setAttribute("idSlide",                   row[WorkItemFilter.ID_SLIDE] == null ? "" :  ((Integer)row[WorkItemFilter.ID_SLIDE]).toString());
    n.setAttribute("slideBarcode",              row[WorkItemFilter.SLIDE_BARCODE] == null ? "" :  (String)row[WorkItemFilter.SLIDE_BARCODE]);
    n.setAttribute("idSlideDesign",             row[WorkItemFilter.ID_SLIDE_DESIGN] == null ? "" :  ((Integer)row[WorkItemFilter.ID_SLIDE_DESIGN]).toString());
    n.setAttribute("idArrayCoordinate",         row[WorkItemFilter.ID_ARRAY_COORDINATE] == null ? "" :  ((Integer)row[WorkItemFilter.ID_ARRAY_COORDINATE]).toString());
    n.setAttribute("arrayCoordinate",           row[WorkItemFilter.ARRAY_COORDINATE_NAME] == null ? "" :  (String)row[WorkItemFilter.ARRAY_COORDINATE_NAME]);
    n.setAttribute("hybBypassed",               row[WorkItemFilter.HYB_BYPASSED] == null ? "" :  (String)row[WorkItemFilter.HYB_BYPASSED]);
    n.setAttribute("slideDesignName",           row[WorkItemFilter.SLIDE_DESIGN_NAME] == null ? "" :  (String)row[WorkItemFilter.SLIDE_DESIGN_NAME]);
    n.setAttribute("arraysPerSlide",            row[WorkItemFilter.ARRAYS_PER_SLIDE] == null ? "" :  ((Integer)row[WorkItemFilter.ARRAYS_PER_SLIDE]).toString());

    String hybStatus = "";
    if (n.getAttributeValue("hybCompleted").equals("Y")) {
      hybStatus = Constants.STATUS_COMPLETED;
    } else if (n.getAttributeValue("hybFailed").equals("Y")) {
      hybStatus = Constants.STATUS_TERMINATED;
    } else if (n.getAttributeValue("hybBypassed").equals("Y")) {
      hybStatus = Constants.STATUS_BYPASSED;
    } else {
      hybStatus = row[WorkItemFilter.WI_STATUS] == null ? "" :  (String)row[WorkItemFilter.WI_STATUS];
    }

    n.setAttribute("hybStatus", hybStatus);
  }

  private void fillExtraction(Element n, Object[] row, String codeRequestCategory) {
    n.setAttribute("extractionDate",               row[WorkItemFilter.EXTRACTION_DATE] == null ? "" :  this.formatDate((java.sql.Date)row[WorkItemFilter.EXTRACTION_DATE]));
    n.setAttribute("extractionCompleted",          row[WorkItemFilter.EXTRACTION_DATE] == null ? "N" : "Y");
    n.setAttribute("idScanProtocol",               row[WorkItemFilter.ID_SCAN_PROTOCOL] == null ? "" :  ((Integer)row[WorkItemFilter.ID_SCAN_PROTOCOL]).toString());
    n.setAttribute("idFeatureExtractionProtocol",  row[WorkItemFilter.ID_FEATURE_EXTRACTION_PROTOCOL] == null ? "" :  ((Integer)row[WorkItemFilter.ID_FEATURE_EXTRACTION_PROTOCOL]).toString());
    n.setAttribute("extractionFailed",             row[WorkItemFilter.EXTRACTION_FAILED] == null ? "" :  (String)row[WorkItemFilter.EXTRACTION_FAILED]);
    n.setAttribute("extractionBypassed",           row[WorkItemFilter.EXTRACTION_BYPASSED] == null ? "" :  (String)row[WorkItemFilter.EXTRACTION_BYPASSED]);


    String extractionStatus = "";
    if (n.getAttributeValue("extractionCompleted").equals("Y")) {
      extractionStatus = Constants.STATUS_COMPLETED;
    } else if (n.getAttributeValue("extractionFailed").equals("Y")) {
      extractionStatus = Constants.STATUS_TERMINATED;
    } else if (n.getAttributeValue("extractionBypassed").equals("Y")) {
      extractionStatus = Constants.STATUS_BYPASSED;
    } else {
      extractionStatus = row[WorkItemFilter.WI_STATUS] == null ? "" :  (String)row[WorkItemFilter.WI_STATUS];
    }
    n.setAttribute("extractionStatus", extractionStatus);
  }

  private void fillSeqPrepQC(Element n, Object[] row, String codeRequestCategory) {
    Integer idSampleType = row[WorkItemFilter.QC_ID_SAMPLE_TYPE] == null ? 0 : (Integer)row[WorkItemFilter.QC_ID_SAMPLE_TYPE];
    String sampleType = DictionaryManager.getDisplay("hci.gnomex.model.SampleType", idSampleType.toString());

    Integer idLibPrepQCProtocol = row[WorkItemFilter.ID_LIB_PREP_QC_PROTOCOL] == null ? 0 : (Integer)row[WorkItemFilter.ID_LIB_PREP_QC_PROTOCOL];
    String libPrepQCProtocol = DictionaryManager.getDisplay("hci.gnomex.model.LibraryQCPrepProtocol", idLibPrepQCProtocol.toString());

    n.setAttribute("sampleType", sampleType == null ? "" : sampleType);
    n.setAttribute("sampleVolume", row[WorkItemFilter.SAMPLE_VOLUME] == null ? "" :  ((BigDecimal)row[WorkItemFilter.SAMPLE_VOLUME]).toString());
    n.setAttribute("qcLibConcentration", row[WorkItemFilter.QC_LIB_CONCENTRATION] == null ? "" :  ((BigDecimal)row[WorkItemFilter.QC_LIB_CONCENTRATION]).toString());
    //n.setAttribute("libPrepQCProtocol", libPrepQCProtocol == null ? "" : libPrepQCProtocol);
    n.setAttribute("idLibPrepQCProtocol", row[WorkItemFilter.ID_LIB_PREP_QC_PROTOCOL] == null ? "" : ((Integer)row[WorkItemFilter.ID_LIB_PREP_QC_PROTOCOL]).toString());
  }

  private void fillSeqPrep(Element n, Object[] row, String codeRequestCategory, HashMap<String, Integer> seqLibProtocolMap) {
    n.setAttribute("idSeqLibProtocol",                  row[WorkItemFilter.ID_SEQ_LIB_PROTOCOL] == null ? "" :  ((Integer)row[WorkItemFilter.ID_SEQ_LIB_PROTOCOL]).toString());
    n.setAttribute("seqPrepByCore",                     row[WorkItemFilter.SEQ_PREP_BY_CORE] == null ? "" :  (String)row[WorkItemFilter.SEQ_PREP_BY_CORE]);
    n.setAttribute("seqPrepLibConcentration",           row[WorkItemFilter.SEQ_PREP_LIB_CONC] == null ? "" :  ((BigDecimal)row[WorkItemFilter.SEQ_PREP_LIB_CONC]).toString());
    n.setAttribute("seqPrepQualCodeBioanalyzerChipType",row[WorkItemFilter.SEQ_QUAL_BIO_CHIP_TYPE] == null ? "" :  (String)row[WorkItemFilter.SEQ_QUAL_BIO_CHIP_TYPE]);
    n.setAttribute("seqPrepGelFragmentSizeFrom",        row[WorkItemFilter.SEQ_PREP_GEL_FRAG_SIZE_FROM] == null ? "" :  ((Integer)row[WorkItemFilter.SEQ_PREP_GEL_FRAG_SIZE_FROM]).toString());
    n.setAttribute("seqPrepGelFragmentSizeTo",          row[WorkItemFilter.SEQ_PREP_GEL_FRAG_SIZE_TO] == null ? "" :  ((Integer)row[WorkItemFilter.SEQ_PREP_GEL_FRAG_SIZE_TO]).toString());
    n.setAttribute("seqPrepDate",                       row[WorkItemFilter.SEQ_PREP_DATE] == null ? "" :  this.formatDate((java.sql.Date)row[WorkItemFilter.SEQ_PREP_DATE]));
    n.setAttribute("seqPrepFailed",                     row[WorkItemFilter.SEQ_PREP_FAILED] == null ? "" :  (String)row[WorkItemFilter.SEQ_PREP_FAILED]);
    n.setAttribute("seqPrepBypassed",                   row[WorkItemFilter.SEQ_PREP_BYPASSED] == null ? "" :  (String)row[WorkItemFilter.SEQ_PREP_BYPASSED]);
    n.setAttribute("idSampleType",                      row[WorkItemFilter.PREP_ID_SAMPLE_TYPE] == null ? "" :  ((Integer)row[WorkItemFilter.PREP_ID_SAMPLE_TYPE]).toString());
    // Fill in the seq lib protocol with the default specified in dictionary
    String codeApplication = (String)row[WorkItemFilter.PREP_CODE_APPLICATION];
    if (codeApplication != null) {
      Integer idSeqLibProtocolDefault = seqLibProtocolMap.get(codeApplication);
      if (n.getAttributeValue("seqPrepByCore").equals("Y") &&
          n.getAttributeValue("idSeqLibProtocol").equals("") &&
          idSeqLibProtocolDefault != null) {
        n.setAttribute("idSeqLibProtocol", idSeqLibProtocolDefault.toString());
      }
    }
    n.setAttribute("idOligoBarcode",                      row[WorkItemFilter.PREP_ID_OLIGO_BARCODE_A] == null ? "" :  ((Integer)row[WorkItemFilter.PREP_ID_OLIGO_BARCODE_A]).toString());
    n.setAttribute("barcodeSequence",                     row[WorkItemFilter.PREP_BARCODE_SEQUENCE_A] == null ? "" :  ((String)row[WorkItemFilter.PREP_BARCODE_SEQUENCE_A]));
    n.setAttribute("multiplexGroupNumber",                row[WorkItemFilter.MULTIPLEX_GROUP_NUM] == null ? "" :  ((Integer)row[WorkItemFilter.MULTIPLEX_GROUP_NUM]).toString());
    n.setAttribute("meanLibSizeActual",                   row[WorkItemFilter.MEAN_LIB_SIZE_ACTUAL] == null ? "" :  ((Integer)row[WorkItemFilter.MEAN_LIB_SIZE_ACTUAL]).toString());
    n.setAttribute("idOligoBarcodeB",                     row[WorkItemFilter.PREP_ID_OLIGO_BARCODE_B] == null ? "" :  ((Integer)row[WorkItemFilter.PREP_ID_OLIGO_BARCODE_B]).toString());
    n.setAttribute("barcodeSequenceB",                    row[WorkItemFilter.PREP_BARCODE_SEQUENCE_B] == null ? "" :  ((String)row[WorkItemFilter.PREP_BARCODE_SEQUENCE_B]));
    n.setAttribute("idLibPrepPerformedBy",                row[WorkItemFilter.LIB_PREP_PERFORMED_BY_ID] == null ? "" :  ((Integer)row[WorkItemFilter.LIB_PREP_PERFORMED_BY_ID ]).toString());
    n.setAttribute("seqPrepByCore",                       row[WorkItemFilter.SEQ_PREP_BY_CORE] == null ? "" :  ((String)row[WorkItemFilter.SEQ_PREP_BY_CORE ]));

    String seqPrepStatus = "";
    if (!n.getAttributeValue("seqPrepDate").equals("")) {
      seqPrepStatus = Constants.STATUS_COMPLETED;
    } else if (n.getAttributeValue("seqPrepFailed").equals("Y")) {
      seqPrepStatus = Constants.STATUS_TERMINATED;
    } else if (n.getAttributeValue("seqPrepBypassed").equals("Y")) {
      seqPrepStatus = Constants.STATUS_BYPASSED;
    } else {
      seqPrepStatus = row[WorkItemFilter.WI_STATUS] == null ? "" :  (String)row[WorkItemFilter.WI_STATUS];
    }
    n.setAttribute("seqPrepStatus", seqPrepStatus);
  }

  private void fillFlowCellStock(Element n, Object[] row, String codeRequestCategory) {
    n.setAttribute("seqPrepStockLibVol",         row[WorkItemFilter.SEQ_PREP_STOCK_LIB_VOL] == null ? "" :  ((BigDecimal)row[WorkItemFilter.SEQ_PREP_STOCK_LIB_VOL]).toString());
    n.setAttribute("seqPrepStockEBVol",          row[WorkItemFilter.SEQ_PREP_STOCK_EB_VOL] == null ? "" :  ((BigDecimal)row[WorkItemFilter.SEQ_PREP_STOCK_EB_VOL]).toString());
    n.setAttribute("seqPrepStockDate",           row[WorkItemFilter.SEQ_PREP_STOCK_DATE] == null ? "" :  this.formatDate((java.sql.Date)row[WorkItemFilter.SEQ_PREP_STOCK_DATE]));
    n.setAttribute("seqPrepStockFailed",         row[WorkItemFilter.SEQ_PREP_STOCK_FAILED] == null ? "" :  (String)row[WorkItemFilter.SEQ_PREP_STOCK_FAILED]);
    n.setAttribute("seqPrepStockBypassed",       row[WorkItemFilter.SEQ_PREP_STOCK_BYPASSED] == null ? "" :  (String)row[WorkItemFilter.SEQ_PREP_STOCK_BYPASSED]);

    String seqPrepStockStatus = "";
    if (!n.getAttributeValue("seqPrepStockDate").equals("")) {
      seqPrepStockStatus = Constants.STATUS_COMPLETED;
    } else if (n.getAttributeValue("seqPrepStockFailed").equals("Y")) {
      seqPrepStockStatus = Constants.STATUS_TERMINATED;
    } else if (n.getAttributeValue("seqPrepStockBypassed").equals("Y")) {
      seqPrepStockStatus = Constants.STATUS_BYPASSED;
    } else {
      seqPrepStockStatus = row[WorkItemFilter.WI_STATUS] == null ? "" :  (String)row[WorkItemFilter.WI_STATUS];
    }
    n.setAttribute("seqPrepStockStatus", seqPrepStockStatus);


  }

  private void fillSeqAssemble(Element n, Object[] row, String codeRequestCategory, DictionaryHelper dh, Map relatedFlowCellInfoMap, String clusterGenKey, String theLabName) {
    n.setAttribute("idSequenceLane",               row[WorkItemFilter.CLSTR_ID_SEQUENCE_LANE] == null ? "" :  ((Integer)row[WorkItemFilter.CLSTR_ID_SEQUENCE_LANE]).toString());
    n.setAttribute("idSeqRunType",                 row[WorkItemFilter.CLSTR_ID_SEQ_RUN_TYPE] == null ? "" :  ((Integer)row[WorkItemFilter.CLSTR_ID_SEQ_RUN_TYPE]).toString());
    n.setAttribute("idOrganism",                   row[WorkItemFilter.CLSTR_ID_ORGANISM] == null ? "" :  ((Integer)row[WorkItemFilter.CLSTR_ID_ORGANISM]).toString());
    n.setAttribute("idNumberSequencingCycles",     row[WorkItemFilter.CLSTR_ID_NUM_SEQ_CYCLES] == null ? "" :  ((Integer)row[WorkItemFilter.CLSTR_ID_NUM_SEQ_CYCLES]).toString());
    n.setAttribute("numberSequencingCycles",       row[WorkItemFilter.CLSTR_ID_NUM_SEQ_CYCLES] == null ? "" : dh.getNumberSequencingCycles((Integer)row[WorkItemFilter.CLSTR_ID_NUM_SEQ_CYCLES]));
    n.setAttribute("idOligoBarcode",               row[WorkItemFilter.CLSTR_ID_OLIGO_BARCODE_A] == null ? "" :  ((Integer)row[WorkItemFilter.CLSTR_ID_OLIGO_BARCODE_A]).toString());
    n.setAttribute("barcodeSequence",              row[WorkItemFilter.CLSTR_BARCODE_SEQUENCE_A] == null ? "" :  ((String)row[WorkItemFilter.CLSTR_BARCODE_SEQUENCE_A]));
    n.setAttribute("multiplexGroupNumber",         row[WorkItemFilter.CLSTR_MULTIPLEX_GROUP_NUM] == null ? "" :  ((Integer)row[WorkItemFilter.CLSTR_MULTIPLEX_GROUP_NUM]).toString());
    n.setAttribute("isControl",                    "false");
    n.setAttribute("assembleStatus",               row[WorkItemFilter.WI_STATUS] == null ? "" :  (String)row[WorkItemFilter.WI_STATUS]);
    n.setAttribute("idOligoBarcodeB",               row[WorkItemFilter.CLSTR_ID_OLIGO_BARCODE_B] == null ? "" :  ((Integer)row[WorkItemFilter.CLSTR_ID_OLIGO_BARCODE_B]).toString());
    n.setAttribute("barcodeSequenceB",              row[WorkItemFilter.CLSTR_BARCODE_SEQUENCE_B] == null ? "" :  ((String)row[WorkItemFilter.CLSTR_BARCODE_SEQUENCE_B]));
    n.setAttribute("idNumberSequencingCyclesAllowed", row[WorkItemFilter.CLSTR_ID_NUM_SEQ_CYCLES_ALLOWED] == null ? "" : ((Integer)row[WorkItemFilter.CLSTR_ID_NUM_SEQ_CYCLES_ALLOWED]).toString());

    if(row[WorkItemFilter.CLSTR_ID_NUM_SEQ_CYCLES_ALLOWED] == null) {
      String numberSequencingCyclesAllowedKey = n.getAttributeValue("idNumberSequencingCycles") + "\t" + codeRequestCategory + "\t" +  n.getAttributeValue("idSeqRunType");
      String numberSequencingCyclesAllowed = numberSequencingCyclesAllowedMap.get(numberSequencingCyclesAllowedKey);
      n.setAttribute("idNumberSequencingCyclesAllowed", numberSequencingCyclesAllowed == null ? "" : numberSequencingCyclesAllowed);
    }

    n.setAttribute("labName", theLabName);
    n.setAttribute("idLab",                  row[WorkItemFilter.ID_LAB] == null ? "" :  ((Integer)row[WorkItemFilter.ID_LAB]).toString());

    SequenceLane lane = (SequenceLane)row[WorkItemFilter.CLSTR_SEQ_LANE_OBJECT];
    List theLanes = clusterGenLaneMap.get(clusterGenKey);
    if (theLanes == null) {
      theLanes = new ArrayList();
      clusterGenLaneMap.put(clusterGenKey, theLanes);
    }
    theLanes.add(lane);


    Integer idSample = (Integer)row[WorkItemFilter.ID_SAMPLE];
    Integer idSequenceLane = (Integer)row[WorkItemFilter.CLSTR_ID_SEQUENCE_LANE];

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


  }

  private void fillSeqRun(Element n, Object[] row, String codeRequestCategory) {
    FlowCell fc = (FlowCell)row[WorkItemFilter.FLOWCELL_OBJECT];
    FlowCellChannel ch = (FlowCellChannel)row[WorkItemFilter.FLOWCELL_CHANNEL_OBJECT];

    n.setAttribute("idFlowCellChannel",            ch.getIdFlowCellChannel().toString());
    n.setAttribute("idSeqRunType",                 fc.getIdSeqRunType() != null ? fc.getIdSeqRunType().toString() : "");
    n.setAttribute("idNumberSequencingCycles",     fc.getIdNumberSequencingCycles() != null ? fc.getIdNumberSequencingCycles().toString() : "");
    n.setAttribute("idNumberSequencingCyclesAllowed", fc.getIdNumberSequencingCyclesAllowed() != null ? fc.getIdNumberSequencingCyclesAllowed().toString() : "");
    n.setAttribute("number",                       ch.getContentNumbers());
    n.setAttribute("channelNumber",                ch.getNumber().toString());
    n.setAttribute("sequencingControl",            ch.getIdSequencingControl() != null ? ch.getIdSequencingControl().toString() : "");
    n.setAttribute("firstCycleDate",               ch.getFirstCycleDate() == null ? "" :  this.formatDate(ch.getFirstCycleDate()));
    n.setAttribute("firstCycleCompleted",          ch.getFirstCycleDate() == null ? "N" : "Y");
    n.setAttribute("firstCycleFailed",             ch.getFirstCycleFailed() == null ? "" :  ((String)ch.getFirstCycleFailed()));
    n.setAttribute("lastCycleDate",                ch.getLastCycleDate() == null ? "" :  this.formatDate(ch.getLastCycleDate()));
    n.setAttribute("lastCycleCompleted",           ch.getLastCycleDate() == null ? "N" : "Y");
    n.setAttribute("lastCycleFailed",              ch.getLastCycleFailed() == null ? "" :  ((String)ch.getLastCycleFailed()));
    n.setAttribute("firstCycleStartDate",          ch.getFirstCycleDate() == null ? "" :   this.formatDate(ch.getFirstCycleDate()));
    n.setAttribute("flowCellNumber",               fc.getNumber() == null ? "" :  ((String)fc.getNumber()));
    n.setAttribute("numberSequencingCyclesActual", ch.getNumberSequencingCyclesActual() == null ? "" :  ch.getNumberSequencingCyclesActual().toString());
    n.setAttribute("clustersPerTile",              ch.getClustersPerTile() == null ? "" :  clustersPerTileFormat.format(ch.getClustersPerTile()));
    n.setAttribute("fileName",                     ch.getFileName() == null ? "" :  ((String)ch.getFileName()));
    n.setAttribute("flowCellBarcode",              fc.getBarcode() == null ? "" :  ((String)fc.getBarcode()));
    n.setAttribute("idPipelineProtocol",           ch.getIdPipelineProtocol() == null ? "" : ch.getIdPipelineProtocol().toString());


    String firstCycleStatus = "";
    if (n.getAttributeValue("firstCycleCompleted").equals("Y")) {
      firstCycleStatus = Constants.STATUS_COMPLETED;
    } else if (n.getAttributeValue("firstCycleFailed").equals("Y")) {
      firstCycleStatus = Constants.STATUS_TERMINATED;
    } else if (!n.getAttributeValue("firstCycleStartDate").equals("")) {
      firstCycleStatus = Constants.STATUS_IN_PROGRESS;
    }


    n.setAttribute("firstCycleStatus", firstCycleStatus);

    String lastCycleStatus = "";
    if (n.getAttributeValue("lastCycleCompleted").equals("Y")) {
      lastCycleStatus = Constants.STATUS_COMPLETED;
    } else if (n.getAttributeValue("lastCycleFailed").equals("Y")) {
      lastCycleStatus = Constants.STATUS_TERMINATED;
    }  else {
      lastCycleStatus = row[WorkItemFilter.WI_STATUS] == null ? "" :  (String)row[WorkItemFilter.WI_STATUS];
    }
    n.setAttribute("lastCycleStatus", lastCycleStatus);

  }

  private void fillSeqDataPipeline(Element n, Object[] row, String codeRequestCategory) {

    FlowCell fc = (FlowCell)row[WorkItemFilter.FLOWCELL_OBJECT];
    FlowCellChannel ch = (FlowCellChannel)row[WorkItemFilter.FLOWCELL_CHANNEL_OBJECT];

    n.setAttribute("idFlowCellChannel",            ch.getIdFlowCellChannel().toString());
    n.setAttribute("idSeqRunType",                 fc.getIdSeqRunType().toString());
    n.setAttribute("idNumberSequencingCycles",     fc.getIdNumberSequencingCycles().toString());
    n.setAttribute("number",                       ch.getContentNumbers());
    n.setAttribute("channelNumber",                ch.getNumber().toString());
    n.setAttribute("sequencingControl",            ch.getIdSequencingControl() != null ? ch.getIdSequencingControl().toString() : "");
    n.setAttribute("pipelineDate",                 ch.getPipelineDate() == null ? "" :  this.formatDate(ch.getPipelineDate()));
    n.setAttribute("pipelineCompleted",            ch.getPipelineDate() == null ? "N" : "Y");
    n.setAttribute("pipelineFailed",               ch.getPipelineFailed() == null ? "" :  ((String)ch.getPipelineFailed()));
    n.setAttribute("flowCellNumber",               fc.getNumber() == null ? "" :  ((String)fc.getNumber()));
    n.setAttribute("numberSequencingCyclesActual", ch.getNumberSequencingCyclesActual() == null ? "" :  ch.getNumberSequencingCyclesActual().toString());
    n.setAttribute("clustersPerTile",              ch.getClustersPerTile() == null ? "" :  clustersPerTileFormat.format(ch.getClustersPerTile()));
    n.setAttribute("fileName",                     ch.getFileName() == null ? "" :  ((String)ch.getFileName()));
    n.setAttribute("flowCellBarcode",              fc.getBarcode() == null ? "" :  ((String)fc.getBarcode()));
    n.setAttribute("phiXErrorRate",                ch.getPhiXErrorRate()  == null ? "" :  ch.getPhiXErrorRate().toString());
    n.setAttribute("read1ClustersPassedFilterM",   ch.getRead1ClustersPassedFilterM()  == null ? "" :  ch.getRead1ClustersPassedFilterM().toString());
    n.setAttribute("q30PercentForDisplay",         ch.getQ30PercentForDisplay());
    n.setAttribute("idPipelineProtocol",           ch.getIdPipelineProtocol() == null ? "" : ch.getIdPipelineProtocol().toString());


    String pipelineStatus = "";
    if (n.getAttributeValue("pipelineCompleted").equals("Y")) {
      pipelineStatus = Constants.STATUS_COMPLETED;
    } else if (n.getAttributeValue("pipelineFailed").equals("Y")) {
      pipelineStatus = Constants.STATUS_TERMINATED;
    } else {
      pipelineStatus = row[WorkItemFilter.WI_STATUS] == null ? "" :  (String)row[WorkItemFilter.WI_STATUS];
    }


    n.setAttribute("pipelineStatus", pipelineStatus);

  }

  private void organizeSeqAssembleNodes(Document doc) {

    String prevIdRequest = "";
    int multiplexLaneIdx = 1;
    Element requestNode = null;
    for(Iterator i = clusterGenNodeMap.keySet().iterator(); i.hasNext();) {
      String clusterGenKey = (String)i.next();
      List theLanes = clusterGenLaneMap.get(clusterGenKey);

      String [] tokens = clusterGenKey.split(DELIM);
      String requestNumber = tokens[0];
      String theCodeRequestCategory = tokens[1];
      String theLabName = tokens[2];
      String idRequest = tokens[3];

      if (!idRequest.equals(prevIdRequest)) {
        multiplexLaneIdx = 1;
        prevIdRequest = idRequest;
        requestNode = new Element("Request");
        requestNode.setAttribute("codeRequestCategory", theCodeRequestCategory);
        requestNode.setAttribute("labName", theLabName);
        requestNode.setAttribute("idRequest", idRequest);
        requestNode.setAttribute("number", requestNumber + " " + theLabName);
        doc.getRootElement().addContent(requestNode);
      }

      List<Element> theWorkItemNodes = clusterGenNodeMap.get(clusterGenKey);

      // Get a group of lane lists.  Each lane list represents a multiplex lane node.
      List<Set<SequenceLane>> laneGroups = SequenceLane.getMultiplexLaneGroupsConsiderDupIndexTags(theLanes);
      for (Set<SequenceLane> lanesToMultiplex : laneGroups) {
        Element multiplexLaneNode = new Element("MultiplexLane");
        multiplexLaneNode.setAttribute("number", Integer.valueOf(multiplexLaneIdx).toString());
        multiplexLaneNode.setAttribute("idRequest",  idRequest);
        requestNode.addContent(multiplexLaneNode);

        // Now iterate through the lanes in one multiplex to find
        // the matching WorkItem node (for the lane).  Add
        // those matching WorkItem nodes to the MultiplexLaneNode.
        for (SequenceLane theLane : lanesToMultiplex) {
          Element matchingNode = null;
          for (Element workItemNode : theWorkItemNodes) {
            String idSeqLaneString = workItemNode.getAttributeValue("idSequenceLane");
            Integer idSequenceLane = Integer.valueOf(idSeqLaneString);
            if (idSequenceLane.equals(theLane.getIdSequenceLane())) {
              matchingNode = workItemNode;
              break;
            }
          }
          if (matchingNode != null) {
            multiplexLaneNode.addContent(matchingNode);
          }

        }
        multiplexLaneIdx++;
      }

    }
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
        return Util.compareRequestNumbers(reqNumber1, reqNumber2);
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



      String status1             = tokens1[0];
      String reqNumber1          = tokens1[1];
      String multiplexNumber1    = tokens1[2];
      String itemNumber1         = tokens1[3];


      String status2             = tokens2[0];
      String reqNumber2          = tokens2[1];
      String multiplexNumber2    = tokens2[2];
      String itemNumber2         = tokens2[3];


      String sampleNumber1;
      String seqNumber1 = "";
      // Deal with old (ex. 6142L1) and new (ex. 6142F1_1) naming scheme
      if (itemNumber1.indexOf("F") >= 0) {
        String[] tokens        = itemNumber1.split(PropertyDictionaryHelper.getInstance(null).getProperty(PropertyDictionary.SEQ_LANE_LETTER));
        String number1         = tokens[tokens.length - 1];
        String[] numberTokens  = number1.split(PropertyDictionaryHelper.getInstance(null).getProperty(PropertyDictionary.SEQ_LANE_NUMBER_SEPARATOR));
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
        String[] tokens        = itemNumber2.split(PropertyDictionaryHelper.getInstance(null).getProperty(PropertyDictionary.SEQ_LANE_LETTER));
        String number2         = tokens[tokens.length - 1];
        String[] numberTokens  = number2.split(PropertyDictionaryHelper.getInstance(null).getProperty(PropertyDictionary.SEQ_LANE_NUMBER_SEPARATOR));
        sampleNumber2          = numberTokens[0];
        seqNumber2             = numberTokens[1];
      } else {
        String[] tokens        = itemNumber2.split("L");
        sampleNumber2          = tokens[tokens.length - 1];
        seqNumber2 = "-1";
      }


      if (status1.equals(status2)) {
        if (reqNumber1.equals(reqNumber2)) {
          if (multiplexNumber1.equals(multiplexNumber2)) {
            if (sampleNumber1.equals(sampleNumber2)) {
              return new Integer(seqNumber1).compareTo(new Integer(seqNumber2));
            } else {
              return new Integer(sampleNumber1).compareTo(new Integer(sampleNumber2));
            }

          } else {
            return multiplexNumber1.compareTo(multiplexNumber2);
          }
        } else {
          return Util.compareRequestNumbers(reqNumber1, reqNumber2);
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

  public static class  ClusterGenComparator implements Comparator, Serializable {
    public int compare(Object o1, Object o2) {
      String key1 = (String)o1;
      String key2 = (String)o2;



      String[] tokens1 = key1.split(DELIM);
      String[] tokens2 = key2.split(DELIM);

      Integer idRequest1    = Integer.valueOf(tokens1[3]);
      String number1        = tokens1[0];
      Integer multiPlex1    = Integer.valueOf(tokens1[4]);

      Integer idRequest2    = Integer.valueOf(tokens2[3]);
      String number2        = tokens2[0];
      Integer multiPlex2    = Integer.valueOf(tokens2[4]);

      if (idRequest1.equals(idRequest2)) {
        if (number1.equals(number2)) {
          return multiPlex1.compareTo(multiPlex2);
        } else {
          return number1.compareTo(number2);
        }
      } else {
        return idRequest1.compareTo(idRequest2);
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