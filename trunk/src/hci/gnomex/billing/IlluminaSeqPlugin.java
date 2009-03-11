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


public class IlluminaSeqPlugin implements BillingPlugin {

  public List createBillingItems(Session sess, BillingPeriod billingPeriod, BillingCategory billingCategory, List billingPrices, Request request) {
    List billingItems = new ArrayList();
    Map seqLaneMap = new HashMap();
    DictionaryHelper dh = DictionaryHelper.getInstance(sess);
    
    // Count up number of sequence lanes for number seq cycles / seq run type
    for(Iterator i = request.getSequenceLanes().iterator(); i.hasNext();) {
      SequenceLane seqLane = (SequenceLane)i.next();
      
      
      String key = seqLane.getIdNumberSequencingCycles() + "-" + seqLane.getIdSeqRunType();
      
      Integer seqLaneCount = (Integer)seqLaneMap.get(key);
      if (seqLaneCount == null) {
        seqLaneCount = new Integer(0);
      }
      seqLaneCount = new Integer(seqLaneCount.intValue() + 1);
      seqLaneMap.put(key, seqLaneCount);
    }
    
    
    // Now generate a billing item for each seq lane number sequencing cycles  / seq run type
    for(Iterator i = seqLaneMap.keySet().iterator(); i.hasNext();) {
      String  key = (String)i.next();
      String[] tokens = key.split("-");
      String idNumberSequencingCycles = tokens[0];
      String idSeqRunType             = tokens[1];
      
      Integer qty = (Integer)seqLaneMap.get(key);
      
      // Find the billing price 
      BillingPrice billingPrice = null;
      for(Iterator i1 = billingPrices.iterator(); i1.hasNext();) {
        BillingPrice bp = (BillingPrice)i1.next();
        if (bp.getFilter1().equals(idNumberSequencingCycles)) {
          if (bp.getFilter2().equals(idSeqRunType)) {
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
        billingItem.setQty(qty);
        billingItem.setUnitPrice(billingPrice.getUnitPrice());
        billingItem.setPercentagePrice(new BigDecimal(1));        
        if (qty.intValue() > 0 && billingPrice.getUnitPrice() != null) {
          billingItem.setTotalPrice(billingPrice.getUnitPrice().multiply(new BigDecimal(qty.intValue())));          
        }
        billingItem.setCodeBillingStatus(BillingStatus.PENDING);
        billingItem.setIdRequest(request.getIdRequest());
        billingItem.setIdBillingAccount(request.getIdBillingAccount());
        billingItem.setIdLab(request.getIdLab());
        billingItem.setIdBillingPrice(billingPrice.getIdBillingPrice());
        billingItem.setIdBillingCategory(billingPrice.getIdBillingCategory());
        
        
        billingItems.add(billingItem);
        
      }
    }
    
    
    return billingItems;
  }
  

}
