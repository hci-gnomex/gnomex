package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.security.EncryptionUtility;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.MailUtil;
import hci.gnomex.utility.MailUtilHelper;
import hci.gnomex.utility.PropertyDictionaryHelper;
import hci.gnomex.utility.Util;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
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

public String SUCCESS_JSP = "/getXML.jsp";
// public String ERROR_JSP = "/message.jsp";
public String ERROR_JSP = "/change_password.jsp";

private String userName;
private String email;
private String newPassword;
private String newPasswordConfirm;
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

private boolean changingPassword = false;

private static long GUID_EXPIRATION = 1800000; // 30 minutes

public void loadCommand(HttpServletRequest request, HttpSession session) {
	try {
		this.launchAppURL = this.getLaunchAppURL(request);
		this.appURL = this.getAppURL(request);

		changingPassword = false;
		if (request.getParameter("changingPassword") != null && request.getParameter("changingPassword").equals("Y")) {
			changingPassword = true;
		}

		if (request.getParameter("userName") != null && !request.getParameter("userName").equals("")) {
			this.userName = request.getParameter("userName");
		}

		if (request.getParameter("email") != null && !request.getParameter("email").equals("")) {
			this.email = request.getParameter("email");
		}

		if (email == null && userName == null) {
			this.addInvalidField("User name or email required", "Please provide user name or email for lookup.");
		}

		// if (request.getParameter("oldPassword") != null && ! request.getParameter("oldPassword").equals("")) {
		// this.oldPassword = request.getParameter("oldPassword");
		// }

		if (request.getParameter("guid") != null && !request.getParameter("guid").equals("")) {
			this.guid = request.getParameter("guid");
		}

		if (request.getParameter("newPassword") != null && !request.getParameter("newPassword").equals("")) {
			this.newPassword = request.getParameter("newPassword");
		}

		if (request.getParameter("newPasswordConfirm") != null
				&& !request.getParameter("newPasswordConfirm").equals("")) {
			this.newPasswordConfirm = request.getParameter("newPasswordConfirm");
		}

		if (newPassword != null && newPasswordConfirm != null) {
			if (!newPassword.equals(newPasswordConfirm)) {
				this.addInvalidField("passwords",
						"The two passwords you have entered do not match. Please re-enter your new password.");
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
		EncryptionUtility passwordEncrypter = new EncryptionUtility();
		dictionaryHelper = DictionaryHelper.getInstance(sess);
		PropertyDictionaryHelper pdh = PropertyDictionaryHelper.getInstance(sess);

		// First make sure this person is registered by looking them
		// up in the user table
		String checkRegHql = "";
		if (userName != null) {
			checkRegHql = "from AppUser u where u.userNameExternal='" + userName + "'";
		} else {
			checkRegHql = "from AppUser u where u.email='" + email + "'";
		}

		if (changingPassword) {
			checkRegHql += " and u.guid='" + guid + "'";
		}

		appUser = (AppUser) sess.createQuery(checkRegHql).uniqueResult();
		// Check that the user exists
		if (appUser == null) {
			regErrorMsg = "Invalid user.";
			this.addInvalidField("Invalid username or email", regErrorMsg);
		}
		// Check that the user is active
		else if (appUser.getIsActive() != null && appUser.getIsActive().equals("N")) {
			regErrorMsg = "Inactive user account.";
			this.addInvalidField("Inactive user account", regErrorMsg);
		} else {
			// Change password
			if (newPassword != null && newPasswordConfirm != null) {
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
			// Reset password
			else {
				// Check that the user has an email if we are attempting a reset password
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

						// Code for change_password

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
		}
		this.validate();
	} catch (Exception e) {
		LOG.error(e.getClass().toString() + ": ", e);
		throw new RollBackCommandException();
	} finally {
		try {
			this.validate();
			// closeHibernateSession;
		} catch (HibernateException e) {
			LOG.error(e.getClass().toString() + ": ", e);
			this.validate();
			throw new RollBackCommandException();
		}
		/*
		 * catch (SQLException e) { LOG.error(e.getClass().toString() + ": " , e); this.validate(); throw new RollBackCommandException(); }
		 */
	}

	return this;
}

public void validate() {
	// See if we have a valid form
	if (isValid()) {
		setResponsePage(responsePageSuccess != null ? responsePageSuccess : this.SUCCESS_JSP);
	} else {
		setResponsePage(responsePageError != null ? responsePageError : this.ERROR_JSP);
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

		content.append("A change of password has been requested for the gnomex account associated with this email.<br>");
		content.append("Please follow this link to change your password <a href=\"" + appURL + "\">" + appURL
				+ "</a><br>");
		content.append("This link will expire in 30 minutes.");
	}

	MailUtilHelper helper = new MailUtilHelper(appUser.getEmail(), this.labContactEmail, "Reset GNomEx Password",
			content.toString(), null, true, dictionaryHelper, serverName);
	helper.setRecipientAppUser(appUser);
	MailUtil.validateAndSendEmail(helper);
}
}
