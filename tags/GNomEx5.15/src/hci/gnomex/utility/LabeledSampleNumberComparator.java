package hci.gnomex.utility;

import hci.gnomex.model.LabeledSample;
import hci.gnomex.model.Sample;

import java.io.Serializable;
import java.util.Comparator;

 public class LabeledSampleNumberComparator implements Comparator, Serializable {
    public int compare(Object o1, Object o2) {
      Sample s1 = ((LabeledSample)o1).getSample();
      Sample s2 = ((LabeledSample)o2).getSample();
      
      String[] tokens1 = s1.getNumber().split("X");
      String number1 = tokens1[tokens1.length - 1];

      String[] tokens2 = s2.getNumber().split("X");
      String number2 = tokens2[tokens2.length - 1];

      return new Integer(number1).compareTo(new Integer(number2));
      
    }
  }