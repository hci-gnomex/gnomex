package hci.gnomex.utility;

import hci.framework.model.DetailObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;

public class SampleSheetColumnNamesParser extends DetailObject implements Serializable {
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  private Document                    doc;
  private List<String[]>              columnList;
  
  public static final int             PROPERTY_NAME_IDX = 0;
  public static final int             GRID_LABEL_IDX = 1;
  
  public SampleSheetColumnNamesParser(Document doc) {
    this.doc = doc;
  }
  
  public void parse(Session sess) {
    Element listNode = this.doc.getRootElement();
    columnList = new ArrayList<String[]>();

    for(Iterator i = listNode.getChildren("FieldItem").iterator(); i.hasNext();) {
      Element node = (Element)i.next();

      String gridLabel = node.getAttributeValue("fieldText");
      String propertyName = node.getAttributeValue("dataField");
      if (propertyName.startsWith("@")) {
        propertyName = propertyName.substring(1);
      }
      
      String[] names = new String[2];
      names[PROPERTY_NAME_IDX] = propertyName;
      names[GRID_LABEL_IDX] = gridLabel;
      columnList.add(names);
    }
  }
  
  public List<String[]> getColumnList() {
    return columnList;
  }
}
