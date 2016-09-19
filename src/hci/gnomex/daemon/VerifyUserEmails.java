package hci.gnomex.daemon;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.utility.BatchDataSource;
import hci.gnomex.utility.BatchMailer;
import hci.gnomex.utility.MailUtil;
import hci.gnomex.utility.MailUtilHelper;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

//import javax.mail.MessagingException;
//import javax.mail.internet.AddressException;
//import javax.naming.NamingException;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.TransactionException;

public class VerifyUserEmails {
  private static VerifyUserEmails         app = null;

  private Transaction                     tx;

  private Properties                      mailProps;
  private Boolean                         sendMail = true;
  private String                          orionPath = "";
  private Session                         sess;
  private BatchDataSource                 dataSource;
  private int                             noEmailCount = 0;
  private int                             successfulSendCount = 0;
  private String                          doNotReplyEmail;
  private String                          testEmailAddress = "";
  private Boolean                         testEmail = false;
  private String                          serverName = null;
  private String                          gnomexSupportEmail = "";
  private String                          imageLoc = "";
  private String                          protocol = "http://";

  public VerifyUserEmails(String args[]) {
    for(int i = 0; i < args.length; i++) {
      if(args[i].equals("-doNotSendEmail")) {
        sendMail = false;
      } else if (args[i].equals ("-testEmailAddress")) {
        testEmailAddress = args[++i];
      } else if (args[i].equals ("-server")) {
        serverName = args[++i];
      }  else if (args[i].equals ("-orionPath")) {
        orionPath = args[++i];
      } else if (args[i].equals ("-isHTTPS")) {
        protocol = "https://";
      } else if(args[i].equals("-help")) {
        showHelp();
        System.exit(0);
      }
    }

    if (!checkParameters()) {
      showHelp();
      System.exit(0);
    }

  }

  private Boolean checkParameters() {
    if(serverName == null) {
      System.err.println("You must provide a server name.");
      return false;
    }

    return true;
  }

  private void showHelp() {
    System.out.println("Sends out an email to all gnomex users asking them to verify the email address we have on file for them.");
    System.out.println("Switches:");
    System.out.println("   -server - server name that gnomex is running on.");
    System.out.println("   -orionPath - path to Orion directory to get mail properties.");
    System.out.println("   -doNotSendEmail - used for debugging if no email server available.  Emails are created but not sent.");
    System.out.println("   -testEmailAddress - Overrides all lab emails with this email address.  Used for testing.");
    System.out.println("   -isHTTPS - Provide this command if gnomex is running on https.");
    System.out.println("   -help - gives this message.  Note no other processing is performed if the -help switch is specified.");
  }


  public static void main(String[] args) {
    app  = new VerifyUserEmails(args);

    app.run();
  }

  public void run() {

    try {

      dataSource = new BatchDataSource();
      app.connect();

      if (sendMail) {
        mailProps = new BatchMailer(orionPath).getMailProperties();
      }

      PropertyDictionaryHelper ph = PropertyDictionaryHelper.getInstance(sess);
      this.gnomexSupportEmail = ph.getProperty(PropertyDictionary.GNOMEX_SUPPORT_EMAIL);
      this.doNotReplyEmail = ph.getProperty(PropertyDictionary.GENERIC_NO_REPLY_EMAIL);
      testEmail = testEmailAddress.length() > 0;

      if(ph.getProperty(PropertyDictionary.SITE_LOGO) == null || !ph.getProperty(PropertyDictionary.SITE_LOGO).equals("")) {
        imageLoc = protocol + serverName + "/gnomex/images/gnomex_logo.png";
      } else {
        imageLoc = protocol + serverName + "/gnomex/" + ph.getProperty(PropertyDictionary.SITE_LOGO);
        imageLoc = imageLoc.replace("assets/", "images/");
      }

      List appUsers = sess.createQuery("Select au from AppUser au").list();

      tx = sess.beginTransaction();

      for(Iterator i = appUsers.iterator(); i.hasNext();) {
        AppUser au = (AppUser)i.next();
        if(au.getEmail() ==  null || au.getEmail().equals("")) {
          noEmailCount++;
          continue;
        }else {
          String uuidStr = UUID.randomUUID().toString(); 
          au.setConfirmEmailGuid(uuidStr);
          sendEmail(au);
          sess.save(au);
          successfulSendCount++;
        }
      }

      //TESTING CODE:
      //      AppUser au = (AppUser)sess.load(AppUser.class, 1887);
      //      String uuidStr = UUID.randomUUID().toString(); 
      //      au.setConfirmEmailGuid(uuidStr);
      //      sendEmail(au);
      //      sess.save(au);
      //
      //      AppUser au1 = (AppUser)sess.load(AppUser.class, 1888);
      //      uuidStr = UUID.randomUUID().toString(); 
      //      au1.setConfirmEmailGuid(uuidStr);
      //      sendEmail(au1);
      //      sess.save(au1);
      //END TESTING CODE

      sess.flush();
      tx.commit();

      sendCompleteEmail();

    } catch(Exception e) {
      sendErrorReport(e);

    } finally {

      if (sess != null) {
        try {
          app.disconnect();
        } catch(Exception e) {
          System.err.println( "VerifyUserEmail unable to disconnect from hibernate session.   " + e.toString() );
        }
      }

    }
    
    System.exit(0);
  }

  private void sendCompleteEmail() {
    if (sendMail) {
      String toAddress = gnomexSupportEmail;
      if (testEmailAddress.length() > 0) {
        toAddress = testEmailAddress;
      }

      StringBuffer body = new StringBuffer();
      body.append("The Verify User Email cron job completed successfully.  There were " + noEmailCount + " users without an email on file.\n\n");
      body.append("Gnomex sent verification emails to " + successfulSendCount + " users.");


      try {
        MailUtilHelper helper = new MailUtilHelper(
        	mailProps,
            toAddress,
            null,
            null,
            doNotReplyEmail,
            "VerifyUserEmails Complete",
            body.toString(),
            null,
            false,
            testEmail,
            testEmailAddress
        );
        MailUtil.validateAndSendEmail(helper);
      } catch (Exception e) {
        System.err.println("Unable to send confirmation email to gnomex support staff");
      }
    }

  }

  private void sendEmail(AppUser au) {

    if (sendMail) {
      String toAddress = au.getEmail();
      if (testEmailAddress.length() > 0) {
        toAddress = testEmailAddress;
      }
      String loginURL = protocol + serverName + "/gnomex";

      StringBuffer body = new StringBuffer();
      body.append("<img src=" + imageLoc + ">");
      body.append("<p>This email is being sent out to verify that the email address we have on file for you is correct.  Please take a moment to login to GNomEx to verify your email." +
          " Please note that if you don't login within 7 (seven) days," +
      " we will automatically remove this email address from your user account and the next time you login to GNomEx you will be asked to provide a new address.  Thanks!</p><br>");

      body.append("<a href=" + loginURL + ">Click here to launch GNomEx </a>");
      body.append("<br>Or copy and paste this into your browser:  " + loginURL);
      body.append("<br><br>-GNomEx Support Team");

      body.append("<br><br>  Questions?  Email us at: " + gnomexSupportEmail);
      try {
        MailUtilHelper helper = new MailUtilHelper(
        	mailProps,
            toAddress,
            null,
            null,
            doNotReplyEmail,
            "Please verify your GNomEx contact email address",
            body.toString(),
            null,
            true,
            testEmail,
            testEmailAddress
        );
        MailUtil.validateAndSendEmail(helper);
      } 
      catch(Exception ex) {
        System.err.println("Unable to send email to app user " + au.getDisplayName() + " with email " + au.getEmail() + " because of exception: " + ex.getMessage());
      }
    }
  }

  private void connect() throws Exception{
    sess = dataSource.connect();
  }

  private void disconnect() throws Exception {
    sess.close();
  }

  private void sendErrorReport(Exception e)  {

    String msg = "Could not verify user emails. Transaction rolled back:   " + e.toString() + "\n\t";

    StackTraceElement[] stack = e.getStackTrace();
    for (StackTraceElement s : stack) {
      msg = msg + s.toString() + "\n\t\t";
    }

    try {
      if (tx != null) {
        tx.rollback();
      }
    }
    catch(TransactionException te) {
      msg += "\nTransactionException: " + te.getMessage() + "\n\t";
      stack = te.getStackTrace();
      for (StackTraceElement s : stack) {
        msg = msg + s.toString() + "\n\t\t";
      }
    } finally {}

    String errorMessageString = "Error in VerifyUserEmails";
    if ( !errorMessageString.equals( "" )) {
      errorMessageString += "\n";
    }
    errorMessageString += msg;

    System.err.println(errorMessageString);

    if (sess != null) {

      String toAddress = gnomexSupportEmail;
      if (testEmailAddress.length() > 0) {
        toAddress = testEmailAddress;
      }
      System.out.println(toAddress);
      try {
        if (sendMail) {
          MailUtilHelper helper = new MailUtilHelper(
        	  mailProps,
              toAddress,
              null,
              null,
              doNotReplyEmail,
              "VerifyUserEmails Error",
              errorMessageString,
              null,
              true,
              testEmail,
              testEmailAddress
          );
          MailUtil.validateAndSendEmail(helper);
        }
      } catch (Exception e1) {
        System.err.println( "VerifyUserEmails unable to send error report.   " + e1.toString() );
      }
    }
  }

}
