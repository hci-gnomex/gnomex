package hci.gnomex.controller;

import hci.dictionary.utility.DictionaryManager;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.CoreFacility;
import hci.gnomex.model.Lab;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.LabCoreFacilityParser;
import hci.gnomex.utility.MailUtil;
import hci.gnomex.utility.MailUtilHelper;
import hci.gnomex.utility.PropertyDictionaryHelper;
import hci.gnomex.utility.Util;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

import javax.mail.MessagingException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.apache.log4j.Logger;



public class SubmitWorkAuthForm extends GNomExCommand implements Serializable {



  // the static field for logging in Log4J
  private static Logger LOG = Logger.getLogger(SubmitWorkAuthForm.class);

  private BillingAccount                 billingAccountScreen;
  private BillingAccount                 billingAccount;
  private String                         serverName;
  private String                         launchAppURL;
  private StringBuffer                   requestURL;
  private Lab                            lab;
  private CoreFacility                   facility;
  private LabCoreFacilityParser             coreFacilityParser;

  public void validate() {
  }

  public void loadCommand(HttpServletRequest request, HttpSession session) {

    billingAccountScreen = new BillingAccount();
    HashMap errors = this.loadDetailObject(request, billingAccountScreen);
    this.addInvalidFields(errors);

    if (request.getParameter("totalDollarAmountDisplay") != null && !request.getParameter("totalDollarAmountDisplay").equals("")) {
      String tda = request.getParameter("totalDollarAmountDisplay");
      tda = tda.replaceAll("\\$", "");
      tda = tda.replaceAll(",", "");
      billingAccountScreen.setTotalDollarAmount(new BigDecimal(tda));
    }

    String coreFacilitiesXMLString = "";
    if (request.getParameter("coreFacilitiesXMLString") != null && !request.getParameter("coreFacilitiesXMLString").equals("")) {
      coreFacilitiesXMLString = request.getParameter("coreFacilitiesXMLString");

      StringReader reader = new StringReader(coreFacilitiesXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        Document coreFacilitiesDoc = sax.build(reader);
        coreFacilityParser = new LabCoreFacilityParser(coreFacilitiesDoc);

      } catch (Exception je ) {
        LOG.error( "Cannot parse coreFacilitiesXMLString", je );
        this.addInvalidField( "coreFacilitiesXMLString", "Invalid coreFacilitiesXMLString");
      }
    }

    try {
      launchAppURL = this.getLaunchAppURL(request);  
    } catch (Exception e) {
      LOG.warn("Cannot get launch app URL in SubmitWorkAuthForm", e);
    }

    serverName = request.getServerName();
    this.requestURL = request.getRequestURL();

  }

  public Command execute() throws RollBackCommandException {

    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      DictionaryHelper dh = DictionaryHelper.getInstance(sess);

      lab = (Lab)sess.load(Lab.class, billingAccountScreen.getIdLab());

      PropertyDictionaryHelper pdh = PropertyDictionaryHelper.getInstance(sess);
      String configurable = pdh.getProperty(PropertyDictionary.CONFIGURABLE_BILLING_ACCOUNTS);
      boolean hasActivity = (billingAccountScreen.getAccountNumberActivity() != null && billingAccountScreen.getAccountNumberActivity().length() > 0);
      if (configurable == null || configurable.equals("N")) {
        if(billingAccountScreen.getAccountNumberBus() == null || billingAccountScreen.getAccountNumberBus().length() != 2 || 
            billingAccountScreen.getAccountNumberOrg() == null || billingAccountScreen.getAccountNumberOrg().length() != 5 ||
            billingAccountScreen.getAccountNumberFund() == null || billingAccountScreen.getAccountNumberFund().length() != 4 || 
            billingAccountScreen.getAccountNumberAccount() == null || billingAccountScreen.getAccountNumberAccount().length() != 5 ||
            (hasActivity && (billingAccountScreen.getAccountNumberAu() == null || billingAccountScreen.getAccountNumberAu().length() != 1)) || 
            ((billingAccountScreen.getAccountNumberProject() == null || billingAccountScreen.getAccountNumberProject().equals("")) && (billingAccountScreen.getAccountNumberActivity() == null || billingAccountScreen.getAccountNumberActivity().length() !=5)) ||
            ((billingAccountScreen.getAccountNumberActivity() == null || billingAccountScreen.getAccountNumberActivity().equals("")) && (billingAccountScreen.getAccountNumberProject() == null || billingAccountScreen.getAccountNumberProject().length() !=8)) ||
            billingAccountScreen.getExpirationDate() == null || billingAccountScreen.getStartDate() == null) {

          this.addInvalidField("Billing Account Error", "Please make sure all fields are entered and that the correct number of digits are used for each account number field.");
          this.setResponsePage(this.ERROR_JSP);
        }
      }

      if (this.isValid()) {
        if (this.getSecurityAdvisor().hasPermission(SecurityAdvisor.CAN_SUBMIT_WORK_AUTH_FORMS)) {

          billingAccountScreen.setSubmitterUID(this.getSecAdvisor().getUID());
          billingAccountScreen.setIsApproved("N");
          billingAccountScreen.setCreateDate(new java.sql.Date(System.currentTimeMillis()));


          if (coreFacilityParser != null) {
            coreFacilityParser.parse(sess);
          }

          for(Iterator i = coreFacilityParser.getCoreFacilityMap().keySet().iterator(); i.hasNext();) {
            Integer idCoreFacility = (Integer)i.next();
            facility = (CoreFacility)coreFacilityParser.getCoreFacilityMap().get(idCoreFacility);

            billingAccount = new BillingAccount();
            billingAccount.copyFieldsFrom( billingAccountScreen );

            billingAccount.setIdBillingAccount( null );
            billingAccount.setIdCoreFacility( idCoreFacility );

            sess.save(billingAccount);

            sess.flush();

            String emailWarning = "";
            try {
              this.sendConfirmationEmail(sess, facility);
            } catch (MessagingException me) {
              emailWarning = "**Due to an invalid email address, GNomEx was unable to send an email notifying " + me.getMessage() + " that a billing account was submitted.";
            }

            this.xmlResult = "<SUCCESS idBillingAccount=\"" + billingAccount.getIdBillingAccount() + "\" coreFacilityName=\"" + facility.getDisplay() + "\" emailWarning=\"" + emailWarning + "\"" + "/>";

          }


          setResponsePage(this.SUCCESS_JSP);
        } else {
          this.addInvalidField("Insufficient permissions", "Insufficient permission to submit billing account form.");
          setResponsePage(this.ERROR_JSP);
        }
      }

    }catch (Exception e){
      LOG.error("An exception has occurred in SubmitWorkAuthForm ", e);

      throw new RollBackCommandException(e.getMessage());

    }finally {
      try {
        HibernateSession.closeSession();        
      } catch(Exception e){
        LOG.error("Error", e);
      }
    }

    return this;
  }


  private void sendConfirmationEmail(Session sess, CoreFacility facility) throws NamingException, MessagingException, IOException {
    StringBuffer invalidEmails = new StringBuffer();

    DictionaryHelper dictionaryHelper = DictionaryHelper.getInstance(sess);
    PropertyDictionaryHelper propertyDictionaryHelper = PropertyDictionaryHelper.getInstance(sess);

    String launchBillingAccountDetail = Util.addURLParameter(this.launchAppURL, "?launchWindow=" + Constants.WINDOW_BILLING_ACCOUNT_DETAIL + "&idLab=" + lab.getIdLab());
    StringBuffer submitterNote = new StringBuffer();
    StringBuffer body = new StringBuffer();
    String submitterSubject = "GNomEx Billing Account '" + billingAccount.getAccountName() + "' for " + lab.getName(false, true) + " submitted";    
    String coreSubject      = "GNomEx Billing Account '" + billingAccount.getAccountName() + "' for " + lab.getName(false, true) + " pending";    

    String submitterEmail = billingAccount.getSubmitterEmail();
    String emailRecipients = submitterEmail;
    if(!MailUtil.isValidEmail(emailRecipients)){
      LOG.error(emailRecipients);
    }

    String facilityEmail = propertyDictionaryHelper.getCoreFacilityProperty(facility.getIdCoreFacility(), PropertyDictionary.CONTACT_EMAIL_CORE_FACILITY_WORKAUTH);
    if (facilityEmail == null || facilityEmail.equals("")) {
      facilityEmail = facility.getContactEmail();
    }

    submitterNote.append("The following billing account " +
        "has been submitted to the " + facility.getDisplay() + " Core" +  
        ".  After the account information is reviewed and approved, " +
        "you will be notified by email that experiment " +
    "requests can now be submitted against this account in GNomEx.");

    body.append("<br />");
    body.append("<br />");
    body.append("<table border=0>");
    body.append("<tr><td>Lab:</td><td>" + lab.getName(false, true) + "</td></tr>");
    body.append("<tr><td>Core Facility:</td><td>" + facility.getDisplay() + "</td></tr>");
    body.append("<tr><td>Account:</td><td>" + billingAccount.getAccountName() + "</td></tr>");
    body.append("<tr><td>Chartfield:</td><td>" + billingAccount.getAccountNumber() + "</td></tr>");
    if (billingAccount.getIdFundingAgency() != null) {
      body.append("<tr><td>Funding Agency:</td><td>" + DictionaryManager.getDisplay("hci.gnomex.model.FundingAgency", billingAccount.getIdFundingAgency().toString()) + "</td></tr>");
    }
    if (billingAccount.getExpirationDateOther() != null && billingAccount.getExpirationDateOther().length() > 0) {
      body.append("<tr><td>Effective until:</td><td>" + billingAccount.getExpirationDateOther() + "</td></tr>");
    }
    if (billingAccount.getTotalDollarAmountDisplay() != null && billingAccount.getTotalDollarAmountDisplay().length() > 0) {
      body.append("<tr><td>Total Dollar Amount:</td><td>" + billingAccount.getTotalDollarAmountDisplay() + "</td></tr>");
    }
    body.append("<tr><td>Submitter UID:</td><td>" + billingAccount.getSubmitterUID() + "</td></tr>");
    body.append("<tr><td>Submitter Email:</td><td>" + billingAccount.getSubmitterEmail() + "</td></tr>");
    body.append("</table>");

    String replyEmail = facility.getContactEmail();

    if(!MailUtil.isValidEmail(replyEmail)){
        replyEmail = DictionaryHelper.getInstance(sess).getPropertyDictionary(PropertyDictionary.GENERIC_NO_REPLY_EMAIL);
    }
    // Email submitter
    try {
    	MailUtilHelper helper = new MailUtilHelper(	
    			emailRecipients,
    			replyEmail,
    			submitterSubject,
    			submitterNote.toString() + body.toString(),
    			null,
				true, 
				dictionaryHelper,
				serverName 									);
    	MailUtil.validateAndSendEmail(helper);
    } catch (Exception e) {
        // DEAD CODE: Even when mail isn't sent, we don't seem to get an exception 
        LOG.warn("Unable to send email notification to billing account submitter " + billingAccount.getSubmitterEmail() + " UID " + billingAccount.getSubmitterUID(), e);
        body.append("\n\n** NOTE:  GNomEx was unable to send email to submitter " + submitterEmail + " **");
    }

    // Email core facility for lab with approve links
    String contactEmail = facilityEmail;

    if (!contactEmail.equals("")) {
      if(!MailUtil.isValidEmail(contactEmail)){
        LOG.error("Invalid email " + contactEmail);
      }

      String[] emails = contactEmail.split(",");

      //Only one email in contactEmail check
      if(emails.length == 0) {
        emails[0] = contactEmail;
      }

      for(int i = 0; i < emails.length; i++) {
        StringBuffer coreNote= new StringBuffer();
        String approveBillingAccountURL = requestURL.substring(0, requestURL.indexOf("SubmitWorkAuthForm.gx"));
        approveBillingAccountURL += "/" + Constants.APPROVE_BILLING_ACCOUNT_SERVLET + "?idBillingAccount=" + billingAccount.getIdBillingAccount() + "&approverEmail=" + emails[i] ;

        coreNote.append("The following billing account " +
              "has been submitted to the " + facility.getDisplay() + " Core" +  
              " and is pending approval.  You can quickly approve this account by clicking <a href=" + approveBillingAccountURL + ">here</a> or you can view the account in more detail in " +
              "<a href=" + launchBillingAccountDetail + ">GNomEx</a>.");
        
        MailUtilHelper helper = new MailUtilHelper(	
        		emails[i],
        		replyEmail,
        		coreSubject,
        		coreNote.toString() + body.toString(),
        		null,
				true, 
				dictionaryHelper,
				serverName 								);
        MailUtil.validateAndSendEmail(helper);
                    
      }
    }
    
  }

}