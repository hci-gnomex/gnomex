package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.Organism;
import hci.gnomex.model.SampleCharacteristic;
import hci.gnomex.model.SampleCharacteristicOption;
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




public class SaveSampleCharacteristic extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveSampleCharacteristic.class);
  
  private String                         optionsXMLString;
  private Document                       optionsDoc;
  
  private String                         organismsXMLString;
  private Document                       organismsDoc;

  private SampleCharacteristic           sampleCharacteristicScreen;
  private boolean                        isNewSampleCharacteristic = false;

  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    sampleCharacteristicScreen = new SampleCharacteristic();
    HashMap errors = this.loadDetailObject(request, sampleCharacteristicScreen);
    this.addInvalidFields(errors);
    if (sampleCharacteristicScreen.getIdSampleCharacteristic() == null || sampleCharacteristicScreen.getIdSampleCharacteristic().intValue() == 0) {
      isNewSampleCharacteristic = true;
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
    

  }

  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      
      if (this.getSecurityAdvisor().hasPermission(SecurityAdvisor.CAN_SUBMIT_REQUESTS)) {

        
        SampleCharacteristic sc = null;
              
        if (isNewSampleCharacteristic) {
          sc = sampleCharacteristicScreen;
          
          sess.save(sc);
        } else {
          
          sc = (SampleCharacteristic)sess.load(SampleCharacteristic.class, sampleCharacteristicScreen.getIdSampleCharacteristic());
          
          // Need to initialize billing accounts; otherwise new accounts
          // get in the list and get deleted.
          Hibernate.initialize(sc.getOptions());
          Hibernate.initialize(sc.getOrganisms());
          
          initializeSampleCharacteristic(sc);
        }


        //
        // Save options
        //
        HashMap optionMap = new HashMap();
        if (optionsDoc != null) {
          for(Iterator i = this.optionsDoc.getRootElement().getChildren().iterator(); i.hasNext();) {
            Element node = (Element)i.next();
            SampleCharacteristicOption option =  null;
            
            String idSampleCharacteristicOption = node.getAttributeValue("idSampleCharacteristicOption");
            if (idSampleCharacteristicOption.startsWith("SampleCharacteristicOption")) {
              option = new SampleCharacteristicOption();
            } else {
              option = (SampleCharacteristicOption) sess.load(SampleCharacteristicOption.class, Integer.valueOf(idSampleCharacteristicOption));
            }
            
            option.setOption(node.getAttributeValue("option"));
            option.setSortOrder(node.getAttributeValue("sortOrder") != null && !node.getAttributeValue("sortOrder").equals("") ? Integer.valueOf(node.getAttributeValue("sortOrder")) : null);
            option.setIsActive(node.getAttributeValue("isActive"));
            option.setIdSampleCharacteristic(sc.getIdSampleCharacteristic());

            sess.save(option);
            sess.flush();
            optionMap.put(option.getIdSampleCharacteristicOption(), null);
            
          }
        }

        // Remove options no longer in the list
        List optionsToRemove = new ArrayList();
        if (sc.getOptions() != null) {
          for(Iterator i = sc.getOptions().iterator(); i.hasNext();) {
            SampleCharacteristicOption op = (SampleCharacteristicOption)i.next();
            
            if (!optionMap.containsKey(op.getIdSampleCharacteristicOption())) {
              optionsToRemove.add(op);
            }
          }
          for(Iterator i = optionsToRemove.iterator(); i.hasNext();) {
            SampleCharacteristicOption op = (SampleCharacteristicOption)i.next();
            
            Integer entryCount = new Integer(0);
            String buf = "SELECT count(*) from SampleCharacteristicEntry e JOIN e.options as eo where eo.idSampleCharacteristicOption = " + op.getIdSampleCharacteristicOption();
            List entryCounts = sess.createQuery(buf).list();
            if (entryCounts != null && entryCounts.size() > 0) {
              entryCount = Integer.class.cast(entryCounts.get(0));
            }
            
            // Inactive if there are existing sample characteristic entries pointing to this option.
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
        // Save sample characteristic organisms
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
        
        sess.flush();
        
        

        DictionaryHelper.reload(sess);
        
        this.xmlResult = "<SUCCESS idSampleCharacteristic=\"" + sc.getIdSampleCharacteristic() + "\"/>";
      
        setResponsePage(this.SUCCESS_JSP);
      } else {
        this.addInvalidField("Insufficient permissions", "Insufficient permission to save sample characteristic.");
        setResponsePage(this.ERROR_JSP);
      }
      
    }catch (Exception e){
      log.error("An exception has occurred in SaveSampleCharacteristic ", e);
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
  
  private void initializeSampleCharacteristic(SampleCharacteristic sc) {
    sc.setSampleCharacteristic(sampleCharacteristicScreen.getSampleCharacteristic());
    sc.setMageOntologyCode(sampleCharacteristicScreen.getMageOntologyCode());
    sc.setMageOntologyDefinition(sampleCharacteristicScreen.getMageOntologyDefinition());
    sc.setIsActive(sampleCharacteristicScreen.getIsActive());
    sc.setCodeCharacteristicType(sampleCharacteristicScreen.getCodeCharacteristicType());
    sc.setIdAppUser(sampleCharacteristicScreen.getIdAppUser());
    sc.setDescription(sampleCharacteristicScreen.getDescription());
  }
  
  
  private class OrganismComparator implements Comparator, Serializable {
    public int compare(Object o1, Object o2) {
      Organism org1 = (Organism)o1;
      Organism org2 = (Organism)o2;
      
      return org1.getIdOrganism().compareTo(org2.getIdOrganism());
      
    }
  }

}