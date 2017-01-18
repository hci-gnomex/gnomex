package hci.gnomex.controller;

import hci.framework.control.Command;import hci.gnomex.utility.Util;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.ProjectFilter;

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

public class GetProjectList extends GNomExCommand implements Serializable {

  private static Logger LOG = Logger.getLogger(GetProjectList.class);

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


    Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());

    StringBuffer buf = projectFilter.getQuery(this.getSecAdvisor());
    LOG.info("Query for GetProjectList: " + buf.toString());
    List projects = (List)sess.createQuery(buf.toString()).list();

    Document doc = new Document(new Element("ProjectList"));
    for(Iterator i = projects.iterator(); i.hasNext();) {
      Object[] row = (Object[])i.next();

      Integer idProject      = (Integer)row[0];
      String name            = (String)row[1]  == null ? ""  : (String)row[1];
      String description     = (String)row[2]  == null ? ""  : (String)row[2];
      String idLab           = (Integer)row[3] == null ? ""  : ((Integer)row[3]).toString();
      String idAppUser       = (Integer)row[4] == null ? ""  : ((Integer)row[4]).toString();

      Element node = new Element("Project");
      node.setAttribute("idProject", idProject.toString());
      node.setAttribute("name", name);
      node.setAttribute("description", description);
      node.setAttribute("idLab", idLab);
      node.setAttribute("idAppUser", idAppUser);

      doc.getRootElement().addContent(node);

    }

    XMLOutputter out = new org.jdom.output.XMLOutputter();
    this.xmlResult = out.outputString(doc);

    setResponsePage(this.SUCCESS_JSP);
    }catch (NamingException e){
      this.errorDetails = Util.GNLOG(LOG,"An exception has occurred in GetProjectList ", e);

      throw new RollBackCommandException(e.getMessage());
    }catch (SQLException e) {
      this.errorDetails = Util.GNLOG(LOG,"An exception has occurred in GetProjectList ", e);

      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e) {
      this.errorDetails = Util.GNLOG(LOG,"An exception has occurred in GetProjectList ", e);

      throw new RollBackCommandException(e.getMessage());
    }
    return this;
  }

}