package hci.gnomex.model;


import hci.framework.model.DetailObject;
import hci.gnomex.security.SecurityAdvisor;

import java.util.Iterator;
import java.util.Set;

public class WorkItemFilter extends DetailObject {
  
  public static int            SAMPLE_LEVEL            = 0;
  public static int            LABELED_SAMPLE_LEVEL    = 1;
  public static int            HYB_LEVEL               = 2;
  public static int            LANE_LEVEL              = 3;
  public static int            FLOW_CELL_LEVEL         = 4;
  
  //////////////////////////////////////////////////////////////////////////////////////////////
  // column placement
  // base columns
  public static int            ID_REQUEST              = 0; 
  public static int            REQ_NUMBER              = 1; 
  public static int            REQ_CREATE_DATE         = 2; 
  public static int            CODE_REQUEST_CATEGORY   = 3; 
  public static int            ID_APP_USER             = 4; 
  public static int            ID_LAB                  = 5; 
  public static int            ID_WORK_ITEM            = 6; 
  public static int            CODE_STEP_NEXT          = 7; 
  public static int            WI_STATUS               = 8; 
  //hyb
  public static int            UNUSED1                 = 9; 
  public static int            UNUSED2                 = 10; 
  public static int            ID_HYBRIDIZATION        = 11; 
  public static int            HYB_NUMBER              = 12;
  //Lane
  public static int            ID_SAMPLE               = 9; 
  public static int            SAMPLE_NUMBER           = 10; 
  public static int            ID_SEQUENCE_LANE        = 11; 
  public static int            SEQ_NUMBER              = 12;
  // Sample and flow cell level handles above -- below more base
  public static int            WI_CREATE_DATE          = 13; 
  public static int            USER_LAST_NAME          = 14; 
  public static int            USER_FIRST_NAME         = 15; 
  
  // QC specific
  public static int            SAMPLE_QUAL_DATE        = 16;
  public static int            SAMPLE_QUAL_FAILED      = 17; 
  public static int            QUAL_260_280_RATIO      = 18; 
  public static int            QUAL_260_230_RATIO      = 19; 
  public static int            QUAL_CALC_CONC          = 20; 
  public static int            QUAL_28_18_RIBO_RATIO   = 21; 
  public static int            QUAL_RIN_NUMBER         = 22; 
  public static int            QUAL_BYPASSED           = 23; 
  public static int            CODE_BIO_CHIP_TYPE      = 24; 
  public static int            QUAL_FRAG_SIZE_FROM     = 25; 
  public static int            QUAL_FRAG_SIZE_TO       = 26; 
  public static int            ID_SAMPLE_TYPE          = 27; 
  public static int            REQUEST_CATEGORY_TYPE   = 28; 
  public static int            CODE_REQUEST_STATUS     = 29; 
  public static int            REQ_ID_CORE_FACILITY    = 30;
  
  // Labeling specific
  public static int            LABELING_DATE           = 16;
  public static int            LABELING_FAILED         = 17;
  public static int            LABELING_YIELD          = 18;
  public static int            ID_LABEL                = 19;
  public static int            LABELING_PROTOCOL       = 20;
  public static int            ID_LABELED_SAMPLE        = 21;
  public static int            CODE_LABELING_REACTION_SIZE = 22;
  public static int            NUMBER_OF_REACTIONS     = 23;
  public static int            LABELING_BYPASSED       = 24;
  
  // Hybridization specific
  public static int            HYB_DATE                = 16;
  public static int            HYB_FAILED              = 17;
  public static int            ID_HYB_PROTOCOL         = 18;
  public static int            ID_SLIDE                = 19;
  public static int            SLIDE_BARCODE           = 20;
  public static int            ID_SLIDE_DESIGN         = 21;
  public static int            ID_ARRAY_COORDINATE     = 22;
  public static int            ARRAY_COORDINATE_NAME   = 23;
  public static int            HYB_BYPASSED            = 24;
  public static int            SLIDE_DESIGN_NAME       = 25;
  public static int            ARRAYS_PER_SLIDE        = 26;

  // scan extraction specific
  public static int            EXTRACTION_DATE         = 16;
  public static int            ID_SCAN_PROTOCOL        = 17;
  public static int            ID_FEATURE_EXTRACTION_PROTOCOL = 18;
  public static int            EXTRACTION_FAILED       = 19;
  public static int            EXTRACTION_BYPASSED     = 20;
  
  // lib prep step specific
  public static int            ID_SEQ_LIB_PROTOCOL     = 16;
  public static int            SEQ_PREP_BY_CORE        = 17;
  public static int            SEQ_PREP_LIB_CONC       = 18;
  public static int            SEQ_QUAL_BIO_CHIP_TYPE  = 19;
  public static int            SEQ_PREP_GEL_FRAG_SIZE_FROM = 20;
  public static int            SEQ_PREP_GEL_FRAG_SIZE_TO = 21;
  public static int            SEQ_PREP_DATE           = 22;
  public static int            SEQ_PREP_FAILED         = 23;
  public static int            SEQ_PREP_BYPASSED       = 24;
  public static int            PREP_ID_SAMPLE_TYPE     = 25;
  public static int            PREP_CODE_APPLICATION   = 26;
  public static int            PREP_ID_OLIGO_BARCODE_A = 27;
  public static int            PREP_BARCODE_SEQUENCE_A = 28;
  public static int            MULTIPLEX_GROUP_NUM     = 29;
  public static int            MEAN_LIB_SIZE_ACTUAL    = 30;
  public static int            PREP_ID_OLIGO_BARCODE_B = 31;
  public static int            PREP_BARCODE_SEQUENCE_B = 32;
  
  // Flowcell stock specific
  public static int            SEQ_PREP_STOCK_LIB_VOL  = 16;
  public static int            SEQ_PREP_STOCK_EB_VOL   = 17;
  public static int            SEQ_PREP_STOCK_DATE     = 18;
  public static int            SEQ_PREP_STOCK_FAILED   = 19;
  public static int            SEQ_PREP_STOCK_BYPASSED = 20;

  // Cluster gen specific
  public static int            CLSTR_ID_SEQUENCE_LANE  = 16;
  public static int            CLSTR_ID_SEQ_RUN_TYPE   = 17;
  public static int            CLSTR_ID_ORGANISM       = 18;
  public static int            CLSTR_ID_NUM_SEQ_CYCLES = 19;
  public static int            CLSTR_ID_OLIGO_BARCODE_A= 20;
  public static int            CLSTR_BARCODE_SEQUENCE_A= 21;
  public static int            CLSTR_MULTIPLEX_GROUP_NUM = 22;
  public static int            CLSTR_LAB_LAST_NAME     = 23;
  public static int            CLSTR_LAB_FIRST_NAME    = 24;
  public static int            CLSTR_SEQ_LANE_OBJECT   = 25;
  public static int            CLSTR_ID_OLIGO_BARCODE_B= 26;
  public static int            CLSTR_BARCODE_SEQUENCE_B= 27;
  public static int            CLSTR_ID_NUM_SEQ_CYCLES_ALLOWED = 28;
  
  // Seq Run/Pipeline specific
  public static int            FLOWCELL_OBJECT         = 16;
  public static int            FLOWCELL_CHANNEL_OBJECT = 17;

  //////////////////////////////////////////////////////////////////////////////////////////////
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
       this.codeStepNext.equals(Step.HISEQ_QC) ||
       this.codeStepNext.equals(Step.MISEQ_QC) ||
       this.codeStepNext.equals(Step.SEQ_PREP) ||
       this.codeStepNext.equals(Step.HISEQ_PREP) ||
       this.codeStepNext.equals(Step.MISEQ_PREP) ||
       this.codeStepNext.equals(Step.SEQ_FLOWCELL_STOCK)) {
      return getQuery(this.SAMPLE_LEVEL);
    } else if (this.codeStepNext.equals(Step.LABELING_STEP)) {
      return getQuery(this.LABELED_SAMPLE_LEVEL);
    } else if (this.codeStepNext.equals(Step.HYB_STEP) ||
                this.codeStepNext.equals(Step.SCAN_EXTRACTION_STEP)) {
      return getQuery(this.HYB_LEVEL);
    } else if (this.codeStepNext.equals(Step.SEQ_CLUSTER_GEN) ||
                this.codeStepNext.equals(Step.HISEQ_CLUSTER_GEN) ||
                this.codeStepNext.equals(Step.MISEQ_CLUSTER_GEN)) {
      return getQuery(this.LANE_LEVEL);
    } else if (this.codeStepNext.equals(Step.SEQ_RUN) ||
                this.codeStepNext.equals(Step.HISEQ_RUN) ||
                this.codeStepNext.equals(Step.SEQ_DATA_PIPELINE) ||
                this.codeStepNext.equals(Step.HISEQ_DATA_PIPELINE) ||
                this.codeStepNext.equals(Step.MISEQ_DATA_PIPELINE)) {
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
    if (!secAdvisor.hasPermission(SecurityAdvisor.CAN_ADMINISTER_ALL_CORE_FACILITIES)) {
      queryBuf.append(" AND ");
      secAdvisor.appendCoreFacilityCriteria(queryBuf, "wi");
      queryBuf.append(" ");
    }

    
    // We have control break login for Seq Assemble so that seq lanes are grouped by
    // multiplex group number.  So we need this sort so that the control break logic
    // evaluates all sequence lanes for a muliplex group number sequentially.  
    // For each new multiplex lane group, we want the seq lanes to be ordered by the 
    // the order they were submitted.  Ideally, we would sort by the sequence lane number
    // so that they would be order by sample number, then the order for the number
    // of times sequenced for this sample.
    if (this.codeStepNext.equals(Step.SEQ_CLUSTER_GEN) ||
        this.codeStepNext.equals(Step.HISEQ_CLUSTER_GEN) ||
        this.codeStepNext.equals(Step.MISEQ_CLUSTER_GEN)) {
      queryBuf.append(" order by req.idRequest, s.multiplexGroupNumber, l.idSequenceLane ");
    }
    
    
    return queryBuf;
    
  }
  
  /**
   * A Sample can be split/replicated into several copies which we call "Sequence Lanes"
   * These Sequence Lanes are grouped and placed into Channels in a Flow Cell for sequencing.
   * This method will return a query which finds Sequence Lanes which already belong to a
   * Channel in a Flow Cell given a set of Samples.
   * 
   * @param idSampleList
   * @return StringBuffer queryBuf
   */
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
    queryBuf.append(" wi.createDate, ");
    queryBuf.append(" appUser.lastName, ");
    queryBuf.append(" appUser.firstName ");
    
  }
  
  
  private void addSpecificColumns(StringBuffer queryBuf) {

    if (this.codeStepNext.equals(Step.QUALITY_CONTROL_STEP) ||
        this.codeStepNext.equals(Step.SEQ_QC) ||
        this.codeStepNext.equals(Step.HISEQ_QC) ||
        this.codeStepNext.equals(Step.MISEQ_QC)) {
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
      queryBuf.append("    s.qualFragmentSizeTo, ");
      queryBuf.append("    s.idSampleType, ");
      queryBuf.append("    rc.type, ");
      queryBuf.append("    req.codeRequestStatus, ");
      queryBuf.append("    req.idCoreFacility ");
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
      queryBuf.append("    h.hybBypassed, ");
      queryBuf.append("    slideDesign.name, ");
      queryBuf.append("    slideProduct.arraysPerSlide ");
    }else if (this.codeStepNext.equals(Step.SCAN_EXTRACTION_STEP)) {
      queryBuf.append("      , ");
      queryBuf.append("    h.extractionDate, ");
      queryBuf.append("    h.idScanProtocol, ");
      queryBuf.append("    h.idFeatureExtractionProtocol, ");
      queryBuf.append("    h.extractionFailed, ");
      queryBuf.append("    h.extractionBypassed ");
    } else if (this.codeStepNext.equals(Step.SEQ_PREP) ||
                this.codeStepNext.equals(Step.HISEQ_PREP) ||
                this.codeStepNext.equals(Step.MISEQ_PREP) ) {
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
      queryBuf.append("    req.codeApplication, ");
      queryBuf.append("    s.idOligoBarcode, ");
      queryBuf.append("    s.barcodeSequence, ");
      queryBuf.append("    s.multiplexGroupNumber, ");
      queryBuf.append("    s.meanLibSizeActual, ");
      queryBuf.append("    s.idOligoBarcodeB, ");
      queryBuf.append("    s.barcodeSequenceB ");
    }  else if (this.codeStepNext.equals(Step.SEQ_FLOWCELL_STOCK)) {
      queryBuf.append("      , ");
      queryBuf.append("    s.seqPrepStockLibVol, ");
      queryBuf.append("    s.seqPrepStockEBVol, ");
      queryBuf.append("    s.seqPrepStockDate, ");
      queryBuf.append("    s.seqPrepStockFailed, ");
      queryBuf.append("    s.seqPrepStockBypassed ");
    }  else if (this.codeStepNext.equals(Step.SEQ_CLUSTER_GEN) ||
                 this.codeStepNext.equals(Step.HISEQ_CLUSTER_GEN) ||
                 this.codeStepNext.equals(Step.MISEQ_CLUSTER_GEN)) {
      queryBuf.append("      , ");
      queryBuf.append("    l.idSequenceLane, ");
      queryBuf.append("    l.idSeqRunType, ");
      queryBuf.append("    s.idOrganism, ");
      queryBuf.append("    l.idNumberSequencingCycles, ");
      queryBuf.append("    s.idOligoBarcode, ");
      queryBuf.append("    s.barcodeSequence, ");
      queryBuf.append("    s.multiplexGroupNumber, ");
      queryBuf.append("    lab.lastName, ");
      queryBuf.append("    lab.firstName, ");
      queryBuf.append("    l, "); // ???
      queryBuf.append("    s.idOligoBarcodeB, ");
      queryBuf.append("    s.barcodeSequenceB, ");
      queryBuf.append("	   l.idNumberSequencingCyclesAllowed ");
    } else if (this.codeStepNext.equals(Step.SEQ_RUN) ||
                this.codeStepNext.equals(Step.HISEQ_RUN)) {
      queryBuf.append("      , ");
      queryBuf.append("    fc, ");
      queryBuf.append("    ch ");

      
    } else if (this.codeStepNext.equals(Step.SEQ_DATA_PIPELINE) ||
                this.codeStepNext.equals(Step.HISEQ_DATA_PIPELINE) ||
                this.codeStepNext.equals(Step.MISEQ_DATA_PIPELINE)) {
      queryBuf.append("      , ");
      queryBuf.append("    fc, ");
      queryBuf.append("    ch ");
      
    }
    

    
  }
  
  private void addBaseQueryBody(StringBuffer queryBuf, int level) {
    
    queryBuf.append(" FROM                WorkItem wi ");
      
    if (level == SAMPLE_LEVEL) {
      queryBuf.append(" JOIN         wi.request as req ");
      queryBuf.append(" JOIN         req.appUser as appUser ");
      queryBuf.append(" JOIN         wi.sample s ");     
      queryBuf.append(" JOIN         req.requestCategory as rc ");
      queryBuf.append(" LEFT JOIN    req.collaborators as collab ");
    } else if (level == LABELED_SAMPLE_LEVEL) {
      queryBuf.append(" JOIN         wi.request as req ");
      queryBuf.append(" JOIN         req.appUser as appUser ");
      queryBuf.append(" JOIN         wi.labeledSample ls ");
      queryBuf.append(" JOIN         ls.sample s ");
      queryBuf.append(" LEFT JOIN    req.collaborators as collab ");
    } else if (level == LANE_LEVEL) {
      queryBuf.append(" JOIN         wi.request as req ");
      queryBuf.append(" JOIN         req.appUser as appUser ");
      queryBuf.append(" JOIN         req.lab as lab ");
      queryBuf.append(" JOIN         wi.sequenceLane l ");
      queryBuf.append(" JOIN         l.sample s ");
      queryBuf.append(" LEFT JOIN    req.collaborators as collab ");
    } else if (level == HYB_LEVEL) {
      queryBuf.append(" JOIN         wi.request as req ");
      queryBuf.append(" JOIN         req.appUser as appUser ");
      queryBuf.append(" JOIN         wi.hybridization h ");      
      queryBuf.append(" LEFT JOIN    req.collaborators as collab ");
      queryBuf.append(" LEFT JOIN    h.slide  slide ");
      queryBuf.append(" LEFT JOIN    h.arrayCoordinate ac ");
      queryBuf.append(" LEFT JOIN    h.slideDesign slideDesign ");
      queryBuf.append(" LEFT JOIN    req.slideProduct slideProduct ");
    } else if (level == FLOW_CELL_LEVEL) {
      queryBuf.append(" LEFT JOIN    wi.request as req ");
      queryBuf.append(" LEFT JOIN    req.appUser as appUser ");
      queryBuf.append(" LEFT JOIN    req.collaborators as collab ");
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
      if(this.codeStepNext.equals(Step.QUALITY_CONTROL_STEP) ||
          this.codeStepNext.equals(Step.SEQ_QC) ||
          this.codeStepNext.equals(Step.HISEQ_QC) ||
          this.codeStepNext.equals(Step.MISEQ_QC)) {
        // Sample Quality lists are now combined
        queryBuf.append(" (wi.codeStepNext = '" + Step.QUALITY_CONTROL_STEP + "' OR");
        queryBuf.append(" wi.codeStepNext = '" + Step.SEQ_QC + "' OR");
        queryBuf.append(" wi.codeStepNext = '" + Step.HISEQ_QC + "' OR");
        queryBuf.append(" wi.codeStepNext = '" + Step.MISEQ_QC + "')");        
      } else {
        queryBuf.append(" wi.codeStepNext = '");
        queryBuf.append(codeStepNext);
        queryBuf.append("'");        
      }
    }
  }
  

  
  private void addSecurityCriteria() {
    secAdvisor.buildSecurityCriteria(queryBuf, "req", "collab", addWhere, true, false);
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
