package hci.gnomex.daemon;

import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.ExperimentFile;
import hci.gnomex.model.Lab;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.utility.BatchDataSource;
import hci.gnomex.utility.BatchMailer;
import hci.gnomex.utility.MailUtil;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

public class PendingWorkAuthd extends TimerTask {

  private static long fONCE_PER_DAY = 1000*60*60*24; // A day in milliseconds
  
  private static int fONE_DAY = 1;
  private static int wakeupHour = 2;    // Default wakupHour is 2 am
  private static int fZERO_MINUTES = 0;
  
  private BatchDataSource dataSource;
  private Session         sess;

  private String serverName;
  
  private ArrayList<String> waList; 
  
  private PropertyDictionaryHelper propertyHelper; 
  
  private static PendingWorkAuthd app;
  
  private Properties mailProps;
  
  private boolean runAsDaemon = false;

  
  public PendingWorkAuthd(String[] args) {
    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("-server")) {
        serverName = args[++i];
      } else if (args[i].equals("-wakeupHour")) {
        wakeupHour = Integer.valueOf(args[++i]);
      } else if (args[i].equals ("-runAsDaemon")) {
        runAsDaemon = true;
      }
    } 
    
    try {
      mailProps = new BatchMailer().getMailProperties();
    } catch (Exception e){
      System.err.println("Cannot initialize mail properties");
      System.exit(0);
    }
  }
  
  /**
   * @param args
   */
  public static void main(String[] args) {
    app  = new PendingWorkAuthd(args);
    
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
    Calendar calendar = Calendar.getInstance();
    int weekday = calendar.get(Calendar.DAY_OF_WEEK);
    
    if (weekday == 7 || weekday == 1) {
      // Don't send Saturday or Sunday
      return;
    }
    
    try {
      org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger("org.hibernate");
      log.setLevel(Level.ERROR);

      dataSource = new BatchDataSource();
      app.connect();
      
      propertyHelper = PropertyDictionaryHelper.getInstance(sess);
      String contactList = propertyHelper.getQualifiedProperty(PropertyDictionary.CONTACT_EMAIL_CORE_FACILITY_WORKAUTH_REMINDER, serverName); 
      String replyEmail = propertyHelper.getQualifiedProperty(PropertyDictionary.REPLY_EMAIL_CORE_FACILITY_WORKAUTH_REMINDER, serverName);
      if(replyEmail == null || replyEmail.length() == 0) {
        replyEmail = "DoNotReply@hci.utah.edu";
      }
      
      String subject = "Pending Work Authorizations";
      
      getPendingWorkAuthorizations(sess);
      Iterator<String> it = waList.iterator();
      boolean hasWorkAuthorizations = false;

      StringBuffer tableRows = new StringBuffer("");
      while(it.hasNext()) {
        if(!hasWorkAuthorizations) {
          tableRows.append("<tr><td width='250'><span class='fontClassBold'>Lab</span></td><td width='500'><span class='fontClassBold'>Account</span></td></tr>");
        }
        tableRows.append((String) it.next());
        hasWorkAuthorizations = true;
      }

      if(!hasWorkAuthorizations) {
        subject = "(No Pending Work Authorizations)";
        tableRows.append("<tr><td align='center'><span class='fontClass'>There are no pending work authorizations at this time.</span></td></tr>");        
      }
      
      // Build message body in html
      StringBuffer body = new StringBuffer("");
      
      body.append("<html><head><title>Work Authorization Status</title><meta http-equiv='content-style-type' content='text/css'></head>");
      body.append("<body leftmargin='0' marginwidth='0' topmargin='0' marginheight='0' offset='0' bgcolor='#FFFFFF'>");
      body.append("<style>.fontClass{font-size:11px;color:#000000;font-family:verdana;text-decoration:none;}");
      body.append(" .fontClassBold{font-size:11px;font-weight:bold;color:#000000;font-family:verdana;text-decoration:none;}");
      body.append(" .fontClassLgeBold{font-size:12px;line-height:22px;font-weight:bold;color:#000000;font-family:verdana;text-decoration:none;}</style>");
      body.append("<table width='100%' cellpadding='10' cellspacing='0' bgcolor='#FFFFFF'><tr><td width='20'>&nbsp;</td><td valign='top' align='left'>");
      if(hasWorkAuthorizations) {
        body.append("<table cellpadding='0' cellspacing='0' border='0' bgcolor='#FFFFFF'>");
        body.append("<tr><td align='left' height='20'><span class='fontClassLgeBold'>The following work authorizations are waiting to be approved:</span></td></tr>");        
        body.append("</table>");        
      }
      body.append("<table cellpadding='5' cellspacing='0' border='1' bgcolor='#EBF2FC'>");
      body.append(tableRows.toString());
      body.append("</table></td></tr></table></body></html>");
      MailUtil.send(mailProps, contactList, "", replyEmail, subject, body.toString(), true);        
      app.disconnect();      
         
    } catch (Exception e) {
      System.out.println( e.toString() );
      e.printStackTrace();
    }
  }
  
  
  private void getPendingWorkAuthorizations(Session sess) throws Exception{
    Connection myConn = null;

    try 
    {
      myConn = sess.connection();
  
      StringBuffer hqlbuf = new StringBuffer("SELECT l from Lab l ");
      hqlbuf.append(" join l.billingAccounts ba");
      hqlbuf.append(" where ba.isApproved <> 'Y'");
      hqlbuf.append(" order by l.lastName, l.firstName");
      
      waList = new ArrayList<String>();
      
      List results = sess.createQuery(hqlbuf.toString()).list();
      for (Iterator i = results.iterator(); i.hasNext();) {
        Lab thisLab = (Lab)i.next();
        for(Iterator j = thisLab.getPendingBillingAccounts().iterator(); j.hasNext();) {
          BillingAccount ba = (BillingAccount) j.next();
          waList.add("<tr><td width='250'><span class='fontClass'>" + thisLab.getName() + "</span></td><td width='500'><span class='fontClass'>" + ba.getAccountNameAndNumber() + "</span></td></tr>");
        }
      }      
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
  
}
