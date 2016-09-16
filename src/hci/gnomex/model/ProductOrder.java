package hci.gnomex.model;

import hci.dictionary.utility.DictionaryManager;
import hci.framework.model.DetailObject;
import hci.gnomex.utility.BillingItemQueryManager;
import hci.gnomex.utility.BillingTemplateQueryManager;
import hci.gnomex.utility.Order;
import org.hibernate.Session;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Set;
import java.util.TreeSet;


@SuppressWarnings("serial")
public class ProductOrder extends DetailObject implements Serializable, Order {

  public static final String PRODUCT_ORDER_REQUEST_CATEGORY = "PRODUCTORDER";
  public static final String PRODUCT_ORDER_ICON = "assets/basket.png";

  private Integer     idProductOrder;
  private Integer     idAppUser;
  private AppUser     submitter;
  private Integer     idLab;
  private Lab         lab;
  private Integer     idCoreFacility;
  private Integer     idBillingAccount;
  private Date        submitDate;
  private Integer     idProductType;
  private ProductType productType;
  private String      quoteNumber;
  private Date        quoteReceivedDate;
  private String      uuid;
  private String      productOrderNumber;

  private Set<ProductLineItem>         	productLineItems = new TreeSet<ProductLineItem>();
  private Set         					files = new TreeSet();

  //Billing fields
  private Set             billingItems;

  public String getDisplay() {
    if ( this.productOrderNumber != null ) {
      return "Product Order " + this.getNonNullString( getProductOrderNumber() );
    }
    return "Product Order " + this.getNonNullString(getIdProductOrder());
  }

  public String getValue() {
    return this.getNonNullString(getIdProductOrder());
  }


  public Integer getIdProductOrder() {
    return idProductOrder;
  }


  public void setIdProductOrder( Integer idProductOrder ) {
    this.idProductOrder = idProductOrder;
  }


  public Integer getIdAppUser() {
    return idAppUser;
  }


  public void setIdAppUser( Integer idAppUser ) {
    this.idAppUser = idAppUser;
  }



  public AppUser getSubmitter() {
    return submitter;
  }


  public void setSubmitter( AppUser submitter ) {
    this.submitter = submitter;
  }

  public Integer getIdLab() {
    return idLab;
  }


  public void setIdLab( Integer idLab ) {
    this.idLab = idLab;
  }



  public Lab getLab() {
    return lab;
  }

  public void setLab( Lab lab ) {
    this.lab = lab;
  }

  public Integer getIdCoreFacility() {
    return idCoreFacility;
  }

  public void setIdCoreFacility( Integer idCoreFacility ) {
    this.idCoreFacility = idCoreFacility;
  }

  public Date getSubmitDate() {
    return submitDate;
  }


  public void setSubmitDate( Date submitDate ) {
    this.submitDate = submitDate;
  }

  public Integer getIdProductType() {
    return idProductType;
  }

  public void setIdProductType( Integer idProductType ) {
    this.idProductType = idProductType;
  }

  public ProductType getProductType() {
    return productType;
  }

  public void setProductType(ProductType productType) {
    this.productType = productType;
  }

  public String getStatus() {
    boolean isComplete = true;
    boolean isNew = true;

    for (ProductLineItem li : (Set<ProductLineItem>) getProductLineItems()) {
      if ( li.getCodeProductOrderStatus() == null || !li.getCodeProductOrderStatus().equals( ProductOrderStatus.COMPLETED ) ) {
        isComplete = false;
      }
      if ( li.getCodeProductOrderStatus() != null && !li.getCodeProductOrderStatus().equals( ProductOrderStatus.NEW ) ) {
        isNew = false;
      }
    }
    String status = ProductOrderStatus.PENDING;
    if ( isNew ) {
      status = ProductOrderStatus.NEW;
    } else if ( isComplete ) {
      status = ProductOrderStatus.COMPLETED;
    }
    return  status != null ? DictionaryManager.getDisplay("hci.gnomex.model.ProductOrderStatus", status) : "";
  }

  public String getQuoteNumber() {
    return quoteNumber;
  }


  public void setQuoteNumber( String quoteNumber ) {
    this.quoteNumber = quoteNumber;
  }


  public Date getQuoteReceivedDate() {
    return quoteReceivedDate;
  }


  public void setQuoteReceivedDate( Date quoteReceivedDate ) {
    this.quoteReceivedDate = quoteReceivedDate;
  }


  public String getUuid() {
    return uuid;
  }


  public void setUuid( String uuid ) {
    this.uuid = uuid;
  }

  /* Used for file system retrieval of files */
  public String getKey() {
    return getKey(Integer.valueOf(this.getProductOrderNumber()), this.getSubmitDate(), null);  }

  public String getKey(String resultsDir) {
    return ProductOrder.getKey(Integer.valueOf(this.getProductOrderNumber()), this.getSubmitDate(), resultsDir);
  }

  public static String getKey(Integer productOrderNumber,
      java.sql.Date theCreateDate, String resultsDir) {
    if (theCreateDate == null) {
      return "";
    } else {
      String createYear = getCreateYear(theCreateDate);
      String sortDate = new SimpleDateFormat("yyyyMMdd").format(theCreateDate);
      String key = createYear + "-" + sortDate + "-" + productOrderNumber;
      if (resultsDir != null) {
        key += "-" + resultsDir;
      }
      return key;
    }
  }

  public Integer getTargetClassIdentifier() {
	  return idProductOrder;
  }

  public String getTargetClassName() {
	  return this.getClass().getName();
  }



  public Set<ProductLineItem> getProductLineItems() {
    return productLineItems;
  }


  public void setProductLineItems( Set<ProductLineItem> productLineItems ) {
    this.productLineItems = productLineItems;
  }

  public Set getFiles() {
    return files;
  }

  public void setFiles(Set files) {
    this.files = files;
  }

  public Integer getIdBillingAccount() {
    return idBillingAccount;
  }

  public void setIdBillingAccount(Integer idBillingAccount) {
    this.idBillingAccount = idBillingAccount;
  }

  public String getProductOrderNumber() {
    return productOrderNumber;
  }

  
  public void setProductOrderNumber( String productOrderNumber ) {
    this.productOrderNumber = productOrderNumber;
  }
  
  public String getCreateYear() {
    return ProductOrder.getCreateYear(this.getSubmitDate());
  }

  public Boolean addFileToProductOrder (File file, ProductOrder po, Session sess ){
    if(file.exists()) {
      ProductOrderFile poFile = new ProductOrderFile();
      poFile.setIdProductOrder(po.getIdProductOrder());
      poFile.setCreateDate(new Date(System.currentTimeMillis()));
      poFile.setFileName(file.getName());
      poFile.setFileSize(new BigDecimal(file.length()));
      sess.save(poFile);
    } else {
      return false;
    }
    return true;
  }

  public static String getCreateYear(java.util.Date theCreateDate) {
    if (theCreateDate == null) {
      return "";
    } else {
      return new SimpleDateFormat("yyyy").format(theCreateDate);
    }
  }

  public Set getBillingItems() {
    return billingItems;
  }

  public void setBillingItems(Set billingItems) {
    this.billingItems = billingItems;
  }

  @Override
  public Integer getIdProduct() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getCodeApplication() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getCodeRequestCategory() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Integer getIdRequest() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getCodeBioanalyzerChipType() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Set<BillingItem> getBillingItemList(Session sess) {
	BillingTemplate template = BillingTemplateQueryManager.retrieveBillingTemplate(sess, this);
	if (template != null) {
		return BillingItemQueryManager.getBillingItemsForBillingTemplate(sess, template.getIdBillingTemplate());
	} else {
		return new TreeSet<BillingItem>();
	}
  }

  @Override
  public BillingTemplate getBillingTemplate(Session sess) {
	return BillingTemplateQueryManager.retrieveBillingTemplate(sess, this);
  }

  @Override
public Integer getAcceptingBalanceAccountId(Session sess) {
	BillingTemplate billingTemplate = this.getBillingTemplate(sess);
	if (billingTemplate != null) {
		BillingTemplateItem item = billingTemplate.getAcceptingBalanceItem();
		if (item != null) {
			return item.getIdBillingAccount();
		}
	}
	return null;
}

}