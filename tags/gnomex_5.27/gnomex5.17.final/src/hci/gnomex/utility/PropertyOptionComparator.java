package hci.gnomex.utility;


import hci.gnomex.model.PropertyOption;

import java.io.Serializable;
import java.util.Comparator;

public class PropertyOptionComparator implements Comparator<PropertyOption>, Serializable {
	public int compare(PropertyOption o1, PropertyOption o2) {
		return o1.getIdPropertyOption().compareTo(o2.getIdPropertyOption());
	}
}
