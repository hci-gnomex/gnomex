package hci.gnomex.model;

import hci.dictionary.utility.DictionaryManager;
import hci.framework.model.DetailObject;
import hci.gnomex.utility.Order;

import java.io.Serializable;
import java.sql.Date;
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
  private String      codeProductType;
  private String      quoteNumber;
  private Date        quoteReceivedDate;
  private String      uuid;
  private String      productOrderNumber;

  private Set         productLineItems = new TreeSet();  
  private Set         files = new TreeSet();

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

  public String getCodeProductType() {
    return codeProductType;
  }

  public void setCodeProductType( String codeProductType ) {
    this.codeProductType = codeProductType;
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



  public Set getProductLineItems() {
    return productLineItems;
  }


  public void setProductLineItems( Set productLineItems ) {
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


}