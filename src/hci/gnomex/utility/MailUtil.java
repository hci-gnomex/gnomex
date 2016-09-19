package hci.gnomex.utility;

import java.io.File;
import java.io.IOException;
import java.util.Date;

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

public class MailUtil {
	
	public static boolean validateAndSendEmail(MailUtilHelper helper) throws AddressException, NamingException, MessagingException, IOException {
		
		if (helper != null && helper.hasEssentialParams()) {
			String to = helper.getNonNullTo();
			String cc = helper.getNonNullCC();
			String bcc = helper.getNonNullBCC();
			String subject = helper.getNonNullSubject();
			String body = helper.getNonNullBody();
			
			if (to.trim().equals("") || !isValidEmail(to)) {
				to = helper.getTesterEmail();
		    	subject = "The following email does not have a valid recipient - " + subject;
		    	body = helper.addNoAddressDetailsToBody();
			}
			
			if (helper.isTestEmail()) {
				subject = "GNomEx Test Email - " + subject;

				// Add details about who the email would have gone to
				StringBuffer newBody = new StringBuffer("[If this were a production environment then this email would have been sent to: ");
				newBody.append(to);
				if (!cc.trim().equals("")) {
					newBody.append(", cc: ");
					newBody.append(cc);
				}
				if (!bcc.trim().equals("")) {
					newBody.append(", bcc: ");
					newBody.append(bcc);
				}
				newBody.append("]");

				if (helper.getFormatHtml()) {
					newBody.append("<br><br>");
				} else {
					newBody.append("\n\n");
				}
				newBody.append(body);
				
				body = newBody.toString();
				to = helper.getTesterEmail();
				cc = "";
				bcc = "";
			}
			
			send(helper.getSession(), to, cc, bcc, helper.getNonNullFrom(), subject, body, helper.getFile(), helper.getFormatHtml());
			
			return true;
		}
		
		return false;
		
	}
	
	private static void send(	Session sess, String to, String cc, String bcc, String from, 
								String subject, String body, File file, boolean formatHtml		) throws NamingException, AddressException, MessagingException, IOException {

		if (sess != null) {
			if (sess.getProperty("mail.smtp.auth") != null && sess.getProperty("mail.smtp.auth").equals("true")) {
				// Fetch user and password
				PasswordAuthentication auth= new PasswordAuthentication(
																			sess.getProperty("mail.smtp.user"),
																			sess.getProperty("mail.smtp.password")	);
				// Build URL for the session's password cache
				URLName url= new URLName(
											sess.getProperty("mail.transport.protocol"),
											sess.getProperty("mail.smtp.host"),
											-1,
											null,
											sess.getProperty("mail.smtp.user"),
											null											);
				// Fill password cache
				sess.setPasswordAuthentication(url,auth);
	        }
	  
	        javax.mail.Message msg = new MimeMessage(sess);
	  
	        msg.setFrom(new InternetAddress(from));
	        msg.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(to, false));
	        
	        if (cc != null && !cc.trim().equals("")) {
	        	msg.setRecipients(javax.mail.Message.RecipientType.CC, InternetAddress.parse(cc, false));
	        }
	        
	        if (bcc != null && !bcc.trim().equals("")) {
	        	msg.setRecipients(javax.mail.Message.RecipientType.BCC, InternetAddress.parse(bcc, false));
	        }
	        msg.setSubject(subject);
	  
	        String format = "text/plain";
	        if (formatHtml) {
	        	format = "text/html";
	        }
	        
	        if (file != null) {
	        	Multipart multipart = new MimeMultipart();
	            
	            // Email body
	            BodyPart messageBodyPart = new MimeBodyPart();
	            messageBodyPart.setDataHandler(new DataHandler(body, format));
	            multipart.addBodyPart(messageBodyPart);
	      
	            // Add file attachment(s)
	            recurseAddAttachment(multipart, file);
	      
	            msg.setContent(multipart);
	        } else {
	        	msg.setDataHandler(new DataHandler(body, format));
	        }
	        
	        msg.setHeader("X-Mailer", "JavaMailer");
	        msg.setSentDate(new Date());
	  
	        Transport.send(msg);
		}
	      
	}
	
	private static void recurseAddAttachment(Multipart multipart, File file) throws MessagingException, IOException {
		if (multipart == null || file == null || !file.exists()) {
			return;
		}
      
		// If multipart has more than 10 file attachments, don't send.
		if (multipart.getCount() > 11) {
			throw new IOException("Too many files. Cannot send more than 10 files at once.");
		}
      
		if (file.isFile()) {
			addAttachment(multipart, file);
			return;
		}
		String[] fileList = file.list();
		if (fileList.length == 0) {
			return;
		}
		for (int x = 0; x < fileList.length; x++) {
			String fileName = file.getCanonicalPath() + File.separator + fileList[x];
			File f1 = new File(fileName);
			recurseAddAttachment( multipart, f1 );
		}
	}
	
	private static void addAttachment(Multipart multipart, File file) throws MessagingException, IOException {
		if (multipart == null || file == null || !file.exists()) {
			return;
		}
		BodyPart messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(file.getCanonicalPath());
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(file.getName());
        multipart.addBodyPart(messageBodyPart);
	}
	
	public static boolean isValidEmail(String emailAddress) {
		if (emailAddress == null || emailAddress.trim().equals("")) {
			return false;
		}
	      
		String[] tokens = null;
		tokens = emailAddress.split("[,; ]");
		if (tokens != null && tokens.length > 0) {
			for (int x = 0; x < tokens.length; x++) {
				if (!tokens[x].equals("")) {
					if (!isValidEmailImpl(tokens[x].trim())) {
						return false;
					}
				}
			}
		} else {
			return isValidEmailImpl(emailAddress.trim());
		}
	      
		return true;
	}
	
	private static boolean isValidEmailImpl(String emailAddress) {
		EmailValidator ev = EmailValidator.getInstance();
		if (ev.isValid(emailAddress)) {
			return true;
		}   
	      	return false;
	}

}
