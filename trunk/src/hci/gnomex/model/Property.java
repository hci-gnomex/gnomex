package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import hci.dictionary.model.NullDictionaryEntry;

import java.io.Serializable;



public class Property extends DictionaryEntry implements Serializable {
  
  public static final String        CORE_FACILITY_NAME                  = "core_facility_name";
  public static final String        CONTACT_EMAIL_CORE_FACILITY         = "contact_email_core_facility";
  public static final String        CONTACT_NAME_CORE_FACILITY          = "contact_name_core_facility";
  public static final String        CONTACT_PHONE_CORE_FACILITY         = "contact_phone_core_facility";
  public static final String        CONTACT_EMAIL_SOFTWARE_TESTER       = "contact_email_software_tester";
  public static final String        CONTACT_EMAIL_SOFTWARE_BUGS         = "contact_email_software_bugs";
  public static final String        CONTACT_EMAIL_CORE_FACILITY_WORKAUTH = "contact_email_core_facility_workauth";
  public static final String        FLOWCELL_DIRECTORY_FLAG             = "flowcell_directory_flag";
  public static final String        QC_DIRECTORY                        = "qc_directory";
  public static final String        LUCENE_INDEX_DIRECTORY              = "lucene_index_directory";
  public static final String        LUCENE_EXPERIMENT_INDEX_DIRECTORY   = "lucene_experiment_index_directory";
  public static final String        LUCENE_PROTOCOL_INDEX_DIRECTORY     = "lucene_protocol_index_directory";
  public static final String        LUCENE_ANALYSIS_INDEX_DIRECTORY     = "lucene_analysis_index_directory";
  public static final String        BILLING_CORE_FACILITY_BUSINESS_UNIT = "billing_core_facility_business_unit"; 
  public static final String        BILLING_CORE_FACILITY_ACCOUNT       = "billing_core_facility_account";
  public static final String        BILLING_CORE_FACILITY_PO_ACCOUNT    = "billing_core_facility_po_account";
  public static final String        BILLING_CORE_FACILITY_FUND          = "billing_core_facility_fund";
  public static final String        BILLING_CORE_FACILITY_ORG           = "billing_core_facility_org";
  public static final String        BILLING_CORE_FACILITY_ACTIVITY      = "billing_core_facility_activity";
  public static final String        BILLING_PO_ACCOUNT                  = "billing_po_account";
  public static final String        EXPERIMENT_SUBMISSION_DEFAULT_MODE  = "experiment_submission_default_mode";
  public static final String        TEMP_DIRECTORY                      = "temp_directory";
  public static final String        BST_LINKAGE_SUPPORTED               = "bst_linkage_supported";  
  public static final String        FDT_SUPPORTED                       = "fdt_supported";
  public static final String        HTTP_PORT                           = "http_port";


  private Integer  idProperty;
  private String   propertyName;
  private String   propertyValue;
  private String   propertyDescription;
  private String   forServerOnly;

  public Integer getIdProperty() {
    return idProperty;
  }
  
  public void setIdProperty(Integer idProperty) {
    this.idProperty = idProperty;
  }
  
  public String getValue() {
    return idProperty.toString();
  }
 
  public String getDisplay() {
    return getPropertyName();
  }

  
  public String getPropertyName() {
    return propertyName;
  }

  
  public void setPropertyName(String propertyName) {
    this.propertyName = propertyName;
  }

  
  public String getPropertyValue() {
    return propertyValue;
  }

  
  public void setPropertyValue(String propertyValue) {
    this.propertyValue = propertyValue;
  }

  
  public String getPropertyDescription() {
    return propertyDescription;
  }

  
  public void setPropertyDescription(String propertyDescription) {
    this.propertyDescription = propertyDescription;
  }

  
  public String getForServerOnly() {
    return forServerOnly;
  }

  
  public void setForServerOnly(String forServerOnly) {
    this.forServerOnly = forServerOnly;
  }

}