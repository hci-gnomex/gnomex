package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.Price;
import hci.gnomex.model.Product;
import hci.gnomex.model.ProductType;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Hibernate;
import org.hibernate.Session;



public class DeleteProduct extends GNomExCommand implements Serializable {
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DeleteProduct.class);
  
  
  private Integer      idProduct = null; 
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
   if (request.getParameter("idProduct") != null && !request.getParameter("idProduct").equals("")) {
     idProduct = new Integer(request.getParameter("idProduct"));
   }

  }

  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      DictionaryHelper dictionaryHelper = DictionaryHelper.getInstance(sess);
      
      Product product = (Product)sess.load(Product.class, idProduct);
      ProductType pt = dictionaryHelper.getProductTypeObject(product.getCodeProductType());
           
      if (pt != null && ((pt.getIdCoreFacility() != null && this.getSecAdvisor().isCoreFacilityIManage(pt.getIdCoreFacility())) || this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_ADMINISTER_ALL_CORE_FACILITIES))) {
          
        // Get and delete the corresponding price        
        Price price = GetProductList.getProductPrice( sess, product, pt );
        if ( price != null ) {
          product.setIdPrice( null );
          sess.flush();
          
          Hibernate.initialize(price.getPriceCriterias());
          sess.delete(price);
        }
        
        //
        // Delete product
        //
        sess.delete(product);
        
        sess.flush();
        
        this.xmlResult = "<SUCCESS/>";
      
        setResponsePage(this.SUCCESS_JSP);
        
      } else {
        this.addInvalidField("Insufficient permissions", "Insufficient permissions to delete product.");
        setResponsePage(this.ERROR_JSP);
      }
      
    }catch (Exception e){
      log.error("An exception has occurred in DeleteProduct ", e);
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