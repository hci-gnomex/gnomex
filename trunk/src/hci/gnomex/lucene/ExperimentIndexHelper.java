package hci.gnomex.lucene;

import java.util.Iterator;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;


public class ExperimentIndexHelper extends IndexHelper {
  
  // Non-indexed fields
  public static final String       ID_PROJECT = "idProject";
  public static final String       ID_REQUEST = "idRequest";
  public static final String       REQUEST_NUMBER = "requestNumber";
  public static final String       DISPLAY_NAME = "requestDisplayName";
  public static final String       OWNER_FIRST_NAME = "requestOwnerFirstName";
  public static final String       OWNER_LAST_NAME = "requestOwnerLastName";
  public static final String       CREATE_DATE = "requestCreateDate";
  public static final String       MICROARRAY_CATEGORY = "microarrayCategory";
  public static final String       PROJECT_PUBLIC_NOTE = "projectPublicNote";
  public static final String       PUBLIC_NOTE = "requestPublicNote";  
  
  // Indexed fields
  public static final String       PROJECT_NAME = "projectName";
  public static final String       PROJECT_DESCRIPTION = "projectDescription";
  public static final String       HYB_NOTES = "hybNotes";
  public static final String       SAMPLE_NAMES = "sampleNames";
  public static final String       SAMPLE_DESCRIPTIONS = "sampleDescriptions";
  public static final String       SAMPLE_ORGANISMS = "sampleOrganisms";
  public static final String       ID_ORGANISM_SAMPLE = "idOrganismSamples";
  public static final String       ID_SAMPLE_TYPES = "idSampleTypes";
  public static final String       SAMPLE_SOURCES = "sampleSources";
  public static final String       ID_SAMPLE_SOURCES = "idSampleSources";
  public static final String       REQUEST_CATEGORY = "requestCategory";
  public static final String       CODE_REQUEST_CATEGORY = "codeRequestCategory";
  public static final String       CODE_MICROARRAY_CATEGORY = "codeMicroarrayCategory";
  public static final String       ID_SLIDE_PRODUCT = "idSlideProduct";
  public static final String       SLIDE_PRODUCT = "slideProduct";
  public static final String       SLIDE_PRODUCT_ORGANISM = "slideProductOrganism";
  public static final String       ID_ORGANISM_SLIDE_PRODUCT = "idOrganismSlideProduct";
  public static final String       ID_LAB_PROJECT = "projectIdLab";
  public static final String       PROJECT_LAB_NAME = "projectLab";
  public static final String       ID_LAB = "requestIdLab";
  public static final String       LAB_NAME = "requestLab";
  public static final String       CODE_VISIBILITY = "requestCodeVisibility";
  public static final String       CODE_EXPERIMENT_DESIGNS = "codeExperimentDesigns";
  public static final String       CODE_EXPERIMENT_FACTORS = "codeExperimentFactors";
  public static final String       SAMPLE_ANNOTATIONS = "sampleAnnotations";
  public static final String       PROJECT_ANNOTATIONS = "projectAnnotations";
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
