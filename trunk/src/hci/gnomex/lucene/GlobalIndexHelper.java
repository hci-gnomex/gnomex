package hci.gnomex.lucene;

import java.util.Iterator;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;


public class GlobalIndexHelper extends IndexHelper {
  
  // Non-indexed fields
  public static final String       OBJECT_TYPE = "globalObjectType";
  public static final String       ID = "globalId";
  public static final String       NUMBER = "globalNumber";
  public static final String       NAME = "globalName";
  public static final String       CODE_VISIBILITY = "globalCodeVisibility";
  public static final String       CODE_REQUEST_CATEGORY = "globalCodeRequestCategory";
  public static final String       PROTOCOL_CLASS_NAME = "globalProtocolClassName";

  // Indexed fields
  public static final String       ID_LAB = "globalIdLab";
  public static final String       ID_ORGANISM = "globalIdOrganism";
  public static final String       LAB_NAME = "globalLabName";
  public static final String       TEXT = "text";

  // Object types
  public static final String       PROJECT_FOLDER = "globalObjectTypeProjectFolder";
  public static final String       PROTOCOL = "globalObjectTypeProtocol";
  public static final String       ANALYSIS = "globalObjectTypeAnalysis";
  public static final String       DATA_TRACK = "globalObjectTypeDataTrack";
  public static final String       TOPIC = "globalObjectTypeTopic";
  

}
