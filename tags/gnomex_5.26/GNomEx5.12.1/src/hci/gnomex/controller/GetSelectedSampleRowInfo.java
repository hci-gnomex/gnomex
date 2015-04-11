package hci.gnomex.controller;

import hci.gnomex.utility.HibernateSession;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.framework.utilities.XMLReflectException;

import java.io.Serializable;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Element;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;




public class GetSelectedSampleRowInfo extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetSelectedSampleRowInfo.class);
  
  private String    selectedSampleXMLString;
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    
    if (request.getParameter("selectedSampleXMLString") != null && !request.getParameter("selectedSampleXMLString").equals("")) {
      selectedSampleXMLString = request.getParameter("selectedSampleXMLString");
      
    }
    
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      
      Document theDoc = new Document();
      Element theRoot = new Element("SelectedSampleRowInfo");
      theDoc.setRootElement(theRoot);
      
      selectedSampleXMLString = "<SampleList>" + selectedSampleXMLString + "</SampleList>";
      StringReader reader = new StringReader(selectedSampleXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        Document doc = sax.build(reader);
        
        for(Iterator i = doc.getRootElement().getChildren("Sample").iterator(); i.hasNext(); ) {
          Element sampleNode = (Element)i.next();
          
          String row = sampleNode.getAttributeValue("row");
          if (row != null) {
            Element theRowNode = new Element("SelectedSample");
            theRowNode.setAttribute("row", row);
            theRoot.addContent(theRowNode);
            
          }
          
        }
            

      } catch (JDOMException je ) {
        log.error( "Cannot parse selectedSampleXMLString " + selectedSampleXMLString, je );
        this.addInvalidField( "SampleRowXML", "Invalidselect row sample XML");
      }

      XMLOutputter out = new org.jdom.output.XMLOutputter();
      this.xmlResult = out.outputString(theDoc);
      
 
    
    setResponsePage(this.SUCCESS_JSP);
    }catch (Exception e){
      log.error("An exception has occurred in GetSelectedLabRowInfo ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }
    
    return this;
  }

}