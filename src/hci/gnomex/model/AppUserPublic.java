package hci.gnomex.model;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;

import hci.dictionary.model.NullDictionaryEntry;
import hci.hibernate3utils.HibernateDetailObject;


public class AppUserPublic extends HibernateDetailObject implements Serializable, Comparable {
  public static final String    MASKED_PASSWORD = "XXXX";
  
  private Integer idAppUser;
  private String  firstName;
  private String  lastName;
  private String  email;
  private String  phone;
  private String  department;
  private String  institute;

  
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
