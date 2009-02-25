package hci.gnomex.billing;

import hci.gnomex.model.BillingCategory;
import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.BillingPrice;
import hci.gnomex.model.BillingStatus;
import hci.gnomex.model.BioanalyzerChipType;
import hci.gnomex.model.Request;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.model.Sample;
import hci.gnomex.utility.DictionaryHelper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;


public class SampleQualityPlugin implements BillingPlugin {

  private static final String           PICO_GREEN = "PICOGREEN";
  private static final String           DNA_GEL    = "DNAGEL";
  
  public List createBillingItems(Session sess, BillingPeriod billingPeriod, BillingCategory billingCategory, List billingPrices, Request request) {
    List billingItems = new ArrayList();
    Map codeChipTypeMap = new HashMap();
    
    // Count up number of samples for each codeBioanalyzerChipType
    for(Iterator i = request.getSamples().iterator(); i.hasNext();) {
      Sample sample = (Sample)i.next();
      
      String filter1 = null;
      DictionaryHelper dh = DictionaryHelper.getInstance(sess);
      
      if (request.getCodeRequestCategory().equals(RequestCategory.SOLEXA_REQUEST_CATEGORY)) {
        
        
        if (!sample.getSeqPrepByCore().equals("Y")) {
          continue;
        }
        
        if (dh.getSampleType(sample).indexOf("chIP") >= 0) {
          filter1 = this.PICO_GREEN;
        } else if (dh.getSampleType(sample).indexOf("DNA") >= 0) {
          filter1 = this.DNA_GEL;
        } else if (dh.getSampleType(sample).indexOf("RNA") >= 0) {
          filter1 = BioanalyzerChipType.RNA_NANO;
        }
        
      } else {
        filter1 = sample.getCodeBioanalyzerChipType();
        // If we don't have a chip type assigned yet on the sample,
        // use the default based on the sample type
        if ( filter1 == null || filter1.equals("")) {
          if (dh.getSampleType(sample).indexOf("RNA") >= 1) {
            filter1 = BioanalyzerChipType.RNA_NANO;
          } else {
            filter1 = BioanalyzerChipType.DNA1000;
          }
        }
      }        
      
 
      
      Integer sampleCount = (Integer)codeChipTypeMap.get(filter1);
      if (sampleCount == null) {
        sampleCount = new Integer(0);
      }
      sampleCount = new Integer(sampleCount.intValue() + 1);
      codeChipTypeMap.put(filter1, sampleCount);
    }
    
    
    // Now generate a billing item for each bioanalyzer chip type
    for(Iterator i = codeChipTypeMap.keySet().iterator(); i.hasNext();) {
      String  filter1 = (String)i.next();
      Integer qty = (Integer)codeChipTypeMap.get(filter1);
      
      // Find the billing price for kind of sample quality
      BillingPrice billingPrice = null;
      for(Iterator i1 = billingPrices.iterator(); i1.hasNext();) {
        BillingPrice bp = (BillingPrice)i1.next();
        if (bp.getFilter1().equals(filter1)) {
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
