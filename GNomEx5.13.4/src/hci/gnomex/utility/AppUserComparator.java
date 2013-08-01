package hci.gnomex.utility;


import hci.gnomex.model.AppUser;

import java.io.Serializable;
import java.util.Comparator;

 public class AppUserComparator implements Comparator<AppUser>, Serializable {
    public int compare(AppUser u1, AppUser u2) {
      return u1.getIdAppUser().compareTo(u2.getIdAppUser());
    }
  }
