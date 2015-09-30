package hci.gnomex.billing;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.Application;
import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.BillingStatus;
import hci.gnomex.model.Hybridization;
import hci.gnomex.model.LabeledSample;
import hci.gnomex.model.PlateType;
import hci.gnomex.model.PlateWell;
import hci.gnomex.model.Price;
import hci.gnomex.model.PriceCategory;
import hci.gnomex.model.Product;
import hci.gnomex.model.PropertyEntry;
import hci.gnomex.model.Request;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.model.Sample;
import hci.gnomex.model.SequenceLane;
import hci.gnomex.utility.Order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;

public abstract class BillingPlugin {
	
	protected int qty;
	
	public abstract List<BillingItem> constructBillingItems(Session sess, String amendState, BillingPeriod billingPeriod, PriceCategory priceCategory, Request request, 
      Set<Sample> samples, Set<LabeledSample> labeledSamples, Set<Hybridization> hybs, Set<SequenceLane> lanes, Map<String, ArrayList<String>> sampleToAssaysMap, 
      String billingStatus, Set<PropertyEntry> propertyEntries);
	
	protected boolean hasValidData(Session sess, Order request, Set<Sample> samples) {
		if (sess == null || request == null || samples == null || samples.size() == 0) {
			return false;
		}
	    
	    return true;
	}
	
	protected int getQty(Session sess, Order request, Set<Sample> samples) {
		if (sess == null || request == null || samples == null) {
			return 0;
		}
		
		int qtyIfProductBatching = getQtyIfProductBatching(sess, request, samples, samples.size());
		if (qtyIfProductBatching > 0) {
			return qtyIfProductBatching;
		}
		
	    int qtyIfApplicationBatching = getQtyIfApplicationBatching(sess, request, samples, samples.size());
		if (qtyIfApplicationBatching > 0) {
			return qtyIfApplicationBatching;
		}
		
		int qtyIfRequestCategoryBatching = getQtyIfRequestCategoryBatching(sess, request, samples, samples.size());
		if (qtyIfRequestCategoryBatching > 0) {
			return qtyIfRequestCategoryBatching;
		}
		
		return samples.size();
	}
	
	protected int checkQty(Session sess, Order request, Set<Sample> samples, int qty) {
		if (sess == null || request == null || samples == null) {
			return qty;
		}
		
		int qtyIfProductBatching = getQtyIfProductBatching(sess, request, samples, qty);
		if (qtyIfProductBatching > 0) {
			return qtyIfProductBatching;
		}
		
	    int qtyIfApplicationBatching = getQtyIfApplicationBatching(sess, request, samples, qty);
		if (qtyIfApplicationBatching > 0) {
			return qtyIfApplicationBatching;
		}
		
		int qtyIfRequestCategoryBatching = getQtyIfRequestCategoryBatching(sess, request, samples, qty);
		if (qtyIfRequestCategoryBatching > 0) {
			return qtyIfRequestCategoryBatching;
		}
		
		return qty;
	}
	
	protected int getQtyIfProductBatching(Session sess, Order request, Set<Sample> samples, int qty) {
		if (sess != null && request != null && samples != null && request.getIdProduct() != null) {
			Product product = (Product) sess.load(Product.class, request.getIdProduct());
			if (product != null && product.getBatchSamplesByUseQuantity() != null && product.getBatchSamplesByUseQuantity().equalsIgnoreCase("Y") && product.getUseQty() != null) {
				int productBatch = product.getUseQty();
				return doBatching(productBatch, qty);
			}
		}
		
		return -1;
	}
	
	protected int getQtyIfApplicationBatching(Session sess, Order request, Set<Sample> samples, int qty) {
		Application application = null;
	    if (request != null && request.getCodeApplication() != null && !request.getCodeApplication().equals("")) {
	    	application = (Application) sess.get(Application.class, request.getCodeApplication());
	    }
		
		if (sess != null && request != null && application != null && samples != null && application.getSamplesPerBatch() != null) {
			int applicationBatch = application.getSamplesPerBatch();
			return doBatching(applicationBatch, qty);
		}
		
		return -1;
	}
	
	protected int getQtyIfRequestCategoryBatching(Session sess, Order request, Set<Sample> samples, int qty) {
		if (sess != null && request != null && samples != null && request.getCodeRequestCategory() != null) {
			RequestCategory reqCat = (RequestCategory) sess.get(RequestCategory.class, request.getCodeRequestCategory());
			if (reqCat.getSampleBatchSize() != null) {
				int reqCatBatch = reqCat.getSampleBatchSize();
				return doBatching(reqCatBatch, qty);
			}
		}
		
		return -1;
	}
	
	protected int doBatching(int batchSize, int sampleSize) {
		if (batchSize < 1) {
			return sampleSize;
		} else {
			if (batchSize >= sampleSize) {
				return batchSize;
			} else {
				return (sampleSize + (batchSize - 1)) / batchSize * batchSize;
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	protected boolean isSampleInPlateWell(Set<Sample> samples) {
		if (samples != null) {
			for (Sample s : samples) {
				if (s.getWells() != null) {
					for (PlateWell w : (Set<PlateWell>) s.getWells()) {
						if (w.getPlate() != null && w.getPlate().getCodePlateType().equals(PlateType.SOURCE_PLATE_TYPE)) {
							return true;
						}
					}
				}
			}
		}
	    
		return false;
	}
	
	protected ArrayList<BillingItem> makeBillingItems(	Order request, Price price, PriceCategory priceCategory, int qty, BillingPeriod billingPeriod, 
														String billingStatus, String notes, String description, BigDecimal unitPrice, Integer idProductLineItem	) {
		ArrayList<BillingItem> billingItems = new ArrayList<BillingItem>();
		
		billingItems.add(makeBillingItem(request, price, priceCategory, qty, billingPeriod, billingStatus, new BigDecimal(1), Constants.BILLING_SPLIT_TYPE_PERCENT_CODE, notes, description, unitPrice, idProductLineItem));
		
		return billingItems;
	}
	
	protected ArrayList<BillingItem> makeBillingItems(	Order request, Price price, PriceCategory priceCategory, int qty, BillingPeriod billingPeriod, 
														String billingStatus																				) {
		return makeBillingItems(request, price, priceCategory, qty, billingPeriod, billingStatus, null, null, null, null);
	}
	
	protected BillingItem makeBillingItem(	Order request, Price price, PriceCategory priceCategory, int qty, BillingPeriod billingPeriod, 
											String billingStatus, BigDecimal percentagePrice, String splitType, String notes, String description, 
											BigDecimal unitPrice, Integer idProductLineItem															) {
        BillingItem billingItem = new BillingItem();
        
        if (request.getIdRequest() != null) {
        	billingItem.setIdRequest(request.getIdRequest());
        }
        billingItem.setIdLab(request.getIdLab());
        billingItem.setIdBillingAccount(request.getIdBillingAccount());
        billingItem.setIdCoreFacility(request.getIdCoreFacility());
        
        if (description != null) {
        	billingItem.setDescription(description);
        } else {
        	billingItem.setDescription(price.getName());
        }
        billingItem.setIdPrice(price.getIdPrice());
        
        billingItem.setCodeBillingChargeKind(priceCategory.getCodeBillingChargeKind());
        billingItem.setIdPriceCategory(priceCategory.getIdPriceCategory());
        billingItem.setCategory(priceCategory.getName());
        
        BigDecimal theUnitPrice;
        if (unitPrice != null) {
        	theUnitPrice = unitPrice;
        } else {
        	theUnitPrice = price.getEffectiveUnitPrice(request.getLab());
        }
        billingItem.setUnitPrice(theUnitPrice);
        
        billingItem.setQty(new Integer(qty));
        if (qty > 0 && theUnitPrice != null) {      
        	billingItem.setInvoicePrice(theUnitPrice.multiply(new BigDecimal(qty)));
        }
        
        billingItem.setIdBillingPeriod(billingPeriod.getIdBillingPeriod());
        
        billingItem.setCodeBillingStatus(billingStatus);
        if (!billingStatus.equals(BillingStatus.NEW) && !billingStatus.equals(BillingStatus.PENDING)) {
        	billingItem.setCompleteDate(new java.sql.Date(System.currentTimeMillis()));
        }
        
        billingItem.setPercentagePrice(percentagePrice);
        billingItem.setSplitType(splitType);
        
        if (notes != null) {
        	billingItem.setNotes(notes);
        }
        
        if (idProductLineItem != null) {
        	billingItem.setIdProductLineItem(idProductLineItem);
        }

        return billingItem;
	}
	
}
