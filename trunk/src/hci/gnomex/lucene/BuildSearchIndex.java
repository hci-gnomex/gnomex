package hci.gnomex.lucene;

import hci.framework.model.DetailObject;
import hci.gnomex.constants.Constants;
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
  private static String  dbUserName = "GuestGNomEx";
  private static String  dbPassword = "p@ssw0rd";
  
  private Map projectRequestMap;
  private Map projectAnnotationMap;
  private Map sampleAnnotationMap;
  private Map codeExperimentFactorMap;
  private Map codeExperimentDesignMap;
  private Map protocolMap;
  
  private DictionaryHelper dh = null;
  

  public BuildSearchIndex() {
  }
  public static void main(String[] args)
  {
    if (args.length > 0) {
      dbhost = args[0];
    }
    if (args.length > 1) {
      dbPassword = args[1];
    }
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
    configuration = new Configuration()
    .addFile("SchemaGNomEx.hbm.xml");
    
      
    configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.SybaseDialect")
                 .setProperty("hibernate.query.substitutions", "true 1, false 0, yes 'Y', no 'N'")
                 .setProperty("hibernate.connection.driver_class", "com.microsoft.jdbc.sqlserver.SQLServerDriver")
                 .setProperty("hibernate.connection.username", dbUserName)
                 .setProperty("hibernate.connection.password", dbPassword)
                 .setProperty("hibernate.connection.url", "jdbc:microsoft:sqlserver://" + dbhost + ":1433;databaseName=GNomEx;SelectMethod=cursor" )
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
    dh = new DictionaryHelper();
    dh.getDictionaries(sess);
    
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
      
      Object[] keyTokens = key.split("-");
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
      Object[] keyTokens = key.split("-");
      String  protocolType = (String)keyTokens[0];
      Integer idProtocol  = new Integer((String)keyTokens[1]);
      Object[] row = (Object[])protocolMap.get(key);
      
      Document doc = buildProtocolDocument(protocolType, idProtocol, row);
      protocolIndexWriter.addDocument(doc);
    }
    protocolIndexWriter.optimize();
    protocolIndexWriter.close();
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
    buf.append("       s1.idSampleSource, ");
    buf.append("       s2.name, ");
    buf.append("       s2.description,  ");
    buf.append("       s2.idOrganism,  ");
    buf.append("       s2.idSampleSource, ");
    buf.append("       req.idSlideProduct,  ");
    buf.append("       slideProd.idOrganism,  ");
    buf.append("       req.codeRequestCategory,  ");
    buf.append("       proj.idLab,  ");
    buf.append("       labProj.name,  ");
    buf.append("       req.idLab,  ");
    buf.append("       labReq.name,  ");
    buf.append("       req.codeMicroarrayCategory, ");
    buf.append("       reqOwner.firstName, ");
    buf.append("       reqOwner.lastName, ");
    buf.append("       proj.codeVisibility, ");
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
      String key = idProject + "-" + (idRequest != null ? idRequest.toString() : "");
      
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
    buf.append("       s1.idSampleSource, ");
    buf.append("       '', ");
    buf.append("       '',  ");
    buf.append("       '',  ");
    buf.append("       '', ");
    buf.append("       '',  ");
    buf.append("       '',  ");
    buf.append("       req.codeRequestCategory,  ");
    buf.append("       proj.idLab,  ");
    buf.append("       labProj.name,  ");
    buf.append("       req.idLab,  ");
    buf.append("       labReq.name,  ");
    buf.append("       req.codeMicroarrayCategory, ");
    buf.append("       reqOwner.firstName, ");
    buf.append("       reqOwner.lastName, ");
    buf.append("       proj.codeVisibility, ");
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
      String key = idProject + "-" + (idRequest != null ? idRequest.toString() : "");
      
      List rows = (List)projectRequestMap.get(key);
      if (rows == null) {
        rows = new ArrayList();
        projectRequestMap.put(key, rows);
      }
      rows.add(row);
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
      String key = "Labeling Protocol-" + row[0];      
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
      String key = "Hyb Protocol-" + row[0];      
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
      String key = "Scan Protocol-" + row[0];      
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
      String key = "Feature Extraction Protocol-" + row[0];      
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
    String       labProject = null;
    Integer      idLabRequest = null;
    String       labRequest = null;
    String       codeMicroarrayCategory = null;
    String       microarrayCategory = null;
    String       requestOwnerFirstName = null;
    String       requestOwnerLastName = null;
    String       projectCodeVisibility = null;
    String       projectPublicNote = null;
    String       requestCodeVisibility = null;
    String       requestPublicNote = null;
    Date         requestCreateDate = null;
    StringBuffer requestDisplayName = null;
    
    
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
      Integer idSampleSource   = (Integer)row[9];
      Integer idSampleType   = (Integer)row[27];
      if (idOrganism != null) {
        idOrganismSampleMap.put(idOrganism, null);            
      }
      if (idSampleSource != null) {
        idSampleSourceMap.put(idSampleSource, null);
      }
      if (idSampleType != null) {
        idSampleTypeMap.put(idSampleType, null);
      }      
      sampleNames.append       (sampleName    != null ? sampleName + " " : "");
      sampleDescriptions.append(sampleDesc    != null ? sampleDesc + " " : "");
      sampleOrganisms.append(   idOrganism != null    ? dh.getOrganism(idOrganism) + " " : "");
      sampleSources.append(     idSampleSource != null ? dh.getSampleSource(idSampleSource) + " " : "");
      sampleTypes.append(     idSampleType != null ? dh.getSampleType(idSampleType) + " " : "");

      // sample 2
      sampleName      = (String) row[10];
      sampleDesc      = (String) row[11];
      idOrganism      = row[12] instanceof Integer ? (Integer)row[12] : null;
      idSampleSource   =row[13] instanceof Integer ? (Integer)row[13] : null;
      if (idOrganism != null) {
        idOrganismSampleMap.put(idOrganism, null);            
      }
      if (idSampleSource != null) {
        idSampleSourceMap.put(idSampleSource, null);
      }
      
      sampleNames.append       (sampleName    != null ? sampleName + " " : "");
      sampleDescriptions.append(sampleDesc    != null ? sampleDesc + " " : "");
      sampleOrganisms.append(   idOrganism != null    ? dh.getOrganism(idOrganism) + " " : "");
      sampleSources.append(     idSampleSource != null ? dh.getSampleSource(idSampleSource) + " " : "");
      
      // more request data
      idSlideProduct           = row[14] instanceof Integer ? (Integer)row[14] : null;
      idOrganismSlideProduct   = row[14] instanceof Integer ? (Integer)row[15] : null;
      codeRequestCategory      = (String) row[16];
      idLabProject             = (Integer)row[17];
      labProject               = (String) row[18];
      idLabRequest             = (Integer)row[19];
      labRequest               = (String) row[20];
      codeMicroarrayCategory   = (String) row[21];
      requestOwnerFirstName    = (String) row[22];
      requestOwnerLastName     = (String) row[23];
      projectCodeVisibility    = (String) row[24];
      requestCodeVisibility    = (String) row[25];
      requestCreateDate        = (java.sql.Date) row[26];
      
      slideProduct             = idSlideProduct != null ? dh.getSlideProductName(idSlideProduct) : null;
      slideProductOrganism     = idOrganismSlideProduct != null ? dh.getOrganism(idOrganismSlideProduct) : null;
      requestCategory          = dh.getRequestCategory(codeRequestCategory);
      microarrayCategory       = dh.getMicroarrayCategory(codeMicroarrayCategory);
      if (projectCodeVisibility != null && projectCodeVisibility.equals(Visibility.VISIBLE_TO_PUBLIC)) {
        projectPublicNote = "(Public) ";
      } 
      if (requestCodeVisibility != null && requestCodeVisibility.equals(Visibility.VISIBLE_TO_PUBLIC)) {
        requestPublicNote = "(Public) ";
      }
      
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
    // Obtain sample annoations on samples of request
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
    nonIndexedFieldMap.put(ExperimentIndexHelper.PROJECT_PUBLIC_NOTE, projectPublicNote);
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
    indexedFieldMap.put(ExperimentIndexHelper.ID_SAMPLE_SOURCES, idSampleSources.toString());
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
    indexedFieldMap.put(ExperimentIndexHelper.PROJECT_CODE_VISIBILITY, projectCodeVisibility);
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


}