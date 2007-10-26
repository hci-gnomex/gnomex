package hci.gnomex.controller;

import hci.gnomex.model.Project;
import hci.gnomex.model.ProjectFilter;
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
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;


public class GetProjectList extends GNomExCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetProjectList.class);
  
  private ProjectFilter projectFilter;
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    projectFilter = new ProjectFilter();
    HashMap errors = this.loadDetailObject(request, projectFilter);
    this.addInvalidFields(errors);
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      
   
    Session sess = HibernateSession.currentSession(this.getUsername());
    
    StringBuffer buf = projectFilter.getQuery(this.getSecAdvisor());
    log.info("Query for GetProjectList: " + buf.toString());
    List projects = (List)sess.createQuery(buf.toString()).list();
    
    Document doc = new Document(new Element("ProjectList"));
    for(Iterator i = projects.iterator(); i.hasNext();) {
      Object[] row = (Object[])i.next();
      
      Integer idProject      = (Integer)row[0];
      String name            = (String)row[1]  == null ? ""  : (String)row[1];
      String description     = (String)row[2]  == null ? ""  : (String)row[2];
      String idLab           = (Integer)row[3] == null ? ""  : ((Integer)row[3]).toString();
      String codeVisibility  = (String)row[4]  == null ? ""  : (String)row[4];
      
      Element node = new Element("Project");
      node.setAttribute("idProject", idProject.toString());
      node.setAttribute("name", name);
      node.setAttribute("description", description);
      node.setAttribute("idLab", idLab);
      node.setAttribute("codeVisibility", codeVisibility);
      
      doc.getRootElement().addContent(node);
      
    }
    
    XMLOutputter out = new org.jdom.output.XMLOutputter();
    this.xmlResult = out.outputString(doc);
    
    setResponsePage(this.SUCCESS_JSP);
    }catch (NamingException e){
      log.error("An exception has occurred in GetProjectList ", e);
      throw new RollBackCommandException(e.getMessage());
        
    }catch (SQLException e) {
      log.error("An exception has occurred in GetProjectList ", e);
      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        HibernateSession.closeSession();        
      } catch(Exception e) {
        
      }
    }
    
    return this;
  }

}