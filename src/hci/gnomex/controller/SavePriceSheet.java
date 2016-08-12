package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.PriceSheet;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.PriceSheetCategoryParser;

import java.io.Serializable;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.apache.log4j.Logger;



public class SavePriceSheet extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static Logger LOG = Logger.getLogger(SavePriceSheet.class);
  
  private String                     requestCategoriesXMLString;
  private Document                   requestCategoryDoc;
  private PriceSheetCategoryParser   requestCategoryParser;
  

  private PriceSheet                 priceSheetScreen;
  private boolean                   isNewPriceSheet = false;
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    priceSheetScreen = new PriceSheet();
    HashMap errors = this.loadDetailObject(request, priceSheetScreen);
    this.addInvalidFields(errors);
    if (priceSheetScreen.getIdPriceSheet() == null || priceSheetScreen.getIdPriceSheet().intValue() == 0) {
      isNewPriceSheet = true;
    }
    
    
    if (request.getParameter("requestCategoriesXMLString") != null && !request.getParameter("requestCategoriesXMLString").equals("")) {
      requestCategoriesXMLString = request.getParameter("requestCategoriesXMLString");
    }
    
    StringReader reader = new StringReader(requestCategoriesXMLString);
    try {
      SAXBuilder sax = new SAXBuilder();
      requestCategoryDoc = sax.build(reader);
      requestCategoryParser = new PriceSheetCategoryParser(requestCategoryDoc);
    } catch (JDOMException je ) {
      LOG.error( "Cannot parse requestCategoriesXMLString", je );
      this.addInvalidField( "requestCategoriesXMLString", "Invalid requestCategoriesXMLString");
    }

  }

  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      DictionaryHelper dh = DictionaryHelper.getInstance(sess);
      
      if (this.getSecurityAdvisor().hasPermission(SecurityAdvisor.CAN_MANAGE_BILLING)) {
        requestCategoryParser.parse(sess);
        
        PriceSheet priceSheet = null;
              
        if (isNewPriceSheet) {
          priceSheet = priceSheetScreen;
          sess.save(priceSheet);
        } else {
          
          priceSheet = (PriceSheet)sess.load(PriceSheet.class, priceSheetScreen.getIdPriceSheet());
          
          initializePriceSheet(priceSheet);
        }


        //
        // Save priceSheet requestCategory
        //
        TreeSet requestCategories = new TreeSet();
        for(Iterator i = requestCategoryParser.getRequestCategoryMap().keySet().iterator(); i.hasNext();) {
          String codeRequestCategory = (String)i.next();
          RequestCategory requestCategory = dh.getRequestCategoryObject(codeRequestCategory);
          requestCategories.add(requestCategory);
        }
        priceSheet.setRequestCategories(requestCategories);
 
        sess.flush();
        
        this.xmlResult = "<SUCCESS idPriceSheet=\"" + priceSheet.getIdPriceSheet() + "\"/>";
      
        setResponsePage(this.SUCCESS_JSP);
      } else {
        this.addInvalidField("Insufficient permissions", "Insufficient permission to save price sheet.");
        setResponsePage(this.ERROR_JSP);
      }
      
    }catch (Exception e){
      LOG.error("An exception has occurred in SavePriceSheet ", e);

      throw new RollBackCommandException(e.getMessage());
        
    }finally {
      try {
        HibernateSession.closeSession();        
      } catch(Exception e){
        LOG.error("Error", e);
      }
    }
    
    return this;
  }
  
  private void initializePriceSheet(PriceSheet priceSheet) {
    priceSheet.setName(priceSheetScreen.getName());
    priceSheet.setDescription(priceSheetScreen.getDescription());
    priceSheet.setIsActive(priceSheetScreen.getIsActive());
    
  }
  

}