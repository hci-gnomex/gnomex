package hci.gnomex.controller;

import hci.gnomex.model.FAQ;
import hci.gnomex.model.Institution;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.RequestParser;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;

import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class SaveFAQ extends GNomExCommand implements Serializable {
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveFAQ.class);
  private static final long serialVersionUID = 42L;
  
  private FAQ		FAQ;
  private Integer	idFAQ;
  private String	title;
  private String	url;
  private String  faqXMLString;
  private Document faqDoc;
    
  public void validate() {
  
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    if (request.getParameter("faqXMLString") != null && !request.getParameter("faqXMLString").equals("")) {
      faqXMLString = request.getParameter("faqXMLString");
      StringReader reader = new StringReader(faqXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        faqDoc = sax.build(reader);
      } catch (JDOMException je) {
        log.error("Cannot parse faqXMLString", je);
        this.addInvalidField("faqXMLString",
        "Invalid faqXMLString");
      }
    }
    
    if (faqDoc == null) {
      this.addInvalidField("institutionsXMLString",
      "institutionsXMLString is required");
    }
  }

  public Command execute() throws RollBackCommandException {
    
    FAQ faq = null;
    List faqsToDelete = new ArrayList();

    try {
    if(this.isValid()){
        Session sess = HibernateSession.currentSession(this.getUsername());

        StringBuffer query = new StringBuffer("SELECT f from FAQ f");
        List faqs = sess.createQuery(query.toString()).list();

        for (int i = 0; i < faqs.size(); i++) {
          boolean isFound = false;
          FAQ dbFaq = (FAQ) faqs.get(i);
          for (Iterator j = this.faqDoc.getRootElement().getChildren().iterator(); j.hasNext();) {
            Element node = (Element) j.next();
            //If it isn't a new institution and the inst. from the doc is in the database then we don't delete it
            if (!node.getAttributeValue("idFAQ").equals("") && 
                dbFaq.getIdFAQ().equals(Integer.parseInt(node.getAttributeValue("idFAQ")))) {
              isFound = true;
              break;
            }
          }
          //if we can't find it, delete it
          if (!isFound) {
            faqsToDelete.add(dbFaq);
          }
        }

        if (faqDoc != null) {
          for (Iterator i = this.faqDoc.getRootElement().getChildren().iterator(); i.hasNext();) {
            Element node = (Element) i.next();

            String idFaq = node.getAttributeValue("idFAQ");
            if (idFaq == null || idFaq.equals("")) {
              faq = new FAQ();
            } 
            else {
              faq = (FAQ) sess.load(FAQ.class, Integer.valueOf(idFaq));
            }

            faq.setTitle(node.getAttributeValue("title"));
            faq.setUrl(node.getAttributeValue("url"));
            sess.save(faq);
          }
          
          for (Iterator j = faqsToDelete.iterator(); j.hasNext();){
            FAQ faqToDelete = (FAQ)j.next();
            sess.delete(faqToDelete);
        }
          sess.flush();
        }
      
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