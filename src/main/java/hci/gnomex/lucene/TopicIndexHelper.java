package hci.gnomex.lucene;

import java.util.Iterator;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;


public class TopicIndexHelper extends IndexHelper {
  
  // Non-indexed fields
  public static final String       ID_TOPIC = "idTopic";
  public static final String       OWNER_FIRST_NAME = "topicOwnerFirstName";
  public static final String       OWNER_LAST_NAME = "topicOwnerLastName";
  public static final String       CREATE_DATE = "topicCreateDate";
  public static final String       PUBLIC_NOTE = "topicPublicNote";  
  
  // Indexed fields
  public static final String       TOPIC_NAME = "topicName";
  public static final String       DESCRIPTION = "topicDescription";
  public static final String       ID_LAB = "topicIdLab";
  public static final String       ID_INSTITUTION = "topicIdInstitution";
  public static final String       ID_APPUSER = "topicIdAppUser";
  public static final String       LAB_NAME = "topicLabName";
  public static final String       CODE_VISIBILITY = "topicCodeVisibility";
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
        doc.add( new Field(name, v.toLowerCase(), Field.Store.YES, Field.Index.TOKENIZED));
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
      doc.add( new Field(name, value.toLowerCase(), Field.Store.YES, Field.Index.NO));
    }
  }

  
}
