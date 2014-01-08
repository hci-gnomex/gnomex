package hci.gnomex.utility;

import hci.framework.model.DetailObject;
import hci.gnomex.model.Institution;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;


public class LabInstitutionParser extends DetailObject implements Serializable {
  
  protected Document   doc;
  protected Map        institutionMap = new HashMap();
  
  public LabInstitutionParser(Document doc) {
    this.doc = doc;
 
  }
  
  public void parse(Session sess) throws Exception{
    
    Element root = this.doc.getRootElement();
    
    
    for(Iterator i = root.getChildren("Institution").iterator(); i.hasNext();) {
      Element node = (Element)i.next();
      
      String idInstitutionString = node.getAttributeValue("idInstitution");
      Institution institution = (Institution)sess.get(Institution.class, new Integer(idInstitutionString));
      
      institutionMap.put(institution.getIdInstitution(), institution);
    }
  }

  
  public Map getInstititionMap() {
    return institutionMap;
  }
}
