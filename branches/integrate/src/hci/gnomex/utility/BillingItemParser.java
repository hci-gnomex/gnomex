package hci.gnomex.utility;

import hci.framework.model.DetailObject;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingStatus;
import hci.gnomex.model.Invoice;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;


public class BillingItemParser extends DetailObject implements Serializable {
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private Document   doc;
  private List<BillingItem>       billingItems = new ArrayList<BillingItem>();
  private List<BillingItem>       billingItemsToRemove = new ArrayList<BillingItem>();
  private List<Invoice>           invoices = new ArrayList<Invoice>();
  
  
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
        BigDecimal originalUnitPrice = new BigDecimal(0);
        Integer originalQty = new Integer(0);
        if (idBillingItemString == null || idBillingItemString.equals("") || idBillingItemString.startsWith("BillingItem")) {
          billingItem = new BillingItem();
        } else {
          billingItem = (BillingItem)sess.load(BillingItem.class, new Integer(idBillingItemString));
          originalUnitPrice = billingItem.getUnitPrice();
          originalQty = billingItem.getQty();
        }
        
        billingItem.setCategory(node.getAttributeValue("category"));
        billingItem.setCodeBillingChargeKind(node.getAttributeValue("codeBillingChargeKind"));
        billingItem.setDescription(node.getAttributeValue("description"));
        billingItem.setIdPriceCategory(!node.getAttributeValue("idPriceCategory").equals("") ? new Integer(node.getAttributeValue("idPriceCategory")) : null);
        billingItem.setIdBillingPeriod(!node.getAttributeValue("idBillingPeriod").equals("") ? new Integer(node.getAttributeValue("idBillingPeriod")) : null);
        billingItem.setIdPrice(!node.getAttributeValue("idPrice").equals("") ? new Integer(node.getAttributeValue("idPrice")) : null);
        if (node.getAttributeValue("idDiskUsageByMonth") != null) {
          billingItem.setIdDiskUsageByMonth(!node.getAttributeValue("idDiskUsageByMonth").equals("") ? new Integer(node.getAttributeValue("idDiskUsageByMonth")) : null);          
        }
        billingItem.setIdRequest(!node.getAttributeValue("idRequest").equals("") ? new Integer(node.getAttributeValue("idRequest")) : null);
        billingItem.setIdBillingAccount(!node.getAttributeValue("idBillingAccount").equals("") ? new Integer(node.getAttributeValue("idBillingAccount")) : null);
        billingItem.setIdLab(!node.getAttributeValue("idLab").equals("") ? new Integer(node.getAttributeValue("idLab")) : null);
        billingItem.setQty(!node.getAttributeValue("qty").equals("") ? new Integer(node.getAttributeValue("qty")) : null);
        billingItem.setIdCoreFacility(!node.getAttributeValue("idCoreFacility").equals("") ? new Integer(node.getAttributeValue("idCoreFacility")) : null);
        
        if (node.getAttributeValue("completeDate") != null && !node.getAttributeValue("completeDate").equals("")) {
          billingItem.setCompleteDate(this.parseDate(node.getAttributeValue("completeDate")));
        } else {
          billingItem.setCompleteDate(null);
        }
        
        String unitPrice = node.getAttributeValue("unitPrice");
        unitPrice = unitPrice.replaceAll("\\$", "");
        unitPrice = unitPrice.replaceAll(",", "");
        billingItem.setUnitPrice(!unitPrice.equals("") ? new BigDecimal(unitPrice) : null);
        
        billingItem.setNotes(node.getAttributeValue("notes"));

        String percentageDisplay = node.getAttributeValue("percentageDisplay") != null ? node.getAttributeValue("percentageDisplay") : "";        
        percentageDisplay = percentageDisplay.replaceAll("\\%", "");
        billingItem.setPercentagePrice(percentageDisplay.length() != 0 ? new BigDecimal(percentageDisplay).movePointLeft(2) : null);

        
        if (billingItem.getQty() != null && billingItem.getUnitPrice() != null) {
          if (billingItem.getSplitType() == null) {
            billingItem.setSplitType(Constants.BILLING_SPLIT_TYPE_PERCENT_CODE);
          } 
          if (billingItem.getSplitType().equals(Constants.BILLING_SPLIT_TYPE_PERCENT_CODE)) {
            billingItem.setInvoicePrice(billingItem.getUnitPrice().multiply(new BigDecimal(billingItem.getQty()).multiply(billingItem.getPercentagePrice())));
          } else {
            if (!billingItem.getUnitPrice().equals(originalUnitPrice) || !billingItem.getQty().equals(originalQty)) {
              BigDecimal originalTotalPrice = originalUnitPrice.multiply(new BigDecimal(originalQty));
              BigDecimal newTotalPrice = billingItem.getUnitPrice().multiply(new BigDecimal(billingItem.getQty()));
              billingItem.setInvoicePrice(billingItem.getInvoicePrice().add(newTotalPrice.subtract(originalTotalPrice)));
            }
          }
        }

        
        if (node.getAttributeValue("codeBillingStatus") != null && !node.getAttributeValue("codeBillingStatus").equals("")) {
          String codeBillingStatus = node.getAttributeValue("codeBillingStatus");

          // If we have toggled from not complete to complete, set complete date
          if (codeBillingStatus.equals(BillingStatus.COMPLETED) && billingItem.getIdBillingItem() != null) {
            if (billingItem.getCodeBillingStatus() == null || 
                billingItem.getCodeBillingStatus().equals("") ||
                !billingItem.getCodeBillingStatus().equals(BillingStatus.COMPLETED)) {
              billingItem.setCompleteDate(new java.sql.Date(System.currentTimeMillis()));
            }
          }
          billingItem.setCodeBillingStatus(codeBillingStatus);  
        }
        
        // Set the billing status
        String codeBillingStatus = node.getAttributeValue("codeBillingStatus");
        billingItem.setCodeBillingStatus(codeBillingStatus);
        
        
        // Set the billing status
        String currentCodeBillingStatus = node.getAttributeValue("currentCodeBillingStatus");
        billingItem.setCurrentCodeBillingStatus(currentCodeBillingStatus);
        
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
        } 
          billingItem = (BillingItem)sess.load(BillingItem.class, new Integer(idBillingItemString));
          billingItemsToRemove.add(billingItem);
        
      }
    }

    for(Iterator i = billingItemListNode.getChildren("InvoiceList").iterator(); i.hasNext();) {
      Element parentNode = (Element)i.next();
      
      for(Iterator i1 = parentNode.getChildren("Invoice").iterator(); i1.hasNext();) {
        Element node = (Element)i1.next();
        
        
        String idInvoice   = node.getAttributeValue("idInvoice");
        
        Invoice invoice = null;
        if (idInvoice == null || idInvoice.equals("")) {
          continue;
        } 
          invoice = (Invoice)sess.load(Invoice.class, new Integer(idInvoice));
          if (invoice.getInvoiceNumber() == null || !invoice.getInvoiceNumber().equals(node.getAttributeValue("invoiceNumber"))) {
            invoice.setInvoiceNumber(node.getAttributeValue("invoiceNumber"));
            invoices.add(invoice);
          
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
  
  public List<Invoice> getInvoices() {
    return invoices;
  }

}
