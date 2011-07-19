/**
 * 
 */
package hci.gnomex.utility;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.Analysis;
import hci.gnomex.model.Request;
import hci.gnomex.model.TransferLog;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * @author Tony Di Sera
 * This is a java main that will be called by a script to 
 * log info about a file upload or download.
 *
 */
public class TransferLoggerMain {
  
  private Configuration   configuration;
  private Session         sess;
  private SessionFactory  sessionFactory;


  private String gnomex_db_driver;
  private String gnomex_db_url;
  private String gnomex_db_username;
  private String gnomex_db_password;
  
  private String      type;
  private String      method;
  private String      fileName;
  private String      startDateTimeStr;
  private String      endDateTimeStr;
  

  /**
   * @param args
   */
  public static void main(String[] args) {

    TransferLoggerMain transferLogger = new TransferLoggerMain(args);
    try {
      transferLogger.connect();
      transferLogger.insertLog();
      transferLogger.disconnect();      
    } catch (Exception e) {
      System.out.println(e.toString());
      e.printStackTrace();
    }
  }
  
  private TransferLoggerMain(String[] args) {
    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("-h")) {
        printUsage();
        return;
      } else if (args[i].equals("-type")) {
        type = args[++i];
      } else if (args[i].equals("-method")) {
        method = args[++i];
      } else if (args[i].equals("-fileName")) {
        fileName = args[++i];
      } 
    }
  }
    
  
  private void printUsage() {
    System.out.println("java hci.gnomex.utility.TransferLogger " + "\n" +
        "-method [http|ftp]" + "\n" +
        "-type [upload|download]" + "\n" + 
        "-fileName");
  }
  
  private void insertLog() throws Exception {
    System.out.println(fileName);
    // Parse the fileName to figure out the analysis or request number
    // which will be the second to last part of the full path
    /*
    String[] tokens = fileName.split("[\\\\|/]+");
    if (tokens.length >= 1) {
      throw new RuntimeException("unexpected file path pattern");
    }
    String simpleFileName = tokens[tokens.length - 1];
    String number = tokens[tokens.length -2];
    
    Integer idLab = null;
    Integer idAnalysis = null;
    Integer idRequest = null;
    if (number.startsWith("A")) {
      List results = sess.createQuery("SELECT a from Analysis where a.number = '" + number + "'").list();
      if (results.size() == 0) {
        throw new Exception("cannot find analysis " + number);
      }
      Analysis a = (Analysis)results.get(0);
      idLab = a.getIdLab();
    } else {
      List results = sess.createQuery("SELECT r from Request where r.number = '" + number + "'").list();
      if (results.size() == 0) {
        throw new Exception("cannot find experiment " + number);
      }
      Request r = (Request)results.get(0);
      idLab = r.getIdLab();
    }
    
    TransferLog xferLog = new TransferLog();
    xferLog.setTransferType(type);
    xferLog.setTransferMethod(method);
    xferLog.setFileName(number + simpleFileName);

    */
    
  }
  


  private void connect() throws Exception
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

  private void disconnect() throws Exception {
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
        if (e.getAttributeValue("name") != null && e.getAttributeValue("name").equals("GNOMEX_GUEST")) {
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
