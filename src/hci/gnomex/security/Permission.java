package hci.gnomex.security;

import hci.framework.model.DetailObject;
import java.util.*;
import java.io.Serializable;


public class Permission extends DetailObject implements Serializable {
 
  private String name;

  public Permission(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
  
  public boolean equals(Object other) {
    if (other instanceof Permission) {
      return this.getName().equals(((Permission)other).getName());
    } else {
      return false;
    } 
  }
  
  public int hashCode() {
    return this.getName().hashCode();
  }
  
}
