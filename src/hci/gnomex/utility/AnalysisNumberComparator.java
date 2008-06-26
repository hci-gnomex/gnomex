package hci.gnomex.utility;

import hci.gnomex.model.Analysis;

import java.io.Serializable;
import java.util.Comparator;

 public class AnalysisNumberComparator implements Comparator, Serializable {
    public int compare(Object o1, Object o2) {
      Analysis a1 = (Analysis)o1;
      Analysis a2 = (Analysis)o2;
      
      String[] tokens1 = a1.getNumber().split("A");
      String number1 = tokens1[tokens1.length - 1];

      String[] tokens2 = a2.getNumber().split("A");
      String number2 = tokens2[tokens2.length - 1];

      return new Integer(number1).compareTo(new Integer(number2));
      
    }
  }