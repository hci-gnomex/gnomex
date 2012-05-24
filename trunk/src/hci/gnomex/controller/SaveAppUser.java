package hci.gnomex.controller;

import hci.gnomex.lucene.SearchListParser;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.CoreFacility;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.UserPermissionKind;
import hci.gnomex.security.EncrypterService;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;

import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;




public class SaveAppUser extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveAppUser.class);
  

  private AppUser               appUserScreen;
  private boolean              isNewAppUser = false;
  private ArrayList<CoreFacilityCheck>    managingCoreFacilityIds;
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
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
  }

  public Command execute() throws RollBackCommandException {
    

    
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      
      
      if (this.getSecurityAdvisor().hasPermission(SecurityAdvisor.CAN_ADMINISTER_USERS)) {
        AppUser appUser = null;
        
        boolean checkUsername = true;
        boolean isUsedUsername = false;
        boolean isUseduNID = false;
        boolean isManageFacilityError = false;
        
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
           
            appUser.setManagingCoreFacilities(new HashSet());
            sess.save(appUser);
            
          }
        } else {
          
          if(checkUsername) {
            if (userNameAlreadyExists(sess, appUserScreen.getUserNameExternal(), appUserScreen.getIdAppUser())) {
              this.addInvalidField("Username exists", "The User name " + appUserScreen.getUserNameExternal() + " already exists.  Please use another name.");
              isUsedUsername = true;
            }            
          } else {
            if (uNID_AlreadyExists(sess, appUserScreen.getuNID(), appUserScreen.getIdAppUser())) {
              this.addInvalidField("uNID exists", "The uNID " + appUserScreen.getuNID() + " already exists.  Please use another.");
              isUseduNID = true;
            }            
          }          
          
          if (this.isValid()) {
            appUser = (AppUser)sess.load(AppUser.class, appUserScreen.getIdAppUser());
            initializeAppUser(appUser);                
          }

        }

        // Only super admins can assign core facilities to labs.
        if (!this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_ADMINISTER_ALL_CORE_FACILITIES)) {
          if (!appUserScreen.getCodeUserPermissionKind().equals(UserPermissionKind.ADMIN_PERMISSION_KIND) 
              && !appUserScreen.getCodeUserPermissionKind().equals(UserPermissionKind.SUPER_ADMIN_PERMISSION_KIND)) {
            for(Iterator chkIter = managingCoreFacilityIds.iterator();chkIter.hasNext();) {
              CoreFacilityCheck chk = (CoreFacilityCheck)chkIter.next();
              if (chk.selected.equals("Y")) {
                this.addInvalidField("Manage Core Facility", "Only Admin or Super Admin users can manage core facilities.");
                isManageFacilityError = true;
              }
            }
          }
        }
        
        if (this.isValid()) {
          setCoreFacilities(sess, appUser);
          sess.flush();
          this.xmlResult = "<SUCCESS idAppUser=\"" + appUser.getIdAppUser() + "\"/>";
          setResponsePage(this.SUCCESS_JSP);
        } else {
          if(isUsedUsername || isUseduNID || isManageFacilityError) {
            String outMsg = "";
            if(isUsedUsername) {
              outMsg = "Username '" + appUserScreen.getUserNameExternal() + "' is already being used. Please select a different username.";              
            } else if (isUseduNID) {
              outMsg = "uNID '" + appUserScreen.getuNID() + "' is already being used. Please select a different uNID.";                            
            } else {
              outMsg = "Only Admin or Super Admin users can manage core facilities.";
            }
            this.xmlResult = "<ERROR message=\"" + outMsg + "\"/>";
            setResponsePage(this.SUCCESS_JSP);            
          } else {
            setResponsePage(this.ERROR_JSP);            
          }
        }
      
      } else {
        this.addInvalidField("Insufficient permissions", "Insufficient permission to save member.");
        setResponsePage(this.ERROR_JSP);
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
          String encryptedPassword = EncrypterService.getInstance().encrypt(appUserScreen.getPasswordExternal());
          appUser.setPasswordExternal(encryptedPassword);      
        }
      }
    
    
  }
  
  private void setCoreFacilities(Session sess, AppUser appUser) {
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

  private class CoreFacilityCheck implements Serializable {
    public Integer idCoreFacility;
    public String  selected;
  }
}