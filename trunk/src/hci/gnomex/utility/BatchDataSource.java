package hci.gnomex.utility;

import hci.framework.model.DetailObject;
import hci.gnomex.security.tomcat.AESEncryption;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class BatchDataSource extends DetailObject {
  
  
  private String          gnomex_db_driver;
  private String          gnomex_db_url;
  private String          gnomex_db_username;
  private String          gnomex_db_password;

  private Configuration   configuration;
  private Session         sess;
  private SessionFactory  sessionFactory;
  private String          specifiedOrionPath = "";
  private String          specifiedSchemaPath = "";
  
  private TomcatCatalinaProperties catalinaProperties;
  
  
  public BatchDataSource() {
    specifiedOrionPath = "";
    specifiedSchemaPath = "";
  }
  
  public BatchDataSource(String orionPath, String schemaPath) {
    specifiedOrionPath = orionPath;
    specifiedSchemaPath = schemaPath;
  }
  
  public Session connect() throws Exception {
    String filePath = "../";
    if(specifiedOrionPath.length() > 0) {
      filePath = specifiedOrionPath;
    }
    filePath = filePath + "META-INF/context.xml";
    File file = new File(filePath);

    if (file.exists()) {
      return connectTomcat(file);
    } else {
      filePath = "../../";
      if(specifiedOrionPath.length() > 0) {
        filePath = specifiedOrionPath;
      }
      filePath = filePath + "config/data-sources.xml";
      file = new File(filePath);
      return connectOrion(file);	
    }
  }

  public Session connectTomcat(File dataSourcesFile) throws Exception {
    catalinaProperties = new TomcatCatalinaProperties(specifiedOrionPath);
    this.registerTomcatDataSources(dataSourcesFile);
    String filePath = "../WEB-INF/classes/";
    if(specifiedSchemaPath.length() > 0) {
      filePath = specifiedSchemaPath;
    }    
    filePath = filePath + "SchemaGNomEx.hbm.xml";
    configuration = new Configuration().addFile(filePath);
    return connectImpl();
  }
  
  public Session connectOrion(File dataSourcesFile) throws Exception {
    this.registerDataSources(dataSourcesFile);
    String filePath = "";
    if(specifiedOrionPath.length() > 0) {
      filePath = specifiedSchemaPath;
    }    
    filePath = filePath + "SchemaGNomEx.hbm.xml";
    configuration = new Configuration().addFile(filePath);
    return connectImpl();
  }
  
  public Session getSession() {
    return sess;
  }

  public Session connectImpl() throws Exception {
    
    configuration.setProperty("hibernate.query.substitutions", "true 1, false 0, yes 'Y', no 'N'")
                 .setProperty("hibernate.connection.driver_class", this.gnomex_db_driver)
                 .setProperty("hibernate.connection.username", this.gnomex_db_username)
                 .setProperty("hibernate.connection.password", this.gnomex_db_password)
                 .setProperty("hibernate.connection.url", this.gnomex_db_url )
                 .setProperty("hibernate.cache.provider_class", "org.hibernate.cache.HashtableCacheProvider");
    
    sessionFactory = configuration.buildSessionFactory();
    sess = sessionFactory.openSession();    
    return sess;
  }
  
  public void close() throws Exception {
    if (sess != null) {
      sess.close();
    }
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

  private void registerTomcatDataSources(File xmlFile) throws Exception {
    if(xmlFile.exists()) {
      try {
        SAXBuilder builder = new SAXBuilder();
        // Just in case the orion site is down, we don't want 
        // to fail because the dtd is unreachable 
        builder.setEntityResolver(new DummyEntityRes());
        
        org.jdom.Document doc = builder.build(xmlFile);
        this.registerTomcatDataSources(doc);
      } catch (JDOMException e) {
      }
    }
  }


  private void registerTomcatDataSources(org.jdom.Document doc) throws Exception {
    Element root = doc.getRootElement();
    Iterator i = root.getChildren("Resource").iterator();
    while (i.hasNext()) {
      Element e = (Element) i.next();
      if (e.getAttributeValue("name") != null && e.getAttributeValue("name").equals("jdbc/GNOMEX")) {
        gnomex_db_driver = catalinaProperties.getTomcatPropertyToken(e.getAttributeValue("driverClassName"));
        gnomex_db_url = catalinaProperties.getTomcatPropertyToken(e.getAttributeValue("url"));
        gnomex_db_password = catalinaProperties.decryptPassword(catalinaProperties.getTomcatPropertyToken(e.getAttributeValue("password")));
        gnomex_db_username = catalinaProperties.getTomcatPropertyToken(e.getAttributeValue("username"));
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
