package hci.gnomex.billing;

import hci.gnomex.model.BillingCategory;
import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.BillingPrice;
import hci.gnomex.model.BillingStatus;
import hci.gnomex.model.LabeledSample;
import hci.gnomex.model.Request;
import hci.gnomex.model.Sample;
import hci.gnomex.utility.DictionaryHelper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;


public class MicroarrayPlugin implements BillingPlugin {

  public List createBillingItems(Session sess, BillingPeriod billingPeriod, BillingCategory billingCategory, List billingPrices, Request request) {
    List billingItems = new ArrayList();
    
    
    
    // Total number arrays
    int qty = request.getHybridizations().size();
    // If this is a multi-array slide, qty must be in same interval
    if (request.getSlideProduct() != null &&
        request.getSlideProduct().getArraysPerSlide() != null && request.getSlideProduct().getArraysPerSlide().intValue() > 1) {
      if (qty < request.getSlideProduct().getArraysPerSlide().intValue()) {
        qty = request.getSlideProduct().getArraysPerSlide().intValue();
      } else if (qty > request.getSlideProduct().getArraysPerSlide().intValue()) {
        int mod = qty %  request.getSlideProduct().getArraysPerSlide().intValue();
        int diff = request.getSlideProduct().getArraysPerSlide().intValue() - mod;
        qty += diff;        
      } 
    }

    
    // Now find the billing price
    BillingPrice billingPrice = null;
    if (request.getSlideProduct() != null && request.getSlideProduct().getIdBillingSlideProductClass() != null) {
      for(Iterator i1 = billingPrices.iterator(); i1.hasNext();) {
        BillingPrice bp = (BillingPrice)i1.next();
        if (bp.getFilter1().equals(request.getSlideProduct().getIdBillingSlideProductClass().toString())) {
          billingPrice = bp;
          break;          
        }

      }      
    }

    // Instantiate a BillingItem for the matched billing price
    if (billingPrice != null) {
      DictionaryHelper dh = DictionaryHelper.getInstance(sess);
      String slideCategoryName = dh.getOrganism(request.getSlideProduct().getIdOrganism());
      slideCategoryName += " " + dh.getMicroarrayCategory(request.getCodeMicroarrayCategory());
      
      BillingItem billingItem = new BillingItem();
      billingItem.setCategory(billingPrice.getDescription());
      billingItem.setDescription(slideCategoryName);
      billingItem.setCodeBillingChargeKind(billingCategory.getCodeBillingChargeKind());
      billingItem.setIdBillingPeriod(billingPeriod.getIdBillingPeriod());
      billingItem.setQty(new Integer(qty));
      billingItem.setUnitPrice(billingPrice.getUnitPrice());
      billingItem.setPercentagePrice(new BigDecimal(1));      
      if (qty > 0 && billingPrice.getUnitPrice() != null) {      
        billingItem.setTotalPrice(billingPrice.getUnitPrice().multiply(new BigDecimal(qty)));
      }
      billingItem.setCodeBillingStatus(BillingStatus.PENDING);
      billingItem.setIdRequest(request.getIdRequest());
      billingItem.setIdLab(request.getIdLab());
      billingItem.setIdBillingAccount(request.getIdBillingAccount());      
      billingItem.setIdBillingPrice(billingPrice.getIdBillingPrice());
      billingItem.setIdBillingCategory(billingPrice.getIdBillingCategory());
      

      billingItems.add(billingItem);

    }
    
    
    return billingItems;
  }
  

}
