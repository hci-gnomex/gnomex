package hci.gnomex.lucene;

import java.util.Iterator;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;


public class IndexHelper {
  
  public static final String       SEARCH_RANK  = "searchRank";
  public static final String       SEARCH_SCORE = "searchScore";
  public static final String       SEARCH_INFO  = "searchInfo";
  
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
      String value = (String)indexedFieldMap.get(fieldName);
      
      if (value != null) {
        addIndexedField(doc, fieldName, value);        
      }
    }

  }
  
  private static void addIndexedField(Document doc, String name, String value) {
    if (value != null && !value.trim().equals("")) {
      doc.add( new Field(name, value, Field.Store.YES, Field.Index.TOKENIZED));          
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
