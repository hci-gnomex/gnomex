package hci.gnomex.utility;

import java.io.Serializable;

public class AnalysisMatrixLinkInfo extends MatrixLinkInfoBase implements Serializable {
  
  public String analysisProtocol;
  public String analysisType;
  public String genomeBuilds;
  public String groups;

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

  public String getAnalysisProtocol() {
    return analysisProtocol;
  }
  
  public String getAnalysisType() {
    return analysisType;
  }
  
  public String getGenomeBuilds() {
    return genomeBuilds;
  }
  
  public String getGroups() {
    return groups;
  }
}
