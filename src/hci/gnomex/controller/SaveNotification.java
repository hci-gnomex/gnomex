package hci.gnomex.controller;

import hci.gnomex.model.Notification;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.RequestParser;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.apache.log4j.Logger;
public class SaveNotification extends GNomExCommand implements Serializable {
  
  // the static field for logging in Log4J
  private static Logger LOG = Logger.getLogger(SaveNotification.class);
  private static final long serialVersionUID = 42L;
  private Notification		notification;
  
  public void validate() {
  
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
	  
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());

      if(notification == null){
    	  this.addInvalidField("Notification Object Empty", "Notification object is empty!");
    	  this.setResponsePage(this.ERROR_JSP);
      }
      
      if(notification.getSourceType() == null || notification.getSourceType().equals("")) {
        this.addInvalidField("Notification Sourcetype", "Notification sourcetype is required.");
        this.setResponsePage(this.ERROR_JSP);
      }
      
      if (this.isValid() && this.getSecAdvisor().canUpdate(notification)) {
        sess.save(notification);			// Save the notification.
        sess.flush();						// Flush session buffer.

        this.xmlResult = "<SUCCESS idNotification=\"" + notification.getIdNotification() + "\"/>";
      
        setResponsePage(this.SUCCESS_JSP);
      } else {
        this.addInvalidField("Insufficient permissions", "Insufficient permission to save Notification.");
        setResponsePage(this.ERROR_JSP);
      }
      
    }catch (Exception e){
      LOG.error("An exception has occurred in SaveNotification ", e);

      throw new RollBackCommandException(e.getMessage());
        
    }finally {
      try {
        HibernateSession.closeSession();        
      } catch(Exception e) {
    	  System.out.println("Exception in SaveNotification : " + e);
      }
    }
    
    return this;
  }

  public void setNotification(Notification n){
	  this.notification = n;
  }

}