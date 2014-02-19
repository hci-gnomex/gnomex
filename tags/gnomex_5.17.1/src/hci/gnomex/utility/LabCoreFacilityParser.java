package hci.gnomex.utility;

import hci.framework.model.DetailObject;
import hci.gnomex.model.CoreFacility;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;


public class LabCoreFacilityParser extends DetailObject implements Serializable {
  
  protected Document   doc;
  protected Map        coreFacilityMap = new HashMap();
  
  public LabCoreFacilityParser(Document doc) {
    this.doc = doc;
 
  }
  
  public void parse(Session sess) throws Exception{
    
    Element root = this.doc.getRootElement();
    
    
    for(Iterator i = root.getChildren("CoreFacility").iterator(); i.hasNext();) {
      Element node = (Element)i.next();
      
      String idCoreFacilityString = node.getAttributeValue("idCoreFacility");
      CoreFacility facility = (CoreFacility)sess.get(CoreFacility.class, new Integer(idCoreFacilityString));
      
      coreFacilityMap.put(facility.getIdCoreFacility(), facility);
    }
  }

  
  public Map getCoreFacilityMap() {
    return coreFacilityMap;
  }
}
