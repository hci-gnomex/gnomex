package hci.gnomex.controller;


import hci.gnomex.model.NewsItem;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.RequestParser;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;

import java.io.Serializable;
import java.io.StringReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;



public class SaveNewsItem extends GNomExCommand implements Serializable {
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveNewsItem.class);
  
  private String     newsItemXMLString;
  private Document   newsItemDoc;
  
  private NewsItem	 newsItem;
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    
    if (request.getParameter("newsItemXMLString") != null && !request.getParameter("newsItemXMLString").equals("")) {
      newsItemXMLString = request.getParameter("newsItemXMLString");
    }

    StringReader reader = new StringReader(newsItemXMLString);
    try {
      SAXBuilder sax = new SAXBuilder();
      newsItemDoc = sax.build(reader);
    } catch (JDOMException je ) {
      log.error( "Cannot parse newsItemXMLString", je );
      this.addInvalidField( "RequestXMLString", "Invalid request xml");
    }
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());

      Element newsItemNode = newsItemDoc.getRootElement();
      initializeNewsItem(newsItemNode, sess);      
      
      if(newsItem.getTitle() == null || newsItem.getTitle().equals("")) {
        this.addInvalidField("Newsitem Title", "Newsitem title is required.");
        this.setResponsePage(this.ERROR_JSP);
      }
      
      if (this.isValid() && this.getSecAdvisor().canUpdate(newsItem)) {
        sess.save(newsItem);	// Save the newsitem.
        sess.flush();			// Flush session buffer.

        XMLOutputter out = new org.jdom.output.XMLOutputter();
        this.xmlResult = out.outputString(newsItemDoc);
      

        this.xmlResult = "<SUCCESS idNewsItem=\"" + newsItem.getIdNewsItem() + "\"/>";
      
        setResponsePage(this.SUCCESS_JSP);
      
      } else {
        this.addInvalidField("Insufficient permissions", "Insufficient permission to save NewsItem.");
        setResponsePage(this.ERROR_JSP);
      }
      
    }catch (Exception e){
      log.error("An exception has occurred in SaveNewsItem ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }finally {
      try {
        HibernateSession.closeSession();        
      } catch(Exception e) {
        
      }
    }
    
    return this;
  }
  
  
  private void initializeNewsItem(Element n, Session sess) throws Exception {
    
    Integer idNewsItem = new Integer(n.getAttributeValue("idNewsItem"));
    if (idNewsItem.intValue() == 0) {
    	newsItem = new NewsItem();
    } else {
    	newsItem = (NewsItem)sess.load(NewsItem.class, idNewsItem);
    }
        
    newsItem.setTitle(RequestParser.unEscape(n.getAttributeValue("title")));
    newsItem.setMessage(RequestParser.unEscapeBasic(n.getAttributeValue("message")));
    newsItem.setIdSubmitter(new Integer(n.getAttributeValue("idAppUser")));
    newsItem.setIdCoreTarget(new Integer(n.getAttributeValue("idCoreTarget")));
    newsItem.setIdCoreSender(new Integer(n.getAttributeValue("idCoreSender")));
    
  }

}