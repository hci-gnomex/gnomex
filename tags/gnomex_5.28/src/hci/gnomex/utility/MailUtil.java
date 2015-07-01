package hci.gnomex.utility;

import hci.gnomex.controller.GNomExFrontController;
import hci.gnomex.model.PropertyDictionary;

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
    private static void send( String to,
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
    
    private static boolean validateAndSendEmail(	Session session,
    												String to,
    												String cc,
    												String bcc,
    												String from,
    												String subject,
    												String body,
    												boolean formatHtml, 
    												DictionaryHelper dictionaryHelper,
    												String serverName 					) throws AddressException, NamingException, MessagingException {
    	
    	// Get test email information
    	boolean testEmail = false;
    	String testEmailTo = "";
    	
    	if (!dictionaryHelper.isProductionServer(serverName)) {
    		testEmail = true;
    		testEmailTo = dictionaryHelper.getPropertyDictionary(PropertyDictionary.CONTACT_EMAIL_SOFTWARE_TESTER);
    	}
    	
    	// Make sure we have an email address to send to 
        if (to == null || to.equals("")) {
        	if (!testEmail) {
        		sendNoRecipientEmailNotice(	session, 
        									from, 
        									subject, 
        									body, 
        									formatHtml,
        									dictionaryHelper );
        		return false;
        	}
        }
        
        sendCheckTest(	session,
        		to,
        		cc,
        		bcc,
        		from,
        		subject,
        		body,
        		formatHtml,
        		testEmail,
        		testEmailTo	);
        
        return true;
    }
    
    public static boolean validateAndSendEmail( 
    								String to,
    								String cc, 
    								String from, 
    								String subject, 
    								String body,
    								File file,
    								boolean formatHtml, 
    								DictionaryHelper dictionaryHelper,
    								String serverName) 
    										throws AddressException, NamingException, MessagingException, IOException {
    	
    	Session session = GNomExFrontController.getMailSession();
    	return validateAndSendEmail(session, to, cc, null, from, subject, body, file, formatHtml, dictionaryHelper, serverName);
    }
    
	private static boolean validateAndSendEmail(	Session session, 
													String to,
													String cc, 
													String bcc, 
													String from, 
													String subject, 
													String body,
													File file,
													boolean formatHtml, 
													DictionaryHelper dictionaryHelper,
													String serverName	) 
															throws AddressException, NamingException, MessagingException, IOException {

		// Get test email information
		boolean testEmail = false;
		String testEmailTo = "";

		if (!dictionaryHelper.isProductionServer(serverName)) {
			testEmail = true;
			testEmailTo = dictionaryHelper.getPropertyDictionary(PropertyDictionary.CONTACT_EMAIL_SOFTWARE_TESTER);
		}

		// Make sure we have an email address to send to
		if (to == null || to.equals("")) {
			if (!testEmail) {
				sendNoRecipientEmailNotice(	session, 
											from, 
											subject, 
											body,
											file,
											formatHtml, 
											dictionaryHelper);
				return false;
			}
		}

		sendCheckTest(	session, 
						to, 
						cc, 
						bcc, 
						from, 
						subject, 
						body,
						file,
						formatHtml,
						testEmail, 
						testEmailTo);

		return true;
	}
    
    public static boolean validateAndSendEmail(	Properties props,
    											String to,
    											String cc,
            									String bcc,
												String from,
												String subject,
												String body,
												boolean formatHtml, 
												DictionaryHelper dictionaryHelper,
												String serverName 					) throws AddressException, NamingException, MessagingException {

    	Session session = javax.mail.Session.getDefaultInstance(props, null);
    	
    	return validateAndSendEmail(session, to, cc, bcc, from, subject, body, formatHtml, dictionaryHelper, serverName);
    }
    
    public static boolean validateAndSendEmail(	String to,
												String from,
												String subject,
												String body,
												boolean formatHtml, 
												DictionaryHelper dictionaryHelper,
												String serverName 					) throws AddressException, NamingException, MessagingException {
    	
    	Session session = GNomExFrontController.getMailSession();
    	
    	return validateAndSendEmail(session, to, null, null, from, subject, body, formatHtml, dictionaryHelper, serverName);
    }
    
    public static boolean validateAndSendEmail(	String to,
    											String cc,
    											String bcc,
												String from,
												String subject,
												String body,
												boolean formatHtml, 
												DictionaryHelper dictionaryHelper,
												String serverName 					) throws AddressException, NamingException, MessagingException {

    	Session session = GNomExFrontController.getMailSession();

    	return validateAndSendEmail(session, to, cc, bcc, from, subject, body, formatHtml, dictionaryHelper, serverName);
    }
    
    private static void sendNoRecipientEmailNotice(	Session session,
            										String from,
            										String subject,
            										String body,
            										boolean formatHtml,
            										DictionaryHelper dictionaryHelper ) throws AddressException, NamingException, MessagingException {
    	
    	String toAddress = dictionaryHelper.getPropertyDictionary(PropertyDictionary.CONTACT_EMAIL_SOFTWARE_TESTER);
    	subject = "The following email does not have a valid recipient - " + subject;
    	
    	send(	toAddress,
                null,
                from,
                subject,
                body,
                formatHtml );
    }
    
    private static void sendNoRecipientEmailNotice(	Session session,
													String from,
													String subject,
													String body,
													File file,
													boolean formatHtml,
													DictionaryHelper dictionaryHelper ) throws AddressException, NamingException, MessagingException, IOException {

    	String toAddress = dictionaryHelper.getPropertyDictionary(PropertyDictionary.CONTACT_EMAIL_SOFTWARE_TESTER);
    	subject = "The following email does not have a valid recipient - " + subject;

    	send_attach(session, toAddress, null, null, from, subject, body, formatHtml, file);
    }    

    public static void sendCheckTest(Properties props,
                             String to,
                             String cc,
                             String from,
                             String subject,
                             String body,
                             boolean formatHtml, 
                             boolean testEmail,
                             String testEmailTo)
         throws NamingException, AddressException, MessagingException {
      Session session = javax.mail.Session.getDefaultInstance(props, null);
      sendCheckTest(session, to, cc, "", from, subject, body, formatHtml, testEmail, testEmailTo);
    }
    
    public static void sendCheckTest(Properties props,
            				String to,
            				String cc,
            				String bcc,
            				String from,
            				String subject,
            				String body,
            				boolean formatHtml, 
            				boolean testEmail,
            				String testEmailTo)
         throws NamingException, AddressException, MessagingException {
    	Session session = javax.mail.Session.getDefaultInstance(props, null);
    	sendCheckTest(session, to, cc, bcc, from, subject, body, formatHtml, testEmail, testEmailTo);
    }
    
    private static void sendCheckTest(Session session,
                              String to,
                              String cc,
                              String bcc,
                              String from,
                              String subject,
                              String body,
                              boolean formatHtml,
                              boolean testEmail,
                              String testEmailTo)
          throws NamingException, AddressException, MessagingException {

      if (testEmail) {
        subject = "GNomEx Test Email - " + subject;
        
        // Add details about who the email would have gone to
        String newBody = "[If this were a production environment then this email would have been sent to: " + to;
        if ( cc!=null && !cc.equals("") ) {
          newBody += ", cc: " + cc;
        }
        if ( bcc!=null && !bcc.equals("") ) {
          newBody += ", bcc: " + bcc;
        }
        newBody += "]";
        
        if (formatHtml) {
          newBody += "<br><br>";
        } else {
          newBody += "\n\n";
        }
        newBody += body;
        body = newBody;
        to = testEmailTo;
        bcc = "";
        cc = "";
      }
      send(session, to, cc, bcc, from, subject, body, formatHtml);
    }
    
	private static void sendCheckTest(	Session session, 
										String to, 
										String cc,
										String bcc, 
										String from, 
										String subject, 
										String body,
										File file,
										boolean formatHtml, 
										boolean testEmail, 
										String testEmailTo)
												throws NamingException, AddressException, MessagingException, IOException {

		if (testEmail) {
			subject = "GNomEx Test Email - " + subject;

			// Add details about who the email would have gone to
			String newBody = "[If this were a production environment then this email would have been sent to: "
					+ to;
			if (cc != null && !cc.equals("")) {
				newBody += ", cc: " + cc;
			}
			if (bcc != null && !bcc.equals("")) {
				newBody += ", bcc: " + bcc;
			}
			newBody += "]";

			if (formatHtml) {
				newBody += "<br><br>";
			} else {
				newBody += "\n\n";
			}
			newBody += body;
			body = newBody;
			to = testEmailTo;
			bcc = "";
			cc = "";
		}
		send_attach(session, to, cc, bcc, from, subject, body, formatHtml, file);
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
    
    private static void send(Session session,
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

    // Send an email with an attachment(s)
    // Parameter file can be a file or a directory.  
    // If it is a directory, every file under that directory will be attached
    // recursively.
    private static void send_attach(Session session,
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
        recurseAddAttachment( multipart, file );
                
        msg.setHeader( "X-Mailer", "JavaMailer" );
        msg.setSentDate( new Date(  ) );
  
        msg.setContent( multipart );
        
        Transport.send( msg );
      }
    }
    
    private static void recurseAddAttachment(Multipart multipart, File file) throws MessagingException, IOException
    {
      if ( multipart== null || file == null || !file.exists() ) {
        return;
      }
      
      // If multipart has more than 10 file attachments, don't send.
      if ( multipart.getCount() > 11 ) {
        throw new IOException("Too many files. Cannot send more than 10 files at once.");
      }
      
      if ( file.isFile() ) {
        addAttachment( multipart, file );
        return;
      }
      String[] fileList = file.list();
      if ( fileList.length == 0 ) {
        return;
      }
      for (int x = 0; x < fileList.length; x++) {
        String fileName = file.getCanonicalPath() + File.separator + fileList[x];
        File f1 = new File(fileName);
          recurseAddAttachment( multipart, f1 );
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
