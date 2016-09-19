package hci.gnomex.billing;

import java.util.Set;

import org.hibernate.Session;

import hci.gnomex.model.Sample;
import hci.gnomex.utility.Order;


public class PropertyPricingNotBySamplePlugin extends PropertyPricingPlugin {


  protected int getQty(Session sess, Order request, Set<Sample> samples) {
    return 1;
  }


}