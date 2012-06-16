package hci.gnomex.billing;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.BillingStatus;
import hci.gnomex.model.BioanalyzerChipType;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;


public class IlluminaExomeCapturePerCapPlugin implements BillingPlugin {
  // Capture is for up to six samples
  static final int SAMPLES_PER_CAPTURE = 6;

  public List constructBillingItems(Session sess, String amendState, BillingPeriod billingPeriod, PriceCategory priceCategory, Request request, 
      Set<Sample> samples, Set<LabeledSample> labeledSamples, Set<Hybridization> hybs, Set<SequenceLane> lanes, Map<String, ArrayList<Integer>> sampleToAssaysMap) {

    List billingItems = new ArrayList<BillingItem>();
    
    if (samples == null || samples.size() == 0) {
      return billingItems;
    }
    
    HashMap<String, Integer> sampleMap = new HashMap<String, Integer>();
    for(Iterator i = samples.iterator(); i.hasNext();) {
      Sample s = (Sample)i.next();
      
      // Only charge for lib prep if core is performing it.
      if (s.getSeqPrepByCore() != null && s.getSeqPrepByCore().equals("N")) {
        continue;
      } 
      
      String key = null;
      if (s.getMultiplexGroupNumber() == null || s.getMultiplexGroupNumber().equals("")) {
        key = s.getName();
      } else {
        key = s.getMultiplexGroupNumber().toString();
      }
      Integer sampleCount = sampleMap.get(key);
      if (sampleCount == null) {
        sampleCount = new Integer(0);
      }
      sampleCount = new Integer(sampleCount.intValue() + 1);
      sampleMap.put(key, sampleCount);
    }
    
    int qty = 0;
    for (Iterator i = sampleMap.keySet().iterator(); i.hasNext();) {
      String key = (String)i.next();
      Integer sampleCount = sampleMap.get(key);

      qty ++;
    }

    
    
    

    // Find the price
    Price price = null;
    for(Iterator i1 = priceCategory.getPrices().iterator(); i1.hasNext();) {
      Price p = (Price)i1.next();
      if (p.getIsActive() != null && p.getIsActive().equals("Y")) {
        for(Iterator i2 = p.getPriceCriterias().iterator(); i2.hasNext();) {
          PriceCriteria criteria = (PriceCriteria)i2.next();
          if (criteria.getFilter1().equals(request.getCodeApplication())) {          
            price = p;
            break;            
          }
        }
      }
    }

    // Instantiate a BillingItem for the matched price
    if (price != null) {
      BigDecimal theUnitPrice = price.getEffectiveUnitPrice(request.getLab());

      BillingItem billingItem = new BillingItem();
      billingItem.setCategory(priceCategory.getName());
      billingItem.setCodeBillingChargeKind(priceCategory.getCodeBillingChargeKind());
      billingItem.setIdBillingPeriod(billingPeriod.getIdBillingPeriod());
      billingItem.setDescription(price.getName());
      billingItem.setQty(Integer.valueOf(qty));
      billingItem.setUnitPrice(theUnitPrice);
      billingItem.setPercentagePrice(new BigDecimal(1));
      if (qty > 0 && theUnitPrice != null) {
        billingItem.setInvoicePrice(theUnitPrice.multiply(new BigDecimal(qty)));          
      }
      billingItem.setCodeBillingStatus(BillingStatus.PENDING);
      billingItem.setIdRequest(request.getIdRequest());
      billingItem.setIdBillingAccount(request.getIdBillingAccount());
      billingItem.setIdLab(request.getIdLab());
      billingItem.setIdPrice(price.getIdPrice());
      billingItem.setIdPriceCategory(price.getIdPriceCategory());
      billingItem.setSplitType(Constants.BILLING_SPLIT_TYPE_PERCENT_CODE);

      billingItems.add(billingItem);

    }
    
    
    return billingItems;
  }

  

}
