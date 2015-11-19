package hci.gnomex.daemon;

import hci.framework.control.RollBackCommandException;
import hci.framework.model.FieldFormatter;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.Lab;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.utility.BatchDataSource;
import hci.gnomex.utility.PropertyDictionaryHelper;
import hci.gnomex.utility.mail.BatchMailer;
import hci.gnomex.utility.mail.MailUtil;
import hci.gnomex.utility.mail.MailUtilHelper;

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
    startDate.add(Calendar.DAY_OF_YEAR, -7); //jfk change back to -7
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
      
      // Build message body in html
      StringBuffer body = new StringBuffer("");
      
      body.append("<html>");
      body.append("<head><title>GNomEx Usage Report</title><meta http-equiv='content-style-type' content='text/css'>");
      body.append("<style>");
      body.append(" .fontClass{font-size:11px;color:#000000;font-family:verdana;text-decoration:none;} ");
      body.append(" .fontClassBold{font-size:11px;font-weight:bold;color:#000000;font-family:verdana;text-decoration:none;} ");
      body.append(" .fontClassMedBold{font-size:12px;font-weight:bold;color:#000000;bgcolor:#c7e3f9;font-family:verdana;text-decoration:none;} ");
      body.append(" .fontClassLgeBold{font-size:13px;line-height:22px;font-weight:bold;color:#000000;font-family:verdana;text-decoration:none;} ");
      body.append(" .table700{width:700} ");
      body.append(" .tableHeadTop{background-color:#afd7f7;text-align:center} ");
      body.append(" .tableHeadMiddle{background-color:#c7e3f9;text-align:center} ");
      body.append(" .tableHeadBottom{background-color:#deeefc;text-align:center} ");
      body.append(" </style></head>");
      body.append("<body leftmargin='0' marginwidth='0' topmargin='0' marginheight='0' offset='0' bgcolor='#FFFFFF'>");
      if(isTestMode) {
        body.append("Distribution List: " + (toList==null?" ":toList) + "<br><br>");        
      }

      body.append("<table  cellpadding='10' cellspacing='0' bgcolor='#FFFFFF'>");
      
      // User Usage Table
      body.append("<tr>");
      body.append("<td width='20'>&nbsp;</td>"); //left padding
      body.append("<td width='800' valign='top' align='left'>");
      body.append(buildUserUsageTable(todaysDate).toString());
      body.append("</td>");
      body.append("<td width='20'>&nbsp;</td></tr>"); // right padding
      
      // New Analysis/Experiment Table
      body.append("<tr>");
      body.append("<td width='20'>&nbsp;</td>"); //left padding
      body.append("<td width='800' valign='top' align='left'>");
      body.append(buildNewAETable(todaysDate).toString());
      body.append("</td>");
      body.append("<td width='20'>&nbsp;</td></tr>"); // right padding
      
      // Guest Usage Table
      body.append("<tr>");
      body.append("<td width='20'>&nbsp;</td>"); //left padding
      body.append("<td width='800' valign='top' align='left'>");
      body.append(buildGuestUsageTable(todaysDate).toString());
      body.append("</td>");
      body.append("<td width='20'>&nbsp;</td></tr>"); // right padding
      
      body.append("</table>");

      body.append("</body></html>");
      
      if(isTestMode) {
    	  MailUtilHelper helper = new MailUtilHelper(mailProps, bccTo, null, null, replyEmail, subject, body.toString(), null, true, isTestMode, propertyHelper.getProperty(PropertyDictionary.CONTACT_EMAIL_SOFTWARE_TESTER));
    	  MailUtil.validateAndSendEmail(helper);
      } else {
    	  if(toList==null) {
    		  System.out.print(body.toString()); 
    	  } else {
    		  MailUtilHelper helper = new MailUtilHelper(mailProps, toList, null, bccTo, replyEmail, subject, body.toString(), null, true, isTestMode, propertyHelper.getProperty(PropertyDictionary.CONTACT_EMAIL_SOFTWARE_TESTER));
    		  MailUtil.validateAndSendEmail(helper);
    	  }               
      }
      app.disconnect();      
         
    } catch (Exception e) {
      System.out.println( e.toString() );
      e.printStackTrace();
    }
  }
  
  
  private StringBuffer buildUserUsageTable(String todaysDate) {	  
	  StringBuffer userUsageTable = new StringBuffer("");
      // Table Title      
	  userUsageTable.append("<table class='table700'  cellpadding='5' cellspacing='0' border='1' bgcolor='#F5FAFE'>");
      userUsageTable.append("<tr><td class='tableHeadTop'  colspan='6'><span class='fontClassLgeBold'>" + "GNomEx Weekly Usage Table for " + todaysDate + "</span></td></tr>");
            
      // Table Header
      userUsageTable.append("<tr>");
      userUsageTable.append("<td class='tableHeadMiddle' align='left' ><span class='fontClassBold'>Lab<br>&nbsp;</span></td>");
      userUsageTable.append("<td class='tableHeadMiddle' ><span class='fontClassMedBold'>Days Since<br>Last Upload</span></td>");
      userUsageTable.append("<td class='tableHeadMiddle' ><span class='fontClassMedBold'>Number of<br>Experiments<br>Created</span></td>");
      userUsageTable.append("<td class='tableHeadMiddle' ><span class='fontClassMedBold'>Number of<br>Analyses<br>Conducted</span></td>");
      userUsageTable.append("<td class='tableHeadMiddle' ><span class='fontClassMedBold'>Number of<br>Files<br>Uploaded</span></td>");
      userUsageTable.append("<td class='tableHeadMiddle' ><span class='fontClassMedBold'>Number of<br>Files<br>Downloaded</span></td>");
      userUsageTable.append("</tr>");
      
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
        userUsageTable.append("<tr>");        
        userUsageTable.append("<td ><span class='fontClass'>" + value.getLabName() + "</span></td>");
        userUsageTable.append("<td  align='right'><span class='fontClass'>" + daysSinceLastUpload + "</span></td>");
        userUsageTable.append("<td  align='right'><span class='fontClass'>" + value.getExperimentCountOutput() + "</span></td>");
        userUsageTable.append("<td  align='right'><span class='fontClass'>" + value.getAnalysisCountOutput() + "</span></td>");
        userUsageTable.append("<td  align='right'><span class='fontClass'>" + value.getUploadCountOutput() + "</span></td>");
        userUsageTable.append("<td  align='right'><span class='fontClass'>" + value.getDownloadCountOutput() + "</span></td>");
        userUsageTable.append("</tr>");
                
      }
      userUsageTable.append("</table>");
      
      return userUsageTable;
  }
  
  private StringBuffer buildNewAETable(String todaysDate) {
	  StringBuffer newAETable = new StringBuffer("");
      // Table Title      
	  newAETable.append("<table class='table700' cellpadding='5' cellspacing='0' border='1' bgcolor='#F5FAFE'>");
      newAETable.append("<tr><td class='tableHeadTop' colspan='3' ><span class='fontClassLgeBold'>" + "New Experiments/Analyses for " + todaysDate + "</span></td></tr>");
      
      // Table Header
      newAETable.append("<tr><td class='tableHeadMiddle' align='left' width='20%' ><span class='fontClassMedBold'>Lab</span></td>");
      newAETable.append("<td class='tableHeadMiddle' width='20%' ><span class='fontClassMedBold'>Link</span></td>");
      newAETable.append("<td class='tableHeadMiddle' ><span class='fontClassMedBold'>Name and Description</span></td></tr>");

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
        } else {
        	continue;
        }
        newAETable.append("<tr><td  rowspan='" + expAnalysisCount + "' valign='top'><span class='fontClass'>" + value.getLabName() + "</span></td>");
        if(labRowSpan > 1) {
        	Boolean isFirst = true;
          for(Object eaKey : expAnalysisList) {
        	  if(!isFirst){
        		  newAETable.append("<tr>");        	
        	  }
        		  newAETable.append("<td colspan='1'  align='left'><span class='fontClass'>");
        	  
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
            newAETable.append("<a href='" + href + "'>" + thisExpAnalysisNumber  + "</a> ");
            newAETable.append("</span></td>");
            newAETable.append("<td><span class='fontClass'>" + nameAndDescr + "</span></td>");
            	if(!isFirst){
            		newAETable.append("</tr>");            		
            	} else {
            		isFirst = false;
            	}
          }
                      
        }
      }
      newAETable.append("</table>");
      return newAETable;
  }
  
  private StringBuilder buildGuestUsageTable(String todaysDate) {
	  StringBuilder guestUsageTable = new StringBuilder();
      if(propertyHelper.getQualifiedProperty(PropertyDictionary.USAGE_GUEST_STATS, serverName) != null &&
    		  propertyHelper.getQualifiedProperty(PropertyDictionary.USAGE_GUEST_STATS, serverName).equals("Y")){
    	  StringBuffer usageRows = new StringBuffer("");
    	  String currentLab = "";
          boolean isFirst = false;

          for (Object key : sorted_map.keySet()) {
              LabStats value = (LabStats) sorted_map.get(key);
              HashMap<String, AEGuestUsageStats> guestUsageStatsMap = value.getGuestUsageStatsList();
              if(!guestUsageStatsMap.isEmpty()) {
            	  if(value.getGuestDownloadCountSum() == 0 && value.getGuestUserCountSum() == 0) {
            		  continue;
            	  }
            	  
            	  String rowspan = "1";
            	  if(value.getGuestDownloadCountSum() > 0) { 
            		  // need extra rowspan for weekly download details
            		  rowspan = "2";
            	  }
            		  usageRows.append("<tr><td  rowspan='" + rowspan + "' colspan='1' valign='top'><span class='fontClass'>" + value.getLabName() +  "</span></td>");
            		  String guestDownloadCountSum = value.getGuestDownloadCountSum()==0?"-":Integer.toString(value.getGuestDownloadCountSum());
            		  String guestCumDownloadCountSum = value.getGuestCumDownloadCountSum()==0?"-":Integer.toString(value.getGuestCumDownloadCountSum());
            		  String guestUserCountSum = value.getGuestUserCountSum()==0?"-":Integer.toString(value.getGuestUserCountSum());
            		  String guestCumUserCountSum = value.getGuestCumUserCountSum()==0?"-":Integer.toString(value.getGuestCumUserCountSum());
            		  
        			usageRows.append("<td  colspan='1' align='right'><span class='fontClass'>" + guestDownloadCountSum + "</span></td>");
        			usageRows.append("<td  colspan='1' align='right'><span class='fontClass'>" + guestUserCountSum + "</span></td>");    			       
        			if(value.getGuestDownloadCountSum() > 0) { // add details for downloads for this week
        				usageRows.append("<tr><td colspan='2'  align='left'><span class='fontClass'><b>Experiment/Analysis Links: </b><br>");   
    	    			for(String ae : value.getGuestAnalysisAndExperimentDownloadNamesForThisWeek().keySet()) {
    	    			      String thisExpAnalysisNumber = ae;
    	    		            String nameAndDescr = value.getGuestAnalysisAndExperimentDownloadNamesForThisWeek().get(ae);
    	    		            String href = baseURL + Constants.LAUNCH_APP_JSP + "?";
    	    		            if(thisExpAnalysisNumber.charAt(0) == 'A') {
    	    		              // Link to analysis
    	    		              href = href + "analysisNumber=" + thisExpAnalysisNumber + "&launchWindow=" + Constants.WINDOW_TRACK_ANALYSES;
    	    		            } else {
    	    		              // Link to experiment
    	    		              href = href + "requestNumber=" + thisExpAnalysisNumber + "&launchWindow=" + Constants.WINDOW_TRACK_REQUESTS;
    	    		            }
    	    		            usageRows.append("<a href='" + href + "'>" + thisExpAnalysisNumber  + "</a> " + " " + nameAndDescr + "<br>");            
    	    		          }
    	    		          usageRows.append("</span></td></tr>");                				
    	    			}        	  
              }
          }    	  

	      // Guest Usage Table
	      guestUsageTable.append("<table class='table700'  cellpadding='5' cellspacing='0' border='1' bgcolor='#F5FAFE'>");
	      guestUsageTable.append("<tr>");
	      guestUsageTable.append("<td class='tableHeadTop' colspan='3'><span class='fontClassLgeBold'>External Visits and Downloads for " + todaysDate + "</span></td>");
	      guestUsageTable.append("</tr>");
	      
	      // Summary - Header
	      guestUsageTable.append("<tr><td class='tableHeadMiddle' colspan='3'><span class='fontClassMedBold'>" + "Summary Data" + "</span></td></tr>");
	      guestUsageTable.append("<tr>");
	      guestUsageTable.append("<td class='tableHeadBottom' colspan='1'><span class='fontClassBold'>" + "Visits by Guests*" + "</span></td>");
	      guestUsageTable.append("<td class='tableHeadBottom' colspan='1'><span class='fontClassBold'>" + "Files Downloaded by Guests" + "</span></td>");
	      guestUsageTable.append("<td class='tableHeadBottom' colspan='1'><span class='fontClassBold'>" + "Guests Who Downloaded" + "</span></td>");
	      guestUsageTable.append("</tr>");  
	      // Summary - Data
	      guestUsageTable.append("<tr>");
	      guestUsageTable.append("<td  align='right' colspan='1'><span class='fontClass'>" + weeklyTotalVisits + "</span></td>");
	      guestUsageTable.append("<td  align='right' colspan='1'><span class='fontClass'>" + weeklyTotalGuestDownloads + "</span></td>");  
	      guestUsageTable.append("<td  align='right' colspan='1'><span class='fontClass'>" + weeklyTotalGuests + "</span></td>");
	      guestUsageTable.append("</tr>"); 

	      // Lab Detail - Header
	      guestUsageTable.append("<tr><td bgcolor='#c7e3f9' align='center' colspan='3'><span class='fontClassMedBold'>" + "Guest Usage By Lab" + "</span></td></tr>");
	      guestUsageTable.append("<tr>");
	      guestUsageTable.append("<td bgcolor='#deeefc'  align='center' colspan='1' rowspan='1'><span class='fontClassBold'>" + "Lab" 									+ "</span></td>");
	      guestUsageTable.append("<td bgcolor='#deeefc'  align='center' colspan='1'><span class='fontClassBold'>" 			+ "Files Downloaded by Guests"				+ "</span></td>");
	      guestUsageTable.append("<td bgcolor='#deeefc'  align='center' colspan='1'><span class='fontClassBold'>" 			+ "Guests Who Downloaded" 	+ "</span></td>");
	      guestUsageTable.append("</tr>");
	      
	      // Lab Detail - Data
	      guestUsageTable.append(usageRows.toString());
	      guestUsageTable.append("</table>");
	      guestUsageTable.append("<table style='width:700px;font-style:italic;font-size:x-small'><tr><td>&nbsp;&nbsp;</td><td>*A guest visit is any session using the Guest sign-in. Includes visits by B2B members who enter through Guest sign-in to view only the publicly available entries.</td></table>");
      }
      return guestUsageTable;
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
	      expName = "<b>" + expName + "</b><br>";
	      String expDescription = stripAndTruncate((String)row[6], 250);
	      if(expDescription == null || expDescription.length() == 0) {
	        expDescription = " ";        
	      }
	      expDescription = expDescription;
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
	      analysisName = "<b>" + analysisName + "</b><br>";     
	      String analysisDescription = stripAndTruncate((String)row[6], 250);
	      if(analysisDescription == null || analysisDescription.length() == 0) {
	        analysisDescription = " ";        
	      }
	      analysisDescription = analysisDescription;
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
				String name = (String) row[3];
				Integer userCount = ((Integer) row[4]).intValue();
				Integer downloadCount = ((Integer) row[5]).intValue();
				HashMap<String, AEGuestUsageStats> statsMap = ls
						.getGuestUsageStatsList();
				AEGuestUsageStats stat = new AEGuestUsageStats();
				stat.setAEnumber(number);
				stat.setAEname(name);
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
				String name = (String) row[3];
				Integer userCount = ((Integer) row[4]).intValue();
				Integer downloadCount = ((Integer) row[5]).intValue();
				HashMap<String, AEGuestUsageStats> statsMap = ls
						.getGuestUsageStatsList();
				AEGuestUsageStats stat = new AEGuestUsageStats();
				stat.setAEnumber(number);
				stat.setAEname(name);
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
//				cumTotalGuests			 += cumUserCount;
//				cumTotalGuestDownloads += cumDownloadCount;
				HashMap<String, AEGuestUsageStats> statsMap = ls
						.getGuestUsageStatsList();
				AEGuestUsageStats stat = statsMap.get(number);
				if (stat == null) {
					stat = new AEGuestUsageStats();
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
		
		
		
		// -- Get the WEEKLY COUNT for the number of UNIQUE USERS who downloaded
		// ANALYSIS OR EXPERIMENT files for each lab
		queryBuf = new StringBuffer();
		queryBuf.append(" SELECT DISTINCT tl.idLab, COUNT(DISTINCT tl.emailAddress), COUNT(tl.fileName) ");
		queryBuf.append(" FROM TransferLog AS tl, Lab AS l ");
		queryBuf.append(" WHERE tl.idLab = l.idLab AND tl.idAppUser = -999999 AND l.excludeUsage = 'N' ");
		queryBuf.append(" AND startDateTime > '" + fFormat.formatDate(startDate.getTime(), FieldFormatter.DATE_OUTPUT_SQL) + "' ");
		queryBuf.append(" AND startDateTime < '" + fFormat.formatDate(endDate.getTime(), FieldFormatter.DATE_OUTPUT_SQL) + "' ");
		queryBuf.append(" AND l.isActive = 'Y' AND tl.transferType = 'download'	GROUP BY tl.idLab ");
		
		rows = sess.createQuery(queryBuf.toString()).list();
		for(Iterator i = rows.iterator(); i.hasNext();) {
			Object[] row = (Object[]) i.next();
			Integer idLab = (Integer) row[0];
			LabStats ls = labInfo.get(idLab);
			if(ls != null) {
				Integer guestUserCountSum = (Integer) row[1];
				ls.setGuestUserCountSum(guestUserCountSum);
			}
		}

		
		
		// -- Get the CUMULATIVE COUNT for the number of UNIQUE USERS who downloaded
		// ANALYSIS OR EXPERIMENT files for each lab
		queryBuf = new StringBuffer();
		queryBuf.append(" SELECT DISTINCT tl.idLab, COUNT(DISTINCT tl.emailAddress), COUNT(tl.fileName) ");
		queryBuf.append(" FROM TransferLog AS tl, Lab AS l ");
		queryBuf.append(" WHERE tl.idLab = l.idLab AND tl.idAppUser = -999999 AND l.excludeUsage = 'N' ");
		queryBuf.append(" AND l.isActive = 'Y' AND tl.transferType = 'download'	GROUP BY tl.idLab ");
		
		rows = sess.createQuery(queryBuf.toString()).list();
		for(Iterator i = rows.iterator(); i.hasNext();) {
			Object[] row = (Object[]) i.next();
			Integer idLab = (Integer) row[0];
			LabStats ls = labInfo.get(idLab);
			if(ls != null) {
				Integer guestCumUserCountSum = (Integer) row[1];
				ls.setGuestCumUserCountSum(guestCumUserCountSum);
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
				HashMap<String, AEGuestUsageStats> statsMap = ls
						.getGuestUsageStatsList();
				AEGuestUsageStats stat = statsMap.get(number);
				if (stat == null) {
					stat = new AEGuestUsageStats();
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
		//-- Get TOTAL (all GNomEx guests) WEEKLY COUNT for visits (every visit is counted, not just visits by unique guests)
		queryBuf.append(" SELECT COUNT(idVisitLog) FROM VisitLog AS vl ");
		queryBuf.append(" WHERE visitDateTime > '" + fFormat.formatDate(startDate.getTime(), FieldFormatter.DATE_OUTPUT_SQL) + "' ");
		queryBuf.append(" AND visitDateTime < '" + fFormat.formatDate(endDate.getTime(), FieldFormatter.DATE_OUTPUT_SQL) + "' ");
		queryBuf.append(" AND idAppUser = -999999 ");
		rows = sess.createQuery(queryBuf.toString()).list();
		for(Iterator i = rows.iterator(); i.hasNext();) {
			weeklyTotalVisits = (Integer)i.next();
			break;
		}
		
		queryBuf = new StringBuffer();
		//-- Get TOTAL (all GNomEx guests) CUMULATIVE COUNT for visits (every visit is counted, not just visits by unique guests)
		queryBuf.append(" SELECT COUNT(idVisitLog) FROM VisitLog AS vl ");
		queryBuf.append(" WHERE idAppUser = -999999 ");
		rows = sess.createQuery(queryBuf.toString()).list();
		for(Iterator i = rows.iterator(); i.hasNext(); ) {			
			cumTotalVisits = (Integer)i.next();
			break;
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
  
  private static String stripAndTruncate(String value, int length) {
    if(value != null) {
      value = value.replaceAll("(\\r|\\n)", "");
      value = value.replaceAll("(<P.*?>|</P>)", "");
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
  // a GuestUsageStats holds data for either an experiment or an anlaysis for which a requested data at any time in the past
  private class AEGuestUsageStats {
	  private String aeNumber;
	  private String aeName;
	  private Integer aeUserCount; // number of users requesting data for this experiment/analysis this week
	  private Integer aeDownloadCount; // number of downloads by guests for this experiment/analysis this week
	  private Integer aeCumUserCount; // number of users requesting data for this experiment/analysis EVER
	  private Integer aeCumDownloadCount; // number of downloads by guests for this experiment/analysis EVER
	  
	  public AEGuestUsageStats() {
		  aeNumber = "";
		  aeUserCount = new Integer(0);
		  aeDownloadCount = new Integer(0);
		  aeCumUserCount = new Integer(0);
		  aeCumDownloadCount = new Integer(0);
	  }
	  
	  public String getAEnumber() {
		  return aeNumber;
	  }
	  public String getAEname() {
		  return aeName;
	  }
	  public Integer getUserCount() {
		  return aeUserCount;
	  }
	  public Integer getDownloadCount() {
		  return aeDownloadCount;
	  }	  
	  public Integer getCumUserCount() {
		  return aeCumUserCount;
	  }	  
	  public Integer getCumDownloadCount() {
		  return aeCumDownloadCount;
	  }
	  
	  public void setAEnumber(String AEnumber) {
		  this.aeNumber = AEnumber;
	  }
	  public void setAEname(String AEname) {
		  this.aeName = AEname;
	  }
	  public void setUserCount(Integer userCount) {
		  this.aeUserCount = userCount;
	  }
	  public void setDownloadCount(Integer downloadCount) {
		  this.aeDownloadCount = downloadCount;
	  }	  
	  public void setCumUserCount(Integer cumUserCount) {
		  this.aeCumUserCount = cumUserCount;
	  }	  
	  public void setCumDownloadCount(Integer cumDownloadCount) {
		  this.aeCumDownloadCount = cumDownloadCount;
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
    private HashMap<String, AEGuestUsageStats> guestUsageStatsList;
    // can't simply sum over data for analyses and experiments as we get double counting of users who downloaded from both analyses and experiments
    // must run a separate query. This problem doesn't occur with downloads as they are unique with respect to each analysis/experiment (we can sum them)
    private int guestUserCountSum; // total number of guest users across all analyses/experiments for this lab in THIS WEEK    
    private int guestCumUserCountSum; // total number of guest users across all analyses/experiments for this lab EVER    
    
    public HashMap<String, AEGuestUsageStats> getGuestUsageStatsList () {
    	return guestUsageStatsList;
    }
    
    public void setGuestUserCountSum(int guestUserCountSum) {
    	this.guestUserCountSum = guestUserCountSum;
    }
    
    public int getGuestUserCountSum() {
//    	int result = 0;
//    	for(String s : guestUsageStatsList.keySet()){
//    		result += guestUsageStatsList.get(s).getUserCount();    		
//    	}
//    	return result;
    	return guestUserCountSum;
    }
    
    // get a sum for downloads for all analyses/experiments for this lab
    public int getGuestDownloadCountSum() {
    	int result = 0;
    	for(String s : guestUsageStatsList.keySet()){
    		result += guestUsageStatsList.get(s).getDownloadCount();    		
    	}
    	return result;
    }
    
    public void setGuestCumUserCountSum(int guestCumUserCountSum) {
    	this.guestCumUserCountSum = guestCumUserCountSum;
    }
    
    public int getGuestCumUserCountSum() {
//    	int result = 0;
//    	for(String s : guestUsageStatsList.keySet()){
//    		String AEname = s;
//    		String labName = this.labName;
//    		AEGuestUsageStats gs = guestUsageStatsList.get(s);
//    		int cumUserCount = gs.getCumUserCount();
//    		result += guestUsageStatsList.get(s).getCumUserCount();    		
//    	}
//    	return result;
    	return guestCumUserCountSum;
    }
    public int getGuestCumDownloadCountSum() {
    	int result = 0;
    	for(String s : guestUsageStatsList.keySet()){
    		result += guestUsageStatsList.get(s).getCumDownloadCount();    		
    	}
    	return result;
    }
    
    public HashMap<String,String> getGuestAnalysisAndExperimentDownloadNamesForThisWeek() {
    	HashMap<String,String> result = new HashMap<String,String>();
    	for(String s : guestUsageStatsList.keySet()){
    		if(guestUsageStatsList.get(s).getDownloadCount() > 0)
    			result.put( guestUsageStatsList.get(s).getAEnumber(),guestUsageStatsList.get(s).getAEname() );    		
    	}
    	return result;
    }
    
    
	  private String AEnumber;
	  
    
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
      guestUsageStatsList = new HashMap<String, AEGuestUsageStats>();
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
