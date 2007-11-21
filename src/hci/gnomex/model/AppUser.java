package hci.gnomex.model;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;

import hci.hibernate3utils.HibernateDetailObject;


public class AppUser extends HibernateDetailObject implements Serializable {
  public static final String    MASKED_PASSWORD = "XXXX";
  
  private Integer idAppUser;
  private String  firstName;
  private String  lastName;
  private String  uNID;
  private String  email;
  private String  phone;
  private String  department;
  private String  institute;
  private String  jobTitle;
  private String  codeUserPermissionKind;
  private String  isActive;
  private String  userNameExternal;
  private String  passwordExternal;
  private Set     labs;
  private Set     collaboratingLabs;
  private Set     managingLabs;
  
  public String getDepartment() {
    return department;
  }
  
  public void setDepartment(String department) {
    this.department = department;
  }
  
  public String getEmail() {
    return email;
  }
  
  public void setEmail(String email) {
    this.email = email;
  }
  
  public String getFirstName() {
    return firstName;
  }
  
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }
  
  public Integer getIdAppUser() {
    return idAppUser;
  }
  
  public void setIdAppUser(Integer idAppUser) {
    this.idAppUser = idAppUser;
  }
  
  public String getInstitute() {
    return institute;
  }
  
  public void setInstitute(String institute) {
    this.institute = institute;
  }
  
  public String getIsActive() {
    return isActive;
  }
  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }
  
  public String getJobTitle() {
    return jobTitle;
  }
  
  public void setJobTitle(String jobTitle) {
    this.jobTitle = jobTitle;
  }
  
  public String getLastName() {
    return lastName;
  }
  
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
  
  public String getPhone() {
    return phone;
  }
  
  public void setPhone(String phone) {
    this.phone = phone;
  }
  
  public String getuNID() {
    return uNID;
  }
  
  public void setuNID(String uNID) {
    this.uNID = uNID;
  }
  
  public Set getLabs() {
    return labs;
  }

  
  public void setLabs(Set labs) {
    this.labs = labs;
  }
 

  
  public String getCodeUserPermissionKind() {
    return codeUserPermissionKind;
  }

  
  public void setCodeUserPermissionKind(String codeUserPermissionKind) {
    this.codeUserPermissionKind = codeUserPermissionKind;
  }
  
  public String getIsAdminPermissionLevel() {
    if (codeUserPermissionKind != null && codeUserPermissionKind.equals(UserPermissionKind.ADMIN_PERMISSION_KIND)) {
      return "Y";
    } else {
      return "N";
    }
  }
  
  public String getIsLabPermissionLevel() {
    if (codeUserPermissionKind != null && codeUserPermissionKind.equals(UserPermissionKind.GROUP_PERMISSION_KIND)) {
      return "Y";
    } else {
      return "N";
    }
  }

  
  public String getPasswordExternal() {
    return passwordExternal;
  }

  
  public void setPasswordExternal(String passwordExternal) {
    this.passwordExternal = passwordExternal;
  }

  
  public String getUserNameExternal() {
    return userNameExternal;
  }

  
  public void setUserNameExternal(String userNameExternal) {
    this.userNameExternal = userNameExternal;
  }

  
  public Set getCollaboratingLabs() {
    return collaboratingLabs;
  }

  
  public void setCollaboratingLabs(Set collaboratingLabs) {
    this.collaboratingLabs = collaboratingLabs;
  }

  
  public Set getManagingLabs() {
    return managingLabs;
  }
  
  public String getPasswordExternalEntered() {
    if (this.passwordExternal != null && !this.passwordExternal.equals("")) {
      return MASKED_PASSWORD;      
    } else {
      return "";
    }
  }

  
  public void setManagingLabs(Set managingLabs) {
    this.managingLabs = managingLabs;
  }
  
  public String getIsExternalUser() {
    String isExternal = "N";
    if (uNID == null || uNID.equals("")) {
      if (userNameExternal != null && !userNameExternal.equals("")) {
        isExternal =  "Y";
      }
    }
    return isExternal;
  }
  
  public String getDisplayName() {
    if (getIsExternalUser().equals("Y")) {
      return lastName + ", " + firstName + " (external user)";
    } else {
      return lastName + ", " + firstName;
    }
  }
  
  
}
