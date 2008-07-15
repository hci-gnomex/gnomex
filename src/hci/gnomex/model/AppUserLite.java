package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;

import java.io.Serializable;


public class AppUserLite extends DictionaryEntry implements Serializable {
  
  private Integer idAppUser;
  private String  firstName;
  private String  lastName;
  private String  isActive;
  
  public String getDataKey() {
    return lastName + "," + firstName + "," + idAppUser;
   
  }
  public String getValue() {
    return idAppUser.toString();
  }
  
  public String getDisplay() {
    if ((lastName == null || lastName.trim().equals("")) &&
         (firstName == null || firstName.trim().equals(""))) {
      return "";
    } else {
      return lastName.trim() + ", " + firstName.trim();
      
    }
  }

  
  public Integer getIdAppUser() {
    return idAppUser;
  }

  
  public void setIdAppUser(Integer idAppUser) {
    this.idAppUser = idAppUser;
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

  
  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }
  
  
}
