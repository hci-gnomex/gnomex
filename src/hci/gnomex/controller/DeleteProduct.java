package hci.gnomex.controller;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Hibernate;
import org.hibernate.Session;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.Price;
import hci.gnomex.model.Product;
import hci.gnomex.model.ProductType;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;
import org.apache.log4j.Logger;


public class DeleteProduct extends GNomExCommand implements Serializable {

  // the static field for logging in Log4J
  private static Logger LOG = Logger.getLogger(DeleteProduct.class);


  private Integer      idProduct = null;
  private String       resultMessage = "";

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

      Product product = sess.load(Product.class, idProduct);
      ProductType pt = dictionaryHelper.getProductTypeObject(product.getIdProductType());

      if (pt != null && ((pt.getIdCoreFacility() != null && this.getSecAdvisor().isCoreFacilityIManage(pt.getIdCoreFacility())) || this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_ADMINISTER_ALL_CORE_FACILITIES))) {

        // Get and delete the corresponding price
        Price price = GetProductList.getProductPrice( sess, product, pt );
        if ( price != null ) {
          product.setIdPrice( null );
          sess.save( product );
          sess.flush();

          if ( getPriceBillingItems(price, sess) == null || getPriceBillingItems( price, sess ).size() == 0 ) {
            deletePrice( price, sess );
          } else {
            inactivatePrice( price, sess );
          }
        }

        //
        // Delete product
        //
        if ( getProductLedgerEntries( product, sess ) == null || getProductLedgerEntries( product, sess ).size() == 0 ) {
          sess.delete(product);
          resultMessage += product.getDisplay() + " deleted.";
        } else {
          inactivateProduct( product, sess );
          resultMessage += "There are ledger entries for this product.  " + product.getDisplay() + " marked as inactive instead of deleted.";
        }

        sess.flush();

        this.xmlResult = "<SUCCESS message=\"" + resultMessage + "\"/>";

        setResponsePage(this.SUCCESS_JSP);

      } else {
        this.addInvalidField("Insufficient permissions", "Insufficient permissions to delete product.");
        setResponsePage(this.ERROR_JSP);
      }

    }catch (Exception e){
      LOG.error("An exception has occurred in DeleteProduct ", e);

      throw new RollBackCommandException(e.getMessage());

    }finally {
      try {
        //closeHibernateSession;
      } catch(Exception e) {
        LOG.error("An exception has occurred in DeleteProduct ", e);
      }
    }

    return this;
  }

  private void deletePrice(Price price, Session sess) {
    Hibernate.initialize(price.getPriceCriterias());
    sess.delete(price);
    sess.flush();
  }

  private List getPriceBillingItems(Price price, Session sess) {
    if ( price == null ) {
      return null;
    }
    String billingItemQuery = "SELECT bi from BillingItem as bi where bi.idPrice=" + price.getIdPrice();
    List bi = sess.createQuery( billingItemQuery ).list();
    return bi;
  }

  private void inactivatePrice(Price price, Session sess) {
    price.setIsActive( "N" );
    sess.save( price );
    sess.flush();
  }

  private List getProductLedgerEntries(Product product, Session sess) {
    if ( product == null ) {
      return null;
    }
    String ledgerEntryQuery = "SELECT le from ProductLedger as le where le.idProduct=" + product.getIdProduct();
    List le = sess.createQuery( ledgerEntryQuery ).list();
    return le;
  }

  private void inactivateProduct(Product product, Session sess) {
    product.setIsActive( "N" );
    sess.save( product );
    sess.flush();
  }
}