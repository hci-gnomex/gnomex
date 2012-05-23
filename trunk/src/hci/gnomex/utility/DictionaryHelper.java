package hci.gnomex.utility;

import hci.dictionary.model.NullDictionaryEntry;
import hci.dictionary.utility.DictionaryManager;
import hci.gnomex.controller.ManageDictionaries;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.GenomeBuild;
import hci.gnomex.model.Lab;
import hci.gnomex.model.OligoBarcode;
import hci.gnomex.model.Organism;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.model.Sample;
import hci.gnomex.model.Property;
import hci.gnomex.model.SeqLibTreatment;
import hci.gnomex.model.SeqRunType;
import hci.gnomex.model.SlideDesign;
import hci.gnomex.model.SubmissionInstruction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Session;



public class DictionaryHelper implements Serializable {
  private static DictionaryHelper theInstance;
  
  private PropertyDictionaryHelper propertyDictionaryHelper;
  private List                     requestCategoryList      = new ArrayList();
  private Map                      requestCategoryMap       = new HashMap();
  private Map                      oligoBarcodeMap          = new HashMap();
  private Map                      submissionInstructionMap = new HashMap();
  private Map                      billingPeriodMap         = new HashMap();
  private Map                      seqLibTreatmentMap       = new HashMap();
  private Map                      slideDesignMap           = new HashMap();
  private Map                      propertyDictionaryMap    = new HashMap();
  private List                     seqRunTypeList           = new ArrayList();

  // For DataTrack functionality
  private final HashMap<Integer, Property>            propertyMap  = new HashMap<Integer, Property>();
  private final List<Property>                        propertyList = new ArrayList<Property>();
  private final HashMap<Integer, Organism>            organismMap  = new HashMap<Integer, Organism>();
  private final  List<Organism>                       organismList = new ArrayList<Organism>();
  private final HashMap<Integer, GenomeBuild>         genomeBuildMap  = new HashMap<Integer, GenomeBuild>();
  private final List<GenomeBuild>                     genomeBuildList = new ArrayList<GenomeBuild>();
  private final HashMap<Integer, List<GenomeBuild>>   organismToGenomeBuildMap = new HashMap<Integer, List<GenomeBuild>>();
  private final HashMap<Integer, AppUser>             appUserMap               = new HashMap<Integer, AppUser>();
  private final HashMap<Integer, Lab>                 labMap  = new HashMap<Integer, Lab>();
  private final List<Lab>                             labList = new ArrayList<Lab>();
  
  private boolean managedDictionariesLoaded = false;

  public DictionaryHelper() {    
  }
  
  public static synchronized DictionaryHelper getInstance(Session sess) {
    if (theInstance == null) {
      theInstance = new DictionaryHelper();
      theInstance.loadDictionaries(sess);
    }
    return theInstance;
    
  }
  
  public static synchronized DictionaryHelper reload(Session sess) {
    theInstance = new DictionaryHelper();
    PropertyDictionaryHelper.reload(sess);
    theInstance.loadDictionaries(sess);  
    theInstance.loadManagedDictionaries();
    return theInstance;
    
  }
  
  /**
   * Only reload the cached dictionaries here, not the dictionary managed
   * dictionaries.  we need this special reload for web apps outside
   * of gnomex (das2) so that they can get a fresh copy of dictionaries
   * like organism and genome build.
   */
  public static synchronized DictionaryHelper reloadLimited(Session sess) {
    theInstance = new DictionaryHelper();
    PropertyDictionaryHelper.reload(sess);
    theInstance.loadDictionaries(sess);  
    return theInstance;
  }
  
  private void lazyLoadManagedDictionaries(){
    if (!managedDictionariesLoaded) {
      loadManagedDictionaries();
    }
  }
  
  
  private void loadDictionaries(Session sess)  {
    
    propertyDictionaryHelper = PropertyDictionaryHelper.getInstance(sess);
    
    
    StringBuffer queryBuf = new StringBuffer();
    queryBuf.append("SELECT p from Property as p order by p.name");
    List properties = sess.createQuery(queryBuf.toString()).list();
    for (Iterator i = properties.iterator(); i.hasNext();) {
      Property prop = (Property)i.next();
      try {
        Hibernate.initialize(prop.getOptions());        
      } catch (HibernateException e) {
        System.out.println("warning - unable to initialize options on property " + prop.getIdProperty() + " " + e.toString());
      } 
      propertyMap.put(prop.getIdProperty(), prop);
      propertyList.add(prop);
    }
    
    List<Organism> organisms = (List<Organism>) sess.createQuery("SELECT d from Organism d order by d.binomialName").list();
    for (Organism d : organisms) {
      organismMap.put(d.getIdOrganism(), d);
      organismList.add(d);
    }

    List<GenomeBuild> genomeBuilds = (List<GenomeBuild>) sess.createQuery("SELECT d from GenomeBuild d order by d.buildDate desc, d.genomeBuildName asc").list();
    for (GenomeBuild d : genomeBuilds) {
      Hibernate.initialize(d.getDataTrackFolders());
      genomeBuildMap.put(d.getIdGenomeBuild(), d);
      genomeBuildList.add(d);

      List<GenomeBuild> versions = organismToGenomeBuildMap.get(d.getIdOrganism());
      if (versions == null) {
        versions = new ArrayList<GenomeBuild>();
        organismToGenomeBuildMap.put(d.getIdOrganism(), versions);
      }
      versions.add(d);
    }

    List<Lab> labs = (List<Lab>) sess.createQuery("SELECT d from Lab d order by d.lastName, d.firstName").list();
    for (Lab l : labs) {
      labMap.put(l.getIdLab(), l);
      labList.add(l);
    }


    
    queryBuf = new StringBuffer();
    queryBuf.append("SELECT au from AppUser as au ");
    List appUsers = sess.createQuery(queryBuf.toString()).list();
    for (Iterator i = appUsers.iterator(); i.hasNext();) {
      AppUser appUser = (AppUser)i.next();
      appUserMap.put(appUser.getIdAppUser(), appUser);
      
    }

   }
  
  private void loadManagedDictionaries() {
    if (!ManageDictionaries.isLoaded) {
      theInstance = null;
      throw new RuntimeException("Please run ManageDictionaries command first");
    }
    for (Iterator i = DictionaryManager.getDictionaryEntries("hci.gnomex.model.RequestCategory").iterator(); i.hasNext();) {
      Object de = i.next();
      if (de instanceof NullDictionaryEntry) {
        continue;
      }
      RequestCategory rc = (RequestCategory)de;
      requestCategoryList.add(rc);
      requestCategoryMap.put(rc.getCodeRequestCategory(), rc);
    }
    for (Iterator i = DictionaryManager.getDictionaryEntries("hci.gnomex.model.SeqRunType").iterator(); i.hasNext();) {
      Object de = i.next();
      if (de instanceof NullDictionaryEntry) {
        continue;
      }
      SeqRunType srt = (SeqRunType)de;
      seqRunTypeList.add(srt);
    }
    for (Iterator i = DictionaryManager.getDictionaryEntries("hci.gnomex.model.OligoBarcode").iterator(); i.hasNext();) {
      Object de = i.next();
      if (de instanceof NullDictionaryEntry) {
        continue;
      }
      OligoBarcode ob = (OligoBarcode)de;
      oligoBarcodeMap.put(ob.getIdOligoBarcode(), ob);
    }
    for (Iterator i = DictionaryManager.getDictionaryEntries("hci.gnomex.model.SubmissionInstruction").iterator(); i.hasNext();) {
      Object de = i.next();
      if (de instanceof NullDictionaryEntry) {
        continue;
      }
      SubmissionInstruction si = (SubmissionInstruction)de;
      submissionInstructionMap.put(si.getIdSubmissionInstruction(), si);
    }
    for (Iterator i = DictionaryManager.getDictionaryEntries("hci.gnomex.model.BillingPeriod").iterator(); i.hasNext();) {
      Object de = i.next();
      if (de instanceof NullDictionaryEntry) {
        continue;
      }
      BillingPeriod bp = (BillingPeriod)de;
      billingPeriodMap.put(bp.getIdBillingPeriod(), bp);
    }
    for (Iterator i = DictionaryManager.getDictionaryEntries("hci.gnomex.model.SeqLibTreatment").iterator(); i.hasNext();) {
      Object de = i.next();
      if (de instanceof NullDictionaryEntry) {
        continue;
      }
      SeqLibTreatment st = (SeqLibTreatment)de;
      seqLibTreatmentMap.put(st.getIdSeqLibTreatment(), st);
    }
    
    for(Iterator i = DictionaryManager.getDictionaryEntries("hci.gnomex.model.SlideDesign").iterator(); i.hasNext();) {
      Object de = i.next();
      if (de instanceof NullDictionaryEntry) {
        continue;
      }
      SlideDesign sd = (SlideDesign)de;
      slideDesignMap.put(sd.getIdSlideDesign(), sd);
    }
    
  }
  
  public Property getPropertyDictionary(Integer idProperty) {
    return (Property)propertyDictionaryMap.get(idProperty);
  }
  
  public Map getPropertyDictionaryMap() {
    return propertyDictionaryMap;
  }
  
  public String getPlateType(String codePlateType) {
    lazyLoadManagedDictionaries();
    String name = "";
    if (codePlateType != null && codePlateType.length() > 0) {
      name = DictionaryManager.getDisplay("hci.gnomex.model.PlateType", codePlateType);
    }
    return name;
  }
  public String getReactionType(String codeReactionType) {
    lazyLoadManagedDictionaries();
    String name = "";
    if (codeReactionType != null && codeReactionType.length() > 0) {
      name = DictionaryManager.getDisplay("hci.gnomex.model.ReactionType", codeReactionType);
    }
    return name;
  }
  public String getSealType(String codeSealType) {
    lazyLoadManagedDictionaries();
    String name = "";
    if (codeSealType != null && codeSealType.length() > 0) {
      name = DictionaryManager.getDisplay("hci.gnomex.model.SealType", codeSealType);
    }
    return name;
  }
  public String getInstrumentRunStatus(String codeInstrumentRunStatus) {
    lazyLoadManagedDictionaries();
    String name = "";
    if (codeInstrumentRunStatus != null && codeInstrumentRunStatus.length() > 0) {
      name = DictionaryManager.getDisplay("hci.gnomex.model.InstrumentRunStatus", codeInstrumentRunStatus);
    }
    return name;
  }
  public String getRequestStatus(String codeRequestStatus) {
    lazyLoadManagedDictionaries();
    String name = "";
    if (codeRequestStatus != null && codeRequestStatus.length() > 0) {
      name = DictionaryManager.getDisplay("hci.gnomex.model.RequestStatus", codeRequestStatus);
    }
    return name;
  }
  public String getSampleType(Sample sample) {
    lazyLoadManagedDictionaries();
    String name = "";
    if (sample.getIdSampleType() != null) {
      name = DictionaryManager.getDisplay("hci.gnomex.model.SampleType", sample.getIdSampleType().toString());
    }
    return name;
  }
  public String getOrganism(Sample sample) {
    lazyLoadManagedDictionaries();
    String name = "";
    if (sample.getIdOrganism() != null) {
      name = DictionaryManager.getDisplay("hci.gnomex.model.OrganismLite", sample.getIdOrganism().toString());
    }
    return name;
  }
  public String getOrganism(Integer idOrganism) {
    lazyLoadManagedDictionaries();
    String name = "";
    if (idOrganism != null) {
      name = DictionaryManager.getDisplay("hci.gnomex.model.OrganismLite", idOrganism.toString());
    }
    return name;
  }
  public String getSampleSource(Integer idSampleSource) {
    lazyLoadManagedDictionaries();
    String name = "";
    if (idSampleSource != null) {
      name = DictionaryManager.getDisplay("hci.gnomex.model.SampleSource", idSampleSource.toString());
    }
    return name;
  }
  public String getSampleDropOffLocation(Integer idSampleDropOffLocation) {
    lazyLoadManagedDictionaries();
    String name = "";
    if (idSampleDropOffLocation != null) {
      name = DictionaryManager.getDisplay("hci.gnomex.model.SampleDropOffLocation", idSampleDropOffLocation.toString());
    }
    return name;
  }
  public String getSampleType(Integer idSampleType) {
    lazyLoadManagedDictionaries();
    String name = "";
    if (idSampleType != null) {
      name = DictionaryManager.getDisplay("hci.gnomex.model.SampleType", idSampleType.toString());
    }
    return name;
  }
  public String getSequencingPlatform(String codeSequencingPlatform) {
    lazyLoadManagedDictionaries();
    String name = "";
    name = DictionaryManager.getDisplay("hci.gnomex.model.SequencingPlatform", codeSequencingPlatform);
    return name;
  }
  public String getSlideDesignName(Integer idSlideDesign) {
    String name = "";
    if (idSlideDesign != null) {
      SlideDesign sd = (SlideDesign)slideDesignMap.get(idSlideDesign);
      if (sd != null) {
        name = sd.getName();
      }
    }
    return name;
  }
  public String getSlideSource(String code) {
    lazyLoadManagedDictionaries();
    return DictionaryManager.getDisplay("hci.gnomex.model.SlideSource", code);
  }
  public String getSlideDesignProtocolName(Integer idSlideDesign) {
    String name = "";
    if (idSlideDesign != null) {
      SlideDesign sd = (SlideDesign)slideDesignMap.get(idSlideDesign);
      if (sd != null) {
        name = sd.getSlideDesignProtocolName();
      }
    }
    return name;
  }

  public String getChipTypeName(String codeBioanalyzerChipType) {
    lazyLoadManagedDictionaries();
    String name = "";
    if (codeBioanalyzerChipType != null && !codeBioanalyzerChipType.equals("")) {
      name = DictionaryManager.getDisplay("hci.gnomex.model.BioanalyzerChipType", codeBioanalyzerChipType);
    }
    return name;
  }
  public String getApplication(String code) {
    lazyLoadManagedDictionaries();
    String name = "";
    if (code != null) {
      name = DictionaryManager.getDisplay("hci.gnomex.model.Application", code);
    }
    return name;
  }
  
  public List getRequestCategoryList() {
    lazyLoadManagedDictionaries();
    return requestCategoryList;
  }

  public List getSeqRunTypeList() {
    lazyLoadManagedDictionaries();
    return seqRunTypeList;
  }
  
  public String getRequestCategory(String code) {
    lazyLoadManagedDictionaries();
    String name = "";
    if (code != null) {
      name = DictionaryManager.getDisplay("hci.gnomex.model.RequestCategory", code);
    }
    return name;
  }
  
  public RequestCategory getRequestCategoryObject(String code) {
    lazyLoadManagedDictionaries();
    return (RequestCategory)requestCategoryMap.get(code);
  }
  public String getSeqRunType(Integer id) {
    lazyLoadManagedDictionaries();
    String name = "";
    if (id != null) {
      name = DictionaryManager.getDisplay("hci.gnomex.model.SeqRunType", id.toString());
    }
    return name;
  }
  public String getNumberSequencingCycles(Integer id) {
    lazyLoadManagedDictionaries();
    String name = "";
    if (id != null) {
      name = DictionaryManager.getDisplay("hci.gnomex.model.NumberSequencingCycles", id.toString());
    }
    return name;
  }
  
  public String getGenomeBuild(Integer id) {
    lazyLoadManagedDictionaries();
    String name = "";
    if (id != null) {
      name = DictionaryManager.getDisplay("hci.gnomex.model.GenomeBuildLite", id.toString());
    }
    return name;
  }
  public String getLabel(Integer id) {
    lazyLoadManagedDictionaries();
    String name = "";
    if (id != null) {
      name = DictionaryManager.getDisplay("hci.gnomex.model.Label", id.toString());
    }
    return name;
  }
  public String getAnalysisType(Integer id) {
    lazyLoadManagedDictionaries();
    String name = "";
    if (id != null) {
      name = DictionaryManager.getDisplay("hci.gnomex.model.AnalysisType", id.toString());
    }
    return name;
  }
  public String getAnalysisProtocol(Integer id) {
    lazyLoadManagedDictionaries();
    String name = "";
    if (id != null) {
      name = DictionaryManager.getDisplay("hci.gnomex.model.AnalysisProtocol", id.toString());
    }
    return name;
  }
  public String getLabelingProtocol(Integer id) {
    lazyLoadManagedDictionaries();
    String name = "";
    if (id != null) {
      name = DictionaryManager.getDisplay("hci.gnomex.model.LabelingProtocol", id.toString());
    }
    return name;
  }
  public String getHybProtocol(Integer id) {
    lazyLoadManagedDictionaries();
    String name = "";
    if (id != null) {
      name = DictionaryManager.getDisplay("hci.gnomex.model.HybProtocol", id.toString());
    }
    return name;
  } 
  public String getScanProtocol(Integer id) {
    lazyLoadManagedDictionaries();
    String name = "";
    if (id != null) {
      name = DictionaryManager.getDisplay("hci.gnomex.model.ScanProtocol", id.toString());
    }
    return name;
  }  
  public String getFeatureExtractionProtocol(Integer id) {
    lazyLoadManagedDictionaries();
    String name = "";
    if (id != null) {
      name = DictionaryManager.getDisplay("hci.gnomex.model.FeatureExtractionProtocol", id.toString());
    }
    return name;
  }  
  public String getSeqLibProtocol(Integer id) {
    lazyLoadManagedDictionaries();
    String name = "";
    if (id != null) {
      name = DictionaryManager.getDisplay("hci.gnomex.model.SeqLibProtocol", id.toString());
    }
    return name;
  }   
  public SeqLibTreatment getSeqLibTreatment(Integer id) {
    lazyLoadManagedDictionaries();
    if (id != null) {
      SeqLibTreatment t = (SeqLibTreatment)seqLibTreatmentMap.get(id);
      return t;
    }
    return null;
  }   
  public Set getSeqLibTreatments() {
    lazyLoadManagedDictionaries();
    return DictionaryManager.getDictionaryEntries("hci.gnomex.model.SeqLibTreatment");
  }
  public String getBillingStatus(String codeBillingStatus) {
    lazyLoadManagedDictionaries();
    String billingStatus = "";
    if (codeBillingStatus != null) {
      billingStatus = DictionaryManager.getDisplay("hci.gnomex.model.BillingStatus", codeBillingStatus);
    }
    return billingStatus;
  }
  public String getBillingChargeKind(String codeBillingChargeKind) {
    lazyLoadManagedDictionaries();
    String billingChargeKind = "";
    if (codeBillingChargeKind != null) {
      billingChargeKind = DictionaryManager.getDisplay("hci.gnomex.model.BillingChargeKind", codeBillingChargeKind);
    }
    return billingChargeKind;
  }
  
  public BillingPeriod getBillingPeriod(Integer idBillingPeriod) {
    lazyLoadManagedDictionaries();
    BillingPeriod billingPeriod = null;
    if (idBillingPeriod != null) {
      BillingPeriod bp = (BillingPeriod)billingPeriodMap.get(idBillingPeriod);
      if (bp != null) {
        billingPeriod = bp;
      }
    }
    return billingPeriod;
  }
  
  public BillingPeriod getCurrentBillingPeriod() {
    lazyLoadManagedDictionaries();
    BillingPeriod billingPeriod = null;
    for(Iterator i = billingPeriodMap.keySet().iterator(); i.hasNext();) {
      Integer id = (Integer)i.next();
      BillingPeriod bp = (BillingPeriod)billingPeriodMap.get(id);
      if (bp.getIsCurrentPeriod().equals("Y")) {
        billingPeriod = bp;
        break;
      }
    }
    return billingPeriod;
  }
  public String getInstrument(Integer id) {
    lazyLoadManagedDictionaries();
    String name = "";
    if (id != null) {
      name = DictionaryManager.getDisplay("hci.gnomex.model.Instrument", id.toString());
    }
    return name;
  }
  

  public List<Organism> getOrganisms() {
    return this.organismList;
  }

  public List<GenomeBuild> getGenomeBuilds(Integer idOrganism) {
    return this.organismToGenomeBuildMap.get(idOrganism);
  }

  
  public GenomeBuild getGenomeBuildObject(Integer idGenomeBuild) {
    return (GenomeBuild)genomeBuildMap.get(idGenomeBuild);
  }
  public String getOrganismName(GenomeBuild genomeBuild) {
    if (genomeBuild != null && genomeBuild.getIdOrganism() != null) {
      Organism organism = organismMap.get(genomeBuild.getIdOrganism());
      if (organism != null) {
        return organism.getOrganism();
      } else {
        return "";
      }     
    } else {
      return "";
    }
  }
  public String getOrganismBinomialName(Integer idOrganism) {
    Organism organism = organismMap.get(idOrganism);
    if (organism != null) {
      return organism.getBinomialName();
    } else {
      return "";
    }
  }
  public String getOrganismBinomialName(GenomeBuild genomeBuild) {
    if (genomeBuild != null && genomeBuild.getIdOrganism() != null) {
      Organism organism = organismMap.get(genomeBuild.getIdOrganism());
      if (organism != null) {
        return organism.getBinomialName();
      } else {
        return "";
      }     
    } else {
      return "";
    }
  }
  public String getGenomeBuildName(Integer idGenomeBuild) {
    GenomeBuild genomeBuild = genomeBuildMap.get(idGenomeBuild);
    if (genomeBuild != null) {
      return genomeBuild.getGenomeBuildName();
    } else {
      return "";
    }
  }

  public AppUser getAppUserObject(Integer idAppUser) {
    return appUserMap.get(idAppUser);
  }
  
  public Lab getLabObject(Integer idLab) {
    return labMap.get(idLab);
  }

  public Map getSubmissionInstructionMap() {
    lazyLoadManagedDictionaries();
    return submissionInstructionMap;
  }

  public String getBarcodeSequence(Integer idOligoBarcode) {
    lazyLoadManagedDictionaries();
    String barcodeSequence = null;
    if (idOligoBarcode != null) {
      OligoBarcode bc = (OligoBarcode)oligoBarcodeMap.get(idOligoBarcode);
      if (bc != null) {
        barcodeSequence = bc.getBarcodeSequence();
      }
    }
    return barcodeSequence;
  }
  public List<Property> getPropertyList() {
    return propertyList;
  }
  
  public Property getPropertyObject(Integer idProperty) {
    return propertyMap.get(idProperty);
  }

  
  public String getPropertyDictionary(String name) {
    return propertyDictionaryHelper.getProperty(name);
  }
  
  public boolean isProductionServer(String serverName) {
    return propertyDictionaryHelper.isProductionServer(serverName);
  }
  
  public String getAnalysisReadDirectory(String serverName) {
	  return propertyDictionaryHelper.getAnalysisReadDirectory(serverName);
  }

  public String getAnalysisWriteDirectory(String serverName) {
	  return propertyDictionaryHelper.getAnalysisWriteDirectory(serverName);
  }
	  
  public String getFlowCellDirectory(String serverName) {
    return propertyDictionaryHelper.getFlowCellDirectory(serverName);
  }

  public String getMicroarrayDirectoryForWriting(String serverName) {
    return propertyDictionaryHelper.getMicroarrayDirectoryForWriting(serverName);
  }

  public  String getMicroarrayDirectoryForReading(String serverName) {
    return propertyDictionaryHelper.getMicroarrayDirectoryForReading(serverName);
     
  }
  
  public String getDataTrackDirectoryForWriting(String serverName) {
    return propertyDictionaryHelper.getDataTrackReadDirectory(serverName);
  }

  public  String getDataTrackDirectoryForReading(String serverName) {
    return propertyDictionaryHelper.getDataTrackWriteDirectory(serverName);
  }

  public String parseMainFolderName(String serverName, String fileName) {
    return propertyDictionaryHelper.parseMainFolderName(serverName, fileName);
  }
  
}
