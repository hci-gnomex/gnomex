package hci.gnomex.utility;

import hci.gnomex.model.BillingAccount;

import java.io.Serializable;
import java.util.Comparator;

 public class BillingAccountComparator implements Comparator, Serializable {
    public int compare(Object o1, Object o2) {
      BillingAccount ba1 = (BillingAccount)o1;
      BillingAccount ba2 = (BillingAccount)o2;
      
      // Sort so that active accounts order before inactive accounts,
      // then sort by account name.
      String key1 = ""; 
      if (ba1.getIsActive().equals("Y")) {
        key1 = "1" + (ba1.getIsApproved() != null && ba1.getIsApproved().equals("Y") ? "a" : "b") + ba1.getAccountName();
      } else {
        key1 = "2" + (ba1.getIsApproved() != null && ba1.getIsApproved().equals("Y") ? "a" : "b") + ba1.getAccountName();
      }
      String key2 = ""; 
      if (ba2.getIsActive().equals("Y")) {
        key2 = "1" + (ba2.getIsApproved() != null && ba2.getIsApproved().equals("Y") ? "a" : "b") + ba2.getAccountName();
      } else {
        key2 = "2" + (ba2.getIsApproved() != null && ba2.getIsApproved().equals("Y") ? "a" : "b") + ba2.getAccountName();
      }
      
      return key1.toUpperCase().compareTo(key2.toUpperCase());
      
    }
  }