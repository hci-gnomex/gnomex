package hci.gnomex.utility;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingStatus;
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
        billingItem.setDescription(node.getAttributeValue("description"));
        billingItem.setIdBillingCategory(!node.getAttributeValue("idBillingCategory").equals("") ? new Integer(node.getAttributeValue("idBillingCategory")) : null);
        billingItem.setIdBillingPeriod(!node.getAttributeValue("idBillingPeriod").equals("") ? new Integer(node.getAttributeValue("idBillingPeriod")) : null);
        billingItem.setIdBillingPrice(!node.getAttributeValue("idBillingPrice").equals("") ? new Integer(node.getAttributeValue("idBillingPrice")) : null);
        billingItem.setIdRequest(!node.getAttributeValue("idRequest").equals("") ? new Integer(node.getAttributeValue("idRequest")) : null);
        billingItem.setIdBillingAccount(!node.getAttributeValue("idBillingAccount").equals("") ? new Integer(node.getAttributeValue("idBillingAccount")) : null);
        billingItem.setIdLab(!node.getAttributeValue("idLab").equals("") ? new Integer(node.getAttributeValue("idLab")) : null);
        billingItem.setQty(!node.getAttributeValue("qty").equals("") ? new Integer(node.getAttributeValue("qty")) : null);
        
        String unitPrice = node.getAttributeValue("unitPrice");
        unitPrice = unitPrice.replaceAll("\\$", "");
        unitPrice = unitPrice.replaceAll(",", "");
        billingItem.setUnitPrice(!unitPrice.equals("") ? new BigDecimal(unitPrice) : null);
        
        billingItem.setNotes(node.getAttributeValue("notes"));

        String percentageDisplay = node.getAttributeValue("percentageDisplay") != null ? node.getAttributeValue("percentageDisplay") : "";        
        percentageDisplay = percentageDisplay.replaceAll("\\%", "");
        billingItem.setPercentagePrice(percentageDisplay != "" ? new BigDecimal(percentageDisplay).movePointLeft(2) : null);

        String totalPrice = node.getAttributeValue("totalPrice");
        if (!totalPrice.equals("")) {
          totalPrice = totalPrice.replaceAll("\\$", "");
          totalPrice = totalPrice.replaceAll(",", "");
          billingItem.setTotalPrice(new BigDecimal(totalPrice));        
        } else {
          billingItem.setTotalPrice(null);
        }
        
        
        // For groups with external billing, approved status is changed to 
        // 'Approved External'
        String codeBillingStatus = node.getAttributeValue("codeBillingStatus");
        if (codeBillingStatus.equals(BillingStatus.APPROVED)) {
          if (billingItem.getLab().getIsExternal() != null && billingItem.getLab().getIsExternal().equals("Y")) {
            codeBillingStatus = BillingStatus.APPROVED_EXTERNAL;
          }
        }
        billingItem.setCodeBillingStatus(codeBillingStatus);

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
