package hci.gnomex.model;

import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.hibernate3utils.HibernateDetailObject;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.jdom.Document;
import org.jdom.Element;

public class Analysis extends HibernateDetailObject {

  private Integer idAnalysis;
  private String number;
  private String name;
  private String description;
  private Integer idLab;
  private Lab lab;
  private Integer idAppUser;
  private AppUser appUser;
  private Integer idAnalysisType;
  private Integer idAnalysisProtocol;
  private Integer idOrganism;
  private Date createDate;
  private String codeVisibility;
  private Integer idInstitution;
  private Integer idCoreFacility;
  private Date privacyExpirationDate;
  private Integer idSubmitter;
  private AppUser submitter;
  private Set analysisGroups = new TreeSet();
  private Set experimentItems = new TreeSet();
  private Set files = new TreeSet();
  private Set collaborators = new TreeSet();
  private Set<GenomeBuild> genomeBuilds = new TreeSet<GenomeBuild>();
  private Set propertyEntries;
  private Set topics;

  // permission field
  private boolean canUpdateVisibility;
  private boolean canUploadData;

  public Set getTopics() {
    return topics;
  }

  public void setTopics(Set topics) {
    this.topics = topics;
  }

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
    String createDate = this.formatDate(this.getCreateDate());
    String tokens[] = createDate.split("/");
    String createYear = tokens[2];
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
    this.excludeMethodFromXML("getSubmitter");
    this.excludeMethodFromXML("getPropertyEntries");
    this.excludeMethodFromXML("getFiles");
  }

  public Set getAnalysisGroups() {
    return analysisGroups;
  }

  public void setAnalysisGroups(Set analysisGroups) {
    this.analysisGroups = analysisGroups;
  }

  public String getAnalysisGroupNames() {
    StringBuffer buf = new StringBuffer();
    for (Iterator i = analysisGroups.iterator(); i.hasNext();) {
      AnalysisGroup ag = (AnalysisGroup) i.next();
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
  
  public Integer getIdSubmitter() {
    return idSubmitter;
  }

  public void setIdSubmitter(Integer idSubmitter) {
    this.idSubmitter = idSubmitter;
  }

  public AppUser getSubmitter() {
    return submitter;
  }

  public void setSubmitter(AppUser submitter) {
    this.submitter = submitter;
  }  

  public String getOwnerName() {
    if (appUser != null) {
      return (appUser.getFirstName() != null ? appUser.getFirstName() : "")
          + " " + (appUser.getLastName() != null ? appUser.getLastName() : "");
    } else {
      return "";
    }
  }
  
  public String getSubmitterName() {
    if (submitter != null) {
      return (submitter.getFirstName() != null ? submitter.getFirstName() : "")
          + " " + (submitter.getLastName() != null ? submitter.getLastName() : "");
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
    String createDate = this.formatDate(this.getCreateDate());
    String tokens[] = createDate.split("/");
    String createMonth = tokens[0];
    String createDay = tokens[1];
    String createYear = tokens[2];
    String sortDate = createYear + createMonth + createDay;
    String key = createYear + "-" + sortDate + "-" + this.getNumber();
    return key;
  }

  public String getKey(String resultsDir) {
    return Analysis.getKey(this.getNumber(), this.getCreateDate(), resultsDir);
  }

  public static String getKey(String analysisNumber,
      java.sql.Date theCreateDate, String resultsDir) {
    if (theCreateDate == null) {
      return "";
    } else {
      String createDate = new SimpleDateFormat("MM/dd/yyyy")
          .format(theCreateDate);
      String tokens[] = createDate.split("/");
      String createMonth = tokens[0];
      String createDay = tokens[1];
      String createYear = tokens[2];
      String sortDate = createYear + createMonth + createDay;
      String key = createYear + "-" + sortDate + "-" + analysisNumber + "-"
          + resultsDir;
      return key;
    }
  }

  public Integer getIdInstitution() {
    return idInstitution;
  }

  public void setIdInstitution(Integer idInstitution) {
    this.idInstitution = idInstitution;
  }

  public Integer getIdCoreFacility() {
    return idCoreFacility;
  }

  public void setIdCoreFacility(Integer idCoreFacility) {
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
      String createDate = new SimpleDateFormat("MM/dd/yyyy")
          .format(theCreateDate);
      String tokens[] = createDate.split("/");
      String createYear = tokens[2];
      return createYear;
    }
  }

  public Set getPropertyEntries() {
    return propertyEntries;
  }

  public void setPropertyEntries(Set propertyEntries) {
    this.propertyEntries = propertyEntries;
  }

  @SuppressWarnings("unchecked")
  public Document getXML(SecurityAdvisor secAdvisor, DictionaryHelper dh)
      throws Exception {
    Document doc = new Document(new Element("Analysis"));
    Element root = doc.getRootElement();

    String labName = "";
    if (this.getLab() != null) {
      labName = Lab.formatLabName(this.getLab().getLastName(), this.getLab()
          .getFirstName());
    }

    root.setAttribute("idAnalysis", this.getNonNullString(this.getIdAnalysis()));
    root.setAttribute("number", this.getNonNullString(this.getNumber()));
    root.setAttribute("name", this.getNonNullString(this.getName()));
    root.setAttribute("label", this.getNonNullString(this.getName()));
    root.setAttribute("description",
        this.getNonNullString(this.getDescription()));
    root.setAttribute("createDateDisplay", this.getCreateDate() == null ? ""
        : this.formatDate(this.getCreateDate(), this.DATE_OUTPUT_SQL));
    root.setAttribute(
        "createDate",
        this.getCreateDate() == null ? "" : this.formatDate(this.createDate,
            this.DATE_OUTPUT_SLASH));
    root.setAttribute("idLab", this.getNonNullString(this.getIdLab()));
    root.setAttribute("labName", labName);
    root.setAttribute("idAnalysisType",
        this.getNonNullString(this.getIdAnalysisType()));
    root.setAttribute("idAnalysisProtocol",
        this.getNonNullString(this.getIdAnalysisProtocol()));
    root.setAttribute("idOrganism", this.getNonNullString(this.getIdOrganism()));
    root.setAttribute("codeVisibility",
        this.getNonNullString(this.getCodeVisibility()));
    root.setAttribute("idAppUser", this.getNonNullString(this.getIdAppUser()));
    root.setAttribute("ownerName", this.getOwnerName());
    root.setAttribute("isDirty", "N");
    root.setAttribute("isSelected", "N");

    if (root.getAttributeValue("codeVisibility").equals(
        Visibility.VISIBLE_TO_PUBLIC)) {
      root.setAttribute("requestPublicNote", "(Public) ");
    } else {
      root.setAttribute("requestPublicNote", "");
    }

    String createDate = this.formatDate(this.getCreateDate());
    String analysisNumber = this.getNonNullString(this.getNumber());
    String tokens[] = createDate.split("/");
    String createMonth = tokens[0];
    String createDay = tokens[1];
    String createYear = tokens[2];
    String sortDate = createYear + createMonth + createDay;
    String key = createYear + "-" + sortDate + "-" + analysisNumber;
    root.setAttribute("key", key);

    Integer idLab = this.getIdLab();
    Integer idAppUser = this.getIdAppUser();
    root.setAttribute("canUpdateVisibility",
        secAdvisor.canUpdateVisibility(idLab, idAppUser) ? "Y" : "N");
    root.setAttribute("idSubmitter", this.getNonNullString(this.getIdSubmitter()));

    return doc;
  }

}
