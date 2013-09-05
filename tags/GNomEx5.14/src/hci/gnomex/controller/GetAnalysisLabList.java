package hci.gnomex.controller;

import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.HibernateSession;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.framework.utilities.XMLReflectException;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.jdom.Element;
import org.jdom.Document;
import org.jdom.output.XMLOutputter;

import hci.gnomex.model.Lab;
import hci.gnomex.model.LabFilter;
import hci.gnomex.model.Visibility;


public class GetAnalysisLabList extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetAnalysisLabList.class);
  
  private LabFilter labFilter;
  private String    listKind = "LabList";
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
  }

  public Command execute() throws RollBackCommandException {
    
    try {
    Document doc = new Document(new Element(listKind));
    
    if (this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_ACCESS_ANY_OBJECT)) {
      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
      
      
      StringBuffer queryBuf = new StringBuffer();
      queryBuf.append("SELECT lab.id, lab.lastName, lab.firstName, count(*)");
      queryBuf.append("FROM   Analysis a ");
      queryBuf.append("JOIN   a.lab as lab " );
      queryBuf.append("GROUP BY lab.id, lab.lastName, lab.firstName ");
      queryBuf.append("ORDER BY lab.lastName, lab.firstName ");
      List rows = (List)sess.createQuery(queryBuf.toString()).list();
      


      
      for(Iterator i = rows.iterator(); i.hasNext();) {
        Object[] row = (Object[])i.next();
        
        Integer idLab          = (Integer)row[0];
        String lastName        = (String)row[1];
        String firstName       = (String)row[2];
        Integer analysisCount  = (Integer)row[3];
        
        String labName = "";
        if (lastName != null) {
          labName = lastName;
          if (firstName != null && !firstName.equals("")) {
            labName += ", ";
          }
        }
        if (firstName != null && !firstName.equals("")) {
          labName += firstName;
        }
        labName += " Lab";
        
        Element labNode = new Element("Lab");
        labNode.setAttribute("idLab", idLab.toString());
        labNode.setAttribute("lastName", lastName != null ? lastName : "");
        labNode.setAttribute("firstName", firstName != null ? firstName : "");
        labNode.setAttribute("name", labName);
        labNode.setAttribute("analysisCount", analysisCount.toString());
        
        doc.getRootElement().addContent(labNode);
      }
    } else {
      throw new RollBackCommandException("Insufficient permissions");
    }
   
    
    XMLOutputter out = new org.jdom.output.XMLOutputter();
    this.xmlResult = out.outputString(doc);
    
    setResponsePage(this.SUCCESS_JSP);
    }catch (NamingException e){
      log.error("An exception has occurred in GetAnalysisLabList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }catch (SQLException e) {
      log.error("An exception has occurred in GetAnalysisLabList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (XMLReflectException e){
      log.error("An exception has occurred in GetAnalysisLabList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e){
      log.error("An exception has occurred in GetAnalysisLabList ", e);
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

}