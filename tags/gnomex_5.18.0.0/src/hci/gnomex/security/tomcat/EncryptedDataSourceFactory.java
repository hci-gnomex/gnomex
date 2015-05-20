package hci.gnomex.security.tomcat;
import hci.gnomex.controller.CheckDataTrackPermission;
import hci.gnomex.utility.TomcatCatalinaProperties;

import java.io.File;
import java.io.FileInputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Properties;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.naming.Context;
import javax.sql.DataSource;

import org.apache.tomcat.jdbc.pool.DataSourceFactory;
import org.apache.tomcat.jdbc.pool.PoolConfiguration;
import org.apache.tomcat.jdbc.pool.XADataSource;

public class EncryptedDataSourceFactory extends DataSourceFactory {       
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CheckDataTrackPermission.class);
  
  private static TomcatCatalinaProperties catalinaProperties = null;
  
  public EncryptedDataSourceFactory() {         
  }       
  
  @Override    
  public DataSource createDataSource(Properties properties, Context context, boolean XA) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, SQLException, NoSuchAlgorithmException, NoSuchPaddingException {
    org.apache.tomcat.jdbc.pool.DataSource dataSource = null;
    try {
      if (catalinaProperties == null) {
        synchronized(this) {
          if (catalinaProperties == null) {
            String catalinaPath = System.getProperty("catalina.base") + "/conf/catalina.properties";
            catalinaProperties = new TomcatCatalinaProperties(catalinaPath);
          }
        }
      }
      
      if (catalinaProperties.getTomcatPropertyToken(TomcatCatalinaProperties.GNOMEX_AES_KEY) == null) {
        log.error("Unable to get key property in EncryptedDataSourceFactory class");
      }
      
      // Here we decrypt our password.         
      PoolConfiguration poolProperties = EncryptedDataSourceFactory.parsePoolProperties(properties);  
      poolProperties.setPassword(catalinaProperties.decryptPassword(poolProperties.getPassword()));          
      
      // The rest of the code is copied from Tomcat's DataSourceFactory.         
      if (poolProperties.getDataSourceJNDI() != null && poolProperties.getDataSource() == null) {             
        performJNDILookup(context, poolProperties);         
      }         
      dataSource = XA ? new XADataSource(poolProperties) : new org.apache.tomcat.jdbc.pool.DataSource(poolProperties);         
      dataSource.createPool();         
    } catch(Exception e) {
      log.fatal("Error instantiating EncryptedDataSourceFactory class.", e);             
      throw new RuntimeException(e);         
    }     
    return dataSource;     
  }   
} 
