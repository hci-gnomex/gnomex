package hci.gnomex.utility;

import java.io.Serializable;
import java.util.Comparator;

 public class MultiplexLaneComparator implements Comparator, Serializable {
    public int compare(Object o1, Object o2) {
      String key1 = (String)o1;
      String key2 = (String)o2;
      
      if (key1.equals(key2)) {
        return 0;
      } else if (key1.equals("")) {
        return -1;
      } else if (key2.equals("")) {
        return 1;
      }
      
      Integer keyInt1 = null;
      try {
        keyInt1 = Integer.parseInt(key1);
      } catch (NumberFormatException e) {        
      }
      
      Integer keyInt2 = null;
      try {
        keyInt2 = Integer.parseInt(key2);
      } catch (NumberFormatException e) {        
      }
      
      if (keyInt1 == null && keyInt2 == null) {
        return key1.compareTo(key2);
      } else if (keyInt1 == null ) {
        return -1;
      } else if (keyInt2 == null) {
        return 1;
      } else {
        return keyInt1.compareTo(keyInt2);
      }

    }
  }