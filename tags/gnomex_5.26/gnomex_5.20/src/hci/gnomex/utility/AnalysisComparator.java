package hci.gnomex.utility;


import hci.gnomex.model.Analysis;

import java.io.Serializable;
import java.util.Comparator;

public class AnalysisComparator implements Comparator<Analysis>, Serializable {
	public int compare(Analysis dt1, Analysis dt2) {
		return dt1.getIdAnalysis().compareTo(dt2.getIdAnalysis());
	}
}
