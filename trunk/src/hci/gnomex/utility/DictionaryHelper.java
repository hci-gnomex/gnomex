package hci.gnomex.utility;

import hci.gnomex.model.AnalysisProtocol;
import hci.gnomex.model.AnalysisType;
import hci.gnomex.model.BioanalyzerChipType;
import hci.gnomex.model.FlowCellType;
import hci.gnomex.model.GenomeBuild;
import hci.gnomex.model.MicroarrayCategory;
import hci.gnomex.model.NumberSequencingCycles;
import hci.gnomex.model.Organism;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.model.Sample;
import hci.gnomex.model.SampleCharacteristic;
import hci.gnomex.model.SamplePrepMethod;
import hci.gnomex.model.SampleSource;
import hci.gnomex.model.SampleType;
import hci.gnomex.model.SlideDesign;
import hci.gnomex.model.SlideProduct;
import hci.gnomex.model.SlideSource;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;


public class DictionaryHelper implements Serializable {
  private Map              sampleTypeMap = new HashMap();
  private Map              samplePrepMethodMap = new HashMap();
  private Map              sampleCharacteristicMap = new HashMap();
  private Map              slideDesignMap = new HashMap();
  private Map              slideProductMap = new HashMap();
  private Map              chipMap = new HashMap();
  private Map              microarrayCategoryMap = new HashMap();
  private Map              requestCategoryMap = new HashMap();
  private Map              slideSourceMap = new HashMap();
  private Map              sampleSourceMap = new HashMap();
  private Map              organismMap = new HashMap();
  private Map              flowCellTypeMap = new HashMap();
  private Map              numberSequencingCyclesMap = new HashMap();
  private Map              genomeBuildMap = new HashMap();  
  private Map              analysisProtocolMap = new HashMap();
  private Map              analysisTypeMap = new HashMap();
  
  public DictionaryHelper() {    
  }
  
  
  public void getDictionaries(Session sess) {
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
    List microarrayCategories = sess.createQuery("SELECT mc from MicroarrayCategory as mc").list();
    for(Iterator i = microarrayCategories.iterator(); i.hasNext();) {
      MicroarrayCategory mc = (MicroarrayCategory)i.next();
      microarrayCategoryMap.put(mc.getCodeMicroarrayCategory(), mc.getMicroarrayCategory());
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
    List flowCells = sess.createQuery("SELECT fc from FlowCellType as fc").list();
    for(Iterator i = flowCells.iterator(); i.hasNext();) {
      FlowCellType fc = (FlowCellType)i.next();
      flowCellTypeMap.put(fc.getIdFlowCellType(), fc.getFlowCellType());
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
  public String getSampleSource(Sample sample) {
    String name = "";
    if (sample.getIdSampleSource() != null) {
      name = (String)sampleSourceMap.get(sample.getIdSampleSource());
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
  public String getMicroarrayCategory(String code) {
    String name = "";
    if (code != null) {
      name = (String)microarrayCategoryMap.get(code);
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
  public String getFlowCellType(Integer id) {
    String name = "";
    if (id != null) {
      name = (String)flowCellTypeMap.get(id);
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

}
