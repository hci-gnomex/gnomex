package hci.gnomex.controller;

import hci.gnomex.model.NewsItem;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.RequestParser;
import hci.framework.control.Command;import hci.gnomex.utility.Util;
import hci.framework.control.RollBackCommandException;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.apache.log4j.Logger;
public class SaveNewsItem extends GNomExCommand implements Serializable {
  
  // the static field for logging in Log4J
  private static Logger LOG = Logger.getLogger(SaveNewsItem.class);
  private static final long serialVersionUID = 42L;
  
  private NewsItem	newsItem;
  private Integer	idNewsItem;
  private Integer	idSubmitter;
  private String	title;
  private String	message;
  private Integer 	idCoreFacility = 0;
  
  public void validate() {
  
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    if (request.getParameter("title") != null && !request.getParameter("message").equals("")) {
      title = request.getParameter("title");
      message = request.getParameter("message");
      idNewsItem = Integer.parseInt(request.getParameter("idNewsItem"));
      idSubmitter = Integer.parseInt(request.getParameter("idAppUser"));
      idCoreFacility = Integer.parseInt(request.getParameter("idCoreFacility"));
    }
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      
      initializeNewsItem(sess);
      newsItem.setIdSubmitter(this.getSecAdvisor().getIdAppUser());
      
      if(newsItem.getTitle() == null || newsItem.getTitle().equals("")) {
        this.addInvalidField("Newsitem Title", "Newsitem title is required.");
        this.setResponsePage(this.ERROR_JSP);
      }

      if(newsItem.getMessage() == null || newsItem.getMessage().equals("")) {
          this.addInvalidField("Newsitem Title", "Newsitem message is required.");
          this.setResponsePage(this.ERROR_JSP);
       }      
      
      if (this.isValid() && this.getSecAdvisor().canUpdate(newsItem)) {
        sess.save(newsItem);	// Save the newsitem.
        sess.flush();			// Flush session buffer.

        this.xmlResult = "<SUCCESS idNewsItem=\"" + newsItem.getIdNewsItem() + "\"/>";
      
        setResponsePage(this.SUCCESS_JSP);
      
      } else {
        this.addInvalidField("Insufficient permissions", "Insufficient permission to save NewsItem.");
        setResponsePage(this.ERROR_JSP);
      }
      
    }catch (Exception e){
      this.errorDetails = Util.GNLOG(LOG,"An exception has occurred in SaveNewsItem ", e);
      throw new RollBackCommandException(e.getMessage());
        
    }
    
    return this;
  }
  
  private void initializeNewsItem(Session sess) throws Exception {
    
    if (idNewsItem.intValue() == 0) {
    	newsItem = new NewsItem();
    } else {
    	newsItem = (NewsItem)sess.load(NewsItem.class, idNewsItem);
    }

    newsItem.setTitle(RequestParser.unEscape(title));
    newsItem.setMessage(message);
    newsItem.setIdSubmitter(new Integer(idSubmitter));
    newsItem.setIdCoreFacility(new Integer(idCoreFacility));
    newsItem.setDate(new java.sql.Date(System.currentTimeMillis()));
    
  }

}
