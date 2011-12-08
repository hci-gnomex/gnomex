package hci.gnomex.utility;


import hci.gnomex.model.DataTrack;

import java.io.Serializable;
import java.util.Comparator;

public class DataTrackComparator implements Comparator<DataTrack>, Serializable {
	public int compare(DataTrack dt1, DataTrack dt2) {
		return dt1.getIdDataTrack().compareTo(dt2.getIdDataTrack());
	}
}
