package hci.gnomex.utility;

import hci.framework.model.DetailObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;


public class AnalysisCollaboratorParser extends DetailObject implements Serializable {
  
  protected Document    doc;
  protected HashMap     collaboratorMap = new HashMap();
  
  public AnalysisCollaboratorParser(Document doc) {
    this.doc = doc;
 
  }
  
  public void parse(Session sess) throws Exception{
    
    Element root = this.doc.getRootElement();
    
    
    for(Iterator i = root.getChildren("AnalysisCollaborator").iterator(); i.hasNext();) {
      Element node = (Element)i.next();
      
      String idAppUserString = node.getAttributeValue("idAppUser");
      Integer idAppUser = Integer.valueOf(idAppUserString);
      String canUploadData = node.getAttributeValue("canUploadData");

      collaboratorMap.put(idAppUser, canUploadData);
    }
  }

  
  public HashMap getCollaboratorMap() {
    return collaboratorMap;
  }

}
