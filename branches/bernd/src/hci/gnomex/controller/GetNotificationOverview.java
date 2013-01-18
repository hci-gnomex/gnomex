package hci.gnomex.controller;

import hci.gnomex.model.NotificationOverviewFilter;
import hci.gnomex.model.Step;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;

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

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;


public class GetNotificationOverview extends GNomExCommand implements Serializable {
  
  
private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetNotificationOverview.class);
  
  private NotificationOverviewFilter filter;
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    filter = new NotificationOverviewFilter();
    
    HashMap errors = this.loadDetailObject(request, filter);
    this.addInvalidFields(errors);
  }

  public Command execute() throws RollBackCommandException {
    
    try {
    	
      if (this.getSecurityAdvisor().hasPermission(SecurityAdvisor.CAN_MANAGE_WORKFLOW)) {
        Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
        

        /// Process filter calling here 
        Document doc = new Document(new Element("NotificationOverviewList"));
        
        StringBuffer queryBuf = filter.getQuery(this.getSecAdvisor());
        List rows = (List) sess.createQuery(queryBuf.toString()).list();
        
        for (Iterator<Object[]> i1 = rows.iterator(); i1.hasNext();) {
            Object[] row = (Object[]) i1.next();
            
            Element n = new Element("NotificationOverview");

            /*
             * Add values to Element n
             * Example:
             * n.setAttribute("sampleId",              	 row[0] == null ? "" :  ((Integer)row[0]).toString()); 
             */
            

            // Add node content to rootElement XML output.
            doc.getRootElement().addContent(n);
        }
  	  
        XMLOutputter out = new org.jdom.output.XMLOutputter();
        this.xmlResult = out.outputString(doc);
        
        // Send redirect with response SUCCESS or ERROR page.
        setResponsePage(this.SUCCESS_JSP);
        
      } else {
        this.addInvalidField("Insufficient permissions", "Insufficient permission to manage workflow.");
        setResponsePage(this.ERROR_JSP);
      }
    
    }catch (NamingException e){
      log.error("An exception has occurred in GetExperimentOverviewList ", e);
      e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());
        
    }catch (SQLException e) {
      log.error("An exception has occurred in GetExperimentOverviewList ", e);
      e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e) {
      log.error("An exception has occurred in GetExperimentOverviewList ", e);
      e.printStackTrace(System.out);
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