package hci.gnomex.utility;

import java.io.Serializable;
import java.util.Comparator;

 public class UsageRowDescriptorComparator implements Comparator, Serializable {
    public int compare(Object o1, Object o2) {
      UsageRowDescriptor ur1 = (UsageRowDescriptor)o1;
      UsageRowDescriptor ur2 = (UsageRowDescriptor)o2;
      String combinedName1 = ur1.getLabLastName() + ur1.getLabFirstName() + ur1.getNumber() + ur1.getFileName();
      String combinedName2 = ur2.getLabLastName() + ur2.getLabFirstName() + ur2.getNumber() + ur2.getFileName();

      int retValue = combinedName1.compareTo(combinedName2);
      if(retValue == 0) {
        // if string portion identical then compare against date
        retValue = ur1.getCreateDate().compareTo(ur2.getCreateDate());
      }
      return retValue;
      
    }
  }