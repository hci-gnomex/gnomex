package hci.gnomex.daemon;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.utility.BatchDataSource;
import hci.gnomex.utility.BatchMailer;
import hci.gnomex.utility.MailUtil;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.naming.NamingException;

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
  private Boolean                         testEmail;
  private String                          serverName = null;
  private String                          gnomexSupportEmail = "";
  private String                          imageLoc = "";
  private String                          portNumber = "";
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
      testEmail = !ph.isProductionServer(serverName);
      portNumber = PropertyDictionaryHelper.getInstance(sess).getQualifiedProperty(PropertyDictionary.HTTP_PORT, serverName);
      if(portNumber == null) {
        portNumber = "";
      }else {
        portNumber = ":" + portNumber;
      }
      imageLoc = "http://" + serverName + portNumber + "/gnomex/" + ph.getProperty(PropertyDictionary.SITE_LOGO);
      System.out.println(imageLoc); //DEBUG
      imageLoc = imageLoc.replace("assets/", "images/");
      System.out.println(imageLoc); //DEBUG

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
  }

  private void sendCompleteEmail() {
    if (sendMail) {
      String toAddress = gnomexSupportEmail;
      if (testEmailAddress.length() > 0) {
        toAddress = testEmailAddress;
      }

      StringBuffer body = new StringBuffer();
      body.append("The Verify User Email cron job completed successfully.  There were " + noEmailCount + " users without an email on file.\n\n");
      body.append("Gnomex sent verifification emails to " + successfulSendCount + " users.");


      try {
        MailUtil.sendCheckTest( mailProps,
            toAddress,
            null,
            doNotReplyEmail,
            "VerifyUserEmails Complete",
            body.toString(),
            false,
            testEmail,
            testEmailAddress
        );
      } catch (Exception e) {
        System.err.println("Unable to send confirmation email to gnomex support staff");
      }
    }

  }

  private void sendEmail(AppUser au) {

    if (sendMail) {
      String approveURL = protocol + serverName + portNumber + "/gnomex" + Constants.VERIFY_EMAIL_SERVLET + "?guid=" + au.getConfirmEmailGuid() + "&idAppUser=" + au.getIdAppUser().intValue();

      StringBuffer body = new StringBuffer();
      body.append("<img src=" + imageLoc + ">");
      body.append("<p>This email is being sent out to verify that the email address we have on file for you is correct.  Please click the link below to verify your email address." +
          "  If this is no longer the best email to contact you at, please take a moment to update your gnomex acccount.  If the link below has not been visited within 7 (seven) days," +
      " we will automatically remove this email address from your user account.  Thanks!</p><br><br>");

      body.append("<a href=" + approveURL + ">Verify Email </a> <br><br>-GNomEx Support Team" );

      body.append("<br><br>  Questions?  Email us at: " + gnomexSupportEmail);

      try {
        MailUtil.sendCheckTest( mailProps,
            au.getEmail(),
            null,
            doNotReplyEmail,
            "Please verify your GNomEx contact email address",
            body.toString(),
            true,
            testEmail,
            testEmailAddress
        );
      } catch(Exception ex) {
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
      try {
        if (sendMail) {
          MailUtil.sendCheckTest( mailProps,
              toAddress,
              null,
              doNotReplyEmail,
              "VerifyUserEmails Error",
              errorMessageString,
              true,
              testEmail,
              testEmailAddress
          );
        }
      } catch (Exception e1) {
        System.err.println( "VerifyUserEmails unable to send error report.   " + e1.toString() );
      }
    }
  }

}
