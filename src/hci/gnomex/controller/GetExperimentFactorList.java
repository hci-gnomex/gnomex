package hci.gnomex.controller;

import hci.gnomex.model.ExperimentFactor;
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


public class GetExperimentFactorList extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetExperimentFactorList.class);
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      
   
    Session sess = HibernateSession.currentSession(this.getUsername());
    
    

    // Get codes that are used
    StringBuffer queryBuf = new StringBuffer();
    queryBuf.append("SELECT distinct ef.codeExperimentFactor from ExperimentFactorEntry as ef ");
    List usedCodes = (List)sess.createQuery(queryBuf.toString()).list();
    
    
    //  Now get all used experiment factors
    List usedFactors = new ArrayList();
    if (usedCodes.size() > 0) {
      queryBuf = new StringBuffer();
      queryBuf.append("SELECT ef from ExperimentFactor as ef ");
      if (usedCodes.size() > 0) {
        queryBuf.append(" where ef.codeExperimentFactor in (");
        for(Iterator i = usedCodes.iterator(); i.hasNext();) {
          String code= (String)i.next();
          queryBuf.append("'" + code + "'");
          if (i.hasNext()) {
            queryBuf.append(", ");
          }
        }
        queryBuf.append(")");
      }
      usedFactors = sess.createQuery(queryBuf.toString()).list();
    }
    
    
    // Now get all other experiment factors
    queryBuf = new StringBuffer();
    queryBuf.append("SELECT ef from ExperimentFactor as ef ");
    if (usedCodes.size() > 0) {
      queryBuf.append(" where ef.codeExperimentFactor not in (");
      for(Iterator i = usedCodes.iterator(); i.hasNext();) {
        String code= (String)i.next();
        queryBuf.append("'" + code + "'");
        if (i.hasNext()) {
          queryBuf.append(", ");
        }
      }
      queryBuf.append(")");
    }
    
    List notUsedFactors = (List)sess.createQuery(queryBuf.toString()).list();
    
    
    // Generate XML for each experiment factor.
    Document doc = new Document(new Element("ExperimentFactorList"));
    generateXML(doc, usedFactors,    "Y");
    generateXML(doc, notUsedFactors, "N");
    
    
    XMLOutputter out = new org.jdom.output.XMLOutputter();
    this.xmlResult = out.outputString(doc);
    
    setResponsePage(this.SUCCESS_JSP);
    }catch (NamingException e){
      log.error("An exception has occurred in GetExperimentFactorList ", e);
      throw new RollBackCommandException(e.getMessage());
        
    }catch (SQLException e) {
      log.error("An exception has occurred in GetExperimentFactorList ", e);
      throw new RollBackCommandException(e.getMessage());
    } catch (XMLReflectException e){
      log.error("An exception has occurred in GetExperimentFactorList ", e);
      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        HibernateSession.closeSession();        
      } catch(Exception e) {
        
      }
    }
    
    return this;
  }

  private void generateXML(Document doc, List factors, String isUsed) throws XMLReflectException {
    for(Iterator i = factors.iterator(); i.hasNext();) {
      ExperimentFactor ef = (ExperimentFactor)i.next();
      Element node = ef.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();
      node.setAttribute("isUsed", isUsed);
      doc.getRootElement().addContent(node);      
    }
  }

}