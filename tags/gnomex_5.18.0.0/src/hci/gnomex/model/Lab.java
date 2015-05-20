package hci.gnomex.model;

import hci.hibernate3utils.HibernateDetailObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class Lab extends HibernateDetailObject implements java.lang.Comparable {
  private Integer idLab;
  private String  firstName;
  private String  lastName;
  private String  isExternalPricing;
  private String  isExternalPricingCommercial;
  private String  department;
  private String  contactName;
  private String  contactAddress2;
  private String  contactAddress;
  private String  contactCodeState;
  private String  contactCity;
  private String  contactZip;
  private String  contactCountry;
  private String  contactEmail;
  private String  contactPhone;
  private String  isCcsgMember;
  private String  isActive;
  private String  excludeUsage;
  private String  billingContactEmail;  private Long    version;
  private Set     billingAccounts;
  private Set     members;
  private Set     collaborators;
  private Set     managers;
  private Set     projects;
  private Set     institutions;
  private Set     coreFacilities;

  // Permission flag
  private boolean canSubmitRequests = false;
  private boolean canManage = false;
  private boolean isMyLab = false;
  
  
  public String getContactAddress() {
    return contactAddress;
  }
  
  public void setContactAddress(String contactAddress) {
    this.contactAddress = contactAddress;
  }
  
  public String getContactCity() {
    return contactCity;
  }
  
  public void setContactCity(String contactCity) {
    this.contactCity = contactCity;
  }
  
  public String getContactCodeState() {
    return contactCodeState;
  }
  
  public void setContactCodeState(String contactCodeState) {
    this.contactCodeState = contactCodeState;
  }
  
  public String getContactEmail() {
    return contactEmail;
  }
  
  public void setContactEmail(String contactEmail) {
    this.contactEmail = contactEmail;
  }
  
  public String getContactName() {
    return contactName;
  }
  
  public void setContactName(String contactName) {
    this.contactName = contactName;
  }
  
  public String getContactAddress2() {
    return contactAddress2;
  }
  
  public void setContactAddress2(String contactAddress2) {
    this.contactAddress2 = contactAddress2;
  }
  
  public String getContactPhone() {
    return contactPhone;
  }
  
  public void setContactPhone(String contactPhone) {
    this.contactPhone = contactPhone;
  }
  
  public String getContactZip() {
    return contactZip;
  }
  
  public void setContactZip(String contactZip) {
    this.contactZip = contactZip;
  }
  
  public String getContactCountry() {
	    return contactCountry;
	  }
	  
	  public void setContactCountry(String contactCountry) {
	    this.contactCountry = contactCountry;
	  }
  
  public String getDepartment() {
    return department;
  }
  
  public void setDepartment(String department) {
    this.department = department;
  }
  
  public Integer getIdLab() {
    return idLab;
  }
  
  public void setIdLab(Integer idLab) {
    this.idLab = idLab;
  }
  
  
  public String getBillingContactEmail() {
    return billingContactEmail;
  }
  
  public void setBillingContactEmail(String billingContactEmail) {
    this.billingContactEmail = billingContactEmail;
  }
  

  public Long getVersion() {
    return version;
  }
  
  public void setVersion(Long version) {
    this.version = version;
  }
  
  public String getName() {
    String name = "";
    if (lastName != null && !lastName.equals("")) {
      name = lastName;
    }
    if (firstName != null && !firstName.equals("")) {
      if (name.length() > 0) {
        name += ", ";
      }
      name += firstName;
    }
    if (name.length() > 0) {
      name += " Lab";      
    }
    if (this.getIsActive() != null && this.getIsActive().equalsIgnoreCase("N")) {
      name += " (inactive)";
    }
    return name;
  }


 
  
  public void registerMethodsToExcludeFromXML() {
    this.excludeMethodFromXML("getAppUsers");
  }

  
  public Set getBillingAccounts() {
    return billingAccounts;
  }

  
  public void setBillingAccounts(Set billingAccounts) {
    this.billingAccounts = billingAccounts;
  }

  
  public Set getMembers() {
    return members;
  }

  
  public void setMembers(Set members) {
    this.members = members;
  }

  
  public Set getCollaborators() {
    return collaborators;
  }

  
  public void setCollaborators(Set collaborators) {
    this.collaborators = collaborators;
  }

  
  public Set getManagers() {
    return managers;
  }

  
  public void setManagers(Set managers) {
    this.managers = managers;
  }
  
  public String getCanSubmitRequests() {
    if (this.canSubmitRequests) {
      return "Y";
    } else {
      return "N";
    }
  }
  public void canSubmitRequests(boolean canDo) {
    canSubmitRequests = canDo;
  }
  
  public String getCanManage() {
    if (this.canManage) {
      return "Y";
    } else {
      return "N";
    }
  }
  public void canManage(boolean canDo) {
    canManage = canDo;
  }

  
  public String getIsMyLab() {
    if (isMyLab) {
      return "Y";
    } else {
      return "N";
    }
  }

  
  public void isMyLab(boolean isMyLab) {
    this.isMyLab = isMyLab;
  }

 
  
  public String getIsCcsgMember() {
    return isCcsgMember;
  }

  
  public void setIsCcsgMember(String isCcsgMember) {
    this.isCcsgMember = isCcsgMember;
  }

  
  public String getFirstName() {
    return firstName;
  }

  
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  
  public String getLastName() {
    return lastName;
  }

  
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getFormattedLabName() {
    return formatLabName(getLastName(), getFirstName());
  }

  public static String formatLabName(String lastName, String firstName) {
    
    String labName = "";
    if (lastName != null && !lastName.equals("")) {
      labName = lastName;
    }
    if (firstName != null && !firstName.equals("")) {
      if (labName.length() > 0) {
        labName += ", ";
      }
      labName += firstName;
    }
    if (labName.length() > 0) {
      labName += " Lab";
    }
    return labName;
  }

  
  public Set getProjects() {
    return projects;
  }

  
  public void setProjects(Set projects) {
    this.projects = projects;
  }

  
  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }

  
  public String getIsExternalPricing() {
    return isExternalPricing;
  }

  
  public void setIsExternalPricing(String isExternalPricing) {
    this.isExternalPricing = isExternalPricing;
  }
  
  
  public String getIsExternalPricingCommercial() {
    return isExternalPricingCommercial;
  }

  
  public void setIsExternalPricingCommercial(String isExternalPricingCommercial) {
    this.isExternalPricingCommercial = isExternalPricingCommercial;
  }
  
  public Set getInstitutions() {
    return institutions;
  }

  public void setInstitutions(Set institutions) {
    this.institutions = institutions;
  }  
  
  public Set getCoreFacilities() {
    return coreFacilities;
  }

  public void setCoreFacilities(Set coreFacilities) {
    this.coreFacilities = coreFacilities;
  }  
  
  public Boolean isExternalLab() {
    return (getIsExternalPricing() != null && getIsExternalPricing().equals("Y"))
    || (getIsExternalPricingCommercial() != null && getIsExternalPricingCommercial().equals("Y"));
  }
  
  public List getApprovedBillingAccounts() {
    ArrayList approvedBillingAccounts = new ArrayList();
    for(Iterator i = getBillingAccounts().iterator(); i.hasNext();) {
      BillingAccount ba = (BillingAccount)i.next();
      if (ba.getIsApproved() != null && ba.getIsApproved().equalsIgnoreCase("Y")) {
        approvedBillingAccounts.add(ba);
      }
    }
    return approvedBillingAccounts;
  }
  public List getInternalBillingAccounts() {
    ArrayList internalBillingAccounts = new ArrayList();
    for(Iterator i = getBillingAccounts().iterator(); i.hasNext();) {
      BillingAccount ba = (BillingAccount)i.next();
      if ((ba.getIsPO() == null || ba.getIsPO().equals("N")) &&
          (ba.getIsCreditCard() == null || ba.getIsCreditCard().equals("N"))) {
        internalBillingAccounts.add(ba);
      }
    }
    return internalBillingAccounts;
  }
  
  public List getPOBillingAccounts() {
    ArrayList poBillingAccounts = new ArrayList();
    for(Iterator i = getBillingAccounts().iterator(); i.hasNext();) {
      BillingAccount ba = (BillingAccount)i.next();
      if (ba.getIsPO() != null && 
          ba.getIsPO().equals("Y") ) {
        poBillingAccounts.add(ba);
      }
    }
    return poBillingAccounts;
  }
  public List getCreditCardBillingAccounts() {
    ArrayList creditCardBillingAccounts = new ArrayList();
    for(Iterator i = getBillingAccounts().iterator(); i.hasNext();) {
      BillingAccount ba = (BillingAccount)i.next();
      if (ba.getIsCreditCard() != null && 
          ba.getIsCreditCard().equals("Y") ) {
        creditCardBillingAccounts.add(ba);
      }
    }
    return creditCardBillingAccounts;
  }
  public String getExcludeUsage() {
    return excludeUsage;
  }

  public void setExcludeUsage(String excludeUsage) {
    this.excludeUsage = excludeUsage;
  }

  // Includes PI email if requested.
  public String getBillingNotificationEmail() {
    return formatBillingNotificationEmail(getContactEmail(), getBillingContactEmail());
  }
  
  // Always inclused PI
  public String getWorkAuthSubmitEmail() {
    return formatBillingNotificationEmail(getContactEmail(), getBillingContactEmail());
  }
  
  public static String formatBillingNotificationEmail(String contactEmail, String billingContactEmail) {
    String email = "";
    if (billingContactEmail != null) {
      email = billingContactEmail;
    }
    String piEmail = "";
    if (contactEmail != null) {
      piEmail = contactEmail;
    }
    if (piEmail.length() > 0) {
      if (email.length() > 0) {
        email += ",";
      }
      email += piEmail;
    }
    
    return email;
  }
  
  public Boolean validateVisibilityInLab(VisibilityInterface object) {
    Boolean valid = true;
    if (object != null && object.getCodeVisibility().equals(Visibility.VISIBLE_TO_INSTITUTION_MEMBERS)) {
      if (object.getIdInstitution() == null) {
        valid = false;
        Integer inst = getDefaultIdInstitutionForLab();
        if (inst != null) {
          valid = true;
          object.setIdInstitution(inst);
        }
      }
    }

    return valid;
  }
  
  public Integer getDefaultIdInstitutionForLab() {
    return getDefaultIdInstitutionForLab((Set<Institution>)getInstitutions());
  }
  
  public Integer getDefaultIdInstitutionForLab(Collection<Institution> institutions) {
    Integer defaultInst = null;
    Integer onlyInst = null;
    Integer numActiveInstitutions = 0;
    for(Institution inst : institutions) {
      if (inst.getIsActive() != null && inst.getIsActive().equals("Y")) {
        numActiveInstitutions++;
        if (numActiveInstitutions.equals(1)) {
          onlyInst = inst.getIdInstitution();
        } else {
          onlyInst = null;
        }
        if (inst.getIsDefault() != null && inst.getIsDefault().equals("Y")) {
          defaultInst = inst.getIdInstitution();
          break;
        }
      }
    }
    
    if (defaultInst == null && onlyInst != null) {
      defaultInst = onlyInst;
    }
    
    return defaultInst;
  }

  @Override
  public int compareTo(Object o) {
    if (o instanceof Lab) {
      Lab ol = (Lab)o;
      if (ol.getIdLab() == null && this.getIdLab() == null) {
        return 0;
      } else if (ol.getIdLab() == null) {
        return 1;
      } else if (this.getIdLab() == null) {
        return -1;
      } else {
        return this.getIdLab().compareTo(ol.getIdLab());
      }
    } else {
      return 1;
    }
  }
}
