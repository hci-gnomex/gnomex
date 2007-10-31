package hci.gnomex.controller;

import hci.gnomex.utility.HibernateSession;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.framework.security.UnknownPermissionException;
import hci.framework.utilities.XMLReflectException;

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
import org.jdom.Element;
import org.jdom.Document;
import org.jdom.output.XMLOutputter;


import hci.gnomex.model.Project;


public class GetProject extends GNomExCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetProject.class);
  
  private Integer idProject;

  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    if (request.getParameter("idProject") != null) {
      idProject = new Integer(request.getParameter("idProject"));
    } else {
      this.addInvalidField("idProject", "idProject is required");
    }
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      
   
      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
    
      Project project = (Project)sess.get(Project.class, idProject);
      if (this.getSecAdvisor().canRead(project)) {
      
        this.getSecAdvisor().flagPermissions(project);

      
        Document doc = new Document(new Element("OpenProjectList"));
        doc.getRootElement().addContent(project.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement());
      
        XMLOutputter out = new org.jdom.output.XMLOutputter();
        this.xmlResult = out.outputString(doc);
      } else {
        this.addInvalidField("Insufficient Permission", "Insufficient permission to access this project");      
      }
    
      if (isValid()) {
        setResponsePage(this.SUCCESS_JSP);
      } else {
        setResponsePage(this.ERROR_JSP);
      }
    
    }catch (UnknownPermissionException e){
      log.error("An exception has occurred in GetProject ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }catch (NamingException e){
      log.error("An exception has occurred in GetProject ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    }catch (SQLException e) {
      log.error("An exception has occurred in GetProject ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (XMLReflectException e){
      log.error("An exception has occurred in GetProject ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e){
      log.error("An exception has occurred in GetProject ", e);
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