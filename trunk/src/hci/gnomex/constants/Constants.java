package hci.gnomex.constants;

import java.text.DecimalFormat;


public class Constants {
  
  public static final String              APP_NAME                        = "GNomEx";
  
  public static final String              LAUNCH_APP_JSP                  = "/gnomexFlex.jsp";
  
  public static final String              QC_DIRECTORY                    = "bioanalysis";
   
  public static final String              PRODUCTION_SERVER               = "hci-as1.hci.utah.edu";
  
  public static final String              INVOICE_FORM_CSS                = "applications/gnomex/gnomex/invoiceForm.css";
  public static final String              REQUEST_FORM_CSS                = "applications/gnomex/gnomex/requestForm.css";
  
  public static final String              DEVELOPER_EMAIL                 = "tony.disera@hci.utah.edu";
  

  public static final String              EMAIL_BIOINFORMATICS_MICROARRAY = "bioinformaticscore@utah.edu";
  public static final String              EMAIL_MICROARRAY_CORE_FACILITY  = "brian.dalley@hci.utah.edu";
  
  public static final String              PHONE_MICROARRAY_CORE_FACILITY  = "(801-585-7192).";
  
  public static final String              WINDOW_TRACK_REQUESTS           = "ExperimentDetail";
  public static final String              WINDOW_FETCH_RESULTS            = "ExperimentDownload";

  public static final String              WINDOW_NAME_TRACK_REQUESTS      = "Experiment Detail";
  public static final String              WINDOW_NAME_FETCH_RESULTS       = "Experiment Download";

  public static final  String             STATUS_IN_PROGRESS              = "In Progress";
  public static final  String             STATUS_COMPLETED                = "Completed";
  public static final  String             STATUS_TERMINATED               = "Terminated";
  public static final  String             STATUS_BYPASSED                 = "Bypassed";
  
  public static final  String             FLOWCELL_DIRECTORY_FLAG         = "FC";

  public static final String              LUCENE_INDEX_DIRECTORY                  = "c:/orion/luceneIndexGnomEx";

  public static final String              LUCENE_EXPERIMENT_INDEX_DIRECTORY       = "c:/orion/luceneIndexGnomEx/Experiment";
  public static final String              LUCENE_PROTOCOL_INDEX_DIRECTORY         = "c:/orion/luceneIndexGnomEx/Protocol";
  public static final String              LUCENE_ANALYSIS_INDEX_DIRECTORY         = "c:/orion/luceneIndexGnomEx/Analysis";

  public static final String              BILLING_MICROARRAY_BUSINESS_UNIT         = "01"; 
  public static final String              BILLING_MICROARRAY_ACCOUNT               = "40420";
  public static final String              BILLING_MICROARRAY_FUND                  = "2000";
  public static final String              BILLING_MICROARRAY_ORG                   = "01219";
  public static final String              BILLING_MICROARRAY_ACTIVITY              = "10292";
  
  
  private static final String             MICROARRAY_DIRECTORY            = "\\\\hci-ma\\MicroarrayData$\\";
  private static final String             TEST_MICROARRAY_DIRECTORY       = "C:\\temp\\MicroarrayData01\\";
  private static final String             ANALYSIS_DIRECTORY              = "\\\\hci-ma\\AnalysisDataShare$\\";
  private static final String             TEST_ANALYSIS_DIRECTORY         = "C:\\temp\\AnalysisDataShare01\\";
  private static final String             FLOWCELL_DIRECTORY              = "\\\\hci-ma\\FlowCellDataShare$\\";
  private static final String             TEST_FLOWCELL_DIRECTORY         = "C:\\temp\\FlowCellDataShare01\\";
  
  

  
  
  public static DecimalFormat               concentrationFormatter = new DecimalFormat("######.##");

  public static String getAnalysisDirectory(String serverName) {
    if (serverName.equals(PRODUCTION_SERVER)) {
      return ANALYSIS_DIRECTORY;
    } else {
      return TEST_ANALYSIS_DIRECTORY;
    }
  }
  
  public static String getFlowCellDirectory(String serverName) {
    if (serverName.equals(PRODUCTION_SERVER)) {
      return FLOWCELL_DIRECTORY;
    } else {
      return TEST_FLOWCELL_DIRECTORY;
    }
  }

  public static String getMicroarrayDirectoryForWriting(String serverName) {
    if (serverName.equals(PRODUCTION_SERVER)) {
      return MICROARRAY_DIRECTORY;
    } else {
      return TEST_MICROARRAY_DIRECTORY;
    }
  }

  public static String getMicroarrayDirectoryForReading(String serverName) {
    return MICROARRAY_DIRECTORY;
  }
  
  public static int getAnalysisDirectoryNameLength() {
    return ANALYSIS_DIRECTORY.length();
  }
  public static int getMicroarrayDirectoryNameLength() {
    return MICROARRAY_DIRECTORY.length();
  }
  public static int getFlowCellDirectryNameLength() {
    return FLOWCELL_DIRECTORY.length();
  }
  
  
  public static String parseMainFolderName(String fileName) {
    String mainFolderName = "";
    String baseDir = "";
    
    if (fileName.indexOf(MICROARRAY_DIRECTORY) >= 0) {
      baseDir = MICROARRAY_DIRECTORY;
    } else if (fileName.indexOf(TEST_MICROARRAY_DIRECTORY) >= 0) {
      baseDir = TEST_MICROARRAY_DIRECTORY;
    } else if (fileName.indexOf(FLOWCELL_DIRECTORY) >= 0) {
      baseDir = FLOWCELL_DIRECTORY;
    } else if (fileName.indexOf(TEST_FLOWCELL_DIRECTORY) >= 0) {
      baseDir = TEST_FLOWCELL_DIRECTORY;
    }

  
    
    String relativePath = fileName.substring(baseDir.length() + 5);
    String tokens[] = relativePath.split("/", 2);
    if (tokens == null || tokens.length == 1) {
      tokens = relativePath.split("\\\\", 2);
    }
    if (tokens.length == 2) {
      mainFolderName = tokens[0];
    }
    
    return mainFolderName;
  }
    
      
}
