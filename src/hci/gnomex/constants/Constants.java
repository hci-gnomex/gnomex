package hci.gnomex.constants;

import java.text.DecimalFormat;
import java.util.regex.Pattern;


public class Constants {
  public static final String              DATA_SOURCES                     = "config/data-sources.xml";  

  public static final String              APP_NAME                        = "GNomEx";

  public static final String              LAUNCH_APP_JSP                  = "/gnomexFlex.jsp";
  public static final String              SHOW_REQUEST_FORM               = "/ShowRequestForm.gx";
  public static final String              UPLOAD_QUOTE_JSP                = "/uploadQuoteInfo.jsp";
  public static final String              APPROVE_USER_SERVLET            = "/ApproveUser.gx";
  public static final String              APPROVE_BILLING_ACCOUNT_SERVLET = "/ApproveBillingAccount.gx";
  public static final String              VERIFY_EMAIL_SERVLET            = "/ConfirmEmail.gx";

  public static final String              DOWNLOAD_SINGLE_FILE_SERVLET           = "DownloadSingleFileServlet.gx";
  public static final String              DOWNLOAD_ANALYSIS_SINGLE_FILE_SERVLET  = "DownloadAnalysisSingleFileServlet.gx";
  public static final String              DOWNLOAD_CHROMATOGRAM_FILE_SERVLET     = "DownloadChromatogramFileServlet.gx";

  public static final boolean             REQUIRE_SECURE_REMOTE           = true;

  public static final String              SQL_SERVER                      = "SQL SERVER";

  public static final String              MAIL_SESSION                    = "mail/MailSession";

  public static final String              INVOICE_FORM_CSS                = "css/invoiceForm.css";
  public static final String              REQUEST_FORM_CSS                = "css/requestForm.css";
  public static final String              REQUEST_FORM_EMAIL_CSS          = "css/requestFormEmail.css";
  public static final String              REQUEST_FORM_PRINT_CSS          = "css/requestFormPrint.css";
  public static final String              REQUEST_FORM_PRINT_INSTRUCTIONS_CSS  = "css/requestFormPrintAll.css";
  public static final String              EMAIL_NOTIFY_CSS                = "css/emailNotify.css";
  public static final String              PLATE_REPORT_CSS                = "css/plateReport.css";

  public static final String              LOGGING_PROPERTIES              = "gnomex_logging.properties";

  public static final String              WINDOW_TRACK_REQUESTS           = "ExperimentDetail";
  public static final String              WINDOW_TRACK_ANALYSES           = "AnalysisDetail";
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
  public static final String              MATERIAL_QUOTE_DIR              = "material_quote";
  public static final String              REQUISITION_DIR                 = "requisition";
  public static final String              DOWNLOAD_KEY_SEPARATOR          = "\t";

  public static DecimalFormat              concentrationFormatter = new DecimalFormat("######.##");

  public static final String              BILLING_SPLIT_TYPE_PERCENT_CODE = "%";
  public static final String              BILLING_SPLIT_TYPE_AMOUNT_CODE  = "$";

  // DataTrack Constants
  public static final String SEQUENCE_DIR_PREFIX    = "SEQ";

  public static final int MAXIMUM_NUMBER_TEXT_FILE_LINES = 10000;

  public static final String UCSC_URL = "http://genome.ucsc.edu";
  //public static final int DAYS_TO_KEEP_URL_LINKS= 7; //We used to delete links based on time, we are now allowing customers to delete whenever they want
  public static final String URL_LINK_DIR_NAME = "URLLinks";
  public static final String IGV_LINK_DIR_NAME = "IGVLinks";
  public static final String UCSC_EXECUTABLE_DIR_NAME = "UCSCExecutables";

  public static final String UCSC_WIG_TO_BIG_WIG_NAME = "wigToBigWig";
  public static final String UCSC_BED_TO_BIG_BED_NAME = "bedToBigBed";
  public static final String SAMTOOLS_BGZIP_NAME = "bgzip";
  public static final String SAMTOOLS_TABIX_NAME = "tabix";

  public static final Pattern HTML_BRACKETS = Pattern.compile("<[^>]+>");

  public static final String FDT_DOWNLOAD_INFO_FILE_NAME = "fdtDownloadInfoFile.txt";

  /*Keep lower case.*/
  public static final String[] DATATRACK_FILE_EXTENSIONS = new String[] {
    ".bam.bai",    //this must precede ".bam"  and ".bai"                                                   
    ".bam",
    ".bai",
    ".useq",
    ".bb", 
    ".bw",
    ".bed",
    ".bgr",
    ".brs",
    ".gff", 
    ".gtf",
    ".vcf.gz",
    ".vcf.gz.tbi",
  };

  public static final String[] DATATRACK_FILE_EXTENSIONS_NO_INDEX = new String[] {                                                  
	    ".bam",
	    ".useq",
	    ".bb", 
	    ".bw",
	    ".bed",
	    ".bgr",
	    ".brs",
	    ".gff", 
	    ".gtf",
	    ".vcf.gz",
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
    ".bai",
    ".vcf.gz",
    ".vcf.gz.tbi",
    ".useq",
  };

  /**xxx.ext for bigBed, bigWig, bam that can be accessed via http.*/
  public static final String[] FILE_EXTENSIONS_FOR_UCSC_LINKS_NO_INDEX = new String[] {
    ".bb",
    ".bw",
    ".bam",
    ".vcf.gz",
    ".useq",
  };

  public static final String[] FILE_EXTENSIONS_FOR_IGV_LINKS = new String[] {
    ".bb",
    ".bw",
    ".bam",
    ".bai",
    ".vcf.gz",
    ".vcf.gz.tbi",
  };

  public static final String[] FILE_EXTENSIONS_FOR_IGV_LINKS_NO_INDEX = new String[] {
	    ".bb",
	    ".bw",
	    ".bam",
	    ".vcf.gz",
	  };

  public static final String[] FILE_EXTENSIONS_FOR_BAMIOBIO_LINKS = new String[] {
    ".bam",
    ".bai",
    ".vcf.gz",
    ".vcf.gz.tbi",
  };

  public static final String[] FILE_EXTENSIONS_FOR_BAMIOBIO_LINKS_NO_INDEX = new String[] {
	    ".bam",
	    ".vcf.gz",
	  };

  public static final String[] FILE_EXTENSIONS_FOR_VCFIOBIO_LINKS = new String[] {
    ".vcf.gz",
  };

  
  public static final String[] FILE_EXTENSIONS_FOR_VIEW = new String[] {
    ".pdf",
    ".jpg",
    ".png",
    ".gif",
    ".rtf",
    ".txt",
    ".html",
    ".htm",
    ".csv",
    ".ppt",
    ".pptx",
    ".xls",
    ".xlsx",
    ".xml",
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

  public static final String[] DELETE_OLD_EXPERIMENT_AND_ANALYSIS_FILES_CSS = new String[] {
    "body {",
    "  font-family: arial,Helvetica,sans-serif;",
    "  font-size: 9pt;",
    "  width: 960;",
    "  text-align: left;",
    "  margin-left: auto;",
    "  margin-right: auto;",
    "}",
    "",
    "table {",
    "  width: 960;",
    "}",
    "",
    "table.grid {",
    "  border: none;",
    "  width: 960;",
    "}",
    "",
    "hr {",
    "  width: 960;",
    "  text-align: left;",
    "  color: #9E9E9E;",
    "  background-color: #9E9E9E;",
    "  margin-bottom: 10px;",
    "}",
    "",
    "H3 {",
    "  font-size: 10pt;",
    "  font-weight: bold;",
    "  padding-top: 0;",
    "  padding-right: 8;",
    "  padding-bottom: 0;",
    "}",
    "",
    "caption{",
    "  font-size: 10pt;",
    "  color: black;",
    "  font-weight: bold;",
    "  padding-top: 15;",
    "  padding-bottom: 5;",
    "  text-align: left;",
    "}",
    "",
    "td.value {",
    "  font-size: 10pt;",
    "  padding-top: 0;",
    "  padding-bottom: 0;",
    "}",
    "",
    "td.label {",
    "  font-size: 9pt;",
    "  font-weight: bold;",
    "  padding-top: 0;",
    "  padding-right: 8;",
    "  padding-bottom: 0;",
    "}",
    "",
    "td.note {",
    "  font-size: 10pt;",
    "  padding-top: 4;",
    "  padding-bottom: 0;",
    "  padding-right: 8;",
    "  padding-left: 4;",
    "}",
    "",
    "td.grid {",
    "  font-size: 9pt;",
    "  padding-top: 4;",
    "  padding-bottom: 0;",
    "  padding-right: 8;",
    "  padding-left: 4;",
    "}",
    "",
    "td.gridrelated {",
    "  font-size: 8pt;",
    "  font-style: italic;",
    "  padding-top: 0;",
    "  padding-bottom: 0;",
    "  padding-right: 8;",
    "  padding-left: 4;",
    "  cell",
    "}",
    "",
    "td.gridright {",
    "  font-size: 9pt;",
    "  text-align: RIGHT;",
    "  padding-top: 4;",
    "  padding-bottom: 0;",
    "  padding-right: 8;",
    "  padding-left: 4;",
    "}",
    "td.gridrightrelated {",
    "  font-size: 8pt;",
    "  font-style: italic;",
    "  text-align: RIGHT;",
    "  padding-top: 0;",
    "  padding-bottom: 0;",
    "  padding-right: 8;",
    "  padding-left: 4;",
    "}",
    "",
    "td.gridcenter {",
    "  font-size: 9pt;",
    "  text-align: CENTER;",
    "  padding-top: 4;",
    "  padding-bottom: 0;",
    "  padding-right: 8;",
    "  padding-left: 4;",
    "}",
    "",
    "td.gridtotal {",
    "  font-size: 9pt;",
    "  font-weight: bold;",
    "  text-align: RIGHT;",
    "  padding-top: 4;",
    "  padding-bottom: 0;",
    "  padding-right: 8;",
    "  padding-left: 4;",
    "  border-color: #CDCDC1;",
    "  border-top: thin  solid;",
    "  border-width: 1;",
    "}",
    "",
    "td.gridempty {",
    "  width: 150;",
    "}",
    "",
    "td.gridemptysmall {",
    "  border-color: #CDCDC1;",
    "  width: 50;",
    "}",
    "",
    "th {",
    "  font-size: 9pt;",
    "  font-weight: bold;",
    "  text-decoration: underline;",
    "  padding-top: 8;",
    "  padding-bottom: 0;",
    "  padding-right: 8;",
    "  padding-left: 4;",
    "  text-align: left;",
    "  border: none;",
    "}",
    "",
    "th.right {",
    "  text-align: right;",
    "}",
    "",
    "  a {",
    "  font-size: 9pt;",
    "  text-align: left;",
    "}"
  };



}
