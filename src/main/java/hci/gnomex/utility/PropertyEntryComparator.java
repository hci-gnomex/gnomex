package hci.gnomex.utility;


import hci.gnomex.model.PropertyEntry;

import java.io.Serializable;
import java.util.Comparator;

public class PropertyEntryComparator implements Comparator<PropertyEntry>, Serializable {
	public int compare(PropertyEntry o1, PropertyEntry o2) {
	  
	  if (o1.getIdPropertyEntry() != null && o2.getIdPropertyEntry() != null) {
      return o1.getIdPropertyEntry().compareTo(o2.getIdPropertyEntry());
    } else if (o1.getIdPropertyEntry() != null) {
      return -1;
    } else if (o2.getIdPropertyEntry() != null) {
      return 1;
    } else {
      if ( o1.getIdProperty() == o2.getIdProperty()) {
        return o1.getValue().compareTo( o2.getValue() );
      } 
      return o1.getIdProperty().compareTo(o2.getIdProperty());
    }
	  
	}
}
