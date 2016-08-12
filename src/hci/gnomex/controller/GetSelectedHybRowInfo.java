package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;

import java.io.Serializable;
import java.io.StringReader;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.apache.log4j.Logger;



public class GetSelectedHybRowInfo extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static Logger LOG = Logger.getLogger(GetSelectedSampleRowInfo.class);
  
  private String    selectedHybXMLString;
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    
    if (request.getParameter("selectedHybXMLString") != null && !request.getParameter("selectedHybXMLString").equals("")) {
      selectedHybXMLString = request.getParameter("selectedHybXMLString");
    }
    
  }

  public Command execute() throws RollBackCommandException {
    
   try {
      
      Document theDoc = new Document();
      Element theRoot = new Element("SelectedHybRowInfo");
      theDoc.setRootElement(theRoot);
      
      selectedHybXMLString = "<HybList>" + selectedHybXMLString + "</HybList>";
      StringReader reader = new StringReader(selectedHybXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        Document doc = sax.build(reader);
        
        for(Iterator i = doc.getRootElement().getChildren("Hybridization").iterator(); i.hasNext(); ) {
          Element sampleNode = (Element)i.next();
          
          String row = sampleNode.getAttributeValue("row");
          if (row != null) {
            Element theRowNode = new Element("SelectedHyb");
            theRowNode.setAttribute("row", row);
            theRoot.addContent(theRowNode);
            
          }
          
        }
            

      } catch (JDOMException je ) {
        LOG.error( "Cannot parse selectedHybXMLString " + selectedHybXMLString, je );
        this.addInvalidField( "HybRowXML", "Invalid select row hyb XML");
      }

      XMLOutputter out = new org.jdom.output.XMLOutputter();
      this.xmlResult = out.outputString(theDoc);
      
 
    
    setResponsePage(this.SUCCESS_JSP);
    }catch (Exception e){
      LOG.error("An exception has occurred in GetSelectedHybRowInfo ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }
    
    return this;
  }

}