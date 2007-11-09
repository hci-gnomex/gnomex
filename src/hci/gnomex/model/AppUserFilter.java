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
  private boolean              isUnbounded = false;
  
  private SecurityAdvisor       secAdvisor;
  
  
  private StringBuffer          queryBuf;
  private boolean              addWhere = true;
  
  
  public StringBuffer getQuery(SecurityAdvisor secAdvisor) {
    addWhere = true;
    this.secAdvisor = secAdvisor;
    queryBuf = new StringBuffer();
    
    queryBuf.append(" SELECT user");
    
    getQueryBody(queryBuf);
    
    return queryBuf;
    
  }
  
  public void getQueryBody(StringBuffer queryBuf) {
    
    queryBuf.append(" FROM           AppUser as user ");
    if (hasLabCriteria()) {
      queryBuf.append(" JOIN           user.labs as lab ");      
    }
    
    
    
    addUserCriteria();
    addLabCriteria();
    
    if (!isUnbounded) {
      addSecurityCriteria();      
    }
    
  }
  
  
  private boolean hasLabCriteria() {
    // If we will add security criteria by authorized lab, join to lab
    if (!secAdvisor.hasPermission(secAdvisor.CAN_ACCESS_ANY_OBJECT) &&
        secAdvisor.getGroupsIManage().isEmpty()) {
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
    if (secAdvisor.hasPermission(secAdvisor.CAN_ACCESS_ANY_OBJECT) ) {
      // No criteria needed if user can view all requests
      
    } else if (!secAdvisor.getGroupsIManage().isEmpty()){
      // No criterea for group mananagers - they need to see full
      // user list in order to assign new members to groups
      
    } else if (secAdvisor.hasPermission(secAdvisor.CAN_PARTICIPATE_IN_GROUPS)) {
      
      if (this.secAdvisor.getAllMyGroups().size() > 0) {
        // Limit to users who are members of the authorized labs
        this.addWhereOrAnd();
        queryBuf.append(" lab.idLab in ( ");
        Set labs = this.secAdvisor.getAllMyGroups();
        for(Iterator i = labs.iterator(); i.hasNext();) {
          Lab theLab = (Lab)i.next();
          queryBuf.append(theLab.getIdLab());
          if (i.hasNext()) {
            queryBuf.append(", ");
          }
        }      
        queryBuf.append(" )");
        
      }
      
    } else {
      // Limit to this user only
      this.addWhereOrAnd();
      queryBuf.append(" user.idAppUser = ");
      queryBuf.append(secAdvisor.getIdAppUser());
    }
    
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

  
  public boolean isUnbounded() {
    return isUnbounded;
  }

  
  public void setUnbounded(boolean isUnbounded) {
    this.isUnbounded = isUnbounded;
  }

  
 
  
}
