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
import java.util.Collections;
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
  
  private int 						weeklyTotalGuestDownloads	=0;
  private int						cumTotalGuestDownloads		=0;
  private int						weeklyTotalGuests			=0;
  private int						cumTotalGuests				=0;
  private int						weeklyTotalVisits			=0;
  private int						cumTotalVisits				=0;

  
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
      String toList = propertyHelper.getQualifiedProperty(PropertyDictionary.USAGE_REPORT_EMAILS, serverName);
      if(toList == null) {} // property not set, use system.out for output
      else if(toList.equals("")) {toList=null;} // use system.out for output
      else if(toList.toLowerCase().equals("all")) {
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
      
      toList = distributionList.toString();
      }
      // if not "all" nor "" then toList must be comma separated list of valid emails
      
      String replyEmail = propertyHelper.getQualifiedProperty(PropertyDictionary.GENERIC_NO_REPLY_EMAIL, serverName);
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
      
      // Populate the LabStats in labInfo HashMap
      getUsageByLab(sess);

      StringBuffer tableRows = new StringBuffer("");
      // Table Title      
      //body.append(subject + " for " + todaysDate + "<br>");     
      tableRows.append("<tr><td width='200' colspan='10' align='center'><span class='fontClassBold'>" + subject + " for " + todaysDate + "</span></td></tr>");
      
      
      // Table Header
      tableRows.append("<tr><td width='200'><span class='fontClassBold'>Lab<br>&nbsp;</span></td>");
      tableRows.append("<td width='200' align='center' colspan='2'><span class='fontClassBold'>Number of<br>Experiments</span></td>");
      tableRows.append("<td width='200' align='center' colspan='2'><span class='fontClassBold'>Number of<br>Analyses</span></td>");
      tableRows.append("<td width='200' align='center' colspan='2'><span class='fontClassBold'>Number of<br>File Uploads</span></td>");
      tableRows.append("<td width='200' align='center' colspan='2'><span class='fontClassBold'>Number of<br>File Downloads</span></td>");
      tableRows.append("<td width='100' align='center'><span class='fontClassBold'>Days Since<br>Last Upload</span></td></tr>");

      // Weekly/Cumulative Sub Header
      tableRows.append("<tr><td width='200'><span class='fontClassBold'>&nbsp;</span></td>");
      tableRows.append("<td width='100' align='center'><span class='fontClassBold'>Weekly</span></td>");
      tableRows.append("<td width='100' align='center'><span class='fontClassBold'>Cumulative</span></td>");
      tableRows.append("<td width='100' align='center'><span class='fontClassBold'>Weekly</span></td>");
      tableRows.append("<td width='100' align='center'><span class='fontClassBold'>Cumulative</span></td>");
      tableRows.append("<td width='100' align='center'><span class='fontClassBold'>Weekly</span></td>");
      tableRows.append("<td width='100' align='center'><span class='fontClassBold'>Cumulative</span></td>");
      tableRows.append("<td width='100' align='center'><span class='fontClassBold'>Weekly</span></td>");
      tableRows.append("<td width='100' align='center'><span class='fontClassBold'>Cumulative</span></td>");
      tableRows.append("<td width='100' align='center'><span class='fontClassBold'>&nbsp;</span></td></tr>");

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
        tableRows.append("<td width='100' align='right'><span class='fontClass'>" + value.getExperimentCountOutput() + "</span></td>");
        tableRows.append("<td width='100' align='right'><span class='fontClass'>" + value.getCumulativeExperimentCountOutput() + "</span></td>");
        tableRows.append("<td width='100' align='right'><span class='fontClass'>" + value.getAnalysisCountOutput() + "</span></td>");
        tableRows.append("<td width='100' align='right'><span class='fontClass'>" + value.getCumulativeAnalysisCountOutput() + "</span></td>");
        tableRows.append("<td width='100' align='right'><span class='fontClass'>" + value.getUploadCountOutput() + "</span></td>");
        tableRows.append("<td width='100' align='right'><span class='fontClass'>" + value.getCumulativeUploadCountOutput() + "</span></td>");
        tableRows.append("<td width='100' align='right'><span class='fontClass'>" + value.getDownloadCountOutput() + "</span></td>");
        tableRows.append("<td width='100' align='right'><span class='fontClass'>" + value.getCumulativeDownloadCountOutput() + "</span></td>");
        tableRows.append("<td width='100' align='right'><span class='fontClass'>" + daysSinceLastUpload + "</span></td></tr>");
        if(labRowSpan > 1) {
          tableRows.append("<tr><td colspan='9' width='600' align='left'><span class='fontClass'>Experiment/Analysis Links: <br>");
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
      
      // Guest usage stats for each lab   JFK 	JFK	JFK	JFK
      StringBuffer usageRows = new StringBuffer("");
      

      
      String currentLab = "";
      boolean isFirst = false;

      for (Object key : sorted_map.keySet()) {
          LabStats value = (LabStats) sorted_map.get(key);
          HashMap<String, GuestUsageStats> guestUsageStatsMap = value.getGuestUsageStatsList();
          ArrayList<String> list = new ArrayList<String>(guestUsageStatsMap.keySet());
          Collections.sort(list);
          for(String ae: list) {
        	  GuestUsageStats stat = guestUsageStatsMap.get(ae);
        	  if(currentLab.equals(value.getLabName())) {
        		  isFirst = false;
        		  usageRows.append("<tr><td width='200' colspan='2'><span class='fontClass'>" + "&nbsp;" +  "</span></td>");
        	  } else { 
        		  isFirst = true;
        		  currentLab = value.getLabName();
        		  usageRows.append("<tr><td width='200' colspan='2'><span class='fontClass'>" + value.getLabName() +  "</span></td>");
        	  }        	  
        	String AEnumber = stat.getAEnumber().equals("null")?"":stat.getAEnumber();
        	String downloadCount = (stat.getDownloadCount()==0?"-":stat.getDownloadCount()).toString();
        	String cumDownloadCount = (stat.getCumDownloadCount()==0?"-":stat.getCumDownloadCount()).toString();
        	String userCount = (stat.getUserCount()==0?"-":stat.getUserCount()).toString();
        	String cumUserCount = (stat.getCumUserCount()==0?"=":stat.getCumUserCount()).toString();
        	
			
			usageRows.append("<td width='200' colspan='2' align='right'><span class='fontClass'>" + AEnumber + "</span></td>");
			usageRows.append("<td width='100' colspan='1' align='right'><span class='fontClass'>" + downloadCount + "</span></td>");
			usageRows.append("<td width='100' colspan='1' align='right'><span class='fontClass'>" + cumDownloadCount + "</span></td>");
			usageRows.append("<td width='100' colspan='1' align='right'><span class='fontClass'>" + userCount + "</span></td>");
			usageRows.append("<td width='100' colspan='1' align='right'><span class='fontClass'>" + cumUserCount + "</span></td></tr>");        	  
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
        body.append("Distribution List: " + (toList==null?"empty":toList) + "<br><br>");        
      }

      body.append("<table width='1120' cellpadding='10' cellspacing='0' bgcolor='#FFFFFF'>");
      body.append("<tr>");
      body.append("<td width='20'>&nbsp;</td>");
      body.append("<td width='800' valign='top' align='left'>");
      body.append("<table cellpadding='5' cellspacing='0' border='1' bgcolor='#F5FAFE'>");
      body.append(tableRows.toString());
      body.append("</table></td></tr></table>"); //</body></html>");
      
      if(propertyHelper.getQualifiedProperty(PropertyDictionary.USAGE_GUEST_STATS, serverName) != null &&
    		  propertyHelper.getQualifiedProperty(PropertyDictionary.USAGE_GUEST_STATS, serverName).equals("Y")){
	      // jfk
	      // Guest Usage Table
	      body.append("<table width='1120' cellpadding='10' cellspacing='0' bgcolor='#FFFFFF'>");
	      body.append("<tr>");
	      body.append("<td width='20'>&nbsp;</td>");
	      body.append("<td width='800' valign='top' align='left'>");
	      body.append("<table cellpadding='5' cellspacing='0' border='1' bgcolor='#F5FAFE'>");
	      body.append("<tr>");
	      body.append("<td width='1000' colspan='8' align='center'><span class='fontClassLgeBold'>External Visits and Downloads</span></td>");
	      body.append("</tr>");
	      
	      // 	Summary Data
	      //body.append("<tr><td width='200' colspan='4'><span class='fontClassBold'>" + "&nbsp;" + "</span></td>");
	      body.append("<tr><td width='1000' colspan='8'><span class='fontClassBold'>" + "&nbsp;" + "</span></td></tr>");
	      
	      body.append("<tr><td width='200' align='center' colspan='8'><span class='fontClassBold'>" + "Summary Data" + "</span></td></tr>");
	      
	      body.append("<tr>");
	      body.append("<td width='800' align='center' colspan='4'><span class='fontClassBold'>" + "Number of Visits (Users and Guests)" + "</span></td>");
	      body.append("<td width='200' align='center' colspan='2'><span class='fontClassBold'>" + "Total Downloads By Guests" + "</span></td>");
	      body.append("<td width='200' align='center' colspan='2'><span class='fontClassBold'>" + "Total Guests Who Downloaded" + "</span></td>");
	      body.append("</tr>");   
	      //body.append("<td width='200' colspan='2'><span class='fontClassBold'>" + "&nbsp;" + "</span></td></tr>");
	      
	      body.append("<tr>");//<td width='200' align='center' colspan='4'><span class='fontClassBold'>" + "&nbsp;" + "</span></td>");
	      body.append("<td width='400' align='center' colspan='2'><span class='fontClassBold'>" + "Weekly" + "</span></td>");
	      body.append("<td width='400' align='center' colspan='2'><span class='fontClassBold'>" + "Cumulative" + "</span></td>");
	      body.append("<td width='200' align='center' colspan='1'><span class='fontClassBold'>" + "Weekly" + "</span></td>");
	      body.append("<td width='200' align='center' colspan='1'><span class='fontClassBold'>" + "Cumulative" + "</span></td>");
	      body.append("<td width='200' align='center' colspan='1'><span class='fontClassBold'>" + "Weekly" + "</span></td>");
	      body.append("<td width='200' align='center' colspan='1'><span class='fontClassBold'>" + "Cumulative" + "</span></td>");
	      body.append("</tr>");      
	      //body.append("<td width='200' colspan='2'><span class='fontClassBold'>" + "&nbsp;" + "</span></td></tr>");
	      
	      body.append("<tr>"); //<td width='200' colspan='4'><span class='fontClassBold'>" + "&nbsp;" + "</span></td>");
	      body.append("<td width='200' align='right' colspan='2'><span class='fontClass'>" + weeklyTotalVisits + "</span></td>");
	      body.append("<td width='200' align='right' colspan='2'><span class='fontClass'>" + cumTotalVisits + "</span></td>");
	      body.append("<td width='200' align='right' colspan='1'><span class='fontClass'>" + weeklyTotalGuestDownloads + "</span></td>");
	      body.append("<td width='200' align='right' colspan='1'><span class='fontClass'>" + cumTotalGuestDownloads + "</span></td>");   
	      body.append("<td width='200' align='right' colspan='1'><span class='fontClass'>" + weeklyTotalGuests + "</span></td>");
	      body.append("<td width='200' align='right' colspan='1'><span class='fontClass'>" + cumTotalGuests + "</span></td>");
	      body.append("</tr>"); 
	      
	      body.append("<tr><td width='1000' colspan='8'><span class='fontClassBold'>" + "&nbsp;" + "</span></td></tr>");
	      
	      //body.append("<tr><td width='200' colspan='8'><span class='fontClassBold'>" + "&nbsp;" + "</span></td></tr>");
	      
	      // 		Lab Detail Data
	      body.append("<tr>");
	      body.append("<td width='400' align='center' colspan='2' rowspan='2'><span class='fontClassBold'>" + "Lab" 									+ "</span></td>");
	      body.append("<td width='400' align='center' colspan='2' rowspan='2'><span class='fontClassBold'>" + "Analysis/ Experiment ID" 				+ "</span></td>");
	      body.append("<td width='400' align='center' colspan='2'><span class='fontClassBold'>" 			+ "Number of Files Downloaded"				+ "</span></td>");
	      body.append("<td width='400' align='center' colspan='2'><span class='fontClassBold'>" 			+ "Number of Guest Users Who Downloaded" 	+ "</span></td>");
	      body.append("</tr>");
	      body.append("<tr>");
	      //body.append("<td width='200' colspan='4'><span class='fontClassBold'>&nbsp;</span></td>");
	      body.append("<td width='200' align='center' colspan='1'><span class='fontClassBold'>Weekly</span></td>");
	      body.append("<td width='200' align='center' colspan='1'><span class='fontClassBold'>Cumulative</span></td>");
	      body.append("<td width='200' align='center' colspan='1'><span class='fontClassBold'>Weekly</span></td>");
	      body.append("<td width='200' align='center' colspan='1'><span class='fontClassBold'>Cumulative</span></td>");
	      body.append("</tr>");
	      
	      body.append(usageRows.toString());
	      body.append("</table></td>&nbsp;</tr></table>");
      }
      body.append("</body></html>");
      
      if(isTestMode) {
        MailUtil.send_bcc(mailProps, bccTo, "", "", replyEmail, subject, body.toString(), true);                
      } else {
    	  if(toList.equals(null)) {
    		  System.out.print(body.toString()); }
    	  	else {
    	  		MailUtil.send_bcc(mailProps, toList, "", bccTo, replyEmail, subject, body.toString(), true); }               
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
      
//      waList = new ArrayList<String>(); doesn't appear to be used - jfk
        
      List results = sess.createQuery(hqlbuf.toString()).list();
      // Build a hashmap "labInfo" with lab IDs for keys and new LabStats objects for values.
      for (Iterator i = results.iterator(); i.hasNext();) {
        Lab thisLab = (Lab)i.next();
        LabStats ls = new LabStats(thisLab.getName());
        ls.setLabId(thisLab.getIdLab().intValue()); // This variable for needed only for debugging
        labInfo.put(thisLab.getIdLab(), ls);
      }      
      // Now fill in the fields of the LabStats objects in the hashmap
      
      // Add Requests to Lab's LabStats's expAnalysisList (Treemap<requestNumber+" "+requestCategroy, expName+" "+expDescription>)
      getActivityExperimentDetail();      //B      
      getCumulativeActivityExperimentDetail(); //C
      getActivityAnalysisDetail(); //D
      getCumulativeActivityAnalysisDetail(); //E
      getActivityTransferDetail("upload"); //F
      getCumulativeActivityTransferDetail("upload"); //G
      getActivityTransferDetail("download"); //H
      getCumulativeActivityTransferDetail("download"); //I
      getDaysSinceLastUpload(); //J
      getGuestUsageDetail();

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
  
  // Finds all Requests, pairs them with their lab and requestCategory.
  // Adds each request to its Lab's LabAnalysis's expAnalysisList
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
  
  private void getCumulativeActivityExperimentDetail() {
	    StringBuffer queryBuf = new StringBuffer();
	    queryBuf.append("SELECT lab.idLab, lab.lastName, lab.firstName, r.createDate, r.number, r.name, r.description, requestCategory.requestCategory from Request r ");
	    queryBuf.append("join r.lab as lab ");
	    queryBuf.append("join r.requestCategory as requestCategory ");
	    queryBuf.append("order by lab.idLab, lab.lastName, lab.firstName, r.createDate, r.number");
	    
	    List rows = sess.createQuery(queryBuf.toString()).list();

	    for(Iterator i = rows.iterator(); i.hasNext();) {
	      Object[] row = (Object[])i.next();
	      Integer idLab = (Integer)row[0];
	      LabStats ls = labInfo.get(idLab);
	      if(ls != null) {
	        int expCount = ls.getCumulativeExperimentCount() + 1;
	        ls.setCumulativeExperimentCount(expCount); 
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
  
  private void getCumulativeActivityAnalysisDetail() {
	    StringBuffer queryBuf = new StringBuffer();
	    queryBuf.append("SELECT lab.idLab, lab.lastName, lab.firstName, a.createDate, a.number, a.name, a.description from Analysis a ");
	    queryBuf.append("join a.lab as lab ");
	    queryBuf.append("order by lab.idLab, lab.lastName, lab.firstName, a.createDate, a.number");
	    
	    List rows = sess.createQuery(queryBuf.toString()).list();
	    for(Iterator i = rows.iterator(); i.hasNext();) {
	      Object[] row = (Object[])i.next();
	      Integer idLab = (Integer)row[0]; 
	      LabStats ls = labInfo.get(idLab);
	      if(ls != null) {
	        int analysisCount = ls.getCumulativeAnalysisCount() + 1;
	        ls.setCumulativeAnalysisCount(analysisCount);        
	      }
	    }
	  }
  
  private void getActivityTransferDetail(String transferType) {
	    
	    String distinctStr = "";
	    if(transferType.compareTo("upload")==0) {
	      distinctStr = "distinct";
	    }

	    StringBuffer queryBuf = new StringBuffer();
	    queryBuf.append("SELECT " + distinctStr + " lab.idLab, lab.lastName, lab.firstName, tl.fileName, r.number ");
	    queryBuf.append("from TransferLog tl, Request r, Lab lab ");
	    queryBuf.append("where tl.idRequest = r.idRequest ");
	    queryBuf.append("and r.idLab = lab.idLab ");
	    queryBuf.append("and tl.startDateTime > '" + fFormat.formatDate(startDate.getTime(), FieldFormatter.DATE_OUTPUT_SQL) + "' ");
	    queryBuf.append("and tl.startDateTime < '" + fFormat.formatDate(endDate.getTime(), FieldFormatter.DATE_OUTPUT_SQL) + "' ");
	    queryBuf.append("and tl.transferType = '" + transferType + "' ");
	    queryBuf.append("group by lab.idLab, lab.lastName, lab.firstName, tl.fileName, r.number ");
	    queryBuf.append("order by lab.idLab, lab.lastName, lab.firstName, tl.fileName, r.number");
	    
	    List rows = sess.createQuery(queryBuf.toString()).list();
	    for(Iterator i = rows.iterator(); i.hasNext();) {
	      Object[] row = (Object[])i.next();
	      Integer idLab = (Integer)row[0];
	      LabStats ls = labInfo.get(idLab);
	      if(ls != null) {
	        if(transferType.compareTo("upload") == 0) {
	          int uploadCount = ls.getUploadCount() + 1;
	          ls.setUploadCount(uploadCount); 
	        } else {
	          int downloadCount = ls.getDownloadCount() + 1;
	          ls.setDownloadCount(downloadCount);                
	        }        
	      }
	    }
	    
	    queryBuf = new StringBuffer();
	    queryBuf.append("SELECT " + distinctStr + " lab.idLab, lab.lastName, lab.firstName, tl.fileName, a.number ");
	    queryBuf.append("from TransferLog tl, Analysis a, Lab lab ");
	    queryBuf.append("where tl.idAnalysis = a.idAnalysis ");
	    queryBuf.append("and a.idLab = lab.idLab ");
	    queryBuf.append("and tl.startDateTime > '" + fFormat.formatDate(startDate.getTime(), FieldFormatter.DATE_OUTPUT_SQL) + "' ");
	    queryBuf.append("and tl.startDateTime < '" + fFormat.formatDate(endDate.getTime(), FieldFormatter.DATE_OUTPUT_SQL) + "' ");
	    queryBuf.append("and tl.transferType = '" + transferType + "' ");
	    queryBuf.append("group by lab.idLab, lab.lastName, lab.firstName, tl.fileName, a.number ");
	    queryBuf.append("order by lab.idLab, lab.lastName, lab.firstName, tl.fileName, a.number");

	    rows = sess.createQuery(queryBuf.toString()).list();
	    for(Iterator i = rows.iterator(); i.hasNext();) {
	      Object[] row = (Object[])i.next();
	      Integer idLab = (Integer)row[0];
	      LabStats ls = labInfo.get(idLab);
	      if(ls != null) {
	        if(transferType.compareTo("upload") == 0) {
	          int uploadCount = ls.getUploadCount() + 1;
	          ls.setUploadCount(uploadCount);        
	        } else {
	          int downloadCount = ls.getDownloadCount() + 1;
	          ls.setDownloadCount(downloadCount);                
	        }        
	      }
	    }    
	  } 
  
  private void getCumulativeActivityTransferDetail(String transferType) {
	    String distinctStr = "";
	    if(transferType.compareTo("upload")==0) {
	      distinctStr = "distinct";
	    }
	    
	    StringBuffer queryBuf = new StringBuffer();
	    queryBuf.append("SELECT " + distinctStr + " lab.idLab, lab.lastName, lab.firstName, tl.fileName, r.number ");
	    queryBuf.append("from TransferLog tl, Request r, Lab lab ");
	    queryBuf.append("where tl.idRequest = r.idRequest ");
	    queryBuf.append("and r.idLab = lab.idLab ");
	    queryBuf.append("and tl.transferType = '" + transferType + "' ");
	    queryBuf.append("group by lab.idLab, lab.lastName, lab.firstName, tl.fileName, r.number ");
	    queryBuf.append("order by lab.idLab, lab.lastName, lab.firstName, tl.fileName, r.number");
	    
	    List rows = sess.createQuery(queryBuf.toString()).list();
	    for(Iterator i = rows.iterator(); i.hasNext();) {
	      Object[] row = (Object[])i.next();
	      Integer idLab = (Integer)row[0];
	      LabStats ls = labInfo.get(idLab);
	      if(ls != null) {
	        if(transferType.compareTo("upload") == 0) {
	          int uploadCount = ls.getCumulativeUploadCount() + 1;
	          ls.setCumulativeUploadCount(uploadCount);        
	        } else {
	          int downloadCount = ls.getCumulativeDownloadCount() + 1;
	          ls.setCumulativeDownloadCount(downloadCount);                
	        }        
	      }
	    }
	    
	    queryBuf = new StringBuffer();
	    queryBuf.append("SELECT " + distinctStr + " lab.idLab, lab.lastName, lab.firstName, tl.fileName, a.number ");
	    queryBuf.append("from TransferLog tl, Analysis a, Lab lab ");
	    queryBuf.append("where tl.idAnalysis = a.idAnalysis ");
	    queryBuf.append("and a.idLab = lab.idLab ");
	    queryBuf.append("and tl.transferType = '" + transferType + "' ");
	    queryBuf.append("group by lab.idLab, lab.lastName, lab.firstName, tl.fileName, a.number ");
	    queryBuf.append("order by lab.idLab, lab.lastName, lab.firstName, tl.fileName, a.number");

	    rows = sess.createQuery(queryBuf.toString()).list();
	    for(Iterator i = rows.iterator(); i.hasNext();) {
	      Object[] row = (Object[])i.next();
	      Integer idLab = (Integer)row[0];
	      LabStats ls = labInfo.get(idLab);
	      if(ls != null) {
	        if(transferType.compareTo("upload") == 0) {
	          int uploadCount = ls.getCumulativeUploadCount() + 1;
	          ls.setCumulativeUploadCount(uploadCount);        
	        } else {
	          int downloadCount = ls.getCumulativeDownloadCount() + 1;
	          ls.setCumulativeDownloadCount(downloadCount);                
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
  
  private void getGuestUsageDetail() {
	  
		// -- Get the WEEKLY COUNT for the number of UNIQUE USERS who downloaded
		// EXPERIMENT files for each experiment grouped by lab
		// -- Get the WEEKLY COUNT for the number of EXPERIMENT file downloads
		// for each experiment grouped by lab
		StringBuffer queryBuf = new StringBuffer();
		queryBuf.append(" SELECT tl.idLab, r.idRequest, r.number, r.name, COUNT(DISTINCT tl.emailAddress ), COUNT(tl.fileName) ");
		queryBuf.append(" FROM TransferLog AS tl, Request AS r, Lab as l ");
		queryBuf.append(" WHERE tl.idRequest = r.idRequest ");
		queryBuf.append(" AND tl.idLab = l.idLab ");
		queryBuf.append(" AND l.excludeUsage = 'N' AND l.isActive = 'Y' ");
		queryBuf.append(" AND tl.idRequest IS NOT NULL AND tl.idAppUser = -999999 AND tl.transferType = 'download' ");
		queryBuf.append(" AND startDateTime > '" + fFormat.formatDate(startDate.getTime(), FieldFormatter.DATE_OUTPUT_SQL) + "' ");
		queryBuf.append(" AND startDateTime < '" + fFormat.formatDate(endDate.getTime(), FieldFormatter.DATE_OUTPUT_SQL) + "' ");
		queryBuf.append("	GROUP BY tl.idLab, r.idRequest, r.number, r.name ORDER BY tl.idLab, r.idRequest ");

		List rows = sess.createQuery(queryBuf.toString()).list();
		for (Iterator i = rows.iterator(); i.hasNext();) {
			Object[] row = (Object[]) i.next();
			Integer idLab = (Integer) row[0];
			LabStats ls = labInfo.get(idLab);
			if (ls != null) {
				String number = (String) row[2];
				Integer userCount = ((Integer) row[4]).intValue();
				Integer downloadCount = ((Integer) row[5]).intValue();
				HashMap<String, GuestUsageStats> statsMap = ls
						.getGuestUsageStatsList();
				GuestUsageStats stat = new GuestUsageStats();
				stat.setAEnumber(number);
				stat.setUserCount(userCount);
				stat.setDownloadCount(downloadCount);
				statsMap.put(number, stat);
			}
		}

		// -- Get the WEEKLY COUNT for the number of UNIQUE USERS who downloaded
		// ANALYSIS files for each experiment grouped by lab
		// -- Get the WEEKLY COUNT for the number of ANALYSIS file downloads for
		// each experiment grouped by lab
		queryBuf = new StringBuffer();
		queryBuf.append(" SELECT tl.idLab, a.idAnalysis, a.number, a.name, COUNT(DISTINCT tl.emailAddress ), COUNT(tl.fileName) ");
		queryBuf.append(" FROM TransferLog AS tl, Analysis AS a, Lab AS l ");
		queryBuf.append(" WHERE tl.idAnalysis = a.idAnalysis ");
		queryBuf.append(" AND tl.idLab = l.idLab ");
		queryBuf.append(" AND l.excludeUsage = 'N' AND l.isActive = 'Y' ");
		queryBuf.append(" AND tl.idAnalysis IS NOT NULL AND tl.idAppUser = -999999  AND tl.transferType = 'download' ");
		queryBuf.append(" AND startDateTime > '" + fFormat.formatDate(startDate.getTime(), FieldFormatter.DATE_OUTPUT_SQL) + "' ");
		queryBuf.append(" AND startDateTime < '" + fFormat.formatDate(endDate.getTime(), FieldFormatter.DATE_OUTPUT_SQL) + "' ");
		queryBuf.append(" GROUP BY tl.idLab, a.idAnalysis, a.number, a.name ORDER BY tl.idLab, a.idAnalysis ");

		rows = sess.createQuery(queryBuf.toString()).list();
		for (Iterator i = rows.iterator(); i.hasNext();) {
			Object[] row = (Object[]) i.next();
			Integer idLab = (Integer) row[0];
			LabStats ls = labInfo.get(idLab);
			if (ls != null) {
				String number = (String) row[2];
				Integer userCount = ((Integer) row[4]).intValue();
				Integer downloadCount = ((Integer) row[5]).intValue();
				HashMap<String, GuestUsageStats> statsMap = ls
						.getGuestUsageStatsList();
				GuestUsageStats stat = new GuestUsageStats();
				stat.setAEnumber(number);
				stat.setUserCount(userCount);
				stat.setDownloadCount(downloadCount);
				statsMap.put(number, stat);
			}
		}
		// -- Get the CUMULATIVE COUNT for the number of UNIQUE USERS who
		// downloaded EXPERIMENT files for each experiment grouped by lab
		// -- Get the CUMULATIVE COUNT for the number of EXPERIMENT file
		// downloads for each experiment grouped by lab
		queryBuf = new StringBuffer();
		queryBuf.append(" SELECT tl.idLab, r.idRequest, r.number, r.name, COUNT(DISTINCT tl.emailAddress ), COUNT(tl.fileName) ");
		queryBuf.append(" FROM TransferLog AS tl, Request AS r, Lab as l ");
		queryBuf.append(" WHERE tl.idRequest = r.idRequest ");
		queryBuf.append(" AND tl.idLab = l.idLab ");
		queryBuf.append(" AND l.excludeUsage = 'N' AND l.isActive = 'Y' ");
		queryBuf.append(" AND tl.idRequest IS NOT NULL AND tl.idAppUser = -999999 AND tl.transferType = 'download' ");
		queryBuf.append("	GROUP BY tl.idLab, r.idRequest, r.number, r.name ORDER BY tl.idLab, r.idRequest ");

		rows = sess.createQuery(queryBuf.toString()).list();
		for (Iterator i = rows.iterator(); i.hasNext();) {
			Object[] row = (Object[]) i.next();
			Integer idLab = (Integer) row[0];
			LabStats ls = labInfo.get(idLab);
			if (ls != null) {
				String number = (String) row[2];
				Integer cumUserCount = ((Integer) row[4]).intValue();
				Integer cumDownloadCount = ((Integer) row[5]).intValue();
				cumTotalGuestDownloads += cumUserCount;
				cumTotalGuestDownloads += cumDownloadCount;
				HashMap<String, GuestUsageStats> statsMap = ls
						.getGuestUsageStatsList();
				GuestUsageStats stat = statsMap.get(number);
				if (stat == null) {
					stat = new GuestUsageStats();
					stat.setAEnumber(number);
					stat.setCumUserCount(cumUserCount);
					stat.setCumDownloadCount(cumDownloadCount);
					statsMap.put(number, stat);
				} else {
					stat.setCumUserCount(cumUserCount);
					stat.setCumDownloadCount(cumDownloadCount);
				}
			}
		}
		// -- Get the CUMULATIVE COUNT for the number of UNIQUE USERS who
		// downloaded ANALYSIS files for each experiment grouped by lab
		// -- Get the CUMULATIVE COUNT for the number of ANALYSIS file downloads
		// for each experiment grouped by lab
		queryBuf = new StringBuffer();
		queryBuf.append(" SELECT tl.idLab, a.idAnalysis, a.number, a.name, COUNT(DISTINCT tl.emailAddress ), COUNT(tl.fileName) ");
		queryBuf.append(" FROM TransferLog AS tl, Analysis AS a, Lab AS l ");
		queryBuf.append(" WHERE tl.idAnalysis = a.idAnalysis ");
		queryBuf.append(" AND tl.idLab = l.idLab ");
		queryBuf.append(" AND l.excludeUsage = 'N' AND l.isActive = 'Y' ");
		queryBuf.append(" AND tl.idAnalysis IS NOT NULL AND tl.idAppUser = -999999  AND tl.transferType = 'download' ");
		queryBuf.append(" GROUP BY tl.idLab, a.idAnalysis, a.number, a.name ORDER BY tl.idLab, a.idAnalysis ");

		rows = sess.createQuery(queryBuf.toString()).list();
		for (Iterator i = rows.iterator(); i.hasNext();) {
			Object[] row = (Object[]) i.next();
			Integer idLab = (Integer) row[0];
			LabStats ls = labInfo.get(idLab);
			if (ls != null) {
				String number = (String) row[2];
				Integer cumUserCount = ((Integer) row[4]).intValue();
				Integer cumDownloadCount = ((Integer) row[5]).intValue();
				HashMap<String, GuestUsageStats> statsMap = ls
						.getGuestUsageStatsList();
				GuestUsageStats stat = statsMap.get(number);
				if (stat == null) {
					stat = new GuestUsageStats();
					stat.setAEnumber(number);
					stat.setCumUserCount(cumUserCount);
					stat.setCumDownloadCount(cumDownloadCount);
					statsMap.put(number, stat);
				} else {
					stat.setCumUserCount(cumUserCount);
					stat.setCumDownloadCount(cumDownloadCount);
				}
			}
		}
		
		
		queryBuf = new StringBuffer();
		// -- Get TOTAL (all GNomEx guests) WEEKLY COUNT for UNIQUE GUESTS and for ANALYSIS or EXPERIMENT files downloaded by guests
		queryBuf.append(" SELECT COUNT(DISTINCT tl.emailAddress ), COUNT(tl.fileName)");
		queryBuf.append(" FROM TransferLog AS tl, Lab as l ");
		queryBuf.append(" WHERE tl.idLab = l.idLab AND tl.idAppUser = -999999 AND l.excludeUsage = 'N' ");
		queryBuf.append(" AND l.isActive = 'Y' AND tl.transferType = 'download' ");
		queryBuf.append(" AND startDateTime > '" + fFormat.formatDate(startDate.getTime(), FieldFormatter.DATE_OUTPUT_SQL) + "' ");
		queryBuf.append(" AND startDateTime < '"+ fFormat.formatDate(endDate.getTime(),	FieldFormatter.DATE_OUTPUT_SQL) + "' ");
		
		rows = sess.createQuery(queryBuf.toString()).list();
		for (Iterator i = rows.iterator(); i.hasNext();) {
			Object[] row = (Object[]) i.next();
			weeklyTotalGuests = (Integer)row[0];
			weeklyTotalGuestDownloads = (Integer)row[1];
			break;
		}
		
		queryBuf = new StringBuffer();
		// -- Get TOTAL (all GNomEx guests) CUMULATIVE COUNT for UNIQUE GUESTS and for ANALYSIS or EXPERIMENT files downloaded by guests
		queryBuf.append(" SELECT COUNT(DISTINCT tl.emailAddress ), COUNT(tl.fileName)");
		queryBuf.append(" FROM TransferLog AS tl, Lab as l ");
		queryBuf.append(" WHERE tl.idLab = l.idLab AND tl.idAppUser = -999999 AND l.excludeUsage = 'N' ");
		queryBuf.append(" AND l.isActive = 'Y' AND tl.transferType = 'download' ");
		
		rows = sess.createQuery(queryBuf.toString()).list();
		for (Iterator i = rows.iterator(); i.hasNext();) {
			Object[] row = (Object[]) i.next();
			cumTotalGuests = (Integer)row[0];
			cumTotalGuestDownloads = (Integer)row[1];
			break;
		}
		
		queryBuf = new StringBuffer();
		//-- Get TOTAL (all GNomEx visitors) WEEKLY COUNT for visits (every visit is counted, not just visits by unique users)
		queryBuf.append(" SELECT COUNT(idAppUser) FROM VisitLog AS vl ");
		queryBuf.append(" WHERE visitDateTime > '" + fFormat.formatDate(startDate.getTime(), FieldFormatter.DATE_OUTPUT_SQL) + "' ");
		queryBuf.append(" AND visitDateTime < '" + fFormat.formatDate(endDate.getTime(),
				FieldFormatter.DATE_OUTPUT_SQL) + "' ");
		rows = sess.createQuery(queryBuf.toString()).list();
		Iterator i = rows.iterator();
			weeklyTotalVisits = (Integer)i.next();
		
		queryBuf = new StringBuffer();
		//-- Get TOTAL (all GNomEx visitors) CUMULATIVE COUNT for visits (every visit is counted, not just visits by unique users)
		queryBuf.append(" SELECT COUNT(idAppUser) FROM VisitLog AS vl ");
		rows = sess.createQuery(queryBuf.toString()).list();
			i = rows.iterator();			
			cumTotalVisits = (Integer)i.next();

			    
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
  
  // used to store guest downloads stats for experiments and analyses per lab
  private class GuestUsageStats {
	  private String AEnumber;
	  private Integer userCount;
	  private Integer downloadCount;
	  private Integer cumUserCount;
	  private Integer cumDownloadCount;
	  
	  public GuestUsageStats() {
		  AEnumber = "";
		  userCount = new Integer(0);
		  downloadCount = new Integer(0);
		  cumUserCount = new Integer(0);
		  cumDownloadCount = new Integer(0);
	  }
	  
	  public String getAEnumber() {
		  return AEnumber;
	  }
	  public Integer getUserCount() {
		  return userCount;
	  }
	  public Integer getDownloadCount() {
		  return downloadCount;
	  }	  
	  public Integer getCumUserCount() {
		  return cumUserCount;
	  }	  
	  public Integer getCumDownloadCount() {
		  return cumDownloadCount;
	  }
	  
	  public void setAEnumber(String AEnumber) {
		  this.AEnumber = AEnumber;
	  }
	  public void setUserCount(Integer userCount) {
		  this.userCount = userCount;
	  }
	  public void setDownloadCount(Integer downloadCount) {
		  this.downloadCount = downloadCount;
	  }	  
	  public void setCumUserCount(Integer cumUserCount) {
		  this.cumUserCount = cumUserCount;
	  }	  
	  public void setCumDownloadCount(Integer cumDownloadCount) {
		  this.cumDownloadCount = cumDownloadCount;
	  }
  } 
 
  
  private class LabStats {
    private String labName;
    private int labId;
    private int daysSinceLastUpload;
    private int experimentCount;
    private int cumulativeExperimentCount;
    private int analysisCount;
    private int cumulativeAnalysisCount;
    private int uploadCount;
    private int downloadCount;
    private int cumulativeUploadCount;
    private int cumulativeDownloadCount;
    private TreeMap expAnalysisList;
    private HashMap<String, GuestUsageStats> guestUsageStatsList;
    
    public HashMap<String, GuestUsageStats> getGuestUsageStatsList () {
    	return guestUsageStatsList;
    }
    
    public int getLabId() {
      return labId;
    }

    public void setLabId(int labId) {
      this.labId = labId;
    }

    public int getCumulativeExperimentCount() {
      return cumulativeExperimentCount;
    }

    public void setCumulativeExperimentCount(int cumulativeExperimentCount) {
      this.cumulativeExperimentCount = cumulativeExperimentCount;
    }

    public int getCumulativeAnalysisCount() {
      return cumulativeAnalysisCount;
    }

    public void setCumulativeAnalysisCount(int cumulativeAnalysisCount) {
      this.cumulativeAnalysisCount = cumulativeAnalysisCount;
    }

    
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
      cumulativeExperimentCount = 0;
      cumulativeAnalysisCount = 0;
      uploadCount = 0;
      downloadCount = 0;
      cumulativeUploadCount = 0;
      cumulativeDownloadCount = 0;
      expAnalysisList = new TreeMap(new ExpAnalysisComparator());
      guestUsageStatsList = new HashMap<String, GuestUsageStats>();
    }
    
    public int getCumulativeUploadCount() {
      return cumulativeUploadCount;
    }

    public void setCumulativeUploadCount(int cumulativeUploadCount) {
      this.cumulativeUploadCount = cumulativeUploadCount;
    }

    public int getCumulativeDownloadCount() {
      return cumulativeDownloadCount;
    }

    public void setCumulativeDownloadCount(int cumulativeDownloadCount) {
      this.cumulativeDownloadCount = cumulativeDownloadCount;
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

    public String getCumulativeExperimentCountOutput() {
      if(cumulativeExperimentCount > 0) {
        return "" + cumulativeExperimentCount;
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

    public String getCumulativeAnalysisCountOutput() {
      if(cumulativeAnalysisCount > 0) {
        return "" + cumulativeAnalysisCount;
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


    public String getCumulativeUploadCountOutput() {
      if(cumulativeUploadCount > 0) {
        return "" + cumulativeUploadCount;
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
    
    public String getCumulativeDownloadCountOutput() {
      if(cumulativeDownloadCount > 0) {
        return "" + cumulativeDownloadCount;
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
