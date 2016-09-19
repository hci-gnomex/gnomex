package hci.gnomex.utility;

import hci.gnomex.model.AppUser;

import java.io.Serializable;
import java.util.Comparator;

 public class AppUserNameComparator implements Comparator, Serializable {
    public int compare(Object o1, Object o2) {
      AppUser u1 = (AppUser)o1;
      AppUser u2 = (AppUser)o2;

      return u1.getDisplayName().toLowerCase().compareTo(u2.getDisplayName().toLowerCase());
      
    }
  }