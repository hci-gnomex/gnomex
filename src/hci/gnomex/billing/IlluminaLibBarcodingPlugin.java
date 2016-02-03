package hci.gnomex.billing;

import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.BillingTemplate;
import hci.gnomex.model.Hybridization;
import hci.gnomex.model.LabeledSample;
import hci.gnomex.model.Price;
import hci.gnomex.model.PriceCategory;
import hci.gnomex.model.PropertyEntry;
import hci.gnomex.model.Request;
import hci.gnomex.model.Sample;
import hci.gnomex.model.SequenceLane;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;


public class IlluminaLibBarcodingPlugin extends BillingPlugin {

  public List<BillingItem> constructBillingItems(Session sess, String amendState, BillingPeriod billingPeriod, PriceCategory priceCategory, Request request, 
      Set<Sample> samples, Set<LabeledSample> labeledSamples, Set<Hybridization> hybs, Set<SequenceLane> lanes, Map<String, ArrayList<String>> sampleToAssaysMap, 
      String billingStatus, Set<PropertyEntry> propertyEntries, BillingTemplate billingTemplate) {

    List<BillingItem> billingItems = new ArrayList<BillingItem>();
    
    if (!this.hasValidData(sess, request, samples)) {
    	return billingItems;
    }

    
    // Generate the billing item.  Find the price using the
    // criteria of the illumina application.
    qty = 0;
    
    // Show the sample numbers in the notes
    String notes = "";
    for(Iterator i = samples.iterator(); i.hasNext();) {
      Sample s = (Sample)i.next();
      
      if (notes.length() > 0) {
        notes += ",";
      }
      notes += s.getNumber();
      
      // Only charge for lib prep if core is performing it and seq barcoding
      // occurs.
      if (s.getSeqPrepByCore() != null && s.getSeqPrepByCore().equals("Y")) {
        if (s.getIdOligoBarcode() != null) {
          qty++;
        }
      } 
      
    }
    
    // If we don't have any samples that were prepped by core
    // just bypass creating billing item.
    if (qty == 0) {
      return billingItems;
    }

    // Find the price.  (There is only one standard price for barcoding.)
    Price price = null;
    for(Iterator i1 = priceCategory.getPrices().iterator(); i1.hasNext();) {
      if (price.getIsActive() != null && price.getIsActive().equals("Y")) {
        price = (Price)i1.next();
        break;
      }
    }
    
    qty = this.checkQty(sess, request, samples, qty);

    // Instantiate a BillingItem for the matched price
    if (price != null) {
    	billingItems.addAll(this.makeBillingItems(request, price, priceCategory, qty, billingPeriod, billingStatus, sess, billingTemplate));
    }
    
    
    return billingItems;
  }

  

}
