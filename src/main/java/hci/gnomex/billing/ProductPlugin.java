package hci.gnomex.billing;

import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.BillingStatus;
import hci.gnomex.model.BillingTemplate;
import hci.gnomex.model.Hybridization;
import hci.gnomex.model.LabeledSample;
import hci.gnomex.model.Price;
import hci.gnomex.model.PriceCategory;
import hci.gnomex.model.Product;
import hci.gnomex.model.ProductLineItem;
import hci.gnomex.model.ProductOrder;
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

public class ProductPlugin extends BillingPlugin {

  public List<BillingItem> constructBillingItems(Session sess, String amendState, BillingPeriod billingPeriod, PriceCategory priceCategory, Request request, Set<Sample> samples, Set<LabeledSample> labeledSamples, Set<Hybridization> hybs, Set<SequenceLane> lanes, Map<String, ArrayList<String>> sampleToAssaysMap, String billingStatus, Set<PropertyEntry> propertyEntries, BillingTemplate billingTemplate) {

    // This method not used for Products
    List<BillingItem> billingItems = new ArrayList<BillingItem>();

    return billingItems;
  }

  public List<BillingItem> constructBillingItems(Session sess, BillingPeriod billingPeriod, PriceCategory priceCategory, ProductOrder po, Set<ProductLineItem> productLineItems, BillingTemplate billingTemplate) {

    List<BillingItem> billingItems = new ArrayList<BillingItem>();

    // Get Product

    for (Iterator<ProductLineItem> i = productLineItems.iterator(); i.hasNext();) {
      ProductLineItem lineItem = (ProductLineItem) i.next();
      Product product = (Product) sess.load(Product.class, lineItem.getIdProduct());

      // If no product or if a product but we do not bill through gnomex then
      // return an empty list.
      if (product == null || (product.getBillThroughGnomex() != null && product.getBillThroughGnomex().equals("N"))) {
        continue;
      }

      // Find the price for product
      int idPrice = product.getIdPrice();
      Price price = (Price) sess.get(Price.class, idPrice);

      int qty = lineItem.getQty();

      // Instantiate a BillingItem for the matched billing price
      if (price != null) {
        // Get the price from the chip object
        BigDecimal theUnitPrice = lineItem.getUnitPrice();

        billingItems.addAll(this.makeBillingItems(po, price, priceCategory, qty, billingPeriod, BillingStatus.PENDING, null, null, theUnitPrice, lineItem.getIdProductOrder(), sess, billingTemplate));

      }
    }

    return billingItems;
  }

}