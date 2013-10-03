package hci.gnomex.utility;

import hci.gnomex.security.tomcat.AESEncryption;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class TomcatCatalinaProperties {
  private static final String         GNOMEX_AES_KEY = "gnomex.aes.key"; 

  private String specifiedOrionPath;
  private Properties catalinaProperties;
  
  public TomcatCatalinaProperties(String specifiedOrionPath) throws IOException {
    this.specifiedOrionPath = specifiedOrionPath == null ? "" : specifiedOrionPath;
    getCatalinaProperties();
  }
  
  private void getCatalinaProperties() throws IOException {
    String filePath = specifiedOrionPath + "../../../conf/catalina.properties";
    catalinaProperties = new Properties();
    catalinaProperties.load(new FileInputStream(filePath));
    
    filePath = "/properties/gnomex_tomcat.properties";
    File f = new File(filePath);
    if (f.exists()) {
      Properties p1 = new Properties();
      p1.load(new FileInputStream(filePath));
      catalinaProperties.put(GNOMEX_AES_KEY, p1.getProperty("key"));
    }
  }
  
  public String getTomcatPropertyToken(String inputToken) {
    String outputToken = inputToken;
    if (inputToken != null && inputToken.startsWith("${") && inputToken.endsWith("}")) {
      outputToken = catalinaProperties.getProperty(inputToken.substring(2, inputToken.length() - 1));
    }
    return outputToken;
  }
  
  public String decryptPassword(String encryptedPassword) throws Exception {
    String key = catalinaProperties.getProperty(GNOMEX_AES_KEY);
    String decryptedPassword = encryptedPassword;
    if (key != null) {
      AESEncryption e = new AESEncryption(key);
      decryptedPassword = e.decrypt(encryptedPassword);
    }
    return decryptedPassword;
  }

}
