package hci.gnomex.security.tomcat;
import hci.gnomex.controller.CheckDataTrackPermission;

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
  private AESEncryption encryptor = null;       
  private String propertyPath = null;
  
  public String getPropertyPath() {
    return propertyPath;
  }
  
  public void setPropertyPath(String propertyPath) {
    this.propertyPath = propertyPath;
  }
  
  public EncryptedDataSourceFactory() {         
  }       
  
  @Override    
  public DataSource createDataSource(Properties properties, Context context, boolean XA) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, SQLException, NoSuchAlgorithmException, NoSuchPaddingException {
    org.apache.tomcat.jdbc.pool.DataSource dataSource = null;
    try {
      String key = null;
      try {
        File file = new File("/properties/gnomex_tomcat.properties");
        FileInputStream fis = new FileInputStream(file);
        Properties p = new Properties();
        p.load(fis);
        if (p.getProperty("key") != null) {
          key = p.getProperty("key");
          
        }
      } catch(Exception ex) {
        log.error("Unable to get key property in EncryptedDataSourceFactory class", ex);
      }
      
      AESEncryption encryptor = new AESEncryption();
      if (key != null) {
        encryptor = new AESEncryption(key);
      }
      // Here we decrypt our password.         
      PoolConfiguration poolProperties = EncryptedDataSourceFactory.parsePoolProperties(properties);  
      poolProperties.setPassword(encryptor.decrypt(poolProperties.getPassword()));          
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
