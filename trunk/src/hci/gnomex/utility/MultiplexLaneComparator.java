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
      
      
      
      String[] tokens1 = key1.split("-");
      Integer ordinal1 = Integer.valueOf(tokens1[0]);
      Integer indexTagGroup1 = Integer.valueOf(tokens1[1]);
      String other1 = tokens1[2];

      String[] tokens2 = key2.split("-");
      Integer ordinal2 = Integer.valueOf(tokens2[0]);
      Integer indexTagGroup2 = Integer.valueOf(tokens2[1]);
      String other2 = tokens2[2];
      
      if (ordinal1.equals(ordinal2)) {
        if (indexTagGroup1.equals(indexTagGroup2)) {
          return other1.compareTo(other2);
        } else {
          return indexTagGroup1.compareTo(indexTagGroup2);
        } 
      } else {
        return ordinal1.compareTo(ordinal2);
      }
    }
  }