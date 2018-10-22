package hci.gnomex.utility;

import hci.gnomex.security.tomcat.AESEncryption;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class TomcatCatalinaProperties {
  public static final String         GNOMEX_AES_KEY = "gnomex.aes.key"; 
  private static final String         GNOMEX_PROPERTIES_PATH = "gnomex.properties_path";

  private String filePath;
  private Properties catalinaProperties;
  
  public TomcatCatalinaProperties(String filePath) throws IOException {
    getCatalinaProperties(filePath);
  }
  
  public static String getCatalinaPropertiesPathFromScripts(String specifiedOrionPath) {
    specifiedOrionPath = specifiedOrionPath == null ? "" : specifiedOrionPath;
    specifiedOrionPath = specifiedOrionPath.length()==0?"":specifiedOrionPath + "scripts/";
    String filePath = specifiedOrionPath + "../../../conf/catalina.properties";
    return filePath;
  }
  
  private void getCatalinaProperties(String filePath) throws IOException {
    catalinaProperties = new Properties();
    catalinaProperties.load(new FileInputStream(filePath));
    
    filePath = "/properties/gnomex_tomcat.properties";
    if (catalinaProperties.getProperty(GNOMEX_PROPERTIES_PATH) != null) {
      filePath = catalinaProperties.getProperty(GNOMEX_PROPERTIES_PATH);
    }
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
//    System.out.println ("[decryptPassword] key: " + key);
    String decryptedPassword = encryptedPassword;
    if (key != null) {
      AESEncryption e = new AESEncryption(key);
      decryptedPassword = e.decrypt(encryptedPassword);
//        System.out.println ("[decryptPassword] encryptedPassword: " + encryptedPassword + " decryptedPassword: " + decryptedPassword  );

    }
    return decryptedPassword;
  }

}
