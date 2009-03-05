package hci.gnomex.billing;

import hci.gnomex.model.BillingCategory;
import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.BillingPrice;
import hci.gnomex.model.BillingStatus;
import hci.gnomex.model.LabeledSample;
import hci.gnomex.model.MicroarrayCategory;
import hci.gnomex.model.Request;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.model.Sample;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;


public class LabelingReactionPlugin implements BillingPlugin {

  public List createBillingItems(Session sess, BillingPeriod billingPeriod, BillingCategory billingCategory, List billingPrices, Request request) {
    List billingItems = new ArrayList();

    
    // Total number of labeling reactions
    int qty = 0;
    for(Iterator i = request.getLabeledSamples().iterator(); i.hasNext();) {
      LabeledSample ls = (LabeledSample)i.next();
      
      Integer numberReactions = ls.getNumberOfReactions();
      if (numberReactions == null || numberReactions.intValue() == 0) {
        numberReactions = new Integer(1);
      }
      
      qty += numberReactions.intValue();
    }
    
    Integer idBillingSlideServiceClass = request.getSlideProduct().getIdBillingSlideServiceClass();
    
    // Now find the billing price
    BillingPrice billingPrice = null;
    
    // Bypass labeling for HybMap microarray experiments
    if (request.getCodeMicroarrayCategory().equals(MicroarrayCategory.HYBMAP_MICROARRAY_CATEGORY)) {
      return billingItems;
    }
    
    // Lookup prices.  
    // For Affymetrix requests, look at the microarray service class to determine
    // labeling price.
    // For Agilent (and other) requests, look at microarray category to
    // determine lableing price.
    for(Iterator i1 = billingPrices.iterator(); i1.hasNext();) {
      BillingPrice bp = (BillingPrice)i1.next();
      if (request.getCodeRequestCategory().equals(RequestCategory.AFFYMETRIX_MICROARRAY_REQUEST_CATEGORY)) {
        if (bp.getFilter1() != null && bp.getFilter1().equals(RequestCategory.AFFYMETRIX_MICROARRAY_REQUEST_CATEGORY)) {
          
          if (idBillingSlideServiceClass != null) {
            if (bp.getFilter2().equals(idBillingSlideServiceClass.toString())) {
              billingPrice = bp;
              break;          
            }        
            
          }
        }        
      } else {
        if (bp.getFilter1() != null && bp.getFilter1().equals(request.getCodeRequestCategory())) {
          if (bp.getFilter2() != null && bp.getFilter2().equals(request.getCodeMicroarrayCategory())) {
            billingPrice = bp;
            break;          
          }        
        }
        
      }

    }

    // Instantiate a BillingItem for the matched billing price
    if (billingPrice != null) {
      BillingItem billingItem = new BillingItem();
      billingItem.setCategory(billingCategory.getDescription());
      billingItem.setCodeBillingChargeKind(billingCategory.getCodeBillingChargeKind());
      billingItem.setIdBillingPeriod(billingPeriod.getIdBillingPeriod());
      billingItem.setDescription(billingPrice.getDescription());
      billingItem.setQty(new Integer(qty));
      billingItem.setUnitPrice(billingPrice.getUnitPrice());
      billingItem.setPercentagePrice(new BigDecimal(1));      
      if (qty > 0 && billingPrice.getUnitPrice() != null) {      
        billingItem.setTotalPrice(billingPrice.getUnitPrice().multiply(new BigDecimal(qty)));
      }
      billingItem.setCodeBillingStatus(BillingStatus.PENDING);
      billingItem.setIdRequest(request.getIdRequest());
      billingItem.setIdBillingAccount(request.getIdBillingAccount());      
      billingItem.setIdBillingPrice(billingPrice.getIdBillingPrice());
      billingItem.setIdBillingCategory(billingPrice.getIdBillingCategory());
      

      billingItems.add(billingItem);

    }
    
    
    return billingItems;
  }
  

}
