package hci.gnomex.utility;

import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingStatus;
import hci.gnomex.model.Request;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

/*
 * Auto Completes billing items based on work item step being processed.
 */
public class BillingItemAutoComplete {

  private Session   sess;
  private String    codeStep;
  private Request   request;
  private Boolean   skip = false;
  
  public BillingItemAutoComplete(Session sess, String codeStep, Request request) {
    this.sess     = sess;
    this.codeStep = codeStep;
    this.request  = request;
    this.skip     = false;
  }
  
  public void completeItems(Integer totalQty, Integer completedQty) {
    if (skip) {
      return;
    }
    
    ArrayList<BillingItemBucket> buckets = getBuckets();
    
    Integer rollingTotal = 0;
    for(BillingItemBucket bk : buckets) {
      rollingTotal += bk.getQty();
      if (rollingTotal <= completedQty) {
        bk.completeBillingItems();
      } else {
        break;
      }
    }
  }
  
  private ArrayList<BillingItemBucket> getBuckets() {
    ArrayList<BillingItemBucket> buckets = new ArrayList<BillingItemBucket>();
    
    List<BillingItem> biList = getBillingItems();
    for(BillingItem bi : biList) {
      Boolean added = false;
      for(BillingItemBucket bk : buckets) {
        if (bk.canAdd(bi)) {
          bk.addBillingItem(bi);
          added = true;
          break;
        } 
      }
      
      if (!added) {
        BillingItemBucket bk = new BillingItemBucket(bi);
        buckets.add(bk);
      }
    }
    
    return buckets;
  }
  
  private List<BillingItem> getBillingItems() {
    StringBuffer queryBuf = new StringBuffer();
    queryBuf.append("select distinct bi from BillingItem bi ")
      .append("  join bi.priceCategory pc ")
      .append("  join pc.steps step ")
      .append("  where bi.idRequest = :idRequest AND step.codeStep = :codeStep ")
      .append("  order by bi.idBillingItem ");
    Query query = sess.createQuery(queryBuf.toString());
    query.setParameter("idRequest", request.getIdRequest());
    query.setParameter("codeStep", codeStep);
    List<BillingItem> biList = query.list();
    
    return biList;
  }
  
  public Boolean getSkip() {
    return skip;
  }
  public void setSkip() {
    this.skip = true;
  }
  
  public String getCodeStep() {
    return codeStep;
  }
  
  public Request getRequest() {
    return request;
  }
  
  private class BillingItemBucket {
    private Integer                       qty = 0;
    private List<BillingItem>             billingItems = new ArrayList<BillingItem>();
    
    public BillingItemBucket(BillingItem bi) {
      this.qty = bi.getQty();
      billingItems.add(bi);
    }
    
    public Boolean canAdd(BillingItem newBi) {
      Boolean add = true;
      
      if (newBi == null) {
        add = false;
      } else if (newBi.getQty() == null || !newBi.getQty().equals(qty)) {
        add = false;
      } else {
        for(BillingItem bi : billingItems) {
          if (bi.getIdPrice().equals(newBi.getIdPrice()) && bi.getIdBillingAccount().equals(newBi.getIdBillingAccount())) {
            add = false;
            break;
          }
        }
      }
      
      return add;
    }
    
    public void completeBillingItems() {
      for(BillingItem bi : billingItems) {
        if (bi.getCodeBillingStatus().equals(BillingStatus.PENDING)) {
          bi.setCodeBillingStatus(BillingStatus.COMPLETED);
          bi.setCompleteDate(new java.sql.Date(System.currentTimeMillis()));
          sess.save(bi);
        }
      }
    }
    public void addBillingItem(BillingItem item) {
      billingItems.add(item);
    }
    
    public Integer getQty() {
      return qty;
    }
    
    public List<BillingItem> getBillingItems() {
      return billingItems;
    }
  }
}
