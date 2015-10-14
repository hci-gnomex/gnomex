package hci.gnomex.model;


import hci.framework.model.DetailObject;
import hci.gnomex.security.SecurityAdvisor;

import java.util.Iterator;
import java.util.Set;

public class BillingItemFilter extends DetailObject {


  // Criteria
  private Integer               idBillingPeriod;
  private BillingPeriod         billingPeriod;
  private Integer               idLab;
  private Integer               idBillingAccount;
  private String                requestNumber;
  private String                invoiceLookupNumber;
  private Integer               idCoreFacility;
  private String                excludeNewRequests;


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
    queryBuf.append("            lab.isExternalPricingCommercial, ");
    queryBuf.append("            lab.idLab ");

    queryBuf.append(" FROM       Request as req ");
    queryBuf.append(" JOIN       req.lab as lab ");
    queryBuf.append(" JOIN       req.billingAccount as ba ");
    queryBuf.append(" LEFT JOIN  req.appUser as appUser ");
    queryBuf.append(" LEFT JOIN  req.billingItems as bi ");
    queryBuf.append(" LEFT JOIN  bi.invoice as inv ");
    queryBuf.append(" WHERE      bi.idBillingItem is NULL ");

    if (billingPeriod != null) {
      queryBuf.append(" AND        ((req.createDate >= '" + this.formatDateTime(billingPeriod.getStartDate(),  this.dateOutputStyle) + "'");
      queryBuf.append(" AND        req.createDate <= '" + this.formatDate(billingPeriod.getEndDate(), this.DATE_OUTPUT_SQL) + " 23:59:59')");
      queryBuf.append(" OR ");
      queryBuf.append(" (        req.completedDate >= '" + this.formatDate(billingPeriod.getStartDate(), this.DATE_OUTPUT_SQL) + "'");
      queryBuf.append(" AND        req.completedDate <= '" + this.formatDate(billingPeriod.getEndDate(), this.DATE_OUTPUT_SQL) + " 23:59:59' ))");
    }




    addWhere = false;

    addRequestCriteria();

    this.addSecurityCriteria("req");

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
    queryBuf.append("        bi.idInvoice, ");
    queryBuf.append("        lab.contactEmail, ");
    queryBuf.append("        lab.billingContactEmail ");

    queryBuf.append(" FROM        Request as req ");
    queryBuf.append(" JOIN        req.billingItems as bi ");
    queryBuf.append(" LEFT JOIN   bi.invoice as inv ");
    queryBuf.append(" JOIN        req.appUser as appUser ");
    queryBuf.append(" JOIN        bi.billingAccount as ba ");
    queryBuf.append(" JOIN        bi.lab as lab ");

    addRequestCriteria();
    addBillingItemCriteria();

    this.addSecurityCriteria("req");

    queryBuf.append(" order by bi.codeBillingStatus, req.number, lab.lastName, lab.firstName, ba.accountName ");

    return queryBuf;

  }


  public StringBuffer getBillingDiskUsageQuery() {
    addWhere = true;
    queryBuf = new StringBuffer();

    queryBuf.append(" SELECT DISTINCT ");
    queryBuf.append("        bi.codeBillingStatus, ");
    queryBuf.append("        dsk.idDiskUsageByMonth, ");
    queryBuf.append("        bi.idLab, ");
    queryBuf.append("        lab.lastName, ");
    queryBuf.append("        lab.firstName, ");
    queryBuf.append("        dsk.asOfDate, ");
    queryBuf.append("        ba, ");
    queryBuf.append("        lab.isExternalPricing, ");
    queryBuf.append("        lab.isExternalPricingCommercial, ");
    queryBuf.append("        bi.idInvoice, ");
    queryBuf.append("        lab.contactEmail ");

    queryBuf.append(" FROM        DiskUsageByMonth as dsk ");
    queryBuf.append(" JOIN        dsk.billingItems as bi ");
    queryBuf.append(" LEFT JOIN   bi.invoice as inv ");
    queryBuf.append(" JOIN        bi.billingAccount as ba ");
    queryBuf.append(" JOIN        bi.lab as lab ");

    addDiskUsageCriteria();
    addBillingItemCriteria();

    this.addSecurityCriteria("dsk");

    queryBuf.append(" order by bi.codeBillingStatus, dsk.idDiskUsageByMonth, lab.lastName, lab.firstName, ba.accountName ");

    return queryBuf;

  }

  public StringBuffer getBillingProductOrderQuery() {
    addWhere = true;
    queryBuf = new StringBuffer();

    queryBuf.append(" SELECT DISTINCT ");
    queryBuf.append("        bi.codeBillingStatus, ");
    queryBuf.append("        po.productOrderNumber, ");
    queryBuf.append("        po.idProductOrder, ");
    queryBuf.append("        bi.idLab, ");
    queryBuf.append("        lab.lastName, ");
    queryBuf.append("        lab.firstName, ");
    queryBuf.append("        po.submitDate, ");
    queryBuf.append("        ba, ");
    queryBuf.append("        lab.isExternalPricing, ");
    queryBuf.append("        lab.isExternalPricingCommercial, ");
    queryBuf.append("        bi.idInvoice, ");
    queryBuf.append("        lab.contactEmail ");

    queryBuf.append(" FROM        ProductLineItem as pli ");
    queryBuf.append(" JOIN        pli.productOrder as po ");
    queryBuf.append(" JOIN        po.billingItems as bi ");
    queryBuf.append(" LEFT JOIN   bi.invoice as inv ");
    queryBuf.append(" JOIN        bi.billingAccount as ba ");
    queryBuf.append(" JOIN        bi.lab as lab ");

    addProductOrderCriteria();
    addBillingItemCriteria();

    addSecurityCriteria("po");
    
    return queryBuf;


  }

  public StringBuffer getBillingItemQuery() {
    getBaseBillingItemQuery();

    addRequestCriteria();
    addBillingItemCriteria();

    queryBuf.append(" order by req.number, bi.idLab, bi.idBillingAccount, bi.idBillingItem");

    return queryBuf;

  }  

  public StringBuffer getDiskUsageBillingItemQuery() {
    addWhere = true;
    queryBuf = new StringBuffer();

    queryBuf.append(" SELECT DISTINCT ");
    queryBuf.append("        bi.codeBillingStatus, ");
    queryBuf.append("        dsk.idDiskUsageByMonth, ");
    queryBuf.append("        dsk.idLab, ");
    queryBuf.append("        dsk.idCoreFacility, ");
    queryBuf.append("        lab.lastName, ");
    queryBuf.append("        lab.firstName, ");
    queryBuf.append("        bi, ");
    queryBuf.append("        lab.isExternalPricing, ");
    queryBuf.append("        lab.isExternalPricingCommercial ");

    queryBuf.append(" FROM        DiskUsageByMonth as dsk ");
    queryBuf.append(" JOIN        dsk.billingItems as bi ");
    queryBuf.append(" LEFT JOIN   bi.invoice as inv ");
    queryBuf.append(" JOIN        bi.billingAccount as ba ");
    queryBuf.append(" JOIN        bi.lab as lab ");

    addDiskUsageCriteria();
    addBillingItemCriteria();

    queryBuf.append(" order by dsk.idDiskUsageByMonth, bi.idLab, bi.idBillingAccount, bi.idBillingItem");

    return queryBuf;
  }

  public StringBuffer getProductOrderBillingItemQuery() {
    addWhere = true;
    queryBuf = new StringBuffer();

    queryBuf.append(" SELECT DISTINCT ");
    queryBuf.append("        bi.codeBillingStatus, ");
    queryBuf.append("        po.productOrderNumber, ");
    queryBuf.append("        po.idProductOrder, ");
    queryBuf.append("        po.idLab, ");
    queryBuf.append("        po.idCoreFacility, ");
    queryBuf.append("        lab.lastName, ");
    queryBuf.append("        lab.firstName, ");
    queryBuf.append("        bi, ");
    queryBuf.append("        lab.isExternalPricing, ");
    queryBuf.append("        lab.isExternalPricingCommercial ");

    queryBuf.append(" FROM        ProductLineItem as pli ");
    queryBuf.append(" JOIN        pli.productOrder as po ");
    queryBuf.append(" JOIN        po.billingItems as bi ");
    queryBuf.append(" LEFT JOIN   bi.invoice as inv ");
    queryBuf.append(" JOIN        bi.billingAccount as ba ");
    queryBuf.append(" JOIN        bi.lab as lab ");

    addProductOrderCriteria();
    addBillingItemCriteria();

    queryBuf.append(" order by po.idProductOrder, bi.idLab, bi.idBillingAccount, bi.idBillingItem");

    return queryBuf;
  }


  public StringBuffer getRelatedBillingItemQuery(Set<Integer> idRequests) {
    getBaseBillingItemQuery();

    // Get all billing items in a different billing period for the
    // requests of the billing items obtained in the main query and
    // split billing items in same period
    if (idRequests != null && idRequests.size() > 0) {
      this.addWhereOrAnd();
      queryBuf.append(" req.idRequest in (");
      for(Iterator<Integer> i = idRequests.iterator(); i.hasNext();) {
        Integer idRequest = i.next();
        queryBuf.append(idRequest.toString());
        if (i.hasNext()) {
          queryBuf.append(", ");
        }
      }
      queryBuf.append(") ");
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

    this.addSecurityCriteria("req");

    queryBuf.append(" order by inv.idInvoice ");

    return queryBuf;

  }

  public StringBuffer getDiskUsageInvoiceQuery() {
    addWhere = true;
    queryBuf = new StringBuffer();

    queryBuf.append(" SELECT DISTINCT ");
    queryBuf.append("        inv ");
    queryBuf.append(" FROM        DiskUsageByMonth as dsk ");
    queryBuf.append(" JOIN        dsk.billingItems as bi ");
    queryBuf.append(" JOIN        bi.billingAccount as ba ");
    queryBuf.append(" JOIN        bi.lab as lab ");
    queryBuf.append(" JOIN        bi.invoice as inv ");

    addDiskUsageCriteria();
    addBillingItemCriteria();

    this.addSecurityCriteria("dsk");

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

  public StringBuffer getCoreCommentsQuery() {
    addWhere = true;
    queryBuf = new StringBuffer();

    queryBuf.append(" SELECT DISTINCT ");
    queryBuf.append("        req.number, ");
    queryBuf.append("        req.name, ");
    queryBuf.append("        bi.codeBillingStatus, ");
    queryBuf.append("        req.corePrepInstructions, ");
    queryBuf.append("        lab.lastName, ");
    queryBuf.append("        lab.firstName ");

    queryBuf.append(" FROM        Request as req ");
    queryBuf.append(" JOIN        req.billingItems as bi ");
    queryBuf.append(" JOIN        req.lab as lab ");

    addRequestCriteria();
    addBillingItemCriteria();
    addCommentsCriteria();

    this.addSecurityCriteria("req");

    queryBuf.append(" order by bi.codeBillingStatus, req.number ");

    return queryBuf;

  }

  private void addSecurityCriteria(String classShortName) {
    if (this.secAdvisor.hasPermission(SecurityAdvisor.CAN_ADMINISTER_ALL_CORE_FACILITIES)) {
      return;
    } else if(idCoreFacility == null) {
      this.addWhereOrAnd();
      this.secAdvisor.appendCoreFacilityCriteria(queryBuf, classShortName);
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
      queryBuf.append(" (inv.invoiceNumber like '%" + invoiceLookupNumber + "%') ");
    }

    if (excludeNewRequests != null && excludeNewRequests.equals( "Y" )) {
      this.addWhereOrAnd();
      queryBuf.append( " req.codeRequestStatus != 'NEW'" );
    }
  }



  private void addDiskUsageCriteria() {

    // Search by lab 
    if (idLab != null){
      this.addWhereOrAnd();
      queryBuf.append(" bi.idLab =");
      queryBuf.append(idLab);
    }

    // Note core is on the billing item and may be different than that on
    // the disk usage.
    if (idCoreFacility != null){
      this.addWhereOrAnd();
      queryBuf.append(" bi.idCoreFacility =");
      queryBuf.append(idCoreFacility);
    }

    // Search by billing account 
    if (idBillingAccount != null){
      this.addWhereOrAnd();
      queryBuf.append(" dsk.idBillingAccount = ");
      queryBuf.append(idBillingAccount);
    } 
    // Search by request number Note that search by request number excludes all disk usage rows.
    if (requestNumber != null && 
        !requestNumber.equals("")){
      this.addWhereOrAnd();
      queryBuf.append("1 = 2");
    }     

    // Search by invoice number 
    if (invoiceLookupNumber != null && 
        !invoiceLookupNumber.equals("")){
      this.addWhereOrAnd();
      queryBuf.append(" (inv.invoiceNumber like '%" + invoiceLookupNumber + "%') ");
    }
  }

  private void addProductOrderCriteria() {

    // Search by lab 
    if (idLab != null){
      this.addWhereOrAnd();
      queryBuf.append(" bi.idLab =");
      queryBuf.append(idLab);
    }

    if (idCoreFacility != null){
      this.addWhereOrAnd();
      queryBuf.append(" po.idCoreFacility =");
      queryBuf.append(idCoreFacility);
    }
    // Search by billing account 
    if (idBillingAccount != null){
      this.addWhereOrAnd();
      queryBuf.append(" po.idBillingAccount = ");
      queryBuf.append(idBillingAccount);
    } 
    
    // Search by request number Note that search by request number excludes all product order rows.
    if (requestNumber != null && 
        !requestNumber.equals("")){
      this.addWhereOrAnd();
      queryBuf.append("1 = 2");
    }     

    // Search by invoice number 
    if (invoiceLookupNumber != null && 
        !invoiceLookupNumber.equals("")){
      this.addWhereOrAnd();
      queryBuf.append(" (inv.invoiceNumber like '%" + invoiceLookupNumber + "%') ");
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

  private void addCommentsCriteria() {

    this.addWhereOrAnd();
    queryBuf.append(" req.corePrepInstructions != '' ");

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


  public String getExcludeNewRequests() {
    return excludeNewRequests;
  }


  public void setExcludeNewRequests( String excludeNewRequests ) {
    this.excludeNewRequests = excludeNewRequests;
  }





}
