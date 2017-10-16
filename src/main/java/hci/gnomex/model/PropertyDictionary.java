package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import hci.dictionary.utility.DictionaryManager;

import java.io.Serializable;

public class PropertyDictionary extends DictionaryEntry implements Serializable {

	public static final String CONTACT_EMAIL_BIOINFORMATICS = "contact_email_bioinformatics";
	public static final String CONTACT_EMAIL_SOFTWARE_TESTER = "contact_email_software_tester";
	public static final String CONTACT_EMAIL_SOFTWARE_BUGS = "contact_email_software_bugs";
	public static final String CONTACT_EMAIL_CORE_FACILITY_WORKAUTH = "contact_email_core_facility_workauth";
	public static final String CONTACT_EMAIL_CORE_FACILITY_WORKAUTH_REMINDER = "contact_email_core_facility_workauth_reminder";
	public static final String REPLY_EMAIL_CORE_FACILITY_WORKAUTH_REMINDER = "reply_email_core_facility_workauth_reminder";
	public static final String CONTACT_EMAIL_ILLUMINA_REP = "contact_email_illumina_rep";
	public static final String CONTACT_EMAIL_PURCHASING = "contact_email_purchasing";
	public static final String CONTACT_ADDRESS_CORE_FACILITY = "contact_address_core_facility";
	public static final String CONTACT_REMIT_ADDRESS_CORE_FACILITY = "contact_remit_address_core_facility";
	public static final String FLOWCELL_DIRECTORY_FLAG = "flowcell_directory_flag";
	public static final String LAST_UPLOAD_NOTIFICATION = "last_upload_notification";
	public static final String QC_DIRECTORY = "qc_directory";
	public static final String DIRECTORY_BIOINFORMATICS_SCRATCH = "directory_bioinformatics_scratch";
	public static final String LUCENE_INDEX_DIRECTORY = "lucene_index_directory";
	public static final String LUCENE_EXPERIMENT_INDEX_DIRECTORY = "lucene_experiment_index_directory";
	public static final String LUCENE_PROTOCOL_INDEX_DIRECTORY = "lucene_protocol_index_directory";
	public static final String LUCENE_ANALYSIS_INDEX_DIRECTORY = "lucene_analysis_index_directory";
	public static final String LUCENE_DATATRACK_INDEX_DIRECTORY = "lucene_datatrack_index_directory";
	public static final String LUCENE_TOPIC_INDEX_DIRECTORY = "lucene_topic_index_directory";
	public static final String LUCENE_GLOBAL_INDEX_DIRECTORY = "lucene_global_index_directory";
	public static final String BILLING_CORE_FACILITY_BUSINESS_UNIT = "billing_core_facility_business_unit";
	public static final String BILLING_CORE_FACILITY_ACCOUNT = "billing_core_facility_account";
	public static final String BILLING_CORE_FACILITY_PO_ACCOUNT = "billing_core_facility_po_account";
	public static final String BILLING_CORE_FACILITY_FUND = "billing_core_facility_fund";
	public static final String BILLING_CORE_FACILITY_ORG = "billing_core_facility_org";
	public static final String BILLING_CORE_FACILITY_ACTIVITY = "billing_core_facility_activity";
	public static final String BILLING_PO_ACCOUNT = "billing_po_account";
	public static final String BILLING_GL_JOURNAL_ID_CORE_FACILITY = "billing_gl_journal_id_core_facility";
	public static final String BILLING_GL_JOURNAL_LINE_REF_CORE_FACILITY = "billing_gl_journal_line_ref_core_facility";
	public static final String BILLING_GL_HEADER_FACILITY = "billing_gl_header_facility";
	public static final String BILLING_GL_HEADER_DESCRIPTION = "billing_gl_header_description";
	public static final String INVOICE_HEADER_NOTE = "invoice_header_note";
	public static final String INVOICE_HEADER_NOTE_PO = "invoice_header_note_po";
	public static final String INVOICE_FOOTER_NOTE = "invoice_footer_note";
	public static final String DATASET_PRIVACY_EXPIRATION = "dataset_privacy_expiration";
	public static final String DATASET_PRIVACY_EXPIRATION_WARNING = "dataset_privacy_expiration_warning";
	public static final String DATASET_PRIVACY_EXPIRATION_REPLY_EMAIL = "dataset_privacy_expiration_reply_email";
	public static final String EXPERIMENT_SUBMISSION_DEFAULT_MODE = "experiment_submission_default_mode";
	public static final String TEMP_DIRECTORY = "temp_directory";
	public static final String BST_LINKAGE_SUPPORTED = "bst_linkage_supported";
	public static final String BST_LINKAGE_GNOMEX_URL = "bst_linkage_gnomex_url";
	public static final String GNOMEX_LINKAGE_BST_URL = "gnomex_linkage_bst_url";
	public static final String FDT_SUPPORTED = "fdt_supported";
	public static final String HTTP_PORT = "http_port";
	public static final String FDT_USER = "fdt_user";
	public static final String FDT_GROUP = "fdt_group";
	public static final String UNIVERSITY_USER_AUTHENTICATION = "university_user_authentication";
	public static final String USAGE_USER_VISIBILITY = "usage_user_visibility";
	public static final String EXTERNAL_DATA_SHARING_SITE = "external_data_sharing_site";
	public static final String SITE_TITLE = "site_title";
	public static final String SITE_WELCOME_MESSAGE = "site_welcome_message";
	public static final String SHOW_USAGE_ON_STARTUP = "show_usage_on_startup";
	public static final String TOPICS_SUPPORTED = "topics_supported";
	public static final String DATATRACK_FILESERVER_URL = "datatrack_fileserver_url";
	public static final String DATATRACK_FILESERVER_WEB_CONTEXT = "datatrack_fileserver_web_context";
	public static final String BAM_IOBIO_VIEWER_URL = "bam_iobio_viewer_url";
	public static final String GENE_IOBIO_VIEWER_URL = "gene_iobio_viewer_url";
	public static final String VCF_IOBIO_VIEWER_URL = "vcf_iobio_viewer_url";
	public static final String EXPERIMENT_FILE_SAMPLE_LINKING_ENABLED = "experiment_file_sample_linking_enabled";
	public static final String DEFAULT_VISIBILITY_EXPERIMENT = "default_visibility_experiment";
	public static final String DEFAULT_VISIBILITY_ANALYSIS = "default_visibility_analysis";
	public static final String DEFAULT_VISIBILITY_DATATRACK = "default_visibility_datatrack";
	public static final String DEFAULT_VISIBILITY_TOPIC = "default_visibility_topic";
	public static final String GENERIC_NO_REPLY_EMAIL = "generic_no_reply_email";
	public static final String PERFORMANCE_LOG_ENABLED = "performance_log_enabled";
	public static final String PERFORMANCE_LOG_CUTOFF_MILLISECONDS = "performance_log_cutoff_milliseconds";
	public static final String EXPERIMENT_DEFAULT_VIEW = "experiment_default_view";
	public static final String EXPERIMENT_VIEW_LIMIT = "view_limit_experiments";
	public static final String GET_REQUEST_NUMBER_PROCEDURE = "get_request_number_procedure";
	public static final String GET_PO_NUMBER_PROCEDURE = "get_po_number_procedure";
	public static final String ID_DEFAULT_INSTITUTION = "id_default_institution";
	public static final String SITE_LOGO = "site_logo";
	public static final String SITE_SPLASH = "site_splash";
	public static final String CONFIGURABLE_BILLING_ACCOUNTS = "configurable_billing_accounts";
	public static final String INVOICE_NOTE_1 = "invoice_note_1";
	public static final String INVOICE_NOTE_2 = "invoice_note_2";
	public static final String DISK_USAGE_FOR_EXPERIMENTS = "disk_usage_for_experiments";
	public static final String DISK_USAGE_FOR_ANALYSIS = "disk_usage_for_analysis";
	public static final String DISK_USAGE_INCREMENT = "disk_usage_increment";
	public static final String DISK_USAGE_MONTHLY_CHARGE_PER_GB = "disk_usage_monthly_charge_per_gb";
	public static final String DISK_USAGE_FREE_PER_INCREMENT = "disk_usage_free_per_increment";
	public static final String DISK_USAGE_FREE_GB = "disk_usage_free_gb";
	public static final String DISK_USAGE_ANALYSIS_GRACE_PERIOD_IN_MONTHS = "disk_usage_analysis_grace_period_in_months";
	public static final String DISK_USAGE_EXPERIMENT_GRACE_PERIOD_IN_MONTHS = "disk_usage_experiment_grace_period_in_months";
	public static final String DISK_USAGE_PRICE_CATEGORY_NAME = "disk_usage_price_category_name";
	public static final String GNOMEX_SUPPORT_EMAIL = "gnomex_support_email";
	public static final String GUEST_DOWNLOAD_TERMS = "guest_download_terms";
	public static final String CAN_ACCESS_BSTX = "can_access_bstx";
	public static final String REQUEST_SUBMIT_CONFIRMATION_EMAIL = "request_submit_confirmation_email";
	public static final String SUBMIT_REQUEST_INSTRUCTIONS = "submit_request_instructions";
	public static final String ONCOCARTA_FDF_TEMPLATE = "oncocarta_fdf_template";
	public static final String PUBLIC_DATA_NOTICE = "public_data_notice";
	public static final String HISEQ_RUN_TYPE_LABEL_STANDARD = "hiseq_run_type_label_standard";
	public static final String HISEQ_RUN_TYPE_LABEL_CUSTOM = "hiseq_run_type_label_custom";
	public static final String HISEQ_RUN_TYPE_CUSTOM_WARNING = "hiseq_run_type_custom_warning";
	public static final String ANALYSIS_ASSISTANCE_NOTE = "analysis_assistance_note";
	public static final String CONTACT_EMAIL_MANAGE_SAMPLE_FILE_LINK = "contact_email_manage_sample_file_link";
	public static final String CONTACT_EMAIL_BIOINFORMATICS_ANALYSIS_REQUESTS = "contact_email_bioinformatics_analysis_requests";
	public static final String CUSTOM_BILLING_MESSAGE = "custom_billing_message";
	public static final String DISABLE_USER_SIGNUP = "disable_user_signup";
	public static final String EXPERIMENT_PLATFORM_HIDE_VENDOR = "experiment_platform_hide_vendor";
	public static final String EXPERIMENT_PLATFORM_HIDE_NOTES = "experiment_platform_hide_notes";
	public static final String EXPERIMENT_PLATFORM_HIDE_ORGANISM = "experiment_platform_hide_organism";
	public static final String INTERNAL_PRICE_LABEL = "internal_price_label";
	public static final String EXTERNAL_ACADEMIC_PRICE_LABEL = "external_academic_price_label";
	public static final String EXTERNAL_COMMERCIAL_PRICE_LABEL = "external_commercial_price_label";
	public static final String ILLUMINA_LIBPREP_DEFAULT_PRICE_CATEGORY = "illumina_libprep_default_price_category";
	public static final String ILLUMINA_SEQOPTION_DEFAULT_PRICE_CATEGORY = "illumina_seqoption_default_price_category";
	public static final String FILE_MAX_VIEWABLE_SIZE = "file_max_viewable_size";
	public static final String QC_ASSAY_HIDE_BUFFER_STRENGTH = "qc_assay_hide_buffer_strength";
	public static final String QC_ASSAY_HIDE_WELLS_PER_CHIP = "qc_assay_hide_wells_per_chip";

	public static final String OPTION_USER_USER_VISIBILITY_NONE = "none";
	public static final String OPTION_USER_USER_VISIBILITY_MASKED = "masked";
	public static final String OPTION_USER_USER_VISIBILITY_FULL = "full";
	public static final String USAGE_GUEST_STATS = "usage_guest_stats";
	public static final String USAGE_REPORT_EMAILS = "usage_report_emails";
	public static final String METRIX_SERVER_HOST = "metrix_server_host";
	public static final String METRIX_SERVER_PORT = "metrix_server_port";
	public static final String SEQ_LANE_NUMBER_SEPARATOR = "seq_lane_number_separator";
	public static final String SEQ_LANE_LETTER = "seq_lane_letter";
	public static final String STATUS_TO_START_WORKFLOW = "status_to_start_workflow";
	public static final String NO_GUEST_ACCESS = "no_guest_access";
	public static final String NO_PUBLIC_VISIBILITY = "no_public_visibility";
	public static final String ALLOW_CORE_GLOBAL_SUBMISSION = "allow_core_global_submission";
	public static final String REQUEST_COMPLETE_CONFIRMATION_EMAIL_MESSAGE = "request_complete_confirmation_email_message";
	public static final String ANNOTATION_OPTION_EQUIVALENTS = "annotation_option_equivalents";
	public static final String ANNOTATION_OPTION_INVALID = "annotation_option_invalid";
	public static final String EXPERIMENT_MATRIX_PROPERTIES = "experiment_matrix_properties";
	public static final String BILLING_GL_HEADER_CURRENCY = "billing_gl_header_currency";
	public static final String BILLING_GL_BLANK_YEAR = "billing_gl_blank_year";
	public static final String FISCAL_YEAR_BREAK_MONTH = "fiscal_year_break_month";
	public static final String FISCAL_YEAR_BREAK_DAY = "fiscal_year_break_day";
	public static final String NOTIFY_SUPPORT_OF_NEW_USER = "notify_support_of_new_user";
	public static final String ILLUMINA_QC_IN_LIB_PREP = "illumina_qc_in_lib_prep";
	public static final String BILLING_DURING_WORKFLOW = "billing_during_workflow";
	public static final String PROPERTY_HIDE_260_230_QC_WORKFLOW = "qc_hide_260_230_qc_workflow";
	public static final String PROPERTY_SAMPLE_BATCH_WARNING = "sample_batch_warning";
	public static final String PLATE_AND_RUN_VIEW_LIMIT = "view_limit_plates_and_runs";
	public static final String CHROMATOGRAM_VIEW_LIMIT = "view_limit_chromatograms";
	public static final String DISK_USAGE_BILLING_CORE = "disk_usage_billing_core";
	public static final String CORE_BILLING_OFFICE = "core_billing_office";
	public static final String RESTRICT_ANNOTATION_ON_AUTO_ADD = "restrict_annotation_on_auto_add";
	public static final String DESCRIPTION_NAME_MANDATORY_FOR_INTERNAL_EXPERIMENTS = "description_name_mandatory_for_internal_experiments";
	public static final String SORT_APPLICATION_ALPHABETICALLY_IN_EDIT = "sort_application_alphabetically_in_edit";
	public static final String ADD_PHI_TO_SUPPORT_EMAIL = "add_phi_to_support_email";
	public static final String EXPERIMENT_INVOICE_EMAIL_TEMPLATE = "experiment_invoice_email_template";
	public static final String EXPERIMENT_CONFIRMATION_EMAIL_TEMPLATE = "experiment_confirmation_email_template";
	public static final String SHOW_ACTIVITY_DASHBOARD = "show_activity_dashboard";
	public static final String USE_INVOICE_NUMBERING = "use_invoice_numbering";
	public static final String SAMPLE_CONCENTRATION_PRECISION = "sample_concentration_precision";
	public static final String STATUS_TO_USE_PRODUCTS = "status_to_use_products";
	public static final String PROPERTY_NO_PRODUCTS_MESSAGE = "no_products_message";
	public static final String ANALYSIS_VIEW_LIMIT = "view_limit_analyses";
	public static final String DATATRACK_VIEW_LIMIT = "view_limit_datatracks";
	public static final String ISOLATION_DEFAULT_PRICE_CATEGORY = "isolation_default_price_category";
	public static final String SEQUENOM_DEFAULT_PRICE_CATEGORY = "sequenom_default_price_category";
	public static final String PRODUCT_SHEET_NAME = "product_sheet_name";
	public static final String REQUEST_PROPS_ON_CONFIRM_TAB = "request_props_on_confirm_tab";
	public static final String NEW_REQUEST_SAVE_BEFORE_SUBMIT = "new_request_save_before_submit";
	public static final String USE_EXTERNAL_EXPERIMENT_NUMBERING = "use_external_experiment_numbering";
	public static final String GET_REQUEST_NUMBER_PROCEDURE_EXTERNAL = "get_request_number_procedure_external";
	public static final String PROTECTED_DIRECTORIES_ANALYSIS = "protected_directories_analysis";
	public static final String PROTECTED_DIRECTORIES_EXPERIMENT = "protected_directories_experiment";
	public static final String FAST_BROWSE_EXPERIMENTS = "fast_browse_experiments";

	public static final String USE_ALT_REPOSITORY = "use_alternative_repository";
	public static final String ANALYSIS_DIRECTORY_ALT = "analysis_directory_alternative";
	public static final String ANALYSIS_ALTERNATIVE = "analysis_alternative";

	public static final String EXPERIMENTALIAS = "experiment_alias";

	private Integer idPropertyDictionary;
	private String propertyName;
	private String propertyValue;
	private String propertyDescription;
	private String forServerOnly;
	private Integer idCoreFacility;
	private String codeRequestCategory;

	public Integer getIdPropertyDictionary() {
		return idPropertyDictionary;
	}

	public void setIdPropertyDictionary(Integer idPropertyDictionary) {
		this.idPropertyDictionary = idPropertyDictionary;
	}

	public String getValue() {
		return idPropertyDictionary.toString();
	}

	public String getDisplay() {
		String postfix = "";
		if (getIdCoreFacility() != null) {
			postfix = DictionaryManager.getDisplay("hci.gnomex.model.CoreFacility", getIdCoreFacility().toString());
			if (postfix == null) {
				postfix = "";
			} else {
				postfix = "(" + postfix + ")";
			}
		}
		return getPropertyName() + postfix;
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

	public Integer getIdCoreFacility() {
		return idCoreFacility;
	}

	public void setIdCoreFacility(Integer id) {
		idCoreFacility = id;
	}

	public String getCodeRequestCategory() {
		return codeRequestCategory;
	}

	public void setCodeRequestCategory(String codeRequestCategory) {
		this.codeRequestCategory = codeRequestCategory;
	}
}
