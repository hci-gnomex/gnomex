package hci.gnomex.utility;

import hci.framework.model.DetailObject;
import hci.gnomex.model.AppUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;


public class LabMemberParser extends DetailObject implements Serializable {

  protected Document   doc;
  protected Map        appUserMap = new HashMap();
  protected ArrayList  newMemberEmailList = new ArrayList();

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


      if(node.getAttributeValue("newMember") != null && node.getAttributeValue("newMember").equals("Y")) {
        newMemberEmailList.add(appUser.getEmail());
      }
    }
  }


  public Map getAppUserMap() {
    return appUserMap;
  }

  public ArrayList getNewMemberEmailList() {
    return newMemberEmailList;
  }
}
