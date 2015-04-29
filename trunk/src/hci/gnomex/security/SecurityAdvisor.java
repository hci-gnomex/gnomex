package hci.gnomex.security;

import hci.bstx.utility.BSTXSecurityAdvisor;
import hci.dictionary.model.DictionaryEntry;
import hci.dictionary.model.NullDictionaryEntry;
import hci.framework.model.DetailObject;
import hci.framework.security.UnknownPermissionException;
import hci.gnomex.constants.Constants;
import hci.gnomex.lucene.GlobalIndexHelper;
import hci.gnomex.model.Analysis;
import hci.gnomex.model.AnalysisCollaborator;
import hci.gnomex.model.AnalysisGroup;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.AppUserLite;
import hci.gnomex.model.CoreFacility;
import hci.gnomex.model.DataTrack;
import hci.gnomex.model.DataTrackFolder;
import hci.gnomex.model.DictionaryEntryUserOwned;
import hci.gnomex.model.ExperimentCollaborator;
import hci.gnomex.model.FAQ;
import hci.gnomex.model.FlowCell;
import hci.gnomex.model.Institution;
import hci.gnomex.model.Lab;
import hci.gnomex.model.NewsItem;
import hci.gnomex.model.PlateType;
import hci.gnomex.model.PlateWell;
import hci.gnomex.model.Project;
import hci.gnomex.model.Property;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.Request;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.model.RequestStatus;
import hci.gnomex.model.Sample;
import hci.gnomex.model.SlideProduct;
import hci.gnomex.model.Topic;
import hci.gnomex.model.UserPermissionKind;
import hci.gnomex.model.Visibility;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateBSTXSession;
import hci.gnomex.utility.HibernateGuestSession;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.LabComparator;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;


public class SecurityAdvisor extends DetailObject implements Serializable, hci.framework.security.SecurityAdvisor {
  // Security advisor session variable
  public static final String          SECURITY_ADVISOR_SESSION_KEY       = "gnomexSecurityAdvisor";

  private static final String         RESTRICTED = "(restricted)";

  private static org.apache.log4j.Logger log                             = org.apache.log4j.Logger.getLogger(SecurityAdvisor.class);

  public static final int            PROFILE_OBJECT_VISIBILITY          = 1;
  public static final int            PROFILE_GROUP_MEMBERSHIP           = 2;
  public static final int            SAMPLES_UPDATE                     = 3;


  // Global Permissions
  public static final String          CAN_WRITE_DICTIONARIES                      = "canWriteDictionaries";            
  public static final String          CAN_WRITE_PROPERTY_DICTIONARY               = "canWritePropertyDictionary";
  public static final String          CAN_MANAGE_WORKFLOW                         = "canManageWorkflow";
  public static final String          CAN_MANAGE_BILLING                          = "canManageBilling";
  public static final String          CAN_ADMINISTER_USERS                        = "canAdministerUsers";
  public static final String          CAN_ACCESS_ANY_OBJECT                       = "canAccessAnyObject";
  public static final String          CAN_WRITE_ANY_OBJECT                        = "canWriteAnyObject";
  public static final String          CAN_DELETE_ANY_PROJECT                      = "canDeleteAnyProject";            
  public static final String          CAN_DELETE_REQUESTS                         = "canDeleteRequests";   
  public static final String          CAN_MANAGE_DNA_SEQ_CORE                     = "canManageDNASeqCore";
  public static final String          CAN_MANAGE_GENOMICS_CORE                    = "canManageGenomicsCore";
  public static final String          CAN_SUBMIT_FOR_OTHER_CORES                  = "canSubmitForOtherCores";

  public static final String          CAN_PARTICIPATE_IN_GROUPS                   = "canParticipateInGroups";            
  public static final String          CAN_SUBMIT_REQUESTS                         = "canSubmitRequests";            

  public static final String          CAN_MANAGE_DASHBOARD                        = "canManageDashboard";
  public static final String          CAN_RECEIVE_ADMIN_NOTIFICATION              = "canReceiveAdminNotification";
  public static final String          CAN_RECEIVE_BILLING_NOTIFICATION            = "canReceiveBillingNotification";
  public static final String          CAN_RECEIVE_WORKFLOW_NOTIFICATION           = "canReceiveWorkflowNotification";

  public static final String          CAN_BE_LAB_MEMBER                           = "canBeLabMember";  
  public static final String          CAN_BE_LAB_COLLABORATOR                     = "canBeLabCollaborator";            
  public static final String          CAN_SUBMIT_WORK_AUTH_FORMS                  = "canSubmitWorkAuthForms";            

  public static final String          CAN_ADMINISTER_ALL_CORE_FACILITIES          = "canAdministerAllCoreFacilities";          
  public static final String          CAN_ASSIGN_SUPER_ADMIN_ROLE                 = "canAssignSuperAdminRole";

  public static final String          USER_SCOPE_LEVEL  = "USER";
  public static final String          GROUP_SCOPE_LEVEL = "GROUP";
  public static final String          ALL_SCOPE_LEVEL   = "ALL";


  // Session info
  private AppUser                      appUser;
  private boolean                     isGuest = false;
  private boolean                     isGNomExUniversityUser = false;
  private boolean                     isGNomExExternalUser = false;
  private boolean                     isUniversityOnlyUser = false;
  private boolean                     isLabManager = false;
  private boolean                     isReadOnlySession = false;
  private boolean                     canAccessBSTX = false;
  private Integer                     specifiedIdCoreFacility = null;

  // version info
  private String                       version;

  // Global permission map
  private Map                          globalPermissionMap = new HashMap();

  // ids of core facilities that allow users to have global submission privileges.
  private Map<Integer, Integer>        coreFacilitiesAllowingGlobalSubmission = new HashMap<Integer, Integer>();

  private String                       loginDateTime;

  private BSTXSecurityAdvisor          bstxSecurityAdvisor;

  private String                       ntUserName;

  public String getLoginDateTime() {
    return loginDateTime;
  }

  private SecurityAdvisor(AppUser appUser, boolean isGNomExUniversityUser, boolean isGNomExExternalUser, boolean isUniversityOnlyUser, boolean isLabManager, boolean canAccessBSTX, String ntUserName, Integer idCoreFacility, Map<Integer, Integer>submitMap) throws InvalidSecurityAdvisorException {

    this.appUser = appUser;
    this.isGNomExUniversityUser = isGNomExUniversityUser;
    this.isGNomExExternalUser = isGNomExExternalUser;
    this.isUniversityOnlyUser = isUniversityOnlyUser;
    this.loginDateTime = new SimpleDateFormat("MMM dd hh:mm a").format(System.currentTimeMillis());
    this.isLabManager = isLabManager;
    this.canAccessBSTX = canAccessBSTX;
    this.ntUserName = ntUserName;
    this.specifiedIdCoreFacility = idCoreFacility;
    this.coreFacilitiesAllowingGlobalSubmission = submitMap;

    setGlobalPermissions();    
    validate();
  }

  private SecurityAdvisor(Integer idCoreFacility) throws InvalidSecurityAdvisorException {
    isGuest = true;
    isGNomExUniversityUser = false;
    isGNomExExternalUser = false;
    isUniversityOnlyUser = false;
    isLabManager = false;
    canAccessBSTX = false;
    this.loginDateTime = new SimpleDateFormat("MMM-dd HH:mm").format(System.currentTimeMillis());
    this.specifiedIdCoreFacility = idCoreFacility;
    this.coreFacilitiesAllowingGlobalSubmission = new HashMap<Integer, Integer>();
    setGlobalPermissions();
  }
  /*
  public boolean isAdmin() {
    return this.hasPermission(CAN_ACCESS_ANY_OBJECT);
  }
   */
  public boolean isGuest() {
    return isGuest;
  }

  public boolean isUniversityOnlyUser() {
    return isUniversityOnlyUser;
  }

  public boolean isLabManager() {
    return isLabManager;
  }

  public String getIsGuest() {
    if (isGuest) {
      return "Y";
    }
    return "N";
  }

  public String getIsUniversityOnlyUser() {
    if (this.isUniversityOnlyUser) {
      return "Y";
    }
    return "N";
  }


  public String getIsExternalUser() {
    if (this.isGNomExExternalUser) {
      return "Y";
    }
    return "N";
  }

  public Integer getSpecifiedIdCoreFacility() {
    return this.specifiedIdCoreFacility;
  }

  public static SecurityAdvisor create(Session   sess, 
      String    uid) throws InvalidSecurityAdvisorException {
    return create(sess, uid, null);
  }

  public static SecurityAdvisor create(Session   sess, 
      String    uid,
      Integer   idCoreFacility) throws InvalidSecurityAdvisorException {
    SecurityAdvisor securityAdvisor = null;

    // If the login is "guest" just instantiate a security advisor
    // as 'guest'.
    if (uid.equalsIgnoreCase("guest")) {
      return new SecurityAdvisor(idCoreFacility);
    }

    boolean isGNomExUniversityUser = false;
    boolean isGNomExExternalUser = false;
    boolean isUniversityOnlyUser = true;
    boolean isLabManager = false;
    boolean canAccessBSTX = false;

    // Is this a GNomEx university user?
    StringBuffer queryBuf = new StringBuffer();
    queryBuf.append(" SELECT user from AppUser as user ");
    queryBuf.append(" WHERE  user.uNID =  '" + uid + "' ");

    List users = sess.createQuery(queryBuf.toString()).list();

    AppUser appUser = null;
    if (users.size() > 0) {
      appUser = (AppUser)users.get(0);
      Hibernate.initialize(appUser.getLabs());
      Hibernate.initialize(appUser.getCollaboratingLabs());
      Hibernate.initialize(appUser.getManagingLabs());
      Hibernate.initialize(appUser.getManagingCoreFacilities());
      Hibernate.initialize(appUser.getCoreFacilitiesICanSubmitTo());


      isGNomExUniversityUser = true;
      isGNomExExternalUser = false;
      isUniversityOnlyUser = false;
    }

    // Is this a GNomEx external user?
    if (appUser == null) {
      queryBuf = new StringBuffer();
      queryBuf.append(" SELECT user from AppUser as user ");
      queryBuf.append(" WHERE  user.userNameExternal =  '" + uid + "' ");

      users = sess.createQuery(queryBuf.toString()).list();

      appUser = null;
      if (users.size() > 0) {
        appUser = (AppUser)users.get(0);
        Hibernate.initialize(appUser.getLabs());
        Hibernate.initialize(appUser.getCollaboratingLabs());
        Hibernate.initialize(appUser.getManagingLabs());
        Hibernate.initialize(appUser.getManagingCoreFacilities());
        Hibernate.initialize(appUser.getCoreFacilitiesICanSubmitTo());

        isGNomExExternalUser = true;
        isGNomExUniversityUser = false;
        isUniversityOnlyUser = false;
      }

    }

    // Is this a non-GNomEx University user?
    if (appUser == null) {
      isUniversityOnlyUser = true;
      isGNomExExternalUser = false;
      isGNomExUniversityUser = false;

      appUser = new AppUser();
      appUser.setuNID(uid);
      appUser.setCodeUserPermissionKind(UserPermissionKind.UNIVERSITY_ONLY_PERMISSION_KIND);

      //TODO: Would like to get user name from UofU LDAP
      appUser.setLastName("University User " + uid);
      appUser.setFirstName("");
      appUser.setLabs(new TreeSet());
      appUser.setCollaboratingLabs(new TreeSet());
      appUser.setManagingLabs(new TreeSet());
      appUser.setManagingCoreFacilities(new TreeSet());
      appUser.setCoreFacilitiesICanSubmitTo(new TreeSet());

    }

    if (appUser == null) {
      throw new InvalidSecurityAdvisorException("Cannot find AppUser " + uid);
    } else {
      if(appUser.getManagingLabs() != null && appUser.getManagingLabs().size() > 0) {
        isLabManager = true;
      }
    }

    // check if can access BSTX
    String ntUserName = null;
    if (isGNomExUniversityUser) {
      String canAccessBSTXString = PropertyDictionaryHelper.getInstance(sess).getProperty(PropertyDictionary.CAN_ACCESS_BSTX);
      if (canAccessBSTXString != null && canAccessBSTXString.equals("Y")) {
        ntUserName = getNtUserName(uid);
        if (ntUserName != null) {
          canAccessBSTX = true;
        }
      }
    }

    HashMap<Integer, Integer>submitMap = new HashMap<Integer,Integer>();
    for (Iterator i = CoreFacility.getActiveCoreFacilities(sess).iterator(); i.hasNext();) {
      DictionaryEntry de = (DictionaryEntry)i.next();

      if (de == null) {
        continue;
      }

      CoreFacility cf = (CoreFacility)de;
      String canHaveGlobalSubmission = PropertyDictionaryHelper.getInstance(sess).getCoreFacilityProperty(cf.getIdCoreFacility(), PropertyDictionary.ALLOW_CORE_GLOBAL_SUBMISSION);
      if (canHaveGlobalSubmission != null && canHaveGlobalSubmission.equals("Y")) {
        submitMap.put(cf.getIdCoreFacility(), cf.getIdCoreFacility());
      }
    }

    // Instantiate SecurityAdvisor
    securityAdvisor = new SecurityAdvisor(appUser, isGNomExUniversityUser, isGNomExExternalUser, isUniversityOnlyUser, isLabManager, canAccessBSTX, ntUserName, idCoreFacility, submitMap);
    // Make sure we have a valid state.
    securityAdvisor.validate();
    // Initialize institutions (lazy loading causing invalid object
    securityAdvisor.getInstitutionsIAmMemberOf();

    return securityAdvisor;
  }

  private static String getNtUserName(String uid) {
    String name = null;
    String peopleSoftID = "0" + uid.substring(1);
    try {
      Session bstxSession = HibernateBSTXSession.currentBSTXSession(uid);
      Query query = bstxSession.createQuery("SELECT ntUserName, idSite, assocPreferredName, assocFirstName, assocLastName, personID " +
      "FROM Associate WHERE peopleSoftID = :peopleSoftID");
      query.setParameter("peopleSoftID", peopleSoftID);
      List assocUsers = query.list();
      if (assocUsers.size() > 0) {
        Object[] row = (Object[])assocUsers.get(0);
        name = (String)row[0];
      }
    } catch(Exception ex) {
      log.error("Error querying associate table.", ex);
    } finally {
      try {
        HibernateBSTXSession.closeBSTXSession();
      } catch(Exception ex) {
        log.error("Error closing BSTX session", ex);
      }
    }

    return name;
  }

  public static SecurityAdvisor createGuest()
  throws InvalidSecurityAdvisorException {
    SecurityAdvisor securityAdvisor = null;

    securityAdvisor = new SecurityAdvisor(null);


    return securityAdvisor;
  }


  public boolean hasPermission(String permission) {
    return globalPermissionMap.containsKey(new Permission(permission));
  }


  public boolean canRead(DetailObject object) throws UnknownPermissionException {
    boolean canRead = false;

    // 
    // Request
    //
    if (object instanceof Request) {
      Request req = (Request)object;

      // Super Admins
      if (hasPermission(this.CAN_ADMINISTER_ALL_CORE_FACILITIES)) {
        canRead = true;
      } // Admins - restrict to core facility
      else if (hasPermission(this.CAN_ACCESS_ANY_OBJECT)) {
        canRead = isCoreFacilityIManage(req.getIdCoreFacility()) || this.isCoreFacilityICanSubmitTo(req.getIdCoreFacility());
      } 
      // Normal gnomex users
      else if (hasPermission(this.CAN_PARTICIPATE_IN_GROUPS)) {
        // First, check to see if the user is a specified as a collaborator
        // on this request.
        for (Iterator i = req.getCollaborators().iterator(); i.hasNext();) {
          ExperimentCollaborator collaborator = (ExperimentCollaborator)i.next();
          if (isLoggedInUser(collaborator.getIdAppUser())) {
            canRead = true;
            break;
          }
        }

        //Is this a request that was submitted by user who can submit on behalf of other cores
        if(hasPermission(SecurityAdvisor.CAN_SUBMIT_FOR_OTHER_CORES) && this.isLabICanSubmitTo(req.getLab()) && this.appUser.getIdAppUser().equals(req.getIdSubmitter())) {
          canRead = true;
        }

        // Now look at the visibility and see if this user has access
        if (!canRead) {
          // Request has owner visibility
          if (req.getCodeVisibility().equals(Visibility.VISIBLE_TO_OWNER)) {
            // Is owner of request or manager of lab's request
            if (isOwner(req.getIdAppUser()) || isGroupIManage(req.getIdLab())) {
              canRead = true;
            }            
          }
          // Request has membership visibility
          else if (req.getCodeVisibility().equals(Visibility.VISIBLE_TO_GROUP_MEMBERS)) {
            if (isGroupIAmMemberOf(req.getIdLab()) || isGroupIManage(req.getIdLab())) {
              canRead = true;
            }            
          }
          // Request has membership + collaborator visibility
          else if (req.getCodeVisibility().equals(Visibility.VISIBLE_TO_GROUP_MEMBERS_AND_COLLABORATORS)) {
            if (isGroupIAmMemberOf(req.getIdLab()) || 
                isGroupIManage(req.getIdLab()) || 
                isGroupICollaborateWith(req.getIdLab())) {
              canRead = true;
            } 
          }
          // Request has institution visibility
          else if (req.getCodeVisibility().equals(Visibility.VISIBLE_TO_INSTITUTION_MEMBERS)) {
            if (isInstitutionIAmMemberOf(req.getIdInstitution())) {
              canRead = true;
            }            
          }
          // Request has public visibility
          else if (req.getCodeVisibility().equals(Visibility.VISIBLE_TO_PUBLIC)) {
            canRead = true;
          }          
        }
      } 
      else if(hasPermission(SecurityAdvisor.CAN_SUBMIT_FOR_OTHER_CORES) && this.isLabICanSubmitTo(req.getLab()) && this.appUser.getIdAppUser().equals(req.getIdSubmitter())) {
        canRead = true;
      }
      // Guest users
      else {
        // Request has public visibility
        if (req.getCodeVisibility().equals(Visibility.VISIBLE_TO_PUBLIC)) {
          canRead = true;
        }
      }
      if (canRead) {
        canRead = checkBSTXSecurity(object);
      }
    }
    // 
    // Analysis
    //
    else if (object instanceof Analysis) {
      if (hasPermission(this.CAN_ACCESS_ANY_OBJECT)){
        canRead = true;
      }
      // Normal gnomex users
      else if (hasPermission(this.CAN_PARTICIPATE_IN_GROUPS)) {
        Analysis a = (Analysis)object;

        // First check to see if user is listed as a collaborator on this specific
        // analysis
        for (Iterator i = a.getCollaborators().iterator(); i.hasNext();) {
          AnalysisCollaborator collaborator = (AnalysisCollaborator)i.next();
          if (this.isLoggedInUser(collaborator.getIdAppUser())) {
            canRead = true;
            break;
          }
        }

        // Next, look at visibility to find out if user has access to this
        // analysis.
        if (!canRead) {
          // Analysis has owner visibility
          if (a.getCodeVisibility().equals(Visibility.VISIBLE_TO_OWNER)) {
            // Is owner of analysis or manager of lab's analysis
            if (isOwner(a.getIdAppUser()) || isGroupIManage(a.getIdLab())) {
              canRead = true;
            }            
          }        
          // Analysis has membership visibility
          else if (a.getCodeVisibility().equals(Visibility.VISIBLE_TO_GROUP_MEMBERS)) {
            if (isGroupIAmMemberOf(a.getIdLab()) || isGroupIManage(a.getIdLab())) {
              canRead = true;
            }            
          }
          // Analysis has membership + collaborator visiblity
          else if (a.getCodeVisibility().equals(Visibility.VISIBLE_TO_GROUP_MEMBERS_AND_COLLABORATORS)) {
            if (isGroupIAmMemberOf(a.getIdLab()) || 
                isGroupIManage(a.getIdLab()) || 
                isGroupICollaborateWith(a.getIdLab())) {
              canRead = true;
            } 
          }
          // Analysis has institution visibility
          else if (a.getCodeVisibility().equals(Visibility.VISIBLE_TO_INSTITUTION_MEMBERS)) {
            if (isInstitutionIAmMemberOf(a.getIdInstitution())) {
              canRead = true;
            }            
          }
          // Analysis has public visibility
          else if (a.getCodeVisibility().equals(Visibility.VISIBLE_TO_PUBLIC)) {
            canRead = true;
          }

        }

      }
      // Guest users
      else {
        Analysis a = (Analysis)object;

        // Request has public visibility
        if (a.getCodeVisibility().equals(Visibility.VISIBLE_TO_PUBLIC)) {
          canRead = true;
        }
      }
    }
    // 
    // DataTrack
    //
    else if (object instanceof DataTrack) {
      // Admins
      if (hasPermission(this.CAN_ACCESS_ANY_OBJECT)) {
        canRead = true;
      }
      // Normal gnomex users
      else if (hasPermission(this.CAN_PARTICIPATE_IN_GROUPS)) {
        DataTrack dt = (DataTrack)object;

        // First check to see if user is listed as a collaborator on this specific
        // data track
        for (Iterator i = dt.getCollaborators().iterator(); i.hasNext();) {
          AppUser collaborator = (AppUser)i.next();
          if (this.isLoggedInUser(collaborator.getIdAppUser())) {
            canRead = true;
            break;
          }
        }

        // Next, look at visibility to find out if user has access to this
        // data track.
        if (!canRead) {
          // DataTrack has owner visibility
          if (dt.getCodeVisibility().equals(Visibility.VISIBLE_TO_OWNER)) {
            // Is owner of data track or manager of lab's data track
            if (isOwner(dt.getIdAppUser()) || isGroupIManage(dt.getIdLab())) {
              canRead = true;
            }            
          }        
          // DataTrack has membership visibility
          else if (dt.getCodeVisibility().equals(Visibility.VISIBLE_TO_GROUP_MEMBERS)) {
            if (isGroupIAmMemberOf(dt.getIdLab()) || isGroupIManage(dt.getIdLab())) {
              canRead = true;
            }            
          }
          // DataTrack has membership + collaborator visiblity
          else if (dt.getCodeVisibility().equals(Visibility.VISIBLE_TO_GROUP_MEMBERS_AND_COLLABORATORS)) {
            if (isGroupIAmMemberOf(dt.getIdLab()) || 
                isGroupIManage(dt.getIdLab()) || 
                isGroupICollaborateWith(dt.getIdLab())) {
              canRead = true;
            } 
          }
          // DataTrack has institution visibility
          else if (dt.getCodeVisibility().equals(Visibility.VISIBLE_TO_INSTITUTION_MEMBERS)) {
            if (isInstitutionIAmMemberOf(dt.getIdInstitution())) {
              canRead = true;
            }            
          }
          // DataTrack has public visibility
          else if (dt.getCodeVisibility().equals(Visibility.VISIBLE_TO_PUBLIC)) {
            canRead = true;
          }

        }

      }
      // Guest users
      else {
        DataTrack a = (DataTrack)object;

        // Request has public visibility
        if (a.getCodeVisibility().equals(Visibility.VISIBLE_TO_PUBLIC)) {
          canRead = true;
        }
      }
    }

    //
    // Project
    //
    else if (object instanceof Project) {

      // Admins
      if (hasPermission(this.CAN_ACCESS_ANY_OBJECT)) {
        canRead = true;
      }
      // Submitters need to read any project.
      else if (hasPermission(this.CAN_SUBMIT_FOR_OTHER_CORES)) {
        canRead = true;
      }
      // GNomEx Users
      else if (hasPermission(this.CAN_PARTICIPATE_IN_GROUPS)) {
        Project proj = (Project)object;
        // Projects can be read if user is member of project's lab, collaborator of project's lab, or manager of project's lab
        if (isGroupIAmMemberOf(proj.getIdLab()) || isGroupIManage(proj.getIdLab()) || isGroupICollaborateWith(proj.getIdLab())) {
          canRead = true;
        } else {
          if (proj.hasPublicRequest()) {
            canRead = true;
          }

        }
      }  
      // Guests
      else {
        Project proj = (Project)object;
        // Guests can read project if any of its requests are visible to public
        if (proj.hasPublicRequest()) {
          canRead = true;
        }
      }
    } 

    //
    // AnalysisGroup
    //
    else if (object instanceof AnalysisGroup) {

      // Admins
      if (hasPermission(this.CAN_ACCESS_ANY_OBJECT)) {
        canRead = true;
      }
      // GNomEx Users
      else if (hasPermission(this.CAN_PARTICIPATE_IN_GROUPS)) {
        AnalysisGroup ag = (AnalysisGroup)object;
        if (isGroupIAmMemberOf(ag.getIdLab()) || isGroupIManage(ag.getIdLab()) || isGroupICollaborateWith(ag.getIdLab())) {
          canRead = true;
        } else {
          if (ag.hasPublicAnalysis()) {
            canRead = true;
          }
        }
      }  
      // Guests
      else {
        AnalysisGroup ag = (AnalysisGroup)object;
        // Analysis group is accessible if any of its analysis are marked as public
        if (ag.hasPublicAnalysis()) {
          canRead = true;
        }
      }     
    } 
    //
    // DataTrackFolder
    //
    else if (object instanceof DataTrackFolder) {

      // Admins
      if (hasPermission(this.CAN_ACCESS_ANY_OBJECT)) {
        canRead = true;
      }
      // GNomEx Users
      else if (hasPermission(this.CAN_PARTICIPATE_IN_GROUPS)) {
        DataTrackFolder dtf = (DataTrackFolder)object;
        if (isGroupIAmMemberOf(dtf.getIdLab()) || isGroupIManage(dtf.getIdLab()) || isGroupICollaborateWith(dtf.getIdLab())) {
          canRead = true;
        } else {
          if (dtf.hasPublicDataTracks()) {
            canRead = true;
          }
        }
      }  
      // Guests
      else {
        DataTrackFolder dtf = (DataTrackFolder)object;
        // Analysis group is accessible if any of its analysis are marked as public
        if (dtf.hasPublicDataTracks()) {
          canRead = true;
        }
      }     
    } 
    // 
    // SlideProduct
    //
    else if (object instanceof SlideProduct) {
      // Admins can read all slide products
      if (hasPermission(this.CAN_ACCESS_ANY_OBJECT)) {
        canRead = true;
      } else  {
        SlideProduct sp = (SlideProduct)object;
        if (sp.hasPublicExperiments()){
          // Allow anyone read access to slide product it if it
          // it is used on public experiments.
          canRead = true;
        } else if (hasPermission(this.CAN_PARTICIPATE_IN_GROUPS))
          // Normal gnomex users can read any slide that is
          // not custom and can look at custom slides from their
          // labs
          if (sp.getIdLab() == null) {
            canRead = true;
          } else if (isGroupIAmMemberOf(sp.getIdLab()) || 
              isGroupIManage(sp.getIdLab()) || 
              isGroupICollaborateWith(sp.getIdLab())) {
            // Only lab members, collaborators, and managers can
            // read slide products that are custom for their own lab
            canRead = true;
          } 
      }
    }
    //
    // Dictionary
    //    
    else if (object instanceof DictionaryEntry) {
      // Admins can read (almost) every dictionary entry
      if (hasPermission(this.CAN_ACCESS_ANY_OBJECT)) {
        canRead = true;

      }
      // Filter out server-only properties
      else if (object instanceof PropertyDictionary) {
        PropertyDictionary prop = (PropertyDictionary)object;
        if (!prop.getForServerOnly().equals("Y")) {
          canRead = true;
        } 
      }
      // All null dictionary entries can be read, except for the
      // null entry from AppUserLite.
      else if (object instanceof NullDictionaryEntry) {
        NullDictionaryEntry de = (NullDictionaryEntry)object;
        // Filter out null AppUserLite entry if user is not an admin
        if (!de.getDictionaryClassName().equals("hci.gnomex.model.AppUserLite")) {
          canRead = true;
        }    
      }
      // All other dictionary entries are readable
      else {
        canRead = true;
      }

    }
    //
    // Topic
    //
    else if (object instanceof Topic) {
      // Admins
      if (hasPermission(this.CAN_ACCESS_ANY_OBJECT)) {
        canRead = true;
      }
      // Normal gnomex users
      else if (hasPermission(this.CAN_PARTICIPATE_IN_GROUPS)) {
        Topic t = (Topic)object;

        // Next, look at visibility to find out if user has access to this
        // topic
        if (!canRead) {
          // DataTrack has owner visibility
          if (t.getCodeVisibility().equals(Visibility.VISIBLE_TO_OWNER)) {
            // Is owner of data track or manager of lab's data track
            if (isOwner(t.getIdAppUser()) || isGroupIManage(t.getIdLab())) {
              canRead = true;
            }            
          }        
          // Topic has membership visibility
          else if (t.getCodeVisibility().equals(Visibility.VISIBLE_TO_GROUP_MEMBERS)) {
            if (isGroupIAmMemberOf(t.getIdLab()) || isGroupIManage(t.getIdLab())) {
              canRead = true;
            }            
          }
          // Topic has membership + collaborator visiblity
          else if (t.getCodeVisibility().equals(Visibility.VISIBLE_TO_GROUP_MEMBERS_AND_COLLABORATORS)) {
            if (isGroupIAmMemberOf(t.getIdLab()) || 
                isGroupIManage(t.getIdLab()) || 
                isGroupICollaborateWith(t.getIdLab())) {
              canRead = true;
            } 
          }
          // Topic has institution visibility
          else if (t.getCodeVisibility().equals(Visibility.VISIBLE_TO_INSTITUTION_MEMBERS)) {
            if (isInstitutionIAmMemberOf(t.getIdInstitution())) {
              canRead = true;
            }            
          }
          // DataTrack has public visibility
          else if (t.getCodeVisibility().equals(Visibility.VISIBLE_TO_PUBLIC)) {
            canRead = true;
          }

        }

      }
      // Guest users
      else {
        Topic t = (Topic)object;

        // Request has public visibility
        if (t.getCodeVisibility().equals(Visibility.VISIBLE_TO_PUBLIC)) {
          canRead = true;
        }
      }
      /*      
      // Admins
      if (hasPermission(this.CAN_ACCESS_ANY_OBJECT)) {
        canRead = true;
      }
      // GNomEx Users
      else if (hasPermission(this.CAN_PARTICIPATE_IN_GROUPS)) {
        Topic t = (Topic)object;
        if (isGroupIAmMemberOf(t.getIdLab()) || isGroupIManage(t.getIdLab()) || isGroupICollaborateWith(t.getIdLab())) {
          canRead = true;
        } else {
          if (t.hasPublicChildren()) {
            canRead = true;
          }
        }
      }  
      // Guests
      else {
        Topic t = (Topic)object;
        // Analysis group is accessible if any of its analysis are marked as public
        if (t.hasPublicChildren()) {
          canRead = true;
        }
      }*/     
    }       



    if (canRead) {
      // Property dictionaries are a special case of the a dictionary.
      // Super admins can read them, but admins should only see them
      // for those that aren't associated with a specific core facility
      // or are associated with a core facility that the user, as an admin either
      // manages or as a user is associated with via the lab membership.
      if (!hasPermission(this.CAN_ADMINISTER_ALL_CORE_FACILITIES)) {
        if (object instanceof PropertyDictionary) {
          PropertyDictionary prop1 = (PropertyDictionary)object;
          if (prop1.getIdCoreFacility() != null) {
            // Only show properties with core facility user can see.
            Boolean found = false;
            if (this.getCoreFacilitiesIManage() != null) {
              for(Iterator facilityIter = this.getCoreFacilitiesIManage().iterator();facilityIter.hasNext();) {
                CoreFacility facility = (CoreFacility)facilityIter.next();
                if (facility.getIdCoreFacility().equals(prop1.getIdCoreFacility())) {
                  found = true;
                }
              }
            }
            // ?? Should we allow normal users to see properties specific
            // to a core facility?
            if (this.getCoreFacilitiesForMyLab() != null) {
              for(Iterator facilityIter = this.getCoreFacilitiesForMyLab().iterator();facilityIter.hasNext();) {
                CoreFacility facility = (CoreFacility)facilityIter.next();
                if (facility.getIdCoreFacility().equals(prop1.getIdCoreFacility())) {
                  found = true;
                }
              }
            }
            canRead = found;
          }
        }
      }
    }
    return canRead;
  }

  public boolean canRead(DetailObject object, int dataProfile) throws UnknownPermissionException {
    throw new UnknownPermissionException("Unimplemented method");    
  }

  public boolean canRead(Session sess, Class theClass, Integer id, int dataProfile) throws UnknownPermissionException {
    throw new UnknownPermissionException("Unimplemented method");
  }


  public boolean checkBSTXSecurity(DetailObject object) {
    canRead = true;
    if (object instanceof Request) {
      Request req = (Request)object;
      if (req.getRequestCategory().getIsClinicalResearch() != null && req.getRequestCategory().getIsClinicalResearch().equals("Y")) {
        if (this.canAccessBSTX) {
          if (this.bstxSecurityAdvisor != null) {
            canRead = canReadAllCCNumbers(req);
          }
        } else {
          canRead = false;
        }
      }
    }

    return canRead;
  }

  private Map<String, boolean[]> canReadCCNumbers(List<String> ccNumbers) throws NamingException, SQLException {
    Map<String, boolean[]> secMap = new HashMap<String, boolean[]>();
    if (bstxSecurityAdvisor != null) {
      try {
        Session bstxSession = HibernateBSTXSession.currentBSTXSession(this.username);
        secMap = bstxSecurityAdvisor.canReadSamples(ccNumbers, bstxSession);
      } finally {
        try {
          HibernateBSTXSession.closeBSTXSession();
        } catch(Exception e) {
          log.error("Error closing BSTX session", e);
        }
      }
    }
    return secMap;
  }

  public boolean canReadAllCCNumbers(Request req) {
    boolean canRead = true;
    if (bstxSecurityAdvisor == null) {
      canRead = false;
    } else {
      try {
        List<String> ccNumbers = new ArrayList<String>();
        for (Sample s : (Set<Sample>)req.getSamples()) {
          if (s.getCcNumber() != null) {
            ccNumbers.add(s.getCcNumber());
          } else {
            canRead = false;
            break;
          }
        }

        if (canRead) {
          canRead = canReadAllCCNumbers(ccNumbers);
        }
      } catch(Exception ex) {
        log.error("Error getting BSTX security settings to check ccNumbers", ex);
        canRead = false;
      }
    }

    return canRead;
  }

  private boolean canReadAllCCNumbers(List<String> ccNumbers)  throws NamingException, SQLException {
    canRead = true;
    Map<String, boolean[]> secMap = canReadCCNumbers(ccNumbers);
    for(String key : ccNumbers) {
      boolean[] perms = secMap.get(key);
      if (perms == null || !perms[0] || !perms[1] || !perms[2]) {
        canRead = false;
        break;
      }
    }
    return canRead;
  }

  // Used to scrub list of requests being shown to user.  CanRead makes sure even if this scrubbing is not done they don't see details.
  public Map<Integer, Integer> getBSTXSecurityIdsToExclude(Session sess, DictionaryHelper dh, List rows, Integer idRequestIdx, Integer codeRequestCategoryIdx) throws NamingException, SQLException {
    Map<Integer, Integer> idsToSkip = new HashMap<Integer, Integer>();
    ArrayList<Integer> reqIds = new ArrayList<Integer>();
    for(Object[] row : (List<Object[]>)rows) {
      Integer idRequest               = (Integer)row[idRequestIdx];
      String codeRequestCategory      = (String)row[codeRequestCategoryIdx];
      if (idRequest != null && codeRequestCategory != null) {
        RequestCategory requestCategory = dh.getRequestCategoryObject(codeRequestCategory);
        if (requestCategory.getIsClinicalResearch() != null && requestCategory.getIsClinicalResearch().equals("Y")) {
          if (canAccessBSTX) {
            reqIds.add(idRequest);
          } else {
            idsToSkip.put(idRequest, idRequest);
          }
        }
      }
    }

    if (canAccessBSTX && reqIds.size() > 0) {
      String queryString = "select distinct idRequest, case when ccNumber is null then '@@NULL@@' else ccNumber end from Sample where idRequest in (:reqIds)";
      Query query = sess.createQuery(queryString);
      query.setParameterList("reqIds", reqIds);
      List ccRows = query.list();
      Map<String, Integer> ccMap = new HashMap<String, Integer>();
      List<String> ccList = new ArrayList<String>();
      for(Object[] ccRow : (List<Object[]>)ccRows) {
        Integer idRequest = (Integer)ccRow[0];
        String ccNumber = (String)ccRow[1];
        ccMap.put(ccNumber, idRequest);
        ccList.add(ccNumber);
      }

      Map<String, boolean[]> secMap = this.canReadCCNumbers(ccList);

      for(String ccNumber : ccList) {
        boolean[] perms = secMap.get(ccNumber);
        boolean hasPermission = false;
        // perms of null means ccNumber doesn't exist in BST
        // In that case we show experiment to super users but no one else
        if (perms == null) {
          if (hasPermission(this.CAN_ADMINISTER_ALL_CORE_FACILITIES)) {
            hasPermission = true;
          }
        } else if (perms[0] && perms[1] && perms[2]) {
          hasPermission = true;
        }

        if (!hasPermission) {
          Integer idRequest = ccMap.get(ccNumber);
          idsToSkip.put(idRequest, idRequest);
        }
      }
    }    
    return idsToSkip;
  }

  public boolean canUpdate(DetailObject object) throws UnknownPermissionException {
    boolean canUpdate = false;

    //
    // Request
    //
    if (object instanceof Request) {
      Request req = (Request)object;

      // Super Admins
      if (hasPermission(SecurityAdvisor.CAN_ADMINISTER_ALL_CORE_FACILITIES)) {
        canUpdate = true;
      }
      // Admins - Can only update requests from core facility user manages
      else if (hasPermission(SecurityAdvisor.CAN_WRITE_ANY_OBJECT)) {
        canUpdate = isCoreFacilityIManage(req.getIdCoreFacility());
      }
      // University GNomEx users
      else if (hasPermission(SecurityAdvisor.CAN_PARTICIPATE_IN_GROUPS)) {
        // Lab manager
        if (isGroupIManage(req.getIdLab())) {
          canUpdate = true;
        }
        // Owner of request
        else if (isGroupIAmMemberOf(req.getIdLab()) && isOwner(req.getIdAppUser())) {
          canUpdate = true;
          // Collaborator with update
        } else if (this.isCollaboratorUpdater(req)) {
          canUpdate = true;
        } //Is this a request that was submitted by user who can submit on behalf of other cores
        else if(hasPermission(SecurityAdvisor.CAN_SUBMIT_FOR_OTHER_CORES) && this.isLabICanSubmitTo(req.getLab()) && this.appUser.getIdAppUser().equals(req.getIdSubmitter())) {
          canUpdate = true;
        }
      }
      else if(hasPermission(SecurityAdvisor.CAN_SUBMIT_FOR_OTHER_CORES) && this.isLabICanSubmitTo(req.getLab()) && this.appUser.getIdAppUser().equals(req.getIdSubmitter())) {
        canUpdate = true;
      }
    }
    //
    // Analysis
    //
    else if (object instanceof Analysis) {

      // Admins
      if (hasPermission(this.CAN_WRITE_ANY_OBJECT)) {
        canUpdate = true;
      }
      // Univerity GNomEx users
      else if (hasPermission(this.CAN_PARTICIPATE_IN_GROUPS)) {
        Analysis a = (Analysis)object;

        // Lab manager
        if (isGroupIManage(a.getIdLab())) {
          canUpdate = true;
        }
        //  Owner of analysis
        else if (isGroupIAmMemberOf(a.getIdLab()) && isOwner(a.getIdAppUser())) {
          canUpdate = true;
          // Collaborator with update
        }  else if (this.isCollaboratorUpdater(a)) {
          canUpdate = true;
        }

      } 
    }   
    //
    // DataTrack
    //
    else if (object instanceof DataTrack) {

      // Admins
      if (hasPermission(this.CAN_WRITE_ANY_OBJECT)) {
        canUpdate = true;
      }
      // Univerity GNomEx users
      else if (hasPermission(this.CAN_PARTICIPATE_IN_GROUPS)) {
        DataTrack dt = (DataTrack)object;

        // Lab manager
        if (isGroupIManage(dt.getIdLab())) {
          canUpdate = true;
        }
        //  Owner of analysis
        else if (isGroupIAmMemberOf(dt.getIdLab()) && isOwner(dt.getIdAppUser())) {
          canUpdate = true;
        } 

      } 
    }      
    //
    // Project
    //
    else if (object instanceof Project) {

      // Admins
      if (hasPermission(this.CAN_WRITE_ANY_OBJECT)) {
        canUpdate = true;
      }
      // Submitters need to create projects
      else if (hasPermission(this.CAN_SUBMIT_FOR_OTHER_CORES)) {
        canUpdate = true;
      }
      // University GNomEx users
      else if (hasPermission(this.CAN_PARTICIPATE_IN_GROUPS)) {
        Project proj = (Project)object;
        // Lab members or managers
        if (isGroupIAmMemberOf(proj.getIdLab()) || isGroupIManage(proj.getIdLab())) {
          canUpdate = true;
        } 
      }  
    } 
    //
    // AnalysisGroup
    //
    else if (object instanceof AnalysisGroup) {

      // Admins
      if (hasPermission(this.CAN_WRITE_ANY_OBJECT)) {
        canUpdate = true;
      }
      // University GNomEx users
      else if (hasPermission(this.CAN_PARTICIPATE_IN_GROUPS)) {
        AnalysisGroup ag = (AnalysisGroup)object;
        // Lab members or managers
        if (isGroupIAmMemberOf(ag.getIdLab()) || isGroupIManage(ag.getIdLab())) {
          canUpdate = true;
        } 
        //  Owner of analysis group
        else if (isGroupIAmMemberOf(ag.getIdLab()) && isOwner(ag.getIdAppUser())) {
          canUpdate = true;
        } 
      }  
    }
    //
    // DataTrackFolder
    //
    else if (object instanceof DataTrackFolder) {
      // Admins
      if (hasPermission(this.CAN_WRITE_ANY_OBJECT)) {
        canUpdate = true;
      }
      // University GNomEx users
      else if (hasPermission(this.CAN_PARTICIPATE_IN_GROUPS)) {
        DataTrackFolder dtf = (DataTrackFolder)object;
        // Lab members or managers
        if (isGroupIAmMemberOf(dtf.getIdLab()) || isGroupIManage(dtf.getIdLab())) {
          canUpdate = true;
        } 
      }  
    }
    //
    // FlowCell
    //
    else if (object instanceof FlowCell) {

      // Admins
      if (hasPermission(this.CAN_WRITE_ANY_OBJECT)) {
        canUpdate = true;
      }
    }
    //
    // Dictionary
    //
    else if (object instanceof DictionaryEntry) {
      if (hasPermission(this.CAN_WRITE_DICTIONARIES)) {
        // Admins can write every dictionary except for coreFacility,PropertyDictionary, AppUserLite and null entries.
        if ((object instanceof AppUserLite) ||
            (object instanceof NullDictionaryEntry)) {
        } else if (object instanceof PropertyDictionary || object instanceof CoreFacility) {
          canUpdate = hasPermission(this.CAN_WRITE_PROPERTY_DICTIONARY);
        } else {
          canUpdate = true;
        }
      } else if (object instanceof DictionaryEntryUserOwned) {
        // Normal users can only write dictionary entries they own
        DictionaryEntryUserOwned de = (DictionaryEntryUserOwned)object;
        if (de.getIdAppUser() != null && !this.isGuest() &&
            de.getIdAppUser().equals(this.getIdAppUser())) {
          canUpdate = true;
        }
        if(!canUpdate && object instanceof Property) {
          if(de.getIdAppUser() == null && isLabManager()) {
            canUpdate = true;
          }
        }
      }
    }
    //
    // Topic
    //
    else if (object instanceof Topic) {

      // Admins
      if (hasPermission(this.CAN_WRITE_ANY_OBJECT)) {
        canUpdate = true;
      }
      // Univerity GNomEx users
      else if (hasPermission(this.CAN_PARTICIPATE_IN_GROUPS)) {
        Topic t = (Topic)object;

        // Lab manager
        if (isGroupIManage(t.getIdLab())) {
          canUpdate = true;
        }
        //  Owner of topic
        else if (isGroupIAmMemberOf(t.getIdLab()) && isOwner(t.getIdAppUser())) {
          canUpdate = true;
        } 

      } 
    }

    //
    // News Item
    // 
    else if (object instanceof NewsItem) {
      // Admins
      if (hasPermission(this.CAN_WRITE_ANY_OBJECT)) {
        canUpdate = true;
      }
    }

    //
    // FAQ
    //
    else if(object instanceof FAQ){
      // Admins
      if(hasPermission(this.CAN_WRITE_ANY_OBJECT)){
        canUpdate = true;
      }
    }

    return canUpdate;
  }

  public boolean canUpdate(Class theClass) throws UnknownPermissionException {
    boolean canUpdate = false;
    if (DictionaryEntry.class.isAssignableFrom(theClass)) {
      if (hasPermission(this.CAN_WRITE_DICTIONARIES)) {
        canUpdate = true;
      } else if (!this.isGuest() && DictionaryEntryUserOwned.class.isAssignableFrom(theClass)) {
        canUpdate = true;
      } else if (!this.isGuest() && AppUserLite.class.isAssignableFrom(theClass)) {
        canUpdate = true;
      }      
    } else {
      throw new UnknownPermissionException("Unimplemented method");
    }
    return canUpdate;
  }

  public boolean canUpdate(DetailObject object, int dataProfile) throws UnknownPermissionException {
    boolean canUpdate = false;
    if (dataProfile == PROFILE_OBJECT_VISIBILITY) {
      //
      // Request
      //
      if (object instanceof Request) {
        Request req = (Request)object;

        // Super Admins
        if (hasPermission(SecurityAdvisor.CAN_ADMINISTER_ALL_CORE_FACILITIES)) {
          canUpdate = true;
        }
        // Admins - Can only update requests from core facility user manages
        else if (hasPermission(SecurityAdvisor.CAN_WRITE_ANY_OBJECT)) {
          canUpdate = isCoreFacilityIManage(req.getIdCoreFacility());
        }
        // University GNomEx users
        else if (hasPermission(SecurityAdvisor.CAN_PARTICIPATE_IN_GROUPS)) {
          // Lab manager
          if (isGroupIManage(req.getIdLab())) {
            canUpdate = true;
          } 
          // Owner of request
          else if (isGroupIAmMemberOf(req.getIdLab()) && isOwner(req.getIdAppUser())) {
            canUpdate = true;
          } else if(hasPermission(SecurityAdvisor.CAN_SUBMIT_FOR_OTHER_CORES) && this.isLabICanSubmitTo(req.getLab()) && this.appUser.getIdAppUser().equals(req.getIdSubmitter())) {
            canUpdate = true;
          } 
        } else if(hasPermission(SecurityAdvisor.CAN_SUBMIT_FOR_OTHER_CORES) && this.isLabICanSubmitTo(req.getLab()) && this.appUser.getIdAppUser().equals(req.getIdSubmitter())) {
          canUpdate = true;
        }
      } 
      //
      // Analysis
      //
      else if (object instanceof Analysis) {          
        // Admins
        if (hasPermission(this.CAN_WRITE_ANY_OBJECT)) {
          canUpdate = true;
        }
        // University GNomEx users
        else if (hasPermission(this.CAN_PARTICIPATE_IN_GROUPS)) {
          Analysis analysis = (Analysis)object;
          // Lab manager
          if (isGroupIManage(analysis.getIdLab())) {
            canUpdate = true;
          } 
          // Owner of analysis
          else if (isGroupIAmMemberOf(analysis.getIdLab()) && isOwner(analysis.getIdAppUser())) {
            canUpdate = true;
          } 
        } 
      }
      else {
        throw new UnknownPermissionException("Unknown object for data profile PROFILE_OBJECT_VISIBILITY");
      }
    } else if (dataProfile == PROFILE_GROUP_MEMBERSHIP) {
      //
      // Lab
      //
      if (object instanceof Lab) {

        // Admins
        if (hasPermission(this.CAN_ADMINISTER_USERS)) {
          canUpdate = true;
        }
        // University GNomEx users
        else if (hasPermission(this.CAN_PARTICIPATE_IN_GROUPS)) {
          Lab lab = (Lab)object;
          // Lab manager
          if (isGroupIManage(lab.getIdLab())) {
            canUpdate = true;
          } //else if(hasPermission(SecurityAdvisor.CAN_SUBMIT_FOR_OTHER_CORES) && this.isLabICanSubmitTo((Lab)object)) {
          //canUpdate = true;
          //} 

        } //else if(hasPermission(SecurityAdvisor.CAN_SUBMIT_FOR_OTHER_CORES) && this.isLabICanSubmitTo((Lab)object)) {
        //canUpdate = true;
        //} 
      } else {
        throw new UnknownPermissionException("Unknown object for data profile PROFILE_GROUP_MEMBERSHIP");
      }
    } else if (dataProfile == SAMPLES_UPDATE) {
      if (object instanceof Request) {
        Request req = (Request)object;

        if (hasPermission(this.CAN_ADMINISTER_ALL_CORE_FACILITIES)) {
          canUpdate = canUpdate(req);
        }
        // Admins - Can only update requests from core facility user manages
        else if (hasPermission(this.CAN_WRITE_ANY_OBJECT)) {
          canUpdate = canUpdate(req);
          if (req.isDNASeqExperiment().equals("Y") && !(req.getCodeRequestStatus().equals(RequestStatus.SUBMITTED) || req.getCodeRequestStatus().equals(RequestStatus.NEW)
              || (req.getCodeRequestStatus().equals(RequestStatus.PROCESSING) && !req.onReactionPlate()))) {
            canUpdate = false;
          }
        }
        // University GNomEx users
        else if (hasPermission(this.CAN_PARTICIPATE_IN_GROUPS)) {
          canUpdate = canUpdate(req);
          if (canUpdate && req.isDNASeqExperiment().equals("Y") && !req.getCodeRequestStatus().equals(RequestStatus.NEW)) {
            canUpdate = false;
          }
        } 
      } else {
        throw new UnknownPermissionException("Unknown object for data profile PROFILE_GROUP_MEMBERSHIP");
      }
    }
    else {
      throw new UnknownPermissionException("Unspecified data profile");      
    }
    return canUpdate;
  }

  public boolean canUpdateVisibility(Integer idLab, Integer idAppUser)  {
    boolean canUpdate = false;

    // Admins
    if (hasPermission(this.CAN_WRITE_ANY_OBJECT)) {
      canUpdate = true;
    }
    // University GNomEx users
    else if (hasPermission(this.CAN_PARTICIPATE_IN_GROUPS)) {
      // Lab manager
      if (isGroupIManage(idLab)) {
        canUpdate = true;
      } 
      // Owner of object
      else if (isGroupIAmMemberOf(idLab) && isOwner(idAppUser)) {
        canUpdate = true;
      } 
    } 

    return canUpdate;
  }

  public boolean canUploadData(DetailObject object) throws UnknownPermissionException {
    boolean canUpload = false;

    //
    // Request
    //
    if (object instanceof Request) {

      Request req = (Request)object;

      // Super Admins
      if (hasPermission(this.CAN_ADMINISTER_ALL_CORE_FACILITIES)) {
        canUpload = true;
      }
      // Admins - Can only update requests from core facility user manages
      else if (hasPermission(this.CAN_WRITE_ANY_OBJECT)) {
        canUpload = isCoreFacilityIManage(req.getIdCoreFacility());
      }
      // University GNomEx users
      else if (hasPermission(this.CAN_PARTICIPATE_IN_GROUPS)) {
        // Assumes if canUpdate and isCollaboratorUpdater then CollaboraterUpdater is why they can update.
        canUpload = (canUpdate(req) && !isCollaboratorUpdater(req)) || isCollaboratorUploader(req);
      } 
    }
    //
    // Analysis
    //
    else if (object instanceof Analysis) {
      Analysis analysis = (Analysis)object;
      // Assumes if canUpdate and isCollaboratorUpdater then CollaboraterUpdater is why they can update.
      canUpload = (canUpdate(analysis) && !isCollaboratorUpdater(analysis)) || isCollaboratorUploader(analysis);
    }   
    return canUpload;
  }




  public boolean canDelete(DetailObject object) throws UnknownPermissionException {
    boolean canDelete = false;

    // 
    // Request
    //
    if (object instanceof Request) { 
      Request r = (Request)object;

      // Super Admin can delete any request
      if (hasPermission(this.CAN_ADMINISTER_ALL_CORE_FACILITIES)) {
        canDelete = true;
      } 
      // Admin - can delete experiments from core facility user manages
      else if (hasPermission(this.CAN_DELETE_REQUESTS)) {
        canDelete = true;
        if (r.isDNASeqExperiment().equals("Y") && !r.getCodeRequestStatus().equals(RequestStatus.SUBMITTED) && !r.getCodeRequestStatus().equals(RequestStatus.NEW)) {
          canDelete = false;
        }
      } 
      // Lab manager or owner can delete experiment if the samples
      // have not proceeded in the workflow
      else if (isGroupIManage(r.getIdLab()) || (isGroupIAmMemberOf(r.getIdLab()) && isOwner(r.getIdAppUser()))) {
        if (r.isDNASeqExperiment().equals("Y")) {
          canDelete = r.getCodeRequestStatus().equals(RequestStatus.NEW);
        } else {
          int deleteSampleCount = 0;
          for (Iterator i = r.getSamples().iterator(); i.hasNext();) {
            Sample s = (Sample)i.next();
            if (s.getCanChangeSampleInfo().equals("Y")) {
              deleteSampleCount++;
            }
          }
          canDelete = r.getSamples().size() == deleteSampleCount;
        }
      } else if(hasPermission(SecurityAdvisor.CAN_SUBMIT_FOR_OTHER_CORES) && this.isLabICanSubmitTo(r.getLab()) && this.appUser.getIdAppUser().equals(r.getIdSubmitter())) {
        canDelete = true;
      }
      if (canDelete && r.isDNASeqExperiment().equals("Y") && !r.getCodeRequestCategory().equals(RequestCategory.CHERRY_PICKING_REQUEST_CATEGORY)) {
        for (Iterator i = r.getSamples().iterator(); i.hasNext();) {
          Sample s = (Sample)i.next();
          if (s.getWells() != null) {
            for(Iterator i1 = s.getWells().iterator(); i1.hasNext();) {
              PlateWell w = (PlateWell)i1.next();
              if (w.getPlate() != null && w.getPlate().getCodePlateType().equals(PlateType.REACTION_PLATE_TYPE)) {
                canDelete = false;
                break;
              }
            }
          }
        }
      }
    }    
    // 
    // Analysis
    //
    else if (object instanceof Analysis) { 
      Analysis a = (Analysis)object;
      // Admin
      if (hasPermission(this.CAN_DELETE_REQUESTS)) {
        canDelete = true;
      } 
      // Lab manager
      else if (isGroupIManage(a.getIdLab())) {
        canDelete = true;
      }  
      // Analysis owner
      else if (isGroupIAmMemberOf(a.getIdLab()) && isOwner(a.getIdAppUser())) {
        canDelete = true;
      }

    }   
    //
    // Project
    //
    else if (object instanceof Project) {
      Project proj = (Project)object;

      // Admin
      if (hasPermission(this.CAN_DELETE_ANY_PROJECT)) {
        canDelete = true;
      } 
      // Lab manager
      else if (isGroupIManage(proj.getIdLab())) {
        canDelete = true;
      }  
      // Project owner
      else if (isGroupIAmMemberOf(proj.getIdLab()) && isOwner(proj.getIdAppUser())) {
        canDelete = true;
      }
    }
    //
    // AnalysisGroup
    //
    else if (object instanceof AnalysisGroup) {
      AnalysisGroup ag = (AnalysisGroup)object;

      // Admin
      if (hasPermission(this.CAN_DELETE_ANY_PROJECT)) {
        canDelete = true;
      } 
      // Lab manager
      else if (isGroupIManage(ag.getIdLab())) {
        canDelete = true;
      }        
      // Analysis group owner
      else if (isGroupIAmMemberOf(ag.getIdLab()) && isOwner(ag.getIdAppUser())) {
        canDelete = true;
      }
    } 
    //
    // DataTrack
    //
    else if (object instanceof DataTrack) {
      DataTrack dataTrack = (DataTrack)object;
      canDelete = canUpdate(dataTrack);
    } 
    //
    // DataTrackFolder
    //
    else if (object instanceof DataTrackFolder) {
      DataTrackFolder folder = (DataTrackFolder)object;
      canDelete = canUpdate(folder);
    } 
    //
    // Sample Characteristic can be deleted if user owns it
    //
    else if (object instanceof Property) {
      canDelete = canUpdate(object);
    }
    //
    // Dictionary
    //
    else if (object instanceof DictionaryEntry) {
      if (hasPermission(this.CAN_WRITE_DICTIONARIES)) {
        if (object instanceof PropertyDictionary) {
          canDelete = hasPermission(this.CAN_WRITE_PROPERTY_DICTIONARY);
        } else if (object instanceof CoreFacility) {
          canDelete = hasPermission(this.CAN_WRITE_PROPERTY_DICTIONARY);
        } else {
          canDelete = true;
        }
      }   
    }
    //
    // Topic
    //
    else if (object instanceof Topic) {
      Topic t = (Topic)object;

      // Admin
      if (hasPermission(this.CAN_DELETE_ANY_PROJECT)) {
        canDelete = true;
      } 
      // Lab manager
      else if (isGroupIManage(t.getIdLab())) {
        canDelete = true;
      }        
      // Analysis group owner
      else if (isGroupIAmMemberOf(t.getIdLab()) && isOwner(t.getIdAppUser())) {
        canDelete = true;
      }
    }
    //
    // NewsItem
    //
    else if (object instanceof NewsItem){
      if(hasPermission(this.CAN_MANAGE_DASHBOARD)){
        canDelete = true;
      }
    }

    //
    // FAQ
    //
    else if(object instanceof FAQ){
      if(hasPermission(this.CAN_MANAGE_DASHBOARD)){
        canDelete = true;
      }
    }
    return canDelete;
  }

  public Boolean canDeleteSample(Request req) throws UnknownPermissionException {
    if ((req.isDNASeqExperiment() != null && req.isDNASeqExperiment().equals("Y")) || 
        (req.getIsExternal() != null && req.getIsExternal().equals("Y"))) {
      return canDelete(req);
    }
    return hasPermission(CAN_WRITE_ANY_OBJECT);
  }

  public void flagPermissions(DetailObject object) throws UnknownPermissionException {
    object.canRead(this.canRead(object));
    object.canUpdate(this.canUpdate(object));
    object.canDelete(this.canDelete(object));

    if (object instanceof Request) {
      Request req = (Request)object;
      req.canUpdateVisibility(this.canUpdate(object, this.PROFILE_OBJECT_VISIBILITY));
      req.canUploadData(this.canUploadData(req));
      req.setCanDeleteSample(this.canDeleteSample(req));
      req.setCanUpdateSamples(canUpdate(req, this.SAMPLES_UPDATE));
    } else if (object instanceof Analysis) {
      Analysis a = (Analysis)object;
      a.canUpdateVisibility(this.canUpdate(object, this.PROFILE_OBJECT_VISIBILITY));
      a.canUploadData(this.canUploadData(a));
    } 
  }

  private boolean isCollaboratorUploader(Request req) {
    // First, check to see if the user is a specified as a collaborator
    // on this request.
    boolean canUpload = false;
    for (Iterator i = req.getCollaborators().iterator(); i.hasNext();) {
      ExperimentCollaborator collaborator = (ExperimentCollaborator)i.next();
      if (isLoggedInUser(collaborator.getIdAppUser())) {
        if (collaborator.getCanUploadData() != null && collaborator.getCanUploadData().equals("Y")) {
          canUpload = true;
        }
        break;
      }
    }  
    return canUpload;
  }

  private boolean isCollaboratorUpdater(Request req) {
    // First, check to see if the user is a specified as a collaborator
    // on this request.
    boolean canUpdate = false;
    for (Iterator i = req.getCollaborators().iterator(); i.hasNext();) {
      ExperimentCollaborator collaborator = (ExperimentCollaborator)i.next();
      if (isLoggedInUser(collaborator.getIdAppUser())) {
        if (collaborator.getCanUpdate() != null && collaborator.getCanUpdate().equals("Y")) {
          canUpdate = true;
        }
        break;
      }
    }  
    return canUpdate;
  }

  private boolean isCollaboratorUploader(Analysis analysis) {
    // First, check to see if the user is a specified as a collaborator
    // on this object.
    boolean canUpload = false;
    for (Iterator i = analysis.getCollaborators().iterator(); i.hasNext();) {
      AnalysisCollaborator collaborator = (AnalysisCollaborator)i.next();
      if (isLoggedInUser(collaborator.getIdAppUser())) {
        if (collaborator.getCanUploadData() != null && collaborator.getCanUploadData().equals("Y")) {
          canUpload = true;
        }
        break;
      }
    }  
    return canUpload;
  }

  private boolean isCollaboratorUpdater(Analysis analysis) {
    // First, check to see if the user is a specified as a collaborator
    // on this object.
    boolean canUpdate = false;
    for (Iterator i = analysis.getCollaborators().iterator(); i.hasNext();) {
      AnalysisCollaborator collaborator = (AnalysisCollaborator)i.next();
      if (isLoggedInUser(collaborator.getIdAppUser())) {
        if (collaborator.getCanUpdate() != null && collaborator.getCanUpdate().equals("Y")) {
          canUpdate = true;
        }
        break;
      }
    }  
    return canUpdate;
  }

  public void scrub(DetailObject object) throws UnknownPermissionException {
  }


  public Set getGlobalPermissions() {
    return globalPermissionMap.keySet();
  }

  private boolean isActiveUser() {
    return getAppUser() != null && getAppUser().getIsActive() != null && getAppUser().getIsActive().equalsIgnoreCase("Y");
  }

  private void setGlobalPermissions() {
    globalPermissionMap = new HashMap();


    if (isGuest || appUser.getCodeUserPermissionKind() == null) {
      return;
    }

    if (isActiveUser()) {
      // Can administer all core facilities (Super user)
      if (appUser.getCodeUserPermissionKind().equals(UserPermissionKind.SUPER_ADMIN_PERMISSION_KIND)) {
        globalPermissionMap.put(new Permission(CAN_ADMINISTER_ALL_CORE_FACILITIES), null);
      }

      // Can assign the super user role. (Super user)
      if (appUser.getCodeUserPermissionKind().equals(UserPermissionKind.SUPER_ADMIN_PERMISSION_KIND)) {
        globalPermissionMap.put(new Permission(CAN_ASSIGN_SUPER_ADMIN_ROLE), null);
      }


      // Can manage DNA Seq Core
      if (appUser.getCodeUserPermissionKind().equals(UserPermissionKind.SUPER_ADMIN_PERMISSION_KIND)) {
        globalPermissionMap.put(new Permission(this.CAN_MANAGE_DNA_SEQ_CORE), null);
      } else if (appUser.getCodeUserPermissionKind().equals(UserPermissionKind.ADMIN_PERMISSION_KIND)) {
        if (appUser.getManagingCoreFacilities() != null) {
          for (Iterator i = appUser.getManagingCoreFacilities().iterator(); i.hasNext();) {
            CoreFacility coreFacility = (CoreFacility)i.next();
            if (specifiedIdCoreFacility == null || coreFacility.getIdCoreFacility().equals(specifiedIdCoreFacility)) {
              if (coreFacility.getFacilityName().equals(CoreFacility.CORE_FACILITY_DNA_SEQ)) {
                globalPermissionMap.put(new Permission(this.CAN_MANAGE_DNA_SEQ_CORE), null);
              }
            }
          }
        }
      }


      // Can manage Genomics Core Facility
      if (appUser.getCodeUserPermissionKind().equals(UserPermissionKind.SUPER_ADMIN_PERMISSION_KIND)) {
        globalPermissionMap.put(new Permission(this.CAN_MANAGE_GENOMICS_CORE), null);
      } else if (appUser.getCodeUserPermissionKind().equals(UserPermissionKind.ADMIN_PERMISSION_KIND)) {
        if (appUser.getManagingCoreFacilities() != null) {
          for (Iterator i = appUser.getManagingCoreFacilities().iterator(); i.hasNext();) {
            CoreFacility coreFacility = (CoreFacility)i.next();
            if (specifiedIdCoreFacility == null || coreFacility.getIdCoreFacility().equals(specifiedIdCoreFacility)) {
              if (coreFacility.getFacilityName().equals(CoreFacility.CORE_FACILITY_GENOMICS)) {
                globalPermissionMap.put(new Permission(this.CAN_MANAGE_GENOMICS_CORE), null);
              }
            }
          }
        }
      }

      // Group or Billing Permission Can submit for other cores?
      if (appUser.getCodeUserPermissionKind().equals(UserPermissionKind.BILLING_PERMISSION_KIND) 
          || appUser.getCodeUserPermissionKind().equals(UserPermissionKind.GROUP_PERMISSION_KIND) 
          || appUser.getCodeUserPermissionKind().equals(UserPermissionKind.ADMIN_PERMISSION_KIND) ) {
        for(Iterator coreIt = appUser.getCoreFacilitiesICanSubmitTo().iterator(); coreIt.hasNext();) {
          CoreFacility cf = (CoreFacility)coreIt.next();
          if (coreAllowsGlobalSubmission(cf.getIdCoreFacility())) {
            globalPermissionMap.put(new Permission(this.CAN_SUBMIT_FOR_OTHER_CORES), null);  
            break;
          }
        }
      }

      // Can write common dictionaries
      if (appUser.getCodeUserPermissionKind().equals(UserPermissionKind.ADMIN_PERMISSION_KIND) ||
          appUser.getCodeUserPermissionKind().equals(UserPermissionKind.SUPER_ADMIN_PERMISSION_KIND)) {
        globalPermissionMap.put(new Permission(CAN_WRITE_DICTIONARIES), null);
      }


      // Can write property dictionary
      if (appUser.getCodeUserPermissionKind().equals(UserPermissionKind.SUPER_ADMIN_PERMISSION_KIND)) {
        globalPermissionMap.put(new Permission(CAN_WRITE_PROPERTY_DICTIONARY), null);
      }

      if (appUser.getCodeUserPermissionKind().equals(UserPermissionKind.SUPER_ADMIN_PERMISSION_KIND)) {
        globalPermissionMap.put(new Permission(CAN_MANAGE_WORKFLOW), null);
      } else if (appUser.getCodeUserPermissionKind().equals(UserPermissionKind.ADMIN_PERMISSION_KIND)) {
        globalPermissionMap.put(new Permission(CAN_MANAGE_WORKFLOW), null);
      }

      // Can manage billing
      if (appUser.getCodeUserPermissionKind().equals(UserPermissionKind.ADMIN_PERMISSION_KIND) ||
          appUser.getCodeUserPermissionKind().equals(UserPermissionKind.SUPER_ADMIN_PERMISSION_KIND) ||
          appUser.getCodeUserPermissionKind().equals(UserPermissionKind.BILLING_PERMISSION_KIND)) {
        globalPermissionMap.put(new Permission(CAN_MANAGE_BILLING), null);
      }

      // Can manage dashboard
      if (appUser.getCodeUserPermissionKind().equals(UserPermissionKind.SUPER_ADMIN_PERMISSION_KIND)){
        globalPermissionMap.put(new Permission(CAN_MANAGE_DASHBOARD), null);
      } else if (appUser.getCodeUserPermissionKind().equals(UserPermissionKind.ADMIN_PERMISSION_KIND)) {
        if (hasPermission(this.CAN_MANAGE_GENOMICS_CORE)) {
          globalPermissionMap.put(new Permission(CAN_MANAGE_DASHBOARD), null);  
        }
      }

      // Can receive admin notifications
      if(appUser.getCodeUserPermissionKind().equals(UserPermissionKind.SUPER_ADMIN_PERMISSION_KIND)){
        globalPermissionMap.put(new Permission(CAN_RECEIVE_ADMIN_NOTIFICATION), null);
      } else if (appUser.getCodeUserPermissionKind().equals(UserPermissionKind.ADMIN_PERMISSION_KIND)) {
        if (hasPermission(this.CAN_MANAGE_GENOMICS_CORE) || (hasPermission(this.CAN_MANAGE_DNA_SEQ_CORE))) {
          globalPermissionMap.put(new Permission(CAN_RECEIVE_ADMIN_NOTIFICATION), null);  
        }
      }

      // Can receive billing notifications
      if(appUser.getCodeUserPermissionKind().equals(UserPermissionKind.SUPER_ADMIN_PERMISSION_KIND)){
        globalPermissionMap.put(new Permission(CAN_RECEIVE_BILLING_NOTIFICATION), null);    
      } else if (appUser.getCodeUserPermissionKind().equals(UserPermissionKind.ADMIN_PERMISSION_KIND)) {
        if (hasPermission(this.CAN_MANAGE_GENOMICS_CORE) || (hasPermission(this.CAN_MANAGE_DNA_SEQ_CORE))) {
          globalPermissionMap.put(new Permission(CAN_RECEIVE_BILLING_NOTIFICATION), null);  
        }
      } else if (appUser.getCodeUserPermissionKind().equals(CAN_MANAGE_BILLING)) {
        globalPermissionMap.put(new Permission(CAN_RECEIVE_BILLING_NOTIFICATION), null);
      }

      // Can receive workflow notifications
      if(appUser.getCodeUserPermissionKind().equals(UserPermissionKind.SUPER_ADMIN_PERMISSION_KIND)){
        globalPermissionMap.put(new Permission(CAN_RECEIVE_WORKFLOW_NOTIFICATION), null);
      } else if(appUser.getCodeUserPermissionKind().equals(UserPermissionKind.ADMIN_PERMISSION_KIND)) {
        if (hasPermission(this.CAN_MANAGE_GENOMICS_CORE) || (hasPermission(this.CAN_MANAGE_DNA_SEQ_CORE))) {
          globalPermissionMap.put(new Permission(CAN_RECEIVE_WORKFLOW_NOTIFICATION), null);  
        }
      }      


      // Can administer users
      if (appUser.getCodeUserPermissionKind().equals(UserPermissionKind.ADMIN_PERMISSION_KIND) ||
          appUser.getCodeUserPermissionKind().equals(UserPermissionKind.SUPER_ADMIN_PERMISSION_KIND) ||
          appUser.getCodeUserPermissionKind().equals(UserPermissionKind.BILLING_PERMISSION_KIND)) {
        globalPermissionMap.put(new Permission(CAN_ADMINISTER_USERS), null);
      }

      // Can access any requests
      if (appUser.getCodeUserPermissionKind().equals(UserPermissionKind.ADMIN_PERMISSION_KIND) ||
          appUser.getCodeUserPermissionKind().equals(UserPermissionKind.SUPER_ADMIN_PERMISSION_KIND) ||
          appUser.getCodeUserPermissionKind().equals(UserPermissionKind.BILLING_PERMISSION_KIND)) {
        globalPermissionMap.put(new Permission(CAN_ACCESS_ANY_OBJECT), null);
      }

      // Can write any request
      if (appUser.getCodeUserPermissionKind().equals(UserPermissionKind.ADMIN_PERMISSION_KIND) ||
          appUser.getCodeUserPermissionKind().equals(UserPermissionKind.SUPER_ADMIN_PERMISSION_KIND)) {
        globalPermissionMap.put(new Permission(CAN_WRITE_ANY_OBJECT), null);
      }

      // Can delete requests
      if (appUser.getCodeUserPermissionKind().equals(UserPermissionKind.ADMIN_PERMISSION_KIND) ||
          appUser.getCodeUserPermissionKind().equals(UserPermissionKind.SUPER_ADMIN_PERMISSION_KIND)) {
        globalPermissionMap.put(new Permission(CAN_DELETE_REQUESTS), null);
      }


      //  Can delete any project
      if (appUser.getCodeUserPermissionKind().equals(UserPermissionKind.ADMIN_PERMISSION_KIND) ||
          appUser.getCodeUserPermissionKind().equals(UserPermissionKind.SUPER_ADMIN_PERMISSION_KIND)) {        
        globalPermissionMap.put(new Permission(CAN_DELETE_ANY_PROJECT), null);
      }
    }

    // Can access objects governed by group level permissions
    if (this.isGNomExExternalUser || this.isGNomExUniversityUser) {
      globalPermissionMap.put(new Permission(CAN_PARTICIPATE_IN_GROUPS), null);
    }


    // Can be lab member
    if (this.isGNomExExternalUser || this.isGNomExUniversityUser) {
      globalPermissionMap.put(new Permission(CAN_BE_LAB_MEMBER), null);            
    }


    // Can be lab collaborator
    if (this.isGNomExExternalUser || this.isGNomExUniversityUser) {
      globalPermissionMap.put(new Permission(CAN_BE_LAB_COLLABORATOR), null);      
    }

    // Can submit work authorization forms
    if (!isGuest) {
      globalPermissionMap.put(new Permission(CAN_SUBMIT_WORK_AUTH_FORMS), null);      
    }

    // Can submit requests
    if (isActiveUser()) {
      if (this.getAllMyGroups().size() > 0 || appUser.getCodeUserPermissionKind().equals(UserPermissionKind.ADMIN_PERMISSION_KIND) ||
          appUser.getCodeUserPermissionKind().equals(UserPermissionKind.SUPER_ADMIN_PERMISSION_KIND)) {
        globalPermissionMap.put(new Permission(CAN_SUBMIT_REQUESTS), null);            
      }
    }




  }



  public String getUserFirstName() {
    if (isGuest) {
      return "guest";
    }
    return appUser.getFirstName();
  }


  public String getUserLastName() {
    if (isGuest) {
      return "";
    }
    return appUser.getLastName();
  }


  public String getUserEmail() {
    if (isGuest) {
      return "";
    }
    return appUser.getEmail();
  }


  public Integer getIdAppUser() {
    if (isGuest) {
      return new Integer(-999999);
    } else if (isUniversityOnlyUser) {
      return new Integer(-999999);
    } else {
      return appUser.getIdAppUser();
    }
  }

  public String getConfirmEmailGuid() {
    if (isGuest) {
      return "";
    } else {
      return appUser.getConfirmEmailGuid();
    }
  }

  public String getUID() {
    if (isGuest) {
      return "";
    } else if (this.isGNomExUniversityUser || this.isUniversityOnlyUser) {
      return appUser.getuNID();
    } else {
      return appUser.getUserNameExternal();
    }

  }

  public String getPasswordExpired() {
    if(isGuest || this.isGNomExUniversityUser || this.isUniversityOnlyUser) {
      return "";
    } else {
      return appUser.getPasswordExpired();
    }
  }

  public String getUserUcscUrl() {
    if (isGuest) {
      return Constants.UCSC_URL;
    } else if (this.appUser != null) {
      String url = appUser.getUcscUrl() == null || appUser.getUcscUrl().equals("") ? Constants.UCSC_URL : appUser.getUcscUrl();
      return url;
    } else {
      return Constants.UCSC_URL;
    }
  }


  public Set getAllMyGroups() {
    TreeSet labs = new TreeSet(new LabComparator());

    labs.addAll(getGroupsIAmMemberOf());
    labs.addAll(getGroupsICollaborateWith());
    labs.addAll(getGroupsIManage());

    return labs;
  }

  public Set getGroupsIAmMemberOrManagerOf() {
    TreeSet labs = new TreeSet(new LabComparator());

    labs.addAll(getGroupsIAmMemberOf());
    labs.addAll(getGroupsIManage());

    return labs;
  }

  private Set filterLabSetByCoreFacility(Set<Lab> inLabs) {
    if (specifiedIdCoreFacility == null) {
      return inLabs;
    } else {
      TreeSet outLabs = new TreeSet();
      for (Lab l:inLabs) {
        for(CoreFacility c:(Set<CoreFacility>)l.getCoreFacilities()) {
          if (c.getIdCoreFacility().equals(specifiedIdCoreFacility)) {
            outLabs.add(l);
            break;
          }
        }
      }
      return outLabs;
    }
  }
  public Set getGroupsIAmMemberOf() {
    if (hasPermission(this.CAN_PARTICIPATE_IN_GROUPS)) {
      return filterLabSetByCoreFacility(this.getAppUser().getLabs());
    }
    return new TreeSet();
  }


  public Set getInstitutionsIAmMemberOf() {
    TreeSet institutions = new TreeSet();
    if (hasPermission(this.CAN_PARTICIPATE_IN_GROUPS)) {
      for(Iterator i = this.getGroupsIAmMemberOrManagerOf().iterator(); i.hasNext();) {
        Lab l = (Lab)i.next();
        institutions.addAll(l.getInstitutions());
      }
    } 
    return institutions;
  }

  public Set getGroupsICollaborateWith() {
    if (hasPermission(this.CAN_PARTICIPATE_IN_GROUPS)) {
      return filterLabSetByCoreFacility(this.getAppUser().getCollaboratingLabs());
    }
    return new TreeSet();
  }

  public Set getGroupsIManage() {
    if (hasPermission(this.CAN_PARTICIPATE_IN_GROUPS)) {
      return filterLabSetByCoreFacility(this.getAppUser().getManagingLabs());
    }
    return new TreeSet();
  }

  // For XML
  public List getGroupsToManage() {

    List labs = new ArrayList();
    for(Iterator i = getGroupsIManage().iterator(); i.hasNext();) {
      Lab lab = (Lab)i.next();

      lab.excludeMethodFromXML("getBillingAccounts");
      lab.excludeMethodFromXML("getDepartment");
      lab.excludeMethodFromXML("getNotes");
      lab.excludeMethodFromXML("getContactName");
      lab.excludeMethodFromXML("getContactAddress2");
      lab.excludeMethodFromXML("getContactAddress");
      lab.excludeMethodFromXML("getContactCity");
      lab.excludeMethodFromXML("getContactCodeState");
      lab.excludeMethodFromXML("getContactZip");
      lab.excludeMethodFromXML("getContactCountry");
      lab.excludeMethodFromXML("getContactEmail");
      lab.excludeMethodFromXML("getContactPhone");

      lab.excludeMethodFromXML("getMembers");
      lab.excludeMethodFromXML("getCollaborators");
      lab.excludeMethodFromXML("getManagers");

      lab.excludeMethodFromXML("getBillingAccounts");
      lab.excludeMethodFromXML("getApprovedBillingAccounts");
      lab.excludeMethodFromXML("getPendingBillingAccounts");

      lab.excludeMethodFromXML("getIsMyLab");
      lab.excludeMethodFromXML("getCanSubmitRequests");
      lab.excludeMethodFromXML("getCanManage");
      lab.excludeMethodFromXML("getHasPublicData");

      labs.add(lab);
    }

    return labs;
  }

  public boolean isCoreFacilityIManage(Integer idCoreFacility) {
    boolean isMyCoreFacility = false;
    if (hasPermission(this.CAN_ACCESS_ANY_OBJECT)) {
      for (Iterator i = this.getCoreFacilitiesIManage().iterator(); i.hasNext();) {
        CoreFacility coreFacility = (CoreFacility)i.next();
        if (coreFacility.getIdCoreFacility().equals(idCoreFacility)) {
          isMyCoreFacility = true;
          break;
        }
      }
    }
    return isMyCoreFacility;
  }

  public boolean isCoreFacilityICanSubmitTo(Integer idCoreFacility) {
    boolean isCoreICanSubmitTo = false;
    if (this.coreAllowsGlobalSubmission(idCoreFacility)) {
      for (Iterator i = appUser.getCoreFacilitiesICanSubmitTo().iterator(); i.hasNext();) {
        CoreFacility coreFacility = (CoreFacility)i.next();
        if (coreFacility.getIdCoreFacility().equals(idCoreFacility)) {
          isCoreICanSubmitTo = true;
          break;
        }
      }
    }
    return isCoreICanSubmitTo;
  }

  public boolean isLabICanSubmitTo(Lab l) {
    if(l == null) {
      return true;
    }

    if(isGuest && appUser == null) {
      return false;
    }

    boolean isLabICanSubmitTo = false;
    for (Iterator i = l.getCoreFacilities().iterator(); i.hasNext();) {
      CoreFacility coreFacility = (CoreFacility)i.next();
      if (isCoreFacilityICanSubmitTo(coreFacility.getIdCoreFacility())) {
        isLabICanSubmitTo = true;
        break;
      }
    }
    return isLabICanSubmitTo;
  }

  public boolean isCoreFacilityForMyLab(Integer idCoreFacility) {
    boolean isMyCoreFacility = false;
    for (Iterator i = this.getCoreFacilitiesForMyLab().iterator(); i.hasNext();) {
      CoreFacility coreFacility = (CoreFacility)i.next();
      if (coreFacility.getIdCoreFacility().equals(idCoreFacility)) {
        isMyCoreFacility = true;
        break;
      }
    }
    return isMyCoreFacility;
  }

  public boolean isInstitutionIAmMemberOf(Integer idInstitution) {
    boolean isMyInstitution = false;

    if (hasPermission(this.CAN_ACCESS_ANY_OBJECT)) {
      isMyInstitution = true;
    } else if (hasPermission(this.CAN_PARTICIPATE_IN_GROUPS)) {

      if (idInstitution != null) {
        for(Iterator i = this.getInstitutionsIAmMemberOf().iterator(); i.hasNext();) {
          Institution institution = (Institution)i.next();
          if (institution.getIdInstitution().equals(idInstitution)) {
            isMyInstitution = true;
            break;
          }
        }      
      }
    }

    return isMyInstitution;
  }

  public boolean isGroupIAmMemberOf(Integer idLab) {
    boolean isMyLab = false;

    if (hasPermission(this.CAN_WRITE_ANY_OBJECT)) {
      isMyLab = true;
    } else if (hasPermission(this.CAN_PARTICIPATE_IN_GROUPS)) {

      if (idLab != null) {
        for(Iterator i = filterLabSetByCoreFacility(this.getAppUser().getLabs()).iterator(); i.hasNext();) {
          Lab lab = (Lab)i.next();
          if (lab.getIdLab().equals(idLab)) {
            isMyLab = true;
            break;
          }
        }      
      }
    }

    return isMyLab;
  }


  public boolean isGroupIAmMemberOrManagerOf(Integer idLab) {
    return isGroupIAmMemberOf(idLab) || isGroupIManage(idLab);

  }

  public boolean isGroupICollaborateWith(Integer idLab) {
    boolean isMyLab = false;

    if (hasPermission(this.CAN_ACCESS_ANY_OBJECT)) {
      isMyLab = true;
    } else if (hasPermission(this.CAN_PARTICIPATE_IN_GROUPS)) {

      if (idLab != null) {
        for(Iterator i = filterLabSetByCoreFacility(this.getAppUser().getCollaboratingLabs()).iterator(); i.hasNext();) {
          Lab lab = (Lab)i.next();
          if (lab.getIdLab().equals(idLab)) {
            isMyLab = true;
            break;
          }
        }      
      }
    }

    return isMyLab;
  }

  public boolean isGroupIdManager(Lab theLab) {
    return isGroupIManage(theLab.getIdLab());
  }

  public boolean isGroupIManage(Integer idLab) {
    boolean isMyLab = false;

    if (hasPermission(this.CAN_WRITE_ANY_OBJECT)) {
      isMyLab = true;
    } else if (hasPermission(this.CAN_PARTICIPATE_IN_GROUPS)) {
      for(Iterator i = filterLabSetByCoreFacility(this.getAppUser().getManagingLabs()).iterator(); i.hasNext();) {
        Lab lab = (Lab)i.next();
        if (lab.getIdLab().equals(idLab)) {
          isMyLab = true;
          break;
        }
      }      

    }

    return isMyLab;
  }

  public boolean isOwner(Integer idAppUserOfObject) {
    if (hasPermission(this.CAN_WRITE_ANY_OBJECT)) {
      return true;
    } else if (isGuest) {
      return false;
    } else if (this.isLoggedInUser(idAppUserOfObject)) {
      return true;
    } else {
      return false;
    }
  }


  private boolean isLoggedInUser(Integer idAppUserOfObject) {
    if (isGuest) {
      return false;
    } else if (this.getIdAppUser().equals(idAppUserOfObject)) {
      return true;
    } else {
      return false;
    }
  }



  private void validate() throws InvalidSecurityAdvisorException {

    // user is required
    if (!isGuest && getAppUser() == null) {
      throw new InvalidSecurityAdvisorException("User is required for SecurityAdvisor");
    }
    // user permission is required
    if (!isGuest && getAppUser().getCodeUserPermissionKind() == null) {
      throw new InvalidSecurityAdvisorException("UserPermissionKind required for SecurityAdvisor");
    }
    if (this.hasPermission(SecurityAdvisor.CAN_ADMINISTER_ALL_CORE_FACILITIES)) {
      // No criteria needed if this is a super user
    } else if (this.hasPermission(SecurityAdvisor.CAN_ACCESS_ANY_OBJECT)) {

      // Filter to show only labs associated with core facilities this admin manages
      if (this.getCoreFacilitiesIManage().isEmpty()) {
        if (specifiedIdCoreFacility == null) {
          throw new InvalidSecurityAdvisorException("Admin is not assigned to any core facilities.");
        } else {
          throw new InvalidSecurityAdvisorException("Admin is not assigned to core facility specified on login.");
        }
      }
    } 
  }

  public void registerMethodsToExcludeFromXML() {
    this.excludeMethodFromXML("getCanRead");
    this.excludeMethodFromXML("getCanUpdate");
    this.excludeMethodFromXML("getCanDelete");
    this.excludeMethodFromXML("getAppUser");
    this.excludeMethodFromXML("getAllMyGroups");
    this.excludeMethodFromXML("getGroupsIManage");
    this.excludeMethodFromXML("getGroupsIAmMemberOf");
    this.excludeMethodFromXML("getGroupsICollaborateWith");
    this.excludeMethodFromXML("getGroupsIAmMemberOrManagerOf");
    this.excludeMethodFromXML("getInstitutionsIAmMemberOf");
  }


  public AppUser getAppUser() {
    if (isGuest) {
      return null;
    }
    return appUser;
  }


  public void setAppUser(AppUser appUser) {
    this.appUser = appUser;
  }

  public boolean buildSpannedSecurityCriteria(StringBuffer queryBuf, String inheritedClassShortName, String classShortName, String collabClassShortName, boolean isFirstCriteria, String visibilityField, boolean scopeToGroup, String leftJoinExclusionCriteria, String labCoreFacilitiesName, Boolean hasCoreFacility) {
    if (hasPermission(SecurityAdvisor.CAN_ADMINISTER_ALL_CORE_FACILITIES)) {
      // GNomex is not restricted
    } else if (hasPermission(SecurityAdvisor.CAN_ACCESS_ANY_OBJECT)) {

      // GNomex admin is restricted to objects for their core facility.
      // Same restriction applies for CAN_SUBMIT_FOR_OTHER_CORES).
      queryBuf.append(isFirstCriteria ? "WHERE " : " AND ");
      isFirstCriteria = false;
      queryBuf.append(" ( ");

      // Pick up "empty" projects or analysis groups that don't have any children but
      // belong to same lab user is member or manager of.
      if (leftJoinExclusionCriteria != null && labCoreFacilitiesName != null) {
        queryBuf.append(" ( ");
        appendCoreFacilityCriteria(queryBuf, labCoreFacilitiesName);
        queryBuf.append(" AND ");
        queryBuf.append(" " + leftJoinExclusionCriteria + " is NULL ");
        queryBuf.append(" ) ");
        queryBuf.append(" OR ");
      } 

      appendCoreFacilityCriteria(queryBuf, classShortName);
      if(hasPermission(this.CAN_SUBMIT_FOR_OTHER_CORES)) {
        queryBuf.append(" OR ");
        appendSubmitterCriteria(queryBuf, classShortName);
      }

      queryBuf.append(" ) ");

    } else if (hasPermission(SecurityAdvisor.CAN_PARTICIPATE_IN_GROUPS)) {
      queryBuf.append(isFirstCriteria ? "WHERE " : " AND ");
      isFirstCriteria = false;
      queryBuf.append(" ( ");

      // Add criteria for collaborator list
      boolean criteriaAdded = appendSecurityCollaboratorListCriteria(queryBuf, collabClassShortName);

      // Add criteria with owner visibility
      queryBuf.append( !criteriaAdded ? "WHERE " : " OR ");
      criteriaAdded = appendOwnerCriteria(queryBuf, classShortName);

      if(hasPermission(this.CAN_SUBMIT_FOR_OTHER_CORES)) {
        queryBuf.append( !criteriaAdded ? "WHERE " : " OR ");
        criteriaAdded = appendSubmitterCriteria(queryBuf, classShortName);
      }

      // Add criteria for objects with members visibility
      if (getGroupsIAmMemberOrManagerOf().size() > 0) {
        queryBuf.append( !criteriaAdded ? "WHERE " : " OR ");
        criteriaAdded = appendMembershipCriteria(queryBuf, classShortName);
      }

      // Add criteria for objects with collaborator visibility
      if (getAllMyGroups().size() > 0) {
        queryBuf.append( !criteriaAdded ? "WHERE " : " OR ");
        criteriaAdded = appendMembersAndCollaboratorsCriteria(queryBuf, classShortName);
      }

      // Add criteria for objects with institution visibility
      if (getInstitutionsIAmMemberOf().size() > 0) {        
        queryBuf.append( !criteriaAdded ? "WHERE " : " OR ");
        criteriaAdded = appendInstitutionCriteria(queryBuf, classShortName);        
      }

      // Add criteria for public objects
      queryBuf.append( !criteriaAdded ? "WHERE " : " OR ");
      criteriaAdded = appendPublicCriteria(queryBuf, classShortName, scopeToGroup);

      // Pick up "empty" projects or analysis groups that don't have any children but
      // belong to same lab user is member or manager of.
      if (leftJoinExclusionCriteria != null && this.getGroupsIAmMemberOrManagerOf().size() > 0) {
        queryBuf.append( !criteriaAdded ? "WHERE " : " OR ");
        queryBuf.append(" ( ");
        criteriaAdded = this.appendGroupCriteria(queryBuf, inheritedClassShortName);
        queryBuf.append(" AND ");
        queryBuf.append(" " + leftJoinExclusionCriteria + " is NULL ");
        queryBuf.append(" ) ");
      } 

      // Pick up "empty" projects or analysis groups that don't have any children but
      // belong to same core user can submit to.
      if (leftJoinExclusionCriteria != null && this.getCoreFacilitiesICanSubmitTo().size() > 0 && labCoreFacilitiesName != null) {
        queryBuf.append( !criteriaAdded ? "WHERE " : " OR ");
        queryBuf.append(" ( ");
        criteriaAdded = this.appendSubmitterCoreFacilityCriteria(queryBuf, labCoreFacilitiesName);
        queryBuf.append(" AND ");
        queryBuf.append(" " + leftJoinExclusionCriteria + " is NULL ");
        queryBuf.append(" ) ");
      } 


      queryBuf.append(" ) ");

      if (hasCoreFacility) {
        this.appendSpecifiedCoreFacilityCriteria(queryBuf, classShortName, isFirstCriteria);
      }
    } else {
      // Guest or University only user can access public objects
      queryBuf.append(isFirstCriteria ? "WHERE " : " AND ");
      isFirstCriteria = false;
      queryBuf.append(" ( ");

      appendPublicCriteria(queryBuf, classShortName, false);        

      queryBuf.append(" ) ");

      if (hasCoreFacility) {
        this.appendSpecifiedCoreFacilityCriteria(queryBuf, classShortName, isFirstCriteria);
      }
    }
    return isFirstCriteria;
  }
  public boolean buildSecurityCriteria(StringBuffer queryBuf, String classShortName, String collabClassShortName, boolean isFirstCriteria, boolean scopeToGroup, boolean hasCoreFacility) {
    return buildSecurityCriteria(queryBuf, classShortName, collabClassShortName, isFirstCriteria, scopeToGroup, hasCoreFacility, true);
  }

  public boolean buildSecurityCriteria(StringBuffer queryBuf, String classShortName, String collabClassShortName, boolean isFirstCriteria, boolean scopeToGroup, boolean hasCoreFacility, boolean checkSubmitter) {
    if (hasPermission(SecurityAdvisor.CAN_ADMINISTER_ALL_CORE_FACILITIES)) {

      // GNomex super admin is not restricted (much)

    } else if (hasPermission(SecurityAdvisor.CAN_ACCESS_ANY_OBJECT)) {
      if (hasCoreFacility) {
        // GNomex admin is restricted to objects for their core facility
        queryBuf.append(isFirstCriteria ? "WHERE " : " AND ");
        isFirstCriteria = false;
        appendCoreFacilityCriteria(queryBuf, classShortName);
      }

    } else if (hasPermission(SecurityAdvisor.CAN_PARTICIPATE_IN_GROUPS)) {

      // GNomex user can access public objects, objects he is listed as
      // a collaborator or objects matching visibility
      queryBuf.append(isFirstCriteria ? "WHERE " : " AND ");

      isFirstCriteria = false;
      queryBuf.append(" ( ");

      // Add criteria for collaborator list
      boolean criteriaAdded = false;
      if(collabClassShortName != null && collabClassShortName.length() > 0) {
        criteriaAdded = appendSecurityCollaboratorListCriteria(queryBuf, collabClassShortName);
        queryBuf.append( !criteriaAdded ? "WHERE " : " OR ");
      }


      // Add criteria with owner visibility
      criteriaAdded = appendOwnerCriteria(queryBuf, classShortName);

      // Add criteria for submitters
      if(hasPermission(this.CAN_SUBMIT_FOR_OTHER_CORES) && checkSubmitter) {
        queryBuf.append( !criteriaAdded ? "WHERE " : " OR ");
        criteriaAdded = appendSubmitterCriteria(queryBuf, classShortName);
      }

      // Add criteria for objects with members visibility
      if (getGroupsIAmMemberOrManagerOf().size() > 0) {
        queryBuf.append( !criteriaAdded ? "WHERE " : " OR ");
        criteriaAdded = appendMembershipCriteria(queryBuf, classShortName);
      }

      // Add criteria for objects with collaborator visibility
      if (getAllMyGroups().size() > 0) {
        queryBuf.append( !criteriaAdded ? "WHERE " : " OR ");
        criteriaAdded = appendMembersAndCollaboratorsCriteria(queryBuf, classShortName);
      }

      // Add criteria for objects with institution visibility
      if (getInstitutionsIAmMemberOf().size() > 0) {        
        queryBuf.append( !criteriaAdded ? "WHERE " : " OR ");
        criteriaAdded = appendInstitutionCriteria(queryBuf, classShortName);        
      }

      // Add criteria for public objects
      queryBuf.append( !criteriaAdded ? "WHERE " : " OR ");
      criteriaAdded = appendPublicCriteria(queryBuf, classShortName, scopeToGroup);


      queryBuf.append(" ) ");

      if (hasCoreFacility) {
        this.appendSpecifiedCoreFacilityCriteria(queryBuf, classShortName, isFirstCriteria);
      }
    } else if(hasPermission(SecurityAdvisor.CAN_SUBMIT_FOR_OTHER_CORES) && checkSubmitter){
      queryBuf.append( !isFirstCriteria ? "WHERE " : " AND ");
      appendSubmitterCriteria(queryBuf, classShortName); 
    } else {

      // Guest or University only user cab access public objects
      queryBuf.append(isFirstCriteria ? "WHERE " : " AND ");
      isFirstCriteria = false;
      queryBuf.append(" ( ");

      appendPublicCriteria(queryBuf, classShortName, false);        

      queryBuf.append(" ) ");

      if (hasCoreFacility) {
        this.appendSpecifiedCoreFacilityCriteria(queryBuf, classShortName, isFirstCriteria);
      }
    }

    return isFirstCriteria;
  }

  private boolean appendSecurityCollaboratorListCriteria(StringBuffer queryBuf, String collabClassShortName ) {
    // Admins
    if (hasPermission(CAN_ACCESS_ANY_OBJECT)) {
      return false;
    }
    // GNomEx users
    else if (hasPermission(this.CAN_PARTICIPATE_IN_GROUPS)) {

      queryBuf.append(" ( ");

      // collaborator.idAppUser = logged in user
      queryBuf.append(collabClassShortName);
      queryBuf.append(".idAppUser = ");
      queryBuf.append(this.getIdAppUser());

      queryBuf.append(" ) ");
      return true;
    } else {
      return false;
    }
  }




  public boolean addPublicOnlySecurityCriteria(StringBuffer queryBuf, String classShortName, boolean addWhereOrAnd, boolean hasCoreFacility) {

    addWhereOrAnd = this.addWhereOrAnd(queryBuf, addWhereOrAnd);
    queryBuf.append("(");

    appendPublicCriteria(queryBuf, classShortName, false);

    // Now exclude this users's groups
    Set labs = getAllMyGroups();
    if (!labs.isEmpty()) {
      this.addWhereOrAnd(queryBuf, addWhereOrAnd);
      queryBuf.append(classShortName);
      queryBuf.append(".idLab not in ( ");
      for (Iterator i = labs.iterator(); i.hasNext();) {
        Lab theLab = (Lab) i.next();
        queryBuf.append(theLab.getIdLab());
        if (i.hasNext()) {
          queryBuf.append(", ");
        }
      }
      queryBuf.append(" )");
    }


    queryBuf.append(")");

    if (hasCoreFacility) {
      this.appendSpecifiedCoreFacilityCriteria(queryBuf, classShortName, false);
    }
    return addWhereOrAnd;
  }

  private boolean appendSubmitterCriteria(StringBuffer queryBuf, String classShortName) {
    queryBuf.append(" ( ");

    queryBuf.append(classShortName);
    queryBuf.append(".idSubmitter = ");
    queryBuf.append(this.getIdAppUser());

    queryBuf.append(" ) ");
    return true;
  }

  private boolean appendOwnerCriteria(StringBuffer queryBuf, String classShortName ) {
    Set mgrLabs = getGroupsIManage();


    queryBuf.append(" ( ");


    queryBuf.append(" ( ");
    // req.idAppUser
    queryBuf.append(classShortName);
    queryBuf.append(".idAppUser = ");
    queryBuf.append(this.getIdAppUser());

    if (mgrLabs.size() > 0) {
      queryBuf.append(" OR ");

      // req.idLab in labs I manage(....)
      queryBuf.append(classShortName);
      queryBuf.append(".idLab in ( ");
      for(Iterator i = mgrLabs.iterator(); i.hasNext();) {
        Lab theLab = (Lab)i.next();
        queryBuf.append(theLab.getIdLab());
        if (i.hasNext()) {
          queryBuf.append(", ");
        }
      }      
      queryBuf.append(" )");      
    }

    queryBuf.append(" ) ");




    // req.codeVisibility = 'visible to members'
    queryBuf.append(" AND ");
    queryBuf.append(classShortName);
    queryBuf.append(".codeVisibility = '");
    queryBuf.append(Visibility.VISIBLE_TO_OWNER);
    queryBuf.append("'");

    queryBuf.append(" ) ");
    return true;
  }

  private boolean appendMembershipCriteria(StringBuffer queryBuf, String classShortName ) {
    Set labs = getGroupsIAmMemberOrManagerOf();
    if (labs.isEmpty()) {
      return false;
    }

    queryBuf.append(" ( ");

    // req.idLab in (....)
    queryBuf.append(classShortName);
    queryBuf.append(".idLab in ( ");
    for(Iterator i = labs.iterator(); i.hasNext();) {
      Lab theLab = (Lab)i.next();
      queryBuf.append(theLab.getIdLab());
      if (i.hasNext()) {
        queryBuf.append(", ");
      }
    }      
    queryBuf.append(" )");

    // req.codeVisibility = 'visible to members'
    queryBuf.append(" AND ");
    queryBuf.append(classShortName);
    queryBuf.append(".codeVisibility = '");
    queryBuf.append(Visibility.VISIBLE_TO_GROUP_MEMBERS);
    queryBuf.append("'");

    queryBuf.append(" ) ");
    return true;
  }


  public boolean appendCoreFacilityCriteria(StringBuffer queryBuf, String classShortName ) {
    if (this.getCoreFacilitiesIManage().isEmpty() && this.getSpecifiedIdCoreFacility() == null) {
      throw new RuntimeException("Unable to filter admin by core facilties -- no core facilities have been assiged to this user");
    }

    queryBuf.append(" ( ");

    // req.idCoreFacility in (....)
    queryBuf.append(classShortName);
    queryBuf.append(".idCoreFacility in ( ");
    for(Iterator i = this.getCoreFacilitiesIManage().iterator(); i.hasNext();) {
      CoreFacility coreFacility = (CoreFacility)i.next();
      queryBuf.append(coreFacility.getIdCoreFacility());
      if (i.hasNext()) {
        queryBuf.append(", ");
      }
    }      
    queryBuf.append(" )");

    queryBuf.append(" )");

    return true;
  }

  private boolean appendSubmitterCoreFacilityCriteria(StringBuffer queryBuf, String classShortName ) {
    if (this.getCoreFacilitiesICanSubmitTo().isEmpty()) {
      throw new RuntimeException("Unable to filter submitter by core facilties -- no core facilities have been assiged to this user");
    }

    queryBuf.append(" ( ");

    // req.idCoreFacility in (....)
    queryBuf.append(classShortName);
    queryBuf.append(".idCoreFacility in ( ");
    for(Iterator i = this.getCoreFacilitiesICanSubmitTo().iterator(); i.hasNext();) {
      CoreFacility coreFacility = (CoreFacility)i.next();
      queryBuf.append(coreFacility.getIdCoreFacility());
      if (i.hasNext()) {
        queryBuf.append(", ");
      }
    }      
    queryBuf.append(" )");

    queryBuf.append(" )");

    return true;
  }

  private boolean appendSpecifiedCoreFacilityCriteria(StringBuffer queryBuf, String classShortName, Boolean isFirstCriteria ) {
    if (this.getSpecifiedIdCoreFacility() != null) {
      queryBuf.append(isFirstCriteria ? " WHERE " : " AND ");
      isFirstCriteria = false;

      // req.idCoreFacility = x
      queryBuf.append(classShortName);
      queryBuf.append(".idCoreFacility = ");
      queryBuf.append(this.getSpecifiedIdCoreFacility());

      return true;
    } else {
      return false;
    }
  }

  private boolean appendInstitutionCriteria(StringBuffer queryBuf, String classShortName ) {
    Set institutions = getInstitutionsIAmMemberOf();
    if (institutions.isEmpty()) {
      return false;
    }

    queryBuf.append(" ( ");

    // req.idLab in (....)
    queryBuf.append(classShortName);
    queryBuf.append(".idInstitution in ( ");
    for(Iterator i = institutions.iterator(); i.hasNext();) {
      Institution theInstitution = (Institution)i.next();
      queryBuf.append(theInstitution.getIdInstitution());
      if (i.hasNext()) {
        queryBuf.append(", ");
      }
    }      
    queryBuf.append(" )");

    // req.codeVisibility = 'visible to members'
    queryBuf.append(" AND ");
    queryBuf.append(classShortName);
    queryBuf.append(".codeVisibility = '");
    queryBuf.append(Visibility.VISIBLE_TO_INSTITUTION_MEMBERS);
    queryBuf.append("'");

    queryBuf.append(" ) ");
    return true;
  }

  private  boolean appendMembersAndCollaboratorsCriteria(StringBuffer queryBuf, String classShortName ) {
    Set labs = getAllMyGroups();
    if (labs.isEmpty()) {
      return false;
    }

    // req.idLab in (....)
    queryBuf.append(" ( ");
    queryBuf.append(classShortName);
    queryBuf.append(".idLab in ( ");
    for (Iterator i = labs.iterator(); i.hasNext();) {
      Lab theLab = (Lab) i.next();
      queryBuf.append(theLab.getIdLab());
      if (i.hasNext()) {
        queryBuf.append(", ");
      }
    }
    queryBuf.append(" )");
    queryBuf.append(" AND ");

    // req.codeVisibility is 'visible to collaborators and members'
    queryBuf.append(classShortName);
    queryBuf.append(".codeVisibility = '");
    queryBuf.append(Visibility.VISIBLE_TO_GROUP_MEMBERS_AND_COLLABORATORS);
    queryBuf.append("'");

    queryBuf.append(" ) ");

    return true;
  }




  private boolean appendGroupCriteria(StringBuffer queryBuf, String classShortName) {
    Set labs = getAllMyGroups();
    if (labs.isEmpty()) {
      return false;
    }


    queryBuf.append(" ( ");



    // object.idLab in (....)
    if (!labs.isEmpty()) {
      queryBuf.append(classShortName);
      queryBuf.append(".idLab in ( ");
      for (Iterator i = labs.iterator(); i.hasNext();) {
        Lab theLab = (Lab) i.next();
        queryBuf.append(theLab.getIdLab());
        if (i.hasNext()) {
          queryBuf.append(", ");
        }
      }
      queryBuf.append(" )");
    }

    queryBuf.append(" ) ");

    return true;
  }



  private boolean appendPublicCriteria(StringBuffer queryBuf, String classShortName, boolean scopeToGroups) {
    queryBuf.append(" ( ");

    if (scopeToGroups) {

      // req.idLab in (....)
      Set labs = getAllMyGroups();
      if (!labs.isEmpty()) {

        queryBuf.append(classShortName);
        queryBuf.append(".idLab in ( ");
        for (Iterator i = labs.iterator(); i.hasNext();) {
          Lab theLab = (Lab) i.next();
          queryBuf.append(theLab.getIdLab());
          if (i.hasNext()) {
            queryBuf.append(", ");
          }
        }
        queryBuf.append(" )");
        queryBuf.append(" AND ");
      } else {
        // User doesn't belong to any labs, yet we are supposed to scope by lab.
        // Therefore, generate criteria that will result in empty results
        queryBuf.append(classShortName);
        queryBuf.append(".idLab = -1 ");
        queryBuf.append(" AND ");        

      }

    } 

    queryBuf.append(classShortName);
    queryBuf.append(".codeVisibility = '");
    queryBuf.append(Visibility.VISIBLE_TO_PUBLIC);
    queryBuf.append("'");

    queryBuf.append(" ) ");

    return true;

  }


  private void appendLuceneCoreFacilitiesIManage(StringBuffer searchText) {
    boolean firstTime = true;
    for (CoreFacility cf : (Set<CoreFacility>)this.getCoreFacilitiesIManage()) {
      if (!firstTime) {
        searchText.append(" ");
        firstTime = false;
      }
      searchText.append(cf.getIdCoreFacility());
    }

  }

  public boolean buildLuceneSecurityFilter(StringBuffer searchText, String labField, String institutionField, String coreFacilityField, 
      String projectCoreFacilityField, String collaboratorField, String ownerField,String visibilityField, boolean scopeToGroup, 
      String inheritedLabField, String leftJoinExclusionCriteria, Boolean forGlobal, String submitterField) {
    boolean addedFilter = false;

    // Admins
    if (hasPermission(this.CAN_ADMINISTER_ALL_CORE_FACILITIES)) {
    } 
    else if (hasPermission(CAN_ACCESS_ANY_OBJECT)) {
      if (coreFacilityField != null) {
        if (forGlobal) {
          searchText.append(" ( ");
        }
        if (this.getCoreFacilitiesIManage().isEmpty()) {
          throw new RuntimeException("Unable to filter experiments by core facility because admin not associated with any core facilty");
        }
        searchText.append(" ( ");
        searchText.append(coreFacilityField + ":(");
        appendLuceneCoreFacilitiesIManage(searchText);
        searchText.append(")");
        searchText.append(" ) ");

        //need submitter field
        if(submitterField != null) {
          searchText.append(" OR (  ");
          searchText.append(submitterField + ":( " + this.getIdAppUser() );
          searchText.append(") )");
        }

        searchText.append(" OR ( ( ").append(leftJoinExclusionCriteria).append(" ) AND ( ").append(projectCoreFacilityField).append(":(");
        appendLuceneCoreFacilitiesIManage(searchText);
        searchText.append(") ) )");
        addedFilter = true;
      }
      if (addedFilter && forGlobal) {
        // close opening paren added in top forglobal
        searchText.append(" ) ");

        // Global searches have all object types and admin security restrictions only apply to experiments and empty project folders.
        searchText.append(" OR ( ");
        searchText.append(GlobalIndexHelper.OBJECT_TYPE).append(":(").append(GlobalIndexHelper.ANALYSIS);
        searchText.append(" ").append(GlobalIndexHelper.PROTOCOL);
        searchText.append(" ").append(GlobalIndexHelper.TOPIC);
        searchText.append(" ").append(GlobalIndexHelper.DATA_TRACK);
        searchText.append(")");

        searchText.append(" ) ");
      }
    }
    // GNomEx users
    else if (hasPermission(this.CAN_PARTICIPATE_IN_GROUPS)) {
      if (forGlobal) {
        searchText.append(" ( ");
      }
      searchText.append(" ( ");

      boolean added = buildLuceneOwnershipFilter(searchText, labField, ownerField, visibilityField);

      boolean added1 =  buildLuceneCollaboratorListFilter(searchText, collaboratorField);

      boolean added2 = false;
      if (getGroupsIAmMemberOrManagerOf().size() > 0) {
        if (added || added1) {
          searchText.append(" OR ");
        }
        added2 = buildLuceneMembershipFilter(searchText, labField, visibilityField);
      }

      // Add criteria for objects with collaborator visibility
      boolean added3 = false;
      if (getAllMyGroups().size() > 0) {
        if (added || added1 || added2) {
          searchText.append(" OR ");        
        }
        added3 = buildLuceneMemberCollaboratorFilter(searchText, labField, visibilityField);        
      }


      // Add criteria for objects with institution visibility
      boolean added4 = false;
      if (getInstitutionsIAmMemberOf().size() > 0) {
        if (added1 || added2 || added3) {
          searchText.append(" OR ");        
        }
        added4  = buildLuceneInstitutionFilter(searchText, institutionField, visibilityField);        
      }


      // Add criteria for public objects
      if (added1 || added2 || added3 || added4) {
        searchText.append(" OR ");        
      }
      boolean added5 = buildLucenePublicFilter(searchText, labField, visibilityField, scopeToGroup);

      boolean added6 = false;
      if(submitterField != null) {
        searchText.append(" OR (  ");
        searchText.append(submitterField + ":( " + this.getIdAppUser() );
        searchText.append(" ) ) ");
        added6 = true;
      }

      addedFilter = added || added1 || added2 || added3 || added4 || added5 || added6;        

      searchText.append(" ) ");

      // Add exclusion criteria to pick up "empty" projects / analysis groups 
      // for the lab the user belongs to
      if (addedFilter && leftJoinExclusionCriteria != null && this.getAllMyGroups().size() > 0) {
        searchText.append(" OR ");
        searchText.append(" ( ");

        buildLuceneGroupFilter(searchText, inheritedLabField);
        searchText.append(" AND ");
        searchText.append(" ( ");
        searchText.append(leftJoinExclusionCriteria);
        searchText.append(" ) ");

        searchText.append(" ) ");
        addedFilter = true;
      }
      if (addedFilter && forGlobal) {
        // Global searches have all object types and non-admin security restrictions do not apply to protocols
        searchText.append(" OR ").append(GlobalIndexHelper.OBJECT_TYPE).append(":").append(GlobalIndexHelper.PROTOCOL);
        searchText.append(" ) ");
      }
    }
    // Guest
    else {
      if (forGlobal) {
        searchText.append(" ( ");
      }
      addedFilter = buildLucenePublicFilter(searchText, labField, visibilityField, false);        
      if (addedFilter && forGlobal) {
        // Global searches have all object types and non-admin security restrictions do not apply to protocols
        searchText.append(" OR ").append(GlobalIndexHelper.OBJECT_TYPE).append(":").append(GlobalIndexHelper.PROTOCOL);
        searchText.append(" ) ");
      }
    }

    return addedFilter;
  }


  private boolean buildLuceneOwnershipFilter(StringBuffer searchText, String labField, String ownerField, String visibilityField ) {
    Set mgrLabs = getGroupsIManage();

    searchText.append(" ( ");

    // Get all objects owned by this user or objects belonging to lab managed by this user    
    searchText.append(" ( ");
    searchText.append(ownerField + ":");
    searchText.append(this.getIdAppUser());

    if (!mgrLabs.isEmpty()) {
      searchText.append(" OR ");
      // req.idLab in (....)
      searchText.append(labField);
      searchText.append(":(");
      for(Iterator i = mgrLabs.iterator(); i.hasNext();) {
        Lab theLab = (Lab)i.next();
        searchText.append(theLab.getIdLab());
        if (i.hasNext()) {
          searchText.append(" ");
        }
      }      
      searchText.append(")");      
    }
    searchText.append(" ) ");


    // req.codeVisibility = 'visible to members'
    searchText.append(" AND ");
    searchText.append(visibilityField + ":");
    searchText.append(Visibility.VISIBLE_TO_OWNER);

    searchText.append(" ) ");
    return true;
  }

  private boolean buildLuceneCollaboratorListFilter(StringBuffer searchText, String collaboratorField) {
    boolean addedFilter = false;

    searchText.append(" ( ");

    // Get all objects this user is specified as a collaborator on.    
    searchText.append(collaboratorField + ":");
    searchText.append("COLLAB-" +  this.getIdAppUser() + "-COLLAB");

    searchText.append(" ) ");

    addedFilter = true;


    return addedFilter;
  }

  private boolean buildLuceneMembershipFilter(StringBuffer searchText, String labField, String visibilityField ) {
    Set labs = getGroupsIAmMemberOrManagerOf();
    if (labs.isEmpty()) {
      return false;
    }

    searchText.append(" ( ");

    // req.idLab in (....)
    searchText.append(labField);
    searchText.append(":(");
    for(Iterator i = labs.iterator(); i.hasNext();) {
      Lab theLab = (Lab)i.next();
      searchText.append(theLab.getIdLab());
      if (i.hasNext()) {
        searchText.append(" ");
      }
    }      
    searchText.append(")");

    // req.codeVisibility = 'visible to members'
    searchText.append(" AND ");
    searchText.append(visibilityField + ":");
    searchText.append(Visibility.VISIBLE_TO_GROUP_MEMBERS);
    searchText.append("'");

    searchText.append(" ) ");
    return true;
  }

  private boolean buildLuceneMemberCollaboratorFilter(StringBuffer searchText, String labField, String visibilityField ) {
    Set labs = getAllMyGroups();
    if (labs.isEmpty()) {
      return false;
    }

    searchText.append(" ( ");
    searchText.append(labField);
    searchText.append(":(");
    for (Iterator i = labs.iterator(); i.hasNext();) {
      Lab theLab = (Lab) i.next();
      searchText.append(theLab.getIdLab());
      if (i.hasNext()) {
        searchText.append(" ");
      }
    }
    searchText.append(" )");
    searchText.append(" AND ");

    // req.codeVisibility is 'visible to collaborators and members'
    searchText.append(visibilityField + ":");
    searchText.append(Visibility.VISIBLE_TO_GROUP_MEMBERS_AND_COLLABORATORS);

    searchText.append(" ) ");

    return true;
  }

  private boolean buildLuceneInstitutionFilter(StringBuffer searchText, String institutionField, String visibilityField ) {
    Set institutions = getInstitutionsIAmMemberOf();
    if (institutions.isEmpty()) {
      return false;
    }

    searchText.append(" ( ");

    // req.idLab in (....)
    searchText.append(institutionField);
    searchText.append(":(");
    for(Iterator i = institutions.iterator(); i.hasNext();) {
      Institution theInstitution = (Institution)i.next();
      searchText.append(theInstitution.getIdInstitution());
      if (i.hasNext()) {
        searchText.append(" ");
      }
    }      
    searchText.append(")");

    // req.codeVisibility = 'visible to institution'
    searchText.append(" AND ");
    searchText.append(visibilityField + ":");
    searchText.append(Visibility.VISIBLE_TO_INSTITUTION_MEMBERS);
    searchText.append("'");

    searchText.append(" ) ");
    return true;
  }


  private boolean buildLuceneGroupFilter(StringBuffer searchText, String labField) {
    if (getAllMyGroups().isEmpty()) {
      return false;
    }

    searchText.append(" ( ");

    // req.idLab in (....)
    searchText.append(labField);
    searchText.append(":(");
    for(Iterator i = getAllMyGroups().iterator(); i.hasNext();) {
      Lab theLab = (Lab)i.next();
      searchText.append(theLab.getIdLab());
      if (i.hasNext()) {
        searchText.append(" ");
      }
    }      
    searchText.append(")");


    searchText.append(" ) ");
    return true;
  }

  private boolean buildLucenePublicFilter(StringBuffer searchText, String labField, String visibilityField, boolean scopeToGroups) {
    searchText.append(" ( ");

    searchText.append(" ( ");
    if (scopeToGroups) {
      Set labs = getAllMyGroups();
      if (!labs.isEmpty()) {
        searchText.append(labField);
        searchText.append(":(");
        for (Iterator i = labs.iterator(); i.hasNext();) {
          Lab theLab = (Lab) i.next();
          searchText.append(theLab.getIdLab());
          if (i.hasNext()) {
            searchText.append(" ");
          }
        }
        searchText.append(")");
        searchText.append(" AND ");
      }
    }

    searchText.append(visibilityField);
    searchText.append(":");
    searchText.append(Visibility.VISIBLE_TO_PUBLIC);
    searchText.append(" ) ");


    searchText.append(" ) ");
    return true;
  }


  private boolean addWhereOrAnd(StringBuffer queryBuf, boolean addWhere) {
    if (addWhere) {
      queryBuf.append(" WHERE ");
      addWhere = false;
    } else {
      queryBuf.append(" AND ");
    }
    return addWhere;
  }

  /**
   * This method retrieves the BSTX Security Advisor used for testing BST permissions through an
   * external application. If the BSTX Security Advisor hasn't been created yet, this method creates 
   * it.
   * 
   * @param request A copy of the active HTTP Servlet Request that was passed into the command.
   * @param httpSession A copy of the HTTP Session.
   * @param username The username of the individual the initiated this request.
   * @return A copy of the BSTX Security Advisor.
   */
  public BSTXSecurityAdvisor getBSTXSecurityAdvisor(HttpServletRequest request, HttpSession httpSession, String username) throws SQLException, NamingException
  {
    if (bstxSecurityAdvisor == null && canAccessBSTX)
    {
      // Get the BSTX SecurityAdvisor.
      if (httpSession.getAttribute(BSTXSecurityAdvisor.SECURITY_ADVISOR_SESSION_KEY) != null)
      {
        bstxSecurityAdvisor = (BSTXSecurityAdvisor)httpSession.getAttribute(BSTXSecurityAdvisor.SECURITY_ADVISOR_SESSION_KEY);
      }

      // If it doesn't exist, create it and save it for later (so we only have to create it once).
      else
      {
        Session bstxSession = HibernateBSTXSession.currentBSTXSession(username);
        try {
          bstxSecurityAdvisor = BSTXSecurityAdvisor.create(request, httpSession, bstxSession, this.ntUserName);
          httpSession.setAttribute(BSTXSecurityAdvisor.SECURITY_ADVISOR_SESSION_KEY, bstxSecurityAdvisor);
        } finally {
          HibernateBSTXSession.closeBSTXSession();
        }
      }
    }

    return bstxSecurityAdvisor;
  }

  public Session getReadOnlyHibernateSession(String userName) throws Exception{
    Session sess = null;
    sess = HibernateGuestSession.currentGuestSession(userName);    
    isReadOnlySession = true;
    return sess;
  }

  public void closeReadOnlyHibernateSession() throws Exception{
    HibernateGuestSession.closeGuestSession();
    isReadOnlySession = false;
  }


  public Session getHibernateSession(String userName) throws Exception{
    Session sess = null;

    if (this.isGuest()) {
      sess = HibernateGuestSession.currentGuestSession(userName);
      isReadOnlySession = true;
    } else {
      sess = HibernateSession.currentSession(userName);
      isReadOnlySession = false;
    }
    return sess;
  }

  public Session getWritableHibernateSession(String userName) throws Exception {
    Session sess = HibernateSession.currentSession(userName);
    isReadOnlySession = false;
    return sess;
  }

  public void closeHibernateSession() throws Exception{
    if (this.isReadOnlySession) {
      HibernateGuestSession.closeGuestSession();
    } else {
      HibernateSession.closeSession();
      HibernateSession.closeTomcatSession();
    }
    this.isReadOnlySession = false;
  }


  public String getVersion() {
    return version;
  }


  public void setVersion(String version) {
    this.version = version;
  }

  private Set<CoreFacility> filterCoreFacilities(Set<CoreFacility> inCores) {
    if (specifiedIdCoreFacility == null) {
      return inCores;
    } else {
      Set<CoreFacility> outCores = new TreeSet();
      for(CoreFacility f : inCores) {
        if (f.getIdCoreFacility().equals(specifiedIdCoreFacility)) {
          outCores.add(f);
        }
      }
      return outCores;
    }
  }

  public Set getCoreFacilitiesIManage() {
    if (appUser != null && appUser.getManagingCoreFacilities() != null) {
      return filterCoreFacilities(appUser.getManagingCoreFacilities());
    }
    return new TreeSet();
  }

  public Set getCoreFacilitiesForMyLab() {

    TreeSet coreFacilities = new TreeSet();
    if (hasPermission(this.CAN_PARTICIPATE_IN_GROUPS)) {
      for(Iterator i = this.getAllMyGroups().iterator(); i.hasNext();) {
        Lab l = (Lab)i.next();
        coreFacilities.addAll(l.getCoreFacilities());
      }
    } 
    return filterCoreFacilities(coreFacilities);

  }

  public Set getCoreFacilitiesICanSubmitTo() {
    if(appUser != null && appUser.getCoreFacilitiesICanSubmitTo() != null) {
      TreeSet cores = new TreeSet();
      for(Iterator i = appUser.getCoreFacilitiesICanSubmitTo().iterator(); i.hasNext();) {
        CoreFacility cf = (CoreFacility)i.next();
        if (this.coreAllowsGlobalSubmission(cf.getIdCoreFacility())) {
          cores.add(cf);
        }
      }
      return cores;
    }

    return new TreeSet();
  }

  public boolean canAccessBSTX() {
    return canAccessBSTX;
  }

  public boolean appendExcludeClinicResearchCriteria(StringBuffer queryBuf, boolean isFirstCriteria, DictionaryHelper dictionaryHelper, String reqShortName) {
    List requestCategories = dictionaryHelper.getClinicResearchRequestCategoryList();

    if (requestCategories.size() > 0) {
      queryBuf.append(isFirstCriteria ? "WHERE " : " AND ");
      isFirstCriteria = false;

      queryBuf.append(" " + reqShortName + ".codeRequestCategory not in (");

      int count = 0;
      for (Iterator i = requestCategories.iterator(); i.hasNext();) {
        RequestCategory requestCategory = (RequestCategory)i.next();
        if (requestCategory.getIsClinicalResearch() != null && requestCategory.getIsClinicalResearch().equals("Y")) {
          if (count > 0) {
            queryBuf.append(", ");            
          }

          queryBuf.append("'");
          queryBuf.append(requestCategory.getCodeRequestCategory());
          queryBuf.append("'");    
          count++;
        }

      }

      queryBuf.append(") ");

      return true;
    }
    return false;
  }

  public String appendIdCoreForUrl(String Url) {
    // if not super user and core facility specified when logging in, pass the core facility along on the url.
    if (!hasPermission(CAN_ADMINISTER_ALL_CORE_FACILITIES) && this.specifiedIdCoreFacility != null) {
      if (Url.contains("?")) {
        Url += "&";
      } else {
        Url += "?";
      }
      Url += "idCore=" + this.specifiedIdCoreFacility.toString();
    }
    return Url;
  }

  public Boolean coreAllowsGlobalSubmission(Integer idCoreFacility) {
    return this.coreFacilitiesAllowingGlobalSubmission.containsKey(idCoreFacility);
  }
}
