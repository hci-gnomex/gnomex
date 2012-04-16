package hci.gnomex.daemon;

import hci.framework.control.RollBackCommandException;
import hci.framework.model.FieldFormatter;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.Lab;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.utility.BatchDataSource;
import hci.gnomex.utility.BatchMailer;
import hci.gnomex.utility.MailUtil;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Level;
import org.hibernate.Session;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class UsageReportd extends TimerTask {

  private static long                 fONCE_PER_WEEK = 1000*60*60*24*7; // A day in milliseconds
  
  private static int                  fONE_DAY = 1;
  private static int                  wakeupHour = 2;    // Default wakupHour is 2 am
  private static int                  fZERO_MINUTES = 0;
  
  private BatchDataSource             dataSource;
  private Session                     sess;

  private String                      serverName = "localhost";
  
  private String                      baseURL="https://b2b.hci.utah.edu/gnomex";
  
  private String                      bccTo="";
  
  private ArrayList<String>           waList; 
  
  private PropertyDictionaryHelper    propertyHelper; 
  
  private static UsageReportd         app;
  
  private Properties                  mailProps;
  
  private boolean                     runAsDaemon = false;
  
  private boolean                     isTestMode = false;
  
  private FieldFormatter              fFormat;
  
  private Calendar                    startDate = null;
  private Calendar                    endDate = null;
  
  private HashMap<Integer, LabStats>  labInfo = new HashMap<Integer, LabStats>();
  private TreeMap sorted_map;
  
  private String orionPath = "";
  private String schemaPath = "";

  
  public UsageReportd(String[] args) {
    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("-server")) {
        serverName = args[++i];
      } else if (args[i].equals("-wakeupHour")) {
        wakeupHour = Integer.valueOf(args[++i]);
      } else if (args[i].equals ("-runAsDaemon")) {
        runAsDaemon = true;
      } else if (args[i].equals ("-isTestMode")) {
        isTestMode = true;
      } else if (args[i].equals ("-baseURL")) {
        baseURL = args[++i];
      } else if (args[i].equals ("-orionPath")) {
        orionPath = args[++i];
      } else if (args[i].equals ("-bccTo")) {
        bccTo = args[++i];
      } else if (args[i].equals ("-schemaPath")) {
        schemaPath = args[++i];
      }
    } 
    
    try {
      mailProps = new BatchMailer(orionPath).getMailProperties();
    } catch (Exception e){
      System.err.println("Cannot initialize mail properties");
      System.exit(0);
    }
  }
  
  /**
   * @param args
   */
  public static void main(String[] args) {
    app  = new UsageReportd(args);
    
    // Can either be run as daemon or run once (for scheduled execution - e.g. crontab)
    if(app.runAsDaemon) {
      // Perform the task once a day at <wakeupHour>., starting tomorrow morning
      Timer timer = new Timer();
      timer.scheduleAtFixedRate(app, getWakeupTime(), fONCE_PER_WEEK);       
    } else {
      app.run();
    }
  }

  @Override
  public void run() {
    Calendar calendar = Calendar.getInstance();
    
    startDate = new GregorianCalendar();
    startDate.add(Calendar.DAY_OF_YEAR, -7);
    endDate = new GregorianCalendar();
    endDate.add(Calendar.DAY_OF_YEAR, 1);

    
    fFormat = new FieldFormatter();
    
    try {
      org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger("org.hibernate");
      log.setLevel(Level.ERROR);

      dataSource = new BatchDataSource(orionPath, schemaPath);
      app.connect();
      
      propertyHelper = PropertyDictionaryHelper.getInstance(sess);

      // Get a list of all active users with email accounts
      List appUsers = sess.createQuery("SELECT a from AppUser a where a.isActive = 'Y' and a.email is not NULL and a.email != '' ORDER BY a.lastName, a.firstName ").list();

      StringBuffer distributionList = new StringBuffer();
      boolean isFirst = true;
      for (Iterator i = appUsers.iterator(); i.hasNext();) {
        AppUser appUser = (AppUser)i.next();
        String addComma = ", ";
        if(isFirst) {
          addComma = "";
          isFirst = false;
        }
        distributionList.append(addComma + appUser.getEmail());
      }
      
      String toList = distributionList.toString();
      
      String replyEmail = propertyHelper.getQualifiedProperty(PropertyDictionary.REPLY_EMAIL_CORE_FACILITY_WORKAUTH_REMINDER, serverName);
      if(replyEmail == null || replyEmail.length() == 0) {
        replyEmail = "DoNotReply@hci.utah.edu";
      }
      
      String site_title = propertyHelper.getProperty(PropertyDictionary.SITE_TITLE);
      if(site_title==null) {
        site_title = "";
      } else {
        site_title = site_title + " ";
      }
      
      String subject = "GNomEx " + site_title + "Weekly Usage Report";
      
      SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
      String todaysDate = sdf.format(new Date());
      
      getUsageByLab(sess);

      StringBuffer tableRows = new StringBuffer("");
      // Table Title      
      //body.append(subject + " for " + todaysDate + "<br>");     
      tableRows.append("<tr><td width='200' colspan='6' align='center'><span class='fontClassBold'>" + subject + " for " + todaysDate + "</span></td></tr>");
      
      
      // Table Header
      tableRows.append("<tr><td width='200'><span class='fontClassBold'>Lab<br>&nbsp;</span></td>");
      tableRows.append("<td width='120' align='center'><span class='fontClassBold'>Number of<br>Experiments</span></td>");
      tableRows.append("<td width='120' align='center'><span class='fontClassBold'>Number of<br>Analyses</span></td>");
      tableRows.append("<td width='120' align='center'><span class='fontClassBold'>Number of<br>Uploads</span></td>");
      tableRows.append("<td width='120' align='center'><span class='fontClassBold'>Number of<br>Downloads</span></td>");
      tableRows.append("<td width='120' align='center'><span class='fontClassBold'>Days Since<br>Last Upload</span></td></tr>");

      // Stats for each lab
      for (Object key : sorted_map.keySet()) {
        LabStats value = (LabStats) sorted_map.get(key);
        String daysSinceLastUpload = "" + value.getDaysSinceLastUpload();
        if(value.getDaysSinceLastUpload() == -1) {
          daysSinceLastUpload = "-";
        }
        Set expAnalysisList = value.getExpAnalysisList().keySet();
        int expAnalysisCount = expAnalysisList.size();
        int labRowSpan = 1;
        if(expAnalysisCount > 0) {
          labRowSpan++;
        }
        tableRows.append("<tr><td width='200' rowspan='" + labRowSpan + "'><span class='fontClass'>" + value.getLabName() + "</span></td>");
        tableRows.append("<td width='120' align='right'><span class='fontClass'>" + value.getExperimentCountOutput() + "</span></td>");
        tableRows.append("<td width='120' align='right'><span class='fontClass'>" + value.getAnalysisCountOutput() + "</span></td>");
        tableRows.append("<td width='120' align='right'><span class='fontClass'>" + value.getUploadCountOutput() + "</span></td>");
        tableRows.append("<td width='120' align='right'><span class='fontClass'>" + value.getDownloadCountOutput() + "</span></td>");
        tableRows.append("<td width='120' align='right'><span class='fontClass'>" + daysSinceLastUpload + "</span></td></tr>");
        if(labRowSpan > 1) {
          tableRows.append("<tr><td colspan='5' width='600' align='left'><span class='fontClass'>Experiment/Analysis Links: <br>");
          for(Object eaKey : expAnalysisList) {
            String thisExpAnalysisNumber = (String) eaKey;
            String nameAndDescr = (String) value.getExpAnalysisList().get(eaKey);
            String href = baseURL + Constants.LAUNCH_APP_JSP + "?";
            if(thisExpAnalysisNumber.charAt(0) == 'A') {
              // Link to analysis
              href = href + "analysisNumber=" + thisExpAnalysisNumber + "&launchWindow=" + Constants.WINDOW_TRACK_ANALYSES;
            } else {
              // Link to experiment
              href = href + "requestNumber=" + thisExpAnalysisNumber + "&launchWindow=" + Constants.WINDOW_TRACK_REQUESTS;
            }
            tableRows.append("<a href='" + href + "'>" + thisExpAnalysisNumber  + "</a> " + " " + nameAndDescr + "<br>");            
          }
          tableRows.append("</span></td></tr>");            
        }
      }      
      // Build message body in html
      StringBuffer body = new StringBuffer("");
      
      body.append("<html><head><title>GNomEx Usage Report</title><meta http-equiv='content-style-type' content='text/css'></head>");
      body.append("<body leftmargin='0' marginwidth='0' topmargin='0' marginheight='0' offset='0' bgcolor='#FFFFFF'>");
      body.append("<style>.fontClass{font-size:11px;color:#000000;font-family:verdana;text-decoration:none;}");
      body.append(" .fontClassBold{font-size:11px;font-weight:bold;color:#000000;font-family:verdana;text-decoration:none;}");
      body.append(" .fontClassLgeBold{font-size:12px;line-height:22px;font-weight:bold;color:#000000;font-family:verdana;text-decoration:none;}</style>");
      if(isTestMode) {
        body.append("Distribution List: " + toList + "<br><br>");        
      }

      body.append("<table width='820' cellpadding='10' cellspacing='0' bgcolor='#FFFFFF'><tr><td width='20'>&nbsp;</td><td width='800' valign='top' align='left'>");
      body.append("<table cellpadding='5' cellspacing='0' border='1' bgcolor='#F5FAFE'>");
      body.append(tableRows.toString());
      body.append("</table></td></tr></table></body></html>");
      if(isTestMode) {
        MailUtil.send_bcc(mailProps, bccTo, "", "", replyEmail, subject, body.toString(), true);                
      } else {
        MailUtil.send_bcc(mailProps, toList, "", bccTo, replyEmail, subject, body.toString(), true);               
      }
      app.disconnect();      
         
    } catch (Exception e) {
      System.out.println( e.toString() );
      e.printStackTrace();
    }
  }
  
  
  private void getUsageByLab(Session sess) throws Exception{
    Connection myConn = null;

    try 
    {
      myConn = sess.connection();
      
      StringBuffer hqlbuf = new StringBuffer("SELECT l from Lab l ");
      hqlbuf.append(" where l.isActive <> 'N' and l.excludeUsage <> 'Y'");
      //hqlbuf.append(" where l.isActive <> 'N'");
      
      waList = new ArrayList<String>();
  
      List results = sess.createQuery(hqlbuf.toString()).list();
      for (Iterator i = results.iterator(); i.hasNext();) {
        Lab thisLab = (Lab)i.next();
        LabStats ls = new LabStats(thisLab.getName());
        labInfo.put(thisLab.getIdLab(), ls);
      } 
      
      getActivityExperimentDetail();
      getActivityAnalysisDetail();
      getActivityTransferDetail("upload");
      getActivityTransferDetail("download");
      getDaysSinceLastUpload();
      
      ValueComparator bvc =  new ValueComparator(labInfo);
      sorted_map = new TreeMap(bvc);
      
      sorted_map.putAll(labInfo);
    }

    catch (Exception ex) {
      throw new RollBackCommandException();
    }
    finally {
      if(myConn != null) {
        try {
          myConn.close();        
        } catch (SQLException e) {
          System.out.println( e.toString() );
          e.printStackTrace();
        }
      }
    }         
  } 
  
  private void getDaysSinceLastUpload() {
    List summaryRows = sess.createQuery("SELECT tl.idLab, max(tl.startDateTime) from TransferLog tl where tl.transferType = 'upload' group by tl.idLab  order by max(tl.startDateTime) desc").list();

    Calendar today = Calendar.getInstance();
    
    //System.out.println("Days since last upload:");
    
    for(Iterator i = summaryRows.iterator(); i.hasNext();) {
      Object[] rows = (Object[])i.next();
      Integer idLab = (Integer)rows[0];
      java.util.Date lastUploadDate = (java.util.Date)rows[1];
      
      if (idLab == null || lastUploadDate == null) {
        continue;
      }

      Calendar lastUploadCalendar = new GregorianCalendar();
      lastUploadCalendar.setTime(lastUploadDate);
      
      int daysSinceLastUpload = daysBetween(lastUploadCalendar, today);
      LabStats ls = labInfo.get(idLab);
      if(ls != null) {
        ls.setDaysSinceLastUpload(daysSinceLastUpload);
      }
    }
  }
  
  private int daysBetween(Calendar startDate, Calendar endDate) {  
    Calendar date = (Calendar) startDate.clone();  
    int daysBetween = -1;  
    while (date.before(endDate)) {  
      date.add(Calendar.DAY_OF_MONTH, 1);  
      daysBetween++;  
    }  
    return daysBetween;  
  }    
  
  private void getActivityTransferDetail(String transferType) {

    StringBuffer queryBuf = new StringBuffer();
    queryBuf.append("SELECT lab.idLab, lab.lastName, lab.firstName, tl.startDateTime, r.number, count(*) ");
    queryBuf.append("from TransferLog tl, Request r, Lab lab ");
    queryBuf.append("where tl.idRequest = r.idRequest ");
    queryBuf.append("and r.idLab = lab.idLab ");
    queryBuf.append("and tl.startDateTime > '" + fFormat.formatDate(startDate.getTime(), FieldFormatter.DATE_OUTPUT_SQL) + "' ");
    queryBuf.append("and tl.startDateTime < '" + fFormat.formatDate(endDate.getTime(), FieldFormatter.DATE_OUTPUT_SQL) + "' ");
    queryBuf.append("and tl.transferType = '" + transferType + "' ");
    queryBuf.append("group by lab.idLab, lab.lastName, lab.firstName, tl.startDateTime, r.number ");
    queryBuf.append("order by lab.idLab, lab.lastName, lab.firstName, tl.startDateTime, r.number");
    
    List rows = sess.createQuery(queryBuf.toString()).list();
    for(Iterator i = rows.iterator(); i.hasNext();) {
      Object[] row = (Object[])i.next();
      Integer idLab = (Integer)row[0];
      Integer count = (Integer)row[5];
      LabStats ls = labInfo.get(idLab);
      if(ls != null) {
        if(transferType.compareTo("upload") == 0) {
          int uploadCount = ls.getUploadCount() + count.intValue();
          ls.setUploadCount(uploadCount);        
        } else {
          int downloadCount = ls.getDownloadCount() + count.intValue();
          ls.setUploadCount(downloadCount);                
        }        
      }
    }
    
    queryBuf = new StringBuffer();
    queryBuf.append("SELECT lab.idLab, lab.lastName, lab.firstName, tl.startDateTime, a.number, count(*) ");
    queryBuf.append("from TransferLog tl, Analysis a, Lab lab ");
    queryBuf.append("where tl.idAnalysis = a.idAnalysis ");
    queryBuf.append("and a.idLab = lab.idLab ");
    queryBuf.append("and tl.startDateTime > '" + fFormat.formatDate(startDate.getTime(), FieldFormatter.DATE_OUTPUT_SQL) + "' ");
    queryBuf.append("and tl.startDateTime < '" + fFormat.formatDate(endDate.getTime(), FieldFormatter.DATE_OUTPUT_SQL) + "' ");
    queryBuf.append("and tl.transferType = '" + transferType + "' ");
    queryBuf.append("group by lab.idLab, lab.lastName, lab.firstName, tl.startDateTime, a.number ");
    queryBuf.append("order by lab.idLab, lab.lastName, lab.firstName, tl.startDateTime, a.number");

    rows = sess.createQuery(queryBuf.toString()).list();
    for(Iterator i = rows.iterator(); i.hasNext();) {
      Object[] row = (Object[])i.next();
      Integer idLab = (Integer)row[0];

      Integer count = (Integer)row[5];
      LabStats ls = labInfo.get(idLab);
      if(ls != null) {
        if(transferType.compareTo("upload") == 0) {
          int uploadCount = ls.getUploadCount() + count.intValue();
          ls.setUploadCount(uploadCount);        
        } else {
          int downloadCount = ls.getDownloadCount() + count.intValue();
          ls.setUploadCount(downloadCount);                
        }        
      }
    }    
  }  
  

  private void getActivityExperimentDetail() {
    StringBuffer queryBuf = new StringBuffer();
    queryBuf.append("SELECT lab.idLab, lab.lastName, lab.firstName, r.createDate, r.number, r.name, r.description, requestCategory.requestCategory from Request r ");
    queryBuf.append("join r.lab as lab ");
    queryBuf.append("join r.requestCategory as requestCategory ");
    queryBuf.append("where r.createDate > '" + fFormat.formatDate(startDate.getTime(), FieldFormatter.DATE_OUTPUT_SQL) + "' ");
    queryBuf.append("and r.createDate < '" + fFormat.formatDate(endDate.getTime(), FieldFormatter.DATE_OUTPUT_SQL) + "' ");
    queryBuf.append("order by lab.idLab, lab.lastName, lab.firstName, r.createDate, r.number");
    
    List rows = sess.createQuery(queryBuf.toString()).list();

    for(Iterator i = rows.iterator(); i.hasNext();) {
      Object[] row = (Object[])i.next();
      Integer idLab = (Integer)row[0];
      String expName = (String)row[5];
      if(expName == null || expName.length() == 0) {
        expName = " ";
      }
      expName = "<b>Name:</b> " + expName;
      String expDescription = stripAndTruncate((String)row[6], 250);
      if(expDescription == null || expDescription.length() == 0) {
        expDescription = " ";        
      }
      expDescription = "<b>Description:</b> " + expDescription;
      LabStats ls = labInfo.get(idLab);
      if(ls != null) {
        int expCount = ls.getExperimentCount() + 1;
        ls.setExperimentCount(expCount); 
        String requestNumber = (String)row[4];
        String requestCategory = row[7] == null ? "" : ((String)row[7]).toString();    
        ls.addToExpAnalysisList(requestNumber + " " + requestCategory, expName + "&nbsp;&nbsp;" + expDescription);
      }
    }
  }  
  
  private void getActivityAnalysisDetail() {

    StringBuffer queryBuf = new StringBuffer();
    queryBuf.append("SELECT lab.idLab, lab.lastName, lab.firstName, a.createDate, a.number, a.name, a.description from Analysis a ");
    queryBuf.append("join a.lab as lab ");
    queryBuf.append("where a.createDate > '" + fFormat.formatDate(startDate.getTime(), FieldFormatter.DATE_OUTPUT_SQL) + "' ");
    queryBuf.append("and a.createDate < '" + fFormat.formatDate(endDate.getTime(), FieldFormatter.DATE_OUTPUT_SQL) + "' ");
    queryBuf.append("order by lab.idLab, lab.lastName, lab.firstName, a.createDate, a.number");
    
    List rows = sess.createQuery(queryBuf.toString()).list();
    for(Iterator i = rows.iterator(); i.hasNext();) {
      Object[] row = (Object[])i.next();
      Integer idLab = (Integer)row[0];
      String analysisName = (String)row[5];
      if(analysisName == null || analysisName.length() == 0) {
        analysisName = " ";
      }
      analysisName = "<b>Name:</b> " + analysisName;     
      String analysisDescription = stripAndTruncate((String)row[6], 250);
      if(analysisDescription == null || analysisDescription.length() == 0) {
        analysisDescription = " ";        
      }
      analysisDescription = "<b>Description:</b> " + analysisDescription;
      LabStats ls = labInfo.get(idLab);
      if(ls != null) {
        int analysisCount = ls.getAnalysisCount() + 1;
        ls.setAnalysisCount(analysisCount);        
        String analysisNumber = (String)row[4];
        ls.addToExpAnalysisList(analysisNumber, analysisName + "&nbsp;&nbsp;" + analysisDescription);
      }
    }
  } 
  
  private static String stripAndTruncate(String value, int length) {
    if(value != null) {
      value = value.replaceAll("(\\r|\\n)", "");
      if (value.length() > length) {
        value = value.substring(0, length) + "...";
      }
    }

    return value;
  }  
  
  private static Date getWakeupTime(){
    Calendar tomorrow = new GregorianCalendar();
    tomorrow.add(Calendar.DATE, fONE_DAY);
    Calendar result = new GregorianCalendar(
      tomorrow.get(Calendar.YEAR),
      tomorrow.get(Calendar.MONTH),
      tomorrow.get(Calendar.DATE),
      wakeupHour,
      fZERO_MINUTES
    );
    return result.getTime();
  } 
  
  
  private void connect()
  throws Exception
  {
    sess = dataSource.connect();
  }
  
  private void disconnect() 
  throws Exception {
    dataSource.close();
  }   
  

  // Bypassed dtd validation when reading data sources.
  public class DummyEntityRes implements EntityResolver
  {
      public InputSource resolveEntity(String publicId, String systemId)
              throws SAXException, IOException
      {
          return new InputSource(new StringReader(" "));
      }

  } 
  
  public void postMail( String recipients, String subject, String message , String from) throws MessagingException
  {
    //Set the host smtp address
    Properties props = new Properties();
    props.put("mail.smtp.host", "hci-mail.hci.utah.edu");

    // create some properties and get the default Session

    javax.mail.Session session = javax.mail.Session.getDefaultInstance(props, null);

    // create a message
    Message msg = new MimeMessage(session);

    // set the from and to address
    InternetAddress addressFrom = new InternetAddress(from);
    msg.setFrom(addressFrom);

    msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse( recipients, false ));


    // Optional : You can also set your custom headers in the Email if you Want
    // msg.addHeader("MyHeaderName", "myHeaderValue");

    // Setting the Subject and Content Type
    msg.setSubject(subject);
    msg.setContent(message, "text/plain");
    Transport.send(msg);
  } 
  
  private class LabStats {
    private String labName;
    private int daysSinceLastUpload;
    private int experimentCount;
    private int analysisCount;
    private int uploadCount;
    private int downloadCount;
    private TreeMap expAnalysisList;
    
    public TreeMap getExpAnalysisList() {
      return expAnalysisList;
    }

    public void setExpAnalysisList(TreeMap expAnalysisList) {
      this.expAnalysisList = expAnalysisList;
    }

    public LabStats(String initLabName) {
      labName = initLabName;
      daysSinceLastUpload = -1;
      experimentCount = 0;
      analysisCount = 0;
      uploadCount = 0;
      downloadCount = 0;
      expAnalysisList = new TreeMap(new ExpAnalysisComparator());
    }
    
    public void addToExpAnalysisList(String expAnalysisLabel, String nameAndDescription) {
      expAnalysisList.put(expAnalysisLabel, nameAndDescription);
    }

    public String getLabName() {
      return labName;
    }

    public int getDaysSinceLastUpload() {
      return daysSinceLastUpload;
    }

    public void setDaysSinceLastUpload(int daysSinceLastUpload) {
      this.daysSinceLastUpload = daysSinceLastUpload;
    }

    public int getExperimentCount() {
      return experimentCount;
    }

    public String getExperimentCountOutput() {
      if(experimentCount > 0) {
        return "" + experimentCount;
      }
      return "-";
    }

    public void setExperimentCount(int experimentCount) {
      this.experimentCount = experimentCount;
    }

    public int getAnalysisCount() {
      return analysisCount;
    }

    public String getAnalysisCountOutput() {
      if(analysisCount > 0) {
        return "" + analysisCount;
      }
      return "-";
    }

    public void setAnalysisCount(int analysisCount) {
      this.analysisCount = analysisCount;
    }

    public int getUploadCount() {
      return uploadCount;
    }
    
    public String getUploadCountOutput() {
      if(uploadCount > 0) {
        return "" + uploadCount;
      }
      return "-";
    }


    public void setUploadCount(int uploadCount) {
      this.uploadCount = uploadCount;
    }

    public int getDownloadCount() {
      return downloadCount;
    }
    
    public String getDownloadCountOutput() {
      if(downloadCount > 0) {
        return "" + downloadCount;
      }
      return "-";
    }
    public void setDownloadCount(int downloadCount) {
      this.downloadCount = downloadCount;
    }

  }  
  
  private class ValueComparator implements Comparator<Integer>{
    Map<Integer, LabStats> base;
    public ValueComparator(Map<Integer, LabStats> base) {
        this.base = base;
    }

    public int compare(Integer a, Integer b) {
      LabStats aValue = (LabStats) base.get(a);
      LabStats bValue = (LabStats) base.get(b);
      return(aValue.getLabName().compareTo(bValue.getLabName()));
    }
  }
  
  public static class ExpAnalysisComparator implements Comparator<String>, Serializable {
    private static final long serialVersionUID = 1L;

    public int compare(String o1, String o2) {
      String key1 = (String)o1;
      String key2 = (String)o2;
      return key1.compareTo(key2);
    }
  }  
}
