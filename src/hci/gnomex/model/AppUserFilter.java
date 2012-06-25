package hci.gnomex.model;


import java.util.Iterator;
import java.util.Set;

import hci.gnomex.security.SecurityAdvisor;
import hci.framework.model.DetailObject;

public class AppUserFilter extends DetailObject {
  
  
  // Criteria
  private String                lab;
  private String                userLastName;
  private String                userFirstName;
  private Integer               idLab;
  private Integer               idAppUser;

  
  private SecurityAdvisor       secAdvisor;
  
  
  private StringBuffer          queryBuf;
  private boolean              addWhere = true;
  
  
  public StringBuffer getQuery(SecurityAdvisor secAdvisor) {
    addWhere = true;
    this.secAdvisor = secAdvisor;
    queryBuf = new StringBuffer();
    
    queryBuf.append(" SELECT distinct user");
    
    getQueryBody(queryBuf);
    
    return queryBuf;
    
  }
  
  public void getQueryBody(StringBuffer queryBuf) {
    
    // We need to join to labs and the core facilities if the user is an admin (not a super admin).
    boolean joinToCoreFacility = false;
    if (!secAdvisor.hasPermission(SecurityAdvisor.CAN_ADMINISTER_ALL_CORE_FACILITIES) &&
        secAdvisor.hasPermission(SecurityAdvisor.CAN_ACCESS_ANY_OBJECT)) {
      joinToCoreFacility = true;
    }

    
    
    queryBuf.append(" FROM           AppUser as user ");
    if (hasLabCriteria() || joinToCoreFacility) {
      queryBuf.append(" LEFT JOIN           user.labs as lab ");      
      queryBuf.append(" LEFT JOIN           user.collaboratingLabs as collabLab ");      
      queryBuf.append(" LEFT JOIN           user.managingLabs as managerLab ");      
    }
    
    // If the user is an admin (not a super admin), we need to filter lab list by core facility
    if (joinToCoreFacility) {
      queryBuf.append(" LEFT JOIN lab.coreFacilities as coreFacilityMem ");
      queryBuf.append(" LEFT JOIN collabLab.coreFacilities as coreFacilityCollab ");
      queryBuf.append(" LEFT JOIN managerLab.coreFacilities as coreFacilityMgr ");
    }
    
    
    addUserCriteria();
    addLabCriteria();
    
    addSecurityCriteria();      
    
    queryBuf.append(" order by user.lastName, user.firstName ");
  }
  
  
  private boolean hasLabCriteria() {
    // If we will add security criteria by authorized lab, join to lab
    if (!secAdvisor.hasPermission(secAdvisor.CAN_ACCESS_ANY_OBJECT)) {
      return true;
    }
    
    // If we have any lab selection criteria, join to lab
    if ( (lab != null && !lab.equals("")) ||
         (idLab != null)) {
      return true;
    } else {
      return false;
    }
  }

  private void addUserCriteria() {
    // Search by user name 
    if (userLastName != null && !userLastName.equals("")){
      this.addWhereOrAnd();
      queryBuf.append(" user.lastName like '%");
      queryBuf.append(userLastName);
      queryBuf.append("%'");
    } 
    if (userFirstName != null && !userFirstName.equals("")){
      this.addWhereOrAnd();
      queryBuf.append(" user.FirstName like '%");
      queryBuf.append(userFirstName);
      queryBuf.append("%'");
    } 
    // Search by user id
    if (idAppUser != null){
      this.addWhereOrAnd();
      queryBuf.append(" user.idAppUser = ");
      queryBuf.append(idAppUser);
    }     
  }
  
  private void addLabCriteria() {
    // Search by lab name
    if (lab != null && !lab.equals("")) {
      this.addWhereOrAnd();
      queryBuf.append(" lab.name like '%");
      queryBuf.append(lab);
      queryBuf.append("%'");
    }
    // Search by lab id
    if (idLab != null){
      this.addWhereOrAnd();
      queryBuf.append(" lab.idLab = ");
      queryBuf.append(idLab);
    }     
  }
    
  private void addSecurityCriteria() {
    if (secAdvisor.hasPermission(secAdvisor.CAN_ADMINISTER_ALL_CORE_FACILITIES) ) {
      // No criteria needed for super users
    } 
    else if (secAdvisor.hasPermission(secAdvisor.CAN_ACCESS_ANY_OBJECT) ) {
      // Admins can see users belong to labs that are associated with the core facilties
      // this admin manages.
      
      if (secAdvisor.getCoreFacilitiesIManage().isEmpty()) {
        throw new RuntimeException("Admin is not assigned to any core facilities.  Cannot apply appropriate filter to user query.");
      }
      this.addWhereOrAnd();
      queryBuf.append("(");
      
      queryBuf.append(" coreFacilityMem.idCoreFacility in ( ");
      appendCoreFacilityInClause();
      queryBuf.append(" OR ");

      
      queryBuf.append(" coreFacilityCollab.idCoreFacility in ( ");
      appendCoreFacilityInClause();
      queryBuf.append(" OR ");
 
      queryBuf.append(" coreFacilityMgr.idCoreFacility in ( ");
      appendCoreFacilityInClause();
      queryBuf.append(" OR ");
           
      // Also include any app user that is not yet assigned to a lab
      queryBuf.append(" (lab.idLab is NULL AND collabLab.idLab is NULL AND managerLab.idLab is NULL) ");
      
      queryBuf.append(")");

    } else if (secAdvisor.getGroupsIManage().size() > 0) {
      
      // Lab managers must be able to add any user to his/her lab,
      // so only criteria applied is to get active gnomex accounts
      this.addWhereOrAnd();
      queryBuf.append(" user.isActive = 'Y' ");
      
      
    } else if (secAdvisor.hasPermission(secAdvisor.CAN_PARTICIPATE_IN_GROUPS)) {
      this.addWhereOrAnd();
      queryBuf.append(" user.isActive = 'Y' ");
      
      if (this.secAdvisor.getAllMyGroups().size() > 0) {
        // Limit to users who are members of the authorized labs
        this.addWhereOrAnd();
        
        queryBuf.append(" ( ");
        
        
        queryBuf.append(" lab.idLab in ( ");
        for(Iterator i =  this.secAdvisor.getAllMyGroups().iterator(); i.hasNext();) {
          Lab theLab = (Lab)i.next();
          queryBuf.append(theLab.getIdLab());
          if (i.hasNext()) {
            queryBuf.append(", ");
          }
        }      
        queryBuf.append(" )");  
        
        queryBuf.append(" OR ");
        queryBuf.append(" collabLab.idLab in ( ");
        for(Iterator i = this.secAdvisor.getAllMyGroups().iterator(); i.hasNext();) {
          Lab theLab = (Lab)i.next();
          queryBuf.append(theLab.getIdLab());
          if (i.hasNext()) {
            queryBuf.append(", ");
          }
        }      
        queryBuf.append(" )");

        queryBuf.append(" OR ");
        queryBuf.append(" managerLab.idLab in ( ");            
        for(Iterator i =  this.secAdvisor.getAllMyGroups().iterator(); i.hasNext();) {
          Lab theLab = (Lab)i.next();
          queryBuf.append(theLab.getIdLab());
          if (i.hasNext()) {
            queryBuf.append(", ");
          }
        }      
        queryBuf.append(" )");          

        queryBuf.append(")");

      } else {
        // We have a user that is not set up in any labs.  Just 
        // Limit to this user only.
        
        this.addWhereOrAnd();
        queryBuf.append(" user.idAppUser = ");
        queryBuf.append(secAdvisor.getIdAppUser());
      }
      
    } else {
      // Limit to this user only
      this.addWhereOrAnd();
      queryBuf.append(" user.idAppUser = ");
      queryBuf.append(secAdvisor.getIdAppUser());
    }
    
  }
  
  private void appendCoreFacilityInClause() {
    for(Iterator i = secAdvisor.getCoreFacilitiesIManage().iterator(); i.hasNext();) {
      CoreFacility cf = (CoreFacility)i.next();
      queryBuf.append(cf.getIdCoreFacility());
      if (i.hasNext()) {
        queryBuf.append(", ");
      }
    }      
    queryBuf.append(" )");
    
  }
  
  protected boolean addWhereOrAnd() {
    if (addWhere) {
      queryBuf.append(" WHERE ");
      addWhere = false;
    } else {
      queryBuf.append(" AND ");
    }
    return addWhere;
  }

  
  public String getLab() {
    return lab;
  }

  
  public void setLab(String lab) {
    this.lab = lab;
  }


  
  public Integer getIdLab() {
    return idLab;
  }

  
  public void setIdLab(Integer idLab) {
    this.idLab = idLab;
  }

  
  public boolean isAddWhere() {
    return addWhere;
  }

  
  public void setAddWhere(boolean addWhere) {
    this.addWhere = addWhere;
  }

  
  public Integer getIdAppUser() {
    return idAppUser;
  }

  
  public void setIdAppUser(Integer idAppUser) {
    this.idAppUser = idAppUser;
  }

  
  public String getUserFirstName() {
    return userFirstName;
  }

  
  public void setUserFirstName(String userFirstName) {
    this.userFirstName = userFirstName;
  }

  
  public String getUserLastName() {
    return userLastName;
  }

  
  public void setUserLastName(String userLastName) {
    this.userLastName = userLastName;
  }


  
 
  
}
