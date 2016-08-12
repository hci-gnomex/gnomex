package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.model.Lab;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.PropertyDictionaryHelper;
import hci.gnomex.utility.UsageRowDescriptor;
import hci.gnomex.utility.UsageRowDescriptorComparator;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.apache.log4j.Logger;

public class GetUsageData extends GNomExCommand implements Serializable {

  // the static field for logging in Log4J
  private static Logger LOG = Logger.getLogger(GetUsageData.class);

  private Integer idCoreFacility = null;
  private String usageUserVisibility = "";
  private String currentView = "";

  private TreeMap<String, Lab>  labMap   = new TreeMap<String, Lab>();
  private HashMap<Integer, Lab> labIdMap = new HashMap<Integer, Lab>();
  private TreeMap<Integer, ActivityInfo> weeklyActivityMap = new TreeMap<Integer, ActivityInfo>();
  private HashMap<String, Integer> weekNumberMap = new HashMap<String, Integer>();


  private String asOfLast6Months = "N";
  private String asOfLastYear = "N";
  private String asOfLast2Years = "N";

  private Integer endRank = Integer.valueOf(9999);

  private static final BigDecimal    MB = new BigDecimal(2).pow(20);
  private static final BigDecimal    GB = new BigDecimal(2).pow(30);

  DateFormat dfShort    = new SimpleDateFormat("MMM yyyy");
  DateFormat dfDataTip  = new SimpleDateFormat("MMM dd yyyy");
  DateFormat dfNormal   = new SimpleDateFormat("MM-dd-yyyy");

  Calendar today = Calendar.getInstance();

  public static final String SUMMARY_DAYS_SINCE_LAST_UPLOAD = "SummaryDaysSinceLastUpload";
  public static final String SUMMARY_UPLOADS_BY_LAB = "SummaryUploadsByLab";
  public static final String SUMMARY_DISK_SPACE_BY_LAB = "SummaryDiskSpaceByLab";
  public static final String SUMMARY_DISK_SPACE_BY_YEAR = "SummaryDiskSpaceByYear";
  public static final String SUMMARY_DISK_SPACE_BY_EXPERIMENT = "SummaryDiskSpaceByExperiment";
  public static final String SUMMARY_DISK_SPACE_BY_ANALYSIS = "SummaryDiskSpaceByAnalysis";
  public static final String TOTAL_DISK_SPACE = "TotalDiskSpace";
  public static final String SUMMARY_DOWNLOADS_BY_LAB = "SummaryDownloadsByLab";
  public static final String SUMMARY_EXPERIMENTS_BY_LAB = "SummaryExperimentsByLab";
  public static final String SUMMARY_ANALYSIS_BY_LAB = "SummaryAnalysisByLab";
  public static final String SUMMARY_ACTIVITY_BY_WEEK = "SummaryActivityByWeek";
  public static final String SUMMARY_EXPERIMENTS_BY_TYPE = "SummaryExperimentsByType";
  public static final String SUMMARY_SEQ_EXPERIMENTS_BY_APP = "SummarySeqExperimentsByApp";
  public static final String SUMMARY_ANALYSIS_BY_TYPE = "SummaryAnalysisByType";

  public static final String ANALYSIS_VIEW = "Analysis";
  public static final String EXPERIMENTS_VIEW = "Experiments";
  public static final String FILES_VIEW = "Files";

  public void validate() {
  }

  public void loadCommand(HttpServletRequest request, HttpSession session) {

    if (request.getParameter("asOfLast6Months") != null && !request.getParameter("asOfLast6Months").equals("")) {
      asOfLast6Months = request.getParameter("asOfLast6Months");
    }
    if (request.getParameter("asOfLastYear") != null && !request.getParameter("asOfLastYear").equals("")) {
      asOfLastYear = request.getParameter("asOfLastYear");
    }
    if (request.getParameter("asOfLast2Years") != null && !request.getParameter("asOfLast2Years").equals("")) {
      asOfLast2Years = request.getParameter("asOfLast2Years");
    }
    if (request.getParameter("endRank") != null && !request.getParameter("endRank").equals("")) {
      endRank = Integer.valueOf(request.getParameter("endRank"));
    }
    if (request.getParameter("idCoreFacility") != null && !request.getParameter("idCoreFacility").equals("")) {
      idCoreFacility = Integer.valueOf(request.getParameter("idCoreFacility"));
    }
    if(request.getParameter("currentView") != null && !request.getParameter("currentView").equals("")){
      currentView = request.getParameter("currentView");
    }
  }

  public Command execute() throws RollBackCommandException {

    try {
      Document doc = new Document(new Element("UsageData"));

      Element summaryDaysNode = new Element(SUMMARY_DAYS_SINCE_LAST_UPLOAD);
      doc.getRootElement().addContent(summaryDaysNode);

      Element summaryUploadsNode = new Element(SUMMARY_UPLOADS_BY_LAB);
      doc.getRootElement().addContent(summaryUploadsNode);

      Element summaryDiskSpaceNode = new Element(SUMMARY_DISK_SPACE_BY_LAB);
      doc.getRootElement().addContent(summaryDiskSpaceNode);

      Element summaryDiskSpaceByYearNode = new Element(SUMMARY_DISK_SPACE_BY_YEAR);
      doc.getRootElement().addContent(summaryDiskSpaceByYearNode);


      Element summaryDiskSpaceByExperimentNode = new Element(SUMMARY_DISK_SPACE_BY_EXPERIMENT);
      doc.getRootElement().addContent(summaryDiskSpaceByExperimentNode);

      Element summaryDiskSpaceByAnalysisNode = new Element(SUMMARY_DISK_SPACE_BY_ANALYSIS);
      doc.getRootElement().addContent(summaryDiskSpaceByAnalysisNode);

      Element totalDiskSpaceNode = new Element(TOTAL_DISK_SPACE);
      doc.getRootElement().addContent(totalDiskSpaceNode);

      Element summaryDownloadsNode = new Element(SUMMARY_DOWNLOADS_BY_LAB);
      doc.getRootElement().addContent(summaryDownloadsNode);

      Element summaryExperimentsNode = new Element(SUMMARY_EXPERIMENTS_BY_LAB);
      doc.getRootElement().addContent(summaryExperimentsNode);

      Element summaryAnalysisNode = new Element(SUMMARY_ANALYSIS_BY_LAB);
      doc.getRootElement().addContent(summaryAnalysisNode);

      Element summaryWeeklyActivityNode = new Element(SUMMARY_ACTIVITY_BY_WEEK);
      doc.getRootElement().addContent(summaryWeeklyActivityNode);

      Element summaryExperimentsByTypeNode = new Element(SUMMARY_EXPERIMENTS_BY_TYPE);
      doc.getRootElement().addContent(summaryExperimentsByTypeNode);

      Element summarySeqExperimentsByAppNode = new Element(SUMMARY_SEQ_EXPERIMENTS_BY_APP);
      doc.getRootElement().addContent(summarySeqExperimentsByAppNode);

      Element summaryAnalysisByTypeNode = new Element(SUMMARY_ANALYSIS_BY_TYPE);
      doc.getRootElement().addContent(summaryAnalysisByTypeNode);     

      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());


      usageUserVisibility = PropertyDictionaryHelper.getInstance(sess).getProperty(PropertyDictionary.USAGE_USER_VISIBILITY);

      // Guests cannot run this command
      if (this.getSecAdvisor().isGuest()) {
        this.addInvalidField("Insufficient permissions", "Insufficient permission to get usage data.  Guests cannot access usage data.");
        setResponsePage(this.ERROR_JSP);
      } 

      // Admins can run this command.  Normal gnomex users can if usage_user_visibility
      // property is set to an appropriate level ('masked' or 'full').
      if (this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_ACCESS_ANY_OBJECT)) {
      } else if (usageUserVisibility.equals("") || usageUserVisibility.equals(PropertyDictionary.OPTION_USER_USER_VISIBILITY_NONE)) {
        this.addInvalidField("Insufficient permissions", "Insufficient permission to get usage data.  Property usage_user_visibility does not allow users to access usage data");
        setResponsePage(this.ERROR_JSP);
      }

      // Make sure that the core facility admin or user is part of the core facility being used to filter
      // the usage.
      boolean hasPermission = true;
      if (idCoreFacility != null) {
        // Turn it off so we can figure out if user has permission by looking at core facilities 
        hasPermission = false;  
        if (this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_ADMINISTER_ALL_CORE_FACILITIES)) {
          hasPermission = true;
        } else if (this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_ACCESS_ANY_OBJECT)) {
          // Admin of a core facility.  Make sure core facility that admin manages is this core facility
          if (this.getSecAdvisor().isCoreFacilityIManage(idCoreFacility)) {
            hasPermission = true;
          }
        } else {
          // Normal user.  Make sure user belongs to lab that is associated with core facility
          if (this.getSecAdvisor().isCoreFacilityForMyLab(idCoreFacility)) {
            hasPermission = true;
          }
        }
      }
      if (!hasPermission) {
        throw new Exception("Insufficient permission to access usage date for selected core facility");
      }


      if (this.isValid()) {
        // Hash all labs
        StringBuffer buf = new StringBuffer();
        buf.append("SELECT l from Lab l ");
        if (idCoreFacility != null) {
          buf.append("JOIN l.coreFacilities as cf ");
          buf.append("WHERE cf.idCoreFacility = " + idCoreFacility + " ");
        }
        buf.append("order by l.lastName, l.firstName ");
        List labs = sess.createQuery(buf.toString()).list();
        for(Iterator i = labs.iterator(); i.hasNext();) {
          Lab lab = (Lab)i.next();

          // Don't show labs marked for exclusion in XML
          if (lab.getExcludeUsage() != null && lab.getExcludeUsage().equals("Y")) {
            continue;
          }

          labMap.put(lab.getName(false, true), lab);
          labIdMap.put(lab.getIdLab(), lab);
        }

        // Hash all weeks from a specific date on        
        Calendar now = GregorianCalendar.getInstance();

        // Back up today's date to correct interval
        if (asOfLast6Months.equals("Y")) {
          now.add(Calendar.MONTH, -6);          
        }else if (asOfLastYear.equals("Y")) {
          now.add(Calendar.YEAR, -1);          
        }else if (asOfLast2Years.equals("Y")) {
          now.add(Calendar.YEAR, - 2);          
        }else {
          now.add(Calendar.YEAR, -10);
        }
        // Now back up to a Monday
        int weekday = now.get(Calendar.DAY_OF_WEEK);  
        if (weekday != Calendar.MONDAY)   
        {   
          // calculate how much to add   
          // the 2 is the difference between Saturday and Monday   
          int days = (Calendar.SATURDAY - weekday + 2) % 7;   
          now.add(Calendar.DAY_OF_YEAR, days);
          now.add(Calendar.DAY_OF_YEAR, -7);
        }           


        int week = 0;
        while (true) {
          if (now.after(today)) {
            break;
          }

          ActivityInfo ai = new ActivityInfo();
          ai.label    = dfShort.format(now.getTime());
          ai.dataTip  = "Week of " + dfDataTip.format(now.getTime());
          ai.startDate = now.getTime();


          weeklyActivityMap.put(Integer.valueOf(week), ai);

          int x = 1;
          for (x = 1; x < 8; x++) {
            now.add(Calendar.DATE, 1);
            weekNumberMap.put(dfNormal.format(now.getTime()), week);
          }

          week++;
        }


        //Get a list of analysis types
        List analysisTypes = sess.createQuery("Select at.analysisType from AnalysisType at").list();

        // Get experiment count
        int rank = 0;
        int totalCount = 0;
        List summaryRows;
        Integer totalExperiments = null;
        Integer totalAnalysisCount = null;

        if(currentView.equals(EXPERIMENTS_VIEW) || currentView.equals(FILES_VIEW)){
          buf = new StringBuffer();
          buf.append("SELECT r.idLab, count(*) from Request r ");
          if (idCoreFacility != null) {
            buf.append("WHERE r.idCoreFacility = " + idCoreFacility + " ");
          }
          buf.append("GROUP BY r.idLab ");
          buf.append("ORDER BY count(*) desc");

          summaryRows = sess.createQuery(buf.toString()).list();
          addEntryIntegerNodes(summaryExperimentsNode, summaryRows, "experimentCount", true);
          if(idCoreFacility == null){
            totalExperiments = (int) (long)sess.createQuery("SELECT count(*) from Request r ").uniqueResult();
          }
          else{
            totalExperiments = (int) (long)sess.createQuery("SELECT count(*) from Request r where r.idCoreFacility = " + idCoreFacility + " ").uniqueResult();
          }
          summaryExperimentsNode.setAttribute("experimentCount", totalExperiments.toString());
        }


        // Get analysis count
        if(currentView.equals(ANALYSIS_VIEW) || currentView.equals(FILES_VIEW)){
          rank = 0;
          totalCount = 0;
          summaryRows = sess.createQuery("SELECT a.idLab, count(*) from Analysis a group by a.idLab order by count(*) desc").list();
          addEntryIntegerNodes(summaryAnalysisNode, summaryRows, "analysisCount", true);
          totalAnalysisCount = (int) (long)sess.createQuery("SELECT count(*) from Analysis r ").uniqueResult();
          summaryAnalysisNode.setAttribute("analysisCount", totalAnalysisCount.toString());
        }


        //Get total upload and download counts
        if(currentView.equals(FILES_VIEW)){
          //Upload count ordered by lab and count
          rank = 0;
          buf = new StringBuffer();
          buf.append("SELECT tl.idLab, count(distinct fileName) ");
          buf.append("from TransferLog tl ");
          buf.append("WHERE tl.transferType = 'upload' ");
          buf.append("GROUP BY tl.idLab ");
          buf.append("ORDER BY count(distinct fileName) desc");
          summaryRows = sess.createQuery(buf.toString()).list();
          addEntryIntegerNodes(summaryUploadsNode, summaryRows, "uploadCount", true);

          //Total uploads
          buf = new StringBuffer();
          buf.append("SELECT count(distinct fileName) ");
          buf.append("FROM TransferLog tl ");
          buf.append("WHERE tl.transferType = 'upload' ");
          Integer totalUploadCount = (int) (long)sess.createQuery(buf.toString()).uniqueResult();
          summaryUploadsNode.setAttribute("uploadCount", totalUploadCount.toString());

          //Download count ordered by lab and count
          rank = 0;
          buf = new StringBuffer();
          buf.append("SELECT tl.idLab, count(distinct fileName) ");
          buf.append("from TransferLog tl ");
          buf.append("WHERE tl.transferType = 'download' ");
          buf.append("GROUP BY tl.idLab ");
          buf.append("ORDER BY count(distinct fileName) desc");
          summaryRows = sess.createQuery(buf.toString()).list();
          addEntryIntegerNodes(summaryDownloadsNode, summaryRows, "downloadCount", true);

          //Total download count
          buf = new StringBuffer();
          buf.append("SELECT count(distinct fileName) ");
          buf.append("FROM TransferLog tl ");
          buf.append("WHERE tl.transferType = 'download' ");
          Integer totalDownloadCount = (int) (long)sess.createQuery(buf.toString()).uniqueResult();
          summaryRows = sess.createQuery(buf.toString()).list();
          summaryDownloadsNode.setAttribute("downloadCount", totalDownloadCount.toString());
        }


        //
        // Get disk space by lab
        //
        // Get total file size for experiments by lab
        TreeMap diskSpaceMap = new TreeMap();
        buf = new StringBuffer();
        BigDecimal totalDiskSpace = new BigDecimal(0);

        if(currentView.equals(EXPERIMENTS_VIEW) || currentView.equals(FILES_VIEW)){
          buf.append("SELECT r.idLab, sum(ef.fileSize) ");
          buf.append("FROM Request r ");
          buf.append("JOIN r.files as ef  ");
          buf.append("WHERE ef.fileSize is not NULL ");
          if (idCoreFacility != null) {
            buf.append("AND r.idCoreFacility = " + idCoreFacility + " ");
          }
          buf.append("GROUP BY r.idLab ");
          summaryRows = sess.createQuery(buf.toString()).list();
          mapDiskSpace(summaryRows, diskSpaceMap);
        }

        // Add in total file size for analysis by lab
        if(currentView.equals(ANALYSIS_VIEW) || currentView.equals(FILES_VIEW)){
          summaryRows = sess.createQuery("SELECT a.idLab, sum(af.fileSize) from Analysis a join a.files as af  where af.fileSize is not NULL group by a.idLab").list();
          mapDiskSpace(summaryRows, diskSpaceMap);
        }

        addEntryDiskSpaceNodes(summaryDiskSpaceNode, diskSpaceMap, true, true, analysisTypes);

        buf = new StringBuffer();
        BigDecimal totalExperimentDiskSpace = null;
        BigDecimal totalAnalysisDiskSpace = null;
        if(currentView.equals(EXPERIMENTS_VIEW) || currentView.equals(FILES_VIEW)){
          buf.append("SELECT  sum(ef.fileSize) ");
          buf.append("FROM Request r ");
          buf.append("JOIN r.files as ef ");
          buf.append("WHERE ef.fileSize is not NULL ");
          if (idCoreFacility != null) {
            buf.append("AND r.idCoreFacility = " + idCoreFacility + " ");
          }
          totalExperimentDiskSpace = (BigDecimal)sess.createQuery(buf.toString()).uniqueResult();
        }

        if(currentView.equals(ANALYSIS_VIEW) || currentView.equals(FILES_VIEW)){
          totalAnalysisDiskSpace = (BigDecimal)sess.createQuery("SELECT   sum(af.fileSize) from Analysis a join a.files as af where af.fileSize is not NULL").uniqueResult();
        }

        totalDiskSpace = totalExperimentDiskSpace != null ? totalExperimentDiskSpace.add(totalAnalysisDiskSpace != null ? totalAnalysisDiskSpace : new BigDecimal(0)) : new BigDecimal(0);

        summaryDiskSpaceNode.setAttribute("diskSpace",   totalDiskSpace.toString());      
        summaryDiskSpaceNode.setAttribute("diskSpaceMB", totalDiskSpace.divide(MB, BigDecimal.ROUND_HALF_EVEN).toString());
        summaryDiskSpaceNode.setAttribute("diskSpaceGB", totalDiskSpace.divide(GB, BigDecimal.ROUND_HALF_EVEN).toString());


        //
        // Get disk space by year
        //
        diskSpaceMap = new TreeMap();
        buf = new StringBuffer();

        if(currentView.equals(EXPERIMENTS_VIEW)){
          buf.append("SELECT year(r.createDate), sum(ef.fileSize) ");
          buf.append("FROM Request r ");
          buf.append("JOIN r.files as ef ");
          buf.append("where ef.fileSize is not NULL ");
          if (idCoreFacility != null) {
            buf.append("AND r.idCoreFacility = " + idCoreFacility + " ");
          }
          buf.append("GROUP BY year(r.createDate) ");
          summaryRows = sess.createQuery(buf.toString()).list();
          mapDiskSpace(summaryRows, diskSpaceMap);
          buf = new StringBuffer();
          buf.append("SELECT sum(ef.fileSize) from Request r JOIN r.files as ef WHERE ef.fileSize is not NULL ");
          if(idCoreFacility != null){
            buf.append("AND r.idCoreFacility = " + idCoreFacility + " ");
          }
          totalDiskSpace = (BigDecimal)sess.createQuery(buf.toString()).uniqueResult();
        }

        if(currentView.equals(ANALYSIS_VIEW)){
          summaryRows = sess.createQuery("SELECT year(a.createDate), sum(af.fileSize) from Analysis a join a.files as af  where af.fileSize is not NULL group by year(a.createDate) ").list();
          mapDiskSpace(summaryRows, diskSpaceMap);
          totalDiskSpace = (BigDecimal)sess.createQuery("SELECT   sum(af.fileSize) from Analysis a join a.files as af where af.fileSize is not NULL").uniqueResult();
        }

        addEntryDiskSpaceNodes(summaryDiskSpaceByYearNode, diskSpaceMap, false, false, analysisTypes);
        summaryDiskSpaceByYearNode.setAttribute("diskSpace",   totalDiskSpace != null ? totalDiskSpace.toString() : "0");      
        summaryDiskSpaceByYearNode.setAttribute("diskSpaceMB", totalDiskSpace != null ? totalDiskSpace.divide(MB, BigDecimal.ROUND_HALF_EVEN).toString() : "0");
        summaryDiskSpaceByYearNode.setAttribute("diskSpaceGB", totalDiskSpace != null ? totalDiskSpace.divide(GB, BigDecimal.ROUND_HALF_EVEN).toString() : "0");


        //
        // Get disk space by analysis vs. experiment
        //
        if(currentView.equals(FILES_VIEW)){
          diskSpaceMap = new TreeMap();
          StringBuffer queryBuf = new StringBuffer("SELECT rc.requestCategory, sum(ef.fileSize) ");
          queryBuf.append(" FROM Request r, RequestCategory rc ");
          queryBuf.append(" JOIN r.files as ef ");
          queryBuf.append(" WHERE r.codeRequestCategory = rc.codeRequestCategory ");
          if(idCoreFacility != null){
            queryBuf.append(" AND rc.idCoreFacility = " + idCoreFacility + " ");
          }
          queryBuf.append(" AND ef.fileSize is not NULL ");
          queryBuf.append(" GROUP BY rc.requestCategory");
          queryBuf.append(" ORDER BY rc.requestCategory ");
          summaryRows = sess.createQuery(queryBuf.toString()).list();
          queryBuf = new StringBuffer("SELECT sum(ef.fileSize) from Request r join r.files as ef where ef.fileSize is not NULL ");
          if(idCoreFacility != null){
            queryBuf.append(" AND r.idCoreFacility = " + idCoreFacility + " ");
          }
          BigDecimal experimentFileSize = (BigDecimal)sess.createQuery(queryBuf.toString()).uniqueResult();
          mapDiskSpace(summaryRows, diskSpaceMap);
          addEntryDiskSpaceNodes(summaryDiskSpaceByExperimentNode, diskSpaceMap, false, true, analysisTypes);
          if(experimentFileSize == null){
            experimentFileSize = new BigDecimal(0);
          }
          summaryDiskSpaceByExperimentNode.setAttribute("diskSpace",   experimentFileSize.toString());      
          summaryDiskSpaceByExperimentNode.setAttribute("diskSpaceMB", experimentFileSize.divide(MB, BigDecimal.ROUND_HALF_EVEN).toString());
          summaryDiskSpaceByExperimentNode.setAttribute("diskSpaceGB", experimentFileSize.divide(GB, BigDecimal.ROUND_HALF_EVEN).toString());

          // Now add the analysis disk space
          diskSpaceMap = new TreeMap();
          queryBuf = new StringBuffer("SELECT at.analysisType, sum(af.fileSize) ");
          queryBuf.append(" FROM Analysis a, AnalysisType at ");
          queryBuf.append(" JOIN a.files as af ");
          queryBuf.append(" WHERE a.idAnalysisType = at.idAnalysisType ");
          queryBuf.append(" AND af.fileSize is not NULL ");
          queryBuf.append(" GROUP BY at.analysisType ");
          queryBuf.append(" ORDER BY at.analysisType ");
          summaryRows = sess.createQuery(queryBuf.toString()).list();
          BigDecimal analysisFileSize   = (BigDecimal)sess.createQuery("SELECT sum(af.fileSize) from Analysis a join a.files as af where af.fileSize is not NULL").uniqueResult();
          mapDiskSpace(summaryRows, diskSpaceMap);
          addEntryDiskSpaceNodes(summaryDiskSpaceByAnalysisNode, diskSpaceMap, false, true, analysisTypes);

          if(analysisFileSize == null){
            analysisFileSize = new BigDecimal(0);
          }
          summaryDiskSpaceByAnalysisNode.setAttribute("diskSpace",   analysisFileSize.toString());      
          summaryDiskSpaceByAnalysisNode.setAttribute("diskSpaceMB", analysisFileSize.divide(MB, BigDecimal.ROUND_HALF_EVEN).toString());
          summaryDiskSpaceByAnalysisNode.setAttribute("diskSpaceGB", analysisFileSize.divide(GB, BigDecimal.ROUND_HALF_EVEN).toString());

          totalDiskSpace = experimentFileSize.add(analysisFileSize);
          totalDiskSpaceNode.setAttribute("diskSpace",   totalDiskSpace.toString());      
          totalDiskSpaceNode.setAttribute("diskSpaceMB", totalDiskSpace.divide(MB, BigDecimal.ROUND_HALF_EVEN).toString());
          totalDiskSpaceNode.setAttribute("diskSpaceGB", totalDiskSpace.divide(GB, BigDecimal.ROUND_HALF_EVEN).toString());


        }



        // Get days since last upload
        rank = 0;
        buf = new StringBuffer();
        buf.append("SELECT tl.idLab, max(tl.startDateTime) ");
        buf.append("FROM TransferLog tl ");
        buf.append("JOIN tl.lab as lab ");
        buf.append("JOIN lab.coreFacilities as cf ");
        buf.append("WHERE tl.transferType = 'upload' ");
        if (idCoreFacility != null) {
          buf.append("AND cf.idCoreFacility = " + idCoreFacility + " ");
        }
        buf.append("GROUP BY tl.idLab  ");
        buf.append("ORDER BY max(tl.startDateTime) desc");
        summaryRows = sess.createQuery(buf.toString()).list();
        this.addEntryDaysSinceNodes(summaryDaysNode, summaryRows, "days");


        // Tally counts by week
        if(!currentView.equals(FILES_VIEW)){
          tallyWeeklyActivity(sess);
          addWeeklyActivityNodes(summaryWeeklyActivityNode);
        }


        //
        // Get # of experiments by experiment platform
        //
        StringBuffer queryBuf = new StringBuffer();
        if(currentView.equals(EXPERIMENTS_VIEW) || currentView.equals(FILES_VIEW)){
          queryBuf = new StringBuffer("SELECT rc.requestCategory, count(*) ");
          queryBuf.append(" FROM Request r, RequestCategory rc ");
          queryBuf.append(" WHERE r.codeRequestCategory = rc.codeRequestCategory ");
          if (idCoreFacility != null) {
            queryBuf.append("AND r.idCoreFacility = " + idCoreFacility + " ");
          }
          queryBuf.append(" GROUP BY rc.requestCategory");
          queryBuf.append(" ORDER BY count(*) desc ");
          summaryRows = sess.createQuery(queryBuf.toString()).list();

          queryBuf = new StringBuffer("SELECT count(*) FROM Request r, RequestCategory rc WHERE r.codeRequestCategory = rc.codeRequestCategory ");
          if(idCoreFacility != null){
            queryBuf.append(" AND r.idCoreFacility = " + idCoreFacility + " ");
          }

          totalExperiments = (int) (long)sess.createQuery(queryBuf.toString()).uniqueResult();
          addEntryIntegerNodes(summaryExperimentsByTypeNode, summaryRows, "experimentCount", false);
          summaryExperimentsByTypeNode.setAttribute("experimentCount", totalExperiments.toString());
        }

        //
        // Get # of next gen seq experiments by application
        //
        if(currentView.equals(EXPERIMENTS_VIEW)){
          queryBuf = new StringBuffer("SELECT app.application, count(*) ");
          queryBuf.append(" FROM Request r, Application app, RequestCategory rc, RequestCategoryType rct ");
          queryBuf.append(" WHERE r.codeApplication = app.codeApplication ");
          queryBuf.append(" AND r.codeRequestCategory  = rc.codeRequestCategory ");
          queryBuf.append(" AND rct.codeRequestCategoryType  = rc.type ");
          queryBuf.append(" AND rct.isIllumina  = 'Y' ");
          if (idCoreFacility != null) {
            queryBuf.append("AND r.idCoreFacility = " + idCoreFacility + " ");
          }
          queryBuf.append(" GROUP BY app.application");
          queryBuf.append(" ORDER BY count(*) desc ");
          summaryRows = sess.createQuery(queryBuf.toString()).list();
          queryBuf = new StringBuffer("SELECT count(*) FROM Request r, Application app, RequestCategory rc, RequestCategoryType rct WHERE r.codeApplication = app.codeApplication AND r.codeRequestCategory = rc.codeRequestCategory AND rct.codeRequestCategoryType = rc.type AND rct.isIllumina = 'Y'");
          if(idCoreFacility != null){
            queryBuf.append(" AND r.idCoreFacility = " + idCoreFacility + " ");
          }
          totalExperiments = (int) (long)sess.createQuery(queryBuf.toString()).uniqueResult();
          rank = 0;
          totalCount = 0;
          addEntryIntegerNodes(summarySeqExperimentsByAppNode, summaryRows, "experimentCount", false);
          summarySeqExperimentsByAppNode.setAttribute("experimentCount", totalExperiments.toString());
        }



        //
        // Get # of analysis by analysis type
        //
        if (currentView.equals(ANALYSIS_VIEW) || currentView.equals(FILES_VIEW)){
          queryBuf = new StringBuffer("SELECT at.analysisType, count(*) ");
          queryBuf.append(" FROM Analysis a, AnalysisType at ");
          queryBuf.append(" WHERE a.idAnalysisType = at.idAnalysisType ");
          queryBuf.append(" GROUP BY at.analysisType");
          queryBuf.append(" ORDER BY count(*) desc ");
          summaryRows = sess.createQuery(queryBuf.toString()).list();
          addEntryIntegerNodes(summaryAnalysisByTypeNode, summaryRows, "analysisCount", false);
          summaryAnalysisByTypeNode.setAttribute("analysisCount", totalAnalysisCount.toString());
        }

        XMLOutputter out = new org.jdom.output.XMLOutputter();
        this.xmlResult = out.outputString(doc);

        setResponsePage(this.SUCCESS_JSP);
      }

    }catch (NamingException e){
      LOG.error("An exception has occurred in GetUsageData ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());

    }catch (SQLException e) {
      LOG.error("An exception has occurred in GetUsageData ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (XMLReflectException e){
      LOG.error("An exception has occurred in GetUsageData ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e){
      LOG.error("An exception has occurred in GetUsageData ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        this.getSecAdvisor().closeReadOnlyHibernateSession();        
      } catch(Exception e) {

      }
    }

    return this;
  }

  private void tallyWeeklyActivity(Session sess) {
    List summaryRows;
    StringBuffer buf = new StringBuffer();
    // Tally experiment count by week
    if(currentView.equals(EXPERIMENTS_VIEW)){
      buf.append("SELECT r.createDate, count(*) ");
      buf.append("FROM Request r ");
      if (idCoreFacility != null) {
        buf.append("WHERE r.idCoreFacility = " + idCoreFacility + " ");
      }
      buf.append("GROUP BY r.createDate ");
      buf.append("ORDER BY r.createDate");
      summaryRows = sess.createQuery(buf.toString()).list();
      for(Iterator i = summaryRows.iterator(); i.hasNext();) {
        Object[] rows = (Object[])i.next();
        java.util.Date createDate  = (java.util.Date)rows[0];
        Integer experimentCount   = (int) (long)rows[1];

        String createDateKey = dfNormal.format(createDate);
        Integer weekNumber = weekNumberMap.get(createDateKey);
        if (weekNumber != null) {
          ActivityInfo ai = this.weeklyActivityMap.get(weekNumber);
          if (ai != null) {
            ai.experimentCount += experimentCount;
          }
        }
      }
    }


    // Tally analysis count by week
    if(currentView.equals(ANALYSIS_VIEW)){
      summaryRows = sess.createQuery("SELECT a.createDate, count(*) from Analysis a group by a.createDate order by a.createDate").list();
      for(Iterator i = summaryRows.iterator(); i.hasNext();) {
        Object[] rows = (Object[])i.next();
        java.sql.Date createDate  = (java.sql.Date)rows[0];
        Integer analysisCount     = (int) (long)rows[1];

        String createDateKey = dfNormal.format(createDate);
        Integer weekNumber = weekNumberMap.get(createDateKey);
        if (weekNumber != null) {
          ActivityInfo ai = this.weeklyActivityMap.get(weekNumber);
          if (ai != null) {
            ai.analysisCount += analysisCount;
          }
        }
      }
    }


    // Tally upload count by week
    buf = new StringBuffer();
    if(currentView.equals(EXPERIMENTS_VIEW)){
      buf.append("SELECT tl.startDateTime, tl.fileName ");
      buf.append("FROM TransferLog tl ");
      buf.append("JOIN tl.request as r ");
      buf.append("WHERE transferType = 'upload' ");
      buf.append("AND tl.idRequest is not NULL ");
      if (idCoreFacility != null) {
        buf.append("AND r.idCoreFacility = " + idCoreFacility + " ");
      }    
      buf.append("GROUP BY tl.startDateTime, tl.fileName ");
      buf.append("ORDER BY tl.startDateTime, tl.fileName");
    }
    if(currentView.equals(ANALYSIS_VIEW)){
      buf.append("SELECT tl.startDateTime, tl.fileName ");
      buf.append("FROM TransferLog tl ");
      buf.append("WHERE transferType = 'upload' ");
      buf.append("AND tl.idAnalysis is not NULL ");    
      buf.append("GROUP BY tl.startDateTime, tl.fileName ");
      buf.append("ORDER BY tl.startDateTime, tl.fileName");
    }

    summaryRows = sess.createQuery(buf.toString()).list();

    Set<UsageRowDescriptor> uniqueEntries = new TreeSet<UsageRowDescriptor> (new UsageRowDescriptorComparator()); 
    TreeMap<UsageRowDescriptor, Integer> rowCounterMap = new TreeMap<UsageRowDescriptor, Integer> ();
    for(Iterator<Object> i = summaryRows.iterator(); i.hasNext();) {
      Object[] row = (Object[])i.next();
      UsageRowDescriptor thisDescriptor = new UsageRowDescriptor();

      thisDescriptor.setIdLab(new Integer(0));
      thisDescriptor.setLabLastName("");
      thisDescriptor.setLabFirstName("");
      thisDescriptor.setCreateDate(UsageRowDescriptor.stripTime((Date)row[0]));
      thisDescriptor.setNumber("");
      thisDescriptor.setFileName((String)row[1]); 

      // Use UsageRowDescriptor as key for count TreeMap by setting fileName to ""
      UsageRowDescriptor thisCounter = new UsageRowDescriptor();
      thisCounter.setUsageRowDescriptorAsCounter(thisDescriptor);

      if (uniqueEntries.add(thisDescriptor)) {
        // If current row descriptor not already on the list, then increment counter
        Integer thisCount = rowCounterMap.get(thisCounter);
        if(thisCount != null) {
          // If counter already present then increment
          thisCount = new Integer(thisCount.intValue()+1);        
        } else {
          // If counter not present then start at 1
          thisCount = new Integer(1);
        }
        rowCounterMap.put(thisCounter, thisCount);

      }
    } 
    // Now traverse the rowCounter list and retrieve the counts
    Iterator<UsageRowDescriptor> it = rowCounterMap.keySet().iterator();
    while(it.hasNext()) {
      UsageRowDescriptor thisRow = (UsageRowDescriptor) it.next();
      Integer thisCount = rowCounterMap.get(thisRow);


      String createDateKey = dfNormal.format(thisRow.getCreateDate());
      Integer weekNumber = weekNumberMap.get(createDateKey);
      if (weekNumber != null) {
        ActivityInfo ai = this.weeklyActivityMap.get(weekNumber);
        if (ai != null) {
          ai.uploadCount += thisCount;
        }
      }      
    }       

    // Tally download count by week
    buf = new StringBuffer();
    if(currentView.equals(EXPERIMENTS_VIEW)){
      buf.append("SELECT tl.startDateTime, count(tl.fileName) ");
      buf.append("FROM TransferLog tl ");
      buf.append("JOIN tl.request as r ");
      buf.append("WHERE transferType = 'download' ");
      buf.append("AND tl.idRequest is not NULL ");
      if (idCoreFacility != null) {
        buf.append("AND r.idCoreFacility = " + idCoreFacility + " ");
      }    
      buf.append("GROUP BY tl.startDateTime ");
      buf.append("ORDER BY tl.startDateTime, count(tl.fileName) ");
    }
    if(currentView.equals(ANALYSIS_VIEW)){
      buf.append("SELECT tl.startDateTime, count(tl.fileName) ");
      buf.append("FROM TransferLog tl ");
      buf.append("WHERE transferType = 'download' ");
      buf.append("AND tl.idAnalysis is not NULL "); 
      buf.append("GROUP BY tl.startDateTime ");
      buf.append("ORDER BY tl.startDateTime, count(tl.fileName) ");
    }

    summaryRows = sess.createQuery(buf.toString()).list();
    for(Iterator i = summaryRows.iterator(); i.hasNext();) {
      Object[] rows = (Object[])i.next();
      java.util.Date createDate  = (java.util.Date)rows[0];
      Integer  downloadCount  = (int) (long)rows[1];

      String createDateKey = dfNormal.format(createDate);
      Integer weekNumber = weekNumberMap.get(createDateKey);
      if (weekNumber != null) {
        ActivityInfo ai = this.weeklyActivityMap.get(weekNumber);
        if (ai != null) {
          ai.downloadCount += downloadCount;
        }
      }
    }

  }

  private void addWeeklyActivityNodes(Element parentNode) {

    // Create weekly activity entries
    String prevLabel = "";
    int weekCounter = 1;
    for (Iterator i = this.weeklyActivityMap.keySet().iterator(); i.hasNext();) {
      Integer weekNumber = (Integer)i.next();
      ActivityInfo ai = weeklyActivityMap.get(weekNumber);

      String label = "";
      if (!prevLabel.equals(ai.label)) {
        weekCounter = 1;
        label = ai.label;
      }


      Element entryNode = new Element("Entry");
      parentNode.addContent(entryNode);
      entryNode.setAttribute("label", label);
      entryNode.setAttribute("dataTip", ai.dataTip);
      entryNode.setAttribute("weekNumber", weekNumber.toString());
      if(currentView.equals(EXPERIMENTS_VIEW) || currentView.equals(FILES_VIEW)){
        entryNode.setAttribute("experimentCount", Integer.valueOf(ai.experimentCount).toString());
      }
      if(currentView.equals(ANALYSIS_VIEW) || currentView.equals(FILES_VIEW)){
        entryNode.setAttribute("analysisCount", Integer.valueOf(ai.analysisCount).toString());
      }
      entryNode.setAttribute("uploadCount", Integer.valueOf(ai.uploadCount).toString());
      entryNode.setAttribute("downloadCount", Integer.valueOf(ai.downloadCount).toString());
      entryNode.setAttribute("startDate", dfNormal.format(ai.startDate));

      prevLabel = ai.label;
      weekCounter++;
    }

  }

  private void addEntryIntegerNodes(Element parentNode, List summaryRows, String dataField, boolean mapToLab) {

    int rank = 0;
    int total = 0;
    for(Iterator i = summaryRows.iterator(); i.hasNext();) {
      Object[] rows = (Object[])i.next();
      Object label    = rows[0];
      Integer value   = (int) (long)rows[1];

      if (label == null) {
        continue;
      }

      if (mapToLab) {
        Integer idLab = Integer.valueOf(label.toString());
        Lab lab = (Lab)labIdMap.get(idLab);
        if (lab == null) {
          continue;
        }
        label = this.getLabName(lab);
      }

      Element entryNode = new Element("Entry");
      parentNode.addContent(entryNode);
      entryNode.setAttribute("label", label.toString());
      entryNode.setAttribute("labelFull", label + "  (" + value.toString() + ")");
      entryNode.setAttribute(dataField, value.toString());
      entryNode.setAttribute("rank", Integer.valueOf(rank).toString());

      rank++;

      total += value.intValue();

      if (rank > endRank.intValue()) {
        break;
      }
    }


  }

  private void addEntryDaysSinceNodes(Element parentNode, List summaryRows, String dataField) {
    int rank = 0;
    for(Iterator i = summaryRows.iterator(); i.hasNext();) {
      Object[] rows = (Object[])i.next();
      Integer idLab                   = (Integer)rows[0];
      java.util.Date lastUploadDate = (java.util.Date)rows[1];

      if (idLab == null || lastUploadDate == null) {
        continue;
      }

      Calendar lastUploadCalendar = new GregorianCalendar();
      lastUploadCalendar.setTime(lastUploadDate);

      int daysSinceLastUpload = daysBetween(lastUploadCalendar, today);

      Lab lab = labIdMap.get(idLab);

      if (lab == null) {
        continue;
      }

      Element entryNode = new Element("Entry");
      parentNode.addContent(entryNode);
      entryNode.setAttribute("label", getLabName(lab));
      entryNode.setAttribute(dataField, Integer.valueOf(daysSinceLastUpload).toString());
      entryNode.setAttribute("rank", Integer.valueOf(rank).toString());

      rank++;
      if (rank > endRank.intValue()) {
        break;
      }

    }

  }


  private void addEntryDiskSpaceNodes(Element parentNode, Map diskSpaceMap, boolean mapToLab, boolean sort, List analysisTypes) {
    // Sort by disk space
    Set diskSpaceInfos = sortDiskSpaceMap(diskSpaceMap, sort);

    int rank = 0;
    BigDecimal totalDiskSpace = new BigDecimal(0);
    for (Iterator i = diskSpaceInfos.iterator(); i.hasNext();) {
      DiskSpaceInfo info = (DiskSpaceInfo)i.next();


      BigDecimal fileSize   = info.totalFileSize;
      String label       = info.label;         

      BigDecimal fileSizeMB = fileSize.divide(MB, BigDecimal.ROUND_HALF_EVEN);
      BigDecimal fileSizeGB = fileSize.divide(GB, BigDecimal.ROUND_HALF_EVEN);

      if (label == null) {
        continue;
      }

      if (mapToLab) {

        Integer idLab = Integer.valueOf(label);
        Lab lab = (Lab)labIdMap.get(idLab);

        if (lab == null) {
          continue;
        }
        label = this.getLabName(lab);
      }

      Element entryNode = new Element("Entry");
      parentNode.addContent(entryNode);
      entryNode.setAttribute("label", label);
      entryNode.setAttribute("labelFull", label + " (" + fileSize.divide(GB, BigDecimal.ROUND_HALF_EVEN).toString() + " GB)");
      entryNode.setAttribute("diskSpaceBytes", fileSize.toString());
      entryNode.setAttribute("diskSpaceMB", fileSizeMB.toString());
      entryNode.setAttribute("diskSpaceGB", fileSizeGB.toString());
      entryNode.setAttribute("rank", Integer.valueOf(rank).toString());

      if(!mapToLab && analysisTypes.contains(label)){
        entryNode.setAttribute("isAnalysis", "Y");
      }
      else if(!mapToLab && !analysisTypes.contains(label)){
        entryNode.setAttribute("isAnalysis", "N");
      }

      rank++;

      totalDiskSpace = totalDiskSpace.add(fileSize);

      if (rank > endRank.intValue()) {
        break;
      }
    }

  }

  private void mapDiskSpace(List summaryRows, Map diskSpaceMap) {
    for(Iterator i = summaryRows.iterator(); i.hasNext();) {
      Object[] rows = (Object[])i.next();
      Object label            = rows[0];
      BigDecimal fileSize     = (BigDecimal)rows[1];
      if (fileSize == null) {
        fileSize = new BigDecimal(0);
      }

      if (label == null) {
        continue;
      }

      BigDecimal totalFileSize = (BigDecimal)diskSpaceMap.get(label);
      if (totalFileSize == null) {
        totalFileSize = new BigDecimal(0);
      }
      totalFileSize = totalFileSize.add(fileSize);
      diskSpaceMap.put(label, totalFileSize);
    }
  }

  private Set sortDiskSpaceMap(Map diskSpaceMap, boolean sort) {
    TreeSet diskInfos = new TreeSet(sort ? new DiskSpaceComparator() : new DiskSpaceRowNumberComparator());

    int rowNumber = 0;
    for (Iterator i = diskSpaceMap.keySet().iterator(); i.hasNext();) {
      Object label = i.next();
      BigDecimal totalFileSize = (BigDecimal)diskSpaceMap.get(label);

      DiskSpaceInfo info = new DiskSpaceInfo();
      info.label = label.toString();
      info.totalFileSize = totalFileSize;
      info.rowNumber = rowNumber++;
      diskInfos.add(info);
    }
    return diskInfos;

  }

  /**
   * Show lab label if logged in user is admin or usage_user_visibility set to 'full'.
   * If usage_user_visibility set to 'masked', mask lab names for labs that user
   * is not a part of (member, manager, or collaborator).
   */
  private String getLabName(Lab lab) {
    String labName = "";
    if (this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_ACCESS_ANY_OBJECT)) {
      labName = lab.getName(false, true);
    } else if (usageUserVisibility.equals(PropertyDictionary.OPTION_USER_USER_VISIBILITY_MASKED)) {
      if (this.getSecAdvisor().isGroupIAmMemberOf(lab.getIdLab()) || this.getSecAdvisor().isGroupICollaborateWith(lab.getIdLab())) {
        labName = lab.getName(false, true);
      } else {
        labName = "-";
      }
    } else {
      labName = lab.getName(false, true);
    }
    return labName;
  }

  private int daysBetween(Calendar startDate, Calendar endDate) {  
    Calendar date = (Calendar) startDate.clone();  
    int daysBetween = 0;  
    while (date.before(endDate)) {  
      date.add(Calendar.DAY_OF_MONTH, 1);  
      daysBetween++;  
    }  
    return daysBetween;  
  }  


  static class DiskSpaceComparator implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
      DiskSpaceInfo i1 = (DiskSpaceInfo)o1;
      DiskSpaceInfo i2 = (DiskSpaceInfo)o2;

      if (i1.label.equals(i2.label)) {
        return 0;
      } else {
        return i1.totalFileSize.compareTo(i2.totalFileSize) * -1;
      }
    }

  }
  static class DiskSpaceRowNumberComparator implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
      DiskSpaceInfo i1 = (DiskSpaceInfo)o1;
      DiskSpaceInfo i2 = (DiskSpaceInfo)o2;

      if (i1.rowNumber == i2.rowNumber) {
        return 0;
      } else {
        return Integer.valueOf(i1.rowNumber).compareTo(Integer.valueOf(i2.rowNumber));
      }
    }

  }

  static class DiskSpaceInfo {
    BigDecimal totalFileSize;
    String     label;
    int        rowNumber;
  }


  static class ActivityInfo implements Serializable {
    private String label = "";
    private Date startDate = null;
    private String dataTip;
    private int daysSinceLastUpload = -1;
    private int uploadCount = 0;
    private int downloadCount = 0;
    private int experimentCount = 0;
    private int analysisCount = 0;


  }

}