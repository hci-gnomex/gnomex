package hci.gnomex.utility;

import hci.gnomex.model.PlateWell;

import java.io.Serializable;
import java.util.Comparator;

 public class PlateWellComparator implements Comparator, Serializable {
    public int compare(Object o1, Object o2) {
      PlateWell pw1 = (PlateWell)o1;
      PlateWell pw2 = (PlateWell)o2;
      
      if (pw1 != null && pw2 != null) {
        if (pw1.getIdPlateWell() != null && pw2.getIdPlateWell() != null) {
          return pw1.getIdPlateWell().compareTo(pw2.getIdPlateWell());
        } else if (pw1.getIdPlateWell() != null) {
          return -1;
        } else if (pw2.getIdPlateWell() != null) {
          return 1;
        } else {
          return 0;
        }
      } else if (pw1 != null) {
        return -1;
      } else if (pw2 != null) {
        return 1;
      } else {
        return 0;
      }
      
      
    }
  }