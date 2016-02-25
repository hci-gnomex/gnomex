package hci.gnomex.controller;

import hci.dictionary.utility.DictionaryManager;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.CoreFacility;
import hci.gnomex.model.Lab;
import hci.gnomex.model.UserPermissionKind;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

public class GetCoreFacilityLabList extends GNomExCommand implements Serializable {
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetCoreFacilityLabList.class);

  @Override
  public void loadCommand(HttpServletRequest req, HttpSession sess) {
    // TODO Auto-generated method stub

  }

  @Override
  public Command execute() throws RollBackCommandException {

    try{
      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
      List idCoreFacility = new ArrayList();

      // If we are super admin add all active core ids.  Otherwise get the core facilities that a normal admin manages.
      if(this.getSecAdvisor().getAppUser().getCodeUserPermissionKind().equals(UserPermissionKind.SUPER_ADMIN_PERMISSION_KIND)){
        for(Iterator i = CoreFacility.getActiveCoreFacilities(sess).iterator(); i.hasNext();){
          CoreFacility cf = (CoreFacility)i.next();
          idCoreFacility.add(cf.getIdCoreFacility());
        }

      } else{
        for(Iterator i = this.getSecAdvisor().getCoreFacilitiesIManage().iterator(); i.hasNext();){
          CoreFacility cf = (CoreFacility)i.next();
          idCoreFacility.add(cf.getIdCoreFacility());
        }

      }
      StringBuffer buf = new StringBuffer();
      buf.append(" SELECT distinct lab from Lab as lab ");
      buf.append(" JOIN lab.coreFacilities as cf ");
      buf.append(" WHERE cf.idCoreFacility IN (:ids) ");
      buf.append(" ORDER BY lab.lastName ");
      Query q = sess.createQuery(buf.toString());
      q.setParameterList("ids", idCoreFacility);

      List l = q.list();

      Document doc = new Document(new Element("LabList"));

      for(Iterator i = l.iterator(); i.hasNext();){
        Lab lab = (Lab)i.next();
        Element node = new Element("Lab");
        node.setAttribute("displayName", lab.getName(true, true));
        node.setAttribute("idLab", lab.getIdLab().toString());
        doc.getRootElement().addContent(node);
      }

      XMLOutputter out = new org.jdom.output.XMLOutputter();
      this.xmlResult = out.outputString(doc);

      setResponsePage(this.SUCCESS_JSP);
    }catch(Exception e){
      log.error("An exception has occurred in GetCoreFacilityLabList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());

    }finally{

      try {
        this.getSecAdvisor().closeReadOnlyHibernateSession();
      } catch (Exception e) {

      }

    }

    return this;
  }

  @Override
  public void validate() {
    // TODO Auto-generated method stub

  }

}
