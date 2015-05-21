package hci.gnomex.model;

import hci.dictionary.model.NullDictionaryEntry;
import hci.hibernate3utils.HibernateDetailObject;

import java.io.Serializable;


public class AppUserPublic extends HibernateDetailObject implements Serializable, Comparable {
  public static final String    MASKED_PASSWORD = "XXXX";
  
  private Integer idAppUser;
  private String  firstName;
  private String  lastName;
  private String  email;
  private String  phone;
  private String  department;
  private String  institute;
  private String  ucscUrl;
  private String  uNID;
  private String  userNameExternal;
  private String  passwordExternal;


  
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

  public String getUcscUrl() {
    return ucscUrl;
  }

  public void setUcscUrl(String ucscUrl) {
    this.ucscUrl = ucscUrl;
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
    return name;
  }
  
  public String getuNID() {
    return uNID;
  }

  public void setuNID(String uNID) {
    this.uNID = uNID;
  }

  public String getUserNameExternal() {
    return userNameExternal;
  }

  public void setUserNameExternal(String userNameExternal) {
    this.userNameExternal = userNameExternal;
  }

  public String getPasswordExternal() {
    return passwordExternal;
  }

  public void setPasswordExternal(String passwordExternal) {
    this.passwordExternal = passwordExternal;
  }
  
  public String getPasswordExternalEntered() {
    if (this.passwordExternal != null && !this.passwordExternal.equals("")) {
      return MASKED_PASSWORD;      
    } else {
      return "";
    }
  }

  public void registerMethodsToExcludeFromXML() {
    this.excludeMethodFromXML("getPasswordExternal");
  }


  public int compareTo(Object o) {
    if (o instanceof AppUserPublic) {
      AppUserPublic other = (AppUserPublic)o;      
      return this.getIdAppUser().compareTo(other.getIdAppUser());
    } else if (o instanceof NullDictionaryEntry) {
      return 1;
    } else {
      return -1;      
    }
  }  


}
