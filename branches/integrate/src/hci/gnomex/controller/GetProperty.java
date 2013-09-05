package hci.gnomex.controller;


import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.framework.security.UnknownPermissionException;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.model.Property;

import java.io.Serializable;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;


public class GetProperty extends GNomExCommand implements Serializable {

  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetProperty.class);

  // Parameter:
  private Integer idProperty;

  public void loadCommand(HttpServletRequest request, HttpSession session) {
    if (request.getParameter("idProperty") != null) {
      idProperty = new Integer(request.getParameter("idProperty"));
    } else {
      this.addInvalidField("idProperty", "idProperty is required");
    }
    this.validate();
  }

  public Command execute() throws RollBackCommandException {
    try {

        Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
        Property property = null;

        property = (Property)sess.get(Property.class, idProperty);
        
        Document doc = new Document(new Element("PropertyList"));

        Element node = property.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();
        doc.getRootElement().addContent(node);

        XMLOutputter out = new org.jdom.output.XMLOutputter();
        this.xmlResult = out.outputString(doc);

      

    }catch (UnknownPermissionException e){
      log.error("An exception has occurred in GetProperty ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    }catch (NamingException e){
      log.error("An exception has occurred in GetProperty ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    }catch (SQLException e) {
      log.error("An exception has occurred in GetProperty ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (XMLReflectException e){
      log.error("An exception has occurred in GetProperty ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e){
      log.error("An exception has occurred in GetProperty ", e);
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