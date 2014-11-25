package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.security.UnknownPermissionException;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.Lab;
import hci.gnomex.model.Organism;
import hci.gnomex.model.Property;
import hci.gnomex.model.PropertyPlatformApplication;
import hci.gnomex.model.ReportTrayList;
import hci.gnomex.model.RequestSampleFilter;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.report.constants.ReportFormats;
import hci.report.model.Column;
import hci.report.model.ReportRow;
import hci.report.model.ReportTray;
import hci.report.utility.ReportCommand;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;

/*
 * This class will generate a report (excel spreadsheet) show how complete the annotations
 * are for each lab.
 * 
 * The summary report provide experiment counts by lab, showing how many experiments
 * are fully annotated and how many are missing annotations.
 * 
 * The detail report lists the experiments that are fully annotated followed by a list
 * of experiments that are missing annotations.  Each experiment shows a list of the
 * annotations that are missing.
 */
public class ShowAnnotationProgressReport extends ReportCommand implements Serializable {

  private static org.apache.log4j.Logger    log = org.apache.log4j.Logger.getLogger(ShowAnnotationProgressReport.class);

  private SecurityAdvisor                   secAdvisor;

  private RequestSampleFilter               sampleFilter;

  public  ReportTrayList                    reportTrayList = new ReportTrayList();
  private static String                     REPORT_SUMMARY = "summary";
  private static String                     REPORT_DETAIL_INCOMPLETE  = "incomplete";
  private static String                     REPORT_DETAIL_COMPLETE    = "complete";

  private static String                     SAMPLE_FILE_LINK = "Link Samples to Files";




  // Hash of each sample and its annotation values (map). Contains all sample annotations
  // for entire result set.
  private TreeMap<Integer, Map>             propertyEntryAnnotationMap = new TreeMap<Integer, Map>();

  // Hash of each sample that is linked to at least one SampleExperimentFile (read1 or read2)
  private HashMap<Integer, Object>          sampleLinkedToFileMap = new HashMap<Integer, Object>();

  //Hash of each request that has at least one experiment file
  private HashMap<Integer, Object>          experimentWithFileMap = new HashMap<Integer, Object>();

  // List of required properties
  private ArrayList<Property>               requiredProperties = new ArrayList<Property>();

  // List of required properties for a given experiment (considering experiment platform, organism)
  private ArrayList<Property>               requiredPropertiesForExperiment = new ArrayList<Property>();

  // Hash of each property name (annotation) missing for a given experiment
  private TreeMap<String, List<String>>     missingAnnotationMapForExperiment = new TreeMap<String, List<String>>();

  // Hash of each complete (fully annotated) experiment.
  // Contains experiments for a given lab.
  private TreeMap<Integer, ExperimentInfo>  completeExperimentMapForLab = new TreeMap<Integer, ExperimentInfo>();

  // Hash of each incomplete experiment and its missing annotations (property names).
  // Contains experiment for a given lab.
  private TreeMap<Integer, ExperimentInfo>  incompleteExperimentMapForLab = new TreeMap<Integer, ExperimentInfo>();

  // The final hash that we will use to generate the report.  Hash of each lab
  // with its LabInfo, which contains the completeExperimentMap and incompleteExperimentMap
  private TreeMap<String, LabInfo>          labInfoMap = new TreeMap<String, LabInfo>();


  private Integer                           idLab;

  private String                            today;

  private static final Boolean              IS_CREATE_REPORT = true;

  public void validate() {
  }

  public void loadCommand(HttpServletRequest request, HttpSession session) {

    if (request.getParameter("idLab") != null && !request.getParameter("idLab").equals("")) {
      idLab = Integer.valueOf(request.getParameter("idLab"));
    }

    sampleFilter = new RequestSampleFilter();
    this.loadDetailObject(request, sampleFilter);

    secAdvisor = (SecurityAdvisor)session.getAttribute(SecurityAdvisor.SECURITY_ADVISOR_SESSION_KEY);
    if (secAdvisor == null) {
      this.addInvalidField("secAdvisor", "A security advisor must be created before this command can be executed.");
    }

    today = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());

  }

  @SuppressWarnings("unchecked")
  public Command execute() throws RollBackCommandException {

    this.SUCCESS_JSP_XLS = "/report_xls_annot_progress.jsp";
    this.ERROR_JSP = "/message.jsp";


    try {

      Session sess = secAdvisor.getReadOnlyHibernateSession(this.getUsername());
      DictionaryHelper dh = DictionaryHelper.getInstance(sess);

      // Check permissions
      if (this.secAdvisor.hasPermission(SecurityAdvisor.CAN_ACCESS_ANY_OBJECT)) {
        // No restrictions for admins
      } else {
        if (idLab != null) {
          if (!this.secAdvisor.isGroupIAmMemberOrManagerOf(idLab)) {
            throw new RuntimeException("Insufficient permissions to access experiments for lab " + idLab );
          }
        }
      }

      // Get the lab if one was provided as a parameter.  we will use this
      // to format the excel spreadsheet file na,e
      String labQualifier = "";
      if (idLab != null) {
        Lab lab = (Lab)sess.get(Lab.class, idLab);
        labQualifier += "_" + lab.getLastName();
      }
      String fileName = "gnomex_annotation_progress" + "_" + labQualifier + "_" + today;
      reportTrayList.setFileName(fileName);



      // Get list of all required properties.  This will be used to generate the column headers.
      this.getRequiredProperties(dh, null, requiredProperties);

      // Write a query to get all samples for all experiments.
      // (Row for each sample)
      StringBuffer queryBuf = sampleFilter.getQuery(secAdvisor, IS_CREATE_REPORT, new ArrayList());
      List<Object[]> sampleRows = (List<Object[]>)sess.createQuery(queryBuf.toString()).list();

      // Write a query to get all sample annotations for all experiments
      // (Row for each annotation on each sample)
      queryBuf = sampleFilter.getAnnotationQuery(secAdvisor, IS_CREATE_REPORT);
      ShowAnnotationReport.hashAnnotations(sess,
          dh,
          queryBuf,
          ShowAnnotationReport.TARGET_SAMPLE,
          propertyEntryAnnotationMap);

      // Run a query to get all samples that are linked to ExperimentSampleFiles
      queryBuf = sampleFilter.getSampleFileLinkQuery(secAdvisor, IS_CREATE_REPORT);
      List<Object[]> sampleFileRows = (List<Object[]>)sess.createQuery(queryBuf.toString()).list();
      for (Object[] sampleFileRow : sampleFileRows) {
        this.sampleLinkedToFileMap.put((Integer)sampleFileRow[RequestSampleFilter.COL_ID_SAMPLE], null);
      }

      // Run a query to get all experiments that have at least one experiment file
      queryBuf = sampleFilter.getExperimentFileQuery(secAdvisor, IS_CREATE_REPORT);
      List<Object[]> experimentFileRows = (List<Object[]>)sess.createQuery(queryBuf.toString()).list();
      for (Object[] experimentFileRow : experimentFileRows) {
        this.experimentWithFileMap.put((Integer)experimentFileRow[RequestSampleFilter.COL_ID_REQUEST], null);
      }


      // For every sample row
      Integer prevIdRequest = new Integer(-1);
      String prevLabName = "";
      ExperimentInfo prevExperimentInfo = null;
      boolean firstTime = true;
      int sampleCount = 0;

      for(Object[] row : sampleRows) {

        Integer idSample =  (Integer)row[RequestSampleFilter.COL_ID_SAMPLE];
        String labLastName = (String)row[RequestSampleFilter.COL_LAB_LAST_NAME];
        String labFirstName = (String)row[RequestSampleFilter.COL_LAB_FIRST_NAME];
        String submitterLastName = (String)row[RequestSampleFilter.COL_SUBMITTER_LAST_NAME];
        String submitterFirstName = (String)row[RequestSampleFilter.COL_SUBMITTER_FIRST_NAME];
        Integer idRequest = (Integer)row[RequestSampleFilter.COL_ID_REQUEST];
        String requestNumber = (String)row[RequestSampleFilter.COL_REQUEST_NUMBER];
        Integer idOrganism = (Integer)row[RequestSampleFilter.COL_SAMPLE_ID_ORGANISM];
        String codeRequestCategory = (String)row[RequestSampleFilter.COL_CODE_REQUEST_CATEGORY];
        String codeApplication = (String)row[RequestSampleFilter.COL_CODE_APPLICATION];
        String labName = Lab.formatLabNameFirstLast(labFirstName, labLastName);
        String sampleNumber = (String)row[RequestSampleFilter.COL_SAMPLE_NUMBER];


        // Get sample source
        Map<String, String>annotationMap = this.propertyEntryAnnotationMap.get(idSample);
        String sampleSource = annotationMap != null ? annotationMap.get("SAMPLE SOURCE") : "";

        ExperimentInfo experimentInfo = new ExperimentInfo();
        experimentInfo.requestNumber       = requestNumber;
        experimentInfo.idRequest           = idRequest;
        experimentInfo.idOrganism          = idOrganism;
        experimentInfo.codeRequestCategory = codeRequestCategory;
        experimentInfo.codeApplication     = codeApplication;
        experimentInfo.labName             = labName;
        experimentInfo.submitter           = AppUser.formatName(submitterLastName, submitterFirstName);
        experimentInfo.sampleSource        = sampleSource;




        // When we encounter a new experiment
        // 1. We have information about the annotations that are missing on the previous.
        //    Classify the previous experiment it as either a fully annotated or
        //    incomplete annotation. (Hash the experiment)
        // 2. Get a new set of required annotations for the current experiment.
        //    This will be used on each iteration of the results set (each sample)
        //    to determine what is annotations are missing.
        if (!prevIdRequest.equals(idRequest)) {
          if (!firstTime) {
            // If there is only one sample on the experiment, don't consider replica count
            // as missing
            if (sampleCount == 1) {
              missingAnnotationMapForExperiment.remove("Replica Number");
            }

            prevExperimentInfo.sampleCount = sampleCount;


            // If the missing annotation hash is empty, add the experiment to the complete experiment list
            // otherwise, add experiment to the incomplete experiment list
            if (missingAnnotationMapForExperiment.isEmpty()) {
              this.completeExperimentMapForLab.put(prevIdRequest, prevExperimentInfo);
            } else {
              // Put the missing annotations in a list so we can reference them with the experiment
              // when we generate the detail report
              prevExperimentInfo.missingAnnotationMapForExperiment = missingAnnotationMapForExperiment;
              prevExperimentInfo.requiredPropertiesForExperiment = requiredPropertiesForExperiment;
              this.incompleteExperimentMapForLab.put(prevIdRequest, prevExperimentInfo);
            }

            sampleCount = 0;
          }

          // When we encounter a new lab, generate the report row(s) for the
          // previous lab.
          if (!firstTime && !prevLabName.equals(labName)) {
            LabInfo labInfo = new LabInfo();
            labInfo.labName = prevLabName;
            labInfo.completeExperimentMap = completeExperimentMapForLab;
            labInfo.incompleteExperimentMap = incompleteExperimentMapForLab;

            labInfoMap.put(prevLabName, labInfo);

            this.completeExperimentMapForLab   = new TreeMap<Integer, ExperimentInfo>();
            this.incompleteExperimentMapForLab = new TreeMap<Integer, ExperimentInfo>();
          }

          // Get the required set of annotations for this experiment
          missingAnnotationMapForExperiment = new TreeMap<String, List<String>>();
          requiredPropertiesForExperiment = new ArrayList<Property>();
          this.getRequiredProperties(dh, experimentInfo, requiredPropertiesForExperiment);
        }

        // Hash missing annotations for sample
        hashMissingAnnotations(idSample, sampleNumber, experimentInfo);


        prevIdRequest = idRequest;
        prevLabName = labName;
        prevExperimentInfo = experimentInfo;
        sampleCount++;
        firstTime = false;


      }
      //  After the last row is read, hash the last lab
      LabInfo labInfo = new LabInfo();
      labInfo.labName = prevLabName;
      labInfo.completeExperimentMap = completeExperimentMapForLab;
      labInfo.incompleteExperimentMap = incompleteExperimentMapForLab;
      labInfoMap.put(prevLabName, labInfo);


      // Now that we have hashed the complete and incomplete experiments by lab, we can generate
      // the report.  We will generate three sections:  summary, missing annotations, fully annotated
      // annotations.

      // Create the report and define the columns

      //Sort the properties by their sort order
      Collections.sort(requiredProperties, new sortOrderComparator());

      createReportTray(sess, dh, REPORT_DETAIL_INCOMPLETE, "Experiments Missing Annotations", requiredProperties);
      for (String labName : labInfoMap.keySet()) {

        LabInfo theLabInfo = labInfoMap.get(labName);
        generateIncompleteExperimentReportRows(dh, theLabInfo);

      }
      createReportTray(sess, dh, REPORT_DETAIL_COMPLETE, "Experiments Fully Annotated", requiredProperties);
      for (String labName : labInfoMap.keySet()) {

        LabInfo theLabInfo = labInfoMap.get(labName);
        generateCompleteExperimentReportRows(dh, theLabInfo);

      }
      createReportTray(sess, dh, REPORT_SUMMARY, "Summary", null);
      for (String labName : labInfoMap.keySet()) {

        LabInfo theLabInfo = labInfoMap.get(labName);
        generateSummaryRowForLab(theLabInfo);

      }


      if (isValid()) {
        this.setSuccessJsp(this, tray.getFormat());
      } else {
        setResponsePage(this.ERROR_JSP);
      }

    }catch (UnknownPermissionException e){
      log.error("An exception has occurred in ShowAnnotationProgressReport ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());

    }catch (NamingException e){
      log.error("An exception has occurred in ShowAnnotationProgressReport ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());

    }catch (SQLException e) {
      log.error("An exception has occurred in ShowAnnotationProgressReport ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());

    } catch (Exception e) {
      log.error("An exception has occurred in ShowAnnotationProgressReport ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        secAdvisor.closeReadOnlyHibernateSession();
      } catch(Exception e) {

      }
    }

    return this;
  }


  @SuppressWarnings("unchecked")
  private void getRequiredProperties(DictionaryHelper dh, ExperimentInfo experimentInfo, List<Property> propertyList) {
    for (Property prop : dh.getPropertyList()) {


      boolean matchesOrganism = true;
      boolean matchesPlatform = true;

      if (experimentInfo != null) {
        // Does the experiment's organism match the applicable organism?
        if (prop.getOrganisms() != null && prop.getOrganisms().size() > 0) {
          matchesOrganism = false;
          if (experimentInfo.idOrganism != null) {
            for (Organism organism : (Set<Organism>) prop.getOrganisms()) {
              if (organism.getIdOrganism().equals(experimentInfo.idOrganism)) {
                matchesOrganism = true;
              }
            }
          }
        }

        // Does the experiment platform match the applicable platform?
        if (prop.getPlatformApplications() != null && prop.getPlatformApplications().size() > 0) {
          matchesPlatform = false;
          for (PropertyPlatformApplication platformApp : (Set<PropertyPlatformApplication>)prop.getPlatformApplications()) {
            if (experimentInfo.codeRequestCategory != null && platformApp.getCodeRequestCategory().equals(experimentInfo.codeRequestCategory)) {
              if (platformApp.getCodeApplication() == null) {
                matchesPlatform = true;
              } else if (experimentInfo.codeApplication != null && platformApp.getCodeApplication().equals(experimentInfo.codeApplication)) {
                matchesPlatform = true;
              }
            }
          }
        }
      }

      boolean includeProperty = true;
      // For the column headers, we won't show the individual stages, just one general column header for 'Stage' annotation.
      if (experimentInfo == null) {
        if (prop.getName().startsWith("Stage")) {
          includeProperty = false;
        }
      }

      // We want to keep all properties that are required and if specified, match the experiment's
      // organism and experiment platform (codeRequestCategory + codeApplication)
      if (prop.getIsActive().equals("Y") && prop.getIsRequired().equals("Y")) {
        if (includeProperty && matchesOrganism && matchesPlatform) {
          propertyList.add(prop);
        }
      }
    }

  }

  @SuppressWarnings("unchecked")
  private void hashMissingAnnotations(Integer idSample, String sampleNumber, ExperimentInfo experimentInfo) {
    Map<String, String>annotationMap = this.propertyEntryAnnotationMap.get(idSample);
    String sampleSource = annotationMap != null ? annotationMap.get("SAMPLE SOURCE") : "";

    boolean stageAnnotationFound = false;

    for (Property prop : this.requiredPropertiesForExperiment) {


      String annotationValue = null;
      if (annotationMap != null) {
        annotationValue = annotationMap.get(prop.getName().toUpperCase());
      }

      if (annotationValue != null && !annotationValue.equals("")) {
        // Annotation has a value
        if (prop.getName().startsWith("Stage")) {
          stageAnnotationFound = true;
        }
      } else {
        // Don't look at stage annotations independently.  We will just determine
        // if at least one stage annotation was filled in
        if (prop.getName().startsWith("Stage")) {

        } else {
          addMissingAnnotation(prop.getName(), sampleNumber);
        }
      }
    }

    if (!stageAnnotationFound) {
      addMissingAnnotation("Stage", sampleNumber);
    }

    // Find out if files are linked to this sample.  If not, we treat this as a missing annotation
    if (!this.sampleLinkedToFileMap.containsKey(idSample)) {
      addMissingAnnotation(this.SAMPLE_FILE_LINK, sampleNumber);
    }


  }

  private void addMissingAnnotation(String propertyName, String sampleNumber) {
    List<String> sampleNumbers = missingAnnotationMapForExperiment.get(propertyName);
    if (sampleNumbers == null) {
      sampleNumbers = new ArrayList<String>();
    }
    sampleNumbers.add(sampleNumber);

    missingAnnotationMapForExperiment.put(propertyName, sampleNumbers);


  }

  private void createReportTray(Session sess, DictionaryHelper dh, String reportType, String title, List<Property> requiredProperties) {



    // set up the ReportTray
    tray = new ReportTray();
    tray.setReportDate(new java.util.Date(System.currentTimeMillis()));
    tray.setReportTitle(title);
    tray.setReportDescription(title);
    tray.setFileName(title);
    tray.setFormat(ReportFormats.XLS);

    Set<Column> columns = new TreeSet<Column>();

    if (reportType.equals(REPORT_SUMMARY)) {
      columns.add(makeReportColumn("Lab", 1));
      columns.add(makeReportColumn("Total Experiments", 2));
      columns.add(makeReportColumn("Fully Annotated", 3));
      columns.add(makeReportColumn("Missing Annotations", 4));

    } else {
      columns.add(makeReportColumn("Lab", 1));
      columns.add(makeReportColumn("Experiment #", 2));
      columns.add(makeReportColumn("Samples", 3));
      columns.add(makeReportColumn("Experiment Type", 4));
      columns.add(makeReportColumn("Organism", 5));
      columns.add(makeReportColumn("Sample Source", 6));



      // Show missing annotation columns
      if (reportType.equals(REPORT_DETAIL_INCOMPLETE)) {
        int colNbr = 7;

        // Stage is a special case.  Add it to the list of required properties.
        Property stageProp = new Property();
        stageProp.setName("Stage");
        requiredProperties.add(stageProp);



        for (Property prop: requiredProperties) {
          if (prop.getName().equals("Sample Source")) {
            continue;
          }
          columns.add(makeReportColumn(prop.getName(), colNbr++));
        }

        // Last column will indicate if any samples are not linked to file(s)
        columns.add(makeReportColumn(SAMPLE_FILE_LINK, colNbr++));

      }

    }

    tray.setColumns(columns);

    reportTrayList.addTray(tray);
  }

  private void generateSummaryRowForLab(LabInfo labInfo) {
    // Show Lab, total experiments, complete experiment count, missing experiment count
    ReportRow reportRow = new ReportRow();
    List<String> values  = new ArrayList<String>();

    int totalExperiments = labInfo.completeExperimentMap.size() + labInfo.incompleteExperimentMap.size();
    int completeExperiments = labInfo.completeExperimentMap.size();
    int incompleteExperiments = labInfo.incompleteExperimentMap.size();

    values.add(labInfo.labName);
    values.add(Integer.valueOf(totalExperiments).toString());
    values.add(Integer.valueOf(completeExperiments).toString());
    values.add(Integer.valueOf(incompleteExperiments).toString());

    reportRow.setValues(values);
    tray.addRow(reportRow);

  }

  private void generateIncompleteExperimentReportRows(DictionaryHelper dh, LabInfo labInfo) {
    // Print lab name followed by experiment ids for missing
    // annotations as well as comma separated list of missing annotations
    ReportRow reportRow = new ReportRow();
    List<String> values  = new ArrayList<String>();

    values.add(labInfo.labName);

    boolean firstTime = true;
    for (Integer theIdRequest : labInfo.incompleteExperimentMap.keySet()) {
      if (!firstTime) {
        reportRow = new ReportRow();
        values  = new ArrayList<String>();
        values.add("");
      }
      ExperimentInfo expInfo = labInfo.incompleteExperimentMap.get(theIdRequest);
      values.add(expInfo.requestNumber);
      values.add(Integer.valueOf(expInfo.sampleCount).toString());
      values.add(dh.getApplication(expInfo.codeApplication));
      values.add(expInfo.idOrganism != null ? dh.getOrganism(expInfo.idOrganism) : "");

      if (expInfo.sampleSource == null || expInfo.sampleSource.equals("")) {
        if (!this.experimentWithFileMap.containsKey(expInfo.idRequest)) {
          // If the sample source is not filled in but this is an experiment without samples, we will
          // grey out the cell.
          values.add("missingNew");
        } else {
          values.add("missing");
        }
      } else {
        values.add(expInfo.sampleSource);
      }



      // Show either a sample count (for those missing annotation) or "" for each required property
      for (Property prop : requiredProperties) {
        if (prop.getName().equals("Sample Source")) {
          continue;
        }
        //
        //  For each property, the cell will contain one of these values:
        //     "missing"     - annotation is blank on this experiment's samples
        //     "missingNew"  - same as "missing", but this is for a new experiment
        //     [an integer]  - partially missing. annotation is blank on some of the experiment's samples
        //     "complete"    - annotation is filled in on all of the samples for the experiment
        //     "n/a"         - this annotation isn't applicable for this experiment
        if (expInfo.missingAnnotationMapForExperiment.containsKey(prop.getName())) {
          List<String> sampleNumbers = expInfo.missingAnnotationMapForExperiment.get(prop.getName());
          if (!this.experimentWithFileMap.containsKey(expInfo.idRequest)) {
            // If the experiment doesn't have any files, we will mark the as missing/new experiment.
            values.add("missingNew");
          } else if (sampleNumbers.size() < expInfo.sampleCount) {
            // If only some of the samples are missing the annotation, show the count of the samples missing annot;
            values.add(Integer.valueOf(sampleNumbers.size()).toString());
          } else {
            values.add("missing");
          }
        } else {
          // There are 2 cases where required property is not in missingAnnotationMap for experiment.
          // 1. All annotations were present ("present")
          // 2. The annotation is not applicable  ("n/a")
          boolean isApplicable = false;
          if (prop.getName().equals("Stage")) {
            // Stage is a special case.  We don't compare to a specific annotation.
            // Just assume that if stage isn't in missing annotations, it is filled in.
            isApplicable = true;
          } else {
            for (Property requiredProperty : expInfo.requiredPropertiesForExperiment) {
              if (requiredProperty.getName().equals(prop.getName())) {
                isApplicable = true;
                break;
              }
            }
          }
          values.add(isApplicable ? "complete" : "n/a");

        }
      }

      // Last annotation column will indicate if any sample is missing link to files
      if (expInfo.missingAnnotationMapForExperiment.containsKey(SAMPLE_FILE_LINK)) {
        List<String> sampleNumbers = expInfo.missingAnnotationMapForExperiment.get(SAMPLE_FILE_LINK);
        // If all of the samples have links to files, show "complete" in the cell.
        // If only some of the samples are missing link to the files, show the count of the samples
        // that are missing the link; otherwise mark the cell as "missing".
        if (sampleNumbers.size() < expInfo.sampleCount) {
          values.add(Integer.valueOf(sampleNumbers.size()).toString());
        } else {
          values.add("missing");
        }
      } else {
        values.add("complete");
      }

      reportRow.setValues(values);
      tray.addRow(reportRow);

      firstTime = false;
    }
  }



  private void generateCompleteExperimentReportRows(DictionaryHelper dh, LabInfo labInfo) {
    // Print lab name followed by all complete experiment ids
    ReportRow reportRow = new ReportRow();
    List<String> values  = new ArrayList<String>();

    values.add(labInfo.labName);

    boolean firstTime = true;
    for (Integer theIdRequest : labInfo.completeExperimentMap.keySet()) {
      if (!firstTime) {
        reportRow = new ReportRow();
        values  = new ArrayList<String>();
        values.add("");
      }
      ExperimentInfo expInfo = labInfo.completeExperimentMap.get(theIdRequest);
      values.add(expInfo.requestNumber);
      values.add(Integer.valueOf(expInfo.sampleCount).toString());
      values.add(dh.getApplication(expInfo.codeApplication));
      values.add(expInfo.idOrganism != null ? dh.getOrganism(expInfo.idOrganism) : "");
      values.add(expInfo.sampleSource);

      values.add("");  // blank for fully annotated experiment (shows missing annotation names)

      reportRow.setValues(values);
      tray.addRow(reportRow);
      firstTime = false;
    }

  }





  private Column makeReportColumn(String name, int colNumber) {
    Column reportCol = new Column();
    reportCol.setName(name);
    reportCol.setCaption(name);
    reportCol.setDisplayOrder(new Integer(colNumber));
    return reportCol;
  }


  /* (non-Javadoc)
   * @see hci.framework.control.Command#setRequestState(javax.servlet.http.HttpServletRequest)
   */
  public HttpServletRequest setRequestState(HttpServletRequest request) {
    request.setAttribute("reportTrayList", this.reportTrayList);
    return request;
  }

  /* (non-Javadoc)
   * @see hci.framework.control.Command#setResponseState(javax.servlet.http.HttpServletResponse)
   */
  public HttpServletResponse setResponseState(HttpServletResponse response) {
    // TODO Auto-generated method stub
    return response;
  }

  /* (non-Javadoc)
   * @see hci.framework.control.Command#setSessionState(javax.servlet.http.HttpSession)
   */
  public HttpSession setSessionState(HttpSession session) {
    // TODO Auto-generated method stub
    return session;
  }

  /* (non-Javadoc)
   * @see hci.report.utility.ReportCommand#loadContextPermissions()
   */
  public void loadContextPermissions(){

  }
  public void loadContextPermissions(String userName) throws SQLException {

  }

  private static class ExperimentInfo {
    public String         labName;
    public String         requestNumber;
    public Integer        idRequest;
    public String         submitter;
    public Integer        idOrganism;
    public String         codeRequestCategory;
    public String         codeApplication;
    public String         sampleSource;
    public int            sampleCount;
    public Map<String,    List<String>> missingAnnotationMapForExperiment;
    public List<Property> requiredPropertiesForExperiment;

  }

  private static class LabInfo {
    public String                            labName;
    public TreeMap<Integer, ExperimentInfo>  completeExperimentMap;
    public TreeMap<Integer, ExperimentInfo>  incompleteExperimentMap;

  }

  private class sortOrderComparator implements Comparator<Property>{

    @Override
    public int compare(Property o1, Property o2) {
      if(o1.getSortOrder() == null && o2.getSortOrder() == null) {
        return 0;
      } else if(o1.getSortOrder() == null) {
        return 1;
      } else if(o2.getSortOrder() == null) {
        return -1;
      } else {
        return o1.getSortOrder().compareTo(o2.getSortOrder());
      }
    }

  }

}