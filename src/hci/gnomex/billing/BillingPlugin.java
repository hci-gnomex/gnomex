package hci.gnomex.billing;

import hci.gnomex.constants.Constants;
import hci.gnomex.controller.SaveBillingTemplate;
import hci.gnomex.model.*;
import hci.gnomex.utility.Order;

import java.math.BigDecimal;
import java.util.*;

import org.hibernate.Session;

public abstract class BillingPlugin {

	protected int qty;

	public abstract List<BillingItem> constructBillingItems(Session sess, String amendState, BillingPeriod billingPeriod, PriceCategory priceCategory, Request request,
      Set<Sample> samples, Set<LabeledSample> labeledSamples, Set<Hybridization> hybs, Set<SequenceLane> lanes, Map<String, ArrayList<String>> sampleToAssaysMap,
      String billingStatus, Set<PropertyEntry> propertyEntries, BillingTemplate billingTemplate);

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
			Product product = sess.load(Product.class, request.getIdProduct());
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
	    	application = sess.get(Application.class, request.getCodeApplication());
	    }

		if (sess != null && request != null && application != null && samples != null && application.getSamplesPerBatch() != null) {
			int applicationBatch = application.getSamplesPerBatch();
			return doBatching(applicationBatch, qty);
		}

		return -1;
	}

	protected int getQtyIfRequestCategoryBatching(Session sess, Order request, Set<Sample> samples, int qty) {
		if (sess != null && request != null && samples != null && request.getCodeRequestCategory() != null) {
			RequestCategory reqCat = sess.get(RequestCategory.class, request.getCodeRequestCategory());
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
														String billingStatus, String notes, String description, BigDecimal unitPrice, Integer idProductOrder,
														Session sess, BillingTemplate billingTemplate															) {

		ArrayList<BillingItem> billingItems = new ArrayList<BillingItem>();

		billingItems.addAll(makeBillingItem(request, price, priceCategory, qty, billingPeriod, billingStatus, new BigDecimal(1), Constants.BILLING_SPLIT_TYPE_PERCENT_CODE, notes, description, unitPrice, idProductOrder, sess, billingTemplate));

		return billingItems;
	}

	protected ArrayList<BillingItem> makeBillingItems(	Order request, Price price, PriceCategory priceCategory, int qty, BillingPeriod billingPeriod,
														String billingStatus, Session sess, BillingTemplate billingTemplate								) {

		return makeBillingItems(request, price, priceCategory, qty, billingPeriod, billingStatus, null, null, null, null, sess, billingTemplate);
	}

	protected Set<BillingItem> makeBillingItem(	Order request, Price price, PriceCategory priceCategory, int qty, BillingPeriod billingPeriod,
												String billingStatus, BigDecimal percentagePrice, String splitType, String notes, String description,
												BigDecimal unitPrice, Integer idProductOrder, Session sess, BillingTemplate template					) {

		MasterBillingItem master = new MasterBillingItem();
		master.setIdCoreFacility(request.getIdCoreFacility());
        if (description != null) {
        	master.setDescription(description);
        } else {
        	master.setDescription(price.getName());
        }
        master.setIdPrice(price.getIdPrice());
        master.setCodeBillingChargeKind(priceCategory.getCodeBillingChargeKind());
        master.setIdPriceCategory(priceCategory.getIdPriceCategory());
        master.setCategory(priceCategory.getName());
        BigDecimal theUnitPrice;
        if (unitPrice != null) {
        	theUnitPrice = unitPrice;
        } else {
        	theUnitPrice = price.getEffectiveUnitPrice(request.getLab());
        }
        master.setUnitPrice(theUnitPrice);
        master.setQty(new Integer(qty));
        master.setTotalPrice(theUnitPrice.multiply(BigDecimal.valueOf(qty)));
        master.setIdBillingPeriod(billingPeriod.getIdBillingPeriod());
        master.setIdBillingTemplate(template.getIdBillingTemplate());
        master.setBillingItems(new HashSet<BillingItem>());
        template.getMasterBillingItems().add(master);

        Set<BillingItem> newlyCreatedBillingItems = SaveBillingTemplate.createBillingItemsForMaster(sess, master, template);

        for (BillingItem billingItem : newlyCreatedBillingItems) {
        	if (billingStatus != null) {
        		billingItem.setCodeBillingStatus(billingStatus);
                if (!billingStatus.equals(BillingStatus.NEW) && !billingStatus.equals(BillingStatus.PENDING)) {
                	billingItem.setCompleteDate(new java.sql.Date(System.currentTimeMillis()));
                }
        	}
            if (notes != null) {
            	billingItem.setNotes(notes);
            }

            if (request.getIdRequest() != null) {
            	billingItem.setIdRequest(request.getIdRequest());
            } else if (request.getIdProductOrder() != null) {
            	billingItem.setIdProductOrder(request.getIdProductOrder());
            }
        }

        return newlyCreatedBillingItems;
	}

	protected Price getPriceForRange(PriceCategory priceCategory) {
		return getPriceForRange(priceCategory, qty);
	}

	protected Price getPriceForRange(PriceCategory priceCategory, int qty){

		// Find the price for capillary sequencing
		Price price = null;
		for(Iterator i1 = priceCategory.getPrices().iterator(); i1.hasNext();) {
			Price p = (Price)i1.next();
			if (p.getIsActive() != null && p.getIsActive().equals("Y")) {
				// Pricing for is tiered.  Look at filter 1 on the prices to find the one where the qty range applies.
				for(Iterator i2 = p.getPriceCriterias().iterator(); i2.hasNext();) {
					PriceCriteria criteria = (PriceCriteria)i2.next();

					Integer qty1 = null;
					Integer qty2 = null;

					if (criteria.getFilter1().contains("-")) {
						// Range check
						String[] tokens = criteria.getFilter1().split("-");
						if (tokens.length < 2) {
							continue;
						}

						qty1 = Integer.valueOf(tokens[0]);
						qty2 = Integer.valueOf(tokens[1]);

						// If the qty falls within the range, this is the price that applies
						if (qty >= qty1.intValue() && qty <= qty2.intValue()) {
							price = p;
							break;
						}
					} else if (criteria.getFilter1().contains("+")) {
						// Lower limit check
						String tokens[] =  criteria.getFilter1().split("\\+");

						qty1 = Integer.valueOf(tokens[0]);

						if (qty >= qty1.intValue()) {
							price = p;
							break;
						}
					} else {
						// Equals check
						qty1 = Integer.valueOf(criteria.getFilter1());

						if (qty == qty1.intValue()) {
							price = p;
							break;
						}
					}

				}
			}
		}
		return price;
	}

}
