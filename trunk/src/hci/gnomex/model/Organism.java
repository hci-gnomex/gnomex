package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import hci.framework.security.UnknownPermissionException;
import hci.gnomex.security.SecurityAdvisor;

import java.io.Serializable;
import java.util.Set;

import org.jdom.Document;
import org.jdom.Element;



public class Organism extends DictionaryEntry implements Serializable, OntologyEntry, DictionaryEntryUserOwned {
  private Integer idOrganism;
  private String  organism;
  private String  abbreviation;
  private String  mageOntologyCode;
  private String  mageOntologyDefinition;
  private String  isActive;
  private Integer idAppUser;
  private String  das2Name;
  private Integer sortOrder;
  private String  binomialName;
  private String  ncbiTaxID;  
  private Set     genomeBuilds;


  public String getDisplay() {
    String display = this.getNonNullString(getOrganism());
    return display;
  }

  public String getValue() {
    return getIdOrganism().toString();
  }

  
  public Integer getIdOrganism() {
    return idOrganism;
  }

  
  public void setIdOrganism(Integer idOrganism) {
    this.idOrganism = idOrganism;
  }

  
  public String getOrganism() {
    return organism;
  }

  
  public void setOrganism(String organism) {
    this.organism = organism;
  }

  
  public String getMageOntologyCode() {
    return mageOntologyCode;
  }

  
  public void setMageOntologyCode(String mageOntologyCode) {
    this.mageOntologyCode = mageOntologyCode;
  }

  
  public String getMageOntologyDefinition() {
    return mageOntologyDefinition;
  }

  
  public void setMageOntologyDefinition(String mageOntologyDefinition) {
    this.mageOntologyDefinition = mageOntologyDefinition;
  }

  
  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }

  
  public String getAbbreviation() {
    return abbreviation;
  }

  
  public void setAbbreviation(String abbreviation) {
    this.abbreviation = abbreviation;
  }

  
  public Integer getIdAppUser() {
    return idAppUser;
  }

  
  public void setIdAppUser(Integer idAppUser) {
    this.idAppUser = idAppUser;
  }

  public String getDas2Name() {
    return das2Name;
  }

  public Integer getSortOrder() {
    return sortOrder;
  }

  public String getBinomialName() {
    return binomialName;
  }

  public void setDas2Name(String das2Name) {
    this.das2Name = das2Name;
  }

  public void setSortOrder(Integer sortOrder) {
    this.sortOrder = sortOrder;
  }

  public void setBinomialName(String binomialName) {
    this.binomialName = binomialName;
  }

  private Set getGenomeBuilds() {
    return genomeBuilds;
  }

  private void setGenomeBuilds(Set genomeBuilds) {
    this.genomeBuilds = genomeBuilds;
  }

  public Document getXML(SecurityAdvisor secAdvisor) throws UnknownPermissionException {
    Document doc = new Document(new Element("Organism"));
    Element root = doc.getRootElement();
    
    root.setAttribute("label",        this.getBinomialName() != null ? this.getBinomialName() : "");
    root.setAttribute("idOrganism",   this.getIdOrganism().toString());
    root.setAttribute("name",         this.getDas2Name() != null ? this.getDas2Name() : "");        
    root.setAttribute("commonName",   this.getOrganism() != null ? this.getOrganism() : "");        
    root.setAttribute("binomialName", this.getBinomialName() != null ? this.getBinomialName() : "");        
    root.setAttribute("NCBITaxID",    this.getNcbiTaxID() != null ? this.getNcbiTaxID() : "");    
    root.setAttribute("canWrite",     secAdvisor.canUpdate(this) ? "Y" : "N");

    return doc;
  }

  
  public void registerMethodsToExcludeFromXML() {
    this.excludeMethodFromXML("getGenomeBuilds");
    this.excludeMethodFromXML("getExcludedMethodsMap");
  }

  public String getNcbiTaxID() {
    return ncbiTaxID;
  }

  public void setNcbiTaxID(String ncbiTaxID) {
    this.ncbiTaxID = ncbiTaxID;
  }


}