package hci.gnomex.controller;

import hci.dictionary.utility.DictionaryManager;
import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.CoreFacility;
import hci.gnomex.model.Lab;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.MailUtil;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.IOException;
import java.sql.Date;

import javax.mail.MessagingException;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

public class ApproveBillingAccount extends HttpServlet {

  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveLab.class);
  private String idBillingAccount = "";
  private BillingAccount ba;
  private String message = "";
  private Boolean approveAccount = false;
  private String serverName = "";
  private String launchAppURL = "";

  protected void doGet( HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException {
    doPost(req, res);
  }


  protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
    try {
      Session sess =  HibernateSession.currentSession("approveBillingAccountServlet");
      idBillingAccount = ( String ) ( ( request.getParameter( "idBillingAccount" ) != null ) ? request.getParameter( "idBillingAccount" ) : "" );
      //      idAppUser = ( String ) ( ( request.getParameter( "idAppUser" ) != null ) ? request.getParameter( "idAppUser" ) : "" );

      if(request.getParameter("approveAccount") != null && !request.getParameter("approveAccount").equals("") && request.getParameter("approveAccount").equals("Y")) {
        approveAccount = true;
      }

      PropertyDictionaryHelper pdh = PropertyDictionaryHelper.getInstance(sess);
      String doNotReplyEmail = pdh.getProperty(PropertyDictionary.GENERIC_NO_REPLY_EMAIL);
      serverName = request.getServerName();

      ba = ( BillingAccount ) sess.createQuery( "from BillingAccount ba where ba.idBillingAccount = '" + idBillingAccount + "'" ).uniqueResult();

      if( ba == null ) {
        message = "This billing account does not exist";
      } else if(ba.getIsApproved() != null && ba.getIsApproved().equals("Y")){
        message = "This billing account has already been approved";
      } else {
        Lab lab = (Lab) sess.load(Lab.class, ba.getIdLab());
        ba.setApprovedDate(new Date(System.currentTimeMillis()));
        ba.setIsApproved("Y");
        sendApprovedBillingAccountEmail(sess, ba, lab);

        message = "Billing Account " + ba.getAccountNameDisplay() + " has been successfully approved.";

      }


    } catch (Exception e) {
      message = "There was an issue activating the billing account.  Please activate through the GNomEx app and contact GNomEx support.  Thanks.";
      e.printStackTrace();
    } finally {
      try {
        HibernateSession.closeSession();
        HibernateSession.closeTomcatSession();
        String url= "/approve_billing_account.jsp"; //relative url for display jsp page
        ServletContext sc = getServletContext();
        RequestDispatcher rd = sc.getRequestDispatcher(url);
        request.setAttribute("message",message);

        rd.forward(request, response);
      } catch (Exception e1) {
        System.out.println("ApproveUser warning - cannot close hibernate session");
      }
    }
  }


  private void sendApprovedBillingAccountEmail(Session sess, BillingAccount billingAccount, Lab lab) throws NamingException, MessagingException {

    PropertyDictionaryHelper dictionaryHelper = PropertyDictionaryHelper.getInstance(sess);


    StringBuffer submitterNote = new StringBuffer();
    StringBuffer body = new StringBuffer();
    String submitterSubject = "GNomEx Billing Account '" + billingAccount.getAccountName() + "' for " + lab.getName(false, true) + " approved";    

    boolean send = false;
    String submitterEmail = billingAccount.getSubmitterEmail();

    CoreFacility facility = (CoreFacility)sess.load(CoreFacility.class, billingAccount.getIdCoreFacility());

    String facilityEmail = dictionaryHelper.getCoreFacilityProperty(facility.getIdCoreFacility(), PropertyDictionary.CONTACT_EMAIL_CORE_FACILITY);


    boolean isTestEmail = false;
    String emailInfo = "";
    String emailRecipients = submitterEmail;
    if(!MailUtil.isValidEmail(submitterEmail)){
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
        "has been approved by the " + facility.getDisplay() + " Core" +  
        ".  Lab members can now submit experiment " +
        "requests against this account in GNomEx " + launchAppURL + ".");


    body.append("\n");
    body.append("\n");
    body.append("Lab:               " + lab.getName(false, false) + "\n");
    body.append("Core Facility      " + facility.getDisplay() + "\n");
    body.append("Account:           " + billingAccount.getAccountName() + "\n");
    body.append("Chartfield:        " + billingAccount.getAccountNumber() + "\n");
    if (billingAccount.getIdFundingAgency() != null) {
      body.append("Funding Agency:    " + DictionaryManager.getDisplay("hci.gnomex.model.FundingAgency", billingAccount.getIdFundingAgency().toString()) + "\n");
    }
    if (billingAccount.getExpirationDateOther() != null && billingAccount.getExpirationDateOther().length() > 0) {
      body.append("Effective until:   " + billingAccount.getExpirationDateOther() + "\n");
    }
    body.append("Submitter UID:     " + billingAccount.getSubmitterUID() + "\n");
    body.append("Submitter Email:   " + billingAccount.getSubmitterEmail() + "\n");

    String from = dictionaryHelper.getCoreFacilityProperty(facility.getIdCoreFacility(), PropertyDictionary.CONTACT_EMAIL_CORE_FACILITY);


    if (send) {
      // Email submitter
      if(!MailUtil.isValidEmail(from)){
        from = DictionaryHelper.getInstance(sess).getPropertyDictionary(PropertyDictionary.GENERIC_NO_REPLY_EMAIL);
      }
      MailUtil.send(emailRecipients, 
          null,
          from, 
          submitterSubject, 
          emailInfo + submitterNote.toString() + body.toString(),
          false); 


      // Email lab contact email address(es)
      if (lab.getBillingNotificationEmail() != null && !lab.getBillingNotificationEmail().equals("")) {
        String contactEmail = lab.getBillingNotificationEmail();
        if (isTestEmail) {
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
}

