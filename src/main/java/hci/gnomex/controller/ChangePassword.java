package hci.gnomex.controller;

import hci.framework.control.Command;import hci.gnomex.utility.Util;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.security.EncryptionUtility;
import hci.gnomex.utility.*;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;

public class ChangePassword extends GNomExCommand implements Serializable {

private static Logger LOG = Logger.getLogger(ChangePassword.class);

public final static String SUCCESS_JSP = "/getXML.jsp";
// public String ERROR_JSP = "/message.jsp";
public final static String ERROR_JSP = "/change_password.jsp";

private String userName;
private String email;
private String newPassword;
private String guid;
private String responsePageSuccess = null;
private String responsePageError = null;

private AppUser appUser = null;
private String labContactEmail = null;
private String regErrorMsg = null;

private String launchAppURL = "";
private String appURL = "";
private String serverName;
private DictionaryHelper dictionaryHelper;

private static long GUID_EXPIRATION = 1800000; // 30 minutes

private String action;
public final static String ACTION_REQUEST_PASSWORD_RESET = "requestPasswordReset";
public final static String ACTION_FINALIZE_PASSWORD_RESET = "finalizePasswordReset";
public final static String ACTION_CHANGE_EXPIRED_PASSWORD = "changeExpiredPassword";  // ChangePassword.mxml hard-codes this value

public void loadCommand(HttpServletRequest request, HttpSession session) {
	try {
		this.launchAppURL = this.getLaunchAppURL(request);
		this.appURL = this.getAppURL(request);

		this.action = request.getParameter("action");

		if (request.getParameter("userName") != null && !request.getParameter("userName").equals("")) {
			this.userName = request.getParameter("userName");
		}

		if (request.getParameter("email") != null && !request.getParameter("email").equals("")) {
			this.email = request.getParameter("email");
		}

		if (email == null && userName == null) {
			this.addInvalidField("User name or email required", "Please provide user name or email for lookup.");
		}

		if (request.getParameter("guid") != null && !request.getParameter("guid").equals("")) {
			this.guid = request.getParameter("guid");
		}

		if (request.getParameter("newPassword") != null && !request.getParameter("newPassword").equals("")) {
			this.newPassword = request.getParameter("newPassword");
		}

		if (action.equals(ACTION_FINALIZE_PASSWORD_RESET) || action.equals(ACTION_CHANGE_EXPIRED_PASSWORD)) {
			if (newPassword == null || newPassword.equals("")) {
				this.addInvalidField("password", "Password is required");
			} else if (!PasswordUtil.passwordMeetsRequirements(newPassword)) {
				this.addInvalidField("password", PasswordUtil.COMPLEXITY_ERROR_TEXT);
			}
		}
		if (request.getParameter("responsePageSuccess") != null
				&& !request.getParameter("responsePageSuccess").equals("")) {
			responsePageSuccess = request.getParameter("responsePageSuccess");
		}
		if (request.getParameter("responsePageError") != null && !request.getParameter("responsePageError").equals("")) {
			responsePageError = request.getParameter("responsePageError");
		}
		if (request.getParameter("idCoreParm") != null && !request.getParameter("idCoreParm").equals("")) {
			String idCoreParm = request.getParameter("idCoreParm");
			launchAppURL = Util.addURLParameter(launchAppURL, idCoreParm);
		}

		serverName = request.getServerName();

		this.validate();
	} catch (Exception e) {
		LOG.error(e.getClass().toString() + ": ", e);
	}
}

public Command execute() throws RollBackCommandException {

	try {
		Session sess = HibernateSession.currentSession(this.getUsername());
		dictionaryHelper = DictionaryHelper.getInstance(sess);
		appUser = lookupAndValidateAppUser(sess);
		if (isValid()) {
			if (action.equals(ACTION_FINALIZE_PASSWORD_RESET) || action.equals(ACTION_CHANGE_EXPIRED_PASSWORD)) {
				changePassword(sess);
			} else if (action.equals(ACTION_REQUEST_PASSWORD_RESET)) {
				requestPasswordReset(sess);
			} else {
				addInvalidField("action", "An error occurred processing your request");
				LOG.error("Request parameter 'action' had an unexpected value: '" + action + "'");
				responsePageError = "/";
			}
		}
	} catch (Exception e) {
		addInvalidField("exception", "An error occurred processing your request");
		this.errorDetails = Util.GNLOG(LOG,"An exception occured in ChangePassword ", e);
		throw new RollBackCommandException();
	} finally {
		this.validate();
	}
	return this;
}

private AppUser lookupAndValidateAppUser(Session sess) {
	String checkRegHql = "";
	if (userName != null) {
		checkRegHql = "from AppUser u where u.userNameExternal='" + userName + "'";
	} else {
		checkRegHql = "from AppUser u where u.email='" + email + "'";
	}

	if (action.equals(ACTION_FINALIZE_PASSWORD_RESET)) {
		checkRegHql += " and u.guid='" + guid + "'";
	}
	AppUser appUser = (AppUser) sess.createQuery(checkRegHql).uniqueResult();

	if (appUser == null) {
		regErrorMsg = "Invalid user.";
		this.addInvalidField("Invalid username or email", regErrorMsg);
	}
	if (appUser.getIsActive() != null && appUser.getIsActive().equals("N")) {
		regErrorMsg = "Inactive user account.";
		this.addInvalidField("Inactive user account", regErrorMsg);
	}
	return appUser;
}

private void changePassword(Session sess) {
	EncryptionUtility passwordEncrypter = new EncryptionUtility();
	String salt = passwordEncrypter.createSalt();
	String thePasswordEncrypted = passwordEncrypter.createPassword(newPassword, salt);
	appUser.setPasswordExternal(thePasswordEncrypted);
	appUser.setSalt(salt);
	appUser.setGuid(null);
	appUser.setGuidExpiration(null);
	appUser.setPasswordExpired("N");
	sess.update(appUser);
	sess.flush();
}

private void requestPasswordReset(Session sess) throws NamingException, MessagingException, IOException {
	PropertyDictionaryHelper pdh = PropertyDictionaryHelper.getInstance(sess);
	if (appUser.getEmail() == null || appUser.getEmail().equals("")) {
		regErrorMsg = "Your GNomEx account does not have an email address associated with it. Please contact "
				+ pdh.getProperty(PropertyDictionary.CONTACT_EMAIL_SOFTWARE_BUGS) + " for help.";
		this.addInvalidField("Invalid email", regErrorMsg);
	} else {
		UUID uuid = UUID.randomUUID();
		Timestamp ts = new Timestamp(System.currentTimeMillis() + ChangePassword.GUID_EXPIRATION);
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());

		if ((appUser.getGuid() == null && appUser.getGuidExpiration() == null)
				|| (appUser.getGuidExpiration() != null && currentTime.after(appUser.getGuidExpiration()))) {

			appUser.setGuid(uuid.toString());
			appUser.setGuidExpiration(ts);
			sess.update(appUser);
			sess.flush();

			if (pdh.getProperty(PropertyDictionary.CONTACT_EMAIL_SOFTWARE_BUGS) != null) {
				labContactEmail = pdh.getProperty(PropertyDictionary.CONTACT_EMAIL_SOFTWARE_BUGS);
			} else {
				labContactEmail = pdh.getProperty(PropertyDictionary.CONTACT_EMAIL_SOFTWARE_BUGS);
			}

			sendConfirmationEmail(appUser, uuid.toString());
		} else {
			regErrorMsg = "Reset password email already requested. Please wait for the instructions in your email.";
			this.addInvalidField("Reset password email already requested", regErrorMsg);
		}
	}
}

public void validate() {
	// See if we have a valid form
	if (isValid()) {
		setResponsePage(responsePageSuccess != null ? responsePageSuccess : SUCCESS_JSP);
	} else {
		setResponsePage(responsePageError != null ? responsePageError : ERROR_JSP);
	}
}

public void sendConfirmationEmail(AppUser appUser, String guid) throws NamingException, MessagingException, IOException {
	StringBuffer content = new StringBuffer();

	if (appUser.getuNID() != null && !appUser.getuNID().equals("")) {
		// If they provided their email, give them their user name. It might cause them to remember their password.
		// Still provide the link just in case
		if (email != null) {
			content.append("Your uNID is : " + appUser.getuNID() + "<br><br>");
		}

		content.append("A change of password has been requested for the gnomex account associated with this email.  ");
		content.append("However, this account is associated with a University of Utah account.  If you know your CIS credentials please login using those here: <a href=\""
				+ appURL + "\">" + appURL + "</a>");
		content.append("<br><br>If you do not recall your CIS credentials you can reset them here: <a href=https://go.utah.edu/cas/login>https://go.utah.edu/cas/login</a><br>");

	} else {
		appURL += "/change_password.jsp?guid=" + guid;

		// If they provided their email, give them their user name. It might cause them to remember their password.
		// Still provide the link just in case
		if (email != null) {
			content.append("Your user name is: " + appUser.getUserNameExternal() + "<br><br>");
		}

		content.append("A change of password has been requested for the gnomex account associated with this email.<br/>");
		content.append("Please follow <a href=\"" + appURL + "\">this link</a> to change your password.<br/>");
		content.append("This link will expire in 30 minutes.");
	}

	MailUtilHelper helper = new MailUtilHelper(appUser.getEmail(), this.labContactEmail, "Reset GNomEx Password",
			content.toString(), null, true, dictionaryHelper, serverName);
	helper.setRecipientAppUser(appUser);
	MailUtil.validateAndSendEmail(helper);
}
}
