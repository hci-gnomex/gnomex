package hci.gnomex.utility;

import hci.framework.model.DetailObject;
import hci.gnomex.model.AppUser;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;


public class LabMemberParser extends DetailObject implements Serializable {
  
  protected Document   doc;
  protected Map        appUserMap = new HashMap();
  
  public LabMemberParser(Document doc) {
    this.doc = doc;
 
  }
  
  public void parse(Session sess) throws Exception{
    
    Element root = this.doc.getRootElement();
    
    
    for(Iterator i = root.getChildren("AppUser").iterator(); i.hasNext();) {
      Element node = (Element)i.next();
      
      String idAppUserString = node.getAttributeValue("idAppUser");
      AppUser appUser = (AppUser)sess.get(AppUser.class, new Integer(idAppUserString));
      
      appUserMap.put(appUser.getIdAppUser(), appUser);
    }
  }

  
  public Map getAppUserMap() {
    return appUserMap;
  }
}