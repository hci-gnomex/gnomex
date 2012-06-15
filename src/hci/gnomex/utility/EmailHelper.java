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
    
    StringBuffer introNote = new StringBuffer();
    String downloadRequestURL = launchAppURL + "?requestNumber=" + request.getNumber() + "&launchWindow=" + Constants.WINDOW_FETCH_RESULTS;
    introNote.append("Request " + request.getNumber() + " has been completed by the " + dictionaryHelper.getPropertyDictionary(PropertyDictionary.CORE_FACILITY_NAME) + ".");
    introNote.append("<br>To fetch the results, click <a href=\"" + downloadRequestURL + "\">" + Constants.APP_NAME + " - " + Constants.WINDOW_NAME_FETCH_RESULTS + "</a>.");
    
    RequestEmailBodyFormatter emailFormatter = new RequestEmailBodyFormatter(sess, secAdvisor, appURL, dictionaryHelper, request, null, request.getSamples(), request.getHybridizations(), request.getSequenceLanes(), introNote.toString());
    emailFormatter.setIncludeMicroarrayCoreNotes(false);
        
    String subject = dictionaryHelper.getRequestCategory(request.getCodeRequestCategory()) + " Request " + request.getNumber() + " completed";
    
    boolean send = false;
    if (dictionaryHelper.isProductionServer(serverName)) {
      send = true;
    } else {
      if (request.getAppUser().getEmail().equals(dictionaryHelper.getPropertyDictionary(PropertyDictionary.CONTACT_EMAIL_SOFTWARE_TESTER))) {
        send = true;
        subject = "TEST - " + subject;
      }
    }
    
    if (send) {
      MailUtil.send(request.getAppUser().getEmail(), 
          null,
          dictionaryHelper.getPropertyDictionary(PropertyDictionary.CONTACT_EMAIL_CORE_FACILITY), 
          subject, 
          emailFormatter.format(),
          true);
      
    }
    
  }

}
