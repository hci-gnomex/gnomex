package hci.gnomex.utility;

import java.io.Serializable;

public class ExperimentMatrixLinkInfo extends MatrixLinkInfoBase implements Serializable {
  public String requestCategory;
  public String application;

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
  
  public String getRequestCategory() {
    return requestCategory;
  }
  
  public String getApplication() {
    return application;
  }
}

