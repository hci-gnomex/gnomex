package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.Organism;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.model.Property;
import hci.gnomex.model.PropertyOption;
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
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
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

  private Property                       propertyScreen;
  private boolean                        isNewProperty = false;

  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    propertyScreen = new Property();
    HashMap errors = this.loadDetailObject(request, propertyScreen);
    this.addInvalidFields(errors);
    if (propertyScreen.getIdProperty() == null || propertyScreen.getIdProperty().intValue() == 0) {
      isNewProperty = true;
    }
    

    if (request.getParameter("optionsXMLString") != null && !request.getParameter("optionsXMLString").equals("")) {
      optionsXMLString = request.getParameter("optionsXMLString");
      StringReader reader = new StringReader(optionsXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        optionsDoc = sax.build(reader);     
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
      

  }

  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      
      if (this.getSecurityAdvisor().hasPermission(SecurityAdvisor.CAN_SUBMIT_REQUESTS)) {

        
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
            
            option.setOption(node.getAttributeValue("option"));
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
        // Save property platforms
        //
        TreeSet platforms = new TreeSet(new RequestCategoryComparator());
        if (platformsDoc != null) {
          for(Iterator i = this.platformsDoc.getRootElement().getChildren().iterator(); i.hasNext();) {
            Element platformNode = (Element)i.next();
            RequestCategory rc = (RequestCategory)sess.load(RequestCategory.class, platformNode.getAttributeValue("codeRequestCategory"));
            platforms.add(rc);
          }
        }
        sc.setPlatforms(platforms);
        
        
        sess.flush();
        
        

        DictionaryHelper.reload(sess);
        
        this.xmlResult = "<SUCCESS idProperty=\"" + sc.getIdProperty() + "\"/>";
      
        setResponsePage(this.SUCCESS_JSP);
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
  
  private void initializeProperty(Property prop) {
    prop.setName(propertyScreen.getName());
    prop.setMageOntologyCode(propertyScreen.getMageOntologyCode());
    prop.setMageOntologyDefinition(propertyScreen.getMageOntologyDefinition());
    prop.setIsActive(propertyScreen.getIsActive());
    prop.setForSample(propertyScreen.getForSample());
    prop.setForDataTrack(propertyScreen.getForDataTrack());
    prop.setIsRequired(propertyScreen.getIsRequired());
    prop.setCodePropertyType(propertyScreen.getCodePropertyType());
    prop.setIdAppUser(propertyScreen.getIdAppUser());
    prop.setDescription(propertyScreen.getDescription());
  }
  
  
  private class OrganismComparator implements Comparator, Serializable {
    public int compare(Object o1, Object o2) {
      Organism org1 = (Organism)o1;
      Organism org2 = (Organism)o2;
      
      return org1.getIdOrganism().compareTo(org2.getIdOrganism());
      
    }
  }
  private class RequestCategoryComparator implements Comparator, Serializable {
    public int compare(Object o1, Object o2) {
      RequestCategory rc1 = (RequestCategory)o1;
      RequestCategory rc2 = (RequestCategory)o2;
      
      return rc1.getCodeRequestCategory().compareTo(rc2.getCodeRequestCategory());
      
    }
  }
}