package hci.gnomex.model;

import java.util.Iterator;
import java.util.Set;

import hci.hibernate3utils.HibernateDetailObject;


public class QualityControlStepEntry extends HibernateDetailObject {
  public static final String        OTHER_LABEL = "otherLabel";
  public static final String        OTHER_VALIDATION_LABEL = "otherValidLabel";
  
  private Integer idQualityControlStepEntry;
  private String  codeQualityControlStep;
  private Integer idProject;
  private String  value;
  private String  otherLabel;
 

  
  public String getCodeQualityControlStep() {
    return codeQualityControlStep;
  }
  
  public void setCodeQualityControlStep(String codeQualityControlStep) {
    this.codeQualityControlStep = codeQualityControlStep;
  }
  
  public Integer getIdQualityControlStepEntry() {
    return idQualityControlStepEntry;
  }
  
  public void setIdQualityControlStepEntry(Integer idQualityControlStepEntry) {
    this.idQualityControlStepEntry = idQualityControlStepEntry;
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
