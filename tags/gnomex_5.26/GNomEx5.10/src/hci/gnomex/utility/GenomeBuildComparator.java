package hci.gnomex.utility;


import hci.gnomex.model.GenomeBuild;

import java.io.Serializable;
import java.util.Comparator;

public class GenomeBuildComparator implements Comparator<GenomeBuild>, Serializable {
	public int compare(GenomeBuild v1, GenomeBuild v2) {
		if (v1.getBuildDate() != null && v2.getBuildDate() != null) {
			return v2.getBuildDate().compareTo(v1.getBuildDate());
		} else if (v1.getBuildDate() != null) {
			return 1;
		} else if (v2.getBuildDate() != null) {
			return 2;
		} else {
			return v1.getGenomeBuildName().compareTo(v2.getGenomeBuildName());
		}

	}
}
