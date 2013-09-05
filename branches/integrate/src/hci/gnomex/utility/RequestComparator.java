package hci.gnomex.utility;


import hci.gnomex.model.Request;

import java.io.Serializable;
import java.util.Comparator;

public class RequestComparator implements Comparator<Request>, Serializable {
	public int compare(Request dt1, Request dt2) {
		return dt1.getIdRequest().compareTo(dt2.getIdRequest());
	}
}
