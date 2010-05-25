package hci.gnomex.billing;

import hci.gnomex.model.Application;
import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.BillingStatus;
import hci.gnomex.model.BioanalyzerChipType;
import hci.gnomex.model.Price;
import hci.gnomex.model.PriceCategory;
import hci.gnomex.model.PriceCriteria;
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

  public List constructBillingItems(Session sess, BillingPeriod billingPeriod, PriceCategory priceCategory, Request request) {
    List billingItems = new ArrayList<BillingItem>();
    Map codeChipTypeMap = new HashMap();
    
    // Count up number of samples for each codeBioanalyzerChipType
    for(Iterator i = request.getSamples().iterator(); i.hasNext();) {
      Sample sample = (Sample)i.next();
      
      String filter1 = Application.BIOANALYZER_QC;
      String filter2 = null;
      DictionaryHelper dh = DictionaryHelper.getInstance(sess);
      
      if (request.getCodeRequestCategory().equals(RequestCategory.SOLEXA_REQUEST_CATEGORY)) {
        if (!sample.getSeqPrepByCore().equals("Y")) {
          continue;
        }
        
        if (request.getCodeApplication().equals(Application.CHIP_SEQ_CATEGORY)) {
          filter1 = Application.QUBIT_PICOGREEN_QC;
        } else if (request.getCodeApplication().indexOf("DNA") >= 0) {
          filter1 = Application.DNA_GEL_QC;
        } else if (request.getCodeApplication().indexOf("RNA") >= 0) {
          filter2 = BioanalyzerChipType.RNA_NANO;
        } else  {
          filter1 = Application.DNA_GEL_QC;
        } 
        
      } else if ((request.getCodeRequestCategory().equals(RequestCategory.AGILIENT_MICROARRAY_REQUEST_CATEGORY) || request.getCodeRequestCategory().equals(RequestCategory.AGILIENT_1_COLOR_MICROARRAY_REQUEST_CATEGORY)) &&
                  request.getCodeApplication().equals(Application.CGH_MICROARRAY_CATEGORY)) {
          filter2 =  Application.DNA_GEL_QC;
      } else if ((request.getCodeRequestCategory().equals(RequestCategory.AGILIENT_MICROARRAY_REQUEST_CATEGORY) || request.getCodeRequestCategory().equals(RequestCategory.AGILIENT_1_COLOR_MICROARRAY_REQUEST_CATEGORY)) &&
                  request.getCodeApplication().equals(Application.CHIP_ON_CHIP_MICROARRAY_CATEGORY)) {
          filter2 = BioanalyzerChipType.DNA1000;
      } else if ((request.getCodeRequestCategory().equals(RequestCategory.AGILIENT_MICROARRAY_REQUEST_CATEGORY) || request.getCodeRequestCategory().equals(RequestCategory.AGILIENT_1_COLOR_MICROARRAY_REQUEST_CATEGORY)) &&
                  request.getCodeApplication().equals(Application.EXPRESSION_MICROARRAY_CATEGORY)) {
          filter2 = BioanalyzerChipType.RNA_NANO;
      } else if (request.getCodeRequestCategory().equals(RequestCategory.AFFYMETRIX_MICROARRAY_REQUEST_CATEGORY) &&
                  request.getCodeApplication().equals(Application.EXPRESSION_MICROARRAY_CATEGORY)) {
        filter2 = BioanalyzerChipType.RNA_NANO;
      } else if (request.getCodeRequestCategory().equals(RequestCategory.AFFYMETRIX_MICROARRAY_REQUEST_CATEGORY) &&
                  request.getCodeApplication().equals(Application.SNP_MICROARRAY_CATEGORY)) {
        filter1 = Application.DNA_GEL_QC;
      } else {
        filter2 = sample.getCodeBioanalyzerChipType();
        // If we don't have a chip type assigned yet on the sample,
        // use the default based on the sample type
        if ( filter1.equals(Application.BIOANALYZER_QC)) {
          if (filter2 == null || filter2.equals("")) {
            if (dh.getSampleType(sample).indexOf("RNA") >= 1) {
              filter2 = BioanalyzerChipType.RNA_NANO;
            } else {
              filter2 = BioanalyzerChipType.DNA1000;
            }
          }
        }
      }        
      
 
      String key = filter1 + "-" + filter2;
      Integer sampleCount = (Integer)codeChipTypeMap.get(key);
      if (sampleCount == null) {
        sampleCount = new Integer(0);
      }
      sampleCount = new Integer(sampleCount.intValue() + 1);
      codeChipTypeMap.put(key, sampleCount);
    
    }
    
    // Now generate a billing item for each bioanalyzer chip type
    for(Iterator i = codeChipTypeMap.keySet().iterator(); i.hasNext();) {
      String  key = (String)i.next();
      String[] tokens = key.split("-");
      String  filter1 = tokens[0];
      String  filter2 = tokens[1];
      
      Integer qty = (Integer)codeChipTypeMap.get(key);
      
      // Find the price for kind of sample quality
      Price price = null;
      for(Iterator i1 = priceCategory.getPrices().iterator(); i1.hasNext();) {
        Price p = (Price)i1.next();
        for(Iterator i2 = p.getPriceCriterias().iterator(); i2.hasNext();) {
          PriceCriteria criteria = (PriceCriteria)i2.next();
          if (criteria.getFilter1().equals(filter1)) {
            if (filter2.equals("null")) {
              if (criteria.getFilter2() == null || criteria.getFilter2().equals("")) {
                price = p;
                break;              
                
              }
            } else {
              if (criteria.getFilter2() != null && criteria.getFilter2().equals(filter2)) {
                price = p;
                break;
              }
            }
          } 
          
        }
          
      }
      
      // Instantiate a BillingItem for the matched billing price
      if (price != null) {
        BigDecimal theUnitPrice = price.getUnitPrice();
        if (request.getLab() != null && request.getLab().getIsExternal() != null && request.getLab().getIsExternal().equalsIgnoreCase("Y")) {
          theUnitPrice = price.getUnitPriceExternal();
        }
        
        BillingItem billingItem = new BillingItem();
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
        billingItem.setIdLab(request.getIdLab());
        billingItem.setIdBillingAccount(request.getIdBillingAccount());        
        billingItem.setIdPrice(price.getIdPrice());
        billingItem.setIdPriceCategory(priceCategory.getIdPriceCategory());
        billingItem.setCategory(priceCategory.getName());
        
        billingItems.add(billingItem);
        
      }
    }
    
    
    return billingItems;
  }
  
}