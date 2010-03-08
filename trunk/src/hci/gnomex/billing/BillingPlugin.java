package hci.gnomex.billing;

import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.PriceCategory;
import hci.gnomex.model.Request;

import java.util.List;

import org.hibernate.Session;


public interface BillingPlugin {
  public List constructBillingItems(Session sess, BillingPeriod billingPeriod, PriceCategory priceCategory, Request request);
}
