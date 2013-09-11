package hci.gnomex.controller;

import hci.gnomex.model.FAQ;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.RequestParser;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;

public class SaveFAQ extends GNomExCommand implements Serializable {
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveFAQ.class);
  private static final long serialVersionUID = 42L;
  
  private FAQ		FAQ;
  private Integer	idFAQ;
  private String	title;
  private String	url;
    
  public void validate() {
  
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    if (request.getParameter("title") != null && !request.getParameter("title").equals("")) {
      title = request.getParameter("title");
      url = request.getParameter("url");
    }
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      
      if(title == null || title.equals("")) {
        this.addInvalidField("FAQ Title", "FAQ title is required.");
        this.setResponsePage(this.ERROR_JSP);
      }else{
    	  FAQ = new FAQ();
    	  FAQ.setTitle(title);		// Set the FAQ Title
      }

      if(url == null || url.equals("")) {
          this.addInvalidField("FAQ URL", "FAQ url is required.");
          this.setResponsePage(this.ERROR_JSP);
       }else{
    	   FAQ.setUrl(url);			// Set the FAQ url
       }
      
      if (this.isValid() && this.getSecAdvisor().canUpdate(FAQ)) {
        sess.save(FAQ);			// Save the FAQ.
        sess.flush();			// Flush session buffer.

        this.xmlResult = "<SUCCESS idNewsItem=\"" + FAQ.getIdFAQ() + "\"/>";
      
        setResponsePage(this.SUCCESS_JSP);
      
      } else {
        this.addInvalidField("Insufficient permissions", "Insufficient permission to save FAQ.");
        setResponsePage(this.ERROR_JSP);
      }
      
    }catch (Exception e){
      log.error("An exception has occurred in SaveFAQ ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }finally {
      try {
        HibernateSession.closeSession();        
      } catch(Exception e) {
    	  System.out.println("EXCEPTION! : " + e);
      }
    }
    
    return this;
  }
  
}