package hci.gnomex.billing;

import hci.gnomex.model.BillingCategory;
import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.BillingPrice;
import hci.gnomex.model.BillingStatus;
import hci.gnomex.model.LabeledSample;
import hci.gnomex.model.Request;
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
    
    // Now find the billing price
    BillingPrice billingPrice = null;
    for(Iterator i1 = billingPrices.iterator(); i1.hasNext();) {
      BillingPrice bp = (BillingPrice)i1.next();
      if (bp.getFilter1().equals(request.getCodeRequestCategory())) {
        if (bp.getFilter2().equals(request.getCodeMicroarrayCategory())) {
          billingPrice = bp;
          break;          
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
