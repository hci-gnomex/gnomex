package hci.gnomex.billing;

import hci.gnomex.model.BillingCategory;
import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.BillingPrice;
import hci.gnomex.model.BillingStatus;
import hci.gnomex.model.BioanalyzerChipType;
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


public class IlluminaLibSampleQualityPlugin implements BillingPlugin {

  public List createBillingItems(Session sess, BillingPeriod billingPeriod, BillingCategory billingCategory, List billingPrices, Request request) {
    List billingItems = new ArrayList();
    Map codeChipTypeMap = new HashMap();
    
    // Count up number of samples for each codeBioanalyzerChipType
    for(Iterator i = request.getSamples().iterator(); i.hasNext();) {
      Sample sample = (Sample)i.next();
      
      String codeChipType = sample.getSeqPrepQualCodeBioanalyzerChipType();
      
      // If we don't have a bioanalyzer chip type assigned yet on the sample,
      // use the default based on the sample type
      if ( codeChipType == null || codeChipType.equals("")) {
        DictionaryHelper dh = DictionaryHelper.getInstance(sess);
        codeChipType = BioanalyzerChipType.DNA1000;
      } 
      
      Integer sampleCount = (Integer)codeChipTypeMap.get(codeChipType);
      if (sampleCount == null) {
        sampleCount = new Integer(0);
      }
      sampleCount = new Integer(sampleCount.intValue() + 1);
      codeChipTypeMap.put(codeChipType, sampleCount);
    }
    
    
    // Now generate a billing item for each bioanalyzer chip type
    for(Iterator i = codeChipTypeMap.keySet().iterator(); i.hasNext();) {
      String  codeBioanalyzerChipType = (String)i.next();
      Integer qty = (Integer)codeChipTypeMap.get(codeBioanalyzerChipType);
      
      // Find the billing price for the bioanalyzer chip type
      BillingPrice billingPrice = null;
      for(Iterator i1 = billingPrices.iterator(); i1.hasNext();) {
        BillingPrice bp = (BillingPrice)i1.next();
        if (bp.getFilter1().equals(codeBioanalyzerChipType)) {
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
        billingItem.setIdLab(request.getIdLab());
        billingItem.setIdBillingPrice(billingPrice.getIdBillingPrice());
        billingItem.setIdBillingCategory(billingPrice.getIdBillingCategory());
        
        
        billingItems.add(billingItem);
        
      }
    }
    
    
    return billingItems;
  }
  

}
