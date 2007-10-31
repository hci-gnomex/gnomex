package hci.gnomex.controller;

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

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Element;
import org.jdom.Document;
import org.jdom.output.XMLOutputter;

import hci.gnomex.model.RequestFilter;
import hci.gnomex.model.Request;


public class GetRequestList extends GNomExCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetRequestList.class);
  
  private RequestFilter requestFilter;
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    requestFilter = new RequestFilter();
    HashMap errors = this.loadDetailObject(request, requestFilter);
    this.addInvalidFields(errors);
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      
   
    Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
    
    StringBuffer buf = requestFilter.getQuery(this.getSecAdvisor());
    log.info("Query for GetRequestList: " + buf.toString());
    List labs = (List)sess.createQuery(buf.toString()).list();
    
    Document doc = new Document(new Element("RequestList"));
    for(Iterator i = labs.iterator(); i.hasNext();) {
      Request req = (Request)i.next();
      doc.getRootElement().addContent(req.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement());
      
    }
    
    XMLOutputter out = new org.jdom.output.XMLOutputter();
    this.xmlResult = out.outputString(doc);
    
    setResponsePage(this.SUCCESS_JSP);
    }catch (NamingException e){
      log.error("An exception has occurred in GetRequestList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    }catch (SQLException e) {
      log.error("An exception has occurred in GetRequestList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (XMLReflectException e){
      log.error("An exception has occurred in GetRequestList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e){
      log.error("An exception has occurred in GetRequestList ", e);
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