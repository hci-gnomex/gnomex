package hci.gnomex.utility;

import hci.gnomex.model.Lab;

import java.io.Serializable;
import java.util.Comparator;

 public class LabComparator implements Comparator, Serializable {
    public int compare(Object o1, Object o2) {
      Lab l1 = (Lab)o1;
      Lab l2 = (Lab)o2;

      return l1.getIdLab().compareTo(l2.getIdLab());
      
    }
  }