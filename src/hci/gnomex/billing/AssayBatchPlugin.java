package hci.gnomex.billing;

import hci.gnomex.model.BioanalyzerChipType;
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
import hci.gnomex.utility.Order;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;


// Note that Assays are actually stored in the BioanalyzerChipType object
public class AssayBatchPlugin extends BillingPlugin {

  public List<BillingItem> constructBillingItems(Session sess, String amendState, BillingPeriod billingPeriod, PriceCategory priceCategory, Request request, 
      Set<Sample> samples, Set<LabeledSample> labeledSamples, Set<Hybridization> hybs, Set<SequenceLane> lanes, Map<String, ArrayList<String>> sampleToAssaysMap, 
      String billingStatus, Set<PropertyEntry> propertyEntries) {

    List<BillingItem> billingItems = new ArrayList<BillingItem>();
    
    if (!this.hasValidData(sess, request, samples)) {
    	return billingItems;
    }
    
    // Generate the billing item.  Find the price using the
    // criteria of the application.
    qty = this.getQty(sess, request, samples);

    // Find the price - there is only one
    Price price = null;
    for(Iterator i1 = priceCategory.getPrices().iterator(); i1.hasNext() && price == null;) {
      Price p = (Price)i1.next();
      if (p.getIsActive() != null && p.getIsActive().equals("Y")) {
        // If the price an application criteria, find the appropriate one.
        if ( p.getPriceCriterias()!=null && p.getPriceCriterias().size()>0 ) {
          for(Iterator i2 = p.getPriceCriterias().iterator(); i2.hasNext();) {
            PriceCriteria criteria = (PriceCriteria)i2.next();
            if (criteria.getFilter1().equals(request.getCodeApplication())) {
              if ((request.getCodeBioanalyzerChipType() == null && (criteria.getFilter2() == null || criteria.getFilter2().length() == 0))
                  || (request.getCodeBioanalyzerChipType() != null && criteria.getFilter2() != null && criteria.getFilter2().length() > 0 
                      && criteria.getFilter2().equals(request.getCodeBioanalyzerChipType()))) {          
                price = p;
                break;        
              }
            }
          }
        } else {
          price = p;
          break;
        }
      }
    }
    
    // Instantiate a BillingItem for the matched price
    if (price != null) {
    	billingItems.addAll(this.makeBillingItems(request, price, priceCategory, qty, billingPeriod, billingStatus));
    }
    
    
    return billingItems;
  }

  @Override
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
		
		BioanalyzerChipType assay = null;
	    if (request.getCodeBioanalyzerChipType() != null) {
	      assay = (BioanalyzerChipType) sess.get( BioanalyzerChipType.class, request.getCodeBioanalyzerChipType() );
	    }
	    int batch = 0;
	    if ( assay != null) {
	      batch = assay.getSampleWellsPerChip()!=null ? assay.getSampleWellsPerChip():0;
	    }
	    return doBatching(batch, samples.size());
	}

}
