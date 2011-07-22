package hci.gnomex.controller;

import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.HibernateSession;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.framework.utilities.Annotations;
import hci.framework.utilities.XMLReflectException;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
import hci.gnomex.model.Visibility;


public class GetUsageData extends GNomExCommand implements Serializable {
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetUsageData.class);
  
  private TreeMap<String, Lab>  labMap   = new TreeMap<String, Lab>();
  private HashMap<Integer, Lab> labIdMap = new HashMap<Integer, Lab>();
  private TreeMap<Integer, ActivityInfo> weeklyActivityMap = new TreeMap<Integer, ActivityInfo>();
  private HashMap<String, Integer> weekNumberMap = new HashMap<String, Integer>();
  

  private String asOfLast6Months = "N";
  private String asOfLastYear = "N";
  private String asOfLast2Years = "N";
  
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
    
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      Document doc = new Document(new Element("UsageData"));
      
      DateFormat dfShort = new SimpleDateFormat("MMM yyyy");
      DateFormat dfNormal = new SimpleDateFormat("MM-dd-yyyy");

      
      Element summaryDaysNode = new Element("Summary");
      summaryDaysNode.setAttribute("name", "DaysSinceLastUpload");
      doc.getRootElement().addContent(summaryDaysNode);

      Element summaryUploadsNode = new Element("Summary");
      summaryUploadsNode.setAttribute("name", "UploadsByLab");
      doc.getRootElement().addContent(summaryUploadsNode);

      Element summaryDownloadsNode = new Element("Summary");
      summaryDownloadsNode.setAttribute("name", "DownloadsByLab");
      doc.getRootElement().addContent(summaryDownloadsNode);

      Element summaryExperimentsNode = new Element("Summary");
      summaryExperimentsNode.setAttribute("name", "ExperimentsByLab");
      doc.getRootElement().addContent(summaryExperimentsNode);

      Element summaryAnalysisNode = new Element("Summary");
      summaryAnalysisNode.setAttribute("name", "AnalysisByLab");
      doc.getRootElement().addContent(summaryAnalysisNode);

      Element summaryWeeklyActivityNode = new Element("Summary");
      summaryWeeklyActivityNode.setAttribute("name", "ActivityByWeek");
      doc.getRootElement().addContent(summaryWeeklyActivityNode);
      
      Calendar today = Calendar.getInstance();
      

      // Only admins can run this command
      if (!this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_WRITE_ANY_OBJECT)) {
        this.addInvalidField("Insufficient permissions", "Insufficient permission to get usage data.");
        setResponsePage(this.ERROR_JSP);
      } else {
        Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
        
        // Hash all labs
        List labs = sess.createQuery("SELECT l from Lab l order by l.lastName, l.firstName").list();
        for(Iterator i = labs.iterator(); i.hasNext();) {
          Lab lab = (Lab)i.next();
          labMap.put(lab.getName(), lab);
          labIdMap.put(lab.getIdLab(), lab);
        }
        
        // Hash all weeks from 2007 on
        Calendar now = new GregorianCalendar().getInstance();
        if (asOfLast6Months.equals("Y")) {
          now.add(Calendar.MONTH, -6);          
        }else if (asOfLastYear.equals("Y")) {
          now.add(Calendar.YEAR, -1);          
        }else if (asOfLast2Years.equals("Y")) {
          now.add(Calendar.YEAR, - 2);          
        } else {
          now.set(Calendar.MONTH, Calendar.JANUARY);
          now.set(Calendar.DAY_OF_MONTH, 1);
          now.set(Calendar.YEAR, 2007);       
        }
        
        int week = 0;
        while (true) {
          if (now.after(today)) {
            break;
          }

          ActivityInfo ai = new ActivityInfo();
          ai.label =  dfShort.format(now.getTime());
          ai.startDate = now;
          
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
        List summaryRows = sess.createQuery("SELECT r.idLab, count(*) from Request r group by r.idLab order by count(*) desc").list();
        for(Iterator i = summaryRows.iterator(); i.hasNext();) {
          Object[] rows = (Object[])i.next();
          Integer idLab           = (Integer)rows[0];
          Integer experimentCount = (Integer)rows[1];
          Lab lab = labIdMap.get(idLab);
          
          Element entryNode = new Element("Entry");
          summaryExperimentsNode.addContent(entryNode);
          entryNode.setAttribute("label", lab.getName());
          entryNode.setAttribute("experimentCount", Integer.valueOf(experimentCount).toString());
          entryNode.setAttribute("rank", Integer.valueOf(rank).toString());
          
          rank++;
        }
        
        // Get analysis count
        rank = 0;
        summaryRows = sess.createQuery("SELECT a.idLab, count(*) from Analysis a group by a.idLab order by count(*) desc").list();
        for(Iterator i = summaryRows.iterator(); i.hasNext();) {
          Object[] rows = (Object[])i.next();
          Integer idLab           = (Integer)rows[0];
          Integer analysisCount   = (Integer)rows[1];
          Lab lab = labIdMap.get(idLab);
          
          Element entryNode = new Element("Entry");
          summaryAnalysisNode.addContent(entryNode);
          entryNode.setAttribute("label", lab.getName());
          entryNode.setAttribute("analysisCount", Integer.valueOf(analysisCount).toString());
          entryNode.setAttribute("rank", Integer.valueOf(rank).toString());
          
          rank++;
        }
        
        
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
          
          Element entryNode = new Element("Entry");
          summaryUploadsNode.addContent(entryNode);
          entryNode.setAttribute("label", lab.getName());
          entryNode.setAttribute("uploadCount", Integer.valueOf(uploadCount).toString());
          entryNode.setAttribute("rank", Integer.valueOf(rank).toString());
          
          rank++;
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
          
          Element entryNode = new Element("Entry");
          summaryDownloadsNode.addContent(entryNode);
          entryNode.setAttribute("label", lab.getName());
          entryNode.setAttribute("downloadCount", Integer.valueOf(downloadCount).toString());
          entryNode.setAttribute("rank", Integer.valueOf(rank).toString());
          
          rank++;
        }
        
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
          
          Element entryNode = new Element("Entry");
          summaryDaysNode.addContent(entryNode);
          entryNode.setAttribute("label", lab.getName());
          entryNode.setAttribute("days", Integer.valueOf(daysSinceLastUpload).toString());
          entryNode.setAttribute("rank", Integer.valueOf(rank).toString());

          rank++;
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
          
          if (!prevLabel.equals(ai.label)) {
            weekCounter = 1;
          }
          
          Element entryNode = new Element("Entry");
          summaryWeeklyActivityNode.addContent(entryNode);
          entryNode.setAttribute("label", ai.label + " (" + weekCounter + ")");
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

  private int daysBetween(Calendar startDate, Calendar endDate) {  
    Calendar date = (Calendar) startDate.clone();  
    int daysBetween = 0;  
    while (date.before(endDate)) {  
      date.add(Calendar.DAY_OF_MONTH, 1);  
      daysBetween++;  
    }  
    return daysBetween;  
  }  
  

 
  

  static class ActivityInfo implements Serializable {
    private String label = "";
    private Calendar startDate;
    private int daysSinceLastUpload = -1;
    private int uploadCount = 0;
    private int downloadCount = 0;
    private int experimentCount = 0;;
    private int analysisCount = 0;
    
  }

}