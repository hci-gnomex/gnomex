package hci.gnomex.model;

import hci.hibernate3utils.HibernateDetailObject;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.NumberFormat;
import java.util.Set;


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
  private Integer    idCreditCardCompany;
  private String     isPO;
  private String     isCreditCard;
  private String     zipCode;
  private String     isApproved;
  private Date       approvedDate;
  private Date       createDate;
  private String     submitterEmail;
  private String     submitterUID;
  private BigDecimal totalDollarAmount;
  private String     orderFormFileType;
  private byte []    purchaseOrderForm;
  private Long       orderFormFileSize;
  private Date       startDate;
  private String     shortAcct;
  private Integer    idCoreFacility;
  private Set        users;
  private String     custom1;
  private String     custom2;
  private String     custom3;
  private String     approverEmail;
  private Integer    idApprover;

  public byte [] getPurchaseOrderForm() {
    return purchaseOrderForm;
  }

  public void setPurchaseOrderForm(byte [] purchaseOrderForm) {
    this.purchaseOrderForm = purchaseOrderForm;
  }

  public String getOrderFormFileType() {
    return orderFormFileType;
  }

  public void setOrderFormFileType(String orderFormFileType) {
    this.orderFormFileType = orderFormFileType;
  }

  // transient field used to keep track of billing accounts just approved
  private boolean isJustApproved = false;

  // transient field use to keep track of total charges to date
  private BigDecimal totalChargesToDate;


  public String getAccountNumber() {
    if (InternalAccountFieldsConfiguration.getUseConfigurableBillingAccounts(null)) {
      String[] parts = new String[10];
      for(int i = 0; i < 10; i++) {
        parts[i] = "";
      }
      for(InternalAccountFieldsConfiguration conf:InternalAccountFieldsConfiguration.getConfiguration(null)) {
        if (conf.getInclude() != null && conf.getInclude().equals("Y")) {
          int order = 0;
          if (conf.getSortOrder() != null && conf.getSortOrder() >= 0 && conf.getSortOrder() < 10) {
            order = conf.getSortOrder();
          }
          if (conf.getFieldName().equals(InternalAccountFieldsConfiguration.ACCOUNT)) {
            parts[order] = getNonNullString(accountNumberAccount);
          } else if (conf.getFieldName().equals(InternalAccountFieldsConfiguration.PROJECT)) {
            parts[order] = getNonNullString(accountNumberProject);
          } else if (conf.getFieldName().equals(InternalAccountFieldsConfiguration.CUSTOM_1)) {
            parts[order] = getNonNullString(custom1);
          } else if (conf.getFieldName().equals(InternalAccountFieldsConfiguration.CUSTOM_2)) {
            parts[order] = getNonNullString(custom2);
          } else if (conf.getFieldName().equals(InternalAccountFieldsConfiguration.CUSTOM_3)) {
            parts[order] = getNonNullString(custom3);
          }
        }
      }

      String accountNumber = "";
      for(int i = 0; i < 10; i++) {
        if (parts[i].length() > 0) {
          if (accountNumber.length() > 0) {
            accountNumber += "-";
          }
          accountNumber += parts[i];
        }
      }
      return accountNumber;

    }
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
    }
    return "";    
  }  

  private String getAccountNumberPart(String part) {
    if (part != null && !part.equals("")) {
      return "-" + part;
    }
    return "";
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
    if (expirationDate == null && startDate == null) {
      return "Y";
    }
    // Expiration date is today or past today - account is not active
    else if (expirationDate != null && now.compareTo(expirationDate) >= 0) {
      return "N";
    }
    // start date is after today.  account is not active.
    else if (startDate != null && now.compareTo(startDate) < 0) {
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

  public String getStartDateOther() {
    return this.formatDate(this.startDate, this.DATE_OUTPUT_SLASH);
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

  public BillingAccount getCopy(Integer newIdCoreFacility) {
    BillingAccount newBA = new BillingAccount();
    newBA.setAccountName(getAccountName());
    newBA.setAccountNumberAccount(getAccountNumberAccount());
    newBA.setAccountNumberActivity(getAccountNumberActivity());
    newBA.setAccountNumberAu(getAccountNumberAu());
    newBA.setAccountNumberBus(getAccountNumberBus());
    newBA.setAccountNumberFund(getAccountNumberFund());
    newBA.setAccountNumberOrg(getAccountNumberOrg());
    newBA.setAccountNumberProject(getAccountNumberProject());
    newBA.setAccountNumberYear(getAccountNumberYear());
    newBA.setApprovedDate(getApprovedDate());
    newBA.setCreateDate(getCreateDate());
    newBA.setCustom1(getCustom1());
    newBA.setCustom2(getCustom2());
    newBA.setCustom3(getCustom3());
    newBA.setExpirationDate(getExpirationDate());
    newBA.setIdCreditCardCompany(getIdCreditCardCompany());
    newBA.setIdFundingAgency(getIdFundingAgency());
    newBA.setIdLab(getIdLab());
    newBA.setIsApproved(getIsApproved());
    newBA.setIsCreditCard(getIsCreditCard());
    newBA.setIsPO(getIsPO());
    newBA.setOrderFormFileSize(getOrderFormFileSize());
    newBA.setOrderFormFileType(getOrderFormFileType());
    newBA.setPurchaseOrderForm(getPurchaseOrderForm());
    newBA.setShortAcct(getShortAcct());
    newBA.setStartDate(getStartDate());
    newBA.setSubmitterEmail(getSubmitterEmail());
    newBA.setSubmitterUID(getSubmitterUID());
    newBA.setTotalDollarAmount(getTotalDollarAmount());
    newBA.setTotalChargesToDate(totalChargesToDate);
    newBA.setZipCode(getZipCode());

    newBA.setIdCoreFacility(newIdCoreFacility);

    return newBA;
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
    }
    return "";
  }


  public void setTotalChargesToDate(BigDecimal totalChargesToDate) {
    this.totalChargesToDate = totalChargesToDate;
  }


  public String getTotalChargesToDateDisplay() {
    if (totalChargesToDate != null) {
      return NumberFormat.getCurrencyInstance().format(totalChargesToDate);
    }
    return "";
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date date) {
    startDate = date;
  }

  public String getShortAcct() {
    return shortAcct;
  }

  public void setShortAcct(String acct) {
    shortAcct = acct;
  }

  public Integer getIdCoreFacility() {
    return idCoreFacility;
  }

  public void setIdCoreFacility(Integer id) {
    idCoreFacility = id;
  }

  public Set getUsers() {
    return users;
  }

  public void setUsers(Set users) {
    this.users = users;
  }

  public String getCustom1() {
    return custom1;
  }
  public void setCustom1(String custom1) {
    this.custom1 = custom1;
  }

  public String getCustom2() {
    return custom2;
  }
  public void setCustom2(String custom2) {
    this.custom2 = custom2;
  }

  public String getCustom3() {
    return custom3;
  }
  public void setCustom3(String custom3) {
    this.custom3 = custom3;
  }

  public BigDecimal getTotalDollarAmountRemaining() {
    if (totalDollarAmount != null && totalChargesToDate != null) {
      return totalDollarAmount.subtract(totalChargesToDate);
    }
    return null;
  }

  public String getTotalDollarAmountRemainingDisplay() {
    if (totalDollarAmount != null && totalChargesToDate != null) {
      return NumberFormat.getCurrencyInstance().format(totalDollarAmount.subtract(totalChargesToDate));
    }
    return null;
  }

  public void registerMethodsToExcludeFromXML() {
    this.excludeMethodFromXML("getExcludedMethodsMap");
    this.excludeMethodFromXML("getUsers");
  }


  public String getIsCreditCard() {
    return isCreditCard;
  }

  public void setIsCreditCard(String isCreditCard) {
    this.isCreditCard = isCreditCard;
  }

  public Integer getIdCreditCardCompany() {
    return idCreditCardCompany;
  }

  public void setIdCreditCardCompany(Integer idCreditCardCompany) {
    this.idCreditCardCompany = idCreditCardCompany;
  }


  public String getZipCode() {
    return zipCode;
  }

  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }

  public Long getOrderFormFileSize() {
    return orderFormFileSize;
  }

  public void setOrderFormFileSize(Long orderFormFileSize) {
    this.orderFormFileSize = orderFormFileSize;
  }

  public String getApproverEmail() {
    return approverEmail;
  }

  public void setApproverEmail(String approverEmail) {
    this.approverEmail = approverEmail;
  }

  public Integer getIdApprover() {
    return idApprover;
  }

  public void setIdApprover(Integer idApprover) {
    this.idApprover = idApprover;
  }


}