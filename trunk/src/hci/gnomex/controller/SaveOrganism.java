package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.GenomeBuild;
import hci.gnomex.model.Organism;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;

import java.io.Serializable;
import java.io.StringReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;


public class SaveOrganism extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveOrganism.class);
  
  private String                         genomeBuildsXMLString;
  private Document                       genomeBuildsDoc;

  private Organism                       organismScreen;
  private boolean                        isNewOrganism = false;

  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    organismScreen = new Organism();
    HashMap errors = this.loadDetailObject(request, organismScreen);
    this.addInvalidFields(errors);
    if (organismScreen.getIdOrganism() == null || organismScreen.getIdOrganism().intValue() == 0) {
      isNewOrganism = true;
    }
    
    // Make sure that the DAS2 name has no spaces or special characters
    if (isValid() && organismScreen.getDas2Name() != null && organismScreen.getDas2Name() != null) {
      if (organismScreen.getDas2Name().indexOf(" ") >= 0) {
        addInvalidField("namespaces", "The DAS2 name cannot have spaces.");
      }
      Pattern pattern = Pattern.compile("\\W");
      Matcher matcher = pattern.matcher(organismScreen.getDas2Name());
      if (matcher.find()) {
        this.addInvalidField("specialc", "The DAS2 name cannot have special characters.");
      }      
    }
    
    
    if (request.getParameter("genomeBuildsXMLString") != null && !request.getParameter("genomeBuildsXMLString").equals("")) {
      genomeBuildsXMLString = request.getParameter("genomeBuildsXMLString");
      StringReader reader = new StringReader(genomeBuildsXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        genomeBuildsDoc = sax.build(reader);     
      } catch (JDOMException je ) {
        log.error( "Cannot parse genomeBuildsXMLString", je );
        this.addInvalidField( "genomeBuildsXMLString", "Invalid genomeBuildsXMLString");
      }
    }     
    
    
    
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      
      if (this.getSecurityAdvisor().hasPermission(SecurityAdvisor.CAN_SUBMIT_REQUESTS)) {

        
        Organism o = null;
              
        if (isNewOrganism) {
          o = organismScreen;
          
          sess.save(o);
          sess.flush();

        } else {
          
          o = (Organism)sess.load(Organism.class, organismScreen.getIdOrganism());
          
          //Hibernate.initialize(o.getGenomeBuilds());
          
          initializeOrganism(o);
          sess.save(o);
          sess.flush();
        }
        
        //
        // Save genome builds
        //
        HashMap genomeBuildMap = new HashMap();
        if (genomeBuildsDoc != null) {
           
          for(Iterator i = this.genomeBuildsDoc.getRootElement().getChildren().iterator(); i.hasNext();) {
            Element node = (Element)i.next();
            GenomeBuild genomeBuild =  null;
            
            String idGenomeBuild = node.getAttributeValue("idGenomeBuild");
            if (idGenomeBuild.startsWith("GenomeBuild")) {
              genomeBuild = new GenomeBuild();
            } else {
              genomeBuild = (GenomeBuild) sess.load(GenomeBuild.class, Integer.valueOf(idGenomeBuild));
            }
            
            genomeBuild.setGenomeBuildName(node.getAttributeValue("genomeBuildName"));
            genomeBuild.setIsLatestBuild(node.getAttributeValue("isLatestBuild"));
            genomeBuild.setIsActive(node.getAttributeValue("isActive"));
            genomeBuild.setIdOrganism(o.getIdOrganism());

            sess.save(genomeBuild);
            sess.flush();
            genomeBuildMap.put(genomeBuild.getIdGenomeBuild(), null);
            
            
            
            // Delete items no longer present
            StringBuffer query = new StringBuffer("SELECT gb from GenomeBuild gb");
            query.append(" where gb.idOrganism=" + o.getIdOrganism());
            query.append(" order by gb.genomeBuildName");
            List genomeBuilds = sess.createQuery(query.toString()).list();
            
            if (!genomeBuilds.isEmpty()) {          
              Element gbEle = new Element("genomeBuilds");
              for(Iterator j = genomeBuilds.iterator(); j.hasNext();) {
                GenomeBuild gb = (GenomeBuild)j.next();
                if (!genomeBuildMap.containsKey(gb.getIdGenomeBuild())) {
                  sess.delete(gb);
                }
              }
            }
            
          }
        }

        sess.flush();
               
        DictionaryHelper.reload(sess);
        
        this.xmlResult = "<SUCCESS idOrganism=\"" + o.getIdOrganism() + "\"/>";
      
        setResponsePage(this.SUCCESS_JSP);
      } else {
        this.addInvalidField("Insufficient permissions", "Insufficient permission to save organism.");
        setResponsePage(this.ERROR_JSP);
      }
      
    }catch (Exception e){
      log.error("An exception has occurred in SaveOrganism ", e);
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
  
  private void initializeOrganism(Organism o) {
    o.setOrganism(organismScreen.getOrganism());
    o.setAbbreviation(organismScreen.getAbbreviation());
    o.setMageOntologyCode(organismScreen.getMageOntologyCode());
    o.setMageOntologyDefinition(organismScreen.getMageOntologyDefinition());
    o.setIsActive(organismScreen.getIsActive());
    o.setIdAppUser(organismScreen.getIdAppUser());
    o.setBinomialName(organismScreen.getBinomialName());
    o.setNcbiTaxID(organismScreen.getNcbiTaxID());
    o.setDas2Name(organismScreen.getDas2Name());
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