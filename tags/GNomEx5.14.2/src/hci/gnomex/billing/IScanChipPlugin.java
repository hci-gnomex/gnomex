package hci.gnomex.billing;

import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.BillingStatus;
import hci.gnomex.model.Hybridization;
import hci.gnomex.model.IScanChip;
import hci.gnomex.model.LabeledSample;
import hci.gnomex.model.Plate;
import hci.gnomex.model.PlateType;
import hci.gnomex.model.PlateWell;
import hci.gnomex.model.Price;
import hci.gnomex.model.PriceCategory;
import hci.gnomex.model.PriceCriteria;
import hci.gnomex.model.Request;
import hci.gnomex.model.Sample;
import hci.gnomex.model.SequenceLane;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;


public class IScanChipPlugin implements BillingPlugin {

  public List constructBillingItems(Session sess, String amendState, BillingPeriod billingPeriod, PriceCategory priceCategory, Request request, 
      Set<Sample> samples, Set<LabeledSample> labeledSamples, Set<Hybridization> hybs, Set<SequenceLane> lanes, Map<String, ArrayList<String>> sampleToAssaysMap) {
    
    
    List billingItems = new ArrayList<BillingItem>();
    
    if (samples == null || samples.size() == 0) {
      return billingItems;
    }
    
    
    // Get iScan Chip
    int idChip = request.getIdIScanChip();
    IScanChip chip = (IScanChip) sess.get( IScanChip.class, idChip );
    
    if ( chip == null || chip.getSamplesPerChip() == null ) {
      return billingItems;
    }
        
    // Find the price for iScanChip - there is only one
    Price price = null;
    for(Iterator i1 = priceCategory.getPrices().iterator(); i1.hasNext();) {
      Price p = (Price)i1.next();
      if (p.getIsActive() != null && p.getIsActive().equals("Y")) {
        price = p;
        break;
      }
    }
    
    
    // Price is per sample but you have to pay for the possible samples per chip times the number of chips
    int qty = request.getNumberIScanChips() * chip.getSamplesPerChip();

    // Instantiate a BillingItem for the matched billing price
    if (price != null) {
      // Get the price from the chip object
      BigDecimal theUnitPrice = chip.getCostPerSample(); 
      
      BillingItem billingItem = new BillingItem();
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
      billingItem.setIdLab(request.getIdLab());
      billingItem.setIdBillingAccount(request.getIdBillingAccount());        
      billingItem.setIdPrice(price.getIdPrice());
      billingItem.setIdPriceCategory(priceCategory.getIdPriceCategory());
      billingItem.setCategory(priceCategory.getName());
      billingItem.setIdCoreFacility(request.getIdCoreFacility());
    
      
      billingItems.add(billingItem);
      
    }
    
    
    return billingItems;
  }
  
}