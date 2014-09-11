package hci.gnomex.utility;

import java.io.Serializable;

public class ExperimentMatrixLinkInfo extends MatrixLinkInfoBase implements Serializable {

  @Override
  public String getNumberArg() {
    return "requestNumber";
  }

  @Override
  public String getTitle() {
    return "Experiment";
  }

  @Override
  public Boolean isExperiment() {
    return true;
  }

  @Override
  public Boolean isAnalysis() {
    return false;
  }
  
  
}

