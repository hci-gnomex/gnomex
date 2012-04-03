package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.Application;
import hci.gnomex.model.NumberSequencingCycles;
import hci.gnomex.model.NumberSequencingCyclesAllowed;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.model.RequestCategoryApplication;
import hci.gnomex.model.SampleType;
import hci.gnomex.model.SampleTypeApplication;
import hci.gnomex.model.SampleTypeRequestCategory;
import hci.gnomex.model.SeqLibProtocol;
import hci.gnomex.model.SeqLibProtocolApplication;
import hci.gnomex.model.SeqRunType;
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
  
  private String                         applicationsXMLString;
  private Document                       applicationsDoc;

  private String                         sequencingOptionsXMLString;
  private Document                       sequencingOptionsDoc; 
  
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
    

    if (request.getParameter("sequencingOptionsXMLString") != null && !request.getParameter("sequencingOptionsXMLString").equals("")) {
      sequencingOptionsXMLString = request.getParameter("sequencingOptionsXMLString");
      StringReader reader = new StringReader(sequencingOptionsXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        sequencingOptionsDoc = sax.build(reader);     
      } catch (JDOMException je ) {
        log.error( "Cannot parse sequencingOptionsXMLString", je );
        this.addInvalidField( "sequencingOptionsXMLString", "Invalid sequencingOptionsXMLString");
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
          rc.setCodeRequestCategory("EXP" + this.getNextAssignedRequestCategoryNumber(sess));
          
          sess.save(rc);
        } else {
          
          rc = (RequestCategory)sess.load(RequestCategory.class, rcScreen.getCodeRequestCategory());
          
          initializeRequestCategory(rc);
        }
        
        sess.flush();
        
        saveApplications(sess, rc);
        saveSampleTypes(sess, rc);
        saveSequencingOptions(sess, rc);



        
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
    if (sampleTypesDoc == null || sampleTypesDoc.getRootElement().getChildren().size() == 0) {
      return;
    }
    //
    // Save sampleTypes
    //
    HashMap sampleTypeMap = new HashMap();

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
      // Save association between sample type and request category
      //
      List existingAssociations = sess.createQuery("SELECT x from SampleTypeRequestCategory x where idSampleType = " + st.getIdSampleType() + " and x.codeRequestCategory = '" + rc.getCodeRequestCategory() + "'").list();

      if (node.getAttributeValue("isSelected").equals("Y")) {
        if (existingAssociations.size()  == 0) {
          SampleTypeRequestCategory x = new SampleTypeRequestCategory();
          x.setIdSampleType(st.getIdSampleType());
          x.setCodeRequestCategory(rc.getCodeRequestCategory());
          sess.save(x);
        }
      } else {
        for (Iterator ix = existingAssociations.iterator(); ix.hasNext();) {
          SampleTypeRequestCategory x = (SampleTypeRequestCategory)ix.next();
          sess.delete(x);
        }
      }

      //
      // Save association between sample types and applications
      //
      String codeApplications = node.getAttributeValue("codeApplications");
      existingAssociations = sess.createQuery("SELECT x from SampleTypeApplication x where idSampleType = " + st.getIdSampleType()).list();
      HashMap<String, SampleTypeApplication> existingApplicationMap = new HashMap<String, SampleTypeApplication>();
      for (Iterator i1 = existingAssociations.iterator(); i1.hasNext();) {
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

      sess.flush();
      sampleTypeMap.put(st.getIdSampleType(), null);
    }
    

    
    
    // Remove sample types
    for (Iterator i = sess.createQuery("SELECT st from SampleType st").list().iterator(); i.hasNext();) {
      SampleType sampleType = (SampleType)i.next();
      if (!sampleTypeMap.containsKey(sampleType.getIdSampleType())) {
        boolean deleteSampleType = true;
        List samples = sess.createQuery("select count(*) from Sample s where s.idSampleType = " + sampleType.getIdSampleType()).list();
        if (samples.size() > 0) {
          Integer count = (Integer)samples.get(0);
          if (count.intValue() > 0) {
            deleteSampleType = false;
          }
        }
        
        // Remove associations
        List existingsAssociations = sess.createQuery("SELECT x from SampleTypeApplication x where idSampleType = " + sampleType.getIdSampleType()).list();
        for(Iterator i1 = existingsAssociations.iterator(); i1.hasNext();) {
          SampleTypeApplication x = (SampleTypeApplication)i1.next();
          sess.delete(x);
        }
        
        if (deleteSampleType) {
          sess.delete(sampleType);
        } else {
          sampleType.setIsActive("N");
        }
      }
    }  
    sess.flush();
  }
    
  private void saveApplications(Session sess, RequestCategory rc) {
    if (applicationsDoc == null || applicationsDoc.getRootElement().getChildren().size() == 0) {
      return;
    }

    //
    // Save applications
    //
    HashMap applicationMap = new HashMap();
    for(Iterator i = this.applicationsDoc.getRootElement().getChildren().iterator(); i.hasNext();) {
      Element node = (Element)i.next();
      Application app =  null;

      String codeApplication = node.getAttributeValue("codeApplication");

      // Save new application or load the existing one
      if (codeApplication.startsWith("Application")) {
        app = new Application();
        app.setCodeApplication("APP" + getNextAssignedAppNumber(sess));
      } else {
        app = (Application) sess.load(Application.class, codeApplication);
      }

      String idLabelingProtocolDefault = node.getAttributeValue("idLabelingProtocolDefault");
      String idHybProtocolDefault = node.getAttributeValue("idHybProtocolDefault");
      String idScanProtocolDefault = node.getAttributeValue("idScanProtocolDefault");
      String idFeatureExtractionProtocolDefault = node.getAttributeValue("idFeatureExtractionProtocolDefault");
      String idApplicationTheme = node.getAttributeValue("idApplicationTheme");

      app.setApplication(node.getAttributeValue("display"));
      app.setIsActive(node.getAttributeValue("isActive"));
      app.setIdApplicationTheme(idApplicationTheme != null && !idApplicationTheme.equals("") ? Integer.valueOf(idApplicationTheme) : null);
      sess.save(app);

      applicationMap.put(app.getCodeApplication(), null);

      //
      // Save association between application and request category
      //
      List existingAssociations = sess.createQuery("SELECT x from RequestCategoryApplication x where codeApplication = '" + app.getCodeApplication() + "' and x.codeRequestCategory = '" + rc.getCodeRequestCategory() + "'").list();
      if (node.getAttributeValue("isSelected").equals("Y")) {
        if (existingAssociations.size() == 0) {
          RequestCategoryApplication x = new RequestCategoryApplication();
          x.setCodeApplication(app.getCodeApplication());
          x.setCodeRequestCategory(rc.getCodeRequestCategory());
          x.setIdLabelingProtocolDefault(idLabelingProtocolDefault != null && !idLabelingProtocolDefault.equals("") ? Integer.valueOf(idLabelingProtocolDefault) : null);
          x.setIdHybProtocolDefault(idHybProtocolDefault != null && !idHybProtocolDefault.equals("") ? Integer.valueOf(idHybProtocolDefault) : null);
          x.setIdScanProtocolDefault(idScanProtocolDefault != null && !idScanProtocolDefault.equals("") ? Integer.valueOf(idScanProtocolDefault) : null);
          x.setIdFeatureExtractionProtocolDefault(idFeatureExtractionProtocolDefault != null && !idFeatureExtractionProtocolDefault.equals("") ? Integer.valueOf(idFeatureExtractionProtocolDefault) : null);
          sess.save(x);
        } else {
          for(Iterator ix = existingAssociations.iterator(); ix.hasNext();) {
            RequestCategoryApplication x = (RequestCategoryApplication)ix.next();
            x.setIdLabelingProtocolDefault(idLabelingProtocolDefault != null && !idLabelingProtocolDefault.equals("") ? Integer.valueOf(idLabelingProtocolDefault) : null);
            x.setIdHybProtocolDefault(idHybProtocolDefault != null && !idHybProtocolDefault.equals("") ? Integer.valueOf(idHybProtocolDefault) : null);
            x.setIdScanProtocolDefault(idScanProtocolDefault != null && !idScanProtocolDefault.equals("") ? Integer.valueOf(idScanProtocolDefault) : null);
            x.setIdFeatureExtractionProtocolDefault(idFeatureExtractionProtocolDefault != null && !idFeatureExtractionProtocolDefault.equals("") ? Integer.valueOf(idFeatureExtractionProtocolDefault) : null);
            sess.save(x);
          }
        }
      } else {
        for(Iterator ix = existingAssociations.iterator(); ix.hasNext();) {
          RequestCategoryApplication x = (RequestCategoryApplication)ix.next();
          sess.delete(x);
        }
        sess.flush();
      }

      //
      // Save association between applications and seq lib protocols
      //
      String idSeqLibProtocols = node.getAttributeValue("idSeqLibProtocols");
      existingAssociations = sess.createQuery("SELECT x from SeqLibProtocolApplication x where codeApplication = '" + app.getCodeApplication() + "'").list();
      HashMap<Integer, SeqLibProtocolApplication> existingProtocolMap = new HashMap<Integer, SeqLibProtocolApplication>();
      for (Iterator i1 = existingAssociations.iterator(); i1.hasNext();) {
        SeqLibProtocolApplication x = (SeqLibProtocolApplication)i1.next();
        existingProtocolMap.put(x.getIdSeqLibProtocol(), x);
      }
      HashMap<Integer, SeqLibProtocol> protocolMap = new HashMap<Integer, SeqLibProtocol>();
      if (idSeqLibProtocols != null && !idSeqLibProtocols.equals("")) {
        String[] tokens = idSeqLibProtocols.split(",");
        for (int x = 0; x < tokens.length; x++) {
          String idSeqLibProtocolString = tokens[x];
          protocolMap.put(Integer.valueOf(idSeqLibProtocolString), null);
        }
      }
      // Add associations
      for (Iterator i1 = protocolMap.keySet().iterator(); i1.hasNext();) {
        Integer idSeqLibProtocol = (Integer)i1.next();
        if (!existingProtocolMap.containsKey(idSeqLibProtocol)) {
          SeqLibProtocolApplication x = new SeqLibProtocolApplication();
          x.setCodeApplication(app.getCodeApplication());
          x.setIdSeqLibProtocol(idSeqLibProtocol);
          sess.save(x);
        }
      }

      // Remove associations
      for (Iterator i1 = existingProtocolMap.keySet().iterator(); i1.hasNext();) {
        Integer idSeqLibProtocol = (Integer)i1.next();
        if (!protocolMap.containsKey(idSeqLibProtocol)) {
          SeqLibProtocolApplication x = existingProtocolMap.get(idSeqLibProtocol);
          sess.delete(x);
        }
      }



      sess.flush();
    }

    // Remove applications
    for (Iterator i = sess.createQuery("SELECT a from Application a").list().iterator(); i.hasNext();) {
      Application application = (Application)i.next();
      if (!applicationMap.containsKey(application.getCodeApplication())) {

        boolean deleteApplication = true;
        List experiments = sess.createQuery("select count(*)from Request r where r.codeApplication = '" + application.getCodeApplication() + "'").list();
        if (experiments.size() > 0) {
          Integer count = (Integer)experiments.get(0);
          if (count.intValue() > 0) {
            deleteApplication = false;
          }
        }

        // Remove associations
        List existingsAssociations = sess.createQuery("SELECT x from SampleTypeApplication x where codeApplication = '" + application.getCodeApplication() + "'").list();
        for(Iterator i1 = existingsAssociations.iterator(); i1.hasNext();) {
          SampleTypeApplication x = (SampleTypeApplication)i1.next();
          sess.delete(x);
        }
        existingsAssociations = sess.createQuery("SELECT x from SeqLibProtocolApplication x where codeApplication = '" + application.getCodeApplication() + "'").list();
        for(Iterator i1 = existingsAssociations.iterator(); i1.hasNext();) {
          SeqLibProtocolApplication x = (SeqLibProtocolApplication)i1.next();
          sess.delete(x);
        }
        existingsAssociations = sess.createQuery("SELECT x from RequestCategoryApplication x where codeApplication = '" + application.getCodeApplication() + "'").list();
        for(Iterator i1 = existingsAssociations.iterator(); i1.hasNext();) {
          RequestCategoryApplication x = (RequestCategoryApplication)i1.next();
          sess.delete(x);
        }

        // Remove or inactivate application
        if (deleteApplication) {
          sess.delete(application);
        } else {
          application.setIsActive("N");          
        }

      }
    }    
    sess.flush();

  }
  
  private void saveSequencingOptions(Session sess, RequestCategory rc) {
    
    if (sequencingOptionsDoc == null || sequencingOptionsDoc.getRootElement().getChildren().size() == 0) {
      return;
    }
    
    
    DictionaryHelper dh = DictionaryHelper.getInstance(sess);
    
    Integer idSeqRunTypePaired = null;
    Integer idSeqRunTypeSingle = null;
    for(Iterator i = dh.getSeqRunTypeList().iterator(); i.hasNext();) {
      SeqRunType srt = (SeqRunType)i.next();
      if (srt.getSeqRunType().indexOf("Paired") > -1) {
        idSeqRunTypePaired = srt.getIdSeqRunType();
      }
      if (srt.getSeqRunType().indexOf("Single") > -1) {
        idSeqRunTypeSingle = srt.getIdSeqRunType();
      }
    }
    

    //
    // Save numberSequencingCycles
    //
    HashMap numberSequencingCyclesMap = new HashMap();
    for(Iterator i = this.sequencingOptionsDoc.getRootElement().getChildren().iterator(); i.hasNext();) {
      Element node = (Element)i.next();
      NumberSequencingCycles cycles =  null;

      String idNumberSequencingCycles = node.getAttributeValue("idNumberSequencingCycles");

      // Save new numberSequencingCycles or load the existing one
      if (idNumberSequencingCycles.startsWith("NumberSequencingCycles")) {
        cycles = new NumberSequencingCycles();
      } else {
        cycles = (NumberSequencingCycles) sess.load(NumberSequencingCycles.class, Integer.valueOf(idNumberSequencingCycles));
      }

      String paired = node.getAttributeValue("paired");
      String pairedName = node.getAttributeValue("pairedName");
      String pairedNote = node.getAttributeValue("pairedNote");
      String single = node.getAttributeValue("single");
      String singleName = node.getAttributeValue("singleName");
      String singleNote = node.getAttributeValue("singleNote");

      List idSeqRunTypes = new ArrayList();
      idSeqRunTypes.add(idSeqRunTypePaired);
      idSeqRunTypes.add(idSeqRunTypeSingle);

      cycles.setNumberSequencingCycles(Integer.valueOf(node.getAttributeValue("display")));
      cycles.setIsActive(node.getAttributeValue("isActive"));
      sess.save(cycles);

      numberSequencingCyclesMap.put(cycles.getIdNumberSequencingCycles(), null);

      //
      // Save association between numberSequencingCycles and request category (NumberSequencingCyclesAllowed)
      //
      for (Iterator isr = idSeqRunTypes.iterator(); isr.hasNext();) {
        Integer idSeqRunType = (Integer)isr.next();

        List existingAssociations = sess.createQuery("SELECT x from NumberSequencingCyclesAllowed x where x.idNumberSequencingCycles = " + cycles.getIdNumberSequencingCycles() + " and x.idSeqRunType = " + idSeqRunType + " and x.codeRequestCategory ='" + rc.getCodeRequestCategory() + "'").list();
        if ((paired.equals("Y") && idSeqRunType == idSeqRunTypePaired) || 
            (single.equals("Y") && idSeqRunType == idSeqRunTypeSingle)) {
          if (existingAssociations.size() == 0) {
            NumberSequencingCyclesAllowed x = new NumberSequencingCyclesAllowed();
            x.setIdNumberSequencingCycles(cycles.getIdNumberSequencingCycles());
            x.setCodeRequestCategory(rc.getCodeRequestCategory());
            x.setIdSeqRunType(idSeqRunType);
            x.setName(idSeqRunType == idSeqRunTypePaired ? pairedName : singleName);
            x.setNotes(idSeqRunType == idSeqRunTypePaired ? pairedNote : singleNote);
            sess.save(x);
          } else {
            for (Iterator iex = existingAssociations.iterator(); iex.hasNext();) {
              NumberSequencingCyclesAllowed x = (NumberSequencingCyclesAllowed)iex.next();
              x.setName(idSeqRunType == idSeqRunTypePaired ? pairedName : singleName);
              x.setNotes(idSeqRunType == idSeqRunTypePaired ? pairedNote : singleNote);
              sess.save(x);
            }
          }
        } else {
          for (Iterator iex = existingAssociations.iterator(); iex.hasNext();) {
            NumberSequencingCyclesAllowed x = (NumberSequencingCyclesAllowed)iex.next();
            sess.delete(x);
          }
        }
        sess.flush();

      }
      
    }




    // Remove numberSequencingCycles
    for (Iterator i = sess.createQuery("SELECT a from NumberSequencingCycles a").list().iterator(); i.hasNext();) {
      NumberSequencingCycles numberSequencingCycles = (NumberSequencingCycles)i.next();
      if (!numberSequencingCyclesMap.containsKey(numberSequencingCycles.getIdNumberSequencingCycles())) {
        
        boolean deleteNumberSequencingCycles = true;
        List lanes = sess.createQuery("select count(*)from SequenceLane r where r.idNumberSequencingCycles = " + numberSequencingCycles.getIdNumberSequencingCycles()).list();
        if (lanes.size() > 0) {
          Integer count = (Integer)lanes.get(0);
          if (count.intValue() > 0) {
            deleteNumberSequencingCycles = false;
          }
        }

        // Remove associations
        List existingsAssociations = sess.createQuery("SELECT x from NumberSequencingCyclesAllowed x where idNumberSequencingCycles = " + numberSequencingCycles.getIdNumberSequencingCycles()).list();
        for(Iterator i1 = existingsAssociations.iterator(); i1.hasNext();) {
          NumberSequencingCyclesAllowed x = (NumberSequencingCyclesAllowed)i1.next();
          sess.delete(x);
        }
        
        // Remove or inactivate numberSequencingCycles
        if (deleteNumberSequencingCycles) {
          sess.delete(numberSequencingCycles);
        } else {
          numberSequencingCycles.setIsActive("N");          
        }

      }
    }    
    sess.flush();
  }

  
  
  private Integer getNextAssignedAppNumber(Session sess) {
    int lastNumber = 0;
    List apps = sess.createQuery("SELECT a.codeApplication from Application a where a.codeApplication like 'APP[0-9]'").list();
    for(Iterator i = apps.iterator(); i.hasNext();) {
      String codeApplication = (String)i.next();
      String theNumber = codeApplication.substring(3);
      try {
        int number = Integer.parseInt(theNumber);
        if (number > lastNumber) {
          lastNumber = number;
        }
      } catch (Exception e) {
      }
    }
    return lastNumber + 1;
  }
  
  private Integer getNextAssignedRequestCategoryNumber(Session sess) {
    int lastNumber = 0;
    List requestCategories = sess.createQuery("SELECT rc.codeRequestCategory from RequestCategory rc where rc.codeRequestCategory like 'EXP[0-9]'").list();
    for(Iterator i = requestCategories.iterator(); i.hasNext();) {
      String codeRequestCategory = (String)i.next();
      String theNumber = codeRequestCategory.substring(3);
      try {
        int number = Integer.parseInt(theNumber);
        if (number > lastNumber) {
          lastNumber = number;
        }
      } catch (Exception e) {
      }
    }
    return lastNumber + 1;
  }
  
  

}