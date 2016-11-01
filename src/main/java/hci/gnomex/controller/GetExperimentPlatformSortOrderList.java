package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.RequestCategory;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.query.Query;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.apache.log4j.Logger;
public class GetExperimentPlatformSortOrderList extends GNomExCommand implements Serializable {
  
  // the static field for logging in Log4J
  private static Logger LOG = Logger.getLogger(GetExperimentPlatformSortOrderList.class);

  private Integer idCoreFacility = null;
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    if (request.getParameter("idCoreFacility") != null && !request.getParameter("idCoreFacility").equals("")) {
      try {
        idCoreFacility = new Integer(request.getParameter("idCoreFacility"));
      } catch(NumberFormatException ex) {
        LOG.error("Invalid idCoreFacility for GetExperimentPlatformSortOrderList: " + request.getParameter("idCoreFacility"), ex);
        this.addInvalidField("Missing parameters", "idCoreFacility required");
      }
    } else {
      this.addInvalidField("Missing parameters", "idCoreFacility required");
    }

    
    if (isValid()) {
      setResponsePage(this.SUCCESS_JSP);
    } else {
      setResponsePage(this.ERROR_JSP);
    }

  }

  public Command execute() throws RollBackCommandException {
    try {
      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
      String queryString = "select rc from RequestCategory rc where idCoreFacility=:id AND isActive='Y'";
      Query query = sess.createQuery(queryString);
      query.setParameter("id", idCoreFacility);
      List requestCategories = query.list();
      Document doc = new Document(new Element("ExperimentPlatformSortOrderList"));

      for (RequestCategory cat : (List<RequestCategory>)requestCategories) {
        Element node = new Element("RequestCategory");
        node.setAttribute("codeRequestCategory", cat.getCodeRequestCategory());
        node.setAttribute("requestCategory", cat.getRequestCategory());
        node.setAttribute("sortOrder", cat.getSortOrder() == null ? "0" : cat.getSortOrder().toString());
        doc.getRootElement().addContent(node);
      }
      
      org.jdom.output.XMLOutputter out = new org.jdom.output.XMLOutputter();
      this.xmlResult = out.outputString(doc);
      
    } catch (Exception e) {
      LOG.error("An exception has occurred in GetExperimentPlatformSortOrderList ", e);
      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        //closeReadOnlyHibernateSession;        
      } catch(Exception e) {
        LOG.error("An exception has occurred in GetExperimentPlatformSortOrderList ", e);
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
