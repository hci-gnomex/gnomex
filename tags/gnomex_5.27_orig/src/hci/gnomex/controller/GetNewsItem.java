package hci.gnomex.controller;

import hci.gnomex.model.NewsItemFilter;
import hci.gnomex.security.SecurityAdvisor;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;

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


public class GetNewsItem extends GNomExCommand implements Serializable {
  
  
private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetNewsItem.class);
  
  private NewsItemFilter filter;
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    filter = new NewsItemFilter();
    
    HashMap errors = this.loadDetailObject(request, filter);
    this.addInvalidFields(errors);
  }

  public Command execute() throws RollBackCommandException {
    
    try {

        Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
        StringBuffer queryBuf = filter.getQuery(this.getSecAdvisor());
        
        /// Process filter calling here 
        Document doc = new Document(new Element("NewsItemList"));
        
        
        List rows = (List) sess.createQuery(queryBuf.toString()).list();

        for (Iterator<Object[]> i1 = rows.iterator(); i1.hasNext();) {
            Object[] row = (Object[]) i1.next();
            Element n = new Element("NewsItem");

            /*
             * Add values to Element n
             */
              n.setAttribute("idNewsItem",              	row[0] == null ? "" :  ((Integer)row[0]).toString()); 
              n.setAttribute("title",              	 		  row[1] == null ? "" :  ((String) row[1]));
              n.setAttribute("message", 					        row[2] == null ? "" :  ((String) row[2]));
              n.setAttribute("date",              	 		  row[3] == null ? "" :  this.formatDate((java.util.Date)row[3]));
              n.setAttribute("idSubmitter",              	row[4] == null ? "" :  ((Integer)row[4]).toString());
              n.setAttribute("idCoreFacility",            row[5] == null ? "" :  ((Integer)row[5]).toString());
         
              // Add node content to rootElement XML output.
            doc.getRootElement().addContent(n);
        }

        XMLOutputter out = new org.jdom.output.XMLOutputter();
        this.xmlResult = out.outputString(doc);
        
        // Send redirect with response SUCCESS or ERROR page.
        if (isValid()) {
            setResponsePage(this.SUCCESS_JSP);
          } else {
            setResponsePage(this.ERROR_JSP);
          }

      /*} else {
        this.addInvalidField("Insufficient permissions", "Insufficient permission to retrieve NewsItems.");
        setResponsePage(this.ERROR_JSP);
      }*/
    //	}
    }catch (NamingException e){
      log.error("An exception has occurred in GetNewsItem ", e);
      e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());
        
    }catch (SQLException e) {
      log.error("An exception has occurred in GetNewsItem ", e);
      e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e) {
      log.error("An exception has occurred in GetNewsItem ", e);
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