package hci.gnomex.utility;

import hci.gnomex.model.SequenceLane;

import java.io.Serializable;
import java.util.Comparator;

 public class SequenceLaneNumberComparator implements Comparator, Serializable {
    public int compare(Object o1, Object o2) {
      SequenceLane l1 = (SequenceLane)o1;
      SequenceLane l2 = (SequenceLane)o2;
      
      String sampleNumber1;
      String seqNumber1 = "";
      // Deal with old (ex. 6142L1) and new (ex. 6142F1_1) naming scheme
      if (l1.getNumber().indexOf("F") >= 0) {
        String[] tokens        = l1.getNumber().split("F");
        String number1         = tokens[tokens.length - 1];
        String[] numberTokens  = number1.split("_");
        sampleNumber1          = numberTokens[0];
        seqNumber1             = numberTokens[1];        
      } else {
        String[] tokens = l1.getNumber().split("L");
        sampleNumber1          = tokens[tokens.length - 1];
        seqNumber1 = "";
      }

      String sampleNumber2;
      String seqNumber2 = "";
      if (l2.getNumber().indexOf("F") >= 0) {
        String[] tokens        = l2.getNumber().split("F");
        String number2         = tokens[tokens.length - 1];
        String[] numberTokens  = number2.split("_");
        sampleNumber2          = numberTokens[0];
        seqNumber2             = numberTokens[1];        
      } else {
        String[] tokens        = l2.getNumber().split("L");
        sampleNumber2          = tokens[tokens.length - 1];
        seqNumber2 = "";
      }


      
      if (sampleNumber1.equals(sampleNumber2)) {
        return new Integer(seqNumber1).compareTo(new Integer(seqNumber2));
      } else {
        return new Integer(sampleNumber1).compareTo(new Integer(sampleNumber2));        
      }

      
    }
  }