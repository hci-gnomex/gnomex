package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.model.LabFilter;
import hci.gnomex.security.SecurityAdvisor;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.apache.log4j.Logger;

public class GetAnalysisLabList extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static Logger LOG = Logger.getLogger(GetAnalysisLabList.class);
  
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
      
      //Build a HashMap for Labs with > 0 Analyses
      HashMap<Integer, Integer> labs = new HashMap<Integer, Integer>();

      
      for(Iterator i = rows.iterator(); i.hasNext();) {
        Object[] row = (Object[])i.next();
        
        Integer idLab          = (Integer)row[0];
        String lastName        = (String)row[1];
        String firstName       = (String)row[2];
        Integer analysisCount  = (int) (long)row[3];
        
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
        
        labs.put(idLab, analysisCount);
      }
      
      // The Analysis table only contains labs with one or more analyses.
      // Query the Lab table and add all labs not already included with a zero value for analysisCount
      queryBuf = new StringBuffer();
      queryBuf.append("SELECT lab.idLab, lab.lastName, lab.firstName ");
      queryBuf.append("FROM   Lab as lab ");
      queryBuf.append("WHERE isActive='Y' ");
      queryBuf.append("GROUP BY lab.id, lab.lastName, lab.firstName ");
      queryBuf.append("ORDER BY lab.lastName, lab.firstName ");

      List labRows = (List)sess.createQuery(queryBuf.toString()).list();
      
      for(Iterator i = labRows.iterator(); i.hasNext();) {
          Object[] row = (Object[])i.next();
          
          Integer idLab          = (Integer)row[0];
          String lastName        = (String)row[1];
          String firstName       = (String)row[2];
          Integer numAnalyses = 0;
          
          // If we already have an entry for this lab, get its number of analyses
          if(labs.containsKey(idLab))
        	  numAnalyses = labs.get(idLab);
          
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
          labNode.setAttribute("analysisCount", numAnalyses.toString());
          
          doc.getRootElement().addContent(labNode);
      
      }    
    } else {
      throw new RollBackCommandException("Insufficient permissions");
    }
    

    
   
    
    XMLOutputter out = new org.jdom.output.XMLOutputter();
    this.xmlResult = out.outputString(doc);
    
    setResponsePage(this.SUCCESS_JSP);
    } catch (Exception e){
      LOG.error("An exception has occurred in GetAnalysisLabList ", e);

      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        //closeReadOnlyHibernateSession;        
      } catch(Exception e) {
        LOG.error("An exception has occurred in GetAnalysisLabList ", e);
      }
    }
    
    return this;
  }

}