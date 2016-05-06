package hci.gnomex.model;

import hci.dictionary.utility.DictionaryManager;
import hci.framework.model.DetailObject;
import hci.gnomex.constants.Constants;
import hci.gnomex.utility.*;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.hibernate.Session;

import javax.mail.MessagingException;
import javax.naming.NamingException;


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
    String createDate = this.formatDate(this.getSubmitDate());
    String tokens[] = createDate.split("/");
    String createMonth = tokens[0];
    String createDay = tokens[1];
    String createYear = tokens[2];
    String sortDate = createYear + createMonth + createDay;
    String key = createYear + "-" + sortDate + "-" + this.getIdProductOrder();
    return key;
  }

  public String getKey(String resultsDir) {
    return ProductOrder.getKey(this.getIdProductOrder(), this.getSubmitDate(), resultsDir);
  }

  public static String getKey(Integer idProductOrder,
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
      String key = createYear + "-" + sortDate + "-" + idProductOrder + "-"
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
      String createDate  = new SimpleDateFormat("MM/dd/yyyy").format(theCreateDate);
      String tokens[] = createDate.split("/");
      String createYear  = tokens[2];
      return createYear;
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

  public static Boolean sendPurchasingEmail(Session sess, ProductOrder po, String serverName) throws NamingException, MessagingException, IOException {

    //workaround until NullPointer exception is dealt with
    InternalAccountFieldsConfiguration.getConfiguration(sess);

    DictionaryHelper dictionaryHelper = DictionaryHelper.getInstance(sess);
    Lab lab = sess.get( Lab.class, po.getIdLab() );
    BillingAccount acct = sess.get( BillingAccount.class, po.getAcceptingBalanceAccountId(sess) );
    AppUser user = sess.get(AppUser.class, po.getIdAppUser());

    if ( po == null || po.getProductLineItems().size() == 0 || acct == null ) {
      throw new MessagingException( "Product Order or Account is null" );
    }

    String labName = lab.getName(false, true)!=null ? lab.getName(false, true) : "";
    String submitterName = user.getFirstLastDisplayName()!=null ? user.getFirstLastDisplayName() : "";
    String acctName = acct.getAccountName() != null ? acct.getAccountName() : "";
    String acctNumber = acct.getAccountNumber() != null ? acct.getAccountNumber() : "";
    String quoteNum = po.getQuoteNumber() != null ? po.getQuoteNumber() : "";

    BigDecimal grandTotal = new BigDecimal( BigInteger.ZERO, 2 ) ;

    // Email to purchasing department
    StringBuffer emailBody = new StringBuffer();

    emailBody.append("A purchasing request for products has been submitted by the " +
            labName +
            ".");

    // Email for the lab PI and Billing contact requesting signature on req form
    StringBuffer emailBodyForLab = new StringBuffer();

    emailBodyForLab.append("A purchasing request for " + po.getProductType().getDisplay() +
            " has been submitted by " +
            submitterName +
            ".");

    emailBody.append("<br><br><table border='0' width = '400'><tr><td>Lab:</td><td>" + labName );
    emailBody.append("</td></tr><tr><td>Account Name:</td><td>" + acctName );
    emailBody.append("</td></tr><tr><td>Account Number:</td><td>" + acctNumber );
    emailBody.append("</td></tr></table>");

    emailBodyForLab.append("<br><br><table border='0' width = '400'><tr><td>Lab:</td><td>" + labName );
    emailBodyForLab.append("</td></tr><tr><td>Account Name:</td><td>" + acctName );
    emailBodyForLab.append("</td></tr><tr><td>Account Number:</td><td>" + acctNumber );
    emailBodyForLab.append("</td></tr></table>");

    for(Iterator i = po.getProductLineItems().iterator(); i.hasNext();) {
      ProductLineItem lineItem = (ProductLineItem) i.next();
      Product product = sess.load(Product.class, lineItem.getIdProduct());

      String numberProducts = lineItem.getQty() != null ? lineItem.getQty().toString() : "";
      String productName = product.getName() != null ? product.getName() : "";
      String catNumber = product.getCatalogNumber() != null ? product.getCatalogNumber() : "";

      BigDecimal estimatedCost = new BigDecimal( BigInteger.ZERO, 2 ) ;
      estimatedCost = lineItem.getUnitPrice().multiply(new BigDecimal(lineItem.getQty()));

      grandTotal = grandTotal.add(estimatedCost);

      emailBody.append("<br><table border='0' width = '400'>");
      emailBody.append("<tr><td>Product Name:</td><td>" + productName );
      emailBody.append("</td></tr><tr><td>Catalog Number:</td><td>" + catNumber );
      emailBody.append("</td></tr><tr><td>Number Requested:</td><td>" + numberProducts );
      emailBody.append("</td></tr><tr><td>Total Estimated Charges:</td><td>" + "$" + estimatedCost);
      emailBody.append("</td></tr><tr><td>Quote Number:</td><td>" + quoteNum);

      emailBody.append("</td></tr></table>");

      emailBodyForLab.append("<br><table border='0' width = '400'>");
      emailBodyForLab.append("<tr><td>Product Name:</td><td>" + productName );
      emailBodyForLab.append("</td></tr><tr><td>Catalog Number:</td><td>" + catNumber );
      emailBodyForLab.append("</td></tr><tr><td>Number Requested:</td><td>" + numberProducts );
      emailBodyForLab.append("</td></tr><tr><td>Total Estimated Charges:</td><td>" + "$" + estimatedCost);
      emailBodyForLab.append("</td></tr><tr><td>Quote Number:</td><td>" + quoteNum);

      emailBodyForLab.append("</td></tr></table>");
    }

    emailBody.append("<br>Grand Total:  $" + grandTotal);
    emailBodyForLab.append("<br>Grand Total:  $" + grandTotal);

    emailBodyForLab.append("<br><br><b><FONT COLOR=\"#ff0000\">Please sign the attached requisition form and send it to the purchasing department.</FONT></b>");

    String subject = "Purchasing Request for " + po.getProductType().getDisplay();

    String contactEmailCoreFacility = sess.load(CoreFacility.class, po.getIdCoreFacility()).getContactEmail();
    String contactEmailPurchasing  = PropertyDictionaryHelper.getInstance(sess).getCoreFacilityProperty(po.getIdCoreFacility(), PropertyDictionary.CONTACT_EMAIL_PURCHASING);
    String contactEmailLabBilling = lab.getBillingContactEmail() != null ? lab.getBillingContactEmail() : "";
    String contactEmailLabPI = lab.getContactEmail() != null ? lab.getContactEmail() : "";

    String senderEmail = contactEmailCoreFacility;
    String contactEmail = contactEmailPurchasing;
    String ccEmail = null;

    boolean send = true;

    // Check that purchasing email is valid
    if(!MailUtil.isValidEmail(contactEmail)){
      System.out.println("Invalid email address: " + contactEmail);
    }

    // Find requisition file
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
    String createYear = formatter.format(po.getSubmitDate());
    String baseDir = PropertyDictionaryHelper.getInstance(sess).getDirectory(serverName, po.getIdCoreFacility(), PropertyDictionaryHelper.PROPERTY_PRODUCT_ORDER_DIRECTORY);
    baseDir +=  "/" + createYear;
    baseDir = baseDir + "/" + po.getIdProductOrder();
    String directoryName = baseDir + "/" + Constants.REQUISITION_DIR;
    File reqFolder = new File(directoryName);
    if ( !reqFolder.exists() ) {
      send = false;
    }

    // Send email to purchasing
    if (send) {
      if(!MailUtil.isValidEmail(senderEmail)){
        senderEmail = DictionaryHelper.getInstance(sess).getPropertyDictionary(PropertyDictionary.GENERIC_NO_REPLY_EMAIL);
      }
      MailUtilHelper helper = new MailUtilHelper(contactEmail, ccEmail, null, senderEmail, subject, emailBody.toString(), new File(baseDir), true, dictionaryHelper, serverName);
      MailUtil.validateAndSendEmail(helper);
    }

    // Now send the email to lab billing contact and PI
    contactEmail = contactEmailLabPI;
    ccEmail = contactEmailLabBilling;

    // Check billing contact email
    if(!MailUtil.isValidEmail(contactEmail)){
      System.out.println("Invalid email address for lab: " + contactEmail);
    }

    // Check PI email
    if(!MailUtil.isValidEmail(ccEmail)){
      ccEmail = null;
    }

    if ( contactEmail.equals("") && ccEmail != null ) {
      contactEmail = ccEmail;
      ccEmail = null;
    }
    if (send && !contactEmail.equals( "" )) {
      MailUtilHelper helper = new MailUtilHelper(contactEmail, ccEmail, null, senderEmail, subject, emailBodyForLab.toString(), reqFolder, true, dictionaryHelper, serverName);
      MailUtil.validateAndSendEmail(helper);
    }

    return send;
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