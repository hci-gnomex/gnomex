package hci.gnomex.utility;

import java.util.Date;

import javax.activation.DataHandler;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
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
               MessagingException{

        InitialContext ic = new InitialContext(  );
        Session session = ( Session ) ic.lookup( MAIL_SESSION );
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
