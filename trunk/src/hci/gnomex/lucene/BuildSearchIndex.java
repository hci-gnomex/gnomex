package hci.gnomex.lucene;

import hci.framework.model.DetailObject;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.Lab;
import hci.gnomex.model.Request;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.model.Visibility;
import hci.gnomex.utility.DictionaryHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;



/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class BuildSearchIndex extends DetailObject {

  private Configuration   configuration;
  private Session         sess;
  private SessionFactory  sessionFactory;
  private static String  dbhost = "hci-db";

  private String gnomex_db_driver;
  private String gnomex_db_url;
  private String gnomex_db_username;
  private String gnomex_db_password;
  
  
  private Map projectRequestMap;
  private Map projectAnnotationMap;
  private Map analysisGroupMap;
  private Map sampleAnnotationMap;
  private Map codeExperimentFactorMap;
  private Map codeExperimentDesignMap;
  private Map protocolMap;
  private Map analysisFileCommentsMap;
  
  private DictionaryHelper dh = null;
  
  protected static String DATA_SOURCES = "/orion/config/data-sources.xml";  

  private static final String          KEY_DELIM = "&-&-&";

  public BuildSearchIndex() {
  }
  public static void main(String[] args)
  {
    BuildSearchIndex app = new BuildSearchIndex();
    try
    {
      System.out.println(new Date() + " connecting...");
      app.connect();

      System.out.println(new Date() + " initializing...");
      app.init();

      System.out.println(new Date() + " building lucene experiment index...");
      app.buildExperimentIndex();

      System.out.println(new Date() + " building lucene protocol index...");
      app.buildProtocolIndex();
      
      System.out.println(new Date() + " building lucena analysis index...");
      app.buildAnalysisIndex();
      
      System.out.println(new Date() + " disconnecting...");
      System.out.println();
      app.disconnect();
    }
    catch( Exception e )
    {
      System.out.println( e.toString() );
      e.printStackTrace();
    }
  }


  private void connect()
      throws Exception
  {
    registerDataSources(new File(DATA_SOURCES));
    
    configuration = new Configuration()
    .addFile("SchemaGNomEx.hbm.xml");
    
      
    configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.SybaseDialect")
                 .setProperty("hibernate.query.substitutions", "true 1, false 0, yes 'Y', no 'N'")
                 .setProperty("hibernate.connection.driver_class", this.gnomex_db_driver)
                 .setProperty("hibernate.connection.username", this.gnomex_db_username)
                 .setProperty("hibernate.connection.password", this.gnomex_db_password)
                 .setProperty("hibernate.connection.url", this.gnomex_db_url )
                 .setProperty("hibernate.cache.provider_class", "org.hibernate.cache.HashtableCacheProvider");
                 
    
    sessionFactory = configuration.buildSessionFactory();
    sess = sessionFactory.openSession();
  }
  
  private void disconnect() 
    throws Exception {
    sess.close();
  }
  
  private void init() throws Exception {
    // Get dictionaries
    dh = DictionaryHelper.getInstance(sess);
    
  }
  
  private void buildExperimentIndex() throws Exception{

    IndexWriter experimentIndexWriter = new IndexWriter(Constants.LUCENE_EXPERIMENT_INDEX_DIRECTORY, new StandardAnalyzer(), true);

    // Get basic project/request data
    getProjectRequestData(sess);
    
    // Get project annotations (experiment design and factors)
    getProjectAnnotations(sess);
    
    // Get sample annotations (sample characteristics)
    getSampleAnnotations(sess);
    
    //
    // Write Experiment Lucene Index.
    // (A document for each request)
    //
    for(Iterator i = projectRequestMap.keySet().iterator(); i.hasNext();) {
      String key = (String)i.next();
      
      Object[] keyTokens = key.split(KEY_DELIM);
      Integer idProject = new Integer((String)keyTokens[0]);
      Integer idRequest = keyTokens.length == 2 && keyTokens[1] != null ? new Integer((String)keyTokens[1]) : null;
      List rows = (List)projectRequestMap.get(key);
      
      
      Document doc = buildExperimentDocument(idProject, idRequest, rows);
      experimentIndexWriter.addDocument(doc);
    }
    experimentIndexWriter.optimize();
    experimentIndexWriter.close();
  }
  
  private void buildProtocolIndex() throws Exception{

    IndexWriter protocolIndexWriter   = new IndexWriter(Constants.LUCENE_PROTOCOL_INDEX_DIRECTORY,   new StandardAnalyzer(), true);

    // Get basic protocol data
    getProtocolData(sess);

    
    //
    // Write Protocol Lucene Index.
    // (A document for each protocol)
    //
    for( Iterator i = protocolMap.keySet().iterator(); i.hasNext();) {
      String key = (String)i.next();
      Object[] keyTokens = key.split(KEY_DELIM);
      String  protocolType = (String)keyTokens[0];
      Integer idProtocol  = new Integer((String)keyTokens[1]);
      Object[] row = (Object[])protocolMap.get(key);
      
      Document doc = buildProtocolDocument(protocolType, idProtocol, row);
      protocolIndexWriter.addDocument(doc);
    }
    protocolIndexWriter.optimize();
    protocolIndexWriter.close();
  }
  
  private void buildAnalysisIndex() throws Exception{

    IndexWriter analysisIndexWriter   = new IndexWriter(Constants.LUCENE_ANALYSIS_INDEX_DIRECTORY,   new StandardAnalyzer(), true);

    // Get analysis data
    getAnalysisData(sess);

    
    //
    // Write Analysis Lucene Index.
    // (A document for each protocol)
    //
    for( Iterator i = analysisGroupMap.keySet().iterator(); i.hasNext();) {
      String key = (String)i.next();
      Object[] keyTokens = key.split(KEY_DELIM);
      Integer idAnalysisGroup = new Integer((String)keyTokens[0]);
      Integer idAnalysis = keyTokens.length == 2 && keyTokens[1] != null ? new Integer((String)keyTokens[1]) : null;
      Object[] row = (Object[])analysisGroupMap.get(key);
      
      StringBuffer analysisFileComments = (StringBuffer)analysisFileCommentsMap.get(idAnalysis);
      
      Document doc = buildAnalysisDocument(idAnalysisGroup, idAnalysis, row, analysisFileComments);
      analysisIndexWriter.addDocument(doc);
    }
    analysisIndexWriter.optimize();
    analysisIndexWriter.close();
  }
  
  
  private void getProjectRequestData(Session sess) throws Exception{
    StringBuffer buf = new StringBuffer();
    buf.append("SELECT proj.id, ");
    buf.append("       req.id, ");
    buf.append("       req.number, ");
    buf.append("       proj.name, ");
    buf.append("       proj.description, ");
    buf.append("       hyb.notes, ");
    buf.append("       s1.name, ");
    buf.append("       s1.description, ");
    buf.append("       s1.idOrganism, ");
    buf.append("       '', ");
    buf.append("       s2.name, ");
    buf.append("       s2.description,  ");
    buf.append("       s2.idOrganism,  ");
    buf.append("       '', ");
    buf.append("       req.idSlideProduct,  ");
    buf.append("       slideProd.idOrganism,  ");
    buf.append("       req.codeRequestCategory,  ");
    buf.append("       proj.idLab,  ");
    buf.append("       labProj.lastName,  ");
    buf.append("       labProj.firstName,  ");
    buf.append("       req.idLab,  ");
    buf.append("       labReq.lastName,  ");
    buf.append("       labReq.firstName,  ");
    buf.append("       req.codeMicroarrayCategory, ");
    buf.append("       reqOwner.firstName, ");
    buf.append("       reqOwner.lastName, ");
    buf.append("       '', ");
    buf.append("       req.codeVisibility, ");
    buf.append("       req.createDate, ");
    buf.append("       s1.idSampleType ");
    
    buf.append("FROM        Project as proj ");
    buf.append("LEFT JOIN   proj.requests as req ");
    buf.append("LEFT JOIN   proj.lab as labProj ");
    buf.append("LEFT JOIN   req.lab as labReq ");
    buf.append("LEFT JOIN   req.slideProduct as slideProd ");
    buf.append("LEFT JOIN   req.appUser as reqOwner ");
    buf.append("LEFT JOIN   req.hybridizations as hyb ");
    buf.append("LEFT JOIN   hyb.labeledSampleChannel1 as ls1 ");
    buf.append("LEFT JOIN   ls1.sample as s1 ");
    buf.append("LEFT JOIN   hyb.labeledSampleChannel1 as ls2 ");
    buf.append("LEFT JOIN   ls2.sample as s2 ");
    buf.append("WHERE       req.codeRequestCategory != '" + RequestCategory.SOLEXA_REQUEST_CATEGORY + "' ");
    buf.append("ORDER BY proj.idProject, req.idRequest ");
    
    List results = sess.createQuery(buf.toString()).list();
    projectRequestMap = new HashMap();
    for(Iterator i = results.iterator(); i.hasNext();) {
      Object[] row = (Object[])i.next();
      
      Integer idProject = (Integer)row[0];
      Integer idRequest = (Integer)row[1];
      String key = idProject + KEY_DELIM + (idRequest != null ? idRequest.toString() : "");
      
      List rows = (List)projectRequestMap.get(key);
      if (rows == null) {
        rows = new ArrayList();
        projectRequestMap.put(key, rows);
      }
      rows.add(row);
    }    
    
    buf = new StringBuffer();
    buf.append("SELECT proj.id, ");
    buf.append("       req.id, ");
    buf.append("       req.number, ");
    buf.append("       proj.name, ");
    buf.append("       proj.description, ");
    buf.append("       '', ");
    buf.append("       s1.name, ");
    buf.append("       s1.description, ");
    buf.append("       s1.idOrganism, ");
    buf.append("       '', ");
    buf.append("       '', ");
    buf.append("       '',  ");
    buf.append("       '',  ");
    buf.append("       '', ");
    buf.append("       '',  ");
    buf.append("       '',  ");
    buf.append("       req.codeRequestCategory,  ");
    buf.append("       proj.idLab,  ");
    buf.append("       labProj.lastName,  ");
    buf.append("       labProj.firstName,  ");
    buf.append("       req.idLab,  ");
    buf.append("       labReq.lastName,  ");
    buf.append("       labReq.firstName,  ");
    buf.append("       req.codeMicroarrayCategory, ");
    buf.append("       reqOwner.firstName, ");
    buf.append("       reqOwner.lastName, ");
    buf.append("       '', ");
    buf.append("       req.codeVisibility, ");
    buf.append("       req.createDate, ");
    buf.append("       s1.idSampleType ");
    
    buf.append("FROM        Project as proj ");
    buf.append("LEFT JOIN   proj.requests as req ");
    buf.append("LEFT JOIN   proj.lab as labProj ");
    buf.append("LEFT JOIN   req.lab as labReq ");
    buf.append("LEFT JOIN   req.appUser as reqOwner ");
    buf.append("LEFT JOIN   req.sequenceLanes as lane ");
    buf.append("LEFT JOIN   lane.sample as s1 ");
    buf.append("WHERE       req.codeRequestCategory = '" + RequestCategory.SOLEXA_REQUEST_CATEGORY + "' ");
    buf.append("ORDER BY proj.idProject, req.idRequest ");
    
    results = sess.createQuery(buf.toString()).list();
    for(Iterator i = results.iterator(); i.hasNext();) {
      Object[] row = (Object[])i.next();
      
      Integer idProject = (Integer)row[0];
      Integer idRequest = (Integer)row[1];
      String key = idProject + KEY_DELIM + (idRequest != null ? idRequest.toString() : "");
      
      List rows = (List)projectRequestMap.get(key);
      if (rows == null) {
        rows = new ArrayList();
        projectRequestMap.put(key, rows);
      }
      rows.add(row);
    }  
  }
  
  private void getAnalysisData(Session sess) throws Exception{
    StringBuffer buf = new StringBuffer();
    buf.append("SELECT ag.id, ");
    buf.append("       a.id, ");
    buf.append("       ag.name, ");
    buf.append("       ag.description, ");
    buf.append("       '', ");
    buf.append("       ag.idLab, ");
    buf.append("       agLab.lastName, ");
    buf.append("       agLab.firstName, ");
    buf.append("       owner.firstName, ");
    buf.append("       owner.lastName, ");
    buf.append("       lab.lastName,  ");
    buf.append("       lab.firstName,  ");
    buf.append("       a.number, ");
    buf.append("       a.name, ");
    buf.append("       a.description, ");
    buf.append("       a.idAnalysisType, ");
    buf.append("       a.idAnalysisProtocol, ");
    buf.append("       a.idOrganism, ");
    buf.append("       a.idLab,  ");
    buf.append("       a.createDate, ");
    buf.append("       a.codeVisibility ");
    
    buf.append("FROM        AnalysisGroup as ag ");
    buf.append("LEFT JOIN   ag.lab as agLab ");
    buf.append("LEFT JOIN   ag.analysisItems as a ");
    buf.append("LEFT JOIN   a.lab as lab ");
    buf.append("LEFT JOIN   a.appUser as owner ");

    buf.append("ORDER BY ag.name, a.number, a.name ");
    
    List results = sess.createQuery(buf.toString()).list();
    analysisGroupMap = new HashMap();
    for(Iterator i = results.iterator(); i.hasNext();) {
      Object[] row = (Object[])i.next();
      
      Integer idAnalysisGroup = (Integer)row[0];
      Integer idAnalysis = (Integer)row[1];
      String key = idAnalysisGroup + KEY_DELIM + (idAnalysis != null ? idAnalysis.toString() : "");
      
      analysisGroupMap.put(key, row);
    }

    // Get analysis file comments
    buf = new StringBuffer();
    buf.append("SELECT a.id, ");
    buf.append("       af.fileName, ");
    buf.append("       af.comments ");
    buf.append("FROM        Analysis as a ");
    buf.append("LEFT JOIN   a.files as af ");
    
    results = sess.createQuery(buf.toString()).list();
    analysisFileCommentsMap = new HashMap();
    for(Iterator i = results.iterator(); i.hasNext();) {
      Object[] row = (Object[])i.next();
      
      Integer idAnalysis = (Integer)row[0];
      String fileName  = (String)row[1];
      String comments = (String)row[2];
      
      StringBuffer analysisFileComments = (StringBuffer)analysisFileCommentsMap.get(idAnalysis);
      if (analysisFileComments == null) {
        analysisFileComments = new StringBuffer();
      }
      analysisFileComments.append(comments);
      analysisFileComments.append(" ");
      
      
      analysisFileCommentsMap.put(idAnalysis, analysisFileComments);
    }    
    
  }
  
  
  private void getProjectAnnotations(Session sess) throws Exception{
    StringBuffer buf = new StringBuffer();
    buf.append("SELECT ede.idProject, ");
    buf.append("       ed.experimentDesign, ");
    buf.append("       ede.otherLabel,  ");
    buf.append("       ede.codeExperimentDesign ");
    buf.append("FROM   ExperimentDesignEntry as ede, ExperimentDesign ed ");
    buf.append("WHERE  ede.value = 'Y' ");
    buf.append("AND    ede.codeExperimentDesign = ed.codeExperimentDesign ");
    buf.append("ORDER BY ede.idProject ");
    
    List results = sess.createQuery(buf.toString()).list();
    projectAnnotationMap = new HashMap();
    codeExperimentDesignMap = new HashMap();
    codeExperimentFactorMap = new HashMap();
    for(Iterator i = results.iterator(); i.hasNext();) {
      Object[] row = (Object[])i.next();
      
      Integer idProject = (Integer)row[0];
      String code = (String)row[3];
      
      List rows = (List)projectAnnotationMap.get(idProject);
      if (rows == null) {
        rows = new ArrayList();
        projectAnnotationMap.put(idProject, rows);
      }
      rows.add(row);
      
      List codes = (List)codeExperimentDesignMap.get(idProject);
      if (codes == null) {
        codes = new ArrayList();
        codeExperimentDesignMap.put(idProject, codes);
      }
      codes.add(code);
    }   
    
    buf = new StringBuffer();
    buf.append("SELECT efe.idProject, ");
    buf.append("       ef.experimentFactor, ");
    buf.append("       efe.otherLabel,  ");
    buf.append("       efe.codeExperimentFactor ");
    buf.append("FROM   ExperimentFactorEntry as efe, ExperimentFactor ef ");
    buf.append("WHERE  efe.value = 'Y' ");
    buf.append("AND    efe.codeExperimentFactor = ef.codeExperimentFactor ");
    buf.append("ORDER BY efe.idProject ");
    
    results = sess.createQuery(buf.toString()).list();
    for(Iterator i = results.iterator(); i.hasNext();) {
      Object[] row = (Object[])i.next();
      
      Integer idProject = (Integer)row[0];
      String code = (String)row[3];
      
      List rows = (List)projectAnnotationMap.get(idProject);
      if (rows == null) {
        rows = new ArrayList();
        projectAnnotationMap.put(idProject, rows);
      }
      rows.add(row);
      
      List codes = (List)codeExperimentFactorMap.get(idProject);
      if (codes == null) {
        codes = new ArrayList();
        codeExperimentFactorMap.put(idProject, codes);
      }
      codes.add(code);

    }    
  }
  
  
  private void getSampleAnnotations(Session sess) throws Exception{
    StringBuffer buf = new StringBuffer();
    buf.append("SELECT s.idRequest, ");
    buf.append("       sc.sampleCharacteristic, ");
    buf.append("       sce.value,  ");
    buf.append("       sce.otherLabel  ");
    buf.append("FROM   Sample s, SampleCharacteristicEntry as sce, SampleCharacteristic sc ");
    buf.append("WHERE  sce.value is not NULL ");
    buf.append("AND    s.idSample = sce.idSample ");
    buf.append("AND    sce.codeSampleCharacteristic = sc.codeSampleCharacteristic ");
    buf.append("ORDER BY s.idRequest ");
    
    List results = sess.createQuery(buf.toString()).list();
    sampleAnnotationMap = new HashMap();
    for(Iterator i = results.iterator(); i.hasNext();) {
      Object[] row = (Object[])i.next();
      
      Integer idRequest = (Integer)row[0];
      
      List rows = (List)sampleAnnotationMap.get(idRequest);
      if (rows == null) {
        rows = new ArrayList();
        sampleAnnotationMap.put(idRequest, rows);
      }
      rows.add(row);
    }   
  }
  
  private void getProtocolData(Session sess) throws Exception {
    protocolMap = new HashMap();

    StringBuffer buf = new StringBuffer();
    buf.append("SELECT prot.idLabelingProtocol, ");
    buf.append("       prot.labelingProtocol, ");
    buf.append("       prot.description, ");
    buf.append("       'hci.gnomex.model.LabelingProtocol' ");
    buf.append("FROM   LabelingProtocol as prot");

    List results = sess.createQuery(buf.toString()).list();    
    for(Iterator i = results.iterator(); i.hasNext();) {
      Object[] row = (Object[])i.next();      
      String key = "Labeling Protocol" + KEY_DELIM + row[0];      
      protocolMap.put(key, row);      
    }
    
    buf = new StringBuffer();
    buf.append("SELECT prot.idHybProtocol, ");
    buf.append("       prot.hybProtocol, ");
    buf.append("       prot.description, ");
    buf.append("       'hci.gnomex.model.HybProtocol' ");
    buf.append("FROM   HybProtocol as prot");

    results = sess.createQuery(buf.toString()).list();    
    for(Iterator i = results.iterator(); i.hasNext();) {
      Object[] row = (Object[])i.next();      
      String key = "Hyb Protocol" + KEY_DELIM + row[0];      
      protocolMap.put(key, row);      
    }
    
    buf = new StringBuffer();
    buf.append("SELECT prot.idScanProtocol, ");
    buf.append("       prot.scanProtocol, ");
    buf.append("       prot.description, ");
    buf.append("       'hci.gnomex.model.ScanProtocol' ");
    buf.append("FROM   ScanProtocol as prot");

    results = sess.createQuery(buf.toString()).list();    
    for(Iterator i = results.iterator(); i.hasNext();) {
      Object[] row = (Object[])i.next();      
      String key = "Scan Protocol"  + KEY_DELIM + row[0];      
      protocolMap.put(key, row);      
    }
    
    buf = new StringBuffer();
    buf.append("SELECT prot.idFeatureExtractionProtocol, ");
    buf.append("       prot.featureExtractionProtocol, ");
    buf.append("       prot.description, ");
    buf.append("       'hci.gnomex.model.FeatureExtractionProtocol' ");
    buf.append("FROM   FeatureExtractionProtocol as prot");

    results = sess.createQuery(buf.toString()).list();    
    for(Iterator i = results.iterator(); i.hasNext();) {
      Object[] row = (Object[])i.next();      
      String key = "Feature Extraction Protocol"  + KEY_DELIM + row[0];      
      protocolMap.put(key, row);      
    }

    buf = new StringBuffer();
    buf.append("SELECT prot.idAnalysisProtocol, ");
    buf.append("       prot.analysisProtocol, ");
    buf.append("       prot.description, ");
    buf.append("       'hci.gnomex.model.AnalysisProtocol' ");
    buf.append("FROM   AnalysisProtocol as prot");

    results = sess.createQuery(buf.toString()).list();    
    for(Iterator i = results.iterator(); i.hasNext();) {
      Object[] row = (Object[])i.next();      
      String key = "Analysis Protocol"  + KEY_DELIM + row[0];      
      protocolMap.put(key, row);      
    }



  }

  
  private Document buildExperimentDocument(Integer idProject, Integer idRequest, List rows) {
    
    Document doc = new Document();
    //
    // Obtain basic project and request text fields
    //
    String  requestNumber = null;
    String  projectName = null;
    String  projectDescription = null;

    
    StringBuffer hybNotes = new StringBuffer();
    StringBuffer sampleNames = new StringBuffer();
    StringBuffer sampleDescriptions = new StringBuffer();
    StringBuffer sampleOrganisms = new StringBuffer();
    HashMap      idOrganismSampleMap = new HashMap();
    HashMap      idSampleTypeMap = new HashMap();
    StringBuffer sampleTypes = new StringBuffer();
    HashMap      idSampleSourceMap = new HashMap();
    StringBuffer sampleSources = new StringBuffer();
    Integer      idSlideProduct = null;
    String       slideProduct = null;
    Integer      idOrganismSlideProduct = null;
    String       slideProductOrganism = null;
    String       codeRequestCategory = null;
    String       requestCategory = null;
    Integer      idLabProject = null;
    String       labLastNameProject = null;
    String       labFirstNameProject = null;
    Integer      idLabRequest = null;
    String       labLastNameRequest = null;
    String       labFirstNameRequest = null;
    String       codeMicroarrayCategory = null;
    String       microarrayCategory = null;
    String       requestOwnerFirstName = null;
    String       requestOwnerLastName = null;
    String       requestCodeVisibility = null;
    String       requestPublicNote = null;
    Date         requestCreateDate = null;
    StringBuffer requestDisplayName = null;
    String       labProject = null;
    String       labRequest = null;
    
    
    for(Iterator i1 = rows.iterator(); i1.hasNext();) {
      Object[] row = (Object[])i1.next();
      
      idProject           = (Integer)row[0];
      idRequest           = (Integer)row[1];
      requestNumber       = (String) row[2];
      projectName         = (String) row[3];
      projectDescription  = (String) row[4];
      
      String hybNote = (String) row[5];
      hybNotes.append(hybNote          != null ? hybNote + " " : "");

      // sample 1
      String  sampleName      = (String) row[6];
      String  sampleDesc      = (String) row[7];
      Integer idOrganism      = (Integer)row[8];
      Integer idSampleType   = (Integer)row[29];
      if (idOrganism != null) {
        idOrganismSampleMap.put(idOrganism, null);            
      }

      if (idSampleType != null) {
        idSampleTypeMap.put(idSampleType, null);
      }      
      sampleNames.append       (sampleName    != null ? sampleName + " " : "");
      sampleDescriptions.append(sampleDesc    != null ? sampleDesc + " " : "");
      sampleOrganisms.append(   idOrganism != null    ? dh.getOrganism(idOrganism) + " " : "");
      sampleTypes.append(     idSampleType != null ? dh.getSampleType(idSampleType) + " " : "");

      // sample 2
      sampleName      = (String) row[10];
      sampleDesc      = (String) row[11];
      idOrganism      = row[12] instanceof Integer ? (Integer)row[12] : null;
      if (idOrganism != null) {
        idOrganismSampleMap.put(idOrganism, null);            
      }
      
      sampleNames.append       (sampleName    != null ? sampleName + " " : "");
      sampleDescriptions.append(sampleDesc    != null ? sampleDesc + " " : "");
      sampleOrganisms.append(   idOrganism != null    ? dh.getOrganism(idOrganism) + " " : "");
      
      // more request data
      idSlideProduct           = row[14] instanceof Integer ? (Integer)row[14] : null;
      idOrganismSlideProduct   = row[14] instanceof Integer ? (Integer)row[15] : null;
      codeRequestCategory      = (String) row[16];
      idLabProject             = (Integer)row[17];
      labLastNameProject       = (String) row[18];
      labFirstNameProject      = (String) row[19];
      idLabRequest             = (Integer)row[20];
      labLastNameRequest       = (String) row[21];
      labFirstNameRequest      = (String) row[22];
      codeMicroarrayCategory   = (String) row[23];
      requestOwnerFirstName    = (String) row[24];
      requestOwnerLastName     = (String) row[25];      
      requestCodeVisibility    = (String) row[27];
      requestCreateDate        = (java.sql.Date) row[28];
      
      slideProduct             = idSlideProduct != null ? dh.getSlideProductName(idSlideProduct) : null;
      slideProductOrganism     = idOrganismSlideProduct != null ? dh.getOrganism(idOrganismSlideProduct) : null;
      requestCategory          = dh.getRequestCategory(codeRequestCategory);
      microarrayCategory       = dh.getMicroarrayCategory(codeMicroarrayCategory);
      if (requestCodeVisibility != null && requestCodeVisibility.equals(Visibility.VISIBLE_TO_PUBLIC)) {
        requestPublicNote = "(Public) ";
      }
      
      labProject = Lab.formatLabName(labLastNameProject, labFirstNameProject);
      labRequest = Lab.formatLabName(labLastNameRequest, labFirstNameRequest);
      
      requestDisplayName = new StringBuffer();
      if (codeRequestCategory != null && codeRequestCategory.equals(RequestCategory.QUALITY_CONTROL_REQUEST_CATEGORY)) {
        requestDisplayName.append(requestNumber);
        requestDisplayName.append(" - ");
        requestDisplayName.append(requestOwnerFirstName);
        requestDisplayName.append(" ");
        requestDisplayName.append(requestOwnerLastName);
        requestDisplayName.append(" ");
        requestDisplayName.append(this.formatDate(requestCreateDate, this.DATE_OUTPUT_SQL));      
      } else {
        requestDisplayName.append(requestNumber);
        requestDisplayName.append(" - ");
        requestDisplayName.append(slideProduct);      
        requestDisplayName.append(" - ");
        requestDisplayName.append(requestOwnerFirstName);
        requestDisplayName.append(" ");
        requestDisplayName.append(requestOwnerLastName);
        requestDisplayName.append(" ");
        requestDisplayName.append(this.formatDate(requestCreateDate, this.DATE_OUTPUT_SQL));      
      }
      
    }
    
    // Concatenate string of distinct idOrganism for samples
    StringBuffer idOrganismSamples = new StringBuffer();
    for(Iterator i2 = idOrganismSampleMap.keySet().iterator(); i2.hasNext();) {
      Integer idOrganismSample = (Integer)i2.next();
      idOrganismSamples.append(idOrganismSample.toString());
      if (i2.hasNext()) {
        idOrganismSamples.append(" ");
      }
    }
    //  Concatenate string of distinct idSampleTypes for samples
    StringBuffer idSampleTypes = new StringBuffer();
    for(Iterator i2 = idSampleTypeMap.keySet().iterator(); i2.hasNext();) {
      Integer idSampleType = (Integer)i2.next();
      idSampleTypes.append(idSampleType.toString());
      if (i2.hasNext()) {
        idSampleTypes.append(" ");
      }
    }
    
    //  Concatenate string of distinct idSampleSource for samples
    StringBuffer idSampleSources = new StringBuffer();
    for(Iterator i2 = idSampleSourceMap.keySet().iterator(); i2.hasNext();) {
      Integer idSampleSource = (Integer)i2.next();
      idSampleSources.append(idSampleSource.toString());
      if (i2.hasNext()) {
        idSampleSources.append(" ");
      }
    }
    
    
    //
    // Obtain experiment design entries and experiment factor entries
    // on project
    //
    StringBuffer projectAnnotations = new StringBuffer();
    List projectAnnotationRows = (List)projectAnnotationMap.get(idProject);
    if (projectAnnotationRows != null) {
      for(Iterator i1 = projectAnnotationRows.iterator(); i1.hasNext();) {
        Object[] row = (Object[])i1.next();
        projectAnnotations.append((String)row[1] != null && !((String)row[1]).trim().equals("") ? (String)row[1] + " " : "");
        projectAnnotations.append((String)row[2] != null && !((String)row[2]).trim().equals("") ? (String)row[2] + " " : "");
      }          
    }
    StringBuffer codeExperimentDesigns = new StringBuffer();
    List codes = (List)codeExperimentDesignMap.get(idProject);
    if (codes != null) {
      for(Iterator i1 = codes.iterator(); i1.hasNext();) {
        String code = (String)i1.next();
        codeExperimentDesigns.append(code + " ");
      }          
    }
    StringBuffer codeExperimentFactors = new StringBuffer();
    codes = (List)codeExperimentFactorMap.get(idProject);
    if (codes != null) {
      for(Iterator i1 = codes.iterator(); i1.hasNext();) {
        String code = (String)i1.next();
        codeExperimentFactors.append(code + " ");
      }          
    }

    //
    // Obtain sample annotations on samples of request
    //
    StringBuffer sampleAnnotations = new StringBuffer();
    if (idRequest != null) {
      List sampleAnnotationRows = (List)sampleAnnotationMap.get(idRequest);
      if (sampleAnnotationRows != null) {
        for(Iterator i1 = sampleAnnotationRows.iterator(); i1.hasNext();) {
          Object[] row = (Object[])i1.next();
          sampleAnnotations.append((String)row[1] != null && !((String)row[1]).trim().equals("") ? (String)row[1] + " " : "");
          sampleAnnotations.append((String)row[2] != null && !((String)row[2]).trim().equals("") ? (String)row[2] + " " : "");
          sampleAnnotations.append((String)row[3] != null && !((String)row[3]).trim().equals("") ? (String)row[3] + " " : "");
        }          
      }
      
    }
    

    // Combine all text into one search field
    StringBuffer text = new StringBuffer();
    text.append(projectName);
    text.append(" ");
    text.append(projectDescription);
    text.append(" ");
    text.append(hybNotes.toString());
    text.append(" ");
    text.append(sampleNames.toString());
    text.append(" ");
    text.append(sampleDescriptions.toString());
    text.append(" ");
    text.append(projectAnnotations.toString());
    text.append(" ");
    text.append(sampleAnnotations.toString());
    text.append(" ");        
    text.append(sampleOrganisms.toString());
    text.append(" ");        
    text.append(sampleSources.toString());
    text.append(" ");        
    text.append(slideProduct);
    text.append(" ");        
    text.append(slideProductOrganism);
    text.append(" ");        
    text.append(labProject);
    text.append(" ");        
    text.append(labRequest);
    text.append(" ");        

    
    Map nonIndexedFieldMap = new HashMap();
    nonIndexedFieldMap.put(ExperimentIndexHelper.ID_PROJECT, idProject.toString());
    nonIndexedFieldMap.put(ExperimentIndexHelper.REQUEST_NUMBER, requestNumber);
    nonIndexedFieldMap.put(ExperimentIndexHelper.DISPLAY_NAME, requestDisplayName.toString());
    nonIndexedFieldMap.put(ExperimentIndexHelper.OWNER_FIRST_NAME, requestOwnerFirstName);
    nonIndexedFieldMap.put(ExperimentIndexHelper.OWNER_LAST_NAME, requestOwnerLastName);
    nonIndexedFieldMap.put(ExperimentIndexHelper.CREATE_DATE, requestCreateDate != null ? this.formatDate(requestCreateDate, this.DATE_OUTPUT_SQL) : null);
    nonIndexedFieldMap.put(ExperimentIndexHelper.MICROARRAY_CATEGORY, microarrayCategory);
    nonIndexedFieldMap.put(ExperimentIndexHelper.PROJECT_PUBLIC_NOTE, "");
    nonIndexedFieldMap.put(ExperimentIndexHelper.PUBLIC_NOTE, requestPublicNote);
    
    Map indexedFieldMap = new HashMap();
    indexedFieldMap.put(ExperimentIndexHelper.ID_REQUEST, idRequest != null ? idRequest.toString() : "unknown");      
    indexedFieldMap.put(ExperimentIndexHelper.PROJECT_NAME, projectName);
    indexedFieldMap.put(ExperimentIndexHelper.PROJECT_DESCRIPTION, projectDescription);
    indexedFieldMap.put(ExperimentIndexHelper.HYB_NOTES, hybNotes.toString());
    indexedFieldMap.put(ExperimentIndexHelper.SAMPLE_NAMES, sampleNames.toString());
    indexedFieldMap.put(ExperimentIndexHelper.SAMPLE_DESCRIPTIONS, sampleDescriptions.toString());
    indexedFieldMap.put(ExperimentIndexHelper.SAMPLE_ORGANISMS, sampleOrganisms.toString());
    indexedFieldMap.put(ExperimentIndexHelper.ID_ORGANISM_SAMPLE, idOrganismSamples.toString());
    indexedFieldMap.put(ExperimentIndexHelper.SAMPLE_SOURCES, sampleSources.toString());
    indexedFieldMap.put(ExperimentIndexHelper.ID_SAMPLE_TYPES, idSampleTypes.toString());
    indexedFieldMap.put(ExperimentIndexHelper.REQUEST_CATEGORY, requestCategory);
    indexedFieldMap.put(ExperimentIndexHelper.CODE_REQUEST_CATEGORY, codeRequestCategory);
    indexedFieldMap.put(ExperimentIndexHelper.CODE_MICROARRAY_CATEGORY, codeMicroarrayCategory);
    indexedFieldMap.put(ExperimentIndexHelper.ID_SLIDE_PRODUCT, idSlideProduct != null ? idSlideProduct.toString() : null);
    indexedFieldMap.put(ExperimentIndexHelper.SLIDE_PRODUCT, slideProduct);
    indexedFieldMap.put(ExperimentIndexHelper.SLIDE_PRODUCT_ORGANISM, slideProductOrganism);
    indexedFieldMap.put(ExperimentIndexHelper.ID_ORGANISM_SLIDE_PRODUCT, idOrganismSlideProduct != null ? idOrganismSlideProduct.toString() : null);
    indexedFieldMap.put(ExperimentIndexHelper.REQUEST_CATEGORY, requestCategory);
    indexedFieldMap.put(ExperimentIndexHelper.ID_LAB_PROJECT, idLabProject != null ? idLabProject.toString() : null);
    indexedFieldMap.put(ExperimentIndexHelper.PROJECT_LAB_NAME, labProject);
    indexedFieldMap.put(ExperimentIndexHelper.ID_LAB, idLabRequest != null ? idLabRequest.toString() : null);
    indexedFieldMap.put(ExperimentIndexHelper.LAB_NAME, labRequest);
    indexedFieldMap.put(ExperimentIndexHelper.CODE_VISIBILITY, requestCodeVisibility);
    indexedFieldMap.put(ExperimentIndexHelper.PROJECT_ANNOTATIONS, projectAnnotations.toString());
    indexedFieldMap.put(ExperimentIndexHelper.CODE_EXPERIMENT_DESIGNS, codeExperimentDesigns.toString());
    indexedFieldMap.put(ExperimentIndexHelper.CODE_EXPERIMENT_FACTORS, codeExperimentFactors.toString());
    indexedFieldMap.put(ExperimentIndexHelper.SAMPLE_ANNOTATIONS, sampleAnnotations.toString());        
    indexedFieldMap.put(ExperimentIndexHelper.TEXT, text.toString());
    
    ExperimentIndexHelper.build(doc, nonIndexedFieldMap, indexedFieldMap);
    
    return doc;
    
  }
  
  private Document buildProtocolDocument(String protocolType, Integer idProtocol, Object[] row) {
    
    Document doc = new Document();
    
    String name        = (String)row[1];
    String description = (String)row[2];
    String className   = (String)row[3];
    
    Map nonIndexedFieldMap = new HashMap();
    nonIndexedFieldMap.put(ProtocolIndexHelper.ID_PROTOCOL, idProtocol.toString());
    nonIndexedFieldMap.put(ProtocolIndexHelper.PROTOCOL_TYPE, protocolType);
    nonIndexedFieldMap.put(ProtocolIndexHelper.CLASS_NAME, className);
    

    Map indexedFieldMap = new HashMap();
    indexedFieldMap.put(ProtocolIndexHelper.NAME, name);
    indexedFieldMap.put(ProtocolIndexHelper.DESCRIPTION, description);
    indexedFieldMap.put(ProtocolIndexHelper.TEXT, name + " " + description);
    
    ProtocolIndexHelper.build(doc, nonIndexedFieldMap, indexedFieldMap);
    
    return doc;
  }

  private Document buildAnalysisDocument(Integer idAnalysisGroup, Integer idAnalysis, Object[] row, StringBuffer analysisFileComments) {
    
    Document doc = new Document();

        
    String agName                 = (String)row[2];
    String agDesc                 = (String)row[3];
    Integer agIdLab               = (Integer)row[5];
    String agLabLastName          = (String)row[6];
    String agLabFirstName         = (String)row[7];
    String ownerFirstName         = (String)row[8];
    String ownerLastName          = (String)row[9];
    String labLastName            = (String)row[10];
    String labFirstName           = (String)row[11];
    String number                 = (String)row[12];
    String name                   = (String)row[13];
    String desc                   = (String)row[14];
    Integer idAnalysisType        = (Integer)row[15];
    Integer idAnalysisProtocol    = (Integer)row[16];
    Integer idOrganism            = (Integer)row[17];
    Integer idLab                 = (Integer)row[18];
    java.sql.Date createDate      = (java.sql.Date)row[19];
    String codeVisibility         = (String)row[20];
    String publicNote             = ""; 
    
   
    
    String agLabName = Lab.formatLabName(agLabLastName, agLabFirstName);
    String labName   = Lab.formatLabName(labLastName, labFirstName);

    if (codeVisibility != null && codeVisibility.equals(Visibility.VISIBLE_TO_PUBLIC)) {
        publicNote = "(Public) ";
    }

    
    Map nonIndexedFieldMap = new HashMap();
    nonIndexedFieldMap.put(AnalysisIndexHelper.ID_ANALYSISGROUP, idAnalysisGroup.toString());
    nonIndexedFieldMap.put(AnalysisIndexHelper.ID_LAB_ANALYSISGROUP, agIdLab.toString());
    nonIndexedFieldMap.put(AnalysisIndexHelper.LAB_NAME_ANALYSISGROUP, agLabName);
    nonIndexedFieldMap.put(AnalysisIndexHelper.ID_ANALYSIS, idAnalysis != null ? idAnalysis.toString() : "");
    nonIndexedFieldMap.put(AnalysisIndexHelper.ANALYSIS_NUMBER, number);
    nonIndexedFieldMap.put(AnalysisIndexHelper.OWNER_FIRST_NAME, ownerFirstName);
    nonIndexedFieldMap.put(AnalysisIndexHelper.OWNER_LAST_NAME, ownerLastName);
    nonIndexedFieldMap.put(AnalysisIndexHelper.CREATE_DATE, createDate != null ? this.formatDate(createDate, this.DATE_OUTPUT_SQL) : null);
    nonIndexedFieldMap.put(AnalysisIndexHelper.PUBLIC_NOTE, publicNote);
    

    Map indexedFieldMap = new HashMap();
    indexedFieldMap.put(AnalysisIndexHelper.ID_LAB_ANALYSISGROUP, agIdLab);
    indexedFieldMap.put(AnalysisIndexHelper.ANALYSIS_GROUP_NAME, agName);
    indexedFieldMap.put(AnalysisIndexHelper.ANALYSIS_GROUP_DESCRIPTION, agDesc);
    indexedFieldMap.put(AnalysisIndexHelper.ANALYSIS_NAME, name);
    indexedFieldMap.put(AnalysisIndexHelper.DESCRIPTION, desc);
    indexedFieldMap.put(AnalysisIndexHelper.ID_ORGANISM, idOrganism);
    indexedFieldMap.put(AnalysisIndexHelper.ORGANISM, idOrganism != null ? dh.getOrganism(idOrganism) : "");
    indexedFieldMap.put(AnalysisIndexHelper.ID_ANALYSIS_TYPE, idAnalysisType);
    indexedFieldMap.put(AnalysisIndexHelper.ANALYSIS_TYPE, idAnalysisType != null ? dh.getAnalysisType(idAnalysisType) : "");
    indexedFieldMap.put(AnalysisIndexHelper.ID_ANALYSIS_PROTOCOL, idAnalysisProtocol);
    indexedFieldMap.put(AnalysisIndexHelper.ANALYSIS_PROTOCOL, idAnalysisProtocol != null ? dh.getAnalysisProtocol(idAnalysisProtocol) : "");
    indexedFieldMap.put(AnalysisIndexHelper.ID_LAB, idLab != null ? idLab.toString() : "");
    indexedFieldMap.put(AnalysisIndexHelper.LAB_NAME, labName != null ? labName : "");
    indexedFieldMap.put(AnalysisIndexHelper.CODE_VISIBILITY, codeVisibility != null ? codeVisibility : "");

    
    
    StringBuffer buf = new StringBuffer();
    buf.append(name);
    buf.append(" ");
    buf.append(desc);
    buf.append(" ");
    buf.append(agName);
    buf.append(" ");
    buf.append(agDesc);
    buf.append(" ");
    buf.append(analysisFileComments != null ? analysisFileComments.toString() : "");
    buf.append(" ");
    indexedFieldMap.put(AnalysisIndexHelper.TEXT, buf.toString());
    
    AnalysisIndexHelper.build(doc, nonIndexedFieldMap, indexedFieldMap);
    
    return doc;
  }

  private void registerDataSources(File xmlFile) {
    if(xmlFile.exists()) {
      try {
        SAXBuilder builder = new SAXBuilder();
        org.jdom.Document doc = builder.build(xmlFile);
        this.registerDataSources(doc);
      } catch (JDOMException e) {
      }
    }
  }

  

  private void registerDataSources(org.jdom.Document doc) {
    Element root = doc.getRootElement();
    if (root.getChildren("data-source") != null) {
      Iterator i = root.getChildren("data-source").iterator();
      while (i.hasNext()) {
        Element e = (Element) i.next();
        if (e.getAttributeValue("name") != null && e.getAttributeValue("name").equals("GNOMEX_GUEST")) {
          this.gnomex_db_driver = e.getAttributeValue("connection-driver");
          this.gnomex_db_url = e.getAttributeValue("url");
          this.gnomex_db_password = e.getAttributeValue("password");
          this.gnomex_db_username = e.getAttributeValue("username");
        } 
      }
    }
  }


}