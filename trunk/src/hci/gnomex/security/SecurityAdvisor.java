package hci.gnomex.security;

import hci.dictionary.model.DictionaryEntry;
import hci.dictionary.model.NullDictionaryEntry;
import hci.framework.model.DetailObject;
import hci.framework.security.UnknownPermissionException;
import hci.gnomex.constants.Constants;
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
import hci.gnomex.model.FlowCell;
import hci.gnomex.model.Institution;
import hci.gnomex.model.Lab;
import hci.gnomex.model.Project;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.Request;
import hci.gnomex.model.Sample;
import hci.gnomex.model.Property;
import hci.gnomex.model.SlideProduct;
import hci.gnomex.model.UserPermissionKind;
import hci.gnomex.model.Visibility;
import hci.gnomex.utility.HibernateGuestSession;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.LabComparator;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.hibernate.Hibernate;
import org.hibernate.Session;


public class SecurityAdvisor extends DetailObject implements Serializable, hci.framework.security.SecurityAdvisor {
  // Security advisor session variable
  public static final String          SECURITY_ADVISOR_SESSION_KEY       = "gnomexSecurityAdvisor";
  
  private static final String         RESTRICTED = "(restricted)";
  
  public static final int            PROFILE_OBJECT_VISIBILITY          = 1;
  public static final int            PROFILE_GROUP_MEMBERSHIP           = 2;

  
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
  
  public static final String          CAN_PARTICIPATE_IN_GROUPS                   = "canParticipateInGroups";            
  public static final String          CAN_SUBMIT_REQUESTS                         = "canSubmitRequests";            
  
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
  
  // version info
  private String                       version;
  
  // Global permission map
  private Map                          globalPermissionMap = new HashMap();
  
  private String                       loginDateTime;
  
  
  public String getLoginDateTime() {
    return loginDateTime;
  }

  private SecurityAdvisor(AppUser appUser, boolean isGNomExUniversityUser, boolean isGNomExExternalUser, boolean isUniversityOnlyUser, boolean isLabManager) throws InvalidSecurityAdvisorException {
    
    this.appUser = appUser;
    this.isGNomExUniversityUser = isGNomExUniversityUser;
    this.isGNomExExternalUser = isGNomExExternalUser;
    this.isUniversityOnlyUser = isUniversityOnlyUser;
    this.loginDateTime = new SimpleDateFormat("MMM dd hh:mm a").format(System.currentTimeMillis());
    this.isLabManager = isLabManager;

    validate();
    setGlobalPermissions();    
  }
  
  private SecurityAdvisor() throws InvalidSecurityAdvisorException {
    isGuest = true;
    isGNomExUniversityUser = false;
    isGNomExExternalUser = false;
    isUniversityOnlyUser = false;
    isLabManager = false;
    this.loginDateTime = new SimpleDateFormat("MMM-dd HH:mm").format(System.currentTimeMillis());
    setGlobalPermissions();
  }
  
  public boolean isAdmin() {
    return this.hasPermission(CAN_ACCESS_ANY_OBJECT);
  }
  
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
    } else {
      return "N";
    }
  }

  public String getIsUniversityOnlyUser() {
    if (this.isUniversityOnlyUser) {
      return "Y";
    } else {
      return "N";
    }
  }


  public String getIsExternalUser() {
    if (this.isGNomExExternalUser) {
      return "Y";
    } else {
      return "N";
    }
  }

  public static SecurityAdvisor create(Session   sess, 
                                       String    uid) throws InvalidSecurityAdvisorException {
    SecurityAdvisor securityAdvisor = null;
    
    // If the login is "guest" just instantiate a security advisor
    // as 'guest'.
    if (uid.equalsIgnoreCase("guest")) {
      return new SecurityAdvisor();
    }
    
    boolean isGNomExUniversityUser = false;
    boolean isGNomExExternalUser = false;
    boolean isUniversityOnlyUser = true;
    boolean isLabManager = false;
    
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
      
    }
    
    if (appUser == null) {
      throw new InvalidSecurityAdvisorException("Cannot find AppUser " + uid);
    } else {
      if(appUser.getManagingLabs() != null && appUser.getManagingLabs().size() > 0) {
        isLabManager = true;
      }
    }
    
    
    // Instantiate SecurityAdvisor
    securityAdvisor = new SecurityAdvisor(appUser, isGNomExUniversityUser, isGNomExExternalUser, isUniversityOnlyUser, isLabManager);
    // Make sure we have a valid state.
    securityAdvisor.validate();
    // Initialize institutions (lazy loading causing invalid object
    securityAdvisor.getInstitutionsIAmMemberOf();
    
    return securityAdvisor;
  }
  
  public static SecurityAdvisor createGuest()
      throws InvalidSecurityAdvisorException {
    SecurityAdvisor securityAdvisor = null;

    securityAdvisor = new SecurityAdvisor();
    

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
      // Admins
      if (hasPermission(this.CAN_ACCESS_ANY_OBJECT)) {
        canRead = true;
      }
      // Normal gnomex users
      else if (hasPermission(this.CAN_PARTICIPATE_IN_GROUPS)) {
        Request req = (Request)object;
        
        // First, check to see if the user is a specified as a collaborator
        // on this request.
        for (Iterator i = req.getCollaborators().iterator(); i.hasNext();) {
          ExperimentCollaborator collaborator = (ExperimentCollaborator)i.next();
          if (isLoggedInUser(collaborator.getIdAppUser())) {
            canRead = true;
            break;
          }
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
      // Guest users
      else {
        Request req = (Request)object;
        
        // Request has public visibility
        if (req.getCodeVisibility().equals(Visibility.VISIBLE_TO_PUBLIC)) {
          canRead = true;
        }
      }
    }
    // 
    // Analysis
    //
    else if (object instanceof Analysis) {
      // Admins
      if (hasPermission(this.CAN_ACCESS_ANY_OBJECT)) {
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
      // Normal users can read only their own AppUserLite dictionary entry
      else if (object instanceof AppUserLite) {
        AppUserLite u = (AppUserLite)object;
        if (u.getIdAppUser() != null && !this.isGuest() &&
            u.getIdAppUser().equals(this.getIdAppUser())) {
          canRead = true;
        } 
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

    if (canRead) {
      // Super admins can read every dictionary entry
      if (!hasPermission(this.CAN_ADMINISTER_ALL_CORE_FACILITIES)) {
        if (object instanceof PropertyDictionary) {
          PropertyDictionary prop1 = (PropertyDictionary)object;
          if (prop1.getIdCoreFacility() != null) {
            // Only show properties with core facility user can see.
            Boolean found = false;
            for(Iterator facilityIter = getAppUser().getManagingCoreFacilities().iterator();facilityIter.hasNext();) {
              CoreFacility facility = (CoreFacility)facilityIter.next();
              if (facility.getIdCoreFacility().equals(prop1.getIdCoreFacility())) {
                found = true;
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


  public boolean canUpdate(DetailObject object) throws UnknownPermissionException {
    boolean canUpdate = false;
    
    //
    // Request
    //
    if (object instanceof Request) {
      
      // Admins
      if (hasPermission(this.CAN_WRITE_ANY_OBJECT)) {
        canUpdate = true;
      }
      // University GNomEx users
      else if (hasPermission(this.CAN_PARTICIPATE_IN_GROUPS)) {
        Request req = (Request)object;
        
        // Lab manager
        if (isGroupIManage(req.getIdLab())) {
          canUpdate = true;
        }
        // Owner of request
        else if (isGroupIAmMemberOf(req.getIdLab()) && isOwner(req.getIdAppUser())) {
          canUpdate = true;
        } 
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
        
        // Admins
        if (hasPermission(this.CAN_WRITE_ANY_OBJECT)) {
          canUpdate = true;
        }
        // University GNomEx users
        else if (hasPermission(this.CAN_PARTICIPATE_IN_GROUPS)) {
          Request req = (Request)object;
          // Lab manager
          if (isGroupIManage(req.getIdLab())) {
            canUpdate = true;
          } 
          // Owner of request
          else if (isGroupIAmMemberOf(req.getIdLab()) && isOwner(req.getIdAppUser())) {
            canUpdate = true;
          } 
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
      canUpload = canUpdate(req) || isCollaboratorUploader(req);
    }
    //
    // Analysis
    //
    else if (object instanceof Analysis) {
      Analysis analysis = (Analysis)object;
      canUpload = canUpdate(analysis) || isCollaboratorUploader(analysis);
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
      // Admin
      if (hasPermission(this.CAN_DELETE_REQUESTS)) {
        canDelete = true;
      } 
      // Lab manager or owner can delete experiment if the samples
      // have not proceeded in the workflow
      else if (isGroupIManage(r.getIdLab()) || (isGroupIAmMemberOf(r.getIdLab()) && isOwner(r.getIdAppUser()))) {
        int deleteSampleCount = 0;
        for (Iterator i = r.getSamples().iterator(); i.hasNext();) {
          Sample s = (Sample)i.next();
          if (s.getCanChangeSampleInfo().equals("Y")) {
            deleteSampleCount++;
          }
        }
        canDelete = r.getSamples().size() == deleteSampleCount;
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
    
    return canDelete;
  }
  
  public void flagPermissions(DetailObject object) throws UnknownPermissionException {
    object.canRead(this.canRead(object));
    object.canUpdate(this.canUpdate(object));
    object.canDelete(this.canDelete(object));
    
    if (object instanceof Request) {
      Request req = (Request)object;
      req.canUpdateVisibility(this.canUpdate(object, this.PROFILE_OBJECT_VISIBILITY));
      req.canUploadData(this.canUploadData(req));
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
  
  
  private boolean isCollaboratorUploader(Analysis analysis) {
    // First, check to see if the user is a specified as a collaborator
    // on this request.
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
  
  public void scrub(DetailObject object) throws UnknownPermissionException {
  }
  

  public Set getGlobalPermissions() {
    return globalPermissionMap.keySet();
  }
  
  private void setGlobalPermissions() {
    globalPermissionMap = new HashMap();
    
   
    if (isGuest) {
      return;
    }
    
    // Can administer all core facilities (Super user)
    if (appUser.getCodeUserPermissionKind().equals(UserPermissionKind.SUPER_ADMIN_PERMISSION_KIND)) {
      globalPermissionMap.put(new Permission(CAN_ADMINISTER_ALL_CORE_FACILITIES), null);
    }
    
    // Can assign the super user role. (Super user)
    if (appUser.getCodeUserPermissionKind().equals(UserPermissionKind.SUPER_ADMIN_PERMISSION_KIND)) {
      globalPermissionMap.put(new Permission(CAN_ASSIGN_SUPER_ADMIN_ROLE), null);
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
    
    // Can manager workflow
    if (appUser.getCodeUserPermissionKind().equals(UserPermissionKind.ADMIN_PERMISSION_KIND) ||
        appUser.getCodeUserPermissionKind().equals(UserPermissionKind.SUPER_ADMIN_PERMISSION_KIND)) {
      globalPermissionMap.put(new Permission(CAN_MANAGE_WORKFLOW), null);
    }

    // Can manage billing
    if (appUser.getCodeUserPermissionKind().equals(UserPermissionKind.ADMIN_PERMISSION_KIND) ||
        appUser.getCodeUserPermissionKind().equals(UserPermissionKind.SUPER_ADMIN_PERMISSION_KIND) ||
        appUser.getCodeUserPermissionKind().equals(UserPermissionKind.BILLING_PERMISSION_KIND)) {
      globalPermissionMap.put(new Permission(CAN_MANAGE_BILLING), null);
    }
    
    // Can administer users
    if (appUser.getCodeUserPermissionKind().equals(UserPermissionKind.ADMIN_PERMISSION_KIND) ||
        appUser.getCodeUserPermissionKind().equals(UserPermissionKind.SUPER_ADMIN_PERMISSION_KIND)) {
      globalPermissionMap.put(new Permission(CAN_ADMINISTER_USERS), null);
    }

    // Can access any requests
    if (appUser.getCodeUserPermissionKind().equals(UserPermissionKind.ADMIN_PERMISSION_KIND) ||
        appUser.getCodeUserPermissionKind().equals(UserPermissionKind.SUPER_ADMIN_PERMISSION_KIND)) {
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

    // Can access objects governed by group level permissions
    if (this.isGNomExExternalUser || this.isGNomExUniversityUser) {
      globalPermissionMap.put(new Permission(CAN_PARTICIPATE_IN_GROUPS), null);
    }
    
    // Can submit requests
    if (this.getAllMyGroups().size() > 0 || appUser.getCodeUserPermissionKind().equals(UserPermissionKind.ADMIN_PERMISSION_KIND) ||
        appUser.getCodeUserPermissionKind().equals(UserPermissionKind.SUPER_ADMIN_PERMISSION_KIND)) {
      globalPermissionMap.put(new Permission(CAN_SUBMIT_REQUESTS), null);            
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


  }
  
    
  
  public String getUserFirstName() {
    if (isGuest) {
      return "guest";
    } else {
      return appUser.getFirstName();      
    }
  }

  
  public String getUserLastName() {
    if (isGuest) {
      return "";
    } else {
      return appUser.getLastName();
    }
  }
  
  
  public String getUserEmail() {
    if (isGuest) {
      return "";
    } else {
      return appUser.getEmail();      
    }
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
  
  public String getUID() {
    if (isGuest) {
      return "";
    } else if (this.isGNomExUniversityUser || this.isUniversityOnlyUser) {
      return appUser.getuNID();
    } else {
      return appUser.getUserNameExternal();
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

  
  public Set getGroupsIAmMemberOf() {
    if (hasPermission(this.CAN_PARTICIPATE_IN_GROUPS)) {
      return this.getAppUser().getLabs();
    } else { 
      return new TreeSet();
    }
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
      return this.getAppUser().getCollaboratingLabs();
    } else {
      return new TreeSet();
    }
  }
  
  public Set getGroupsIManage() {
    if (hasPermission(this.CAN_PARTICIPATE_IN_GROUPS)) {
      return this.getAppUser().getManagingLabs();
    } else {
      return new TreeSet();
    }
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
      lab.excludeMethodFromXML("getContactAddress");
      lab.excludeMethodFromXML("getContactCity");
      lab.excludeMethodFromXML("getContactCodeState");
      lab.excludeMethodFromXML("getContactZip");
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
    
    if (hasPermission(this.CAN_ACCESS_ANY_OBJECT)) {
      isMyLab = true;
    } else if (hasPermission(this.CAN_PARTICIPATE_IN_GROUPS)) {
      
      if (idLab != null) {
        for(Iterator i = this.getAppUser().getLabs().iterator(); i.hasNext();) {
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
        for(Iterator i = this.getAppUser().getCollaboratingLabs().iterator(); i.hasNext();) {
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
  
  public boolean isGroupIManage(Integer idLab) {
    boolean isMyLab = false;
    
    if (hasPermission(this.CAN_ACCESS_ANY_OBJECT)) {
      isMyLab = true;
    } else if (hasPermission(this.CAN_PARTICIPATE_IN_GROUPS)) {
      
      if (idLab != null) {
        for(Iterator i = this.getAppUser().getManagingLabs().iterator(); i.hasNext();) {
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
  
  private boolean isOwner(Integer idAppUserOfObject) {
    if (hasPermission(this.CAN_ACCESS_ANY_OBJECT)) {
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
    } else {
      return appUser;      
    }
  }

  
  public void setAppUser(AppUser appUser) {
    this.appUser = appUser;
  }
  
  public boolean buildSpannedSecurityCriteria(StringBuffer queryBuf, String inheritedClassShortName, String classShortName, String collabClassShortName, boolean isFirstCriteria, String visibilityField, boolean scopeToGroup, String leftJoinExclusionCriteria) {
    if (hasPermission(SecurityAdvisor.CAN_ACCESS_ANY_OBJECT)) {
      
      // GNomex is not restricted
      
    } else if (hasPermission(SecurityAdvisor.CAN_PARTICIPATE_IN_GROUPS)) {
      queryBuf.append(isFirstCriteria ? "WHERE " : " AND ");
      isFirstCriteria = false;
      queryBuf.append(" ( ");
      
      // Add criteria for collaborator list
      boolean criteriaAdded = appendSecurityCollaboratorListCriteria(queryBuf, collabClassShortName);
      
      // Add criteria with owner visibility
      queryBuf.append( !criteriaAdded ? "WHERE " : " OR ");
      criteriaAdded = appendOwnerCriteria(queryBuf, classShortName);

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


      queryBuf.append(" ) ");
    } else {
      // Guest or University only user can access public objects
      queryBuf.append(isFirstCriteria ? "WHERE " : " AND ");
      isFirstCriteria = false;
      queryBuf.append(" ( ");

      appendPublicCriteria(queryBuf, classShortName, false);        

      queryBuf.append(" ) ");
    }
    return isFirstCriteria;
  }
  
  public boolean buildSecurityCriteria(StringBuffer queryBuf, String classShortName, String collabClassShortName, boolean isFirstCriteria, boolean scopeToGroup) {
    if (hasPermission(SecurityAdvisor.CAN_ACCESS_ANY_OBJECT)) {
      
      // GNomex is not restricted
      
    } else if (hasPermission(SecurityAdvisor.CAN_PARTICIPATE_IN_GROUPS)) {
      
      // GNomex user can access public objects, objects he is listed as
      // a collaborator or objects matching visibility
      queryBuf.append(isFirstCriteria ? "WHERE " : " AND ");
      
      isFirstCriteria = false;
      queryBuf.append(" ( ");
      
      // Add criteria for collaborator list
      boolean criteriaAdded = appendSecurityCollaboratorListCriteria(queryBuf, collabClassShortName);

      // Add criteria with owner visibility
      queryBuf.append( !criteriaAdded ? "WHERE " : " OR ");
      criteriaAdded = appendOwnerCriteria(queryBuf, classShortName);

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
    } else {
      
      // Guest or University only user cab access public objects
      queryBuf.append(isFirstCriteria ? "WHERE " : " AND ");
      isFirstCriteria = false;
      queryBuf.append(" ( ");

      appendPublicCriteria(queryBuf, classShortName, false);        

      queryBuf.append(" ) ");
       
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




  public boolean addPublicOnlySecurityCriteria(StringBuffer queryBuf, String classShortName, boolean addWhereOrAnd) {

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
    
    return addWhereOrAnd;
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


  public boolean buildLuceneSecurityFilter(StringBuffer searchText, String labField, String institutionField, 
      String collaboratorField, String ownerField,String visibilityField, boolean scopeToGroup, 
      String inheritedLabField, String leftJoinExclusionCriteria) {
    boolean addedFilter = false;
    
    // Admins
    if (hasPermission(CAN_ACCESS_ANY_OBJECT)) {
    }
    // GNomEx users
    else if (hasPermission(this.CAN_PARTICIPATE_IN_GROUPS)) {
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
      addedFilter = added || added1 || added2 || added3 || added4 || added5;        

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
        return true;
      }
    }
    // Guest
    else {
      addedFilter = buildLucenePublicFilter(searchText, labField, visibilityField, false);        
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
  
  public Session getReadOnlyHibernateSession(String userName) throws Exception{
    Session sess = null;
    sess = HibernateGuestSession.currentGuestSession(userName);    
    return sess;
  }
  
  public void closeReadOnlyHibernateSession() throws Exception{
    HibernateGuestSession.closeGuestSession();
  }
  

  public Session getHibernateSession(String userName) throws Exception{
    Session sess = null;
    
    if (this.isGuest()) {
      sess = HibernateGuestSession.currentGuestSession(userName);
    } else {
      sess = HibernateSession.currentSession(userName);
    }
    return sess;
  }
  
  public void closeHibernateSession() throws Exception{
    if (this.isGuest()) {
      HibernateGuestSession.closeGuestSession();
    } else {
      HibernateSession.closeSession();
    }
  }

  
  public String getVersion() {
    return version;
  }

  
  public void setVersion(String version) {
    this.version = version;
  }

  public Set getCoreFacilities() {
    Set coreFacilities = new TreeSet();
    // Uncomment to test as DNA Seq core facility admin 
    /*
      CoreFacility cf = new CoreFacility();
      cf.setIdCoreFacility(new Integer(1));
      cf.setFacilityName("DNA Sequencing");
      coreFacilities.add(cf);
    */
    
      return coreFacilities;
  }
  
 

  
  
}
