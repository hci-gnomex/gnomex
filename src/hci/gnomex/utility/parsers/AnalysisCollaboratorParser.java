package hci.gnomex.utility.parsers;

import hci.framework.model.DetailObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;


public class AnalysisCollaboratorParser extends DetailObject implements Serializable {
  
  protected Document    doc;
  protected HashMap     collaboratorUploadMap = new HashMap();
  protected HashMap     collaboratorUpdateMap = new HashMap();
  
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
      String canUpdate = node.getAttributeValue("canUpdate");

      collaboratorUploadMap.put(idAppUser, canUploadData);
      collaboratorUpdateMap.put(idAppUser, canUpdate);
    }
  }

  
  public HashMap getCollaboratorUploadMap() {
    return collaboratorUploadMap;
  }
  
  public HashMap getCollaboratorUpdateMap() {
    return collaboratorUpdateMap;
  }

}
