package hci.gnomex.model;

import hci.gnomex.constants.Constants;
import hci.hibernate3utils.HibernateDetailObject;

import java.sql.Date;

public class Invoice extends HibernateDetailObject {
  
  private Integer        idInvoice;
  private Integer        idCoreFacility;
  private Integer        idBillingPeriod;
  private Integer        idBillingAccount;
  private String         invoiceNumber;
  private Date           lastEmailDate;

  public Integer getIdInvoice() {
    return idInvoice;
  }
  public void setIdInvoice(Integer id) {
    idInvoice = id;
  }
  
  public Integer getIdCoreFacility() {
    return idCoreFacility;
  }
  public void setIdCoreFacility(Integer id) {
    idCoreFacility = id;
  }
  
  public Integer getIdBillingPeriod() {
    return idBillingPeriod;
  }
  public void setIdBillingPeriod(Integer id) {
    idBillingPeriod = id;
  }
  
  public Integer getIdBillingAccount() {
    return idBillingAccount;
  }
  public void setIdBillingAccount(Integer id) {
    idBillingAccount = id;
  }
  
  public String getInvoiceNumber() {
    return invoiceNumber;
  }
  public void setInvoiceNumber(String number) {
    invoiceNumber = number;
  }
  
  public Date getLastEmailDate() {
    return lastEmailDate;
  }
  public void setLastEmailDate(Date date) {
    lastEmailDate = date;
  }
}
