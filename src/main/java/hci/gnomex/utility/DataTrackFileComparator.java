package hci.gnomex.utility;


import hci.gnomex.model.DataTrackFile;

import java.io.Serializable;
import java.util.Comparator;

 public class DataTrackFileComparator implements Comparator<DataTrackFile>, Serializable {
    public int compare(DataTrackFile dt1, DataTrackFile dt2) {
      return dt1.getIdDataTrackFile().compareTo(dt2.getIdDataTrackFile());
      
    }
  }
