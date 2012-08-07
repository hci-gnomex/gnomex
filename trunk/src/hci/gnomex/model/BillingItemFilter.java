package hci.gnomex.model;


import java.util.Set;
import java.util.Iterator;

import hci.framework.model.DetailObject;
import hci.gnomex.security.SecurityAdvisor;

public class BillingItemFilter extends DetailObject {
  
  
  // Criteria
  private Integer               idBillingPeriod;
  private BillingPeriod         billingPeriod;
  private Integer               idLab;
  private Integer               idBillingAccount;
  private String                requestNumber;
  private String                invoiceLookupNumber;
  private Integer               idCoreFacility;
  
  
  private StringBuffer          queryBuf;
  private boolean              addWhere = true;
  
  private SecurityAdvisor       secAdvisor;
  
  public BillingItemFilter(SecurityAdvisor secAdvisor) {
    this.secAdvisor =  secAdvisor;
  }
  
  public boolean hasCriteria() {
    if (idBillingPeriod == null &&
        idCoreFacility == null &&
        idLab == null &&
        idBillingAccount == null &&
        (requestNumber == null || requestNumber.equals("")) &&
        (invoiceLookupNumber == null || invoiceLookupNumber.equals(""))) {
      return false;
    } else {
      return true;
    }
  
  }

  public StringBuffer getBillingNewRequestQuery() {
    addWhere = true;
    queryBuf = new StringBuffer();
    
    queryBuf.append(" SELECT     req.idRequest, ");
    queryBuf.append("            req.number, ");
    queryBuf.append("            req.codeRequestCategory, ");
    queryBuf.append("            req.idBillingAccount, ");
    queryBuf.append("            lab.lastName, ");
    queryBuf.append("            lab.firstName, ");
    queryBuf.append("            appUser, ");
    queryBuf.append("            req.createDate, ");
    queryBuf.append("            req.completedDate, ");
    queryBuf.append("            ba, ");
    queryBuf.append("            lab.isExternalPricing, ");
    queryBuf.append("            lab.isExternalPricingCommercial ");

    queryBuf.append(" FROM       Request as req ");
    queryBuf.append(" JOIN       req.lab as lab ");
    queryBuf.append(" JOIN       req.billingAccount as ba ");
    queryBuf.append(" LEFT JOIN  req.appUser as appUser ");
    queryBuf.append(" LEFT JOIN  req.billingItems as bi ");
    queryBuf.append(" LEFT JOIN  bi.invoice as inv ");
    queryBuf.append(" WHERE      bi.idBillingItem is NULL ");
    
    if (billingPeriod != null) {
      queryBuf.append(" AND        req.createDate >= '" + this.formatDateTime(billingPeriod.getStartDate(),  this.dateOutputStyle) + "'");
      queryBuf.append(" AND        req.createDate <= '" + this.formatDate(billingPeriod.getEndDate(), this.DATE_OUTPUT_SQL) + " 23:59:59'");      
    }
    
    

    
    addWhere = false;
    
    addRequestCriteria();
   
    this.addSecurityCriteria();

    return queryBuf;
    
  }
  
  
  public StringBuffer getBillingRequestQuery() {
    addWhere = true;
    queryBuf = new StringBuffer();
    
    queryBuf.append(" SELECT DISTINCT ");
    queryBuf.append("        bi.codeBillingStatus, ");
    queryBuf.append("        req.idRequest, ");
    queryBuf.append("        req.number, ");
    queryBuf.append("        req.codeRequestCategory, ");
    queryBuf.append("        bi.idLab, ");
    queryBuf.append("        lab.lastName, ");
    queryBuf.append("        lab.firstName, ");
    queryBuf.append("        appUser, ");
    queryBuf.append("        req.createDate, ");
    queryBuf.append("        req.completedDate, ");
    queryBuf.append("        ba, ");
    queryBuf.append("        lab.isExternalPricing, ");
    queryBuf.append("        lab.isExternalPricingCommercial, ");
    queryBuf.append("        bi.idInvoice ");
    
    queryBuf.append(" FROM        Request as req ");
    queryBuf.append(" JOIN        req.billingItems as bi ");
    queryBuf.append(" LEFT JOIN   bi.invoice as inv ");
    queryBuf.append(" JOIN        req.appUser as appUser ");
    queryBuf.append(" JOIN        bi.billingAccount as ba ");
    queryBuf.append(" JOIN        bi.lab as lab ");
    
    addRequestCriteria();
    addBillingItemCriteria();
    
    this.addSecurityCriteria();
    
    queryBuf.append(" order by bi.codeBillingStatus, req.number, lab.lastName, lab.firstName, ba.accountName ");
    
    return queryBuf;
    
  }

  public StringBuffer getBillingItemQuery() {
    getBaseBillingItemQuery();
    
    addRequestCriteria();
    addBillingItemCriteria();
    
    queryBuf.append(" order by req.number, bi.idLab, bi.idBillingAccount, bi.idBillingItem");
    
    return queryBuf;
    
  }  
  
  public StringBuffer getRelatedBillingItemQuery(Set idRequests) {
    getBaseBillingItemQuery();
    
    
    // Get all billing items in a different billing period for the
    // requests of the billing items obtained in the main query
    if (idRequests != null && idRequests.size() > 0){
      this.addWhereOrAnd();
      queryBuf.append(" req.idRequest in (");
      for(Iterator i = idRequests.iterator(); i.hasNext();) {
        Integer idRequest = (Integer)i.next();
        queryBuf.append(idRequest.toString());
        if (i.hasNext()) {
          queryBuf.append(", ");
        }
      }
      queryBuf.append(") ");
      this.addWhereOrAnd();
      // Get billing items from other period
      queryBuf.append(" bi.idBillingPeriod !=");
      queryBuf.append(idBillingPeriod);

    } 
    
    queryBuf.append(" order by req.number, bi.idLab, bi.idBillingAccount, bi.idBillingItem");

    return queryBuf;
  }
  
  public StringBuffer getBillingInvoiceQuery() {
    addWhere = true;
    queryBuf = new StringBuffer();
    
    queryBuf.append(" SELECT DISTINCT ");
    queryBuf.append("        inv ");
    queryBuf.append(" FROM        Request as req ");
    queryBuf.append(" JOIN        req.billingItems as bi ");
    queryBuf.append(" JOIN        req.appUser as appUser ");
    queryBuf.append(" JOIN        bi.billingAccount as ba ");
    queryBuf.append(" JOIN        bi.lab as lab ");
    queryBuf.append(" JOIN        bi.invoice as inv ");
    
    addRequestCriteria();
    addBillingItemCriteria();
    
    this.addSecurityCriteria();
    
    queryBuf.append(" order by inv.idInvoice ");
    
    return queryBuf;
    
  }
  
  private StringBuffer getBaseBillingItemQuery() {
    addWhere = true;
    queryBuf = new StringBuffer();
    
    queryBuf.append(" SELECT DISTINCT ");
    queryBuf.append("        bi.codeBillingStatus, ");
    queryBuf.append("        req.idRequest, ");
    queryBuf.append("        req.number, ");
    queryBuf.append("        req.idLab, ");
    queryBuf.append("        req.codeRequestCategory, ");
    queryBuf.append("        req.idCoreFacility, ");
    queryBuf.append("        lab.lastName, ");
    queryBuf.append("        lab.firstName, ");
    queryBuf.append("        appUser, ");
    queryBuf.append("        bi, ");
    queryBuf.append("        lab.isExternalPricing, ");
    queryBuf.append("        lab.isExternalPricingCommercial ");
    
    queryBuf.append(" FROM        Request as req ");
    queryBuf.append(" LEFT JOIN   req.appUser as appUser ");
    queryBuf.append(" JOIN        req.billingItems as bi ");
    queryBuf.append(" LEFT JOIN   bi.invoice as inv ");
    queryBuf.append(" JOIN        bi.billingAccount as ba ");
    queryBuf.append(" JOIN        bi.lab as lab ");
    
    return queryBuf;
  }   
  
  
  private void addSecurityCriteria() {
    if (this.secAdvisor.hasPermission(SecurityAdvisor.CAN_ADMINISTER_ALL_CORE_FACILITIES)) {
      return;
    } else {
      this.addWhereOrAnd();
      this.secAdvisor.appendCoreFacilityCriteria(queryBuf, "bi");
    }
    return;
  
  }
  

  private void addRequestCriteria() {

    // Search by lab 
    if (idLab != null){
      this.addWhereOrAnd();
      queryBuf.append(" bi.idLab =");
      queryBuf.append(idLab);
    }
    
    if (idCoreFacility != null){
      this.addWhereOrAnd();
      queryBuf.append(" req.idCoreFacility =");
      queryBuf.append(idCoreFacility);
    }
    // Search by billing account 
    if (idBillingAccount != null){
      this.addWhereOrAnd();
      queryBuf.append(" req.idBillingAccount = ");
      queryBuf.append(idBillingAccount);
    } 
    // Search by request number 
    if (requestNumber != null && 
        !requestNumber.equals("")){
      this.addWhereOrAnd();
      
      String requestNumberBase = Request.getBaseRequestNumber(requestNumber);
      queryBuf.append(" (req.number like '" + requestNumberBase + "[0-9]' OR req.number = '" + requestNumberBase + "' OR req.number like '" + requestNumberBase + "R[0-9]' OR req.number = '" + requestNumberBase + "R') ");
    }     
    
    // Search by invoice number 
    if (invoiceLookupNumber != null && 
        !invoiceLookupNumber.equals("")){
      this.addWhereOrAnd();
      String invoiceLookupNumberBase = Invoice.getBaseInvoiceNumber(invoiceLookupNumber);
      queryBuf.append(" (inv.invoiceNumber like '" + invoiceLookupNumber + "[0-9]' OR inv.invoiceNumber = '" + invoiceLookupNumber + "' OR inv.invoiceNumber like '" + invoiceLookupNumber + "I[0-9]' OR inv.invoiceNumber = '" + invoiceLookupNumber + "I') ");
    }     
  }
  private void addBillingItemCriteria() {


    // Search by billing period 
    if (idBillingPeriod != null){
      this.addWhereOrAnd();
      queryBuf.append(" bi.idBillingPeriod =");
      queryBuf.append(idBillingPeriod);
    } 
    
    
  }  
  

  
  protected boolean addWhereOrAnd() {
    if (addWhere) {
      queryBuf.append(" WHERE ");
      addWhere = false;
    } else {
      queryBuf.append(" AND ");
    }
    return addWhere;
  }

  
  public Integer getIdLab() {
    return idLab;
  }

  
  public Integer getIdBillingPeriod() {
    return idBillingPeriod;
  }

  
  public void setIdBillingPeriod(Integer idBillingPeriod) {
    this.idBillingPeriod = idBillingPeriod;
  }

  
  public Integer getIdBillingAccount() {
    return idBillingAccount;
  }

  
  public void setIdBillingAccount(Integer idBillingAccount) {
    this.idBillingAccount = idBillingAccount;
  }

  
  public String getRequestNumber() {
    return requestNumber;
  }

  
  public void setRequestNumber(String requestNumber) {
    this.requestNumber = requestNumber;
  }

  
  public String getInvoiceLookupNumber() {
    return invoiceLookupNumber;
  }

  
  public void setInvoiceLookupNumber(String invoiceLookupNumber) {
    this.invoiceLookupNumber = invoiceLookupNumber;
  }

  
  public void setIdLab(Integer idLab) {
    this.idLab = idLab;
  }

  
  public BillingPeriod getBillingPeriod() {
    return billingPeriod;
  }

  
  public void setBillingPeriod(BillingPeriod billingPeriod) {
    this.billingPeriod = billingPeriod;
  }

  public Integer getIdCoreFacility() {
    return idCoreFacility;
  }

  public void setIdCoreFacility(Integer idCoreFacility) {
    this.idCoreFacility = idCoreFacility;
  }



  
  
}
