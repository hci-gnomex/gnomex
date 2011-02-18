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
  
  private PropertyHelper   propertyHelper;
  private Map              requestCategoryMap = new HashMap();
  private Map              oligoBarcodeMap = new HashMap();
  private Map              submissionInstructionMap = new HashMap();
  private Map              billingPeriodMap = new HashMap();
  private Map              seqLibTreatmentMap = new HashMap();
  private Map              slideDesignMap = new HashMap();

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
    PropertyHelper.reload(sess);
    theInstance.loadDictionaries(sess);  
    return theInstance;
    
  }
  
  
  private void loadDictionaries(Session sess)  {
    if (!ManageDictionaries.isLoaded) {
      theInstance = null;
      throw new RuntimeException("Please run ManageDictionaries command first");
    }
    
    propertyHelper = PropertyHelper.getInstance(sess);
    
    for (Iterator i = DictionaryManager.getDictionaryEntries("hci.gnomex.model.RequestCategory").iterator(); i.hasNext();) {
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
  
  public String getProperty(String name) {
    return propertyHelper.getProperty(name);
  }
  
  public boolean isProductionServer(String serverName) {
    return propertyHelper.isProductionServer(serverName);
  }
  
  public String getAnalysisReadDirectory(String serverName) {
	  return propertyHelper.getAnalysisReadDirectory(serverName);
  }

  public String getAnalysisWriteDirectory(String serverName) {
	  return propertyHelper.getAnalysisWriteDirectory(serverName);
  }
	  
  public String getFlowCellDirectory(String serverName) {
    return propertyHelper.getFlowCellDirectory(serverName);
  }

  public String getMicroarrayDirectoryForWriting(String serverName) {
    return propertyHelper.getMicroarrayDirectoryForWriting(serverName);
  }

  public  String getMicroarrayDirectoryForReading(String serverName) {
    return propertyHelper.getMicroarrayDirectoryForReading(serverName);
     
  }
  public String parseMainFolderName(String serverName, String fileName) {
    return propertyHelper.parseMainFolderName(serverName, fileName);
  }
  
}
