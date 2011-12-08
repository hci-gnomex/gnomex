package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.PriceCategory;
import hci.gnomex.model.PriceCriteria;
import hci.gnomex.model.PriceSheet;
import hci.gnomex.model.PriceSheetPriceCategory;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.PriceCriteriaParser;

import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;




public class SavePriceCategory extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SavePriceCategory.class);
  

  private PriceCategory         priceCategoryScreen;
  private Integer               idPriceSheet;
  private boolean              isNewPriceCategory = false;
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    priceCategoryScreen = new PriceCategory();
    HashMap errors = this.loadDetailObject(request, priceCategoryScreen);
    this.addInvalidFields(errors);

    if (request.getParameter("idPriceSheet") != null && !request.getParameter("idPriceSheet").equals("")) {
      idPriceSheet = Integer.valueOf(request.getParameter("idPriceSheet"));
    }

    if (priceCategoryScreen.getIdPriceCategory() == null || priceCategoryScreen.getIdPriceCategory().intValue() == 0) {
      isNewPriceCategory = true;
    }
   

  }

  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      
      if (this.getSecurityAdvisor().hasPermission(SecurityAdvisor.CAN_MANAGE_BILLING)) {
        
        
        PriceCategory priceCategory = null;
              
        if (isNewPriceCategory) {
          priceCategory = priceCategoryScreen;
          sess.save(priceCategory);
        } else {
          
          priceCategory = (PriceCategory)sess.load(PriceCategory.class, priceCategoryScreen.getIdPriceCategory());
          
          initializePrice(priceCategory);
        }

        sess.flush();
        
        //
        // Attach price category to price sheet
        //
        if (idPriceSheet != null) {
          PriceSheet priceSheet = (PriceSheet)sess.load(PriceSheet.class, idPriceSheet);
          boolean found = false;
          Integer maxSortOrder = Integer.valueOf(0);
          for(Iterator i = priceSheet.getPriceCategories().iterator(); i.hasNext();) {
            PriceSheetPriceCategory x  = (PriceSheetPriceCategory)i.next();
            PriceCategory cat = x.getPriceCategory();
            if (x.getSortOrder().compareTo(maxSortOrder) > 0) {
              maxSortOrder = x.getSortOrder();
            }
            if (cat.getIdPriceCategory().equals(priceCategory.getIdPriceCategory())) {
              found = true;
            }
          }
          if (!found) {
            Set priceCategories = priceSheet.getPriceCategories();
            PriceSheetPriceCategory x = new PriceSheetPriceCategory();
            x.setIdPriceCategory(priceCategory.getIdPriceCategory());
            x.setIdPriceSheet(priceSheet.getIdPriceSheet());
            x.setPriceCategory(priceCategory);
            x.setSortOrder(Integer.valueOf(maxSortOrder.intValue() + 1));
            sess.save(x);          
            sess.flush();
          }
          
        }
 
        
        this.xmlResult = "<SUCCESS idPriceCategory=\"" + priceCategory.getIdPriceCategory() + "\"/>";
      
        setResponsePage(this.SUCCESS_JSP);
      } else {
        this.addInvalidField("Insufficient permissions", "Insufficient permission to save priceCategory.");
        setResponsePage(this.ERROR_JSP);
      }
      
    }catch (Exception e){
      log.error("An exception has occurred in SavePriceCategory ", e);
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
  
  private void initializePrice(PriceCategory priceCategory) {
    priceCategory.setName(priceCategoryScreen.getName());
    priceCategory.setDescription(priceCategoryScreen.getDescription());
    priceCategory.setIsActive(priceCategoryScreen.getIsActive());
    priceCategory.setCodeBillingChargeKind(priceCategoryScreen.getCodeBillingChargeKind());
    priceCategory.setDictionaryClassNameFilter1(priceCategoryScreen.getDictionaryClassNameFilter1());
    priceCategory.setDictionaryClassNameFilter2(priceCategoryScreen.getDictionaryClassNameFilter2());
    priceCategory.setPluginClassName(priceCategoryScreen.getPluginClassName());
    
  }

}