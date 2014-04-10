package hci.gnomex.controller;

import hci.gnomex.security.SecurityAdvisor;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;

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
import org.jdom.output.XMLOutputter;


public class GetFAQ extends GNomExCommand implements Serializable {
  
  
private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetNewsItem.class);
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

  }

  public Command execute() throws RollBackCommandException {
    
    try {
    	
//      if (this.getSecurityAdvisor().hasPermission(SecurityAdvisor.CAN_MANAGE_DASHBOARD)) {

        Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
        Document doc = new Document(new Element("FaqCollection"));
        StringBuffer buf =  new StringBuffer();
        
        buf.append("SELECT f.idFAQ, f.title, f.url, f.idCoreFacility");
        buf.append(" FROM  FAQ f ");
  	  	
  	  	List rows = (List) sess.createQuery(buf.toString()).list();

        for (Iterator<Object[]> i1 = rows.iterator(); i1.hasNext();) {
            Object[] row = (Object[]) i1.next();
            Element n = new Element("FAQ");

            /*
             * Add values to Element n
             */
              n.setAttribute("idFAQ",              			row[0] == null ? "" :  ((Integer)row[0]).toString()); 
              n.setAttribute("title",              	 		row[1] == null ? "" :  ((String) row[1]));
              n.setAttribute("url", 						        row[2] == null ? "" :  ((String) row[2]));
              n.setAttribute("idCoreFacility",	        row[3] == null ? "0" : ((Integer)row[3]).toString());
              
              // Add node content to rootElement XML output.
            doc.getRootElement().addContent(n);
        }

        XMLOutputter out = new org.jdom.output.XMLOutputter();
        this.xmlResult = out.outputString(doc);
        
        // Send redirect with response SUCCESS or ERROR page.
        setResponsePage(this.SUCCESS_JSP);

//      } else {
//        this.addInvalidField("Insufficient permissions", "Insufficient permission to retrieve FAQs.");
//        setResponsePage(this.ERROR_JSP);
//      }

    }catch (NamingException e){
      log.error("An exception has occurred in GetFAQ ", e);
      e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());
        
    }catch (SQLException e) {
      log.error("An exception has occurred in GetFAQ ", e);
      e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e) {
      log.error("An exception has occurred in GetFAQ ", e);
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