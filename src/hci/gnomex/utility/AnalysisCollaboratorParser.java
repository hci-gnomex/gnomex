package hci.gnomex.utility;

import hci.framework.model.DetailObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;


public class AnalysisCollaboratorParser extends DetailObject implements Serializable {
  
  protected Document    doc;
  protected List        idCollaboratorList = new ArrayList();
  
  public AnalysisCollaboratorParser(Document doc) {
    this.doc = doc;
 
  }
  
  public void parse(Session sess) throws Exception{
    
    Element root = this.doc.getRootElement();
    
    
    for(Iterator i = root.getChildren("AppUser").iterator(); i.hasNext();) {
      Element node = (Element)i.next();
      
      String idAppUserString = node.getAttributeValue("idAppUser");
      Integer idAppUser = Integer.valueOf(idAppUserString);
      
      
      
      idCollaboratorList.add(idAppUser);
    }
  }

  
  public List getIdCollaboratorList() {
    return idCollaboratorList;
  }

}
