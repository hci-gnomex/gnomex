package hci.gnomex.model;

import hci.framework.security.UnknownPermissionException;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.constants.Constants;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.hibernate3utils.HibernateDetailObject;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.hibernate.Hibernate;
import org.jdom.Document;
import org.jdom.Element;


public class Request extends HibernateDetailObject implements VisibilityInterface {
  
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
  private Application     application;
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
  private Set             plateWells;
  private Set             topics;    
  private Integer         idSubmitter;
  private AppUser         submitter;
  private Integer         idIScanChip;
  private Integer         numberIScanChips;
  private String          applicationNotes;
  private String          coreToExtractDNA;
  private Date            processingDate;   // Date request was set to Processing status
  private String          materialQuoteNumber;
  private Date            quoteReceivedDate;
  private String          uuid;
  private String          codeDNAPrepType;
  private DNAPrepType     dnaPrepType;
  private String          codeRNAPrepType;
  private RNAPrepType     rnaPrepType;
  private String          bioinformaticsAssist;
  private String          hasPrePooledLibraries;
  private Integer         numPrePooledTubes;
  private String          trimAdapter;
  private String          includeBisulfideConversion;
  private String          includeQubitConcentration;
  
  // permission field
  private boolean     canUpdateVisibility;
  private boolean     canUploadData;
  private boolean     canDeleteSample;
  private boolean     canUpdateSamples;
 
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
  
  public Application getApplication() {
    return application;
  }

  public void setApplication(Application app) {
    this.application = app;
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

  
  public String getBioinformaticsAssist() {
    return bioinformaticsAssist;
  }
  
  public void setBioinformaticsAssist(String bioinformaticsAssist) {
    this.bioinformaticsAssist = bioinformaticsAssist;
  }
  
  public Document toXMLDocument(List useBaseClass) throws XMLReflectException {
    return toXMLDocument(useBaseClass, DATE_OUTPUT_SQL);
  }

  public Document toXMLDocument(List list, int dateOutputStyle ) throws XMLReflectException {
    
    Document doc = super.toXMLDocument(list, dateOutputStyle);
    
    // Sample properties
    String otherLabel = null;
    Map idProperties = new HashMap();
    for(Iterator i = getSamples().iterator(); i.hasNext();) {
      Sample sample = (Sample)i.next();
      for (Iterator i1 = sample.getPropertyEntries().iterator(); i1.hasNext();) {
        PropertyEntry entry = (PropertyEntry) i1.next();
        idProperties.put(entry.getIdProperty(), "Y");
        if (entry.getOtherLabel() != null && !entry.getOtherLabel().equals("")) {
          otherLabel = entry.getOtherLabel();
        }
      }
      
    }
    for(Iterator i = idProperties.keySet().iterator(); i.hasNext();) {
      Integer idProperty = (Integer)i.next();
      doc.getRootElement().setAttribute("ANNOT" + idProperty, "Y");
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
  
  public void setCanUpdateSamples(Boolean canDo) {
    this.canUpdateSamples = canDo;
  }
  
  public String getCanUpdateSamples() {
    if (canUpdateSamples) {
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
    this.excludeMethodFromXML("getSubmitter");
    this.excludeMethodFromXML("getLab");
    this.excludeMethodFromXML("getBillingAccount");
    this.excludeMethodFromXML("getKey");
    this.excludeMethodFromXML("getCreateYear");
    this.excludeMethodFromXML("getRequestCategory");
    this.excludeMethodFromXML("getChromatograms");
    this.excludeMethodFromXML("getPlateWells");
    this.excludeMethodFromXML("getAssays");
    this.excludeMethodFromXML("getFlowCellChannels");
    this.excludeMethodFromXML("getRedoSampleNames");
  }
  
  public String getOwnerName() {
    if (appUser != null) {
      return appUser.getFirstName() + " " + appUser.getLastName();
    } else {
      return "";
    }
  }
  
  public String getSubmitterName() {
    if (submitter != null) {
      return submitter.getFirstName() + " " + submitter.getLastName();
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

  public static String getRequestNumberNoR(String requestNumber) {
    // Get rid of extraneous #
    requestNumber = requestNumber.replaceAll("#", "");
    
    String[] tokens = requestNumber.split("R");
    if(tokens.length > 0) {
      return tokens[0];
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
    return Request.getKey(this.getNumber(), this.getCreateDate(), resultsDir, this.getIdCoreFacility());
  }
  
  public static String getKey(String requestNumber, java.util.Date theCreateDate, String resultsDir, Integer idCoreFacility) {
    if (theCreateDate == null) {
      return "";
    } else {
      String createDate    = new SimpleDateFormat("MM/dd/yyyy").format(theCreateDate);
      String tokens[] = createDate.split("/");
      String createMonth = tokens[0];
      String createDay   = tokens[1];
      String createYear  = tokens[2];
      String sortDate = createYear + createMonth + createDay;
      String key = createYear + Constants.DOWNLOAD_KEY_SEPARATOR + sortDate + Constants.DOWNLOAD_KEY_SEPARATOR + requestNumber + Constants.DOWNLOAD_KEY_SEPARATOR + resultsDir + Constants.DOWNLOAD_KEY_SEPARATOR + idCoreFacility;     
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
  
  public Set getPlateWells() {
    return plateWells;
  }
  
  public void setPlateWells( Set plateWells ) {
    this.plateWells = plateWells;
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
    root.setAttribute("number",          		this.getNonNullString(this.getNumber())); // analysis and datatrack have this "requestNumber" named "number". Now all three are congruent.
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
    root.setAttribute("idInstitution",          this.getIdInstitution() != null ? this.getIdInstitution().toString() : "");
    root.setAttribute("ownerFirstName",         ownerFirstName);
    root.setAttribute("ownerLastName",          ownerLastName);
    root.setAttribute("isExternal",             this.getNonNullString(this.getIsExternal()));
    root.setAttribute("name",                   this.getNonNullString(this.getName()));
    root.setAttribute("isDirty",                "N");
    root.setAttribute("isSelected",             "N");
    root.setAttribute("analysisNames",          "");
    root.setAttribute("idSubmitter",              this.getNonNullString(this.getIdSubmitter()));
    
    root.setAttribute("application",			this.getNonNullString(this.getApplication()));
    
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
      displayName.append(root.getAttributeValue("labName"));
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
      displayName.append(root.getAttributeValue("labName"));
      displayName.append(" ");
      displayName.append(root.getAttributeValue("requestCreateDateDisplayMedium"));      

      root.setAttribute("displayName", displayName.toString());
      root.setAttribute("label",       displayName.toString());
      
    }
    return doc;
  }
  
  public Document getRestrictedVisibilityXML(SecurityAdvisor secAdvisor, DictionaryHelper dh) throws Exception {
    Document doc = new Document(new Element("Request"));
    Element root = doc.getRootElement();
    
    String codeRequestCategory =  this.getNonNullString(this.getCodeRequestCategory());
    RequestCategory requestCategory = dh.getRequestCategoryObject(codeRequestCategory);
    
    root.setAttribute("idRequest", this.getNonNullString(this.getIdRequest()));
    root.setAttribute("icon", requestCategory != null && requestCategory.getIcon() != null ? requestCategory.getIcon() : "");
    root.setAttribute("label", this.getNumber() + " (Restricted Visibility)");
    root.setAttribute("name",                   this.getNonNullString(this.getName()));
    root.setAttribute("number",          this.getNonNullString(this.getNumber())); // analysis and datatrack have "number". Now all three are congruent.
    return doc;
  }  
 
  
  public String isDNASeqExperiment() {
    return RequestCategory.isDNASeqCoreRequestCategory(this.getCodeRequestCategory()) == true ? "Y" : "N";
  }
  
  public String getIsDNASeqExperiment() {
    return isDNASeqExperiment();
  }

  public Boolean isCapSeqPlate() {
    Boolean retVal = false;
    if (this.getCodeRequestCategory().equals(RequestCategory.CAPILLARY_SEQUENCING_REQUEST_CATEGORY) && this.getSamples().size() > 0) {
      retVal = isPlateRequest();
    }
    return retVal;
  }
  

  public Boolean isSequenomPlate() {
    Boolean retVal = false;
    if (this.getCodeRequestCategory() != null && RequestCategory.isSequenomType( this.getCodeRequestCategory() ) && this.getSamples().size() > 0) {
      retVal = isPlateRequest();
    }
    return retVal;
  }
  
  private Boolean isPlateRequest() {
    Boolean retVal = false;
    if (this.getSamples().size() > 0) {
      Sample firstSample = (Sample)this.getSamples().toArray()[0];
      if (firstSample.getWells() != null) {
        for(Iterator i = firstSample.getWells().iterator();i.hasNext();) {
          PlateWell w = (PlateWell)i.next();
          if (w.getIdPlate() == null) {
            // null plate only happens for source plate of tube types.
            retVal = false;
            break;
          } else {
            retVal = true;
          }
        }
      }
    }
    return retVal;
  }
  
  public Map<String, Assay> getAssays() {
    TreeMap<String, Assay> assays = new TreeMap<String, Assay>();
    if (this.getCodeRequestCategory().equals(RequestCategory.FRAGMENT_ANALYSIS_REQUEST_CATEGORY)) {
      for(Iterator i=this.getSamples().iterator();i.hasNext();) {
        Sample sample = (Sample)i.next();
        assays.putAll(sample.getAssays());
      }
    }
    return assays;
  }
  
  public Boolean onReactionPlate() {
    
    boolean onReactionPlate = false;
    
    
      // Find out if the samples are on a reaction plate.  If they
      // are, flag the request so that appropriate warnings
      // can be displayed if the data is changed.
      for (Sample s : (Set<Sample>)this.getSamples()) {
        for (PlateWell well : (Set<PlateWell>)s.getWells()) {
          if (well.getPlate() != null && well.getPlate().getCodePlateType().equals(PlateType.REACTION_PLATE_TYPE)) {
              onReactionPlate = true;
              break;
          }
        }
      }

    return onReactionPlate;
    
  }
  
  public String getRedoSampleNames() {
    StringBuffer redoSamples = new StringBuffer();
    
    for (Sample s : (Set<Sample>)this.getSamples()) {
      for (PlateWell well : (Set<PlateWell>)s.getWells()) {
        if (well.getPlate() == null || well.getPlate().getCodePlateType().equals(PlateType.SOURCE_PLATE_TYPE)) {
          if (well.getRedoFlag() != null && well.getRedoFlag().equals("Y")) {
            if (redoSamples.length() > 0) {
              redoSamples.append(", ");
            }
            redoSamples.append(s.getName());
          }
        }
      }
    }
    return redoSamples.toString();
  }
  
  public Integer getIdIScanChip() {
    return idIScanChip;
  }
  
  public void setIdIScanChip( Integer idIScanChip ) {
    this.idIScanChip = idIScanChip;
  }
  public Integer getNumberIScanChips() {
    return numberIScanChips;
  }
  
  public void setNumberIScanChips( Integer numberIScanChips ) {
    this.numberIScanChips = numberIScanChips;
  }
  
  
  public String getApplicationNotes() {
    return applicationNotes;
  }
  
  public void setApplicationNotes( String applicationNotes ) {
    this.applicationNotes = applicationNotes;
  }
  
  public String getCoreToExtractDNA() {
    return coreToExtractDNA;
  }
  
  public void setCoreToExtractDNA( String coreToExtractDNA ) {
    this.coreToExtractDNA = coreToExtractDNA;
  }
  
  public Date getProcessingDate() {
    return processingDate;
  }
  public void setProcessingDate(Date processingDate) {
    this.processingDate = processingDate;
  }

  public boolean isLibPrepByCore() {
    if (this.getSamples().size() > 0) {
      Sample sample = (Sample)this.getSamples().iterator().next();
      return sample.getSeqPrepByCore() == null || sample.getSeqPrepByCore().equals("Y");
    } else {
      return true;
    }
  }
  


  public Map<Integer, FlowCellChannel> getFlowCellChannels() {
    Map<Integer, FlowCellChannel> channels = new HashMap<Integer, FlowCellChannel>();
    for(SequenceLane lane : (Set<SequenceLane>)this.getSequenceLanes()) {
      if (lane.getIdFlowCellChannel() != null && !channels.containsKey(lane.getIdFlowCellChannel())) {
        channels.put(lane.getIdFlowCellChannel(), lane.getFlowCellChannel());
      }
    }
    
    return channels;
  }

  
  /*
   * This is a convenience method used by GetRequest, GetAnalysis, GetDataTrack to fill in the XML for a "related" experiment.
   * This experiment may be related in terms of the Experiment->Analysis->DataTrack links or the links to Topics.
   */

  public Element appendBasicXML(SecurityAdvisor secAdvisor, Element parentNode) throws UnknownPermissionException {
    String icon = "";
    if (this.getRequestCategory().getIcon() != null) {
      icon = this.getRequestCategory().getIcon();
    }
    Element requestNode = new Element("Request");
    requestNode.setAttribute("idRequest", this.getIdRequest().toString());
    requestNode.setAttribute("label", this.getNumber() + " " + (secAdvisor.canRead(this) ? (this.getName() != null ? this.getName() : "") : "(Not authorized)"));
    requestNode.setAttribute("codeVisibility", this.getCodeVisibility());
    requestNode.setAttribute("number", this.getNumber());
    requestNode.setAttribute("icon", icon);
    requestNode.setAttribute("icon", icon);
    parentNode.addContent(requestNode);
    return requestNode;
  }
  
  public String getMaterialQuoteNumber() {
    return materialQuoteNumber;
  }
  
  public void setMaterialQuoteNumber( String materialQuoteNumber ) {
    this.materialQuoteNumber = materialQuoteNumber;
  }
  
  public Date getQuoteReceivedDate() {
    return quoteReceivedDate;
  }
  
  public void setQuoteReceivedDate( Date quoteReceivedDate ) {
    this.quoteReceivedDate = quoteReceivedDate;
  }
  
  public String getUuid() {
    return uuid;
  }
  
  public void setUuid( String uuid ) {
    this.uuid = uuid;
  }
  
  public String getCodeDNAPrepType() {
    return codeDNAPrepType;
  }
  
  public void setCodeDNAPrepType( String codeDNAPrepType ) {
    this.codeDNAPrepType = codeDNAPrepType;
  }
  
  public DNAPrepType getDnaPrepType() {
    return dnaPrepType;
  }
  
  public void setDnaPrepType( DNAPrepType dnaPrepType ) {
    this.dnaPrepType = dnaPrepType;
  }

  public String getCodeRNAPrepType() {
    return codeRNAPrepType;
  }
  
  public void setCodeRNAPrepType( String codeRNAPrepType ) {
    this.codeRNAPrepType = codeRNAPrepType;
  }
  
  public RNAPrepType getRnaPrepType() {
    return rnaPrepType;
  }
  
  public void setRnaPrepType( RNAPrepType rnaPrepType ) {
    this.rnaPrepType = rnaPrepType;
  }
  
  public String getHasPrePooledLibraries() {
    return this.hasPrePooledLibraries;
  }
  
  public void setHasPrePooledLibraries(String hasPrePooledLibraries) {
    this.hasPrePooledLibraries = hasPrePooledLibraries;
  }
  
  public Integer getNumPrePooledTubes() {
    return this.numPrePooledTubes;
  }
  
  public void setNumPrePooledTubes(Integer numPrePooledTubes) {
    this.numPrePooledTubes = numPrePooledTubes;
  }
  
  public String getSeqPrepByCore() {
    // Since all samples have to have the same value for this, expose it at this level.
    if (Hibernate.isInitialized(samples) && samples.size() > 0) {
      Sample sample = (Sample)samples.iterator().next();
      if (sample.getSeqPrepByCore() != null) {
        return sample.getSeqPrepByCore();
      } else {
        return "";
      }
    } else {
      return "";
    }
  }
  public String getTrimAdapter() {
    return trimAdapter;
  }
  public void setTrimAdapter(String trimAdapter) {
    this.trimAdapter = trimAdapter;
  }
  
  public String getIncludeBisulfideConversion() {
    return includeBisulfideConversion;
  }
  
  public void setIncludeBisulfideConversion( String includeBisulfideConversion ) {
    this.includeBisulfideConversion = includeBisulfideConversion;
  }
  
  public String getIncludeQubitConcentration() {
    return includeQubitConcentration;
  }
  
  public void setIncludeQubitConcentration( String includeQubitConcentration ) {
    this.includeQubitConcentration = includeQubitConcentration;
  }
  
  public String getTurnAroundTime() {
    if(this.createDate != null && this.completedDate != null) {
      long difference = ((this.completedDate.getTime() - this.createDate.getTime()) / (1000 * 60 * 60 * 24));
      return Long.toString(difference);
    }
    
    return "";
  }
}
