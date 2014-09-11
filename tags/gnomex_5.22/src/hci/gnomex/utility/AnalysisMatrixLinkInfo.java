package hci.gnomex.utility;

import java.io.Serializable;

public class AnalysisMatrixLinkInfo extends MatrixLinkInfoBase implements Serializable {

  @Override
  public String getNumberArg() {
    return "analysisNumber";
  }

  @Override
  public String getTitle() {
    return "Analysis";
  }

  @Override
  public Boolean isExperiment() {
    return false;
  }

  @Override
  public Boolean isAnalysis() {
    return true;
  }

}
