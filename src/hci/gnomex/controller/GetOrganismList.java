package hci.gnomex.controller;

import hci.dictionary.utility.DictionaryManager;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.model.GenomeBuild;
import hci.gnomex.model.Price;
import hci.gnomex.model.PriceCategory;
import hci.gnomex.model.PriceCriteria;
import hci.gnomex.model.PriceSheet;
import hci.gnomex.model.PriceSheetPriceCategory;
import hci.gnomex.model.Organism;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;


public class GetOrganismList extends GNomExCommand implements Serializable {
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetOrganismList.class);

  
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



      DictionaryManager dictionaryManager = DictionaryManager.getDictionaryManager(ManageDictionaries.DICTIONARY_NAMES_XML, sess, this, true);

      Document doc = new Document(new Element("OrganismList"));

      List organisms = sess.createQuery("SELECT o from Organism o order by o.organism").list();

      for(Iterator i = organisms.iterator(); i.hasNext();) {
        Organism organism = (Organism)i.next();
        this.getSecAdvisor().flagPermissions(organism);
        Element node = organism.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();

        StringBuffer query = new StringBuffer("SELECT gb from GenomeBuild gb");
        query.append(" where gb.idOrganism=" + organism.getIdOrganism());
        query.append(" order by gb.genomeBuildName");
        List genomeBuilds = sess.createQuery(query.toString()).list();
        
        Element gbEle = new Element("genomeBuilds");
        for(Iterator j = genomeBuilds.iterator(); j.hasNext();) {
          GenomeBuild genomeBuild = (GenomeBuild)j.next();
          this.getSecAdvisor().flagPermissions(genomeBuild);
          Element childNode = genomeBuild.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();
          gbEle.addContent(childNode);
        }
        node.addContent(gbEle);
        
        doc.getRootElement().addContent(node);
      }

      org.jdom.output.XMLOutputter out = new org.jdom.output.XMLOutputter();
      this.xmlResult = out.outputString(doc);

      setResponsePage(this.SUCCESS_JSP);
    }catch (NamingException e){
      log.error("An exception has occurred in GetOrganismList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }catch (SQLException e) {
      log.error("An exception has occurred in GetOrganismList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (XMLReflectException e){
      log.error("An exception has occurred in GetOrganismList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e) {
      log.error("An exception has occurred in GetOrganismList ", e);
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

}