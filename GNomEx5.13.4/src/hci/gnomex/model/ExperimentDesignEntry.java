package hci.gnomex.model;

import java.util.Iterator;
import java.util.Set;

import hci.hibernate3utils.HibernateDetailObject;


public class ExperimentDesignEntry extends HibernateDetailObject {
  public static final String        OTHER_LABEL = "otherLabel";
 
  private Integer idExperimentDesignEntry;
  private String  codeExperimentDesign;
  private Integer idProject;
  private String  value;
  private String  otherLabel;

  
  public String getCodeExperimentDesign() {
    return codeExperimentDesign;
  }
  
  public void setCodeExperimentDesign(String codeExperimentDesign) {
    this.codeExperimentDesign = codeExperimentDesign;
  }
  
  public Integer getIdExperimentDesignEntry() {
    return idExperimentDesignEntry;
  }
  
  public void setIdExperimentDesignEntry(Integer idExperimentDesignEntry) {
    this.idExperimentDesignEntry = idExperimentDesignEntry;
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
  
  public String getIsSelected() {
    if (value != null && value.equals("Y")) {
      return "true";
    } else {
      return "false";
    }
  }
    
}
