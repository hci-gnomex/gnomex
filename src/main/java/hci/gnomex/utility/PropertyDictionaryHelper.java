package hci.gnomex.utility;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.PropertyDictionary;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.query.Query;
import org.hibernate.Session;

public class PropertyDictionaryHelper implements Serializable {
  private static PropertyDictionaryHelper theInstance;

  private Map propertyMap = new HashMap();

  private static final String PROPERTY_PRODUCTION_SERVER = "production_server";

  public static final String PROPERTY_EXPERIMENT_DIRECTORY = "directory_experiment";
  public static final String PROPERTY_ANALYSIS_DIRECTORY = "directory_analysis";
  public static final String PROPERTY_DATATRACK_DIRECTORY = "directory_datatrack";
  public static final String PROPERTY_FLOWCELL_DIRECTORY = "directory_flowcell";
  public static final String PROPERTY_INSTRUMENT_RUN_DIRECTORY = "directory_instrument_run";
  private static final String PROPERTY_FDT_DIRECTORY_GNOMEX = "fdt_directory_gnomex";
  private static final String PROPERTY_FDT_DIRECTORY = "fdt_directory";
  private static final String PROPERTY_FDT_CLIENT_CODEBASE = "fdt_client_codebase";
  private static final String PROPERTY_FDT_SERVER_NAME = "fdt_server_name";
  //private static final String PROPERTY_FILE_FDT_FILE_DAEMON_TASK_DIR = "fdt_file_daemon_task_dir";
  private static final String PROPERTY_FDT_JAR_LOCATION = "fdt_jar_location";
  public static final String PROPERTY_PRODUCT_ORDER_DIRECTORY = "directory_product_order";
  public static final String PROPERTY_RUNTIME_ERROR_SERVER_LIST = "runtime_error_server_list";

  public static final String ANALYSIS_DIRECTORY_ALT = "analysis_directory_alternative";
  public static final String ANALYSIS_ALTERNATIVE_USERS = "analysis_alternative_users";


  public PropertyDictionaryHelper() {
  }

/*
  public String getFDTFileDaemonTaskDir(String serverName) {
    String property = "";
    String propertyName = null;

    // First use the property qualified by server name. If
    // it isn't found, get the property without any qualification.
    propertyName = PROPERTY_FILE_FDT_FILE_DAEMON_TASK_DIR + "_" + serverName;
    property = this.getProperty(propertyName);
    if (property == null || property.equals("")) {
      propertyName = PROPERTY_FILE_FDT_FILE_DAEMON_TASK_DIR;
      property = this.getProperty(propertyName);
    }
    return addFileSepIfNec(property);
  }
*/
  public String getFDTDirectoryForGNomEx(String serverName) {
    String property = "";
    String propertyName = null;

    // First use the property qualified by server name. If
    // it isn't found, get the property without any qualification.
    propertyName = PROPERTY_FDT_DIRECTORY_GNOMEX + "_" + serverName;
    property = this.getProperty(propertyName);
    if (property == null || property.equals("")) {
      propertyName = PROPERTY_FDT_DIRECTORY_GNOMEX;
      property = this.getProperty(propertyName);
    }
    return addFileSepIfNec(property);
  }

  public String getSFTPDirectoryForGNomEx(String serverName) {
    String property = "";
    String propertyName = null;

    // First use the property qualified by server name. If
    // it isn't found, get the property without any qualification.
    propertyName = PROPERTY_FDT_DIRECTORY_GNOMEX + "_" + serverName;
    property = this.getProperty(propertyName);
    if (property == null || property.equals("")) {
      propertyName = PROPERTY_FDT_DIRECTORY_GNOMEX;
      property = this.getProperty(propertyName);
    }
    return addFileSepIfNec(property);
  }


  public String GetFDTDirectory(String serverName) {
    String property = "";
    String propertyName = null;

    // First use the property qualified by server name. If
    // it isn't found, get the property without any qualification.
    propertyName = PROPERTY_FDT_DIRECTORY + "_" + serverName;
    property = this.getProperty(propertyName);
    if (property == null || property.equals("")) {
      propertyName = PROPERTY_FDT_DIRECTORY;
      property = this.getProperty(propertyName);
    }
    return addFileSepIfNec(property);
  }

  public String getFDTClientCodebase(String serverName) {
    String property = "";
    String propertyName = null;

    // First use the property qualified by server name. If
    // it isn't found, get the property without any qualification.
    propertyName = PROPERTY_FDT_CLIENT_CODEBASE + "_" + serverName;
    property = this.getProperty(propertyName);
    if (property == null || property.equals("")) {
      propertyName = PROPERTY_FDT_CLIENT_CODEBASE;
      property = this.getProperty(propertyName);
    }
    return property;
  }

  public String getFDTServerName(String serverName) {
    String property = "";
    String propertyName = null;

    // First use the property qualified by server name. If
    // it isn't found, get the property without any qualification.
    propertyName = PROPERTY_FDT_SERVER_NAME + "_" + serverName;
    property = this.getProperty(propertyName);
    if (property == null || property.equals("")) {
      propertyName = PROPERTY_FDT_SERVER_NAME;
      property = this.getProperty(propertyName);
    }
    return property;
  }

  public static synchronized PropertyDictionaryHelper getInstance(Session sess) {
    if (theInstance == null) {
      theInstance = new PropertyDictionaryHelper();
      theInstance.loadProperties(sess);
    }
    return theInstance;

  }

  public static synchronized PropertyDictionaryHelper reload(Session sess) {
    theInstance = new PropertyDictionaryHelper();
    theInstance.loadProperties(sess);
    return theInstance;

  }

  private void loadProperties(Session sess) {
    Query propQuery = sess.createQuery("select p from PropertyDictionary as p");
    List properties = propQuery.list();
    for (Iterator i = properties.iterator(); i.hasNext();) {
      PropertyDictionary prop = (PropertyDictionary) i.next();
      String name = prop.getPropertyName();
      if (prop.getIdCoreFacility() != null) {
        name = prop.getIdCoreFacility().toString() + "\t" + prop.getPropertyName();
      }
      if (prop.getCodeRequestCategory() != null) {
        name = prop.getCodeRequestCategory() + "\t" + prop.getPropertyName();
      }
      propertyMap.put(name, prop.getPropertyValue());
    }
  }

  public String getProperty(String name) {
    String propertyValue = "";
    if (name != null && !name.equals("")) {
      return (String) propertyMap.get(name);
    } else {
      return "";
    }
  }

  public String getCoreFacilityProperty(Integer idCoreFacility, String name) {
    if (idCoreFacility == null) {
      return "";
    }
    String propertyValue = "";
    if (name != null && !name.equals("")) {
      String qualName = idCoreFacility.toString() + "\t" + name;
      propertyValue = (String) propertyMap.get(qualName);
      if (propertyValue == null) {
        propertyValue = (String) propertyMap.get(name);
      }
    }

    return propertyValue;
  }

  public String getCoreFacilityRequestCategoryProperty(Integer idCoreFacility, String codeRequestCategory, String name) {
    String propertyValue = "";
    if (name != null && !name.equals("") && idCoreFacility != null) {
      propertyValue = null;
      if (codeRequestCategory != null && codeRequestCategory.length() > 0) {
        String qualName = codeRequestCategory + "\t" + name;
        propertyValue = (String) propertyMap.get(qualName);
      }
      if (propertyValue == null) {
        propertyValue = getCoreFacilityProperty(idCoreFacility, name);
      }
    }

    return propertyValue;
  }

  public String getQualifiedProperty(String name, String serverName) {
    // First try to get property that is
    // qualified by server name. If that isn't found, get the property without
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

  public String getQualifiedCoreFacilityProperty(String name, String serverName, Integer idCoreFacility) {
    String property = "";
    if (serverName != null && !serverName.equals("")) {
      String qualifiedName = name + "_" + serverName;
      property = this.getCoreFacilityProperty(idCoreFacility, qualifiedName);
    }
    if (property == null || property.equals("")) {
      property = this.getCoreFacilityProperty(idCoreFacility, name);
    }
    return property;
  }

  public Integer convertPropertyToInteger(String property, Integer def) {
    Integer intProperty = def;
    if (property != null) {
      try {
        intProperty = Integer.parseInt(property);
      } catch (NumberFormatException ex) {

      }
    }
    return intProperty;
  }

  public boolean isProductionServer(String serverName) {
    if (this.getProperty(PROPERTY_PRODUCTION_SERVER) != null && this.getProperty(PROPERTY_PRODUCTION_SERVER).contains(serverName)) {
      return true;
    } else {
      return false;
    }
  }

  public boolean isUniversityUserAuthentication() {
    return this.getProperty(PropertyDictionary.UNIVERSITY_USER_AUTHENTICATION).equals("Y");
  }

  public String getFDTJarLocation(String serverName) {
    // First try to get property that is qualified by server name.
    // If that isn't found then get the property without any qualification.
    String property = "";
    String propertyName = PROPERTY_FDT_JAR_LOCATION + "_" + serverName;
    property = this.getProperty(propertyName);
    if (property == null || property.equals("")) {
      propertyName = PROPERTY_FDT_JAR_LOCATION;
      property = this.getProperty(propertyName);
    }

    return property;
  }

  public String getDirectory(String serverName, Integer idCoreFacility, String directoryProperty) {
    // First try to get property that is qualified by server name
    // and core facility. if not found, if not found, try by
    // property name (not qualified) and id core facility. last,
    // try by just plain property name.
    String property = "";
    property = this.getCoreFacilityProperty(idCoreFacility, directoryProperty + "_" + serverName);
    if (property == null || property.equals("")) {
      property = this.getCoreFacilityProperty(idCoreFacility, directoryProperty);
    }
    if (property == null || property.equals("")) {
      property = this.getProperty(directoryProperty + "_" + serverName);
    }
    if (property == null || property.equals("")) {
      property = this.getProperty(directoryProperty);
    }

    // Make sure the property ends with a directory separator
    if (property != null && !property.equals("")) {
      if (!property.endsWith(Constants.FILE_SEPARATOR) && !property.endsWith("\\")) {
        property = property + Constants.FILE_SEPARATOR;
      }
    }

    return addFileSepIfNec(property);
  }

  /* If an alternative repository is being used make sure the user is allowed to see it
   *   08/10/2017 tim
   */
  public String getDirectory(String serverName, Integer idCoreFacility, String directoryProperty, String username) {
    // First try to get property that is qualified by server name
    // and core facility. if not found, if not found, try by
    // property name (not qualified) and id core facility. last,
    // try by just plain property name.
    String property = "";
    property = this.getCoreFacilityProperty(idCoreFacility, directoryProperty + "_" + serverName);
    if (property == null || property.equals("")) {
      property = this.getCoreFacilityProperty(idCoreFacility, directoryProperty);
    }
    if (property == null || property.equals("")) {
      property = this.getProperty(directoryProperty + "_" + serverName);
    }
    if (property == null || property.equals("")) {
      property = this.getProperty(directoryProperty);
    }

    // Make sure the property ends with a directory separator
    if (property != null && !property.equals("")) {

      // should this be visible to this user?
      System.out.println ("[PDH.getDirectory] directoryProperty: " + directoryProperty + " username: " + username);
      if (directoryProperty.equals(PropertyDictionaryHelper.ANALYSIS_DIRECTORY_ALT)) {
        // is the alternative repository visible to this user?
        if (username == null || username.equals("")) {
          // nope
          return getDirectory (serverName,idCoreFacility,PropertyDictionaryHelper.PROPERTY_ANALYSIS_DIRECTORY);
        }

        String altUsers = this.getProperty(PropertyDictionaryHelper.ANALYSIS_ALTERNATIVE_USERS);
        System.out.println ("[PDH.getDirectory] altUsers: " + altUsers + " username: " + username);
        if (altUsers == null || altUsers.equals("")) {
          // they didn't list any users that can see the alternative so no one can
          return getDirectory (serverName,idCoreFacility,PropertyDictionaryHelper.PROPERTY_ANALYSIS_DIRECTORY);
        }

        // are we one of the choosen few
        if (altUsers.indexOf(username) < 0 && altUsers.indexOf("any") < 0) {
          // no
          System.out.println ("[PDH.getDirectory] NOT IN LIST altUsers: " + altUsers + " username: " + username);
          return getDirectory (serverName,idCoreFacility,PropertyDictionaryHelper.PROPERTY_ANALYSIS_DIRECTORY);
        }

        // return the alternative repository
      }

      System.out.println ("[PDH.getDirectory] property: " + property);
      if (!property.endsWith(Constants.FILE_SEPARATOR) && !property.endsWith("\\")) {
        property = property + Constants.FILE_SEPARATOR;
      }
    }

    return addFileSepIfNec(property);
  }


  public static String parseZipEntryName(String baseDir, String fileName) {
    String zipEntryName = "";
    String baseDirLastPart = "";

    String baseDirCanonicalPath = "";
      baseDirCanonicalPath = new File(baseDir).getAbsolutePath().replace("\\", Constants.FILE_SEPARATOR);

    // Change all file separators to forward slash
    String theFileName = fileName.replaceAll("\\\\", Constants.FILE_SEPARATOR);
    baseDirCanonicalPath = baseDirCanonicalPath.replaceAll("\\\\", Constants.FILE_SEPARATOR);

    String tokens[] = baseDirCanonicalPath.split(Constants.FILE_SEPARATOR);
    if (tokens == null || tokens.length == 0) {
      throw new RuntimeException("Cannot parse directory into expected last file part " + baseDirCanonicalPath);
    }
    baseDirLastPart = tokens[tokens.length - 1];

    // Strip off the leading part of the path, up through the year subdirectory,
    // to leave only the path that starts with the request number subdirectory.
    tokens = theFileName.split(baseDirLastPart, 2);
    if (tokens == null || tokens.length < 2) {
      throw new RuntimeException("Cannot parse file into expected parts " + theFileName);
    }
    String lastFilePart = tokens[tokens.length - 1];

    // Now loop through the remaining file parts, leaving off year and
    // concatenating everything else.
    tokens = lastFilePart.split(Constants.FILE_SEPARATOR);
    if (tokens == null || tokens.length < 2) {
      throw new RuntimeException("Cannot parse file into expected parts for year and number " + lastFilePart);
    }
    int x = 0;
    String yearPart = "";
    for (x = 0; x < tokens.length; x++) {
      if (tokens[x].equals("")) {
        continue;
      }
      if (yearPart.equals("")) {
        yearPart = tokens[x];
      } else {
        if (zipEntryName.length() > 0) {
          zipEntryName += Constants.FILE_SEPARATOR;
        }
        zipEntryName += tokens[x];
      }
    }

    return zipEntryName;
  }

  public String parseMainFolderName(String serverName, String fileName, Integer idCoreFacility) {
    String mainFolderName = "";
    String baseDirLastPart = "";

    String experimentDirectory = this.getDirectory(serverName, idCoreFacility, PROPERTY_EXPERIMENT_DIRECTORY);
    String flowCellDirectory = this.getDirectory(serverName, null, PROPERTY_FLOWCELL_DIRECTORY);

      experimentDirectory = new File(experimentDirectory).getAbsolutePath().replace("\\", Constants.FILE_SEPARATOR);

      flowCellDirectory = new File(flowCellDirectory).getAbsolutePath().replace("\\", Constants.FILE_SEPARATOR);

    // Change all file separators to forward slash
    String theFileName = fileName.replaceAll("\\\\", Constants.FILE_SEPARATOR);
    experimentDirectory = experimentDirectory.replaceAll("\\\\", Constants.FILE_SEPARATOR);
    flowCellDirectory = flowCellDirectory.replaceAll("\\\\", Constants.FILE_SEPARATOR);

    // Parse out last part of experiment and flow cell path. We must isolate
    // this part because the canonical path of the file may not match the
    // experiment/flowcell directory (due to file links).
    String tokens[] = experimentDirectory.split(Constants.FILE_SEPARATOR);
    String experimentDirLastPart = tokens[tokens.length - 1];
    if (tokens == null || tokens.length == 0) {
      throw new RuntimeException("Cannot parse experiment directory into expected parts");
    }
    tokens = flowCellDirectory.split(Constants.FILE_SEPARATOR);
    if (tokens == null || tokens.length == 0) {
      throw new RuntimeException("Cannot parse flow cell directory into expected parts");
    }
    String flowCellDirLastPart = tokens[tokens.length - 1];

    if (theFileName.toLowerCase().indexOf(experimentDirLastPart.toLowerCase()) >= 0) {
      baseDirLastPart = experimentDirLastPart;
    } else if (theFileName.toLowerCase().indexOf(flowCellDirLastPart.toLowerCase()) >= 0) {
      baseDirLastPart = flowCellDirLastPart;
    } else {
      throw new RuntimeException("Cannot determine base directory.  Neither flowcell directory or experiment directory match file name " + fileName);
    }

    // Strip off the leading part of the path, up through the year subdirectory,
    // to leave only the path that starts with the request number subdirectory.
    tokens = theFileName.split(baseDirLastPart, 2);
    if (tokens == null || tokens.length < 2) {
      throw new RuntimeException("Cannot parse experiment directory into expected parts");
    }
    String lastFilePart = tokens[tokens.length - 1];

    tokens = lastFilePart.split(Constants.FILE_SEPARATOR);
    if (tokens == null || tokens.length < 2) {
      throw new RuntimeException("Cannot parse experiment directory into expected parts for year and number");
    }
    int x = 0;
    String year = "";
    for (x = 0; x < tokens.length; x++) {
      if (tokens[x].equals("")) {
        continue;
      }
      // First non-blank token is the year;
      // then second non-blank token is the
      // main folder name.
      if (year.equals("")) {
        year = tokens[x];
      } else if (mainFolderName.equals("")) {
        mainFolderName = tokens[x];
      }
    }

    return mainFolderName;
  }

  public static String addFileSepIfNec(String inputPath) {

    if (inputPath == null || inputPath.length() < 1) {
      return inputPath;
    }
    String tempStr = inputPath;
    // Change all file separators to forward slash
    tempStr = tempStr.replaceAll("\\\\", Constants.FILE_SEPARATOR);

    // And then see if there is a separator at the end of the string
    if (tempStr.charAt(tempStr.length() - 1) != Constants.FILE_SEPARATOR_CHAR) {
      inputPath = inputPath + Constants.FILE_SEPARATOR;
    }

    return inputPath;

  }

  // returns facility specific site logo. Note that this is static because it is
  // called before
  // properties are loaded.
  public static String getSiteLogo(Session sess, Integer idCoreFacility) {
    String siteLogo = "./assets/gnomex_logo.png";
    PropertyDictionary propSiteLogo = null;
    if (idCoreFacility != null) {
      Query propSiteQuery = sess.createQuery("from PropertyDictionary p where p.propertyName=:propName AND p.idCoreFacility = :idCoreFacility");
      propSiteQuery.setParameter("propName", PropertyDictionary.SITE_LOGO);
      propSiteQuery.setParameter("idCoreFacility", idCoreFacility);
      propSiteLogo = (PropertyDictionary) propSiteQuery.uniqueResult();
    }
    if (propSiteLogo == null) {
      Query propSiteQuery = sess.createQuery("from PropertyDictionary p where p.propertyName=:propName AND p.idCoreFacility is null");
      propSiteQuery.setParameter("propName", PropertyDictionary.SITE_LOGO);
      propSiteLogo = (PropertyDictionary) propSiteQuery.uniqueResult();
    }
    if (propSiteLogo != null && !propSiteLogo.getPropertyValue().equals("")) {
      siteLogo = "./" + propSiteLogo.getPropertyValue();
    }

    return siteLogo;
  }

  // returns facility specific site splash. Note that this is static because it
  // is called before
  // properties are loaded.
  public static String getSiteSplash(Session sess, Integer idCoreFacility) {
    String siteSplash = "./assets/gnomex_splash_logo.png";
    PropertyDictionary propSiteSplash = null;
    if (idCoreFacility != null) {
      Query propSiteQuery = sess.createQuery("from PropertyDictionary p where p.propertyName=:propName AND p.idCoreFacility = :idCoreFacility");
      propSiteQuery.setParameter("propName", PropertyDictionary.SITE_SPLASH);
      propSiteQuery.setParameter("idCoreFacility", idCoreFacility);
      propSiteSplash = (PropertyDictionary) propSiteQuery.uniqueResult();
    }
    if (propSiteSplash == null) {
      Query propSiteQuery = sess.createQuery("from PropertyDictionary p where p.propertyName=:propName AND p.idCoreFacility is null");
      propSiteQuery.setParameter("propName", PropertyDictionary.SITE_SPLASH);
      propSiteSplash = (PropertyDictionary) propSiteQuery.uniqueResult();
    }
    if (propSiteSplash != null && !propSiteSplash.getPropertyValue().equals("")) {
      siteSplash = "./" + propSiteSplash.getPropertyValue();
    }

    return siteSplash;
  }

  // returns facility specific site logo. Note that this is static because it is
  // called before
  // properties are loaded.
  public static String getExperimentAlias(Session sess, Integer idCoreFacility) {
    String experimentAlias = "Experiment";

    PropertyDictionary propexperimentAlias = null;
    String thePropertyAlias = "";
    if (idCoreFacility != null) {
      Query propSiteQuery = sess.createQuery("from PropertyDictionary p where p.propertyName=:propName AND p.idCoreFacility = :idCoreFacility");
      propSiteQuery.setParameter("propName", PropertyDictionary.EXPERIMENTALIAS);
      propSiteQuery.setParameter("idCoreFacility", idCoreFacility);
      propexperimentAlias = (PropertyDictionary) propSiteQuery.uniqueResult();
    }
    if (propexperimentAlias == null) {
      Query propSiteQuery = sess.createQuery("from PropertyDictionary p where p.propertyName=:propName AND p.idCoreFacility is null");
      propSiteQuery.setParameter("propName", PropertyDictionary.EXPERIMENTALIAS);
      propexperimentAlias = (PropertyDictionary) propSiteQuery.uniqueResult();
//      if (propexperimentAlias != null)
//      System.out.println ("[getExperimentAlias] " + propexperimentAlias.getPropertyValue());
//      else
//        System.out.println ("[getExperimentAlias] NO EXPERIMENT ALIAS PROVIDED");
    }

      if (propexperimentAlias != null && !propexperimentAlias.getPropertyValue().equals("")) {
          experimentAlias = propexperimentAlias.getPropertyValue();
      }

//      System.out.println ("[getExperimentAlias] " + experimentAlias);
      return experimentAlias;
  }

}
