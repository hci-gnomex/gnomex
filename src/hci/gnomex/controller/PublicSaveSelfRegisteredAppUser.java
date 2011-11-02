package hci.gnomex.controller;

import hci.gnomex.model.AppUser;
import hci.gnomex.model.Property;
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
  private Property       labContactEmail = null;
  private String         requestedLab = "";
  private StringBuffer   requestURL;
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    this.requestURL = request.getRequestURL(); 
    appUserScreen = new AppUser();
    HashMap errors = this.loadDetailObject(request, appUserScreen);
    requestedLab = request.getParameter("lab");
    this.addInvalidFields(errors);
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

        sess.save(appUser);

      }

      if (this.isValid()) {
        sess.flush();
        this.xmlResult = "<SUCCESS idAppUser=\"" + appUser.getIdAppUser() + "\"/>";
        setResponsePage(this.SUCCESS_JSP);
        
        String contactEmailProperty = "from Property p where p.propertyName='" + Property.CONTACT_EMAIL_CORE_FACILITY_WORKAUTH_REMINDER + "'";

        labContactEmail = (Property) sess.createQuery(contactEmailProperty).uniqueResult();
        
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
          setResponsePage(this.SUCCESS_JSP);            
        } else {
          setResponsePage(this.ERROR_JSP);            
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
    
    
    StringBuffer body = new StringBuffer();
    body.append("Requested GNomEx Account:<br><br>");
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
      body.append("</td></tr><tr><td>External username:</td><td>" + this.getNonNullString(appUser.getUserNameExternal()));    
    }
    body.append("</td></tr></table>");

    
    String labEmail = this.labContactEmail.getPropertyValue();

    MailUtil.send(
        appUser.getEmail(),
        "",
        labEmail,
        "GNomEx User Account Request Received",
        body.toString(),
        true
      );
    
    body.append("<br><br>The account has been created but not activated.<br><br><a href='" + url + "gnomexFlex.jsp?idAppUser=" + appUser.getIdAppUser().intValue() + "&launchWindow=UserDetail'>Click here</a> to edit the new account.");
    
    MailUtil.send(
        labEmail,
        "",
        labEmail,
        "GNomEx User Account Request",
        body.toString(),
        true
      );

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