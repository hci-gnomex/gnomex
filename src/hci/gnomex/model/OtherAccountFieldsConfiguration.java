package hci.gnomex.model;

import hci.hibernate5utils.HibernateDetailObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;


public class OtherAccountFieldsConfiguration extends HibernateDetailObject implements Serializable {
  private Integer idOtherAccountFieldsConfiguration; 
  private String fieldName;
  private String include;
  private String isRequired;
  
  public static final String EXPIRATION_DATE                    = "expirationDate";
  public static final String START_DATE                         = "startDate";
  public static final String FUNDING_AGENCY                     = "idFundingAgency";
  public static final String TOTAL_DOLLAR_AMOUNT                = "totalDollarAmount";
  public static final String SHORT_ACCT                         = "shortAcct";
  
  private static List<OtherAccountFieldsConfiguration> configurations = null;
  
  public Integer getIdOtherAccountFieldsConfiguration() {
    return idOtherAccountFieldsConfiguration;
  }
  public void setIdOtherAccountFieldsConfiguration(Integer id) {
    idOtherAccountFieldsConfiguration = id;
  }
  
  public String getFieldName() {
    return fieldName;
  }
  public void setFieldName(String fieldName) {
    this.fieldName = fieldName;
  }
  
  public String getInclude() {
    return include;
  }
  public void setInclude(String include) {
    this.include = include;
  }
  
  public String getIsRequired() {
    return isRequired;
  }
  public void setIsRequired(String isRequired) {
    this.isRequired = isRequired;
  }
  
  public static List<OtherAccountFieldsConfiguration> getConfiguration(Session sess) {
    loadConfigurations(sess);
    return configurations;
  }
  
  public static synchronized void reloadConfigurations(Session sess) {
    configurations = null;
    loadConfigurations(sess);
  }
  
  private static synchronized void loadConfigurations(Session sess) {
    if (configurations == null) {
      configurations = new ArrayList<OtherAccountFieldsConfiguration>();
      List l = sess.createQuery("from OtherAccountFieldsConfiguration").list();
      addConfiguration(EXPIRATION_DATE, l);
      addConfiguration(START_DATE, l);
      addConfiguration(FUNDING_AGENCY, l);
      addConfiguration(TOTAL_DOLLAR_AMOUNT, l);
      addConfiguration(SHORT_ACCT, l);
    }
  }
  
  private static void addConfiguration(String fieldName, List l) {
    Boolean found = false;
    for(Iterator i = l.iterator(); i.hasNext(); ) {
      OtherAccountFieldsConfiguration c = (OtherAccountFieldsConfiguration)i.next();
      if (c.getFieldName().equals(fieldName)) {
        found = true;
        configurations.add(c);
        break;
      }
    }
    if (!found) {
      configurations.add(createEmptyConfiguration(fieldName));
    }
  }
  
  private static OtherAccountFieldsConfiguration createEmptyConfiguration(String fieldName) {
    OtherAccountFieldsConfiguration c = new OtherAccountFieldsConfiguration();
    c.setFieldName(fieldName);
    c.setInclude("N");
    c.setIsRequired("N");
    return c;
  }
}
