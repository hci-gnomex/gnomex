package hci.gnomex.utility;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import hci.gnomex.model.*;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;

import hci.gnomex.constants.Constants;
import hci.gnomex.security.SecurityAdvisor;

public class RequestParser implements Serializable {

  private SecurityAdvisor secAdvisor;
  private boolean isImport = false;
  private Element requestNode;
  private Request request;
  private boolean isNewRequest = false;
  private boolean reassignBillingAccount = false;
  private String otherCharacteristicLabel;
  private List sampleIds = new ArrayList();
  private Map sampleMap = new HashMap();
  private Map propertiesToApplyMap = new TreeMap();
  private Map seqLibTreatmentMap = new HashMap();
  private Map collaboratorUploadMap = new HashMap();
  private Map collaboratorUpdateMap = new HashMap();
  private Map sampleAnnotationMap = new HashMap();
  private boolean showTreatments = false;
  private Map sampleTreatmentMap = new HashMap();
  private Map sampleAnnotationCodeMap = new TreeMap();
  private List hybInfos = new ArrayList();
  private List sequenceLaneInfos = new ArrayList();
  private boolean saveReuseOfSlides = false;
  private String amendState = "";
  private List<String> ccNumberList = new ArrayList<String>();
  private Integer originalIdLab = null;
  private Map<String, Plate> plateMap = new HashMap<String, Plate>();
  private Map<String, PlateWell> wellMap = new HashMap<String, PlateWell>();
  private Map<String, SamplePlateWell> sampleToPlateMap = new HashMap<String, SamplePlateWell>();
  private Map<String, ArrayList<String>> sampleAssays = new HashMap<String, ArrayList<String>>();
  private Map<String, String> cherryPickSourceWells = new HashMap<String, String>();
  private Map<String, String> cherryPickSourcePlates = new HashMap<String, String>();
  private Map<String, String> cherryPickDestinationWells = new HashMap<String, String>();
  private Boolean hasPlates = false;
  private Boolean forDownload = false;
  private String seqPrepByCore = null;
  private String previousCodeRequestStatus = null;
  private BillingTemplate billingTemplate;
  private Set<BillingTemplateItem> 	billingTemplateItems;
  private boolean isOpeningNewBillingTemplate;

  public RequestParser(Document requestDoc, SecurityAdvisor secAdvisor) {
    this.requestNode = requestDoc.getRootElement();
    this.secAdvisor = secAdvisor;
    this.forDownload = false;
  }

  public RequestParser(Element requestNode, SecurityAdvisor secAdvisor) {
    this.requestNode = requestNode;
    this.secAdvisor = secAdvisor;
    this.forDownload = false;
  }

  public RequestParser(Document requestDoc, SecurityAdvisor secAdvisor, Boolean forDownload) {
    this.requestNode = requestDoc.getRootElement();
    this.secAdvisor = secAdvisor;
    this.forDownload = forDownload;
  }

  public void init() {
    request = null;
    isNewRequest = false;
    otherCharacteristicLabel = null;
    sampleIds = new ArrayList();
    sampleMap = new HashMap();
    propertiesToApplyMap = new TreeMap();
    seqLibTreatmentMap = new HashMap();
    collaboratorUploadMap = new HashMap();
    collaboratorUpdateMap = new HashMap();
    sampleAnnotationMap = new HashMap();
    showTreatments = false;
    sampleTreatmentMap = new HashMap();
    sampleAnnotationCodeMap = new TreeMap();
    hybInfos = new ArrayList();
    sequenceLaneInfos = new ArrayList();
    saveReuseOfSlides = false;
    amendState = "";
    ccNumberList = new ArrayList<String>();
    plateMap = new HashMap<String, Plate>();
    wellMap = new HashMap<String, PlateWell>();
    sampleToPlateMap = new HashMap<String, SamplePlateWell>();
    sampleAssays = new HashMap<String, ArrayList<String>>();
    cherryPickSourcePlates = new HashMap<String, String>();
    cherryPickSourceWells = new HashMap<String, String>();
    cherryPickDestinationWells = new HashMap<String, String>();
    isOpeningNewBillingTemplate = false;
  }

  /*
   * Call this parse method from controller classes. In this case,
   * DictionaryHelper can be used because the servlet ManageDictionaries has
   * already been called during initialization.
   */
  public void parse(Session sess) throws Exception {
    DictionaryHelper dictionaryHelper = DictionaryHelper.getInstance(sess);
    RequestCategory requestCategory = dictionaryHelper.getRequestCategoryObject(requestNode.getAttributeValue("codeRequestCategory"));
    parse(sess, requestCategory);
  }

  public void parseForImport(Session sess, RequestCategory requestCategory) throws Exception {
    parse(sess, requestCategory, true);
  }

  public void parse(Session sess, RequestCategory requestCategory) throws Exception {
    parse(sess, requestCategory, false);
  }

  /*
   * Call this version of parse when coming from a batch java app instead of the
   * web (servlet) interface. In this case, we can't rely on DictionaryHelper
   * since ManageDictionaries only works as a command, not in a stand-alone java
   * app.
   */
  private void parse(Session sess, RequestCategory requestCategory, boolean isImport) throws Exception {
    this.isImport = isImport;

    this.initializeRequest(requestNode, sess, requestCategory);

    for (Iterator i = requestNode.getChild("samples").getChildren("Sample").iterator(); i.hasNext();) {
      Element sampleNode = (Element) i.next();
      this.initializeSample(requestNode, sampleNode, sess, requestCategory);
    }

    if (requestNode.getChild("hybridizations") != null && !requestNode.getChild("hybridizations").getChildren("Hybridization").isEmpty()) {

      for (Iterator i = requestNode.getChild("hybridizations").getChildren("Hybridization").iterator(); i.hasNext();) {
        Element hybNode = (Element) i.next();
        initializeHyb(hybNode);
      }
    }
    if (requestNode.getChild("sequenceLanes") != null && !requestNode.getChild("sequenceLanes").getChildren("SequenceLane").isEmpty()) {

      for (Iterator i = requestNode.getChild("sequenceLanes").getChildren("SequenceLane").iterator(); i.hasNext();) {
        Element sequenceLaneNode = (Element) i.next();
        initializeSequenceLane(sequenceLaneNode);
      }
    }

  }

  private void initializeRequest(Element n, Session sess, RequestCategory requestCategory) throws Exception {

    Integer idRequest = new Integer(n.getAttributeValue("idRequest"));
    System.out.println ("[initializeRequest] idRequest: " + idRequest);
    if (idRequest.intValue() == 0) {
      request = new Request();
      request.setCreateDate(new java.sql.Date(System.currentTimeMillis()));
      request.setCodeVisibility(n.getAttributeValue("codeVisibility"));
      request.setPrivacyExpirationDate(convertDate(n.getAttributeValue("privacyExpirationDate")));

      // We use the experiment ID in the XML if this is an import
      if (isImport) {
        request.setNumber(n.getAttributeValue("number"));
      }

      if (n.getAttributeValue("idInstitution") != null && !n.getAttributeValue("idInstitution").equals("")) {
        request.setIdInstitution(new Integer(n.getAttributeValue("idInstitution")));
      }
      isNewRequest = true;
    } else {
      request = sess.load(Request.class, idRequest);
      originalIdLab = request.getIdLab();
      saveReuseOfSlides = true;

      // If it is an existing request we want to set any new samples to have
      // same seqPrepByCore vaule as old samples.
      if (request.getSamples().size() > 0) {
        seqPrepByCore = ((Sample) request.getSamples().iterator().next()).getSeqPrepByCore();
      }

      // Reset the complete date
      // a QC request to a microarray or sequencing request
      if (this.isQCAmendRequest()) {
        request.setCompletedDate(null);
        request.setCodeRequestStatus(RequestStatus.SUBMITTED);
      }
      request.setLastModifyDate(new java.sql.Date(System.currentTimeMillis()));

      // Only some users have permissions to set the visibility on the request
      if (this.secAdvisor.canUpdate(request, SecurityAdvisor.PROFILE_OBJECT_VISIBILITY)) {
        if (n.getAttributeValue("codeVisibility") == null || n.getAttributeValue("codeVisibility").equals("")) {
          throw new Exception("Visibility is required for experiment " + request.getNumber());
        }
        request.setCodeVisibility(n.getAttributeValue("codeVisibility"));
        request.setPrivacyExpirationDate(convertDate(n.getAttributeValue("privacyExpirationDate")));

        if (n.getAttributeValue("idInstitution") != null && !n.getAttributeValue("idInstitution").equals("") && !n.getAttributeValue("idInstitution").equals("null")) {
          request.setIdInstitution(new Integer(n.getAttributeValue("idInstitution")));
        }
      }
    }

    if (n.getAttributeValue("codeRequestCategory") != null) {
      request.setCodeRequestCategory(n.getAttributeValue("codeRequestCategory"));
      if (requestCategory.getIsOwnerOnly() != null && requestCategory.getIsOwnerOnly().equals("Y")) {
        request.setCodeVisibility(Visibility.VISIBLE_TO_OWNER);
      }
    }

    initializeRequest(n, request, sess, requestCategory);
  }

  private java.sql.Date convertDate(String dateString) throws Exception {
    java.sql.Date date = null;
    if (dateString != null && dateString.length() > 0) {
      DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
      java.util.Date tmpDate = formatter.parse(dateString);
      date = new java.sql.Date(tmpDate.getTime());
    }
    return date;
  }

  private void initializeRequest(Element n, Request request, Session sess, RequestCategory requestCategory) throws Exception {

    if (n.getAttributeValue("isExternal") != null && !n.getAttributeValue("isExternal").equals("")) {
      request.setIsExternal(n.getAttributeValue("isExternal"));
    }

    if (n.getAttributeValue("amendState") != null && !n.getAttributeValue("amendState").equals("")) {
      amendState = n.getAttributeValue("amendState");
    }

    request.setName(this.unEscape(n.getAttributeValue("name")));

    otherCharacteristicLabel = this.unEscape(n.getAttributeValue(PropertyEntry.OTHER_LABEL));

    request.setCodeRequestCategory(n.getAttributeValue("codeRequestCategory"));

    if (n.getAttributeValue("idCoreFacility") != null && !n.getAttributeValue("idCoreFacility").equals("")) {
      request.setIdCoreFacility(new Integer(n.getAttributeValue("idCoreFacility")));
    } else {
      request.setIdCoreFacility(null);
    }

    if (n.getAttributeValue("codeApplication") != null && !n.getAttributeValue("codeApplication").equals("")) {
      request.setCodeApplication(n.getAttributeValue("codeApplication"));
    }

    if (n.getAttributeValue("idAppUser") != null && !n.getAttributeValue("idAppUser").equals("")) {
      request.setIdAppUser(new Integer(n.getAttributeValue("idAppUser")));
    }
    if (n.getAttributeValue("idSubmitter") != null && !n.getAttributeValue("idSubmitter").equals("")) {
      request.setIdSubmitter(new Integer(n.getAttributeValue("idSubmitter")));
    }
    if (n.getAttributeValue("idLab") != null && !n.getAttributeValue("idLab").equals("")) {
      request.setIdLab(new Integer(n.getAttributeValue("idLab")));
    }
    if (n.getAttributeValue("idProject") != null && !n.getAttributeValue("idProject").equals("")) {
      request.setIdProject(new Integer(n.getAttributeValue("idProject")));
    }

    if (n.getAttributeValue("idSlideProduct") != null && !n.getAttributeValue("idSlideProduct").equals("")) {
      request.setIdSlideProduct(new Integer(n.getAttributeValue("idSlideProduct")));
    }

    if (n.getAttributeValue("idSampleTypeDefault") != null && !n.getAttributeValue("idSampleTypeDefault").equals("")) {
      request.setIdSampleTypeDefault(new Integer(n.getAttributeValue("idSampleTypeDefault")));
    }
    if (n.getAttributeValue("idOrganismSampleDefault") != null && !n.getAttributeValue("idOrganismSampleDefault").equals("")) {
      request.setIdOrganismSampleDefault(new Integer(n.getAttributeValue("idOrganismSampleDefault")));
    } else {
      request.setIdOrganismSampleDefault(null);
    }
    if (n.getAttributeValue("idSampleDropOffLocation") != null && !n.getAttributeValue("idSampleDropOffLocation").equals("")) {
      request.setIdSampleDropOffLocation(new Integer(n.getAttributeValue("idSampleDropOffLocation")));
    } else {
      request.setIdSampleDropOffLocation(null);
    }
    if (n.getAttributeValue("idProduct") != null && !n.getAttributeValue("idProduct").equals("")) {
      request.setIdProduct(new Integer(n.getAttributeValue("idProduct")));
    }
    if (n.getAttributeValue("coreToExtractDNA") != null && !n.getAttributeValue("coreToExtractDNA").equals(""))
      request.setCoreToExtractDNA(n.getAttributeValue("coreToExtractDNA"));

    if (n.getAttributeValue("applicationNotes") != null && !n.getAttributeValue("applicationNotes").equals(""))
      request.setApplicationNotes(n.getAttributeValue("applicationNotes"));

    if (n.getAttributeValue("includeBisulfideConversion") != null && !n.getAttributeValue("includeBisulfideConversion").equals(""))
      request.setIncludeBisulfideConversion(n.getAttributeValue("includeBisulfideConversion"));

    if (n.getAttributeValue("includeQubitConcentration") != null && !n.getAttributeValue("includeQubitConcentration").equals(""))
      request.setIncludeQubitConcentration(n.getAttributeValue("includeQubitConcentration"));

    if (n.getAttributeValue("newBillingTemplateIdBillingAccount") != null && !n.getAttributeValue("newBillingTemplateIdBillingAccount").equals("")) {
      BillingTemplate oldBillingTemplate = BillingTemplateQueryManager.retrieveBillingTemplate(sess, request);
      if (oldBillingTemplate == null || !oldBillingTemplate.canBeDeactivated(sess)) {
        throw new Exception("Current billing template cannot be deactivated");
      }
      Integer newIdBillingAccount = new Integer(n.getAttributeValue("newBillingTemplateIdBillingAccount"));
      request.setIdBillingAccount(newIdBillingAccount);
      billingTemplate = new BillingTemplate(request);
      billingTemplateItems = new TreeSet<BillingTemplateItem>();
      billingTemplateItems.add(getBillingTemplateItemForIdBA(newIdBillingAccount));
      isOpeningNewBillingTemplate = true;
      reassignBillingAccount = false;
    } else if (n.getAttributeValue("isOpeningNewBillingTemplate") != null && n.getAttributeValue("isOpeningNewBillingTemplate").equals("Y") && (n.getChild("BillingTemplate") != null || (n.getChild("billingTemplate") != null && n.getChild("billingTemplate").getChild("BillingTemplate") != null))) {
      BillingTemplate oldBillingTemplate = BillingTemplateQueryManager.retrieveBillingTemplate(sess, request);
      if (oldBillingTemplate == null || !oldBillingTemplate.canBeDeactivated(sess)) {
        throw new Exception("Current billing template cannot be deactivated");
      }
      Element billingTemplateNode = n.getChild("BillingTemplate") != null ? n.getChild("BillingTemplate") : n.getChild("billingTemplate").getChild("BillingTemplate");
      BillingTemplateParser btParser = new BillingTemplateParser(billingTemplateNode);
      btParser.parse(sess);
      billingTemplate = btParser.getBillingTemplate();
      billingTemplate.setOrder(request);
      billingTemplateItems = btParser.getBillingTemplateItems();
      isOpeningNewBillingTemplate = true;
      reassignBillingAccount = false;
    } else if (n.getAttributeValue("idBillingAccount") != null && !n.getAttributeValue("idBillingAccount").equals("")) {
      Integer newIdBillingAccount = new Integer(n.getAttributeValue("idBillingAccount"));
      request.setIdBillingAccount(newIdBillingAccount);
      billingTemplate = BillingTemplateQueryManager.retrieveBillingTemplate(sess, request);
      // If the billing account has been changed, we need to know so that any billing items can be revised as well.
      if (!isNewRequest && !this.isExternalExperiment()) {
        if (request.getAcceptingBalanceAccountId(sess) == null || !request.getAcceptingBalanceAccountId(sess).equals(newIdBillingAccount) || (billingTemplate != null && billingTemplate.getItems().size() > 1)) {
          reassignBillingAccount = true;
          if (!this.secAdvisor.hasPermission(SecurityAdvisor.CAN_ACCESS_ANY_OBJECT) && !ensureNonAdminCanAccessBillingAccount(newIdBillingAccount, sess)) {
            throw new Exception("User cannot access selected billing account.");
          }
        }
      }
      if (billingTemplate == null) {
        billingTemplate = new BillingTemplate(request);
      }
      billingTemplateItems = new TreeSet<BillingTemplateItem>();
      billingTemplateItems.add(getBillingTemplateItemForIdBA(newIdBillingAccount));
    } else if (n.getAttributeValue("idBillingTemplate") != null && !n.getAttributeValue("idBillingTemplate").equals("")) {
      billingTemplate = sess.get(BillingTemplate.class, Integer.parseInt(n.getAttributeValue("idBillingTemplate")));
      Hibernate.initialize(billingTemplate.getItems());
      billingTemplateItems = billingTemplate.getItems();
      Hibernate.initialize(billingTemplate.getMasterBillingItems());
      if (!isNewRequest && !this.isExternalExperiment()) {
        BillingTemplate oldTemplate = BillingTemplateQueryManager.retrieveBillingTemplate(sess, request);
        if (oldTemplate == null || !oldTemplate.equals(billingTemplate)) {
          reassignBillingAccount = true;
        }
      }
      billingTemplate.setOrder(request);
    } else if (n.getChild("BillingTemplate") != null || (n.getChild("billingTemplate") != null && n.getChild("billingTemplate").getChild("BillingTemplate") != null)) {
      Element billingTemplateNode = n.getChild("BillingTemplate") != null ? n.getChild("BillingTemplate") : n.getChild("billingTemplate").getChild("BillingTemplate");
      BillingTemplateParser btParser = new BillingTemplateParser(billingTemplateNode);
      btParser.parse(sess);
      billingTemplate = btParser.getBillingTemplate();
      billingTemplateItems = btParser.getBillingTemplateItems();
      if (!isNewRequest && !this.isExternalExperiment()) {
        BillingTemplate oldTemplate = BillingTemplateQueryManager.retrieveBillingTemplate(sess, request);
        if (oldTemplate == null || !oldTemplate.equals(billingTemplate)) {
          reassignBillingAccount = true;
        }
      }
      billingTemplate.setOrder(request);
    }

    if (n.getAttributeValue("description") != null && !n.getAttributeValue("description").equals("")) {
      request.setDescription(n.getAttributeValue("description"));
    }

    if (n.getAttributeValue("reagent") != null && !n.getAttributeValue("reagent").equals("")) {
      request.setReagent(n.getAttributeValue("reagent"));
    }
    if (n.getAttributeValue("elutionBuffer") != null && !n.getAttributeValue("elutionBuffer").equals("")) {
      request.setElutionBuffer(n.getAttributeValue("elutionBuffer"));
    }
    if (n.getAttributeValue("usedDnase") != null && !n.getAttributeValue("usedDnase").equals("")) {
      request.setUsedDnase(n.getAttributeValue("usedDnase"));
    } else{
      request.setUsedDnase("N");
    }
    if (n.getAttributeValue("usedRnase") != null && !n.getAttributeValue("usedRnase").equals("")) {
      request.setUsedRnase(n.getAttributeValue("usedRnase"));
    } else{
      request.setUsedRnase("N");
    }
    if (n.getAttributeValue("keepSamples") != null && !n.getAttributeValue("keepSamples").equals("")) {
      request.setKeepSamples(n.getAttributeValue("keepSamples"));
    }

    if (n.getAttributeValue("captureLibDesignId") != null && !n.getAttributeValue("captureLibDesignId").equals(""))
      request.setCaptureLibDesignId(n.getAttributeValue("captureLibDesignId"));

    if (n.getAttributeValue("analysisInstructions") != null && !n.getAttributeValue("analysisInstructions").equals(""))
      request.setAnalysisInstructions(n.getAttributeValue("analysisInstructions"));

    if (n.getAttributeValue("corePrepInstructions") != null && !n.getAttributeValue("corePrepInstructions").equals(""))
      request.setCorePrepInstructions(n.getAttributeValue("corePrepInstructions"));

    if (n.getAttributeValue("adminNotes") != null && !n.getAttributeValue("adminNotes").equals(""))
      request.setAdminNotes(n.getAttributeValue("adminNotes"));

    if (n.getAttributeValue("codeProtocolType") != null && !n.getAttributeValue("codeProtocolType").equals("")) {
      request.setCodeProtocolType(n.getAttributeValue("codeProtocolType"));
    }
    if (n.getAttributeValue("codeBioanalyzerChipType") != null && !n.getAttributeValue("codeBioanalyzerChipType").equals("")) {
      request.setCodeBioanalyzerChipType(n.getAttributeValue("codeBioanalyzerChipType"));
    }
    if (n.getAttributeValue("codeIsolationPrepType") != null && !n.getAttributeValue("codeIsolationPrepType").equals("")) {
      request.setCodeIsolationPrepType(n.getAttributeValue("codeIsolationPrepType"));
    }
    if (n.getAttributeValue("bioinformaticsAssist") != null && !n.getAttributeValue("bioinformaticsAssist").equals("")) {
      request.setBioinformaticsAssist(n.getAttributeValue("bioinformaticsAssist"));
    }
    if (request.getBioinformaticsAssist() == null || (!request.getBioinformaticsAssist().equals("Y") && !request.getBioinformaticsAssist().equals("N"))) {
      request.setBioinformaticsAssist("N");
    }

    if (n.getAttributeValue("hasPrePooledLibraries") != null && !n.getAttributeValue("hasPrePooledLibraries").equals("")) {
      request.setHasPrePooledLibraries(n.getAttributeValue("hasPrePooledLibraries"));
    }
    if (request.getHasPrePooledLibraries() == null || (!request.getHasPrePooledLibraries().equals("Y") && !request.getHasPrePooledLibraries().equals("N"))) {
      request.setHasPrePooledLibraries("N");
    }

    if (request.getHasPrePooledLibraries().equals("Y") && n.getAttributeValue("numPrePooledTubes") != null && !n.getAttributeValue("numPrePooledTubes").equals("")) {
      request.setNumPrePooledTubes(new Integer(n.getAttributeValue("numPrePooledTubes")));
    } else {
      request.setNumPrePooledTubes(null);
    }

    previousCodeRequestStatus = request.getCodeRequestStatus();
    if (n.getAttributeValue("codeRequestStatus") != null && !n.getAttributeValue("codeRequestStatus").equals("")) {
      // Don't change request status to submitted unless the request is in new status
      if (n.getAttributeValue("codeRequestStatus").equals(RequestStatus.SUBMITTED) && (request.getCodeRequestStatus() != null && !request.getCodeRequestStatus().equals(RequestStatus.NEW))) {
        // Do nothing
      } else {
        request.setCodeRequestStatus(n.getAttributeValue("codeRequestStatus"));
        if (n.getAttributeValue("codeRequestStatus").equals(RequestStatus.COMPLETED)) {
          if (request.getCompletedDate() == null) {
            request.setCompletedDate(new java.sql.Date(System.currentTimeMillis()));
          }
          // Now change the billing items for the request from PENDING to COMPLETE
          for (BillingItem billingItem : request.getBillingItemList(sess)) {
            if (billingItem.getCodeBillingStatus().equals(BillingStatus.PENDING)) {
              billingItem.setCodeBillingStatus(BillingStatus.COMPLETED);
            }
          }
        }
      }
    } else {
      if (PropertyDictionaryHelper.getInstance(sess).getCoreFacilityRequestCategoryProperty(request.getIdCoreFacility(), request.getCodeRequestCategory(), PropertyDictionary.NEW_REQUEST_SAVE_BEFORE_SUBMIT).equals("Y")) {
        request.setCodeRequestStatus(RequestStatus.NEW);
      } else {
        request.setCodeRequestStatus(RequestStatus.SUBMITTED);
      }
    }
    request.setProtocolNumber(n.getAttributeValue("protocolNumber"));

    if (n.getChild("PropertyEntries") != null) {
      for (Iterator i1 = n.getChild("PropertyEntries").getChildren("PropertyEntry").iterator(); i1.hasNext();) {
        Element scNode = (Element) i1.next();
        if (scNode.getAttributeValue("isSelected").equals("true")) {
          this.propertiesToApplyMap.put(scNode.getAttributeValue("idProperty"), null);
        }
      }
    }

    if (n.getChild("SeqLibTreatmentEntries") != null) {
      for (Iterator i1 = n.getChild("SeqLibTreatmentEntries").getChildren("SeqLibTreatment").iterator(); i1.hasNext();) {
        Element sltNode = (Element) i1.next();
        this.seqLibTreatmentMap.put(sltNode.getAttributeValue("value"), null);
      }
    }

    if (n.getChild("collaborators") != null) {
      for (Iterator i1 = n.getChild("collaborators").getChildren("ExperimentCollaborator").iterator(); i1.hasNext();) {
        Element collaboratorNode = (Element) i1.next();
        this.collaboratorUploadMap.put(collaboratorNode.getAttributeValue("idAppUser"), collaboratorNode.getAttributeValue("canUploadData"));
        this.collaboratorUpdateMap.put(collaboratorNode.getAttributeValue("idAppUser"), collaboratorNode.getAttributeValue("canUpdate"));
      }
    }

    // Figure out if the user intended to save sample treatments
    if (n.getAttributeValue(TreatmentEntry.TREATMENT) != null && n.getAttributeValue(TreatmentEntry.TREATMENT).equalsIgnoreCase("Y")) {
      showTreatments = true;
    }

    // Is reuse slides checked on request (for new submits only, not updates)
    if (n.getAttributeValue("reuseSlides") != null && n.getAttributeValue("reuseSlides").equalsIgnoreCase("Y")) {
      this.saveReuseOfSlides = true;
    }

    // On existing requests, save visibility and privacyExpirationDate
    if (!isNewRequest) {
      if (request.getRequestCategory().getIsOwnerOnly() == null || request.getRequestCategory().getIsOwnerOnly().equals("N")) {
        if (this.secAdvisor.canUpdate(request, SecurityAdvisor.PROFILE_OBJECT_VISIBILITY)) {
          request.setCodeVisibility(n.getAttributeValue("codeVisibility"));
          request.setPrivacyExpirationDate(convertDate(n.getAttributeValue("privacyExpirationDate")));
        }
      } else if (request.getRequestCategory().getIsOwnerOnly() != null && request.getRequestCategory().getIsOwnerOnly().equals("Y")) {
        request.setCodeVisibility(Visibility.VISIBLE_TO_OWNER);
      }
    }

  }

  public BillingTemplateItem getBillingTemplateItemForIdBA(int idBillingAccount) throws Exception {
    BillingTemplateItem item = new BillingTemplateItem();
    item.setIdBillingAccount(idBillingAccount);
    item.setPercentSplit(BillingTemplateItem.WILL_TAKE_REMAINING_BALANCE);
    return item;
  }

  public Set<BillingTemplateItem> getBillingTemplateItems() {
    return billingTemplateItems;
  }

  private void initializeSample(Element requestNode, Element n, Session sess, RequestCategory requestCategory) throws Exception {
    boolean isNewSample = false;
    Sample sample = null;

    String idSampleString = n.getAttributeValue("idSample");
    if (isNewRequest || idSampleString == null || idSampleString.equals("") || idSampleString.startsWith("Sample")) {
      sample = new Sample();
      isNewSample = true;
    } else {
      sample = sess.load(Sample.class, new Integer(idSampleString));
    }
    sample.setIdSampleString(idSampleString);

    PropertyDictionaryHelper propertyHelper = PropertyDictionaryHelper.getInstance(sess);

    Boolean isExternal = (requestNode.getAttributeValue("isExternal") != null && requestNode.getAttributeValue("isExternal").equals("Y"));

    if (requestCategory.getCategoryType() != null && requestCategory.getCategoryType().getIsIllumina().equals("Y") && !isExternal) {
      initializeSample(n, sample, idSampleString, isNewSample, propertyHelper, true);
    } else if (requestCategory.getCategoryType() != null && requestCategory.getType().equals(RequestCategoryType.TYPE_MISEQ) && !isExternal) {
      initializeSample(n, sample, idSampleString, isNewSample, propertyHelper, true);
    } else {
      initializeSample(n, sample, idSampleString, isNewSample, propertyHelper, false);
    }

    if (isExternal) {
      // the request create screen doesn't do the idOrganism at the request
      // level so skip.
      if (requestNode.getAttributeValue("idOrganism") != null && requestNode.getAttributeValue("idOrganism").toString().length() > 0) {
        sample.setIdOrganism(new Integer(requestNode.getAttributeValue("idOrganism")));
        if (requestNode.getAttributeValue("otherOrganism") != null) {
          sample.setOtherOrganism(requestNode.getAttributeValue("otherOrganism"));
        } else {
          sample.setOtherOrganism("");
        }
      }
    }
  }

  private void initializeSample(Element n, Sample sample, String idSampleString, boolean isNewSample, PropertyDictionaryHelper propertyHelper, boolean isHiseqOrMiseq) throws Exception {

    sample.setName(unEscape(n.getAttributeValue("name")));

    sample.setDescription(unEscape(n.getAttributeValue("description")));

    // We use the sample ID in the XML if this is an import
    if (isImport) {
      sample.setNumber(n.getAttributeValue("number"));
    }

    if (n.getAttributeValue("idSampleType") != null && !n.getAttributeValue("idSampleType").equals("")) {
      sample.setIdSampleType(new Integer(n.getAttributeValue("idSampleType")));
    } else {
      sample.setIdSampleType(null);
    }
    if (n.getAttributeValue("idSampleSource") != null && !n.getAttributeValue("idSampleSource").equals("")) {
      sample.setIdSampleSource(new Integer(n.getAttributeValue("idSampleSource")));
    } else {
      sample.setIdSampleSource(null);
    }
    if (n.getAttributeValue("numberSequencingLanes") != null && !n.getAttributeValue("numberSequencingLanes").equals("")) {
      sample.setNumberSequencingLanes(new Integer(n.getAttributeValue("numberSequencingLanes")));
    } else {
      sample.setNumberSequencingLanes(null);
    }

    if (n.getAttributeValue("otherSamplePrepMethod") != null && !n.getAttributeValue("otherSamplePrepMethod").equals("")) {
      sample.setOtherSamplePrepMethod(n.getAttributeValue("otherSamplePrepMethod"));
    } else {
      sample.setOtherSamplePrepMethod(null);
    }
    if (n.getAttributeValue("idOrganism") != null && !n.getAttributeValue("idOrganism").equals("")) {
      sample.setIdOrganism(new Integer(n.getAttributeValue("idOrganism")));
    } else {
      sample.setIdOrganism(null);
    }
    if (n.getAttributeValue("otherOrganism") != null && !n.getAttributeValue("otherOrganism").equals("")) {
      sample.setOtherOrganism(n.getAttributeValue("otherOrganism"));
    } else {
      sample.setOtherOrganism(null);
    }
    if (n.getAttributeValue("concentration") != null && !n.getAttributeValue("concentration").equals("")) {
      String conc = n.getAttributeValue("concentration").replaceAll(",", "");
      sample.setConcentration(new BigDecimal(conc));
    } else {
      sample.setConcentration(null);
    }

    if (n.getAttributeValue("sampleVolume") != null && !n.getAttributeValue("sampleVolume").equals("")) {
      String volume = n.getAttributeValue("sampleVolume").replaceAll(",", "");
      sample.setSampleVolume(new BigDecimal(volume));
    } else {
      sample.setSampleVolume(null);
    }

    if (n.getAttributeValue("codeConcentrationUnit") != null && !n.getAttributeValue("codeConcentrationUnit").equals("")) {
      sample.setCodeConcentrationUnit(unEscape(n.getAttributeValue("codeConcentrationUnit")));
    } else {
      sample.setCodeConcentrationUnit(ConcentrationUnit.DEFAULT_SAMPLE_CONCENTRATION_UNIT);
    }
    if (n.getAttributeValue("qubitConcentration") != null && !n.getAttributeValue("qubitConcentration").equals("")) {
      sample.setQubitConcentration(new BigDecimal(n.getAttributeValue("qubitConcentration")));
    } else {
      sample.setQubitConcentration(null);
    }
    if (n.getAttributeValue("qcCodeApplication") != null && !n.getAttributeValue("qcCodeApplication").equals("")) {
      sample.setQcCodeApplication(n.getAttributeValue("qcCodeApplication"));
    } else {
      sample.setQcCodeApplication(null);
    }
    if (n.getAttributeValue("codeBioanalyzerChipType") != null && !n.getAttributeValue("codeBioanalyzerChipType").equals("")) {
      sample.setCodeBioanalyzerChipType(n.getAttributeValue("codeBioanalyzerChipType"));
    } else {
      sample.setCodeBioanalyzerChipType(null);
    }
    if (n.getAttributeValue("idOligoBarcode") != null && !n.getAttributeValue("idOligoBarcode").equals("")) {
      sample.setIdOligoBarcode(new Integer(n.getAttributeValue("idOligoBarcode")));
    } else {
      sample.setIdOligoBarcode(null);
    }
    if (n.getAttributeValue("idOligoBarcodeB") != null && !n.getAttributeValue("idOligoBarcodeB").equals("")) {
      sample.setIdOligoBarcodeB(new Integer(n.getAttributeValue("idOligoBarcodeB")));
    } else {
      sample.setIdOligoBarcodeB(null);
    }

    if (isHiseqOrMiseq) {
      if (n.getAttributeValue("multiplexGroupNumber") != null && !n.getAttributeValue("multiplexGroupNumber").equals("")) {
        sample.setMultiplexGroupNumber(new Integer(n.getAttributeValue("multiplexGroupNumber")));
      } else {
        // Allow to continue if just downloading a spread sheet.
        if (!this.forDownload) {
          throw new Exception("MultiplexGroupNumber cannot be empty for HiSeq or MiSeq experiments");
        }
      }
    } else {
      if (n.getAttributeValue("multiplexGroupNumber") != null && !n.getAttributeValue("multiplexGroupNumber").equals("")) {
        sample.setMultiplexGroupNumber(new Integer(n.getAttributeValue("multiplexGroupNumber")));
      } else {
        sample.setMultiplexGroupNumber(null);
      }
    }

    if (n.getAttributeValue("barcodeSequence") != null && !n.getAttributeValue("barcodeSequence").equals("")) {
      sample.setBarcodeSequence(n.getAttributeValue("barcodeSequence"));
    } else {
      sample.setBarcodeSequence(null);
    }
    if (n.getAttributeValue("barcodeSequenceB") != null && !n.getAttributeValue("barcodeSequenceB").equals("")) {
      sample.setBarcodeSequenceB(n.getAttributeValue("barcodeSequenceB"));
    } else {
      sample.setBarcodeSequenceB(null);
    }
    if (n.getAttributeValue("idSeqLibProtocol") != null && !n.getAttributeValue("idSeqLibProtocol").trim().equals("")) {
      sample.setIdSeqLibProtocol(new Integer(n.getAttributeValue("idSeqLibProtocol")));
    } else {
      sample.setIdSeqLibProtocol(null);
    }

    if (seqPrepByCore != null) {
      sample.setSeqPrepByCore(seqPrepByCore);
    } else if (n.getAttributeValue("seqPrepByCore") != null && !n.getAttributeValue("seqPrepByCore").equals("")) {
      sample.setSeqPrepByCore(n.getAttributeValue("seqPrepByCore"));
    } else {
      sample.setSeqPrepByCore("Y");
    }

    if (n.getAttributeValue("fragmentSizeFrom") != null && !n.getAttributeValue("fragmentSizeFrom").equals("")) {
      sample.setFragmentSizeFrom(new Integer(n.getAttributeValue("fragmentSizeFrom")));
    } else {
      sample.setFragmentSizeFrom(null);
    }
    if (n.getAttributeValue("fragmentSizeTo") != null && !n.getAttributeValue("fragmentSizeTo").equals("")) {
      sample.setFragmentSizeTo(new Integer(n.getAttributeValue("fragmentSizeTo")));
    } else {
      sample.setFragmentSizeTo(null);
    }
    if (n.getAttributeValue("prepInstructions") != null && !n.getAttributeValue("prepInstructions").equals("")) {
      sample.setPrepInstructions(n.getAttributeValue("prepInstructions"));
    } else {
      sample.setPrepInstructions(null);
    }
    if (n.getAttributeValue("meanLibSizeActual") != null && !n.getAttributeValue("meanLibSizeActual").equals("")) {
      sample.setMeanLibSizeActual(new Integer((n.getAttributeValue("meanLibSizeActual"))));
    } else {
      sample.setMeanLibSizeActual(null);
    }

    if (propertyHelper.getProperty(PropertyDictionary.BST_LINKAGE_SUPPORTED) != null && propertyHelper.getProperty(PropertyDictionary.BST_LINKAGE_SUPPORTED).equals("Y")) {
      if (n.getAttributeValue("ccNumber") != null && !n.getAttributeValue("ccNumber").equals("")) {
        String ccNumber = n.getAttributeValue("ccNumber");
        sample.setCcNumber(ccNumber);
        if (!ccNumberList.contains(ccNumber)) {
          ccNumberList.add(ccNumber);
        }
      } else {
        sample.setCcNumber(null);
      }
    }

    sampleMap.put(idSampleString, sample);
    sampleIds.add(idSampleString);

    // Hash sample characteristics entries
    Map annotations = new HashMap();
    for (Iterator i = n.getAttributes().iterator(); i.hasNext();) {

      Attribute a = (Attribute) i.next();
      String attributeName = a.getName();
      String value = unEscape(a.getValue());

      // Strip off "ANNOT" from attribute name
      if (attributeName.startsWith("ANNOT")) {
        attributeName = attributeName.substring(5);
      }

      if (value != null && this.propertiesToApplyMap.containsKey(attributeName)) {
        annotations.put(Integer.valueOf(attributeName), value);
        sampleAnnotationCodeMap.put(attributeName, null);
      }
    }
    sampleAnnotationMap.put(idSampleString, annotations);

    // Hash sample treatment
    if (showTreatments && n.getAttributeValue(TreatmentEntry.TREATMENT) != null && !n.getAttributeValue(TreatmentEntry.TREATMENT).equals("")) {
      sampleTreatmentMap.put(idSampleString, unEscape(n.getAttributeValue(TreatmentEntry.TREATMENT)));
    }

    // If the user can manage workflow, initialize the sample quality control fields
    // (for updating).
    if (this.secAdvisor.hasPermission(SecurityAdvisor.CAN_MANAGE_WORKFLOW)) {
      if (n.getAttributeValue("qual260nmTo280nmRatio") != null && !n.getAttributeValue("qual260nmTo280nmRatio").equals("")) {
        sample.setQual260nmTo280nmRatio(new BigDecimal(n.getAttributeValue("qual260nmTo280nmRatio")));
      } else {
        sample.setQual260nmTo280nmRatio(null);
      }

      if (n.getAttributeValue("qual260nmTo230nmRatio") != null && !n.getAttributeValue("qual260nmTo230nmRatio").equals("")) {
        sample.setQual260nmTo230nmRatio(new BigDecimal(n.getAttributeValue("qual260nmTo230nmRatio")));
      } else {
        sample.setQual260nmTo230nmRatio(null);
      }

      if (n.getAttributeValue("qualFragmentSizeFrom") != null && !n.getAttributeValue("qualFragmentSizeFrom").equals("")) {
        sample.setQualFragmentSizeFrom(new Integer(n.getAttributeValue("qualFragmentSizeFrom")));
      } else {
        sample.setQualFragmentSizeFrom(null);
      }
      if (n.getAttributeValue("qualFragmentSizeTo") != null && !n.getAttributeValue("qualFragmentSizeTo").equals("")) {
        sample.setQualFragmentSizeTo(new Integer(n.getAttributeValue("qualFragmentSizeTo")));
      } else {
        sample.setQualFragmentSizeTo(null);
      }

      if (n.getAttributeValue("qualCalcConcentration") != null && !n.getAttributeValue("qualCalcConcentration").equals("")) {
        sample.setQualCalcConcentration(new BigDecimal(n.getAttributeValue("qualCalcConcentration")));
      } else {
        sample.setQualCalcConcentration(null);
      }

      if (n.getAttributeValue("qual28sTo18sRibosomalRatio") != null && !n.getAttributeValue("qual28sTo18sRibosomalRatio").equals("")) {
        sample.setQual28sTo18sRibosomalRatio(new BigDecimal(n.getAttributeValue("qual28sTo18sRibosomalRatio")));
      } else {
        sample.setQual28sTo18sRibosomalRatio(null);
      }

      if (n.getAttributeValue("qualRINNumber") != null && !n.getAttributeValue("qualRINNumber").equals("")) {
        sample.setQualRINNumber(n.getAttributeValue("qualRINNumber"));
      } else {
        sample.setQualRINNumber(null);
      }

      if (n.getAttributeValue("qualStatus") != null && !n.getAttributeValue("qualStatus").equals("")) {
        String status = n.getAttributeValue("qualStatus");
        if (status.equals(Constants.STATUS_COMPLETED)) {
          sample.setQualDate(new java.sql.Date(System.currentTimeMillis()));
          sample.setQualFailed("N");
          sample.setQualBypassed("N");

        } else if (status.equals(Constants.STATUS_TERMINATED)) {
          sample.setQualDate(null);
          sample.setQualFailed("Y");
          sample.setQualBypassed("N");

        } else if (status.equals(Constants.STATUS_BYPASSED)) {
          sample.setQualDate(null);
          sample.setQualFailed("N");
          sample.setQualBypassed("Y");
        }
      } else {
        sample.setQualDate(null);
        sample.setQualFailed("N");
        sample.setQualBypassed("N");
      }

      if (n.getAttributeValue("seqPrepQualCodeBioanalyzerChipType") != null && !n.getAttributeValue("seqPrepQualCodeBioanalyzerChipType").equals("")) {
        sample.setSeqPrepQualCodeBioanalyzerChipType(n.getAttributeValue("seqPrepQualCodeBioanalyzerChipType"));
      } else {
        sample.setSeqPrepQualCodeBioanalyzerChipType(null);
      }

      if (n.getAttributeValue("seqPrepGelFragmentSizeFrom") != null && !n.getAttributeValue("seqPrepGelFragmentSizeFrom").equals("")) {
        sample.setSeqPrepGelFragmentSizeFrom(new Integer(n.getAttributeValue("seqPrepGelFragmentSizeFrom")));
      } else {
        sample.setSeqPrepGelFragmentSizeFrom(null);
      }
      if (n.getAttributeValue("seqPrepGelFragmentSizeTo") != null && !n.getAttributeValue("seqPrepGelFragmentSizeTo").equals("")) {
        sample.setSeqPrepGelFragmentSizeTo(new Integer(n.getAttributeValue("seqPrepGelFragmentSizeTo")));
      } else {
        sample.setSeqPrepGelFragmentSizeTo(null);
      }

      if (n.getAttributeValue("seqPrepStatus") != null && !n.getAttributeValue("seqPrepStatus").equals("")) {
        String status = n.getAttributeValue("seqPrepStatus");
        if (status.equals(Constants.STATUS_COMPLETED)) {
          sample.setSeqPrepDate(new java.sql.Date(System.currentTimeMillis()));
          sample.setSeqPrepFailed("N");
          sample.setSeqPrepBypassed("N");

        } else if (status.equals(Constants.STATUS_TERMINATED)) {
          sample.setSeqPrepDate(null);
          sample.setSeqPrepFailed("Y");
          sample.setSeqPrepBypassed("N");

        } else if (status.equals(Constants.STATUS_BYPASSED)) {
          sample.setSeqPrepDate(null);
          sample.setSeqPrepFailed("N");
          sample.setSeqPrepBypassed("Y");
        }
      } else {
        sample.setSeqPrepDate(null);
        sample.setSeqPrepFailed("N");
        sample.setSeqPrepBypassed("N");
      }

    }

    // Have well and plate names so create well and plate rows
    if (n.getAttributeValue("wellName") != null && n.getAttributeValue("wellName").length() > 0 && n.getAttributeValue("plateName") != null && n.getAttributeValue("plateName").length() > 0) {
      this.hasPlates = true;
      Plate plate = new Plate();
      String plateIdAsString = "";
      if (n.getAttributeValue("idPlate") != null && n.getAttributeValue("idPlate").length() > 0) {
        plateIdAsString = n.getAttributeValue("idPlate");
        plate.setIdPlate(Integer.parseInt(n.getAttributeValue("idPlate")));
      } else {
        plateIdAsString = n.getAttributeValue("plateName");
        if (plateMap.containsKey(plateIdAsString)) {
          plate.setIdPlate(plateMap.get(plateIdAsString).getIdPlate());
        }
      }
      plate.setLabel(n.getAttributeValue("plateName"));
      this.plateMap.put(plateIdAsString, plate);

      PlateWell well = new PlateWell();
      String wellIdAsString = "";
      if (n.getAttributeValue("idPlateWell") != null && n.getAttributeValue("idPlateWell").length() > 0) {
        wellIdAsString = n.getAttributeValue("idPlateWell");
        well.setIdPlateWell(Integer.parseInt(n.getAttributeValue("idPlateWell")));
      } else {
        wellIdAsString = plateIdAsString + "&" + n.getAttributeValue("wellName");
      }
      well.setRow(n.getAttributeValue("wellName").substring(0, 1));
      well.setCol(Integer.parseInt(n.getAttributeValue("wellName").substring(1)));
      this.wellMap.put(wellIdAsString, well);

      SamplePlateWell samplePlateWell = new SamplePlateWell();
      samplePlateWell.plateIdAsString = plateIdAsString;
      samplePlateWell.wellIdAsString = wellIdAsString;
      this.sampleToPlateMap.put(idSampleString, samplePlateWell);
    }

    // Hash map of assays chosen. Build up the map
    ArrayList<String> assays = new ArrayList<String>();
    for (Iterator i = n.getAttributes().iterator(); i.hasNext();) {
      Attribute attr = (Attribute) i.next();
      if (attr.getName().startsWith("hasAssay") && attr.getValue() != null && attr.getValue().equals("Y")) {
        String name = attr.getName().substring(8);
        assays.add(name);
      }
    }
    this.sampleAssays.put(idSampleString, assays);

    // Cherry picking source and destination wells.
    if (n.getAttributeValue("sourcePlate") != null && n.getAttributeValue("sourcePlate").length() > 0) {
      this.cherryPickSourcePlates.put(idSampleString, n.getAttributeValue("sourcePlate"));
    }
    if (n.getAttributeValue("sourceWell") != null && n.getAttributeValue("sourceWell").length() > 0) {
      this.cherryPickSourceWells.put(idSampleString, n.getAttributeValue("sourceWell"));
    }
    if (n.getAttributeValue("destinationWell") != null && n.getAttributeValue("destinationWell").length() > 0) {
      this.cherryPickDestinationWells.put(idSampleString, n.getAttributeValue("destinationWell"));
    }
  }

  private void initializeHyb(Element n) {

    HybInfo hybInfo = new HybInfo();

    hybInfo.setIdHybridization(n.getAttributeValue("idHybridization"));

    String idSampleChannel1String = n.getAttributeValue("idSampleChannel1");
    if (idSampleChannel1String != null && !idSampleChannel1String.equals("")) {
      hybInfo.setIdSampleChannel1String(idSampleChannel1String);
      hybInfo.setSampleChannel1((Sample) sampleMap.get(idSampleChannel1String));
    }

    String idSampleChannel2String = n.getAttributeValue("idSampleChannel2");
    if (idSampleChannel2String != null && !idSampleChannel2String.equals("")) {
      hybInfo.setIdSampleChannel2String(idSampleChannel2String);
      hybInfo.setSampleChannel2((Sample) sampleMap.get(idSampleChannel2String));
    }

    String codeSlideSource = null;
    if (n.getAttributeValue("codeSlideSource") != null && !n.getAttributeValue("codeSlideSource").equals("")) {
      codeSlideSource = n.getAttributeValue("codeSlideSource");
    }
    hybInfo.setCodeSlideSource(codeSlideSource);

    if (n.getAttributeValue("idSlideDesign") != null && !n.getAttributeValue("idSlideDesign").equals("")) {
      hybInfo.setIdSlideDesign(new Integer(n.getAttributeValue("idSlideDesign")));
    }

    hybInfo.setNotes(unEscape(n.getAttributeValue("notes")));

    //
    // Workflow fields
    //

    // Labeling (channel1)
    if (n.getAttributeValue("labelingYieldChannel1") != null && !n.getAttributeValue("labelingYieldChannel1").equals("")) {
      hybInfo.setLabelingYieldChannel1(new BigDecimal(n.getAttributeValue("labelingYieldChannel1")));
    }
    if (n.getAttributeValue("idLabelingProtocolChannel1") != null && !n.getAttributeValue("idLabelingProtocolChannel1").equals("")) {
      hybInfo.setIdLabelingProtocolChannel1(new Integer(n.getAttributeValue("idLabelingProtocolChannel1")));
    }

    if (n.getAttributeValue("codeLabelingReactionSizeChannel1") != null && !n.getAttributeValue("codeLabelingReactionSizeChannel1").equals("")) {
      hybInfo.setCodeLabelingReactionSizeChannel1(n.getAttributeValue("codeLabelingReactionSizeChannel1"));
    }

    if (n.getAttributeValue("numberOfReactionsChannel1") != null && !n.getAttributeValue("numberOfReactionsChannel1").equals("")) {
      hybInfo.setNumberOfReactionsChannel1(new Integer(n.getAttributeValue("numberOfReactionsChannel1")));
    }
    if (n.getAttributeValue("labelingStatusChannel1") != null && !n.getAttributeValue("labelingStatusChannel1").equals("")) {
      String status = n.getAttributeValue("labelingStatusChannel1");
      if (status.equals(Constants.STATUS_COMPLETED)) {
        hybInfo.setLabelingCompletedChannel1("Y");
        hybInfo.setLabelingFailedChannel1("N");
        hybInfo.setLabelingBypassedChannel1("N");

      } else if (status.equals(Constants.STATUS_TERMINATED)) {
        hybInfo.setLabelingCompletedChannel1("N");
        hybInfo.setLabelingFailedChannel1("Y");
        hybInfo.setLabelingBypassedChannel1("N");

      } else if (status.equals(Constants.STATUS_BYPASSED)) {
        hybInfo.setLabelingCompletedChannel1("N");
        hybInfo.setLabelingFailedChannel1("N");
        hybInfo.setLabelingBypassedChannel1("Y");
      }
    } else {
      hybInfo.setLabelingCompletedChannel1("N");
      hybInfo.setLabelingFailedChannel1("N");
      hybInfo.setLabelingBypassedChannel1("N");
    }

    // Labeling (channel2)
    if (n.getAttributeValue("labelingYieldChannel2") != null && !n.getAttributeValue("labelingYieldChannel2").equals("")) {
      hybInfo.setLabelingYieldChannel2(new BigDecimal(n.getAttributeValue("labelingYieldChannel2")));
    }
    if (n.getAttributeValue("idLabelingProtocolChannel2") != null && !n.getAttributeValue("idLabelingProtocolChannel2").equals("")) {
      hybInfo.setIdLabelingProtocolChannel2(new Integer(n.getAttributeValue("idLabelingProtocolChannel2")));
    }

    if (n.getAttributeValue("codeLabelingReactionSizeChannel2") != null && !n.getAttributeValue("codeLabelingReactionSizeChannel2").equals("")) {
      hybInfo.setCodeLabelingReactionSizeChannel2(n.getAttributeValue("codeLabelingReactionSizeChannel2"));
    }

    if (n.getAttributeValue("numberOfReactionsChannel2") != null && !n.getAttributeValue("numberOfReactionsChannel2").equals("")) {
      hybInfo.setNumberOfReactionsChannel2(new Integer(n.getAttributeValue("numberOfReactionsChannel2")));
    }
    if (n.getAttributeValue("labelingStatusChannel2") != null && !n.getAttributeValue("labelingStatusChannel2").equals("")) {
      String status = n.getAttributeValue("labelingStatusChannel2");
      if (status.equals(Constants.STATUS_COMPLETED)) {
        hybInfo.setLabelingCompletedChannel2("Y");
        hybInfo.setLabelingFailedChannel2("N");
        hybInfo.setLabelingBypassedChannel2("N");

      } else if (status.equals(Constants.STATUS_TERMINATED)) {
        hybInfo.setLabelingCompletedChannel2("N");
        hybInfo.setLabelingFailedChannel2("Y");
        hybInfo.setLabelingBypassedChannel2("N");

      } else if (status.equals(Constants.STATUS_BYPASSED)) {
        hybInfo.setLabelingCompletedChannel2("N");
        hybInfo.setLabelingFailedChannel2("N");
        hybInfo.setLabelingBypassedChannel2("Y");
      }
    } else {
      hybInfo.setLabelingCompletedChannel2("N");
      hybInfo.setLabelingFailedChannel2("N");
      hybInfo.setLabelingBypassedChannel2("N");
    }

    // Hyb
    if (n.getAttributeValue("hybStatus") != null && !n.getAttributeValue("hybStatus").equals("")) {
      String status = n.getAttributeValue("hybStatus");
      if (status.equals(Constants.STATUS_COMPLETED)) {
        hybInfo.setHybCompleted("Y");
        hybInfo.setHybFailed("N");
        hybInfo.setHybBypassed("N");
      } else if (status.equals(Constants.STATUS_TERMINATED)) {
        hybInfo.setHybCompleted("N");
        hybInfo.setHybFailed("Y");
        hybInfo.setHybBypassed("N");
      } else if (status.equals(Constants.STATUS_BYPASSED)) {
        hybInfo.setHybCompleted("N");
        hybInfo.setHybFailed("N");
        hybInfo.setHybBypassed("Y");
      }
    } else {
      hybInfo.setHybCompleted("N");
      hybInfo.setHybFailed("N");
      hybInfo.setHybBypassed("N");
    }

    if (n.getAttributeValue("slideBarcode") != null && !n.getAttributeValue("slideBarcode").equals("")) {
      hybInfo.setSlideBarcode(n.getAttributeValue("slideBarcode"));
    }
    if (n.getAttributeValue("arrayCoordinateName") != null && !n.getAttributeValue("arrayCoordinateName").equals("")) {
      hybInfo.setArrayCoordinateName(n.getAttributeValue("arrayCoordinateName"));
    }

    if (n.getAttributeValue("idHybProtocol") != null && !n.getAttributeValue("idHybProtocol").equals("")) {
      hybInfo.setIdHybProtocol(new Integer(n.getAttributeValue("idHybProtocol")));
    }
    if (n.getAttributeValue("idScanProtocol") != null && !n.getAttributeValue("idScanProtocol").equals("")) {
      hybInfo.setIdScanProtocol(new Integer(n.getAttributeValue("idScanProtocol")));
    }
    if (n.getAttributeValue("idFeatureExtractionProtocol") != null && !n.getAttributeValue("idFeatureExtractionProtocol").equals("")) {
      hybInfo.setIdFeatureExtractionProtocol(new Integer(n.getAttributeValue("idFeatureExtractionProtocol")));
    }

    // Extraction
    if (n.getAttributeValue("extractionStatus") != null && !n.getAttributeValue("extractionStatus").equals("")) {
      String status = n.getAttributeValue("extractionStatus");
      if (status.equals(Constants.STATUS_COMPLETED)) {
        hybInfo.setExtractionCompleted("Y");
        hybInfo.setExtractionFailed("N");
        hybInfo.setExtractionBypassed("N");
      } else if (status.equals(Constants.STATUS_TERMINATED)) {
        hybInfo.setExtractionCompleted("N");
        hybInfo.setExtractionFailed("Y");
        hybInfo.setExtractionBypassed("N");
      } else if (status.equals(Constants.STATUS_BYPASSED)) {
        hybInfo.setExtractionCompleted("N");
        hybInfo.setExtractionFailed("N");
        hybInfo.setExtractionBypassed("Y");
      }
    } else {
      hybInfo.setExtractionCompleted("N");
      hybInfo.setExtractionFailed("N");
      hybInfo.setExtractionBypassed("N");
    }

    hybInfos.add(hybInfo);
  }

  private void initializeSequenceLane(Element n) {

    SequenceLaneInfo sequenceLaneInfo = new SequenceLaneInfo();

    sequenceLaneInfo.setIdSequenceLane(n.getAttributeValue("idSequenceLane"));

    String idSampleString = n.getAttributeValue("idSample");
    if (idSampleString != null && !idSampleString.equals("")) {
      sequenceLaneInfo.setIdSampleString(idSampleString);
      sequenceLaneInfo.setSample((Sample) sampleMap.get(idSampleString));
    }

    // We use the sample ID in the XML if this is an import
    if (isImport) {
      sequenceLaneInfo.setNumber(n.getAttributeValue("number"));
    }

    if (n.getAttributeValue("idNumberSequencingCycles") != null && !n.getAttributeValue("idNumberSequencingCycles").equals("")) {
      sequenceLaneInfo.setIdNumberSequencingCycles(new Integer(n.getAttributeValue("idNumberSequencingCycles")));
    }

    if (n.getAttributeValue("idNumberSequencingCyclesAllowed") != null && !n.getAttributeValue("idNumberSequencingCyclesAllowed").equals("")) {
      sequenceLaneInfo.setIdNumberSequencingCyclesAllowed(new Integer(n.getAttributeValue("idNumberSequencingCyclesAllowed")));
    }

    if (n.getAttributeValue("idSeqRunType") != null && !n.getAttributeValue("idSeqRunType").equals("")) {
      sequenceLaneInfo.setIdSeqRunType(new Integer(n.getAttributeValue("idSeqRunType")));
    }

    if (n.getAttributeValue("idGenomeBuildAlignTo") != null && !n.getAttributeValue("idGenomeBuildAlignTo").equals("")) {
      sequenceLaneInfo.setIdGenomeBuildAlignTo(new Integer(n.getAttributeValue("idGenomeBuildAlignTo")));
    }

    if (n.getAttributeValue("flowCellChannelSampleConcentrationpM") != null && !n.getAttributeValue("flowCellChannelSampleConcentrationpM").equals("")) {
      sequenceLaneInfo.setFlowCellChannelSampleConcentrationpM(new BigDecimal(n.getAttributeValue("flowCellChannelSampleConcentrationpM")));
    }

    //
    // workflow fields
    //
    if (n.getAttributeValue("numberSequencingCyclesActual") != null && !n.getAttributeValue("numberSequencingCyclesActual").equals("")) {
      sequenceLaneInfo.setNumberSequencingCyclesActual(new Integer(n.getAttributeValue("numberSequencingCyclesActual")));
    }
    if (n.getAttributeValue("clustersPerTile") != null && !n.getAttributeValue("clustersPerTile").equals("")) {
      sequenceLaneInfo.setClustersPerTile(new Integer(n.getAttributeValue("clustersPerTile")));
    }
    if (n.getAttributeValue("fileName") != null && !n.getAttributeValue("fileName").equals("")) {
      sequenceLaneInfo.setFileName(n.getAttributeValue("fileName"));
    }

    // first cycle status
    if (n.getAttributeValue("firstCycleStatus") != null && !n.getAttributeValue("firstCycleStatus").equals("")) {
      String status = n.getAttributeValue("firstCycleStatus");
      if (status.equals(Constants.STATUS_COMPLETED)) {
        sequenceLaneInfo.setSeqRunFirstCycleCompleted("Y");
        sequenceLaneInfo.setSeqRunFirstCycleFailed("N");
      } else if (status.equals(Constants.STATUS_TERMINATED)) {
        sequenceLaneInfo.setSeqRunFirstCycleCompleted("N");
        sequenceLaneInfo.setSeqRunFirstCycleFailed("Y");
      }
    } else {
      sequenceLaneInfo.setSeqRunFirstCycleCompleted("N");
      sequenceLaneInfo.setSeqRunFirstCycleFailed("N");
    }

    // last cycle status
    if (n.getAttributeValue("lastCycleStatus") != null && !n.getAttributeValue("lastCycleStatus").equals("")) {
      String status = n.getAttributeValue("lastCycleStatus");
      if (status.equals(Constants.STATUS_COMPLETED)) {
        sequenceLaneInfo.setSeqRunLastCycleCompleted("Y");
        sequenceLaneInfo.setSeqRunLastCycleFailed("N");
      } else if (status.equals(Constants.STATUS_TERMINATED)) {
        sequenceLaneInfo.setSeqRunLastCycleCompleted("N");
        sequenceLaneInfo.setSeqRunLastCycleFailed("Y");
      }
    } else {
      sequenceLaneInfo.setSeqRunLastCycleCompleted("N");
      sequenceLaneInfo.setSeqRunLastCycleFailed("N");
    }

    // pipeline status
    if (n.getAttributeValue("pipelineStatus") != null && !n.getAttributeValue("pipelineStatus").equals("")) {
      String status = n.getAttributeValue("pipelineStatus");
      if (status.equals(Constants.STATUS_COMPLETED)) {
        sequenceLaneInfo.setSeqRunPipelineCompleted("Y");
        sequenceLaneInfo.setSeqRunPipelineFailed("N");
      } else if (status.equals(Constants.STATUS_TERMINATED)) {
        sequenceLaneInfo.setSeqRunPipelineCompleted("N");
        sequenceLaneInfo.setSeqRunPipelineFailed("Y");
      }
    } else {
      sequenceLaneInfo.setSeqRunPipelineCompleted("N");
      sequenceLaneInfo.setSeqRunPipelineFailed("N");
    }

    sequenceLaneInfos.add(sequenceLaneInfo);
  }

  private boolean ensureNonAdminCanAccessBillingAccount(Integer idBillingAccount, Session sess) {
    boolean canAccess = false;

    BillingAccount billingAccount = sess.load(BillingAccount.class, idBillingAccount);
    if (billingAccount.getUsers() != null && billingAccount.getUsers().size() > 0) {
      for (Iterator iter = billingAccount.getUsers().iterator(); iter.hasNext();) {
        AppUser user = (AppUser) iter.next();
        if (user.getIdAppUser().equals(secAdvisor.getIdAppUser())) {
          canAccess = true;
          break;
        }
      }
    } else {
      canAccess = true;
    }

    if (secAdvisor.hasPermission(SecurityAdvisor.CAN_ACCESS_ANY_OBJECT)) {
      canAccess = true;
    }

    return canAccess;
  }

  public Map getCharacteristicsToApplyMap() {
    return propertiesToApplyMap;
  }

  public Map getSampleAnnotationCodeMap() {
    return sampleAnnotationCodeMap;
  }

  public Map getSampleAnnotationMap() {
    return sampleAnnotationMap;
  }

  public Map getSampleMap() {
    return sampleMap;
  }

  public Map getSampleTreatmentMap() {
    return sampleTreatmentMap;
  }

  public Map getSeqLibTreatmentMap() {
    return seqLibTreatmentMap;
  }

  public Map getCollaboratorUploadMap() {
    return collaboratorUploadMap;
  }

  public Map getCollaboratorUpdateMap() {
    return collaboratorUpdateMap;
  }

  public boolean getShowTreatments() {
    return showTreatments;
  }

  public String getOtherCharacteristicLabel() {
    return otherCharacteristicLabel;
  }

  public Request getRequest() {
    return request;
  }

  public List getHybInfos() {
    return hybInfos;
  }

  public List getSampleIds() {
    return sampleIds;
  }

  public static class HybInfo implements Serializable {
    private String idHybridization;
    private String idSampleChannel1String;
    private String idSampleChannel2String;
    private Sample sampleChannel1;
    private Sample sampleChannel2;
    private String codeSlideSource;
    private Integer idSlideDesign;
    private String notes;

    private BigDecimal labelingYieldChannel1;
    private Integer idLabelingProtocolChannel1;
    private Integer numberOfReactionsChannel1;
    private String codeLabelingReactionSizeChannel1;
    private String labelingCompletedChannel1 = "N";
    private String labelingFailedChannel1 = "N";
    private String labelingBypassedChannel1 = "N";

    private BigDecimal labelingYieldChannel2;
    private Integer idLabelingProtocolChannel2;
    private Integer numberOfReactionsChannel2;
    private String codeLabelingReactionSizeChannel2;
    private String labelingCompletedChannel2 = "N";
    private String labelingFailedChannel2 = "N";
    private String labelingBypassedChannel2 = "N";

    private String slideBarcode;
    private String arrayCoordinateName;
    private Integer idHybProtocol;
    private Integer idScanProtocol;
    private Integer idFeatureExtractionProtocol;
    private String hybCompleted = "N";
    private String hybFailed = "N";
    private String hybBypassed = "N";
    private String extractionCompleted;
    private String extractionFailed;
    private String extractionBypassed;

    public String getIdHybridization() {
      return idHybridization;
    }

    public void setIdHybridization(String idHybridization) {
      this.idHybridization = idHybridization;
    }

    public Integer getIdSlideDesign() {
      return idSlideDesign;
    }

    public void setIdSlideDesign(Integer idSlideDesign) {
      this.idSlideDesign = idSlideDesign;
    }

    public Sample getSampleChannel1() {
      return sampleChannel1;
    }

    public void setSampleChannel1(Sample sampleChannel1) {
      this.sampleChannel1 = sampleChannel1;
    }

    public Sample getSampleChannel2() {
      return sampleChannel2;
    }

    public void setSampleChannel2(Sample sampleChannel2) {
      this.sampleChannel2 = sampleChannel2;
    }

    public String getNotes() {
      return notes;
    }

    public void setNotes(String notes) {
      this.notes = notes;
    }

    public String getIdSampleChannel1String() {
      return idSampleChannel1String;
    }

    public void setIdSampleChannel1String(String idSampleChannel1String) {
      this.idSampleChannel1String = idSampleChannel1String;
    }

    public String getIdSampleChannel2String() {
      return idSampleChannel2String;
    }

    public void setIdSampleChannel2String(String idSampleChannel2String) {
      this.idSampleChannel2String = idSampleChannel2String;
    }

    public String getCodeSlideSource() {
      return codeSlideSource;
    }

    public void setCodeSlideSource(String codeSlideSource) {
      this.codeSlideSource = codeSlideSource;
    }

    public String getArrayCoordinateName() {
      return arrayCoordinateName;
    }

    public void setArrayCoordinateName(String arrayCoordinateName) {
      this.arrayCoordinateName = arrayCoordinateName;
    }

    public String getCodeLabelingReactionSizeChannel1() {
      return codeLabelingReactionSizeChannel1;
    }

    public void setCodeLabelingReactionSizeChannel1(String codeLabelingReactionSizeChannel1) {
      this.codeLabelingReactionSizeChannel1 = codeLabelingReactionSizeChannel1;
    }

    public String getCodeLabelingReactionSizeChannel2() {
      return codeLabelingReactionSizeChannel2;
    }

    public void setCodeLabelingReactionSizeChannel2(String codeLabelingReactionSizeChannel2) {
      this.codeLabelingReactionSizeChannel2 = codeLabelingReactionSizeChannel2;
    }

    public String getExtractionCompleted() {
      return extractionCompleted;
    }

    public void setExtractionCompleted(String extractionCompleted) {
      this.extractionCompleted = extractionCompleted;
    }

    public String getHybCompleted() {
      return hybCompleted;
    }

    public void setHybCompleted(String hybCompleted) {
      this.hybCompleted = hybCompleted;
    }

    public String getHybFailed() {
      return hybFailed;
    }

    public void setHybFailed(String hybFailed) {
      this.hybFailed = hybFailed;
    }

    public Integer getIdHybProtocol() {
      return idHybProtocol;
    }

    public void setIdHybProtocol(Integer idHybProtocol) {
      this.idHybProtocol = idHybProtocol;
    }

    public BigDecimal getLabelingYieldChannel1() {
      return labelingYieldChannel1;
    }

    public void setLabelingYieldChannel1(BigDecimal labelingYieldChannel1) {
      this.labelingYieldChannel1 = labelingYieldChannel1;
    }

    public BigDecimal getLabelingYieldChannel2() {
      return labelingYieldChannel2;
    }

    public void setLabelingYieldChannel2(BigDecimal labelingYieldChannel2) {
      this.labelingYieldChannel2 = labelingYieldChannel2;
    }

    public Integer getIdLabelingProtocolChannel1() {
      return idLabelingProtocolChannel1;
    }

    public void setIdLabelingProtocolChannel1(Integer idLabelingProtocolChannel1) {
      this.idLabelingProtocolChannel1 = idLabelingProtocolChannel1;
    }

    public Integer getIdLabelingProtocolChannel2() {
      return idLabelingProtocolChannel2;
    }

    public void setIdLabelingProtocolChannel2(Integer idLabelingProtocolChannel2) {
      this.idLabelingProtocolChannel2 = idLabelingProtocolChannel2;
    }

    public Integer getNumberOfReactionsChannel1() {
      return numberOfReactionsChannel1;
    }

    public void setNumberOfReactionsChannel1(Integer numberOfReactionsChannel1) {
      this.numberOfReactionsChannel1 = numberOfReactionsChannel1;
    }

    public Integer getNumberOfReactionsChannel2() {
      return numberOfReactionsChannel2;
    }

    public void setNumberOfReactionsChannel2(Integer numberOfReactionsChannel2) {
      this.numberOfReactionsChannel2 = numberOfReactionsChannel2;
    }

    public String getSlideBarcode() {
      return slideBarcode;
    }

    public void setSlideBarcode(String slideBarcode) {
      this.slideBarcode = slideBarcode;
    }

    public String getLabelingCompletedChannel1() {
      return labelingCompletedChannel1;
    }

    public void setLabelingCompletedChannel1(String labelingCompletedChannel1) {
      this.labelingCompletedChannel1 = labelingCompletedChannel1;
    }

    public String getLabelingCompletedChannel2() {
      return labelingCompletedChannel2;
    }

    public void setLabelingCompletedChannel2(String labelingCompletedChannel2) {
      this.labelingCompletedChannel2 = labelingCompletedChannel2;
    }

    public String getLabelingFailedChannel1() {
      return labelingFailedChannel1;
    }

    public void setLabelingFailedChannel1(String labelingFailedChannel1) {
      this.labelingFailedChannel1 = labelingFailedChannel1;
    }

    public String getLabelingFailedChannel2() {
      return labelingFailedChannel2;
    }

    public void setLabelingFailedChannel2(String labelingFailedChannel2) {
      this.labelingFailedChannel2 = labelingFailedChannel2;
    }

    public Integer getIdFeatureExtractionProtocol() {
      return idFeatureExtractionProtocol;
    }

    public void setIdFeatureExtractionProtocol(Integer idFeatureExtractionProtocol) {
      this.idFeatureExtractionProtocol = idFeatureExtractionProtocol;
    }

    public Integer getIdScanProtocol() {
      return idScanProtocol;
    }

    public void setIdScanProtocol(Integer idScanProtocol) {
      this.idScanProtocol = idScanProtocol;
    }

    public String getExtractionBypassed() {
      return extractionBypassed;
    }

    public void setExtractionBypassed(String extractionBypassed) {
      this.extractionBypassed = extractionBypassed;
    }

    public String getExtractionFailed() {
      return extractionFailed;
    }

    public void setExtractionFailed(String extractionFailed) {
      this.extractionFailed = extractionFailed;
    }

    public String getHybBypassed() {
      return hybBypassed;
    }

    public void setHybBypassed(String hybBypassed) {
      this.hybBypassed = hybBypassed;
    }

    public String getLabelingBypassedChannel1() {
      return labelingBypassedChannel1;
    }

    public void setLabelingBypassedChannel1(String labelingBypassedChannel1) {
      this.labelingBypassedChannel1 = labelingBypassedChannel1;
    }

    public String getLabelingBypassedChannel2() {
      return labelingBypassedChannel2;
    }

    public void setLabelingBypassedChannel2(String labelingBypassedChannel2) {
      this.labelingBypassedChannel2 = labelingBypassedChannel2;
    }

  }

  public static class SequenceLaneInfo implements Serializable {
    private String idSequenceLane;
    private String idSampleString;
    private String number;

    private Sample sample;
    private Integer idSeqRunType;
    private Integer idNumberSequencingCycles;
    private Integer idNumberSequencingCyclesAllowed;
    private Integer idGenomeBuildAlignTo;
    private String analysisInstructions;
    private Integer numberSequencingCyclesActual;
    private Integer clustersPerTile;
    private String fileName;
    private String seqRunFirstCycleCompleted = "N";
    private String seqRunFirstCycleFailed = "N";
    private String seqRunLastCycleCompleted = "N";
    private String seqRunLastCycleFailed = "N";
    private String seqRunPipelineCompleted = "N";
    private String seqRunPipelineFailed = "N";
    private BigDecimal flowCellChannelSampleConcentrationpM;

    public Integer getIdSeqRunType() {
      return idSeqRunType;
    }

    public void setIdSeqRunType(Integer idSeqRunType) {
      this.idSeqRunType = idSeqRunType;
    }

    public BigDecimal getFlowCellChannelSampleConcentrationpM() {
      return flowCellChannelSampleConcentrationpM;
    }

    public void setFlowCellChannelSampleConcentrationpM(BigDecimal flowCellChannelSampleConcentrationpM) {
      this.flowCellChannelSampleConcentrationpM = flowCellChannelSampleConcentrationpM;
    }

    public Integer getIdNumberSequencingCycles() {
      return idNumberSequencingCycles;
    }

    public void setIdNumberSequencingCycles(Integer idNumberSequencingCycles) {
      this.idNumberSequencingCycles = idNumberSequencingCycles;
    }

    public Integer getIdNumberSequencingCyclesAllowed() {
      return idNumberSequencingCyclesAllowed;
    }

    public void setIdNumberSequencingCyclesAllowed(Integer idNumberSequencingCyclesAllowed) {
      this.idNumberSequencingCyclesAllowed = idNumberSequencingCyclesAllowed;
    }

    public String getIdSampleString() {
      return idSampleString;
    }

    public void setIdSampleString(String idSampleString) {
      this.idSampleString = idSampleString;
    }

    public String getIdSequenceLane() {
      return idSequenceLane;
    }

    public void setIdSequenceLane(String idSequenceLane) {
      this.idSequenceLane = idSequenceLane;
    }

    public Sample getSample() {
      return sample;
    }

    public void setSample(Sample sample) {
      this.sample = sample;
    }

    public Integer getIdGenomeBuildAlignTo() {
      return idGenomeBuildAlignTo;
    }

    public void setIdGenomeBuildAlignTo(Integer idGenomeBuildAlignTo) {
      this.idGenomeBuildAlignTo = idGenomeBuildAlignTo;
    }

    public String getAnalysisInstructions() {
      return analysisInstructions;
    }

    public void setAnalysisInstructions(String analysisInstructions) {
      this.analysisInstructions = analysisInstructions;
    }

    public Integer getNumberSequencingCyclesActual() {
      return numberSequencingCyclesActual;
    }

    public void setNumberSequencingCyclesActual(Integer numberSequencingCyclesActual) {
      this.numberSequencingCyclesActual = numberSequencingCyclesActual;
    }

    public Integer getClustersPerTile() {
      return clustersPerTile;
    }

    public void setClustersPerTile(Integer clustersPerTile) {
      this.clustersPerTile = clustersPerTile;
    }

    public String getFileName() {
      return fileName;
    }

    public void setFileName(String fileName) {
      this.fileName = fileName;
    }

    public String getSeqRunFirstCycleCompleted() {
      return seqRunFirstCycleCompleted;
    }

    public void setSeqRunFirstCycleCompleted(String seqRunFirstCycleCompleted) {
      this.seqRunFirstCycleCompleted = seqRunFirstCycleCompleted;
    }

    public String getSeqRunFirstCycleFailed() {
      return seqRunFirstCycleFailed;
    }

    public void setSeqRunFirstCycleFailed(String seqRunFirstCycleFailed) {
      this.seqRunFirstCycleFailed = seqRunFirstCycleFailed;
    }

    public String getSeqRunLastCycleCompleted() {
      return seqRunLastCycleCompleted;
    }

    public void setSeqRunLastCycleCompleted(String seqRunLastCycleCompleted) {
      this.seqRunLastCycleCompleted = seqRunLastCycleCompleted;
    }

    public String getSeqRunLastCycleFailed() {
      return seqRunLastCycleFailed;
    }

    public void setSeqRunLastCycleFailed(String seqRunLastCycleFailed) {
      this.seqRunLastCycleFailed = seqRunLastCycleFailed;
    }

    public String getSeqRunPipelineCompleted() {
      return seqRunPipelineCompleted;
    }

    public void setSeqRunPipelineCompleted(String seqRunPipelineCompleted) {
      this.seqRunPipelineCompleted = seqRunPipelineCompleted;
    }

    public String getSeqRunPipelineFailed() {
      return seqRunPipelineFailed;
    }

    public void setSeqRunPipelineFailed(String seqRunPipelineFailed) {
      this.seqRunPipelineFailed = seqRunPipelineFailed;
    }

    public String getNumber() {
      return number;
    }

    public void setNumber(String number) {
      this.number = number;
    }

  }

  public boolean isNewRequest() {
    return isNewRequest;
  }

  public Integer getOriginalIdLab() {
    return this.originalIdLab;
  }

  public boolean isExternalExperiment() {
    return request.getIsExternal() != null && request.getIsExternal().equalsIgnoreCase("Y");
  }

  public boolean isAmendRequest() {
    return amendState != null && !amendState.equals("");
  }

  public boolean isQCAmendRequest() {
    return amendState.equals(Constants.AMEND_QC_TO_MICROARRAY) || amendState.equals(Constants.AMEND_QC_TO_SEQ);
  }

  public boolean isSaveReuseOfSlides() {
    return saveReuseOfSlides;
  }

  public void setSaveReuseOfSlides(boolean saveReuseOfSlides) {
    this.saveReuseOfSlides = saveReuseOfSlides;
  }

  public static String unEscapeBasic(String text) {
    if (text == null) {
      return text;
    }
    text = text.replaceAll("&amp;", "&");
    text = text.replaceAll("&quot;", "\"");
    text = text.replaceAll("&apos;", "'");
    text = text.replaceAll("&gt;", ">");
    text = text.replaceAll("&lt;", "<");
    text = text.replaceAll("&#181;", "�");
    text = text.replaceAll("&#xD;", "\r");

    return text;
  }

  public static String unEscape(String text) {
    if (text == null) {
      return text;
    }

    text = text.replaceAll("&#xD;", "         ");
    text = unEscapeBasic(text);
    text = text.replaceAll("&#xA;", "         ");
    text = text.replaceAll("&#x10;", "         ");
    text = text.replaceAll("&#13;", "         ");
    text = text.replaceAll("&#x9;", "    ");
    return text;
  }

  public List getSequenceLaneInfos() {
    return sequenceLaneInfos;
  }

  public void setSequenceLaneInfos(List sequenceLaneInfos) {
    this.sequenceLaneInfos = sequenceLaneInfos;
  }

  public String getAmendState() {
    return amendState;
  }

  public void setAmendState(String amendState) {
    this.amendState = amendState;
  }

  public boolean isReassignBillingAccount() {
    return reassignBillingAccount;
  }

  public List<String> getCcNumberList() {
    return ccNumberList;
  }

  public Plate getPlate(String idSampleString) {
    SamplePlateWell spw = this.sampleToPlateMap.get(idSampleString);
    if (spw != null) {
      return this.plateMap.get(spw.plateIdAsString);
    } else {
      return null;
    }
  }

  public PlateWell getWell(String idSampleString) {
    SamplePlateWell spw = this.sampleToPlateMap.get(idSampleString);
    if (spw != null) {
      return this.wellMap.get(spw.wellIdAsString);
    } else {
      return null;
    }
  }

  public String getPlateIdAsString(String idSampleString) {
    SamplePlateWell spw = this.sampleToPlateMap.get(idSampleString);
    if (spw != null) {
      return spw.plateIdAsString;
    } else {
      return null;
    }
  }

  public ArrayList<String> getAssays(String idSampleString) {
    return sampleAssays.get(idSampleString);
  }

  public String getCherryPickSourcePlate(String idSampleString) {
    return this.cherryPickSourcePlates.get(idSampleString);
  }

  public String getCherryPickSourceWell(String idSampleString) {
    return this.cherryPickSourceWells.get(idSampleString);
  }

  public String getCherryPickDestinationWell(String idSampleString) {
    return this.cherryPickDestinationWells.get(idSampleString);
  }

  public Map<String, ArrayList<String>> getSampleAssays() {
    return sampleAssays;
  }

  public Boolean hasPlates() {
    return this.hasPlates;
  }

  public String getPreviousCodeRequestStatus() {
    return previousCodeRequestStatus;
  }

  public BillingTemplate getBillingTemplate() {
    return billingTemplate;
  }

  private class SamplePlateWell implements Serializable {
    public String plateIdAsString = "";
    public String wellIdAsString = "";
  }

  public Boolean getIsOpeningNewBillingTemplate() { return isOpeningNewBillingTemplate; }

}
