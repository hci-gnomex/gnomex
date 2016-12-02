package hci.gnomex.daemon;

import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.Analysis;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.Lab;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.Request;
import hci.gnomex.model.Visibility;
import hci.gnomex.model.VisibilityInterface;
import hci.gnomex.utility.BatchDataSource;
import hci.gnomex.utility.BatchMailer;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.MailUtil;
import hci.gnomex.utility.MailUtilHelper;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.IOException;
import java.io.StringReader;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.naming.NamingException;

import org.apache.log4j.Level;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.apache.log4j.Logger;


public class DatasetExpirationCheckd extends TimerTask {

  private static long fONCE_PER_DAY = 1000*60*60*24; // A day in milliseconds
  
  private static int fONE_DAY = 1;
  private static int wakeupHour = 2;    // Default wakupHour is 2 am
  private static int fZERO_MINUTES = 0;
  
  private int expWarningDays;
  
  private BatchDataSource dataSource;
  private Session         sess;

  private String serverName;
  
  private ArrayList<String> waList; 
  
  private PropertyDictionaryHelper propertyHelper; 
  
  private static DatasetExpirationCheckd app;
  
  private Properties mailProps;
  
  private boolean runAsDaemon = false;

  private boolean warningOnly = false;
  
  private boolean catchUpWarnings = false;
  
  private boolean sendMail = true;
  
  private String extraEmailAddress = null;
  
  public DatasetExpirationCheckd(String[] args) {
    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("-server")) {
        serverName = args[++i];
      } else if (args[i].equals("-wakeupHour")) {
        wakeupHour = Integer.valueOf(args[++i]);
      } else if (args[i].equals ("-runAsDaemon")) {
        runAsDaemon = true;
      } else if (args[i].equals ("-warningOnly")) {
        warningOnly = true;
      } else if (args[i].equals ("-doNotSendMail")) {
        sendMail = false;
      } else if (args[i].equals ("-catchUpWarnings")) {
        catchUpWarnings = true;
      } else if (args[i].equals ("-extraEmailAddress")) {
        extraEmailAddress = args[++i];
      }
    } 

    if (catchUpWarnings && !warningOnly) {
      System.err.println("-warningOnly must be specified if -catchUpWarnings is specified.");
      System.exit(0);
    }
    
    if (sendMail) {
      try {
        mailProps = new BatchMailer().getMailProperties();
      } catch (Exception e){
        System.err.println("Cannot initialize mail properties");
        System.exit(0);
      }
    }
  }
  
  /**
   * @param args
   */
  public static void main(String[] args) {
    app  = new DatasetExpirationCheckd(args);

    // Can either be run as daemon or run once (for scheduled execution - e.g. crontab)
    if(app.runAsDaemon) {
      // Perform the task once a day at <wakeupHour>., starting tomorrow morning
      Timer timer = new Timer();
      timer.scheduleAtFixedRate(app, getWakeupTime(), fONCE_PER_DAY);       
    } else {
      app.run();
    }

  }

  @Override
  public void run() {
   
    try {
      Logger log = Logger.getLogger("org.hibernate");
      log.setLevel(Level.ERROR);

      dataSource = new BatchDataSource();
      app.connect();
      
      if(serverName == null) {
        serverName = InetAddress.getLocalHost().getHostName();
      }
      
      propertyHelper = PropertyDictionaryHelper.getInstance(sess);
      String datasetPrivacyExp = propertyHelper.getQualifiedProperty(PropertyDictionary.DATASET_PRIVACY_EXPIRATION, serverName);
      if(datasetPrivacyExp == null || datasetPrivacyExp.length() == 0) {
        System.out.println("Feature disabled: DATASET_PRIVACY_EXPIRATION has not been set.");        
        app.disconnect();
        return;
      }
      
      String datasetPrivacyExpWarning = propertyHelper.getQualifiedProperty(PropertyDictionary.DATASET_PRIVACY_EXPIRATION_WARNING, serverName);
      if(datasetPrivacyExpWarning == null || datasetPrivacyExpWarning.length() == 0) {
        expWarningDays = 0;
      } else {
        try {
          expWarningDays = Integer.parseInt(datasetPrivacyExpWarning);
        } catch (NumberFormatException e) {
          System.out.println("Feature disabled: illegal DATASET_PRIVACY_EXPIRATION_WARNING value.");
          expWarningDays = 0;
        }           
      } 
           
      Integer dpExp = null;
      try {
        dpExp = Integer.parseInt(datasetPrivacyExp);
      } catch (NumberFormatException e) {
        System.out.println("Feature disabled: illegal DATASET_PRIVACY_EXPIRATION value.");        
        app.disconnect();
        return;
      }
      
      if(dpExp <= 0) {
        System.out.println("Feature disabled: DATASET_PRIVACY_EXPIRATION is <= 0.");        
        app.disconnect();
        return;
      }
      
      sendExpirationWarnings(sess);
      
      if (!warningOnly) {
        changeVisibilityIfExpired(sess);
      }
      
    
      app.disconnect();      
         
    } catch (Exception e) {
      System.out.println( e.toString() );

    }
    
    System.exit(0);

  }
  
  private void emailExpirationWarning(String emailTo, String number, java.sql.Date expireDate, String typeName) throws AddressException, NamingException, MessagingException, IOException {
    
    if (extraEmailAddress != null) {
      emailTo += "," + extraEmailAddress;
    }
    
    // Build message body in html
    StringBuffer body = new StringBuffer("");
    
    SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
    
    String replyEmail = propertyHelper.getQualifiedProperty(PropertyDictionary.DATASET_PRIVACY_EXPIRATION_REPLY_EMAIL, serverName);
    if(replyEmail == null || replyEmail.length() == 0) {
      replyEmail = "DoNotReply@hci.utah.edu";
    }    
    
    DictionaryHelper dictionaryHelper = DictionaryHelper.getInstance(sess); 

    body.append("<html><head><title>Restricted Visiblity Expiration</title><meta http-equiv='content-style-type' content='text/css'></head>");
    body.append("<body leftmargin='0' marginwidth='0' topmargin='0' marginheight='0' offset='0' bgcolor='#FFFFFF'>");
    body.append("<style>.fontClass{font-size:11px;color:#000000;font-family:verdana;text-decoration:none;}");
    body.append(" .fontClassBold{font-size:11px;font-weight:bold;color:#000000;font-family:verdana;text-decoration:none;}");
    body.append(" .fontClassLgeBold{font-size:12px;line-height:22px;font-weight:bold;color:#000000;font-family:verdana;text-decoration:none;}</style>");

    String subject = "Restricted visibility expiration for " + typeName + " " + number;
    
    body.append("<table width='400' cellpadding='0' cellspacing='0' bgcolor='#FFFFFF'><tr><td width='10'>&nbsp;</td><td valign='top' align='left'>");
    body.append("This is to inform you that visiblity for " + typeName + " " + number + " is set to expire on " + dateFormatter.format(expireDate) + ".");
    body.append(" If you do not change the visibility expiration date, visibility will change to " + Visibility.VISIBLE_TO_PUBLIC + ".<br><br>");
    body.append(" You are receiving this notice because you and/or your group is listed as owner.");
    
    body.append("</td></tr></table></body></html>");
    if (sendMail) {
      MailUtilHelper helper = new MailUtilHelper(	
    		  	mailProps,
    		  	emailTo,
				null,
				null,
				replyEmail,
				subject,
				body.toString(),
				null,
				true, 
				dictionaryHelper,
				serverName 			);
      MailUtil.validateAndSendEmail(helper);
    }
  }

  private String getEmailTo(AppUser appUser, Lab lab) {
    String ownerEmail = "";
    if(appUser != null) {
      ownerEmail = appUser.getEmail();
      if (ownerEmail == null || ownerEmail.equals("")) {
        ownerEmail = "";
      }
    }
    
    String managerEmails = "";
    if(lab != null && lab.getManagers() != null) {
      for(Iterator i1 = lab.getManagers().iterator(); i1.hasNext();) {
        AppUser manager = (AppUser)i1.next();
        if (manager.getIsActive() != null && manager.getIsActive().equalsIgnoreCase("Y")) {
          String currentManagerEmail = manager.getEmail();
          if (currentManagerEmail == null || currentManagerEmail.equals("")) {
            currentManagerEmail = "";
          }                            
          if(managerEmails.length() == 0) {
            managerEmails = currentManagerEmail;
          } else {
            managerEmails = managerEmails + ", " + currentManagerEmail;
          }                
        }
      }           
    }
    

    String labEmail = "";
    if(lab != null) {
      labEmail = lab.getContactEmail();
      if (labEmail == null || labEmail.equals("")) {
        labEmail = "";
      }
    }
    
    String emailTo = "";        
    if(ownerEmail.length() == 0) {
      emailTo = managerEmails;
    } else {
      if(managerEmails.length() == 0) {
        emailTo = ownerEmail;
      } else {
        emailTo = ownerEmail + ", " + managerEmails;
      }
    }
    
    return emailTo;
  }

  private void sendExpirationWarnings(Session sess) throws Exception{
    if(expWarningDays <= 0) {
      return; // Nothing to do if exp warning days <= 0
    }
    
    try {
      Date futureDate = new Date();
      Calendar cal = Calendar.getInstance();
      cal.clear();
      cal.setTime(futureDate);
      cal.clear(Calendar.HOUR);
      cal.clear(Calendar.MINUTE);
      cal.clear(Calendar.SECOND);
      cal.clear(Calendar.MILLISECOND);
      cal.add(Calendar.DATE, expWarningDays);
      futureDate = cal.getTime();
      
      String whereClause = " where privacyExpirationDate is not null and privacyExpirationDate = :futureDate and codeVisibility <> '" + Visibility.VISIBLE_TO_PUBLIC + "'";
      if (catchUpWarnings) {
        whereClause = " where privacyExpirationDate is not null and privacyExpirationDate <= :futureDate and codeVisibility <> '" + Visibility.VISIBLE_TO_PUBLIC + "'";
      }

      Query aQuery = sess.createQuery("SELECT a from Analysis a " + whereClause);
      aQuery.setDate("futureDate", futureDate);
      List aItems = (List)aQuery.list();
      for(Iterator<Analysis> i = aItems.iterator(); i.hasNext();) {
        Analysis a = i.next();
        
        String emailTo = getEmailTo(a.getAppUser(), a.getLab());
        if(emailTo.length() > 0) {
          emailExpirationWarning(emailTo, a.getNumber(), a.getPrivacyExpirationDate(), "analysis");
        }
      }         

      Query rQuery = sess.createQuery("SELECT r from Request r " + whereClause);
      rQuery.setDate("futureDate", futureDate);
      List rItems = (List)rQuery.list();
      for(Iterator<Request> i = rItems.iterator(); i.hasNext();) {
        Request r = i.next();
        
        String emailTo = getEmailTo(r.getAppUser(), r.getLab());
        if(emailTo.length() > 0) {
          emailExpirationWarning(emailTo, r.getNumber(), r.getPrivacyExpirationDate(), "request");
        }
      }         
    }

    catch (Exception ex) {
      System.out.println("Error sending restricted visibility warning: "  + ex.getMessage());
      ex.printStackTrace(); //DEBUG LINE
      throw new RollBackCommandException();
    }        
  }  

  
  private void changeVisibilityIfExpired(Session sess) throws Exception{
    try {
      Date today = new Date();
      Calendar cal = Calendar.getInstance();
      cal.clear(Calendar.HOUR);
      cal.clear(Calendar.MINUTE);
      cal.clear(Calendar.SECOND);
      cal.clear(Calendar.MILLISECOND);
      today = cal.getTime();

      String whereClause = " where privacyExpirationDate is not null and privacyExpirationDate < :today and codeVisibility != '" + Visibility.VISIBLE_TO_PUBLIC + "'";
      
      Query aQuery = sess.createQuery(" SELECT a from Analysis a " + whereClause);
      aQuery.setDate("today", today);
      List<VisibilityInterface> aItems = (List<VisibilityInterface>)aQuery.list();
      changeVisibilityIfExpired(sess, aItems);
      
      Query rQuery = sess.createQuery(" SELECT r from Request r " + whereClause);
      rQuery.setDate("today", today);
      List<VisibilityInterface> rItems = (List<VisibilityInterface>)rQuery.list();
      changeVisibilityIfExpired(sess, rItems);
    }

    catch (Exception ex) {
      System.out.println("Error setting dataset visibility: " + ex.getMessage());
      throw new RollBackCommandException();
    }        
  }  

  private void changeVisibilityIfExpired(Session sess, List<VisibilityInterface> objects) throws Exception {
    Transaction trans = sess.beginTransaction();
    
    for(VisibilityInterface vis : objects) {
      vis.setCodeVisibility(Visibility.VISIBLE_TO_PUBLIC);
      sess.save(vis);
      sess.flush();   
    }      
    trans.commit();     
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
  
}
