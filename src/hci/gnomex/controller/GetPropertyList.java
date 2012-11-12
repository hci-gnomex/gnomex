package hci.gnomex.controller;

import hci.dictionary.utility.DictionaryManager;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.model.Property;
import hci.gnomex.model.PropertyOption;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;


public class GetPropertyList extends GNomExCommand implements Serializable {
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetPropertyList.class);
  
  private String showOptions = "N";

  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    if (request.getParameter("showOptions")!= null && !request.getParameter("showOptions").equals("")) {
      showOptions = request.getParameter("showOptions");
    }
    
    if (isValid()) {
      setResponsePage(this.SUCCESS_JSP);
    } else {
      setResponsePage(this.ERROR_JSP);
    }

  }

  public Command execute() throws RollBackCommandException {

    try {

      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());



      DictionaryManager dictionaryManager = DictionaryManager.getDictionaryManager(ManageDictionaries.DICTIONARY_NAMES_XML, sess, this, true);

      Document doc = new Document(new Element("PropertyList"));

      List properties = sess.createQuery("SELECT prop from Property prop order by prop.name").list();

      for(Iterator i = properties.iterator(); i.hasNext();) {
        
        Property property = (Property)i.next();
        this.getSecAdvisor().flagPermissions(property);
        
        if (showOptions.equals("Y")) {
          property.excludeMethodFromXML("getOptions");
          property.excludeMethodFromXML("getOrganisms");
          property.excludeMethodFromXML("getPlatformApplications");
          property.excludeMethodFromXML("getAnalysisTypes");
        }
        
        Element node = property.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();
        doc.getRootElement().addContent(node);
        
        // Add the option nodes to display in tree of 'Configure Annotations' window.
        if (showOptions.equals("Y")) {
          for (Iterator i1 = property.getOptions().iterator(); i1.hasNext();) {
            PropertyOption option = (PropertyOption)i1.next();
            if (option.getOption().trim().equals("")) {
              continue;
            } 
            Element optionNode = new Element("Option");
            optionNode.setAttribute("name", "    " + option.getOption());
            optionNode.setAttribute("option", option.getOption());
            node.addContent(optionNode);
          }
        }
      }

      org.jdom.output.XMLOutputter out = new org.jdom.output.XMLOutputter();
      this.xmlResult = out.outputString(doc);

      setResponsePage(this.SUCCESS_JSP);
    }catch (NamingException e){
      log.error("An exception has occurred in GetPropertyList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }catch (SQLException e) {
      log.error("An exception has occurred in GetPropertyList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (XMLReflectException e){
      log.error("An exception has occurred in GetPropertyList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e) {
      log.error("An exception has occurred in GetPropertyList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        this.getSecAdvisor().closeReadOnlyHibernateSession();        
      } catch(Exception e) {
        
      }
    }

    if (isValid()) {
      setResponsePage(this.SUCCESS_JSP);
    } else {
      setResponsePage(this.ERROR_JSP);
    }
    
    return this;
  }

}