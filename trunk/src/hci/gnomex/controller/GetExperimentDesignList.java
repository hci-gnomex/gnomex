package hci.gnomex.controller;

import hci.gnomex.model.ExperimentDesign;
import hci.gnomex.utility.HibernateSession;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.framework.utilities.XMLReflectException;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;


public class GetExperimentDesignList extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetExperimentDesignList.class);
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      
   
    Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
    
    

    // Get codes that are used
    StringBuffer queryBuf = new StringBuffer();
    queryBuf.append("SELECT distinct ed.codeExperimentDesign from ExperimentDesignEntry as ed ");
    List usedCodes = (List)sess.createQuery(queryBuf.toString()).list();
    
    
    //  Now get all used experiment designs
    List usedDesigns = new ArrayList();
    if (usedCodes.size() > 0) {
      queryBuf = new StringBuffer();
      queryBuf.append("SELECT ed from ExperimentDesign as ed ");
      if (usedCodes.size() > 0) {
        queryBuf.append(" where ed.codeExperimentDesign in (");
        for(Iterator i = usedCodes.iterator(); i.hasNext();) {
          String code= (String)i.next();
          queryBuf.append("'" + code + "'");
          if (i.hasNext()) {
            queryBuf.append(", ");
          }
        }
        queryBuf.append(")");
      }
      usedDesigns = sess.createQuery(queryBuf.toString()).list();
    }
    
    
    // Now get all other experiment designs
    queryBuf = new StringBuffer();
    queryBuf.append("SELECT ed from ExperimentDesign as ed ");
    if (usedCodes.size() > 0) {
      queryBuf.append(" where ed.codeExperimentDesign not in (");
      for(Iterator i = usedCodes.iterator(); i.hasNext();) {
        String code= (String)i.next();
        queryBuf.append("'" + code + "'");
        if (i.hasNext()) {
          queryBuf.append(", ");
        }
      }
      queryBuf.append(")");
    }
    
    List notUsedDesigns = (List)sess.createQuery(queryBuf.toString()).list();
    
    
    // Generate XML for each experiment design.
    Document doc = new Document(new Element("ExperimentDesignList"));
    generateXML(doc, usedDesigns,    "Y");
    generateXML(doc, notUsedDesigns, "N");
    
    
    XMLOutputter out = new org.jdom.output.XMLOutputter();
    this.xmlResult = out.outputString(doc);
    
    setResponsePage(this.SUCCESS_JSP);
    }catch (NamingException e){
      log.error("An exception has occurred in GetExperimentDesignList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }catch (SQLException e) {
      log.error("An exception has occurred in GetExperimentDesignList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (XMLReflectException e){
      log.error("An exception has occurred in GetExperimentDesignList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e){
      log.error("An exception has occurred in GetExperimentDesignList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        this.getSecAdvisor().closeReadOnlyHibernateSession();        
      } catch(Exception e) {
        
      }
    }
    
    return this;
  }

  private void generateXML(Document doc, List designs, String isUsed) throws XMLReflectException {
    for(Iterator i = designs.iterator(); i.hasNext();) {
      ExperimentDesign ed = (ExperimentDesign)i.next();
      Element node = ed.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();
      node.setAttribute("isUsed", isUsed);
      doc.getRootElement().addContent(node);      
    }
  }

}