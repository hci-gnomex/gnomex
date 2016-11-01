package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.CoreFacility;
import hci.gnomex.model.Invoice;
import hci.gnomex.model.Lab;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.BillingInvoiceEmailFormatter;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.MailUtil;
import hci.gnomex.utility.MailUtilHelper;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.File;
import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.jdom.output.XMLOutputter;

@SuppressWarnings("serial")
public class SendBillingInvoiceEmail extends GNomExCommand implements Serializable {

	private static Logger LOG = Logger.getLogger(SendBillingInvoiceEmail.class);
	
	public 	String 							SUCCESS_JSP = "/getHTML.jsp";
	
	private Integer 						idLab;
	private Integer 						idBillingAccount;
	private Integer 						idBillingPeriod;
	private Integer 						idCoreFacility;
	
	private SecurityAdvisor 				secAdvisor;
	private DictionaryHelper				dh;
	private BillingPeriod 					billingPeriod;
	private CoreFacility 					coreFacility;
	private Lab 							lab;
	private BillingAccount 					billingAccount;
	
	private String							emailAddress;
	private boolean							includeBillingAccountContact;
	private boolean          				respondInHTML;
	private String							serverName;

	public void validate() {
	}

	public void loadCommand(HttpServletRequest request, HttpSession session) {
		if (request.getParameter("idLab") != null) {
			idLab = new Integer(request.getParameter("idLab"));
		} else {
			this.addInvalidField("idLab", "idLab is required");
		}
		
		if (request.getParameter("idBillingAccount") != null && request.getParameter("idBillingAccount").length() > 0) {
			idBillingAccount = new Integer(request.getParameter("idBillingAccount"));
		} else {
			this.addInvalidField("idBillingAccount", "idBillingAccount is required");
		}
		
		if (request.getParameter("idBillingPeriod") != null) {
			idBillingPeriod = new Integer(request.getParameter("idBillingPeriod"));
		} else {
			this.addInvalidField("idBillingPeriod", "idBillingPeriod is required");
		}
		
		if (request.getParameter("idCoreFacility") != null) {
			idCoreFacility = new Integer(request.getParameter("idCoreFacility"));
		} else {
			this.addInvalidField("idCoreFacility", "idCoreFacility is required");
		}
		
		if (request.getParameter("emailAddress") != null && !request.getParameter("emailAddress").equals("")) {
			emailAddress = request.getParameter("emailAddress");
		}
	if (request.getParameter("includeBillingAccountContact") != null
			&& request.getParameter("includeBillingAccountContact").equalsIgnoreCase("Y")) {
			includeBillingAccountContact = true;
		} else {
			includeBillingAccountContact = false;
		}
		if (request.getParameter("respondInHTML") != null && request.getParameter("respondInHTML").equalsIgnoreCase("Y")) {
			respondInHTML = true;
		} else {
			respondInHTML = false;
		}
		
		secAdvisor = (SecurityAdvisor) session.getAttribute(SecurityAdvisor.SECURITY_ADVISOR_SESSION_KEY);
		serverName = request.getServerName();
	}

	public Command execute() throws RollBackCommandException {
		try {
		    
		    if (isValid()) {
		    	
		    	if (secAdvisor.hasPermission(SecurityAdvisor.CAN_MANAGE_BILLING)) {
		    		
		    		Session sess = secAdvisor.getHibernateSession(this.getUsername());
		    		dh = DictionaryHelper.getInstance(sess);
		    		billingPeriod = dh.getBillingPeriod(idBillingPeriod);
		    		coreFacility = (CoreFacility) sess.get(CoreFacility.class, idCoreFacility);
		    		lab = (Lab) sess.get(Lab.class, new Integer(idLab));
	    			billingAccount = (BillingAccount) sess.get(BillingAccount.class, idBillingAccount);
	    			TreeMap requestMap = new TreeMap();
	                TreeMap billingItemMap = new TreeMap();
	                TreeMap relatedBillingItemMap = new TreeMap();
				ShowBillingInvoiceForm.cacheBillingItemMaps(sess, secAdvisor, idBillingPeriod, idLab, idBillingAccount,
						idCoreFacility, billingItemMap, relatedBillingItemMap, requestMap);
	    			String contactEmail = this.emailAddress;
	                if (contactEmail == null) {
	                	contactEmail = lab.getBillingNotificationEmail();
	                }
				this.sendInvoiceEmail(sess, contactEmail, coreFacility, billingPeriod, lab, billingAccount,
						billingItemMap, relatedBillingItemMap, requestMap);
			    }
		    	
		    }
		    
		    if (isValid()) {
				setResponsePage(this.SUCCESS_JSP);
		    } else {
		        setResponsePage(this.ERROR_JSP);
		    }
				
		} catch (Exception e) {
			LOG.error("An exception has occurred in SendBillingInvoiceEmail ", e);

			throw new RollBackCommandException(e.getMessage());
      }
			
		return this;
	}
	
	private void sendInvoiceEmail(	Session sess, String contactEmail, CoreFacility coreFacility,
		BillingPeriod billingPeriod, Lab lab, BillingAccount billingAccount, Map billingItemMap,
		Map relatedBillingItemMap, Map requestMap) throws Exception {

	Query query = sess
			.createQuery("from Invoice where idCoreFacility=:idCoreFacility and idBillingPeriod=:idBillingPeriod and idBillingAccount=:idBillingAccount");
		query.setParameter("idCoreFacility", idCoreFacility);
		query.setParameter("idBillingPeriod", idBillingPeriod);
		query.setParameter("idBillingAccount", idBillingAccount);
		Invoice invoice = (Invoice) query.uniqueResult();
	BillingInvoiceEmailFormatter emailFormatter = new BillingInvoiceEmailFormatter(sess, coreFacility, billingPeriod,
			lab, billingAccount, invoice, billingItemMap, relatedBillingItemMap, requestMap);
		String subject = emailFormatter.getSubject();
		String body = emailFormatter.format();

		String note = "";
		boolean send = false;
		String emailRecipients = contactEmail;
	if (includeBillingAccountContact && billingAccount.getLab() != null
			&& !lab.getIdLab().equals(billingAccount.getLab().getIdLab())) {
			emailRecipients = this.appendBillingAccountLabEmail(emailRecipients, billingAccount);
		}
		String ccList = emailFormatter.getCCList(sess);
		String fromAddress = coreFacility.getContactEmail();
		if (emailRecipients.contains(",")) {
			for (String e : emailRecipients.split(",")) {
				if (!MailUtil.isValidEmail(e.trim())) {
					LOG.error("Invalid email address " + e);
				}
			}
		} else if (!MailUtil.isValidEmail(emailRecipients)) {
			LOG.error("Invalid email address " + emailRecipients);
		}
		if (emailRecipients != null && !emailRecipients.equals("")) {
			send = true;
		} else {
			note = "Unable to email billing invoice. Billing contact email is blank for " + lab.getName(false, true);
		}

		Map[] billingItemMaps = { billingItemMap };
		Map[] relatedBillingItemMaps = { relatedBillingItemMap };
		Map[] requestMaps = { requestMap };

		if (send) {
			if (!MailUtil.isValidEmail(fromAddress)) {
				fromAddress = dh.getPropertyDictionary(PropertyDictionary.GENERIC_NO_REPLY_EMAIL);
			}
			try {
				File billingInvoice = ShowBillingInvoiceForm.makePDFBillingInvoice(
					sess,
					serverName,
					billingPeriod,
					coreFacility,
					false,
					lab,
					new Lab[0],
					billingAccount,
					new BillingAccount[0],
					PropertyDictionaryHelper.getInstance(sess).getCoreFacilityProperty(
							coreFacility.getIdCoreFacility(), PropertyDictionary.CONTACT_ADDRESS_CORE_FACILITY),
					PropertyDictionaryHelper.getInstance(sess).getCoreFacilityProperty(
							coreFacility.getIdCoreFacility(), PropertyDictionary.CONTACT_REMIT_ADDRESS_CORE_FACILITY),
					billingItemMaps, relatedBillingItemMaps, requestMaps);

			MailUtilHelper helper = new MailUtilHelper(emailRecipients, ccList, null, fromAddress, subject, body,
					billingInvoice, true, dh, serverName);
				MailUtil.validateAndSendEmail(helper);

				billingInvoice.delete();

				note = "Billing invoice emailed to " + emailRecipients + ".";

				// Set last email date
				if (invoice != null) {
					invoice.setLastEmailDate(new java.sql.Date(System.currentTimeMillis()));
					sess.save(invoice);
					sess.flush();
				}
			} catch (Exception e) {
				LOG.error("Unable to send invoice email to " + emailRecipients, e);
				note = "Unable to email invoice to " + emailRecipients + " due to the following error: " + e.toString();
			}
		}
		org.jdom.Document doc = null;
		if (respondInHTML) {
			if (note.length() == 0) {
				note = "&nbsp";
			}
			org.jdom.Element root = new org.jdom.Element("HTML");
			doc = new org.jdom.Document(root);

			org.jdom.Element head = new org.jdom.Element("HEAD");
			root.addContent(head);

			org.jdom.Element link = new org.jdom.Element("link");
			link.setAttribute("rel", "stylesheet");
			link.setAttribute("type", "text/css");
			link.setAttribute("href", "invoiceForm.css");
			head.addContent(link);

			org.jdom.Element title = new org.jdom.Element("TITLE");
			title.addContent("Email Billing Invoice - " + lab.getName(false, true) + " " + billingAccount.getAccountName());
			head.addContent(title);

			org.jdom.Element responseBody = new org.jdom.Element("BODY");
			root.addContent(responseBody);

			org.jdom.Element h = new org.jdom.Element("H3");
			h.addContent(note);
			responseBody.addContent(h);
		} else {
			org.jdom.Element root = new org.jdom.Element("BillingInvoiceEmail");
			doc = new org.jdom.Document(root);
			root.setAttribute("note", note);
		root.setAttribute("title",
				"Email Billing Invoice - " + lab.getName(false, true) + " " + billingAccount.getAccountName());
		}

		XMLOutputter out = new org.jdom.output.XMLOutputter();
		out.setOmitEncoding(true);
		this.xmlResult = out.outputString(doc);
	}
	
	private String appendBillingAccountLabEmail(String recipients, BillingAccount billingAccount) {
		StringBuffer allRecipients = new StringBuffer(recipients);

		if (allRecipients.length() > 0) {
			allRecipients.append(",");
		}

		Lab lab = billingAccount.getLab();
	if (lab.getBillingContactEmail() != null && !lab.getBillingContactEmail().equals("")
			&& allRecipients.indexOf(lab.getBillingContactEmail()) == -1) {
			allRecipients.append(lab.getBillingContactEmail());
	} else if (lab.getContactEmail() != null && !lab.getContactEmail().equals("")
			&& allRecipients.indexOf(lab.getContactEmail()) == -1) {
			allRecipients.append(lab.getContactEmail());
		}

		return allRecipients.toString();
	}

	/**
 * The callback method called after the loadCommand, and execute methods, this method allows you to manipulate the HttpServletResponse object prior to
 * forwarding to the result JSP (add a cookie, etc.)
	 *
	 * @param request
	 *            The HttpServletResponse for the command
	 * @return The processed response
	 */
	public HttpServletResponse setResponseState(HttpServletResponse response) {
		return response;
	}

}
