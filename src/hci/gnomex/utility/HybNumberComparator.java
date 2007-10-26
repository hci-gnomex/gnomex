package hci.gnomex.utility;

import hci.gnomex.model.Hybridization;

import java.io.Serializable;
import java.util.Comparator;

 public class HybNumberComparator implements Comparator, Serializable {
    public int compare(Object o1, Object o2) {
      Hybridization h1 = (Hybridization)o1;
      Hybridization h2 = (Hybridization)o2;
      
      String[] tokens1 = h1.getNumber().split("E");
      String number1 = tokens1[tokens1.length - 1];

      String[] tokens2 = h2.getNumber().split("E");
      String number2 = tokens2[tokens2.length - 1];

      return new Integer(number1).compareTo(new Integer(number2));
      
    }
  }