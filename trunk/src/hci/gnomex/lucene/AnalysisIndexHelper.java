package hci.gnomex.lucene;

import java.util.Iterator;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;


public class AnalysisIndexHelper extends IndexHelper {
  
  // Non-indexed fields
  public static final String       ID_ANALYSISGROUP = "idAnalysisGroup";
  public static final String       ID_ANALYSIS = "idAnalysis";
  public static final String       ID_LAB_ANALYSISGROUP = "idLabAnalysisGroup";
  public static final String       LAB_NAME_ANALYSISGROUP = "labNameAnalysisGroup";
  public static final String       ANALYSIS_NUMBER = "analysisNumber";
  public static final String       OWNER_FIRST_NAME = "ownerFirstName";
  public static final String       OWNER_LAST_NAME = "ownerLastName";
  public static final String       CREATE_DATE = "createDate";
  public static final String       PUBLIC_NOTE = "publicNote";  
  
  // Indexed fields
  public static final String       ANALYSIS_GROUP_NAME = "analysisGroupName";
  public static final String       ANALYSIS_GROUP_DESCRIPTION = "analysisGroupDescription";
  public static final String       ANALYSIS_NAME = "analysisName";
  public static final String       DESCRIPTION = "description";
  public static final String       ID_ORGANISM = "idOrganism";
  public static final String       ORGANISM = "organism";
  public static final String       ID_ANALYSIS_TYPE = "idAnalysisType";
  public static final String       ANALYSIS_TYPE = "analysisType";
  public static final String       ID_ANALYSIS_PROTOCOL = "idAnalysisProtocol";
  public static final String       ANALYSIS_PROTOCOL = "analysisProtocol";
  public static final String       ID_LAB = "idLab";
  public static final String       LAB_NAME = "labName";
  public static final String       CODE_VISIBILITY = "codeVisibility";
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
