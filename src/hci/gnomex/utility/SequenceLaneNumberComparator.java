package hci.gnomex.utility;

import hci.gnomex.model.SequenceLane;

import java.io.Serializable;
import java.util.Comparator;

 public class SequenceLaneNumberComparator implements Comparator, Serializable {
    public int compare(Object o1, Object o2) {
      SequenceLane l1 = (SequenceLane)o1;
      SequenceLane l2 = (SequenceLane)o2;
      
      String[] tokens1 = l1.getNumber().split("L");
      String number1 = tokens1[tokens1.length - 1];

      String[] tokens2 = l2.getNumber().split("L");
      String number2 = tokens2[tokens2.length - 1];

      return new Integer(number1).compareTo(new Integer(number2));
      
    }
  }