package hci.gnomex.security;

import java.math.BigInteger;
import java.security.SecureRandom;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class EncryptionUtility {
  
  private static int ITERATIONS = 1000;
  private static int KEY_LENGTH = 192;
  
  public EncryptionUtility() {
    
  }
  
  public String createSalt() {
    SecureRandom srandom = new SecureRandom();
    byte[] data = new byte[64];
    srandom.nextBytes(data);
    return String.format("%x", new BigInteger(data));
  }
  
  public String createPassword(String password, String salt) {
    char[] passwordChars = password.toCharArray();
    byte[] saltBytes = salt.getBytes();
    
    PBEKeySpec spec = new PBEKeySpec(passwordChars, saltBytes, EncryptionUtility.ITERATIONS, EncryptionUtility.KEY_LENGTH);
    
    byte[] hashedPassword = null;
    try {
      SecretKeyFactory key = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
      hashedPassword = key.generateSecret(spec).getEncoded();

    } catch(Exception ioex) {
      System.out.println(ioex.getMessage());
    }
    
    return String.format("%x", new BigInteger(hashedPassword));
 
  }

}
