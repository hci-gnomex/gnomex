package hci.gnomex.utility;


import hci.gnomex.model.GenomeIndex;

import java.io.Serializable;
import java.util.Comparator;

public class GenomeIndexComparator implements Comparator<GenomeIndex>, Serializable {
	public int compare(GenomeIndex gIndx1, GenomeIndex gIndx2) {
		return gIndx1.getGenomeIndexName().compareTo(gIndx2.getGenomeIndexName());	
	}
}
