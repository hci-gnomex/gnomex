package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.Price;
import hci.gnomex.model.PriceCriteria;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.PriceCriteriaParser;

import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;




public class SavePrice extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SavePrice.class);
  
  private String                criteriasXMLString;
  private Document              criteriaDoc;
  private PriceCriteriaParser   criteriaParser;
  

  private Price                 priceScreen;
  private boolean              isNewPrice = false;
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    priceScreen = new Price();
    HashMap errors = this.loadDetailObject(request, priceScreen);
    this.addInvalidFields(errors);
    if (priceScreen.getIdPrice() == null || priceScreen.getIdPrice().intValue() == 0) {
      isNewPrice = true;
    }
    
    
    if (request.getParameter("priceCriteriasXMLString") != null && !request.getParameter("priceCriteriasXMLString").equals("")) {
      criteriasXMLString = request.getParameter("priceCriteriasXMLString");
    }
    
    StringReader reader = new StringReader(criteriasXMLString);
    try {
      SAXBuilder sax = new SAXBuilder();
      criteriaDoc = sax.build(reader);
      criteriaParser = new PriceCriteriaParser(criteriaDoc);
    } catch (JDOMException je ) {
      log.error( "Cannot parse priceCriteriasXMLString", je );
      this.addInvalidField( "priceCriteriasXMLString", "Invalid priceCriteriasXMLString");
    }

  }

  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      
      if (this.getSecurityAdvisor().hasPermission(SecurityAdvisor.CAN_MANAGE_BILLING)) {
        criteriaParser.parse(sess);
        
        Price price = null;
              
        if (isNewPrice) {
          price = priceScreen;
          sess.save(price);
        } else {
          
          price = (Price)sess.load(Price.class, priceScreen.getIdPrice());
          
          initializePrice(price);
        }
        sess.flush();


        //
        // Save price criteria
        //
        HashMap criteriaJustAdded = new HashMap();
        for(Iterator i = criteriaParser.getPriceCriteriaMap().keySet().iterator(); i.hasNext();) {
          String idPriceCriteria = (String)i.next();
          PriceCriteria criteria = (PriceCriteria)criteriaParser.getPriceCriteriaMap().get(idPriceCriteria);
          criteria.setIdPrice(price.getIdPrice());
          
          if (criteria.getIdPriceCriteria() == null) {
            sess.save(criteria);
            criteriaJustAdded.put(criteria.getIdPriceCriteria(), null);
            
          }
        }
        sess.flush();
        
        // Remove price criteria no longer in the criteria list
        List criteriaToRemove = new ArrayList();
        if (price.getPriceCriterias() != null) {
          for(Iterator i = price.getPriceCriterias().iterator(); i.hasNext();) {
            PriceCriteria c = (PriceCriteria)i.next();
            // If the existing criteria is not in the XML, then it might
            // be a criteria that was removed.
            if (!criteriaParser.getPriceCriteriaMap().containsKey(c.getIdPriceCriteria().toString())) {
              // Don't remove the criteria that was just inserted.  
              if (!criteriaJustAdded.containsKey(c.getIdPriceCriteria())) {
                criteriaToRemove.add(c);
                
              }
            }
          }
          for(Iterator i = criteriaToRemove.iterator(); i.hasNext();) {
            PriceCriteria c = (PriceCriteria)i.next();
            sess.delete(c);
          }
        }
 
        sess.flush();
        
        this.xmlResult = "<SUCCESS idPrice=\"" + price.getIdPrice() + "\"/>";
      
        setResponsePage(this.SUCCESS_JSP);
      } else {
        this.addInvalidField("Insufficient permissions", "Insufficient permission to save price.");
        setResponsePage(this.ERROR_JSP);
      }
      
    }catch (Exception e){
      log.error("An exception has occurred in SavePrice ", e);
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
  
  private void initializePrice(Price price) {
    price.setName(priceScreen.getName());
    price.setDescription(priceScreen.getDescription());
    price.setUnitPrice(priceScreen.getUnitPrice());
    price.setUnitPriceExternalAcademic(priceScreen.getUnitPriceExternalAcademic());
    price.setIdPriceCategory(priceScreen.getIdPriceCategory());
    price.setIsActive(priceScreen.getIsActive());
    
  }
  

}