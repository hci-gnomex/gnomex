package hci.gnomex.utility;

import hci.gnomex.model.Property;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;



public class PropertyHelper implements Serializable {
  private static PropertyHelper   theInstance;
  
  private Map                      propertyMap = new HashMap();

  private static final String    PROPERTY_PRODUCTION_SERVER                   = "production_server";
  
  private static final String    PROPERTY_EXPERIMENT_READ_DIRECTORY           = "experiment_read_directory";
  private static final String    PROPERTY_EXPERIMENT_WRITE_DIRECTORY          = "experiment_write_directory";
  private static final String    PROPERTY_ANALYSIS_READ_DIRECTORY             = "analysis_read_directory";
  private static final String    PROPERTY_ANALYSIS_WRITE_DIRECTORY            = "analysis_write_directory";
  private static final String    PROPERTY_FLOWCELL_DIRECTORY                  = "flowcell_directory";
  private static final String    PROPERTY_FDT_DIRECTORY_GNOMEX                = "fdt_directory_gnomex";
  private static final String    PROPERTY_FDT_DIRECTORY                       = "fdt_directory";
  private static final String    PROPERTY_FDT_CLIENT_CODEBASE                 = "fdt_client_codebase";
  private static final String    PROPERTY_FDT_SERVER_NAME                     = "fdt_server_name";
  private static final String    PROPERTY_FILE_FDT_FILE_DAEMON_TASK_DIR       = "fdt_file_daemon_task_dir";

  
  public PropertyHelper() {    
  }

  public String getFDTFileDaemonTaskDir(String serverName) {
    String property = "";
    String propertyName = null;

    // First use the property qualified by server name.  If
    // it isn't found, get the property without any qualification.   
    propertyName = PROPERTY_FILE_FDT_FILE_DAEMON_TASK_DIR + "_" + serverName;
    property = this.getProperty(propertyName);
    if (property == null || property.equals("")) {  
      property = this.getPropertyPartialMatch(propertyName);
      if (property == null || property.equals("")) {  
        propertyName = PROPERTY_FILE_FDT_FILE_DAEMON_TASK_DIR;
        property = this.getProperty(propertyName);        
      }
    }
    return addFileSepIfNec(property);
  }
  
  public String getFDTDirectoryForGNomEx(String serverName) {
	  String property = "";
	  String propertyName = null;

	  // First use the property qualified by server name.  If
	  // it isn't found, get the property without any qualification.   
	  propertyName = PROPERTY_FDT_DIRECTORY_GNOMEX + "_" + serverName;
	  property = this.getProperty(propertyName);
	  if (property == null || property.equals("")) {  
		  property = this.getPropertyPartialMatch(propertyName);
		  if (property == null || property.equals("")) {  
			  propertyName = PROPERTY_FDT_DIRECTORY_GNOMEX;
			  property = this.getProperty(propertyName);			  
		  }
	  }
	  return addFileSepIfNec(property);
  }
  
  public String GetFDTDirectory(String serverName) {
	  String property = "";
	  String propertyName = null;

	  // First use the property qualified by server name.  If
	  // it isn't found, get the property without any qualification.   
	  propertyName = PROPERTY_FDT_DIRECTORY + "_" + serverName;
	  property = this.getProperty(propertyName);
	  if (property == null || property.equals("")) {  
		  property = this.getPropertyPartialMatch(propertyName);
		  if (property == null || property.equals("")) {  
			  propertyName = PROPERTY_FDT_DIRECTORY;
			  property = this.getProperty(propertyName);
		  }
	  }
	  return addFileSepIfNec(property);
  }
  public String getFDTClientCodebase(String serverName) {
	  String property = "";
	  String propertyName = null;

	  // First use the property qualified by server name.  If
	  // it isn't found, get the property without any qualification.   
	  propertyName = PROPERTY_FDT_CLIENT_CODEBASE + "_" + serverName;
	  property = this.getProperty(propertyName);
	  if (property == null || property.equals("")) {  
		  property = this.getPropertyPartialMatch(propertyName);
		  if (property == null || property.equals("")) {  
			  propertyName = PROPERTY_FDT_CLIENT_CODEBASE;
			  property = this.getProperty(propertyName);
		  }
	  }
	  return property;
  }
  
  public String getFDTServerName(String serverName) {
	  String property = "";
	  String propertyName = null;

	  // First use the property qualified by server name.  If
	  // it isn't found, get the property without any qualification.   
	  propertyName = PROPERTY_FDT_SERVER_NAME + "_" + serverName;
	  property = this.getProperty(propertyName);
	  if (property == null || property.equals("")) {  
		  property = this.getPropertyPartialMatch(propertyName);
		  if (property == null || property.equals("")) {  
			  propertyName = PROPERTY_FDT_SERVER_NAME;
			  property = this.getProperty(propertyName);
		  }
	  }
	  return property;
  }
  
  public static synchronized PropertyHelper getInstance(Session sess) {
    if (theInstance == null) {
      theInstance = new PropertyHelper();
      theInstance.loadProperties(sess);
    }
    return theInstance;
    
  }
  
  public static synchronized PropertyHelper reload(Session sess) {
    theInstance = new PropertyHelper();
    theInstance.loadProperties(sess);  
    return theInstance;
    
  }
  
  
  private void loadProperties(Session sess)  {
    List properties = sess.createQuery("select p from Property as p").list();
    for (Iterator i = properties.iterator(); i.hasNext();) {
      Property prop = (Property)i.next();
      propertyMap.put(prop.getPropertyName(), prop.getPropertyValue());
    }   
  }
  
  

  public String getProperty(String name) {
    String propertyValue = "";
    if (name != null && !name.equals("")) {
      return (String)propertyMap.get(name);
    } else {
      return "";
    }
  }
  
  public String getPropertyPartialMatch(String name) {
	  Set keySet = propertyMap.keySet();
	  Iterator i = keySet.iterator();
	  while(i.hasNext()) {
		  String thisKey = (String) i.next();
		  if(thisKey.startsWith(name)) {
			  return getProperty(thisKey);
		  }
	  }
	  return "";

  }
  
  public String getQualifiedProperty(String name, String serverName) {
    // First try to get property that is 
    // qualified by server name.  If that isn't found, get the property without
    // any qualification.
    String property = "";
    if (serverName != null && !serverName.equals("")) {
      String qualifiedName = name + "_" + serverName;
      property = this.getProperty(qualifiedName);      
    }
    if (property == null || property.equals("")) {   
      property = this.getProperty(name);
    }
    return property;
  }
  
  public boolean isProductionServer(String serverName) {
    if (this.getProperty(PROPERTY_PRODUCTION_SERVER) != null &&
        this.getProperty(PROPERTY_PRODUCTION_SERVER).contains(serverName)) {
      return true;
    } else {
      return false;
    }
  }
  
  public String getAnalysisReadDirectory(String serverName) {
	  // First try to get property that is qualified by server name.  
	  // Then, try with partial mathc. If that isn't found then get 
	  // the property without any qualification.	  
	  String property = "";
	  String propertyName = PROPERTY_ANALYSIS_READ_DIRECTORY + "_" + serverName;
	  property = this.getProperty(propertyName);
	  if (property == null || property.equals("")) { 
		  property = this.getPropertyPartialMatch(propertyName);
		  if (property == null || property.equals("")) { 
			  propertyName = PROPERTY_ANALYSIS_READ_DIRECTORY;
			  property = this.getProperty(propertyName);       	
		  }
	  }   

	  return property;
  }

  public String getAnalysisWriteDirectory(String serverName) {
	  // First try to get property that is qualified by server name.  
	  // Then, try with partial mathc. If that isn't found then get 
	  // the property without any qualification.	  
	  String property = "";
	  String propertyName = PROPERTY_ANALYSIS_WRITE_DIRECTORY + "_" + serverName;
	  property = this.getProperty(propertyName);
	  if (property == null || property.equals("")) { 
		  property = this.getPropertyPartialMatch(propertyName);
		  if (property == null || property.equals("")) { 
			  propertyName = PROPERTY_ANALYSIS_WRITE_DIRECTORY;
			  property = this.getProperty(propertyName);       	
		  }
	  }   

	  return addFileSepIfNec(property);
  }
  
  public String getFlowCellDirectory(String serverName) {
    // First try to get property that is qualified by server name.  
	// Then, try with partial mathc. If that isn't found then get 
    // the property without any qualification.	  
    String property = "";
    String propertyName = PROPERTY_FLOWCELL_DIRECTORY + "_" + serverName;
    property = this.getProperty(propertyName);
    if (property == null || property.equals("")) { 
    	property = this.getPropertyPartialMatch(propertyName);
    	if (property == null || property.equals("")) { 
			propertyName = PROPERTY_FLOWCELL_DIRECTORY;
			property = this.getProperty(propertyName);				  
    	}  
    }
      
    return property;
  }

  public String getMicroarrayDirectoryForWriting(String serverName) {
    // First try to get property that is qualified by server name.  
	// Then, try with partial mathc. If that isn't found then get 
    // the property without any qualification.	  
	String property = "";
    String propertyName = PROPERTY_EXPERIMENT_WRITE_DIRECTORY + "_" + serverName;
    property = this.getProperty(propertyName);
    if (property == null || property.equals("")) { 
    	property = this.getPropertyPartialMatch(propertyName);
    	if (property == null || property.equals("")) {
			propertyName = PROPERTY_EXPERIMENT_WRITE_DIRECTORY;
			property = this.getProperty(propertyName);
    	}
    }
    
    return addFileSepIfNec(property);
  }

  public  String getMicroarrayDirectoryForReading(String serverName) {
    // First try to get property that is qualified by server name.  
	// Then, try with partial mathc. If that isn't found then get 
    // the property without any qualification.
    String property = "";
    String propertyName = PROPERTY_EXPERIMENT_READ_DIRECTORY + "_" + serverName;
    property = this.getProperty(propertyName);
    if (property == null || property.equals("")) { 
      property = this.getPropertyPartialMatch(propertyName);
      if (property == null || property.equals("")) {
          propertyName = PROPERTY_EXPERIMENT_READ_DIRECTORY;
          property = this.getProperty(propertyName);    	  
      }
    }
    return property;    
  }
  
   
  public String parseMainFolderName(String serverName, String fileName) {
    String mainFolderName = "";
    String baseDir = "";
    
    String experimentDirectory = this.getMicroarrayDirectoryForReading(serverName);
    String flowCellDirectory   = this.getFlowCellDirectory(serverName);
    
    // Change all file separators to forward slash
    String theFileName = fileName.replaceAll("\\\\", "/");
    experimentDirectory = experimentDirectory.replaceAll("\\\\", "/");
    flowCellDirectory = flowCellDirectory.replaceAll("\\\\", "/");
    
    if (theFileName.toLowerCase().indexOf(experimentDirectory.toLowerCase()) >= 0) {
      baseDir = experimentDirectory;
    } else if (theFileName.toLowerCase().indexOf(flowCellDirectory.toLowerCase()) >= 0) {
      baseDir = flowCellDirectory;
    } else {
      throw new RuntimeException("Cannot determine base directory.  Neither flowcell directory or experiment directory match file name " + fileName);
    }
    
    
    // Strip off the leading part of the path, up through the year subdirectory,
    // to leave only the path that starts with the request number subdirectory.
    String relativePath = theFileName.substring(baseDir.length() + (baseDir.endsWith("/") | baseDir.endsWith("\\") ? 5 : 6));
    
    String tokens[] = relativePath.split("/", 2);
    if (tokens == null || tokens.length == 1) {
      tokens = relativePath.split("\\\\", 2);
    }
    if (tokens.length == 2) {
      mainFolderName = tokens[0];
    }
    
    return mainFolderName;
  }
  
  public static String addFileSepIfNec(String inputPath) {
    
    if(inputPath == null || inputPath.length() < 1) {
      return inputPath;
    }
    String tempStr = inputPath;
    // Change all file separators to forward slash
    tempStr.replaceAll("\\\\", "/");
    
    // And then see if there is a separator at the end of the string
    if(tempStr.charAt(tempStr.length()-1) != '/') {
      inputPath = inputPath + File.separator;
    }

    return inputPath;
    
  }
 
  
}
