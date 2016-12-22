package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.AppUserLite;
import hci.gnomex.model.Property;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;

import java.io.Serializable;
import java.io.StringReader;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.apache.log4j.Logger;
import org.apache.log4j.Logger;



public class AddPropertyAppUsers extends GNomExCommand implements Serializable {
  
  private Integer idProperty;
  private Document appUsersDoc;
  
  // the static field for logging in Log4J
  private static Logger LOG = Logger.getLogger(AddPropertyAppUsers.class);
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    try {
      idProperty = Integer.parseInt(request.getParameter("idProperty"));
    } catch(Exception ex) {
      addInvalidField("idProperty", "Invalid idProperty string" + (request.getParameter("idProperty") == null ? "NULL" : request.getParameter("idProperty")));
      LOG.error("AddPropertyAppUsers: Invalid idProperty string" + (request.getParameter("idProperty") == null ? "NULL" : request.getParameter("idProperty")), ex);
    }
    
    String appUsersXMLString = request.getParameter("appUsersXMLString");
    try {
      StringReader reader = new StringReader(appUsersXMLString);
      SAXBuilder sax = new SAXBuilder();
      appUsersDoc = sax.build(reader);
    } catch(Exception ex) {
      addInvalidField("idProperty", "Invalid appUsers string" + (appUsersXMLString == null ? "NULL" : appUsersXMLString));
      LOG.error("AddPropertyAppUsers: Invalid appUsers string" + (appUsersXMLString == null ? "NULL" : appUsersXMLString), ex);
    }    
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      
      if (this.getSecurityAdvisor().hasPermission(SecurityAdvisor.CAN_SUBMIT_REQUESTS)) {
        Property property = (Property)sess.load(Property.class, idProperty);
        Hibernate.initialize(property.getOptions());
        Hibernate.initialize(property.getOrganisms());
        Hibernate.initialize(property.getAppUsers());
        Hibernate.initialize(property.getAnalysisTypes());
          
        //
        // Save property users
        //
        if (appUsersDoc != null) {
          Set<AppUserLite> appUsers = (Set<AppUserLite>)property.getAppUsers();
          for(Iterator i = this.appUsersDoc.getRootElement().getChildren().iterator(); i.hasNext();) {
              Element appUserNode = (Element)i.next();
              AppUserLite appUser = (AppUserLite)sess.load(AppUserLite.class, Integer.valueOf(appUserNode.getAttributeValue("idAppUser")));
              Boolean found = false;
              for(AppUserLite oldAppUser : appUsers) {
                if (oldAppUser.getIdAppUser().equals(appUser.getIdAppUser())) {
                  found = true;
                  break;
                }
              }
              if (!found) {
                appUsers.add(appUser);
              }
          }
        }
        
        sess.save(property);
        sess.flush();

        DictionaryHelper.reload(sess);
        
        this.xmlResult = "<SUCCESS idProperty=\"" + property.getIdProperty() + "\"/>";
      
        setResponsePage(this.SUCCESS_JSP);
      } else {
        this.addInvalidField("Insufficient permissions", "Insufficient permission to save property.");
        setResponsePage(this.ERROR_JSP);
      }
    }catch (Exception e){
      LOG.error("An exception has occurred in AddPropertyAppUsers ", e);
      throw new RollBackCommandException(e.getMessage());
        
    }finally {
      try {
        //closeHibernateSession;        
      } catch(Exception e) {
        LOG.error("An exception has occurred in AddPropertyAppUsers", e);
      }
    }
    
    return this;
  }

}