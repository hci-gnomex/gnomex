package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.model.GenomeIndex;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;


public class GetGenomeIndexList extends GNomExCommand implements Serializable {
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetGenomeIndexList.class);

  
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

      //DictionaryManager dictionaryManager = DictionaryManager.getDictionaryManager(ManageDictionaries.DICTIONARY_NAMES_XML, sess, this, true);

      Document doc = new Document(new Element("GenomeIndexList"));

      List genomeIndexList = sess.createQuery("SELECT genomeIndex from GenomeIndex genomeIndex order by genomeIndex.genomeIndexName").list();

      for(Iterator i = genomeIndexList.iterator(); i.hasNext();) {
        GenomeIndex gnIdx = (GenomeIndex)i.next();
        this.getSecAdvisor().flagPermissions(gnIdx);
        Element node = gnIdx.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();
        doc.getRootElement().addContent(node);
      }

      org.jdom.output.XMLOutputter out = new org.jdom.output.XMLOutputter();
      this.xmlResult = out.outputString(doc);

      setResponsePage(this.SUCCESS_JSP);
    }catch (NamingException e){
      log.error("An exception has occurred in GetGenomeIndexList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }catch (SQLException e) {
      log.error("An exception has occurred in GetGenomeIndexList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (XMLReflectException e){
      log.error("An exception has occurred in GetGenomeIndexList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e) {
      log.error("An exception has occurred in GetGenomeIndexList ", e);
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