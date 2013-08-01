package hci.gnomex.controller;


import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.model.FlowCell;
import hci.gnomex.security.SecurityAdvisor;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import hci.framework.security.UnknownPermissionException;

import hci.gnomex.security.EncrypterService;
import hci.gnomex.utility.HibernateSession;


public class GetFlowCell extends GNomExCommand implements Serializable {

  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetFlowCell.class);

  // Parameter:
  private Integer idFlowCell;

  public void loadCommand(HttpServletRequest request, HttpSession session) {
    if (request.getParameter("id") != null) {
      idFlowCell = new Integer(request.getParameter("id"));
    } else {
      this.addInvalidField("idFlowCell", "idFlowCell is required");
    }
    this.validate();
  }

  public Command execute() throws RollBackCommandException {
    try {
      if (this.getSecurityAdvisor().hasPermission(SecurityAdvisor.CAN_MANAGE_WORKFLOW)) {

        Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
        FlowCell fc = null;

        if (idFlowCell == null || idFlowCell.intValue() == 0) {
          fc = new FlowCell();
        } else {
          fc = (FlowCell)sess.get(FlowCell.class, idFlowCell);
        }

        Document doc = new Document(new Element("FlowCell"));

        Element fcNode = fc.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();
        doc.getRootElement().addContent(fcNode);

        XMLOutputter out = new org.jdom.output.XMLOutputter();
        this.xmlResult = out.outputString(doc);

      } else {
        this.addInvalidField("Insufficient permissions", "Insufficient permission to manage workflow.");
        setResponsePage(this.ERROR_JSP);
      }

    }catch (UnknownPermissionException e){
      log.error("An exception has occurred in GetFlowCell ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    }catch (NamingException e){
      log.error("An exception has occurred in GetFlowCell ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    }catch (SQLException e) {
      log.error("An exception has occurred in GetFlowCell ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (XMLReflectException e){
      log.error("An exception has occurred in GetFlowCell ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e){
      log.error("An exception has occurred in GetFlowCell ", e);
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

  public void validate() {
    if (isValid()) {
      setResponsePage(this.SUCCESS_JSP);
    } else {
      setResponsePage(this.ERROR_JSP);
    }
  }
}