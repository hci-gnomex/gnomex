package hci.gnomex.billing;

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
import hci.gnomex.utility.DictionaryHelper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;


public class IlluminaSeqPlugin implements BillingPlugin {

  public List constructBillingItems(Session sess, String amendState, BillingPeriod billingPeriod, PriceCategory priceCategory, Request request, 
      Set<Sample> samples, Set<LabeledSample> labeledSamples, Set<Hybridization> hybs, Set<SequenceLane> lanes) {

    List billingItems = new ArrayList<BillingItem>();
    Map seqLaneMap = new HashMap();
    Map seqLaneNoteMap = new HashMap();
    DictionaryHelper dh = DictionaryHelper.getInstance(sess);
    
    if (lanes == null || lanes.size() == 0) {
      return billingItems;
    }
    
    // Count up number of sequence lanes for number seq cycles / seq run type
    for(Iterator i = lanes.iterator(); i.hasNext();) {
      SequenceLane seqLane = (SequenceLane)i.next();
      
      
      String key = seqLane.getIdNumberSequencingCycles() + "-" + seqLane.getIdSeqRunType();
      
      // Keep track of the lanes for # cycles/seq run type
      List theLanes = (List)seqLaneMap.get(key);
      if (theLanes == null) {
        theLanes = new ArrayList();
        seqLaneMap.put(key, theLanes);
      }
      theLanes.add(seqLane);
      
      // Show the seq lane numbers in the notes for the billing item
      String notes = (String)seqLaneNoteMap.get(key);
      if (notes == null) {
        notes = "";
      }
      if (notes.length() > 0) {
        notes += ",";
      } 
      notes += seqLane.getNumber();
      seqLaneNoteMap.put(key, notes);
    }
    
    
    // Now generate a billing item for each seq lane number sequencing cycles  / seq run type
    for(Iterator i = seqLaneMap.keySet().iterator(); i.hasNext();) {
      String  key = (String)i.next();
      String[] tokens = key.split("-");
      String idNumberSequencingCycles = tokens[0];
      String idSeqRunType             = tokens[1];
      
      List theLanes = (List)seqLaneMap.get(key);
      
      Integer qty = getMultiplexedLaneQty(theLanes);
      String notes = (String)seqLaneNoteMap.get(key);
      
      // Find the billing price 
      Price price = null;
      for(Iterator i1 = priceCategory.getPrices().iterator(); i1.hasNext();) {
        Price p = (Price)i1.next();
        for(Iterator i2 = p.getPriceCriterias().iterator(); i2.hasNext();) {
          PriceCriteria criteria = (PriceCriteria)i2.next();
          if (criteria.getFilter1().equals(idSeqRunType)) {
            if (criteria.getFilter2().equals(idNumberSequencingCycles)) {
              price = p;
              break;            
            }
          }
          
        }
          
      }
      
      // Instantiate a BillingItem for the matched price
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
          billingItem.setTotalPrice(theUnitPrice.multiply(new BigDecimal(qty.intValue())));          
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
  
  private Integer getMultiplexedLaneQty(List seqLanes) {
    int maxQty = 0;
    HashMap seqTagMap = new HashMap();
    for(Iterator i = seqLanes.iterator(); i.hasNext();) {
      SequenceLane theLane = (SequenceLane)i.next();
      Integer idOligoBarcode = theLane.getSample().getIdOligoBarcode();
      if (idOligoBarcode == null) {
        idOligoBarcode = Integer.valueOf(-99);
      }
      List theLanes = (List)seqTagMap.get(idOligoBarcode);
      if (theLanes == null) {
        theLanes = new ArrayList();
        seqTagMap.put(idOligoBarcode, theLanes);
      }
      theLanes.add(theLane);
    }
    
    // To determine the number of lanes, we find the highest number
    // of sequence lanes with the same sequence tag (or no tag).  
    // For example, if there are 3 unique seq tag AAA, GGG, TTT
    // and there are 3 lanes with AAA, 3 lanes with GGG, and 4 lanes
    // with TTT, there are a total of 4 multiplexed lanes.  In the
    // case where there are no sequence tags, the qty will equal
    // the total number of lanes.
    for (Iterator i = seqTagMap.keySet().iterator(); i.hasNext();) {
      Integer idOligoBarcode = (Integer)i.next();
      List theLanes = (List)seqTagMap.get(idOligoBarcode);
      
      if (theLanes.size() > maxQty) {
        maxQty = theLanes.size();
      }
    }
    
    
    return Integer.valueOf(maxQty);
  }



}
