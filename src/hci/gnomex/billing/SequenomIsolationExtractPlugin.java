package hci.gnomex.billing;

import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.BillingStatus;
import hci.gnomex.model.Hybridization;
import hci.gnomex.model.IsolationPrepType;
import hci.gnomex.model.LabeledSample;
import hci.gnomex.model.Price;
import hci.gnomex.model.PriceCategory;
import hci.gnomex.model.PriceCriteria;
import hci.gnomex.model.PropertyEntry;
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


public class SequenomIsolationExtractPlugin implements BillingPlugin {

  public List constructBillingItems(Session sess, String amendState, BillingPeriod billingPeriod, PriceCategory priceCategory, Request request, 
      Set<Sample> samples, Set<LabeledSample> labeledSamples, Set<Hybridization> hybs, Set<SequenceLane> lanes, Map<String, ArrayList<String>> sampleToAssaysMap, 
      String billingStatus, Set<PropertyEntry> propertyEntries) {


    List billingItems = new ArrayList<BillingItem>();

    if (samples == null || samples.size() == 0) {
      return billingItems;
    }

    Boolean isDNAExtraction = false;
    if(request.getCodeIsolationPrepType() != null && !request.getCodeIsolationPrepType().equals("")){
      IsolationPrepType isoPrep = (IsolationPrepType)sess.load(IsolationPrepType.class, request.getCodeIsolationPrepType());
      if(isoPrep.getType().equals(IsolationPrepType.TYPE_DNA)){
        isDNAExtraction = true;
      }
    }

    // If core does not need to extract DNA, do not charge for it.

    if(isDNAExtraction){
      if (request.getCoreToExtractDNA()==null || !request.getCoreToExtractDNA().equals("Y")) {
        return billingItems;
      }
    } else{
      if (request.getCodeIsolationPrepType()==null || request.getCodeIsolationPrepType().equals("")) {
        return billingItems;
      }
    }

    // Rate is determined by the number of samples
    int qty = samples.size();


    // Find the price
    Price price = null;
    for(Iterator i1 = priceCategory.getPrices().iterator(); i1.hasNext();) {
      Price p = (Price)i1.next();
      if (p.getIsActive() != null && p.getIsActive().equals("Y")) {
        for(Iterator i2 = p.getPriceCriterias().iterator(); i2.hasNext();) {
          PriceCriteria criteria = (PriceCriteria)i2.next();
          if(isDNAExtraction){
            if (criteria.getFilter1().equals(request.getCodeIsolationPrepType())) {          
              price = p;
              break;            
            }
          } else{
            if (criteria.getFilter1().equals(request.getCodeIsolationPrepType())) {          
              price = p;
              break;            
            }

          }
        }
      }
    }


    // Instantiate a BillingItem for the matched billing price
    if (price != null) {
      BigDecimal theUnitPrice = price.getEffectiveUnitPrice(request.getLab());

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
      billingItem.setCodeBillingStatus(billingStatus);
      if (!billingStatus.equals(BillingStatus.NEW) && !billingStatus.equals(BillingStatus.PENDING)) {
        billingItem.setCompleteDate(new java.sql.Date(System.currentTimeMillis()));
      }
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