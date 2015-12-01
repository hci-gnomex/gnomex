
package hci.gnomex.utility;

import java.math.BigDecimal;

import hci.gnomex.model.Price;

public class PriceUtil {

  public static final String PRICE_INTERNAL            = "internal";
  public static final String PRICE_EXTERNAL_ACADEMIC   = "academic";
  public static final String PRICE_EXTERNAL_COMMERCIAL = "commercial";


  public static Boolean setPrice( BigDecimal value, BigDecimal existingPrice,
      Price price, String whichPrice ) {
    Boolean modified = false;

    if( existingPrice == null || ! existingPrice.equals( value ) ) {
      setPrice( value, price, whichPrice );
      modified = true;
    }

    return modified;
  }


  public static void setPrice( BigDecimal value, Price price,
      String whichPrice ) {
    if( whichPrice.equals( PRICE_INTERNAL ) ) {
      price.setUnitPrice( value );
    } else if( whichPrice.equals( PRICE_EXTERNAL_ACADEMIC ) ) {
      price.setUnitPriceExternalAcademic( value );
    } else if( whichPrice.equals( PRICE_EXTERNAL_COMMERCIAL ) ) {
      price.setUnitPriceExternalCommercial( value );
    }
  }

}
