package hci.gnomex.utility;

import hci.gnomex.model.BillingAccount;
import hci.framework.model.DetailObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;


public class BillingAccountParser extends DetailObject implements Serializable {
  
  protected Document   doc;
  protected Map        billingAccountMap = new HashMap();
  
  public BillingAccountParser(Document doc) {
    this.doc = doc;
 
  }
  
  public void parse(Session sess) throws Exception{
    
    Element root = this.doc.getRootElement();
    
    
    for(Iterator i = root.getChildren("BillingAccount").iterator(); i.hasNext();) {
      Element node = (Element)i.next();
      
      String idBillingAccountString = node.getAttributeValue("idBillingAccount");
      BillingAccount billingAccount = null;
      if (idBillingAccountString.startsWith("BillingAccount")) {
        billingAccount = new BillingAccount();
      } else {
        billingAccount = (BillingAccount)sess.load(BillingAccount.class, new Integer(idBillingAccountString));
      }
      
      
      
      this.initializeBillingAccount(sess, node, billingAccount);
      
      billingAccountMap.put(idBillingAccountString, billingAccount);
    }
    
   
  }
  
  protected void initializeBillingAccount(Session sess, Element n, BillingAccount billingAccount) throws Exception {

    if (n.getAttributeValue("accountName") != null && !n.getAttributeValue("accountName").equals("")) {
      billingAccount.setAccountName(n.getAttributeValue("accountName"));
    } 
    if (n.getAttributeValue("accountNumberBus") != null && !n.getAttributeValue("accountNumberBus").equals("")) {
      billingAccount.setAccountNumberBus(n.getAttributeValue("accountNumberBus"));
    } 
    if (n.getAttributeValue("accountNumberOrg") != null && !n.getAttributeValue("accountNumberOrg").equals("")) {
      billingAccount.setAccountNumberOrg(n.getAttributeValue("accountNumberOrg"));
    } 
    if (n.getAttributeValue("accountNumberFund") != null && !n.getAttributeValue("accountNumberFund").equals("")) {
      billingAccount.setAccountNumberFund(n.getAttributeValue("accountNumberFund"));
    } 
    if (n.getAttributeValue("accountNumberActivity") != null && !n.getAttributeValue("accountNumberActivity").equals("")) {
      billingAccount.setAccountNumberActivity(n.getAttributeValue("accountNumberActivity"));
    } 
    if (n.getAttributeValue("accountNumberProject") != null && !n.getAttributeValue("accountNumberProject").equals("")) {
      billingAccount.setAccountNumberProject(n.getAttributeValue("accountNumberProject"));
    } 
    if (n.getAttributeValue("accountNumberAccount") != null && !n.getAttributeValue("accountNumberAccount").equals("")) {
      billingAccount.setAccountNumberAccount(n.getAttributeValue("accountNumberAccount"));
    } 
    if (n.getAttributeValue("accountNumberAu") != null && !n.getAttributeValue("accountNumberAu").equals("")) {
      billingAccount.setAccountNumberAu(n.getAttributeValue("accountNumberAu"));
    } 
    if (n.getAttributeValue("accountNumberYear") != null && !n.getAttributeValue("accountNumberYear").equals("")) {
      billingAccount.setAccountNumberYear(n.getAttributeValue("accountNumberYear"));
    } 
    
    if (n.getAttributeValue("expirationDate") != null && !n.getAttributeValue("expirationDate").equals("")) {
      billingAccount.setExpirationDate(this.parseDate(n.getAttributeValue("expirationDate")));
    } else {
      billingAccount.setExpirationDate(null);
    }
    if (n.getAttributeValue("idFundingAgency") != null && !n.getAttributeValue("idFundingAgency").equals("")) {
      billingAccount.setIdFundingAgency(new Integer(n.getAttributeValue("idFundingAgency")));
    } 
  }

  
  public Map getBillingAccountMap() {
    return billingAccountMap;
  }

  
  public void setBillingAccountMap(Map billingAccountMap) {
    this.billingAccountMap = billingAccountMap;
  }
  


}
