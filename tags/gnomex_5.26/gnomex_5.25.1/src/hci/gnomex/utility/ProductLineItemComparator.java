package hci.gnomex.utility;


import hci.gnomex.model.ProductLineItem;

import java.io.Serializable;
import java.util.Comparator;

public class ProductLineItemComparator implements Comparator<ProductLineItem>, Serializable {
	public int compare(ProductLineItem o1, ProductLineItem o2) {
	  
	  if (o1.getIdProductLineItem() != null && o2.getIdProductLineItem() != null) {
      return o1.getIdProductLineItem().compareTo(o2.getIdProductLineItem());
    } else if (o1.getIdProductLineItem() != null) {
      return -1;
    } else if (o2.getIdProductLineItem() != null) {
      return 1;
    } else {
      if ( o1.getIdProductOrder() == o2.getIdProductOrder()) {
        return o1.getProduct().compareTo( o2.getProduct() );
      } 
      return o1.getIdProductOrder().compareTo(o2.getIdProductOrder());
    }
	  
	}
}
