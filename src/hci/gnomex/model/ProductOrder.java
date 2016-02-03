package hci.gnomex.model;

import hci.dictionary.utility.DictionaryManager;
import hci.framework.model.DetailObject;
import hci.gnomex.utility.BillingTemplateQueryManager;
import hci.gnomex.utility.Order;

import java.io.Serializable;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Set;
import java.util.TreeSet;

import org.hibernate.Session;



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
  private String      quoteNumber;
  private Date        quoteReceivedDate;
  private String      uuid;
  private String      productOrderNumber;

  private Set<ProductLineItem>         	productLineItems = new TreeSet<ProductLineItem>();
  private Set         					billingItems = new TreeSet();
  private Set         					files = new TreeSet();

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
    String createDate = this.formatDate(this.getSubmitDate());
    String tokens[] = createDate.split("/");
    String createMonth = tokens[0];
    String createDay = tokens[1];
    String createYear = tokens[2];
    String sortDate = createYear + createMonth + createDay;
    String key = createYear + "-" + sortDate + "-" + this.getProductOrderNumber();
    return key;
  }

  public String getKey(String resultsDir) {
    return ProductOrder.getKey(this.getProductOrderNumber(), this.getSubmitDate(), resultsDir);
  }

  public static String getKey(String analysisNumber,
      java.sql.Date theCreateDate, String resultsDir) {
    if (theCreateDate == null) {
      return "";
    } else {
      String createDate = new SimpleDateFormat("MM/dd/yyyy")
          .format(theCreateDate);
      String tokens[] = createDate.split("/");
      String createMonth = tokens[0];
      String createDay = tokens[1];
      String createYear = tokens[2];
      String sortDate = createYear + createMonth + createDay;
      String key = createYear + "-" + sortDate + "-" + analysisNumber + "-"
          + resultsDir;
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

  public Set getBillingItems() {
    return billingItems;
  }

  public void setBillingItems(Set billingItems) {
    this.billingItems = billingItems;
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
  
  public static String getCreateYear(java.util.Date theCreateDate) {
    if (theCreateDate == null) {
      return "";
    } else {
      String createDate  = new SimpleDateFormat("MM/dd/yyyy").format(theCreateDate);
      String tokens[] = createDate.split("/");
      String createYear  = tokens[2];
      return createYear;
    }
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

@SuppressWarnings("unchecked")
@Override
public Set<BillingItem> getBillingItems(Session sess) {
	return (Set<BillingItem>) billingItems;
}

@Override
public BillingTemplate getBillingTemplate(Session sess) {
	return BillingTemplateQueryManager.retrieveBillingTemplate(sess, this);
}


}