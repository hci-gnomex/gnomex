package hci.gnomex.utility;

import hci.gnomex.model.Sample;

import java.io.Serializable;
import java.util.Comparator;

 public class SampleNumberComparator implements Comparator, Serializable {
    public int compare(Object o1, Object o2) {
      Sample s1 = (Sample)o1;
      Sample s2 = (Sample)o2;
      
      String[] tokens1 = s1.getNumber().split("X");
      String number1 = tokens1[tokens1.length - 1];

      String[] tokens2 = s2.getNumber().split("X");
      String number2 = tokens2[tokens2.length - 1];
      
      String multiplexKey1 = s1.getMultiplexGroupNumber() != null ? s1.getMultiplexGroupNumber().toString() : "";
      String multiplexKey2 = s2.getMultiplexGroupNumber() != null ? s2.getMultiplexGroupNumber().toString() : "";

      if (multiplexKey1.equals(multiplexKey2)) {
        return new Integer(number1).compareTo(new Integer(number2));        
      } else {
        return multiplexKey1.compareTo(multiplexKey2);
      }
      
    }
  }