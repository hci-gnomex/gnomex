package hci.gnomex.httpclient;



import hci.gnomex.model.AppUser;
import hci.gnomex.model.Application;
import hci.gnomex.model.Lab;
import hci.gnomex.model.Organism;
import hci.gnomex.model.Project;
import hci.gnomex.model.Property;
import hci.gnomex.model.PropertyOption;
import hci.gnomex.model.PropertyType;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.model.RequestCategoryType;
import hci.gnomex.utility.BatchDataSource;
import hci.gnomex.utility.DictionaryHelper;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Level;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

public class ImportExperiment extends HttpClientBase {

  private BatchDataSource                 dataSource;
  private Session                         sess;

  private Transaction                     tx;
  
  private static ImportExperiment  app = null;

  private String                          errorMessagePrefixString = "Error in ImportExperiment";
  
  private String                          fileName = "";
  private String                          annotationFileName = "";
  private String                          isExternal = "Y";  // By default, experiment imported will be external
  
  private DictionaryHelper                dictionaryHelper;
  private Document                        document;
  private Element                         requestNode;
  
  private Lab                             lab;
  private String                          labLastName;
  private String                          labFirstName;
  private Integer                         idAppUser;
  private String                          submitterFirstName;
  private String                          submitterLastName;
  private Project                         project;
  private Organism                        organism;
  private Application                     application;
  private HashMap<String, String>         sourcePropertyOptionMap = new HashMap<String, String>();  // key is idProperty "-" idPropertyOption, value is option name
  private HashMap<Integer, Property>      sourceToTargetPropertyMap = new HashMap<Integer, Property>();
  private HashMap<String, Property>       targetPropertyMap = new HashMap<String, Property>();
  private HashMap<String, RequestCategory> requestCategoryMap = new HashMap<String, RequestCategory>();
  
  
  public ImportExperiment(String[] args) {
    for (int i = 0; i < args.length; i++) {
      if (args[i].equals ("-file")) {
        fileName = args[++i];
      } if (args[i].equals ("-annotationFile")) {
        annotationFileName = args[++i];
      } if (args[i].equals ("-isExternal")) {
        isExternal = args[++i];
      } else if (args[i].equals("-properties")) {
        propertiesFileName = args[++i];
      } else if (args[i].equals("-server")) {
        server = args[++i];
      } else if (args[i].equals("-serverURL")) {
        serverURL = args[++i];
      } else if (args[i].equals ("-help")) {
        printUsage();
        System.exit(0);
      } 
    }
  }
  
  /**
   * @param args
   */
  public static void main(String[] args) {
    app  = new ImportExperiment(args);
    
    app.run();
  }

  public void run(){
    Calendar calendar = Calendar.getInstance();
    errorMessagePrefixString += " on " + new SimpleDateFormat("MM-dd-yyyy_HH:mm:ss").format(calendar.getTime()) + "\n";

    try {
      org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger("org.hibernate");
      log.setLevel(Level.ERROR);

      // Connect to the database
      dataSource = new BatchDataSource();
      app.connect();
      
      Transaction tx = sess.beginTransaction();
      
      
      // Init dictionaries
      initDictionaries();
      
      // Read the input XML file.  This is the output from GetRequest.gx
      readExperimentXMLFile();
      
      // Read the annotation XML File.  This is the output from GetPropertyList.
      readAnnotationXMLFile();
            
      
      // Set the attributes on Request to the correct ids for lab, app user, codeApplication, project
      initRequestNode();
      
      // Set the annotation attributes so that the name contains the id of the property from the
      // target gnomex db.
      mapAnnotations();
      
      tx.commit();
      
     
      // Call SaveRequest.gx
      callServlet();
      
      

    } catch (Exception e) {
            
      this.sendErrorReport(e);

    } finally {
      if (sess != null) {
        try {
          app.disconnect();
        } catch(Exception e) {
          System.err.println( "ImportExperiment unable to disconnect from hibernate session.   " + e.toString() );
        }
      }
    }

      
  }
  
  private void readExperimentXMLFile() throws Exception {
    SAXBuilder builder = new SAXBuilder();
    File xmlFile = new File(fileName);
    document = (Document) builder.build(xmlFile);
  }
  
  private void readAnnotationXMLFile() throws Exception {
    SAXBuilder builder = new SAXBuilder();
    File annotationXMLFile = new File(annotationFileName);
    Document annotationDocument = (Document)builder.build(annotationXMLFile);

    // We need a map that will allow us to lookup the option name based on the source db idPropertyOption.
    // Then we can lookup the target idPropertyOption based on this option name.
    for (Iterator i = annotationDocument.getRootElement().getChildren("Property").iterator(); i.hasNext();) {
      Element propertyNode = (Element)i.next();
      for (Iterator i1 = propertyNode.getChildren("PropertyOption").iterator(); i1.hasNext();) {
        Element optionNode = (Element)i1.next();
        String key = propertyNode.getAttributeValue("idProperty") + "-" + optionNode.getAttributeValue("idPropertyOption");
        sourcePropertyOptionMap.put(key, optionNode.getAttributeValue("option"));
      }
    }
    
  }
  
  private void initDictionaries() throws Exception {
    
    // Load properties
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
      targetPropertyMap.put(prop.getName(), prop);
    }    
    
    // Load request categories
    queryBuf = new StringBuffer();
    queryBuf.append("SELECT rc from RequestCategory as rc");
    List requestCategories = sess.createQuery(queryBuf.toString()).list();
    for (Iterator i = requestCategories.iterator(); i.hasNext();) {
      RequestCategory rc = (RequestCategory)i.next();
      requestCategoryMap.put(rc.getCodeRequestCategory(), rc);
    }    
     
  }
  
  private void initRequestNode() throws Exception {
    // Zero out the idRequest so that a new request is created
    requestNode = document.getRootElement().getChild("Request");
    requestNode.setAttribute("idRequest", "0");
    requestNode.setAttribute("isExternal", isExternal);
    
    // We only handle Illumina request types for importing at this time
    String codeRequestCategory = requestNode.getAttributeValue("codeRequestCategory");
    RequestCategory rc = requestCategoryMap.get(codeRequestCategory);
    if (!rc.getType().equals(RequestCategoryType.TYPE_MISEQ ) && !rc.getType().equals(RequestCategoryType.TYPE_HISEQ)) {
      throw new Exception("Only Illumina HiSeq/MiSeq experiment types can be imported");
    }

    // Fill in the idLab based on the lab name 
    parseLabName();
    Query labQuery = sess.createQuery("select l from Lab l where lastName = '" + labLastName + "'" + " and firstName = '" + labFirstName + "'");
    lab = (Lab)labQuery.uniqueResult();   
    if (lab == null) {
      throw new Exception("Cannot find lab " + labLastName + ", " + labFirstName);
    }
    requestNode.setAttribute("idLab", lab.getIdLab().toString());
    
    // Fill in the  idOwner, idAppUser based on and submitter name
    parseSubmitterName();
    idAppUser = getIdAppUser(lab);
    requestNode.setAttribute("idAppUser", idAppUser.toString());
    requestNode.setAttribute("idOwner", idAppUser.toString());
    
    // Obtain the idOrganism based on the organismName
    String organismName = requestNode.getAttributeValue("organismName");
    Query organismQuery = sess.createQuery("select o from Organism o where o.organism = '" + organismName + "'");
    organism = (Organism)organismQuery.uniqueResult();
    if (organism == null) {
      throw new Exception("Cannot find organism " + organismName);
    }
    requestNode.setAttribute("idOrganism", organism.getIdOrganism().toString());
    
    // Make sure that we can find the application based on the application name
    String applicationName = requestNode.getChild("application").getChild("Application").getAttributeValue("application");
    Query applicationQuery = sess.createQuery("select a from Application a where application = '" + applicationName + "'");
    application = (Application)applicationQuery.uniqueResult();
    if (application == null) {
      throw new Exception("Cannot find the application '" + applicationName + "'");
    }

    // Find the project based on the name.  If it isn't found, create a new one
    String projectName        = requestNode.getChild("project").getChild("Project").getAttributeValue("name");
    String projectDescription = requestNode.getChild("project").getChild("Project").getAttributeValue("description");
    Query projectQuery = sess.createQuery("select p from Project p where p.name = '" + projectName + "' and p.idLab = " + lab.getIdLab());
    project = (Project)projectQuery.uniqueResult();
    if (project == null) {
      project = new Project();
      project.setName(projectName);
      project.setDescription(projectDescription);
      project.setIdLab(lab.getIdLab());
      project.setIdAppUser(idAppUser);
      sess.save(project);
      sess.flush();
    }
    requestNode.setAttribute("idProject", project.getIdProject().toString());

    
  }
  
  private void mapAnnotations() throws Exception{
    
    
    // Lookup the properties based on the property name.  Then use the target db's idProperty in the 
    // XML for PropertyEntry.
    for (Iterator i = requestNode.getChild("PropertyEntries").getChildren("PropertyEntry").iterator(); i.hasNext();) {
      Element propertyNode = (Element)i.next();
      
      // Only map the attributes that are selected. In other words, only deal with the annotations that are actually
      // present on the sample.
      if (!propertyNode.getAttributeValue("isSelected").equals("Y")) {
        continue;
      }
      
      String propertyName  = propertyNode.getAttributeValue("name");
      Integer idPropertySource    = Integer.parseInt(propertyNode.getAttributeValue("idProperty"));
      
      Property prop = targetPropertyMap.get(propertyName);
      if (prop == null) {
        throw new Exception("Sample annotation " + propertyName + " needs to be added to GNomEx before this experiment can be imported.");
      }
      propertyNode.setAttribute("idProperty", prop.getIdProperty().toString());
      
      // Map to source idProperty to the target property so that we can change the 'ANNOT' attributes
      // according to the ids in the target gnomex db.
      sourceToTargetPropertyMap.put(idPropertySource, prop);      
    }
    
    // For each sample, change the ANNOT### attributes to use the target db's idProperty.  Also, for
    // single and multi-option annotations, use the target db's idPropertyOption as the value.
    for (Iterator i1 = requestNode.getChild("samples").getChildren("Sample").iterator(); i1.hasNext();) {
      Element sampleNode = (Element)i1.next();
      for (Iterator i2 = sampleNode.getAttributes().iterator(); i2.hasNext();) {
        Attribute a = (Attribute)i2.next();
        String attributeName = a.getName();
        String value = a.getValue();
        
        //Strip off "ANNOT" from attribute name
        Integer idPropertySource = null;
        if (attributeName.startsWith("ANNOT")) {
          idPropertySource = Integer.parseInt(attributeName.substring(5));
        } else {
          continue;
        }
        
        Property prop = sourceToTargetPropertyMap.get(idPropertySource);
        if (prop == null) {
          throw new Exception("Cannot find target db property for " + attributeName);
        }

        
        // For single or multi-choice, lookup the idPropertyOption in the target db.
        String newValue = value;
        if (prop.getCodePropertyType().equals(PropertyType.MULTI_OPTION) || prop.getCodePropertyType().equals(PropertyType.OPTION)) {
          newValue = "";
          String[] valueTokens = value.split(",");
          // For multi-option attribues, we have a comma separated list of the options selected.  We will just loop
          // through once in the single-option case.
          for (int x = 0; x < valueTokens.length; x++) {
            String valueToken = valueTokens[x];
            
            String sourceOptionKey = sourcePropertyOptionMap.get(idPropertySource + "-" + valueToken);
            String sourceOptionName = sourcePropertyOptionMap.get(sourceOptionKey);
            if (sourceOptionName == null) {
              throw new Exception("Cannot find source option with the idPropertyOption = " + valueToken + " for property " + attributeName);
            }
            Integer targetIdPropertyOption = null;
            for (PropertyOption option : (Set<PropertyOption>)prop.getOptions()) {
              if (option.getOption().equals(sourceOptionName)) {
                targetIdPropertyOption = option.getIdPropertyOption();
                break;
              }
            }
            if (targetIdPropertyOption == null) {
              throw new Exception("Cannot find option " + sourceOptionName + " for property " + prop.getName());
            }
            
            if (newValue.length() > 0) {
              newValue += ",";
              newValue += targetIdPropertyOption;
            }
            
          }
        } 

        // Add a new one with ANNOT + prop.getIdProperty()
        sampleNode.setAttribute("ANNOT" + prop.getIdProperty(), newValue);
        
        // Remove the current attribute
        sampleNode.removeAttribute(a);

      }
    }
      
  }
  
  
  private void parseLabName() {
    String labName = requestNode.getAttributeValue("labName");
    String[] tokens = labName.split(", ");
    labLastName = tokens[0];
    if (tokens.length > 1) {
      labFirstName = tokens[1];
      // Now strip off 'Lab' from first name.
      if (labFirstName.contains(" Lab")) {
        labFirstName = labFirstName.substring(0, labFirstName.lastIndexOf(" Lab"));
      }
    }
  }
  
  private void parseSubmitterName() {
    String submitterName = requestNode.getAttributeValue("submitterName");
    String tokens[] = submitterName.split(" ");
    submitterFirstName = tokens[0];
    if (tokens.length > 1) {
      submitterLastName = tokens[1];      
    }
  }
  
  private Integer getIdAppUser(Lab lab) {
    Integer idAppUser = null;
    // See if the submitter name matches one of the lab members
    for (AppUser submitter : (Set<AppUser>)lab.getMembers()) {
      if (submitter.getLastName().equals(submitterLastName)) {
        if (submitterFirstName != null) {
          if (submitter.getFirstName() != null && submitter.getFirstName().equals(submitterFirstName)) {
            idAppUser = submitter.getIdAppUser();
          }
        } else {
          if (submitter.getFirstName() == null) {
            idAppUser = submitter.getIdAppUser();
          }
        }
      }
    }
    // Otherwise, see if the submitter name matches one of the lab managers
    if (idAppUser == null) {
      for (AppUser submitter : (Set<AppUser>)lab.getManagers()) {
        if (submitter.getLastName().equals(submitterLastName)) {
          if (submitterFirstName != null) {
            if (submitter.getFirstName() != null && submitter.getFirstName().equals(submitterFirstName)) {
              idAppUser = submitter.getIdAppUser();
            }
          } else {
            if (submitter.getFirstName() == null) {
              idAppUser = submitter.getIdAppUser();
            }
          }
        }
      }
    }
    // We can't find the matching name in members or managers, so just use the first
    // manager in the list.  If there isn't a manager, use the first member.
    if (idAppUser == null) {
      if (!lab.getManagers().isEmpty()) {
        idAppUser = ((AppUser)lab.getManagers().iterator().next()).getIdAppUser();
      } else if (!lab.getMembers().isEmpty()) {
        idAppUser = ((AppUser)lab.getMembers().iterator().next()).getIdAppUser();
      }
    }
    return idAppUser;
    
  }
  
  private void sendErrorReport(Exception e)  {
    
    String msg = "Could not import experiment. " + e.toString() + "\n\t";
    
    StackTraceElement[] stack = e.getStackTrace();
    for (StackTraceElement s : stack) {
      msg = msg + s.toString() + "\n\t\t";
    }
    
    String errorMessageString = errorMessagePrefixString;
    if ( !errorMessageString.equals( "" )) {
      errorMessageString += "\n";
    }
    errorMessageString += msg;
    
    System.err.println(errorMessageString);
    
    tx.rollback();    
    

  }

  
  private void connect()
  throws Exception
  {
    sess = dataSource.connect();
  }
  
  private void disconnect() 
  throws Exception {
    sess.close();
  }
  
  protected boolean checkParms() {
    if (fileName == null || annotationFileName == null) {
      return false;
    } else {
      return true;
    }
  }
  
  protected String getParms() throws UnsupportedEncodingException {
    String parms = URLEncoder.encode("idProject", "UTF-8") + "=" + URLEncoder.encode(project.getIdProject().toString(), "UTF-8");
    parms += "&" + "requestXMLString" + "=" + requestNode.toString();        
    
    return parms;
  }
  
  protected String getServletName() {
    return "CreateRequestServlet";
  }

  @Override
  protected void printUsage() {
    
    System.out.println("Import an experiment in an xml file to a gnomex db.");
    System.out.println("Command line args");
    System.out.println("   -properties      <propertiesFileName> ");
    System.out.println("   -server          <serverName>");
    System.out.println("   -file            XML file to be imported, generated from GetRequest.gx.");
    System.out.println("   -annotationFile  XML file to be imported, generated from GetPropertyList.gx.");
    System.out.println("   -[isExternal     Y/N]");
    System.out.println("   -help - gives this message.  Note no other processing is performed if the -help switch is specified.");

    
  }

  
}
