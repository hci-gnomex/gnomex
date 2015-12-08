
package hci.gnomex.utility;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Session;

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


  public static Boolean setPrice( String attributeValue, BigDecimal existingPrice, Price price, String whichPrice ) throws NumberFormatException {
    Boolean modified = false;
    // If attribute not specified then don't set the value
    if( attributeValue != null && attributeValue.length() > 0 ) {

      BigDecimal value = new BigDecimal( attributeValue );
      if( existingPrice == null || ! existingPrice.equals( value ) ) {
        setPrice( value, price, whichPrice );
        modified = true;
      }

    } else {

      BigDecimal value = new BigDecimal( 0 );
      if( existingPrice == null || ! existingPrice.equals( value ) ) {
        setPrice( value, price, whichPrice );
        modified = true;
      }
    }

    return modified;
  }

  public static String getUnitPrice(Price p, String priceType) {
    String priceAsString = "";
    if (p == null) {
      priceAsString = "";
    } else {
      if (priceType.equals(PRICE_INTERNAL)) {
        priceAsString = p.getUnitPrice().toString();
      } else if (priceType.equals(PRICE_EXTERNAL_ACADEMIC)) {
        priceAsString = p.getUnitPriceExternalAcademic().toString();
      } else if (priceType.equals(PRICE_EXTERNAL_COMMERCIAL)) {
        priceAsString = p.getUnitPriceExternalCommercial().toString();
      } else {
        priceAsString = "";
      }
    }

    return priceAsString;
  }


  public static boolean priceHasBillingItems( Price price, Session sess ) {
    // Determine if this price is already referenced on any billing items
    boolean existingBillingItems = false;
    StringBuffer buf = new StringBuffer();
    buf.append( "SELECT bi from BillingItem bi " );
    buf.append( "WHERE  bi.idPrice = " + price.getIdPrice().toString() );
    List billingItems = sess.createQuery( buf.toString() ).list();
    if( billingItems.size() > 0 ) {
      existingBillingItems = true;
    }
    return existingBillingItems;
  }

}
