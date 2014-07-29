package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.Application;
import hci.gnomex.model.Price;
import hci.gnomex.model.PriceCriteria;
import hci.gnomex.model.Product;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Element;


public class SaveProduct extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveProduct.class);
  
  private Product             productScreen;
  private boolean             isNewProduct = false;
  private BigDecimal          unitPriceInternal;
  private BigDecimal          unitPriceExternalAcademic;
  private BigDecimal          unitPriceExternalCommercial;

  private static final String PRICE_INTERNAL              = "internal";
  private static final String PRICE_EXTERNAL_ACADEMIC     = "academic";
  private static final String PRICE_EXTERNAL_COMMERCIAL   = "commercial";
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    productScreen = new Product();
    HashMap errors = this.loadDetailObject(request, productScreen);
        
    this.addInvalidFields(errors);
        
    if (productScreen.getIdProduct() == null || productScreen.getIdProduct().intValue() == 0) {
      isNewProduct = true;
    }
    
    if (request.getParameter("unitPriceInternal") != null && request.getParameter("unitPriceInternal").length() > 0) {
      try {
        unitPriceInternal = new BigDecimal(request.getParameter("unitPriceInternal"));
      } catch(NumberFormatException e) {
        log.error("Unable to parse internal price: " + request.getParameter("unitPriceInternal"), e);
      }    
    } else {
      unitPriceInternal = null;
    }
    
    if (request.getParameter("unitPriceExternalAcademic") != null && request.getParameter("unitPriceExternalAcademic").length() > 0) {
      try {
        unitPriceExternalAcademic = new BigDecimal(request.getParameter("unitPriceExternalAcademic"));
      } catch(NumberFormatException e) {
        log.error("Unable to parse internal price: " + request.getParameter("unitPriceExternalAcademic"), e);
      }    
    } else {
      unitPriceInternal = null;
    }
    
    if (request.getParameter("unitPriceExternalCommercial") != null && request.getParameter("unitPriceExternalCommercial").length() > 0) {
      try {
        unitPriceExternalCommercial = new BigDecimal(request.getParameter("unitPriceExternalCommercial"));
      } catch(NumberFormatException e) {
        log.error("Unable to parse internal price: " + request.getParameter("unitPriceExternalCommercial"), e);
      }    
    } else {
      unitPriceInternal = null;
    }
    
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      
      if (this.getSecurityAdvisor().hasPermission(SecurityAdvisor.CAN_MANAGE_DNA_SEQ_CORE)) {

        
        Product product = null;
        
        if (isNewProduct) {
          product = productScreen;

          sess.save(product);
          sess.flush();

        } else {
          
          product = (Product)sess.load(Product.class, productScreen.getIdProduct());
          
          initializeProduct(product);
          
          sess.save(product);
          sess.flush();
        }
        
//        saveProductPrices(sess,);
               
        DictionaryHelper.reload(sess);
        
        this.xmlResult = "<SUCCESS idProduct=\"" + product.getIdProduct() + "\"/>";
      
        setResponsePage(this.SUCCESS_JSP);
      } else {
        this.addInvalidField("Insufficient permissions", "Insufficient permission to save product.");
        setResponsePage(this.ERROR_JSP);
      }
      
    }catch (Exception e){
      log.error("An exception has occurred in SaveProduct ", e);
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
  
  private void initializeProduct(Product product) {
    
    product.setName(productScreen.getName());
    product.setCodeProductType(productScreen.getCodeProductType());
    product.setIdPrice(productScreen.getIdPrice());
    product.setOrderQty(productScreen.getOrderQty());
    product.setUseQty(productScreen.getUseQty());
    product.setCatalogNumber(productScreen.getCatalogNumber());
    product.setIsActive(productScreen.getIsActive());
    
  }
  
  private void saveProductPrices(Session sess, RequestCategory rc, Application app, Element node, Map<String, Price> map, Integer defaultCategoryId) {
    
    // Get id price category from product type
    // Check to see if there is a price in db already - search by name and idPriceCategory
    // Get existing Price or create a new one
    // Initialize Price
    // Create a filter with filter1 = idproduct, idPrice
    
    if (!priceModified()) {
      return;
    }

    Boolean modified = false;
    Price price = map.get(app.getCodeApplication());
    if (price == null) {
      if (defaultCategoryId == null) {
        // no default price category -- can't store new price.
        log.error("SaveExperimentPlatform: Unable to store new lib prep price due to no default category for " + rc.getCodeRequestCategory());
        return;
      }
      price = new Price();
      price.setName(app.getApplication());
      price.setDescription("");
      price.setIdPriceCategory(defaultCategoryId);
      price.setIsActive("Y");
      price.setUnitPrice(BigDecimal.ZERO);
      price.setUnitPriceExternalAcademic(BigDecimal.ZERO);
      price.setUnitPriceExternalCommercial(BigDecimal.ZERO);
      sess.save(price);
      sess.flush();
      PriceCriteria crit = new PriceCriteria();
      crit.setIdPrice(price.getIdPrice());
      crit.setFilter1(app.getCodeApplication());
      sess.save(crit);
      modified = true;
    }

    if (setPrice(unitPriceInternal, price.getUnitPrice(), price, PRICE_INTERNAL)) {
      modified = true;
    }
    if (setPrice(unitPriceExternalAcademic, price.getUnitPriceExternalAcademic(), price, PRICE_EXTERNAL_ACADEMIC)) {
      modified = true;
    }
    if (setPrice(unitPriceExternalCommercial, price.getUnitPriceExternalCommercial(), price, PRICE_EXTERNAL_COMMERCIAL)) {
      modified = true;
    }
    
    if (modified) {
      sess.flush();
    }
  }
  
  private Boolean priceModified() {
    if (unitPriceInternal != null || unitPriceExternalAcademic != null  || unitPriceExternalCommercial != null ) {
      return true;
    }
    return false;
  }
  
  private Boolean setPrice(BigDecimal value, BigDecimal existingPrice, Price price, String whichPrice) {
    Boolean modified = false;
    
    if (existingPrice == null || !existingPrice.equals(value)) {
      setPrice(value, price, whichPrice);
      modified = true;
    }
    
    return modified;
  }
  
  private void setPrice(BigDecimal value, Price price, String whichPrice) {
    if (whichPrice.equals(PRICE_INTERNAL)) {
      price.setUnitPrice(value);
    } else if (whichPrice.equals(PRICE_EXTERNAL_ACADEMIC)) {
      price.setUnitPriceExternalAcademic(value);
    } else if (whichPrice.equals(PRICE_EXTERNAL_COMMERCIAL)) {
      price.setUnitPriceExternalCommercial(value);
    }
  }
  
  private class ProductComparator implements Comparator, Serializable {
    public int compare(Object o1, Object o2) {
      Product org1 = (Product)o1;
      Product org2 = (Product)o2;
      
      return org1.getIdProduct().compareTo(org2.getIdProduct());
      
    }
  }
}