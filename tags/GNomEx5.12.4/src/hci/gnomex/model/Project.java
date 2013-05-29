package hci.gnomex.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import hci.framework.utilities.XMLReflectException;
import hci.hibernate3utils.HibernateDetailObject;
import java.sql.Date;

import org.jdom.Document;
import org.jdom.Element;


public class Project extends HibernateDetailObject {
  
  private Integer   idProject;
  private String    name;
  private String    description;
  private Integer   idLab;
  private Lab       lab;
  private Integer   idAppUser;
  private AppUser   appUser;
  private Set       requests = new TreeSet();
  private Set       experimentDesignEntries = new TreeSet();
  private Set       experimentFactorEntries = new TreeSet();
  private Set       qualityControlStepEntries = new TreeSet();
  
  
  public String getDescription() {
    return description;
  }
  
  public void setDescription(String description) {
    this.description = description;
  }
  
  public Integer getIdProject() {
    return idProject;
  }
  
  public void setIdProject(Integer idProject) {
    this.idProject = idProject;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }

  
  public Integer getIdAppUser() {
    return idAppUser;
  }

  
  public void setIdAppUser(Integer idAppUser) {
    this.idAppUser = idAppUser;
  }

  
  public Integer getIdLab() {
    return idLab;
  }

  
  public void setIdLab(Integer idLab) {
    this.idLab = idLab;
  }

  
  public Set getRequests() {
    return requests;
  }

  
  public void setRequests(Set requests) {
    this.requests = requests;
  }

  
  public Set getExperimentFactorEntries() {
    return experimentFactorEntries;
  }

  
  public void setExperimentFactorEntries(Set experimentFactorEntries) {
    this.experimentFactorEntries = experimentFactorEntries;
  }

  
  public Set getExperimentDesignEntries() {
    return experimentDesignEntries;
  }

  
  public void setExperimentDesignEntries(Set experimentDesignEntries) {
    this.experimentDesignEntries = experimentDesignEntries;
  }

  
  public Set getQualityControlStepEntries() {
    return qualityControlStepEntries;
  }

  
  public void setQualityControlStepEntries(Set qualityControlStepEntries) {
    this.qualityControlStepEntries = qualityControlStepEntries;
  }


  public void registerMethodsToExcludeFromXML() {
    this.excludeMethodFromXML("getExperimentFactorEntries");
    this.excludeMethodFromXML("getExperimentDesignEntries");
    this.excludeMethodFromXML("getQualityControlStepEntries");
    this.excludeMethodFromXML("getAppUser");
    this.excludeMethodFromXML("getLab");
  }
  
  public Document toXMLDocument(List useBaseClass) throws XMLReflectException {
    return toXMLDocument(useBaseClass, DATE_OUTPUT_SQL);
  }

  public Document toXMLDocument(List list, int dateOutputStyle ) throws XMLReflectException {

    Document doc = super.toXMLDocument(list, dateOutputStyle);
   
    Element factorNode = new Element("ExperimentFactor");
    doc.getRootElement().addContent(factorNode);
    // Experimental factors
    for (Iterator i = getExperimentFactorEntries().iterator(); i.hasNext();) {
      ExperimentFactorEntry entry = (ExperimentFactorEntry) i.next();
      factorNode.setAttribute(entry.getCodeExperimentFactor(), entry.getValue());
      if (entry.getCodeExperimentFactor().equals(ExperimentFactor.OTHER)) {
        factorNode.setAttribute(ExperimentFactorEntry.OTHER_LABEL, entry.getOtherLabel() == null ? "" : entry.getOtherLabel());
      }
    }
    
    Element designNode = new Element("ExperimentDesign");
    doc.getRootElement().addContent(designNode);
    // Experimental designs
    for (Iterator i = getExperimentDesignEntries().iterator(); i.hasNext();) {
      ExperimentDesignEntry entry = (ExperimentDesignEntry) i.next();
      designNode.setAttribute(entry.getCodeExperimentDesign(), entry.getValue());
      if (entry.getCodeExperimentDesign().equals(ExperimentDesign.OTHER)) {
        designNode.setAttribute(ExperimentDesignEntry.OTHER_LABEL, entry.getOtherLabel() == null ? "" : entry.getOtherLabel());
      }
    }
    
    Element qualityNode = new Element("ExperimentQuality");
    doc.getRootElement().addContent(qualityNode);
    // Experimental factors
    for (Iterator i = getQualityControlStepEntries().iterator(); i.hasNext();) {
      QualityControlStepEntry entry = (QualityControlStepEntry) i.next();
      qualityNode.setAttribute(entry.getCodeQualityControlStep(), entry.getValue());
      if (entry.getCodeQualityControlStep().equals(QualityControlStep.OTHER)) {
        qualityNode.setAttribute(QualityControlStepEntry.OTHER_LABEL, entry.getOtherLabel() == null ? "" : entry.getOtherLabel());
      }
      if (entry.getCodeQualityControlStep().equals(QualityControlStep.OTHER_VALIDATION)) {
        qualityNode.setAttribute(QualityControlStepEntry.OTHER_VALIDATION_LABEL, entry.getOtherLabel() == null ? "" : entry.getOtherLabel());
      }
    }

    return doc;
  }

  
  public Lab getLab() {
    return lab;
  }

  
  public void setLab(Lab lab) {
    this.lab = lab;
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

 
  
  public String getLabName() {
    if (lab != null) {
      return lab.getName();      
    } else {
      return "";
    }
  }


  
  public AppUser getAppUser() {
    return appUser;
  }

  
  public void setAppUser(AppUser appUser) {
    this.appUser = appUser;
  }
  
  public String getOwnerName() {
    if (appUser != null) {
      return appUser.getFirstName() + " " + appUser.getLastName();
    } else {
      return "";
    }
  }
  
  public boolean hasPublicRequest() {
    boolean hasPublicRequest = false;
    // Otherwise, project can be read if any of its requests are visible to public
    for (Iterator i2 = this.getRequests().iterator(); i2.hasNext();) {
      Request r = (Request)i2.next();
      if (r.getCodeVisibility().equals(Visibility.VISIBLE_TO_PUBLIC)) {
        hasPublicRequest = true;
        break;
      }
    }  
    return hasPublicRequest;
  }
}
