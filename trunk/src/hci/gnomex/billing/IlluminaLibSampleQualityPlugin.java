package hci.gnomex.billing;

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


public class IlluminaLibSampleQualityPlugin implements BillingPlugin {

  public List constructBillingItems(Session sess, String amendState, BillingPeriod billingPeriod, PriceCategory priceCategory, Request request, 
      Set<Sample> samples, Set<LabeledSample> labeledSamples, Set<Hybridization> hybs, Set<SequenceLane> lanes) {
    
    List billingItems = new ArrayList<BillingItem>();
    Map codeChipTypeMap = new HashMap();
    Map codeChipTypeNoteMap = new HashMap();
    
    if (samples == null || samples.size() == 0) {
      return billingItems;
    }
    
    
    // Count up number of samples for each codeBioanalyzerChipType
    for(Iterator i = samples.iterator(); i.hasNext();) {
      Sample sample = (Sample)i.next();

      // The price of QC is built into libary prep when core preps the library,
      // so bypass creating billing item when seqPrepCore=Y.
      if (sample.getSeqPrepByCore() != null && sample.getSeqPrepByCore().equals("Y")) {
        continue;
      }
      
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
      
      // Store the notes associated with this billing item
      // Show the sample numbers in the billing note
      String notes = (String)codeChipTypeNoteMap.get(codeChipType);
      if (notes == null) {
        notes = "";
      }
      if (notes.length() > 0) {
        notes += ",";
      }
      notes += sample.getNumber();
      codeChipTypeNoteMap.put(codeChipType, notes);
    }
    
    
    // Now generate a billing item for each bioanalyzer chip type
    for(Iterator i = codeChipTypeMap.keySet().iterator(); i.hasNext();) {
      String  codeBioanalyzerChipType = (String)i.next();
      Integer qty = (Integer)codeChipTypeMap.get(codeBioanalyzerChipType);
      String notes = (String)codeChipTypeNoteMap.get(codeBioanalyzerChipType);
      
      // Find the billing price for the bioanalyzer chip type
      Price price = null;
      for(Iterator i1 = priceCategory.getPrices().iterator(); i1.hasNext();) {
        Price p = (Price)i1.next();
        for(Iterator i2 = p.getPriceCriterias().iterator(); i2.hasNext();) {
          PriceCriteria criteria = (PriceCriteria)i2.next();
          if (criteria.getFilter2() != null && criteria.getFilter2().equals(codeBioanalyzerChipType)) {
            price = p;
            break;
          }
          
        }
          
      }
      
      // Instantiate a BillingItem for the matched billing price
      if (price != null) {
        BigDecimal theUnitPrice = price.getUnitPrice();
        if (request.getLab() != null && request.getLab().getIsExternalPricing() != null && request.getLab().getIsExternalPricing().equalsIgnoreCase("Y")) {
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
          billingItem.setInvoicePrice(theUnitPrice.multiply(new BigDecimal(qty.intValue())));
        }
        billingItem.setCodeBillingStatus(BillingStatus.PENDING);
        billingItem.setIdRequest(request.getIdRequest());
        billingItem.setIdBillingAccount(request.getIdBillingAccount());        
        billingItem.setIdLab(request.getIdLab());
        billingItem.setIdPrice(price.getIdPrice());
        billingItem.setIdPriceCategory(price.getIdPriceCategory());
        
        // Hold off on saving the notes.  Need to reserve note field
        // for complete date, etc at this time.
        //billingItem.setNotes(notes);

        
        billingItems.add(billingItem);
        
      }
    }
    
    
    return billingItems;
  }



}
