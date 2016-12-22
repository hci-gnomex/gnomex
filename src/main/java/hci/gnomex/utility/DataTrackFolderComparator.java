package hci.gnomex.utility;


import hci.gnomex.model.DataTrackFolder;

import java.io.Serializable;
import java.util.Comparator;

 public class DataTrackFolderComparator implements Comparator<DataTrackFolder>, Serializable {
    public int compare(DataTrackFolder dt1, DataTrackFolder dt2) {
      return dt1.getIdDataTrackFolder().compareTo(dt2.getIdDataTrackFolder());
      
    }
  }
