package hci.gnomex.utility;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

 public class DescendingDateComparator implements Comparator, Serializable {
    public int compare(Object o1, Object o2) {
      Date d1 = (Date)o1;
      Date d2 = (Date)o2;
      
      
      return d2.compareTo(d1);
      
    }
  }