package hci.gnomex.model;

import hci.hibernate3utils.HibernateDetailObject;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;


public class Analysis extends HibernateDetailObject {
  
  private Integer   idAnalysis;
  private String    number;
  private String    name;
  private String    description;
  private Integer   idLab;
  private Lab       lab;
  private Integer   idAppUser;
  private AppUser   appUser;
  private Integer   idAnalysisType;
  private Integer   idAnalysisProtocol;
  private Integer   idOrganism;
  private Date      createDate;
  private String    codeVisibility;
  private Integer   idInstitution;
  private Integer   idCoreFacility;
  private Date      privacyExpirationDate;
  private Set       analysisGroups = new TreeSet();  
  private Set       experimentItems = new TreeSet();
  private Set       files = new TreeSet();
  private Set       collaborators = new TreeSet();
  private Set<GenomeBuild>   genomeBuilds = new TreeSet<GenomeBuild>();
  private Set                 propertyEntries;


  // permission field
  private boolean     canUpdateVisibility;
  private boolean     canUploadData;
  
  
  
  public String getCodeVisibility() {
    return codeVisibility;
  }


  
  public void setCodeVisibility(String codeVisibility) {
    this.codeVisibility = codeVisibility;
  }


  public Date getCreateDate() {
    return createDate;
  }

  
  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }
  
  public String getCreateYear() {
    String createDate    = this.formatDate(this.getCreateDate());
    String tokens[] = createDate.split("/");
    String createYear  = tokens[2];  
    return createYear;
  }
  
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


  
  public Lab getLab() {
    return lab;
  }

  
  public void setLab(Lab lab) {
    this.lab = lab;
  }

  
  public Integer getIdAnalysis() {
    return idAnalysis;
  }

  
  public void setIdAnalysis(Integer idAnalysis) {
    this.idAnalysis = idAnalysis;
  }

  
  public String getNumber() {
    return number;
  }

  
  public void setNumber(String number) {
    this.number = number;
  }

  
  public Integer getIdAnalysisType() {
    return idAnalysisType;
  }

  
  public void setIdAnalysisType(Integer idAnalysisType) {
    this.idAnalysisType = idAnalysisType;
  }

  
  public Integer getIdAnalysisProtocol() {
    return idAnalysisProtocol;
  }

  
  public void setIdAnalysisProtocol(Integer idAnalysisProtocol) {
    this.idAnalysisProtocol = idAnalysisProtocol;
  }

  
  public Integer getIdOrganism() {
    return idOrganism;
  }

  
  public void setIdOrganism(Integer idOrganism) {
    this.idOrganism = idOrganism;
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
  
  public String getCanUploadData() {
    if (this.canUploadData) {
      return "Y";
    } else {
      return "N";
    }
  }
  public void canUploadData(boolean canDo) {
    canUploadData = canDo;
  }
  

  public void registerMethodsToExcludeFromXML() {
    this.excludeMethodFromXML("getLab");
    this.excludeMethodFromXML("getAppUser");
    this.excludeMethodFromXML("getPropertyEntries");
  }



  
  public Set getAnalysisGroups() {
    return analysisGroups;
  }



  
  public void setAnalysisGroups(Set analysisGroups) {
    this.analysisGroups = analysisGroups;
  }
  
  public String getAnalysisGroupNames() {
    StringBuffer buf = new StringBuffer();
    for(Iterator i = analysisGroups.iterator(); i.hasNext();) {
      AnalysisGroup ag = (AnalysisGroup)i.next();
      buf.append(ag.getName());
      if (i.hasNext()) {
        buf.append(", ");
      }
    }
    return buf.toString();
  }



  
  public Set getExperimentItems() {
    return experimentItems;
  }



  
  public void setExperimentItems(Set experimentItems) {
    this.experimentItems = experimentItems;
  }



  
  public Set getFiles() {
    return files;
  }



  
  public void setFiles(Set files) {
    this.files = files;
  }



  
  public Integer getIdAppUser() {
    return idAppUser;
  }



  
  public void setIdAppUser(Integer idAppUser) {
    this.idAppUser = idAppUser;
  }



  
  public AppUser getAppUser() {
    return appUser;
  }



  
  public void setAppUser(AppUser appUser) {
    this.appUser = appUser;
  }


  public String getOwnerName() {
    if (appUser != null) {
      return (appUser.getFirstName() != null ? appUser.getFirstName() : "") + " " + 
              (appUser.getLastName() != null ? appUser.getLastName() : "");
    } else {
      return "";
    }
  }
 
  public Set getCollaborators() {
    return collaborators;
  }

  public void setCollaborators(Set collaborators) {
    this.collaborators = collaborators;
  }
   
  public Set<GenomeBuild> getGenomeBuilds() {
    return genomeBuilds;
  }

  public void setGenomeBuilds(Set<GenomeBuild> genomeBuilds) {
    this.genomeBuilds = genomeBuilds;
  }
  
  public String getKey() {
    String createDate    = this.formatDate(this.getCreateDate());
    String tokens[] = createDate.split("/");
    String createMonth = tokens[0];
    String createDay   = tokens[1];
    String createYear  = tokens[2];
    String sortDate = createYear + createMonth + createDay;
    String key = createYear + "-" + sortDate + "-" + this.getNumber();     
    return key;
  }

  public String getKey(String resultsDir) {
    return Analysis.getKey(this.getNumber(), this.getCreateDate(), resultsDir);
  }
  
  public static String getKey(String analysisNumber, java.sql.Date theCreateDate, String resultsDir) {
    if (theCreateDate == null) {
      return "";
    } else {
      String createDate    = new SimpleDateFormat("MM/dd/yyyy").format(theCreateDate);
      String tokens[] = createDate.split("/");
      String createMonth = tokens[0];
      String createDay   = tokens[1];
      String createYear  = tokens[2];
      String sortDate = createYear + createMonth + createDay;
      String key = createYear + "-" + sortDate + "-" + analysisNumber + "-" + resultsDir;     
      return key;
    }
  }
  
  public Integer getIdInstitution() {
    return idInstitution;
  }

  public void setIdInstitution(Integer idInstitution) {
    this.idInstitution = idInstitution;
  }
  
  public Integer getIdCoreFacility()
  {
    return idCoreFacility;
  }



  public void setIdCoreFacility(Integer idCoreFacility)
  {
    this.idCoreFacility = idCoreFacility;
  }



  public Date getPrivacyExpirationDate() {
    return privacyExpirationDate;
  }

  public void setPrivacyExpirationDate(Date privacyExpirationDate) {
    this.privacyExpirationDate = privacyExpirationDate;
  }
  
  public static String getCreateYear(Date theCreateDate) {
    if (theCreateDate == null) {
      return "";
    } else {
      String createDate  = new SimpleDateFormat("MM/dd/yyyy").format(theCreateDate);
      String tokens[] = createDate.split("/");
      String createYear  = tokens[2];
      return createYear;
    }
  }

  public Set getPropertyEntries() {
    return propertyEntries;
  }
  public void setPropertyEntries(Set propertyEntries) {
    this.propertyEntries = propertyEntries;
  }

}
