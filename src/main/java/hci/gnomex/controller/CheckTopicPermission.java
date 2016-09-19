package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.Topic;

import java.io.Serializable;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.apache.log4j.Logger;



public class CheckTopicPermission extends GNomExCommand implements Serializable {
  
  private static Logger LOG = Logger.getLogger(CheckTopicPermission.class);
  
  private Integer idTopic = null;
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    String topicNumber = "";
    if (request.getParameter("topicNumber") != null && !request.getParameter("topicNumber").equals("")) {
      topicNumber = request.getParameter("topicNumber");   
    } else {
      this.addInvalidField("topicNumber", "topicNumber is required");
    }
    if(topicNumber.length() > 0) {
      idTopic = new Integer(topicNumber);
    }
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      
   
      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
      
      if(idTopic != null) {
        Topic dataTrack = (Topic) (sess.load(Topic.class, idTopic));
        if (!this.getSecAdvisor().canRead(dataTrack)) {
          this.addInvalidField("perm", "Insufficient permission to access this topic");
        }          
      } else {
        this.addInvalidField("topicNumber", "topicNumber is either invalid or not provided");
      }
      if (isValid()) {
        this.xmlResult = "<SUCCESS/>";
        setResponsePage(this.SUCCESS_JSP);
      } else {
        setResponsePage(this.ERROR_JSP);
      }
    } catch (Exception e) {
      LOG.error("An exception has occurred in CheckTopicPermission ", e);
      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        this.getSecAdvisor().closeReadOnlyHibernateSession();        
      } catch(Exception e) {
        LOG.error("An exception has occurred in CheckTopicPermission ", e);
      }
    }
    
    return this;
  }
}