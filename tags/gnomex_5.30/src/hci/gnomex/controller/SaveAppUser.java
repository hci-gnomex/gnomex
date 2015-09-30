package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.CoreFacility;
import hci.gnomex.model.Lab;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.UserPermissionKind;
import hci.gnomex.security.EncryptionUtility;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.MailUtil;
import hci.gnomex.utility.MailUtilHelper;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.hibernate.Session;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;




public class SaveAppUser extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveAppUser.class);
  

  private AppUser                         appUserScreen;
  private boolean                         isNewAppUser = false;
  private ArrayList<CoreFacilityCheck>    managingCoreFacilityIds;
  private ArrayList<CoreFacilityCheck>    coreFacilityIdsICanSubmitTo;
  private String                          url;
  private String                          isWebForm = "Y";
  private EncryptionUtility               passwordEncrypter;
  private String 						  serverName;
  private boolean						  beingInactivated = false;
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
	serverName = request.getServerName();
    
    try {
      url = this.getLaunchAppURL(request);
    } catch (Exception e) {
      log.warn("Cannot get launch app URL in SaveAppUser", e);
    }
    if (request.getParameter("isWebForm") != null && !request.getParameter("isWebForm").equals("")) {
      isWebForm = request.getParameter("isWebForm");
    }

    if (request.getParameter("beingInactivated") != null && request.getParameter("beingInactivated").equals("Y")) {
      beingInactivated = true;
    }

    appUserScreen = new AppUser();
    HashMap errors = this.loadDetailObject(request, appUserScreen);
    this.addInvalidFields(errors);
    if (appUserScreen.getIdAppUser() == null || appUserScreen.getIdAppUser().intValue() == 0) {
      isNewAppUser = true;
    }
    
    managingCoreFacilityIds = new ArrayList<CoreFacilityCheck>();
    String managingCoreFacilitiesString = request.getParameter("userManagingCoreFacilities");
    if (managingCoreFacilitiesString != null && managingCoreFacilitiesString.length() > 0) {
      managingCoreFacilitiesString = managingCoreFacilitiesString.replaceAll("&", "&amp;");
      managingCoreFacilitiesString = "<managingCoreFacilities>" + managingCoreFacilitiesString + "</managingCoreFacilities>";
      StringReader reader = new StringReader(managingCoreFacilitiesString);
      try {
        SAXBuilder sax = new SAXBuilder();
        org.jdom.Document searchDoc = sax.build(reader);
        Element rootNode = searchDoc.getRootElement();
        for(Iterator i = rootNode.getChildren("coreFacility").iterator(); i.hasNext();) {
          Element facilityNode = (Element)i.next();
          String selected = facilityNode.getAttributeValue("selected");
          String idString = facilityNode.getAttributeValue("value");
          CoreFacilityCheck chk = new CoreFacilityCheck();
          chk.idCoreFacility = Integer.parseInt(idString);
          chk.selected = selected;
          managingCoreFacilityIds.add(chk);
        }
      } catch (Exception je ) {
        log.error( "Cannot parse managingCoreFacilitiesString", je );
        this.addInvalidField( "managingCoreFacilitiesString", "Invalid search xml");
      }
    }
    coreFacilityIdsICanSubmitTo = new ArrayList<CoreFacilityCheck>();
    String coreFacilitiesICanSubmitToString = request.getParameter("coreFacilitiesUserCanSubmitTo");
    if (coreFacilitiesICanSubmitToString != null && coreFacilitiesICanSubmitToString.length() > 0) {
      coreFacilitiesICanSubmitToString = coreFacilitiesICanSubmitToString.replaceAll("&", "&amp;");
      coreFacilitiesICanSubmitToString = "<coreFacilitiesICanSubmitTo>" + coreFacilitiesICanSubmitToString + "</coreFacilitiesICanSubmitTo>";
      StringReader reader = new StringReader(coreFacilitiesICanSubmitToString);
      try {
        SAXBuilder sax = new SAXBuilder();
        org.jdom.Document searchDoc = sax.build(reader);
        Element rootNode = searchDoc.getRootElement();
        for(Iterator i = rootNode.getChildren("coreFacility").iterator(); i.hasNext();) {
          Element facilityNode = (Element)i.next();
          String selected = facilityNode.getAttributeValue("selected");
          String idString = facilityNode.getAttributeValue("value");
          CoreFacilityCheck chk = new CoreFacilityCheck();
          chk.idCoreFacility = Integer.parseInt(idString);
          chk.selected = selected;
          coreFacilityIdsICanSubmitTo.add(chk);
        }
      } catch (Exception je ) {
        log.error( "Cannot parse coreFacilitiesICanSubmitToString", je );
        this.addInvalidField( "coreFacilitiesICanSubmitToString", "Invalid search xml");
      }
    }
  }

  public Command execute() throws RollBackCommandException {
    

    
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      
      
      if (this.getSecurityAdvisor().hasPermission(SecurityAdvisor.CAN_ADMINISTER_USERS)) {
        AppUser appUser = null;
        
        passwordEncrypter = new EncryptionUtility();
        
        boolean checkUsername = true;
        boolean isUsedUsername = false;
        boolean isUseduNID = false;
        boolean isManageFacilityError = false;
        boolean isNullEmail = false;
        boolean isBadEmail = false;
        Boolean isNoLogon = false;
        Object [] user = null;
        
        if (appUserScreen.getuNID() != null && 
            !appUserScreen.getuNID().trim().equals("")) {
          checkUsername = false;
        }        
        
        
        if (isNewAppUser) {
          if(checkUsername) {
            if (userNameAlreadyExists(sess, appUserScreen.getUserNameExternal(), null)) {
              this.addInvalidField("Username exists", "The User name " + appUserScreen.getUserNameExternal() + " already exists.  Please use another name.");
              isUsedUsername = true;
            }            
          } else {
            List existingUsers = uNID_AlreadyExists(sess, appUserScreen.getuNID(), null);
            if (existingUsers.size() > 0) {
              user = (Object[]) existingUsers.get(0);
              this.addInvalidField("uNID exists", "The uNID " + appUserScreen.getuNID() + " is already in use by " + user[1]  + " " + user[2] + ".  Please use another.");
              isUseduNID = true;
            }            
          }
          
          if(appUserScreen.getEmail() != null && !appUserScreen.getEmail().equals("") && !MailUtil.isValidEmail(appUserScreen.getEmail())){
            this.addInvalidField("invalid email", "The email address " + appUserScreen.getEmail() + " is not formatted properly.");
            isBadEmail = true; 
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
                appUser.setPasswordExpired("N");
                appUser.setPasswordExternal(encryptedPassword);      
              }
              
            }
           
            appUser.setManagingCoreFacilities(new HashSet());
            appUser.setCoreFacilitiesICanSubmitTo(new HashSet());
            sess.save(appUser);
            
          }
        } else {
          
          if(checkUsername) {
            if (userNameAlreadyExists(sess, appUserScreen.getUserNameExternal(), appUserScreen.getIdAppUser())) {
              this.addInvalidField("Username exists", "The User name " + appUserScreen.getUserNameExternal() + " already exists.  Please use another name.");
              isUsedUsername = true;
            }            
          } else {
            List existingUsers = uNID_AlreadyExists(sess, appUserScreen.getuNID(), appUserScreen.getIdAppUser());
            if (existingUsers.size() > 0) {
              user = (Object[]) existingUsers.get(0);
              this.addInvalidField("uNID exists", "The uNID " + appUserScreen.getuNID() + " is already in use by " + user[1]  + " " + user[2] + ".  Please use another.");
              isUseduNID = true;
            }            
          }          
          
          if(appUserScreen.getEmail() != null && !appUserScreen.getEmail().equals("") && !MailUtil.isValidEmail(appUserScreen.getEmail())){
            this.addInvalidField("invalid email", "The email address " + appUserScreen.getEmail() + " is not formatted properly.");
            isBadEmail = true; 
          }
          
          if (appUserScreen.getIsActive().equals("Y")) {
            if ((appUserScreen.getUserNameExternal() == null || appUserScreen.getPasswordExternal() == null) && appUserScreen.getuNID() == null) {
              this.addInvalidField("No Logon", "Please enter a user name and a password for an external user or a UNID for a university user.");
              isNoLogon = true;
            }
          }

          
          if (this.isValid()) {
            appUser = (AppUser)sess.load(AppUser.class, appUserScreen.getIdAppUser());
            String initialActiveStatus = appUser.getIsActive();
            initializeAppUser(appUser);
            String newActiveStatus = appUser.getIsActive();
            if(newActiveStatus != null && newActiveStatus.compareTo("Y")==0) {
              if(initialActiveStatus == null || initialActiveStatus.compareTo("Y")!=0) {
                
                Integer idCoreFacility = null;
                for(Iterator i = appUser.getLabs().iterator(); i.hasNext();) {
                  Lab l = (Lab)i.next();
                  for(Iterator j = l.getCoreFacilities().iterator(); j.hasNext(); ) {
                    CoreFacility facility = (CoreFacility)j.next();
                    if (idCoreFacility == null) {
                      idCoreFacility = facility.getIdCoreFacility();
                    } else if(!idCoreFacility.equals(facility.getIdCoreFacility())) {
                      idCoreFacility = null;
                      break;
                    }
                  }
                }
                
                PropertyDictionaryHelper pdh = PropertyDictionaryHelper.getInstance(sess);
                String coreFacilityContactEmail;
                if (idCoreFacility == null) {
                  coreFacilityContactEmail = pdh.getProperty(PropertyDictionary.GENERIC_NO_REPLY_EMAIL);
                } else {
                  coreFacilityContactEmail = ((CoreFacility)sess.load(CoreFacility.class, idCoreFacility)).getContactEmail();
                }
                if(appUser.getEmail() != null){
                  sendAccountActivatedEmail(appUser, coreFacilityContactEmail, sess);
                } else {
                  this.addInvalidField("No email address", "The account has been activated however the user will not be notified by email since one can't be found on file for this user.");
                  isNullEmail = true;
                }
              }
            }
          }

        }
        
        // If setting the user to group permission kind, then remove any core facilities they manage.
        if ( appUserScreen.getCodeUserPermissionKind().equals( UserPermissionKind.GROUP_PERMISSION_KIND )) {
          for(Iterator j = managingCoreFacilityIds.iterator();j.hasNext();) {
            CoreFacilityCheck chk = (CoreFacilityCheck)j.next();
            chk.selected = "N";
          }
        }

        // You can only edit core facilities you manage
        // If you are not a super admin, check to see if you are editing core facilities legally
        if (!this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_ADMINISTER_ALL_CORE_FACILITIES)) {

          for(Iterator chkIter = managingCoreFacilityIds.iterator();chkIter.hasNext();) {
            CoreFacilityCheck chk = (CoreFacilityCheck)chkIter.next();
            // Check to see if that core has been edited and if you have permission to edit it.
            if ( checkCoreHasBeenEdited(chk,appUser)  ) {                 
              if ( !checkCanEditCore(chk)) { 
                this.addInvalidField("Manage Core Facility", "You may only change core facility permissions for the core facilities you manage.");
                isManageFacilityError = true;
              }
            }
          }

        }
        
        if (!this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_ADMINISTER_ALL_CORE_FACILITIES)) {

          for(Iterator chkIter = coreFacilityIdsICanSubmitTo.iterator();chkIter.hasNext();) {
            CoreFacilityCheck chk = (CoreFacilityCheck)chkIter.next();
            // Check to see if that core has been edited and if you have permission to edit it.
            if ( checkCoreHasBeenEdited(chk,appUser)  ) {                 
              if ( !checkCanEditCore(chk)) { 
                this.addInvalidField("Manage Core Facility", "You may only change core facility permissions for the core facilities you manage.");
                isManageFacilityError = true;
              }
            }
          }

        }
        if (this.isValid()) {
          setManagingCoreFacilities(sess, appUser);
          setSubmittingCoreFacilities(sess, appUser);
          if (beingInactivated && appUser.getIsActive().equals("N")) {
        	  updateUserOnInactivation(sess, appUser);
          }
          sess.flush();
          this.xmlResult = "<SUCCESS idAppUser=\"" + appUser.getIdAppUser() + "\"/>";
          setResponsePage(this.SUCCESS_JSP);
        } else {
          if(isUsedUsername || isUseduNID || isManageFacilityError || isNullEmail || isBadEmail || isNoLogon) {
            if (isWebForm.equals("Y")) {
              String outMsg = "";
              if(isUsedUsername) {
                outMsg = "Username '" + appUserScreen.getUserNameExternal() + "' is already being used. Please select a different username.";              
              } else if (isUseduNID) {
                outMsg = "The uNID " + appUserScreen.getuNID() + " is already in use by " + user[1]  + " " + user[2] + ".  Please use another.";                            
              } else if (isManageFacilityError) {
                outMsg = "You may only change core facility permissions for the core facilities you manage.";
              } else if (isNoLogon) {
                outMsg = "Please enter a user name and a password for an external user or a UNID for a university user.";
              } else if (isNullEmail){
                outMsg = "The account has been activated. However, the user will not be notified by email since there is no email listed for this user.";
                sess.flush();
              } else{
                outMsg = "The email address " + appUserScreen.getEmail() + " is not formatted properly.";
              }
              if(isNullEmail){
                this.xmlResult = "<NULL_EMAIL_ERROR message=\"" + outMsg + "\"/>";
              } else{
                this.xmlResult = "<ERROR message=\"" + outMsg + "\"/>";
              }
              setResponsePage(this.SUCCESS_JSP);            
            } else {
              setResponsePage(this.ERROR_JSP);            
            }
                      
          } else {
            this.addInvalidField("Insufficient permissions", "Insufficient permission to save member.");
            setResponsePage(this.ERROR_JSP);
          }
        }
      }      
    }catch (Exception e){
      log.error("An exception has occurred in SaveAppUser ", e);
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
  
  private void updateUserOnInactivation(Session sess, AppUser appUser) {
	  appUser.getLabs().clear();
	  appUser.getCollaboratingLabs().clear();
	  appUser.getManagingLabs().clear();
	  appUser.getManagingCoreFacilities().clear();
      appUser.getCoreFacilitiesICanSubmitTo().clear();
  }
  
  private void sendAccountActivatedEmail(AppUser appUser, String coreFacilityContactEmail, Session sess)  throws NamingException, MessagingException, IOException {
    
    if (appUser.getEmail().equals("bademail@bad.com")) {
      throw new AddressException("'bademail@bad.com' not allowed");
    }
    
    String gnomexURL =  "<a href='" + url + "'>Click here</a> to login.";
    
    DictionaryHelper dictionaryHelper = DictionaryHelper.getInstance(sess);
    
    MailUtilHelper helper = new MailUtilHelper(	
    		appUser.getEmail(),
    		coreFacilityContactEmail,
    		"Your GNomEx account is now active",
    		"Welcome to GNomEx.  Your user account has been activated. " + gnomexURL,
    		null,
			true, 
			dictionaryHelper,
			serverName 									);
    helper.setRecipientAppUser(appUser);
    MailUtil.validateAndSendEmail(helper);
    
  }
  
  private void initializeAppUser(AppUser appUser) {
    appUser.setFirstName(appUserScreen.getFirstName());
    appUser.setLastName(appUserScreen.getLastName());
    appUser.setJobTitle(appUserScreen.getJobTitle());
    appUser.setInstitute(appUserScreen.getInstitute());
    appUser.setDepartment(appUserScreen.getDepartment());
    appUser.setEmail(appUserScreen.getEmail());
    appUser.setPhone(appUserScreen.getPhone());
    appUser.setUcscUrl(appUserScreen.getUcscUrl());
    appUser.setIsActive(appUserScreen.getIsActive());
    appUser.setCodeUserPermissionKind(appUserScreen.getCodeUserPermissionKind());
    appUser.setuNID(appUserScreen.getuNID());
    appUser.setUserNameExternal(appUserScreen.getUserNameExternal());
    
     
      // Blank out the external password if a UNID has been entered.
      if (appUserScreen.getuNID() != null && !appUserScreen.getuNID().trim().equals("")) {
        appUser.setPasswordExternal(null); 
        appUser.setUserNameExternal(null);
      }
      // Only encrypt and set the password if something has been entered in the text field.
      else {
        if (appUserScreen.getUserNameExternal() != null &&
            !appUserScreen.getUserNameExternal().trim().equals("")) {
          appUser.setuNID(null);
        }
        if (appUserScreen.getPasswordExternal() != null && 
            !appUserScreen.getPasswordExternal().trim().equals("") && 
            !appUserScreen.getPasswordExternal().equals(AppUser.MASKED_PASSWORD)) {
          String salt = passwordEncrypter.createSalt();
          String encryptedPassword = passwordEncrypter.createPassword(appUserScreen.getPasswordExternal(), salt);
          appUser.setPasswordExternal(encryptedPassword);
          appUser.setPasswordExpired("N");
          appUser.setSalt(salt);
        }
      }
    
    
  }
  
  private boolean checkCanEditCore (CoreFacilityCheck chk) {
    boolean isValidToEdit = false;
    
    // Check through the core facilities which can be managed
    for(Iterator i =  this.getSecAdvisor().getCoreFacilitiesIManage().iterator();i.hasNext();) {
      CoreFacility facility = (CoreFacility)i.next();
      // If the core facility in question is one of the core facilities the 
      // current user manages, then editing is allowed.
      if (chk.idCoreFacility.equals(facility.getIdCoreFacility())) {
        isValidToEdit = true;
        break;
      }
    }
    
    
    return isValidToEdit;
  }
  
  private boolean checkCoreHasBeenEdited(CoreFacilityCheck chk, AppUser appUser) {
    boolean hasBeenEdited = false;
    boolean existingManagingCore = false;
    
    // Look through the app user's current list of managing core facilities
    if (appUser != null && appUser.getManagingCoreFacilities() != null) {
      for(Iterator i = appUser.getManagingCoreFacilities().iterator();i.hasNext();) {
        CoreFacility facility = (CoreFacility)i.next();
        
        if (chk.idCoreFacility.equals(facility.getIdCoreFacility())) {
          // If the core facility is already being managed by the user, 
          // check to see if it has been changed to NOT being managed by the user any more.
          existingManagingCore = true;
          if (chk.selected.equals("N")) {
            hasBeenEdited = true;
          }
          break;
        }
      }
    }
    // If the core facility was not one that the user already manages, check to see if 
    // it is being changed to one that the user is going to manage.
    if ( !existingManagingCore ) {
      if (chk.selected.equals("Y")) {
        hasBeenEdited = true;
      }
    }
    
    return hasBeenEdited;
  }
  
  private void setManagingCoreFacilities(Session sess, AppUser appUser) {
    // Note that since only core facilities the logged in user can see will be in the
    // list from the front end, we ignore (i.e. neither add or remove) any ids not in
    // list from the front end.
    ArrayList<CoreFacility> facilitiesToRemove = new ArrayList<CoreFacility>();
    ArrayList<CoreFacility> idsToAdd = (ArrayList<CoreFacility>)managingCoreFacilityIds.clone();
    for(Iterator i = appUser.getManagingCoreFacilities().iterator();i.hasNext();) {
      CoreFacility facility = (CoreFacility)i.next();
      for(Iterator j = managingCoreFacilityIds.iterator();j.hasNext();) {
        CoreFacilityCheck chk = (CoreFacilityCheck)j.next();
        if (chk.idCoreFacility.equals(facility.getIdCoreFacility())) {
          idsToAdd.remove(chk);
          if (chk.selected.equals("N")) {
            facilitiesToRemove.add(facility);
          }
          break;
        }
      }
    }
    
    appUser.getManagingCoreFacilities().removeAll(facilitiesToRemove);
    for (Iterator k = idsToAdd.iterator();k.hasNext();) {
      CoreFacilityCheck chk = (CoreFacilityCheck)k.next();
      if (chk.selected.equals("Y")) {
        CoreFacility facility = (CoreFacility)sess.load(CoreFacility.class, chk.idCoreFacility);
        appUser.getManagingCoreFacilities().add(facility);
      }
    }
  }
  
  private void setSubmittingCoreFacilities(Session sess, AppUser appUser) {
    // Note that since only core facilities the logged in user can see will be in the
    // list from the front end, we ignore (i.e. neither add or remove) any ids not in
    // list from the front end.
    ArrayList<CoreFacility> facilitiesToRemove = new ArrayList<CoreFacility>();
    ArrayList<CoreFacility> idsToAdd = (ArrayList<CoreFacility>)coreFacilityIdsICanSubmitTo.clone();
    for(Iterator i = appUser.getCoreFacilitiesICanSubmitTo().iterator();i.hasNext();) {
      CoreFacility facility = (CoreFacility)i.next();
      for(Iterator j = coreFacilityIdsICanSubmitTo.iterator();j.hasNext();) {
        CoreFacilityCheck chk = (CoreFacilityCheck)j.next();
        if (chk.idCoreFacility.equals(facility.getIdCoreFacility())) {
          idsToAdd.remove(chk);
          if (chk.selected.equals("N")) {
            facilitiesToRemove.add(facility);
          }
          break;
        }
      }
    }
    
    appUser.getCoreFacilitiesICanSubmitTo().removeAll(facilitiesToRemove);
    for (Iterator k = idsToAdd.iterator();k.hasNext();) {
      CoreFacilityCheck chk = (CoreFacilityCheck)k.next();
      if (chk.selected.equals("Y")) {
        CoreFacility facility = (CoreFacility)sess.load(CoreFacility.class, chk.idCoreFacility);
        appUser.getCoreFacilitiesICanSubmitTo().add(facility);
      }
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
    buf.append("SELECT a.userNameExternal from AppUser as a where a.userNameExternal = :externalUserName");
    if (idAppUser != null) {
      buf.append(" AND a.idAppUser != :idAppUser");
    }
    Query query = sess.createQuery(buf.toString());
    query.setParameter("externalUserName", userNameExternal);
    if (idAppUser != null) {
    	query.setParameter("idAppUser", idAppUser);
    }
    List users = query.list();
    return users.size() > 0;    
  }
  
  private static List uNID_AlreadyExists(Session sess, String uNID, Integer idAppUser) {
    List users = new ArrayList();
    if (uNID == null || uNID.equals("")) {
      return users;
    }

    StringBuffer buf = new StringBuffer();
    buf.append("SELECT a.uNID, a.firstName, a.lastName from AppUser as a where a.uNID = '"); 
    buf.append(uNID + "'");
    if (idAppUser != null) {
      buf.append(" AND a.idAppUser != " + idAppUser);
    }
    users = sess.createQuery(buf.toString()).list();
    return users;    
  }

  private class CoreFacilityCheck implements Serializable {
    public Integer idCoreFacility;
    public String  selected;
  }
}