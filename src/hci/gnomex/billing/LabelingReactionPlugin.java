package hci.gnomex.billing;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.Application;
import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.BillingStatus;
import hci.gnomex.model.Hybridization;
import hci.gnomex.model.LabeledSample;
import hci.gnomex.model.Price;
import hci.gnomex.model.PriceCategory;
import hci.gnomex.model.PriceCriteria;
import hci.gnomex.model.Request;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.model.Sample;
import hci.gnomex.model.SequenceLane;
import hci.gnomex.utility.DictionaryHelper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;


public class LabelingReactionPlugin implements BillingPlugin {
  public List constructBillingItems(Session sess, String amendState, BillingPeriod billingPeriod, PriceCategory priceCategory, Request request, 
      Set<Sample> samples, Set<LabeledSample> labeledSamples, Set<Hybridization> hybs, Set<SequenceLane> lanes, Map<String, ArrayList<Integer>> sampleToAssaysMap) {
    
    List billingItems = new ArrayList<BillingItem>();
    DictionaryHelper dh = DictionaryHelper.getInstance(sess);
    
    if (labeledSamples == null || labeledSamples.size() == 0) {
      return billingItems;
    }

    
    // Total number of labeling reactions
    int qty = 0;
    for(Iterator i = labeledSamples.iterator(); i.hasNext();) {
      LabeledSample ls = (LabeledSample)i.next();
      
      Integer numberReactions = ls.getNumberOfReactions();
      if (numberReactions == null || numberReactions.intValue() == 0) {
        numberReactions = new Integer(1);
      }
      
      qty += numberReactions.intValue();
    }
    
    // Show the labeled sample numbers in the note
    String notes = "";
    for(Iterator i = labeledSamples.iterator(); i.hasNext();) {
      LabeledSample ls = (LabeledSample)i.next();
      
      if (notes.length() > 0) {
        notes += ",";
      }
      if (ls.getSample() != null && ls.getSample().getNumber() != null) {
        notes += dh.getLabel(ls.getIdLabel()) + "-" + ls.getSample().getNumber();        
      }
    }
    
    Integer idBillingSlideServiceClass = request.getSlideProduct().getIdBillingSlideServiceClass();
    
    // Now find the price
    Price price = null;
    
    // Bypass labeling for HybMap microarray experiments
    if (request.getCodeApplication().equals(Application.HYBMAP_MICROARRAY_CATEGORY)) {
      return billingItems;
    }
    
    // Lookup prices.  
    // For Affymetrix requests, look at the microarray service class to determine
    // labeling price.
    // For Agilent (and other) requests, look at microarray category to
    // determine labeling price.
    for(Iterator i1 = priceCategory.getPrices().iterator(); i1.hasNext();) {
      Price p = (Price)i1.next();
      if (p.getIsActive() != null && p.getIsActive().equals("Y")) {
        for(Iterator i2 = p.getPriceCriterias().iterator(); i2.hasNext();) {
          PriceCriteria criteria = (PriceCriteria)i2.next();          
          if (request.getCodeRequestCategory().equals(RequestCategory.AFFYMETRIX_MICROARRAY_REQUEST_CATEGORY)) {
              
            if (idBillingSlideServiceClass != null) {
              if (criteria.getFilter1().equals(idBillingSlideServiceClass.toString())) {
                price = p;
                break;          
              }        

            }
          } else {
            if (criteria.getFilter1() != null && criteria.getFilter1().equals(request.getCodeApplication())) {
                price = p;
                break;                 
            }
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
      billingItem.setQty(new Integer(qty));
      billingItem.setUnitPrice(theUnitPrice);
      billingItem.setPercentagePrice(new BigDecimal(1));      
      if (qty > 0 && theUnitPrice != null) {      
        billingItem.setInvoicePrice(theUnitPrice.multiply(new BigDecimal(qty)));
      }
      billingItem.setCodeBillingStatus(BillingStatus.PENDING);
      billingItem.setIdRequest(request.getIdRequest());
      billingItem.setIdLab(request.getIdLab());
      billingItem.setIdBillingAccount(request.getIdBillingAccount());      
      billingItem.setIdPrice(price.getIdPrice());
      billingItem.setIdPriceCategory(priceCategory.getIdPriceCategory());
      billingItem.setSplitType(Constants.BILLING_SPLIT_TYPE_PERCENT_CODE);

      // Hold off on saving the notes.  Need to reserve note field
      // for complete date, etc at this time.
      //billingItem.setNotes(notes);

     

      billingItems.add(billingItem);

    }
    
    
    return billingItems;  }



}
