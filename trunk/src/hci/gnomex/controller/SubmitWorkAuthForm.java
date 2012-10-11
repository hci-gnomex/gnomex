package hci.gnomex.controller;

import hci.dictionary.utility.DictionaryManager;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.CoreFacility;
import hci.gnomex.model.Lab;
import hci.gnomex.model.PriceSheet;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.MailUtil;
import hci.gnomex.utility.PriceSheetCategoryParser;
import hci.gnomex.utility.PropertyDictionaryHelper;
import hci.gnomex.utility.RequestEmailBodyFormatter;

import java.io.Serializable;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.mail.MessagingException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;




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
    
    if(request.getParameter("accountNumberBus").length() != 2 || request.getParameter("accountNumberOrg").length() != 5 ||
        request.getParameter("accountNumberFund").length() != 4 || request.getParameter("accountNumberAccount").length() != 5 ||
        request.getParameter("accountNumberAu").length() != 1 || request.getParameter("accountNumberYear").length() != 4 ||
        (request.getParameter("accountNumberActivity").length() != 5 && request.getParameter("accountNumberProject").equals("")) ||
        (request.getParameter("accountNumberProject").length() != 8) && request.getParameter("accountNumberActivity").equals("") ){
      
      this.setResponsePage(this.ERROR_JSP);
      this.addInvalidField("Account Number Length", "There is something wrong with your account number.  Please double check");
      return;
    }
        
    
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
      
      if (this.getSecurityAdvisor().hasPermission(SecurityAdvisor.CAN_SUBMIT_WORK_AUTH_FORMS)) {

        billingAccount.setSubmitterUID(this.getSecAdvisor().getUID());
        billingAccount.setIsApproved("N");
        billingAccount.setCreateDate(new java.sql.Date(System.currentTimeMillis()));
        
        sess.save(billingAccount);
         
        sess.flush();
        
        facility = (CoreFacility)sess.load(CoreFacility.class, billingAccount.getIdCoreFacility());


        this.xmlResult = "<SUCCESS idBillingAccount=\"" + billingAccount.getIdBillingAccount() + "\" coreFacilityName=\"" + facility.getDisplay() + "\" />";
      
        this.sendConfirmationEmail(sess);
        
        setResponsePage(this.SUCCESS_JSP);
      } else {
        this.addInvalidField("Insufficient permissions", "Insufficient permission to submit work authorization form.");
        setResponsePage(this.ERROR_JSP);
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
    
    DictionaryHelper dictionaryHelper = DictionaryHelper.getInstance(sess);
    PropertyDictionaryHelper propertyDictionaryHelper = PropertyDictionaryHelper.getInstance(sess);
    
    String launchBillingAccountDetail = this.launchAppURL + "?launchWindow=" + Constants.WINDOW_BILLING_ACCOUNT_DETAIL + "&idLab=" + lab.getIdLab();  
    
    
    StringBuffer submitterNote = new StringBuffer();
    StringBuffer coreNote= new StringBuffer();
    StringBuffer body = new StringBuffer();
    String submitterSubject = "GNomEx Work authorization '" + billingAccount.getAccountName() + "' for " + lab.getName() + " submitted";    
    String coreSubject      = "GNomEx Work authorization '" + billingAccount.getAccountName() + "' for " + lab.getName() + " pending";    
    
    boolean send = false;
    boolean testEmail = false;
    String submitterEmail = billingAccount.getSubmitterEmail();
    
    String facilityEmail = propertyDictionaryHelper.getCoreFacilityProperty(facility.getIdCoreFacility(), PropertyDictionary.CONTACT_EMAIL_CORE_FACILITY_WORKAUTH);
    if (facilityEmail == null || facilityEmail.equals("")) {
      facilityEmail = propertyDictionaryHelper.getCoreFacilityProperty(facility.getIdCoreFacility(), PropertyDictionary.CONTACT_EMAIL_CORE_FACILITY);
    }
      
    if (dictionaryHelper.isProductionServer(serverName)) {
      send = true;
    } else {
      if (submitterEmail.equals(dictionaryHelper.getPropertyDictionary(PropertyDictionary.CONTACT_EMAIL_SOFTWARE_TESTER))) {
        send = true;
        testEmail = true;
        submitterSubject = "TEST - " + submitterSubject;
        coreSubject = "TEST - " + coreSubject;
        facilityEmail = dictionaryHelper.getPropertyDictionary(PropertyDictionary.CONTACT_EMAIL_SOFTWARE_TESTER);
      }
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
    body.append("<tr><td>Funding Agency:</td><td>" + DictionaryManager.getDisplay("hci.gnomex.model.FundingAgency", billingAccount.getIdFundingAgency().toString()) + "</td></tr>");
    body.append("<tr><td>Effective until:</td><td>" + billingAccount.getExpirationDateOther() + "</td></tr>");
    body.append("<tr><td>Total Dollar Amount:</td><td>" + billingAccount.getTotalDollarAmountDisplay() + "</td></tr>");
    body.append("<tr><td>Submitter UID:</td><td>" + billingAccount.getSubmitterUID() + "</td></tr>");
    body.append("<tr><td>Submitter Email:</td><td>" + billingAccount.getSubmitterEmail() + "</td></tr>");
    body.append("</table>");
    
    String replyEmail = propertyDictionaryHelper.getCoreFacilityProperty(facility.getIdCoreFacility(), PropertyDictionary.CONTACT_EMAIL_CORE_FACILITY);
    
    if (send) {
      // Email submitter
      try {
        MailUtil.send(submitterEmail, 
            null,
            replyEmail, 
            submitterSubject, 
            submitterNote.toString() + body.toString(),
            true);             
      } catch (Exception e) {
        // DEAD CODE: Even when mail isn't sent, we don't seem to get an exception 
        log.warn("Unable to send email notification to work authorization submitter " + billingAccount.getSubmitterEmail() + " UID " + billingAccount.getSubmitterUID());
        body.append("\n\n** NOTE:  GNomEx was unable to send email to submitter " + submitterEmail + " **");
      }
      
      // Email lab contact email address(es)
      if (lab.getContactEmail() != null && !lab.getContactEmail().equals("")) {
        String contactEmail = lab.getContactEmail();
        if (testEmail) {
          contactEmail = dictionaryHelper.getPropertyDictionary(PropertyDictionary.CONTACT_EMAIL_SOFTWARE_TESTER);
        }
        MailUtil.send(contactEmail, 
            null,
            replyEmail, 
            submitterSubject, 
            submitterNote.toString() + body.toString(),
            true);   

      }

      
      // Email core facility
      if (!facilityEmail.equals("")) {
        MailUtil.send(facilityEmail, 
            null,
            replyEmail,
            coreSubject,
            coreNote.toString() + body.toString(),
            true);           
      }
      
     

    }
    
  }


}