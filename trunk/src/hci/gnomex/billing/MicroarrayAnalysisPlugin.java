package hci.gnomex.billing;

import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.BillingStatus;
import hci.gnomex.model.Hybridization;
import hci.gnomex.model.LabeledSample;
import hci.gnomex.model.Price;
import hci.gnomex.model.PriceCategory;
import hci.gnomex.model.PriceCriteria;
import hci.gnomex.model.Request;
import hci.gnomex.model.Sample;
import hci.gnomex.model.SequenceLane;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;


public class MicroarrayAnalysisPlugin implements BillingPlugin {
  public List constructBillingItems(Session sess, String amendState, BillingPeriod billingPeriod, PriceCategory priceCategory, Request request, 
      Set<Sample> samples, Set<LabeledSample> labeledSamples, Set<Hybridization> hybs, Set<SequenceLane> lanes) {

    List billingItems = new ArrayList<BillingItem>();
    
    if (hybs == null || hybs.size() == 0) {
      return billingItems;
    }
    
    
    // Total number of hybs
    int qty = hybs.size();
    
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
      BigDecimal theUnitPrice = price.getUnitPrice();
      if (request.getLab() != null && request.getLab().getIsExternalPricing() != null && request.getLab().getIsExternalPricing().equalsIgnoreCase("Y")) {
        theUnitPrice = price.getUnitPriceExternal();
      }
      
      BillingItem billingItem = new BillingItem();
      billingItem.setCategory(priceCategory.getName());
      billingItem.setCodeBillingChargeKind(priceCategory.getCodeBillingChargeKind());
      billingItem.setIdBillingPeriod(billingPeriod.getIdBillingPeriod());
      billingItem.setDescription(price.getName());
      billingItem.setQty(new Integer(qty));
      billingItem.setUnitPrice(theUnitPrice);
      billingItem.setPercentagePrice(new BigDecimal(1));
      if (qty > 0 && theUnitPrice != null) {      
        billingItem.setTotalPrice(theUnitPrice.multiply(new BigDecimal(qty)));
      }
      billingItem.setCodeBillingStatus(BillingStatus.PENDING);
      billingItem.setIdRequest(request.getIdRequest());
      billingItem.setIdLab(request.getIdLab());
      billingItem.setIdBillingAccount(request.getIdBillingAccount());      
      billingItem.setIdPrice(price.getIdPrice());
      billingItem.setIdPriceCategory(priceCategory.getIdPriceCategory());

      // Hold off on saving the notes.  Need to reserve note field
      // for complete date, etc at this time.
      //billingItem.setNotes(notes);

      

      billingItems.add(billingItem);

    }
    
    
    return billingItems;
  }



}
