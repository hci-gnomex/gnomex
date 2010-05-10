package hci.gnomex.model;


import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import hci.gnomex.security.SecurityAdvisor;
import hci.framework.model.DetailObject;

public class WorkItemFilter extends DetailObject {
  
  public static int            SAMPLE_LEVEL            = 0;
  public static int            LABELED_SAMPLE_LEVEL    = 1;
  public static int            HYB_LEVEL               = 2;
  public static int            LANE_LEVEL              = 3;
  public static int            FLOW_CELL_LEVEL         = 4;
  
  // Criteria
  private Integer               idAppUser;
  private Integer               idLab;
  private String                requestNumber;
  private String                codeStepNext;
  
  private StringBuffer          queryBuf;
  private boolean              addWhere = true;
  private SecurityAdvisor       secAdvisor;
  
  public StringBuffer getQuery(SecurityAdvisor secAdvisor) {
    this.secAdvisor = secAdvisor;
    
    if(this.codeStepNext.equals(Step.QUALITY_CONTROL_STEP) ||
       this.codeStepNext.equals(Step.SEQ_QC) ||
       this.codeStepNext.equals(Step.SEQ_PREP) ||
       this.codeStepNext.equals(Step.SEQ_FLOWCELL_STOCK)) {
      return getQuery(this.SAMPLE_LEVEL);
    } else if (this.codeStepNext.equals(Step.LABELING_STEP)) {
      return getQuery(this.LABELED_SAMPLE_LEVEL);
    } else if (this.codeStepNext.equals(Step.HYB_STEP) ||
                this.codeStepNext.equals(Step.SCAN_EXTRACTION_STEP)) {
      return getQuery(this.HYB_LEVEL);
    } else if (this.codeStepNext.equals(Step.SEQ_CLUSTER_GEN)) {
      return getQuery(this.LANE_LEVEL);
    } else if (this.codeStepNext.equals(Step.SEQ_RUN) ||
                this.codeStepNext.equals(Step.SEQ_DATA_PIPELINE)) {
      return getQuery(this.FLOW_CELL_LEVEL);
    } else {
      return null;
    }

  }

    
  
  private StringBuffer getQuery(int level) {
    queryBuf = new StringBuffer();
    addWhere = true;
      
    addBaseColumns(queryBuf, level);
    addSpecificColumns(queryBuf);
    addBaseQueryBody(queryBuf, level);      
    
    addRequestCriteria();
    addWorkItemCriteria();
    addSecurityCriteria();
    
    
    return queryBuf;
    
  }
  
  public StringBuffer getRelatedFlowCellQuery(Set idSampleList) {
    queryBuf = new StringBuffer();
    addWhere = true;
    
    queryBuf.append("SELECT lane.idSample, ");
    queryBuf.append("       ch.clustersPerTile, ");
    queryBuf.append("       ch.sampleConcentrationpM, " );
    queryBuf.append("       lane.number, " );
    queryBuf.append("       lane.idSequenceLane " );
    queryBuf.append("FROM   FlowCell fc ");
    queryBuf.append("JOIN   fc.flowCellChannels ch ");
    queryBuf.append("JOIN   ch.sequenceLanes lane ");
    queryBuf.append("WHERE  lane.idSample in (");
    for(Iterator i = idSampleList.iterator(); i.hasNext();) {
      Integer idSample = (Integer)i.next();
      queryBuf.append(idSample.toString());
      if (i.hasNext()) {
        queryBuf.append(", ");
      }
    }
    queryBuf.append(") ");
    queryBuf.append("ORDER BY ch.idFlowCellChannel asc");
        
    return queryBuf;
    
  }
  
  

  public void addBaseColumns(StringBuffer queryBuf, int level) {
    queryBuf.append(" SELECT distinct ");
    queryBuf.append("        req.idRequest, req.number, ");
    queryBuf.append("        req.createDate, req.codeRequestCategory, ");
    queryBuf.append("        req.idAppUser, req.idLab, ");
    queryBuf.append("        wi.idWorkItem, wi.codeStepNext, wi.status, ");
    if (level == this.HYB_LEVEL) {
      queryBuf.append("      0, '',                ");     
      queryBuf.append("      h.idHybridization, h.number, "); 
    } else if (level == this.LANE_LEVEL) {
      queryBuf.append("      s.idSample, s.number,       ");     
      queryBuf.append("      l.idSequenceLane, l.number, "); 
    } else if (level== this.SAMPLE_LEVEL ||
                level == this.LABELED_SAMPLE_LEVEL) {
      queryBuf.append("      s.idSample, s.number, ");      
      queryBuf.append("      0, '',                 ");
    } else if (level == this.FLOW_CELL_LEVEL) {
      queryBuf.append("      0, '',                ");     
      queryBuf.append("      0, '',                 ");
    }
    queryBuf.append(" wi.createDate "); 
    
  }
  
  
  private void addSpecificColumns(StringBuffer queryBuf) {

    if (this.codeStepNext.equals(Step.QUALITY_CONTROL_STEP) ||
        this.codeStepNext.equals(Step.SEQ_QC)) {
      queryBuf.append("      , ");
      queryBuf.append("    s.qualDate, ");
      queryBuf.append("    s.qualFailed, ");
      queryBuf.append("    s.qual260nmTo280nmRatio, ");
      queryBuf.append("    s.qual260nmTo230nmRatio, ");
      queryBuf.append("    s.qualCalcConcentration, ");
      queryBuf.append("    s.qual28sTo18sRibosomalRatio, ");
      queryBuf.append("    s.qualRINNumber, ");
      queryBuf.append("    s.qualBypassed, ");
      queryBuf.append("    s.codeBioanalyzerChipType, ");
      queryBuf.append("    s.qualFragmentSizeFrom, ");
      queryBuf.append("    s.qualFragmentSizeTo ");
    } else if (this.codeStepNext.equals(Step.LABELING_STEP)) {
      queryBuf.append("      , ");
      queryBuf.append("    ls.labelingDate, ");
      queryBuf.append("    ls.labelingFailed, ");
      queryBuf.append("    ls.labelingYield, ");
      queryBuf.append("    ls.idLabel, ");
      queryBuf.append("    ls.idLabelingProtocol, ");
      queryBuf.append("    ls.idLabeledSample, ");
      queryBuf.append("    ls.codeLabelingReactionSize, ");
      queryBuf.append("    ls.numberOfReactions, ");
      queryBuf.append("    ls.labelingBypassed ");
    } else if (this.codeStepNext.equals(Step.HYB_STEP)) {
      queryBuf.append("      , ");
      queryBuf.append("    h.hybDate, ");
      queryBuf.append("    h.hybFailed, ");
      queryBuf.append("    h.idHybProtocol, ");
      queryBuf.append("    h.idSlide, ");
      queryBuf.append("    slide.barcode, ");
      queryBuf.append("    h.idSlideDesign, ");
      queryBuf.append("    h.idArrayCoordinate, ");
      queryBuf.append("    ac.name, ");
      queryBuf.append("    h.hybBypassed ");
    }else if (this.codeStepNext.equals(Step.SCAN_EXTRACTION_STEP)) {
      queryBuf.append("      , ");
      queryBuf.append("    h.extractionDate, ");
      queryBuf.append("    h.idScanProtocol, ");
      queryBuf.append("    h.idFeatureExtractionProtocol, ");
      queryBuf.append("    h.extractionFailed, ");
      queryBuf.append("    h.extractionBypassed ");
    } else if (this.codeStepNext.equals(Step.SEQ_PREP)) {
      queryBuf.append("      , ");
      queryBuf.append("    s.idSeqLibProtocol, ");
      queryBuf.append("    s.seqPrepByCore, ");
      queryBuf.append("    s.seqPrepLibConcentration, ");
      queryBuf.append("    s.seqPrepQualCodeBioanalyzerChipType, ");
      queryBuf.append("    s.seqPrepGelFragmentSizeFrom, ");
      queryBuf.append("    s.seqPrepGelFragmentSizeTo, ");
      queryBuf.append("    s.seqPrepDate, ");
      queryBuf.append("    s.seqPrepFailed, ");
      queryBuf.append("    s.seqPrepBypassed, ");
      queryBuf.append("    s.idSampleType, ");
      queryBuf.append("    req.codeApplication ");
    }  else if (this.codeStepNext.equals(Step.SEQ_FLOWCELL_STOCK)) {
      queryBuf.append("      , ");
      queryBuf.append("    s.seqPrepStockLibVol, ");
      queryBuf.append("    s.seqPrepStockEBVol, ");
      queryBuf.append("    s.seqPrepStockDate, ");
      queryBuf.append("    s.seqPrepStockFailed, ");
      queryBuf.append("    s.seqPrepStockBypassed ");
    }  else if (this.codeStepNext.equals(Step.SEQ_CLUSTER_GEN)) {
      queryBuf.append("      , ");
      queryBuf.append("    l.idSequenceLane, ");
      queryBuf.append("    l.idSeqRunType, ");
      queryBuf.append("    s.idOrganism, ");
      queryBuf.append("    l.idNumberSequencingCycles, ");
      queryBuf.append("    s.idOligoBarcode ");
    } else if (this.codeStepNext.equals(Step.SEQ_RUN)) {
      queryBuf.append("      , ");
      queryBuf.append("    fc, ");
      queryBuf.append("    ch ");

      
    } else if (this.codeStepNext.equals(Step.SEQ_DATA_PIPELINE)) {
      queryBuf.append("      , ");
      queryBuf.append("    fc, ");
      queryBuf.append("    ch ");
      
    }
    

    
  }
  
  private void addBaseQueryBody(StringBuffer queryBuf, int level) {
    
    queryBuf.append(" FROM                WorkItem wi ");
      
    if (level == SAMPLE_LEVEL) {
      queryBuf.append(" JOIN         wi.request as req ");
      queryBuf.append(" JOIN         wi.sample s ");     
    } else if (level == LABELED_SAMPLE_LEVEL) {
      queryBuf.append(" JOIN         wi.request as req ");
      queryBuf.append(" JOIN         wi.labeledSample ls ");
      queryBuf.append(" JOIN         ls.sample s ");
    } else if (level == LANE_LEVEL) {
      queryBuf.append(" JOIN         wi.request as req ");
      queryBuf.append(" JOIN         wi.sequenceLane l ");
      queryBuf.append(" JOIN         l.sample s ");
    } else if (level == HYB_LEVEL) {
      queryBuf.append(" JOIN         wi.request as req ");
      queryBuf.append(" JOIN         wi.hybridization h ");      
      queryBuf.append(" LEFT JOIN    h.slide  slide ");
      queryBuf.append(" LEFT JOIN    h.arrayCoordinate ac ");
    } else if (level == FLOW_CELL_LEVEL) {
      queryBuf.append(" LEFT JOIN    wi.request as req ");
      queryBuf.append(" LEFT JOIN    wi.flowCellChannel as ch ");
      queryBuf.append(" LEFT JOIN    ch.flowCell as fc ");
      queryBuf.append(" LEFT JOIN    ch.sequenceLanes as lane ");
      queryBuf.append(" LEFT JOIN    lane.sample as s ");
      queryBuf.append(" LEFT JOIN    ch.sequencingControl as control ");
    }
  }
  
  
 
  
 
 
  
  

  private void addRequestCriteria() {
    
    // Search by lab 
    if (idLab != null){
      this.addWhereOrAnd();
      queryBuf.append(" req.idLab =");
      queryBuf.append(idLab);
    } 
    // Search by user 
    if (idAppUser != null){
      this.addWhereOrAnd();
      queryBuf.append(" req.idAppUser = ");
      queryBuf.append(idAppUser);
    } 
    //  Search by request number 
    if (requestNumber != null && !requestNumber.equals("")){
      this.addWhereOrAnd();
      queryBuf.append(" req.number = '");
      queryBuf.append(requestNumber);
      queryBuf.append("'");
    }
  }
  
  private void addWorkItemCriteria() {
    //  Search by next step 
    if (codeStepNext != null && !codeStepNext.equals("")){
      this.addWhereOrAnd();
      queryBuf.append(" wi.codeStepNext = '");
      queryBuf.append(codeStepNext);
      queryBuf.append("'");
    }
  }
  

  
  private void addSecurityCriteria() {
    secAdvisor.addSecurityCriteria(queryBuf, "req", addWhere, true, true);
  }
    
  
  protected boolean addWhereOrAnd() {
    if (addWhere) {
      queryBuf.append(" WHERE ");
      addWhere = false;
    } else {
      queryBuf.append(" AND ");
    }
    return addWhere;
  }

  
  public Integer getIdLab() {
    return idLab;
  }

  
  public Integer getIdUser() {
    return idAppUser;
  }

  

  
  public void setIdLab(Integer idLab) {
    this.idLab = idLab;
  }

  
  public void setIdUser(Integer idAppUser) {
    this.idAppUser = idAppUser;
  }

 
  
  public Integer getIdAppUser() {
    return idAppUser;
  }

  
  public void setIdAppUser(Integer idAppUser) {
    this.idAppUser = idAppUser;
  }

   
  public String getCodeStepNext() {
    return codeStepNext;
  }

  
  public void setCodeStepNext(String codeStepNext) {
    this.codeStepNext = codeStepNext;
  }

  
  public String getRequestNumber() {
    return requestNumber;
  }

  
  public void setRequestNumber(String requestNumber) {
    this.requestNumber = requestNumber;
  }



  
  
}
