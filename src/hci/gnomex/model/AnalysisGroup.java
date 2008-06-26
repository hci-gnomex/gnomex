package hci.gnomex.model;

import hci.hibernate3utils.HibernateDetailObject;

import java.util.Set;
import java.util.TreeSet;


public class AnalysisGroup extends HibernateDetailObject {
  
  private Integer   idAnalysisGroup;
  private String    name;
  private String    description;
  private Integer   idLab;
  private Lab       lab;
  private String    codeVisibility;
  private Set       analysisItems = new TreeSet();
  
  // permission field
  private boolean     canUpdateVisibility;
    
  public String getDescription() {
    return description;
  }
  
  public void setDescription(String description) {
    this.description = description;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }

  
  public Integer getIdLab() {
    return idLab;
  }

  
  public void setIdLab(Integer idLab) {
    this.idLab = idLab;
  }

 
  
  public String getLabName() {
    if (lab != null) {
      return lab.getName();      
    } else {
      return "";
    }
  }

  
  public Integer getIdAnalysisGroup() {
    return idAnalysisGroup;
  }

  
  public void setIdAnalysisGroup(Integer idAnalysisGroup) {
    this.idAnalysisGroup = idAnalysisGroup;
  }

  
  public Lab getLab() {
    return lab;
  }

  
  public void setLab(Lab lab) {
    this.lab = lab;
  }

  
  public Set getAnalysisItems() {
    return analysisItems;
  }

  
  public void setAnalysisItems(Set analysisItems) {
    this.analysisItems = analysisItems;
  }

  public void registerMethodsToExcludeFromXML() {
    this.excludeMethodFromXML("getLab");
  }

  
  public String getCodeVisibility() {
    return codeVisibility;
  }

  
  public void setCodeVisibility(String codeVisibility) {
    this.codeVisibility = codeVisibility;
  }
  
  public String getCanRead() {
    if (this.canRead()) {
      return "Y";
    } else {
      return "N";
    }
  }

  public String getCanUpdate() {
    if (this.canUpdate()) {
      return "Y";
    } else {
      return "N";
    }
  }

  public String getCanDelete() {
    if (this.canDelete()) {
      return "Y";
    } else {
      return "N";
    }
  }
  public String getCanUpdateVisibility() {
    if (this.canUpdateVisibility) {
      return "Y";
    } else {
      return "N";
    }
  }
  public void canUpdateVisibility(boolean canDo) {
    canUpdateVisibility = canDo;
  }
    
 
}
