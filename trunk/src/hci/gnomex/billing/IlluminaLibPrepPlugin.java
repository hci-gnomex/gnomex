package hci.gnomex.billing;

import hci.gnomex.model.BillingCategory;
import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.BillingPrice;
import hci.gnomex.model.BillingStatus;
import hci.gnomex.model.BioanalyzerChipType;
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

import org.hibernate.Session;


public class IlluminaLibPrepPlugin implements BillingPlugin {

  public List createBillingItems(Session sess, BillingPeriod billingPeriod, BillingCategory billingCategory, List billingPrices, Request request) {
    List billingItems = new ArrayList();
    Map samplePrepMethodMap = new HashMap();
    DictionaryHelper dh = DictionaryHelper.getInstance(sess);
    
    // Count up number of samples for sample prep method / seq run type
    for(Iterator i = request.getSamples().iterator(); i.hasNext();) {
      Sample sample = (Sample)i.next();
      
      if (!sample.getSeqPrepByCore().equals("Y")) {
        continue;
      }
      
      // If the sample prep method isn't filled in yet, grab the default based on the 
      // sample type
      Integer idSamplePrepMethod = sample.getIdSamplePrepMethod();      
      if (idSamplePrepMethod == null) {
        idSamplePrepMethod = dh.getDefaultIdSamplePrepMethod(sample.getIdSampleType());        
      }
      if (idSamplePrepMethod == null) {
        continue;
      }

      
      
      Integer key = idSamplePrepMethod;
      
      Integer sampleCount = (Integer)samplePrepMethodMap.get(key);
      if (sampleCount == null) {
        sampleCount = new Integer(0);
      }
      sampleCount = new Integer(sampleCount.intValue() + 1);
      samplePrepMethodMap.put(key, sampleCount);
    }
    
    
    // Now generate a billing item for each sample prep method  
    for(Iterator i = samplePrepMethodMap.keySet().iterator(); i.hasNext();) {
      Integer  idSamplePrepMethod = (Integer)i.next();
      
      Integer qty = (Integer)samplePrepMethodMap.get(idSamplePrepMethod);
      
      // Find the billing price
      BillingPrice billingPrice = null;
      for(Iterator i1 = billingPrices.iterator(); i1.hasNext();) {
        BillingPrice bp = (BillingPrice)i1.next();
        if (bp.getFilter1().equals(idSamplePrepMethod.toString())) {          
          billingPrice = bp;
          break;            
        }
      }
      
      // Instantiate a BillingItem for the matched billing price
      if (billingPrice != null) {
        BillingItem billingItem = new BillingItem();
        billingItem.setCategory(billingCategory.getDescription());
        billingItem.setCodeBillingChargeKind(billingCategory.getCodeBillingChargeKind());
        billingItem.setIdBillingPeriod(billingPeriod.getIdBillingPeriod());
        billingItem.setDescription(billingPrice.getDescription());
        billingItem.setQty(qty);
        billingItem.setUnitPrice(billingPrice.getUnitPrice());
        billingItem.setPercentagePrice(new BigDecimal(1));
        if (qty.intValue() > 0 && billingPrice.getUnitPrice() != null) {
          billingItem.setTotalPrice(billingPrice.getUnitPrice().multiply(new BigDecimal(qty.intValue())));          
        }
        billingItem.setCodeBillingStatus(BillingStatus.PENDING);
        billingItem.setIdRequest(request.getIdRequest());
        billingItem.setIdBillingAccount(request.getIdBillingAccount());
        billingItem.setIdBillingPrice(billingPrice.getIdBillingPrice());
        billingItem.setIdBillingCategory(billingPrice.getIdBillingCategory());
        
        
        billingItems.add(billingItem);
        
      }
    }
    
    
    return billingItems;
  }
  

}
