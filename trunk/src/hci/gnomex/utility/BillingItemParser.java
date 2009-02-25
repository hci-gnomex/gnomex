package hci.gnomex.utility;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.BillingItem;
import hci.gnomex.model.FlowCell;
import hci.gnomex.model.FlowCellChannel;
import hci.gnomex.model.WorkItem;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;


public class BillingItemParser implements Serializable {
  
  private Document   doc;
  private List       billingItems = new ArrayList();
  private List       billingItemsToRemove = new ArrayList();
  private Map        requestMap = new HashMap();
  
  
  public BillingItemParser(Document doc) {
    this.doc = doc;
 
  }
  
  public void parse(Session sess) throws Exception{
    
    Element billingItemListNode = this.doc.getRootElement();
    
    

    for(Iterator i = billingItemListNode.getChildren("SaveList").iterator(); i.hasNext();) {
      Element parentNode = (Element)i.next();
      
      for(Iterator i1 = parentNode.getChildren("BillingItem").iterator(); i1.hasNext();) {
        Element node = (Element)i1.next();
        
        String idBillingItemString   = node.getAttributeValue("idBillingItem");
        
        BillingItem billingItem = null;
        if (idBillingItemString == null || idBillingItemString.equals("") || idBillingItemString.startsWith("BillingItem")) {
          billingItem = new BillingItem();
        } else {
          billingItem = (BillingItem)sess.load(BillingItem.class, new Integer(idBillingItemString));
        }
        
        billingItem.setCategory(node.getAttributeValue("category"));
        billingItem.setCodeBillingChargeKind(node.getAttributeValue("codeBillingChargeKind"));
        billingItem.setCodeBillingStatus(node.getAttributeValue("codeBillingStatus"));
        billingItem.setDescription(node.getAttributeValue("description"));
        billingItem.setIdBillingCategory(!node.getAttributeValue("idBillingCategory").equals("") ? new Integer(node.getAttributeValue("idBillingCategory")) : null);
        billingItem.setIdBillingPeriod(!node.getAttributeValue("idBillingPeriod").equals("") ? new Integer(node.getAttributeValue("idBillingPeriod")) : null);
        billingItem.setIdBillingPrice(!node.getAttributeValue("idBillingPrice").equals("") ? new Integer(node.getAttributeValue("idBillingPrice")) : null);
        billingItem.setIdRequest(!node.getAttributeValue("idRequest").equals("") ? new Integer(node.getAttributeValue("idRequest")) : null);
        billingItem.setIdBillingAccount(!node.getAttributeValue("idBillingAccount").equals("") ? new Integer(node.getAttributeValue("idBillingAccount")) : null);
        billingItem.setQty(!node.getAttributeValue("qty").equals("") ? new Integer(node.getAttributeValue("qty")) : null);
        billingItem.setUnitPrice(!node.getAttributeValue("unitPrice").equals("") ? new BigDecimal(node.getAttributeValue("unitPrice")) : null);
        billingItem.setPercentagePrice(!node.getAttributeValue("percentagePrice").equals("") ? new BigDecimal(node.getAttributeValue("percentagePrice")) : null);

        String totalPrice = node.getAttributeValue("totalPrice");
        if (!totalPrice.equals("")) {
          totalPrice = totalPrice.replaceAll("\\$", "");
          totalPrice = totalPrice.replaceAll(",", "");
          billingItem.setTotalPrice(new BigDecimal(totalPrice));        
        } else {
          billingItem.setTotalPrice(null);
        }
        
        requestMap.put(billingItem.getIdRequest(), billingItem.getIdBillingPeriod());

        
        billingItems.add(billingItem);
      }
    
    }
    for(Iterator i = billingItemListNode.getChildren("RemoveList").iterator(); i.hasNext();) {
      Element parentNode = (Element)i.next();
      
      for(Iterator i1 = parentNode.getChildren("BillingItem").iterator(); i1.hasNext();) {
        Element node = (Element)i1.next();
        
        
        String idBillingItemString   = node.getAttributeValue("idBillingItem");
        
        BillingItem billingItem = null;
        if (idBillingItemString == null || idBillingItemString.equals("") || idBillingItemString.startsWith("BillingItem")) {
          continue;
        } else {
          billingItem = (BillingItem)sess.load(BillingItem.class, new Integer(idBillingItemString));
          billingItemsToRemove.add(billingItem);
          requestMap.put(billingItem.getIdRequest(), billingItem.getIdBillingPeriod());
        }
      }
    }

    
    
   
  }
 
  
  public List getBillingItems() {
    return billingItems;
  }
  
  
  public List getBillingItemsToRemove() {
    return billingItemsToRemove;
  }
  
  public Set getIdRequests() {
    return requestMap.keySet();
  }
  
  public Integer getIdBillingPeriodForRequest(Integer idRequest) {
    return (Integer)requestMap.get(idRequest);
  }

}
