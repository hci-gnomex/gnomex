package hci.gnomex.utility.parsers;

import hci.framework.model.DetailObject;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.BillingAccount;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.HashSet;
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
    if (n.getAttributeValue("accountNumberBus") != null) {
      billingAccount.setAccountNumberBus(n.getAttributeValue("accountNumberBus"));
    } 
    if (n.getAttributeValue("accountNumberOrg") != null) {
      billingAccount.setAccountNumberOrg(n.getAttributeValue("accountNumberOrg"));
    } 
    if (n.getAttributeValue("accountNumberFund") != null) {
      billingAccount.setAccountNumberFund(n.getAttributeValue("accountNumberFund"));
    } 
    if (n.getAttributeValue("accountNumberActivity") != null) {
      billingAccount.setAccountNumberActivity(n.getAttributeValue("accountNumberActivity"));
    } 
    if (n.getAttributeValue("accountNumberProject") != null) {
      billingAccount.setAccountNumberProject(n.getAttributeValue("accountNumberProject"));
    } 
    if (n.getAttributeValue("accountNumberAccount") != null) {
      billingAccount.setAccountNumberAccount(n.getAttributeValue("accountNumberAccount"));
    } 
    if (n.getAttributeValue("accountNumberAu") != null) {
      billingAccount.setAccountNumberAu(n.getAttributeValue("accountNumberAu"));
    } 
    if (n.getAttributeValue("custom1") != null) {
      billingAccount.setCustom1(n.getAttributeValue("custom1"));
    } 
    if (n.getAttributeValue("custom2") != null) {
      billingAccount.setCustom2(n.getAttributeValue("custom2"));
    } 
    if (n.getAttributeValue("custom3") != null) {
      billingAccount.setCustom3(n.getAttributeValue("custom3"));
    } 
    
    if (n.getAttributeValue("startDate") != null && !n.getAttributeValue("startDate").equals("")) {
      billingAccount.setStartDate(this.parseDate(n.getAttributeValue("startDate")));
    } else {
      billingAccount.setStartDate(null);
    }
    
    if (n.getAttributeValue("expirationDate") != null && !n.getAttributeValue("expirationDate").equals("")) {
      billingAccount.setExpirationDate(this.parseDate(n.getAttributeValue("expirationDate")));
    } else {
      billingAccount.setExpirationDate(null);
    }
    
    if (n.getAttributeValue("idFundingAgency") != null && !n.getAttributeValue("idFundingAgency").equals("")) {
      billingAccount.setIdFundingAgency(new Integer(n.getAttributeValue("idFundingAgency")));
    } else {
      billingAccount.setIdFundingAgency(null);
    }
    if (n.getAttributeValue("isPO") != null && !n.getAttributeValue("isPO").equals("")) {
        billingAccount.setIsPO(n.getAttributeValue("isPO"));
    } else {
      billingAccount.setIsPO("N");
    }
    
    if (n.getAttributeValue("isCreditCard") != null && !n.getAttributeValue("isCreditCard").equals("")) {
      billingAccount.setIsCreditCard(n.getAttributeValue("isCreditCard"));
    } else {
      billingAccount.setIsCreditCard("N");
    }
    
    if (n.getAttributeValue("idCreditCardCompany") != null && !n.getAttributeValue("idCreditCardCompany").equals("")) {
      billingAccount.setIdCreditCardCompany(Integer.valueOf(n.getAttributeValue("idCreditCardCompany")));
    } else {
      billingAccount.setIdCreditCardCompany(null);
    }
    
    if (n.getAttributeValue("zipCode") != null && !n.getAttributeValue("zipCode").equals("")) {
      billingAccount.setZipCode(n.getAttributeValue("zipCode"));
    } else {
      billingAccount.setZipCode("");
    }
    
    if (n.getAttributeValue("totalDollarAmountDisplay") != null && !n.getAttributeValue("totalDollarAmountDisplay").equals("")) {
      String totalDollarAmount = n.getAttributeValue("totalDollarAmountDisplay");
      DecimalFormatSymbols dfs = new DecimalFormatSymbols();
      String regex = "[^0-9" + dfs.getDecimalSeparator() + "]";
      totalDollarAmount = totalDollarAmount.replaceAll(regex, "");
      totalDollarAmount = totalDollarAmount.replace(dfs.getDecimalSeparator(), '.');
      billingAccount.setTotalDollarAmount(new BigDecimal(totalDollarAmount));
   }


    if (n.getAttributeValue("shortAcct") != null && !n.getAttributeValue("shortAcct").equals("")) {
      billingAccount.setShortAcct(n.getAttributeValue("shortAcct"));
    } else {
      billingAccount.setShortAcct(null);
    }
    
    if (n.getAttributeValue("idCoreFacility") != null && !n.getAttributeValue("idCoreFacility").equals("")) {
      billingAccount.setIdCoreFacility(new Integer(n.getAttributeValue("idCoreFacility")));
    } else {
      billingAccount.setIdCoreFacility(null);
    }
        
    if (n.getAttributeValue("isApproved") != null && !n.getAttributeValue("isApproved").equals("")) {
      String isApproved = n.getAttributeValue("isApproved");
      
      // If we have toggled from not approved to approved, set the approved date
      if (isApproved.equals("Y") && billingAccount.getIdBillingAccount() != null) {
        if (billingAccount.getIsApproved() == null || 
            billingAccount.getIsApproved().equals("") ||
            billingAccount.getIsApproved().equalsIgnoreCase("N")) {
          billingAccount.setApprovedDate(new java.sql.Date(System.currentTimeMillis()));
          billingAccount.isJustApproved(true);
        }
      }
      billingAccount.setIsApproved(isApproved);
      
      if (n.getAttributeValue("submitterEmail") != null) {
        billingAccount.setSubmitterEmail(n.getAttributeValue("submitterEmail"));
      }
    } else {
      billingAccount.setIsApproved("N");
    }
    
    updateUsers(sess, n, billingAccount);
  }

  private void updateUsers(Session sess, Element node, BillingAccount acct) {
    HashMap<Integer, AppUser> addMap = new HashMap<Integer, AppUser>();
    HashMap<Integer, AppUser> removeMap = new HashMap<Integer, AppUser>();
    String acctUsers = node.getAttributeValue("acctUsers");
    if (acctUsers != null && !acctUsers.equals("")) {
      String[] tokens = acctUsers.split(",");
      for (int x = 0; x < tokens.length; x++) {
        String idAppUserString = tokens[x];
        addMap.put(Integer.valueOf(idAppUserString), null);
      }
    }
    
    // update arrays.
    if (acct.getUsers() != null) {
      for(Iterator i = acct.getUsers().iterator(); i.hasNext(); ) {
        AppUser user = (AppUser)i.next();
        if (addMap.containsKey(user.getIdAppUser())) {
          addMap.remove(user.getIdAppUser());
        } else {
          removeMap.put(user.getIdAppUser(), user);
        }
      }
    } else {
      acct.setUsers(new HashSet());
    }
    
    // Add new ones
    for(Integer id:addMap.keySet()) {
      AppUser user = addMap.get(id);
      user = (AppUser)sess.load(AppUser.class, id);
      acct.getUsers().add(user);
    }
    
    // Remove deleted ones
    for(Integer id:removeMap.keySet()) {
      AppUser user = removeMap.get(id);
      acct.getUsers().remove(user);
    }
  }
  
  public Map getBillingAccountMap() {
    return billingAccountMap;
  }

  
  public void setBillingAccountMap(Map billingAccountMap) {
    this.billingAccountMap = billingAccountMap;
  }
}
