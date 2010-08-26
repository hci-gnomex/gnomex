package hci.gnomex.utility;

import hci.dictionary.model.NullDictionaryEntry;
import hci.dictionary.utility.DictionaryManager;
import hci.gnomex.controller.ManageDictionaries;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.OligoBarcode;
import hci.gnomex.model.Property;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.model.Sample;
import hci.gnomex.model.SamplePrepMethodSampleType;
import hci.gnomex.model.SeqLibTreatment;
import hci.gnomex.model.SlideDesign;
import hci.gnomex.model.SubmissionInstruction;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;



public class DictionaryHelper implements Serializable {
  private static DictionaryHelper theInstance;
  
  private Map              propertyMap = new HashMap();
  private Map              requestCategoryMap = new HashMap();
  private Map              oligoBarcodeMap = new HashMap();
  private Map              submissionInstructionMap = new HashMap();
  private Map              billingPeriodMap = new HashMap();
  private Map              seqLibTreatmentMap = new HashMap();
  private Map              slideDesignMap = new HashMap();

  private static final String    PROPERTY_PRODUCTION_SERVER                   = "production_server";
  
  private static final String    PROPERTY_EXPERIMENT_DIRECTORY                = "experiment_directory";
  private static final String    PROPERTY_EXPERIMENT_TEST_DIRECTORY           = "experiment_test_directory";
  private static final String    PROPERTY_ANALYSIS_DIRECTORY                  = "analysis_directory";
  private static final String    PROPERTY_ANALYSIS_TEST_DIRECTORY             = "analysis_test_directory";
  private static final String    PROPERTY_FLOWCELL_DIRECTORY                  = "flowcell_directory";
  private static final String    PROPERTY_FLOWCELL_TEST_DIRECTORY             = "flowcell_test_directory";

  
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
  
  
  private void loadDictionaries(Session sess)  {
    if (!ManageDictionaries.isLoaded) {
      theInstance = null;
      throw new RuntimeException("Please run ManagerDictionaries command first");
    }
    for (Iterator i = DictionaryManager.getDictionaryEntries("hci.gnomex.model.Property").iterator(); i.hasNext();) {
      Object de = i.next();
      if (de instanceof NullDictionaryEntry) {
        continue;
      }
      Property prop = (Property)de;
      propertyMap.put(prop.getPropertyName(), prop.getPropertyValue());
    }    for (Iterator i = DictionaryManager.getDictionaryEntries("hci.gnomex.model.RequestCategory").iterator(); i.hasNext();) {
      Object de = i.next();
      if (de instanceof NullDictionaryEntry) {
        continue;
      }
      RequestCategory rc = (RequestCategory)de;
      requestCategoryMap.put(rc.getCodeRequestCategory(), rc);
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
  
  
  public String getSampleType(Sample sample) {
    String name = "";
    if (sample.getIdSampleType() != null) {
      name = DictionaryManager.getDisplay("hci.gnomex.model.SampleType", sample.getIdSampleType().toString());
    }
    return name;
  }

  public String getSamplePrepMethod(Sample sample) {
    String name = "";
    if (sample.getIdSamplePrepMethod() != null) {
      name = DictionaryManager.getDisplay("hci.gnomex.model.SamplePrepMethod", sample.getIdSamplePrepMethod().toString());
    }
    return name;
  }
  public String getOrganism(Sample sample) {
    String name = "";
    if (sample.getIdOrganism() != null) {
      name = DictionaryManager.getDisplay("hci.gnomex.model.Organism", sample.getIdOrganism().toString());
    }
    return name;
  }
  public String getOrganism(Integer idOrganism) {
    String name = "";
    if (idOrganism != null) {
      name = DictionaryManager.getDisplay("hci.gnomex.model.Organism", idOrganism.toString());
    }
    return name;
  }
  public String getSampleSource(Integer idSampleSource) {
    String name = "";
    if (idSampleSource != null) {
      name = DictionaryManager.getDisplay("hci.gnomex.model.SampleSource", idSampleSource.toString());
    }
    return name;
  }
  public String getSampleType(Integer idSampleType) {
    String name = "";
    if (idSampleType != null) {
      name = DictionaryManager.getDisplay("hci.gnomex.model.SampleType", idSampleType.toString());
    }
    return name;
  }
  public String getSequencingPlatform(String codeSequencingPlatform) {
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
    String name = "";
    if (codeBioanalyzerChipType != null && !codeBioanalyzerChipType.equals("")) {
      name = DictionaryManager.getDisplay("hci.gnomex.model.BioanalyzerChipType", codeBioanalyzerChipType);
    }
    return name;
  }
  public String getApplication(String code) {
    String name = "";
    if (code != null) {
      name = DictionaryManager.getDisplay("hci.gnomex.model.Application", code);
    }
    return name;
  }
  public String getRequestCategory(String code) {
    String name = "";
    if (code != null) {
      name = DictionaryManager.getDisplay("hci.gnomex.model.RequestCategory", code);
    }
    return name;
  }
  
  public RequestCategory getRequestCategoryObject(String code) {
    return (RequestCategory)requestCategoryMap.get(code);
  }
  public String getSeqRunType(Integer id) {
    String name = "";
    if (id != null) {
      name = DictionaryManager.getDisplay("hci.gnomex.model.SeqRunType", id.toString());
    }
    return name;
  }
  public String getNumberSequencingCycles(Integer id) {
    String name = "";
    if (id != null) {
      name = DictionaryManager.getDisplay("hci.gnomex.model.NumberSequencingCycles", id.toString());
    }
    return name;
  }
  
  public String getGenomeBuild(Integer id) {
    String name = "";
    if (id != null) {
      name = DictionaryManager.getDisplay("hci.gnomex.model.GenomeBuild", id.toString());
    }
    return name;
  }
  public String getLabel(Integer id) {
    String name = "";
    if (id != null) {
      name = DictionaryManager.getDisplay("hci.gnomex.model.Label", id.toString());
    }
    return name;
  }
  public String getAnalysisType(Integer id) {
    String name = "";
    if (id != null) {
      name = DictionaryManager.getDisplay("hci.gnomex.model.AnalysisType", id.toString());
    }
    return name;
  }
  public String getAnalysisProtocol(Integer id) {
    String name = "";
    if (id != null) {
      name = DictionaryManager.getDisplay("hci.gnomex.model.AnalysisProtocol", id.toString());
    }
    return name;
  }
  public String getLabelingProtocol(Integer id) {
    String name = "";
    if (id != null) {
      name = DictionaryManager.getDisplay("hci.gnomex.model.LabelingProtocol", id.toString());
    }
    return name;
  }
  public String getHybProtocol(Integer id) {
    String name = "";
    if (id != null) {
      name = DictionaryManager.getDisplay("hci.gnomex.model.HybProtocol", id.toString());
    }
    return name;
  } 
  public String getScanProtocol(Integer id) {
    String name = "";
    if (id != null) {
      name = DictionaryManager.getDisplay("hci.gnomex.model.ScanProtocol", id.toString());
    }
    return name;
  }  
  public String getFeatureExtractionProtocol(Integer id) {
    String name = "";
    if (id != null) {
      name = DictionaryManager.getDisplay("hci.gnomex.model.FeatureExtractionProtocol", id.toString());
    }
    return name;
  }  
  public String getSeqLibProtocol(Integer id) {
    String name = "";
    if (id != null) {
      name = DictionaryManager.getDisplay("hci.gnomex.model.SeqLibProtocol", id.toString());
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
  public Set getSeqLibTreatments() {
    return DictionaryManager.getDictionaryEntries("hci.gnomex.model.SeqLibTreatment");
  }
  public String getBillingStatus(String codeBillingStatus) {
    String billingStatus = "";
    if (codeBillingStatus != null) {
      billingStatus = DictionaryManager.getDisplay("hci.gnomex.model.BillingStatus", codeBillingStatus);
    }
    return billingStatus;
  }
  public String getBillingChargeKind(String codeBillingChargeKind) {
    String billingChargeKind = "";
    if (codeBillingChargeKind != null) {
      billingChargeKind = DictionaryManager.getDisplay("hci.gnomex.model.BillingChargeKind", codeBillingChargeKind);
    }
    return billingChargeKind;
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


  public Map getSubmissionInstructionMap() {
    return submissionInstructionMap;
  }


  public String getProperty(String name) {
    String propertyValue = "";
    if (name != null && !name.equals("")) {
      return (String)propertyMap.get(name);
    } else {
      return "";
    }
  }
  
  public boolean isProductionServer(String serverName) {
    if (this.getProperty(PROPERTY_PRODUCTION_SERVER) != null &&
        this.getProperty(PROPERTY_PRODUCTION_SERVER).contains(serverName)) {
      return true;
    } else {
      return false;
    }
  }
  
  public String getAnalysisDirectory(String serverName) {
    String property = "";
    String propertyName = null;
    
    // If this is the production server, first try to get property that is 
    // qualified by server name.  If that isn't found, get the property without
    // any qualification.
    // If this is not the production server, get the property for the analysis
    // test path.  First use the property qualified by server name.  If
    // it isn't found, get the property without any qualification.   
    if (isProductionServer(serverName)) {
      propertyName = PROPERTY_ANALYSIS_DIRECTORY + "_" + serverName;
      property = this.getProperty(propertyName);
      if (property == null || property.equals("")) {  
        propertyName = PROPERTY_ANALYSIS_DIRECTORY;
        property = this.getProperty(propertyName);
      }
    } else {
      propertyName = PROPERTY_ANALYSIS_TEST_DIRECTORY + "_" + serverName;
      property = this.getProperty(propertyName);
      if (property == null || property.equals("")) {  
        propertyName = PROPERTY_ANALYSIS_TEST_DIRECTORY;
        property = this.getProperty(propertyName);
      }
    }
    
    return property;
  }
  
  public String getFlowCellDirectory(String serverName) {
    String property = "";
    String propertyName = null;
    
    // If this is the production server, first try to get property that is 
    // qualified by server name.  If that isn't found, get the property without
    // any qualification.
    // If this is not the production server, get the property for the flowcell
    // test path.  First use the property qualified by server name.  If
    // it isn't found, get the property without any qualification.   
    if (isProductionServer(serverName)) {
      propertyName = PROPERTY_FLOWCELL_DIRECTORY + "_" + serverName;
      property = this.getProperty(propertyName);
      if (property == null || property.equals("")) {  
        propertyName = PROPERTY_FLOWCELL_DIRECTORY;
        property = this.getProperty(propertyName);
      }
    } else {
      propertyName = PROPERTY_FLOWCELL_TEST_DIRECTORY + "_" + serverName;
      property = this.getProperty(propertyName);
      if (property == null || property.equals("")) {  
        propertyName = PROPERTY_FLOWCELL_TEST_DIRECTORY;
        property = this.getProperty(propertyName);
      }
    }
    
    return property;
  }

  public String getMicroarrayDirectoryForWriting(String serverName) {
    String property = "";
    String propertyName = null;
    
    // If this is the production server, first try to get property that is 
    // qualified by server name.  If that isn't found, get the property without
    // any qualification.
    // If this is not the production server, get the property for the experiment
    // test path.  First use the property qualified by server name.  If
    // it isn't found, get the property without any qualification.   
    if (isProductionServer(serverName)) {
      propertyName = PROPERTY_EXPERIMENT_DIRECTORY + "_" + serverName;
      property = this.getProperty(propertyName);
      if (property == null || property.equals("")) {  
        propertyName = PROPERTY_EXPERIMENT_DIRECTORY;
        property = this.getProperty(propertyName);
      }
    } else {
      propertyName = PROPERTY_EXPERIMENT_TEST_DIRECTORY + "_" + serverName;
      property = this.getProperty(propertyName);
      if (property == null || property.equals("")) {  
        propertyName = PROPERTY_EXPERIMENT_TEST_DIRECTORY;
        property = this.getProperty(propertyName);
      }
    }
    
    return property;
  }

  public  String getMicroarrayDirectoryForReading(String serverName) {
    // First try to get property that is 
    // qualified by server name.  If that isn't found, get the property without
    // any qualification.
    String property = "";
    String propertyName = PROPERTY_EXPERIMENT_DIRECTORY + "_" + serverName;
    property = this.getProperty(propertyName);
    if (property == null || property.equals("")) {  
      propertyName = PROPERTY_EXPERIMENT_DIRECTORY;
      property = this.getProperty(propertyName);
    }
    return property;
     
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
  
 
  
  public String parseMainFolderName(String serverName, String fileName) {
    String mainFolderName = "";
    String baseDir = "";
    
    String experimentDirectory = this.getMicroarrayDirectoryForReading(serverName);
    String flowCellDirectory   = this.getFlowCellDirectory(serverName);
    
    if (fileName.indexOf(experimentDirectory) >= 0) {
      baseDir = experimentDirectory;
    } else if (fileName.indexOf(flowCellDirectory) >= 0) {
      baseDir = flowCellDirectory;
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
