package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.Lab;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.UserPermissionKind;
import hci.gnomex.security.EncrypterService;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.MailUtil;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.MessagingException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.Transaction;

import org.apache.commons.validator.routines.EmailValidator;


public class PublicSaveSelfRegisteredAppUser extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(PublicSaveSelfRegisteredAppUser.class);
  

  private AppUser        appUserScreen;
  private String         workAuthAdminEmail  = null;
  private String         coreFacilityEmail = null;
  private String         requestedLab = "";
  private Integer        requestedLabId = null;
  private StringBuffer   requestURL;
  private Boolean        existingLab = false;
  private Boolean        uofuAffiliate = false;
  
  public String responsePageSuccess = null;
  public String responsePageError = null;
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    this.requestURL = request.getRequestURL(); 
    appUserScreen = new AppUser();
    HashMap errors = this.loadDetailObject(request, appUserScreen);
    this.addInvalidFields(errors);
    if (request.getParameter("uofuAffiliate") != null && request.getParameter("uofuAffiliate").equals("y")) {
      uofuAffiliate = true;
      appUserScreen.setUserNameExternal("");
      appUserScreen.setPasswordExternal("");
      appUserScreen.setInstitute("University of Utah");
    } else {
      uofuAffiliate = false;
      appUserScreen.setuNID("");
    }
    if (request.getParameter("existingLab") != null && request.getParameter("existingLab").equals("y")) {
      existingLab = true;
      requestedLabId = Integer.parseInt(request.getParameter("labDropdown"));
    } else {
      existingLab = false;
      requestedLab = request.getParameter("newLab");
    }
    
    if ((appUserScreen.getFirstName() == null || appUserScreen.getFirstName().equals("")) ||
        (appUserScreen.getLastName() == null || appUserScreen.getLastName().equals("")) ||
        (appUserScreen.getEmail() == null || appUserScreen.getEmail().equals("")) ||
        ((requestedLab == null || requestedLab.equals("")) && requestedLabId == null)) {
      this.addInvalidField("requiredField", "Please fill out all mandatory fields (First and last name, email, lab)");
    }
    
    if (uofuAffiliate && (appUserScreen.getuNID() == null || this.appUserScreen.getuNID().equals(""))) {
      this.addInvalidField("userNameRequiredField", "University Id is required");        
    }
      
    if (!uofuAffiliate && (appUserScreen.getUserNameExternal() == null || this.appUserScreen.getUserNameExternal().equals(""))) {
      this.addInvalidField("userNameRequiredField", "User name is required");
    }

    if (!uofuAffiliate) {
      if (appUserScreen.getPasswordExternal() == null || appUserScreen.getPasswordExternal().equals(""))
      this.addInvalidField("passwordRqrd", "Password is required");
    }

    if (request.getParameter("responsePageSuccess") != null && !request.getParameter("responsePageSuccess").equals("")) {
      responsePageSuccess = request.getParameter("responsePageSuccess");
    }
    if (request.getParameter("responsePageError") != null && !request.getParameter("responsePageError").equals("")) {
      responsePageError = request.getParameter("responsePageError");
    }
    
    if (appUserScreen.getEmail() != null && !appUserScreen.getEmail().equals("")) {
      try {
        InternetAddress addresses[] = InternetAddress.parse(appUserScreen.getEmail(), true);
        if (addresses.length > 1) {
          this.addInvalidField("email", "Email address cannot contain spaces");
        }
        if (!EmailValidator.getInstance().isValid(appUserScreen.getEmail())) {
          this.addInvalidField("email", "Email address is not valid.  Please check the email address and try again.");
        }
      } catch(AddressException ex) {
        this.addInvalidField("email", "Invalid Email Address -- " + ex.toString());
      }
    }
    this.validate();
  }

  public Command execute() throws RollBackCommandException {

    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      workAuthAdminEmail = PropertyDictionaryHelper.getInstance(sess).getProperty(PropertyDictionary.CONTACT_EMAIL_CORE_FACILITY_WORKAUTH_REMINDER);
      coreFacilityEmail = PropertyDictionaryHelper.getInstance(sess).getProperty(PropertyDictionary.CONTACT_EMAIL_CORE_FACILITY);

      AppUser appUser = null;

      if(!uofuAffiliate) {
        if (userNameAlreadyExists(sess, appUserScreen.getUserNameExternal(), null)) {
          this.addInvalidField("Username exists", "The User name " + appUserScreen.getUserNameExternal() + " already exists.  Please use another name.");
        }            
      } else {
        if (uNID_AlreadyExists(sess, appUserScreen.getuNID(), null)) {
          this.addInvalidField("uNID exists", "The uNID " + appUserScreen.getuNID() + " already exists.  Please use another.");
        }            
      }      

      if (nameEmailAlreadyExists(sess, appUserScreen)) {
        this.addInvalidField("Name/Email", "The combination of name and email already exists.  Please verify you do not have an existing account.");
      }
      
      if (this.isValid()) {
        // Send user email before storing app user so if it fails we can give error without
        // throwing exception
        try {
          sendUserEmail(appUserScreen);
        } catch(Exception e) {
          this.addInvalidField("email", "Unable to send email.  Please check your email address and try again.");
        }
      }
      
      if (this.isValid()) {
        appUser = appUserScreen;

        if (appUser.getuNID() != null && 
            !appUser.getuNID().trim().equals("")) {
          appUser.setUserNameExternal(null);
          appUser.setPasswordExternal(null);

        } else {
          if (appUser.getUserNameExternal() != null  && !appUser.getUserNameExternal().trim().equals("")) {
            appUser.setuNID(null);
          }
          if (appUser.getPasswordExternal() != null && appUser.getPasswordExternal() != "" && !appUser.getPasswordExternal().equals(AppUser.MASKED_PASSWORD)) {
            String encryptedPassword = EncrypterService.getInstance().encrypt(appUser.getPasswordExternal());
            appUser.setPasswordExternal(encryptedPassword);      
          }

        }
        
        // Default to inactive
        appUser.setIsActive("N");
        
        // Default to Lab permission kind
        appUser.setCodeUserPermissionKind(UserPermissionKind.GROUP_PERMISSION_KIND);

        if (existingLab) {
          Lab lab = (Lab)sess.load(Lab.class, requestedLabId);
          requestedLab = lab.getName();
          HashSet labSet = new HashSet();
          labSet.add(lab);
          appUser.setLabs(labSet);
        }
        sess.save(appUser);

      }

      if (this.isValid()) {
        sendAdminEmail(appUser);
      }
      
      if (this.isValid()) {
        sess.flush();
        
        this.xmlResult = "<SUCCESS idAppUser=\"" + appUser.getIdAppUser() + "\"/>";
        setResponsePage(responsePageSuccess != null ? responsePageSuccess : this.SUCCESS_JSP);
      } else {
        setResponsePage(responsePageError != null ? responsePageError : this.ERROR_JSP);
      }

    } catch (Exception e) {
      log.error("An exception has occurred in SaveSelfRegisteredAppUser ", e);
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

  private String getEmailBody(AppUser appUser) {
    StringBuffer body = new StringBuffer();
    body.append("Requested User Account:<br><br>");
    body.append("<table border='0'><tr><td>Last name:</td><td>" + this.getNonNullString(appUser.getLastName()));
    body.append("</td></tr><tr><td>First name:</td><td>" + this.getNonNullString(appUser.getFirstName()));
    if (existingLab) {
      body.append("</td></tr><tr><td>Requested lab(Existing):</td><td>" + this.getNonNullString(requestedLab));
    } else {
      body.append("</td></tr><tr><td>Requested lab(New):</td><td>" + this.getNonNullString(requestedLab));
    }
    body.append("</td></tr><tr><td>Institution:</td><td>" + this.getNonNullString(appUser.getInstitute()));
    body.append("</td></tr><tr><td>Email:</td><td>" + this.getNonNullString(appUser.getEmail()));
    body.append("</td></tr><tr><td>Phone:</td><td>" + this.getNonNullString(appUser.getPhone()));
    if(appUser.getuNID() != null && appUser.getuNID().length() > 0) {
      body.append("</td></tr><tr><td>University uNID:</td><td>" + this.getNonNullString(appUser.getuNID()));
    } else {
      body.append("</td></tr><tr><td>Username:</td><td>" + this.getNonNullString(appUser.getUserNameExternal()));    
    }
    body.append("</td></tr></table>");

    return body.toString();
  }
  
  private void sendUserEmail(AppUser appUser)  throws NamingException, MessagingException {
    StringBuffer intro = new StringBuffer();
    intro.append("Your request for a GNomEx account has been received.  The core facility staff will verify the information and then activate your account.<br><br>");
    
    if (appUser.getEmail().equals("bademail@bad.com")) {
      throw new AddressException("'bademail@bad.com' not allowed");
    }
    

    MailUtil.send(
        appUser.getEmail(),
        "",
        coreFacilityEmail,
        "GNomEx User Account Request Received",
        intro.toString() + getEmailBody(appUser),
        true
      );
  }
  
  private void sendAdminEmail(AppUser appUser)  throws NamingException, MessagingException {
    //This is to send it to the right application server, without hard coding
    String url = requestURL.substring(0, requestURL.indexOf("PublicSaveSelfRegisteredAppUser.gx"));

    if (appUser.getEmail().equals("bademail@bad.com")) {
      throw new AddressException("'bademail@bad.com' not allowed");
    }

    StringBuffer introForAdmin = new StringBuffer();
    introForAdmin.append("The following person requested a GNomEx user account.  The user account has been created but not activated.<br><br>");
    introForAdmin.append("<a href='" + url + "gnomexFlex.jsp?idAppUser=" + appUser.getIdAppUser().intValue() + "&launchWindow=UserDetail'>Click here</a> to edit the new account.<br><br>");
    MailUtil.send(
        workAuthAdminEmail,
        "",
        coreFacilityEmail,
        "GNomEx User Account Request for " + appUser.getFirstName() + " " + appUser.getLastName(),
        introForAdmin.toString() + getEmailBody(appUser),
        true
      );

  }
  
  public void validate() {
    // See if we have a valid form
    if (isValid()) {
      setResponsePage(responsePageSuccess != null ? responsePageSuccess : this.SUCCESS_JSP);
    } else {
      setResponsePage(responsePageError != null ? responsePageError : this.ERROR_JSP);
    }
  }
  
  private class AppUserComparator implements Comparator, Serializable {
    public int compare(Object o1, Object o2) {
      AppUser u1 = (AppUser)o1;
      AppUser u2 = (AppUser)o2;
      
      return u1.getIdAppUser().compareTo(u2.getIdAppUser());
      
    }
  }
  
  private static boolean userNameAlreadyExists(Session sess, String userNameExternal, Integer idAppUser) {
    if (userNameExternal == null || userNameExternal.equals("")) {
      return false;
    }

    StringBuffer buf = new StringBuffer();
    buf.append("SELECT a.userNameExternal from AppUser as a where a.userNameExternal = '"); 
    buf.append(userNameExternal + "'");
    if (idAppUser != null) {
      buf.append(" AND a.idAppUser != " + idAppUser);
    }
    List users = sess.createQuery(buf.toString()).list();
    return users.size() > 0;    
  }
  
  private static boolean uNID_AlreadyExists(Session sess, String uNID, Integer idAppUser) {
    if (uNID == null || uNID.equals("")) {
      return false;
    }

    StringBuffer buf = new StringBuffer();
    buf.append("SELECT a.uNID from AppUser as a where a.uNID = '"); 
    buf.append(uNID + "'");
    if (idAppUser != null) {
      buf.append(" AND a.idAppUser != " + idAppUser);
    }
    List users = sess.createQuery(buf.toString()).list();
    return users.size() > 0;    
  }
  
  private static boolean nameEmailAlreadyExists(Session sess, AppUser appUser) {
    if (appUser.getFirstName() == null || appUser.getLastName() == null || appUser.getEmail() == null) {
      return false;
    }

    StringBuffer buf = new StringBuffer();
    buf.append("SELECT a.uNID from AppUser as a where a.firstName = '"); 
    buf.append(appUser.getFirstName()).append("'");
    buf.append(" and a.lastName = '").append(appUser.getLastName()).append("'");
    buf.append(" and a.email = '").append(appUser.getEmail()).append("'");
    if (appUser.getIdAppUser() != null) {
      buf.append(" AND a.idAppUser != " + appUser.getIdAppUser());
    }
    
    List users = sess.createQuery(buf.toString()).list();
    return users.size() > 0;
  }
}