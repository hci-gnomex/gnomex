package hci.gnomex.daemon;

import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.Analysis;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.Property;
import hci.gnomex.model.Request;
import hci.gnomex.model.Visibility;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.MailUtil;
import hci.gnomex.utility.PropertyHelper;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import javax.mail.*;
import javax.mail.internet.*;

import javax.mail.MessagingException;
import javax.naming.NamingException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class DatasetExpirationCheckd extends TimerTask {

  private static long fONCE_PER_DAY = 1000*60*60*24; // A day in milliseconds
  
  private static int fONE_DAY = 1;
  private static int wakeupHour = 2;    // Default wakupHour is 2 am
  private static int fZERO_MINUTES = 0;
  
  private int expWarningDays;
  
  private Configuration   configuration;
  private Session         sess;
  private SessionFactory  sessionFactory;


  private String gnomex_db_driver;
  private String gnomex_db_url;
  private String gnomex_db_username;
  private String gnomex_db_password;
  
  private String serverName;
  
  private ArrayList<String> waList; 
  
  private PropertyHelper propertyHelper; 
  
  private static DatasetExpirationCheckd app;
  
  private Properties mailProps;
  
  private boolean runAsDaemon = false;

  
  public DatasetExpirationCheckd(String[] args) {
    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("-server")) {
        serverName = args[++i];
      } else if (args[i].equals("-wakeupHour")) {
        wakeupHour = Integer.valueOf(args[++i]);
      } else if (args[i].equals ("-runAsDaemon")) {
        runAsDaemon = true;
      }
    } 


    setMailProps(); 
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

      app.connect();
      
      propertyHelper = PropertyHelper.getInstance(sess);
      String datasetPrivacyExp = propertyHelper.getQualifiedProperty(Property.DATASET_PRIVACY_EXPIRATION, serverName);
      if(datasetPrivacyExp == null || datasetPrivacyExp.length() == 0) {
        System.out.println("Feature disabled: DATASET_PRIVACY_EXPIRATION has not been set.");        
        app.disconnect();
        return;
      }
      
      String datasetPrivacyExpWarning = propertyHelper.getQualifiedProperty(Property.DATASET_PRIVACY_EXPIRATION_WARNING, serverName);
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
      
      changeVisibilityIfExpired(sess);
      
    
      app.disconnect();      
         
    } catch (Exception e) {
      System.out.println( e.toString() );
      e.printStackTrace();
    }

  }
  
  private void emailExpirationWarning(String emailTo, Analysis a) throws AddressException, NamingException, MessagingException {
    // Build message body in html
    StringBuffer body = new StringBuffer("");
    
    
    
    //if (submitterEmail.equals(dictionaryHelper.getProperty(Property.CONTACT_EMAIL_SOFTWARE_TESTER))) {
    
    String contactEmailSoftwareTester = propertyHelper.getQualifiedProperty(Property.CONTACT_EMAIL_SOFTWARE_TESTER, serverName);
    

    
    body.append("<html><head><title>Restricted Visiblity Expiration</title><meta http-equiv='content-style-type' content='text/css'></head>");
    body.append("<body leftmargin='0' marginwidth='0' topmargin='0' marginheight='0' offset='0' bgcolor='#FFFFFF'>");
    body.append("<style>.fontClass{font-size:11px;color:#000000;font-family:verdana;text-decoration:none;}");
    body.append(" .fontClassBold{font-size:11px;font-weight:bold;color:#000000;font-family:verdana;text-decoration:none;}");
    body.append(" .fontClassLgeBold{font-size:12px;line-height:22px;font-weight:bold;color:#000000;font-family:verdana;text-decoration:none;}</style>");
    body.append("<table width='400' cellpadding='0' cellspacing='0' bgcolor='#FFFFFF'><tr><td width='10'>&nbsp;</td><td valign='top' align='left'>");
    body.append("This is a courtesy notification to inform you that visiblity for analysis " + a.getNumber() + " is set to expire in " + this.expWarningDays + " days.");
    body.append(" If you do not change the visibility expiration date, visibility will change to " + Visibility.VISIBLE_TO_PUBLIC + " at that time.<br><br>");
    body.append(" You are receiving this notice because you and/or your group is listed as owner.");
    
    if(emailTo.contains(contactEmailSoftwareTester)) {
      // If the software tester email is present then send only to the tester
      body.append("<br><br><br>Sent to software tester. Ordinarily this would have been sent to: " + emailTo);
      emailTo = contactEmailSoftwareTester;
    }       
    
    body.append("</td></tr></table></body></html>");
    MailUtil.send(mailProps, emailTo, "", "DoNotReply@hci.utah.edu", "Restricted visibility expiration", body.toString(), true);  
  }
  
  private void setMailProps() {
    // Set up mail properties
    boolean foundHost = false;
    mailProps = new Properties();
    File serverFile = new File("../../" + Constants.SERVER_FILE);
    if(serverFile.exists()) {
      try {
        SAXBuilder builder = new SAXBuilder();
        // Just in case the orion site is down, we don't want 
        // to fail because the dtd is unreachable 
        builder.setEntityResolver(new DummyEntityRes());
        
        org.jdom.Document doc = builder.build(serverFile);
        Element root = doc.getRootElement();
        
        Element mailElement = root.getChild("mail-session");
        if(mailElement != null) {
          if (mailElement.getAttributeValue("smtp-host") != null) {
            foundHost = true;
            mailProps.put("mail.smtp.host", mailElement.getAttributeValue("smtp-host"));
            Iterator i = mailElement.getChildren("property").iterator();
            while (i.hasNext()) {
              Element e = (Element) i.next();
              if (e.getAttributeValue("name") != null && e.getAttributeValue("value") != null) {
                mailProps.put(e.getAttributeValue("name"), e.getAttributeValue("value"));
              }
            }

          } 
          
        }

      } catch (JDOMException e) {
        System.out.println( e.toString() );
        e.printStackTrace();
      }
    }
    if (!foundHost) {
      // Set this as default if smtp-host not found
      mailProps.put("mail.smtp.host", "hci-mail.hci.utah.edu");  
    }    
  }
  
  private void sendExpirationWarnings(Session sess) throws Exception{
    if(expWarningDays <= 0) {
      return; // Nothing to do if exp warning days <= 0
    }
    
    try {
      StringBuffer queryBuf = new StringBuffer();
      
      queryBuf.append(" SELECT a");
      queryBuf.append(" from Analysis a");
      queryBuf.append(" where ");
      queryBuf.append("   DATEDIFF(day, getDate(), privacyExpirationDate) = " + expWarningDays);
      queryBuf.append("   and codeVisibility <> '" + Visibility.VISIBLE_TO_PUBLIC + "'");
      
      List aItems = (List)sess.createQuery(queryBuf.toString()).list();

      
      for(Iterator<Analysis> i = aItems.iterator(); i.hasNext();) {
        Analysis a = i.next();
        
        String ownerEmail = "";
        if(a.getAppUser() != null) {
          ownerEmail = a.getAppUser().getEmail();
          if (ownerEmail == null || ownerEmail.equals("")) {
            ownerEmail = "";
          }
        }
        
        String managerEmails = "";
        if(a.getLab() != null && a.getLab().getManagers() != null) {
          for(Iterator i1 = a.getLab().getManagers().iterator(); i1.hasNext();) {
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
        if(a.getAppUser() != null) {
          labEmail = a.getLab().getContactEmail();
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
        
        if(emailTo.length() > 0) {
          emailExpirationWarning(emailTo, a);
        }
      }         
    }

    catch (Exception ex) {
      System.out.println("Error sending restricted visibility warning: "  + ex.getMessage());
      throw new RollBackCommandException();
    }        
  }  

  
  private void changeVisibilityIfExpired(Session sess) throws Exception{
    try {
      StringBuffer queryBuf = new StringBuffer();
      
      queryBuf.append(" SELECT a");
      queryBuf.append(" from Analysis a");
      queryBuf.append(" where ");
      queryBuf.append("   privacyExpirationDate is not null and ");
      queryBuf.append("   privacyExpirationDate <= GETDATE() and");
      queryBuf.append("   codeVisibility <> '" + Visibility.VISIBLE_TO_PUBLIC + "'");
      
      List aItems = (List)sess.createQuery(queryBuf.toString()).list();
      
      Transaction trans = sess.beginTransaction();
      
      for(Iterator<Analysis> i = aItems.iterator(); i.hasNext();) {
        Analysis a = i.next();
        //System.out.println("" + a.getIdAnalysis() + " " + a.getCodeVisibility());
        a.setCodeVisibility(Visibility.VISIBLE_TO_PUBLIC);
        sess.save(a);
        sess.flush();   
      }      
      trans.commit();     
    }

    catch (Exception ex) {
      System.out.println("Error setting dataset visibility: " + ex.getMessage());
      throw new RollBackCommandException();
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
    registerDataSources(new File("../../" + Constants.DATA_SOURCES));

    configuration = new Configuration()
    .addFile("SchemaGNomEx.hbm.xml");


    configuration.setProperty("hibernate.query.substitutions", "true 1, false 0, yes 'Y', no 'N'")
    .setProperty("hibernate.connection.driver_class", this.gnomex_db_driver)
    .setProperty("hibernate.connection.username", this.gnomex_db_username)
    .setProperty("hibernate.connection.password", this.gnomex_db_password)
    //.setProperty("hibernate.dialect", "org.hibernate.dialect.SQLServerDialect")
    //.setProperty("hibernate.show_sql", "true" )
    //.setProperty("hibernate.cache.provider_class", "org.hibernate.cache.HashtableCacheProvider")
    .setProperty("hibernate.connection.url", this.gnomex_db_url );

    sessionFactory = configuration.buildSessionFactory();
    sess = sessionFactory.openSession();
  }
  
  private void disconnect() 
  throws Exception {
    sess.close();
  }   
  
  private void registerDataSources(File xmlFile) {
    if(xmlFile.exists()) {
      try {
        SAXBuilder builder = new SAXBuilder();
        // Just in case the orion site is down, we don't want 
        // to fail because the dtd is unreachable 
        builder.setEntityResolver(new DummyEntityRes());
        
        org.jdom.Document doc = builder.build(xmlFile);
        this.registerDataSources(doc);
      } catch (JDOMException e) {
      }
    }
  }

  private void registerDataSources(org.jdom.Document doc) {
    Element root = doc.getRootElement();
    if (root.getChildren("data-source") != null) {
      Iterator i = root.getChildren("data-source").iterator();
      while (i.hasNext()) {
        Element e = (Element) i.next();
        if (e.getAttributeValue("name") != null && e.getAttributeValue("name").equals("GNOMEX")) {
          this.gnomex_db_driver = e.getAttributeValue("connection-driver");
          this.gnomex_db_url = e.getAttributeValue("url");
          this.gnomex_db_password = e.getAttributeValue("password");
          this.gnomex_db_username = e.getAttributeValue("username");
        } 
      }
    }
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
