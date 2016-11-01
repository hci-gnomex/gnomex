package hci.gnomex.billing;

import hci.gnomex.model.Application;
import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.BillingTemplate;
import hci.gnomex.model.Hybridization;
import hci.gnomex.model.LabeledSample;
import hci.gnomex.model.Price;
import hci.gnomex.model.PriceCategory;
import hci.gnomex.model.PriceCriteria;
import hci.gnomex.model.PropertyEntry;
import hci.gnomex.model.Request;
import hci.gnomex.model.Sample;
import hci.gnomex.model.SeqLibProtocol;
import hci.gnomex.model.SequenceLane;
import hci.gnomex.utility.DictionaryHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;


public class IlluminaLibPrepPlugin extends BillingPlugin {

  public List<BillingItem> constructBillingItems(Session sess, String amendState, BillingPeriod billingPeriod, PriceCategory priceCategory, Request request, 
      Set<Sample> samples, Set<LabeledSample> labeledSamples, Set<Hybridization> hybs, Set<SequenceLane> lanes, Map<String, ArrayList<String>> sampleToAssaysMap, 
      String billingStatus, Set<PropertyEntry> propertyEntries, BillingTemplate billingTemplate) {

    List<BillingItem> billingItems = new ArrayList<BillingItem>();
    
    if (!this.hasValidData(sess, request, samples)) {
    	return billingItems;
    }
    
    Map<Integer, String> protocolToAppMap = buildProtocolToAppMap(sess);
    Map<String, Integer> appQtyMap = new HashMap<String, Integer>();
    for(Iterator i = samples.iterator(); i.hasNext();) {
      Sample s = (Sample)i.next();
      
      // Only charge for lib prep if core is performing it.
      if (s.getSeqPrepByCore() != null && s.getSeqPrepByCore().equals("Y")) {
        String appCode = protocolToAppMap.get(s.getIdSeqLibProtocol());
        Integer qty = 0;
        if (!appQtyMap.containsKey(appCode)) {
          appQtyMap.put(appCode, 0);
        } else {
          qty = appQtyMap.get(appCode);
        }
        qty++;
        appQtyMap.put(appCode, qty);
      } 
      
    }

    for(String appCode : appQtyMap.keySet()) {
      // Find the price
      Price price = null;
      for(Iterator i1 = priceCategory.getPrices().iterator(); i1.hasNext();) {
        Price p = (Price)i1.next();
        if (p.getIsActive() != null && p.getIsActive().equals("Y")) {
          for(Iterator i2 = p.getPriceCriterias().iterator(); i2.hasNext();) {
            PriceCriteria criteria = (PriceCriteria)i2.next();
            if (criteria.getFilter1().equals(appCode)) {          
              price = p;
              break;            
            }
          }
        }
      }

      // Instantiate a BillingItem for the matched price
      if (price != null) {
        Integer qty = appQtyMap.get(appCode);
        qty = this.checkQty(sess, request, samples, qty.intValue());
  
        billingItems.addAll(this.makeBillingItems(request, price, priceCategory, qty.intValue(), billingPeriod, billingStatus, sess, billingTemplate));
      }
    }    
    
    return billingItems;
  }
  
  private Map<Integer, String> buildProtocolToAppMap(Session sess) {
    List applicationXSeqLibProtocols = sess.createQuery("SELECT prot, app from SeqLibProtocolApplication pa join pa.seqLibProtocol prot join pa.application app where prot.isActive='Y' and app.onlyForLabPrepped='N' and app.isActive = 'Y'").list();
    DictionaryHelper dh = DictionaryHelper.getInstance(sess);
    Map<Integer, String> protocolToAppMap = new HashMap<Integer, String>();
    for(Iterator i = applicationXSeqLibProtocols.iterator(); i.hasNext();) {
      Object[] objs = (Object[])i.next();
      SeqLibProtocol prot = (SeqLibProtocol)objs[0];
      Application app = (Application)objs[1];
      protocolToAppMap.put(prot.getIdSeqLibProtocol(), app.getCodeApplication());
    }
    
    return protocolToAppMap;
  }

}
