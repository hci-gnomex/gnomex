package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.Property;
import hci.gnomex.utility.HibernateSession;

import java.io.Serializable;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.HibernateException;
import org.hibernate.Session;

public class GetLaunchProperties extends GNomExCommand implements Serializable {

  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetLaunchProperties.class);

  public void loadCommand(HttpServletRequest request, HttpSession session) {
  	try {	
      this.validate();
  	} catch (Exception e) {
  		log.error(e.getClass().toString() + ": " + e);
  		e.printStackTrace();
  	}
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      Property propUniversityUserAuth = (Property)sess.createQuery("from Property p where p.propertyName='" + Property.UNIVERSITY_USER_AUTHENTICATION + "'").uniqueResult();
    
      this.xmlResult = "<LaunchProperties>";
      this.xmlResult += "<Property name='university_user_authentication' value='" + (propUniversityUserAuth.getPropertyValue() != null ? propUniversityUserAuth.getPropertyValue() : "N") + "'/>";
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
