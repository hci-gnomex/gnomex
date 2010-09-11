package hci.gnomex.controller;

import hci.gnomex.security.SecurityAdvisor;
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

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.jdom.Element;
import org.jdom.Document;
import org.jdom.output.XMLOutputter;

import hci.gnomex.model.AppUser;
import hci.gnomex.model.AppUserFilter;


public class GetAppUserList extends GNomExCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetAppUserList.class);
  
  private AppUserFilter filter;
  private String        listKind = "AppUserList";

  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    filter = new AppUserFilter();
    HashMap errors = this.loadDetailObject(request, filter);
    this.addInvalidFields(errors);
    
    if (request.getParameter("listKind") != null && !request.getParameter("listKind").equals("")) {
      listKind = request.getParameter("listKind");
    }
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      
   
    Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
    
    
    Document doc = new Document(new Element(listKind));

    StringBuffer buf = filter.getQuery(this.getSecAdvisor());
    log.debug("App user query: " + buf.toString());
    List labs = (List)sess.createQuery(buf.toString()).list();


    for(Iterator i = labs.iterator(); i.hasNext();) {
      AppUser user = (AppUser)i.next();

      // Exclude extra user information
      user.excludeMethodFromXML("getCodeUserPermissionKind");
      user.excludeMethodFromXML("getuNID");
      user.excludeMethodFromXML("getEmail");
      user.excludeMethodFromXML("getDepartment");
      user.excludeMethodFromXML("getInstitute");
      user.excludeMethodFromXML("getJobTitle");
      user.excludeMethodFromXML("getCodeUserPermissionKind");
      user.excludeMethodFromXML("getUserNameExternal");
      user.excludeMethodFromXML("getPasswordExternal");
      user.excludeMethodFromXML("getPhone");
      user.excludeMethodFromXML("getIsAdminPermissionLevel");
      user.excludeMethodFromXML("getIsLabPermissionLevel");
      user.excludeMethodFromXML("getLabs");
      user.excludeMethodFromXML("getCollaboratingLabs");
      user.excludeMethodFromXML("getManagingLabs");  
      user.excludeMethodFromXML("getPasswordExternalEntered");
      user.excludeMethodFromXML("getIsExternalUser");
      user.excludeMethodFromXML("getPasswordExternal");

      
      doc.getRootElement().addContent(user.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement());

    }


    
    XMLOutputter out = new org.jdom.output.XMLOutputter();
    this.xmlResult = out.outputString(doc);
    setResponsePage(this.SUCCESS_JSP);
    }catch (NamingException e){
      log.error("An exception has occurred in GetAppUserList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }catch (SQLException e) {
      log.error("An exception has occurred in GetAppUserList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (XMLReflectException e){
      log.error("An exception has occurred in GetAppUserList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e){
      log.error("An exception has occurred in GetAppUserList ", e);
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