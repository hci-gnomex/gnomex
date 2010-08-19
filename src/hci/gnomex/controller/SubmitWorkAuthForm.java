package hci.gnomex.controller;

import hci.dictionary.utility.DictionaryManager;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.Lab;
import hci.gnomex.model.PriceSheet;
import hci.gnomex.model.Property;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.MailUtil;
import hci.gnomex.utility.PriceSheetCategoryParser;
import hci.gnomex.utility.RequestEmailBodyFormatter;

import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    billingAccount = new BillingAccount();
    HashMap errors = this.loadDetailObject(request, billingAccount);
    this.addInvalidFields(errors);
    
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
        
        this.xmlResult = "<SUCCESS idBillingAccount=\"" + billingAccount.getIdBillingAccount() + "\"/>";
      
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
    
    
    StringBuffer submitterNote = new StringBuffer();
    StringBuffer coreNote= new StringBuffer();
    StringBuffer body = new StringBuffer();
    String submitterSubject = "GNomEx Work authorization '" + billingAccount.getAccountName() + "' for " + lab.getName() + " submitted";    
    String coreSubject      = "GNomEx Work authorization '" + billingAccount.getAccountName() + "' for " + lab.getName() + " pending";    
    
    boolean send = false;
    String submitterEmail = billingAccount.getSubmitterEmail();
    String coreEmail = dictionaryHelper.getProperty(Property.CONTACT_EMAIL_CORE_FACILITY);
    if (serverName.equals(dictionaryHelper.getProperty(Property.PRODUCTION_SERVER))) {
      send = true;
    } else {
      if (submitterEmail.equals(dictionaryHelper.getProperty(Property.CONTACT_EMAIL_SOFTWARE_TESTER))) {
        send = true;
        submitterSubject = "TEST - " + submitterSubject;
        coreSubject = "TEST - " + coreSubject;
        coreEmail = dictionaryHelper.getProperty(Property.CONTACT_EMAIL_SOFTWARE_TESTER);
      }
    }
    
    submitterNote.append("The following work authorization " +
        "has been submitted to the " + dictionaryHelper.getProperty(Property.CORE_FACILITY_NAME) +  
        ".  After the account information is reviewed and approved, " +
        "you will be notified by email that experiment " +
        "requests can now be submitted against this account in GNomEx.");

    coreNote.append("The following work authorization " +
        " has been submitted to the " + dictionaryHelper.getProperty(Property.CORE_FACILITY_NAME) +  
        " and is pending approval in GNomEx " + launchAppURL + ".");

    body.append("\n");
    body.append("\n");
    body.append("Lab:               " + lab.getName() + "\n");
    body.append("Account:           " + billingAccount.getAccountName() + "\n");
    body.append("Chartfield:        " + billingAccount.getAccountNumber() + "\n");
    body.append("Funding Agency:    " + DictionaryManager.getDisplay("hci.gnomex.model.FundingAgency", billingAccount.getIdFundingAgency().toString()) + "\n");
    body.append("Effective until:   " + billingAccount.getExpirationDateOther() + "\n");
    body.append("Submitter UID:     " + billingAccount.getSubmitterUID() + "\n");
    body.append("Submitter Email:   " + billingAccount.getSubmitterEmail() + "\n");
    

    if (send) {
      // Email submitter
      try {
        MailUtil.send(submitterEmail, 
            null,
            dictionaryHelper.getProperty(Property.CONTACT_EMAIL_CORE_FACILITY), 
            submitterSubject, 
            submitterNote.toString() + body.toString(),
            false);             
      } catch (Exception e) {
        // DEAD CODE: Even when mail isn't sent, we don't seem to get an exception 
        log.warn("Unable to send email notification to work authorization submitter " + billingAccount.getSubmitterEmail() + " UID " + billingAccount.getSubmitterUID());
        body.append("\n\n** NOTE:  GNomEx was unable to send email to submitter " + submitterEmail + " **");
      }

      
      // Email core facility
      MailUtil.send(coreEmail, 
          null,
          dictionaryHelper.getProperty(Property.CONTACT_EMAIL_CORE_FACILITY),
          coreSubject,
          coreNote.toString() + body.toString(),
          false);      

    }
    
  }


}