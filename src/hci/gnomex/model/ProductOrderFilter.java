package hci.gnomex.model;

import hci.framework.model.DetailObject;
import hci.gnomex.security.SecurityAdvisor;

import java.sql.Date;

public class ProductOrderFilter extends DetailObject {

  // Criteria
  private Integer idBillingPeriod;
  private BillingPeriod billingPeriod;

  private Integer idProductOrder;
  private Integer idLab;
  private Integer idAppUser;
  private Integer idBillingAccount;
  private Integer idCoreFacility;

  private Date submitDateFrom;
  private Date submitDateTo;

  private String codeProductOrderStatus;
  private String codeProductType;
  private String quoteReceived = "N";

  private StringBuffer queryBuf;
  private boolean addWhere = true;

  private SecurityAdvisor secAdvisor;

  public ProductOrderFilter(SecurityAdvisor secAdvisor) {
    this.secAdvisor = secAdvisor;
  }

  public boolean hasCriteria() {
    if (idBillingPeriod == null && idCoreFacility == null && idLab == null && idBillingAccount == null) {
      return false;
    }
    return true;
  }

  public StringBuffer getProductOrderQuery() {
    addWhere = true;
    queryBuf = new StringBuffer();

    queryBuf.append("SELECT po.idProductOrder, po.productOrderNumber, lab.idLab FROM ProductOrder po JOIN po.lab as lab");
    this.addPOCriteria();
    this.addSecurityCriteria("po");
    return queryBuf;

  }

  public StringBuffer getProductOrderDetailsQuery() {
    addWhere = true;
    queryBuf = new StringBuffer();
    queryBuf.append("SELECT product.name, au.firstName, au.lastName, po.submitDate, po.quoteReceivedDate, po.quoteNumber, pli.qty ");
    queryBuf.append(" FROM ProductOrder as po ");
    queryBuf.append(" JOIN po.productLineItems as pli ");
    queryBuf.append(" JOIN po.submitter as au ");
    queryBuf.append(" JOIN pli.product as product ");

    this.addPOCriteria();
    this.addSecurityCriteria("po");

    return queryBuf;
  }

  public StringBuffer getLineItemQuery() {
    addWhere = true;
    queryBuf = new StringBuffer();

    queryBuf.append(" SELECT DISTINCT ");
    queryBuf.append(" po, ");
    queryBuf.append(" pli.qty, ");
    queryBuf.append(" pli.unitPrice, ");
    queryBuf.append(" product, ");
    queryBuf.append(" pli.idProductLineItem, ");
    queryBuf.append(" pli.codeProductOrderStatus ");

    getLineItemQueryBody(queryBuf);

    return queryBuf;

  }

  public void getLineItemQueryBody(StringBuffer queryBuf) {

    queryBuf.append(" FROM        ProductOrder as po ");
    queryBuf.append(" JOIN        po.productLineItems as pli ");
    queryBuf.append(" JOIN        pli.product as product ");

    addPOCriteria();
    addLineItemCriteria();
    addSecurityCriteria("po");

    // queryBuf.append(" group by po.idProductOrder ");
  }

  public StringBuffer getFilesQuery() {
    addWhere = true;
    queryBuf = new StringBuffer();

    queryBuf.append(" SELECT DISTINCT ");
    queryBuf.append(" po.idProductOrder, ");
    queryBuf.append(" file ");

    getFilesQueryBody(queryBuf);

    return queryBuf;

  }

  public void getFilesQueryBody(StringBuffer queryBuf) {

    queryBuf.append(" FROM        ProductOrder as po ");
    queryBuf.append(" JOIN        po.files as file ");

    addPOCriteria();
    addSecurityCriteria("po");
  }

  private void addPOCriteria() {

    // Search by lab
    if (idLab != null) {
      this.addWhereOrAnd();
      queryBuf.append(" po.idLab =");
      queryBuf.append(idLab);
    }
    // Search by user
    if (idAppUser != null) {
      this.addWhereOrAnd();
      queryBuf.append(" po.idAppUser = ");
      queryBuf.append(idAppUser);
    }
    // Search by submit date from
    if (submitDateFrom != null) {
      this.addWhereOrAnd();
      queryBuf.append(" po.submitDate >= '");
      queryBuf.append(this.formatDate(submitDateFrom, this.DATE_OUTPUT_SQL));
      queryBuf.append("'");
    }
    // Search by submit date to
    if (submitDateTo != null) {
      this.addWhereOrAnd();
      queryBuf.append(" po.submitDate <= '");
      queryBuf.append(this.formatDate(submitDateTo, this.DATE_OUTPUT_SQL));
      queryBuf.append("'");
    }
    // Search by billing period
    if (billingPeriod != null) {
      queryBuf.append(" AND        po.submitDate >= '" + this.formatDateTime(billingPeriod.getStartDate(), this.dateOutputStyle) + "'");
      queryBuf.append(" AND        po.submitDate <= '" + this.formatDate(billingPeriod.getEndDate(), this.DATE_OUTPUT_SQL) + " 23:59:59'");
    }
    // Search by product order type
    if (codeProductType != null && !codeProductType.equals("")) {
      this.addWhereOrAnd();
      queryBuf.append(" po.codeProductType like '");
      queryBuf.append(codeProductType);
      queryBuf.append("%'");
    }
    // Search by core facility
    if (idCoreFacility != null) {
      this.addWhereOrAnd();
      queryBuf.append(" po.idCoreFacility = ");
      queryBuf.append(idCoreFacility);
    }

    // Search by idProductOrder
    if (idProductOrder != null) {
      this.addWhereOrAnd();
      queryBuf.append(" po.idProductOrder = ");
      queryBuf.append(idProductOrder);
    }
  }

  private void addLineItemCriteria() {

    // Search by product order status
    if (codeProductOrderStatus != null && !codeProductOrderStatus.equals("")) {
      this.addWhereOrAnd();
      queryBuf.append(" pli.codeProductOrderStatus like '");
      queryBuf.append(codeProductOrderStatus);
      queryBuf.append("%'");
    }

  }

  private void addSecurityCriteria(String classShortName) {
    if (this.secAdvisor.hasPermission(SecurityAdvisor.CAN_ADMINISTER_ALL_CORE_FACILITIES)) {
      return;
    } else if (idCoreFacility == null) {
      secAdvisor.buildSecurityCriteria(queryBuf, classShortName, null, false, false, true, true, false);
    }
    return;

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

  public Integer getIdBillingPeriod() {
    return idBillingPeriod;
  }

  public void setIdBillingPeriod(Integer idBillingPeriod) {
    this.idBillingPeriod = idBillingPeriod;
  }

  public BillingPeriod getBillingPeriod() {
    return billingPeriod;
  }

  public void setBillingPeriod(BillingPeriod billingPeriod) {
    this.billingPeriod = billingPeriod;
  }

  public Integer getIdProductOrder() {
    return idProductOrder;
  }

  public void setIdProductOrder(Integer idProductOrder) {
    this.idProductOrder = idProductOrder;
  }

  public Integer getIdLab() {
    return idLab;
  }

  public void setIdLab(Integer idLab) {
    this.idLab = idLab;
  }

  public Integer getIdAppUser() {
    return idAppUser;
  }

  public void setIdAppUser(Integer idAppUser) {
    this.idAppUser = idAppUser;
  }

  public Integer getIdBillingAccount() {
    return idBillingAccount;
  }

  public void setIdBillingAccount(Integer idBillingAccount) {
    this.idBillingAccount = idBillingAccount;
  }

  public Integer getIdCoreFacility() {
    return idCoreFacility;
  }

  public void setIdCoreFacility(Integer idCoreFacility) {
    this.idCoreFacility = idCoreFacility;
  }

  public Date getSubmitDateFrom() {
    return submitDateFrom;
  }

  public void setSubmitDateFrom(Date submitDateFrom) {
    this.submitDateFrom = submitDateFrom;
  }

  public Date getSubmitDateTo() {
    return submitDateTo;
  }

  public void setSubmitDateTo(Date submitDateTo) {
    this.submitDateTo = submitDateTo;
  }

  public String getCodeProductOrderStatus() {
    return codeProductOrderStatus;
  }

  public void setCodeProductOrderStatus(String codeProductOrderStatus) {
    this.codeProductOrderStatus = codeProductOrderStatus;
  }

  public String getCodeProductType() {
    return codeProductType;
  }

  public void setCodeProductType(String codeProductType) {
    this.codeProductType = codeProductType;
  }

  public String getQuoteReceived() {
    return quoteReceived;
  }

  public void setQuoteReceived(String quoteReceived) {
    this.quoteReceived = quoteReceived;
  }

}
