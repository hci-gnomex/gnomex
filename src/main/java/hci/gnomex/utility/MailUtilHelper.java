package hci.gnomex.utility;

import hci.gnomex.controller.GNomExFrontController;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.PropertyDictionary;

import java.io.File;
import java.util.Properties;

import javax.mail.Session;

public class MailUtilHelper {
	
	private String 				to;
	private String 				cc;
	private String 				bcc;
	private String 				from;
	private String 				subject;
	private String 				body;
	private File 				file;
	private boolean 			formatHtml;
	
	private Session 			session;
	private String 				serverName;
	private DictionaryHelper 	dictionaryHelper;
	
	private AppUser				recipientAppUser;
	
	private boolean				forceTestEmail;
	private String				forceTestEmailTo;
	
	public MailUtilHelper(	String to, String from, String subject, String body, File file, 
							boolean formatHtml, DictionaryHelper dictionaryHelper, String serverName	) {

		this(to, null, null, from, subject, body, file, formatHtml, null, serverName, dictionaryHelper);
		this.setSession(GNomExFrontController.getMailSession());

	}
	
	public MailUtilHelper(	String to, String cc, String bcc, String from, String subject, 
							String body, File file, boolean formatHtml, DictionaryHelper dictionaryHelper, 
							String serverName																) {
		
		this(to, cc, bcc, from, subject, body, file, formatHtml, null, serverName, dictionaryHelper);
		this.setSession(GNomExFrontController.getMailSession());
		
	}
	
	public MailUtilHelper(	Properties props, String to, String cc, String bcc, String from, String subject, 
							String body, File file, boolean formatHtml, DictionaryHelper dictionaryHelper, 
							String serverName																) {

		this(to, cc, bcc, from, subject, body, file, formatHtml, null, serverName, dictionaryHelper);
		this.setSession(javax.mail.Session.getDefaultInstance(props, null));

	}
	
	public MailUtilHelper(	Properties props, String to, String cc, String bcc, String from, String subject, 
							String body, File file, boolean formatHtml, boolean forceTestEmail, String forceTestEmailTo	) {

		this(to, cc, bcc, from, subject, body, file, formatHtml, null, null, null);
		this.setSession(javax.mail.Session.getDefaultInstance(props, null));
		this.setForceTestEmail(forceTestEmail);
		this.setForceTestEmailTo(forceTestEmailTo);

	}

	public MailUtilHelper(	String to, String from, String subject, String body, File file,
							  boolean formatHtml, DictionaryHelper dictionaryHelper, String serverName, boolean forceTestEmail, String forceTestEmailTo	) {

		this(to, null, null, from, subject, body, file, formatHtml, null, serverName, dictionaryHelper);
		this.setSession(GNomExFrontController.getMailSession());
		this.setForceTestEmail(forceTestEmail);
		this.setForceTestEmailTo(forceTestEmailTo);

	}

	public MailUtilHelper(	String to, String cc, String bcc, String from, String subject,
							  String body, File file, boolean formatHtml, DictionaryHelper dictionaryHelper,
							  String serverName, boolean forceTestEmail, String forceTestEmailTo) {

		this(to, cc, bcc, from, subject, body, file, formatHtml, null, serverName, dictionaryHelper);
		this.setSession(GNomExFrontController.getMailSession());
		this.setForceTestEmail(forceTestEmail);
		this.setForceTestEmailTo(forceTestEmailTo);
	}

	private MailUtilHelper(	String to, String cc, String bcc, String from, String subject, 
							String body, File file, boolean formatHtml, Session session, 
							String serverName, DictionaryHelper dictionaryHelper			) {
		
		this.to = to;
		this.cc = cc;
		this.bcc = bcc;
		this.from = from;
		this.subject = subject;
		this.body = body;
		this.file = file;
		this.formatHtml = formatHtml;
		this.session = session;
		this.serverName = serverName;
		this.dictionaryHelper = dictionaryHelper;
		
	}
	
	public boolean hasEssentialParams() {
		boolean hasEssentialParams = true;
		
		if (session == null) {
			hasEssentialParams = false;
		}
		
		boolean hasOverrideTestParams = false;
		if (forceTestEmail || forceTestEmailTo != null) {
			hasOverrideTestParams = true;
		}
		
		if (!hasOverrideTestParams && (serverName == null || serverName.trim().equals(""))) {
			hasEssentialParams = false;
		}
		
		if (!hasOverrideTestParams && dictionaryHelper == null) {
			hasEssentialParams = false;
		}
		
		return hasEssentialParams;
	}
	
	public boolean isTestEmail() {
		if (forceTestEmail) {
			return true;
		}
		
		if (dictionaryHelper != null && serverName != null) {
			return !dictionaryHelper.isProductionServer(serverName);
		}
		
		return false;
	}
	
	public String getTesterEmail() {
		if (forceTestEmailTo != null && !forceTestEmailTo.trim().equals("")) {
			return forceTestEmailTo;
		}
		
		if (dictionaryHelper != null) {
			return dictionaryHelper.getPropertyDictionary(PropertyDictionary.CONTACT_EMAIL_SOFTWARE_TESTER);
		}
		
		return "";
	}
	
	public String addNoAddressDetailsToBody() {
		StringBuffer newBody = new StringBuffer();
		
		String newLineChar;
		if (formatHtml) {
			newLineChar = "<br>";
		} else {
			newLineChar = "\n";
		}
		
		if (recipientAppUser != null) {
			newBody.append("--- Information about this email's recipient app user ---");
			newBody.append(newLineChar);
			newBody.append("--- Name: ");
			newBody.append(recipientAppUser.getFirstLastDisplayName() != null ? recipientAppUser.getFirstLastDisplayName() : "");
			newBody.append(" ---");
			newBody.append(newLineChar);
			newBody.append("--- Phone: ");
			newBody.append(recipientAppUser.getPhone() != null ? recipientAppUser.getPhone() : "");
			newBody.append(" ---");
			newBody.append(newLineChar);
			newBody.append(newLineChar);
		}
		
		newBody.append(getNonNullBody());
		
		return newBody.toString();
	}
	
	public String getNonNullTo() {
		if (to != null) {
			return to;
		}
		return "";
	}
	
	public void setTo(String to) {
		this.to = to;
	}
	
	public String getNonNullCC() {
		if (cc != null) {
			return cc;
		}
		return "";
	}
	
	public void setCC(String cc) {
		this.cc = cc;
	}
	
	public String getNonNullBCC() {
		if (bcc != null) {
			return bcc;
		}
		return "";
	}
	
	public void setBCC(String bcc) {
		this.bcc = bcc;
	}
	
	public String getNonNullFrom() {
		if (from != null) {
			return from;
		}
		return "";
	}
	
	public void setFrom(String from) {
		this.from = from;
	}
	
	public String getNonNullSubject() {
		if (subject != null) {
			return subject;
		}
		return "";
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public String getNonNullBody() {
		if (body != null) {
			return body;
		}
		return "";
	}
	
	public void setBody(String body) {
		this.body = body;
	}
	
	public File getFile() {
		return file;
	}
	
	public void setFile(File file) {
		this.file = file;
	}
	
	public boolean getFormatHtml() {
		return formatHtml;
	}
	
	public void setFormatHtml(boolean formatHtml) {
		this.formatHtml = formatHtml;
	}
	
	public Session getSession() {
		return session;
	}
	
	public void setSession(Session session) {
		this.session = session;
	}
	
	public String getServerName() {
		return serverName;
	}
	
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	
	public DictionaryHelper getDictionaryHelper() {
		return dictionaryHelper;
	}
	
	public void setDictionaryHelper(DictionaryHelper dictionaryHelper) {
		this.dictionaryHelper = dictionaryHelper;
	}
	
	public AppUser getRecipientAppUser() {
		return recipientAppUser;
	}
	
	public void setRecipientAppUser(AppUser recipientAppUser) {
		this.recipientAppUser = recipientAppUser;
	}
	
	public boolean getForceTestEmail() {
		return forceTestEmail;
	}
	
	public void setForceTestEmail(boolean forceTestEmail) {
		this.forceTestEmail = forceTestEmail;
	}
	
	public String getForceTestEmailTo() {
		return forceTestEmailTo;
	}
	
	public void setForceTestEmailTo(String forceTestEmailTo) {
		this.forceTestEmailTo = forceTestEmailTo;
	}
	
}
