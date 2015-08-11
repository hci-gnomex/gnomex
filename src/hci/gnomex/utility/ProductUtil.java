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

public class ProductUtil {
	
	public static boolean determineIfRequestUsesProducts(Request req) {
		return (req.getIsExternal() != null && !req.getIsExternal().equalsIgnoreCase("Y") && req.getRequestCategory() != null && req.getRequestCategory().getCodeProductType() != null && !req.getRequestCategory().getCodeProductType().trim().equals("")) 
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
		
		Product product = (Product) sess.load(Product.class, req.getIdProduct());
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
	
	public static boolean updateLedgerOnRequestStatusChange(Session sess, Request req, String oldRequestStatus, String newRequestStatus) {
		String statusToUseProducts = determineStatusToUseProducts(sess, req);
		if (statusToUseProducts == null || statusToUseProducts.trim().equals("")) {
			return false;
		}
		
		RequestStatusComparator statusComp = new RequestStatusComparator();
		
		if ((oldRequestStatus == null || statusComp.compare(newRequestStatus, oldRequestStatus) > 0) && newRequestStatus.equals(statusToUseProducts)) {
			Integer productsAvailableToLab = (Integer) sess.createQuery( "select SUM(qty) from ProductLedger where idLab = " + req.getIdLab() + " and idProduct = " + req.getIdProduct() ).uniqueResult();
			Integer productsRequired = determineProductsRequired(sess, req);
			
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
				return false;
			}
		} else if ((oldRequestStatus != null && statusComp.compare(oldRequestStatus, statusToUseProducts) >= 0) && statusComp.compare(newRequestStatus, statusToUseProducts) < 0) {
			Integer productsRequired = determineProductsRequired(sess, req);
			
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

	    		Integer labTotal = (Integer) sess.createQuery( "select SUM(qty) from ProductLedger where idLab = " + po.getIdLab() + " and idProduct = " + pli.getIdProduct() ).uniqueResult();
	    		Integer poTotal = (Integer) sess.createQuery( "select SUM(qty) from ProductLedger where idProductOrder = " + pli.getIdProductOrder() + " and idProduct = " + pli.getIdProduct() ).uniqueResult();

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

}
