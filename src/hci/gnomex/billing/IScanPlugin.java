package hci.gnomex.billing;

import hci.gnomex.model.*;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class IScanPlugin extends BillingPlugin {

  public List<BillingItem> constructBillingItems(Session sess, String amendState, BillingPeriod billingPeriod, PriceCategory priceCategory, Request request, 
      Set<Sample> samples, Set<LabeledSample> labeledSamples, Set<Hybridization> hybs, Set<SequenceLane> lanes, Map<String, ArrayList<String>> sampleToAssaysMap, 
      String billingStatus, Set<PropertyEntry> propertyEntries, BillingTemplate billingTemplate) {
    
    
    List<BillingItem> billingItems = new ArrayList<BillingItem>();
    
    if (!this.hasValidData(sess, request, samples)) {
    	return billingItems;
    }

    qty = this.getQty(sess, request, samples);

    int productBatch = 0;
    Product product = sess.load(Product.class, request.getIdProduct());
    if (product != null && product.getUseQty() != null) {
      productBatch = product.getUseQty();
    }

    // Find the price using the product batching # as the qty check
    Price price = getPriceForRange(priceCategory, productBatch);


    // Instantiate a BillingItem for the matched billing price
    if (price != null) {
    	billingItems.addAll(this.makeBillingItems(request, price, priceCategory, qty, billingPeriod, billingStatus, sess, billingTemplate));
    }
    
    
    return billingItems;
  }
  
}