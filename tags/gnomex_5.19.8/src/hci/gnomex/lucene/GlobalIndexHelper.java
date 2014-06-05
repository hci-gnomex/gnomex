package hci.gnomex.lucene;



public class GlobalIndexHelper extends IndexHelper {
  
  // Non-indexed fields
  public static final String       NUMBER = "globalNumber";
  public static final String       NAME = "globalName";
  public static final String       CODE_REQUEST_CATEGORY = "globalCodeRequestCategory";
  public static final String       PROTOCOL_CLASS_NAME = "globalProtocolClassName";


  // Indexed fields
  public static final String       OBJECT_TYPE = "globalObjectType";
  public static final String       ID = "globalId";
  public static final String       ID_LAB = "globalIdLab";
  public static final String       ID_ORGANISM = "globalIdOrganism";
  public static final String       LAB_NAME = "globalLabName";
  public static final String       CODE_VISIBILITY = "globalCodeVisibility";
  public static final String       ID_PROJECT_CORE_FACILITY = "globalIdProjectCoreFacility";
  public static final String       ID_PROJECT = "globalIdProject";
  public static final String       TEXT = "text";
  
  // Indexed fields included only for security
  public static final String       ID_INSTITUTION = "globalIdInstitution";
  public static final String       ID_CORE_FACILITY = "globalIdCoreFacility";
  public static final String       COLLABORATORS = "globalCollaborators";
  public static final String       ID_APPUSER = "globalIdAppUser";
  public static final String       ID_LAB_FOLDER = "globalIdLabFolder"; 

  // Object types
  public static final String       EXPERIMENT = "globalObjectTypeExperiment";
  public static final String       PROJECT_FOLDER = "globalObjectTypeProjectFolder";
  public static final String       PROTOCOL = "globalObjectTypeProtocol";
  public static final String       ANALYSIS = "globalObjectTypeAnalysis";
  public static final String       DATA_TRACK = "globalObjectTypeDataTrack";
  public static final String       TOPIC = "globalObjectTypeTopic";
  

}
