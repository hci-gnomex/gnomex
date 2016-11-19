package hci.gnomex.utility;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;

import hci.gnomex.model.Product;
import hci.gnomex.model.ProductLedger;
import hci.gnomex.model.ProductLineItem;
import hci.gnomex.model.ProductOrder;
import hci.gnomex.model.ProductOrderStatus;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.Request;
import hci.gnomex.model.RequestStatus;

public class ProductUtil {

  public static boolean determineIfRequestUsesProducts(Request req) {
    return (req.getIsExternal() != null && !req.getIsExternal().equalsIgnoreCase("Y") && req.getRequestCategory() != null && req.getRequestCategory().getIdProductType() != null && req.getRequestCategory().getIdProductType()!=0)
        || (req.getIdProduct() != null);
  }

  public static String determineStatusToUseProducts(Session sess, Request req) {
    return PropertyDictionaryHelper.getInstance(sess).getCoreFacilityRequestCategoryProperty(req.getIdCoreFacility(), req.getCodeRequestCategory(), PropertyDictionary.STATUS_TO_USE_PRODUCTS);
  }

  public static Integer determineProductsRequired(Session sess, Request req) {
    Integer required = -1;

    if (req.getIdProduct() == null) {
      return required;
    }

    Product product = sess.load(Product.class, req.getIdProduct());
    if (product != null) {
      Integer useQuantity = product.getUseQty();
      Integer sampleNumber = req.getNumberOfSamples();

      required = useQuantity;
      while (required < sampleNumber) {
        required += useQuantity;
      }
    }

    return required;
  }

  public static boolean updateLedgerOnRequestStatusChange(Session sess, Request req, String oldRequestStatus, String newRequestStatus) throws ProductException {
    if (!determineIfRequestUsesProducts(req)) {
      return false;
    }

    String statusToUseProducts = determineStatusToUseProducts(sess, req);
    if (statusToUseProducts == null || statusToUseProducts.trim().equals("")) {
      return false;
    }

    RequestStatusComparator statusComp = new RequestStatusComparator();

    if (isGoingForwardThroughProductGate(oldRequestStatus, newRequestStatus, statusToUseProducts, statusComp)) {
      Integer productsAvailableToLab = (int)(long) sess.createQuery( "select SUM(qty) from ProductLedger where idLab = " + req.getIdLab() + " and idProduct = " + req.getIdProduct() ).uniqueResult();
      Integer productsRequired = determineProductsRequired(sess, req);

      if (productsAvailableToLab == null || productsRequired == null || productsRequired == -1) {
        throw new ProductException("Could not retrieve lab inventory and the number of products required. Please ensure a product is selected and the lab has sufficient inventory.");
      }

      if (productsRequired > -1 && productsAvailableToLab - productsRequired >= 0) {
        ProductLedger ledger = new ProductLedger();

        ledger.setIdLab(req.getIdLab());
        ledger.setIdRequest(req.getIdRequest());
        ledger.setQty(-productsRequired);
        ledger.setComment("Status for Request " + req.getNumber() + " was changed to " + statusToUseProducts + ".");
        ledger.setIdProduct(req.getIdProduct());
        ledger.setTimeStamp(new Timestamp(System.currentTimeMillis()));

        sess.save(ledger);
      } else {
        throw new ProductException("Lab does not have sufficient inventory.");
      }
    } else if (isGoingBackwardThroughProductGate(oldRequestStatus, newRequestStatus, statusToUseProducts, statusComp)) {
      Integer productsRequired = determineProductsRequired(sess, req);

      if (productsRequired == null || productsRequired == -1) {
        throw new ProductException("Could not retrieve the number of products required. Please ensure a product is selected.");
      }

      ProductLedger ledger = new ProductLedger();

      ledger.setIdLab(req.getIdLab());
      ledger.setIdRequest(req.getIdRequest());
      ledger.setQty(productsRequired);
      ledger.setComment("Status for Request " + req.getNumber() + " was changed to " + newRequestStatus + " after being " + statusToUseProducts + ".");
      ledger.setIdProduct(req.getIdProduct());
      ledger.setTimeStamp(new Timestamp(System.currentTimeMillis()));

      sess.save(ledger);
    }

    return true;
  }

  public static boolean updateLedgerOnProductOrderStatusChange(ProductLineItem pli, ProductOrder po, String oldCodePOStatus, String newPOStatus, Session sess, StringBuffer resultMessage) {
    int orderQty = 1;
    if ( pli.getProduct().getOrderQty() != null && pli.getProduct().getOrderQty() > 0 ) {
      orderQty = pli.getProduct().getOrderQty();
    }

    // Check for an old status of something other than completed and new status is completed.
    // If so, add items to ledger
    if ( (oldCodePOStatus == null || !oldCodePOStatus.equals( ProductOrderStatus.COMPLETED )) && newPOStatus.equals( ProductOrderStatus.COMPLETED ) ) {
      ProductLedger ledger = new ProductLedger();
      ledger.setIdLab( po.getIdLab() );
      ledger.setIdProduct( pli.getIdProduct() );
      ledger.setQty( orderQty * pli.getQty() );
      ledger.setTimeStamp( new Timestamp( System.currentTimeMillis() ) );
      ledger.setIdProductOrder( po.getIdProductOrder() );
      ledger.setComment( po.getDisplay() + " changed to completed status." );
      sess.save( ledger );
      resultMessage.append("Status changed for " + po.getDisplay() + ",\n " + pli.getDisplay() + ".\nLedger entry created.\r\n");
    }
    // Check for old status is completed and new status is not.
    // If so, remove items in ledger
    else if ( (oldCodePOStatus != null && oldCodePOStatus.equals( ProductOrderStatus.COMPLETED )) && !newPOStatus.equals( ProductOrderStatus.COMPLETED ) ) {
      if ( pli.getIdProductOrder() != null ) {

        Integer labTotal = (int)(long) sess.createQuery( "select SUM(qty) from ProductLedger where idLab = " + po.getIdLab() + " and idProduct = " + pli.getIdProduct() ).uniqueResult();
        Integer poTotal = (int)(long) sess.createQuery( "select SUM(qty) from ProductLedger where idProductOrder = " + pli.getIdProductOrder() + " and idProduct = " + pli.getIdProduct() ).uniqueResult();

        if ( labTotal - poTotal >= 0 ) {
          String query = "SELECT pl from ProductLedger pl where pl.idProductOrder = " + pli.getIdProductOrder() + " and idProduct = " + pli.getIdProduct() ;
          List ledgerEntries = sess.createQuery(query).list();

          for(Iterator i = ledgerEntries.iterator(); i.hasNext();) {
            ProductLedger ledger = (ProductLedger)i.next();
            sess.delete( ledger );
            resultMessage.append("Status updated for " + po.getDisplay() + ",\n " + pli.getDisplay() + ".\nLedger entries deleted.\r\n");
          }
          sess.flush();
        } else {
          resultMessage.append("Cannot revert status for " + po.getDisplay() + ",\n " + pli.getDisplay() + "; removing ledger entries will result in a negative balance.\r\n");
          return false;
        }
      }
    } else {
      resultMessage.append("Status changed for " + po.getDisplay() + ",\n " + pli.getDisplay() + ".\r\n");
    }
    return true;
  }

  public static boolean isGoingForwardThroughProductGate(String oldRequestStatus, String newRequestStatus, String statusToUseProducts, RequestStatusComparator statusComp) {
    if (oldRequestStatus != null && oldRequestStatus.equals(newRequestStatus)) {
      return false;
    }

    // Allow setting status to failed without advancing through product gate
    if (newRequestStatus.equals(RequestStatus.FAILED) && !statusToUseProducts.equals(RequestStatus.FAILED)) {
      return false;
    }

    if (!statusComp.isTerminationStatus(oldRequestStatus)) {
      return 		(statusComp.compare(newRequestStatus, oldRequestStatus) > 0) 													// New status is further in work flow than old status
          && 	(statusComp.compare(oldRequestStatus, statusToUseProducts) < 0) 												// Old status is behind the product gate
          && 	(statusComp.compare(newRequestStatus, statusToUseProducts) >= 0)												// New status is at or beyond product gate
          && 	(statusComp.isTerminationStatus(statusToUseProducts) ? newRequestStatus.equals(statusToUseProducts) : true);	// Correct termination status was reached if applicable
    } else {
      return 		(!statusToUseProducts.equals(oldRequestStatus))																	// Old status is different termination status than gate
          && 	(newRequestStatus.equals(statusToUseProducts))																	// New status is correct termination status as gate
          &&	(statusComp.isTerminationStatus(statusToUseProducts));															// Product gate is a termination status
    }
  }

  public static boolean isGoingBackwardThroughProductGate(String oldRequestStatus, String newRequestStatus, String statusToUseProducts, RequestStatusComparator statusComp) {
    if (oldRequestStatus != null && oldRequestStatus.equals(newRequestStatus)) {
      return false;
    }

    if (!statusComp.isTerminationStatus(newRequestStatus)) {
      return 		(statusComp.compare(newRequestStatus, oldRequestStatus) < 0) 													// New status is earlier in work flow than old status
          && 	(statusComp.compare(oldRequestStatus, statusToUseProducts) >= 0) 												// Old status is at or beyond the product gate
          && 	(statusComp.compare(newRequestStatus, statusToUseProducts) < 0) 												// New status is behind product gate
          &&	(statusComp.isTerminationStatus(statusToUseProducts) ? statusToUseProducts.equals(oldRequestStatus) : true);	// Old status was at correct termination status if applicable
    } else {
      return		(!newRequestStatus.equals(statusToUseProducts))																	// New status is different termination status than gate
          &&	(statusToUseProducts.equals(oldRequestStatus))																	// Old status is correct termination status as gate
          &&	(statusComp.isTerminationStatus(statusToUseProducts));															// Product gate is a termination status
    }
  }

}
