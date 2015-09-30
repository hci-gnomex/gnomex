package hci.gnomex.billing;

import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.Hybridization;
import hci.gnomex.model.LabeledSample;
import hci.gnomex.model.Price;
import hci.gnomex.model.PriceCategory;
import hci.gnomex.model.PriceCriteria;
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


public class MicroarrayAnalysisPlugin extends BillingPlugin {
  public List<BillingItem> constructBillingItems(Session sess, String amendState, BillingPeriod billingPeriod, PriceCategory priceCategory, Request request, 
      Set<Sample> samples, Set<LabeledSample> labeledSamples, Set<Hybridization> hybs, Set<SequenceLane> lanes, Map<String, ArrayList<String>> sampleToAssaysMap, 
      String billingStatus, Set<PropertyEntry> propertyEntries) {

    List<BillingItem> billingItems = new ArrayList<BillingItem>();
    
    if (hybs == null || hybs.size() == 0) {
      return billingItems;
    }
    
    
    // Total number of hybs
    qty = hybs.size();
    
    // Show the hyb numbers in the billing note
    String notes = "";
    for(Iterator i = hybs.iterator(); i.hasNext();) {
        Hybridization hyb = (Hybridization)i.next();
        if (notes.length() > 0) {
          notes += ",";
        }
        notes += hyb.getNumber();
    }

    
    // Now find the price
    Price price = null;
    if (request.getSlideProduct() != null && request.getSlideProduct().getIdBillingSlideServiceClass() != null) {
      for(Iterator i1 = priceCategory.getPrices().iterator(); i1.hasNext();) {
        Price p = (Price)i1.next();
        if (p.getIsActive() != null && p.getIsActive().equals("Y")) {
          for(Iterator i2 = p.getPriceCriterias().iterator(); i2.hasNext();) {
            PriceCriteria criteria = (PriceCriteria)i2.next();
            if (criteria.getFilter1().equals(request.getSlideProduct().getIdBillingSlideServiceClass().toString())) {
              price = p;
              break;          
            }
          }
        }

      }      
    }

    // Instantiate a BillingItem for the matched price
    if (price != null) {
    	billingItems.addAll(this.makeBillingItems(request, price, priceCategory, qty, billingPeriod, billingStatus));
    }
    
    
    return billingItems;
  }



}
