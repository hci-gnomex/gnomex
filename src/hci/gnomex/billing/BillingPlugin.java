package hci.gnomex.billing;

import hci.gnomex.model.BillingCategory;
import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.Request;

import java.util.List;

import org.hibernate.Session;


public interface BillingPlugin {
  public List createBillingItems(Session sess, BillingPeriod billingPeriod, BillingCategory billingCategory, List billingPrices, Request request);
}
