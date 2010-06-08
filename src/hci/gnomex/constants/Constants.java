package hci.gnomex.constants;

import hci.gnomex.utility.DictionaryHelper;

import java.text.DecimalFormat;


public class Constants {
  
  public static final String              APP_NAME                        = "GNomEx";
  
  public static final String              LAUNCH_APP_JSP                  = "/gnomexFlex.jsp";
  
  public static final boolean            REQUIRE_SECURE_REMOTE           = true;
  
  public static final String              SQL_SERVER                      = "SQL SERVER";
  
  public static final String              WEBCONTEXT_DIR                  = "applications/gnomex/gnomex/";
  public static final String              INVOICE_FORM_CSS                = "css/invoiceForm.css";
  public static final String              REQUEST_FORM_CSS                = "css/requestForm.css";
  public static final String              EMAIL_NOTIFY_CSS                = "css/emailNotify.css";

  public static final String              DATA_SOURCES                     = "config/data-sources.xml";  
 
  public static final String              LOGGING_PROPERTIES              = "gnomex_logging.properties";
  
  public static final String              WINDOW_TRACK_REQUESTS           = "ExperimentDetail";
  public static final String              WINDOW_FETCH_RESULTS            = "ExperimentDownload";

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
  
  
  public static DecimalFormat              concentrationFormatter = new DecimalFormat("######.##");

 
    
      
}
