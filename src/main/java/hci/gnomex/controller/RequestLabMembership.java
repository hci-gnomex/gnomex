package hci.gnomex.controller;

import hci.framework.control.Command;import hci.gnomex.utility.Util;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.Lab;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.GNomExRollbackException;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.MailUtil;
import hci.gnomex.utility.MailUtilHelper;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.apache.log4j.Logger;
public class RequestLabMembership extends GNomExCommand implements Serializable {
	private static Logger LOG = Logger.getLogger(RequestLabMembership.class);

	private List<String> idLabs;
	private StringBuffer requestURL;
	private String serverName;

	private final static int APPROVE_USER_EXPIRATION_TIME = 86400000 * 3;// Three

	// days

	@Override
	public void loadCommand(HttpServletRequest request, HttpSession sess) {
		this.requestURL = request.getRequestURL();
		this.serverName = request.getServerName();

		if (request.getParameter("idLabs") != null && !request.getParameter("idLabs").equals("")) {
			this.idLabs = Arrays.asList(request.getParameter("idLabs").split(","));
		}

	}

	@Override
	public Command execute() throws GNomExRollbackException {
		try {
			Session sess = HibernateSession.currentSession(this.getUsername());
			AppUser currentUser = (AppUser) sess.load(AppUser.class, this.getSecAdvisor().getAppUser().getIdAppUser());

			// If we have a guid but it is expired then create a new guid with a new
			// expiration date. Otherwise use what is currently in DB
			if (currentUser.getGuid() == null
					|| (currentUser.getGuid() != null && currentUser.getGuidExpiration().before(new Date(System.currentTimeMillis())))) {
				Timestamp ts = new Timestamp(System.currentTimeMillis() + RequestLabMembership.APPROVE_USER_EXPIRATION_TIME);
				currentUser.setGuid((UUID.randomUUID().toString()));
				currentUser.setGuidExpiration(ts);
				sess.save(currentUser);
				sess.flush();
			}

			for (Iterator i = idLabs.iterator(); i.hasNext();) {
				String id = (String) i.next();
				Lab l = (Lab) sess.load(Lab.class, Integer.parseInt(id));

				sendRequestEmail(l, currentUser, sess);
			}

			setResponsePage(this.SUCCESS_JSP);

		} catch (Exception e) {
			this.errorDetails = Util.GNLOG(LOG,"Error in RequestLabMembership", e);
		}

		return this;
	}

	private void sendRequestEmail(Lab requestedLab, AppUser appUser, Session sess) throws NamingException, MessagingException, IOException {
		// This is to send it to the right application server, without hard coding
		String url = requestURL.substring(0, requestURL.indexOf("RequestLabMembership.gx"));
		DictionaryHelper dictionaryHelper = DictionaryHelper.getInstance(sess);
		PropertyDictionaryHelper propertyHelper = PropertyDictionaryHelper.getInstance(sess);

		if (appUser.getEmail().equals("bademail@bad.com")) {
			throw new AddressException("'bademail@bad.com' not allowed");
		}

		String toAddress = "";
		String ccAddress = "";
		String fromAddress = propertyHelper.getProperty(PropertyDictionary.GENERIC_NO_REPLY_EMAIL);
		String subject = "GNomEx user " + appUser.getFirstName() + " " + appUser.getLastName() + " is requesting permission to join your lab.";

		// we will send activation email to lab pi if user requests membership to
		// existing lab. If new lab or no pi email the activation email will go to
		// core facility director.
		String noEmailOnFileNote = "";
		if (requestedLab != null && requestedLab.getContactEmail() != null && !requestedLab.getContactEmail().equals("")) {
			toAddress += requestedLab.getContactEmail();
		} else {
			toAddress += propertyHelper.getProperty(PropertyDictionary.GNOMEX_SUPPORT_EMAIL);
			noEmailOnFileNote = "<p style=\"color:red\">THERE WAS NO CONTACT EMAIL ON FILE FOR THIS LAB.  LAB P.I. WAS NOT NOTIFIED OF THIS REQUEST!</p><BR><BR>";
		}

		// Add GNomEx support email to cc list
		if (propertyHelper.getProperty(PropertyDictionary.NOTIFY_SUPPORT_OF_NEW_USER).equals("Y")) {
			if (ccAddress.length() > 0) {
				ccAddress += ", ";
			}
			ccAddress += propertyHelper.getProperty(PropertyDictionary.GNOMEX_SUPPORT_EMAIL);
		}

		// Get approve and delete URLs
		String uuidStr = appUser.getGuid();
		String approveURL = url + Constants.FILE_SEPARATOR + Constants.APPROVE_LAB_MEMBERSHIP_SERVLET + "?idAppUser=" + appUser.getIdAppUser().intValue() + "&idLab="
				+ requestedLab.getIdLab() + "&guid=" + uuidStr;
		String deleteURL = url + Constants.FILE_SEPARATOR + Constants.APPROVE_LAB_MEMBERSHIP_SERVLET + "?idAppUser=" + appUser.getIdAppUser().intValue() + "&denyRequest=Y"
				+ "&guid=" + uuidStr + "&idLab=" + requestedLab.getIdLab();
		StringBuffer introForAdmin = new StringBuffer();

		// Intro to Lab PI/Admin
		String greeting = "Dear " + requestedLab.getName(false, false, false) + ",<br><br>";
		String intro = noEmailOnFileNote + "The following person has requested membership to your lab.  Please approve or deny their request.<br><br>";

		introForAdmin.append(greeting);
		introForAdmin.append(intro);
		introForAdmin
				.append("<a href='"
						+ approveURL
						+ "'>Click here</a> to approve their request.  GNomEx will automatically send an email to notify the user that they have been added to your lab.<br><br>");
		introForAdmin
				.append("<a href='"
						+ deleteURL
						+ "'>Click here</a> to deny their request.  GNomEx will automatically send an email to notify the user that their request has been denied.<br><br>");

		// Closing for Lab PI/Admin
		if (requestedLab != null) {
			String closing = "If you have any questions concerning this application for a new account within your lab group, please contact ";
			closing += "GNomEx Support " + " (" + propertyHelper.getProperty(PropertyDictionary.GNOMEX_SUPPORT_EMAIL) + ").<br><br>";
			introForAdmin.append(closing);
			introForAdmin.append("<small>(These links will expire in 3 days.)</small><br><br>");
		}

		MailUtilHelper helper = new MailUtilHelper(toAddress, ccAddress, null, fromAddress, subject, introForAdmin.toString()
				+ getEmailBody(appUser, requestedLab, sess), null, true, dictionaryHelper, serverName);
		MailUtil.validateAndSendEmail(helper);

	}

	private String getEmailBody(AppUser appUser, Lab requestedLab, Session sess) {
		StringBuffer body = new StringBuffer();
		body.append("<table border='0'><tr><td>Last name:</td><td>" + this.getNonNullString(appUser.getLastName()));
		body.append("</td></tr><tr><td>First name:</td><td>" + this.getNonNullString(appUser.getFirstName()));
		body.append("</td></tr><tr><td>Lab:</td><td>" + this.getNonNullString(requestedLab.getName(false, false, false)));
		if (!this.getNonNullString(appUser.getInstitute()).equals("")) {
			body.append("</td></tr><tr><td>Institution:</td><td>" + this.getNonNullString(appUser.getInstitute()));
		}
		body.append("</td></tr><tr><td>Email:</td><td>" + this.getNonNullString(appUser.getEmail()));
		body.append("</td></tr><tr><td>Phone:</td><td>" + this.getNonNullString(appUser.getPhone()));
		if (appUser.getuNID() != null && appUser.getuNID().length() > 0) {
			body.append("</td></tr><tr><td>University uNID:</td><td>" + this.getNonNullString(appUser.getuNID()));
		} else {
			body.append("</td></tr><tr><td>Username:</td><td>" + this.getNonNullString(appUser.getUserNameExternal()));
		}
		body.append("</td></tr></table>");

		return body.toString();
	}

	@Override
	public void validate() {
		// TODO Auto-generated method stub

	}

}
