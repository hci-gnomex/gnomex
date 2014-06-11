package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.CoreFacility;
import hci.gnomex.model.Lab;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.UserPermissionKind;
import hci.gnomex.security.EncrypterService;
import hci.gnomex.security.EncryptionUtility;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.MailUtil;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.validator.routines.EmailValidator;
import org.hibernate.Query;
import org.hibernate.Session;


public class PublicSaveSelfRegisteredAppUser extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(PublicSaveSelfRegisteredAppUser.class);
  

  private AppUser        appUserScreen;
  private PropertyDictionaryHelper propertyHelper = null;
  private String         coreFacilityEmail = null;
  private String         requestedLabName = "";
  private Integer        requestedLabId = null;
  private Lab            requestedLab = null;
  private StringBuffer   requestURL;
  private Boolean        existingLab = false;
  private Boolean        uofuAffiliate = false;
  private CoreFacility   facility = null;
  private String         idFacility = null;
  private String         department = null;
  private String         serverName;
  private List           activeFacilities;
  
  public String responsePageSuccess = null;
  public String responsePageError = null;
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    serverName = request.getServerName();
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
    
    // Trim uNID, external user name, removing trailing spaces
    if (appUserScreen.getuNID() != null) {
      appUserScreen.setuNID(appUserScreen.getuNID().trim());
    }
    if (appUserScreen.getUserNameExternal() != null) {
      appUserScreen.setUserNameExternal(appUserScreen.getUserNameExternal().trim());
    }
    
    if (request.getParameter("labDropdown") != null && Integer.parseInt(request.getParameter("labDropdown")) != 0 ) {
      existingLab = true;
      requestedLabId = Integer.parseInt(request.getParameter("labDropdown"));
      appUserScreen.setDepartment("");
    } else {
      existingLab = false;
      requestedLabName = request.getParameter("newLab");
      department = request.getParameter( "department" );
      appUserScreen.setDepartment( department );
    }
    
    if (request.getParameter("idFacility") != null && !request.getParameter("idFacility").equals("") ) {
      idFacility = request.getParameter("idFacility");
    } else {
      this.addInvalidField("facilityRqrd", "Please select a core facility");
    }
    
    
    if ((appUserScreen.getFirstName() == null || appUserScreen.getFirstName().equals("")) ||
        (appUserScreen.getLastName() == null || appUserScreen.getLastName().equals("")) ||
        (appUserScreen.getEmail() == null || appUserScreen.getEmail().equals("")) ||
        ((requestedLabName == null || requestedLabName.equals("")) && requestedLabId == null)) {
      this.addInvalidField("requiredField", "Please fill out all mandatory fields (First and last name, email, lab)");
    }
    
    if(appUserScreen.getFirstName() != null && appUserScreen.getLastName() != null){
      //if(!appUserScreen.getFirstName().matches("[A-Za-z]+") || !appUserScreen.getLastName().matches("[A-Za-z]+")){
      if(appUserScreen.getFirstName().matches(".*[0-9].*") || appUserScreen.getLastName().matches(".*[0-9].*")){
        this.addInvalidField("improperName", "First and last names may not contain digits");
      }
    }
    
    if (uofuAffiliate && (appUserScreen.getuNID() == null || this.appUserScreen.getuNID().equals(""))) {
      this.addInvalidField("userNameRequiredField", "University Id is required");        
    }
    
    if(appUserScreen.getuNID() != null && !appUserScreen.getuNID().equals("") ){
      if(appUserScreen.getuNID().charAt(0) != 'u' || appUserScreen.getuNID().trim().length() != 8 || !appUserScreen.getuNID().trim().substring(1).matches("[0-9]+")){
        this.addInvalidField("incorrectUNIDFormat", "Your University ID must start with lowercase 'u' followed by 7 digits");
      }
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
      propertyHelper = PropertyDictionaryHelper.getInstance(sess);
      EncryptionUtility passwordEncrypter = new EncryptionUtility();
      
      String disableSignup = propertyHelper.getProperty(PropertyDictionary.DISABLE_USER_SIGNUP);
      if (disableSignup != null && disableSignup.equals("Y")) {
        this.addInvalidField("Signup disabled", "User signup is disabled");
      }
      
      // Get core facilities.
      activeFacilities = CoreFacility.getActiveCoreFacilities(sess);
      if (activeFacilities.size() == 1) {
        facility = (CoreFacility)activeFacilities.get(0);
      } else if (idFacility != null && idFacility.length() > 0) {
        Integer id = Integer.parseInt(idFacility);
        facility = (CoreFacility)sess.load(CoreFacility.class, id);
      }
      
      AppUser appUser = null;

      if(!uofuAffiliate) {
        if (userNameAlreadyExists(sess, appUserScreen.getUserNameExternal(), null)) {
          this.addInvalidField("Username exists", "The user name " + appUserScreen.getUserNameExternal() + " already exists.  Please use another name.");
        }            
      } else {
        if (uNID_AlreadyExists(sess, appUserScreen.getuNID(), null)) {
          this.addInvalidField("uNID exists", "The uNID " + appUserScreen.getuNID() + " already exists.  Please use another.");
        }            
      }      

      if (nameEmailAlreadyExists(sess, appUserScreen)) {
        this.addInvalidField("Name/Email", "The combination of name and email already exists.  Please verify you do not have an existing account.");
      }
      
      if (existingLab) {
        requestedLab = (Lab)sess.load(Lab.class, requestedLabId);
        requestedLabName = requestedLab.getName();
      }

      if (facility == null) {
        if (requestedLab == null || requestedLab.getCoreFacilities().size() != 1) {
          coreFacilityEmail = propertyHelper.getProperty(PropertyDictionary.GENERIC_NO_REPLY_EMAIL);
        } else {
          coreFacilityEmail = propertyHelper.getCoreFacilityProperty(((CoreFacility)requestedLab.getCoreFacilities().toArray()[0]).getIdCoreFacility(), PropertyDictionary.CONTACT_EMAIL_CORE_FACILITY);
        }
      } else {
        if( existingLab && !requestedLab.getCoreFacilities().contains( facility ) ) {
          requestedLab.getCoreFacilities().add( facility );
        }
        coreFacilityEmail = propertyHelper.getCoreFacilityProperty(facility.getIdCoreFacility(),PropertyDictionary.CONTACT_EMAIL_CORE_FACILITY);
      }
      
      if (this.isValid()) {
        // Send user email before storing app user so if it fails we can give error without
        // throwing exception
        try {
          sendUserEmail(appUserScreen);
        } catch(Exception e) {
          log.error("An exception occurred sending the user email ", e);
          e.printStackTrace();
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
          if (appUser.getPasswordExternal() != null && !appUser.getPasswordExternal().equals("") && !appUser.getPasswordExternal().equals(AppUser.MASKED_PASSWORD)) {
            String salt = passwordEncrypter.createSalt();
            String encryptedPassword = passwordEncrypter.createPassword(appUser.getPasswordExternal(), salt);
            appUser.setSalt(salt);
            appUser.setPasswordExternal(encryptedPassword);      
          }

        }
        
        // Default to inactive
        appUser.setIsActive("N");
        
        // Default to Lab permission kind
        appUser.setCodeUserPermissionKind(UserPermissionKind.GROUP_PERMISSION_KIND);

        // Set lab if selected
        if (existingLab) {
          HashSet labSet = new HashSet();
          labSet.add(this.requestedLab);
          appUser.setLabs(labSet);
        }

        sess.save(appUser);
      }

      if (this.isValid()) {
        sendAdminEmail(appUser, sess);    
        sendLabManagerEmail(appUser, sess);
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
    body.append("<table border='0'><tr><td>Last name:</td><td>" + this.getNonNullString(appUser.getLastName()));
    body.append("</td></tr><tr><td>First name:</td><td>" + this.getNonNullString(appUser.getFirstName()));
    if (existingLab) {
      body.append("</td></tr><tr><td>Lab:</td><td>" + this.getNonNullString(requestedLabName));
      if (activeFacilities.size() > 1) {
        body.append("</td></tr><tr><td>Core Facility:</td><td>" + this.getNonNullString(facility.getFacilityName()));
      }
    } else {
      body.append("</td></tr><tr><td>Lab (New. Please add to GNomEx.):</td><td>" + this.getNonNullString(requestedLabName));
      body.append("</td></tr><tr><td>Department:</td><td>" + this.getNonNullString(appUser.getDepartment()));
      if (activeFacilities.size() > 1) {
        body.append("</td></tr><tr><td>Core Facility:</td><td>" + this.getNonNullString(facility.getFacilityName()));
      }
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
    intro.append("Thank you for signing up for a GNomEx account.  We will send you an email once your user account has been activated.<br><br>");
    
    if (appUser.getEmail().equals("bademail@bad.com")) {
      throw new AddressException("'bademail@bad.com' not allowed");
    }
    
    if(!MailUtil.isValidEmail(appUser.getEmail())){
      log.error("Invalid Email Address " + appUser.getEmail());
    }
    

    MailUtil.send(
        appUser.getEmail(),
        "",
        coreFacilityEmail,
        "Your GNomEx user account has been created",
        intro.toString() + getEmailBody(appUser),
        true
      );
  }
  
  private void sendAdminEmail(AppUser appUser, Session sess)  throws NamingException, MessagingException {
    //This is to send it to the right application server, without hard coding
    String url = requestURL.substring(0, requestURL.indexOf("PublicSaveSelfRegisteredAppUser.gx"));
    DictionaryHelper dictionaryHelper = DictionaryHelper.getInstance(sess);

    if (appUser.getEmail().equals("bademail@bad.com")) {
      throw new AddressException("'bademail@bad.com' not allowed");
    }

    String toAddress = "";
    String subject = "GNomEx user account pending approval for " + appUser.getFirstName() + " " + appUser.getLastName();
    String testEmailInfo = "";
    if (facility != null) {
      toAddress = propertyHelper.getQualifiedCoreFacilityProperty(PropertyDictionary.CONTACT_EMAIL_CORE_FACILITY_WORKAUTH_REMINDER, serverName, facility.getIdCoreFacility());
    } else if (requestedLab != null) {
      for(Iterator facilityIter = requestedLab.getCoreFacilities().iterator();facilityIter.hasNext();) {
        CoreFacility f = (CoreFacility)facilityIter.next();
        String add = propertyHelper.getQualifiedCoreFacilityProperty(PropertyDictionary.CONTACT_EMAIL_CORE_FACILITY_WORKAUTH_REMINDER, serverName, f.getIdCoreFacility()); 
        if (add != null && add.length() > 0) {
          if (toAddress != null && !toAddress.equals("")) {
            toAddress += ",";
          }
          toAddress += add;
        }
      }
    }
    
    if (!dictionaryHelper.isProductionServer(serverName)) {
      subject = subject + "  (TEST)";
      testEmailInfo = "[If this were a production environment then this email would have been sent to: " + toAddress + "]<br><br>";
      toAddress = dictionaryHelper.getPropertyDictionary(PropertyDictionary.CONTACT_EMAIL_SOFTWARE_TESTER);
    }
    
    if(toAddress.equals("")){
      return;
    }
    
    url = url + Constants.LAUNCH_APP_JSP + "?idAppUser=" + appUser.getIdAppUser().intValue() + "&launchWindow=UserDetail&idCore=" + facility.getIdCoreFacility().toString();
    StringBuffer introForAdmin = new StringBuffer();
    introForAdmin.append("The following person has signed up for a GNomEx user account.  The user account has been created but not activated.<br><br>");
    introForAdmin.append("<a href='" + url + "'>Click here</a> to review and activate the account.  GNomEx will automatically send an email to notify the user that his/her user account has been activated.<br><br>");
    MailUtil.send(
        toAddress,
        "",
        coreFacilityEmail,
        subject,
        testEmailInfo + introForAdmin.toString() + getEmailBody(appUser),
        true
      );

  }
  
  private void sendLabManagerEmail(AppUser appUser, Session sess) throws NamingException, MessagingException{
    String url = requestURL.substring(0, requestURL.indexOf("PublicSaveSelfRegisteredAppUser.gx"));
    DictionaryHelper dictionaryHelper = DictionaryHelper.getInstance(sess);

    if (appUser.getEmail().equals("bademail@bad.com")) {
      throw new AddressException("'bademail@bad.com' not allowed");
    }
    
    String toAddress = "";
    String subject = "GNomEx user account requested for " + appUser.getFirstName() + " " + appUser.getLastName();
    String testEmailInfo = "";
    if(requestedLab != null){
      if ( requestedLab.getContactEmail() != null ) {
        if(!toAddress.equals("") && !requestedLab.getContactEmail().equals("")){
          toAddress += ",";
        }
        toAddress += requestedLab.getContactEmail();
      }
      
      for(Iterator managerIter = requestedLab.getManagers().iterator(); managerIter.hasNext();){
        AppUser manager = (AppUser)managerIter.next();
        String managerEmail = manager.getEmail();
        if(managerEmail != null && !managerEmail.equals("")){
          if(!toAddress.equals("")){
            toAddress += ",";
          }
          toAddress += managerEmail;
        }
      } 
    }
    
    //Abort the send if the to address is still empty to avoid empty recipient error
    if(toAddress.equals("")){
      return;
    }
    
    if (!dictionaryHelper.isProductionServer(serverName)) {
      subject = subject + "  (TEST)";
      testEmailInfo = "[If this were a production environment then this email would have been sent to: " + toAddress + "]<br><br>";
      toAddress = dictionaryHelper.getPropertyDictionary(PropertyDictionary.CONTACT_EMAIL_SOFTWARE_TESTER);
    }
    
    StringBuffer introForAdmin = new StringBuffer();
    introForAdmin.append("This email is being sent to notify you that the following person has signed up for a GNomEx user account and has requested to be a member of your lab.<br><br>");
    MailUtil.send(
        toAddress,
        "",
        coreFacilityEmail,
        subject,
        testEmailInfo + introForAdmin.toString() + getEmailBody(appUser),
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
    buf.append("SELECT a.uNID from AppUser as a where a.firstName = :firstName"); 
    buf.append(" and a.lastName = :lastName");
    buf.append(" and a.email = :email");
    if (appUser.getIdAppUser() != null) {
      buf.append(" AND a.idAppUser != :idAppUser");
    }
    
    Query usersQuery = sess.createQuery(buf.toString());
    
    usersQuery.setParameter("firstName", appUser.getFirstName());
    usersQuery.setParameter("lastName", appUser.getLastName());
    usersQuery.setParameter("email", appUser.getEmail());
    if (appUser.getIdAppUser() != null) {
      usersQuery.setParameter("idAppUser", appUser.getIdAppUser());
    }
    
    List users = usersQuery.list();
    return users.size() > 0;
  }
}