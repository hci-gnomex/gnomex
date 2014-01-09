package hci.gnomex.utility;

import hci.gnomex.model.PlateWell;

import java.io.Serializable;
import java.util.Comparator;

 public class PlateWellComparator implements Comparator, Serializable {
    public int compare(Object o1, Object o2) {
      PlateWell pw1 = (PlateWell)o1;
      PlateWell pw2 = (PlateWell)o2;
      
      return pw1.getIdPlateWell().compareTo(pw2.getIdPlateWell());
      
    }
  }