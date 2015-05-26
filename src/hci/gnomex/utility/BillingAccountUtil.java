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
	    	emailInfo = "[If this were a production environment then this email would have been sent to: " + emailRecipients + "]\n\n";
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

	    body.append("\n");
	    body.append("\n");
	    body.append("Lab:\t\t\t" + lab.getName(false, false) + "\n");
	    body.append("Core Facility:\t\t" + facility.getDisplay() + "\n");
	    body.append("Account:\t\t" + billingAccount.getAccountName() + "\n");
	    body.append("Chartfield:\t\t" + billingAccount.getAccountNumber() + "\n");
	    if (billingAccount.getIdFundingAgency() != null) {
	    	body.append("Funding Agency:\t" + DictionaryManager.getDisplay("hci.gnomex.model.FundingAgency", billingAccount.getIdFundingAgency().toString()) + "\n");
	    }
	    if (billingAccount.getExpirationDateOther() != null && billingAccount.getExpirationDateOther().length() > 0) {
	    	body.append("Effective until:\t\t" + billingAccount.getExpirationDateOther() + "\n");
	    }
	    body.append("Submitter UID:\t\t" + billingAccount.getSubmitterUID() + "\n");
	    body.append("Submitter Email:\t" + billingAccount.getSubmitterEmail() + "\n");
	    body.append("Submit Date:\t\t" + billingAccount.getCreateDate() + "\n");
	    
	    body.append("Approved By:\t\t");
	    if (approver != null) {
	    	body.append(approver.getDisplayName());
	        if (approver.getEmail() != null && !approver.getEmail().equals("")) {
	        	body.append(" (" + approver.getEmail() + ")" );
	        }
	    } else if (approverEmail != null) {
	    	body.append(approverEmail);
	    }
	    body.append("\n");
	    body.append("Approved Date:\t" + billingAccount.getApprovedDate() + "\n");

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
	    		   		false);

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
	    		  emailInfo = "[If this were a production environment then this email would have been sent to: " + contactEmail + "]\n\n";
	    		  contactEmail = dictionaryHelper.getProperty(PropertyDictionary.CONTACT_EMAIL_SOFTWARE_TESTER);
	    	  }
	    	  MailUtil.send(contactEmail, 
	    			  		null,
	    			  		from, 
	    			  		isTestEmail ? submitterSubject + " (for lab contact " + lab.getContactEmail() + ")" : submitterSubject, 
	    			  		emailInfo + submitterNote.toString() + body.toString(),
	    			  		false);
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
