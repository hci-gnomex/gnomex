package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.Application;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.model.SamplePrepMethod;
import hci.gnomex.model.SamplePrepMethodRequestCategory;
import hci.gnomex.model.SamplePrepMethodSampleType;
import hci.gnomex.model.SampleType;
import hci.gnomex.model.SampleTypeApplication;
import hci.gnomex.model.SampleTypeRequestCategory;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;

import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;




public class SaveExperimentPlatform extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveExperimentPlatform.class);
  
  private String                         sampleTypesXMLString;
  private Document                       sampleTypesDoc;
  
  private String                         samplePrepMethodsXMLString;
  private Document                       samplePrepMethodsDoc;
  
  private String                         applicationsXMLString;
  private Document                       applicationsDoc;

  private RequestCategory                rcScreen;
  private boolean                        isNewRequestCategory = false;

  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    rcScreen = new RequestCategory();
    HashMap errors = this.loadDetailObject(request, rcScreen);
    this.addInvalidFields(errors);
    if (rcScreen.getCodeRequestCategory() == null || rcScreen.getCodeRequestCategory().equals("")) {
      isNewRequestCategory = true;
    }
    

    if (request.getParameter("sampleTypesXMLString") != null && !request.getParameter("sampleTypesXMLString").equals("")) {
      sampleTypesXMLString = request.getParameter("sampleTypesXMLString");
      StringReader reader = new StringReader(sampleTypesXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        sampleTypesDoc = sax.build(reader);     
      } catch (JDOMException je ) {
        log.error( "Cannot parse sampleTypesXMLString", je );
        this.addInvalidField( "sampleTypesXMLString", "Invalid sampleTypesXMLString");
      }
    } 
    
    
    if (request.getParameter("samplePrepMethodsXMLString") != null && !request.getParameter("samplePrepMethodsXMLString").equals("")) {
      samplePrepMethodsXMLString = request.getParameter("samplePrepMethodsXMLString");
      StringReader reader = new StringReader(samplePrepMethodsXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        samplePrepMethodsDoc = sax.build(reader);     
      } catch (JDOMException je ) {
        log.error( "Cannot parse samplePrepMethodsXMLString", je );
        this.addInvalidField( "samplePrepMethodsXMLString", "Invalid samplePrepMethodsXMLString");
      }
    }
    
    if (request.getParameter("applicationsXMLString") != null && !request.getParameter("applicationsXMLString").equals("")) {
      applicationsXMLString = request.getParameter("applicationsXMLString");
      StringReader reader = new StringReader(applicationsXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        applicationsDoc = sax.build(reader);     
      } catch (JDOMException je ) {
        log.error( "Cannot parse applicationsXMLString", je );
        this.addInvalidField( "applicationsXMLString", "Invalid applicationsXMLString");
      }
    }
      

  }

  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      DictionaryHelper dh = DictionaryHelper.getInstance(sess);
      
      if (this.getSecurityAdvisor().hasPermission(SecurityAdvisor.CAN_SUBMIT_REQUESTS)) {

        
        RequestCategory rc = null;
              
        if (isNewRequestCategory) {
          rc = rcScreen;
          
          sess.save(rc);
        } else {
          
          rc = (RequestCategory)sess.load(RequestCategory.class, rcScreen.getCodeRequestCategory());
          
          initializeRequestCategory(rc);
        }
        
        sess.flush();
        
        saveSamplePrepMethods(sess, rc);
        saveSampleTypes(sess, rc);



        
        sess.flush();


        DictionaryHelper.reload(sess);
        
        this.xmlResult = "<SUCCESS codeRequestCategory=\"" + rc.getCodeRequestCategory() + "\"/>";
      
        setResponsePage(this.SUCCESS_JSP);
      } else {
        this.addInvalidField("Insufficient permissions", "Insufficient permission to save experiment platform.");
        setResponsePage(this.ERROR_JSP);
      }
      
    }catch (Exception e){
      log.error("An exception has occurred in SaveExperimentPlatform ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }finally {
      try {
        HibernateSession.closeSession();        
      } catch(Exception e) {
        
      }
    }
    
    return this;
  }
  
  private void initializeRequestCategory(RequestCategory rc) {
    rc.setRequestCategory(rcScreen.getRequestCategory());
    rc.setIsActive(rcScreen.getIsActive());
    rc.setType(rcScreen.getType());
    rc.setNotes(rcScreen.getNotes());
    rc.setSortOrder(rcScreen.getSortOrder());
    rc.setIdVendor(rcScreen.getIdVendor());
    rc.setIsInternal(rcScreen.getIsInternal());
    rc.setIsExternal(rcScreen.getIsExternal());
    rc.setIcon(rcScreen.getIcon());
    rc.setIdOrganism(rcScreen.getIdOrganism());
    rc.setNumberOfChannels(rcScreen.getNumberOfChannels());
    rc.setIsSampleBarcodingOptional(rcScreen.getIsSampleBarcodingOptional());
  }
  
  private void saveSampleTypes(Session sess, RequestCategory rc) {
    //
    // Save sampleTypes
    //
    HashMap sampleTypeMap = new HashMap();
    if (sampleTypesDoc != null) {
      for(Iterator i = this.sampleTypesDoc.getRootElement().getChildren().iterator(); i.hasNext();) {
        Element node = (Element)i.next();
        SampleType st =  null;
        
        String id = node.getAttributeValue("idSampleType");
        
        // Save new sample type or load the existing one
        if (id.startsWith("SampleType")) {
          st = new SampleType();
        } else {
          st = (SampleType) sess.load(SampleType.class, Integer.valueOf(id));
        }
        
        st.setSampleType(node.getAttributeValue("display"));
        st.setIsActive(node.getAttributeValue("isActive"));
        sess.save(st);
        
        //
        // Save assocation between sample type and request category
        //
        List existingsAssociations = sess.createQuery("SELECT x from SampleTypeRequestCategory x where idSampleType = " + st.getIdSampleType()).list();
        HashMap<String, SampleTypeRequestCategory> existingPlatformMap = new HashMap<String, SampleTypeRequestCategory>();
        for (Iterator i1 = existingsAssociations.iterator(); i1.hasNext();) {
          SampleTypeRequestCategory x = (SampleTypeRequestCategory)i1.next();
          existingPlatformMap.put(x.getCodeRequestCategory(), x);
        } 
        if (node.getAttributeValue("isSelected").equals("Y")) {
          if (!existingPlatformMap.containsKey(rc.getCodeRequestCategory())) {
            SampleTypeRequestCategory x = new SampleTypeRequestCategory();
            x.setIdSampleType(st.getIdSampleType());
            x.setCodeRequestCategory(rc.getCodeRequestCategory());
            sess.save(x);
          }
        } else {
          if (existingPlatformMap.containsKey(rc.getCodeRequestCategory())) {
            SampleTypeRequestCategory x = existingPlatformMap.get(rc.getCodeRequestCategory());
            sess.delete(x);
          }
        }
        
        //
        // Save association between sample types and applications
        //
        String codeApplications = node.getAttributeValue("codeApplications");
        existingsAssociations = sess.createQuery("SELECT x from SampleTypeApplication x where idSampleType = " + st.getIdSampleType()).list();
        HashMap<String, SampleTypeApplication> existingApplicationMap = new HashMap<String, SampleTypeApplication>();
        for (Iterator i1 = existingsAssociations.iterator(); i1.hasNext();) {
          SampleTypeApplication sta = (SampleTypeApplication)i1.next();
          existingApplicationMap.put(sta.getCodeApplication(), sta);
        }
        HashMap<String, Application> applicationMap = new HashMap<String, Application>();
        if (codeApplications != null && !codeApplications.equals("")) {
          String[] tokens = codeApplications.split(",");
          for (int x = 0; x < tokens.length; x++) {
            String codeApplication = tokens[x];
            applicationMap.put(codeApplication, null);
          }
        }
        // Add associations
        for (Iterator i1 = applicationMap.keySet().iterator(); i1.hasNext();) {
          String codeApplication = (String)i1.next();
          if (!existingApplicationMap.containsKey(codeApplication)) {
            SampleTypeApplication sta = new SampleTypeApplication();
            sta.setIdSampleType(st.getIdSampleType());
            sta.setCodeApplication(codeApplication);
            sess.save(sta);
          }
        }
        
        // Remove associations
        for (Iterator i1 = existingApplicationMap.keySet().iterator(); i1.hasNext();) {
          String codeApplication = (String)i1.next();
          if (!applicationMap.containsKey(codeApplication)) {
            SampleTypeApplication sta = existingApplicationMap.get(codeApplication);
            sess.delete(sta);
          }
        }
        
        
        //
        // Save association between sample types and sample prep methods
        //
        String idSamplePrepMethods = node.getAttributeValue("idSamplePrepMethods");
        existingsAssociations = sess.createQuery("SELECT x from SamplePrepMethodSampleType x where idSampleType = " + st.getIdSampleType()).list();
        HashMap<Integer, SamplePrepMethodSampleType> existingMethodMap = new HashMap<Integer, SamplePrepMethodSampleType>();
        for (Iterator i1 = existingsAssociations.iterator(); i1.hasNext();) {
          SamplePrepMethodSampleType x = (SamplePrepMethodSampleType)i1.next();
          existingMethodMap.put(x.getIdSamplePrepMethod(), x);
        }
        HashMap<Integer, SamplePrepMethod> methodMap = new HashMap<Integer, SamplePrepMethod>();
        if (idSamplePrepMethods != null && !idSamplePrepMethods.equals("")) {
          String[] tokens = idSamplePrepMethods.split(",");
          for (int x = 0; x < tokens.length; x++) {
            String idSamplePrepMethodString = tokens[x];
            methodMap.put(Integer.valueOf(idSamplePrepMethodString), null);
          }
        }
        // Add associations
        for (Iterator i1 = methodMap.keySet().iterator(); i1.hasNext();) {
          Integer idSamplePrepMethod = (Integer)i1.next();
          if (!existingMethodMap.containsKey(idSamplePrepMethod)) {
            SamplePrepMethodSampleType sta = new SamplePrepMethodSampleType();
            sta.setIdSampleType(st.getIdSampleType());
            sta.setIdSamplePrepMethod(idSamplePrepMethod);
            sess.save(sta);
          }
        }
        
        // Remove associations
        for (Iterator i1 = existingMethodMap.keySet().iterator(); i1.hasNext();) {
          Integer idSamplePrepMethod = (Integer)i1.next();
          if (!methodMap.containsKey(idSamplePrepMethod)) {
            SamplePrepMethodSampleType sta = existingMethodMap.get(idSamplePrepMethod);
            sess.delete(sta);
          }
        }

        
        
        sess.flush();
        sampleTypeMap.put(st.getIdSampleType(), null);
      }
    }
    

    
    
    // Remove sample types
    for (Iterator i = sess.createQuery("SELECT st from SampleType st").list().iterator(); i.hasNext();) {
      SampleType sampleType = (SampleType)i.next();
      if (!sampleTypeMap.containsKey(sampleType.getIdSampleType())) {
        sampleType.setIsActive("N");
        
        // Remove associations
        List existingsAssociations = sess.createQuery("SELECT x from SampleTypeApplication x where idSampleType = " + sampleType.getIdSampleType()).list();
        for(Iterator i1 = existingsAssociations.iterator(); i1.hasNext();) {
          SampleTypeApplication x = (SampleTypeApplication)i1.next();
          sess.delete(x);
        }
        existingsAssociations = sess.createQuery("SELECT x from SamplePrepMethodSampleType x where idSampleType = " + sampleType.getIdSampleType()).list();
        for(Iterator i1 = existingsAssociations.iterator(); i1.hasNext();) {
          SamplePrepMethodSampleType x = (SamplePrepMethodSampleType)i1.next();
          sess.delete(x);
        }
        existingsAssociations = sess.createQuery("SELECT x from SampleTypeRequestCategory x where idSampleType = " + sampleType.getIdSampleType()).list();
        for(Iterator i1 = existingsAssociations.iterator(); i1.hasNext();) {
          SampleTypeRequestCategory x = (SampleTypeRequestCategory)i1.next();
          sess.delete(x);
        }
      }
    }    
  }
  
  private void saveSamplePrepMethods(Session sess, RequestCategory rc) {
    //
    // Save samplePrepMethods
    //
    HashMap samplePrepMethodMap = new HashMap();
    if (samplePrepMethodsDoc != null) {
      for(Iterator i = this.samplePrepMethodsDoc.getRootElement().getChildren().iterator(); i.hasNext();) {
        Element node = (Element)i.next();
        SamplePrepMethod spm =  null;
        
        String id = node.getAttributeValue("idSamplePrepMethod");
        
        // Save new sample type or load the existing one
        if (id.startsWith("SamplePrepMethod")) {
          spm = new SamplePrepMethod();
        } else {
          spm = (SamplePrepMethod) sess.load(SamplePrepMethod.class, Integer.valueOf(id));
        }
        
        spm.setSamplePrepMethod(node.getAttributeValue("display"));
        spm.setIsActive(node.getAttributeValue("isActive"));
        sess.save(spm);
        
        //
        // Save assocation between sample prep method and request category
        //
        List existingsAssociations = sess.createQuery("SELECT x from SamplePrepMethodRequestCategory x where idSamplePrepMethod = " + spm.getIdSamplePrepMethod()).list();
        HashMap<String, SamplePrepMethodRequestCategory> existingPlatformMap = new HashMap<String, SamplePrepMethodRequestCategory>();
        for (Iterator i1 = existingsAssociations.iterator(); i1.hasNext();) {
          SamplePrepMethodRequestCategory x = (SamplePrepMethodRequestCategory)i1.next();
          existingPlatformMap.put(x.getCodeRequestCategory(), x);
        } 
        if (node.getAttributeValue("isSelected").equals("Y")) {
          if (!existingPlatformMap.containsKey(rc.getCodeRequestCategory())) {
            SamplePrepMethodRequestCategory x = new SamplePrepMethodRequestCategory();
            x.setIdSamplePrepMethod(spm.getIdSamplePrepMethod());
            x.setCodeRequestCategory(rc.getCodeRequestCategory());
            sess.save(x);
          }
        } else {
          if (existingPlatformMap.containsKey(rc.getCodeRequestCategory())) {
            SamplePrepMethodRequestCategory x = existingPlatformMap.get(rc.getCodeRequestCategory());
            sess.delete(x);
          }
        }
        
        
        sess.flush();
        samplePrepMethodMap.put(spm.getIdSamplePrepMethod(), null);
      }
    }
    

    
    
    // Remove sample types
    for (Iterator i = sess.createQuery("SELECT sp from SamplePrepMethod sp").list().iterator(); i.hasNext();) {
      SamplePrepMethod samplePrepMethod = (SamplePrepMethod)i.next();
      if (!samplePrepMethodMap.containsKey(samplePrepMethod.getIdSamplePrepMethod())) {
        samplePrepMethod.setIsActive("N");
        
        // Remove associations
        List existingsAssociations = sess.createQuery("SELECT x from SamplePrepMethodSampleType x where idSamplePrepMethod = " + samplePrepMethod.getIdSamplePrepMethod()).list();
        for(Iterator i1 = existingsAssociations.iterator(); i1.hasNext();) {
          SamplePrepMethodSampleType x = (SamplePrepMethodSampleType)i1.next();
          sess.delete(x);
        }
        
        existingsAssociations = sess.createQuery("SELECT x from SamplePrepMethodRequestCategory x where idSamplePrepMethod = " + samplePrepMethod.getIdSamplePrepMethod()).list();
        for(Iterator i1 = existingsAssociations.iterator(); i1.hasNext();) {
          SamplePrepMethodRequestCategory x = (SamplePrepMethodRequestCategory)i1.next();
          sess.delete(x);
        }
      }
    }    
  }
  

}