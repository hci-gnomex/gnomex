package hci.gnomex.controller;

import hci.gnomex.security.SecurityAdvisor;
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

import hci.gnomex.model.AppUser;
import hci.gnomex.model.Lab;


public class GetLab extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetLab.class);
  
  
  private Lab        lab;
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    lab = new Lab();
    HashMap errors = this.loadDetailObject(request, lab);
    this.addInvalidFields(errors);
    
    if (lab.getIdLab() == null) {
      this.addInvalidField("idLab required", "idLab required");
    }
    
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      
   
    Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
    
    Lab theLab = (Lab)sess.get(Lab.class, lab.getIdLab());
    
    if (this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_ADMINISTER_USERS) ||
        this.getSecAdvisor().canUpdate(lab, SecurityAdvisor.PROFILE_GROUP_MEMBERSHIP)) {
      Hibernate.initialize(theLab.getMembers());
      Hibernate.initialize(theLab.getCollaborators());
      Hibernate.initialize(theLab.getManagers());
      
      Document doc = new Document(new Element("OpenLabList"));
      doc.getRootElement().addContent(theLab.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement());
      
      XMLOutputter out = new org.jdom.output.XMLOutputter();
      this.xmlResult = out.outputString(doc);
      
    } else if (this.getSecAdvisor().isGroupIAmMemberOf(lab.getIdLab()) || 
                this.getSecAdvisor().isGroupICollaborateWith(lab.getIdLab())) {
      
      Hibernate.initialize(theLab.getMembers());
      for(Iterator i1 = theLab.getMembers().iterator(); i1.hasNext();) {
        AppUser user = (AppUser)i1.next();
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
      }
      
      Document doc = new Document(new Element("OpenLabList"));
      doc.getRootElement().addContent(theLab.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement());
      
      XMLOutputter out = new org.jdom.output.XMLOutputter();
      this.xmlResult = out.outputString(doc);
      
    } else {
      this.xmlResult = "<OpenLabList/>";
    }
      
    }catch (UnknownPermissionException e){
      log.error("An exception has occurred in GetLab ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }catch (NamingException e){
      log.error("An exception has occurred in GetLab ", e);
      throw new RollBackCommandException(e.getMessage());
        
    }catch (SQLException e) {
      log.error("An exception has occurred in GetLab ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (XMLReflectException e){
      log.error("An exception has occurred in GetLab ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e){
      log.error("An exception has occurred in GetLab ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        this.getSecAdvisor().closeReadOnlyHibernateSession();        
      } catch(Exception e) {
        
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