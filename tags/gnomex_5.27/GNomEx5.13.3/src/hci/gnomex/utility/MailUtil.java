package hci.gnomex.utility;

import hci.gnomex.controller.GNomExFrontController;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.URLName;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.naming.NamingException;

import org.apache.commons.validator.routines.EmailValidator;


public class MailUtil
{

    //~ Methods ----------------------------------------------------------------
    public static void send( String to,
                             String cc,
                             String from,
                             String subject,
                             String body,
                             boolean formatHtml )
        throws NamingException,
               AddressException,
               MessagingException {

        
      Session session = GNomExFrontController.getMailSession();
      send(session, to, cc, "", from, subject, body, formatHtml);        

    }
    
    public static Boolean isValidEmail(String emailAddress){
      if (emailAddress == null || emailAddress.equals("")) {
        return false;
      }
      
      String[] tokens = null;
      tokens = emailAddress.split("[,; ]");
      if (tokens != null && tokens.length > 0) {
        for (int x = 0; x < tokens.length; x++) {
          if(!tokens[x].equals("")){
            if(!isValidEmailImpl(tokens[x].trim())){
              return false;
            }
          }
        }
      } else{
        return isValidEmailImpl(emailAddress.trim());
      }
      
      return true;
    }
    
    private static Boolean isValidEmailImpl(String emailAddress){
      EmailValidator ev = EmailValidator.getInstance();
      if(ev.isValid(emailAddress)){
        return true;
      }   
      return false;
    }

    
    public static void send_bcc( String to,
              String cc,
              String from,
              String subject,
              String body,
              boolean formatHtml )
      throws NamingException,
      AddressException,
      MessagingException {
      
      
      Session session = GNomExFrontController.getMailSession();
      send(session, to, cc, "", from, subject, body, formatHtml);        

    }
    
    
    public static void send(Properties props,
                            String to,
                            String cc,
                            String from,
                            String subject,
                            String body,
                            boolean formatHtml )
    throws NamingException,
    AddressException,
    MessagingException {

      Session session = javax.mail.Session.getDefaultInstance(props, null);
      send(session, to, cc, "", from, subject, body, formatHtml);

    }
    
    public static void send_bcc(Properties props,
              String to,
              String cc,
              String bcc,
              String from,
              String subject,
              String body,
              boolean formatHtml )
      throws NamingException,
      AddressException,
      MessagingException {
      
      Session session = javax.mail.Session.getDefaultInstance(props, null);
      send(session, to, cc, bcc, from, subject, body, formatHtml);
    }
    
    public static void send(Session session,
        String to,
        String cc,
        String bcc,
        String from,
        String subject,
        String body,
        boolean formatHtml )
    throws NamingException,
    AddressException,
    MessagingException {

      if (session != null) {
        if (session.getProperty("mail.smtp.auth") != null && session.getProperty("mail.smtp.auth").equals("true")) {
          // Fetch user and password
          PasswordAuthentication auth=
            new PasswordAuthentication(
                session.getProperty("mail.smtp.user"),
                session.getProperty("mail.smtp.password"));
  
  
  
          // Build URL for the session's password cache
          URLName url=
            new URLName(
                session.getProperty("mail.transport.protocol"),
                session.getProperty("mail.smtp.host"),
                -1,
                null,
                session.getProperty("mail.smtp.user"),
                null);
          // Fill password cache
          session.setPasswordAuthentication(url,auth);
  
        }
  
  
        javax.mail.Message msg = new MimeMessage( session );
  
  
        msg.setFrom( new InternetAddress( from ) );
        msg.setRecipients( javax.mail.Message.RecipientType.TO, InternetAddress.parse( to, false ) );
        
        if(cc != null){
          msg.setRecipients( javax.mail.Message.RecipientType.CC, InternetAddress.parse( cc, false ) );
        }
        
        if(bcc != null){
          msg.setRecipients( javax.mail.Message.RecipientType.BCC, InternetAddress.parse( bcc, false ) );
        }
        msg.setSubject( subject );
  
        String format = "text/plain";
        if(formatHtml){
          format = "text/html";
        }
  
  
        msg.setDataHandler( new DataHandler( body, format ) );
        msg.setHeader( "X-Mailer", "JavaMailer" );
        msg.setSentDate( new Date(  ) );
  
        Transport.send( msg );
      }
    }
    
    public static void send_attach( String to,
        String cc,
        String from,
        String subject,
        String body,
        boolean formatHtml,
        File   file)
    throws NamingException,
    AddressException,
    MessagingException,
    IOException{


      Session session = GNomExFrontController.getMailSession();
      send_attach(session, to, cc, "", from, subject, body, formatHtml, file);        

    }

    public static void send_attach(Session session,
        String to,
        String cc,
        String bcc,
        String from,
        String subject,
        String body,
        boolean formatHtml,
        File   file)
    throws NamingException,
    AddressException,
    MessagingException,
    IOException{

      if (session != null) {
        if (session.getProperty("mail.smtp.auth") != null && session.getProperty("mail.smtp.auth").equals("true")) {
          // Fetch user and password
          PasswordAuthentication auth=
            new PasswordAuthentication(
                session.getProperty("mail.smtp.user"),
                session.getProperty("mail.smtp.password"));
  
          // Build URL for the session's password cache
          URLName url=
            new URLName(
                session.getProperty("mail.transport.protocol"),
                session.getProperty("mail.smtp.host"),
                -1,
                null,
                session.getProperty("mail.smtp.user"),
                null);
          // Fill password cache
          session.setPasswordAuthentication(url,auth);
  
        }
  
        javax.mail.Message msg = new MimeMessage( session );
  
        msg.setFrom( new InternetAddress( from ) );
        msg.setRecipients( javax.mail.Message.RecipientType.TO, InternetAddress.parse( to, false ) );
        
        if(cc != null){
          msg.setRecipients( javax.mail.Message.RecipientType.CC, InternetAddress.parse( cc, false ) );
        }
        
        if(bcc != null){
          msg.setRecipients( javax.mail.Message.RecipientType.BCC, InternetAddress.parse( bcc, false ) );
        }
        
        msg.setSubject( subject );
  
        String format = "text/plain";
        if(formatHtml){
          format = "text/html";
        }
  
        
        Multipart multipart = new MimeMultipart();
        
        // Email body
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setDataHandler( new DataHandler( body, format ) );
        multipart.addBodyPart( messageBodyPart );
  
        // Add file attachment(s)
        if (file.isDirectory()) {
          String[] fileList = file.list();
          for (int x = 0; x < fileList.length; x++) {
            String fileName = file.getCanonicalPath() + File.separator + fileList[x];
            File f1 = new File(fileName);
            addAttachment( multipart, f1 );
          }
        } else {
          addAttachment( multipart, file );
        }
        
        msg.setHeader( "X-Mailer", "JavaMailer" );
        msg.setSentDate( new Date(  ) );
  
        msg.setContent( multipart );
        
        Transport.send( msg );
      }
    }
    
    private static void addAttachment(Multipart multipart, File file) throws MessagingException, IOException
    {
      if ( multipart== null || file == null || !file.exists() ) {
        return;
      }
        BodyPart messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(file.getCanonicalPath());
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(file.getName());
        multipart.addBodyPart(messageBodyPart);
    }
}
