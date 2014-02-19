package hci.gnomex.controller;

import hci.dictionary.model.DictionaryEntry;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.CoreFacility;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Iterator;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

public class GetLaunchProperties extends GNomExCommand implements Serializable {

  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetLaunchProperties.class);
  
  private String scheme;
  private String serverName;
  private String contextPath;
  private boolean isSecure = false;
  private int serverPort;

  public void loadCommand(HttpServletRequest request, HttpSession session) {
  	try {	
      this.validate();
      scheme = request.getScheme();
      serverPort = request.getServerPort();  
      serverName = request.getServerName();
      contextPath = request.getContextPath();  
      isSecure = request.isSecure();
  	} catch (Exception e) {
  		log.error(e.getClass().toString() + ": " + e);
  		e.printStackTrace();
  	}
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      PropertyDictionary propUniversityUserAuth = (PropertyDictionary)sess.createQuery("from PropertyDictionary p where p.propertyName='" + PropertyDictionary.UNIVERSITY_USER_AUTHENTICATION + "'").uniqueResult();
      PropertyDictionary propSiteLogo = (PropertyDictionary)sess.createQuery("from PropertyDictionary p where p.propertyName='" + PropertyDictionary.SITE_LOGO + "'").uniqueResult();

      String baseURL = "";
      if(serverPort == 80 || (serverPort == 443 && isSecure)){
        baseURL = scheme + "://" + serverName  + contextPath;
      }
      else{
        baseURL = scheme + "://" + serverName + ":" + serverPort  + contextPath;
      }
       
      Document doc = new Document(new Element("LaunchProperties"));
      
      Element node = new Element("Property");
      node.setAttribute( "name", "university_user_authentication" );
      node.setAttribute( "value", (propUniversityUserAuth.getPropertyValue() != null ? propUniversityUserAuth.getPropertyValue() : "N") );
      doc.getRootElement().addContent( node );
      
      node = new Element("Property");
      node.setAttribute( "name", "base_url" );
      node.setAttribute( "value", baseURL );
      doc.getRootElement().addContent( node );

      
      node = new Element("Property");
      node.setAttribute( "name", "site_logo" );
      node.setAttribute( "value", (propSiteLogo != null && propSiteLogo.getPropertyValue() != null ? propSiteLogo.getPropertyValue() : "") );
      doc.getRootElement().addContent( node );

      getCoreFacilities( sess, doc );
      
      XMLOutputter out = new org.jdom.output.XMLOutputter();
      this.xmlResult = out.outputString(doc);
      
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

  private void getCoreFacilities(Session sess, Document doc) {
    Element facilitiesNode = new Element("CoreFacilities");
    doc.getRootElement().addContent(facilitiesNode);

    if ( CoreFacility.getActiveCoreFacilities(sess) == null || CoreFacility.getActiveCoreFacilities(sess).size() == 0) {
      return;
    }
    
    for (Iterator i = CoreFacility.getActiveCoreFacilities(sess).iterator(); i.hasNext();) {
      DictionaryEntry de = (DictionaryEntry)i.next();
      
      if (de == null) {
        continue;
      }
      
      CoreFacility cf = (CoreFacility)de;
      
      
      if (cf.getIsActive() != null && cf.getIsActive().equals("Y")) {
        String coreName = cf.getFacilityName();
        int idCoreFacility = cf.getIdCoreFacility();
        String contactName = PropertyDictionaryHelper.getInstance(sess).getCoreFacilityProperty(idCoreFacility, PropertyDictionary.CONTACT_NAME_CORE_FACILITY);
        String contactNumber = PropertyDictionaryHelper.getInstance(sess).getCoreFacilityProperty(idCoreFacility, PropertyDictionary.CONTACT_PHONE_CORE_FACILITY);
        String contactEmail = PropertyDictionaryHelper.getInstance(sess).getCoreFacilityProperty(idCoreFacility, PropertyDictionary.CONTACT_EMAIL_CORE_FACILITY);

        Element facilityNode = new Element("CoreFacility");
        facilitiesNode.addContent(facilityNode);
        facilityNode.setAttribute("idCoreFacility", String.valueOf( idCoreFacility ));
        facilityNode.setAttribute("coreName", coreName != null ? coreName : "");
        facilityNode.setAttribute("contactName", contactName != null ? contactName : "");
        facilityNode.setAttribute("contactNumber", contactNumber != null ? contactNumber : "");
        facilityNode.setAttribute("contactEmail", contactEmail != null ? contactEmail : "");
      }
    }
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
