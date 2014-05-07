package hci.gnomex.utility;

import hci.gnomex.model.FlowCellChannel;

import java.io.Serializable;
import java.util.Comparator;

 public class FlowCellChannelComparator implements Comparator<FlowCellChannel>, Serializable {
    public int compare(FlowCellChannel o1, FlowCellChannel o2) {
      FlowCellChannel ch1 = (FlowCellChannel)o1;
      FlowCellChannel ch2 = (FlowCellChannel)o2;
      
      return ch1.getNumber().compareTo(ch2.getNumber());
      
    }
  }