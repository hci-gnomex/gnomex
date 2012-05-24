package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.utility.HibernateGuestSession;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.Serializable;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.HibernateException;
import org.hibernate.Session;

public class GetLaunchProperties extends GNomExCommand implements Serializable {

  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetLaunchProperties.class);
  
  private String serverName;
  private String contextPath;

  public void loadCommand(HttpServletRequest request, HttpSession session) {
  	try {	
      this.validate();
      serverName = request.getServerName();
      contextPath = request.getContextPath();
  	} catch (Exception e) {
  		log.error(e.getClass().toString() + ": " + e);
  		e.printStackTrace();
  	}
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      PropertyDictionary propUniversityUserAuth = (PropertyDictionary)sess.createQuery("from PropertyDictionary p where p.propertyName='" + PropertyDictionary.UNIVERSITY_USER_AUTHENTICATION + "'").uniqueResult();


      String portNumber = PropertyDictionaryHelper.getInstance(sess).getProperty(PropertyDictionary.HTTP_PORT);
      if (portNumber == null) {
        portNumber = "";
      } else {
        portNumber = ":" + portNumber;           
      }

      String baseURL =  "http"+  "://"  + serverName + portNumber + contextPath;
       
      this.xmlResult = "<LaunchProperties>";
      this.xmlResult += "<Property name='university_user_authentication' value='" + (propUniversityUserAuth.getPropertyValue() != null ? propUniversityUserAuth.getPropertyValue() : "N") + "'/>";
      this.xmlResult += "<Property name='base_url' value='" + baseURL + "'/>";
      this.xmlResult += "</LaunchProperties>";
      
      validate();
      
    } catch (HibernateException e) {
      log.error(e.getClass().toString() + ": " + e);
      throw new RollBackCommandException();
    } catch (NumberFormatException e) {
      log.error(e.getClass().toString() + ": " + e);
      throw new RollBackCommandException();
    } catch (NamingException e) {
      log.error(e.getClass().toString() + ": " + e);
      throw new RollBackCommandException();
    } catch (SQLException e) {
      log.error(e.getClass().toString() + ": " + e);
      throw new RollBackCommandException();
    } catch (Exception e) {
      log.error(e.getClass().toString() + ": " + e);
      throw new RollBackCommandException();    	
    }
    finally {
      try {
        HibernateSession.closeSession();
      } catch (HibernateException e) {
        log.error(e.getClass().toString() + ": " + e);
        throw new RollBackCommandException();
      } catch (SQLException e) {
        log.error(e.getClass().toString() + ": " + e);
        throw new RollBackCommandException();
      }
    }
    
    return this;
  }

  public void validate() {
    // See if we have a valid form
    if (isValid()) {
      setResponsePage(this.SUCCESS_JSP);
    } else {
      setResponsePage(this.ERROR_JSP);
    }
  }
  
}
