package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.MailUtil;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;

public class EmailTopicOwner extends GNomExCommand implements Serializable {
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetRequest.class);

  private Integer idAppUser;
  private String fromAddress;
  private String body;
  private String subject;
  private String serverName;


  public void validate() {
  }

  public void loadCommand(HttpServletRequest request, HttpSession session) {
    if (request.getParameter("idAppUser") != null && !request.getParameter("idAppUser").equals("")) {
      idAppUser = new Integer(request.getParameter("idAppUser"));
    } 
    if (request.getParameter("fromAddress") != null && !request.getParameter("fromAddress").equals("")) {
      fromAddress = request.getParameter("fromAddress");
    } 
    if (request.getParameter("body") != null && !request.getParameter("body").equals("")) {
      body = request.getParameter("body");
    } 
    if (request.getParameter("subject") != null && !request.getParameter("subject").equals("")) {
      subject = request.getParameter("subject");
    }
    
    serverName = request.getServerName();

  }

  public Command execute() throws RollBackCommandException {
    try {

      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
      DictionaryHelper dh = DictionaryHelper.getInstance(sess);
      
      AppUser recipient = (AppUser)sess.get(AppUser.class, idAppUser);
      
      if(recipient.getEmail() == null || recipient.getEmail().equals("")){
        this.addInvalidField("No Email address on file", "There is no email address on file for " + recipient.getFirstLastDisplayName());
      }
      
      if(this.isValid()){
        String toAddress = recipient.getEmail();
        if (!dh.isProductionServer(serverName)) {
          subject += " (TEST)";
          body += "[If this were a production environment then this email would have been sent to: " + toAddress + "]";
          toAddress = dh.getPropertyDictionary(PropertyDictionary.CONTACT_EMAIL_SOFTWARE_TESTER);
        }
        
        MailUtil.send(toAddress, null, fromAddress, subject, body, false);
        this.setResponsePage(SUCCESS_JSP);
      }
      else{
        this.setResponsePage(ERROR_JSP);
      }

    } catch (Exception e){
      log.error("An exception has occurred in EmailTopicOwner ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        this.getSecAdvisor().closeReadOnlyHibernateSession();        
      } catch(Exception e) {

      }
    }
    return this;
  }
}