package hci.gnomex.utility;

import hci.gnomex.model.AnalysisProtocol;
import hci.gnomex.model.AnalysisType;
import hci.gnomex.model.Application;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.BillingStatus;
import hci.gnomex.model.BioanalyzerChipType;
import hci.gnomex.model.FeatureExtractionProtocol;
import hci.gnomex.model.GenomeBuild;
import hci.gnomex.model.HybProtocol;
import hci.gnomex.model.Label;
import hci.gnomex.model.LabelingProtocol;
import hci.gnomex.model.NumberSequencingCycles;
import hci.gnomex.model.OligoBarcode;
import hci.gnomex.model.Organism;
import hci.gnomex.model.Property;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.model.Sample;
import hci.gnomex.model.SampleCharacteristic;
import hci.gnomex.model.SamplePrepMethod;
import hci.gnomex.model.SamplePrepMethodSampleType;
import hci.gnomex.model.SampleSource;
import hci.gnomex.model.SampleType;
import hci.gnomex.model.ScanProtocol;
import hci.gnomex.model.SeqLibProtocol;
import hci.gnomex.model.SeqLibTreatment;
import hci.gnomex.model.SeqRunType;
import hci.gnomex.model.SlideDesign;
import hci.gnomex.model.SlideProduct;
import hci.gnomex.model.SlideSource;
import hci.gnomex.model.SubmissionInstruction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;



public class DictionaryHelper implements Serializable {
  private static DictionaryHelper theInstance;
  
  private Map              sampleTypeMap = new HashMap();
  private Map              samplePrepMethodMap = new HashMap();
  private Map              sampleCharacteristicMap = new HashMap();
  private Map              slideDesignMap = new HashMap();
  private Map              slideProductMap = new HashMap();
  private Map              chipMap = new HashMap();
  private Map              applicationMap = new HashMap();
  private Map              requestCategoryMap = new HashMap();
  private Map              slideSourceMap = new HashMap();
  private Map              sampleSourceMap = new HashMap();
  private Map              organismMap = new HashMap();
  private Map              seqRunTypeMap = new HashMap();
  private Map              numberSequencingCyclesMap = new HashMap();
  private Map              genomeBuildMap = new HashMap();  
  private Map              analysisProtocolMap = new HashMap();
  private Map              analysisTypeMap = new HashMap();
  private Map              billingStatusMap = new HashMap();
  private Map              billingPeriodMap = new HashMap();
  private Map              sampleTypeToDefaultSamplePrepMethodMap = new HashMap();
  private Map              labelMap = new HashMap();
  private Map              propertyMap = new HashMap();
  private Map              submissionInstructionMap = new HashMap();
  private Map              oligoBarcodeMap = new HashMap();
  private Map              labelingProtocolMap = new HashMap();
  private Map              hybProtocolMap = new HashMap();
  private Map              scanProtocolMap = new HashMap();
  private Map              featureExtractionProtocolMap = new HashMap();
  private Map              seqLibProtocolMap = new HashMap();
  private List             seqLibTreatmentList = new ArrayList();
  private Map              seqLibTreatmentMap = new HashMap();
  

  
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
    theInstance.loadDictionaries(sess);  
    return theInstance;
    
  }
  
  
  private void loadDictionaries(Session sess) {
    List sampleTypes = sess.createQuery("SELECT st from SampleType as st").list();
    for(Iterator i = sampleTypes.iterator(); i.hasNext();) {
      SampleType st = (SampleType)i.next();
      sampleTypeMap.put(st.getIdSampleType(), st.getSampleType());
    }
    
    List samplePrepMethods = sess.createQuery("SELECT spm from SamplePrepMethod as spm").list();
    for(Iterator i = samplePrepMethods.iterator(); i.hasNext();) {
      SamplePrepMethod spm = (SamplePrepMethod)i.next();
      samplePrepMethodMap.put(spm.getIdSamplePrepMethod(), spm.getSamplePrepMethod());
    }
    List sampleCharacteristics = sess.createQuery("SELECT sc from SampleCharacteristic as sc").list();
    for(Iterator i = sampleCharacteristics.iterator(); i.hasNext();) {
      SampleCharacteristic sc = (SampleCharacteristic)i.next();
      sampleCharacteristicMap.put(sc.getCodeSampleCharacteristic(), sc.getSampleCharacteristic());
    }
    List slideDesigns = sess.createQuery("SELECT sd from SlideDesign as sd").list();
    for(Iterator i = slideDesigns.iterator(); i.hasNext();) {
      SlideDesign sd = (SlideDesign)i.next();
      slideDesignMap.put(sd.getIdSlideDesign(), sd);
    }
    List slideProducts = sess.createQuery("SELECT sp from SlideProduct as sp").list();
    for(Iterator i = slideProducts.iterator(); i.hasNext();) {
      SlideProduct sp = (SlideProduct)i.next();
      slideProductMap.put(sp.getIdSlideProduct(), sp);
    }
    List chipTypes = sess.createQuery("SELECT ct from BioanalyzerChipType as ct").list();
    for(Iterator i = chipTypes.iterator(); i.hasNext();) {
      BioanalyzerChipType ct = (BioanalyzerChipType)i.next();
      chipMap.put(ct.getCodeBioanalyzerChipType(), ct.getBioanalyzerChipType());
    }
    List microarrayCategories = sess.createQuery("SELECT mc from Application as mc").list();
    for(Iterator i = microarrayCategories.iterator(); i.hasNext();) {
      Application mc = (Application)i.next();
      applicationMap.put(mc.getCodeApplication(), mc.getApplication());
    }
    List requestCategories = sess.createQuery("SELECT mc from RequestCategory as mc").list();
    for(Iterator i = requestCategories.iterator(); i.hasNext();) {
      RequestCategory mc = (RequestCategory)i.next();
      requestCategoryMap.put(mc.getCodeRequestCategory(), mc);
    }
    List slideSources = sess.createQuery("SELECT su from SlideSource as su").list();
    for(Iterator i = slideSources.iterator(); i.hasNext();) {
      SlideSource su = (SlideSource)i.next();
      slideSourceMap.put(su.getCodeSlideSource(), su.getSlideSource());
    }
    List sampleSources = sess.createQuery("SELECT src from SampleSource as src").list();
    for(Iterator i = sampleSources.iterator(); i.hasNext();) {
      SampleSource src = (SampleSource)i.next();
      sampleSourceMap.put(src.getIdSampleSource(), src.getSampleSource());
    }
    List organisms = sess.createQuery("SELECT org from Organism as org").list();
    for(Iterator i = organisms.iterator(); i.hasNext();) {
      Organism org = (Organism)i.next();
      organismMap.put(org.getIdOrganism(), org.getOrganism());
    }
    List flowCells = sess.createQuery("SELECT fc from SeqRunType as fc").list();
    for(Iterator i = flowCells.iterator(); i.hasNext();) {
      SeqRunType fc = (SeqRunType)i.next();
      seqRunTypeMap.put(fc.getIdSeqRunType(), fc.getSeqRunType());
    }    
    List numberSequencingCycles = sess.createQuery("SELECT sc from NumberSequencingCycles as sc").list();
    for(Iterator i = numberSequencingCycles.iterator(); i.hasNext();) {
      NumberSequencingCycles sc = (NumberSequencingCycles)i.next();
      this.numberSequencingCyclesMap.put(sc.getIdNumberSequencingCycles(), sc.getNumberSequencingCycles());
    }    
    List genomeBuilds = sess.createQuery("SELECT gb from GenomeBuild as gb").list();
    for(Iterator i = genomeBuilds.iterator(); i.hasNext();) {
      GenomeBuild gb = (GenomeBuild)i.next();
      genomeBuildMap.put(gb.getIdGenomeBuild(), gb.getGenomeBuildName());
    }    
    List analysisTypes = sess.createQuery("SELECT at from AnalysisType as at").list();
    for(Iterator i = analysisTypes.iterator(); i.hasNext();) {
      AnalysisType at = (AnalysisType)i.next();
      analysisTypeMap.put(at.getIdAnalysisType(), at.getAnalysisType());
    }  
    List analysisProtocols = sess.createQuery("SELECT at from AnalysisProtocol as at").list();
    for(Iterator i = analysisProtocols.iterator(); i.hasNext();) {
      AnalysisProtocol at = (AnalysisProtocol)i.next();
      analysisProtocolMap.put(at.getIdAnalysisProtocol(), at.getAnalysisProtocol());
    }  
    List billingStatusList = sess.createQuery("SELECT st from BillingStatus as st").list();
    for(Iterator i = billingStatusList.iterator(); i.hasNext();) {
      BillingStatus st = (BillingStatus)i.next();
      billingStatusMap.put(st.getCodeBillingStatus(), st.getBillingStatus());
    } 
    List billingPeriodList = sess.createQuery("SELECT bp from BillingPeriod as bp").list();
    for(Iterator i = billingPeriodList.iterator(); i.hasNext();) {
      BillingPeriod bp = (BillingPeriod)i.next();
      billingPeriodMap.put(bp.getIdBillingPeriod(), bp);
    }      
    List samplePrepMethodSampleTypes = sess.createQuery("SELECT x from SamplePrepMethodSampleType as x").list();
    for(Iterator i = samplePrepMethodSampleTypes.iterator(); i.hasNext();) {
      SamplePrepMethodSampleType x = (SamplePrepMethodSampleType)i.next();
      SamplePrepMethodSampleType current = (SamplePrepMethodSampleType)sampleTypeToDefaultSamplePrepMethodMap.get(x.getIdSampleType());
      if (current == null || current.getIsDefaultForSampleType() == null || !current.getIsDefaultForSampleType().equals("Y")) {
        sampleTypeToDefaultSamplePrepMethodMap.put(x.getIdSampleType(), x);        
      }
    }
    List labels = sess.createQuery("SELECT l from Label as l").list();
    for(Iterator i = labels.iterator(); i.hasNext();) {
      Label l = (Label)i.next();
      labelMap.put(l.getIdLabel(), l.getLabel());
    } 
    List props = sess.createQuery("SELECT p from Property as p").list();
    for(Iterator i = props.iterator(); i.hasNext();) {
      Property p = (Property)i.next();
      propertyMap.put(p.getPropertyName(), p.getPropertyValue());
    }
    List instructions = sess.createQuery("SELECT i from SubmissionInstruction as i").list();
    for(Iterator i = instructions.iterator(); i.hasNext();) {
      SubmissionInstruction instruction = (SubmissionInstruction)i.next();
      submissionInstructionMap.put(instruction.getIdSubmissionInstruction(), instruction);
    }
    List oligoBarcodes = sess.createQuery("SELECT b from OligoBarcode as b").list();
    for(Iterator i = oligoBarcodes.iterator(); i.hasNext();) {
      OligoBarcode bc = (OligoBarcode)i.next();
      oligoBarcodeMap.put(bc.getIdOligoBarcode(), bc);
    }
    List labelingProtocols = sess.createQuery("SELECT p from LabelingProtocol as p").list();
    for(Iterator i = labelingProtocols.iterator(); i.hasNext();) {
      LabelingProtocol p = (LabelingProtocol)i.next();
      labelingProtocolMap.put(p.getIdLabelingProtocol(), p);
    }
    List hybProtocols = sess.createQuery("SELECT p from HybProtocol as p").list();
    for(Iterator i = hybProtocols.iterator(); i.hasNext();) {
      HybProtocol p = (HybProtocol)i.next();
      hybProtocolMap.put(p.getIdHybProtocol(), p);
    }
    List scanProtocols = sess.createQuery("SELECT p from ScanProtocol as p").list();
    for(Iterator i = scanProtocols.iterator(); i.hasNext();) {
      ScanProtocol p = (ScanProtocol)i.next();
      scanProtocolMap.put(p.getIdScanProtocol(), p);
    }    
    List featureExtractionProtocols = sess.createQuery("SELECT p from FeatureExtractionProtocol as p").list();
    for(Iterator i = featureExtractionProtocols.iterator(); i.hasNext();) {
      FeatureExtractionProtocol p = (FeatureExtractionProtocol)i.next();
      featureExtractionProtocolMap.put(p.getIdFeatureExtractionProtocol(), p);
    }    
    List seqLibProtocols = sess.createQuery("SELECT p from SeqLibProtocol as p").list();
    for(Iterator i = seqLibProtocols.iterator(); i.hasNext();) {
      SeqLibProtocol p = (SeqLibProtocol)i.next();
      seqLibProtocolMap.put(p.getIdSeqLibProtocol(), p);
    }
    List seqLibTreatments = sess.createQuery("SELECT p from SeqLibTreatment as p").list();
    for(Iterator i = seqLibTreatments.iterator(); i.hasNext();) {
      SeqLibTreatment t = (SeqLibTreatment)i.next();
      seqLibTreatmentMap.put(t.getIdSeqLibTreatment(), t);
      seqLibTreatmentList.add(t);
    }      
   }
  
  
  public String getSampleType(Sample sample) {
    String name = "";
    if (sample.getIdSampleType() != null) {
      name = (String)sampleTypeMap.get(sample.getIdSampleType());
    }
    return name;
  }

  public String getSamplePrepMethod(Sample sample) {
    String name = "";
    if (sample.getIdSamplePrepMethod() != null) {
      name = (String)samplePrepMethodMap.get(sample.getIdSamplePrepMethod());
    }
    return name;
  }
  public String getOrganism(Sample sample) {
    String name = "";
    if (sample.getIdOrganism() != null) {
      name = (String)organismMap.get(sample.getIdOrganism());
    }
    return name;
  }
  public String getOrganism(Integer idOrganism) {
    String name = "";
    if (idOrganism != null) {
      name = (String)organismMap.get(idOrganism);
    }
    return name;
  }
  public String getSampleSource(Integer idSampleSource) {
    String name = "";
    if (idSampleSource != null) {
      name = (String)sampleSourceMap.get(idSampleSource);
    }
    return name;
  }
  public String getSampleType(Integer idSampleType) {
    String name = "";
    if (idSampleType != null) {
      name = (String)sampleTypeMap.get(idSampleType);
    }
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
    return (String)slideSourceMap.get(code);
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
  public String getSlideProductName(Integer idSlideProduct) {
    String name = "";
    if (idSlideProduct != null && slideProductMap.containsKey(idSlideProduct)) {
      name = ((SlideProduct)slideProductMap.get(idSlideProduct)).getName();
    }
    return name;
  }
  public Integer getSlidesInSet(Integer idSlideProduct) {
    Integer slidesInSet = new Integer("0");
    if (idSlideProduct != null) {
      slidesInSet = ((SlideProduct)slideProductMap.get(idSlideProduct)).getSlidesInSet();
    }
    return slidesInSet;
  }
  public String getChipTypeName(String codeBioanalyzerChipType) {
    String name = "";
    if (codeBioanalyzerChipType != null && !codeBioanalyzerChipType.equals("")) {
      name = (String)chipMap.get(codeBioanalyzerChipType);
    }
    return name;
  }
  public String getApplication(String code) {
    String name = "";
    if (code != null) {
      name = (String)applicationMap.get(code);
    }
    return name;
  }
  public String getRequestCategory(String code) {
    String name = "";
    if (code != null && requestCategoryMap.containsKey(code)) {
      name = ((RequestCategory)requestCategoryMap.get(code)).getRequestCategory();
    }
    return name;
  }
  public RequestCategory getRequestCategoryObject(String code) {
    return (RequestCategory)requestCategoryMap.get(code);
  }
  public String getSeqRunType(Integer id) {
    String name = "";
    if (id != null) {
      name = (String)seqRunTypeMap.get(id);
    }
    return name;
  }
  public String getNumberSequencingCycles(Integer id) {
    String name = "";
    if (id != null) {
      name = (String)numberSequencingCyclesMap.get(id).toString();
    }
    return name;
  }
  
  public String getGenomeBuild(Integer id) {
    String name = "";
    if (id != null) {
      name = (String)genomeBuildMap.get(id);
    }
    return name;
  }
  public String getLabel(Integer id) {
    String name = "";
    if (id != null) {
      name = (String)labelMap.get(id);
    }
    return name;
  }
  public String getAnalysisType(Integer id) {
    String name = "";
    if (id != null) {
      name = (String)analysisTypeMap.get(id);
    }
    return name;
  }
  public String getAnalysisProtocol(Integer id) {
    String name = "";
    if (id != null) {
      name = (String)analysisProtocolMap.get(id);
    }
    return name;
  }
  public String getLabelingProtocol(Integer id) {
    String name = "";
    if (id != null) {
      LabelingProtocol lp = (LabelingProtocol)labelingProtocolMap.get(id);
      return lp.getLabelingProtocol();
    }
    return name;
  }
  public String getHybProtocol(Integer id) {
    String name = "";
    if (id != null) {
      HybProtocol p = (HybProtocol)hybProtocolMap.get(id);
      return p.getHybProtocol();
    }
    return name;
  } 
  public String getScanProtocol(Integer id) {
    String name = "";
    if (id != null) {
      ScanProtocol p = (ScanProtocol)scanProtocolMap.get(id);
      return p.getScanProtocol();
    }
    return name;
  }  
  public String getFeatureExtractionProtocol(Integer id) {
    String name = "";
    if (id != null) {
      FeatureExtractionProtocol p = (FeatureExtractionProtocol)featureExtractionProtocolMap.get(id);
      return p.getFeatureExtractionProtocol();
    }
    return name;
  }  
  public String getSeqLibProtocol(Integer id) {
    String name = "";
    if (id != null) {
      SeqLibProtocol p = (SeqLibProtocol)seqLibProtocolMap.get(id);
      return p.getSeqLibProtocol();
    }
    return name;
  }   
  public SeqLibTreatment getSeqLibTreatment(Integer id) {
    if (id != null) {
      SeqLibTreatment t = (SeqLibTreatment)seqLibTreatmentMap.get(id);
      return t;
    }
    return null;
  }   
  public List getSeqLibTreatments() {
    return seqLibTreatmentList;
  }
  
  public String getBillingStatus(String codeBillingStatus) {
    String billingStatus = "";
    if (codeBillingStatus != null) {
      billingStatus = (String)billingStatusMap.get(codeBillingStatus);
    }
    return billingStatus;
  }
  
  public BillingPeriod getBillingPeriod(Integer idBillingPeriod) {
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
  
  public Integer getDefaultIdSamplePrepMethod(Integer idSampleType) {
    SamplePrepMethodSampleType x= (SamplePrepMethodSampleType)this.sampleTypeToDefaultSamplePrepMethodMap.get(idSampleType);
    if (x != null) {
      return x.getIdSamplePrepMethod();
    } else {
      return null;
    }
  }
  
  public Map getSubmissionInstructionMap() {
    return submissionInstructionMap;
  }
  
  public Map getChipMap() {
    return chipMap;
  }


  
  public Map getSampleCharacteristicMap() {
    return sampleCharacteristicMap;
  }


  
  public Map getSamplePrepMethodMap() {
    return samplePrepMethodMap;
  }


  
  public Map getSampleTypeMap() {
    return sampleTypeMap;
  }


  
  public Map getSlideDesignMap() {
    return slideDesignMap;
  }


  
  public Map getSlideProductMap() {
    return slideProductMap;
  }
  

  public String getProperty(String name) {
    if (propertyMap != null && propertyMap.containsKey(name)) {
      return (String)propertyMap.get(name);
    } else {
      return "";
    }
  }
  
  public String getAnalysisDirectory(String serverName) {
    if (serverName.equals(this.getProperty(Property.PRODUCTION_SERVER))) {
      return this.getProperty(Property.ANALYSIS_DIRECTORY);
    } else {
      return this.getProperty(Property.ANALYSIS_TEST_DIRECTORY);
    }
  }
  
  public String getFlowCellDirectory(String serverName) {
    if (serverName.equals(this.getProperty(Property.PRODUCTION_SERVER))) {
      return this.getProperty(Property.FLOWCELL_DIRECTORY);
    } else {
      return this.getProperty(Property.FLOWCELL_TEST_DIRECTORY);
    }
  }

  public String getMicroarrayDirectoryForWriting(String serverName) {
    if (serverName.equals(this.getProperty(Property.PRODUCTION_SERVER))) {
      return this.getProperty(Property.EXPERIMENT_DIRECTORY);
    } else {
      return this.getProperty(Property.EXPERIMENT_TEST_DIRECTORY);
    }
  }

  public  String getMicroarrayDirectoryForReading(String serverName) {
    return getProperty(Property.EXPERIMENT_DIRECTORY);
  }
  
  public  int getAnalysisDirectoryNameLength() {
    return getProperty(Property.ANALYSIS_DIRECTORY).length();
  }
  public  int getMicroarrayDirectoryNameLength() {
    return getProperty(Property.EXPERIMENT_DIRECTORY).length();
  }
  public  int getFlowCellDirectryNameLength() {
    return getProperty(Property.FLOWCELL_DIRECTORY).length();
  }
  
  public String getBarcodeSequence(Integer idOligoBarcode) {
    String barcodeSequence = null;
    if (idOligoBarcode != null) {
      OligoBarcode bc = (OligoBarcode)oligoBarcodeMap.get(idOligoBarcode);
      if (bc != null) {
        barcodeSequence = bc.getBarcodeSequence();
      }
    }
    return barcodeSequence;
  }
  
 
  
  public String parseMainFolderName(String fileName) {
    String mainFolderName = "";
    String baseDir = "";
    
    if (fileName.indexOf(this.getProperty(Property.EXPERIMENT_DIRECTORY)) >= 0) {
      baseDir = this.getProperty(Property.EXPERIMENT_DIRECTORY);
    } else if (fileName.indexOf(getProperty(Property.EXPERIMENT_TEST_DIRECTORY)) >= 0) {
      baseDir = getProperty(Property.EXPERIMENT_TEST_DIRECTORY);
    } else if (fileName.indexOf(getProperty(Property.FLOWCELL_DIRECTORY)) >= 0) {
      baseDir = getProperty(Property.FLOWCELL_DIRECTORY);
    } else if (fileName.indexOf(getProperty(Property.FLOWCELL_TEST_DIRECTORY)) >= 0) {
      baseDir = getProperty(Property.FLOWCELL_TEST_DIRECTORY);
    }

  
    
    String relativePath = fileName.substring(baseDir.length() + 5);
    String tokens[] = relativePath.split("/", 2);
    if (tokens == null || tokens.length == 1) {
      tokens = relativePath.split("\\\\", 2);
    }
    if (tokens.length == 2) {
      mainFolderName = tokens[0];
    }
    
    return mainFolderName;
  }
  
  
}
