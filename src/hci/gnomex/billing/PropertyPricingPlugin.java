package hci.gnomex.billing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;

import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.Hybridization;
import hci.gnomex.model.LabeledSample;
import hci.gnomex.model.Price;
import hci.gnomex.model.PriceCategory;
import hci.gnomex.model.PriceCriteria;
import hci.gnomex.model.Property;
import hci.gnomex.model.PropertyEntry;
import hci.gnomex.model.PropertyOption;
import hci.gnomex.model.Request;
import hci.gnomex.model.Sample;
import hci.gnomex.model.SequenceLane;


public class PropertyPricingPlugin extends BillingPlugin {

  public List<BillingItem> constructBillingItems(Session sess, String amendState, BillingPeriod billingPeriod, PriceCategory priceCategory, Request request,
      Set<Sample> samples, Set<LabeledSample> labeledSamples, Set<Hybridization> hybs, Set<SequenceLane> lanes, Map<String, ArrayList<String>> sampleToAssaysMap,
      String billingStatus, Set<PropertyEntry> propertyEntries) {

    List<BillingItem> billingItems = new ArrayList<BillingItem>();

    if (!this.hasValidData(sess, request, samples)) {
      return billingItems;
    }

    //qty = this.getQty(sess, request, samples);
    // TODO should we allow admins to set price by sample qty or just 1?
    qty = 1;


    // Find the price
    Price price = null;


    // If Price Category has no filter1 then it is for a checkbox property
    if (priceCategory.getDictionaryClassNameFilter1()==null ) {
      for(Iterator i1 = priceCategory.getPrices().iterator(); i1.hasNext();) {
        // There should only be one price
        Price p = (Price)i1.next();

        // See if there is a property entry for this property
        for (Iterator pi = propertyEntries.iterator(); pi.hasNext();) {
          PropertyEntry pe = (PropertyEntry)pi.next();
          Property prop = (Property) sess.load( Property.class, pe.getIdProperty() );

          if ( prop!=null && prop.getIdPriceCategory().equals(priceCategory.getIdPriceCategory()) ) {
            if ( pe.getValue().equalsIgnoreCase( "Y" )) {
              price = p;
              break;
            }
          }

        }
      }

    } else {
      // if Price Category has a filter1 then it is for property with options
      for(Iterator i1 = priceCategory.getPrices().iterator(); i1.hasNext();) {
        Price p = (Price)i1.next();
        if (p.getIsActive() != null && p.getIsActive().equals("Y")) {

          for(Iterator i2 = p.getPriceCriterias().iterator(); i2.hasNext();) {
            PriceCriteria criteria = (PriceCriteria)i2.next();

            // See if there is a property entry for this property
            for (Iterator pi = propertyEntries.iterator(); pi.hasNext();) {
              PropertyEntry pe = (PropertyEntry)pi.next();
              Property prop = (Property) sess.load( Property.class, pe.getIdProperty() );

              // this property entry is for a property that uses the price category
              if ( prop!=null && prop.getIdPriceCategory().equals(priceCategory.getIdPriceCategory()) && pe.getOptions()!=null ) {
                // check all property entry options for one that matches the criteria
                for (PropertyOption option : (Set<PropertyOption>) pe.getOptions()) {
                  if ( option.getIdPropertyOption().toString().equals( criteria.getFilter1() )) {
                    price = p;
                    break;
                  }
                }

              }

            }
          }
        }
      }
    }



    // Instantiate a BillingItem for the matched billing price
    if (price != null) {
      billingItems.addAll(this.makeBillingItems(request, price, priceCategory, qty, billingPeriod, billingStatus));
    }


    return billingItems;
  }

}