package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.AnalysisType;
import hci.gnomex.model.Application;
import hci.gnomex.model.Organism;
import hci.gnomex.model.Property;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.PropertyOption;
import hci.gnomex.model.PropertyPlatformApplication;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;




public class SaveProperty extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveProperty.class);
  
  private String                         optionsXMLString;
  private Document                       optionsDoc;
  
  private String                         organismsXMLString;
  private Document                       organismsDoc;
  
  private String                         platformsXMLString;
  private Document                       platformsDoc;

  private String                         analysisTypesXMLString;
  private Document                       analysisTypesDoc;

  private Property                       propertyScreen;
  private boolean                        isNewProperty = false;

  private String                         annotationPropertyEquivalents = null;
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    propertyScreen = new Property();
    HashMap errors = this.loadDetailObject(request, propertyScreen);
    this.addInvalidFields(errors);
    if (propertyScreen.getIdProperty() == null || propertyScreen.getIdProperty().intValue() == 0) {
      isNewProperty = true;
    }
    
    if (!propertyScreen.getForAnalysis().equals("Y") && !propertyScreen.getForDataTrack().equals("Y") && !propertyScreen.getForSample().equals("Y") && !propertyScreen.getForRequest().equals("Y")) {
      this.addInvalidField("AnnotationAppliesTo", "Please choose the object the annotation applies to");
    }

    if (request.getParameter("optionsXMLString") != null && !request.getParameter("optionsXMLString").equals("")) {
      optionsXMLString = request.getParameter("optionsXMLString");
      StringReader reader = new StringReader(optionsXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        optionsDoc = sax.build(reader);     
        checkOptions();
      } catch (JDOMException je ) {
        log.error( "Cannot parse optionsXMLString", je );
        this.addInvalidField( "optionsXMLString", "Invalid optionsXMLString");
      }
    } 
    
    
    if (request.getParameter("organismsXMLString") != null && !request.getParameter("organismsXMLString").equals("")) {
      organismsXMLString = request.getParameter("organismsXMLString");
      StringReader reader = new StringReader(organismsXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        organismsDoc = sax.build(reader);     
      } catch (JDOMException je ) {
        log.error( "Cannot parse organismsXMLString", je );
        this.addInvalidField( "organismsXMLString", "Invalid organismsXMLString");
      }
    }
    
    if (request.getParameter("platformsXMLString") != null && !request.getParameter("platformsXMLString").equals("")) {
      platformsXMLString = request.getParameter("platformsXMLString");
      StringReader reader = new StringReader(platformsXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        platformsDoc = sax.build(reader);     
      } catch (JDOMException je ) {
        log.error( "Cannot parse platformsXMLString", je );
        this.addInvalidField( "platformsXMLString", "Invalid platformsXMLString");
      }
    }
      
    if (request.getParameter("analysisTypesXMLString") != null && !request.getParameter("analysisTypesXMLString").equals("")) {
      analysisTypesXMLString = request.getParameter("analysisTypesXMLString");
      StringReader reader = new StringReader(analysisTypesXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        analysisTypesDoc = sax.build(reader);     
      } catch (JDOMException je ) {
        log.error( "Cannot parse analysisTypesXMLString", je );
        this.addInvalidField( "analysisTypesXMLString", "Invalid analysisTypesXMLString");
      }
    }
      

  }
  
  private void checkOptions() {
    PropertyDictionaryHelper pdh = PropertyDictionaryHelper.getInstance(null);
    this.annotationPropertyEquivalents = pdh.getProperty(PropertyDictionary.ANNOTATION_OPTION_EQUIVALENTS);
    String annotationPropertyInvalid = pdh.getProperty(PropertyDictionary.ANNOTATION_OPTION_INVALID);
    
    HashMap<String, String> invalidMap = new HashMap<String, String>();
    if (annotationPropertyInvalid != null) {
      for (String i: annotationPropertyInvalid.split(",")) {
        invalidMap.put(i.trim().toUpperCase(), i);
      }
    }
    
    HashMap<String, String> nameMap = new HashMap<String, String>();
    for(Iterator i = this.optionsDoc.getRootElement().getChildren().iterator(); i.hasNext();) {
      Element node = (Element)i.next();
      
      String name = node.getAttributeValue("option");
      name = mapPropertyOptionEquivalents(name);
      
      if (invalidMap.containsKey(name.toUpperCase())) {
        this.addInvalidField("Invalid Option", "The option '" + node.getAttributeValue("option") + "' is not allowed.");
      }
      
      if (nameMap.containsKey(name.toUpperCase())) {
        this.addInvalidField("Duplicate Option", "The options '" + node.getAttributeValue("option") + "' and '" + nameMap.get(name.toUpperCase()) + "' are duplicate.  Please correct and try again.");
      }
      nameMap.put(name.toUpperCase(), node.getAttributeValue("option"));
    }
  }
  
  private String mapPropertyOptionEquivalents(String option) {
    if (option == null) {
      return "";
    }
    option = option.trim();

    String eq = this.annotationPropertyEquivalents;
    if (eq == null || eq.trim().length() == 0) {
      return option;
    }
    
    String opts[] = eq.split(",");
    if (opts.length < 2) {
      return option;
    }
    
    for(String opt : opts) {
      opt = opt.trim();
      if (opt.toUpperCase().equals(option.toUpperCase())) {
        return opts[0].trim();
      }
    }
    
    return option;
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      
      if (this.getSecurityAdvisor().hasPermission(SecurityAdvisor.CAN_SUBMIT_REQUESTS)) {

        if (validatePropertyScreen(sess)) {
        
          Property sc = null;
                
          if (isNewProperty) {
            sc = propertyScreen;
            
            sess.save(sc);
          } else {
            
            sc = (Property)sess.load(Property.class, propertyScreen.getIdProperty());
            
            // Need to initialize billing accounts; otherwise new accounts
            // get in the list and get deleted.
            Hibernate.initialize(sc.getOptions());
            Hibernate.initialize(sc.getOrganisms());
            
            initializeProperty(sc);
          }
  
  
          //
          // Save options
          //
          HashMap optionMap = new HashMap();
          if (optionsDoc != null) {
            for(Iterator i = this.optionsDoc.getRootElement().getChildren().iterator(); i.hasNext();) {
              Element node = (Element)i.next();
              PropertyOption option =  null;
              
              String idPropertyOption = node.getAttributeValue("idPropertyOption");
              if (idPropertyOption.startsWith("PropertyOption")) {
                option = new PropertyOption();
              } else {
                option = (PropertyOption) sess.load(PropertyOption.class, Integer.valueOf(idPropertyOption));
              }
              
              String name = node.getAttributeValue("option");
              name = mapPropertyOptionEquivalents(name);
              
              option.setOption(name);
              option.setSortOrder(node.getAttributeValue("sortOrder") != null && !node.getAttributeValue("sortOrder").equals("") ? Integer.valueOf(node.getAttributeValue("sortOrder")) : null);
              option.setIsActive(node.getAttributeValue("isActive"));
              option.setIdProperty(sc.getIdProperty());
  
              sess.save(option);
              sess.flush();
              optionMap.put(option.getIdPropertyOption(), null);
              
            }
          }
  
          // Remove options no longer in the list
          List optionsToRemove = new ArrayList();
          if (sc.getOptions() != null) {
            for(Iterator i = sc.getOptions().iterator(); i.hasNext();) {
              PropertyOption op = (PropertyOption)i.next();
              
              if (!optionMap.containsKey(op.getIdPropertyOption())) {
                optionsToRemove.add(op);
              }
            }
            for(Iterator i = optionsToRemove.iterator(); i.hasNext();) {
              PropertyOption op = (PropertyOption)i.next();
              
              Integer entryCount = new Integer(0);
              String buf = "SELECT count(*) from PropertyEntry e JOIN e.options as eo where eo.idPropertyOption = " + op.getIdPropertyOption();
              List entryCounts = sess.createQuery(buf).list();
              if (entryCounts != null && entryCounts.size() > 0) {
                entryCount = Integer.class.cast(entryCounts.get(0));
              }
              
              // Inactive if there are existing property entries pointing to this option.
              // If no existing entries, delete option.
              if (entryCount.intValue() > 0) {
                op.setIsActive("N");              
              } else {
                sess.delete(op);
                sc.getOptions().remove(op);              
              }
            }
          }
          sess.flush();
          
  
          //
          // Save property organisms
          //
          TreeSet organisms = new TreeSet(new OrganismComparator());
          if (organismsDoc != null) {
            for(Iterator i = this.organismsDoc.getRootElement().getChildren().iterator(); i.hasNext();) {
              Element organismNode = (Element)i.next();
              Organism organism = (Organism)sess.load(Organism.class, Integer.valueOf(organismNode.getAttributeValue("idOrganism")));
              organisms.add(organism);
            }
          }
          sc.setOrganisms(organisms);
          
  
          //
          // Save property platformApplications
          //
          TreeSet platformApplications = new TreeSet(new PlatformApplicationsComparator());
          HashMap platformApplicationsMap = new HashMap();
          if (platformsDoc != null) {
            for(Iterator i = this.platformsDoc.getRootElement().getChildren().iterator(); i.hasNext();) {
              Element platformNode = (Element)i.next();
              
              // See if this PropertyPlatformApplication object already exists
              StringBuffer queryBuf = new StringBuffer("select pa");
              queryBuf.append(" from PropertyPlatformApplication as pa");
              queryBuf.append(" where pa.idProperty = " + sc.getIdProperty().toString() + " and");
              queryBuf.append(" pa.codeRequestCategory = '" + platformNode.getAttributeValue("codeRequestCategory") + "' and");
              queryBuf.append(" pa.codeApplication ");
              if (platformNode.getAttributeValue("codeApplication").length() > 0) {
                queryBuf.append("= '" + platformNode.getAttributeValue("codeApplication") + "'");
              } else {
                queryBuf.append("is null");
              }  
              
              Query query = sess.createQuery(queryBuf.toString());
              List paRows = (List)query.list();   
              
              PropertyPlatformApplication pa = null;
              if (paRows.size() > 0) {
                pa = (PropertyPlatformApplication) paRows.get(0);
                platformApplicationsMap.put(pa.getIdPlatformApplication(), null);
              } else {
                pa = new PropertyPlatformApplication();
                pa.setIdProperty(sc.getIdProperty());
                pa.setCodeRequestCategory(platformNode.getAttributeValue("codeRequestCategory"));
                if (platformNode.getAttributeValue("codeApplication").length() > 0) {
                  pa.setCodeApplication(platformNode.getAttributeValue("codeApplication"));
                } else {
                  pa.setCodeApplication(null);
                } 
                sess.save(pa);
                platformApplicationsMap.put(pa.getIdPlatformApplication(), null);
                sess.flush();
              }
              // Reload to insure RequestCategory and Application objects are populated
              Integer idPlatformApplication = pa.getIdPlatformApplication();
              pa = (PropertyPlatformApplication)sess.load(PropertyPlatformApplication.class, idPlatformApplication); 
             
              RequestCategory rc = (RequestCategory)sess.load(RequestCategory.class, pa.getCodeRequestCategory()); 
              pa.setRequestCategory(rc);
              if(pa.getCodeApplication() != null) {
                Application a = (Application)sess.load(Application.class, pa.getCodeApplication());
                pa.setApplication(a);
              }
             
              platformApplications.add(pa);
            }
          }    
          
          // Remove platformApplications no longer in the list
          List platformApplicationsToRemove = new ArrayList();
          if (sc.getPlatformApplications() != null) {
            for(Iterator i = sc.getPlatformApplications().iterator(); i.hasNext();) {
              PropertyPlatformApplication pa = (PropertyPlatformApplication) i.next();
              
              if (!platformApplicationsMap.containsKey(pa.getIdPlatformApplication())) {
                platformApplicationsToRemove.add(pa);
              }
            }
            for(Iterator i = platformApplicationsToRemove.iterator(); i.hasNext();) {
              PropertyPlatformApplication pa = (PropertyPlatformApplication)i.next();
              sess.delete(pa);
            }
          }
          sess.flush();         
  
          sc.setPlatformApplications(platformApplications);
          
          //
          // Save property analysisTypes
          //
          TreeSet analysisTypes = new TreeSet(new AnalysisTypeComparator());
          if (analysisTypesDoc != null) {
            for(Iterator i = this.analysisTypesDoc.getRootElement().getChildren().iterator(); i.hasNext();) {
              Element analysisTypeNode = (Element)i.next();
              AnalysisType at = (AnalysisType)sess.load(AnalysisType.class, Integer.valueOf(analysisTypeNode.getAttributeValue("idAnalysisType")));
              analysisTypes.add(at);
            }
          }
          sc.setAnalysisTypes(analysisTypes);
          sess.flush();
  
          DictionaryHelper.reload(sess);
          
          this.xmlResult = "<SUCCESS idProperty=\"" + sc.getIdProperty() + "\"/>";
        
          setResponsePage(this.SUCCESS_JSP);
        }      
      } else {
        this.addInvalidField("Insufficient permissions", "Insufficient permission to save sample property.");
        setResponsePage(this.ERROR_JSP);
      }
    }catch (Exception e){
      log.error("An exception has occurred in SaveProperty ", e);
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
  
  private Boolean validatePropertyScreen(Session sess) {
    if (propertyScreen.getIdCoreFacility() == null) {
      this.addInvalidField("No Core", propertyScreen.getName() + " does not have a core facility specified.  Please specify a core facility.");
      setResponsePage(this.ERROR_JSP);
      return false;
    }
    
    String queryString = "select p from Property p where name = :name";
    Query query = sess.createQuery(queryString);
    query.setParameter("name", propertyScreen.getName());
    @SuppressWarnings("unchecked")
    List<Property> l = (List<Property>)query.list();
    for(Property p : l) {
      // don't want to compare against itself.
      if (p.getIdProperty().equals(propertyScreen.getIdProperty())) {
        continue;
      }
      // note that idCorefacility should never be null, but the check makes this more robust.
      if (p.getIdCoreFacility() == null || p.getIdCoreFacility().equals(propertyScreen.getIdCoreFacility())) {
        this.addInvalidField("Duplicate Name", propertyScreen.getName() + " has been used as the name for a previously defined annotation.  Please choose another name.");
        setResponsePage(this.ERROR_JSP);
        return false;
      }
    }
    
    return true;
  }
  
  private void initializeProperty(Property prop) {
    prop.setName(propertyScreen.getName());
    prop.setMageOntologyCode(propertyScreen.getMageOntologyCode());
    prop.setMageOntologyDefinition(propertyScreen.getMageOntologyDefinition());
    prop.setIsActive(propertyScreen.getIsActive());
    prop.setForSample(propertyScreen.getForSample());
    prop.setForDataTrack(propertyScreen.getForDataTrack());
    prop.setForAnalysis(propertyScreen.getForAnalysis());
    prop.setForRequest(propertyScreen.getForRequest());
    prop.setIsRequired(propertyScreen.getIsRequired());
    prop.setSortOrder(propertyScreen.getSortOrder());
    prop.setCodePropertyType(propertyScreen.getCodePropertyType());
    prop.setIdAppUser(propertyScreen.getIdAppUser());
    prop.setDescription(propertyScreen.getDescription());
    prop.setIdCoreFacility(propertyScreen.getIdCoreFacility());
  }
  
  
  private class OrganismComparator implements Comparator, Serializable {
    public int compare(Object o1, Object o2) {
      Organism org1 = (Organism)o1;
      Organism org2 = (Organism)o2;
      
      return org1.getIdOrganism().compareTo(org2.getIdOrganism());
      
    }
  }
  private class PlatformApplicationsComparator implements Comparator, Serializable {
    public int compare(Object o1, Object o2) {
      PropertyPlatformApplication pa1 = (PropertyPlatformApplication)o1;
      PropertyPlatformApplication pa2 = (PropertyPlatformApplication)o2;
      
      int compVal = pa1.getRequestCategory().getRequestCategory().compareTo(pa2.getRequestCategory().getRequestCategory());
      if(compVal==0) {
        String paApplication1 = "";
        String paApplication2 = "";
        if(pa1.getApplication() != null && pa1.getApplication().getApplication() != null) {
          paApplication1 = pa1.getApplication().getApplication();
        }
        if(pa2.getApplication() != null && pa2.getApplication().getApplication() != null) {
          paApplication2 = pa2.getApplication().getApplication();
        }
        compVal = paApplication1.compareTo(paApplication2);
      }
      
      return compVal;
      
    }
  }
  private class AnalysisTypeComparator implements Comparator, Serializable {
    public int compare(Object o1, Object o2) {
      AnalysisType a1 = (AnalysisType)o1;
      AnalysisType a2 = (AnalysisType)o2;
      
      return a1.getIdAnalysisType().compareTo(a2.getIdAnalysisType());
      
    }
  }

}