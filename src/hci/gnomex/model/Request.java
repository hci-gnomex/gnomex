package hci.gnomex.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.framework.model.DetailObject;
import hci.framework.model.FieldFormatter;
import hci.framework.security.UnknownPermissionException;
import hci.framework.utilities.XMLReflectException;
import hci.hibernate3utils.HibernateDetailObject;
import java.sql.Date;

import org.jdom.Document;
import org.jdom.Element;


public class Request extends HibernateDetailObject {
  
  private Integer         idRequest;
  private String          number;
  private java.util.Date  createDate;
  private String          protocolNumber;
  private String          codeProtocolType;
  private Integer         idCoreFacility;
  private Integer         idLab;
  private Lab             lab;
  private Integer         idAppUser;
  private AppUser         appUser;
  private Project         project;
  private Integer         idProject;
  private Integer         idBillingAccount;
  private BillingAccount  billingAccount;
  private String          codeRequestCategory;
  private RequestCategory requestCategory;
  private String          codeApplication;
  private Integer         idSlideProduct;
  private SlideProduct    slideProduct;
  private Integer         idSampleTypeDefault;
  private Integer         idOrganismSampleDefault;
  private String          captureLibDesignId;
  private String          codeBioanalyzerChipType;
  private String          notes;
  private Date            completedDate;
  private Date            lastModifyDate;
  private String          isArrayINFORequest;
  private String          codeVisibility;
  private String          isExternal;  
  private Integer         idInstitution;
  private String          name;
  private String          description;
  private String          corePrepInstructions;
  private String          analysisInstructions;
  private Date            privacyExpirationDate;
  private Integer         avgInsertSizeFrom;
  private Integer         avgInsertSizeTo;
  private Set             samples = new TreeSet();
  private Set             labeledSamples = new TreeSet();
  private Set             hybridizations = new TreeSet();
  private Set             workItems = new TreeSet();
  private Set             sequenceLanes = new TreeSet();
  private Set             analysisExperimentItems = new TreeSet();
  private Set             billingItems = new TreeSet();  
  private Set             seqLibTreatments = new TreeSet();
  private Set             collaborators = new TreeSet();
  private Set             files = new TreeSet();
  private Integer         idSampleDropOffLocation;
  private String          codeRequestStatus;
  private RequestStatus   requestStatus;
  private Set             chromatograms;
  private Set             topics;    
  
  
  // permission field
  private boolean     canUpdateVisibility;
  private boolean     canUploadData;
  private boolean     canDeleteSample;
 
  public Set getTopics() {
    return topics;
  }
  public void setTopics(Set topics) {
    this.topics = topics;
  }  
  
  public String getCodeApplication() {
    return codeApplication;
  }
  
  public void setCodeApplication(String codeApplication) {
    this.codeApplication = codeApplication;
  }
   
  public String getCodeProtocolType() {
    return codeProtocolType;
  }
  
  public String getDescription() {
    return description;
  }
  
  public String getCorePrepInstructions() {
    return corePrepInstructions;
  }
  
  public String getAnalysisInstructions() {
    return analysisInstructions;
  }
  
  public void setCodeProtocolType(String codeProtocolType) {
    this.codeProtocolType = codeProtocolType;
  }
  
  public String getCodeRequestCategory() {
    return codeRequestCategory;
  }
  
  public void setCodeRequestCategory(String codeRequestCategory) {
    this.codeRequestCategory = codeRequestCategory;
  }
  
  public java.util.Date getCreateDate() {
    return createDate;
  }
  
  public void setCreateDate(java.util.Date createDate) {
    this.createDate = createDate;
  }
  
  public Integer getIdCoreFacility()
  {
    return idCoreFacility;
  }

  public void setIdCoreFacility(Integer idCoreFacility)
  {
    this.idCoreFacility = idCoreFacility;
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
  
  public String getCaptureLibDesignId() {
    return captureLibDesignId;
  }

  public void setCaptureLibDesignId(String captureLibDesignId) {
    this.captureLibDesignId = captureLibDesignId;
  }
  
  public void setAvgInsertSizeFrom(Integer size){
    this.avgInsertSizeFrom = size;
  }
  
  public void setAvgInsertSizeTo(Integer size){
    this.avgInsertSizeTo = size;
  }
  
  public Integer getAvgInsertSizeFrom(){
    return this.avgInsertSizeFrom;
  }
  
  public Integer getAvgInsertSizeTo(){
    return this.avgInsertSizeTo;
  }
  
  public void setDescription(String description){
    this.description = description;
  }
  public void setCorePrepInstructions(String corePrepInstructions){
    this.corePrepInstructions = corePrepInstructions;
  }
  public void setAnalysisInstructions(String analysisInstructions){
    this.analysisInstructions = analysisInstructions;
  }
  public void setIdLab(Integer idLab) {
    this.idLab = idLab;
  }
  
  public Integer getIdProject() {
    return idProject;
  }
  
  public void setIdProject(Integer idProject) {
    this.idProject = idProject;
  }
  
  public Integer getIdRequest() {
    return idRequest;
  }
  
  public void setIdRequest(Integer idRequest) {
    this.idRequest = idRequest;
  }
  
  public Integer getIdSlideProduct() {
    return idSlideProduct;
  }
  
  public void setIdSlideProduct(Integer idSlideProduct) {
    this.idSlideProduct = idSlideProduct;
  }
  
  public String getNumber() {
    return number;
  }
  
  public void setNumber(String number) {
    this.number = number;
  }
  
  public String getProtocolNumber() {
    return protocolNumber;
  }
  
  public void setProtocolNumber(String protocolNumber) {
    this.protocolNumber = protocolNumber;
  }

  
  public Integer getIdBillingAccount() {
    return idBillingAccount;
  }

  
  public void setIdBillingAccount(Integer idBillingAccount) {
    this.idBillingAccount = idBillingAccount;
  }

  
  public Set getSamples() {
    return samples;
  }

  public int getNumberOfSamples() {
    return samples.size();
  }
  
  public void setSamples(Set samples) {
    this.samples = samples;
  }

  
  public Integer getIdSampleTypeDefault() {
    return idSampleTypeDefault;
  }

  
  public void setIdSampleTypeDefault(Integer idSampleTypeDefault) {
    this.idSampleTypeDefault = idSampleTypeDefault;
  }

  
  public Set getHybridizations() {
    return hybridizations;
  }

  
  public void setHybridizations(Set hybridizations) {
    this.hybridizations = hybridizations;
  }

  
  public String getNotes() {
    return notes;
  }

  
  public void setNotes(String notes) {
    this.notes = notes;
  }
  
  public Document toXMLDocument(List useBaseClass) throws XMLReflectException {
    return toXMLDocument(useBaseClass, DATE_OUTPUT_SQL);
  }

  public Document toXMLDocument(List list, int dateOutputStyle ) throws XMLReflectException {

    Document doc = super.toXMLDocument(list, dateOutputStyle);
    
    // Sample properties & treatments
    String otherLabel = null;
    Map idProperties = new HashMap();
    boolean hasTreatment = false;
    for(Iterator i = getSamples().iterator(); i.hasNext();) {
      Sample sample = (Sample)i.next();
      for (Iterator i1 = sample.getPropertyEntries().iterator(); i1.hasNext();) {
        PropertyEntry entry = (PropertyEntry) i1.next();
        idProperties.put(entry.getIdProperty(), "Y");
        if (entry.getOtherLabel() != null && !entry.getOtherLabel().equals("")) {
          otherLabel = entry.getOtherLabel();
        }
      }
      if (sample.getTreatmentEntries().size() > 0) {
        hasTreatment = true;
      }
    }
    for(Iterator i = idProperties.keySet().iterator(); i.hasNext();) {
      Integer idProperty = (Integer)i.next();
      doc.getRootElement().setAttribute("ANNOT" + idProperty, "Y");
    }
    
    // Treatments
    if (hasTreatment) {
      doc.getRootElement().setAttribute(TreatmentEntry.TREATMENT, "Y");
    }
    
    // Other label
    if (otherLabel != null) {
      doc.getRootElement().setAttribute(PropertyEntry.OTHER_LABEL, otherLabel);
    }
    
    if ( samples != null ) {
      doc.getRootElement().setAttribute( "numberOfSamples", String.valueOf(getNumberOfSamples()) );
    }

    return doc;
  }

  
  public String getCodeBioanalyzerChipType() {
    return codeBioanalyzerChipType;
  }

  
  public void setCodeBioanalyzerChipType(String codeBioanalyzerChipType) {
    this.codeBioanalyzerChipType = codeBioanalyzerChipType;
  }

  
  public Set getWorkItems() {
    return workItems;
  }

  
  public void setWorkItems(Set workItems) {
    this.workItems = workItems;
  }

  
  public Set getLabeledSamples() {
    return labeledSamples;
  }

  
  public void setLabeledSamples(Set labeledSamples) {
    this.labeledSamples = labeledSamples;
  }

  
  public Date getCompletedDate() {
    return completedDate;
  }

  
  public void setCompletedDate(Date completedDate) {
    this.completedDate = completedDate;
  }

  
  public SlideProduct getSlideProduct() {
    return slideProduct;
  }

  
  public void setSlideProduct(SlideProduct slideProduct) {
    this.slideProduct = slideProduct;
  }

  
  public Integer getIdOrganismSampleDefault() {
    return idOrganismSampleDefault;
  }

  
  public void setIdOrganismSampleDefault(Integer idOrganismSampleDefault) {
    this.idOrganismSampleDefault = idOrganismSampleDefault;
  }

  
  public Lab getLab() {
    return lab;
  }

  
  public void setLab(Lab lab) {
    this.lab = lab;
  }

  
  public String getIsArrayINFORequest() {
    return isArrayINFORequest;
  }

  
  public void setIsArrayINFORequest(String isArrayINFORequest) {
    this.isArrayINFORequest = isArrayINFORequest;
  }

  
  public Project getProject() {
    return project;
  }

  
  public void setProject(Project project) {
    this.project = project;
  }

  
  public String getProjectName() {
    if (project != null) {
      return project.getName();
    } else {
      return "";
    }
    
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
  
  public void setCanDeleteSample(Boolean canDo) {
    this.canDeleteSample = canDo;
  }
  
  public String getCanDeleteSample() {
    if (canDeleteSample) {
      return "Y";
    } else {
      return "N";
    }
  }
  
  
  public AppUser getAppUser() {
    return appUser;
  }

  
  public void setAppUser(AppUser appUser) {
    this.appUser = appUser;
  }

  public boolean isConsideredFinished() {
    boolean isFinished = false;
    
    if (this.getCodeRequestCategory().equals(RequestCategory.QUALITY_CONTROL_REQUEST_CATEGORY)) {
      isFinished = isFinishedWithQC();
    } else if (RequestCategory.isIlluminaRequestCategory(this.getCodeRequestCategory())) {
      int doneLaneCount = 0;
      for(Iterator i1 = this.getSequenceLanes().iterator(); i1.hasNext();) {
        SequenceLane l = (SequenceLane)i1.next();
        if (l.getFlowCellChannel() != null) {
          if (l.getFlowCellChannel().getFirstCycleFailed() != null && l.getFlowCellChannel().getFirstCycleFailed().equals("Y")) {
            doneLaneCount++;
          } else if (l.getFlowCellChannel().getLastCycleFailed() != null && l.getFlowCellChannel().getLastCycleFailed().equals("Y")) {
            doneLaneCount++;
          } else if (l.getFlowCellChannel().getPipelineFailed() != null && l.getFlowCellChannel().getPipelineFailed().equals("Y")) {
            doneLaneCount++;
          } else if (l.getFlowCellChannel().getPipelineDate() != null) {
            doneLaneCount++;
          } 
        }
      }
      
      if (doneLaneCount == this.getSequenceLanes().size()) {
        isFinished = true;
      }            
    } else {
      int doneHybCount = 0;
      for(Iterator i1 = this.getHybridizations().iterator(); i1.hasNext();) {
        Hybridization h = (Hybridization)i1.next();
        if (h.getExtractionDate() != null) {
          doneHybCount++;
        } else if (h.getExtractionFailed() != null && h.getExtractionFailed().equals("Y")) {
          doneHybCount++;
        } else if (h.getExtractionBypassed() != null && h.getExtractionBypassed().equals("Y")) {
          doneHybCount++;
        } else if (h.getHybFailed() != null && h.getHybFailed().equals("Y")) {
          doneHybCount++;
        } else if (h.getHybBypassed() != null && h.getHybBypassed().equals("Y")) {
          doneHybCount++;
        } else if (h.getLabeledSampleChannel1() != null && h.getLabeledSampleChannel1().getLabelingFailed() != null && h.getLabeledSampleChannel1().getLabelingFailed().equals("Y")) {
          doneHybCount++;
        } else if (h.getLabeledSampleChannel2() != null && h.getLabeledSampleChannel2().getLabelingFailed() != null && h.getLabeledSampleChannel2().getLabelingFailed().equals("Y")) {
          doneHybCount++;
        } else if (h.getLabeledSampleChannel1() != null && h.getLabeledSampleChannel1().getLabelingBypassed() != null && h.getLabeledSampleChannel1().getLabelingBypassed().equals("Y")) {
          doneHybCount++;
        } else if (h.getLabeledSampleChannel2() != null && h.getLabeledSampleChannel2().getLabelingBypassed() != null && h.getLabeledSampleChannel2().getLabelingBypassed().equals("Y")) {
          doneHybCount++;
        } else if (h.getLabeledSampleChannel1() != null && h.getLabeledSampleChannel1().getSample() != null && h.getLabeledSampleChannel1().getSample().getQualFailed() != null && h.getLabeledSampleChannel1().getSample().getQualFailed().equals("Y")) {
          doneHybCount++;
        } else if (h.getLabeledSampleChannel2() != null && h.getLabeledSampleChannel2().getSample() != null && h.getLabeledSampleChannel2().getSample().getQualFailed() != null && h.getLabeledSampleChannel2().getSample().getQualFailed().equals("Y")) {
          doneHybCount++;
        } else if (h.getLabeledSampleChannel1() != null && h.getLabeledSampleChannel1().getSample() != null && h.getLabeledSampleChannel1().getSample().getQualBypassed() != null && h.getLabeledSampleChannel1().getSample().getQualBypassed().equals("Y")) {
          doneHybCount++;
        } else if (h.getLabeledSampleChannel2() != null && h.getLabeledSampleChannel2().getSample() != null && h.getLabeledSampleChannel2().getSample().getQualBypassed() != null && h.getLabeledSampleChannel2().getSample().getQualBypassed().equals("Y")) {
          doneHybCount++;
        } 
      }
      
      if (doneHybCount == this.getHybridizations().size()) {
        isFinished = true;
      }      
    }
        
    return isFinished;
  }
  
  public boolean isFinishedWithQC() {
    boolean isFinished = false;
    
    int completedSampleCount = 0;
    for (Iterator i1 = this.getSamples().iterator(); i1.hasNext();) {
      Sample s = (Sample) i1.next();
      if (s.getQualDate() != null || 
          (s.getQualFailed() != null && s.getQualFailed().equalsIgnoreCase( "Y")) ||
          (s.getQualBypassed() != null && s.getQualBypassed().equalsIgnoreCase( "Y"))) {
        completedSampleCount++;
      }
    }
    if (completedSampleCount == this.getSamples().size()) {
      isFinished = true;
    }

    return isFinished;
    
  }
  
  /**
   * Have all samples passed (or bypassed QC)?
   */
  public boolean isCompleteWithQC() {
    boolean isCompleteWithQC = false;
    
    int completedSampleCount = 0;
    for (Iterator i1 = this.getSamples().iterator(); i1.hasNext();) {
      Sample s = (Sample) i1.next();
      // We don't consider it complete if any sample failed QC
      if (s.getQualFailed() != null && s.getQualFailed().equals("Y")) {
        continue;
      }
      if (s.getQualDate() != null ||
          (s.getQualBypassed() != null && s.getQualBypassed().equalsIgnoreCase( "Y"))) {
        completedSampleCount++;
      }
    }
    if (completedSampleCount == this.getSamples().size()) {
      isCompleteWithQC = true;
    }

    return isCompleteWithQC;
    
  }


  
  public String getCodeVisibility() {
    return codeVisibility;
  }

  
  public void setCodeVisibility(String codeVisibility) {
    this.codeVisibility = codeVisibility;
  }
  
  
  public String getIsVisibleToMembers() {
    if (this.codeVisibility != null && this.codeVisibility.equals(Visibility.VISIBLE_TO_GROUP_MEMBERS)) {
      return "Y";
    } else {
      return "N";
    }
  }

  public String getIsVisibleToMembersAndCollaborators() {
    if (this.codeVisibility != null && this.codeVisibility.equals(Visibility.VISIBLE_TO_GROUP_MEMBERS_AND_COLLABORATORS)) {
      return "Y";
    } else {
      return "N";
    }
  }

  public String getIsVisibleToPublic() {
    if (this.codeVisibility != null && this.codeVisibility.equals(Visibility.VISIBLE_TO_PUBLIC)) {
      return "Y";
    } else {
      return "N";
    }
  }

  public void registerMethodsToExcludeFromXML() {
    this.excludeMethodFromXML("getAppUser");
    this.excludeMethodFromXML("getLab");
    this.excludeMethodFromXML("getBillingAccount");
    this.excludeMethodFromXML("getKey");
    this.excludeMethodFromXML("getCreateYear");
    this.excludeMethodFromXML("getRequestCategory");
    this.excludeMethodFromXML("getChromatograms");
  }
  
  public String getOwnerName() {
    if (appUser != null) {
      return appUser.getFirstName() + " " + appUser.getLastName();
    } else {
      return "";
    }
  }
  public String getLabName() {
    if (lab != null) {
      return lab.getName();
    } else {
      return "";
    }
  }

  
  public Set getSequenceLanes() {
    return sequenceLanes;
  }

  
  public void setSequenceLanes(Set sequenceLanes) {
    this.sequenceLanes = sequenceLanes;
  }

  
  public Set getAnalysisExperimentItems() {
    return analysisExperimentItems;
  }

  
  public void setAnalysisExperimentItems(Set analysisExperimentItems) {
    this.analysisExperimentItems = analysisExperimentItems;
  }

  
  public Set getBillingItems() {
    return billingItems;
  }

  
  public void setBillingItems(Set billingItems) {
    this.billingItems = billingItems;
  }

    
  public BillingAccount getBillingAccount() {
    return billingAccount;
  }

  
  public void setBillingAccount(BillingAccount billingAccount) {
    this.billingAccount = billingAccount;
  }
  
  public String getBillingAccountName() {
    if (this.billingAccount != null) {
      return this.billingAccount.getAccountName();
    } else {
      return "";
    }
  }

  public String getBillingAccountNumber() {
    if (this.billingAccount != null) {
      return this.billingAccount.getAccountNumber();
    } else {
      return "";
    }
  }

  
  public Set getSeqLibTreatments() {
    return seqLibTreatments;
  }


  
  public void setSeqLibTreatments(Set seqLibTreatments) {
    this.seqLibTreatments = seqLibTreatments;
  }


  
  public Date getLastModifyDate() {
    return lastModifyDate;
  }


  
  public void setLastModifyDate(Date lastModifyDate) {
    this.lastModifyDate = lastModifyDate;
  }

 
  public static String getBaseRequestNumber(String requestNumber) {
    // Get rid of extraneous #
    requestNumber = requestNumber.replaceAll("#", "");
    
    // Strip off revision number after "R"
    requestNumber = requestNumber.toUpperCase();
    String[] tokens = requestNumber.split("R");
    if (tokens.length > 1) {
      return tokens[0] + "R";
    } else {
      return requestNumber;
    }
  }


  
  public Set getCollaborators() {
    return collaborators;
  }


  
  public void setCollaborators(Set collaborators) {
    this.collaborators = collaborators;
  }
  
  public String getKey(String resultsDir) {
    return Request.getKey(this.getNumber(), this.getCreateDate(), resultsDir);
  }
  
  public static String getKey(String requestNumber, java.util.Date theCreateDate, String resultsDir) {
    if (theCreateDate == null) {
      return "";
    } else {
      String createDate    = new SimpleDateFormat("MM/dd/yyyy").format(theCreateDate);
      String tokens[] = createDate.split("/");
      String createMonth = tokens[0];
      String createDay   = tokens[1];
      String createYear  = tokens[2];
      String sortDate = createYear + createMonth + createDay;
      String key = createYear + "-" + sortDate + "-" + requestNumber + "-" + resultsDir;     
      return key;
    }
  }

  
  public String getCreateYear() {
    return Request.getCreateYear(this.getCreateDate());
  }
  
  public static String getCreateYear(java.util.Date theCreateDate) {
    if (theCreateDate == null) {
      return "";
    } else {
      String createDate  = new SimpleDateFormat("MM/dd/yyyy").format(theCreateDate);
      String tokens[] = createDate.split("/");
      String createYear  = tokens[2];
      return createYear;
    }
  }


  
  public String getIsExternal() {
    return isExternal;
  }


  
  public void setIsExternal(String isExternal) {
    this.isExternal = isExternal;
  }
  

  public RequestCategory getRequestCategory() {
    return requestCategory;
  }


  public void setRequestCategory(RequestCategory requestCategory) {
    this.requestCategory = requestCategory;
  }


  public Integer getIdInstitution() {
    return idInstitution;
  }


  public void setIdInstitution(Integer idInstitution) {
    this.idInstitution = idInstitution;
  }


  public String getName() {
    return name;
  }


  public void setName(String name) {
    this.name = name;
  }


  public Set getFiles() {
    return files;
  }


  public void setFiles(Set files) {
    this.files = files;
  }

  public Date getPrivacyExpirationDate() {
    return privacyExpirationDate;
  }

  public void setPrivacyExpirationDate(Date privacyExpirationDate) {
    this.privacyExpirationDate = privacyExpirationDate;
  }

  public void setCodeRequestStatus(String requestStatus)
  {
    this.codeRequestStatus = requestStatus;
  }

  public String getCodeRequestStatus()
  {
    return codeRequestStatus;
  }

  
  public RequestStatus getRequestStatus() {
    return requestStatus;
  }

  public Integer getIdSampleDropOffLocation() {
    return idSampleDropOffLocation;
  }
  
  public void setIdSampleDropOffLocation(Integer id) {
    idSampleDropOffLocation = id;
  }
  
  public void setRequestStatus( RequestStatus requestStatus ) {
    this.requestStatus = requestStatus;
  }
  
  public String getSubmitterEmail() {
    if (getAppUser() != null) {
      return getAppUser().getEmail();
    } else {
      return null;
    }
  }
  
  public String getSubmitterPhone() {
    if (getAppUser() != null) {
      return getAppUser().getPhone();
    } else {
      return null;
    }
  }
  
  
  public String getSubmitterInstitution() {
    if (getAppUser() != null) {
      return getAppUser().getInstitute();
    } else {
      return null;
    }
  }

  public Set getChromatograms() {
    return chromatograms;
  }
  public void setChromatograms(Set chromatograms) {
    this.chromatograms = chromatograms;
  }
  @SuppressWarnings("unchecked")
  public Document getXML(SecurityAdvisor secAdvisor, DictionaryHelper dh) throws Exception {
    Document doc = new Document(new Element("Request"));
    Element root = doc.getRootElement();
    
    String labName = "";
    if(this.getLab() != null) {
      labName = Lab.formatLabName(this.getLab().getLastName(), this.getLab().getFirstName());
    }
    
    String projectName = "";
    String projectLabName = "";
    String ownerLastName = "";
    String ownerFirstName = "";
    if(this.getProject() != null) {
      projectName = this.getNonNullString(this.getProject().getName());
      if(this.getProject().getLab() != null) {
        projectLabName = Lab.formatLabName(this.getProject().getLab().getLastName(), this.getProject().getLab().getFirstName());
      }
      if(this.getProject().getAppUser() != null) {
        ownerLastName = this.getNonNullString(this.getProject().getAppUser().getLastName());
        ownerFirstName = this.getNonNullString(this.getProject().getAppUser().getFirstName());
      }
    }


    
    String codeRequestCategory =  this.getNonNullString(this.getCodeRequestCategory());
    RequestCategory requestCategory = dh.getRequestCategoryObject(codeRequestCategory);
    
    root.setAttribute("idRequest",              this.getNonNullString(this.getIdRequest()));
    root.setAttribute("requestNumber",          this.getNonNullString(this.getNumber()));
    root.setAttribute("requestCreateDate",      this.createDate == null ? ""  : this.formatDate(this.createDate, this.DATE_OUTPUT_ALTIO));
    root.setAttribute("requestCreateDateDisplay", this.getCreateDate() == null ? ""  : this.formatDate(this.getCreateDate(), this.DATE_OUTPUT_SQL));
    root.setAttribute("requestCreateDateDisplayMedium", this.getCreateDate() == null ? ""  : DateFormat.getDateInstance(DateFormat.MEDIUM).format(this.getCreateDate()));
    root.setAttribute("createDate",             this.getCreateDate() == null ? ""  : this.formatDate(this.getCreateDate(), this.DATE_OUTPUT_SLASH));
    root.setAttribute("idSlideProduct",         this.getIdSlideProduct() == null ? ""  : this.getIdSlideProduct().toString());
    root.setAttribute("idLab",                  this.getNonNullString(this.getIdLab()));
    root.setAttribute("idAppUser",              this.getNonNullString(this.getIdAppUser()));
    root.setAttribute("codeRequestCategory",    codeRequestCategory);
    root.setAttribute("icon",                   requestCategory != null && requestCategory.getIcon() != null ? requestCategory.getIcon() : "");
    root.setAttribute("type",                   requestCategory != null && requestCategory.getType() != null ? requestCategory.getType() : "");
    root.setAttribute("codeApplication",        this.getNonNullString(this.getCodeApplication()));
    root.setAttribute("labName",                labName);
    root.setAttribute("slideProductName",       this.getSlideProduct() == null ? "":this.getNonNullString(this.getSlideProduct().getName()));
    root.setAttribute("projectLabName",         projectLabName);
    root.setAttribute("projectName",            projectName);
    root.setAttribute("codeVisibility",         this.getNonNullString(this.getCodeVisibility()));
    root.setAttribute("ownerFirstName",         ownerFirstName);
    root.setAttribute("ownerLastName",          ownerLastName);
    root.setAttribute("isExternal",             this.getNonNullString(this.getIsExternal()));
    root.setAttribute("name",                   this.getNonNullString(this.getName()));
    root.setAttribute("isDirty",                "N");
    root.setAttribute("isSelected",             "N");
    root.setAttribute("analysisNames",          "");
    
    if (root.getAttributeValue("codeVisibility").equals(Visibility.VISIBLE_TO_PUBLIC)) {
      root.setAttribute("requestPublicNote",          "(Public) ");
    } else {
      root.setAttribute("requestPublicNote", "");
    }
    
    Integer idLab = this.getIdLab();
    Integer idAppUser = this.getIdAppUser();
    root.setAttribute("canUpdateVisibility", secAdvisor.canUpdateVisibility(idLab, idAppUser) ? "Y" : "N");
   
    if (RequestCategory.isMicroarrayRequestCategory(root.getAttributeValue("codeRequestCategory"))) {
      StringBuffer displayName = new StringBuffer();
      displayName.append(root.getAttributeValue("requestNumber"));
      if (root.getAttributeValue("name") != null && !root.getAttributeValue("name").equals("")) {
        displayName.append(" - ");
        displayName.append(root.getAttributeValue("name"));                
      }      
      displayName.append(" - ");
      displayName.append(root.getAttributeValue("slideProductName"));      
      displayName.append(" - ");
      displayName.append(root.getAttributeValue("ownerFirstName"));
      displayName.append(" ");
      displayName.append(root.getAttributeValue("ownerLastName"));
      displayName.append(" ");
      displayName.append(root.getAttributeValue("requestCreateDateDisplayMedium"));      
      
      root.setAttribute("displayName", displayName.toString());
      root.setAttribute("label",       displayName.toString());
      
    } else {
      StringBuffer displayName = new StringBuffer();
      displayName.append(root.getAttributeValue("requestNumber"));
      if (root.getAttributeValue("name") != null && !root.getAttributeValue("name").equals("")) {
        displayName.append(" - ");
        displayName.append(root.getAttributeValue("name"));                
      }
      if (root.getAttributeValue("codeApplication") != null && !root.getAttributeValue("codeApplication").equals("")) {
        displayName.append(" - ");
        displayName.append(dh.getApplication(root.getAttributeValue("codeApplication")));                
      }
      displayName.append(" - ");
      displayName.append(root.getAttributeValue("ownerFirstName"));
      displayName.append(" ");
      displayName.append(root.getAttributeValue("ownerLastName"));
      displayName.append(" ");
      displayName.append(root.getAttributeValue("requestCreateDateDisplayMedium"));      

      root.setAttribute("displayName", displayName.toString());
      root.setAttribute("label",       displayName.toString());
      
    }
    return doc;
  }  
  
  public String isDNASeqExperiment() {
    return RequestCategory.isDNASeqCoreRequestCategory(this.getCodeRequestCategory()) == true ? "Y" : "N";
  }


}
