package hci.gnomex.model;


import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;

public class RequestProgressDNASeqFilter extends RequestProgressFilter {
  
  public static final Integer REQ_ID_REQUEST = 0;
  public static final Integer REQ_CREATE_DATE = 1;
  public static final Integer REQ_NUMBER = 2;
  public static final Integer REQ_ID_APP_USER = 3;
  public static final Integer SAMPLE_NUMBER = 4;
  public static final Integer SAMPLE_NAME = 5;
  public static final Integer REQ_ID_LAB = 6;
  public static final Integer REQ_CODE_REQUEST_CATEGORY = 7;
  public static final Integer REQ_CODE_REQUEST_STATUS = 8;
  public static final Integer REQOWNER_FIRST_NAME = 9;
  public static final Integer REQOWNER_LAST_NAME = 10;
  public static final Integer PLATE_CODE_PLATE_TYPE = 11;
  public static final Integer PLATE_LABEL = 12;
  public static final Integer WELL_REDO_FLAG = 13;
  public static final Integer WELL_IS_CONTROL = 14;
  public static final Integer WELL_ID_PLATEWELL = 15;
  public static final Integer WELL_ROW = 16;
  public static final Integer WELL_COL = 17;
  public static final Integer WELL_CREATE_DATE = 18;
  public static final Integer PRIMER_ID_PRIMER = 19;
  public static final Integer PRIMER_NAME = 20;
  public static final Integer ASSAY_ID_ASSAY = 21;
  public static final Integer ASSAY_NAME = 22;
  public static final Integer RUN_CODE_INSTRUMENT_RUN_STATUS = 23;
  
  public StringBuffer getDNASeqQuery(SecurityAdvisor secAdvisor, DictionaryHelper dictionaryHelper) {
    this.secAdvisor = secAdvisor;
    this.dictionaryHelper = dictionaryHelper;
    queryBuf = new StringBuffer();
    addWhere = true;
    
    queryBuf.append(" SELECT DISTINCT req.idRequest, req.createDate, req.number,  "); 
    queryBuf.append("        req.idAppUser, ");
    queryBuf.append("        s.number, s.name, ");
    queryBuf.append("        req.idLab, req.codeRequestCategory, req.codeRequestStatus, ");
    queryBuf.append("        reqOwner.firstName, reqOwner.lastName, ");
    queryBuf.append("        plate.codePlateType, plate.label, ");
    queryBuf.append("        well.redoFlag, well.isControl, well.idPlateWell, well.row, well.col, well.createDate, ");
    queryBuf.append("        primer.idPrimer, primer.name as primerName, assay.idAssay, assay.name as assayName, run.codeInstrumentRunStatus");
    getDNASeqQueryBody(queryBuf);
    
    return queryBuf;
    
  }

  
  private void getDNASeqQueryBody(StringBuffer queryBuf) {
     
    queryBuf.append(" FROM           PlateWell as well ");
    queryBuf.append(" LEFT JOIN      well.sample as s ");
    queryBuf.append(" LEFT JOIN      s.request as req ");
    queryBuf.append(" LEFT JOIN      req.appUser as reqOwner ");
    queryBuf.append(" LEFT JOIN      req.collaborators as collab ");
    queryBuf.append(" LEFT JOIN      well.plate as plate ");
    queryBuf.append(" LEFT JOIN      plate.instrumentRun as run ");
    queryBuf.append(" LEFT JOIN      well.primer as primer ");
    queryBuf.append(" LEFT JOIN      well.assay as assay ");
    

    
    addRequestCriteria();
    addSecurityCriteria();

    
    this.addWhereOrAnd();
    queryBuf.append(" req.codeRequestCategory IN (");
    queryBuf.append("'");
    queryBuf.append(RequestCategory.CAPILLARY_SEQUENCING_REQUEST_CATEGORY);
    queryBuf.append("', '");
    queryBuf.append(RequestCategory.CHERRY_PICKING_REQUEST_CATEGORY);
    queryBuf.append("', '");
    queryBuf.append(RequestCategory.FRAGMENT_ANALYSIS_REQUEST_CATEGORY);
    queryBuf.append("', '");
    queryBuf.append(RequestCategory.MITOCHONDRIAL_DLOOP_SEQ_REQUEST_CATEGORY);
    queryBuf.append("') ");

    queryBuf.append(" order by req.createDate desc, req.number desc, s.number asc, primer.name, assay.name");
  
  }
  
  
  public static final Integer CHROMO_ID_REQUEST = 0;
  public static final Integer CHROMO_WELL_CREATE_DATE = 1;
  public static final Integer CHROMO_ID_PLATE_WELL = 2;
  public static final Integer CHROMO_RELEASE_DATE = 3;
  public static final Integer CHROMO_ID_CHROMATOGRAM = 4;

  
  public StringBuffer getDNASeqChromoQuery(SecurityAdvisor secAdvisor, DictionaryHelper dictionaryHelper) {
    this.secAdvisor = secAdvisor;
    this.dictionaryHelper = dictionaryHelper;
    queryBuf = new StringBuffer();
    addWhere = true;
    
    queryBuf.append(" SELECT DISTINCT req.idRequest, well.createDate, chromo.idPlateWell, chromo.releaseDate, chromo.idChromatogram ");
    getDNASeqChromoQueryBody(queryBuf);
    
    return queryBuf;
    
  }

  
  private void getDNASeqChromoQueryBody(StringBuffer queryBuf) {
     
    queryBuf.append(" FROM           PlateWell as well ");
    queryBuf.append(" JOIN           well.sample as s ");
    queryBuf.append(" JOIN           s.request as req ");
    queryBuf.append(" LEFT JOIN      req.appUser as reqOwner ");
    queryBuf.append(" LEFT JOIN      req.collaborators as collab ");
    queryBuf.append(" JOIN           req.chromatograms chromo ");
    

    
    addRequestCriteria();
    addSecurityCriteria();

    
    this.addWhereOrAnd();
    queryBuf.append(" req.codeRequestCategory IN (");
    queryBuf.append("'");
    queryBuf.append(RequestCategory.CAPILLARY_SEQUENCING_REQUEST_CATEGORY);
    queryBuf.append("', '");
    queryBuf.append(RequestCategory.CHERRY_PICKING_REQUEST_CATEGORY);
    queryBuf.append("', '");
    queryBuf.append(RequestCategory.FRAGMENT_ANALYSIS_REQUEST_CATEGORY);
    queryBuf.append("', '");
    queryBuf.append(RequestCategory.MITOCHONDRIAL_DLOOP_SEQ_REQUEST_CATEGORY);
    queryBuf.append("') ");
    this.addWhereOrAnd();
    queryBuf.append(" chromo.idPlateWell = well.idPlateWell ");
  }
}
