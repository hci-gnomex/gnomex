package hci.gnomex.daemon;

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

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.TransactionException;

public class RemoveUserEmails {

  private static RemoveUserEmails         app = null;

  private Transaction                     tx;

  private Properties                      mailProps;
  private Boolean                         sendMail = true;
  private String                          orionPath = "";
  private Session                         sess;
  private BatchDataSource                 dataSource;
  private int                             changeCount = 0;
  private String                          fromEmailAddress;
  private String                          testEmailAddress = "";
  private Boolean                         testEmail;
  private String                          gnomexSupportEmail = "";


  public RemoveUserEmails(String args[]) {
    for(int i = 0; i < args.length; i++) {
      if(args[i].equals("-doNotSendEmail")) {
        sendMail = false;
      } else if (args[i].equals ("-testEmailAddress")) {
        testEmailAddress = args[++i];
      } else if (args[i].equals ("-orionPath")) {
        orionPath = args[++i];
      } else if(args[i].equals("-help")) {
        showHelp();
        System.exit(0);
      }
    }

  }

  private void showHelp() {
    System.out.println("Set user emails to NULL who have not verified.");
    System.out.println("Switches:");
    System.out.println("   -orionPath - path to Orion directory to get mail properties.");
    System.out.println("   -doNotSendEmail - used for debugging if no email server available.  Emails are created but not sent.");
    System.out.println("   -testEmailAddress - Overrides all lab emails with this email address.  Used for testing.");
    System.out.println("   -help - gives this message.  Note no other processing is performed if the -help switch is specified.");
  }

  public static void main(String[] args) {
    app  = new RemoveUserEmails(args);
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
      this.fromEmailAddress = ph.getProperty(PropertyDictionary.GENERIC_NO_REPLY_EMAIL);
      testEmail = testEmailAddress.length() > 0;

      List appUsers = sess.createQuery("Select au from AppUser au").list();

      tx = sess.beginTransaction();

      for(Iterator i = appUsers.iterator(); i.hasNext();) {
        AppUser au = (AppUser)i.next();
        if(au.getConfirmEmailGuid() !=  null) {
          au.setEmail(null);
          au.setConfirmEmailGuid(null);
          au.setIsActive("N");
          changeCount++;
          sess.save(au);
        }
      }

      sess.flush();

      sendConfirmationEmail();

      tx.commit();

    }catch(Exception e) {
      sendErrorReport(e);

    }finally {
      if (sess != null) {
        try {
          app.disconnect();
        } catch(Exception e) {
          System.err.println( "RemoveUserEmails unable to disconnect from hibernate session.   " + e.toString() );
        }
      }

    }
    
    System.exit(0);

  }

  private void sendConfirmationEmail() {
    if (sendMail) {
      String toAddress = gnomexSupportEmail;
      if (testEmailAddress.length() > 0) {
        toAddress = testEmailAddress;
      }

      StringBuffer body = new StringBuffer();
      body.append("The RemoveUserEmails cron job completed successfully.  " + changeCount + " user emails were set to NULL.");


      try {
        MailUtilHelper helper = new MailUtilHelper(
        	mailProps,
            toAddress,
            null,
            null,
            fromEmailAddress,
            "RemoveUserEmails Complete",
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
      try {
        if (sendMail) {
          MailUtilHelper helper = new MailUtilHelper(
        	  mailProps,
              toAddress,
              null,
              null,
              fromEmailAddress,
              "VerifyUserEmails Error",
              errorMessageString,
              null,
              false,
              testEmail,
              testEmailAddress
          );
          MailUtil.validateAndSendEmail(helper);
        }
      } catch (Exception e1) {
        System.err.println( "RemoveUserEmails unable to send error report.   " + e1.toString() );
      }
    }
  }

}
