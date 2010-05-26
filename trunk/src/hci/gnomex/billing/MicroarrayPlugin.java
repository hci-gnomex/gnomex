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
import hci.gnomex.utility.DictionaryHelper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;


public class MicroarrayPlugin implements BillingPlugin {
  public List constructBillingItems(Session sess, String amendState, BillingPeriod billingPeriod, PriceCategory priceCategory, Request request, 
      Set<Sample> samples, Set<LabeledSample> labeledSamples, Set<Hybridization> hybs, Set<SequenceLane> lanes) {
    
    List billingItems = new ArrayList<BillingItem>();
    
    if (hybs == null || hybs.size() == 0) {
      return billingItems;
    }
    
    
    
    // Total number arrays
    int qty = hybs.size();
    // If this is a multi-array slide, qty must be in same interval
    if (request.getSlideProduct() != null &&
        request.getSlideProduct().getArraysPerSlide() != null && request.getSlideProduct().getArraysPerSlide().intValue() > 1) {
      if (qty < request.getSlideProduct().getArraysPerSlide().intValue()) {
        qty = request.getSlideProduct().getArraysPerSlide().intValue();
      } else if (qty > request.getSlideProduct().getArraysPerSlide().intValue()) {
        int mod = qty %  request.getSlideProduct().getArraysPerSlide().intValue();
        if (mod > 0) {
          int diff = request.getSlideProduct().getArraysPerSlide().intValue() - mod;
          qty += diff;                  
        } 
      } 
    }

    
    // Now find the price
    Price price = null;
    if (request.getSlideProduct() != null && request.getSlideProduct().getIdBillingSlideProductClass() != null) {
      for(Iterator i1 = priceCategory.getPrices().iterator(); i1.hasNext();) {
        Price p = (Price)i1.next();
        for(Iterator i2 = p.getPriceCriterias().iterator(); i2.hasNext();) {
          PriceCriteria criteria = (PriceCriteria)i2.next();
          if (criteria.getFilter1().equals(request.getSlideProduct().getIdBillingSlideProductClass().toString())) {
            price = p;
            break;          
          }
          
        }

      }      
    }

    // Instantiate a BillingItem for the matched price
    if (price != null) {
      BigDecimal theUnitPrice = price.getUnitPrice();
      if (request.getLab() != null && request.getLab().getIsExternal() != null && request.getLab().getIsExternal().equalsIgnoreCase("Y")) {
        theUnitPrice = price.getUnitPriceExternal();
      }

      DictionaryHelper dh = DictionaryHelper.getInstance(sess);
      String slideCategoryName = dh.getOrganism(request.getSlideProduct().getIdOrganism());
      slideCategoryName += " " + dh.getApplication(request.getCodeApplication());
      
      BillingItem billingItem = new BillingItem();
      billingItem.setCategory(price.getName());
      billingItem.setDescription(slideCategoryName);
      billingItem.setCodeBillingChargeKind(priceCategory.getCodeBillingChargeKind());
      billingItem.setIdBillingPeriod(billingPeriod.getIdBillingPeriod());
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
      billingItem.setIdPriceCategory(price.getIdPriceCategory());
      

      billingItems.add(billingItem);

    }
    
    
    return billingItems;
 }


}
