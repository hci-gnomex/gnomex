package hci.gnomex.model;

import hci.dictionary.model.NullDictionaryEntry;
import hci.gnomex.utility.XMLTools;
import hci.hibernate5utils.HibernateDetailObject;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;


public class AppUser extends HibernateDetailObject implements Serializable, Comparable {
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
  private String  ucscUrl;
  private String  salt;
  private String  guid;
  private String  confirmEmailGuid;
  private Timestamp  guidExpiration;
  private String  passwordExpired;
  private Set     labs;
  private Set     collaboratingLabs;
  private Set     managingLabs;
  private Set     managingCoreFacilities;
  private Set     coreFacilitiesICanSubmitTo;
  
  @Override
  public boolean equals(Object obj) {
	  if (obj != null && obj instanceof AppUser) {
		  return this.idAppUser.equals(((AppUser) obj).idAppUser);
	  }
	  
	  return false;
  }

  public static String formatName(String lastName, String firstName) {

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

    return name;
  }

  public static String formatNameFirstLast(String firstName, String lastName) {

    String name = "";
    if (firstName != null && !firstName.equals("")) {
      name = firstName;
    }
    if (lastName != null && !lastName.equals("")) {
      if (name.length() > 0) {
        name += ", ";
      }
      name += lastName;
    }

    return name;
  }

  public static String formatShortName(String lastName, String firstName) {
    String name = "";
    if (firstName != null && !firstName.equals("")) {
      name = firstName.substring(0, 1);
    }
    if (lastName != null && !lastName.equals("")) {
      name += lastName;
    }
    return name.toLowerCase();    
  }

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

  public String getPasswordExternal() {
    return passwordExternal;
  }


  public void setPasswordExternal(String passwordExternal) {
    this.passwordExternal = passwordExternal;
  }


  public String getSalt() {
    return salt;
  }

  public void setSalt(String salt) {
    this.salt = salt;
  }

  public String getGuid() {
    return guid;
  }

  public void setGuid(String guid) {
    this.guid = guid;
  }

  public String getPasswordExpired() {
    return passwordExpired;
  }

  public void setPasswordExpired(String passwordExpired) {
    this.passwordExpired = passwordExpired;
  }

  public Timestamp getGuidExpiration() {
    return guidExpiration;
  }

  public void setGuidExpiration(Timestamp guidExpiration) {
    this.guidExpiration = guidExpiration;
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

  public Set getManagingCoreFacilities() {
    return managingCoreFacilities;
  }
  public void setManagingCoreFacilities(Set facilities) {
    managingCoreFacilities = facilities;
  }

  public Set getCoreFacilitiesICanSubmitTo() {
    return coreFacilitiesICanSubmitTo;
  }

  public void setCoreFacilitiesICanSubmitTo(Set coreFacilitiesICanSubmitTo) {
    this.coreFacilitiesICanSubmitTo = coreFacilitiesICanSubmitTo;
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

  public String getQualifiedDisplayName() {
    if (getIsExternalUser().equals("Y")) {
      return getDisplayName() + " (external user)";
    } else {
      return getDisplayName();
    }
  }
  public String getShortName() {
    return formatShortName(this.lastName, this.firstName);
  }

  public String getDisplayName() {
    String name = "";
    if (lastName != null && !lastName.equals("")) {
      name = lastName;
    }
    if (firstName != null && !firstName.equals("")) {
      if (!name.equals("")) {
        name += ", " ;
      }
      name += firstName;
    }
    if (this.getIsActive() != null && this.getIsActive().equalsIgnoreCase("N")) {
      name += " (inactive)";
    }
    return name;
  }

  public String getDisplayNameXMLSafe() {
    String name = "";
    if (lastName != null && !lastName.equals("")) {
      name = XMLTools.escapeXMLChars(lastName);
    }
    if (firstName != null && !firstName.equals("")) {
      if (!name.equals("")) {
        name += ", " ;
      }
      name += XMLTools.escapeXMLChars(firstName);
    }
    if (this.getIsActive() != null && this.getIsActive().equalsIgnoreCase("N")) {
      name += " (inactive)";
    }
    return name;
  }

  public String getFirstLastDisplayName() {
    String name = "";
    if (firstName != null && !firstName.equals("")) {
      name = firstName;
    }
    if (lastName != null && !lastName.equals("")) {
      if (!name.equals("")) {
        name += " " ;
      }
      name += lastName;
    }
    return name;
  }

  public int compareTo(Object o) {
    if (o instanceof AppUser) {
      AppUser other = (AppUser)o;      
      return this.getIdAppUser().compareTo(other.getIdAppUser());
    } else if (o instanceof NullDictionaryEntry) {
      return 1;
    } else {
      return -1;      
    }
  }

  public String getUcscUrl() {
    return ucscUrl;
  }

  public void setUcscUrl(String ucscUrl) {
    this.ucscUrl = ucscUrl;
  }  

  public void registerMethodsToExcludeFromXML() {
    this.excludeMethodFromXML("getPasswordExternal");
  }

  public String getConfirmEmailGuid() {
    return confirmEmailGuid;
  }

  public void setConfirmEmailGuid(String confirmEmailGuid) {
    this.confirmEmailGuid = confirmEmailGuid;
  }


}
