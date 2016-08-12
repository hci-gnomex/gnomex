package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.model.SampleType;
import hci.gnomex.utility.HibernateSession;

import java.io.Serializable;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.apache.log4j.Logger;
public class SaveExperimentPlatformSortOrderList extends GNomExCommand implements Serializable {
  
  // the static field for logging in Log4J
  private static Logger LOG = Logger.getLogger(SaveExperimentPlatformSortOrderList.class);

  private Document categoriesDoc;
  
  public void validate() {
    for(Iterator i = this.categoriesDoc.getRootElement().getChildren().iterator(); i.hasNext();) {
      Element node = (Element)i.next();
      if (node.getAttributeValue("codeRequestCategory") == null || node.getAttributeValue("codeRequestCategory").toString().length() == 0) {
        this.addInvalidField("codeRequestCategory", "each entry in requestCategoriesXMLString must have codeRequestCategory");
        break;
      }
      String codeRequestCategory = node.getAttributeValue("codeRequestCategory");
      if (node.getAttributeValue("sortOrder") == null || node.getAttributeValue("sortOrder").toString().length() == 0) {
        this.addInvalidField("sortOrder", "entry in requestCategoriesXMLString for " + codeRequestCategory + " does not have sortOrder");
        break;
      }
      try {
        Integer sortOrder = Integer.parseInt(node.getAttributeValue("sortOrder"));
      } catch(NumberFormatException ex) {
        this.addInvalidField("sortOrder", "Sort order for " + codeRequestCategory + " is not numberic.");
      }
    }
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    if (request.getParameter("requestCategoriesXMLString") != null && !request.getParameter("requestCategoriesXMLString").equals("")) {
      String requestCategoriesXMLString = request.getParameter("requestCategoriesXMLString");
      StringReader reader = new StringReader(requestCategoriesXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        categoriesDoc = sax.build(reader);     
      } catch (JDOMException je ) {
        LOG.error( "Cannot parse requestCategoriesXMLString", je );
        this.addInvalidField( "requestCategoriesXMLString", "Invalid requestCategoriesXMLString");
      }
    } else {
      this.addInvalidField("requestCategoriesXMLString", "requestCategoriesXMLString is required");
    }

    validate();
    
    if (isValid()) {
      setResponsePage(this.SUCCESS_JSP);
    } else {
      setResponsePage(this.ERROR_JSP);
    }

  }

  public Command execute() throws RollBackCommandException {
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());

      Boolean modified = false;
      for(Iterator i = this.categoriesDoc.getRootElement().getChildren().iterator(); i.hasNext();) {
        Element node = (Element)i.next();
        RequestCategory cat = (RequestCategory)sess.load(RequestCategory.class, node.getAttributeValue("codeRequestCategory"));
        Integer sortOrder = Integer.parseInt(node.getAttributeValue("sortOrder"));
        if (!sortOrder.equals(cat.getSortOrder())) {
          modified = true;
          cat.setSortOrder(sortOrder);
          sess.save(cat);
        }
      }
      
      if (modified) {
        sess.flush();
      }

      this.xmlResult = "<SUCCESS />";

    }catch (Exception e){
      LOG.error("An exception has occurred in SaveExperimentPlatform ", e);

      throw new RollBackCommandException(e.getMessage());
        
    }finally {
      try {
        HibernateSession.closeSession();        
      } catch(Exception e) {
        
      }
    }

    return this;
  }
}
