package hci.dictionary.utility;

import hci.dictionary.model.DictionaryEntry;
import hci.framework.control.Command;
import hci.framework.model.DetailObject;
import hci.framework.security.SecurityAdvisor;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

/**
 * Base class for commands that use the HCIDictionary API to call the
 * loadCommand and execute methods on
 * {@link hci.dictionary.utility.DictionaryManager}.
 * <p>
 * Subclasses have all the standard properties of HCI Framework Commands, but
 * also inherit the four member variables defined below, which the
 * {@link hci.dictionary.utility.DictionaryManager} uses to process commands for
 * manipulating dictionaries.
 * 
 * @author Cody Haroldsen
 * @version 1.0
 * @since 11/4/2005
 */
public abstract class DictionaryCommand extends Command {
  public String action = null;
  public String xmlResult = null;
  public String className = null;
  public String requestURL = null;

  public DictionaryEntry dictionaryEntry = null;
  public SecurityAdvisor securityAdvisor = null;

  public HashMap<String, String> loadDetailObject(HttpServletRequest request, DetailObject detail) {
    Class<? extends DetailObject> detailClass = detail.getClass();
    Method[] methods = detailClass.getMethods();

    for (int i = 0; i < methods.length; i++) {
      String methName = methods[i].getName();
      if (methName.substring(0, 3).equals("set")) {
        String correctedName = methName.substring(3, 4).toLowerCase();
        if (methName.length() > 4) {
          correctedName = correctedName + methName.substring(4);
        }

        // Handle field names that start with a capital letter
        try {
          detailClass.getDeclaredField(correctedName.toString());
        } catch (Exception e) {
          try {
            detailClass.getDeclaredField(correctedName.substring(0, 1).toUpperCase() + correctedName.substring(1));
            correctedName = correctedName.substring(0, 1).toUpperCase() + correctedName.substring(1);
          } catch (Exception ex) {
          }
        }

        String requestedValue = request.getParameter(correctedName.toString());

        if (requestedValue != null && requestedValue.trim().length() > 0) {
          Class<?> argType = methods[i].getParameterTypes()[0];
          String field = correctedName.toString().trim();
          String value = (String) request.getParameter(field);
          Object o = null;
          if (value != null) {
            if (argType == java.sql.Date.class) {
              o = this.parseDate(value);
              if (o == null) {
                detail.addInvalidField(field, "Please enter a valid date in the " + field + " field");
              }
            } else if (argType == java.util.Date.class) {
              try {
                o = this.parseDateTime(value);
                if (o == null) {
                  detail.addInvalidField(field, "Please enter a valid date in the " + field + " field");
                }
              } catch (ParseException pe) {
                o = null;
                detail.addInvalidField(field, "Please enter a valid date in the " + field + " field");
              }
            } else if (argType == String.class) {
              o = value;
            } else if (argType == java.math.BigDecimal.class) {
              value = this.stripChar(value, ',');
              try {
                o = new java.math.BigDecimal(value);
              } catch (NumberFormatException nfe) {
                o = null;
                // put invalid value in invalid fields hashmap
                detail.addInvalidField(field, "Please enter a numeric value in the " + field + " field");
              }
            } else if (argType == java.lang.Integer.class) {
              if (value != null) {
                try {
                  if (value.equals("0")) {
                    o = new java.lang.Integer(0);
                  } else {
                    o = new java.lang.Integer(value);
                  }
                } catch (NumberFormatException nfe) {
                  o = null;
                  // put invalid value in invalid fields hashmap
                  detail.addInvalidField(field, "Please enter a numeric value in the " + field + " field");
                }
              }
            } else if (argType == java.sql.Timestamp.class) {
              try {
                // value = this.formatTimestamp(value);
                // o = new java.sql.Timestamp(new
                // java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(value).getTime());
                o = this.parseTimestamp(value);
              } catch (ParseException pe) {
                o = null;
                detail.addInvalidField(field, "The value entered in the " + field + " field is not valid");
              }
            } else {
              try {
                o = (argType.getMethod("valueOf", new Class[] { String.class })).invoke(argType, new Object[] { value });
              } catch (Exception e) {
                // we must have an invalid value
                detail.addInvalidField(field, "The value entered in the " + field + " field is not valid");
              }
            }
          } else {
            o = null;
          }
          try {
            methods[i].invoke(detail, new Object[] { o });
          } catch (Exception e) {
            e.printStackTrace();
          }
        } else if (requestedValue != null && requestedValue.trim().length() == 0) {
          // we have a null value
          try {
            methods[i].invoke(detail, new Object[] { null });
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }

    }

    HashMap<String, String> invalidFields = detail.getInvalidFields();

    return invalidFields;
  }
}
