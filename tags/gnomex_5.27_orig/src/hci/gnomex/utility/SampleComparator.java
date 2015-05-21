package hci.gnomex.utility;

import hci.gnomex.model.Sample;

import java.io.Serializable;
import java.util.Comparator;

@SuppressWarnings({ "rawtypes", "serial" })
public class SampleComparator implements Comparator, Serializable {
  public int compare(Object o1, Object o2) {
    Sample s1 = (Sample)o1;
    Sample s2 = (Sample)o2;
    return s1.getIdSample().compareTo(s2.getIdSample());
    
  }
}
