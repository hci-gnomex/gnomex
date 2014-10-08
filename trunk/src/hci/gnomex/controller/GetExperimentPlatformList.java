package hci.gnomex.controller;

import hci.dictionary.model.NullDictionaryEntry;
import hci.dictionary.utility.DictionaryManager;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.model.Application;
import hci.gnomex.model.ApplicationTheme;
import hci.gnomex.model.ApplicationType;
import hci.gnomex.model.CoreFacility;
import hci.gnomex.model.NumberSequencingCyclesAllowed;
import hci.gnomex.model.Price;
import hci.gnomex.model.PriceCriteria;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.model.RequestCategoryApplication;
import hci.gnomex.model.RequestCategoryType;
import hci.gnomex.model.SampleType;
import hci.gnomex.model.SampleTypeRequestCategory;
import hci.gnomex.model.SeqLibProtocolApplication;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;


public class GetExperimentPlatformList extends GNomExCommand implements Serializable {
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetExperimentPlatformList.class);

  private List<SampleType> sampleTypes = new ArrayList<SampleType>();
  private List <Application> applications = new ArrayList<Application>();
  private HashMap<String, Map<Integer, ?>> sampleTypeMap = new HashMap<String, Map<Integer, ?>>();
  private HashMap<String, Map<String, RequestCategoryApplication>> applicationMap = new HashMap<String, Map<String, RequestCategoryApplication>>();
  private HashMap<Integer, Map<Integer, ?>> sampleTypeXMethodMap = new HashMap<Integer, Map<Integer, ?>>();
  private HashMap<String, Map<Integer, ?>> applicationXSeqLibProtocolMap = new HashMap<String, Map<Integer, ?>>();
  private HashMap<String, List<NumberSequencingCyclesAllowed>> numberSeqCyclesAllowedMap = new HashMap<String, List<NumberSequencingCyclesAllowed>>();
  private HashMap<Integer, ApplicationTheme> applicationThemeMap = new HashMap<Integer, ApplicationTheme>();
  private Map<String, List<Element>> applicationToRequestCategoryMap = new HashMap<String, List<Element>>();
  
  private final static String PRICE_INTERNAL            = "internal";
  private final static String PRICE_EXTERNAL_ACADEMIC   = "academic";
  private final static String PRICE_EXTERNAL_COMMERCIAL = "commercial";
      
  
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

      DictionaryHelper dh = DictionaryHelper.getInstance(sess);
      hashSupportingDictionaries(sess, dh);
      
      Document doc = new Document(new Element("ExperimentPlatformList"));

      List platforms = sess.createQuery("SELECT rc from RequestCategory rc order by rc.requestCategory").list();

      List<ApplicationTheme> emptyThemes = this.getEmptyApplicationThemes(sess);
      Map<String, Integer> seqLibProtocolToBarcodeSchemeMap = this.getSeqLibProtocolBarCodeSchemeMap(sess);
      Map<String, Price> applicationToLibPrepPriceMap = getApplicationToLibPrepPriceMap(sess);
      Map<String, Price> seqOptionsToPriceMap = getSeqOptionsToPriceMap(sess);
      Map<String, String> requestCategoryToPriceSheetMap = getRequestCategoryToPriceSheetMap(sess);
      for(Iterator i = platforms.iterator(); i.hasNext();) {
        RequestCategory rc = (RequestCategory)i.next();
        this.getSecAdvisor().flagPermissions(rc);
        Element node = rc.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();
        node.setAttribute("canEnterPrices", requestCategoryToPriceSheetMap.containsKey(rc.getCodeRequestCategory()) ? "Y" : "N");
        doc.getRootElement().addContent(node);
        
        Element listNode = new Element("sampleTypes");
        node.addContent(listNode);
        for(Iterator i1 = sampleTypes.iterator(); i1.hasNext();) {
          SampleType st = (SampleType)i1.next();
          this.getSecAdvisor().flagPermissions(st);
          Element sampleTypeNode = st.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();
          if (st.getCodeNucleotideType() == null) {
            sampleTypeNode.setAttribute("codeNucleotideType", "DNA");
          }
          listNode.addContent(sampleTypeNode);
          sampleTypeNode.setAttribute("isSelected", isAssociated(rc, st) ? "Y" : "N");
        }
        
        
        listNode = new Element("applications");
        node.addContent(listNode);
        RequestCategoryType rct = dh.getRequestCategoryType(rc.getType());
        Element parentNode = listNode; 
        String prevTheme = "UnInitialized";
        Integer prevThemeId = -1;
        DictionaryManager dm;
        Boolean foundSelected = false;
        applicationToRequestCategoryMap = this.getApplicationRequestCategoryMap(sess, dh);
        for(Application a : this.getApplications(rc, rct)) {
          this.getSecAdvisor().flagPermissions(a);
          ApplicationTheme theme = applicationThemeMap.get(a.getIdApplicationTheme());
          String curTheme = (theme == null ? (a.getIdApplicationTheme() == null ? "" : a.getIdApplicationTheme().toString()) : theme.getApplicationTheme());
          Integer curThemeId = a.getIdApplicationTheme();
          if (!prevTheme.equals(curTheme) && rct.getIsIllumina() != null && rct.getIsIllumina().equals("Y")) {
            if (parentNode != listNode) {
              parentNode.setAttribute("isSelected", foundSelected ? "Y" : "N");
            }
            Element themeNode = new Element("ApplicationTheme");
            themeNode.setAttribute("applicationTheme", curTheme == null ? "" : curTheme);
            themeNode.setAttribute("idApplicationTheme", curThemeId == null ? "" : curThemeId.toString());
            themeNode.setAttribute("sortOrder", theme == null ? "-1" : theme.getSortOrder().toString());
            parentNode = themeNode;
            listNode.addContent(themeNode);
            prevTheme = curTheme;
            prevThemeId = curThemeId;
            foundSelected = false;
          }
          if (isAssociated(rc, a)) {
            foundSelected = true;
          }
          Element applicationNode = a.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();
          parentNode.addContent(applicationNode);
          applicationNode.setAttribute("applicationThemeDisplay", curTheme);
          applicationNode.setAttribute("applicationThemeSortOrder", theme == null ? "-1" : theme.getSortOrder().toString());
          applicationNode.setAttribute("isSelected", isAssociated(rc, a) ? "Y" : "N");
          applicationNode.setAttribute("idSeqLibProtocols", getIdSeqLibProtocols(a));
          RequestCategoryApplication x = (RequestCategoryApplication)getRequestCategoryApplication(rc, a);
          applicationNode.setAttribute("idLabelingProtocolDefault", x != null && x.getIdLabelingProtocolDefault() != null ? x.getIdLabelingProtocolDefault().toString() : "");
          applicationNode.setAttribute("idHybProtocolDefault", x != null && x.getIdHybProtocolDefault() != null ? x.getIdHybProtocolDefault().toString() : "");
          applicationNode.setAttribute("idScanProtocolDefault", x != null && x.getIdScanProtocolDefault() != null ? x.getIdScanProtocolDefault().toString() : "");
          applicationNode.setAttribute("idFeatureExtractionProtocolDefault", x != null && x.getIdFeatureExtractionProtocolDefault() != null ? x.getIdFeatureExtractionProtocolDefault().toString() : "");
          applicationNode.setAttribute("selectedInOtherCategory", "N");
          applicationNode.setAttribute("idBarcodeSchemeA", getIdBarcodeScheme(seqLibProtocolToBarcodeSchemeMap, a, "A"));
          applicationNode.setAttribute("idBarcodeSchemeB", getIdBarcodeScheme(seqLibProtocolToBarcodeSchemeMap, a, "B"));
          applicationNode.setAttribute("unitPriceInternal", getUnitPrice(applicationToLibPrepPriceMap, a, rc, this.PRICE_INTERNAL));
          applicationNode.setAttribute("unitPriceExternalAcademic", getUnitPrice(applicationToLibPrepPriceMap, a, rc, this.PRICE_EXTERNAL_ACADEMIC));
          applicationNode.setAttribute("unitPriceExternalCommercial", getUnitPrice(applicationToLibPrepPriceMap, a, rc, this.PRICE_EXTERNAL_COMMERCIAL));
          
          List<Element> rcAppList = this.applicationToRequestCategoryMap.get(a.getCodeApplication());
          if (rcAppList != null) {
            for(Element rcAppNode : rcAppList) {
              String rcAppCodeRequestCategory = rcAppNode.getAttributeValue("codeRequestCategory");
              String isSelected = rcAppNode.getAttributeValue("isSelected");
              if (!rc.getCodeRequestCategory().equals(rcAppCodeRequestCategory) && isSelected.equals("Y")) {
                applicationNode.setAttribute("selectedInOtherCategory", "Y");
              }
              applicationNode.addContent(rcAppNode);
            }
          }
        }
        // add empty themes so we can add applications to them.
        if (rct.getIsIllumina() != null && rct.getIsIllumina().equals("Y")) {
          for(ApplicationTheme t : emptyThemes) {
            Element themeNode = new Element("ApplicationTheme");
            themeNode.setAttribute("applicationTheme", t.getApplicationTheme());
            themeNode.setAttribute("idApplicationTheme", t.getIdApplicationTheme().toString());
            listNode.addContent(themeNode);
          }
        }
        
        if (parentNode != listNode) {
          parentNode.setAttribute("isSelected", foundSelected ? "Y" : "N");
        }

        listNode = new Element("sequencingOptions");
        node.addContent(listNode);
        List<NumberSequencingCyclesAllowed> allowedList = this.numberSeqCyclesAllowedMap.get(rc.getCodeRequestCategory());
        if (allowedList != null) {
          for(NumberSequencingCyclesAllowed c : allowedList) {
            this.getSecAdvisor().flagPermissions(c);
            Element cycleNode = c.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();
            cycleNode.setAttribute("unitPriceInternal", getUnitPrice(seqOptionsToPriceMap, c, rc, this.PRICE_INTERNAL));
            cycleNode.setAttribute("unitPriceExternalAcademic", getUnitPrice(seqOptionsToPriceMap, c, rc, this.PRICE_EXTERNAL_ACADEMIC));
            cycleNode.setAttribute("unitPriceExternalCommercial", getUnitPrice(seqOptionsToPriceMap, c, rc, this.PRICE_EXTERNAL_COMMERCIAL));
            listNode.addContent(cycleNode);
          }
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
  
  private boolean isAssociated(RequestCategory rc, Application a) {
    if (a.getIsActive() == null || !a.getIsActive().equals("Y")) {
      return false;
    } else {
      Map idMap = applicationMap.get(rc.getCodeRequestCategory());
      return idMap != null && idMap.containsKey(a.getCodeApplication());
    }
  }
  
  private RequestCategoryApplication getRequestCategoryApplication(RequestCategory rc, Application a) {
    Map<String, RequestCategoryApplication> idMap = applicationMap.get(rc.getCodeRequestCategory());
    if (idMap != null && idMap.containsKey(a.getCodeApplication())) {
      return idMap.get(a.getCodeApplication());
    } else {
      return null;
    }
  }
  
  private String getIdSeqLibProtocols(Application app) {
    String buf = "";
    Map idMap = applicationXSeqLibProtocolMap.get(app.getCodeApplication());
    if (idMap != null) {
      for(Iterator i = idMap.keySet().iterator(); i.hasNext();) {
        Integer id = (Integer)i.next();
        if (buf.length() > 0) {
          buf += ",";
        }
        buf += id.toString();
      }
    }
    return buf;
  }
  
  private String getIdBarcodeScheme(Map<String, Integer> map, Application a, String type) {
    String idBarcodeScheme = "";
    Map idMap = applicationXSeqLibProtocolMap.get(a.getCodeApplication());
    if (idMap != null) {
      for(Iterator i = idMap.keySet().iterator(); i.hasNext();) {
        String key = ((Integer)i.next()).toString() + "\t" + type;
        Integer id = map.get(key);
        if (id != null) {
          idBarcodeScheme = id.toString();
          break;
        }
      }
    }
    return idBarcodeScheme;
  }
  
  private String getUnitPrice(Map<String, Price> applicationToLibPrepPriceMap, Application a, RequestCategory rc, String priceType) {
    String priceAsString = "";
    String key = rc.getCodeRequestCategory() + "\t" + a.getCodeApplication();
    return getUnitPrice(applicationToLibPrepPriceMap.get(key), priceType);
  }
  
  private String getUnitPrice(Map<String, Price> seqOptionsToPriceMap, NumberSequencingCyclesAllowed n, RequestCategory rc, String priceType) {
    String key = rc.getCodeRequestCategory() + "\t" + n.getIdNumberSequencingCyclesAllowed().toString();
    return getUnitPrice(seqOptionsToPriceMap.get(key), priceType);
  }

  private String getUnitPrice(Price p, String priceType) {
    String priceAsString = "";
    if (p == null) {
      priceAsString = "";
    } else {
      if (priceType.equals(this.PRICE_INTERNAL)) {
        priceAsString = p.getUnitPrice().toString();
      } else if (priceType.equals(this.PRICE_EXTERNAL_ACADEMIC)) {
        priceAsString = p.getUnitPriceExternalAcademic().toString();
      } else if (priceType.equals(this.PRICE_EXTERNAL_COMMERCIAL)) {
        priceAsString = p.getUnitPriceExternalCommercial().toString();
      } else {
        priceAsString = "";
      }
    }
    
    return priceAsString;
  }
  
  private void hashSupportingDictionaries(Session sess, DictionaryHelper dh) throws Exception {
    sampleTypes = sess.createQuery("SELECT st from SampleType st where isActive is null OR isActive = 'Y' order by st.sampleType").list();
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
    
    applications = sess.createQuery("SELECT a from Application a order by a.application").list();
    List applicationXrefs = sess.createQuery("SELECT x from RequestCategoryApplication x").list();
    for(Iterator i = applicationXrefs.iterator(); i.hasNext();) {
      RequestCategoryApplication x = (RequestCategoryApplication)i.next();
      Map idMap = (Map)applicationMap.get(x.getCodeRequestCategory());
      if (idMap == null) {
        idMap = new HashMap();
      }
      idMap.put(x.getCodeApplication(), x);
      applicationMap.put(x.getCodeRequestCategory(), idMap);
    }
    
    List applicationXSeqLibProtocols = sess.createQuery("SELECT x from SeqLibProtocolApplication x").list();
    for(Iterator i = applicationXSeqLibProtocols.iterator(); i.hasNext();) {
      SeqLibProtocolApplication x = (SeqLibProtocolApplication)i.next();
      Map idMap = (Map)applicationXSeqLibProtocolMap.get(x.getCodeApplication());
      if (idMap == null) {
        idMap = new HashMap();
      }
      idMap.put(x.getIdSeqLibProtocol(), null);
      applicationXSeqLibProtocolMap.put(x.getCodeApplication(), idMap);
    }
    
    
    List numberSeqCyclesAllowed = sess.createQuery("SELECT x from NumberSequencingCyclesAllowed x join x.numberSequencingCycles c order by c.numberSequencingCycles").list();
    for(Iterator i = numberSeqCyclesAllowed.iterator(); i.hasNext();) {
      NumberSequencingCyclesAllowed x = (NumberSequencingCyclesAllowed)i.next();
      List<NumberSequencingCyclesAllowed> allowedList = numberSeqCyclesAllowedMap.get(x.getCodeRequestCategory());
      if (allowedList == null) {
        allowedList = new ArrayList<NumberSequencingCyclesAllowed>();
      }
      allowedList.add(x);
      numberSeqCyclesAllowedMap.put(x.getCodeRequestCategory(), allowedList);
    }
    
    Set themes = DictionaryManager.getDictionaryEntries("hci.gnomex.model.ApplicationTheme");
    for(Iterator i = themes.iterator(); i.hasNext();) {
      Object obj = i.next();
      if (obj instanceof NullDictionaryEntry) {
        continue;
      }
      ApplicationTheme theme = (ApplicationTheme)obj;
      applicationThemeMap.put(theme.getIdApplicationTheme(), theme);
    }
  }
  
  private List<Application> getApplications(RequestCategory rc, RequestCategoryType rct) {
    ArrayList<Application> apps = new ArrayList<Application>();
    Map<Integer, Integer> selectedThemes = new HashMap<Integer, Integer>(); 
    for(Iterator i1 = applications.iterator(); i1.hasNext();) {
      Application a = (Application)i1.next();
      // Skip applications not of right application type for this request category.
      if (!a.isApplicableApplication(rct, rc.getIdCoreFacility())) {
        continue;
      }
      
      if (isAssociated(rc, a)) {
        selectedThemes.put(a.getIdApplicationTheme(), a.getIdApplicationTheme());
      }
      
      apps.add(a);
    }
    
    if (rct.getIsIllumina() != null && rct.getIsIllumina().equals("Y")) {
      Collections.sort(apps, new illuminaAppComparator(selectedThemes, applicationMap.get(rc.getCodeRequestCategory())));
    } else {
      Collections.sort(apps, new appComparator(applicationMap.get(rc.getCodeRequestCategory())));
    }
    
    return apps;
  }
  
  private List<ApplicationTheme> getEmptyApplicationThemes(Session sess) {
    String queryString = "select distinct idApplicationTheme from Application where idApplicationTheme is not null";
    Query query = sess.createQuery(queryString);
    List ids = query.list();
    queryString = "select t from ApplicationTheme t where t.idApplicationTheme not in (:ids)";
    query = sess.createQuery(queryString);
    query.setParameterList("ids", ids);
    List<ApplicationTheme> l = (List<ApplicationTheme>)query.list();
    return l;
  }
  
  private Map<String, List<Element>> getApplicationRequestCategoryMap(Session sess, DictionaryHelper dh) throws XMLReflectException {
    Map<String, List<Element>> arcMap = new HashMap<String, List<Element>>();
    String appQueryString = "from Application";
    Query appQuery = sess.createQuery(appQueryString);
    List apps = appQuery.list();
    
    Map<String, String> selectedCategories = new HashMap<String, String>();
    String rcaQueryString = "from RequestCategoryApplication";
    Query rcaQuery = sess.createQuery(rcaQueryString);
    List rcaQueryList =rcaQuery.list();
    for(RequestCategoryApplication rca : (List<RequestCategoryApplication>)rcaQueryList) {
      String key = rca.getCodeApplication() + "\t" + rca.getCodeRequestCategory();
      selectedCategories.put(key, key);
    }
    
    String rcQueryString = "from RequestCategory rc";
    if (!this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_ADMINISTER_ALL_CORE_FACILITIES)) {
      rcQueryString += " where idCoreFacility in (:ids)";
    }
    Query rcQuery = sess.createQuery(rcQueryString);
    if (!this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_ADMINISTER_ALL_CORE_FACILITIES)) {
      ArrayList ids = new ArrayList();
      for(Object cf : this.getSecAdvisor().getCoreFacilitiesIManage()) {
        ids.add(((CoreFacility)cf).getIdCoreFacility());
      }
      rcQuery.setParameterList("ids", ids);
    }
    List rcList = rcQuery.list();
    for (RequestCategory rc : (List<RequestCategory>)rcList) {
      Element node = rc.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();
      for(Application app : (List<Application>)apps) {
        if (app.isApplicableApplication(rc.getCategoryType(), rc.getIdCoreFacility())) {
          Element rcaNode = (Element)node.clone();
          rcaNode.setName("RequestCategoryApplication");
          String key = app.getCodeApplication() + "\t" + rc.getCodeRequestCategory();
          rcaNode.setAttribute("isSelected", selectedCategories.containsKey(key) ? "Y" : "N");
          List<Element> appRCList = arcMap.get(app.getCodeApplication());
          if (appRCList == null) {
            appRCList = new ArrayList<Element>();
          }
          appRCList.add(rcaNode);
          arcMap.put(app.getCodeApplication(), appRCList);
        }
      }
    }
    
    return arcMap;
  }
  
  private Map<String, Integer> getSeqLibProtocolBarCodeSchemeMap(Session sess) {
    String queryString = "select slp.idSeqLibProtocol, obsa.isIndexGroupB, MIN(obs.idOligoBarcodeScheme)"
                         + "  from OligoBarcodeSchemeAllowed obsa"
                         + "  join obsa.seqLibProtocol slp"
                         + "  join obsa.oligoBarcodeScheme obs"
                         + "  where case when slp.isActive is null then 'Y' else slp.isActive end = 'Y' "
                         + "        and case when obs.isActive is null then 'Y' else obs.isActive end = 'Y' "
                         + "  group by slp.idSeqLibProtocol, obsa.isIndexGroupB";
    List l = sess.createQuery(queryString).list();
    Map<String, Integer> map = new HashMap<String, Integer>();
    for(Iterator i = l.iterator(); i.hasNext(); ) {
      Object[] objects = (Object[])i.next();
      Integer idSeqLibProtocol = (Integer)objects[0];
      String isIndexGroupB = (String)objects[1];
      Integer idOligoBarCodeScheme = (Integer)objects[2];
      
      String key = idSeqLibProtocol.toString() + "\t" + (isIndexGroupB.equals("Y") ? "B" : "A");
      map.put(key, idOligoBarCodeScheme);
    }
    
    return map;
  }
  
  private Map<String, Price> getApplicationToLibPrepPriceMap(Session sess) {
    String queryString = 
        "select rc, p, crit " +
        " from PriceSheet ps " +
        " join ps.requestCategories rc " +
        " join ps.priceCategories pc " +
        " join pc.priceCategory.prices p " +
        " join p.priceCriterias crit " +
        " where pc.priceCategory.pluginClassName='hci.gnomex.billing.illuminaLibPrepPlugin'" +
        "     and crit.filter1 is not null";
    Query query = sess.createQuery(queryString);
    List l = query.list();
    Map<String, Price> map = new HashMap<String, Price>();
    for(Iterator i = l.iterator(); i.hasNext(); ) {
      Object[] objects = (Object[])i.next();
      RequestCategory requestCategory = (RequestCategory)objects[0];
      Price price = (Price)objects[1];
      PriceCriteria priceCriteria = (PriceCriteria)objects[2];
      
      String key = requestCategory.getCodeRequestCategory() + "\t" + priceCriteria.getFilter1();
      map.put(key, price);
    }
    
    return map;
  }
  
  private Map<String, Price> getSeqOptionsToPriceMap(Session sess) {
    String queryString = 
        "select rc, p, crit " +
        " from PriceSheet ps " +
        " join ps.requestCategories rc " +
        " join ps.priceCategories pc " +
        " join pc.priceCategory.prices p " +
        " join p.priceCriterias crit " +
        " where pc.priceCategory.pluginClassName='hci.gnomex.billing.illuminaSeqPlugin'" +
        "     and crit.filter1 is not null";
    Query query = sess.createQuery(queryString);
    List l = query.list();
    Map<String, Price> map = new HashMap<String, Price>();
    for(Iterator i = l.iterator(); i.hasNext(); ) {
      Object[] objects = (Object[])i.next();
      RequestCategory requestCategory = (RequestCategory)objects[0];
      Price price = (Price)objects[1];
      PriceCriteria priceCriteria = (PriceCriteria)objects[2];
      
      String key = requestCategory.getCodeRequestCategory() + "\t" + priceCriteria.getFilter1();
      map.put(key, price);
    }
    
    return map;
  }
  
  private Map<String, String> getRequestCategoryToPriceSheetMap(Session sess) {
    String queryString = 
        "select rc " +
        " from PriceSheet ps " +
        " join ps.requestCategories rc " +
        " where ps.isActive = 'Y'";
    Query query = sess.createQuery(queryString);
    List l = query.list();
    Map<String, String> map = new HashMap<String, String>();
    for(Iterator i = l.iterator(); i.hasNext(); ) {
      RequestCategory rc = (RequestCategory)i.next();
      if (PropertyDictionaryHelper.getInstance(sess).getCoreFacilityRequestCategoryProperty(rc.getIdCoreFacility(), rc.getCodeRequestCategory(), PropertyDictionary.ILLUMINA_LIBPREP_DEFAULT_PRICE_CATEGORY) != null
          && PropertyDictionaryHelper.getInstance(sess).getCoreFacilityRequestCategoryProperty(rc.getIdCoreFacility(), rc.getCodeRequestCategory(), PropertyDictionary.ILLUMINA_SEQOPTION_DEFAULT_PRICE_CATEGORY) != null) {
        map.put(rc.getCodeRequestCategory(), rc.getCodeRequestCategory());
      }
    }
    
    return map;
  }
  
  private class appComparator implements Comparator<Application>, Serializable {
    private Map<String, RequestCategoryApplication> appMap;

    public appComparator(Map<String, RequestCategoryApplication> appMap) {
      this.appMap = appMap;
      if (this.appMap == null) {
        this.appMap = new HashMap<String, RequestCategoryApplication>();
      }
    }
    
    public int compare(Application a1, Application a2) {
      
      Integer sort1 = a1.getSortOrder() == null ? -1 : a1.getSortOrder();
      Integer sort2 = a2.getSortOrder() == null ? -1 : a2.getSortOrder();

      if (a1.getNonNullString(a1.getIsActive()).equals("Y") && !a2.getNonNullString(a2.getIsActive()).equals("Y")) {
        return -1;
      } else if (!a1.getNonNullString(a1.getIsActive()).equals("Y") && a2.getNonNullString(a2.getIsActive()).equals("Y")) {
        return 1;
      } else if (appMap.containsKey(a1.getCodeApplication()) && !appMap.containsKey(a2.getCodeApplication())) {
        return -1;
      } else if (!appMap.containsKey(a1.getCodeApplication()) && appMap.containsKey(a2.getCodeApplication())) {
        return 1;
      } else if (sort1.equals(sort2)) {
        return a1.getApplication().compareTo(a2.getApplication());
      } else {
        return sort1.compareTo(sort2);
      }
    }
 }
  
  private class illuminaAppComparator extends appComparator implements Serializable {
    private Map<Integer, Integer> selectedThemes;
    
    public illuminaAppComparator(Map<Integer, Integer>selectedThemes, Map<String, RequestCategoryApplication> appMap) {
      super(appMap);
      this.selectedThemes = selectedThemes;
    }
    
    public int compare(Application a1, Application a2) {
      if (selectedThemes.containsKey(a1.getIdApplicationTheme()) && !selectedThemes.containsKey(a2.getIdApplicationTheme())) {
        return -1;
      } else if (!selectedThemes.containsKey(a1.getIdApplicationTheme()) && selectedThemes.containsKey(a2.getIdApplicationTheme())) {
        return 1;
      } else if (a1.getIdApplicationTheme() == null || a2.getIdApplicationTheme() == null || a1.getIdApplicationTheme().equals(a2.getIdApplicationTheme())) {
        return super.compare(a1, a2);
      } else {
        return a1.getIdApplicationTheme().compareTo(a2.getIdApplicationTheme());
      }
    }
  }
}