package hci.gnomex.billing;

import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.BillingStatus;
import hci.gnomex.model.Price;
import hci.gnomex.model.PriceCategory;
import hci.gnomex.model.PriceCriteria;
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


public class IlluminaLibPrepPlugin implements BillingPlugin {
  public List constructBillingItems(Session sess, BillingPeriod billingPeriod, PriceCategory priceCategory, Request request) {
    List billingItems = new ArrayList();

    
    // Generate the billing item.  Find the price using the
    // criteria of the illumina application.

    Integer qty = request.getSamples().size();

    // Find the price
    Price price = null;
    for(Iterator i1 = priceCategory.getPrices().iterator(); i1.hasNext();) {
      Price p = (Price)i1.next();
      for(Iterator i2 = p.getPriceCriterias().iterator(); i2.hasNext();) {
        PriceCriteria criteria = (PriceCriteria)i2.next();
        if (criteria.getFilter1().equals(request.getCodeApplication())) {          
          price = p;
          break;            
        }
      }
    }

    // Instantiate a BillingItem for the matched price
    if (price != null) {
      BigDecimal theUnitPrice = price.getUnitPrice();
      if (request.getLab() != null && request.getLab().getIsExternal() != null && request.getLab().getIsExternal().equalsIgnoreCase("Y")) {
        theUnitPrice = price.getUnitPriceExternal();
      }

      BillingItem billingItem = new BillingItem();
      billingItem.setCategory(priceCategory.getName());
      billingItem.setCodeBillingChargeKind(priceCategory.getCodeBillingChargeKind());
      billingItem.setIdBillingPeriod(billingPeriod.getIdBillingPeriod());
      billingItem.setDescription(price.getName());
      billingItem.setQty(qty);
      billingItem.setUnitPrice(theUnitPrice);
      billingItem.setPercentagePrice(new BigDecimal(1));
      if (qty.intValue() > 0 && theUnitPrice != null) {
        billingItem.setTotalPrice(theUnitPrice.multiply(new BigDecimal(qty.intValue())));          
      }
      billingItem.setCodeBillingStatus(BillingStatus.PENDING);
      billingItem.setIdRequest(request.getIdRequest());
      billingItem.setIdBillingAccount(request.getIdBillingAccount());
      billingItem.setIdLab(request.getIdLab());
      billingItem.setIdPrice(price.getIdPrice());
      billingItem.setIdPriceCategory(price.getIdPriceCategory());


      billingItems.add(billingItem);

    }
    
    
    return billingItems;
  }

  

}
