package hci.gnomex.utility;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.Request;
import hci.gnomex.security.SecurityAdvisor;

import javax.mail.MessagingException;
import javax.naming.NamingException;

import org.hibernate.Session;

public class EmailHelper {
  
  public static void sendConfirmationEmail(Session sess, Request request, SecurityAdvisor secAdvisor, String launchAppURL, String appURL, String serverName) throws NamingException, MessagingException {
    
    DictionaryHelper dictionaryHelper = DictionaryHelper.getInstance(sess);
    
    String coreFacilityName = PropertyDictionaryHelper.getInstance(sess).getCoreFacilityProperty(request.getIdCoreFacility(), PropertyDictionary.CORE_FACILITY_NAME);

    
    StringBuffer introNote = new StringBuffer();
    String downloadRequestURL = launchAppURL + "?requestNumber=" + request.getNumber() + "&launchWindow=" + Constants.WINDOW_TRACK_REQUESTS;
    introNote.append("Order " + request.getNumber() + " has been completed by the " + coreFacilityName + ".");
    introNote.append("<br>To fetch the results, click <a href=\"" + downloadRequestURL + "\">" + Constants.APP_NAME + " - " + Constants.WINDOW_NAME_TRACK_REQUESTS + "</a>.");
    
    RequestEmailBodyFormatter emailFormatter = new RequestEmailBodyFormatter(sess, secAdvisor, appURL, dictionaryHelper, request, null, request.getSamples(), request.getHybridizations(), request.getSequenceLanes(), introNote.toString());
    emailFormatter.setIncludeMicroarrayCoreNotes(false);
        
    String subject = dictionaryHelper.getRequestCategory(request.getCodeRequestCategory()) + " Order " + request.getNumber() + " completed";
    
    boolean send = false;
    if (dictionaryHelper.isProductionServer(serverName)) {
      send = true;
    } else {
      if (request.getAppUser().getEmail().equals(dictionaryHelper.getPropertyDictionary(PropertyDictionary.CONTACT_EMAIL_SOFTWARE_TESTER))) {
        send = true;
        subject = "TEST - " + subject;
      }
    }
    
    String contactEmailCoreFacility = PropertyDictionaryHelper.getInstance(sess).getCoreFacilityProperty(request.getIdCoreFacility(), PropertyDictionary.CONTACT_EMAIL_CORE_FACILITY);

    
    if (send) {
      MailUtil.send(request.getAppUser().getEmail(), 
          null,
          contactEmailCoreFacility, 
          subject, 
          emailFormatter.format(),
          true);
      
    }
    
  }

  public static void sendRedoEmail(Session sess, Request request, SecurityAdvisor secAdvisor, String launchAppURL, String appURL, String serverName) throws NamingException, MessagingException {
    
    DictionaryHelper dictionaryHelper = DictionaryHelper.getInstance(sess);
    
    String coreFacilityName = PropertyDictionaryHelper.getInstance(sess).getCoreFacilityProperty(request.getIdCoreFacility(), PropertyDictionary.CORE_FACILITY_NAME);
    
    StringBuffer introNote = new StringBuffer();
    String downloadRequestURL = launchAppURL + "?requestNumber=" + request.getNumber() + "&launchWindow=" + Constants.WINDOW_TRACK_REQUESTS;
    introNote.append("The following samples on Order " + request.getNumber() + " have been marked for redo by " + coreFacilityName + ": ");
    introNote.append("<br>");
    introNote.append(request.getRedoSampleNames());
    introNote.append("<br><br>");
    introNote.append("When these requeued samples are completed, you will receive email notification that your results can be downloaded.  ");
    introNote.append("To view details about your order in GNomEx, click <a href=\"" + downloadRequestURL + "\">" + Constants.APP_NAME + " - " + Constants.WINDOW_NAME_TRACK_REQUESTS + "</a>.");
    
    RequestEmailBodyFormatter emailFormatter = new RequestEmailBodyFormatter(sess, secAdvisor, appURL, dictionaryHelper, request, null, request.getSamples(), request.getHybridizations(), request.getSequenceLanes(), introNote.toString());
    emailFormatter.setIncludeMicroarrayCoreNotes(false);
        
    String subject = dictionaryHelper.getRequestCategory(request.getCodeRequestCategory()) + " Order " + request.getNumber() + " - Sample(s) marked for redo";
    
    boolean send = false;
    if (dictionaryHelper.isProductionServer(serverName)) {
      send = true;
    } else {
      if (request.getAppUser().getEmail().equals(dictionaryHelper.getPropertyDictionary(PropertyDictionary.CONTACT_EMAIL_SOFTWARE_TESTER))) {
        send = true;
        subject = "TEST - " + subject;
      }
    }
    
    String contactEmailCoreFacility = PropertyDictionaryHelper.getInstance(sess).getCoreFacilityProperty(request.getIdCoreFacility(), PropertyDictionary.CONTACT_EMAIL_CORE_FACILITY);

    
    if (send) {
      MailUtil.send(request.getAppUser().getEmail(), 
          null,
          contactEmailCoreFacility, 
          subject, 
          emailFormatter.format(),
          true);
      
    }
    
  }

}
