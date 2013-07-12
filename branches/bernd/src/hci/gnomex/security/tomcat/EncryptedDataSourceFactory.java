package hci.gnomex.security.tomcat;
import hci.gnomex.controller.CheckDataTrackPermission;

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
  
  public EncryptedDataSourceFactory() {         
    try {             
      encryptor = new AESEncryption(); // If you've used your own secret key, pass it in...         
    } catch (Exception e) {             
      log.fatal("Error instantiating EncryptedDataSourceFactory class.", e);             
      throw new RuntimeException(e);         
    }     
  }       
  
  @Override    
  public DataSource createDataSource(Properties properties, Context context, boolean XA) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, SQLException, NoSuchAlgorithmException, NoSuchPaddingException {         
    // Here we decrypt our password.         
    PoolConfiguration poolProperties = EncryptedDataSourceFactory.parsePoolProperties(properties);         
    poolProperties.setPassword(encryptor.decrypt(poolProperties.getPassword()));          
    // The rest of the code is copied from Tomcat's DataSourceFactory.         
    if (poolProperties.getDataSourceJNDI() != null && poolProperties.getDataSource() == null) {             
      performJNDILookup(context, poolProperties);         
    }         
    org.apache.tomcat.jdbc.pool.DataSource dataSource = XA ? new XADataSource(poolProperties) : new org.apache.tomcat.jdbc.pool.DataSource(poolProperties);         
    dataSource.createPool();           
    return dataSource;     
  }   
} 
