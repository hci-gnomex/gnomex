package hci.gnomex.utility;

import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.URLName;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javax.naming.InitialContext;
import javax.naming.NamingException;


public class MailUtil
{

  //~ Static fields/initializers ---------------------------------------------
    public static final String MAIL_SESSION = "mail/MailSession";

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

        InitialContext ic = new InitialContext(  );
        Session session = ( Session ) ic.lookup( MAIL_SESSION );
        send(session, to, cc, from, subject, body, formatHtml);        

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
      send(session, to, cc, from, subject, body, formatHtml);

    }
    
    public static void send(Session session,
        String to,
        String cc,
        String from,
        String subject,
        String body,
        boolean formatHtml )
    throws NamingException,
    AddressException,
    MessagingException {

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
