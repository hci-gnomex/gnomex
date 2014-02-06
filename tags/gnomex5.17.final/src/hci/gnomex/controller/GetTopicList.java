package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.utility.TopicQuery;

import java.io.Serializable;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;


public class GetTopicList extends GNomExCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetTopicList.class);
  
  private TopicQuery topicQuery;
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    topicQuery = new TopicQuery(request);
    if (isValid()) {
      setResponsePage(this.SUCCESS_JSP);
    } else {
      setResponsePage(this.ERROR_JSP);
    }    
  }

  public Command execute() throws RollBackCommandException {
    try {
      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
      
      Document doc = topicQuery.getTopicDocument(sess, this.getSecAdvisor());
      
      org.jdom.output.XMLOutputter out = new org.jdom.output.XMLOutputter();
      this.xmlResult = out.outputString(doc);
    
      setResponsePage(this.SUCCESS_JSP);      
      
    }catch (NamingException e){
      log.error("An exception has occurred in GetTopicList ", e);
      e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());
    }catch (SQLException e) {
      log.error("An exception has occurred in GetTopicList ", e);
      e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e) {
      log.error("An exception has occurred in GetTopicList ", e);
      e.printStackTrace(System.out);
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