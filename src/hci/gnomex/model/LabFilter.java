package hci.gnomex.model;


import java.util.Iterator;
import java.util.Set;

import hci.gnomex.security.SecurityAdvisor;
import hci.framework.model.DetailObject;

public class LabFilter extends DetailObject {
  
  
  // Criteria
  private String                firstName;
  private String                lastName;
  private String                userLastName;
  private String                userFirstName;
  private Integer               idLab;
  private SecurityAdvisor       secAdvisor;
  private boolean              isUnbounded = false;

  
  
  private StringBuffer          queryBuf;
  private boolean              addWhere = true;
  
  
  
  public StringBuffer getQuery(SecurityAdvisor secAdvisor) {
    this.secAdvisor = secAdvisor;
    
    queryBuf = new StringBuffer();
    
    queryBuf.append(" SELECT lab");
    
    getQueryBody(queryBuf);
    
    return queryBuf;
    
  }
  
  public void getQueryBody(StringBuffer queryBuf) {
    
    queryBuf.append(" FROM        Lab as lab ");
  
    if (hasUserCriteria()) {
      queryBuf.append(" JOIN lab.appUsers as user ");
    }
    
    
    addLabCriteria();
    addUserCriteria();
    if (!isUnbounded) {
      addSecurityCriteria();      
    }
    
    queryBuf.append(" order by lab.lastName, lab.firstName");
  }
  
  
  private boolean hasUserCriteria() {
    if ((userLastName != null && !userLastName.equals("")) ||
        (userFirstName != null && !userFirstName.equals(""))) {
      return true;
    } else {
      return false;
    }
  }

  private void addLabCriteria() {
    // Search by lab last name 
    if (lastName != null && !lastName.equals("")){
      this.addWhereOrAnd();
      queryBuf.append(" lab.lastName like '%");
      queryBuf.append(lastName);
      queryBuf.append("%'");
    } 

    // Search by lab first name 
    if (firstName != null && !firstName.equals("")){
      this.addWhereOrAnd();
      queryBuf.append(" lab.firstName like '%");
      queryBuf.append(firstName);
      queryBuf.append("%'");
    } 
    //  Search by idLab
    if (idLab != null){
      this.addWhereOrAnd();
      queryBuf.append(" lab.idLab =");
      queryBuf.append(idLab);
    } 
  }
  
  private void addUserCriteria() {
    if (userLastName != null && !userLastName.equals("")) {
      this.addWhereOrAnd();
      queryBuf.append(" user.lastName like '%");
      queryBuf.append(userLastName);
      queryBuf.append("%'");
    }
    if (userFirstName != null && !userFirstName.equals("")) {
      this.addWhereOrAnd();
      queryBuf.append(" user.firstName like '%");
      queryBuf.append(userFirstName);
      queryBuf.append("%'");
    }
  }
  
  
  private void addSecurityCriteria() {
    if (secAdvisor.hasPermission(secAdvisor.CAN_ACCESS_ANY_OBJECT)) {
      // No criteria needed if user can view all requests
      
    } else if (secAdvisor.hasPermission(secAdvisor.CAN_PARTICIPATE_IN_GROUPS)) {
      
      if (this.secAdvisor.getAllMyGroups().size() > 0) {
        // Limit to requests for labs that the user is a member of
        this.addWhereOrAnd();
        queryBuf.append(" lab.idLab in ( ");
        for(Iterator i = secAdvisor.getAllMyGroups().iterator(); i.hasNext();) {
          Lab theLab = (Lab)i.next();
          queryBuf.append(theLab.getIdLab());
          if (i.hasNext()) {
            queryBuf.append(", ");
          }
        }      
        queryBuf.append(" )");        
      }
      
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

  

  public Integer getIdLab() {
    return idLab;
  }

  
  public void setIdLab(Integer idLab) {
    this.idLab = idLab;
  }

  
  public boolean isUnbounded() {
    return isUnbounded;
  }

  
  public void setUnbounded(boolean isUnbounded) {
    this.isUnbounded = isUnbounded;
  }

  
  public String getFirstName() {
    return firstName;
  }

  
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  
  public String getLastName() {
    return lastName;
  }

  
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  
 

  
}
