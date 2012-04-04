package hci.gnomex.lucene;

import java.util.Iterator;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;


public class DataTrackIndexHelper extends IndexHelper {
  
  // Non-indexed fields
  public static final String       ID_DATATRACKFOLDER = "idDataTrackFolder";
  public static final String       ID_DATATRACK = "idDataTrack";
  public static final String       ID_LAB_DATATRACKFOLDER = "idLabDataTrackFolder";
  public static final String       LAB_NAME_DATATRACKFOLDER = "labNameDataTrackFolder";
  public static final String       OWNER_FIRST_NAME = "dataTrackOwnerFirstName";
  public static final String       OWNER_LAST_NAME = "dataTrackOwnerLastName";
  public static final String       CREATE_DATE = "dataTrackCreateDate";
  public static final String       PUBLIC_NOTE = "dataTrackPublicNote";  
  
  // Indexed fields
  public static final String       DATATRACK_FOLDER_NAME = "dataTrackFolderName";
  public static final String       DATATRACK_FOLDER_DESCRIPTION = "dataTrackFolderDescription";
  public static final String       DATATRACK_NAME = "datatrackName";
  public static final String       DESCRIPTION = "dataTrackDescription";
  public static final String       FILE_NAME = "dataTrackFileName";
  public static final String       SUMMARY = "dataTrackSummary";
  public static final String       ID_LAB = "dataTrackIdLab";
  public static final String       ID_ORGANISM = "dataTrackIdOrganism";
  public static final String       ID_INSTITUTION = "dataTrackIdInstitution";
  public static final String       ID_APPUSER = "dataTrackIdAppUser";
  public static final String       COLLABORATORS = "dataTrackCollaborators";
  public static final String       LAB_NAME = "dataTrackLabName";
  public static final String       CODE_VISIBILITY = "dataTrackCodeVisibility";
  public static final String       DATA_TRACK_ANNOTATIONS = "dataTrackAnnotations";
  public static final String       TEXT = "text";

  
  public static void build(Document doc, Map nonIndexedFieldMap, Map indexedFieldMap) {

    //
    // Add non-indexed fields
    //
    for(Iterator i = nonIndexedFieldMap.keySet().iterator(); i.hasNext();) {
      String fieldName = (String)i.next();
      String value = (String)nonIndexedFieldMap.get(fieldName);
      
      if (value != null) {
        addNonIndexedField(doc, fieldName, value);        
      }
    }
    
    //
    // Add indexed fields
    //
    for(Iterator i = indexedFieldMap.keySet().iterator(); i.hasNext();) {
      String fieldName = (String)i.next();
      Object value = (Object)indexedFieldMap.get(fieldName);
      
      if (value != null) {
        addIndexedField(doc, fieldName, value);        
      }
    }

  }
  
  private static void addIndexedField(Document doc, String name, Object value) {
    if (value instanceof String) {
      String v = (String)value;
      if (v != null && !v.trim().equals("")) {
        doc.add( new Field(name, v, Field.Store.YES, Field.Index.TOKENIZED));          
      }
    } else if (value instanceof Integer) {
      Integer v = (Integer)value;
      if (v != null) {
        doc.add( new Field(name, v.toString(), Field.Store.YES, Field.Index.TOKENIZED));          
      }
    } else {
      throw new RuntimeException("unknown value type " + value.getClass() + " " + value);
    }
  }
  private static void addIndexedField(Document doc, String name, Integer value) {
    if (value != null) {
      doc.add( new Field(name, value.toString(), Field.Store.YES, Field.Index.UN_TOKENIZED));          
    }
  }

  private static void addNonIndexedField(Document doc, String name, String value) {
    if (value != null && !value.trim().equals("")) {
      doc.add( new Field(name, value, Field.Store.YES, Field.Index.NO));          
    }
  }

  
}
