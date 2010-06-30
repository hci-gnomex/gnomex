package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import java.io.Serializable;



public class Step extends DictionaryEntry implements Serializable {
  public static final String   QUALITY_CONTROL_STEP     = "QC";
  public static final String   LABELING_STEP            = "LABEL";
  public static final String   HYB_STEP                 = "HYB";
  public static final String   SCAN_EXTRACTION_STEP     = "EXT";

  public static final String   SEQ_QC                   = "SEQQC";
  public static final String   SEQ_PREP                 = "SEQPREP";
  public static final String   SEQ_FLOWCELL_STOCK       = "SEQSTOCK";
  public static final String   SEQ_CLUSTER_GEN          = "SEQASSEM";
  public static final String   SEQ_RUN                  = "SEQRUN";
  public static final String   SEQ_DATA_PIPELINE        = "SEQPIPE";

  public static final String   HISEQ_QC                  = "HSEQQC";
  public static final String   HISEQ_PREP                = "HSEQPREP";
  public static final String   HISEQ_CLUSTER_GEN         = "HSEQASSEM";
  public static final String   HISEQ_RUN                 = "HSEQRUN";
  public static final String   HISEQ_DATA_PIPELINE       = "HSEQPIPE";

  private String codeStep;
  private String step;
  private String isActive;
  
  public String getDisplay() {
    String display = this.getNonNullString(getStep());
    return display;
  }

  public String getValue() {
    return getCodeStep();
  }

  
  public String getCodeStep() {
    return codeStep;
  }

  
  public void setCodeStep(String codeStep) {
    this.codeStep = codeStep;
  }

  
  public String getStep() {
    return step;
  }

  
  public void setStep(String step) {
    this.step = step;
  }

  
  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }

}