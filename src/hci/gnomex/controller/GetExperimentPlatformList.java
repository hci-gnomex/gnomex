package hci.gnomex.controller;

import hci.dictionary.utility.DictionaryManager;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.model.Application;
import hci.gnomex.model.Price;
import hci.gnomex.model.PriceCategory;
import hci.gnomex.model.PriceCriteria;
import hci.gnomex.model.PriceSheet;
import hci.gnomex.model.PriceSheetPriceCategory;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.model.RequestCategoryApplication;
import hci.gnomex.model.SampleCharacteristic;
import hci.gnomex.model.SamplePrepMethod;
import hci.gnomex.model.SamplePrepMethodRequestCategory;
import hci.gnomex.model.SamplePrepMethodSampleType;
import hci.gnomex.model.SampleType;
import hci.gnomex.model.SampleTypeRequestCategory;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;


public class GetExperimentPlatformList extends GNomExCommand implements Serializable {
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetExperimentPlatformList.class);

  private List<SampleType> sampleTypes = new ArrayList<SampleType>();
  private List <SamplePrepMethod> samplePrepMethods = new ArrayList<SamplePrepMethod>();
  private List <Application> applications = new ArrayList<Application>();
  private HashMap<String, Map<Integer, ?>> samplePrepMethodMap = new HashMap<String, Map<Integer, ?>>();
  private HashMap<String, Map<Integer, ?>> sampleTypeMap = new HashMap<String, Map<Integer, ?>>();
  private HashMap<String, Map<String, ?>> applicationMap = new HashMap<String, Map<String, ?>>();
  private HashMap<Integer, Map<Integer, ?>> sampleTypeXMethodMap = new HashMap<Integer, Map<Integer, ?>>();
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    
    if (isValid()) {
      setResponsePage(this.SUCCESS_JSP);
    } else {
      setResponsePage(this.ERROR_JSP);
    }

  }

  public Command execute() throws RollBackCommandException {

    try {

      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());

      hashSupportingDictionaries(sess);

      
      Document doc = new Document(new Element("ExperimentPlatformList"));

      List platforms = sess.createQuery("SELECT rc from RequestCategory rc order by rc.requestCategory").list();

      for(Iterator i = platforms.iterator(); i.hasNext();) {
        RequestCategory rc = (RequestCategory)i.next();
        this.getSecAdvisor().flagPermissions(rc);
        Element node = rc.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();
        doc.getRootElement().addContent(node);
        
        Element listNode = new Element("sampleTypes");
        node.addContent(listNode);
        for(Iterator i1 = sampleTypes.iterator(); i1.hasNext();) {
          SampleType st = (SampleType)i1.next();
          this.getSecAdvisor().flagPermissions(st);
          Element sampleTypeNode = st.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();
          listNode.addContent(sampleTypeNode);
          sampleTypeNode.setAttribute("isSelected", isAssociated(rc, st) ? "Y" : "N");
          sampleTypeNode.setAttribute("idSamplePrepMethods", getIdSamplePrepMethods(st));
          sampleTypeNode.setAttribute("samplePrepMethods", getSamplePrepMethods(st));
        }
        
        listNode = new Element("samplePrepMethods");
        node.addContent(listNode);
        for(Iterator i1 = samplePrepMethods.iterator(); i1.hasNext();) {
          SamplePrepMethod sp = (SamplePrepMethod)i1.next();
          this.getSecAdvisor().flagPermissions(sp);
          Element samplePrepMethodNode = sp.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();
          listNode.addContent(samplePrepMethodNode);
          samplePrepMethodNode.setAttribute("isSelected", isAssociated(rc, sp) ? "Y" : "N");
        }
        
        
        listNode = new Element("applications");
        node.addContent(listNode);
        for(Iterator i1 = applications.iterator(); i1.hasNext();) {
          Application a = (Application)i1.next();
          this.getSecAdvisor().flagPermissions(a);
          Element applicationNode = a.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();
          listNode.addContent(applicationNode);
          applicationNode.setAttribute("isSelected", isAssociated(rc, a) ? "Y" : "N");
        }
               
      }

      org.jdom.output.XMLOutputter out = new org.jdom.output.XMLOutputter();
      this.xmlResult = out.outputString(doc);

      setResponsePage(this.SUCCESS_JSP);
    }catch (NamingException e){
      log.error("An exception has occurred in GetExperimentPlatformList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }catch (SQLException e) {
      log.error("An exception has occurred in GetExperimentPlatformList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (XMLReflectException e){
      log.error("An exception has occurred in GetExperimentPlatformList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e) {
      log.error("An exception has occurred in GetExperimentPlatformList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        this.getSecAdvisor().closeReadOnlyHibernateSession();        
      } catch(Exception e) {
        
      }
    }

    if (isValid()) {
      setResponsePage(this.SUCCESS_JSP);
    } else {
      setResponsePage(this.ERROR_JSP);
    }
    
    return this;
  }
  
  private boolean isAssociated(RequestCategory rc, SampleType st) {
    Map idMap = sampleTypeMap.get(rc.getCodeRequestCategory());
    return idMap != null && idMap.containsKey(st.getIdSampleType());
  }
  
  private boolean isAssociated(RequestCategory rc, SamplePrepMethod sp) {
    Map idMap = samplePrepMethodMap.get(rc.getCodeRequestCategory());
    return idMap != null && idMap.containsKey(sp.getIdSamplePrepMethod());
  }
  
  private boolean isAssociated(RequestCategory rc, Application a) {
    Map idMap = applicationMap.get(rc.getCodeRequestCategory());
    return idMap != null && idMap.containsKey(a.getCodeApplication());
  }
  
  private String getIdSamplePrepMethods(SampleType st) {
    String buf = "";
    Map idMap = sampleTypeXMethodMap.get(st.getIdSampleType());
    for(Iterator i = idMap.keySet().iterator(); i.hasNext();) {
      Integer id = (Integer)i.next();
      if (buf.length() > 0) {
        buf += ",";
      }
      buf += id.toString();
    }
    return buf;
  }
  
  private String getSamplePrepMethods(SampleType st) {
    String buf = "";
    Map idMap = sampleTypeXMethodMap.get(st.getIdSampleType());
    for(Iterator i = idMap.keySet().iterator(); i.hasNext();) {
      Integer id = (Integer)i.next();
      if (buf.length() > 0) {
        buf += ",";
      }
      SamplePrepMethod samplePrepMethod = null;
      for(Iterator i1 = samplePrepMethods.iterator(); i1.hasNext();) {
        SamplePrepMethod spm = (SamplePrepMethod)i1.next();
        if (spm.getIdSamplePrepMethod().equals(id)) {
          samplePrepMethod = spm;
          break;
        }
      }
      if (samplePrepMethod != null) {
        buf += samplePrepMethod.getSamplePrepMethod();
      }
    }
    return buf;
  }

  private void hashSupportingDictionaries(Session sess) throws Exception {
    sampleTypes = sess.createQuery("SELECT st from SampleType st order by st.sampleType").list();
    List sampleTypeXrefs = sess.createQuery("SELECT x from SampleTypeRequestCategory x").list();
    for(Iterator i = sampleTypeXrefs.iterator(); i.hasNext();) {
      SampleTypeRequestCategory x = (SampleTypeRequestCategory)i.next();
      Map idMap = (Map)sampleTypeMap.get(x.getCodeRequestCategory());
      if (idMap == null) {
        idMap = new HashMap();
      }
      idMap.put(x.getIdSampleType(), null);
      sampleTypeMap.put(x.getCodeRequestCategory(), idMap);
    }
    
    samplePrepMethods = sess.createQuery("SELECT sp from SamplePrepMethod sp order by sp.samplePrepMethod").list();
    List samplePrepMethodXrefs = sess.createQuery("SELECT x from SamplePrepMethodRequestCategory x").list();
    for(Iterator i = samplePrepMethodXrefs.iterator(); i.hasNext();) {
      SamplePrepMethodRequestCategory x = (SamplePrepMethodRequestCategory)i.next();
      Map idMap = (Map)samplePrepMethodMap.get(x.getCodeRequestCategory());
      if (idMap == null) {
        idMap = new HashMap();
      }
      idMap.put(x.getIdSamplePrepMethod(), null);
      samplePrepMethodMap.put(x.getCodeRequestCategory(), idMap);
    }
    
    applications = sess.createQuery("SELECT a from Application a order by a.application").list();
    List applicationXrefs = sess.createQuery("SELECT x from RequestCategoryApplication x").list();
    for(Iterator i = applicationXrefs.iterator(); i.hasNext();) {
      RequestCategoryApplication x = (RequestCategoryApplication)i.next();
      Map idMap = (Map)applicationMap.get(x.getCodeRequestCategory());
      if (idMap == null) {
        idMap = new HashMap();
      }
      idMap.put(x.getCodeApplication(), null);
      applicationMap.put(x.getCodeRequestCategory(), idMap);
    }
    
    List sampleTypeXMethods = sess.createQuery("SELECT x from SamplePrepMethodSampleType x").list();
    for(Iterator i = sampleTypeXMethods.iterator(); i.hasNext();) {
      SamplePrepMethodSampleType x = (SamplePrepMethodSampleType)i.next();
      Map idMap = (Map)sampleTypeXMethodMap.get(x.getIdSampleType());
      if (idMap == null) {
        idMap = new HashMap();
      }
      idMap.put(x.getIdSamplePrepMethod(), null);
      sampleTypeXMethodMap.put(x.getIdSampleType(), idMap);
    }
  }
}