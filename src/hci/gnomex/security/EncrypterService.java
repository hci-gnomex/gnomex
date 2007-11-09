package hci.gnomex.security;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import sun.misc.BASE64Encoder;
import sun.misc.CharacterEncoder;

public final class EncrypterService
{
  private static EncrypterService instance;
  private EncrypterService()
  {
  }
  
  public synchronized String encrypt(String plaintext) 
  {
    if (plaintext == null || plaintext.equals("")) {
      return plaintext;
    }
    
    MessageDigest md = null;
    try
    {
      md = MessageDigest.getInstance("SHA"); //step 2
    }
    catch(NoSuchAlgorithmException e)
    {
      throw new RuntimeException(e.getMessage());
    }
    try
    {
      md.update(plaintext.getBytes("UTF-8")); //step 3
    }
    catch(UnsupportedEncodingException e)
    {
      throw new RuntimeException(e.getMessage());
    }
    byte raw[] = md.digest(); //step 4
    String hash = (new BASE64Encoder()).encode(raw); //step 5
    return hash; //step 6
  }

  
  public static synchronized EncrypterService getInstance() //step 1
  {
    if(instance == null)
    {
      return new EncrypterService();
    } 
    else    
    {
      return instance;
    }
  }
}
