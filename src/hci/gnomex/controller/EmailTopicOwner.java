package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.MailUtil;
import hci.gnomex.utility.MailUtilHelper;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.apache.log4j.Logger;
public class EmailTopicOwner extends GNomExCommand implements Serializable {
  private static Logger LOG = Logger.getLogger(EmailTopicOwner.class);

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
        
        MailUtilHelper helper = new MailUtilHelper(	
        		toAddress,
        		fromAddress,
        		subject,
        		body,
        		null,
        		false, 
        	    dh,
      		    serverName 	);
        helper.setRecipientAppUser(recipient);
        MailUtil.validateAndSendEmail(helper);
        
        this.setResponsePage(SUCCESS_JSP);
      }
      else{
        this.setResponsePage(ERROR_JSP);
      }

    } catch (Exception e){
      LOG.error("An exception has occurred in EmailTopicOwner ", e);

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