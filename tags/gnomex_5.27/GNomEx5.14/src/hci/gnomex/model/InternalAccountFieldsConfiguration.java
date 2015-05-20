package hci.gnomex.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;

import hci.gnomex.utility.PropertyDictionaryHelper;
import hci.hibernate3utils.HibernateDetailObject;


public class InternalAccountFieldsConfiguration extends HibernateDetailObject implements Serializable {
  private Integer idInternalAccountFieldsConfiguration; 
  private String fieldName;
  private String include;
  private Integer sortOrder;
  private String displayName;
  private String isRequired;
  private String isNumber;
  private Integer minLength;
  private Integer maxLength;
  
  public static final String PROJECT            = "project";
  public static final String ACCOUNT            = "account";
  public static final String CUSTOM_1           = "custom1";
  public static final String CUSTOM_2           = "custom2";
  public static final String CUSTOM_3           = "custom3";

  private static List<InternalAccountFieldsConfiguration> configurations = null;
  private static Boolean useConfigurableBillingAccounts = false;
  
  public Integer getIdInternalAccountFieldsConfiguration() {
    return idInternalAccountFieldsConfiguration;
  }
  public void setIdInternalAccountFieldsConfiguration(Integer id) {
    idInternalAccountFieldsConfiguration = id;
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
  
  public Integer getSortOrder() {
    return sortOrder;
  }
  public void setSortOrder(Integer sortOrder) {
    this.sortOrder = sortOrder;
  }
  
  public String getDisplayName() {
    return displayName;
  }
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }
  
  public String getIsRequired() {
    return isRequired;
  }
  public void setIsRequired(String isRequired) {
    this.isRequired = isRequired;
  }
  
  public String getIsNumber() {
    return isNumber;
  }
  public void setIsNumber(String isNumber) {
    this.isNumber = isNumber;
  }
  
  public Integer getMinLength() {
    return minLength;
  }
  public void setMinLength(Integer minLength) {
    this.minLength = minLength;
  }
  
  public Integer getMaxLength() {
    return maxLength;
  }
  public void setMaxLength(Integer maxLength) {
    this.maxLength = maxLength;
  }
  
  public static List<InternalAccountFieldsConfiguration> getConfiguration(Session sess) {
    loadConfigurations(sess);
    return configurations;
  }
  
  public static Boolean getUseConfigurableBillingAccounts(Session sess) {
    loadConfigurations(sess);
    return useConfigurableBillingAccounts;
  }
  
  public static synchronized void reloadConfigurations(Session sess) {
    configurations = null;
    loadConfigurations(sess);
  }
  
  private static synchronized void loadConfigurations(Session sess) {
    if (configurations == null) {
      configurations = new ArrayList<InternalAccountFieldsConfiguration>();
      List l = sess.createQuery("from InternalAccountFieldsConfiguration").list();
      addConfiguration(PROJECT, l);
      addConfiguration(ACCOUNT, l);
      addConfiguration(CUSTOM_1, l);
      addConfiguration(CUSTOM_2, l);
      addConfiguration(CUSTOM_3, l);
      
      PropertyDictionaryHelper pdh = PropertyDictionaryHelper.getInstance(sess);
      String cba = pdh.getProperty(PropertyDictionary.CONFIGURABLE_BILLING_ACCOUNTS);
      if (cba != null && cba.equals("Y")) {
        useConfigurableBillingAccounts = true;
      } else {
        useConfigurableBillingAccounts = false;
      }
    }
  }
  
  private static void addConfiguration(String fieldName, List l) {
    Boolean found = false;
    for(Iterator i = l.iterator(); i.hasNext(); ) {
      InternalAccountFieldsConfiguration c = (InternalAccountFieldsConfiguration)i.next();
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
  
  private static InternalAccountFieldsConfiguration createEmptyConfiguration(String fieldName) {
    InternalAccountFieldsConfiguration c = new InternalAccountFieldsConfiguration();
    c.setDisplayName(fieldName);
    c.setFieldName(fieldName);
    c.setInclude("N");
    c.setIsNumber("Y");
    c.setIsRequired("N");
    c.setMaxLength(20);
    c.setMinLength(1);
    c.setSortOrder(0);
    return c;
  }
}
