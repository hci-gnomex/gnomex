package hci.gnomex.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.NumberFormat;
import java.util.Set;

import hci.hibernate3utils.HibernateDetailObject;


public class BillingAccount extends HibernateDetailObject {
 
  private Integer    idBillingAccount;
  private String     accountName;
  private Date       expirationDate;
  private Integer    idLab;
  private String     accountNumberBus;
  private String     accountNumberOrg;
  private String     accountNumberFund;
  private String     accountNumberActivity;
  private String     accountNumberProject;
  private String     accountNumberAccount;
  private String     accountNumberAu;
  private String     accountNumberYear;
  private Integer    idFundingAgency;
  private String     isPO;
  private String     isApproved;
  private Date       approvedDate;
  private Date       createDate;
  private String     submitterEmail;
  private String     submitterUID;
  private BigDecimal totalDollarAmount;
  
  // transient field used to keep track of billing accounts just approved
  private boolean isJustApproved = false;
  
  // transient field use to keep track of total charges to date
  private BigDecimal totalChargesToDate;
  
  
  public String getAccountNumber() {
    if ((accountNumberBus != null && !accountNumberBus.equals("")) || 
        (accountNumberOrg != null && !accountNumberOrg.equals("")) || 
        (accountNumberFund != null && !accountNumberFund.equals("")) || 
        (accountNumberActivity != null && !accountNumberActivity.equals("")) || 
        (accountNumberProject != null && !accountNumberProject.equals("")) || 
        (accountNumberAccount != null && !accountNumberAccount.equals("")) ||
        (accountNumberAu != null && !accountNumberAu.equals("")) || 
        (accountNumberYear != null && !accountNumberYear.equals(""))) {
      return this.getNonNullString(accountNumberBus) + 
      this.getAccountNumberPart(accountNumberOrg) +
      this.getAccountNumberPart(accountNumberFund) +
      this.getAccountNumberPart(accountNumberActivity) + 
      this.getAccountNumberPart(accountNumberProject) +
      this.getAccountNumberPart(accountNumberAccount + 
      this.getAccountNumberPart(accountNumberAu) + 
      this.getAccountNumberPart(accountNumberYear) );      
    } else {
      return "";
    }
    
  }  
  
  private String getAccountNumberPart(String part) {
    if (part != null && !part.equals("")) {
      return "-" + part;
    } else {
      return "";
    }
  }

  
  public Date getExpirationDate() {
    return expirationDate;
  }
  
  public void setExpirationDate(Date expirationDate) {
    this.expirationDate = expirationDate;
  }
  
  public Integer getIdBillingAccount() {
    return idBillingAccount;
  }
  
  public void setIdBillingAccount(Integer idBillingAccount) {
    this.idBillingAccount = idBillingAccount;
  }
  
  public Integer getIdLab() {
    return idLab;
  }
  
  public void setIdLab(Integer idLab) {
    this.idLab = idLab;
  }
  
  public String getIsActive() {
    Date now = new Date(System.currentTimeMillis());
    // No expiration date - account is always active 
    if (expirationDate == null) {
      return "Y";
    }
    // Expiration date is today or past today - account is not active
    else if (now.compareTo(expirationDate) >= 0) {
      return "N";
    }
    // Expiration date is earlier than today - account is active
    else {
      return "Y";
    }
  }

  
  public String getAccountName() {
    return accountName;
  }

  
  public void setAccountName(String accountName) {
    this.accountName = accountName;
  }

  
  public String getAccountNumberAccount() {
    return accountNumberAccount;
  }

  
  public void setAccountNumberAccount(String accountNumberAccount) {
    this.accountNumberAccount = accountNumberAccount;
  }

  
  public String getAccountNumberActivity() {
    return accountNumberActivity;
  }

  
  public void setAccountNumberActivity(String accountNumberActivity) {
    this.accountNumberActivity = accountNumberActivity;
  }

  
  public String getAccountNumberAu() {
    return accountNumberAu;
  }

  
  public void setAccountNumberAu(String accountNumberAu) {
    this.accountNumberAu = accountNumberAu;
  }

  
  public String getAccountNumberBus() {
    return accountNumberBus;
  }

  
  public void setAccountNumberBus(String accountNumberBus) {
    this.accountNumberBus = accountNumberBus;
  }

  
  public String getAccountNumberFund() {
    return accountNumberFund;
  }

  
  public void setAccountNumberFund(String accountNumberFund) {
    this.accountNumberFund = accountNumberFund;
  }

  
  public String getAccountNumberOrg() {
    return accountNumberOrg;
  }

  
  public void setAccountNumberOrg(String accountNumberOrg) {
    this.accountNumberOrg = accountNumberOrg;
  }

  
  public String getAccountNumberProject() {
    return accountNumberProject;
  }

  
  public void setAccountNumberProject(String accountNumberProject) {
    this.accountNumberProject = accountNumberProject;
  }

  
  public String getAccountNumberYear() {
    return accountNumberYear;
  }

  
  public void setAccountNumberYear(String accountNumberYear) {
    this.accountNumberYear = accountNumberYear;
  }

  public String getExpirationDateOther() {
    return this.formatDate(this.expirationDate, this.DATE_OUTPUT_SLASH); 
  }


  public Integer getIdFundingAgency() {
    return idFundingAgency;
  }

  
  public void setIdFundingAgency(Integer idFundingAgency) {
    this.idFundingAgency = idFundingAgency;
  }

  public String getAccountNameAndNumber() {
    String number = " - " + getAccountNumber();
    if (getAccountNumber().trim().length() == 0) {
      number = "";
    }
    return getAccountName() + number;
  }
 
  private String getActiveDisplay() {
    if (getIsActive().equals("N")) {
      return "(inactive) ";
    } else if (getIsApproved() == null || getIsApproved().equals("") || getIsApproved().equalsIgnoreCase("N")){
      return "(pending) ";
    } else {
      return "";
    }
  }
  
  public String getAccountNameDisplay() {
    String number =  " - " + getAccountNumber();
    if (getAccountNumber().trim().length() == 0) {
      number = "";
    }    return  getActiveDisplay() + getAccountName() + number;
  }
  
  public String getAccountNumberDisplay() {
    String number = getAccountNumber() + " - ";
    if (getAccountNumber().trim().length() == 0) {
      number = "";
    }
    return getActiveDisplay() + number + getAccountName();       
  }

  public String getIsPO() {
	return isPO;
  }

  public void setIsPO(String isPO) {
	this.isPO = isPO;
  }

  
  public String getIsApproved() {
    return isApproved;
  }

  
  public void setIsApproved(String isApproved) {
    this.isApproved = isApproved;
  }

  
  public Date getApprovedDate() {
    return approvedDate;
  }

  
  public void setApprovedDate(Date approvedDate) {
    this.approvedDate = approvedDate;
  }

  
  public Date getCreateDate() {
    return createDate;
  }

  
  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  
  public String getSubmitterEmail() {
    return submitterEmail;
  }

  
  public void setSubmitterEmail(String submitterEmail) {
    this.submitterEmail = submitterEmail;
  }


  public String getSubmitterUID() {
    return submitterUID;
  }

  
  public void setSubmitterUID(String submitterUID) {
    this.submitterUID = submitterUID;
  }
  
  public void isJustApproved(boolean approved) {
    this.isJustApproved = approved;
  }
  
  public boolean isJustApproved() {
    return isJustApproved;
  }

  
  public BigDecimal getTotalDollarAmount() {
    return totalDollarAmount;
  }

  
  public void setTotalDollarAmount(BigDecimal totalDollarAmount) {
    this.totalDollarAmount = totalDollarAmount;
  }

  public String getTotalDollarAmountDisplay() {
    if (totalDollarAmount != null) {
      return NumberFormat.getCurrencyInstance().format(totalDollarAmount);
    } else {
      return "";
    }
  }
  

  public void setTotalChargesToDate(BigDecimal totalChargesToDate) {
    this.totalChargesToDate = totalChargesToDate;
  }
  
  
  public String getTotalChargesToDateDisplay() {
    if (totalChargesToDate != null) {
      return NumberFormat.getCurrencyInstance().format(totalChargesToDate);
    } else {
      return "";
    }
  }

  
  public BigDecimal getTotalDollarAmountRemaining() {
    if (totalDollarAmount != null && totalChargesToDate != null) {
      return totalDollarAmount.subtract(totalChargesToDate);
    } else {
      return null;
    }
  }
  
  public String getTotalDollarAmountRemainingDisplay() {
    if (totalDollarAmount != null && totalChargesToDate != null) {
      return NumberFormat.getCurrencyInstance().format(totalDollarAmount.subtract(totalChargesToDate));
    } else {
      return null;
    }
  }

  
}