package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
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




public class MovePriceCategory extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(MovePriceCategory.class);
  

  private Integer               idPriceCategorySource;
  private Integer               idPriceSheetTarget;
  private Integer               idPriceCategoryPosition;
  private String                dropPosition = "after";
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    if (request.getParameter("idPriceCategorySource") != null && !request.getParameter("idPriceCategorySource").equals("")) {
      idPriceCategorySource = Integer.valueOf(request.getParameter("idPriceCategorySource"));
    } else {
      this.addInvalidField("idPriceCategorySource", "idPriceCategorySource required");
    }

    if (request.getParameter("idPriceSheetTarget") != null && !request.getParameter("idPriceSheetTarget").equals("")) {
      idPriceSheetTarget = Integer.valueOf(request.getParameter("idPriceSheetTarget"));
    } else {
      this.addInvalidField("idPriceSheetTarget", "idPriceSheetTarget required");
    }
    
    if (request.getParameter("idPriceCategoryPosition") != null && !request.getParameter("idPriceCategoryPosition").equals("")) {
      idPriceCategoryPosition = Integer.valueOf(request.getParameter("idPriceCategoryPosition"));
      if (this.idPriceCategorySource.equals(this.idPriceCategoryPosition)) {
        this.addInvalidField("sourcetarget", "Cannot move category to same location");
      }
    }

    if (request.getParameter("dropPosition") != null && !request.getParameter("dropPosition").equals("")) {
      dropPosition = request.getParameter("dropPosition");
    }
    
    

  }

  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      
      if (this.getSecurityAdvisor().hasPermission(SecurityAdvisor.CAN_MANAGE_BILLING)) {
        
        
        PriceCategory priceCategory = (PriceCategory)sess.load(PriceCategory.class, idPriceCategorySource);
        String message = "";  
      

        if (idPriceSheetTarget != null) {
          PriceSheet priceSheet = (PriceSheet)sess.load(PriceSheet.class, idPriceSheetTarget);
          PriceSheetPriceCategory existingPriceCategory = null;
          PriceSheetPriceCategory positionPriceCategory = null;
          Integer maxSortOrder = Integer.valueOf(0);
          for(Iterator i = priceSheet.getPriceCategories().iterator(); i.hasNext();) {
            PriceSheetPriceCategory x  = (PriceSheetPriceCategory)i.next();
            PriceCategory cat = x.getPriceCategory();
            if (x.getSortOrder().compareTo(maxSortOrder) > 0) {
              maxSortOrder = x.getSortOrder();
            }
            if (cat.getIdPriceCategory().equals(priceCategory.getIdPriceCategory())) {
              existingPriceCategory = x;
            }
            if (this.idPriceCategoryPosition != null && cat.getIdPriceCategory().equals(idPriceCategoryPosition)) {
              positionPriceCategory = x;
            }
          }
          if (existingPriceCategory == null) {
            // If the price category isn't part of the price sheet,
            // attach it.
            Set priceCategories = priceSheet.getPriceCategories();
            PriceSheetPriceCategory x = new PriceSheetPriceCategory();
            x.setIdPriceCategory(priceCategory.getIdPriceCategory());
            x.setIdPriceSheet(priceSheet.getIdPriceSheet());
            x.setPriceCategory(priceCategory);
            if (positionPriceCategory != null) {
              x.setSortOrder(Integer.valueOf(positionPriceCategory.getSortOrder().intValue() + 1));              
            } else {
              x.setSortOrder(Integer.valueOf(maxSortOrder.intValue() + 1));
            }
            priceCategories.add(x);
            message = "Category " + priceCategory.getName() + " attached to Price Sheet " + priceSheet.getName() + ".";
          } else {
            // If the price category was already part of the price sheet,
            // just reassign the sort order based on the position.
            // of the target price category.
            if (positionPriceCategory != null) {
              List orderedCategories = new ArrayList();
              for(Iterator i = priceSheet.getPriceCategories().iterator(); i.hasNext();) {
                PriceSheetPriceCategory x = (PriceSheetPriceCategory)i.next();
                PriceCategory cat = x.getPriceCategory();
                
                // If we are positioned at the correct price category, 
                // add the price category we are moving
                if (dropPosition.equals("before")) {
                  if (cat.getIdPriceCategory().equals(positionPriceCategory.getPriceCategory().getIdPriceCategory())) {
                    orderedCategories.add(existingPriceCategory);
                  } 
                }
                
                // Add everything in the existing order, except for the price category
                // we are moving
                if (!cat.getIdPriceCategory().equals(existingPriceCategory.getPriceCategory().getIdPriceCategory())) {
                  orderedCategories.add(x);
                }
                // If we are positioned at the correct price category, 
                // add the price category we are moving
                if (dropPosition.equals("after")) {
                  if (cat.getIdPriceCategory().equals(positionPriceCategory.getPriceCategory().getIdPriceCategory())) {
                    orderedCategories.add(existingPriceCategory);
                  } 
                }
              }
              int sortOrder = 0;
              // Now reset the sort order accordingly
              for(Iterator i = orderedCategories.iterator(); i.hasNext();) {
                PriceSheetPriceCategory x = (PriceSheetPriceCategory)i.next();
                x.setSortOrder(Integer.valueOf(sortOrder));
                sortOrder++;
              }
              message = "Category " + priceCategory.getName() + " repositioned in Price Sheet " + priceSheet.getName() + ".";
              
            }
            
            
          }
          
        }
 
        sess.flush();
        
        this.xmlResult = "<SUCCESS idPriceCategory=\"" + priceCategory.getIdPriceCategory() + "\"" + 
                                   " message=\"" + message + "\"" + "/>";
      
        setResponsePage(this.SUCCESS_JSP);
      } else {
        this.addInvalidField("Insufficient permissions", "Insufficient permission to move priceCategory.");
        setResponsePage(this.ERROR_JSP);
      }
      
    }catch (Exception e){
      log.error("An exception has occurred in MovePriceCategory ", e);
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
  


}