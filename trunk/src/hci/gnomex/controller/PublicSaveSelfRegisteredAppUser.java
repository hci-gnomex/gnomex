package hci.gnomex.controller;

import hci.gnomex.model.AppUser;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.UserPermissionKind;
import hci.gnomex.security.EncrypterService;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.MailUtil;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javax.mail.MessagingException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;




public class PublicSaveSelfRegisteredAppUser extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(PublicSaveSelfRegisteredAppUser.class);
  

  private AppUser        appUserScreen;
  private PropertyDictionary       adminEmailProperty = null;
  private String         requestedLab = "";
  private StringBuffer   requestURL;
  
  private String responsePageSuccess = null;
  private String responsePageError = null;
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    this.requestURL = request.getRequestURL(); 
    appUserScreen = new AppUser();
    HashMap errors = this.loadDetailObject(request, appUserScreen);
    this.addInvalidFields(errors);

    requestedLab = request.getParameter("lab");
    
    if ((appUserScreen.getFirstName() == null || appUserScreen.getFirstName().equals("")) &&
        (appUserScreen.getLastName() == null || appUserScreen.getLastName().equals("")) &&
        (appUserScreen.getEmail() == null || appUserScreen.getEmail().equals("")) &&
        (requestedLab == null || requestedLab.equals(""))) {
      this.addInvalidField("requiredField", "Please fill out all mandatory fields (First and last name, email, lab)");
    }
    
    if ((appUserScreen.getuNID() == null || this.appUserScreen.getuNID().equals("")) &&
        (appUserScreen.getUserNameExternal() == null || this.appUserScreen.getUserNameExternal().equals(""))) {
      this.addInvalidField("userNameRequiredField", "User name is required");
    }
    
    if (appUserScreen.getUserNameExternal() != null && !this.appUserScreen.getUserNameExternal().equals("")) {
      if (appUserScreen.getPasswordExternal() == null || appUserScreen.getPasswordExternal().equals(""))
      this.addInvalidField("passwordRqrd", "Password is required");
    }

    if (request.getParameter("responsePageSuccess") != null && !request.getParameter("responsePageSuccess").equals("")) {
      responsePageSuccess = request.getParameter("responsePageSuccess");
    }
    if (request.getParameter("responsePageError") != null && !request.getParameter("responsePageError").equals("")) {
      responsePageError = request.getParameter("responsePageError");
    }
    this.validate();
  }

  public Command execute() throws RollBackCommandException {

    try {
      Session sess = HibernateSession.currentSession(this.getUsername());

      boolean checkUsername = true;
      boolean isUsedUsername = false;
      boolean isUseduNID = false;

      if (appUserScreen.getuNID() != null && 
          !appUserScreen.getuNID().trim().equals("")) {
        checkUsername = false;
      }       

      AppUser appUser = null;


      if(checkUsername) {
        if (userNameAlreadyExists(sess, appUserScreen.getUserNameExternal(), null)) {
          this.addInvalidField("Username exists", "The User name " + appUserScreen.getUserNameExternal() + " already exists.  Please use another name.");
          isUsedUsername = true;
        }            
      } else {
        if (uNID_AlreadyExists(sess, appUserScreen.getuNID(), null)) {
          this.addInvalidField("uNID exists", "The uNID " + appUserScreen.getuNID() + " already exists.  Please use another.");
          isUseduNID = true;
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
        
        // Default to Lab permission kind
        appUser.setCodeUserPermissionKind(UserPermissionKind.GROUP_PERMISSION_KIND);

        sess.save(appUser);

      }

      if (this.isValid()) {
        sess.flush();
        this.xmlResult = "<SUCCESS idAppUser=\"" + appUser.getIdAppUser() + "\"/>";
        setResponsePage(responsePageSuccess != null ? responsePageSuccess : this.SUCCESS_JSP);
        
        String contactEmailProperty = "from PropertyDictionary p where p.propertyName='" + PropertyDictionary.CONTACT_EMAIL_CORE_FACILITY_WORKAUTH_REMINDER + "'";

        adminEmailProperty = (PropertyDictionary) sess.createQuery(contactEmailProperty).uniqueResult();
        
        sendAccountRequestEmail(appUser);                      
        
      } else {
        if(isUsedUsername || isUseduNID) {
          String outMsg = "";
          if(isUsedUsername) {
            outMsg = "Username '" + appUserScreen.getUserNameExternal() + "' is already being used. Please select a different username.";              
          } else {
            outMsg = "uNID '" + appUserScreen.getuNID() + "' is already being used. Please select a different uNID.";                            
          }
          this.xmlResult = "<ERROR message=\"" + outMsg + "\"/>";
          setResponsePage(responsePageSuccess != null ? responsePageSuccess : this.SUCCESS_JSP);    
        } else {
          setResponsePage(responsePageError != null ? responsePageError : this.ERROR_JSP);
        }
      }

    }catch (Exception e){
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


  
  public void sendAccountRequestEmail(AppUser appUser)  throws NamingException, MessagingException {
    //This is to send it to the right application server, without hard coding
    String url = requestURL.substring(0, requestURL.indexOf("PublicSaveSelfRegisteredAppUser.gx"));
    
    StringBuffer intro = new StringBuffer();
    intro.append("Your request for a GNomEx account has been received.  The core facility staff will verify the information and then activate your account.<br><br>");
    
    StringBuffer body = new StringBuffer();
    body.append("Requested User Account:<br><br>");
    body.append("<table border='0'><tr><td>Last name:</td><td>" + this.getNonNullString(appUser.getLastName()));
    body.append("</td></tr><tr><td>First name:</td><td>" + this.getNonNullString(appUser.getFirstName()));
    body.append("</td></tr><tr><td>Requested lab:</td><td>" + this.getNonNullString(requestedLab));
    body.append("</td></tr><tr><td>Institution:</td><td>" + this.getNonNullString(appUser.getInstitute()));
    body.append("</td></tr><tr><td>Department:</td><td>" + this.getNonNullString(appUser.getDepartment()));
    body.append("</td></tr><tr><td>Email:</td><td>" + this.getNonNullString(appUser.getEmail()));
    body.append("</td></tr><tr><td>Phone:</td><td>" + this.getNonNullString(appUser.getPhone()));
    if(appUser.getuNID() != null && appUser.getuNID().length() > 0) {
      body.append("</td></tr><tr><td>University uNID:</td><td>" + this.getNonNullString(appUser.getuNID()));
    } else {
      body.append("</td></tr><tr><td>Username:</td><td>" + this.getNonNullString(appUser.getUserNameExternal()));    
    }
    body.append("</td></tr></table>");

    
    String adminEmail = this.adminEmailProperty.getPropertyValue();

    MailUtil.send(
        appUser.getEmail(),
        "",
        adminEmail,
        "GNomEx User Account Request Received",
        intro.toString() + body.toString(),
        true
      );
    

    StringBuffer introForAdmin = new StringBuffer();
    introForAdmin.append("The following person requested a GNomEx user account.  The user account has been created by but not activated.<br><br>");
    introForAdmin.append("<a href='" + url + "gnomexFlex.jsp?idAppUser=" + appUser.getIdAppUser().intValue() + "&launchWindow=UserDetail'>Click here</a> to edit the new account.<br><br>");
    
    MailUtil.send(
        adminEmail,
        "",
        adminEmail,
        "GNomEx User Account Request for " + appUser.getFirstName() + " " + appUser.getLastName(),
        introForAdmin.toString() + body.toString(),
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
}