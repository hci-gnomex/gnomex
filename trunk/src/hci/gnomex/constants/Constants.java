package hci.gnomex.constants;


public class Constants {
  
  public static final String              APP_NAME                        = "GNomEx";
  
  public static final String              LAUNCH_APP_JSP                  = "/gnomexFlex.jsp";
  
  public static final String              QC_DIRECTORY                    = "bioanalysis";
  
  public static final String              PRODUCTION_SERVER               = "hci-as1.hci.utah.edu";
  
  public static final String              DEVELOPER_EMAIL                 = "tony.disera@hci.utah.edu";
  

  public static final String              EMAIL_BIOINFORMATICS_MICROARRAY = "bioinformaticscore@utah.edu";
  public static final String              EMAIL_MICROARRAY_CORE_FACILITY  = "brian.dalley@hci.utah.edu";
  
  public static final String              WINDOW_TRACK_REQUESTS           = "ExperimentDetail";
  public static final String              WINDOW_FETCH_RESULTS            = "ExperimentDownload";

  public static final String              WINDOW_NAME_TRACK_REQUESTS      = "Experiment Detail";
  public static final String              WINDOW_NAME_FETCH_RESULTS       = "Experiment Download";

  public static final  String             STATUS_IN_PROGRESS              = "In Progress";
  public static final  String             STATUS_COMPLETED                = "Completed";
  public static final  String             STATUS_TERMINATED               = "Terminated";
  public static final  String             STATUS_BYPASSED                 = "Bypassed";

  public static final String              LUCENE_INDEX_DIRECTORY                  = "c:/orion/luceneIndexGnomEx";

  public static final String              LUCENE_EXPERIMENT_INDEX_DIRECTORY       = "c:/orion/luceneIndexGnomEx/Experiment";
  public static final String              LUCENE_PROTOCOL_INDEX_DIRECTORY         = "c:/orion/luceneIndexGnomEx/Protocol";

  private static final String              MICROARRAY_DIRECTORY            = "\\\\hci-ma\\MicroarrayDataShare\\";
  private static final String              TEST_MICROARRAY_DIRECTORY       = "C:\\temp\\MicroarrayDataShare01\\";
  private static final String              ANALYSIS_DIRECTORY              = "\\\\hci-ma\\AnalysisDataShare\\";
  private static final String              TEST_ANALYSIS_DIRECTORY         = "C:\\temp\\AnalysisDataShare01\\";

  public static String getAnalysisDirectory(String serverName) {
    if (serverName.equals(PRODUCTION_SERVER)) {
      return ANALYSIS_DIRECTORY;
    } else {
      return TEST_ANALYSIS_DIRECTORY;
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

}
