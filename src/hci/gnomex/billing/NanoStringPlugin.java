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
import hci.gnomex.model.Sample;
import hci.gnomex.model.SequenceLane;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;


public class NanoStringPlugin implements BillingPlugin {

  public List constructBillingItems(Session sess, String amendState, BillingPeriod billingPeriod, PriceCategory priceCategory, Request request, 
      Set<Sample> samples, Set<LabeledSample> labeledSamples, Set<Hybridization> hybs, Set<SequenceLane> lanes, Map<String, ArrayList<String>> sampleToAssaysMap) {

    List billingItems = new ArrayList<BillingItem>();
    
    if (samples == null || samples.size() == 0) {
      return billingItems;
    }

    
    // Generate the billing item.  Find the price using the
    // criteria of the application.
    int numSamples = samples.size();
    int qty = 0;

    // Find the price - there is only one
    Price price = null;
    for(Iterator i1 = priceCategory.getPrices().iterator(); i1.hasNext();) {
      Price p = (Price)i1.next();
      if (p.getIsActive() != null && p.getIsActive().equals("Y")) {       
        price = p;
        break;            
      }
    }
    
    Application application = (Application) sess.get( Application.class, request.getCodeApplication() );
    if ( application == null) {
      return billingItems;
    }
    
    int batch = application.getSamplesPerBatch()!=null ? application.getSamplesPerBatch():0;

    if ( batch == 0 ) {
      qty = numSamples; 
    } else {
      if ( batch >= numSamples ) {
        qty = batch;
      } else {
        qty = (numSamples + (batch-1))/batch * batch;
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
      billingItem.setQty(qty);
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
      billingItem.setIdCoreFacility(request.getIdCoreFacility());

      // Hold off on saving the notes.  Need to reserve note field
      // for complete date, etc at this time.
      //billingItem.setNotes(notes);


      billingItems.add(billingItem);

    }
    
    
    return billingItems;
  }

  

}
