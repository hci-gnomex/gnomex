package hci.gnomex.daemon;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.Property;
import hci.gnomex.utility.MailUtil;
import hci.gnomex.utility.PropertyHelper;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class UploadNotification extends TimerTask {

  private static long                  fONCE_PER_DAY = 1000*60*60*24; // A day in milliseconds
  private static int                   fONE_DAY = 1;
  private static int                   wakeupHour = 2;    // Default wakupHour is 2 am
  private static int                   fZERO_MINUTES = 0;
  
  private Configuration                configuration;
  private Session                      sess;
  private SessionFactory               sessionFactory;

  private String                       gnomex_db_driver;
  private String                       gnomex_db_url;
  private String                       gnomex_db_username;
  private String                       gnomex_db_password;
  
  private static String                serverName = "";
  private static UploadNotification         app = null;
  
  private boolean                      runAsDaemon = false;


  private String                       last_upload_notification = "";
  private String                       contact_email_core_facility = "";
  private Calendar                     asOfDate;

  
  private Properties mailProps;

  public UploadNotification(String[] args) {
    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("-wakeupHour")) {
        wakeupHour = Integer.valueOf(args[++i]);
      } else if (args[i].equals ("-runAsDaemon")) {
        runAsDaemon = true;
      } else if (args[i].equals ("-server")) {
        serverName = args[i++];
      }
    } 
    setMailProps();
  }
  
  /**
   * @param args
   */
  public static void main(String[] args) {
    app  = new UploadNotification(args);
    
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

    try {
      app.connect();  
      
      PropertyHelper ph = PropertyHelper.getInstance(sess);
      
      last_upload_notification = ph.getProperty(Property.LAST_UPLOAD_NOTIFICATION);
      
      contact_email_core_facility = ph.getProperty(Property.CONTACT_EMAIL_CORE_FACILITY);
      
      app.sendUploadNotifications();
      
      app.disconnect(); 
      
    } catch (Exception e) {
      System.out.println( e.toString() );
      e.printStackTrace();
    }

      
  }
  
  private void initialize() {

    
    /*
    if(last_upload_notification == null || last_upload_notification.length() == 0) {
      System.out.println("last_upload_notification is undefined.");
      return false;
    }
    
    SimpleDateFormat f = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss:SSS");
    
    System.out.println(last_upload_notification);
    Calendar asOfDate=Calendar.getInstance();
    asOfDate.setTime(f.parse(last_upload_notification));
    
    System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(asOfDate.getTime()));
    */

  }
  
  private void sendUploadNotifications() throws Exception { 
    Connection myConn = null;
    try 
    {
      myConn = sess.connection();
      Statement stmt = myConn.createStatement();
      ResultSet rs = null;
      
      String currentEmail = "";
      String currentLab = "";
      StringBuffer outputStr = new StringBuffer("");
      
      StringBuffer buf = new StringBuffer("select distinct\n");
      buf.append("   substring(tl.fileName, 0, CHARINDEX('/', fileName)) as number,\n");
      buf.append("   isnull(l.lastName, '') + isnull(', ' + l.firstName, '') + ' Lab' as labName,\n");
      buf.append("   au.email\n");
      buf.append(" from TransferLog tl\n");
      buf.append("   join Lab l\n");
      buf.append("     join UploadAlerts ua\n");
      buf.append("       join AppUser au\n");
      buf.append("       on ua.idAppUser = au.idAppUser\n");
      buf.append("     on l.idLab = ua.idLab\n");
      buf.append("   on l.idLab = tl.idLab\n");
      buf.append(" where transferType = 'upload'\n");
      buf.append(" and endDateTime is not null\n");
      buf.append(" and endDateTime > '" + last_upload_notification + "'\n");
      buf.append(" order by email, labName, number");      
      
      rs = stmt.executeQuery(buf.toString());
      while(rs.next()) {
        String thisEmail = rs.getString("email");
        if(thisEmail == null || thisEmail.length() < 3) {
          // Ignore users for whom no email specified
          continue;
        }
        if(thisEmail.compareTo(currentEmail) != 0) {
          if(currentEmail.length() > 0) {
            outputStr.append("</span></td></tr>");  
            sendEmail(currentEmail, outputStr);
          }
          currentEmail = thisEmail;
          outputStr = new StringBuffer("");  
          String thisLab = rs.getString("labName");
          currentLab = thisLab;
          outputStr.append("<tr><td width='250'><span class='fontClass'>" + thisLab + "</span></td><td width='350'><span class='fontClass'>");                    
        } else {
          String thisLab = rs.getString("labName");
          if(thisLab.compareTo(currentLab) != 0) {
            if(currentLab.length() > 0) {
              outputStr.append("</span></td></tr>");              
            }
            outputStr.append("<tr><td width='250'><span class='fontClass'>" + thisLab + "</span></td><td width='350'><span class='fontClass'>");
            currentLab = thisLab;
          }         
        }
        String thisNumber = rs.getString("number");
        outputStr.append(thisNumber + " ");         
      }
      if(currentEmail.length() > 0) {
        outputStr.append("</span></td></tr>");  
        sendEmail(currentEmail, outputStr);
      }
      rs.close();
      stmt.close();       
      
      String endDateTime = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss:SSS").format(Calendar.getInstance().getTime());
      buf = new StringBuffer("update Property\n");
      buf.append(" set propertyValue = '" + endDateTime + "'\n");
      buf.append(" where propertyName = '" + Property.LAST_UPLOAD_NOTIFICATION + "'\n");
      stmt = myConn.createStatement();
      stmt.executeUpdate(buf.toString());
      stmt.close();
      sess.flush();
    }

    catch (Exception ex) {
      throw ex;
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
  
  private void sendEmail(String currentEmail, StringBuffer outputStr) throws Exception {
    
    
    // Build message body in html
    StringBuffer body = new StringBuffer("");
    
    body.append("<html><head><title>Analysis/Experiment Uploads</title><meta http-equiv='content-style-type' content='text/css'></head>");
    body.append("<body leftmargin='0' marginwidth='0' topmargin='0' marginheight='0' offset='0' bgcolor='#FFFFFF'>");
    body.append("<style>.fontClass{font-size:11px;color:#000000;font-family:verdana;text-decoration:none;}");
    body.append(" .fontClassBold{font-size:11px;font-weight:bold;color:#000000;font-family:verdana;text-decoration:none;}");
    body.append(" .fontClassLgeBold{font-size:12px;line-height:22px;font-weight:bold;color:#000000;font-family:verdana;text-decoration:none;}</style>");
    body.append("<table width='100%' cellpadding='10' cellspacing='0' bgcolor='#FFFFFF'><tr><td width='20'>&nbsp;</td><td valign='top' align='left'>");

    body.append("<table cellpadding='5' cellspacing='0' border='1' bgcolor='#EBF2FC'>");
    body.append("<tr><td width='250'><span class='fontClassBold'>Lab</span></td><td width='350'><span class='fontClassBold'>Analysis/Experiment(s)</span></td></tr>");
    body.append(outputStr);
    body.append("</table></td></tr></table></body></html>");    
    
    MailUtil.send(mailProps, currentEmail, "", contact_email_core_facility, "GNomEx Upload Alert", body.toString(), true);      
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
    .setProperty("hibernate.connection.url", this.gnomex_db_url )
    .setProperty("hibernate.cache.provider_class", "org.hibernate.cache.HashtableCacheProvider");


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
  
}
