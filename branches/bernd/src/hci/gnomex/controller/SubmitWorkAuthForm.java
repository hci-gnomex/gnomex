package hci.gnomex.controller;

import hci.dictionary.utility.DictionaryManager;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.CoreFacility;
import hci.gnomex.model.Lab;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.MailUtil;
import hci.gnomex.utility.PropertyDictionaryHelper;
import hci.gnomex.utility.Util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;

import javax.mail.MessagingException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;




public class SubmitWorkAuthForm extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SubmitWorkAuthForm.class);

  private BillingAccount                 billingAccount;
  private String                         serverName;
  private String                         launchAppURL;
  private Lab                            lab;
  private CoreFacility                   facility;
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    billingAccount = new BillingAccount();
    HashMap errors = this.loadDetailObject(request, billingAccount);
    this.addInvalidFields(errors);
    
    if (request.getParameter("totalDollarAmountDisplay") != null && !request.getParameter("totalDollarAmountDisplay").equals("")) {
      String tda = request.getParameter("totalDollarAmountDisplay");
      tda = tda.replaceAll("\\$", "");
      tda = tda.replaceAll(",", "");
      billingAccount.setTotalDollarAmount(new BigDecimal(tda));
    }
    
    try {
      launchAppURL = this.getLaunchAppURL(request);  
    } catch (Exception e) {
      log.warn("Cannot get launch app URL in SubmitWorkAuthForm", e);
    }

    serverName = request.getServerName();

  }

  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      DictionaryHelper dh = DictionaryHelper.getInstance(sess);
      
      lab = (Lab)sess.load(Lab.class, billingAccount.getIdLab());
      
      PropertyDictionaryHelper pdh = PropertyDictionaryHelper.getInstance(sess);
      String configurable = pdh.getProperty(PropertyDictionary.CONFIGURABLE_BILLING_ACCOUNTS);
      boolean hasActivity = (billingAccount.getAccountNumberActivity() != null && billingAccount.getAccountNumberActivity().length() > 0);
      if (configurable == null || configurable.equals("N")) {
        if(billingAccount.getAccountNumberBus() == null || billingAccount.getAccountNumberBus().length() != 2 || 
            billingAccount.getAccountNumberOrg() == null || billingAccount.getAccountNumberOrg().length() != 5 ||
            billingAccount.getAccountNumberFund() == null || billingAccount.getAccountNumberFund().length() != 4 || 
            billingAccount.getAccountNumberAccount() == null || billingAccount.getAccountNumberAccount().length() != 5 ||
            (hasActivity && (billingAccount.getAccountNumberAu() == null || billingAccount.getAccountNumberAu().length() != 1)) || 
            ((billingAccount.getAccountNumberProject() == null || billingAccount.getAccountNumberProject().equals("")) && (billingAccount.getAccountNumberActivity() == null || billingAccount.getAccountNumberActivity().length() !=5)) ||
            ((billingAccount.getAccountNumberActivity() == null || billingAccount.getAccountNumberActivity().equals("")) && (billingAccount.getAccountNumberProject() == null || billingAccount.getAccountNumberProject().length() !=8)) ||
            billingAccount.getExpirationDate() == null || billingAccount.getStartDate() == null) {
          
          this.addInvalidField("Work Authorization Error", "Please make sure all fields are entered and that the correct number of digits are used for each account number field.");
          this.setResponsePage(this.ERROR_JSP);
        }
      }

      if (this.isValid()) {
        if (this.getSecurityAdvisor().hasPermission(SecurityAdvisor.CAN_SUBMIT_WORK_AUTH_FORMS)) {
  
          billingAccount.setSubmitterUID(this.getSecAdvisor().getUID());
          billingAccount.setIsApproved("N");
          billingAccount.setCreateDate(new java.sql.Date(System.currentTimeMillis()));
          
          sess.save(billingAccount);
           
          sess.flush();
          
          facility = (CoreFacility)sess.load(CoreFacility.class, billingAccount.getIdCoreFacility());
  
          String emailWarning = "";
          try {
            this.sendConfirmationEmail(sess);
          } catch (MessagingException me) {
            emailWarning = "**Due to an invalid email address, GNomEx was unable to send an email notifying " + me.getMessage() + " that a work authorization was submitted.";
          }
  
          this.xmlResult = "<SUCCESS idBillingAccount=\"" + billingAccount.getIdBillingAccount() + "\" coreFacilityName=\"" + facility.getDisplay() + "\" emailWarning=\"" + emailWarning + "\"" + "/>";
        
          
          setResponsePage(this.SUCCESS_JSP);
        } else {
          this.addInvalidField("Insufficient permissions", "Insufficient permission to submit work authorization form.");
          setResponsePage(this.ERROR_JSP);
        }
      }
      
    }catch (Exception e){
      log.error("An exception has occurred in SubmitWorkAuthForm ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }finally {
      try {
        HibernateSession.closeSession();        
      } catch(Exception e) {
        
      }
    }
    
    return this;
  }
  

  private void sendConfirmationEmail(Session sess) throws NamingException, MessagingException {
    StringBuffer invalidEmails = new StringBuffer();
    
    DictionaryHelper dictionaryHelper = DictionaryHelper.getInstance(sess);
    PropertyDictionaryHelper propertyDictionaryHelper = PropertyDictionaryHelper.getInstance(sess);
    
    String launchBillingAccountDetail = Util.addURLParameter(this.launchAppURL, "?launchWindow=" + Constants.WINDOW_BILLING_ACCOUNT_DETAIL + "&idLab=" + lab.getIdLab());  
    
    StringBuffer submitterNote = new StringBuffer();
    StringBuffer coreNote= new StringBuffer();
    StringBuffer body = new StringBuffer();
    String submitterSubject = "GNomEx Work authorization '" + billingAccount.getAccountName() + "' for " + lab.getName() + " submitted";    
    String coreSubject      = "GNomEx Work authorization '" + billingAccount.getAccountName() + "' for " + lab.getName() + " pending";    
    
    boolean send = false;
    boolean testEmail = false;
    String submitterEmail = billingAccount.getSubmitterEmail();
    String emailInfo = "";
    String emailRecipients = submitterEmail;
    if(!MailUtil.isValidEmail(emailRecipients)){
      log.error(emailRecipients);
    }
    
    String facilityEmail = propertyDictionaryHelper.getCoreFacilityProperty(facility.getIdCoreFacility(), PropertyDictionary.CONTACT_EMAIL_CORE_FACILITY_WORKAUTH);
    if (facilityEmail == null || facilityEmail.equals("")) {
      facilityEmail = propertyDictionaryHelper.getCoreFacilityProperty(facility.getIdCoreFacility(), PropertyDictionary.CONTACT_EMAIL_CORE_FACILITY);
    }
      
    if (dictionaryHelper.isProductionServer(serverName)) {
      send = true;
    } else {
      send = true;
      testEmail = true;
      submitterSubject = "TEST - " + submitterSubject;
      coreSubject = "TEST - " + coreSubject;
      emailInfo = "[If this were a production environment then this email would have been sent to: " + emailRecipients + "]<br><br>";
      emailRecipients = dictionaryHelper.getPropertyDictionary(PropertyDictionary.CONTACT_EMAIL_SOFTWARE_TESTER);
    }
    
    submitterNote.append("The following work authorization " +
        "has been submitted to the " + facility.getDisplay() + " Core" +  
        ".  After the account information is reviewed and approved, " +
        "you will be notified by email that experiment " +
        "requests can now be submitted against this account in GNomEx.");

    coreNote.append("The following work authorization " +
        "has been submitted to the " + facility.getDisplay() + " Core" +  
        " and is pending approval in GNomEx " + launchBillingAccountDetail + ".");

    body.append("<br />");
    body.append("<br />");
    body.append("<table border=0>");
    body.append("<tr><td>Lab:</td><td>" + lab.getName() + "</td></tr>");
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
    
    String replyEmail = propertyDictionaryHelper.getCoreFacilityProperty(facility.getIdCoreFacility(), PropertyDictionary.CONTACT_EMAIL_CORE_FACILITY);
    
    if (send) {
      if(!MailUtil.isValidEmail(replyEmail)){
        replyEmail = DictionaryHelper.getInstance(sess).getPropertyDictionary(PropertyDictionary.GENERIC_NO_REPLY_EMAIL);
      }
      // Email submitter
      try {
        MailUtil.send(emailRecipients, 
            null,
            replyEmail, 
            submitterSubject, 
            emailInfo + submitterNote.toString() + body.toString(),
            true);             
      } catch (Exception e) {
        // DEAD CODE: Even when mail isn't sent, we don't seem to get an exception 
        log.warn("Unable to send email notification to work authorization submitter " + billingAccount.getSubmitterEmail() + " UID " + billingAccount.getSubmitterUID());
        body.append("\n\n** NOTE:  GNomEx was unable to send email to submitter " + submitterEmail + " **");
      }
      
      // Email lab contact email address(es)
      if (lab.getWorkAuthSubmitEmail() != null && !lab.getWorkAuthSubmitEmail().equals("")) {
        String contactEmail = lab.getWorkAuthSubmitEmail();
        if(!MailUtil.isValidEmail(contactEmail)){
          log.error("Invalid email " + contactEmail);
        }
        
        if (testEmail) {
          emailInfo = "[If this were a production environment then this email would have been sent to: " + contactEmail + "]<br><br>"; 
          contactEmail = dictionaryHelper.getPropertyDictionary(PropertyDictionary.CONTACT_EMAIL_SOFTWARE_TESTER);
        }
        MailUtil.send(contactEmail, 
            null,
            replyEmail, 
            submitterSubject, 
            emailInfo + submitterNote.toString() + body.toString(),
            true);   

      }

      
      // Email core facility
      if (!facilityEmail.equals("")) {
        if(!MailUtil.isValidEmail(facilityEmail)){
          log.error("Invalid email " + facilityEmail);
        }
        if(testEmail){
          emailInfo = "[If this were a production environment then this email would have been sent to: " + facilityEmail + "]<br><br>";
          facilityEmail = dictionaryHelper.getPropertyDictionary(PropertyDictionary.CONTACT_EMAIL_SOFTWARE_TESTER);
        }
        MailUtil.send(facilityEmail, 
            null,
            replyEmail,
            coreSubject,
            emailInfo + coreNote.toString() + body.toString(),
            true);           
      }
      
     

    }
    
  }


}