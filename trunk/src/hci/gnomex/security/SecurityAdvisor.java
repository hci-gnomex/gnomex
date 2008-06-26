package hci.gnomex.security;

import hci.dictionary.model.DictionaryEntry;
import hci.framework.model.DetailObject;
import hci.framework.security.UnknownPermissionException;
import hci.gnomex.model.Analysis;
import hci.gnomex.model.AnalysisGroup;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.Lab;
import hci.gnomex.model.Project;
import hci.gnomex.model.Request;
import hci.gnomex.model.UserPermissionKind;
import hci.gnomex.model.Visibility;
import hci.gnomex.utility.HibernateGuestSession;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.LabComparator;

import java.io.Serializable;
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
  public static final String          CAN_MANAGE_WORKFLOW                         = "canManageWorkflow";
  public static final String          CAN_ADMINISTER_USERS                        = "canAdministerUsers";
  public static final String          CAN_ACCESS_ANY_OBJECT                       = "canAccessAnyObject";
  public static final String          CAN_WRITE_ANY_OBJECT                        = "canWriteAnyObject";
  public static final String          CAN_DELETE_ANY_PROJECT                      = "canDeleteAnyProject";            
  public static final String          CAN_DELETE_REQUESTS                         = "canDeleteRequests";            
  
  public static final String          CAN_PARTICIPATE_IN_GROUPS                   = "canParticipateInGroups";            
  public static final String          CAN_SUBMIT_REQUESTS                         = "canSubmitRequests";            
  
  public static final String          CAN_BE_LAB_MEMBER                           = "canBeLabMember";  
  public static final String          CAN_BE_LAB_COLLABORATOR                     = "canBeLabCollaborator";            
  


  // Session info
  private AppUser                      appUser;
  private boolean                     isGuest = false;
  private boolean                     isUniversityUser = true;
  
  // Global permission map
  private Map                          globalPermissionMap = new HashMap();
  
  
  private SecurityAdvisor(AppUser appUser, boolean isUniversityUser) throws InvalidSecurityAdvisorException {
    
    this.appUser = appUser;
    this.isUniversityUser = isUniversityUser;
    
    validate();
    setGlobalPermissions();    
  }
  
  private SecurityAdvisor() throws InvalidSecurityAdvisorException {
    isGuest = true;
    isUniversityUser = false;
    setGlobalPermissions();
  }
  
  public boolean isGuest() {
    return isGuest;
  }
  
  public String getIsGuest() {
    if (isGuest) {
      return "Y";
    } else {
      return "N";
    }
  }
  

  public static SecurityAdvisor create(Session   sess, 
                                         String    uid) throws InvalidSecurityAdvisorException {
    SecurityAdvisor securityAdvisor = null;
    
    boolean isUniversityUser = true;
    
    // Get AppUser
    StringBuffer queryBuf = new StringBuffer();
    queryBuf.append(" SELECT user from AppUser as user ");
    queryBuf.append(" WHERE  user.uNID =  '" + uid + "' ");
    
    List users = sess.createQuery(queryBuf.toString()).list();
    
    AppUser appUser = null;
    if (users.size() > 0) {
      appUser = (AppUser)users.get(0);
      isUniversityUser = true;
    }
    
    // If this is not a university user, 
    if (appUser == null) {
      queryBuf = new StringBuffer();
      queryBuf.append(" SELECT user from AppUser as user ");
      queryBuf.append(" WHERE  user.userNameExternal =  '" + uid + "' ");
      
      users = sess.createQuery(queryBuf.toString()).list();
      
      appUser = null;
      if (users.size() > 0) {
        appUser = (AppUser)users.get(0);
        isUniversityUser = false;
      }
      
    }
    
    if (appUser == null) {
      throw new InvalidSecurityAdvisorException("Cannot find AppUser " + uid);
    }
    
    
    // Instantiate SecurityAdvisor
    Hibernate.initialize(appUser.getLabs());
    Hibernate.initialize(appUser.getCollaboratingLabs());
    Hibernate.initialize(appUser.getManagingLabs());
    securityAdvisor = new SecurityAdvisor(appUser, isUniversityUser);
    
    // Make sure we have a valid state.
    securityAdvisor.validate();
    
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
        
        // Request has membership visibility
        if (req.getCodeVisibility().equals(Visibility.VISIBLE_TO_GROUP_MEMBERS)) {
          if (isGroupIAmMemberOf(req.getIdLab()) || isGroupIManage(req.getIdLab())) {
            canRead = true;
          }            
        }
        // Request has membership + collaborator visiblity
        else if (req.getCodeVisibility().equals(Visibility.VISIBLE_TO_GROUP_MEMBERS_AND_COLLABORATORS)) {
          if (isGroupIAmMemberOf(req.getIdLab()) || 
              isGroupIManage(req.getIdLab()) || 
              isGroupICollaborateWith(req.getIdLab())) {
            canRead = true;
          } 
        }
        // Request has public visibility
        else if (req.getCodeVisibility().equals(Visibility.VISIBLE_TO_PUBLIC)) {
          canRead = true;
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
        
        // Analysis has membership visibility
        if (a.getCodeVisibility().equals(Visibility.VISIBLE_TO_GROUP_MEMBERS)) {
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
        // Analysis has public visibility
        else if (a.getCodeVisibility().equals(Visibility.VISIBLE_TO_PUBLIC)) {
          canRead = true;
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
        if (isGroupIAmMemberOf(proj.getIdLab()) || isGroupIManage(proj.getIdLab()) || isGroupICollaborateWith(proj.getIdLab())) {
          canRead = true;
        } else if (proj.getCodeVisibility().equals(Visibility.VISIBLE_TO_PUBLIC)) {
          canRead = true;
        }
      }  
      // Guests
      else {
        Project proj = (Project)object;
        if (proj.getCodeVisibility().equals(Visibility.VISIBLE_TO_PUBLIC)) {
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
        } 
      }  
      // Guests
      else {
        AnalysisGroup ag = (AnalysisGroup)object;
        if (ag.getCodeVisibility().equals(Visibility.VISIBLE_TO_PUBLIC)) {
          canRead = true;
        }
      }     
    } 
    //
    // Dictionary
    //    
    else if (object instanceof DictionaryEntry) {
      if (hasPermission(this.CAN_WRITE_DICTIONARIES)) {
        canRead = true;
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
      // Univerity GNomEx users
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
        // Lab members or mananagers
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
        // Lab members or mananagers
        if (isGroupIAmMemberOf(ag.getIdLab()) || isGroupIManage(ag.getIdLab())) {
          canUpdate = true;
        } 
      }  
    }
    //
    // Dictionary
    //
    else if (object instanceof DictionaryEntry) {
      if (hasPermission(this.CAN_WRITE_DICTIONARIES)) {
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
          Project proj = (Project) object;
          // Lab manager
          if (isGroupIManage(proj.getIdLab())) {
            canUpdate = true;
          }
          // Owner of project
          else if (isGroupIAmMemberOf(proj.getIdLab())
              && isOwner(proj.getIdAppUser())) {
            canUpdate = true;
          }
        } 
        
      }
      //
      // Analysis Group
      //
      else if (object instanceof AnalysisGroup) {
          
        // Admins
        if (hasPermission(this.CAN_WRITE_ANY_OBJECT)) {
          canUpdate = true;
        }
        // University GNomEx users
        else if (hasPermission(this.CAN_PARTICIPATE_IN_GROUPS)) {
          AnalysisGroup ag = (AnalysisGroup) object;
          // Lab manager
          if (isGroupIManage(ag.getIdLab())) {
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

  public boolean canDelete(DetailObject object) throws UnknownPermissionException {
    boolean canDelete = false;
    
    // 
    // Request
    //
    if (object instanceof Request) { 

      // Admin
      if (hasPermission(this.CAN_DELETE_REQUESTS)) {
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
    // Dictionary
    //
    else if (object instanceof DictionaryEntry) {
      if (hasPermission(this.CAN_WRITE_DICTIONARIES)) {
        canDelete = true;
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
    } else if (object instanceof Analysis) {
      Analysis a = (Analysis)object;
      a.canUpdateVisibility(this.canUpdate(object, this.PROFILE_OBJECT_VISIBILITY));
    } else if (object instanceof Project) {
      Project proj = (Project)object;
      proj.canUpdateVisibility(this.canUpdate(object, this.PROFILE_OBJECT_VISIBILITY));
    } else if (object instanceof AnalysisGroup) {
      AnalysisGroup ag = (AnalysisGroup)object;
      ag.canUpdateVisibility(this.canUpdate(object, this.PROFILE_OBJECT_VISIBILITY));
    }
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
    
    // Can write common dictionaries
    if (appUser.getCodeUserPermissionKind().equals(UserPermissionKind.ADMIN_PERMISSION_KIND)) {
      globalPermissionMap.put(new Permission(CAN_WRITE_DICTIONARIES), null);
    }
    
    // Can manager workflow
    if (appUser.getCodeUserPermissionKind().equals(UserPermissionKind.ADMIN_PERMISSION_KIND)) {
      globalPermissionMap.put(new Permission(CAN_MANAGE_WORKFLOW), null);
    }
    
    // Can administer users
    if (appUser.getCodeUserPermissionKind().equals(UserPermissionKind.ADMIN_PERMISSION_KIND)) {
      globalPermissionMap.put(new Permission(CAN_ADMINISTER_USERS), null);
    }

    // Can access any requests
    if (appUser.getCodeUserPermissionKind().equals(UserPermissionKind.ADMIN_PERMISSION_KIND)) {
      globalPermissionMap.put(new Permission(CAN_ACCESS_ANY_OBJECT), null);
    }

    // Can write any request
    if (appUser.getCodeUserPermissionKind().equals(UserPermissionKind.ADMIN_PERMISSION_KIND)) {
      globalPermissionMap.put(new Permission(CAN_WRITE_ANY_OBJECT), null);
    }

    // Can delete requests
    if (appUser.getCodeUserPermissionKind().equals(UserPermissionKind.ADMIN_PERMISSION_KIND)) {
      globalPermissionMap.put(new Permission(CAN_DELETE_REQUESTS), null);
    }

    
    //  Can delete any project
    if (appUser.getCodeUserPermissionKind().equals(UserPermissionKind.ADMIN_PERMISSION_KIND)) {        
      globalPermissionMap.put(new Permission(CAN_DELETE_ANY_PROJECT), null);
    }

    // Can access objects governed by group level permissions
    globalPermissionMap.put(new Permission(CAN_PARTICIPATE_IN_GROUPS), null);
    
    // Can submit requests
    globalPermissionMap.put(new Permission(CAN_SUBMIT_REQUESTS), null);      
    
    
    // Can be lab member
    globalPermissionMap.put(new Permission(CAN_BE_LAB_MEMBER), null);      

    
    // Can be lab collaborator
    if (!isGuest) {
      globalPermissionMap.put(new Permission(CAN_BE_LAB_COLLABORATOR), null);      
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
  
  public Integer getIdAppUser() {
    if (isGuest) {
      return new Integer(-999999);
    } else {
      return appUser.getIdAppUser();
    }
  }
  
  
  public Set getAllMyGroups() {
    TreeSet labs = new TreeSet(new LabComparator());
    
    labs.addAll(getGroupsIAmMemberOf());
    labs.addAll(getGroupsICollaborateWith());
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
      
      lab.excludeMethodFromXML("getIsMyLab");
      lab.excludeMethodFromXML("getCanSubmitRequests");
      lab.excludeMethodFromXML("getCanManage");
      lab.excludeMethodFromXML("getHasPublicData");
      labs.add(lab);
    }
    
    return labs;
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
  
  public boolean addSecurityCriteria(StringBuffer queryBuf, String classShortName, boolean addWhereOrAnd, boolean scopeToGroup ) {
    return addSecurityCriteria(queryBuf, classShortName, addWhereOrAnd, scopeToGroup, null);
  }
  
  public boolean addSecurityCriteria(StringBuffer queryBuf, String classShortName, boolean addWhereOrAnd, boolean scopeToGroup, String leftJoinExclusionCriteria ) {
    // Admins
    if (hasPermission(CAN_ACCESS_ANY_OBJECT)) {
    }
    // GNomEx users
    else if (hasPermission(this.CAN_PARTICIPATE_IN_GROUPS)) {
      addWhereOrAnd = this.addWhereOrAnd(queryBuf, addWhereOrAnd);
      
      queryBuf.append(" ( ");
      
      // Add criteria with member visibility
      boolean addedCriteria = false;
      addedCriteria = addMembershipCriteria(queryBuf, classShortName);

      // Add critiera for objects with collaborator visibility
      if (addedCriteria) {
        queryBuf.append(" OR ");        
      }
      addedCriteria = addCollaborationCriteria(queryBuf, classShortName);
            
      // Add criteria for public objects
      if (addedCriteria) {
        queryBuf.append(" OR ");        
      }
      addPublicCriteria(queryBuf, classShortName, scopeToGroup);
      
      // Add exclusion criteria
      if (leftJoinExclusionCriteria != null) {
        queryBuf.append(" OR ");
        queryBuf.append(" ( ");
        queryBuf.append(leftJoinExclusionCriteria);
        queryBuf.append(" ) ");
        
      }
      
      queryBuf.append(" ) ");
    }
    // Guest
    else {
      addWhereOrAnd = this.addWhereOrAnd(queryBuf, addWhereOrAnd);
      addPublicCriteria(queryBuf, classShortName, false);        
    }
    
    return addWhereOrAnd;
  }
  
  
  public boolean addMembershipCriteria(StringBuffer queryBuf, String classShortName ) {
    Set labs = getGroupsIAmMemberOf();
    Set mgrLabs = getGroupsIManage();
    labs.addAll(mgrLabs);
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

  
  public  boolean addCollaborationCriteria(StringBuffer queryBuf, String classShortName ) {
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
  
  public void addPublicCriteria(StringBuffer queryBuf, String classShortName, boolean scopeToGroups) {
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
      }
    }

    // req.codeVisibility is 'visible to collaborators and members'
    queryBuf.append(classShortName);
    queryBuf.append(".codeVisibility = '");
    queryBuf.append(Visibility.VISIBLE_TO_PUBLIC);
    queryBuf.append("'");
    
    queryBuf.append(" ) ");
  }
  

  
  public boolean buildLuceneSecurityFilter(StringBuffer searchText, String labField, String visibilityField, boolean scopeToGroup, String leftJoinExclusionCriteria) {
    boolean addedFilter = false;
    
    // Admins
    if (hasPermission(CAN_ACCESS_ANY_OBJECT)) {
    }
    // GNomEx users
    else if (hasPermission(this.CAN_PARTICIPATE_IN_GROUPS)) {
      searchText.append(" ( ");
      
      boolean added1 = buildLuceneMembershipFilter(searchText, labField, visibilityField);

      // Add critiera for objects with collaborator visiblity
      if (added1) {
        searchText.append(" OR ");        
      }
      boolean added2 = addedFilter = buildLuceneCollaborationFilter(searchText, labField, visibilityField);
            
      // Add criteria for public objects
      if (added1 || added2) {
        searchText.append(" OR ");        
      }
      boolean added3 = buildLucenePublicFilter(searchText, labField, visibilityField, scopeToGroup, null);
      addedFilter = added1 || added2 || added3;

      searchText.append(" ) ");
      
      // Add exclusion criteria
      if (addedFilter && leftJoinExclusionCriteria != null) {
        searchText.append(" OR ");
        searchText.append(" ( ");
        searchText.append(leftJoinExclusionCriteria);
        searchText.append(" ) ");
      }
    }
    // Guest
    else {
      addedFilter = buildLucenePublicFilter(searchText, labField, visibilityField, false, leftJoinExclusionCriteria);        
    }
    
    return addedFilter;
  }


  public boolean buildLuceneMembershipFilter(StringBuffer searchText, String labField, String visibilityField ) {
    Set labs = getGroupsIAmMemberOf();
    Set mgrLabs = getGroupsIManage();
    labs.addAll(mgrLabs);
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
  
  public boolean buildLuceneCollaborationFilter(StringBuffer searchText, String labField, String visibilityField ) {
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

  public boolean buildLucenePublicFilter(StringBuffer searchText, String labField, String visibilityField, boolean scopeToGroups, String leftJoinExclusionCriteria) {
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
    
    // Add exclusion criteria
    if (leftJoinExclusionCriteria != null) {
      searchText.append(" OR ");
      searchText.append(" ( ");
      searchText.append(leftJoinExclusionCriteria);
      searchText.append(" ) ");
    }
    
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

  
  
}
