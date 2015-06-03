package hci.gnomex.utility;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.naming.NamingException;

import org.hibernate.Session;

import hci.dictionary.utility.DictionaryManager;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.CoreFacility;
import hci.gnomex.model.Lab;
import hci.gnomex.model.PropertyDictionary;

public class BillingAccountUtil {
	
	// The static field for logging in Log4J
	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(BillingAccountUtil.class);
	
	private static void sendApprovedBillingAccountEmail(Session sess, String serverName, String launchAppURL, BillingAccount billingAccount, Lab lab, AppUser approver, String approverEmail) throws AddressException, NamingException, MessagingException {
		PropertyDictionaryHelper dictionaryHelper = PropertyDictionaryHelper.getInstance(sess);

	    StringBuffer submitterNote = new StringBuffer();
	    StringBuffer body = new StringBuffer();
	    String submitterSubject = "GNomEx Billing Account '" + billingAccount.getAccountName() + "' for " + lab.getName(false, true) + " approved";    

	    boolean send = false;
	    String submitterEmail = billingAccount.getSubmitterEmail();

	    CoreFacility facility = (CoreFacility) sess.load(CoreFacility.class, billingAccount.getIdCoreFacility());

	    boolean isTestEmail = false;
	    String emailInfo = "";
	    String emailRecipients = submitterEmail;
	    if (!MailUtil.isValidEmail(submitterEmail)) {
	    	log.error("Invalid Email: " + submitterEmail);
	    }
	    if (dictionaryHelper.isProductionServer(serverName)) {
	    	send = true;
	    } else {
	    	isTestEmail = true;
	    	send = true;
	    	submitterSubject = submitterSubject + "  (TEST)";
	    	emailInfo = "[If this were a production environment then this email would have been sent to: " + emailRecipients + "]<br><br>";
	    	emailRecipients = dictionaryHelper.getProperty(PropertyDictionary.CONTACT_EMAIL_SOFTWARE_TESTER);
	    }

	    submitterNote.append("The following billing account " +
	    					 "has been approved by the " + facility.getDisplay() + " Core. " +  
	    					 "Lab members can now submit experiment " +
	    					 "requests against this account in GNomEx");
	    if (launchAppURL != null && !launchAppURL.trim().equals("")) {
	    	submitterNote.append(" " + launchAppURL);
	    }
	    submitterNote.append(".");

	    body.append("<br />");
	    body.append("<br />");
	    body.append("<table border=0>");
	    body.append("<tr><td>Lab:</td><td>" + lab.getName(false, false) + "</td></tr>");
	    body.append("<tr><td>Core Facility:</td><td>" + facility.getDisplay() + "</td></tr>");
	    body.append("<tr><td>Account:</td><td>" + billingAccount.getAccountName() + "</td></tr>");
	    body.append("<tr><td>Chartfield:</td><td>" + billingAccount.getAccountNumber() + "</td></tr>");
	    if (billingAccount.getIdFundingAgency() != null) {
	    	body.append("<tr><td>Funding Agency:</td><td>" + DictionaryManager.getDisplay("hci.gnomex.model.FundingAgency", billingAccount.getIdFundingAgency().toString()) + "</td></tr>");
	    }
	    body.append("<tr />");
	    if (billingAccount.getExpirationDateOther() != null && billingAccount.getExpirationDateOther().length() > 0) {
	    	body.append("<tr><td>Effective until:</td><td>" + billingAccount.getExpirationDateOther() + "</td></tr>");
	    }
	    body.append("<tr><td>Submitter UID:</td><td>" + billingAccount.getSubmitterUID() + "</td></tr>");
	    body.append("<tr><td>Submitter Email:</td><td>" + billingAccount.getSubmitterEmail() + "</td></tr>");
	    body.append("<tr><td>Submit Date:</td><td>" + billingAccount.getCreateDate() + "</td></tr>");
	    
	    body.append("<tr><td>Approved By:</td><td>");
	    if (approver != null) {
	    	body.append(approver.getDisplayName());
	        if (approver.getEmail() != null && !approver.getEmail().equals("")) {
	        	body.append(" (" + approver.getEmail() + ")" );
	        }
	    } else if (approverEmail != null) {
	    	body.append(approverEmail);
	    }
	    body.append("</td></tr>");
	    body.append("<tr><td>Approved Date:</td><td>" + billingAccount.getApprovedDate() + "</td></tr>");
	    body.append("</table>");

	    String from = dictionaryHelper.getCoreFacilityProperty(facility.getIdCoreFacility(), PropertyDictionary.CONTACT_EMAIL_CORE_FACILITY);

	    if (send) {
	      // Email submitter
	      if (!MailUtil.isValidEmail(from)) {
	    	  from = DictionaryHelper.getInstance(sess).getPropertyDictionary(PropertyDictionary.GENERIC_NO_REPLY_EMAIL);
	      }
	      MailUtil.send(emailRecipients, 
	    		   		null,
	    		   		from, 
	    		   		submitterSubject, 
	    		   		emailInfo + submitterNote.toString() + body.toString(),
	    		   		true);

	      // Email people with approve power notifying them that the account has been approved and they don't have to do anything else.
	      if (lab.getBillingNotificationEmail() != null && !lab.getBillingNotificationEmail().equals("")) {
	    	  String contactEmail = lab.getBillingNotificationEmail();
	    	  if (lab.getWorkAuthSubmitEmail() != null && !lab.getWorkAuthSubmitEmail().equals("")) {
	    		  contactEmail += ", " + lab.getWorkAuthSubmitEmail();
	    	  }
	    	  String facilityEmail = dictionaryHelper.getCoreFacilityProperty(facility.getIdCoreFacility(), PropertyDictionary.CONTACT_EMAIL_CORE_FACILITY_WORKAUTH);
	    	  if (facilityEmail == null || facilityEmail.equals("")) {
	    		  facilityEmail = dictionaryHelper.getCoreFacilityProperty(facility.getIdCoreFacility(), PropertyDictionary.CONTACT_EMAIL_CORE_FACILITY);
	    	  }
	    	  contactEmail += ", " + facilityEmail;
	    	  if (isTestEmail) {
	    		  emailInfo = "[If this were a production environment then this email would have been sent to: " + contactEmail + "]<br><br>";
	    		  contactEmail = dictionaryHelper.getProperty(PropertyDictionary.CONTACT_EMAIL_SOFTWARE_TESTER);
	    	  }
	    	  MailUtil.send(contactEmail, 
	    			  		null,
	    			  		from, 
	    			  		isTestEmail ? submitterSubject + " (for lab contact " + lab.getContactEmail() + ")" : submitterSubject, 
	    			  		emailInfo + submitterNote.toString() + body.toString(),
	    			  		true);
	      }
	    }
	}
	
	public static void sendApprovedBillingAccountEmail(Session sess, String serverName, String launchAppURL, BillingAccount billingAccount, Lab lab, AppUser approver) throws AddressException, NamingException, MessagingException {
		sendApprovedBillingAccountEmail(sess, serverName, launchAppURL, billingAccount, lab, approver, null);
	}
	
	public static void sendApprovedBillingAccountEmail(Session sess, String serverName, String launchAppURL, BillingAccount billingAccount, Lab lab, String approverEmail) throws AddressException, NamingException, MessagingException {
		sendApprovedBillingAccountEmail(sess, serverName, launchAppURL, billingAccount, lab, null, approverEmail);
	}

}
