package hci.gnomex.utility;

import hci.gnomex.model.SequenceLane;

import java.io.Serializable;
import java.util.Comparator;

 public class FlowCellLaneComparator implements Comparator, Serializable {
    public int compare(Object o1, Object o2) {
      SequenceLane sl1 = (SequenceLane)o1;
      SequenceLane sl2 = (SequenceLane)o2;
      
      return sl1.getFlowCellLaneNumber().compareTo(sl2.getFlowCellLaneNumber());
      
    }
  }