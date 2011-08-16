package hci.gnomex.model;

import hci.hibernate3utils.HibernateDetailObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class Lab extends HibernateDetailObject {
  private Integer idLab;
  private String  firstName;
  private String  lastName;
  private String  isExternalPricing;
  private String  department;
  private String  contactName;
  private String  contactAddress;
  private String  contactCodeState;
  private String  contactCity;
  private String  contactZip;
  private String  contactEmail;
  private String  contactPhone;
  private String  isCcsgMember;
  private String  isActive;
  private String  excludeUsage;
  private Set     billingAccounts;
  private Set     members;
  private Set     collaborators;
  private Set     managers;
  private Set     projects;
  private Set     institutions;

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
  
  public Set getInstitutions() {
    return institutions;
  }

  public void setInstitutions(Set institutions) {
    this.institutions = institutions;
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
  
  public List getPendingBillingAccounts() {
    ArrayList pendingBillingAccounts = new ArrayList();
    for(Iterator i = getBillingAccounts().iterator(); i.hasNext();) {
      BillingAccount ba = (BillingAccount)i.next();
      if (ba.getIsApproved() == null || 
          ba.getIsApproved().equals("") ||
          ba.getIsApproved().equalsIgnoreCase("N")) {
        pendingBillingAccounts.add(ba);
      }
    }
    return pendingBillingAccounts;
  }

  public String getExcludeUsage() {
    return excludeUsage;
  }

  public void setExcludeUsage(String excludeUsage) {
    this.excludeUsage = excludeUsage;
  }

}
