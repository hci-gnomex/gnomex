package hci.gnomex.utility;

import hci.gnomex.model.AppUser;
import hci.gnomex.model.ArrayCoordinate;
import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.Hybridization;
import hci.gnomex.model.LabeledSample;
import hci.gnomex.model.Slide;
import hci.gnomex.model.WorkItem;
import hci.framework.model.DetailObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
