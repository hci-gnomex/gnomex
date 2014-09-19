package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;

import java.io.Serializable;
import java.sql.Date;
import java.util.Set;
import java.util.TreeSet;



public class ProductOrder extends DictionaryEntry implements Serializable {

  private Integer     idProductOrder;
  private Integer     idAppUser;
  private Integer     idLab;
  private Integer     idCoreFacility;
  private Integer     idBillingAccount;
  private Date        submitDate;
  private String      codeProductType;
  private String      codeProductOrderStatus;
  private String      quoteNumber;
  private Date        quoteReceivedDate;
  private String      uuid;

  private Set         productLineItems = new TreeSet();
  private Set         billingItems = new TreeSet();
  private Set         files = new TreeSet();

  public String getDisplay() {
    String display = this.getNonNullString(getIdProductOrder());
    return display;
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


  public Integer getIdLab() {
    return idLab;
  }


  public void setIdLab( Integer idLab ) {
    this.idLab = idLab;
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


  public String getCodeProductOrderStatus() {
    return codeProductOrderStatus;
  }


  public void setCodeProductOrderStatus( String codeProductOrderStatus ) {
    this.codeProductOrderStatus = codeProductOrderStatus;
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

  public Set getBillingItems() {
    return billingItems;
  }

  public void setBillingItems(Set billingItems) {
    this.billingItems = billingItems;
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


}