package hci.gnomex.model;

import java.util.Iterator;
import java.util.Set;

import hci.hibernate3utils.HibernateDetailObject;


public class ExperimentFactorEntry extends HibernateDetailObject {
  public static final String        OTHER_LABEL = "otherLabel";
  
  private Integer idExperimentFactorEntry;
  private String  codeExperimentFactor;
  private Integer idProject;
  private String  value;
  private String  otherLabel;
  
  public String getCodeExperimentFactor() {
    return codeExperimentFactor;
  }
  
  public void setCodeExperimentFactor(String codeExperimentFactor) {
    this.codeExperimentFactor = codeExperimentFactor;
  }
  
  public Integer getIdExperimentFactorEntry() {
    return idExperimentFactorEntry;
  }
  
  public void setIdExperimentFactorEntry(Integer idExperimentFactorEntry) {
    this.idExperimentFactorEntry = idExperimentFactorEntry;
  }
  
  public Integer getIdProject() {
    return idProject;
  }
  
  public void setIdProject(Integer idProject) {
    this.idProject = idProject;
  }

  
  public String getValue() {
    return value;
  }

  
  public void setValue(String value) {
    this.value = value;
  }

  
  public String getOtherLabel() {
    return otherLabel;
  }

  
  public void setOtherLabel(String otherLabel) {
    this.otherLabel = otherLabel;
  }
    
}
