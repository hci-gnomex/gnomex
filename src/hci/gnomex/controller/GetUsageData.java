package hci.gnomex.controller;

import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.PropertyHelper;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.framework.utilities.Annotations;
import hci.framework.utilities.XMLReflectException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
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

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.jdom.Element;
import org.jdom.Document;
import org.jdom.output.XMLOutputter;

import hci.gnomex.model.Lab;
import hci.gnomex.model.LabFilter;
import hci.gnomex.model.Property;
import hci.gnomex.model.Visibility;


public class GetUsageData extends GNomExCommand implements Serializable {
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetUsageData.class);

  private String usageUserVisibility = "";
  
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
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      Document doc = new Document(new Element("UsageData"));
      
      DateFormat dfShort    = new SimpleDateFormat("MMM yyyy");
      DateFormat dfDataTip  = new SimpleDateFormat("MMM dd yyyy");
      DateFormat dfNormal   = new SimpleDateFormat("MM-dd-yyyy");

      
      Element summaryDaysNode = new Element("SummaryDaysSinceLastUpload");
      doc.getRootElement().addContent(summaryDaysNode);

      Element summaryUploadsNode = new Element("SummaryUploadsByLab");
      doc.getRootElement().addContent(summaryUploadsNode);
      
      Element summaryDiskSpaceNode = new Element("SummaryDiskSpaceByLab");
      doc.getRootElement().addContent(summaryDiskSpaceNode);
      
      Element summaryDiskSpaceByYearNode = new Element("SummaryDiskSpaceByYear");
      doc.getRootElement().addContent(summaryDiskSpaceByYearNode);

      
      Element summaryDiskSpaceByTypeNode = new Element("SummaryDiskSpaceByType");
      doc.getRootElement().addContent(summaryDiskSpaceByTypeNode);
      
      Element summaryDownloadsNode = new Element("SummaryDownloadsByLab");
      doc.getRootElement().addContent(summaryDownloadsNode);

      Element summaryExperimentsNode = new Element("SummaryExperimentsByLab");
      doc.getRootElement().addContent(summaryExperimentsNode);

      Element summaryAnalysisNode = new Element("SummaryAnalysisByLab");
      doc.getRootElement().addContent(summaryAnalysisNode);

      Element summaryWeeklyActivityNode = new Element("SummaryActivityByWeek");
      doc.getRootElement().addContent(summaryWeeklyActivityNode);
      
      Calendar today = Calendar.getInstance();
      
      
      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());

      usageUserVisibility = PropertyHelper.getInstance(sess).getProperty(Property.USAGE_USER_VISIBILITY);

      // Guests cannot run this command
      if (this.getSecAdvisor().isGuest()) {
        this.addInvalidField("Insufficient permissions", "Insufficient permission to get usage data.  Guests cannot access usage data.");
        setResponsePage(this.ERROR_JSP);
      } 
    
      // Admins can run this command.  Normal gnomex users can if usage_user_visibility
      // property is set to an appropriate level ('masked' or 'full').
      if (this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_ACCESS_ANY_OBJECT)) {
      } else if (usageUserVisibility.equals("") || usageUserVisibility.equals(Property.OPTION_USER_USER_VISIBILITY_NONE)) {
        this.addInvalidField("Insufficient permissions", "Insufficient permission to get usage data.  Property usage_user_visibility does not allow users to access usage data");
        setResponsePage(this.ERROR_JSP);
      }

      
      if (this.isValid()) {
        // Hash all labs
        List labs = sess.createQuery("SELECT l from Lab l order by l.lastName, l.firstName").list();
        for(Iterator i = labs.iterator(); i.hasNext();) {
          Lab lab = (Lab)i.next();
          
          // Don't show labs marked for exclusion in XML
          if (lab.getExcludeUsage() != null && lab.getExcludeUsage().equals("Y")) {
            continue;
          }
          
          labMap.put(lab.getName(), lab);
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
          
          weeklyActivityMap.put(Integer.valueOf(week), ai);
          
          int x = 1;
          for (x = 1; x < 8; x++) {
            now.add(Calendar.DATE, 1);
            weekNumberMap.put(dfNormal.format(now.getTime()), week);
          }
          
          week++;
        }
        
        
        
        // Get experiment count
        int rank = 0;
        int totalCount = 0;
        List summaryRows = sess.createQuery("SELECT r.idLab, count(*) from Request r group by r.idLab order by count(*) desc").list();
        for(Iterator i = summaryRows.iterator(); i.hasNext();) {
          Object[] rows = (Object[])i.next();
          Integer idLab           = (Integer)rows[0];
          Integer experimentCount = (Integer)rows[1];
          Lab lab = labIdMap.get(idLab);
          
          if (lab == null) {
            continue;
          }
          
          Element entryNode = new Element("Entry");
          summaryExperimentsNode.addContent(entryNode);
          entryNode.setAttribute("label", getLabName(lab));
          entryNode.setAttribute("experimentCount", Integer.valueOf(experimentCount).toString());
          entryNode.setAttribute("rank", Integer.valueOf(rank).toString());
          
          rank++;
          
          totalCount += experimentCount;
          
          if (rank > endRank.intValue()) {
            break;
          }

        }
        summaryExperimentsNode.setAttribute("experimentCount", Integer.valueOf(totalCount).toString());
        
        // Get analysis count
        rank = 0;
        totalCount = 0;
        summaryRows = sess.createQuery("SELECT a.idLab, count(*) from Analysis a group by a.idLab order by count(*) desc").list();
        for(Iterator i = summaryRows.iterator(); i.hasNext();) {
          Object[] rows = (Object[])i.next();
          Integer idLab           = (Integer)rows[0];
          Integer analysisCount   = (Integer)rows[1];
          Lab lab = labIdMap.get(idLab);
          
          if (lab == null) {
            continue;
          }

          Element entryNode = new Element("Entry");
          summaryAnalysisNode.addContent(entryNode);
          entryNode.setAttribute("label", getLabName(lab));
          entryNode.setAttribute("analysisCount", Integer.valueOf(analysisCount).toString());
          entryNode.setAttribute("rank", Integer.valueOf(rank).toString());
          
          rank++;
          
          totalCount += analysisCount;
          
          if (rank > endRank.intValue()) {
            break;
          }
        }
        summaryAnalysisNode.setAttribute("analysisCount", Integer.valueOf(totalCount).toString());
        
        
        // Get upload count
        rank = 0;
        summaryRows = sess.createQuery("SELECT tl.idLab, count(*) from TransferLog tl where tl.transferType = 'upload' group by tl.idLab order by count(*) desc").list();
        for(Iterator i = summaryRows.iterator(); i.hasNext();) {
          Object[] rows = (Object[])i.next();
          Integer idLab           = (Integer)rows[0];
          Integer uploadCount     = (Integer)rows[1];
          
          if (idLab == null) {
            continue;
          }
          
          Lab lab = labIdMap.get(idLab);
          
          if (lab == null) {
            continue;
          }
          
          Element entryNode = new Element("Entry");
          summaryUploadsNode.addContent(entryNode);
          entryNode.setAttribute("label", getLabName(lab));
          entryNode.setAttribute("uploadCount", Integer.valueOf(uploadCount).toString());
          entryNode.setAttribute("rank", Integer.valueOf(rank).toString());
          
          rank++;
          
          if (rank > endRank.intValue()) {
            break;
          }
        }
        
        // Get download count
        rank = 0;
        summaryRows = sess.createQuery("SELECT tl.idLab, count(*) from TransferLog tl where tl.transferType = 'download' group by tl.idLab order by count(*) desc").list();
        for(Iterator i = summaryRows.iterator(); i.hasNext();) {
          Object[] rows = (Object[])i.next();
          Integer idLab           = (Integer)rows[0];
          Integer downloadCount   = (Integer)rows[1];
          
          if (idLab == null) {
            continue;
          }
          
          Lab lab = labIdMap.get(idLab);
          
          if (lab == null) {
            continue;
          }
          
          Element entryNode = new Element("Entry");
          summaryDownloadsNode.addContent(entryNode);
          entryNode.setAttribute("label", getLabName(lab));
          entryNode.setAttribute("downloadCount", Integer.valueOf(downloadCount).toString());
          entryNode.setAttribute("rank", Integer.valueOf(rank).toString());
          
          rank++;
          if (rank > endRank.intValue()) {
            break;
          }

        }
        

        //
        // Get disk space by lab
        //
        // Get total file size for experiments by lab
        TreeMap diskSpaceMap = new TreeMap();
        summaryRows = sess.createQuery("SELECT r.idLab, sum(ef.fileSize) from Request r join r.files as ef  group by r.idLab").list();
        mapDiskSpace(summaryRows, diskSpaceMap);
        // Add in total file size for analysis by lab
        summaryRows = sess.createQuery("SELECT a.idLab, sum(af.fileSize) from Analysis a join a.files as af  group by a.idLab").list();
        mapDiskSpace(summaryRows, diskSpaceMap);
        Set diskSpaceInfos = sortDiskSpaceMap(diskSpaceMap);
        
        rank = 0;
        BigDecimal totalDiskSpace = new BigDecimal(0);
        for (Iterator i = diskSpaceInfos.iterator(); i.hasNext();) {
          DiskSpaceInfo info = (DiskSpaceInfo)i.next();
          
        
          BigDecimal fileSize   = info.totalFileSize;
          Integer idLab         = Integer.valueOf(info.label);
          
          BigDecimal fileSizeMB = fileSize.divide(MB, BigDecimal.ROUND_HALF_EVEN);
          BigDecimal fileSizeGB = fileSize.divide(GB, BigDecimal.ROUND_HALF_EVEN);
       
          
          Lab lab = labIdMap.get(idLab);
          
          if (lab == null) {
            continue;
          }
          
          Element entryNode = new Element("Entry");
          summaryDiskSpaceNode.addContent(entryNode);
          entryNode.setAttribute("label", getLabName(lab));
          entryNode.setAttribute("diskSpaceBytes", fileSize.toString());
          entryNode.setAttribute("diskSpaceMB", fileSizeMB.toString());
          entryNode.setAttribute("diskSpaceGB", fileSizeGB.toString());
          entryNode.setAttribute("rank", Integer.valueOf(rank).toString());
          
          rank++;
          
          totalDiskSpace = totalDiskSpace.add(fileSize);
          
          if (rank > endRank.intValue()) {
            break;
          }
        }
        summaryDiskSpaceNode.setAttribute("diskSpace",   totalDiskSpace.toString());      
        summaryDiskSpaceNode.setAttribute("diskSpaceMB", totalDiskSpace.divide(MB, BigDecimal.ROUND_HALF_EVEN).toString());
        summaryDiskSpaceNode.setAttribute("diskSpaceGB", totalDiskSpace.divide(GB, BigDecimal.ROUND_HALF_EVEN).toString());
        
        //
        // Get disk space by year
        //
        diskSpaceMap = new TreeMap();
        summaryRows = sess.createQuery("SELECT year(r.createDate), sum(ef.fileSize) from Request r join r.files as ef  group by year(r.createDate) ").list();
        mapDiskSpace(summaryRows, diskSpaceMap);
        summaryRows = sess.createQuery("SELECT year(a.createDate), sum(af.fileSize) from Analysis a join a.files as af  group by year(a.createDate) ").list();
        mapDiskSpace(summaryRows, diskSpaceMap);
        diskSpaceInfos = sortDiskSpaceMap(diskSpaceMap);
        
        rank = 0;
        totalDiskSpace = new BigDecimal(0);
        for (Iterator i = diskSpaceMap.keySet().iterator(); i.hasNext();) {
          Object year           = i.next();
          BigDecimal fileSize   = (BigDecimal)diskSpaceMap.get(year);
          
          BigDecimal fileSizeMB = fileSize.divide(MB, BigDecimal.ROUND_HALF_EVEN);
          BigDecimal fileSizeGB = fileSize.divide(GB, BigDecimal.ROUND_HALF_EVEN);

          
          Element entryNode = new Element("Entry");
          summaryDiskSpaceByYearNode.addContent(entryNode);
          entryNode.setAttribute("label", year.toString());
          entryNode.setAttribute("diskSpaceBytes", fileSize.toString());
          entryNode.setAttribute("diskSpaceMB", fileSizeMB.toString());
          entryNode.setAttribute("diskSpaceGB", fileSizeGB.toString());
          entryNode.setAttribute("rank", Integer.valueOf(rank).toString());

          totalDiskSpace = totalDiskSpace.add(fileSize);
          
          rank++;
          
          if (rank > endRank.intValue()) {
            break;
          }
        }
        summaryDiskSpaceByYearNode.setAttribute("diskSpace",   totalDiskSpace.toString());      
        summaryDiskSpaceByYearNode.setAttribute("diskSpaceMB", totalDiskSpace.divide(MB, BigDecimal.ROUND_HALF_EVEN).toString());
        summaryDiskSpaceByYearNode.setAttribute("diskSpaceGB", totalDiskSpace.divide(GB, BigDecimal.ROUND_HALF_EVEN).toString());
        
        //
        // Get disk space by analysis vs. experiment
        //
        diskSpaceMap = new TreeMap();
        StringBuffer queryBuf = new StringBuffer("SELECT rc.requestCategory, sum(ef.fileSize) ");
        queryBuf.append(" from Request r, RequestCategory rc ");
        queryBuf.append(" join r.files as ef ");
        queryBuf.append(" where r.codeRequestCategory = rc.codeRequestCategory ");
        queryBuf.append(" group by rc.requestCategory");
        queryBuf.append(" order by rc.requestCategory ");
        summaryRows = sess.createQuery(queryBuf.toString()).list();
        mapDiskSpace(summaryRows, diskSpaceMap);

        // Now add the analysis disk space
        BigDecimal analysisFileSize   = (BigDecimal)sess.createQuery("SELECT sum(af.fileSize) from Analysis a join a.files as af ").uniqueResult();
        diskSpaceMap.put("Analysis", analysisFileSize);
        rank = 0;
        totalDiskSpace = new BigDecimal(0);
        for (Iterator i = diskSpaceMap.keySet().iterator(); i.hasNext();) {
          Object category       = i.next();
          BigDecimal fileSize   = (BigDecimal)diskSpaceMap.get(category);
          
          
          BigDecimal fileSizeMB = fileSize.divide(MB, BigDecimal.ROUND_HALF_EVEN);
          BigDecimal fileSizeGB = fileSize.divide(GB, BigDecimal.ROUND_HALF_EVEN);
          
          if (category == null) {
            continue;
          }
          
          Element entryNode = new Element("Entry");
          summaryDiskSpaceByTypeNode.addContent(entryNode);
          entryNode.setAttribute("label", category.toString());
          entryNode.setAttribute("labelGB", category + " (" + fileSize.divide(GB, BigDecimal.ROUND_HALF_EVEN).toString() + " GB)");
          entryNode.setAttribute("diskSpaceBytes", fileSize.toString());
          entryNode.setAttribute("diskSpaceMB", fileSizeMB.toString());
          entryNode.setAttribute("diskSpaceGB", fileSizeGB.toString());
          entryNode.setAttribute("rank", Integer.valueOf(rank).toString());
          
          rank++;
          
          totalDiskSpace = totalDiskSpace.add(fileSize);
          
          if (rank > endRank.intValue()) {
            break;
          }
        }
        summaryDiskSpaceByTypeNode.setAttribute("diskSpace",   totalDiskSpace.toString());      
        summaryDiskSpaceByTypeNode.setAttribute("diskSpaceMB", totalDiskSpace.divide(MB, BigDecimal.ROUND_HALF_EVEN).toString());
        summaryDiskSpaceByTypeNode.setAttribute("diskSpaceGB", totalDiskSpace.divide(GB, BigDecimal.ROUND_HALF_EVEN).toString());
        
        
        // Get days since last upload
        rank = 0;
        summaryRows = sess.createQuery("SELECT tl.idLab, max(tl.startDateTime) from TransferLog tl where tl.transferType = 'upload' group by tl.idLab  order by max(tl.startDateTime) desc").list();
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
          summaryDaysNode.addContent(entryNode);
          entryNode.setAttribute("label", getLabName(lab));
          entryNode.setAttribute("days", Integer.valueOf(daysSinceLastUpload).toString());
          entryNode.setAttribute("rank", Integer.valueOf(rank).toString());

          rank++;
          if (rank > endRank.intValue()) {
            break;
          }

        }
        

        // Tally experiment count by week
        summaryRows = sess.createQuery("SELECT r.createDate, count(*) from Request r group by r.createDate order by r.createDate").list();
        for(Iterator i = summaryRows.iterator(); i.hasNext();) {
          Object[] rows = (Object[])i.next();
          java.sql.Date createDate  = (java.sql.Date)rows[0];
          Integer experimentCount   = (Integer)rows[1];
          
          String createDateKey = dfNormal.format(createDate);
          Integer weekNumber = weekNumberMap.get(createDateKey);
          if (weekNumber != null) {
            ActivityInfo ai = this.weeklyActivityMap.get(weekNumber);
            if (ai != null) {
              ai.experimentCount += experimentCount;
            }
          }
        }
        
        
        // Tally analysis count by week
        summaryRows = sess.createQuery("SELECT a.createDate, count(*) from Analysis a group by a.createDate order by a.createDate").list();
        for(Iterator i = summaryRows.iterator(); i.hasNext();) {
          Object[] rows = (Object[])i.next();
          java.sql.Date createDate  = (java.sql.Date)rows[0];
          Integer analysisCount     = (Integer)rows[1];
          
          String createDateKey = dfNormal.format(createDate);
          Integer weekNumber = weekNumberMap.get(createDateKey);
          if (weekNumber != null) {
            ActivityInfo ai = this.weeklyActivityMap.get(weekNumber);
            if (ai != null) {
              ai.analysisCount += analysisCount;
            }
          }
        }
        
        
        
        // Tally upload count by week
        summaryRows = sess.createQuery("SELECT tl.startDateTime, count(*) from TransferLog tl where transferType = 'upload' group by tl.startDateTime order by tl.startDateTime").list();
        for(Iterator i = summaryRows.iterator(); i.hasNext();) {
          Object[] rows = (Object[])i.next();
          java.util.Date createDate  = (java.util.Date)rows[0];
          Integer uploadCount     = (Integer)rows[1];
          
          String createDateKey = dfNormal.format(createDate);
          Integer weekNumber = weekNumberMap.get(createDateKey);
          if (weekNumber != null) {
            ActivityInfo ai = this.weeklyActivityMap.get(weekNumber);
            if (ai != null) {
              ai.uploadCount += uploadCount;
            }
          }
        }
        
        
        // Tally download count by week
        summaryRows = sess.createQuery("SELECT tl.startDateTime, count(*) from TransferLog tl where transferType = 'download' group by tl.startDateTime order by tl.startDateTime").list();
        for(Iterator i = summaryRows.iterator(); i.hasNext();) {
          Object[] rows = (Object[])i.next();
          java.util.Date createDate  = (java.util.Date)rows[0];
          Integer downloadCount     = (Integer)rows[1];
          
          String createDateKey = dfNormal.format(createDate);
          Integer weekNumber = weekNumberMap.get(createDateKey);
          if (weekNumber != null) {
            ActivityInfo ai = this.weeklyActivityMap.get(weekNumber);
            if (ai != null) {
              ai.downloadCount += downloadCount;
            }
          }
        }

        
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
          summaryWeeklyActivityNode.addContent(entryNode);
          entryNode.setAttribute("label", label);
          entryNode.setAttribute("dataTip", ai.dataTip);
          entryNode.setAttribute("weekNumber", weekNumber.toString());
          entryNode.setAttribute("experimentCount", Integer.valueOf(ai.experimentCount).toString());
          entryNode.setAttribute("analysisCount", Integer.valueOf(ai.analysisCount).toString());
          entryNode.setAttribute("uploadCount", Integer.valueOf(ai.uploadCount).toString());
          entryNode.setAttribute("downloadCount", Integer.valueOf(ai.downloadCount).toString());
          
          prevLabel = ai.label;
          weekCounter++;
        }
        
        

        XMLOutputter out = new org.jdom.output.XMLOutputter();
        this.xmlResult = out.outputString(doc);

        setResponsePage(this.SUCCESS_JSP);
      }
      
    }catch (NamingException e){
      log.error("An exception has occurred in GetUsageData ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }catch (SQLException e) {
      log.error("An exception has occurred in GetUsageData ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (XMLReflectException e){
      log.error("An exception has occurred in GetUsageData ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e){
      log.error("An exception has occurred in GetUsageData ", e);
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

  private void mapDiskSpace(List summaryRows, Map diskSpaceMap) {
    for(Iterator i = summaryRows.iterator(); i.hasNext();) {
      Object[] rows = (Object[])i.next();
      Object label            = rows[0];
      BigDecimal fileSize     = (BigDecimal)rows[1];
      
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
  
  private Set sortDiskSpaceMap(Map diskSpaceMap) {
    TreeSet diskInfos = new TreeSet(new DiskSpaceComparator());
    
    for (Iterator i = diskSpaceMap.keySet().iterator(); i.hasNext();) {
      Object label = i.next();
      BigDecimal totalFileSize = (BigDecimal)diskSpaceMap.get(label);
      
      DiskSpaceInfo info = new DiskSpaceInfo();
      info.label = label.toString();
      info.totalFileSize = totalFileSize;
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
      labName = lab.getName();
    } else if (usageUserVisibility.equals(Property.OPTION_USER_USER_VISIBILITY_MASKED)) {
      if (this.getSecAdvisor().isGroupIAmMemberOf(lab.getIdLab()) || this.getSecAdvisor().isGroupICollaborateWith(lab.getIdLab())) {
        labName = lab.getName();
      } else {
        labName = "-";
      }
    } else {
      labName = lab.getName();
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
 
 static class DiskSpaceInfo {
   BigDecimal totalFileSize;
   String     label;
 }
  

  static class ActivityInfo implements Serializable {
    private String label = "";
    private String dataTip;
    private int daysSinceLastUpload = -1;
    private int uploadCount = 0;
    private int downloadCount = 0;
    private int experimentCount = 0;;
    private int analysisCount = 0;
    
  }

}