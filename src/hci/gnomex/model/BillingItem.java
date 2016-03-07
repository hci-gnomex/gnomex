package hci.gnomex.model;

import hci.framework.model.FieldFormatter;
import hci.hibernate3utils.HibernateDetailObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;

import org.hibernate.Query;
import org.hibernate.Session;

@SuppressWarnings("serial")
public class BillingItem extends HibernateDetailObject {

  private Integer        	idBillingItem;
  private String         	category;
  private String         	description;
  private Integer        	qty;
  private BigDecimal     	unitPrice;
  private BigDecimal     	totalPrice;
  private BigDecimal     	invoicePrice;
  private BigDecimal     	percentagePrice;
  private String         	codeBillingChargeKind;
  private String         	codeBillingStatus;
  private String         	currentCodeBillingStatus;
  private Integer        	idBillingPeriod;  
  private BillingPeriod  	billingPeriod;
  private Integer        	idPriceCategory;
  private PriceCategory  	priceCategory;
  private Integer        	idPrice;
  private Integer        	idRequest;
  private Integer        	idBillingAccount;
  private BillingAccount 	billingAccount;
  private Integer        	idLab;
  private Lab            	lab;
  private String         	notes;
  private Date           	completeDate;
  private String         	splitType;
  private Integer        	idCoreFacility;
  private Integer        	idInvoice;
  private Invoice        	invoice;
  private Integer        	idDiskUsageByMonth;
  private Integer        	idProductOrder;
  private String			tag;
  private Integer			idMasterBillingItem;
  private MasterBillingItem	masterBillingItem;

  @Override
  public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((this.idBillingItem == null) ? super.hashCode() : this.idBillingItem.hashCode());
	return result;
  }

  @Override
  public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (!(obj instanceof BillingItem))
		return false;
	BillingItem other = (BillingItem) obj;
	if (this.idBillingItem == null) {
		if (other.idBillingItem != null)
			return false;
	} else if (!this.idBillingItem.equals(other.idBillingItem))
		return false;
	return true;
  }

  public Integer getIdBillingItem() {
    return idBillingItem;
  }

  public void setIdBillingItem(Integer idBillingItem) {
    this.idBillingItem = idBillingItem;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Integer getQty() {
    return qty;
  }

  public void setQty(Integer qty) {
    this.qty = qty;
  }

  public BigDecimal getUnitPrice() {
    return unitPrice;
  }

  public void setUnitPrice(BigDecimal unitPrice) {
    this.unitPrice = unitPrice;
  }

  public String getTotalPriceDisplay() {
    return "$" + totalPrice;
  }

  public BigDecimal getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(BigDecimal totalPrice) {
    this.totalPrice = totalPrice;
  }

  public String getInvoicePriceDisplay() {
    return "$" + invoicePrice;
  }

  public BigDecimal getInvoicePrice() {
    return invoicePrice;
  }

  public void setInvoicePrice(BigDecimal invoicePrice) {
    this.invoicePrice = invoicePrice;
  }

  public String getCodeBillingChargeKind() {
    return codeBillingChargeKind;
  }

  public void setCodeBillingChargeKind(String codeBillingChargeKind) {
    this.codeBillingChargeKind = codeBillingChargeKind;
  }

  public String getCodeBillingStatus() {
    return codeBillingStatus;
  }

  public void setCodeBillingStatus(String codeBillingStatus) {
    this.codeBillingStatus = codeBillingStatus;
  }

  public String getCurrentCodeBillingStatus() {
    return currentCodeBillingStatus;
  }

  public void setCurrentCodeBillingStatus(String currentCodeBillingStatus) {
    this.currentCodeBillingStatus = currentCodeBillingStatus;
  }

  public Integer getIdBillingPeriod() {
    return idBillingPeriod;
  }

  public void setIdBillingPeriod(Integer idBillingPeriod) {
    this.idBillingPeriod = idBillingPeriod;
  }

  public Integer getIdRequest() {
    return idRequest;
  }

  public void setIdRequest(Integer idRequest) {
    this.idRequest = idRequest;
  }


  public Integer getIdBillingAccount() {
    return idBillingAccount;
  }


  public void setIdBillingAccount(Integer idBillingAccount) {
    this.idBillingAccount = idBillingAccount;
  }


  public BigDecimal getPercentagePrice() {
    return percentagePrice;
  }


  public void setPercentagePrice(BigDecimal percentagePrice) {
    this.percentagePrice = percentagePrice;
  }


  public BillingAccount getBillingAccount() {
    return billingAccount;
  }


  public void setBillingAccount(BillingAccount billingAccount) {
    this.billingAccount = billingAccount;
  }

  public Date getCompleteDate()
  {
    return completeDate;
  }

  public void setCompleteDate(Date completeDate)
  {
    this.completeDate = completeDate;
  }

  public String getCompleteDateOther() {
    return this.formatDate(this.completeDate, FieldFormatter.DATE_OUTPUT_SLASH); 
  }

  public String getSplitType() {
    return splitType;
  }

  public void setSplitType(String st) {
    splitType = st;
  }

  public void registerMethodsToExcludeFromXML() {
    this.excludeMethodFromXML("getBillingAccount");
    this.excludeMethodFromXML("getLab");
    this.excludeMethodFromXML("getBillingPeriod");
    this.excludeMethodFromXML("getPriceCategory");
    this.excludeMethodFromXML("getMasterBillingItem");
    this.excludeMethodFromXML("getInvoice");
  }


  public String getNotes() {
    return notes;
  }


  public void setNotes(String notes) {
    this.notes = notes;
  }


  public Integer getIdLab() {
    return idLab;
  }


  public void setIdLab(Integer idLab) {
    this.idLab = idLab;
  }


  public Lab getLab() {
    return lab;
  }


  public void setLab(Lab lab) {
    this.lab = lab;
  }

  public String getAccountName() {
    if (billingAccount != null) {
      return billingAccount.getAccountName();
    } else {
      return "";
    }
  }

  public String getAccountNameDisplay() {
    if(billingAccount != null) {
      return billingAccount.getAccountNameDisplay();
    } else {
      return "";
    }
  }

  public String getAccountNameAndNumber() {
    if (billingAccount != null) {
      return billingAccount.getAccountNameAndNumber();
    } else {
      return "";
    }
  }
  public String getAccountNumberDisplay() {
    if (billingAccount != null) {
      return billingAccount.getAccountNumberDisplay();
    } else {
      return "";
    }
  }
  public String getLabName() {
    if (lab != null) {
      return lab.getName();
    } else {
      return "";
    }
  }

  public String getPercentageDisplay() {
    if (percentagePrice != null) {
      return percentagePrice.multiply(new BigDecimal(100)).setScale(1, RoundingMode.HALF_UP).toString() + "%";
    } else {
      return "100.0%";
    }
  }


  public BillingPeriod getBillingPeriod() {
    return billingPeriod;
  }


  public void setBillingPeriod(BillingPeriod billingPeriod) {
    this.billingPeriod = billingPeriod;
  }


  public Integer getIdPriceCategory() {
    return idPriceCategory;
  }


  public void setIdPriceCategory(Integer idPriceCategory) {
    this.idPriceCategory = idPriceCategory;
  }


  public Integer getIdPrice() {
    return idPrice;
  }


  public void setIdPrice(Integer idPrice) {
    this.idPrice = idPrice;
  }


  public PriceCategory getPriceCategory() {
    return priceCategory;
  }


  public void setPriceCategory(PriceCategory priceCategory) {
    this.priceCategory = priceCategory;
  }

  public Integer getIdCoreFacility()
  {
    return idCoreFacility;
  }

  public void setIdCoreFacility(Integer idCoreFacility)
  {
    this.idCoreFacility = idCoreFacility;
  }

  public Integer getIdInvoice() {
    return idInvoice;
  }
  public void setIdInvoice(Integer id) {
    idInvoice = id;
  }

  public Invoice getInvoice() {
    return invoice;
  }
  public void setInvoice(Invoice invoice) {
    this.invoice = invoice;
  }

  public Integer getIdDiskUsageByMonth() {
    return idDiskUsageByMonth;
  }
  public void setIdDiskUsageByMonth(Integer id) {
    idDiskUsageByMonth = id;
  }

  public Integer getIdProductOrder() {
    return idProductOrder;
  }

  public void setIdProductOrder(Integer idProductOrder) {
    this.idProductOrder = idProductOrder;
  }
  
  public String getTag() {
	return tag;
  }
  
  public void setTag(String tag) {
	this.tag = tag;
  }

  public Integer getIdMasterBillingItem() {
	return this.idMasterBillingItem;
}

  public void setIdMasterBillingItem(Integer idMasterBillingItem) {
	this.idMasterBillingItem = idMasterBillingItem;
}

  public MasterBillingItem getMasterBillingItem() {
	return this.masterBillingItem;
}

  public void setMasterBillingItem(MasterBillingItem masterBillingItem) {
	this.masterBillingItem = masterBillingItem;
}

  private Invoice getInvoiceForBillingItem(Session sess) {
    String queryString = "from Invoice where idCoreFacility=:idCoreFacility and idBillingPeriod=:idBillingPeriod and idBillingAccount=:idBillingAccount";
    Query query = sess.createQuery(queryString);
    query.setInteger("idCoreFacility", this.getIdCoreFacility());
    query.setInteger("idBillingPeriod", this.getIdBillingPeriod());
    query.setInteger("idBillingAccount", this.getIdBillingAccount());
    Invoice existingInv = (Invoice)query.uniqueResult();

    return existingInv;
  }

  public Boolean resetInvoiceForBillingItem(Session sess) {
    if (getIdInvoice() == null) {
      return false;
    }

    Invoice invFromIds;
    Invoice existingInv = getInvoiceForBillingItem(sess);

    if (existingInv != null) {
      invFromIds = existingInv;
    } else {
      invFromIds = new Invoice();
      invFromIds.setIdCoreFacility(this.getIdCoreFacility());
      invFromIds.setIdBillingPeriod(this.getIdBillingPeriod());
      invFromIds.setIdBillingAccount(this.getIdBillingAccount());
    }

    if (invFromIds.getIdInvoice() == null || !invFromIds.getIdInvoice().equals(getIdInvoice())) {
      if (invFromIds.getIdInvoice() == null) {
        // new invoice
        sess.save(invFromIds);
        invFromIds.setInvoiceNumber("I-" + invFromIds.getIdInvoice().toString());
      }
      this.setIdInvoice(invFromIds.getIdInvoice());
      this.setInvoice(invFromIds);
      sess.save(invFromIds);
      return true;
    } else {
      return false;
    }
  }
}
