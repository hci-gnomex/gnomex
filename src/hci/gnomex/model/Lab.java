package hci.gnomex.model;

import java.util.Set;

import hci.hibernate3utils.HibernateDetailObject;


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
  private Set     billingAccounts;
  private Set     members;
  private Set     collaborators;
  private Set     managers;
  private Set     projects;
  
  // Permission flag
  private boolean canSubmitRequests = false;
  private boolean canManage = false;
  private boolean isMyLab = false;
  private boolean hasPublicData = false;
  
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

  
  public String getHasPublicData() {
    if (hasPublicData) {
      return "Y";
    } else {
      return "N";
    }
  }

  
  public void setHasPublicData(boolean hasPublicData) {
    this.hasPublicData = hasPublicData;
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
}
