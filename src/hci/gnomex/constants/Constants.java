package hci.gnomex.constants;

import hci.gnomex.utility.DictionaryHelper;

import java.text.DecimalFormat;


public class Constants {
  public static final String              DATA_SOURCES                     = "config/data-sources.xml";  
  
  public static final String              APP_NAME                        = "GNomEx";
  
  public static final String              LAUNCH_APP_JSP                  = "/gnomexFlex.jsp";
  public static final String              SHOW_REQUEST_FORM               = "/ShowRequestForm.gx";
  
  public static final String              DOWNLOAD_SINGLE_FILE_SERVLET           = "DownloadSingleFileServlet.gx";
  public static final String              DOWNLOAD_ANALYSIS_SINGLE_FILE_SERVLET  = "DownloadAnalysisSingleFileServlet.gx";
  
  public static final boolean            REQUIRE_SECURE_REMOTE           = true;
  
  public static final String              SQL_SERVER                      = "SQL SERVER";
  
  public static final String              MAIL_SESSION                    = "mail/MailSession";

  public static final String              INVOICE_FORM_CSS                = "css/invoiceForm.css";
  public static final String              REQUEST_FORM_CSS                = "css/requestForm.css";
  public static final String              REQUEST_FORM_EMAIL_CSS          = "css/requestFormEmail.css";
  public static final String              REQUEST_FORM_PRINT_CSS          = "css/requestFormPrint.css";
  public static final String              REQUEST_FORM_PRINT_INSTRUCTIONS_CSS  = "css/requestFormPrintAll.css";
  public static final String              EMAIL_NOTIFY_CSS                = "css/emailNotify.css";

  public static final String              LOGGING_PROPERTIES              = "gnomex_logging.properties";
  
  public static final String              WINDOW_TRACK_REQUESTS           = "ExperimentDetail";
  public static final String              WINDOW_FETCH_RESULTS            = "ExperimentDownload";
  public static final String              WINDOW_BILLING_ACCOUNT_DETAIL   = "BillingAccountDetail";

  public static final String              WINDOW_NAME_TRACK_REQUESTS      = "Experiment Detail";
  public static final String              WINDOW_NAME_FETCH_RESULTS       = "Experiment Download";

  public static final  String             STATUS_IN_PROGRESS              = "In Progress";
  public static final  String             STATUS_COMPLETED                = "Completed";
  public static final  String             STATUS_TERMINATED               = "Terminated";
  public static final  String             STATUS_BYPASSED                 = "Bypassed";
  public static final  String             STATUS_ON_HOLD                  = "On Hold";
  
  public static final String              AMEND_QC_TO_MICROARRAY          = "MicroarrayAmendState";
  public static final String              AMEND_QC_TO_SEQ                 = "SolexaBaseAmendState";
  public static final String              AMEND_ADD_SEQ_LANES             = "SolexaLaneAmendState";
  
  public static final String              UPLOAD_STAGING_DIR              = "upload_staging";
  
  public static DecimalFormat              concentrationFormatter = new DecimalFormat("######.##");
  
  // DataTrack Constants
  public static final String SEQUENCE_DIR_PREFIX    = "SEQ";

  public static final int MAXIMUM_NUMBER_TEXT_FILE_LINES = 10000;
  
  public static final String UCSC_URL = "http://genome.ucsc.edu";
  public static final int DAYS_TO_KEEP_URL_LINKS= 7;
  public static final String UCSC_URL_LINK_DIR_NAME = "UCSCLinks";
  public static final String UCSC_EXECUTABLE_DIR_NAME = "UCSCExecutables";
  public static final String UCSC_WIG_TO_BIG_WIG_NAME = "wigToBigWig";
  public static final String UCSC_BED_TO_BIG_BED_NAME = "bedToBigBed";

  public static final String[] DATATRACK_FILE_EXTENSIONS = new String[] 
                                                                {
    ".bar",
    ".bam",
    ".bai",
    ".bed",
    ".bgn",
    ".bgr",
    ".bps",
    ".bp1",
    ".bp2",
    ".brs",
    ".cyt",
    ".ead",
    ".gff", 
    ".gtf",
    ".psl",
    ".useq",
    ".bulkUpload",
    ".bb", 
    ".bw"
                                                                };

  public static final String[] FILE_EXTENSIONS_TO_CHECK_SIZE_BEFORE_UPLOADING = new String[] {
    ".bed", 
    ".bgn", 
    ".gff", 
    ".gtf", 
    ".psl", 
  };

  /**xxx.ext for bigBed, bigWig, bam that can be accessed via http.*/
  public static final String[] FILE_EXTENSIONS_FOR_UCSC_LINKS = new String[] {
    ".bb",
    ".bw",
    ".bam",
    ".bai"
  };

  public static final String[] SEQUENCE_FILE_EXTENSIONS = new String[] 
                                                              {
    ".bnib", 
    ".fasta",
                                                              };
  
  
  public static final int ERROR_CODE_OTHER                     = 901;
  public static final int ERROR_CODE_UNSUPPORTED_FILE_TYPE     = 902;
  public static final int ERROR_CODE_INCORRECT_FILENAME        = 903;
  public static final int ERROR_CODE_INSUFFICIENT_PERMISSIONS  = 904;
  public static final int ERROR_CODE_FILE_TOO_BIG              = 905;
  public static final int ERROR_CODE_MALFORMED_BAM_FILE        = 906;
  public static final int ERROR_CODE_INVALID_NAME              = 907;
  public static final int ERROR_CODE_BULK_FILE_UPLOAD          = 908;

 
    
      
}
